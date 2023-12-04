<script setup>
const IMG_SRC_LOGINBACKGROUND = "@/assets/loginBackground"
</script>

<template>
  <div style="width: 100vw; height: 100vh; overflow: hidden; display: flex">
    <div style="flex: 1; background-color: black">
      <img src="@/assets/loginBackground.jpg" style="height: 100%; width: 100%; object-fit: cover" alt="加载失败">
    </div>
    <div class="welcome-title">
      <div style="font-size: 40px;font-weight: bold">My WebSite</div>
      <div style="font-size: 20px;margin-top: 10px">SpringBoot + Vue 前后端分离系统，基于Jwt实现无状态单点登录</div>
      <div style="font-size: 20px;margin-top: 5px">南京工业大学神仙管理系统</div>
    </div>
    <div class="right-card" style="border-radius: 20px">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style scoped>
.welcome-title {
  position: absolute;
  bottom: 30px;
  left: 30px;
  color:white;
  text-shadow: 0 0 10px black;
}
.right-card {
  width: 400px;
  z-index: 1;
  background-color: white;
}
</style>