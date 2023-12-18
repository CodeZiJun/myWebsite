<template>
  <div class="demo-collapse">
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="教学技巧培训" name="1">
        <div>
          提供教师们提升教学技巧和方法的培训资源，包括课程设计、学生互动、评估方法等方面的内容。
        </div>
        <div>
          为教职工提供个人和职业发展的指导和支持，包括培训计划、职业规划、继续教育等方面的资源。
        </div>
      </el-collapse-item>
      <el-collapse-item title="专业发展计划" name="2">
        <div>
          为教职工提供个人和职业发展的指导和支持，包括培训计划、职业规划、继续教育等方面的资源。
        </div>
        <div>
          为教职工提供个人和职业发展的指导和支持，包括培训计划、职业规划、继续教育等方面的资源。
        </div>
      </el-collapse-item>
      <el-collapse-item title="教育技术工具" name="3">
        <div>
          介绍使用教育技术工具的方法和指南，包括在线学习平台、教学软件、多媒体资源等方面的信息。
        </div>
        <div>
          介绍使用教育技术工具的方法和指南，包括在线学习平台、教学软件、多媒体资源等方面的信息。
        </div>
        <div>
          教师们分享和交流教学资源的平台，包括课件、教案、教学视频等方面的资源。
        </div>
      </el-collapse-item>
      <el-collapse-item title="教学资源分享" name="4">
        <div>
          教师们分享和交流教学资源的平台，包括课件、教案、教学视频等方面的资源。
        </div>
        <div>
          教师们分享和交流教学资源的平台，包括课件、教案、教学视频等方面的资源。
        </div>
      </el-collapse-item>
      <el-collapse-item title="教学评估指南" name="5">
        <div>
          提供教学评估的指导和工具，包括评估方法、问卷调查、学生反馈等方面的资源。
        </div>
        <div>
          提供教学评估的指导和工具，包括评估方法、问卷调查、学生反馈等方面的资源。
        </div>
      </el-collapse-item>
      <el-collapse-item title="校园活动组织" name="6">
        <div>
          支持教职工组织校园活动的资源，包括活动策划、场地预订、志愿者招募等方面的支持。
        </div>
        <div>
          支持教职工组织校园活动的资源，包括活动策划、场地预订、志愿者招募等方面的支持。
        </div>
      </el-collapse-item>
      <el-collapse-item title="教育研究论文" name="7">
        <div>
          收集和分享教育研究领域的论文和学术资料，包括教育心理学、教育政策、教育技术等方面的内容。
        </div>
        <div>
          收集和分享教育研究领域的论文和学术资料，包括教育心理学、教育政策、教育技术等方面的内容。
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'

const activeName = ref('1')
</script>
