<template>
    <div class="container">
      <!-- 中间部分 -->
      <div class="middle">
        <h2 class="title">一辨真假</h2>
        <!-- 文本输入区域 -->
        <textarea class="area" v-model="inputText" cols="70" rows="10"></textarea>
        <br>
        <!-- 开始按钮 -->
        <el-button @click="start"  class="begin" type="primary">开始</el-button>
        <!-- <button @click="start" class="begin" >开始</button> -->
      </div>
  
      <!-- 右侧部分 -->
      <div class="right">
        <!-- 我的上传记录 -->
        <div class="history">我的上传记录</div>
        <div>
          <!-- 照片 -->
          <button>照片</button>
          <!-- <div class="photo">照片</div> -->
          <!-- 文字 -->
          <button>文字</button>
          <!-- <div class="write">文字</div> -->
          <div class="detail"></div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        inputText: '', // 存储文本输入区域的文本内容
      };
    },
    methods: {
      start() {
        // 在点击“开始”按钮时执行的方法
        this.$router.push('/result')
        
      }
    }
  };
  </script>
  
  <style scoped>
    /* 样式表 */
    .container {
        width: 100%;
        height: calc(100vh - 284px);
        background-color: rgb(240, 240, 249);
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-radius: 4%;
    }
  
    .middle {
        width: 100%;
        height: 100%;
        text-align:center;
    }

    .title {
        margin-top: 60px;
        margin-bottom: 60px;
    }
    
    .area {
      resize: none;
      height: 30vh; /* 将高度设置为屏幕高度的50% */
      width: 60%;
      border-radius: 4%;
      justify-content: space-between;
      align-items: center;
    }
  
    .right {
      background-color: #348ed2;
      float: right;
      position: fixed;
      right:4%;
    }

    .begin {
        margin-top: 90px;
    }

    .detail {
        height: 500px;
        width: 160px;
        background-color: red;
    }
  </style>