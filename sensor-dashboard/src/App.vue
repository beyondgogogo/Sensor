<template>
  <div id="app">
    <sensor-data-chart></sensor-data-chart>
  </div>
</template>

<script>
import SensorDataChart from './components/SensorDataChart.vue';

export default {
  name: 'App',
  components: {
    SensorDataChart
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  margin-top: 60px;
}
</style>
