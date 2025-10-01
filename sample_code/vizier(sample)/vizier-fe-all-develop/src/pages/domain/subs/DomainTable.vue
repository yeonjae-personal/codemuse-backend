<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useI18n } from "vue-i18n";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import useGlobalStore from "@/store/global.store";
import COMMD001P from "@/pages/domain/subs/COMMD001P.vue";
import COMMW001P from "@/pages/vocap/subs/COMMW001P.vue";
import COMMV001P from "@/pages/vocap/subs/COMMV001P.vue";

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
    headerName: translateMessage("domain.table.domn_id"),
    field: "domnId",
    valueFormatter: () => "",
    headerCheckboxSelection: true,
    checkboxSelection: true,
    resizable: true,
    width: 65,
    cellClass: "wrap-text",
    headerClass: "wrap-text",
  },
  {
    field: "domnNm",
    headerName: translateMessage("domain.table.domn_nm"),
    width: 100,
  },
  {
    field: "domnGrpNm",
    headerName: translateMessage("domain.table.domn_grp_nm"),
  },
  {
    field: "domnEngNm",
    headerName: translateMessage("domain.table.domn_eng_nm"),
  },
  {
    field: "domnDivsNm",
    headerName: translateMessage("domain.table.domn_divs_nm"),
  },
  {
    field: "domnLen",
    headerName: translateMessage("domain.table.domn_len"),
  },
  {
    field: "rgstUsr",
    headerName: translateMessage("domain.table.rgst_usr"),
    hide: props.isPopup,
  },
  {
    field: "rgstDtm",
    headerName: translateMessage("domain.table.rgst_dtm"),
    hide: props.isPopup,
  },
  {
    field: "updDtm",
    headerName: translateMessage("domain.table.upd_dtm"),
    hide: props.isPopup,
  },
]);

// Update column headers
// const updateColumnHeaders = () => {
//   columnDefs.value.forEach((col: any) => {
//     col.headerName = translateMessage(`domain.table.${col.field}`);
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
const handleShowModalAddWo = async () => {
  const objectModal: any = {
    title: translateMessage("term.COMMV001P.title"),
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

const showModalAddOrUpdateDo = async (isAddNew: boolean = true) => {
  const objectModal: any = {
    title: translateMessage("domain.add.title"),
    component: COMMD001P,
    dataInput: isAddNew ? {} : { ...getSelectedRow() },
    width: "600",
  };
  await globalStore.openModal(objectModal);
};

const showModalAddVo = async () => {
  const objectModal: any = {
    title: translateMessage("term.COMMV001P.title"),
    component: COMMV001P,
    dataInput: {},
    width: "600",
  };
  await globalStore.openModal(objectModal);
};

const onGridReady = (params: any) => {
  gridApi.value = params.api;
};

const onRowDoubleClicked = () => {
  if (props.isPopup) {
    emit("doubleClicked", getSelectedRow());
    return;
  }
  showModalAddOrUpdateDo(false);
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
        @click="handleShowModalAddWo"
        >{{ $t(`term.lbl_add_vocab`) }}</v-btn
      >
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddVo"
        >{{ $t(`term.lbl_add_term`) }}</v-btn
      >
      <v-btn
        size="large"
        variant="outlined"
        density="comfortable"
        @click="showModalAddOrUpdateDo"
        >{{ $t(`term.lbl_add_domain`) }}</v-btn
      >
      <v-btn size="large" variant="outlined" density="comfortable">{{
        $t(`term.lbl_add_code`)
      }}</v-btn>
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
