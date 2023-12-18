<template>
  <div>
    <!-- 公告列表 -->
    <el-table :data="notices" style="width: 100%">
      <el-table-column prop="id" label="公告ID" header-align="center" align="center"/>
      <el-table-column prop="title" label="公告标题" header-align="center" align="center"/>
      <el-table-column prop="viewcontent" label="公告内容" header-align="center" align="center"/>
      <el-table-column prop="department" label="发布部门" header-align="center" align="center"/>
      <el-table-column prop="date" label="发布日期" sortable header-align="center" align="center"/>
      <!-- 操作按钮 -->
      <el-table-column header-align="center">
        <template #header>
            <el-input @change="searchMsg" v-model="searchContent" size="small" placeholder="根据内容查找" />
        </template>
        <template #default="scope">
          <el-space>
            <el-button size="small" @click="viewNotice(scope.row)">查看</el-button>
            <span v-if="isAdmin">
              <el-button size="small" @click="editNotice(scope.row)">编辑</el-button>
              <el-button size="small" @click="deleteNotice(scope.row)">删除</el-button>
            </span>

          </el-space>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="notices.length">
        </el-pagination>
    <div style="display: flex; justify-content: center; align-items: center;">
        <el-button
        type="primary"
        :icon="Plus"
        circle
        @click="displayAddNotice"
        style="margin-top: 1%;"
        ></el-button>
    </div>
    <editmsgDialog :displayWindow="displayEdit" :showContent="viewContent">
    </editmsgDialog>
    <simpleDialog :displayWindow="displayView" :showContent="viewContent">
    </simpleDialog>
    <el-dialog
        v-model="dialogVisible"
        title="添加公告"
        width="50%"
        :before-close="handleClose"
    >
        <el-form :model="form" label-width="120px">
          <el-form-item label="公告标题">
            <el-input v-model="form.title" style="width: 50%" />
          </el-form-item>
          <el-form-item label="公告内容">
            <el-input v-model="form.content" type="textarea" />
          </el-form-item>
          <el-form-item label="发布时间">
            <el-col :span="11">
              <el-date-picker
                v-model="form.date"
                type="date"
                placeholder="选择日期"
                style="width: 100%"
              />
            </el-col>
          </el-form-item>
          <el-form-item label="发布部门">
            <el-input v-model="form.department" style="width: 50%" />
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="form.contacts" style="width: 30%" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.phone" style="width: 30%" />
          </el-form-item>
          <!-- <el-form-item label="Instant delivery">
            <el-switch v-model="form.delivery" />
          </el-form-item> -->
        </el-form>
        <template #footer>
          <span class="dialog-footer">
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="doAddNotice">
              确认
              </el-button>
          </span>
        </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref,reactive,computed } from "vue";
import { ElMessage } from "element-plus";
import { Plus } from '@element-plus/icons-vue'
import { get,post } from "@/net";
import axios from 'axios'
import simpleDialog from "./base/simpleDialog.vue";
import editmsgDialog from "./base/editmsgDialog.vue";
import {getStorageInfoJson, myInfo} from "@/utils/profileUtils";


const currentPage = ref(1);
const pageSize = ref(10);
const isAdmin = ref(false)
let notices = ref([]);
let src_notices=ref([]);
let msgContent=ref('');
let setAdmin=ref(false);
let searchContent = ref('');
let displayView=ref(false);
let displayEdit=ref(false);
let form = reactive({
  content: '',
  title:'',
  department:'',
  contacts:'',
  phone:'',
  date: '',
})
let viewContent = reactive({
  content: '',
  title:'',
  department:'',
  contacts:'',
  phone:'',
  date: '',
})

let copy_form=form.value;

// 截取字符串前10个字符的函数
function truncateString(str) {
  return str.length > 10 ? str.substring(0, 10) + '...' : str;
}


const paginatedNotices = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = currentPage.value * pageSize.value;
  return notices.value.slice(start, end);
});

