<script setup>
import '@vue-office/docx/lib/index.css'
import VueOfficeDocx from "@vue-office/docx";
import {ref} from "vue";
import { exportDocx } from "@/utils/docxUtils";

const src = ref()
const previewFile = async (event) => {
  src.value = (event.target).files?.[0]
}
const getFile = () => {
  src.value = 'https://yjy-teach-oss.oss-cn-beijing.aliyuncs.com/solution/demo.docx'
}

</script>

<template>
  <div style="display: flex;justify-content: center">
    <div>
      <h1>证明信息</h1>
      <button @click="getFile">获取远程</button>
      <button @click="exportDocx('/test.docx', {title: '测试填充'}, 'ok.docx')">填充</button>
      <input type="file" accept=".docx" @change="previewFile" />
      <vue-office-docx :src="src" style="height: 100vh; width: 60vw;" />
    </div>
  </div>


</template>

<style scoped>

</style>