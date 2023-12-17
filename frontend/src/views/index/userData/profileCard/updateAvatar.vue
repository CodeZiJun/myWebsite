<script setup>
import{ VueCropper } from 'vue-cropper'
import 'vue-cropper/dist/index.css'
import {ref} from "vue";
import {accountInfoItemName, post} from "@/net";
import {ElMessage} from "element-plus";
import {avatarRef} from '@/utils/profileUtils'
import {getAavater} from '@/utils/profileUtils';


const cropper = ref()
const inputRef = ref()

const loading = ref(false)
const isReady = ref(false)

// vue-cropper的基本配置
const option = ref({
  img: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', //裁剪图片的地址
  outputSize: 1, //裁剪生成图片的质量(可选0.1 - 1)
  outputType: 'jpeg', //裁剪生成图片的格式（jpeg || png || webp）
  info: true, //图片大小信息
  canScale: true, //图片是否允许滚轮缩放
  autoCrop: true, //是否默认生成截图框
  autoCropWidth: 150, //默认生成截图框宽度
  autoCropHeight: 150, //默认生成截图框高度
  fixed: true, //是否开启截图框宽高固定比例
  fixedNumber: [1, 1], //截图框的宽高比例
  full: false, //false按原比例裁切图片，不失真
  fixedBox: false, //固定截图框大小，不允许改变
  canMove: false, //上传图片是否可以移动
  canMoveBox: true, //截图框能否拖动
  original: false, //上传图片按照原始比例渲染
  centerBox: false, //截图框是否被限制在图片里面
  height: true, //是否按照设备的dpr 输出等比例图片
  infoTrue: false, //true为展示真实输出图片宽高，false展示看到的截图框宽高
  maxImgSize: 3000, //限制图片最大宽度和高度
  enlarge: 1, //图片根据截图框输出比例倍数
  mode: '300px 300px' //图片默认渲染方式
})
const previewURL = ref('')
let fileName = ''
// 实时的刷新
const realTime = () => {
  if (loading.value) return
  loading.value = true
  // 节流
  setTimeout(() => {
    if(cropper != null)
    cropper.value.getCropBlob((data) => {
      previewURL.value = URL.createObjectURL(data)
      loading.value = false
    })
  }, 150)
}
// 初始化
const imgLoad = (msg) => {
  if (msg === 'success') {
    isReady.value = true
  }
}
//选择图片
const selectImg = (e) => {
  const eTarget = e.target
  if (!eTarget.files) return
  const file = eTarget.files[0]
  if (!/\.(jpg|jpeg|png|JPG|PNG)$/.test(eTarget.value)) {
    alert('图片格式错误')
    return false
  }
  fileName = file.name;
  const reader = new FileReader()
  reader.readAsDataURL(file) //将文件转化为base64
  reader.onload = e => {
    let data
    if (!e.target) return
    if (typeof e.target.result === 'object') {
      data = URL.createObjectURL(new Blob([e.target.result ?? '']))
    } else {
      data = e.target.result
    }
    option.value.img = data
  }
}
//图片缩放
const changeScale = (num) => {
  num = num || 1
  cropper.value.changeScale(num)
}
//向左旋转
const rotateLeft = () => {
  cropper.value.rotateLeft()
}
//向右旋转
const rotateRight = () => {
  cropper.value.rotateRight()
}
//上传图片
function uploadImage() {
  //获取截图的blob数据
  cropper.value.getCropBlob(async (data) => {
    let formData = new FormData();
    let file = new File([data],fileName + '.jpeg',{type: "text/plain;charset=utf-8"});
    formData.append("avatar", file);
    post("/api/account/updateAvatar", formData, (data) => {
      ElMessage.success("上传成功！");
      let flag = localStorage.getItem(accountInfoItemName) ? 1 : 0
      let temp = JSON.parse(flag ? localStorage.getItem(accountInfoItemName) : sessionStorage.getItem(accountInfoItemName))
      temp.avatar = data.avatar
      flag ? localStorage.setItem(accountInfoItemName, JSON.stringify(temp)) : sessionStorage.setItem(accountInfoItemName, JSON.stringify(temp))
      avatarRef.value = getAavater()
    }, () => {
      ElMessage.error("上传失败！")
    })
  })
}
//选择图片
const selectClick = () => {
  inputRef.value?.click()
}


</script>

<template>
  <el-row :gutter="20" style="margin-top: 15px">
    <el-col :span="14">
      <div class="cropperView">
        <VueCropper
            ref="cropper"
            :img="option.img"
            :outputSize="option.outputSize"
            :outputType="option.outputType"
            :info="option.info"
            :canScale="option.canScale"
            :autoCrop="option.autoCrop"
            :autoCropWidth="option.autoCropWidth"
            :autoCropHeight="option.autoCropHeight"
            :fixed="option.fixed"
            :fixedNumber="option.fixedNumber"
            :full="option.full"
            :fixedBox="option.fixedBox"
            :canMove="option.canMove"
            :canMoveBox="option.canMoveBox"
            :original="option.original"
            :centerBox="option.centerBox"
            :height="option.height"
            :infoTrue="option.infoTrue"
            :maxImgSize="option.maxImgSize"
            :enlarge="option.enlarge"
            :mode="option.mode"
            @realTime="realTime"
            @imgLoad="imgLoad"
        ></VueCropper>
      </div>
      <div style="display: flex; justify-content: center; margin-top: 10px;">
        <el-button style="width: 100%;" type="primary" @click="selectClick" plain>选择图片</el-button>
        <input
            ref="inputRef"
            type="file"
            style="position: absolute; clip: rect(0 0 0 0)"
            accept="image/png, image/jpeg, image/gif, image/jpg"
            @change="selectImg"
        />
      </div>
    </el-col>

    <el-col :span="10">
      <div>
        <div v-if="isReady">
          <el-image class="previewView-image" :src="previewURL" />
        </div>
        <div>
          <div style="margin-bottom: 10px; display: flex; justify-content: center; align-items: center">
            <el-button style="width: 90px" type="primary" @click="changeScale(1)">放大</el-button>
            <el-button style="width: 90px" type="primary" @click="changeScale(-1)">缩小</el-button>
          </div>
          <div style="display: flex; justify-content: center; align-items: center">
            <el-button style="width: 90px" type="primary" @click="rotateLeft">↺ 左旋转</el-button>
            <el-button style="width: 90px" type="primary" @click="rotateRight">↻ 右旋转</el-button>
          </div>
          <div style="display: flex; justify-content: center; align-items: center">
            <el-button style="width: 80%; margin-top: 10px" type="warning"  @click="uploadImage">上传图片</el-button>
          </div>

        </div>
      </div>
    </el-col>
  </el-row>

</template>
<style scoped>
.cropperView {
  display: flex;
  justify-content: center;
  align-content: center;
  width: 100%;
  height: 300px;
}
.previewView-image {
  border-radius: 50%;
  width: 100%;
  display: flex;
  margin-bottom: 10px;
}

</style>