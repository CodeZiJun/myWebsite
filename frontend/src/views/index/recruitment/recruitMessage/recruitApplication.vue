<template>
    <div>
      <!-- 公告列表 -->
        <el-table :data="applications" style="width: 100%">
            <el-table-column prop="id" label="申请ID" header-align="center" align="center"/>
            <el-table-column prop="position" label="申请职位" header-align="center" align="center"/>
            <el-table-column prop="status" label="申请状态" header-align="center" align="center"/>
            <el-table-column prop="phone" label="联系电话" header-align="center" align="center"/>
            <el-table-column prop="applicationDate" label="申请日期" sortable header-align="center" align="center"/>
            <!-- 操作按钮 -->
            <el-table-column header-align="center" label="操作">
            <template #default="scope">
                <el-space>
                <el-button size="small" @click="editApplication(scope.row)">修改</el-button>
                  <el-button size="small" @click="deleteApplication(scope.row)">清除</el-button>
                    <el-button size="small" @click="replyApplication(scope.row)">回复</el-button>
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
      <editapplyDialog title="修改申请" :displayWindow="displayEdit" :showContent="viewApply">
      </editapplyDialog>
      <replyDialog title="查看回复" :displayWindow="displayReply" :showContent="replyContent">
      </replyDialog>
      <el-dialog
          v-model="dialogVisible"
          title="发起申请"
          width="50%"
          :before-close="handleClose"
      >
          <el-form :model="form" label-width="120px">
            <el-form-item label="申请职位">
              <el-input v-model="form.position" style="width: 50%" />
            </el-form-item>
            <el-form-item label="期望薪资">
              <el-input v-model="form.salary" style="width: 50%" />
            </el-form-item>
            <el-form-item label="用户名">
              <el-input v-model="form.username" style="width: 30%" />
            </el-form-item>
            <el-form-item label="联系电话">
              <el-input v-model="form.phone" style="width: 30%" />
            </el-form-item>
            <el-form-item label="申请日期">
              <el-col :span="11">
                <el-date-picker
                  v-model="form.date"
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
              <el-input v-model="form.content" type="textarea" rows="5" />
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
  import editapplyDialog from "./base/editapplyDialog.vue";
  import replyDialog from "./base/replyDialog.vue";
  import {getStorageInfoJson, myInfo} from "@/utils/profileUtils";
  

  const currentPage = ref(1);
  const pageSize = ref(10);

  let applications=ref([]);
  let src_application=ref([]);
  

  let notices = ref([]);
  let src_notices=ref([]);
  let msgContent=ref('');
  let searchContent = ref('');
  let displayView=ref(false);
  let displayEdit=ref(false);
  let displayReply=ref(false);
  const isAdmin = ref(false)
  let form = reactive({
    content: '',
    position:'',
    salary:'',
    username:'',
    phone:'',
    date: '',
    filename:'',
  })
  let viewApply = reactive({
    content: '',
    position:'',
    salary:'',
    username:'',
    phone:'',
    date: '',
    filename:'',
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
              await post("/recruit/apply",null,(data) => {
                src_application=data;
                applications.value=data;
                console.log(applications);
              });
          } catch (error) {
              console.error("An error occurred:", error);
          }
      }
  )
  onMounted(
      () => {
        myInfo.value = getStorageInfoJson()
        isAdmin.value = myInfo.value.role === "admin"
        console.log(isAdmin.value)
      }
  )
  
  
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

  
  
  function updateApply(){
    post("/recruit/apply",null,(data) => {
        src_application=data;
        applications.value=data;
        console.log(applications);
    });
  }
  
  
  const dialogVisible = ref(false);
  
  
  const displayAddNotice = () => {
    dialogVisible.value = true;
  };
  
  const doAddNotice=()=>{
    console.log(form);
    dialogVisible.value = false;
    post("/recruit/addApply",form,(data) => {
        if(data==="success"){
          ElMessage({
            message: "发布申请成功",
            type: "success",
          });
          updateApply();
        }else{
          ElMessage({
            message: "发布申请失败",
            type: "success",
          });
        }
    },(err)=>{
      console.log('Error:',err);
        ElMessage({
            message: "发布申请失败",
            type: "error",
        });
    });
  }

  const replyApplication = (notice) =>{
    displayReply.value=!displayReply.value;
  }
  
  const editApplication = (notice) => {
    post("/recruit/findApplyByUser",{username:notice.applicantUsername},(data) => {
        viewApply=data;
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

  const deleteApplication = (notice)=>{
    post("/recruit/deleteApplyByUser",{username:notice.applicantUsername},(data) => {
      if(data==="success"){
        // applications.value = applications.value.filter((n) => n.id !== applications.id);
        ElMessage({
          message: "删除申请成功",
          type: "success",
        });
        updateApply();
      }else{
        ElMessage({
          message: "删除申请失败",
          type: "error",
        });
      }
    },(err)=>{
      console.log('Error:',err);
      ElMessage({
            message: "删除申请失败",
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
  
  </script>
  