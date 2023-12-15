<script setup>
import {ref, reactive, onMounted} from "vue";
import {del, delWithData, getWithData, post} from "@/net";
import {ElMessage} from "element-plus";
import {Message, User, Warning} from "@element-plus/icons-vue";
import {myInfo} from "@/utils/profileUtils";

let searchText = ref("")
const addFormRef = ref()
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(20)
const deleteDialogVisible = ref(false)
const addDialogVisible = ref(false)
const multipleSelection = ref([])
const validateUsername = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请输入用户名'))
  } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
    callback(new Error('用户名不可包含特殊字符'))
  } else {
    callback()
  }
}
const rule = {
  username: [
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 1, max: 20, message: '用户名的长度必须在1-20个字符之间', trigger: ['blur', 'change'] }
  ],
  email: [
    { required: true, message: '请输入电子邮件地址', trigger: 'blur' },
    { type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ],
  role: [
    { required: true, message: '请勾选用户类型' }
  ]
}
const deleleAccount = reactive({
  username: "",
  id: null,
  role: ""
})
let details = new reactive({
  detail: null
})
let accountList = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const addForm = reactive({
  username: '',
  email: '',
  role: ''
})
const handleSizeChange = (val) => {
  accountList.size = val
  getdata()
}
const handleCurrentChange = (val) => {
  accountList.current = val
  getdata()
}
const handleInputChange = () => {
  details.detail = searchText
  getdata()
}
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map(v => v.id)
}

const submitAddForm = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      post('/api/account/addAccount', {...addForm},
          () => {
            ElMessage.success("添加成功")
          }, () => {ElMessage.error("添加失败")})
      getdata()
      addDialogVisible.value = false
      addFormRef.value.resetFields();
    } else {
      ElMessage.warning("请检查表单内容！")
    }
  })
}

const getdata = () => {
  getWithData(`/api/account/selectAccountPage/${accountList.current}/${accountList.size}`,
      details
      ,(data) => {
        totalPage.value = data.total
        accountList.data = data.records
      }, () => {
          ElMessage.error("数据请求失败！")
      })
}

const confirmDeleteOne = (username, id, role) => {
  deleleAccount.id = id
  deleleAccount.username = username
  deleleAccount.role = role
  deleteDialogVisible.value = true
}

const deleteOne = (id) => {
  del(`/api/account/delete/${id}`,
      () => {
        ElMessage.success("删除成功")
      }, () => {
        ElMessage.error("删除失败")
      }
  )
  deleteDialogVisible.value = false
  getdata()
}

const deleteBatch = () => {
  const ids = multipleSelection.value
  delWithData('/api/account/deleteBatch',
      ids,
      () => {
        ElMessage.success("删除成功")
      }, () => {
        ElMessage.error("删除失败")
      }
  )
  getdata()
}

const canSelect = (row, index) => {
  if (row.role === "admin")
    return false
  else
    return row.email !== myInfo.value.email;
}

onMounted(() => getdata())
</script>

<template>
  <div>
    <div>
      <el-input style="width: 200px" placeholder="查询名称" v-model="searchText" @input="handleInputChange"></el-input>
      <el-button type="primary" style="margin-left: 10px">查询</el-button>
      <el-button type="info" @click="() => {searchText = '';getdata()}">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="evt => {addDialogVisible = true}" plain>新增</el-button>
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
          <el-button type="danger" plain>批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>

    <el-table :data="accountList.data"
              :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              @selection-change="handleSelectionChange"
              border>
      <el-table-column type="selection" width="55" align="center" :selectable="canSelect"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" align="center"></el-table-column>
      <el-table-column prop="email" label="邮箱" align="center"></el-table-column>
      <el-table-column prop="role" label="角色" align="center"></el-table-column>
      <el-table-column prop="registerTime" label="注册时间" align="center"></el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope">
          <el-button size="small" type="primary" plain >编辑</el-button>
          <el-button size="small" type="danger" plain @click="confirmDeleteOne(scope.row.username, scope.row.id, scope.row.role)">删除</el-button>
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
        您将要删除用户的信息，请确认！
        <br>
        <strong>ID:{{ deleleAccount.id }}</strong>
        <br>
        <strong>用户名: {{ deleleAccount.username }}</strong>
        <br>
        <strong>身份: {{ deleleAccount.role }}</strong>
      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="warning" @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="deleteOne(deleleAccount.id)">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      v-model="addDialogVisible"
      title="添加账户"
      align-center
      style="border-radius: 20px; display: flex; justify-items: center; width: 700px"
      draggable
    >
      <el-form :model="addForm" label-width="80px" style="margin-top: 20px" :rules="rule" ref="addFormRef">
        <el-form-item prop="email" label="邮箱:">
          <el-input v-model="addForm.email" maxlength="30" placeholder="请为新账户设置邮箱" style="width: 260px">
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="username" label="用户名:">
          <el-input v-model="addForm.username" maxlength="30" placeholder="请为新账户设置用户名" style="width: 260px">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="role" label="角色:">
          <el-radio-group v-model="addForm.role">
            <el-radio label="user"  />
            <el-radio label="admin" />
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAddForm" style="width: 100px">确认</el-button>
          <el-button type="warning" @click="evt => {addDialogVisible = false;addFormRef.resetFields();}" style="width: 100px;">取消</el-button>
        </el-form-item>
        <span style="color: grey; font-size: 13px; margin-left: -15px">注意：新增加的账户密码与用户名一致，为了安全请联系用户进行修改</span>
      </el-form>
    </el-dialog>
  </div>
</template>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>