const handleSizeChange = (newSize) => {
  pageSize.value = newSize;
};

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage;
};

// 从数据库中请求数据
onMounted(
    async function(){
        try {
            await post("/recruit/msg",null,(data) => {
                src_notices=data;
                let temp=data;
                for(let i=0;i<src_notices.length;i++){
                    temp[i].viewcontent=truncateString(src_notices[i].content);
                    temp[i].date=src_notices[i].date.split('T')[0];
                }
                notices.value=temp;
                console.log(notices);
            });
        } catch (error) {
            console.error("An error occurred:", error);
        }
    }
);



function searchMsg(){
  if(searchContent.value!==""){
    post("/recruit/matchMsgContent ",searchContent.value,(data) => {
        src_notices=data;
        let temp=data;
        for(let i=0;i<src_notices.length;i++){
            temp[i].viewcontent=truncateString(src_notices[i].content);
            temp[i].date=src_notices[i].date.split('T')[0];
        }
        notices.value=temp;
        console.log(notices);
    });
  }else{
    post("/recruit/msg",null,(data) => {
        src_notices=data;
        let temp=data;
        for(let i=0;i<src_notices.length;i++){
            temp[i].viewcontent=truncateString(src_notices[i].content);
            temp[i].date=src_notices[i].date.split('T')[0];
        }
        notices.value=temp;
        console.log(notices);
    });
  }
}


function updateMsg(){
    post("/recruit/msg",null,(data) => {
        src_notices=data;
        let temp=data;
        for(let i=0;i<src_notices.length;i++){
            temp[i].viewcontent=truncateString(src_notices[i].content);
            temp[i].date=src_notices[i].date.split('T')[0];
        }
        notices.value=temp;
    });
}


const dialogVisible = ref(false);


const displayAddNotice = () => {
  dialogVisible.value = true;
};

const doAddNotice=()=>{
  dialogVisible.value = false;
  post("/recruit/addmsg",form,(data) => {
      if(data==="success"){
        ElMessage({
          message: "添加公告成功",
          type: "success",
        });
        updateMsg();
      }else{
        ElMessage({
          message: "添加公告失败",
          type: "success",
        });
      }
  },(err)=>{
    console.log('Error:',err);
    ElMessage({
          message: "添加公告失败",
          type: "error",
    });
  });
}

const editNotice = (notice) => {
  post("/recruit/findMsgById",{id:notice.id},(data) => {
      viewContent=data;
      viewContent.id=notice.id;
      viewContent.contacts='张老';
      displayEdit.value=!displayEdit.value;
  },(err)=>{
    console.log('Error:',err);
    ElMessage({
          message: "添加公告失败",
          type: "error",
    });
  });
  // 编辑公告的逻辑
  // ElMessage({
  //   message: "编辑公告成功",
  //   type: "success",
  // });
};

const deleteNotice = (notice) => {
  // 删除公告的逻辑
  post("/recruit/deleteMsgById",{id:notice.id},(data) => {
    if(data==="success"){
      notices.value = notices.value.filter((n) => n.id !== notice.id);
      ElMessage({
        message: "删除公告成功",
        type: "success",
      });
    }else{
      ElMessage({
        message: "删除公告失败",
        type: "error",
      });
    }
  },(err)=>{
    console.log('Error:',err);
    ElMessage({
          message: "删除公告失败",
          type: "error",
    });
  });
};

const viewNotice =(notice) => {
  post("/recruit/findMsgById",{id:notice.id},(data) => {
      viewContent=data;
      viewContent.contacts='张老';
      displayView.value=!displayView.value;
  },(err)=>{
    console.log('Error:',err);
    ElMessage({
          message: "添加公告失败",
          type: "error",
    });
  });
}

onMounted(
    () => {
      myInfo.value = getStorageInfoJson()
      isAdmin.value = myInfo.value.role === "admin"
      console.log(isAdmin.value)
    }
)

</script>
