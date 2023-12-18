<script setup>
import {accountInfoItemName, logout} from "@/net";
import router from "@/router";
import {avatarRef, getStorageInfoJson, myInfo} from '@/utils/profileUtils'
import {
  ArrowRight,
  Avatar, ChatLineSquare, CreditCard,
  Fold, FullScreen,
  Grid, Guide,
  Histogram, HomeFilled,
  List, Management,
  Memo,
  MessageBox, Money, Postcard, Printer, Suitcase,
  Tickets
} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";
import {getDescriptions, getNames} from "@/utils/routeUtils";
import {ref, onMounted} from "vue";
import screenfull from "screenfull";

const route = useRoute()
let descriptions = ref()
let routeNames = ref()
let isCollapse = ref(false);
let asideWidth = ref('200px');

// const accountName = JSON.parse(
//     (sessionStorage.getItem(accountInfoItemName) ? sessionStorage.getItem(accountInfoItemName)
//         : localStorage.getItem(accountInfoItemName))).username
//获取面包屑数据
routeNames.value = getNames(route.name, router.getRoutes())
descriptions.value = getDescriptions(routeNames.value, router.getRoutes())
function userLogout() {
  sessionStorage.getItem(accountInfoItemName) ? sessionStorage.removeItem(accountInfoItemName) :
                                                localStorage.removeItem(accountInfoItemName)
  logout(() => router.push('/'))
}

//在每次路由跳转后，更新面包屑的数据
router.afterEach((to, from) => {
  //不知道为什么，进入页面时，会有两次路由跳转，route.name为空
  if(!route.name){
    console.log(route.name, route.meta)
    return
  }
  routeNames.value = getNames(route.name, router.getRoutes())
  descriptions.value = getDescriptions(routeNames.value, router.getRoutes())
})
onMounted(
    () => {
      myInfo.value = getStorageInfoJson()
    }
)
</script>

