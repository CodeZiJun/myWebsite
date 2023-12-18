<script setup>
import {reactive, ref, onMounted} from "vue";
import {get, getWithData, post} from "@/net";
import {ElMessage} from "element-plus";
import {User} from "@element-plus/icons-vue";
import {getStorageInfoJson, myInfo} from "@/utils/profileUtils";

const tempEmail = ref("")
let show = ref(false)
let searchText = ref("")
const currentPage = ref(1)
const pageSize = ref(10)
const totalPage = ref(20)
const setDepDialogVisible = ref(false)
const setPosDialogVisible = ref(false)
const findDep = ref("错误编号")
const findPos = ref("错误编号")
const setDepFormRef = ref()
const setPosFormRef = ref()
const setDepDisableFlag = ref(true)
const setDepcolor = ref("var(--el-color-danger-light-9)")
let flag = ref(0)
const validatorDepartmentId = (rule, value, callback) => {
  if(value === ''){
    callback(new Error('请输入部门编号'))
  } else if (!/^(1000|1[0-9]{3}|2000)$/.test(value)) {
    callback(new Error('部门编号格式不正确'))
  } else {
    callback()
  }
}
const validatorPositionId = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请输入职位编号'))
  } else if (!/^([1-9]|[1-9][0-9])$/.test(value)) {
    callback(new Error('职位编号格式不正确'))
  } else {
    callback()
  }
}

const rule = {
  departmentId: [
    { validator: validatorDepartmentId, required: true, trigger: ['blur', 'change'] },
  ],
  positionId: [
    { validator: validatorPositionId, required: true, trigger: ['blur', 'change'] },
  ]
}
const setDepForm = reactive({
  departmentId: ''
})
const setPosForm = reactive({
  positionId: ''
})
let details = new reactive({
  detail: null
})
let commissionList = new reactive({
  current: 1,
  total: null,
  size: 10,
  data: []
})
const handleInputChange = () => {
  details.detail = searchText
  getdata()
}

const handleSizeChange = (val) => {
  commissionList.size = val
  getdata()
}
const handleCurrentChange = (val) => {
  commissionList.current = val
  getdata()
}
const validateDepartmentId = (value) => {
  const checkReg = /^(1000|1[0-9]{3}|2000)$/ ;
  if(value === "" || value === null)
    return false;
  else return checkReg.test(value);
}
const validatePositionId = (value) => {
  const checkReg = /^([1-9]|[1-9][0-9])$/ ;
  if(value === "" || value === null)
    return false;
  else return checkReg.test(value);
}
const getdata = () => {
  getWithData(`/api/commission/selectPage/${commissionList.current}/${commissionList.size}/${flag.value}`,
      details
      ,(data) => {
        totalPage.value = data.total
        commissionList.data = data.records
        for(let i = 0; i < commissionList.data.length; i ++) {
          commissionList.data[i].status = false
        }
      }, () => {
        ElMessage.error("数据请求失败！")
      })
}
const handleSetDepFindInput = (text) => {
  if(validateDepartmentId(text)){
    get(`/api/commission/selectDepartmentById/${text}`, (data) => {
          findDep.value = data.departmentName
          setDepDisableFlag.value = true;
          setDepcolor.value = ""
        }, () => {
          findDep.value = "错误编号"
          setDepDisableFlag.value = false;
          setDepcolor.value = "var(--el-color-danger-light-9)"
        }
    )
  }
}
const handleSetPosFindInput = (text) => {
  if(validatePositionId(text)){
    get(`/api/commission/selectPositionById/${text}`, (data) => {
          findPos.value = data.positionName
          setDepDisableFlag.value = true;
          setDepcolor.value = ""
        }, () => {
          findPos.value = "错误编号"
          setDepDisableFlag.value = false;
          setDepcolor.value = "var(--el-color-danger-light-9)"
        }
    )
  }
}
const submitSetDepForm = () => {
  setDepFormRef.value.validate((valid) => {
    if (valid) {
      get(`/api/archives/commissionDepartment/${tempEmail.value}/${setDepForm.departmentId}`,
          () => {
            getdata()
            ElMessage.success("任命成功")
          }, () => {
            getdata();
            ElMessage.error("任命失败")})
      setDepDialogVisible.value = false
      setDepFormRef.value.resetFields();
    } else {
      ElMessage.warning("请检查表单内容！")
    }
  })
  setDepDisableFlag.value = false
  findDep.value = "错误编号"
  setDepcolor.value = "var(--el-color-danger-light-9)"
}

const submitSetPosForm = () => {
  setPosFormRef.value.validate((valid) => {
    if (valid) {
      get(`/api/archives/commissionPosition/${tempEmail.value}/${setPosForm.positionId}`,
          () => {
            getdata()
            ElMessage.success("任命成功")
          }, () => {
            getdata();
            ElMessage.error("任命失败")})
      setPosDialogVisible.value = false
      setPosFormRef.value.resetFields();
    } else {
      ElMessage.warning("请检查表单内容！")
    }
  })
  setDepDisableFlag.value = false
  findPos.value = "错误编号"
  setDepcolor.value = "var(--el-color-danger-light-9)"
}
onMounted(
    () => {
      myInfo.value = getStorageInfoJson()
    }
)

