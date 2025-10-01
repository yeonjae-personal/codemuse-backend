<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useI18n } from "vue-i18n";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import useGlobalStore from "@/store/global.store";
import COMMS001P from "@/pages/syssms/subs/COMMS001P.vue";

const { t: translateMessage, locale } = useI18n();
const emit = defineEmits(["selectedRow", "doubleClicked"]);

const props = defineProps({
  dataList: {
    type: Array,
    default: () => [],
  },
});

const globalStore = useGlobalStore();

// Table settings
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
  width: 140,
});
const columnDefs = ref([
  {
    headerName: translateMessage("sys_msg.table.select"),
    // valueFormatter: () => "",
    headerCheckboxSelection: true,
    checkboxSelection: true,
    resizable: true,
    width: 65,
    cellClass: "wrap-text",
    headerClass: "wrap-text",
  },
  {
    field: "sysMsgId",
    headerName: translateMessage("sys_msg.table.sys_msg_id"),
  },
  {
    field: "sysMsgLangNm",
    headerName: translateMessage("sys_msg.table.sys_msg_lang_nm"),
    flex: 1,
  },
  {
    field: "sysMsgLangCd",
    hide: true,
  },
  {
    field: "sysMsgCntn",
    headerName: translateMessage("sys_msg.table.sys_msg_cntn"),
    flex: 1,
  },
  {
    field: "rgstUsr",
    headerName: translateMessage("sys_msg.table.rgst_usr"),
    flex: 1,
  },
  {
    field: "rgstDtm",
    headerName: translateMessage("sys_msg.table.rgst_dtm"),
    flex: 1,
  },
  {
    field: "updDtm",
    headerName: translateMessage("sys_msg.table.upd_dtm"),
    flex: 1,
  },
]);

// Update column headers
// const updateColumnHeaders = () => {
//   columnDefs.value.forEach((col: any) => {
//     col.headerName = translateMessage(`sys_msg.table.${col.field}`);
//   });
// };

watch(
  locale,
  () => {
    // updateColumnHeaders();
  },
  { immediate: true }
);

const getSelectedRow = () => {
  if (gridApi.value) {
    const selectedRow = gridApi.value.getSelectedRows();
    if (selectedRow.length > 0) {
      return selectedRow[0];
    }
  }
  return {};
};

const showModalAddOrUpdate = async (isAddNew: boolean) => {
  if (!isAddNew) {
    if (!getSelectedRow().sysMsgId) {
      return;
    }
  }
  const objectModal: any = {
    title: translateMessage("sys_msg.add.title"),
    component: COMMS001P,
    dataInput: isAddNew
      ? { isAddNew: isAddNew }
      : { isAddNew: isAddNew, ...getSelectedRow() },
    width: "700px",
  };
  await globalStore.openModal(objectModal);
};

const onGridReady = (params: any) => {
  gridApi.value = params.api;
};

const onSelectionChanged = () => {
  emit("selectedRow", getSelectedRow());
};
</script>

<template>
  <div class="flex items-center mb-2 mt-4">
    <div class="flex gap-2 items-center" style="margin-left: auto">
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdate(true)"
      >
        {{ $t(`sys_msg.table.btn_create`) }}
      </v-btn>
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdate(false)"
      >
        {{ $t(`sys_msg.table.btn_update`) }}
      </v-btn>
    </div>
  </div>
  <div class="mx-auto prose prose-indigo sm:rounded-md">
    <div class="flex">
      <ag-grid-vue
        :style="{ width: '100%', height: '700px' }"
        class="ag-theme-alpine"
        :column-defs="columnDefs"
        :default-col-def="defaultColDef"
        :row-data="props.dataList"
        :suppress-row-click-selection="false"
        :pagination="true"
        row-selection="single"
        @grid-ready="onGridReady"
        @selection-changed="onSelectionChanged"
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
</style>
