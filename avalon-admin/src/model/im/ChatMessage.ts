/**
 * @author lwlianghehe@gmail.com
 * @date 2025/01/28
 */

export default interface ChatMessage {
    fromUserId: number
    toUserId: number
    teamId: number,
    msgType: "Auth" | "Ack" | "Heartbeat" | "Error" | "Text" | "Image" | "File" | "Video" | "Audio" | "Location" | "Link" | "Event"
    eventType: "OfflineMessage" | "Offline" | "Online",
    content: string
    id: string,
    timestamp: number,
    chatType: "Single" | "Custom" | "Group" | "Room"
    stateEnum: "ClientToServerIng" | "Server" | "ServerToClientIng" | "Client"
    isRead: boolean
}
