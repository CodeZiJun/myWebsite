<script setup>
import {ref, reactive, onMounted} from "vue";
import {get} from "@/net";
import {ElMessage} from "element-plus";

let searchText = ref("")
const currentPage = ref(1)
const pageSize = ref(5)
const totalPage = ref(20)
let orderlist = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const handleSizeChange = (val) => {
  orderlist.size = val
  getdata()
}
const handleCurrentChange = (val) => {
  orderlist.current = val
  getdata()
}

const getdata = () => {
  get(`/api/account/selectAccountPage/${orderlist.current}/${orderlist.size}`,
      (data) => {
        totalPage.value = data.total
        orderlist.data = data.records
        console.log(data)
      }, () => {
          ElMessage.error("数据请求失败！")
      })
}

onMounted(() => getdata())
</script>

<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询名称" v-model="searchText"></el-input>
      <el-button type="primary" style="margin-left: 10px">查询</el-button>
      <el-button type="info">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" plain>新增</el-button>
      <el-button type="danger" plain>批量删除</el-button>
    </div>

    <el-table :data="orderlist.data">
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" align="center"></el-table-column>
      <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
      <el-table-column prop="role" label="角色" align="center"></el-table-column>
      <el-table-column prop="registerTime" label="注册时间" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope">
          <el-button size="small" type="primary" plain >编辑</el-button>
          <el-button size="small" type="danger" plain >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[2, 4, 5, 8, 10]"
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