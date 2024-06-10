<template>
  <div>
    <h2>Sensor Data Chart</h2>
    <div>
      <label for="sensorType">Sensor Type:</label>
      <select id="sensorType" v-model="selectedType">
        <option value="">All</option>
        <option value="TEMPERATURE">Temperature</option>
        <option value="HUMIDITY">Humidity</option>
        <option value="LIGHT_INTENSITY">Light Intensity</option>
      </select>

      <label for="startTime">Start Time:</label>
      <input type="datetime-local" id="startTime" v-model="startTime">

      <label for="endTime">End Time:</label>
      <input type="datetime-local" id="endTime" v-model="endTime">

      <button @click="fetchSensorData">Fetch Data</button>
    </div>
    <line-chart v-if="chartData.labels.length" :chart-data="chartData" :options="chartOptions"></line-chart>
    <p v-else>No data available</p>
  </div>
</template>

<script>
import axios from 'axios';
import { Line } from 'vue-chartjs';
import {
  Chart as ChartJS,
  
  registerables
} from 'chart.js';

ChartJS.register(...registerables);

export default {
  components: {
    LineChart: Line
  },
  data() {
    return {
      selectedType: 'TEMPERATURE',
      startTime: '',
      endTime: '',
      sensorData: [],
      chartData: {
        labels: [],
        datasets: [
          {
            label: 'Sensor Data',
            backgroundColor: '#f87979',
            borderColor: '#f87979',
            fill: false,
            data: []
          }
        ]
      },
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false
      }
    };
  },
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
        this.sensorData = response.data;
        this.updateChart();
      } catch (error) {
        console.error(error);
      }
    },
    updateChart() {
      const labels = this.sensorData.map(data => new Date(data.timestamp).toLocaleString());
      const values = this.sensorData.map(data => data.value);

      this.chartData.labels = labels;
      this.chartData.datasets[0].data = values;
    }
  }
};
</script>

<style scoped>
#sensorType, #startTime, #endTime {
  margin: 0 10px;
}
</style>
