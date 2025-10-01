<script setup lang="ts">
import axios from "axios";
import useGlobalStore from "@/store/global.store";
import { AgGridVue } from "ag-grid-vue3";
import { GridApi } from "ag-grid-community";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { useInputValidation } from "@/composables/useInputValidation";
import COMMW001P from "@/pages/vocap/subs/COMMW001P.vue";
import { API_URL } from "@/constants";

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const globalStore = useGlobalStore();
const loading = ref(false);
const gridApi = ref<GridApi | null>(null);
const rowData = ref([]);

// Data
const refAnalyticalTermName = ref<any>(null);
const analyticalTermName = ref("");
const termConfigurationInformation = ref("");
const termEnglishName = ref("");
const termAbbreviation = ref("");

const columnDefs = ref([
  { field: "vocaNm", headerName: "단어명", checkboxSelection: true },
  { field: "vocaEngAbb", headerName: "단어영문약자" },
  { field: "vocaEngNm", headerName: "단어영문명" },
  { field: "vocaDscr", headerName: "단어설명" },
]);

const defaultColDef = ref({
  wrapText: true,
  resizable: true,
  editable: false,
  filter: false,
  flex: 1,
  wrapHeaderText: true,
  autoHeaderHeight: true,
});

const onGridReady = (params: any) => {
  gridApi.value = params.api;
};

const handleAnalysis = async () => {
  const { valid } = await refAnalyticalTermName.value.validate();

  if (!valid) {
    return;
  }
  fetchCharacterAnalysis(analyticalTermName.value);
};

const handleShowModalAddVoca = async () => {
  const objectModal: any = {
    title: "단어 등록/수정 팝업",
    component: COMMW001P,
    dataInput: {},
    width: "600",
  };
  try {
    await globalStore.openModal(objectModal);
  } catch (ex) {
    alert("An error occurred!");
  }
};

const fetchCharacterAnalysis = async (analWord: string) => {
  try {
    loading.value = true;
    if (gridApi.value) {
      gridApi.value.showLoadingOverlay();
    }

    const response = await axios.get(`${API_URL}/comm/voca/v1/anal`, {
      params: {
        analWord: analWord,
      },
    });
    rowData.value = response.data.list;
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
    gridApi.value?.hideOverlay();
  }
};

const onSelectionChanged = (event: any) => {
  const selectedRows = event.api.getSelectedRows();
  termConfigurationInformation.value = selectedRows
    .map((row: any) => row.vocaNm)
    .join("+");
  termEnglishName.value = selectedRows
    .map((row: any) => row.vocaEngAbb)
    .join(" ");
  termAbbreviation.value = selectedRows
    .map((row: any) => row.vocaEngNm)
    .join("_");
};

const emit = defineEmits(["closeDialog"]);
const closeDialog = () => {
  emit("closeDialog", {
    vocaCstcInfo: termConfigurationInformation.value,
    vocaEngAbb: termEnglishName.value,
    vocaEngNm: termAbbreviation.value,
  });
};

const close = () => {
  emit("closeDialog", {});
};
onMounted(() => {
  analyticalTermName.value = props.data.analWord;
});
</script>
<template>
  <div class="mx-auto prose prose-indigo sm:rounded-md">
    <div class="flex flex-col w-100">
      <v-form ref="refAnalyticalTermName" class="w-100">
        <div class="flex flex-col gap-4">
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="2">
              <v-label>{{ $t("term.COMMV002P.lbl_analWord") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="10">
              <div class="flex gap-4">
                <v-text-field
                  v-model="analyticalTermName"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="100"
                  :rules="
                    useInputValidation({ required: true, maxLength: 100 })
                  "
                ></v-text-field>
                <div class="flex gap-2 ml-6">
                  <cf-button
                    :label="$t('term.COMMV002P.btn_analysis')"
                    @click="handleAnalysis"
                  />
                  <cf-button
                    :label="$t('term.COMMV002P.btn_add_vocab')"
                    @click="handleShowModalAddVoca"
                  />
                </div>
              </div>
            </v-col>
          </v-row>
        </div>
      </v-form>
    </div>

    <ag-grid-vue
      style="width: 100%; height: 219px"
      class="ag-theme-alpine"
      :column-defs="columnDefs"
      :pagination="false"
      suppress-pagination-panel="false"
      suppress-scroll-on-new-data="true"
      :row-data="rowData"
      :default-col-def="defaultColDef"
      :row-selection="'multiple'"
      @grid-ready="onGridReady"
      @selection-changed="onSelectionChanged"
    >
    </ag-grid-vue>
    <h4>
      {{ $t("term.lbl_analysis_information") }}
    </h4>

    <v-row class="p-0">
      <v-col class="no-padding" cols="12" md="3">
        <v-label>{{ $t("term.COMMV002P.lbl_term_vocaCstcInfo") }}</v-label>
      </v-col>
      <v-col class="no-padding" cols="12" md="9">
        <v-text-field
          v-model="termConfigurationInformation"
          :type="'input'"
          :variant="'outlined'"
          density="compact"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row class="p-0">
      <v-col class="no-padding" cols="12" md="3">
        <v-label>{{ $t("term.COMMV002P.lbl_term_english_name") }}</v-label>
      </v-col>
      <v-col class="no-padding" cols="12" md="9">
        <v-text-field
          v-model="termAbbreviation"
          :type="'input'"
          :variant="'outlined'"
          density="compact"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row class="p-0">
      <v-col class="no-padding" cols="12" md="3">
        <v-label>{{ $t("term.COMMV002P.lbl_term_abbreviation") }}</v-label>
      </v-col>
      <v-col class="no-padding" cols="12" md="9">
        <v-text-field
          v-model="termEnglishName"
          :type="'input'"
          :variant="'outlined'"
          density="compact"
        ></v-text-field>
      </v-col>
    </v-row>
  </div>

  <div class="flex gap-4 left-align">
    <cf-button :label="$t('term.COMMV002P.btn_close')" @click="close" />
    <cf-button :label="$t('term.COMMV002P.btn_apply')" @click="closeDialog" />
  </div>
</template>

<style scoped>
.left-align {
  flex-direction: row-reverse;
}

/* custom style for ag-grid */
.ag-theme-alpine {
  --ag-border-color: #828282;
  --ag-row-border-width: 1px;
  --ag-row-border-style: solid;
  --ag-row-border-width: 1px;
}

.ag-theme-alpine :deep(.ag-cell) {
  border-left: 1px solid #828282 !important;
  border-top: 1px solid #828282 !important;
}

.ag-theme-alpine :deep(.ag-center-cols-container) {
  border-right: 1px solid #828282 !important;
}

.ag-theme-alpine :deep(.ag-row) {
  border-bottom: 1px solid #828282 !important;
  background-color: #ffffff;
}

.ag-theme-alpine :deep(.ag-row-last) {
  border-bottom: 1px solid #828282 !important;
}

.ag-theme-alpine :deep(.ag-header-cell) {
  border-right: 1px solid #828282 !important;
}

.ag-theme-alpine :deep(.ag-header-container) {
  border-bottom: 1px solid #828282 !important;
}
</style>
