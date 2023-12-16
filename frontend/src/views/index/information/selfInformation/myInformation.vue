<script setup>
import {
  Calendar,
  Grid, InfoFilled,
  Iphone,
  Memo,
  OfficeBuilding,
  Postcard,
  RefreshLeft,
  Tickets, Timer,
  User,
  UserFilled
} from "@element-plus/icons-vue";
import {reactive, onMounted, ref} from "vue";
import {get} from "@/net";
import {getStorageInfoJson, myInfo} from "@/utils/profileUtils";
import {ElMessage} from "element-plus";


const archivesVO = ref({
})
const getArchives = (email) => {
  get(`/api/archives/selectDetailByEmail/${email}`,
      (data) => {
        archivesVO.value = data
      }, () => {
        ElMessage.error("查询失败，请联系管理员")
      })
}
onMounted(
    () => {
      myInfo.value = getStorageInfoJson()
      getArchives(myInfo.value.email)
    }
)
const refresh = () => {
  myInfo.value = getStorageInfoJson()
  getArchives(myInfo.value.email)
}

</script>

<template>
  <div>
    <el-descriptions
        title="职工个人信息"
        :column="3"
        :size="'large'"
        border
    >
      <template #extra>
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
            <el-icon><InfoFilled /></el-icon>
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
</template>

<style scoped>

</style>