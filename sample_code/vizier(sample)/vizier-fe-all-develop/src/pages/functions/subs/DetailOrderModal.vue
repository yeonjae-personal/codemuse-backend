<script setup lang="ts">
import { httpClient } from "@/utils/http-common";
import { useGlobal } from "@/store";
import { CommonUtil } from "@/utils/common-util";
import { AgGridVue } from "ag-grid-vue3";
import { ColDef } from "ag-grid-community";
import { RowActions } from "./common/CommonConstants";
import { OrderItem } from "@/store/order.store";
import ConfirmErrorPopup from "./ConfirmErrorPopup.vue";

const globalStore = useGlobal();
const { translateMessage } = CommonUtil.useTranslatedMessage();
const rowDt = ref<OrderItem[]>([]);
const gridApi = ref();
const dialogErr = ref(false);
const mes = ref("");
const agGrid = ref<InstanceType<typeof AgGridVue> | null>(null);
const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const ordrItemId = ref();
const totalRecord = ref(0);
const selectedIds = ref<string[]>([]);
let suppressSelectionChange = false;
const step = ref();
const confirmErr = () => {
  dialogErr.value = false;
};
const addCustomRow = () => {
  const newCustomRow: OrderItem[] = [
    {
      ordrItemDetlId: "",
      ordrItemId: `${props.data.selectedRows[0].ordrItemId}`,
      ordrItemAtvl: "atvl",
      ordrAttrEngNm: "",
      ordrAttrKornNm: "",
      dataType: "",
      rowStatCd: "C",
      isChecked: false,
      actionType: RowActions.CREATE,
    },
  ];
  rowDt.value = [...newCustomRow, ...rowDt.value];
  gridApi.value.setRowData(rowDt.value);
};

const deleteSelectedRows = async () => {
  const selectedNodes = gridApi.value.getSelectedNodes();
  selectedNodes.forEach((node: any) => {
    node.data.isChecked = true;
  });
  const selectedRows = rowDt.value.filter((row) => row.isChecked);
  console.log("selectedRows: ", selectedRows);
  if (selectedRows.length === 0) {
    globalStore.setToastInfor(
      {
        title: translateMessage("common.msg_notification"),
        text: translateMessage("system.msg_unselect_row_update"),
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "error",
        class: "bottom-center",
      },
      5000
    );
    return;
  }
  const objectAlert: any = {
    text: translateMessage("order.msg_confirm_delete"),
    width: "400",
  };
  const result = await globalStore.openAlertConfirm(objectAlert);
  if (result) {
    selectedRows.forEach(async (row: any) => {
      if (row.ordrItemDetlId) {
        row.actionType = RowActions.DELETE;
        row.rowStatCd = "D";
      } else if (row.isChecked) {
        const rowIndex = rowDt.value.findIndex((rws) => rws === row);
        if (rowIndex > -1) {
          rowDt.value.splice(rowIndex, 1);
        }
      }
    });
    gridApi.value.setRowData([...rowDt.value]);
    rowDt.value.forEach((row, index) => {
      if (row.isChecked) {
        gridApi.value.getRowNode(index).setSelected(true);
      }
    });
  } else {
    return false;
  }
};

const updateCheckedState = () => {
  rowDt.value.forEach((row: any) => {
    row.isChecked = selectedIds.value.includes(row.ordrItemDetlId);
    console.log("row.isChecked: ", row.isChecked);
  });
};

onMounted(() => {
  fetchData();
});
const fetchData = async () => {
  ordrItemId.value = props.data.selectedRows[0].ordrItemId;
  try {
    let countLength = 0;
    await httpClient
      .get(`/api/ordr/ordritem/v1/ordritemdetl?ordrItemId=${ordrItemId.value}`)
      .then((response) => {
        if (response.status == 200 && !response.data.errorCode) {
          rowDt.value =
            response.data.map((item: OrderItem) => ({
              ...item,
              isChecked: false,
            })) || [];
          countLength = response.data.length;
        }
      });
    totalRecord.value = countLength;
  } catch (error) {
    console.error(error);
  }
};
const onGridReady = (params: any) => {
  gridApi.value = params.api;
};
const gridOptions = ref({
  defaultColDef: {
    sortable: true,
    resizable: true,
  },
  suppressRowClickSelection: true,
});
const renderUpdateIcon = () => {
  return `<div>
    <span class="mdi mdi-pencil mdi-18px"></span>
    </div>`;
};

