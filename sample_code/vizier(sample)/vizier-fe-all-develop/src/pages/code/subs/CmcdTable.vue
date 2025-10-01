<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useI18n } from "vue-i18n";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import useGlobalStore from "@/store/global.store";
import COMMC001P from "@/pages/code/subs/COMMC001P.vue";
import COMMC002P from "@/pages/code/subs/COMMC002P.vue";

const { t: translateMessage, locale } = useI18n();
const emit = defineEmits(["selectedRow", "doubleClicked"]);

const props = defineProps({
  dataList: {
    type: Array,
    default: () => [],
  },
  isPopup: {
    type: Boolean,
    default: false,
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
    headerName: translateMessage("code.table.select"),
    field: "cmcdGrpId",
    valueFormatter: () => "",
    headerCheckboxSelection: true,
    checkboxSelection: true,
    resizable: true,
    width: 65,
    cellClass: "wrap-text",
    headerClass: "wrap-text",
  },
  {
    field: "cmcdGrpId",
    headerName: translateMessage("code.table.cmcdGrpId"),
  },
  {
    field: "cmcdGrpNm",
    headerName: translateMessage("code.table.cmcdGrpNm"),
  },
  {
    field: "cmcdGrpUseYn",
    headerName: translateMessage("code.table.cmcdGrpUseYn"),
    width: 165,
  },
  {
    field: "cmcdDetlId",
    headerName: translateMessage("code.table.cmcdDetlId"),
  },
  {
    field: "cmcdDetlNm",
    headerName: translateMessage("code.table.cmcdDetlNm"),
  },
  {
    field: "cmcdSortRank",
    headerName: translateMessage("code.table.cmcdSortRank"),
  },
  {
    field: "cmcdDetlUseYn",
    headerName: translateMessage("code.table.cmcdDetlUseYn"),
  },
  {
    field: "rgstUsr",
    headerName: translateMessage("code.table.rgstUsr"),
    hide: props.isPopup,
  },
  {
    field: "rgstDtm",
    headerName: translateMessage("code.table.rgstDtm"),
    hide: props.isPopup,
  },
  {
    field: "updDtm",
    headerName: translateMessage("code.table.updDtm"),
    hide: props.isPopup,
  },
]);

// Update column headers
// const updateColumnHeaders = () => {
//   columnDefs.value.forEach((col: any) => {
//     col.headerName = translateMessage(`code.table.${col.field}`);
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
const showModalAddOrUpdateCmcGroup = async () => {
  const objectModal: any = {
    title: translateMessage("code.add.title"),
    component: COMMC001P,
    dataInput: {},
    width: "600",
  };

  try {
    await globalStore.openModal(objectModal);
  } catch (ex) {
    alert("An error occurred!");
  }
};

const showModalAddOrUpdateCmcdDetail = async () => {
  if (!getSelectedRow().cmcdGrpId) {
    return;
  }
  const objectModal: any = {
    title: translateMessage("code.add.title"),
    component: COMMC002P,
    dataInput: {cmcdGrpId : getSelectedRow().cmcdGrpId },
    width: "600",
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
  <div v-if="!isPopup" class="flex items-center mb-2 mt-4">
    <div class="flex gap-2 items-center" style="margin-left: auto">
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdateCmcGroup"
        >{{ $t(`code.table.btn_add_edit_code_grp`) }}</v-btn
      >
      <v-btn  
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdateCmcdDetail"
        >{{ $t(`code.table.btn_add_edit_code`) }}</v-btn
      >
    </div>
  </div>
  <div class="mx-auto prose prose-indigo sm:rounded-md">
    <div class="flex">
      <ag-grid-vue
        :style="{ width: '100%', height: isPopup ? '450px' : '700px' }"
        class="ag-theme-alpine"
        :column-defs="columnDefs"
        :default-col-def="defaultColDef"
        :row-data="props.dataList"
        :suppress-row-click-selection="false"
        :pagination="true"
        row-selection="single"
        @grid-ready="onGridReady"
        @row-double-clicked="onRowDoubleClicked"
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
