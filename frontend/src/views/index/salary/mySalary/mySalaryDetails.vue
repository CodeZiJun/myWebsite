<script setup>
import {reactive, ref, onMounted} from "vue";
import {getWithData} from "@/net";
import {ElMessage} from "element-plus";

let searchText = ref("")
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(20)
const handleInputChange = () => {
  details.detail = searchText
  getdata()
}
let details = new reactive({
  detail: null
})
let salaryList = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const getdata = () => {
  getWithData(`/api/salary/selectSalaryPage/${salaryList.current}/${salaryList.size}`,
      details
      ,(data) => {
        totalPage.value = data.total
        salaryList.data = data.records
        for(let i = 0; i < salaryList.data.length; i ++) {
          salaryList.data[i].status = false
        }
      }, () => {
        ElMessage.error("数据请求失败！")
      })
}
onMounted(
    getdata
)
</script>

<template>
  <div>
    <div style="margin-bottom: 10px">
      <el-input style="width: 200px" placeholder="查询名称" v-model="searchText" @input="handleInputChange"></el-input>
      <el-button type="primary" style="margin-left: 10px">查询</el-button>
      <el-button type="info" @click="() => {searchText = '';getdata()}">重置</el-button>
    </div>

    <el-table :data="salaryList.data"
              :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              highlight-current-row
              border>
      <el-table-column prop="id" label="薪资编号" align="center"></el-table-column>
      <el-table-column prop="amount" label="金额" align="center"></el-table-column>
    </el-table>
  </div>

</template>

<style scoped>

</style>