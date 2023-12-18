<template>
    <div>
        <el-dialog
            v-model="dialogVisible"
            :title="props.title"
            width="50%"
            :before-close="handleClose"
        >
        <el-form :model="viewContent" label-width="120px">
            <el-form-item label="公告标题">
                <el-input v-model="viewContent.title" style="width: 50%" />
            </el-form-item>
            <el-form-item label="公告内容">
                <el-input v-model="viewContent.content" type="textarea" />
            </el-form-item>
            <el-form-item label="发布时间">
                <el-col :span="11">
                <el-date-picker
                    v-model="viewContent.date"
                    type="date"
                    placeholder="选择日期"
                    style="width: 100%"
                />
                </el-col>
            </el-form-item>
            <el-form-item label="发布部门">
                <el-input v-model="viewContent.department" style="width: 50%" />
            </el-form-item>
            <el-form-item label="联系人">
                <el-input v-model="viewContent.contacts" style="width: 30%" />
            </el-form-item>
            <el-form-item label="联系电话">
                <el-input v-model="viewContent.phone" style="width: 30%" />
            </el-form-item>
            <!-- <el-form-item label="Instant delivery">
                <el-switch v-model="form.delivery" />
            </el-form-item> -->
            </el-form>
            <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleFunction">
                确认
                </el-button>
            </span>
            </template>
        </el-dialog>
    </div>
</template>
<script setup>
import {ref,onMounted,watch,reactive, computed} from 'vue';
import { ElMessage } from "element-plus";
import { get,post } from "@/net";


const props=defineProps({
    title:String,
    displayWindow:Boolean,
    showContent:Object,
});

let dialogVisible=ref(props.displayWindow);
watch(() => props.displayWindow, (newVal) => {
  dialogVisible.value = !dialogVisible.value;
});

let viewContent = reactive({
  id:'',
  content: '',
  title:'',
  department:'',
  contacts:'',
  phone:'',
  date: '',
});

watch(() => props.showContent, (newVal) =>
{
  for (const key in viewContent) {
    if (newVal.hasOwnProperty(key)) {
      viewContent[key] = newVal[key];
    }
  }
},{deep:true});

const handleFunction=()=>{
    dialogVisible.value=false;
    post("/recruit/updateMsg",viewContent,(data) => {
      if(data==="success"){
        ElMessage({
          message: "修改公告成功",
          type: "success",
        });
      }else{
        ElMessage({
          message: "修改公告失败",
          type: "success",
        });
      }
  },(err)=>{
    console.log('Error:',err);
    ElMessage({
          message: "修改公告失败",
          type: "error",
    });
  });
}

</script>
<style scoped>
    
</style>