<template>
    <div>
        <el-dialog
            v-model="dialogVisible"
            :title="props.title"
            width="50%"
            :before-close="handleClose"
        >
            <el-form label-width="120px">
                <el-form-item label="公告标题">
                    <div>
                    {{ viewContent.title }}
                    </div>
                </el-form-item>
                <el-form-item label="公告内容">
                    <div>
                    {{ viewContent.content }}
                    </div>
                </el-form-item>
                <el-form-item label="发布时间">
                    <el-col :span="11">
                    <el-date-picker
                        readonly
                        v-model="viewContent.date"
                        type="date"
                        style="width: 50%"
                    />
                    </el-col>
                </el-form-item>
                <el-form-item label="发布部门">
                    <div>
                    {{ viewContent.department }}
                    </div>
                </el-form-item>
                <el-form-item label="联系人">
                    <div>
                    {{ viewContent.contacts }}
                    </div>
                </el-form-item>
                <el-form-item label="联系电话">
                    <div>
                    {{ viewContent.phone }}
                    </div>
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
    console.log('EPS',viewContent);
    dialogVisible.value=false;
}

</script>
<style scoped>
    
</style>