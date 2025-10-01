<template>
  <div class="monthly-report-users-chart">
    <Bar :data="data" :options="options" />
  </div>
</template>

<!-- eslint-disable id-length security/detect-object-injection-->
<script setup>
import { Bar } from "vue-chartjs";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";
import { httpClient } from "@/utils/http-common";
import { UI_DASHBOARD_MONTHLY_USERS } from "@/api/prod/path";
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  ChartDataLabels
);

const dataRef = ref([]);
const colors = ["#1CBDB3", "#92dfdb", "#c8efed"];
const data = computed(() => ({
  labels: [1, 2, 3, 4, 5, 6],
  datasets: dataRef.value,
}));

const options = {
  responsive: true,
  maintainAspectRatio: false,
  indexAxis: "x",
  scales: {
    x: {
      ticks: {
        display: true,
        font: {
          size: 11,
          family: "Noto Sans KR",
        },
      },
      grid: {
        display: false,
        color: (context) => (context.index === 0 ? "transparent" : "#F0F2F5"),
      },
      border: {
        display: false,
      },
    },
    y: {
      ticks: {
        padding: 2,
        font: {
          size: 11,
          family: "Noto Sans KR",
        },
        color: "#6B6D70",
        stepSize: 20,
      },
      grid: {
        display: true,
        color: "#F0F2F5",
      },
    },
  },
  plugins: {
    legend: {
      display: true,
      position: "bottom",
      labels: {
        usePointStyle: true,
        boxWidth: 8,
        boxHeight: 8,
        padding: 20,
        font: {
          size: 11,
          weight: 500,
          family: "Noto Sans KR",
        },
      },
    },
    tooltip: {
      enabled: false,
    },
    datalabels: {
      display: false,
    },
  },
};

const fetchData = async () => {
  try {
    const response = await httpClient.get(UI_DASHBOARD_MONTHLY_USERS);
    const itemKeys = Object.keys(response.data);

    dataRef.value =
      itemKeys.map((key, index) => ({
        label: key,
        backgroundColor: colors[index],
        data: response.data[key]
          .sort((a, b) => a.yearMonth - b.yearMonth)
          .map((item) => item.offerCnt),
        barThickness: 12,
        borderRadius: 4,
        borderWidth: 2,
        borderColor: "transparent",
      })) || [];
  } catch {}
};

onMounted(() => {
  fetchData();
});
</script>
<style lang="scss" scoped>
.monthly-report-users-chart {
  height: 255px;
  width: 100%;
}
</style>
