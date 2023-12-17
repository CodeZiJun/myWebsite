<script setup>
import {onMounted, reactive, ref} from "vue";
import {del, delWithData, get, getWithData, post} from "@/net";
import {ElMessage, ElNotification} from "element-plus";
import {Delete, Edit, Upload, User, Warning} from "@element-plus/icons-vue";
import {myInfo} from "@/utils/profileUtils";

let searchText = ref("")
let flagOpenTip = 0
let flagCloseTip = 0
const deleteDialogVisible = ref(false)
const multipleSelection = ref([])
const addDialogVisible = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(20)
const findSalary = ref("错误等级")
const disableFlag = ref(true)
const addFormRef = ref()
const color = ref("var(--el-color-danger-light-9)")
const validateSalaryId = (value) => {
  const checkReg = /^(2000|200[0-9]|201[0-9]|202[0-9]|2030)$/ ;
  if(value === "" || value === null)
    return false;
  else return checkReg.test(value);
}
const addForm = reactive({
  positionName: '',
  salaryId: ''
})
const delelePosition = reactive({
  id: "",
  positionName: "",
  amount: "",
})
const canSelect = (row, index) => {
  return row.email !== myInfo.value.email;
}
let details = new reactive({
  detail: null
})
const handleSelectionChange = (val) => {
  multipleSelection.value = val.map(v => v.id)
}
let positionList = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const confirmDeleteOne = (id, positionName, amount) => {
  delelePosition.id = id
  delelePosition.departmentName = positionName
  delelePosition.amount = amount
  deleteDialogVisible.value = true
}
const getdata = () => {
  getWithData(`/api/position/selectPage/${positionList.current}/${positionList.size}`,
      details
      ,(data) => {
        totalPage.value = data.total
        positionList.data = data.records
        for(let i = 0; i < positionList.data.length; i ++) {
          positionList.data[i].status = false
        }
      }, () => {
        ElMessage.error("数据请求失败！")
      })
}

const deleteBatch = () => {
  const ids = multipleSelection.value
  delWithData('/api/position/deleteBatch',
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
const deleteOne = (id) => {
  del(`/api/position/delete/${id}`,
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
const getUpdatePositionVos = () => {
  const vos = []
  for (let i = 0; i < positionList.data.length; i ++){
    vos.push({
      positionName: positionList.data[i].positionName,
      amount: positionList.data[i].amount,
      id: positionList.data[i].id
    })
  }
  return vos;
}
const submitAllModified = () => {
  post("/api/position/updateBatch",
      getUpdatePositionVos(),
      () => {
        getdata()
        ElMessage.success("更新成功")
      }, () => {
        ElMessage.error("更新失败")
        getdata()
      })
}
const handleInputChange = () => {
  details.detail = searchText
  getdata()
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
const handleSizeChange = (val) => {
  positionList.size = val
  getdata()
}
const handleCurrentChange = (val) => {
  positionList.current = val
  getdata()
}
const handleAddPositionFindInput = (text) => {
  if(validateSalaryId(text)){
    get(`/api/position/selectSalaryById/${text}`, (data) => {
          findSalary.value = data.amount
          disableFlag.value = true;
          color.value = ""
        }, () => {
          findSalary.value = "错误等级"
          disableFlag.value = false;
          color.value = "var(--el-color-danger-light-9)"
        }
    )
  }
}
const submitAddForm = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      post('/api/position/addPosition', {...addForm},
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
  disableFlag.value = false
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
      <el-button type="primary" @click="evt => {addDialogVisible = true}" plain :icon="Upload">新增职位</el-button>
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

    <el-table :data="positionList.data"
              :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              @selection-change="handleSelectionChange"
              @cell-dblclick="editOpen"
              @current-change="editClose"
              highlight-current-row
              border>
      <el-table-column type="selection" width="55" align="center" :selectable="canSelect"></el-table-column>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="positionName" label="职位名" align="center">
        <template #default="{row}">
          <el-input v-if="row.status" v-model="row.positionName"></el-input>
          <span v-else>{{ row.positionName }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="amount" label="底薪情况" align="center" sortable>
        <template #default="{row}">
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180">
        <template v-slot="scope" #default="{row}">
          <el-button size="small" type="primary" plain @click="editOpen(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" plain :disabled="scope.row.status"
                     @click="confirmDeleteOne(scope.row.id, scope.row.positionName, scope.row.amount)">删除</el-button>
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
        <strong>职位ID:{{ delelePosition.id }}</strong>
        <br>
        <strong>职位名称: {{ delelePosition.positionName }}</strong>
        <br>
        <strong>薪资情况: {{ delelePosition.amount }}</strong>

      </span>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="warning" @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="deleteOne(delelePosition.id)">确定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog
        v-model="addDialogVisible"
        title="添加职位"
        align-center
        style="border-radius: 20px; display: flex; justify-items: center; width: 700px"
        draggable
    >
      <el-form :model="addForm" label-width="120px" style="margin-top: 20px"  ref="addFormRef">
        <el-form-item prop="positionName" label="职位名:">
          <el-input v-model="addForm.positionName" maxlength="30" placeholder="设置新职位名字" style="width: 260px">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="salaryId" label="设置薪资等级:">
          <el-input v-model="addForm.salaryId"
                    @input="handleAddPositionFindInput(addForm.salaryId)"
                    clearable
                    maxlength="30" placeholder="请设置新职位的底薪等级" style="width: 260px">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-descriptions
            :column="1"
            border
            style="margin-bottom: 10px"
        >
          <el-descriptions-item
              label-class-name="my-label"
              class-name="my-content">
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <user />
                </el-icon>
                对应薪资
              </div>
            </template>
            {{ findSalary }}
          </el-descriptions-item>
        </el-descriptions>
        <el-form-item>
          <el-button type="primary"
                     @click="submitAddForm"
                     :disabled = !disableFlag
                     style="width: 100px">确认</el-button>
          <el-button type="warning"
                     @click="() => {
            addDialogVisible = false;
            addFormRef.resetFields();
            disableFlag=false;
            findSalary = '错误等级'
            color = 'var(--el-color-danger-light-9)'
          }" style="width: 100px;">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<style scoped>
.dialog-footer button:first-child {
  margin-right: 10px;
}
:deep(.my-label) {
  background: var(--el-color-success-light-9) !important;
  width: 120px;
}
:deep(.my-content) {
  background: v-bind(color);
}
</style>