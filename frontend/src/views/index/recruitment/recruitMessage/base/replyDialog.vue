<template>
    <div>
        <el-dialog
            v-model="dialogVisible"
            :title="props.title"
            width="50%"
            :before-close="handleClose"
        >
            <el-form :model="viewContent" label-width="120px">
                <el-form-item label="回复内容">
                <el-input v-model="viewContent.content" type="textarea" rows="8" />
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
    isAdmin:Boolean,
});

let dialogVisible=ref(props.displayWindow);
watch(() => props.displayWindow, (newVal) => {
  dialogVisible.value = !dialogVisible.value;
});

let viewContent = reactive({
    content: '',
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
//     post("/recruit/updateApply",viewContent,(data) => {
//       if(data==="success"){
//         ElMessage({
//           message: "修改申请成功",
//           type: "success",
//         });
//       }else{
//         ElMessage({
//           message: "修改申请失败",
//           type: "success",
//         });
//       }
//   },(err)=>{
//     console.log('Error:',err);
//     ElMessage({
//           message: "修改申请失败",
//           type: "error",
//     });
//   });
}

</script>
<style scoped>
    
</style>