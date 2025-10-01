<template>
  <div class="chart-wrapper">
    <div class="offer-type">
      {{ t("product_platform.dashboard.offerName") }}
    </div>
    <ul class="character">
      <li
        v-for="(item, index) in subscriberData"
        v-show="!item.mock"
        :key="index"
      >
        <span
          :class="'offer-item offer-type-'.concat(item.offerType.toLowerCase())"
        >
          {{ item.offerType === "DE" ? "" : item.offerType.charAt(0) }}
        </span>
        <CustomTooltip :content="item.offerName">
          <span
            class="offer-name"
            v-html="
              offerName?.length > 0 && searchBy === 'name'
                ? highlightText(item.offerName, offerName)
                : item.offerName
            "
          />
        </CustomTooltip>
      </li>
    </ul>
    <div class="chart-content">
      <Bar :data="data" :options="options" />
    </div>
    <div class="table-grid">
      <div class="table-grid-header">
        <ul>
          <li>{{ t("product_platform.dashboard.startDate") }}</li>
          <li>{{ t("product_platform.dashboard.endDate") }}</li>
          <li>{{ t("product_platform.dashboard.duration") }}</li>
        </ul>
      </div>
      <div class="table-grid-body">
        <ul>
          <li
            v-for="(item, index) in subscriberData"
            v-show="!item.mock"
            :key="index"
          >
            <div>{{ item.startDate || "-" }}</div>
            <div>{{ item.endDate || "-" }}</div>
            <div>{{ item.duration || "-" }}</div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<!-- eslint-disable id-length -->
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
import { highlightText } from "@/utils/format-data";
import { useI18n } from "vue-i18n";

const props = defineProps({
  offerName: {
    type: String,
    default: "",
  },
  subscriberData: {
    type: Array,
    default: () => [],
  },
  searchBy: {
    type: String,
    default: "",
  },
});

const { t } = useI18n();

ChartJS.register(
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
  ChartDataLabels
);

const data = computed(() => ({
  labels: props.subscriberData.map((item) => item.offerName),
  datasets: [
    {
      label: "Subscriber Top 10",
      backgroundColor: props.subscriberData.map((item) =>
        item.status ? "#FDCED5" : "#ADDBFF"
      ),
      data: props.subscriberData.map((item) => item.subscriber),
      barThickness: 32,
      borderRadius: 6,
      datalabels: {
        align: () => {
          const maxValue = Math.max(
            ...props.subscriberData.map((item) => item.subscriber)
          );
          const aligns = props.subscriberData.map((item) =>
            Math.floor((item.subscriber / maxValue) * 100) <= 10
              ? "end"
              : "start"
          );
          return aligns;
        },
        anchor: "end",
      },
    },
  ],
}));

const options = {
  responsive: true,
  maintainAspectRatio: false,
  indexAxis: "y",
  layout: {
    padding: {
      top: 17,
      left: 165,
    },
  },
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
      grid: {
        display: true,
        color: "#F0F2F5",
      },
      border: {
        display: false,
      },
      ticks: {
        font: {
          size: 13,
          weight: 400,
          family: "Noto Sans KR",
        },
      },
    },
    y: {
      ticks: {
        display: false,
        /*crossAlign: "far",
        padding: 12,
        font: {
          size: 13,
          weight: 400,
          family: "Noto Sans KR",
        },
        color: "#3A3B3D",*/
      },
      grid: {
        display: false,
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
      color: "#3A3B3D",
      font: {
        size: 13,
        family: "Noto Sans KR",
      },
      formatter: (x) =>
        // eslint-disable-next-line security/detect-unsafe-regex
        x === 0 ? "" : x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","),
    },
  },
};
</script>

<style scoped lang="scss">
.chart-wrapper {
  height: 638px;
  margin-top: 22px;
  padding: 20px 24px 20px 40px;
  position: relative;
  display: flex;
  font-family: "Noto Sans KR";
}
.chart-content {
  width: 77%;
  margin-left: 11px;
}
.character {
  position: absolute;
  top: 38px;
  left: 0;
  list-style: none;
  width: 16%;
  li {
    padding: 5px;
    width: 100%;
    box-sizing: border-box;
    height: 55px;
    z-index: 1;
    position: relative;
    display: flex;
    align-items: center;
    border-radius: 8px;
    .offer-item {
      min-width: 32px;
      height: 32px;
      box-sizing: border-box;
      border: 1px solid #e6e9ed;
      color: #eb7a3d;
      background: linear-gradient(130.95deg, #fcfdff 3.07%, #ebeef5 94.84%);
      border-radius: 8px;
      font-size: 18px;
      font-weight: 800;
      display: flex;
      justify-content: center;
      align-items: center;
    }
    .offer-type-ao {
      color: #9947d3;
    }
    .offer-type-dc {
      color: #23b27f;
    }
    .offer-type-de {
      background:
        url("@/assets/icons/device.svg") no-repeat center center,
        linear-gradient(130.95deg, #fcfdff 3.07%, #ebeef5 94.84%);
    }
    .offer-name {
      color: #3a3b3d;
      margin-left: 10px;
      font-size: 13px;
      font-weight: 400;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
  > :nth-child(even) {
    background: #f7f8fa;
  }
}

.offer-type {
  position: absolute;
  top: 14px;
  left: 5px;
  font-size: 15px;
  font-weight: 500;
}

.table-grid {
  width: 312px;
  font-family: "Noto Sans KR";
  position: absolute;
  right: 0;
  top: 14px;
  .table-grid-header ul {
    list-style: none;
    display: flex;
    padding: 0 12px;
    box-sizing: border-box;
    & li {
      width: 33%;
      font-size: 15px;
      font-weight: 500;
      text-align: center;
      white-space: pre;
    }
    li:nth-child(2) {
      padding-left: 20px;
    }
    li:last-child {
      text-align: right;
    }
  }
  .table-grid-body {
    & ul {
      list-style: none;
      li {
        display: flex;
        padding: 10px 12px;
        box-sizing: border-box;
        height: 55px;
        border-radius: 8px;
        div {
          width: 33%;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 13px;
        }
        div:nth-child(2) {
          padding-left: 20px;
        }
        div:last-of-type {
          justify-content: flex-end;
        }
      }
      > :nth-child(even) {
        background: #f7f8fa;
      }
    }
  }
}
:deep(.highlight) {
  background-color: yellow;
}
</style>
