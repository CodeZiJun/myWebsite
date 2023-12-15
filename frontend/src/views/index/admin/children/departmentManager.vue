<template>
  <div>
    <el-table
        :border="true"
        :data="tableData"
        highlight-current-row
        @current-change="close"
        @cell-dblclick="doubleClickCell"
        :header-cell-style="{'background':'#f8f8f9','color':'#515a6e'}">
      <el-table-column v-for="(item,index) in tableHeader"
                       :key="index"
                       :label="item.label"
                       :prop="item.prop"
                       :align="item.align">
        <template #default="{row}">
          <el-input v-if="row.status" v-model="row[item.prop]"></el-input>
          <span v-else>{{row[item.prop]}}</span>
        </template>
      </el-table-column>
    </el-table>
    <el-button type="success" @click="result">提交</el-button>
  </div>

</template>
<script setup>
import  {ref,onMounted} from 'vue'
/** 表头数据 */
const tableHeader=ref([
  {label: '姓名', prop: 'name', width: '',align:'center'},
  {label: '性别', prop: 'sex', width: '',align:'center'},
  {label: '年纪', prop: 'age', width: '',align:'center'},
])
/** 表体数据 */
const tableData=ref([
  { name:'小明',age:'20',sex:'男'},
  { name:'小丽',age:'22',sex:'女'},
  { name:'小华',age:'21',sex:'男'},
])
/** 添加可编辑条件状态值 */
onMounted(()=>{
  for (let i = 0; i < tableData.value.length; i++) {
    tableData.value[i].status=false
  }
})
/** 双击事件打开输入框 */
const doubleClickCell = (row) => {
  row.status=true
}
/** 关闭编辑输入框 */
const close = (currentRow, oldCurrentRow) => {
  if(oldCurrentRow!==null){
    oldCurrentRow.status=false
  }
}
/** 提交时删除控制开关变量 */
const result = () => {
  for (let i = 0; i < tableData.value.length; i++) {
    delete tableData.value[i].status
  }
  console.log(tableData.value)
}
</script>
<style scoped>
:deep(.el-input__inner){
  text-align: center;
}
</style>