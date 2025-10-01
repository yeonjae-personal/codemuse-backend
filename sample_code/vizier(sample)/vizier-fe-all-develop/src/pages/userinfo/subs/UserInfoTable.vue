<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useI18n } from "vue-i18n";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import useGlobalStore from "@/store/global.store";
import COMMU003P from "@/pages/userinfo/subs/COMMU003P.vue";

const { t: translateMessage } = useI18n();
const emit = defineEmits(["selectedRow", "closeDialog", "applySelectedRow"]);

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
    headerName: translateMessage("user_info.table.select"),
    headerCheckboxSelection: true,
    checkboxSelection: true,
    resizable: true,
    width: 65,
    cellClass: "wrap-text",
    headerClass: "wrap-text",
  },
  {
    field: "userId",
    headerName: translateMessage("user_info.table.user_id"),
  },
  {
    field: "userNm",
    headerName: translateMessage("user_info.table.user_nm"),
  },
  {
    field: "userKdCd",
    headerName: translateMessage("user_info.table.user_kd_cd"),
  },
  {
    field: "userKdCdNm",
    headerName: translateMessage("user_info.table.user_kd_cd_nm"),
  },
  {
    field: "orgCd",
    headerName: translateMessage("user_info.table.org_cd"),
  },
  {
    field: "orgNm",
    headerName: translateMessage("user_info.table.org_nm"),
  },
  {
    field: "whofStatCd",
    hide: true,
  },
  {
    field: "whofStatNm",
    headerName: translateMessage("user_info.table.whof_stat_nm"),
  },
  {
    field: "updDtm",
    headerName: translateMessage("user_info.table.upd_dtm"),
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

const handleApply = () => {
  const rowSelected = getSelectedRow();
  emit("applySelectedRow", { ...rowSelected });
};

const showModalAddOrUpdate = async (isAddNew: boolean) => {
  if (!isAddNew) {
    if (!getSelectedRow().userId) {
      return;
    }
  }
  const objectModal: any = {
    title: isAddNew
      ? translateMessage("user_info.add.title_add")
      : translateMessage("user_info.add.title_update"),
    component: COMMU003P,
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
  <div v-if="props.isPopup" class="flex items-center mb-2 mt-4">
    <div class="flex gap-2 items-center" style="margin-left: auto">
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="handleApply"
        >{{ $t(`user_info.table.btn_apply`) }}
      </v-btn>
    </div>
  </div>
  <div v-else class="flex items-center mb-2 mt-4">
    <div class="flex gap-2 items-center" style="margin-left: auto">
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdate(true)"
        >{{ $t(`user_info.table.btn_create`) }}
      </v-btn>
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdate(false)"
        >{{ $t(`user_info.table.btn_update`) }}</v-btn
      >
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
