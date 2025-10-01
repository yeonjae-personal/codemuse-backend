<template>
  <v-dialog
    v-model="dialog"
    max-width="1370"
    max-height="864"
    min-height="864"
    persistent
    transition="v-expand-transition"
    @after-leave="afterLeave"
    @after-enter="afterEnter"
  >
    <template #activator="{ props: activatorProps }">
      <button v-bind="activatorProps">
        <DetailIcon />
      </button>
    </template>
    <v-card>
      <v-card-title>
        <div class="left-icon"><SubscriberTop10Icon />{{ title }}</div>
        <button @click="dialog = false">
          <DashboardCloseIcon />
        </button>
      </v-card-title>
      <div class="card-sub-title">{{ desc }}</div>
      <div class="card-content">
        <div class="search-section">
          <div class="left-section">
            <div class="offer-type">
              <BaseSelectScroll
                v-model="offerType"
                :options="listItemType"
                class="w-full"
                :height="48"
                placeholder=""
                :z-index="3500"
                default-item-select-all
              />
            </div>
            <div class="search-by">
              <BaseSelectScroll
                v-model="searchBy"
                :options="OPTIONS_SELECT_SEARCH"
                :default-item-select-all="false"
                placeholder=""
                class="catalog-select-filter"
                :height="48"
                :z-index="3500"
              />
            </div>
            <div class="offer-name">
              <BaseInputSearch
                v-model="offerName"
                density="comfortable"
                label="inputToSearch"
                variant="solo"
                hide-details
                single-line
                rounded="4"
                @handle-search="handleSearch"
              />
            </div>
            <SearchAndRefreshButton
              @handle-search="handleSearch"
              @handle-refresh="handleResetSearch"
            />
          </div>
          <div class="right-section">
            <span
              class="right-desc"
              :class="viewMode === VIEW_MODE.LIST ? 'mr-[16px]' : 'mr-[56px]'"
              >{{
                locale === "en"
                  ? `${t("product_platform.dashboard.baseOn")} ${
                      dateBatch || ""
                    }`
                  : `${dateBatch || ""} ${t(
                      "product_platform.dashboard.baseOn"
                    )}`
              }}</span
            >
            <BaseButton
              v-if="viewMode === VIEW_MODE.LIST"
              class="mr-[16px]"
              :width="WIDTH_BUTTON.EXCEL"
              :color="ButtonColorType.Gray"
              :disabled="downloading"
              @click="onClickDownload"
            >
              <DownloadIcon class="mr-[6px]" />
              {{ t("product_platform.dashboard.download") }}
            </BaseButton>
            <switch-view-table v-model="viewMode" class="ms-auto" />
          </div>
        </div>
        <div class="content-section">
          <SubscriberTop10Chart
            v-if="viewMode === VIEW_MODE.GRID"
            :subscriber-data="subscriberData"
            :offer-name="offerName"
            :search-by="searchBy"
          />
          <SubscriberTop10Grid
            v-if="viewMode === VIEW_MODE.LIST"
            :subscriber-data="subscriberData"
            :total-items="totalItems"
            :current-page="currentPage"
            :offer-name="offerName"
            :search-by="searchBy"
            :is-loading="isLoading"
            @page-size="updatePageSize"
            @current-page="updateCurrentPage"
          />
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { useSnackbarStore } from "@/store";
import { OPTIONS_SELECT_SEARCH } from "@/constants/offer";
import SubscriberTop10Icon from "../icons/SubscriberTop10Icon.vue";
import { VIEW_MODE, WIDTH_BUTTON } from "@/constants/";
import SubscriberTop10Chart from "./subscriber-top-10/SubscriberTop10Chart.vue";
import SubscriberTop10Grid from "./subscriber-top-10/SubscriberTop10Grid.vue";
import { ButtonColorType } from "@/enums";
import DownloadIcon from "../icons/DownloadIcon.vue";
import DetailIcon from "../icons/DetailIcon.vue";
import { httpClient } from "@/utils/http-common";
import {
  UI_DASHBOARD_GET_MIDDLE_ITEM,
  UI_DASHBOARD_SUBSCRIBERTOP10,
  UI_DASHBOARD_SUBSCRIBERTOP10_EXPORT,
} from "@/api/prod/path";
import DashboardCloseIcon from "../icons/DashboardCloseIcon.vue";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import BaseSelectScroll from "../common/BaseSelectScroll.vue";

defineProps({
  desc: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
});

const { locale, t } = useI18n();
const { downloading, downloadFile } = useDownloadFile();
const snackbarStore = useSnackbarStore();
const listItemType = ref([]);
const offerType = ref("");
const searchBy = ref("name");
const offerName = ref("");
const dialog = ref(false);
const viewMode = ref(VIEW_MODE.GRID);
const pageSize = ref(10);
const currentPage = ref(1);
const totalItems = ref(0);
const dateBatch = ref("");
const OFFER_TYPE = {
  "Add-On": "AO",
  Discount: "DC",
  Device: "DE",
  PricePlan: "PP",
};
const OFFER_TYPE_NAME = {
  AO: "Add-On",
  DC: "Discount",
  DV: "Device",
  PP: "PricePlan",
};
const subscriberData = ref([]);
const isLoading = ref<boolean>(false);

