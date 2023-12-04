import axios from "axios"
import {ElMessage} from "element-plus";

const authItemName = "access_token"

const defaultFailure = (message, code, url) => {
    console.warn(`请求地址: ${url}, 状态码: ${code}, 错误信息: ${message}`)
    ElMessage.warning(message)
}

const defaultError = (err) => {
    console.log(err)
    ElMessage.warning('发生了一些错误，请联系管理员')
}

//获取请求头, 后续操作都需要请求头
function accessHeader() {
    const token = takeAccessToken()
    return token ? { 'Authorization': `Bearer ${takeAccessToken()}`} : {}
}

//读取token
function takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName)
    if(!str) return null;
    const authObj = JSON.parse(str)
    if(authObj.expire <= new Date()) {
        deleteAccessToken()
        ElMessage.warning('登录状态已过期，请重新登录')
        return null
    }
    return authObj.token
}

//前端保存Token
function storeAccessToken(token, remember, expire) {
    const authObj = {
        token: token,
        expire: expire
    }
    const str = JSON.stringify(authObj)
    if(remember)
        localStorage.setItem(authItemName, str)
    else
        sessionStorage.setItem(authItemName, str)
}

function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

//内部的Post Axios封装
function internalPost(url, data, header, success, failure, error = defaultError) {
    axios.post(url, data,{headers: header}).then(({data}) => {
        if(data.code === 200) {
            success(data.data)
        } else {
            failure(data.message, data.code, data.url)
        }
    }).catch(err => error(err))
}

//内部的Get Axios封装
function internalGet(url, header, success, failure, error = defaultError) {
    axios.get(url, {headers: header}).then(({data}) => {
        if (data.code === 200) {
            success(data.data)
        } else {
            failure(data.message, data.code, url)
        }
    }).catch(err => error(err))
}

//已近封装了请求头内加入了Token，可以直接用
function get(url, success, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure)
}

function post(url, data, success, failure = defaultFailure) {
    internalPost(url, data, accessHeader() ,success, failure);
}

function login(username, password, remember, success, failure = defaultFailure) {
    internalPost('/api/auth/login', {
        username: username,
        password: password
    }, {
        //axios默认为json传输，Security只支持form表单登录，这里改变类型为表单
        'Content-Type': 'application/x-www-form-urlencoded'
    }, (data) => {
        storeAccessToken(data.token, remember, data.expire)
        ElMessage.success(`登录成功，欢迎${username}`)
        success(data)
    }, failure)
}

function logout(success, failure = defaultFailure) {
    get("/api/auth/logout", () => {
        deleteAccessToken()
        ElMessage.success('已退出登录')
        success()
    }, failure)
}

//是否未验证
function unauthorized() {
    return !takeAccessToken()
}

export {login, logout, get, post, unauthorized}
