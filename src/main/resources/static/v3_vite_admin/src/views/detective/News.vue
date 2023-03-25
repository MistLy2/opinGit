<template>
    <div class="centainer">
      <!-- 这里是模板内容 -->
      <div class="middle">

        <div class="top">
          <div class="left">
            <el-button type="primary" @click="$event =>$router.push('/upload')">舆情上传</el-button>
          </div>
          <div class="right">
            <el-button type="primary" @click="$event =>$router.push('/record')">上传记录</el-button>
          </div> 
        </div>
        <div class="mid">
          <div class="first">
              <div class="f1">
                  <div class="one">
                      <h3>上海警方通报王某某等大人被行政处罚 权威人士：打人者王某某系王思聪？</h3>
                  </div>
                  <div class="love">
                    300+爱心
                  </div>
                  <div class="people">
                      发布人：张三  
                  </div>
                  <div class="time">
                      发布时间：2022-10-16 15：22
                  </div>
              </div>
              <div class="photo">
                    照片1
              </div>
            </div>  
          <div class="second">
              <div class="f1">
                  <div class="one">
                      <h3>上海警方通报王某某等大人被行政处罚 权威人士：打人者王某某系王思聪？</h3>
                  </div>
                  <div class="love">
                    300+爱心
                  </div>
                  <div class="people">
                      发布人：张三  
                  </div>
                  <div class="time">
                      发布时间：2022-10-16 15：22
                  </div>
                </div>
              <div class="photo">
                  照片2
              </div>
          </div>
      </div>


      </div>

    </div>
  </template>
  
  <script>
  export default {
    // name: 'News', // 组件名称
    data() {
      return {
        // 数据
      }
    },
    methods: {
      // 方法
    },
    mounted() {
      // 挂载后执行的代码
    }
  }
  </script>
  
  <style scoped>
    .centainer {
        width: 100%;
        height: calc(100vh - 284px);
        background-color: rgb(240, 240, 249);
        /* display: flex; */
        justify-content: space-between;
        align-items: center;
        border-radius: 4%;
    }

    .top {
      /* float: left; */
      width: 100%;
      /* background-color: blueviolet; */
      /* margin-right: 50px; */
      display: flex; /* 或者 inline-flex */
      /* justify-content: center; */
    }


    .left {
      margin: 50px 320px;
    }

    .right {
      margin: 50px 10px;
    } 

    .mid {
        border: 1px solid black;
        width: 850px; /* 设置div宽度 */
        height: 330px;
        /* margin: 200px 20px 100px 20px; */
        /* border-top: 300px; */
        margin: 10px  200px;
    }

    .first {
      border-bottom: 1px solid black;
      height: 165px;
    }

    .f1 {
       width: 80%; 
        background-color: blueviolet;
        float:left;
        height: 165px;
        /* margin-top: 5px; */
    }

    .photo {
      width: 20%;
      background-color: rgba(153, 24, 227, 0.587) ;
      float:right;
      height: 165px;
    }

    .one {
      margin-top: 10px;
      margin-left: 10px;
    }

    .love {
      position: relative;
      top: 72px;
      left: 580px;
    }

    .people{
      position: relative;
      top: 75px;
      left: 20px;
      /* width: 100px; */
      display: inline-block;
    }

    .time {
      position: relative;
      top: 75px;
      left: 90px;
      display: inline-block;
    }


    .middle {
      position: relative;
      right: 160px;
        width: 100%;
        height: 100%;
    }

  </style>