const renderCreateIcon = () => {
  return `<div>
    <span class="mdi mdi-plus mdi-18px"></span>
    </div>`;
};

const renderDeleteIcon = () => {
  return `<div>
    <span class="mdi mdi-window-close mdi-18px"></span>
    </div>`;
};

const refreshGridCells = () => {
  if (agGrid.value) {
    agGrid.value.api.refreshCells();
  }
};
const onCellEditingStopped = (event: any) => {
  if (event.valueChanged) {
    if (event.data.actionType !== RowActions.CREATE) {
      event.data.actionType = RowActions.UPDATE;
      event.data.rowStatCd = "U";
      refreshGridCells();
    }
  }
};

const onSelectionChanged = () => {
  if (suppressSelectionChange) return;
  suppressSelectionChange = true;
  selectedIds.value = [];
  const selectedNodes = gridApi.value.getSelectedNodes();
  selectedNodes.forEach((node: any) => {
    selectedIds.value.push(node.data.ordrItemDetlId);
  });

  suppressSelectionChange = false;
};

const columnDefs: ColDef<OrderItem>[] = [
  {
    headerName: "상태",
    flex: 2,
    cellStyle: { textAlign: "center", color: "#828282" },
    headerClass: "header-center",
    cellRenderer: (params: any) => {
      if (params.data.actionType && params.data.actionType === "UPDATE") {
        return renderUpdateIcon();
      } else if (
        params.data.actionType &&
        params.data.actionType === "CREATE"
      ) {
        return renderCreateIcon();
      } else if (
        params.data.actionType &&
        params.data.actionType === "DELETE"
      ) {
        return renderDeleteIcon();
      } else {
        return;
      }
    },
  },
  {
    headerCheckboxSelection: true,
    checkboxSelection: true,
    headerName: "",
    field: "isChecked",
    flex: 2,
    cellStyle: { textAlign: "center", color: "#828282" },
    cellClass: "ag-cell-center custom-checkbox",
    headerClass: "ag-header-cell-center header-custom-checkbox",
  },
  {
    field: "ordrItemAtvl",
    headerName: "오더항목",
    flex: 4,
    cellStyle: { textAlign: "center", color: "#828282" },
    headerClass: "header-center",
    cellEditor: "agTextCellEditor",
    editable: true,
    cellEditorParams: {
      maxLength: 50,
    },
  },
  {
    field: "ordrAttrEngNm",
    headerName: "오더속성명(영문)",
    flex: 4,
    cellStyle: { textAlign: "center", color: "#828282" },
    headerClass: "header-center",
    cellEditor: "agTextCellEditor",
    editable: true,
    cellEditorParams: {
      maxLength: 50,
    },
  },
  {
    field: "ordrAttrKornNm",
    headerName: "오더속성명(한글)",
    cellEditor: "agTextCellEditor",
    flex: 4,
    cellStyle: { textAlign: "center", color: "#828282" },
    headerClass: "header-center",
    editable: true,
    cellEditorParams: {
      maxLength: 50,
    },
  },
  {
    field: "dataType",
    headerName: "데이터타입",
    flex: 4,
    cellStyle: { textAlign: "center", color: "#828282" },
    headerClass: "header-center",
    cellEditor: "agTextCellEditor",
    editable: true,
    cellEditorParams: {
      maxLength: 20,
    },
  },
];
const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};
const handleSave = async () => {
  updateCheckedState();
  const selectedRows = rowDt.value.filter((row: any) => row.isChecked);
  for (let idx = 0; idx < rowDt.value.length; idx++) {
    const row = rowDt.value[idx as number];
    //validate ordrItemAtvl
    const regexOrdrItemAtvl = /^atvl\d+$/;
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !row.ordrItemAtvl
    ) {
      dialogErr.value = true;
      mes.value = "오더항목을 입력해 주세요.";
      agGrid.value?.api.startEditingCell({
        rowIndex: idx,
        colKey: "ordrItemAtvl",
      });
      return;
    }
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !regexOrdrItemAtvl.test(row.ordrItemAtvl)
    ) {
      dialogErr.value = true;
      mes.value = "atvl + 숫자만 입력 가능합니다.";
      row.ordrItemAtvl = "";
      agGrid.value?.api.stopEditing();
      agGrid.value?.api.refreshCells({
        rowNodes: [agGrid.value.api.getRowNode(idx.toString())],
      });
      setTimeout(() => {
        agGrid.value?.api.startEditingCell({
          rowIndex: idx,
          colKey: "ordrItemAtvl",
        });
      }, 100);
      return;
    }
    //validate ordrAttrEngNm
    const regexOrdrAttrEngNm = /^[A-Za-z0-9]+$/;
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !row.ordrAttrEngNm
    ) {
      dialogErr.value = true;
      mes.value = "오더속성명(영문)을 입력해 주세요.";
      agGrid.value?.api.startEditingCell({
        rowIndex: idx,
        colKey: "ordrAttrEngNm",
      });
      return;
    }
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !regexOrdrAttrEngNm.test(row.ordrAttrEngNm)
    ) {
      dialogErr.value = true;
      mes.value = "영문, 숫자만 입력 가능합니다.";
      row.ordrAttrEngNm = "";
      agGrid.value?.api.stopEditing();
      agGrid.value?.api.refreshCells({
        rowNodes: [agGrid.value.api.getRowNode(idx.toString())],
      });
      setTimeout(() => {
        agGrid.value?.api.startEditingCell({
          rowIndex: idx,
          colKey: "ordrAttrEngNm",
        });
      }, 100);
      return;
    }
    //validate ordrAttrKorNm
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !row.ordrAttrKornNm
    ) {
      dialogErr.value = true;
      mes.value = "오더속성명(영문)을 입력해 주세요.";
      agGrid.value?.api.startEditingCell({
        rowIndex: idx,
        colKey: "ordrAttrKornNm",
      });
      return;
    }
    //validate dataType
    const regexDataType = /^[A-Za-z0-9()]+$/;
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !row.dataType
    ) {
      dialogErr.value = true;
      mes.value = "데이터타입을 입력해 주세요.";
      agGrid.value?.api.startEditingCell({
        rowIndex: idx,
        colKey: "dataType",
      });
      return;
    }
    if (
      (row.actionType === RowActions.CREATE ||
        row.actionType === RowActions.UPDATE) &&
      !regexDataType.test(row.dataType)
    ) {
      dialogErr.value = true;
      mes.value = "영문, 숫자만 입력 가능합니다.";
      row.dataType = "";
      agGrid.value?.api.stopEditing();
      agGrid.value?.api.refreshCells({
        rowNodes: [agGrid.value.api.getRowNode(idx.toString())],
      });
      setTimeout(() => {
        agGrid.value?.api.startEditingCell({
          rowIndex: idx,
          colKey: "dataType",
        });
      }, 100);
      return;
    }
  }
  if (selectedRows.length > 0) {
    try {
      await httpClient
        .post(`/api/ordr/ordritem/v1/ordritemdetlprss`, selectedRows)
        .then((response) => {
          if (response.status == 200 && !response.data.errorCode) {
            globalStore.setToastInfor(
              {
                title: translateMessage("common.msg_notification"),
                text: translateMessage("order.msg_success_save"),
                border: "start",
                borderColor: "white",
                type: "success",
                icon: "$success",
                class: "bottom-center",
              },
              5000
            );
            fetchData();
          }
        });
    } catch (error) {
      console.error(error);
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("order.msg_error_save"),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
          class: "bottom-center",
        },
        5000
      );
    }
  } else {
    dialogErr.value = true;
    mes.value = "선 택된 row가 없습니다. 저장할 대상을 선택하세요​";
  }
};
</script>
<template>
  <div class="px-5 flex flex-col">
    <div class="flex justify-between items-center mb-4">
      <span class="text-base font-medium mt-[18px]"
        >Total: {{ totalRecord }}</span
      >
      <div class="flex items-center space-x-2">
        <cf-button label="+ 행추가" class="custom-btn" @click="addCustomRow" />
        <cf-button
          label="- 행삭제"
          class="custom-btn"
          @click="deleteSelectedRows"
        />
      </div>
    </div>
    <div class="overflow-x-auto">
      <ag-grid-vue
        ref="agGrid"
        style="
          width: 100%;
          height: 503px;
          border-color: #b2cee2;
          border-radius: 8px;
        "
        class="ag-theme-alpine"
        :column-defs="columnDefs"
        :row-data="rowDt"
        single-click-edit
        suppress-row-click-selection
        :grid-options="gridOptions"
        row-selection="multiple"
        @grid-ready="onGridReady"
        @cell-editing-stopped="onCellEditingStopped"
        @selection-changed="onSelectionChanged"
      >
      </ag-grid-vue>
    </div>
    <div class="flex justify-end mr-[26px] mt-[30px]">
      <cf-button label="저장" class="custom-btn" @click="handleSave" />
      <cf-button label="닫기" class="custom-btn" @click="closeModal" />
    </div>
    <ConfirmErrorPopup
      v-model:show-err="dialogErr"
      :step="step"
      :mes="mes"
      @confirm="confirmErr"
    />
  </div>
