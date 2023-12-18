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
          <el-input v-if="isManager" v-model="repText" type="textarea" rows="8" />
          <el-input v-else="isManager" disabled v-model="repText" type="textarea" rows="8" />
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
  replyContent:String,
  Username:String,
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


let isManager=ref(props.isAdmin);
watch(()=>props.isAdmin,(newVal)=>{
  isManager.value=newVal;
});

let repText=ref(props.replyContent);
watch(()=>props.replyContent,(newVal)=>{
  repText.value=newVal;
});

let curUname=ref(props.Username);
watch(()=>props.Username,(newVal)=>{
  curUname.value=newVal;
});

function updateMsg(){
  post("/recruit/getReply",{username:curUname.value},(data) => {
    repText.value=data;
  });
}


const handleFunction=()=>{
  dialogVisible.value=false;
  if(isManager.value===true){
    post("/recruit/setReply",{"content":repText.value,"username":curUname.value},(data) => {
      if(data==="success"){
        updateMsg();
        ElMessage({
          message: "回复成功",
          type: "success",
        });
      }else{
        ElMessage({
          message: "回复失败",
          type: "success",
        });
      }
    },(err)=>{
      console.log('Error:',err);
      ElMessage({
        message: "回复失败",
        type: "error",
      });
    });
  }else{
    ElMessage({
      message: "您无权回复",
      type: "error",
    });
  }
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
  console.log({"content":repText.value,"username":curUname.value})
}
</script>
<style scoped>

</style>