const selectAll = () => {flag.value = 0; getdata()}
const selectNArc = () => {flag.value = 1; getdata();}
const selectNDepNPos = () => {flag.value = 2; getdata(); show.value =false}
const selectNDepYPos = () => {flag.value = 3; getdata(); show.value =false}
const selectYDepNPos = () => {flag.value = 4; getdata(); show.value =false}
const selectYDepYPos = () => {flag.value = 5; getdata(); show.value =false}
onMounted(() => {
      getdata()
})
</script>

<template>
  <div>
    <div style="margin-bottom: 10px">
      <el-input style="width: 200px" placeholder="查询名称" v-model="searchText" @input="handleInputChange"></el-input>
      <el-button type="info" style="margin-left: 10px" @click="() => {searchText = '';getdata()}">重置</el-button>
      <el-button type="success" style="margin-left: 10px" @click="selectAll">查询所有</el-button>
      <el-popover
          placement="top-start"
          title="Tips:"
          :width="200"
          trigger="hover"
          content="账户拥有个人档案才能对职位、部门进行操作哦"
      >
        <template #reference>
          <el-button type="danger" style="margin-left: 10px" plain @click="selectNArc">无档案账户</el-button>
        </template>
      </el-popover>
      <el-button v-if="!show" type="success" style="margin-left: 10px" plain @click="show = true">有档案账户</el-button>
      <span v-else>
        <el-button style="margin-left: 10px" type="warning" plain @click="selectNDepNPos">无部门无职位</el-button>
        <el-button style="margin-left: 10px" type="warning" plain @click="selectNDepYPos">无部门有职位</el-button>
        <el-button style="margin-left: 10px" type="primary" plain @click="selectYDepNPos">有部门无职位</el-button>
        <el-button style="margin-left: 10px" type="primary" plain @click="selectYDepYPos">有部门有职位</el-button>
      </span>
    </div>

    <el-table :data="commissionList.data"
              :header-cell-style="{ backgroundColor: 'aliceblue', color: '#666' }"
              highlight-current-row
              border>
      <el-table-column prop="id" label="序号" width="70" align="center"></el-table-column>
      <el-table-column prop="archivesId" label="档案编号" min-width="90" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" min-width="100" align="center"></el-table-column>
      <el-table-column prop="realName" label="真实姓名" min-width="100" align="center"></el-table-column>
      <el-table-column prop="email" label="用户邮箱" min-width="220" align="center"></el-table-column>
      <el-table-column prop="departmentName" label="所属部门" align="center"></el-table-column>
      <el-table-column prop="positionName" label="所居职位" align="center"></el-table-column>
      <el-table-column prop="entryDate" label="入职时间" min-width="160" align="center"></el-table-column>
      <el-table-column label="设置" align="center" width="150">
        <template v-slot="scope" #default="{row}">
          <el-button size="small" type="primary" :disabled="scope.row.archivesId === null" @click="() => {tempEmail = scope.row.email;setDepDialogVisible=true}" plain>部门</el-button>
          <el-button size="small" type="warning" :disabled="scope.row.archivesId === null" @click="() => {tempEmail = scope.row.email;setPosDialogVisible=true}" plain>职位</el-button>
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
        v-model="setDepDialogVisible"
        title="任命部门"
        align-center
        style="border-radius: 20px; display: flex; justify-items: center; width: 700px"
        draggable
    >
      <el-form :model="setDepForm" label-width="120px" style="margin-top: 20px" :rules="rule" ref="setDepFormRef">
        <el-form-item prop="departmentId" label="设置部门:">
          <el-input v-model="setDepForm.departmentId"
                    @input="handleSetDepFindInput(setDepForm.departmentId)"
                    clearable
                    maxlength="30" placeholder="请输入部门编号" style="width: 260px">
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
                对应部门
              </div>
            </template>
            {{ findDep }}
          </el-descriptions-item>
        </el-descriptions>
        <el-form-item>
          <el-button type="primary"
                     @click="submitSetDepForm"
                     :disabled = !setDepDisableFlag
                     style="width: 100px">确认</el-button>
          <el-button type="warning"
                     @click="() => {
            setDepDialogVisible = false;
            setDepFormRef.resetFields();
            setDepDisableFlag=false;
            findDep = '错误编号'
            setDepcolor = 'var(--el-color-danger-light-9)'
          }" style="width: 100px;">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog
        v-model="setPosDialogVisible"
        title="任命职位"
        align-center
        style="border-radius: 20px; display: flex; justify-items: center; width: 700px"
        draggable
    >
      <el-form :model="setPosForm" label-width="120px" style="margin-top: 20px" :rules="rule" ref="setPosFormRef">
        <el-form-item prop="positionId" label="设置职位:">
          <el-input v-model="setPosForm.positionId"
                    @input="handleSetPosFindInput(setPosForm.positionId)"
                    clearable
                    maxlength="30" placeholder="请输入职位编号" style="width: 260px">
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
                对应职位
              </div>
            </template>
            {{ findPos }}
          </el-descriptions-item>
        </el-descriptions>
        <el-form-item>
          <el-button type="primary"
                     @click="submitSetPosForm"
                     :disabled = !setDepDisableFlag
                     style="width: 100px">确认</el-button>
          <el-button type="warning"
                     @click="() => {
            setPosDialogVisible = false;
            setPosFormRef.resetFields();
            setDepDisableFlag=false;
            findPos = '错误编号'
            setDepcolor = 'var(--el-color-danger-light-9)'
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
  background: v-bind(setDepcolor);
}
</style>