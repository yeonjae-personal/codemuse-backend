<template>
  <div class="subscriber-top-chart">
    <Bar :data="data" :options="options" />
  </div>
</template>

<!-- eslint-disable id-length -->
<!-- eslint-disable security/detect-object-injection -->
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
import { UI_DASHBOARD_SUBSCRIBERTOP10 } from "@/api/prod/path";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  ChartDataLabels
);

const subscriberData = ref([]);
const data = computed(() => ({
  labels: subscriberData.value.map((item) => item.offerName),
  datasets: [
    {
      label: "Subscriber Top 10",
      backgroundColor: subscriberData.value.map((item) =>
        item.status ? "#FDCED5" : "#82C5FA"
      ),
      data: subscriberData.value.map((item) => item.subscriber),
      barThickness: 8,
      borderRadius: 2,
    },
  ],
}));

const options = {
  responsive: true,
  maintainAspectRatio: false,
  indexAxis: "y",
  animation: {
    x: {
      easing: "linear",
      duration: 750,
      from: 0,
    },
    y: {
      easing: "linear",
      duration: 0,
      from: 0,
    },
  },
  scales: {
    x: {
      ticks: {
        display: false,
      },
      grid: {
        display: true,
        color: "#F0F2F5",
      },
      border: {
        display: false,
      },
    },
    y: {
      ticks: {
        crossAlign: "far",
        padding: 2,
        font: {
          size: 11,
        },
        color: "#6B6D70",
      },
      grid: {
        display: false,
      },
      beforeUpdate(axis) {
        const labels = axis.chart.data.labels;
        for (let i = 0; i < labels.length; i++) {
          const lbl = labels[i];
          if (typeof lbl === "string" && lbl.length > 15) {
            labels[i] = `${lbl.substring(0, 15)}...`; // cutting
          }
        }
      },
      afterFit(scale) {
        scale.width = 110;
      },
    },
  },
  plugins: {
    legend: {
      display: false,
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
    const response = await httpClient.get(UI_DASHBOARD_SUBSCRIBERTOP10, {
      params: { view: "simple", max: 10 },
    });
    subscriberData.value =
      response?.data?.map((item) => ({
        subscriber: item.subscriber,
        offerName: item.name,
      })) || [];
  } catch {}
};

onMounted(() => {
  fetchData();
});
</script>
<style lang="scss" scoped>
.subscriber-top-chart {
  height: 250px;
  width: 100%;
}
</style>