const afterLeave = () => {
  viewMode.value = VIEW_MODE.GRID;
  offerType.value = "";
  offerName.value = "";
  searchBy.value = "name";
};

const updatePageSize = (value) => {
  pageSize.value = value;
  fetchData();
};

const updateCurrentPage = (value) => {
  currentPage.value = value;
  fetchData();
};

const fetchData = async () => {
  isLoading.value = true;
  try {
    if (viewMode.value === VIEW_MODE.GRID) {
      const params: any = {
        view: "detail",
        max: 10,
        searchBy: searchBy.value === "name" ? "offer-name" : "offer-code",
        searchValue: offerName.value,
      };
      if (offerType.value.trim().length > 0) {
        params.type = OFFER_TYPE_NAME[offerType.value] || "";
      }
      const response = await httpClient.get(UI_DASHBOARD_SUBSCRIBERTOP10, {
        params: params,
      });
      const result = response.data || [];
      dateBatch.value = response.data.dateBatch;
      if (result.length < 10) {
        const numberOfResult = 10 - result.length;
        for (let i = 0; i < numberOfResult; i++) {
          result.push({
            name: "",
            type: "PricePlan",
            subscriber: 0,
            status: false,
            startDate: "",
            endDate: "",
            duration: "",
            mock: true,
          });
        }
      }
      subscriberData.value =
        result.map((item) => ({
          offerType: OFFER_TYPE[item.type],
          offerName: item.name,
          subscriber: item.subscriber,
          status: item.status,
          startDate: item.startDate || "-",
          endDate: item.endDate || "-",
          duration: item.duration || "-",
          mock: item.mock || false,
        })) || [];
    } else {
      const params: any = {
        view: "detail",
        searchBy: searchBy.value === "name" ? "offer-name" : "offer-code",
        searchValue: offerName.value,
        page: currentPage.value,
        size: pageSize.value,
      };
      if (offerType.value.trim().length > 0) {
        params.type = OFFER_TYPE_NAME[offerType.value] || "";
      }
      const response = await httpClient.get(UI_DASHBOARD_SUBSCRIBERTOP10, {
        params: params,
      });
      subscriberData.value =
        response?.data?.elements?.map((item) => ({
          offerCode: item.code,
          offerType: OFFER_TYPE[item.type],
          offerName: item.name,
          subscriber: item.subscriber,
          status: item.status,
          startDate: item.startDate || "-",
          endDate: item.endDate || "-",
          duration: item.duration || "-",
        })) || [];
      totalItems.value = response.data.totalElements;
    }
  } catch (error: any) {
    snackbarStore.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  } finally {
    isLoading.value = false;
  }
};

const handleSearch = () => {
  currentPage.value = 1;
  fetchData();
};

const handleResetSearch = () => {
  offerName.value = "";
  offerType.value = " ";
  searchBy.value = "name";
  fetchData();
};

const onClickDownload = () => {
  const params: any = {
    searchBy: searchBy.value === "name" ? "offer-name" : "offer-code",
    searchValue: offerName.value,
    language: locale.value || "en",
  };
  if (offerType.value.trim().length > 0) {
    params.type = OFFER_TYPE_NAME[offerType.value] || "";
  }

  downloadFile(
    UI_DASHBOARD_SUBSCRIBERTOP10_EXPORT,
    params,
    "SubscriberTop10",
    "xlsx",
    "YYYYMMDD"
  );
};

const getMiddleItem = async () => {
  const response = await httpClient.get(UI_DASHBOARD_GET_MIDDLE_ITEM, {
    params: {
      largeItemCode: "O",
    },
  });
  listItemType.value =
    response?.data?.map((item) => ({
      cmcdDetlId: item.code,
      cmcdDetlNm: item.name,
    })) || [];
};

const afterEnter = () => {
  getMiddleItem();
  fetchData();
};

watch(viewMode, () => {
  currentPage.value = 1;
  pageSize.value = 10;
  fetchData();
});
</script>
<style scoped lang="scss">
.v-card-title {
  padding: 24px 24px 0;
  font-family: "Noto Sans KR";
  font-size: 16px;
  font-weight: 500;
  line-height: 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #3a3b3d;
  .left-icon {
    display: flex;
    align-items: center;
    > svg {
      margin-right: 8px;
      width: 24px;
      height: 24px;
    }
  }
}
.card-sub-title {
  padding: 6px 24px 0;
  font-size: 13px;
  color: #6b6d70;
  font-family: "Noto Sans KR";
}
.card-content {
  padding: 24px 24px 10px;
  .search-section {
    width: 100%;
    display: flex;
    justify-content: space-between;
    .left-section {
      display: flex;
      align-items: center;
      .offer-type {
        width: 122px;
        margin-right: 8px;
      }
      .search-by {
        width: 118px;
        margin-right: 8px;
      }
      .offer-name {
        width: 242px;
        margin-right: 16px;
      }
    }
    .right-section {
      display: flex;
      align-items: center;
      .right-desc {
        background: #f0f2f5;
        padding: 4px 8px;
        height: 24px;
        border-radius: 4px;
        font-family: "Noto Sans KR";
        font-size: 11px;
        color: #6b6d70;
        text-align: center;
      }
    }
  }
  .content-section {
    min-height: 630px;
  }
}
</style>
