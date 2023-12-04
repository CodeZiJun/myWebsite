<script setup>
// 引入 VueOffice 组件
import VueOfficePdf from '@vue-office/pdf'
import {ref} from 'vue'

// 设置文档网络地址，可以是本地文件
const src = ref()

// 本地预览
const previewFile = async (event) => {
  src.value = (event.target).files?.[0]
}

// 请求预览
const getFile = () => {
  src.value = 'https://yjy-teach-oss.oss-cn-beijing.aliyuncs.com/solution/demo.pdf'
}
</script>

<template>
  <div style="display: flex; justify-content: center">
    <div >
      <h1>电子调档函</h1>
      <button @click="getFile">获取远程</button>
      <input type="file" accept=".pdf" @change="previewFile" />
      <vue-office-pdf :src="src" style="height: 100vh; width: 60vw" />
    </div>
  </div>

</template>

<style scoped>

</style>