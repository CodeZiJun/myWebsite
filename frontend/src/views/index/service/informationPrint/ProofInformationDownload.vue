<script setup>
import '@vue-office/docx/lib/index.css'
import VueOfficeDocx from "@vue-office/docx";
import {ref} from "vue";
import { exportDocx } from "@/utils/docxUtils";
import {baseUrl} from "@/main";

const src = ref()
const previewFile = async (event) => {
  src.value = (event.target).files?.[0]
}
const getFile = () => {
  src.value = baseUrl + "/" + "image/reportTable.docx"
}

// 省略其他代码
const downloadFile = async () => {
  const response = await fetch(src.value);
  const data = await response.blob();
  const url = URL.createObjectURL(data);
  const link = document.createElement('a');
  link.href = url;
  link.download = 'downloaded-file.docx';
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
};


</script>

<template>
  <div style="display: flex;justify-content: center">
    <div>
      <h1>工作报表模板下载</h1>
      <button @click="getFile">获取远程</button>
      <button @click="exportDocx('/test.docx', {title: '测试填充'}, 'ok.docx')">填充</button>
      <button @click="downloadFile">下载文件</button>

      <input type="file" accept=".docx" @change="previewFile" />
      <vue-office-docx :src="src" style="height: 100vh; width: 60vw;" />
    </div>
  </div>


</template>

<style scoped>

</style>