<script setup lang="ts">
import { useGlobal } from "@/store";
import { AgGridVue } from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { CommonUtil } from "@/utils/common-util";

const { translateMessage } = CommonUtil.useTranslatedMessage();

onMounted(() => {
  fetchAttrData();
});

const InputPrmt = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const globalStore = useGlobal();
const logCntn = ref("");
const attrTotalRecord = ref(0);
const rowData_item = ref<any[]>([]);
const gridApi_item = ref<any>(null);

const workCd = InputPrmt.data.workCd;

const getBaseUrl = (): string | null => {
  if (!workCd) {
    return null;
  }
  return workCd;
};

const columnDefsDetailLog = ref([
  { field: "evetAttrKornNm", headerName: "EventAttribute(Korean)", flex: 1 },
  { field: "evetAttrEngNm", headerName: "EventAttribute(English)", flex: 1 },
  { field: "logCntn", headerName: "ProcessLogContent", flex: 2 },
]);

const fetchAttrData = async () => {
  let attrRowData: any[] = [];
  let rowData: any = {};
  rowData_item.value = [];
  const baseUrl = getBaseUrl();
  if (!baseUrl) {
    globalStore.setToastInfor(
      {
        title: translateMessage("common.msg_notification"),
        text: translateMessage("system.msg_unsearch"),
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
        class: "bottom-center",
      },
      5000
    );
    return;
  }

  try {
    let countLength = 0;
    if (workCd === "ordr") {
      const data = await CommonOrdrUtil.execute(
        "GET",
        "/api/ordr/ordr/v1/ordrlogdetl",
        {
          ordrId: InputPrmt.data.dataRow[0].ordrId,
        }
      );
      countLength = data.length;
      for (let i = 0; i < countLength; i++) {
        rowData = new Object();
        rowData.evetAttrKornNm = data[i as number].ordrEvetAttrKornNm;
        rowData.evetAttrEngNm = data[i as number].ordrEvetAttrEngNm;
        rowData.logCntn = data[i as number].ordrLogCntn;
        attrRowData.push(rowData);
      }
    } else if (workCd === "cust") {
      const data = await CommonOrdrUtil.execute(
        "GET",
        "/api/cust/custordr/v1/ordrlogdetl",
        {
          custOrdrId: InputPrmt.data.dataRow[0].ordrId,
        }
      );
      countLength = data.length;
      for (let i = 0; i < countLength; i++) {
        rowData = new Object();
        rowData.evetAttrKornNm = data[i as number].custEvetAttrKornNm;
        rowData.evetAttrEngNm = data[i as number].custEvetAttrEngNm;
        rowData.logCntn = data[i as number].custOrdrLogCntn;
        attrRowData.push(rowData);
      }
    }
    rowData_item.value = attrRowData;
    attrTotalRecord.value = countLength;
  } catch (error) {
    attrRowData = [];
  }
};

const onItemGridReady = (params: any) => {
  gridApi_item.value = params.api;
};

// row select
const getContentRowSelect = async () => {
  if (!gridApi_item.value) return;
  const gridApi_rowItem = gridApi_item.value.getSelectedRows();

  logCntn.value = "";

  if (gridApi_rowItem.length !== 0) {
    const jsonObject = JSON.parse(gridApi_rowItem[0].logCntn);
    logCntn.value = JSON.stringify(jsonObject, null, 2);
  }
};

const handleLogCntnUpdate = (value: string) => {
  logCntn.value = value;
};
</script>

<template>
  <div class="flex flex-col custom">
    <span class="total-text">Total : {{ attrTotalRecord }}</span>
    <div>
      <ag-grid-vue
        style="width: 1104px; height: 300px"
        class="ag-theme-alpine"
        :row-data="rowData_item"
        :column-defs="columnDefsDetailLog"
        row-selection="single"
        @grid-ready="onItemGridReady"
        @row-selected="getContentRowSelect"
      ></ag-grid-vue>
    </div>

    <h4 class="font-extrabold">ProcessLogContent</h4>
    <div>
      <cf-textarea
        v-model="logCntn"
        :model="logCntn"
        variant="outlined"
        class="textarea"
        rows="9"
        readonly
        @update:model-value="handleLogCntnUpdate"
      ></cf-textarea>
    </div>
  </div>
</template>

<style scoped>
.total-text {
  font-size: 13px;
}
.custom {
  padding: 0 0 0 30px;
}
.textarea {
  width: 1104px;
}
</style>
