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
        <div class="left-icon"><RecentlyWorkedIcon />{{ title }}</div>
        <button @click="dialog = false">
          <DashboardCloseIcon />
        </button>
      </v-card-title>
      <div class="card-sub-title">{{ desc }}</div>
      <div class="card-content">
        <div class="search-section">
          <div class="left-section">
            <div class="category">
              <BaseSelectScroll
                v-model="category"
                :options="CATEGORY_FIELDS"
                :placeholder="t('product_platform.dashboard.category')"
                class="w-full"
                :height="48"
                :z-index="3500"
                default-item-select-all
                @update:model-value="handleChangeCategory"
              />
            </div>
            <div class="offer-type">
              <BaseSelectScroll
                v-model="offerType"
                :options="listOfferType"
                :placeholder="t('product_platform.dashboard.type')"
                :height="48"
                :z-index="3500"
                default-item-select-all
                class="w-full"
              />
            </div>
            <div class="search-by">
              <BaseSelectScroll
                v-model="searchBy"
                :options="OPTIONS_SELECT_SEARCH"
                :default-item-select-all="false"
                :height="48"
                :z-index="3500"
                placeholder=""
                class="catalog-select-filter"
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
            <span class="right-desc mr-[16px]">{{
              locale === "en"
                ? `${t("product_platform.dashboard.baseOn")} ${dateBatch || ""}`
                : `${dateBatch || ""} ${t("product_platform.dashboard.baseOn")}`
            }}</span>
            <BaseButton
              :width="WIDTH_BUTTON.EXCEL"
              :color="ButtonColorType.Gray"
              :disabled="downloading"
              @click="onClickDownload"
            >
              <DownloadIcon class="mr-[6px]" />
              {{ t("product_platform.dashboard.download") }}
            </BaseButton>
          </div>
        </div>
        <div class="content-section">
          <RecentlyWorkedGrid
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
import {
  UI_DASHBOARD_GET_MIDDLE_ITEM,
  UI_DASHBOARD_RECENTLYWORKED,
  UI_DASHBOARD_RECENTLYWORKED_EXPORT,
} from "@/api/prod/path";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { OPTIONS_SELECT_SEARCH } from "@/constants/offer";
import { ButtonColorType } from "@/enums";
import { useSnackbarStore } from "@/store";
import { httpClient } from "@/utils/http-common";
import { useI18n } from "vue-i18n";
import DashboardCloseIcon from "../../icons/DashboardCloseIcon.vue";
import DetailIcon from "../../icons/DetailIcon.vue";
import DownloadIcon from "../../icons/DownloadIcon.vue";
import RecentlyWorkedIcon from "../../icons/RecentlyWorkedIcon.vue";
import RecentlyWorkedGrid from "./RecentlyWorkedGrid.vue";
import { WIDTH_BUTTON } from "@/constants/index";

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

const category = ref("");
const offerType = ref("");
const searchBy = ref("name");
const offerName = ref("");
const dialog = ref(false);
const pageSize = ref(10);
const currentPage = ref(1);
const totalItems = ref(0);
const dateBatch = ref("");
const isLoading = ref<boolean>(false);

const TARGET_TYPE_CODE = {
  OFFER: "O",
  COMPONENT: "C",
  RESOURCE: "R",
  GROUP: "G",
};

const CATEGORY_FIELDS = [
  { cmcdDetlId: TARGET_TYPE_CODE.OFFER, cmcdDetlNm: "Offer" },
  { cmcdDetlId: TARGET_TYPE_CODE.COMPONENT, cmcdDetlNm: "Component" },
  { cmcdDetlId: TARGET_TYPE_CODE.RESOURCE, cmcdDetlNm: "Resource" },
  { cmcdDetlId: TARGET_TYPE_CODE.GROUP, cmcdDetlNm: "Group" },
];

const listOfferType = ref([]);

const subscriberData = ref([]);

const afterLeave = () => {
  category.value = " ";
  offerType.value = " ";
  offerName.value = "";
  searchBy.value = "name";
  listOfferType.value = [];
};

const updatePageSize = (value) => {
  pageSize.value = value;
  fetchData();
};

const updateCurrentPage = (value) => {
  currentPage.value = value;
  fetchData();
};

const handleChangeCategory = (category) => {
  offerType.value = " ";
  getTypeByCategory(category);
};

const getTypeByCategory = async (largeItemCode) => {
  const params: any = {};
  if (largeItemCode.trim().length > 0) {
    params.largeItemCode = largeItemCode;
  }
  const response = await httpClient.get(UI_DASHBOARD_GET_MIDDLE_ITEM, {
    params,
  });
  listOfferType.value =
    response?.data?.map((item) => ({
      cmcdDetlId: item.code,
      cmcdDetlNm: item.name,
    })) || [];
};

const fetchData = async () => {
  isLoading.value = true;
  try {
    const params: any = {
      view: "detail",
      searchBy: searchBy.value === "name" ? "object-name" : "object-code",
      searchValue: offerName.value,
      page: currentPage.value,
      size: pageSize.value,
    };
    if (offerType.value.trim().length > 0) {
      params.type = offerType.value;
    }
    if (category.value.trim().length > 0) {
      params.category = category.value;
    }
    const response = await httpClient.get(UI_DASHBOARD_RECENTLYWORKED, {
      params: params,
    });

    subscriberData.value =
      response?.data?.elements?.map((item) => ({
        category: item.category || "-",
        type: item.type || "-",
        itemName: item.objName || "-",
        itemCode: item.objCode || "-",
        work: item.workTypeName || "-",
        workCode: item.workTypeCode.trim() || "-",
        responsibleDept: item.responsibleDept || "-",
        responsibleUser: item.responsibleUser || "-",
        dateTime: item.workDate || "-",
      })) || [];
    totalItems.value = response.data.totalElements;
    dateBatch.value = response.data.dateBatch;
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
  category.value = " ";
  offerName.value = "";
  offerType.value = " ";
  searchBy.value = "name";
  fetchData();
  getTypeByCategory("");
};

const onClickDownload = () => {
  const params: any = {
    searchBy: searchBy.value === "name" ? "object-name" : "object-code",
    searchValue: offerName.value,
    language: locale.value || "en",
  };
  if (offerType.value.trim().length > 0) {
    params.type = offerType.value;
  }
  if (category.value.trim().length > 0) {
    params.category = category.value;
  }

  downloadFile(
    UI_DASHBOARD_RECENTLYWORKED_EXPORT,
    params,
    "RecentlyWorked",
    "xlsx",
    "YYYYMMDD"
  );
};

const afterEnter = () => {
  currentPage.value = 1;
  pageSize.value = 10;
  fetchData();
  getTypeByCategory("");
};
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
      .category {
        width: 120px;
        margin-right: 8px;
      }
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
      .date-time {
        width: 256px;
        display: flex;
        align-items: center;
        gap: 4px;
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