</template>

<style scoped>
.v-card-title {
  background-color: #e3e3e3;
}
.v-input__details {
  display: none;
}
.total {
  height: 24px;
  width: 124px;
}
.custom-btn {
  background-color: transparent;
  border-radius: 8px !important;
  border: 1px solid #828282;
  color: #000000;
  height: 46px !important;
  font-weight: 500;
  font-size: 20px;
  padding: 8px;
  width: 120px;
  margin-right: 10px;
}
.v-input__control {
  width: 420px;
}
.sysInput :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  border: 1px solid #d9d9d9;
  /* border-radius: 5px; */
  height: 41px !important;
  min-height: 41px;
  padding-bottom: 12px;
}
.sysInput :deep(.v-input__details) {
  min-height: 16px;
}
.cf-datepicker-input :deep(.dp--menu-wrapper) {
  top: 60px !important;
  left: 200px !important;
  margin: 0 auto;
  display: block;
  position: fixed;
  width: 320px;
  padding: 0;
}
.cf-datepicker-input :deep(.dp__arrow_bottom) {
  height: 0px !important;
  width: 0px !important;
}

.cf-datepicker-input :deep(input) {
  height: 41px !important;
}

.custom-file-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  display: flex;
  justify-content: left;
  padding: 10px 8px 16px 16px;
  height: 40px;
  min-height: 0px;
}
.custom-file-input {
  border: 1px solid #d9d9d9;
}
.custom-file-input :deep(.mdi-menu-down::before) {
  content: "\F0140" !important;
}
.search-btn,
.v-btn {
  box-shadow: none !important;
  border: 1px solid #d9d9d9;
  border-radius: 0px !important;
}
.v-btn.v-btn--density-default {
  border-radius: 4px !important;
}
:deep() .v-input__details {
  padding-top: 20px;
  min-height: 20px !important;
}
:deep() .v-input__details .v-messages__message {
  font-size: 11px !important;
}
:deep() .ag-header-cell-label {
  display: flex;
  align-items: center;
  justify-content: center;
}
:deep() .ag-cell-focus {
  border: none !important;
}
:deep() .ag-header-cell-center,
:deep() .ag-cell-center {
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep().ag-theme-alpine .ag-header-cell,
:deep() .ag-theme-alpine .ag-cell {
  padding: 0;
  /* Remove padding to perfectly center checkboxes */
}
:deep() .ag-cell-value,
:deep() .ag-cell-center,
:deep() .ag-cell-editor {
  border: 1px solid #828282 !important;
  border-top: none !important;
  box-sizing: border-box;
}
:deep() .custom-checkbox .ag-cell-value {
  display: none !important;
}
:deep() .ag-ltr .ag-header-select-all {
  margin-right: 10px !important;
}
:deep() .header-custom-checkbox .ag-header-cell-comp-wrapper {
  display: none !important;
}
:deep() .ag-input-field-input.ag-text-field-input {
  border: 1px solid red;
}
</style>
