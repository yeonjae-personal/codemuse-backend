<template>
  <div id="app">
    <router-view></router-view>
  </div>
  <div
    v-if="isLoadingBulkUpload"
    class="loading-bulk-upload gap-2 border-success bg-[#ECFDF3] p-4 shadow-lg flex rounded-lg justify-between items-center space-x-2 transition-all duration-300"
  >
    <div class="loader"></div>
    <div class="text-sm font-medium w-full break-words text-[#079455]">
      Uploading file ....
    </div>
  </div>
  <SummaryPopup
    v-if="isOpenPopupSummary"
    v-model="isOpenPopupSummary"
    :data="reportData"
    :file-name="fileName"
    :execution-time="executionTime"
    :total-items="totalItems"
    :success-items="successItems"
    :fail-items="failItems"
    @on-reset-popup="handleResetPopupData"
  />
</template>

<script lang="ts" setup>
import { useRoute } from "vue-router";
import { useI18n } from "vue-i18n";
import capitalize from "lodash-es/capitalize";
import { useSnackbarStore } from "./store";
import useApp from "./composables/useApp";
import { fetchAndSaveTranslations } from "./utils/fetch-i18n";
import { uploadExcelFile } from "./api/prod/bulkUploadApi";
import { appProvider, languageProvider } from "./types/common";
import { formatFunctionTiming } from "./utils/date-time-format";
import { UploadFileRowData } from "./types/bulk-upload";

const SummaryPopup = defineAsyncComponent(
  () => import("@/components/bulk-upload/SummaryPopup.vue")
);

const route = useRoute();
const { t } = useI18n();

const { showSnackbar } = useSnackbarStore();
const { languageList } = useApp();

const fileName = ref<string>("");
const executionTime = ref<string>("");
const isOpenPopupSummary = ref<boolean>(false);
const isLoadingBulkUpload = ref<boolean>(false);
const reportData = ref<UploadFileRowData[]>([]);
const totalItems = ref<number>(0);
const successItems = ref<number>(0);
const failItems = ref<number>(0);

const handleResetPopupData = (): void => {
  fileName.value = "";
  executionTime.value = "";
  reportData.value = [];
  totalItems.value = 0;
  successItems.value = 0;
  failItems.value = 0;
};

const onBulkUploadFile = async (
  url: string,
  file: File,
  routePath: string,
  callback: () => Promise<void>
): Promise<void> => {
  const startTime = performance.now();
  isLoadingBulkUpload.value = true;
  try {
    const { data } = await uploadExcelFile(url, file);
    fileName.value = file.name;
    if (route.path === routePath) callback();
    showSnackbar(t("product_platform.upload_file_successfully"), "success");
    isOpenPopupSummary.value = true;
    reportData.value = data.datas.map((item) => ({
      ...item,
      no: (item.index as number) || 1,
      code: item.code || "-",
      message: item.message || "-",
      messageCode: item.messageCode || "-",
      name: item.name || "-",
      result: capitalize(item.result ?? "success"),
      type: item.type || "-",
    }));
    totalItems.value = data.itemQuantity || 0;
    successItems.value = data.itemQuantitySucess || 0;
    failItems.value = data.itemQuantityFail || 0;
    const endTime = performance.now();
    executionTime.value = formatFunctionTiming(endTime - startTime);
  } catch (error: any) {
    if (error.errorCode === "400") {
      showSnackbar(error.errorMsg, "error");
    } else {
      showSnackbar(t("product_platform.internalServerError"), "error");
    }
  } finally {
    isLoadingBulkUpload.value = false;
  }
};

provide(appProvider, { onBulkUploadFile });
provide(languageProvider, { languageList });

onMounted(() => {
  fetchAndSaveTranslations();
});
</script>

<style lang="scss" scoped>
.loading-bulk-upload {
  position: fixed;
  z-index: 9999;
  top: 24px;
  right: 24px;
  min-width: 280px;
  height: 53px;
}

.loader {
  width: 32px;
  aspect-ratio: 1;
  border-radius: 50%;
  padding: 1px;
  background: conic-gradient(#0000 10%, #f03355) content-box;
  -webkit-mask: repeating-conic-gradient(
      #0000 0deg,
      #000 1deg 20deg,
      #0000 21deg 36deg
    ),
    radial-gradient(
      farthest-side,
      #0000 calc(100% - 8px - 1px),
      #000 calc(100% - 8px)
    );
  -webkit-mask-composite: destination-in;
  mask-composite: intersect;
  animation: spinner 1s infinite steps(10);
}

@keyframes spinner {
  to {
    transform: rotate(1turn);
  }
}
</style>
