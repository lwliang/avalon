#聊天系统API接口



```
HTTP服务器接口前缀
http-host:http://localhost:8093/im
im接口地址
websocket:ws://localhost:6666/ws
```





## 注册用户：

请求：

```
url: /user/register
method:post
json: {
	company: "公司名字，可以随便填",
	app: "app名称，可以随便填",
	thirdUserId: "app下的用户唯一标识"
}
```

返回值：

状态status为200

```
{
	userId: 5
}
```

状态status为500

```
{
msg:"错误内容",
code:500 //错误码
}
```

## 分页获取用户消息

请求,返回值小于pageSize说明是最后一页了

```
url: /message/user/get/page
method:post
json: {
    "userId": 3,
    "pageNum": 1,
    "pageSize": 10
}
```

返回值：

```
[
    {
        "msgType": "Text",//消息类型，Text文本，Image图片，
        "fromUserId": 2,// 发送用户id
        "isRead": false,
        "eventType": null,
        "toUserId": 3,//目的用户id
        "content": "消息内容",//消息内容
        "stateEnum": "Client",
        "teamId": null,
        "name": null,
        "serverSendTime": null,
        "id": 1267898438633263104, // id 
        "chatType": "Single",
        "timestamp": 1722332144206 //
    }
]
```



## 获取离线消息

请求

```
url: /message/offline
method:post
json: {
    "userId": 3
}
```

返回值：

```
[
    {
        "msgType": "Text",
        "fromUserId": 2,
        "isRead": false,
        "eventType": null,
        "toUserId": 3,
        "content": "消息内容",
        "stateEnum": "Server",
        "teamId": null,
        "name": null,
        "serverSendTime": null,
        "id": 1267905873892741120,
        "chatType": "Single",
        "timestamp": 1722333916896
    }
]
```





## 获取消息id

请求

```
url: /message/get/id
method:post
json: {
}
```

返回值

```
{
"id":1268242042090295296
}
```

## 清空消息列表的未读消息

请求

```
url: /user/chat/clear/unread
method:post
json: {
 "id":2
}
```

返回值 无

## 删除聊天列表

请求

```
url: /user/chat/clear/unread
method:post
json: {
 "id":2
}
```

返回值 无



## 获取聊天列表

请求

```
url: /user/chat/list
method:post
json: {
    "userId":2
}
```

返回值

```
[
    {
        "top": false, // 置顶
        "lastMsgId": { // 最后一条消息
            "msgType": "Image",
            "id": 1268572304611348480,
            "content": "/file/down/123123.png",
            "timestamp": 1722492806373
        },
        "createTime": "2024-08-01 13:29:50",
        "fromUserId": 2, // 发送方
        "teamId": null,
        "updateTime": "2024-08-01 14:13:26",
        "id": 2,
        "unReadCount": 2, // 未读消息数
        "toUserId": 3,
        "chatType": "Single"
    }
]
```

## 



## 发送消息的流程

建立连接->鉴权->发送消息->服务器回发ack确认消息

### 建立连接

```
ws://localhost:6666/ws
```

### 鉴权消息:

```
{
"msgType":"Auth",
"fromUserId":3,
"content": "{'token'}"
}
```

### 发送消息

```
{
"id":1268242042090295296,
"fromUserId":3,
"toUserId":2
"chatType":"Single",
"msgType":"Text", // Text 文本， Image 图片
"content": "Hello,World" 
}
```



### 服务器返回ack消息

```
{
"id":1268242042090295296
"chatType":"Single",
"msgType":"Ack",
"content": "{'srcId': 1268242042090295295}" // srcId 是确认收到消息的id
}
```



## 接受消息的流程

建立连接->鉴权->接受消息->向服务器发送ack确认消息

### 建立连接

```
ws://localhost:6666/ws
```

### 鉴权消息:

```
{
"msgType":"Auth",
"fromUserId":3,
"content": "{'token'}"
}
```

### 接受消息

```
{
"id":1268242042090295296,
"fromUserId":3,
"toUserId":2
"chatType":"Single",
"msgType":"Text", // Text 文本， Image 图片
"content": "Hello,World" 
}
```



### 服务器返回ack消息

```
{
"id":1268242042090295297
"chatType":"Single",
"msgType":"Ack",
"content": "{'srcId': 1268242042090295296}" // srcId 是确认收到消息的id
}
```

