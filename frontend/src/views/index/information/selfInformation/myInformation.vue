<script setup>

import {Iphone, Location, OfficeBuilding, RefreshLeft, Ticket, Tickets, User} from "@element-plus/icons-vue";
</script>

<template>
  <el-descriptions
    title="职工个人信息"
    :column="3"
    :size="'large'"
    border
  >
    <template #extra>
      <el-button type="primary" plain >
        <template #icon>
          <el-icon><RefreshLeft /></el-icon>
        </template>
        更新
      </el-button>
    </template>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <user />
          </el-icon>
          真实姓名
        </div>
      </template>
        codezijun
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <iphone />
          </el-icon>
          联系电话
        </div>
      </template>
      18859990876
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <location />
          </el-icon>
          籍贯
        </div>
      </template>
      南京
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <tickets />
          </el-icon>
          身份
        </div>
      </template>
      <el-tag size="small">特聘教师</el-tag>
    </el-descriptions-item>
    <el-descriptions-item>
      <template #label>
        <div class="cell-item">
          <el-icon>
            <office-building />
          </el-icon>
          详细住址
        </div>
      </template>
      江苏省南京市浦口区南京工业大学
    </el-descriptions-item>
  </el-descriptions>
</template>

<style scoped>

</style>