<template>
  <div class="sensor-backGround">
    <div class="sensor-dashboard-header">
      <div v-for="(image, index) in backgroundImages" :key="index" class="falling-image"
        :style="{ backgroundImage: 'url(' + require(`../assets/${image.src}`) + ')', top: image.top + 'px' }"></div>

      <img src="../assets/农场.png" alt="Icon" class="dashboard-icon"> <!-- 添加图标 -->
      <h2 class="dashboard-title">Sensor-DashBoard</h2>
    </div>
    <div class="sensor-dashboard">
      <div>
        <label for="sensorType">传感器类型:</label>
        <select id="sensorType" v-model="selectedType">
          <option value="">全部</option>
          <option value="TEMPERATURE">温度</option>
          <option value="HUMIDITY">湿度</option>
          <option value="LIGHT_INTENSITY">光强度</option>
        </select>

        <label for="startTime">开始时间:</label>
        <input type="datetime-local" id="startTime" v-model="startTime">

        <label for="endTime">结束时间:</label>
        <input type="datetime-local" id="endTime" v-model="endTime">

        <button class="custom-button" @click="fetchSensorData">获取数据</button>
      </div>
      <line-chart v-if="chartData.labels.length" :chart-data="chartData" :options="chartOptions" class="line-chart-container"></line-chart>


      <div v-else>没有可用数据</div>

      <div class="pie-charts-container" v-if="chartData.labels.length">
        <div class="pie-chart-wrapper">
          <h3 class="pie-chart-title">温度饼图</h3>
          <pie-chart :chart-data="pieChartDataTemperature" :options="pieChartOptionsSmall"></pie-chart>
        </div>

        <div class="pie-chart-wrapper">
          <h3 class="pie-chart-title">湿度饼图</h3>
          <pie-chart :chart-data="pieChartDataHumidity" :options="pieChartOptionsSmall"></pie-chart>
        </div>

        <div class="pie-chart-wrapper">
          <h3 class="pie-chart-title">光强度饼图</h3>
          <pie-chart :chart-data="pieChartDataLightIntensity" :options="pieChartOptionsSmall"></pie-chart>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ArcElement, Chart as ChartJS, Legend, Tooltip, registerables } from 'chart.js';
import { Line, Pie } from 'vue-chartjs';

ChartJS.register(...registerables, ArcElement, Tooltip, Legend);

