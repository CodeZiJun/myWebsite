<script setup>
import {reactive, ref, onMounted} from "vue";
import {getWithData} from "@/net";
import {ElMessage} from "element-plus";

let searchText = ref("")
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(20)
let details = new reactive({
  detail: null
})
let commissionList = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const handleInputChange = () => {
  details.detail = searchText
  getdata()
}

const handleSizeChange = (val) => {
  commissionList.size = val
  getdata()
}
const handleCurrentChange = (val) => {
  commissionList.current = val
  getdata()
}
const getdata = () => {
  getWithData(`/api/commission/selectPage/${commissionList.current}/${commissionList.size}`,
      details
      ,(data) => {
        totalPage.value = data.total
        commissionList.data = data.records
        for(let i = 0; i < commissionList.data.length; i ++) {
          commissionList.data[i].status = false
        }
      }, () => {
        ElMessage.error("数据请求失败！")
      })
}
onMounted(() => {
      getdata()
})
</script>

<template>
  <div>
    <div style="margin-bottom: 10px">
      <el-input style="width: 200px" placeholder="查询名称" v-model="searchText" @input="handleInputChange"></el-input>
      <el-button type="info" style="margin-left: 10px" @click="() => {searchText = '';getdata()}">重置</el-button>
      <el-button type="success" style="margin-left: 10px">查询所有</el-button>
      <el-button type="warning" style="margin-left: 10px" plain>筛选无职位</el-button>
      <el-button type="danger" style="margin-left: 10px" plain>筛选无部门</el-button>
      <el-button type="primary" style="margin-left: 10px" plain>筛选全无账户</el-button>
    </div>

    <el-table :data="commissionList.data"
              :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              highlight-current-row
              border>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" align="center"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" align="center"></el-table-column>
      <el-table-column prop="email" label="用户邮箱" min-width="220" align="center"></el-table-column>
      <el-table-column prop="departmentName" label="所属部门" align="center"></el-table-column>
      <el-table-column prop="positionName" label="所居职位" align="center"></el-table-column>
      <el-table-column prop="entryDate" label="入职时间" min-width="150" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope" #default="{row}">
          <el-button size="small" type="primary" plain>编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin: 10px 0">
      <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 15]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalPage"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<style scoped>

</style>