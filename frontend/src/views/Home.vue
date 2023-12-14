
<script >
import stepImg1 from '@/assets/1.png'
import stepImg2 from '@/assets/2.png';
import stepImg3 from '@/assets/3.png';
import stepImg4 from '@/assets/4.png';
import stepImg5 from '@/assets/5.png';
import stepImg6 from '@/assets/6.png';
import stepImg7 from '@/assets/7.png';
import stepImg8 from '@/assets/8.png';

import jayjaylu from "@/assets/programmer1.png"
import codezj from "@/assets/programmer2.png"
import windleaf from "@/assets/programmer3.png"

export default {
  name: "Home",
  data:function (){

return {
  contentList: [
    {
      id: '1',
      img: stepImg1,
      text:"人事信息查询"
    },
    {
      id: '2',
      img: stepImg2,
      text:"人事信息管理"
    },
    {
      id: '3',
      img: stepImg3,
      text:"多角色系统"
    },
    {
      id: '4',
      img: stepImg4,
      text:"招聘信息"
    },
    {
      id: '5',
      img: stepImg5,
      text:"多文件上传"
    },
    {
      id: '6',
      img: stepImg6,
      text:"信息维护"
    },
    {
      id: '7',
      img: stepImg7,
      text:"数据可视化"
    },
    {
      id: '8',
      img: stepImg8,
      text:"更多功能开发中。。。"
    },
  ],

  coders: [
    {
      id: '1',
      img: windleaf,
      text:"windleaf 庄洋"
    },
    {
      id: '2',
      img: codezj,
      text:"codezj 赖紫骏"
    },
    {
      id: '3',
      img: jayjaylu,
      text:"jayjaylu 陆子杰"
    },

  ],

}

  }
};
</script>

<template>
  <div :style="{ paddingTop: '56px'}">
    <header class="bg-primary py-3 shadow-sm fixed-navbar">
      <div class="container d-flex justify-content-between">
        <h3 class="mb-0 text-white">人事管理系统</h3>
        <nav>
          <ul class="nav">
            <li class="nav-item">
              <a class="nav-link text-white" href="">进入系统</a>
            </li>
          </ul>
        </nav>
      </div>
    </header>

    <div class="container">
      <div id="carouselExample" class="carousel slide my-5" data-bs-ride="carousel">
        <div class="carousel-inner">
          <div class="carousel-item active">
            <img src="../assets/工作%20.png" class="d-block w-100" alt="Image 1" />
            <div class="carousel-caption d-none d-md-block">
              <h5>Image 1</h5>
              <p>Image 1 description.</p>
            </div>
          </div>
          <div class="carousel-item">
            <img src="../assets/分析%20.png" class="d-block w-100" alt="Image 2" />
            <div class="carousel-caption d-none d-md-block">
              <h5>Image 2</h5>
              <p>Image 2 description.</p>
            </div>
          </div>
          <div class="carousel-item">
            <img src="../assets/解说%20.png" class="d-block w-100" alt="Image 3" />
            <div class="carousel-caption d-none d-md-block">
              <h5>Image 3</h5>
              <p>Image 3 description.</p>
            </div>
          </div>
        </div>
        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Previous</span>
        </button>
        <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="visually-hidden">Next</span>
        </button>
      </div>
    </div>
    </div>
  <section class="my-5 section">
    <div class="container">
      <h2 class="text-center mb-4">我们提供的服务</h2>
      <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
        <div class="col" v-for="(item,index) in contentList " :key="index">
          <div class="card h-100 shadow-sm">
            <img
                :src="item.img"
                class="card-img-top"
                alt="瀑布流图片"
            />

            <div class="card-body">

              <p class="card-text">{{item.text}}</p>
            </div>
          </div>

        </div>
      </div>
    </div>
  </section>
  <section class="bg-light py-5 section section-alternate contributors-section">
      <div class="container">
        <h2 class="text-center mb-4">Contributors</h2>
        <div class="row">
          <div class="col-md-4 mb-4 text-center" v-for="(item,index) in coders " :key="n">
            <div class="p-4 shadow-sm rounded h-100 contributor-card">
              <i class="bi bi-star-fill fs-1 text-primary"></i>
              <h4 class="mt-3">Feature {{ n }}</h4> <img
                :src="item.img"
                class="card-img-top"
                alt="瀑布流图片"
            />
              <p>{{item.text}}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <footer class="bg-dark text-white text-center py-3 mt-4">
      <div class="container">
        <p class="mb-0">&copy; 2023 Introduction Page of the Management Sysytem</p>
        <small> based on Java Springboot and Vue</small>
      </div>
    </footer>

</template>


<style >
.card-img-top {
  /* 配置初始状态 */
  transition: transform 0.3s;
}

.card:hover .card-img-top {
  /* 鼠标悬停时的状态 */
  transform: scale(1.1);
}
.card {
  /* 配置初始状态 */
  transition: transform 0.3s, box-shadow 0.3s;
}

.card:hover {
  /* 鼠标悬停时的状态 */
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);
}

.fixed-navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1030;
  width: 100%;
}

.section {
  padding: 2rem 0;
}

.section-alternate {
  background-color: #f8f9fa;
}

.contributors-section {
  background-image: linear-gradient(to bottom right, #69a6e0, #e9ecef);
}

.contributor-card {
  transition: transform 0.3s;
}

.contributor-card:hover {
  transform: scale(1.05);
}



body {
  background-image: linear-gradient(to bottom, #85b7f1, #6c757d);
  min-height: 100vh;
  background-attachment: fixed;
}
footer{
  background-image: linear-gradient(to bottom, #a2c4e8, #6c757d);
  min-height: 100vh;
  background-attachment: fixed;

}
</style>