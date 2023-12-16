<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="line"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="bar"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="10" style="margin: 10px 0">
      <el-col :span="12">
        <el-card>
          <div style="width: 100%; height: 400px" id="pie"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {ref, reactive, onMounted} from "vue";
import {get} from "@/net";
import {ElMessage} from "element-plus";


const option = {
  title: {
    text: '订单销售的趋势图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '人数',
      data: [],
      type: 'line',
      smooth: true
    },
  ]
}

const option1 = {
  title: {
    text: '用户角色占比柱状图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '角色',
      data: [],
      type: 'bar',
      smooth: true
    },
    {
      name: '数量',
      data: [],
      type: 'bar',
      smooth: true
    }
  ]
}

const option2 = {
  title: {
    text: '角色占比统计',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '人数',
      type: 'pie',
      center: ['50%', '60%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + ' (' + param.percent + '%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}

export default {
  name: "Charts",
  data() {
    return {}
  },
  mounted() {  // 等待页面的元素全部加载完成之后再初始化

    // 折线图
    let linetDom = document.getElementById('line');
    let lineChart = echarts.init(linetDom);
    lineChart.setOption(option)

    // 柱状图
    let barDom = document.getElementById('bar');
    let barChart = echarts.init(barDom);
    barChart.setOption(option1)

    // 饼图
    let pieDom = document.getElementById('pie');
    let pieChart = echarts.init(pieDom);
    pieChart.setOption(option2)


    get(`/api/account/charts`,
        (res) => {
          console.log(res)
          option.xAxis.data = res.line.map(v => v.role)
          option.series[0].data = res.line.map(v => v.value)
          lineChart.setOption(option)

          option1.xAxis.data = res.line.map(v => v.role)
          option1.series[1].data = res.line.map(v => v.value)
          barChart.setOption(option1)



          option2.series[0].data = res.line
          pieChart.setOption(option2)
          console.log(option.xAxis)
        }, () => {
          ElMessage.error("数据请求失败！")
        })
  },
  methods: {}
}
</script>

<style scoped>

</style>