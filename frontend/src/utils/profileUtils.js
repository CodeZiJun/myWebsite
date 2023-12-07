import {accountInfoItemName} from "@/net";
import {baseUrl} from "@/main";
import {ref} from "vue";

export function getAavater() {
    let avatar = JSON.parse(localStorage.getItem(accountInfoItemName) ?
        localStorage.getItem(accountInfoItemName) :
        sessionStorage.getItem(accountInfoItemName)).avatar
    if (avatar == null)
        return ""
    else
        return baseUrl + "/" + avatar
}
export let avatarRef = ref(getAavater())

export function getStorageInfoJson() {
    return localStorage.getItem(accountInfoItemName) ? JSON.parse(localStorage.getItem(accountInfoItemName)) :
                                                JSON.parse(sessionStorage.getItem(accountInfoItemName))
}