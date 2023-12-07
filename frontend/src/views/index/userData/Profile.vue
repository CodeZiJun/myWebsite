<script setup>
import { ref } from 'vue'
import router from "@/router";
let currentDate = ref(new Date())
setInterval(() => {
  currentDate.value = new Date()
}, 1000)


const activeName = ref('first')

function handleClick(tab) {
  if (tab.paneName === "second")
    router.push('/index/profile/resetPassword')
  else if (tab.paneName === "third")
    router.push('/index/profile/avatar')
  else
    router.push('/index/profile/')
}
</script>

<template>
  <div class="profile-back">
    <div class="center-card" :style="{ boxShadow: `var(--el-box-shadow-dark)`}">
      <el-row :gutter="20">
        <el-col :span="15" style="align-items: center; display: flex; margin-left: 30px">
          <div style="  height: 90%; width: 100%">
            <el-tabs v-model="activeName" class="tabs" type="card" @tab-click="handleClick">
              <el-tab-pane label="我的信息" name="first" />
              <el-tab-pane label="修改密码" name="second" />
              <el-tab-pane label="设置头像" name="third" />
            </el-tabs>
            <router-view v-slot="{ Component }">
              <transition name="el-fade-in-linear" mode="out-in">
                <div :key="$route.path">
                  <component :is="Component"/>
                </div>
              </transition>
            </router-view>
          </div>
        </el-col>
        <el-col :span="8" style="align-items: center; display: flex">
          <div style="height: 90%; display: flex; align-items: center; margin-right: 15px;">
            <el-card shadow="hover" :body-style="{ padding: '0px'}">
              <img
                  src="https://cdn.pixabay.com/photo/2022/11/14/06/56/maple-7590861_1280.jpg"
                  class="image"
                  alt=""/>
              <div style="padding: 10px; margin-left: 10px">
                <h1>祝您拥有好心情!</h1>
                <div class="bottom">
                  <time class="time">{{ currentDate }}</time>
                </div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style scoped>
.center-card {
  border-radius: 30px;
  width: 900px;
  height: 500px;
  background-color: white;
  margin-top: 100px;
}
.profile-back {
  display: flex;
  -webkit-justify-content: center;
  width: 100%;
  height: 100%;
  background-image: url("https://cdn.pixabay.com/photo/2017/07/01/19/48/background-2462430_1280.jpg")
}
.el-row {
  height: 100%;
}

.image {
  width: 100%;
  height: 300px;
  object-fit: cover
}
.el-card {
  --el-card-border-radius: 20px;
  height: 100%;
}
.el-tabs {
  width: 100%;
}
.el-main {
  --el-main-padding: 0
}
</style>