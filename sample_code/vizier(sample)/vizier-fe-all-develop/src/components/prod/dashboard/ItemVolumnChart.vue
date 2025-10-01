<template>
  <div class="item-volumn-chart">
    <div class="offer-chart">
      <Doughnut :data="offerData" :options="options" />
      <span class="chart-label">Offer</span>
    </div>
    <div class="items-chart">
      <Doughnut :data="itemsData" :options="itemsOptions" />
      <span class="chart-label">Items</span>
    </div>
  </div>
</template>

<!-- eslint-disable security/detect-unsafe-regex -->
<script setup>
import { Doughnut } from "vue-chartjs";
import { Chart as ChartJS, ArcElement, Tooltip, Legend, Title } from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";
import annotationPlugin from "chartjs-plugin-annotation";
import { httpClient } from "@/utils/http-common";
import { UI_DASHBOARD_ITEM_VOLUMN } from "@/api/prod/path";

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  ArcElement,
  ChartDataLabels,
  annotationPlugin
);

const offerRef = ref([]);
const offerTotalRef = ref(0);
const offerData = computed(() => ({
  labels: offerRef.value.map((item) => item.title),
  datasets: [
    {
      backgroundColor: ["#F9DBAF", "#D6B4ED", "#ABEFC6", "#ABDAFF"],
      data: offerRef.value.map((item) => item.value),
    },
  ],
}));

const itemRef = ref([]);
const itemTotalRef = ref(0);
const itemsData = computed(() => ({
  labels: itemRef.value.map((item) => item.title),
  datasets: [
    {
      backgroundColor: ["#fbe6eb", "#D9325A", "#e77c95", "#f0adbd", "#f6ced7"],
      data: itemRef.value.map((item) => item.value),
    },
  ],
}));

const options = {
  responsive: true,
  maintainAspectRatio: false,
  cutout: "35%",
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: false,
    },
    datalabels: {
      display: true,
      formatter: function (value, context) {
        return value + "%\n" + context.chart.data.labels[context.dataIndex];
      },
      textAlign: "center",
      color: ["#E04F16", "#833FB2", "#079455", "#1570EF"],
      font: {
        size: 9,
        weight: 500,
        lineHeight: "12px",
        family: "Noto Sans KR",
      },
    },
    annotation: {
      annotations: {
        dLabel: {
          type: "doughnutLabel",
          content: ({ _chart }) => [
            offerTotalRef.value
              .toString()
              .replace(/\B(?=(\d{3})+(?!\d))/g, ","),
          ],
          font: { size: 13, weight: 700, family: "Noto Sans KR" },
          color: "#6B6D70",
        },
      },
    },
  },
};

const itemsOptions = {
  responsive: true,
  maintainAspectRatio: false,
  cutout: "35%",
  plugins: {
    legend: {
      display: false,
    },
    tooltip: {
      enabled: false,
    },
    datalabels: {
      display: true,
      formatter: function (value, context) {
        return value + "%\n" + context.chart.data.labels[context.dataIndex];
      },
      textAlign: "center",
      color: ["#BA1642", "#fff", "#fff", "#fff", "#BA1642"],
      font: {
        size: 9,
        weight: 500,
        lineHeight: "12px",
        family: "Noto Sans KR",
      },
    },
    annotation: {
      annotations: {
        dLabel: {
          type: "doughnutLabel",
          content: ({ _chart }) => [
            itemTotalRef.value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","),
          ],
          font: { size: 13, weight: 700, family: "Noto Sans KR" },
          color: "#6B6D70",
        },
      },
    },
  },
};

const fetchData = async () => {
  try {
    const response = await httpClient.get(UI_DASHBOARD_ITEM_VOLUMN);
    itemTotalRef.value = response.data.total || 0;
    itemRef.value =
      response?.data?.map((item) => ({
        title: item.name,
        value: (item.ratio * 100).toFixed(0),
      })) || [];
    // Offer
    const offterItem =
      response?.data?.find((item) => item.name === "Offer") || {};
    offerTotalRef.value = offterItem?.total || 0;
    offerRef.value =
      offterItem?.items.map((item) => ({
        title: item.name,
        value: (item.ratio * 100).toFixed(0),
      })) || [];
  } catch {}
};

onMounted(() => {
  fetchData();
});
</script>
<style lang="scss" scoped>
.item-volumn-chart {
  height: 255px;
  width: 100%;
  display: flex;
  justify-content: center;
  gap: 12px;
  position: relative;
  .offer-chart {
    height: 180px;
    width: 50%;
    .chart-label {
      position: absolute;
      bottom: 30px;
      left: 21%;
      transform: translateX(-20%);
      font-family: "Noto Sans KR";
      font-size: 13px;
      font-weight: 500;
    }
  }
  .items-chart {
    height: 180px;
    width: 50%;
    .chart-label {
      position: absolute;
      bottom: 30px;
      right: 18%;
      font-size: 13px;
      font-weight: 500;
    }
  }
}
</style>
