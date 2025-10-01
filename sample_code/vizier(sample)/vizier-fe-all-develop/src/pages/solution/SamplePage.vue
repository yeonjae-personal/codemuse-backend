<template>
  <div>
    <div class="grid-container">
      <ag-grid-vue
        style="width: 100%; height: 330px"
        class="ag-theme-alpine"
        :column-defs="columnDefs_evetItem"
        :row-data="rowData_evetItem"
        :edit-type="editType_evetItem"
        :pagination="true"
        :pagination-page-size-selector="[5, 10, 20]"
        :pagination-page-size="5"
        :row-drag-managed="true"
        :suppress-move-when-row-dragging="true"
      ></ag-grid-vue>
    </div>
    <div class="grid-container">
      <ag-grid-vue
        style="width: 100%; height: 330px"
        class="ag-theme-alpine"
        :column-defs="columnDefs_evetFunc"
        :row-data="rowData_evetFucn"
        row-selection="multiple"
        :edit-type="editType_evetFucn"
        :row-drag-managed="true"
        :suppress-move-when-row-dragging="true"
      ></ag-grid-vue>
    </div>
  </div>
</template>
<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

const rowData_evetItem = ref([]);
const rowData_evetFucn = ref([]);
const editType_evetItem = ref("fullRow");
const editType_evetFucn = ref("fullRow");

let retData;

onMounted(async () => {
  // 이벤트아이템 조회
  retData = await CommonOrdrUtil.execute(
    "GET",
    "/api/ordr/ordrevetitem/v1/evetitemevet",
    {
      sndSysCd: "WOO",
      rcpSysCd: "LGT",
      ordrEvetCd: "NAC",
      // sndSysCd: null,
      // rcpSysCd: null,
      // ordrEvetCd: null,
    }
  );

  console.log(retData);
});

const columnDefs_evetItem = ref([
  {
    checkboxSelection: true,
    headerCheckboxSelection: true,
    headerCheckboxSelectionFilteredOnly: true,
    width: 30,
  },
  { field: "sndSysCd", headerName: "송신시스템코드" },
  { field: "rcpSysCd", headerName: "수신시스템코드" },
  { field: "evetCd", headerName: "이벤트코드" },
  { field: "evetCdNm", headerName: "이벤트코드명" },
  { field: "evetDetlCd", headerName: "이벤트상세코드" },
  { field: "evetDetlCdNm", headerName: "이벤트상세코드명" },
  { field: "callMthd", headerName: "호출방식" },
  { field: "validStartDtm", headerName: "유효시작일시" },
  { field: "validEndDtm", headerName: "유효종료일시" },
]);

const columnDefs_evetFunc = ref([
  {
    //checkboxSelection: true,
    //headerCheckboxSelection: true,
    //headerCheckboxSelectionFilteredOnly: true,
    width: 30,
  },
  { field: "evetFuncOdr", headerName: "이벤트기능순서" },
  { field: "funcClss", headerName: "기능클래스" },
  { field: "funcMtho", headerName: "기능메소드" },
  { field: "funcDscr", headerName: "기능설명" },
  { field: "retYn", headerName: "반환여부" },
  { field: "validStartDtm", headerName: "유효시작일시" },
  { field: "validEndDtm", headerName: "유효종료일시" },
]);
</script>