export default {
  components: {
    LineChart: Line,
    PieChart: Pie
  },
  data() {
    return {
      //   backgroundImages: [
      //   { src: 'image1.png', top: 0 },
      //   { src: 'image2.png', top: 0 },
      //   { src: 'image3.png', top: 0 }
      // ],
      selectedType: '',
      startTime: '',
      endTime: '',
      sensorData: [],
      chartData: {
        labels: [],
        datasets: [
          {
            label: '温度',
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1,
            fill: false,
            data: []
          },
          {
            label: '湿度',
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1,
            fill: false,
            data: []
          },
          {
            label: '光强度',
            backgroundColor: 'rgba(255, 206, 86, 0.2)',
            borderColor: 'rgba(255, 206, 86, 1)',
            borderWidth: 1,
            fill: false,
            data: []
          }
        ]
      },
      gaugeValue: 0,
      gaugeChart: null,
      pieChartDataTemperature: {
        labels: ['0-10', '10-20', '20-30', '30-40'],
        datasets: [
          {
            data: [0, 0, 0, 0],
            backgroundColor: ['#f87979', '#f8b979', '#79f879', '#7987f8']
          }
        ]
      },
      pieChartDataHumidity: {
        labels: ['0-30', '30-50', '50-70', '70-100'],
        datasets: [
          {
            data: [0, 0, 0, 0],
            backgroundColor: ['#f87979', '#f8b979', '#79f879', '#7987f8']
          }
        ]
      },
      pieChartDataLightIntensity: {
        labels: ['0-200', '200-500', '500-800', '800-1000'],
        datasets: [
          {
            data: [0, 0, 0, 0],
            backgroundColor: ['#f87979', '#f8b979', '#79f879', '#7987f8']
          }
        ]
      },
      pieChartOptions: {
        responsive: true,
        maintainAspectRatio: false
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        width: 400, // 设置折线图宽度
        height: 100 // 设置折线图高度，没用
      }
    };
  },
  //   mounted() {
  //   this.startFallingAnimation();
  // },
  methods: {
    async fetchSensorData() {
      try {
        const response = await axios.get('http://localhost:8080/api/sensor-data/type/range', {
          params: {
            type: this.selectedType,
            startTime: this.startTime ? new Date(this.startTime).toISOString() : null,
            endTime: this.endTime ? new Date(this.endTime).toISOString() : null
          }
        });
        console.log('Fetched Sensor Data:', response.data);
        this.sensorData = response.data;
        this.updateChart();
      } catch (error) {
        console.error(error);
      }
    },
    //   startFallingAnimation() {
    //   setInterval(() => {
    //     this.backgroundImages.forEach(image => {
    //       image.top += 50; // 调整掉落速度
    //       if (image.top > window.innerHeight) {
    //         image.top = -50; // 图片掉落到底部时重新回到顶部
    //       }
    //     });
    //   }, 100); // 调整掉落速度
    // },
    updateChart() {
      const temperatureData = [];
      const humidityData = [];
      const lightIntensityData = [];
      const labels = [];

      const typeCounts = {
        TEMPERATURE: 0,
        HUMIDITY: 0,
        LIGHT_INTENSITY: 0
      };

      this.sensorData.forEach(data => {
        labels.push(new Date(data.timestamp).toLocaleString());
        typeCounts[data.type]++;
      });



      this.sensorData.forEach(data => {
        if (data.type === 'TEMPERATURE') {
          temperatureData.push(data.value);
          humidityData.push(null);
          lightIntensityData.push(null);
        } else if (data.type === 'HUMIDITY') {
          temperatureData.push(null);
          humidityData.push(data.value);
          lightIntensityData.push(null);
        } else if (data.type === 'LIGHT_INTENSITY') {
          temperatureData.push(null);
          humidityData.push(null);
          lightIntensityData.push(data.value);
        }
      });

      this.chartData.labels = labels;
      this.chartData.datasets[0].data = temperatureData;
      this.chartData.datasets[1].data = humidityData;
      this.chartData.datasets[2].data = lightIntensityData;

      this.updatePieChart();
    },
    updatePieChart() {
      const temperatureData = this.sensorData.filter(data => data.type === 'TEMPERATURE').map(data => data.value);
      const humidityData = this.sensorData.filter(data => data.type === 'HUMIDITY').map(data => data.value);
      const lightIntensityData = this.sensorData.filter(data => data.type === 'LIGHT_INTENSITY').map(data => data.value);

      const temperatureRanges = [0, 10, 20, 30, 40];
      const humidityRanges = [0, 30, 50, 70, 100];
      const lightIntensityRanges = [0, 200, 500, 800, 1000];

      const temperaturePercentage = this.calculatePercentageInRanges(temperatureData, temperatureRanges);
      const humidityPercentage = this.calculatePercentageInRanges(humidityData, humidityRanges);
      const lightIntensityPercentage = this.calculatePercentageInRanges(lightIntensityData, lightIntensityRanges);

      // 更新温度饼状图数据
      this.pieChartDataTemperature.datasets[0].data = temperaturePercentage;

      // 更新湿度饼状图数据
      this.pieChartDataHumidity.datasets[0].data = humidityPercentage;

      // 更新光强度饼状图数据
      this.pieChartDataLightIntensity.datasets[0].data = lightIntensityPercentage;
    },


    calculatePercentageInRanges(data, ranges) {
      const counts = Array(ranges.length - 1).fill(0);

      data.forEach(value => {
        for (let i = 1; i < ranges.length; i++) {
          if (value >= ranges[i - 1] && value < ranges[i]) {
            counts[i - 1]++;
            break;
          }
        }
      });

      return counts.map(count => (count / data.length) * 100);
    }
  }
};
</script>
<style scoped>
#sensorType,
#startTime,
#endTime {
  margin: 0 10px;
}
.sensor-backGround {
  position: relative;
  /* 确保伪元素相对于这个元素定位 */
  height: 200vh;
  /* 设置高度为视窗的高度 */
}

.sensor-backGround::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('../assets/背景图7.png');
  background-size: cover;
  /* 背景图尺寸适应容器 */
  background-position: 100% 100%;
  /* 背景图位置右下角对齐，铺满整个容器 */
  opacity: 0.3;
  /* 设置背景图的透明度为 50% */
}


.sensor-dashboard {
  position: relative;
  /* 确保其他内容相对于 .sensor-backGround 定位 */
  max-width: 800px;
  margin: 0 auto;
}


.sensor-dashboard-header {
  position: relative;
  /* 确保其他内容相对于 .sensor-backGround 定位 */
  display: flex;
  justify-content: center;
  /* 水平居中 */
}
.line-chart-container {
  background-color: white; /* 设置背景颜色为白色 */
  border-radius: 20px; /* 添加圆角 */
}


.falling-image {
  position: absolute;
  width: 50px;
  /* 调整图片宽度 */
  height: 50px;
  /* 调整图片高度 */
  animation: fallingAnimation linear infinite;
}

@keyframes fallingAnimation {
  0% {
    transform: translateY(0);
  }

  100% {
    transform: translateY(100vh);
  }
}

.dashboard-icon {
  width: 35px;
  /* 调整图标宽度 */
  height: 35px;
  /* 调整图标高度 */
  margin-right: 10px;
  /* 调整图标与标题之间的间距 */
}

.dashboard-icon,
.dashboard-title {
  align-self: center;
  /* 垂直居中 */
}



h2,
h3 {
  margin-top: 20px;
}




.custom-button {
  background-color: #4CAF50;
  /* Green */
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
}

.custom-button:hover {
  background-color: #45a049;
}

.pie-charts-container {
  display: flex;
  justify-content: center;
  

}

.pie-chart-wrapper {
  margin: 0 10px;
  width: 300px;
  /* 保持相同宽度 */
  height: 300px;
  /* 保持相同高度 */
}

.pie-chart-title {
  font-size: 14px;
}

.pie-chart-options-small {
  width: 150px !important;
  height: 150px !important;
  font-size: 10px;
  /* 调整标签字体大小 */
}
</style>
