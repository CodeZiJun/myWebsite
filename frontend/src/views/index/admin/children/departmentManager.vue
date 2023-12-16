<script setup>
import {ref, reactive, onMounted} from "vue";
import {del, delWithData, getWithData, post} from "@/net";
import {ElMessage, ElNotification} from "element-plus";
import {Delete, Edit, Message, Upload, User, Warning} from "@element-plus/icons-vue";
import {myInfo} from "@/utils/profileUtils";

let searchText = ref("")
let flagOpenTip = 0
let flagCloseTip = 0
const addFormRef = ref()
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(20)
const deleteDialogVisible = ref(false)
const addDialogVisible = ref(false)
const multipleSelection = ref([])

const deleleDepartment = reactive({
  id: "",
  departmentName: "",
  username: "",
  email: ""
})

let details = new reactive({
  detail: null
})
let departmentList = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const addForm = reactive({
  departmentName: '',
  email: ''
})

const confirmDeleteOne = (id, departmentName, username, email) => {
  deleleDepartment.id = id
  deleleDepartment.departmentName = departmentName
  deleleDepartment.username = username
  deleleDepartment.email = email
  deleteDialogVisible.value = true
}
const deleteBatch = () => {
  const ids = multipleSelection.value
  delWithData('/api/department/deleteBatch',
      ids,
      () => {
        getdata()
        ElMessage.success("删除成功")
      }, () => {
        getdata()
        ElMessage.error("删除失败")
      }
  )
}

const canSelect = (row, index) => {
    return row.email !== myInfo.value.email;
}

const validateDepartmentName = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请输入部门名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('不可包含特殊字符'))
  } else {
    callback()
  }
}
const rule = {
  username: [
    { validator: validateDepartmentName, trigger: ['blur', 'change'] },
    { min: 1, max: 20, message: '部门名的长度必须在1-10个字符之间', trigger: ['blur', 'change'] }
  ],
  email: [
    { required: true, message: '请输入电子邮件地址', trigger: 'blur' },
    { type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ],
}

const getUpdateDepartmentVos = () => {

}
const submitAddForm = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      post('/api/department/addDepartment', {...addForm},
          () => {
            getdata()
            ElMessage.success("添加成功")
          }, () => {
          getdata();
          ElMessage.error("添加失败")})
      addDialogVisible.value = false
      addFormRef.value.resetFields();
    } else {
      ElMessage.warning("请检查表单内容！")
    }
  })
}
const deleteOne = (id) => {
  del(`/api/department/delete/${id}`,
      () => {
        getdata()
        ElMessage.success("删除成功")
      }, () => {
        getdata()
        ElMessage.error("删除失败")
      }
  )
  deleteDialogVisible.value = false
}
const handleSizeChange = (val) => {
  departmentList.size = val
  getdata()
}
const handleCurrentChange = (val) => {
  departmentList.current = val
  getdata()
}
const handleInputChange = () => {
  details.detail = searchText
  getdata()
}
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map(v => v.id)
}
const editOpen = (row) => {
  if (!flagOpenTip) {
    openEditTip()
    flagOpenTip ++
  }
  row.status = true
}
const editClose = (currentRow, oldCurrentRow) => {
  if(oldCurrentRow!==null){
    oldCurrentRow.status=false
    if (!flagCloseTip) {
      closeEditTip()
      flagCloseTip ++
    }
  }
}
const openEditTip = () => {
  ElNotification({
    title: '编辑Tip',
    message: '双击或单击编辑按钮开始编辑，选中其他行结束',
    type: 'success',
  })
}
const closeEditTip = () => {
  ElNotification({
    title: '保存Tip',
    message: '每次切换页面或翻页表格前记得提交修改哦',
    type: 'warning',
  })
}
const getdata = () => {
  getWithData(`/api/department/selectPage/${departmentList.current}/${departmentList.size}`,
      details
      ,(data) => {
        totalPage.value = data.total
        departmentList.data = data.records
        for(let i = 0; i < departmentList.data.length; i ++) {
          departmentList.data[i].status = false
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
    <div>
      <el-input style="width: 200px" placeholder="查询名称" v-model="searchText" @input="handleInputChange"></el-input>
      <el-button type="primary" style="margin-left: 10px">查询</el-button>
      <el-button type="info" @click="() => {searchText = '';getdata()}">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="evt => {addDialogVisible = true}" plain :icon="Upload">新增部门</el-button>
      <el-popconfirm
          width="220"
          confirm-button-text="确认"
          @confirm="deleteBatch()"
          cancel-button-text="取消"
          :icon="Warning"
          icon-color="#626EF"
          title="你确定要执行批量删除？"
      >
        <template #reference>
          <el-button type="danger" plain :icon="Delete">批量删除</el-button>
        </template>
      </el-popconfirm>
      <el-button type="success" plain :icon="Edit" @click="submitAllModified">提交修改</el-button>
    </div>

    <el-table :data="departmentList.data"
              :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              @selection-change="handleSelectionChange"
              @cell-dblclick="editOpen"
              @current-change="editClose"
              highlight-current-row
              border>
      <el-table-column type="selection" width="55" align="center" :selectable="canSelect"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="departmentName" label="部门名" align="center">
        <template #default="{row}">
          <el-input v-if="row.status" v-model="row.departmentName"></el-input>
          <span v-else>{{ row.departmentName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="部门负责人" align="center">
        <template #default="{row}">
          <el-input v-if="row.status" v-model="row.username"></el-input>
          <span v-else>{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="email" label="负责人邮箱" align="center">
        <template #default="{row}">
          <el-input v-if="row.status" v-model="row.email"></el-input>
          <span v-else>{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope" #default="{row}">
          <el-button size="small" type="primary" plain @click="editOpen(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain :disabled="scope.row.status"
                     @click="confirmDeleteOne(scope.row.id, scope.row.departmentName, scope.row.username, scope.row.email)">删除</el-button>
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
    <el-dialog
        v-model="deleteDialogVisible"
        title="删除警告"
        width="30%"
        align-center
        style="border-radius: 20px"
    >
      <span style="font-size: 16px">
        您将要删除部门信息，请确认！
        <br>
        <strong>部门ID:{{ deleleDepartment.id }}</strong>
        <br>
        <strong>部门名: {{ deleleDepartment.departmentName }}</strong>
        <br>
        <strong>负责人姓名: {{ deleleDepartment.username }}</strong>
        <br>
        <strong>负责人邮箱:  {{ deleleDepartment.email }}</strong>
      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="warning" @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="deleteOne(deleleDepartment.id)">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
        v-model="addDialogVisible"
        title="添加部门"
        align-center
        style="border-radius: 20px; display: flex; justify-items: center; width: 700px"
        draggable
    >
      <el-form :model="addForm" label-width="90px" style="margin-top: 20px" :rules="rule" ref="addFormRef">
        <el-form-item prop="departmentName" label="部门名:">
          <el-input v-model="addForm.departmentName" maxlength="30" placeholder="设置新部门名字" style="width: 260px">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="minisiterId" label="负责人邮箱:">
          <el-input v-model="addForm.email" maxlength="30" placeholder="请为新部门选定负责人" style="width: 260px">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAddForm" style="width: 100px">确认</el-button>
          <el-button type="warning" @click="evt => {addDialogVisible = false;addFormRef.resetFields();}" style="width: 100px;">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>