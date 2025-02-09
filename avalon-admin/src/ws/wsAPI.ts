import {postHttp} from "../api/http.ts";
import {getModelAllApi, getModelDetailApi} from "../api/modelApi.ts";
import {getUserId} from "../cache/userStorage.ts";

export function getMessageId() {
    return postHttp("/im/message/get/id", {})
}

export function getPageMessage(fromUserId: Number, toUserId: Number, pageNum: Number, pageSize: Number) {
    return postHttp("/im/message/user/get/page", {fromUserId, toUserId, pageNum, pageSize})
}

export function getImUserId() {
    return getModelDetailApi(getUserId(), "id,imUserId", "base.user");
}

let imUserId: any = null;

export async function getImUserIdCache() {
    if (imUserId) {
        return imUserId;
    }
    const user = await getImUserId();
    if (user.imUserId) {
        imUserId = user.imUserId;
    }
    return imUserId;
}

export async function getUserByImUserId(imId: Number) {
    const users = await getModelAllApi("id,name,avatar",
        `(=,imUserId,${imId})`,
        "base.user");

    if (users.length) {
        return users[0]
    }
    return {}
}

let imUser: any = {}

export async function getUserByImUserIdCache(imId: Number) {
    const key = "im_" + imId;
    if (imUser[key]) {
        return imUser[key];
    }

    const user = await getUserByImUserId(imId);
    imUser[key] = user;

    return user;
}