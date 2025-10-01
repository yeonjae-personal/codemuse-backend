<script setup lang="ts">
import { useRouter } from "vue-router";
import { useI18n } from "vue-i18n";
import { AgGridVue } from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import useGlobalStore from "@/store/global.store";
import COMMV001P from "@/pages/vocap/subs/COMMV001P.vue";
import COMMW001P from "@/pages/vocap/subs/COMMW001P.vue";
import CustomCellRenderer from "@/pages/vocap/subs/CustomCellRenderer.vue";
import moment from "moment-timezone";

const props = defineProps({
  dataList: {
    type: Array,
    default: () => [],
  },
});

const { t: translateMessage } = useI18n();
const globalStore = useGlobalStore();
const router = useRouter();

const frameworkComponents = ref({
  customCellRenderer: CustomCellRenderer,
});

const gridApi = ref<any>(null);
const defaultColDef = ref({
  wrapText: true,
  resizable: true,
  editable: false,
  enablePivot: true,
  enableValue: true,
  filter: false,
  wrapHeaderText: true,
  autoHeaderHeight: true,
  width: 120,
});

const columnDefs = ref([
  {
    headerName: translateMessage("term.table.detail"),
    field: "detail",
    valueFormatter: () => "",
    headerCheckboxSelection: false,
    checkboxSelection: false,
    resizable: true,
    width: 65,
    cellClass: "wrap-text",
    headerClass: "wrap-text",
    cellRenderer: CustomCellRenderer,
    cellRendererParams: (params) => ({
      data: params.data,
    }),
  },
  {
    field: "vocaNm",
    headerName: translateMessage("term.table.voca_nm"),
    width: 100,
  },
  {
    field: "vocaEngAbb",
    headerName: translateMessage("term.table.voca_eng_abb"),
  },
  {
    field: "vocaEngNm",
    headerName: translateMessage("term.table.voca_eng_nm"),
  },
  {
    field: "vocaDivsCd",
    headerName: translateMessage("term.table.voca_divs_cd"),
    valueGetter: (item) => (item.data.vocaDivsCd == "WO" ? "단어" : "용어"),
  },
  {
    field: "domnGrpNm",
    headerName: translateMessage("term.table.domn_grp_cd"),
  },
  {
    field: "domnNm",
    headerName: translateMessage("term.table.domn_nm"),
  },
  {
    field: "domnEngNm",
    headerName: translateMessage("term.table.domn_eng_nm"),
  },
  {
    field: "domnDivsNm",
    headerName: translateMessage("term.table.domnDivsNm"),
  },
  {
    field: "domnLen",
    headerName: translateMessage("term.table.domn_len"),
  },
  {
    field: "stndYn",
    headerName: translateMessage("term.table.stnd_yn"),
  },
  {
    field: "rgstUsr",
    headerName: translateMessage("term.table.rgst_usr"),
  },
  {
    field: "rgstDtm",
    headerName: translateMessage("term.table.rgst_dtm"),
    width: 170,
    valueFormatter: function (params) {
      return moment(params.value).format("YYYY-MM-DD HH:mm:ss");
    },
  },
  {
    field: "updDtm",
    headerName: translateMessage("term.table.upd_dtm"),
    width: 170,
    valueFormatter: function (params) {
      return moment(params.value).format("YYYY-MM-DD HH:mm:ss");
    },
  },
]);

const getSelectedRow = () => {
  if (gridApi.value) {
    const selectedRow = gridApi.value.getSelectedRows();
    if (selectedRow.length > 0) {
      return selectedRow[0];
    }
  }
  return {};
};

const showModalAddOrUpdateVo = async (isAddNew: boolean = true) => {
  const objectModal: any = {
    title: translateMessage("term.COMMV001P.title"),
    component: COMMV001P,
    dataInput: isAddNew ? {} : { ...getSelectedRow() },
    width: "600",
  };
  await globalStore.openModal(objectModal);
};

const handleShowModalAddOrUpdateWo = async (isAddNew: boolean = true) => {
  const objectModal: any = {
    title: translateMessage("term.COMMV001P.title"),
    component: COMMW001P,
    dataInput: isAddNew ? {} : { ...getSelectedRow() },
    width: "600",
  };
  try {
    await globalStore.openModal(objectModal);
  } catch (ex) {
    alert("An error occurred!");
  }
};

const goToDomainList = () => {
  router.push("/domain");
};

const onGridReady = (params: any) => {
  gridApi.value = params.api;
};
</script>

<template>
  <div class="flex items-center mb-2 mt-10">
    <div class="flex gap-2 items-center" style="margin-left: auto">
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="handleShowModalAddOrUpdateWo"
        >{{ $t(`term.lbl_add_vocab`) }}</v-btn
      >
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdateVo"
        >{{ $t(`term.lbl_add_term`) }}</v-btn
      >
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="goToDomainList"
        >{{ $t(`term.lbl_add_domain`) }}</v-btn
      >
      <v-btn size="large" variant="outlined" density="comfortable">{{
        $t(`term.lbl_add_code`)
      }}</v-btn>
    </div>
  </div>
  <div class="mx-auto prose prose-indigo sm:rounded-md">
    <div id="agGrid" class="flex">
      <ag-grid-vue
        style="width: 100%; height: 750px"
        class="ag-theme-alpine"
        :column-defs="columnDefs"
        :default-col-def="defaultColDef"
        :row-data="props.dataList"
        :suppress-row-click-selection="false"
        :pagination="true"
        row-selection="single"
        :framework-components="frameworkComponents"
        @grid-ready="onGridReady"
      >
      </ag-grid-vue>
    </div>
  </div>
</template>

<style scoped>
.custom-btn {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
  margin-top: 2px;
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

.icon {
  height: 16px;
  width: 16px;
  padding-top: 1px;
}

.iconContainer button {
  appearance: none;
  display: inline-block;
  margin: 8px;
  width: 30px;
  padding: 0.375em 1em 0.5em;
  white-space: nowrap;
  border-radius: 6px;
  box-shadow:
    0 0 0 4px transparent,
    0 1px 2px 0 #0c111d11;
  outline: none;
  background-color: var(--ag-background-color);
  border: 1px solid #d0d5dd;
  cursor: pointer;
}

:global(.ag-theme-quartz-dark) .iconContainer button {
  background-color: #141d2c;
  border-color: #344054;
}

.iconLink {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
