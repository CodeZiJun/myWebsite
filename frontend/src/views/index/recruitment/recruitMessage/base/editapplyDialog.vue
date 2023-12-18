<template>
    <div>
        <el-dialog
            v-model="dialogVisible"
            :title="props.title"
            width="50%"
            :before-close="handleClose"
        >
            <el-form :model="viewContent" label-width="120px">
                <el-form-item label="申请职位">
                <el-input v-model="viewContent.position" style="width: 50%" />
                </el-form-item>
                <el-form-item label="期望薪资">
                <el-input v-model="viewContent.salary" style="width: 50%" />
                </el-form-item>
                <el-form-item label="用户名">
                <el-input v-model="viewContent.username" style="width: 30%" />
                </el-form-item>
                <el-form-item label="联系电话">
                <el-input v-model="viewContent.phone" style="width: 30%" />
                </el-form-item>
                <el-form-item label="申请日期">
                <el-col :span="11">
                    <el-date-picker
                    v-model="viewContent.date"
                    type="date"
                    placeholder="选择日期"
                    style="width: 100%"
                    />
                </el-col>
                </el-form-item>
                <el-form-item label="上传简历">
                    <el-upload
                    v-model:file-list="fileList"
                    class="upload-demo"
                    action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
                    multiple
                    :on-preview="handlePreview"
                    :on-remove="handleRemove"
                    :before-remove="beforeRemove"
                    :limit="3"
                    :on-exceed="handleExceed"
                >
                    <el-button type="primary">点击上传</el-button>
                    <template #tip>
                    <div class="el-upload__tip">
                        接受pdf/doc/docx文件小于10MB.
                    </div>
                    </template>
                </el-upload>
                </el-form-item>
                <el-form-item label="备注内容">
                <el-input v-model="viewContent.content" type="textarea" rows="5" />
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
    content: '',
    position:'',
    salary:'',
    username:'',
    phone:'',
    date: '',
    filename:'',
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
    post("/recruit/updateApply",viewContent,(data) => {
      if(data==="success"){
        ElMessage({
          message: "修改申请成功",
          type: "success",
        });
      }else{
        ElMessage({
          message: "修改申请失败",
          type: "success",
        });
      }
  },(err)=>{
    console.log('Error:',err);
    ElMessage({
          message: "修改申请失败",
          type: "error",
    });
  });
}

</script>
<style scoped>
    
</style>