<script setup>
import {
  Calendar, ChatDotSquare, CreditCard, EditPen,
  Grid, InfoFilled,
  Iphone, List,
  Memo,
  OfficeBuilding,
  Postcard,
  RefreshLeft,
  Tickets, Timer,
  User,
  UserFilled
} from "@element-plus/icons-vue";
import {reactive, onMounted, ref, onUnmounted} from "vue";
import {get, post} from "@/net";
import {getStorageInfoJson, myInfo} from "@/utils/profileUtils";
import {ElMessage} from "element-plus";
const noArchives = ref(true)
const archivesVO = ref({
})
let loading = ref(false)
const form = reactive({
  realName: '',
  telephone: '',
  gender: '',
  address: '',
  personalId: '',
  education: '',
  personalIntro: '',
  email: ''
})
const validateUsername = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请输入姓名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('姓名不可包含非法字符'))
  } else {
    callback()
  }
}
const validatePersonalId = (rule, value, callback) => {
  if(value === '') {
    callback(new Error('请输入身份证号'))
  } else if (!/^[1-9]\d{5}(19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[Xx\d]$/.test(value)) {
    callback(new Error('身份证号格式不正确'))
  } else {
    callback()
  }
}
const rule = {
  realName: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 1, max: 20, message: "姓名长度不合法", trigger: ['blur', 'change'] }
  ],
  telephone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { min: 5, max: 15, message: '请输入合法的电话号码', trigger: ['blur', 'change']}
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: ['blur', 'change'] },
  ],
  personalId: [
    { validator: validatePersonalId, trigger: ['blur', 'change']  },
  ],
  address: [
    { required: true, message: '请填写详细住址', trigger: 'blur' }
  ],
  education: [
    { required: true, message: '请填写教育经历', trigger: 'blur' }
  ],
  personalIntro: [
    { required: true, message: '请填写个人简介', trigger: 'blur' },
    { min: 20, message: '介绍至少为20字！', trigger: ['blur'] }
  ]
}
const formRef = ref()
const getArchives = (email) => {
  get(`/api/archives/selectDetailByEmail/${email}`,
      (data) => {
        noArchives.value = false
        archivesVO.value = data
        loading.value = false
      }, () => {
        noArchives.value = true;
        ElMessage.error("账户无档案")
        loading.value = false
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
    register()
  }
}
onMounted(
    () => {
      myInfo.value = getStorageInfoJson()
      getArchives(myInfo.value.email)
    }
)
const refresh = () => {
  loading.value = true
  myInfo.value = getStorageInfoJson()
  getArchives(myInfo.value.email)
}

const submitForm = () => {
  form.email = myInfo.value.email
  post('/api/archives/addArchives', {...form}, () => {
    ElMessage.success("提交成功")
    refresh()
  }, () => {
    ElMessage.error("提交失败")
  })
}
</script>

<template>
  <el-watermark :content="myInfo.username">
    <div  v-if="noArchives"
          style="display:flex;
          align-content: center;
          justify-content: center;
          text-align: center;
          height: 750px;"
          class="shadow-lg">
      <div style="margin-top: 30px">
        <h4>初始化档案</h4>
        <span style="color: grey">这代表着你的职业基本信息，请务必如实认真填写</span>
        <el-form :model="form" ref="formRef" style="width: 600px; margin-top: 20px" :rules="rule">
          <el-form-item prop="realName">
            <el-input v-model="form.realName" maxlength="10" type="text" placeholder="请输入真实姓名">
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="telephone">
            <el-input v-model="form.telephone" maxlength="15" type="text" placeholder="请输入联系电话">
              <template #prefix>
                <el-icon><iphone /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="gender">
            <el-radio-group v-model="form.gender">
              <el-radio label="男" />
              <el-radio label="女" />
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="personalId">
            <el-input v-model="form.personalId" maxlength="18" type="text" placeholder="请输入身份证号">
              <template #prefix>
                <el-icon><CreditCard /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="address">
            <el-input v-model="form.address" maxlength="50" type="text" placeholder="请输入详细住址">
              <template #prefix>
                <el-icon><office-building /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="education">
            <el-input v-model="form.education" maxlength="50" type="text" placeholder="请输入教育经历">
              <template #prefix>
                <el-icon><Memo /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="personalIntro">
            <el-input v-model="form.personalIntro"
                      maxlength="500"
                      type="textarea"
                      :rows="8"
                      placeholder="介绍下自己吧">
            </el-input>
          </el-form-item>
        </el-form>
        <div style="margin-top: 20px">
          <el-button @keydown.enter="enterDown" @click="submitForm" type="success" style="width: 400px" plain>提交档案</el-button>
        </div>
        <div style="margin-top: 10px">
          <el-button @click="formRef.resetFields()" type="warning" style="width: 400px" plain>重置填写</el-button>
        </div>
      </div>
    </div>
    <div v-else v-loading="loading">
      <div>
        <el-descriptions
            title="职工个人信息"
            :column="3"
            :size="'large'"
            border
        >
          <template #extra>
            <el-button type="success" plain>
              <template #icon>
                <el-icon><EditPen /></el-icon>
              </template>
              修改档案
            </el-button>
            <el-button type="primary" plain @click="refresh" >
              <template #icon>
                <el-icon><RefreshLeft /></el-icon>
              </template>
              更新
            </el-button>
          </template>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><Postcard /></el-icon>
                档案编号
              </div>
            </template>
            {{ archivesVO.id }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><user /></el-icon>
                真实姓名
              </div>
            </template>
            {{ archivesVO.realName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><iphone /></el-icon>
                联系电话
              </div>
            </template>
            {{ archivesVO.telephone }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><UserFilled /></el-icon>
                性别
              </div>
            </template>
            {{ archivesVO.gender }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><Grid /></el-icon>
                部门名称
              </div>
            </template>
            {{ archivesVO.departmentName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><tickets /></el-icon>
                身份
              </div>
            </template>
            <el-tag>{{ archivesVO.positionName }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><office-building /></el-icon>
                详细住址
              </div>
            </template>
            {{ archivesVO.address }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><CreditCard /></el-icon>
                身份证号
              </div>
            </template>
            {{ archivesVO.personalId }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><Calendar /></el-icon>
                档案变更日期
              </div>
            </template>
            {{ archivesVO.renewTime }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><Timer /></el-icon>
                入职时间
              </div>
            </template>
            {{ archivesVO.entryDate }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon><Memo /></el-icon>
                教育经历
              </div>
            </template>
            {{ archivesVO.education }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <el-divider content-position="left">个人简介</el-divider>
      <div style="margin: 20px 20px">
    <span style="font-size:19px;line-height:150%;
            font-family: 仿宋,serif;
            color:black;background:white;
            padding-left: 20px">
      {{ archivesVO.personalIntro }}
    </span>
      </div>
    </div>
  </el-watermark>

</template>

<style scoped>

</style>