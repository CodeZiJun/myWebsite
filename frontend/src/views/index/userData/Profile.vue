<script setup>

</script>

<template>
  <div class="profile-back">
    <div class="center-card" :style="{boxShadow: `var(--el-box-shadow-dark)`}">
      <router-view v-slot="{ Component }">
        <transition name="el-fade-in-linear" mode="out-in">
          <component :is="Component"/>
        </transition>
      </router-view>
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
}
</style>