<template>
  <el-container style="user-select: none">
    <el-aside :width="asideWidth" style="min-height: 100vh; background-color: #001529">
        <div style="height: 60px; color: white; display: flex; align-items: center; justify-content: center;">
          <img src="@/assets/indexIcon.png" alt="加载失败" style="width: 40px; height: 40px"/>
          <span class="index-logo-title" v-show="!isCollapse">Manager</span>
        </div>
        <el-menu
            :default-openeds="['service']"
            :collapse="isCollapse"
            :collapse-transition="false"
            background-color="#001529"
            text-color="rgba(255, 255, 255, 0.65)"
            active-text-color="#fff"
            style="border: none"
            router>
          <el-sub-menu index="/index/service">
            <template #title>
              <el-icon><Grid /></el-icon>
              <span>办事大厅</span>
            </template>
            <el-menu-item-group title="文件打印">
              <el-menu-item index="/index/service/letter">
                <template #title>
                  <el-icon><Printer /></el-icon>
                  <span>电子调档函</span>
                </template>
              </el-menu-item>
              <el-menu-item index="/index/service/proof">
                <template #title>
                  <el-icon><Printer /></el-icon>
                  <span>教职工证明</span>
                </template>
              </el-menu-item>
            </el-menu-item-group>

            <el-menu-item-group title="报表下载">
              <el-menu-item index="/index/service/advanced">
                <template #title>
                  <el-icon><Tickets /></el-icon>
                  <span>高基统计</span>
                </template>
              </el-menu-item>
              <el-menu-item index="/index/service/talent">
                  <template #title>
                    <el-icon><Memo /></el-icon>
                    <span>人才资源统计</span>
                  </template>
              </el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>

          <el-sub-menu index="/index/information">
            <template #title>
              <el-icon><MessageBox /></el-icon>
              <span>详细信息</span>
            </template>
            <el-menu-item-group title="个人信息">
              <el-menu-item index="/index/information/self">
                <template #title>
                  <el-icon><Avatar /></el-icon>
                  <span >职工个人信息</span>
                </template>
              </el-menu-item>
            </el-menu-item-group>

            <el-menu-item-group title="师资信息">
              <el-menu-item index="/index/information/staffResource">
                <el-icon><List /></el-icon>
                <span>职工资源目录</span>
              </el-menu-item>
              <el-menu-item index="/index/information/chartsView">
                <el-icon><Histogram /></el-icon>
                <span>图表可视化</span>
              </el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>

          <el-sub-menu index="/index/salary">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>薪资查询</span>
            </template>
            <el-menu-item index="/index/salary/mySalary">
              <el-icon><CreditCard /></el-icon>
              <span>我的薪资</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/index/recruitment">
            <template #title>
              <el-icon><Suitcase /></el-icon>
              <span>招聘资讯</span>
            </template>
            <el-menu-item index="/index/recruitment/recruit-message">
              <el-icon><ChatLineSquare /></el-icon>
              <span>招聘公告</span>
            </el-menu-item>
            <el-menu-item index="/index/recruitment/recruit-application">
              <el-icon><Postcard/></el-icon>
              <span>招聘申请</span>
            </el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="/admin">
            <template #title>
              <el-icon><Management /></el-icon>
              <span>超级管理</span>
            </template>
            <el-menu-item index="/admin/staff">
              <template #title>
                <el-icon><Avatar /></el-icon>
                <span>账户管理</span>
              </template>
            </el-menu-item>
            <el-menu-item index="/admin/department">
              <template #title>
                <el-icon><HomeFilled /></el-icon>
                <span>部门管理</span>
              </template>
            </el-menu-item>
            <el-menu-item index="/admin/position">
              <el-icon><Guide /></el-icon>
              <span>职位管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/commission">
              <el-icon><Management /></el-icon>
              <span>任命管理</span>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
    </el-aside>

    <el-container style="user-select: none">
      <el-header>
        <el-icon @click="() => {isCollapse = !isCollapse; asideWidth = isCollapse? '64px':'200px'}"
                 style="font-size: 25px"><Fold /></el-icon>
        <div style="margin-top: -2px">
          <el-breadcrumb :separator-icon="ArrowRight" style="margin-left: 20px; font-size: 15px">
            <el-breadcrumb-item
                v-for="(description, index) in descriptions"
                :key="description"
                :to="{ name: routeNames[index] }">
              {{ description }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div style="position: absolute; right: 20px">
          <el-icon style="font-size: 25px; margin-right: 8px; margin-top: 8px"
                   @click="screenfull.toggle()"><FullScreen /></el-icon>
          <el-dropdown placement="bottom">
            <div style="display: flex; align-items: center; cursor: default">
              <img :src=avatarRef
                   alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: 0 5px" />
              <span style="margin-right: 10px">{{ myInfo.username }}</span>
              <el-tag v-if="myInfo.role === 'admin'" type="warning" effect="dark">{{ myInfo.role }}</el-tag>
              <el-tag v-else type="success" effect="dark">{{ myInfo.role }}</el-tag>
            </div>
            <template #dropdown>
              <el-dropdown-menu slot="dropdown" style="user-select: none">
                <el-dropdown-item @click.native="$router.push('/index/profile')">我的资料</el-dropdown-item>
                <el-dropdown-item @click="userLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view v-slot="{ Component }">
          <transition name="el-fade-in-linear" mode="out-in">
            <div :key="$route.path">
              <component :is="Component" />
            </div>
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.index-logo-title {
  margin-left: 5px;
  font-size: 20px;
  transition: all .3s;
}
.el-menu-item,
.el-menu--inline {
  background-color: #000c17;
  padding-left: 49px;
}
.el-menu-item-group {
  background-color: #000c17;
}

.el-menu-item:hover,
.el-sub-menu__title:hover i{
  color: #fff
}
.el-menu-item.is-active i,
.el-menu-item.is-active{
  margin-left: -4px;
}
.el-menu-item.is-active{
  background-color: #1890ff;
  border-radius: 5px;
  width: calc(100% - 8px);
  margin-left: 4px;
}
.el-menu-item,
.el-sub-menu-title {
  height: 40px;
  line-height: 40px;
}
.el-menu-item,
.el-sub-menu {
  min-width: 0;
}
.el-menu--inline,
.el-menu-item.is-active {
  padding-left: 45px;
}
.el-aside {
  transition: width .3s;
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
}
.el-header {
  box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
  display: flex;
  align-items: center;
}


</style>