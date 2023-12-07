<script setup>

import {computed, onMounted, onUnmounted, reactive, ref} from "vue";
import {Lock, Message, Promotion} from "@element-plus/icons-vue";
import {get, logout, post} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {getStorageInfoJson} from "@/utils/profileUtils";

const active = ref(0)
const coldTime = ref(0)
const formRef = ref()
const userEmail = getStorageInfoJson().email
const form = reactive({
  email: '',
  code: '',
  password: '',
  password_repeat: ''
})

function userLogout() {
  logout(() => router.push('/'))
}

const validateUsername = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不可包含特殊字符'))
  } else {
    callback()
  }
}

const validatePassword = (rule, value, callback) => {
  if (value === '')
    callback(new Error("请再次输入密码"))
  else if (value !== form.password)
    callback(new Error("两次输入密码不一致"))
  else
    callback()
}

const  validateEmail = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请输入邮箱'))
  } else if (value !== userEmail) {
    callback(new Error('请输入此用户正确的邮箱！'))
  } else {
    callback()
  }
}

const rule = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 1, max: 20, message: '用户名的长度必须在1-20个字符之间', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度必须在6-20个字符之间', trigger: ['blur', 'change'] }
  ],
  password_repeat: [
    { validator: validatePassword, trigger: ['blur', 'change'] }
  ],
  email: [
    { validator: validateEmail, trigger: 'blur' },
  ],
  code: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { min: 6, max: 6, message: '验证码格式不正确' }
  ]
}

function askCode() {
  if(isEmailValid) {
    coldTime.value = 60
    get(`/api/auth/ask-code?email=${form.email}&type=register`, () => {
      ElMessage.success(`验证码已发送到: ${form.email}`)
      setInterval(() => coldTime.value--, 1000)
    }, (message) => {
      ElMessage.warning(message)
      coldTime.value = 0
    })
  } else {
    ElMessage.warning("请输入正确的电子邮件")
  }

}

const isEmailValid = computed(() => /^[\w.-]+@[\w.-]+\.\w+$/.test(form.email))

function confirmReset() {
  formRef.value.validate((valid) => {
    if(valid) {
      post('/api/auth/reset-confirm', {
        email: form.email,
        code: form.code
      }, () => active.value++)
    }
  })
}

function doReset() {
  formRef.value.validate((valid) => {
    if (valid) {
      post('/api/auth/reset-password', {...form}, () => {
        ElMessage.success('密码重置成功，请重新登录')
        userLogout()
        router.push('/')
      })
    }
  })
}

onMounted(() => {
  window.addEventListener("keydown", enterDown)
})

onUnmounted(() => {
  window.removeEventListener("keydown", enterDown, false)
})
function enterDown(e) {
  if (e.keyCode === 13) {
    doReset()
  }
}
</script>

<template>
  <div style="text-align: center">
    <div style="margin-top: 10px">
      <el-steps :active="active" finish-status="success" align-center>
        <el-step title="验证电子邮件"/>
        <el-step title="重置你的密码"/>
      </el-steps>
    </div>

    <div v-if="active === 0" style="margin: 20px 25px">
      <div style="margin-top: 10px">
        <div style="font-size: 25px; font-weight: bold">重置密码</div>
        <div style="font-size: 14px; color: grey">请输入要重置密码的电子邮件地址</div>
      </div>
      <div style="margin-top: 30px; justify-content: center; display: flex">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="email">
            <el-input v-model="form.email" type="email" placeholder="请输入电子邮件地址" style="width: 350px">
              <template #prefix>
                <el-icon><Message /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="code">
            <el-row :gutter="10" style="width: 100%">
              <el-col :span="15">
                <el-input v-model="form.code" minlength="6" maxlength="6" type="text" placeholder="请输入验证码">
                  <template #prefix>
                    <el-icon><Promotion /></el-icon>
                  </template>
                </el-input>
              </el-col>

              <el-col :span="9">
                <el-button @click="askCode" :disabled="!isEmailValid || coldTime > 0" type="primary" style="width: 120px" plain>
                  {{ coldTime > 0 ? `获取验证码(${coldTime}秒)` : '获取验证码' }}
                </el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </div>
      <div>
        <el-button @click="confirmReset" type="success" style="width: 300px; margin-top:30px" plain>重置密码</el-button>
      </div>

    </div>

    <div v-if="active === 1" style="margin: 20px 25px">
      <div style="margin-top: 20px">
        <div style="font-size: 25px; font-weight: bold">重置密码</div>
        <div style="font-size: 14px; color: grey">请填写你的新密码，务必牢记你的密码</div>
      </div>
      <div style="margin-top: 30px;display: flex; justify-content: center">
        <el-form :model="form" :rules="rule" ref="formRef">
          <el-form-item prop="password">
            <el-input v-model="form.password" minlength="6" maxlength="20" type="password" placeholder="请输入密码" style="width: 350px;">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <el-form-item prop="password_repeat">
            <el-input v-model="form.password_repeat" maxlength="20" type="password" placeholder="请确认你的密码" style="width: 350px">
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
        </el-form>
      </div>

      <div style="display: flex; align-content: center">
        <el-button @click="doReset" @keydown.enter="doReset" type="danger" style="width: 300px; margin-top: 20px" plain>立即重置</el-button>
        <el-button @click="() => active--"  type="warning" style="width: 200px; margin-top: 20px" plain>返回上一步</el-button>
      </div>

    </div>
  </div>
</template>

<style scoped>

</style>