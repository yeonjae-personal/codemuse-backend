<template>
  <div
    v-if="!shouldReset"
    class="rounded-lg border border-[#666] flex flex-col h-full col-span-3 bg-white"
  >
    <div
      class="w-full flex justify-space-between items-center px-6 gap-2 oneview-header"
    >
      <span
        v-if="tabView === VIEW_MODE.GRID"
        class="flex align-center text-text-base text-base font-medium tracking-[0.5px]"
      >
        {{ $t("product_platform.impactAnalysis.oneViewTitle") }}
      </span>
      <template v-else>
        <div class="flex align-center gap-4">
          <div class="flex align-center gap-2">
            <BaseSelectScroll
              v-model="gridViewParams.lctgrItemCode"
              :options="largeTypeList"
              class="form-item w-[190px] text-[13px]"
              :placeholder="$t('product_platform.selectBoxItem')"
              default-item-select-all
              :height="48"
              @update:model-value="handleChangeItem"
            />
            <TargetInputSearch
              v-model:select-name-code="selectedGridNmCd"
              v-model:prod-item-nm="gridViewParams.objName"
              v-model:prod-item-cd="gridViewParams.objCode"
              class="w-[368px]"
              @on-search="fetchTableData"
              @on-change-type="handleChangeType"
            />
          </div>
          <SearchAndRefreshButton
            @handle-search="fetchTableData"
            @handle-refresh="handleResetForm"
          />
        </div>
      </template>
      <div class="flex flex-nowrap">
        <FileAction
          v-if="tabView === VIEW_MODE.LIST"
          class-name="mr-2"
          title="Upload Impact Analysis"
          description="Please upload Impact Analysis excel file that you have downloaded."
          :is-downloading="downloading"
          @upload-file="handleUploadImpactAnalysis"
          @download-file="handleDownloadDataTable"
        />
        <SwitchViewTable
          :model-value="tabView"
          class="h-[44px] w-[82px]"
          @toggle-view-mode="handleSwitchView"
        >
        </SwitchViewTable>
      </div>
    </div>
    <GridView v-if="tabView === VIEW_MODE.GRID" />
    <TableView v-else-if="tabView === VIEW_MODE.LIST" />
  </div>
</template>

<script setup lang="ts">
import { useImpactAnalysisStore } from "@/store";
import { SPACE, VIEW_MODE } from "@/constants/";
import { useDownloadFile } from "@/composables/useDownloadFIle";
import { DOWNLOAD_EXCEL_IMPACT_ANALYSIS } from "@/api/prod/path";
import { useI18n } from "vue-i18n";
import { DownloadParamsImpact } from "@/interfaces/prod/ImpactAnalysisInterface";
import { appProvider, AppProvider } from "@/types/common";
import { SearchBy } from "@/enums";

type Props = {
  largeTypeList: any[];
};

defineProps<Props>();

const route = useRoute();
const { locale } = useI18n();

const impactAnalysisStore = useImpactAnalysisStore();
const { paramListView, selectedGridNmCd } = storeToRefs(
  useImpactAnalysisStore()
);
const { downloading, downloadFile } = useDownloadFile();
const { onBulkUploadFile } = inject<AppProvider>(appProvider, {
  onBulkUploadFile: async () => {},
});
const gridViewParams = inject("gridViewParams", {
  lctgrItemCode: SPACE,
  objName: "",
  objCode: "",
});

const tabView = ref(VIEW_MODE.GRID);
const shouldReset = ref(false);

watch(
  () => impactAnalysisStore.getShouldReset,
  (val) => {
    shouldReset.value = val;
    nextTick(() => {
      impactAnalysisStore.setShouldReset(false);
    });
  }
);

onBeforeMount(() => {
  tabView.value = impactAnalysisStore.getTabView;
  gridViewParams.lctgrItemCode = paramListView.value.lctgrItemCode;
  gridViewParams.objName = paramListView.value.objName;
  gridViewParams.objCode = paramListView.value.objCode;
});

const fetchTableData = async (): Promise<void> => {
  paramListView.value.page = 1;
  paramListView.value.lctgrItemCode = gridViewParams.lctgrItemCode;
  paramListView.value.objName = gridViewParams.objName;
  paramListView.value.objCode = gridViewParams.objCode;
  await impactAnalysisStore.actionGetListView();
};

const handleResetForm = (): void => {
  gridViewParams.lctgrItemCode = SPACE;
  gridViewParams.objName = "";
  gridViewParams.objCode = "";
  paramListView.value.lctgrItemCode = SPACE;
  paramListView.value.objCode = undefined;
  paramListView.value.objName = undefined;
  selectedGridNmCd.value = SearchBy.Name;
  fetchTableData();
};

const handleChangeItem = (value: string): void => {
  gridViewParams.lctgrItemCode = value === SPACE ? SPACE : value;
  gridViewParams.objName = "";
  gridViewParams.objCode = "";
  paramListView.value.page = 1;
  paramListView.value.size = 10;
};

const handleChangeType = (_value: string): void => {
  gridViewParams.objName = "";
  gridViewParams.objCode = "";
};

const handleSwitchView = (value) => {
  tabView.value = value;
  impactAnalysisStore.setTabView(value);
};

const handleUploadImpactAnalysis = async (file: File): Promise<void> => {
  onBulkUploadFile("", file, route.path, fetchTableData);
};

const handleDownloadDataTable = async () => {
  const params: DownloadParamsImpact = {
    resourceUuid:
      impactAnalysisStore.selectedSearchItem.type === "Resource"
        ? impactAnalysisStore.selectedSearchItem.prodUuid
        : "",
    offerUuid:
      impactAnalysisStore.selectedSearchItem.type === "Offer"
        ? impactAnalysisStore.selectedSearchItem.prodUuid
        : "",
    componentUuid:
      impactAnalysisStore.selectedSearchItem.type === "Component"
        ? impactAnalysisStore.selectedSearchItem.prodUuid
        : "",
    language: (locale.value as any) || "en",
  };
  await downloadFile(DOWNLOAD_EXCEL_IMPACT_ANALYSIS, params, "ImpactAnalysis");
};
</script>

<style scoped>
.oneview-title {
  font-size: 18px;
  font-weight: 500;
  line-height: 27px;
  letter-spacing: 0.5px;
}

.oneview-header {
  padding-top: 22px;
  padding-bottom: 10px;
}
</style>
