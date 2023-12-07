<script setup>
import {InfoFilled, User} from "@element-plus/icons-vue";
import {ref} from "vue";
import {getStorageInfoJson} from "@/utils/profileUtils";
let myInfo = ref(getStorageInfoJson())
let editing = ref(false)

function modifyUsername() {

}
</script>

<template>
  <div style="height:400px">
    <el-card class="infoCard" :style="{ boxShadow: `var(--el-box-shadow-dark)`}">
      <template #header>
        <div class="card-header">
          <el-icon size="25px"><InfoFilled /></el-icon>
          <div>
            <el-button v-show="editing" plain type="primary" class="button">确认修改</el-button>
            <el-button v-show="editing" class="button"  type="warning" text @click="() => editing = !editing">取消修改</el-button>
            <el-button v-show="!editing" class="button" plain type="warning" text @click="() => editing = !editing">修改信息</el-button>
          </div>
        </div>
      </template>
      <div class="card-body">
        <div class="card-body-username">
          <h2 v-show="!editing">{{ myInfo.username }}</h2>
          <el-input v-show="editing" :placeholder="myInfo.username" maxlength="20" show-word-limit clearable size="large">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </div>
        <div style="margin-bottom: 10px">
          <span>邮箱：</span>
          <span>{{ myInfo.email }}</span>
        </div>
        <div>
          <span>注册时间：</span>
          <span>{{ myInfo.registerDate }}</span>
        </div>
      </div>
    </el-card>
    <el-skeleton :rows="2" animated />
  </div>

</template>

<style scoped>
.infoCard {
  width: 450px;
  height: 250px;
  margin-bottom: 40px;
  margin-top: 15px;
  border-radius: 20px;
  background-image: linear-gradient(to bottom right, #f3f3f3, #e8f4fd);
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-body {
  margin-top: -20px;
  margin-left: 20px;
}
.card-body-username {
  margin-top: 5px;
  margin-bottom: 5px;
  width: 80%;
  font-size: 20px;
}

</style>