import {getImUserIdCache, getMessageId} from "./wsAPI.ts";
import {getTick} from "../util/dateUtils.ts";
import {getToken} from "../cache/tokenStorage.ts";
import ChatMessage from "../model/im/ChatMessage.ts";
import MyNotification from "../components/notification";

export interface ReconnectingWebSocketOptions {
    maxReconnectAttempts?: number; // 最大重连次数
    reconnectInterval?: number; // 重连间隔时间（毫秒）
    heartbeatInterval?: number; // 心跳检测间隔时间（毫秒）
}

type WebSocketMessageHandler = (message: ChatMessage) => void;

export class ReconnectingWebSocket {
    private url: string;
    private protocols?: string | string[] | undefined;
    private options: Required<ReconnectingWebSocketOptions>;

    private ws: WebSocket | null = null;
    private reconnectAttempts: number = 0;
    private isManualClose: boolean = false;
    private heartbeatTimer: ReturnType<typeof setInterval> | null = null;
    private messageQueue: string[] = [];

    // 回调函数
    public messageEvents?: WebSocketMessageHandler[] = [];
    public addMessageEvent? = (call: WebSocketMessageHandler) => {
        if (this.messageEvents) {
            this.messageEvents.push(call)
        }
    }
    public removeMessageEvent? = (call: WebSocketMessageHandler) => {
        if (!this.messageEvents) {
            return
        }
        const i = this.messageEvents.indexOf(call)
        if (i > -1) {
            this.messageEvents.splice(i, 1)
        }
    }
    public onOpen?: () => void;
    public onClose?: (event: CloseEvent) => void;
    public onError?: (event: Event) => void;

    constructor(url: string, protocols?: string | string[], options: ReconnectingWebSocketOptions = {}) {
        this.url = url;
        this.protocols = protocols;

        // 合并默认配置
        this.options = {
            maxReconnectAttempts: 10,
            reconnectInterval: 3000,
            heartbeatInterval: 15000,
            ...options,
        };

        this.connect(); // 初始化连接
    }

    // 建立 WebSocket 连接
    private connect(): void {
        this.ws = new WebSocket(this.url, this.protocols);

        // 监听连接事件
        this.ws.onopen = () => {
            console.log('WebSocket connected');
            this.reconnectAttempts = 0; // 重置重连次数

            // 发送消息队列中的消息
            while (this.messageQueue.length > 0) {
                this.ws?.send(this.messageQueue.shift()!);
            }

            // 开始心跳检测
            this.startHeartbeat();
            getImUserIdCache().then(imId => {
                this.sendAuth(imId, getToken())
            })

            // 调用用户定义的 onOpen 回调函数（如果存在）
            if (this.onOpen) {
                this.onOpen();
            }
        };

        // 监听消息事件
        this.ws.onmessage = (event: MessageEvent) => {
            console.log('Message received:', event.data);
            const msg: ChatMessage = JSON.parse(event.data)
            if (this.messageEvents) {
                // 调用用户定义的 onMessage 回调函数
                for (const msgEvent of this.messageEvents) {
                    msgEvent(msg)
                }
            }
            if (msg.msgType == 'Text' || msg.msgType == 'Image') {
                let msgContent = msg.content;
                if (msg.msgType == 'Image') {
                    msgContent = '[图片]';
                }
                MyNotification.info('您有一条消息', msgContent);
            }
        };

        // 监听关闭事件
        this.ws.onclose = (event: CloseEvent) => {
            console.log('WebSocket disconnected:', event.reason);

            // 停止心跳
            this.stopHeartbeat();

            // 调用用户定义的 onClose 回调函数
            if (this.onClose) {
                this.onClose(event);
            }

            // 自动重连逻辑
            if (!this.isManualClose && this.reconnectAttempts < this.options.maxReconnectAttempts) {
                this.reconnectAttempts++;
                console.log(`Reconnecting... Attempt ${this.reconnectAttempts}`);
                setTimeout(() => this.connect(), this.options.reconnectInterval);
            }
        };

        // 监听错误事件
        this.ws.onerror = (event: Event) => {
            console.error('WebSocket error:', event);

            // 调用用户定义的 onError 回调函数
            if (this.onError) {
                this.onError(event);
            }

            // 关闭连接并尝试重连
            this.ws?.close();
        };
    }

    // 发送消息
    public send(message: string): void {
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
            this.ws.send(message);
        } else {
            // 如果连接未就绪，存储消息到队列
            this.messageQueue.push(message);
        }
    }

    // 手动关闭 WebSocket 连接
    public close(): void {
        this.isManualClose = true; // 设置手动关闭标志
        this.stopHeartbeat(); // 停止心跳
        this.ws?.close(); // 关闭连接
    }

    // 开始心跳检测
    private startHeartbeat(): void {
        if (this.options.heartbeatInterval > 0) {
            this.heartbeatTimer = setInterval(() => {
                if (this.ws && this.ws.readyState === WebSocket.OPEN) {
                    getImUserIdCache().then(imUserId => {
                        const heartBeat = {
                            "fromUserId": imUserId,
                            "msgType": "Heartbeat"
                        }
                        if (this.ws) {
                            this.ws.send(JSON.stringify(heartBeat)); // 发送心跳消息
                        }
                    })
                }
            }, this.options.heartbeatInterval);
        }
    }

    // 停止心跳检测
    private stopHeartbeat(): void {
        if (this.heartbeatTimer) {
            clearInterval(this.heartbeatTimer);
            this.heartbeatTimer = null;
        }
    }

    public async sendAuth(imId: number, token: string) {
        const msgId = await getMessageId();
        let msg = {
            "id": msgId.id,
            "msgType": "Auth",
            "fromUserId": imId,
            "content": `{"token":"${token}"}`
        }
        this.send(JSON.stringify(msg))
    }

    public async sendSingleText(fromImUserId: number, toImUserId: number, text: string): Promise<ChatMessage> {
        const msgId = await getMessageId();
        let msg = {
            id: msgId.id,
            "chatType": "Single",
            "content": text,
            "toUserId": toImUserId,
            "fromUserId": fromImUserId,
            msgType: "Text",
            timestamp: getTick()
        }
        this.send(JSON.stringify(msg))
        return msg as ChatMessage;
    }

    public async sendSingleImage(fromImUserId: number, toImUserId: number, imageUrl: string): Promise<ChatMessage> {
        const msgId = await getMessageId();
        let msg = {
            id: msgId.id,
            "chatType": "Single",
            "content": imageUrl,
            "toUserId": toImUserId,
            "fromUserId": fromImUserId,
            msgType: "Image",
            timestamp: getTick()
        }
        this.send(JSON.stringify(msg))
        return msg as ChatMessage;
    }

    public async sendTeamText(fromImUserId: number, teamId: number, text: string): Promise<ChatMessage> {
        const msgId = await getMessageId();
        let msg = {
            id: msgId.id,
            "chatType": "Group",
            "content": text,
            "teamId": teamId,
            "fromUserId": fromImUserId,
            msgType: "Text",
            timestamp: getTick()
        }
        this.send(JSON.stringify(msg))
        return msg as ChatMessage;
    }

    public async sendTeamImage(fromImUserId: number, teamId: number, imageUrl: string): Promise<ChatMessage> {
        const msgId = await getMessageId();
        let msg = {
            id: msgId.id,
            "chatType": "Group",
            "content": imageUrl,
            "teamId": teamId,
            "fromUserId": fromImUserId,
            msgType: "Image",
            timestamp: getTick()
        }
        this.send(JSON.stringify(msg))
        return msg as ChatMessage;
    }
}