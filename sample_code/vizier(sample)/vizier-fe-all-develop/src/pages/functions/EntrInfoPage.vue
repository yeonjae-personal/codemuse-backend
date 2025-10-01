<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

const searchKeywords = ref({
  encnId: "",
  prodNo: "",
});
const custInfo = ref({
  custNm: "김유진",
  custGender: "여",
  custAge: 26,
  custRnno: "991103-2******",
  custKd: "개인",
  custId: "32404510849203",
});

const columnDefs = ref([
  {
    field: "svcNm",
    headerName: "서비스",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.svcNm;
    },
  },
  {
    field: "ppNm",
    headerName: "요금제",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.ppNm;
    },
  },
  {
    field: "billAcntId",
    headerName: "청구계정번호",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.billAcntId;
    },
  },
  {
    field: "prodNo",
    headerName: "상품번호",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.prodNo;
    },
  },
  {
    field: "stat",
    headerName: "상태",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.stat;
    },
  },
  {
    field: "detl",
    headerName: "상세",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.detl;
    },
  },
  {
    field: "realUserNm",
    headerName: "실사용자명",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.realUserNm;
    },
  },
  {
    field: "frstSbgnDt",
    headerName: "최초개통일",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.frstSbgnDt;
    },
  },
  {
    field: "statChanDt",
    headerName: "상태변경일",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.statChanDt;
    },
  },
  {
    field: "encnId",
    headerName: "가입계약번호",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.encnId;
    },
  },
  {
    field: "entrId",
    headerName: "가입번호",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.entrId;
    },
  },
  {
    field: "custId",
    headerName: "고객번호",
    cellStyle: { textAlign: "center" },
    width: 100,
    tooltipValueGetter: function (params) {
      return params.data.custId;
    },
  },
]);
const rowData = ref([]);
const gridApi = ref(null);
const onGridReady = (params: any) => {
  gridApi.value = params.api;
};
</script>

<template>
  <div class="max-w-7xl mx-auto p-6 ml-5">
    <h1 class="text-2xl font-bold mb-10">가입정보조회</h1>
    <div class="grid grid-cols-3 gap-x-10">
      <div class="col-span-1">
        <div class="flex">
          <label
            for="encnId"
            class="block w-1/3 text-base font-normal text-[#2A2A2A] mr-5"
            >가입계약번호</label
          >

          <v-text-field
            v-model="searchKeywords.encnId"
            variant="outlined"
            color="#88A4B8"
            base-color="#88A4B8"
          ></v-text-field>
        </div>

        <div class="flex">
          <label
            for="prodNo"
            class="block w-1/3 text-base font-normal text-[#2A2A2A] mr-5"
            >상품번호</label
          >

          <v-text-field
            v-model="searchKeywords.prodNo"
            variant="outlined"
            color="#88A4B8"
            base-color="#88A4B8"
          ></v-text-field>
        </div>

        <div class="mb-5" align="right">
          <v-btn
            prepend-icon="mdi-magnify mdi-24px"
            color="#b2cee2"
            class="w-[123px] h-[34px] rounded-lg search-btn mr-2"
          >
            검색
          </v-btn>
        </div>

        <div class="space-y-5">
          <div
            class="outline outline-[#88A4B8] p-4 rounded outline-2"
            color="#88A4B8"
            base-color="#88A4B8"
          >
            <div class="text-xl font-bold mb-3">{{ custInfo.custNm }}</div>
            <span class="mr-5">{{ custInfo.custGender }}</span>
            <span class="mr-5 text-gray-300">|</span>
            <span class="mr-5">{{ custInfo.custRnno }}</span>
            <span class="mr-5 text-gray-300">|</span>
            <span class="mr-5">{{ custInfo.custAge }}세</span>
          </div>

          <div class="flex justify-between">
            <div class="font-bold">개인정보</div>
            <div>{{ custInfo.custKd }}</div>
          </div>

          <div class="flex justify-between">
            <div class="font-bold">연락처</div>
            <div> </div>
          </div>

          <div class="flex justify-between">
            <div class="font-bold">고객번호</div>
            <div>{{ custInfo.custId }}</div>
          </div>
        </div>
      </div>
      <div class="col-span-2 space-y-5">
        <div>
          <v-table class="border border-[#b2cee2] rounded"
            ><tr class="border-b h-12">
              <td class="border-r">
                <span class="font-bold mx-4">상품</span>
                <span> </span>
              </td>
              <td class="border-r">
                <span class="font-bold mx-4">청구</span>
                <span>100000000008</span>
              </td>
              <td class="border-r">
                <span class="font-bold mx-4">가입</span>
                <span>51000000103</span>
              </td>
            </tr>
            <tr class="h-12">
              <td class="border-r">
                <span class="font-bold mx-4">가입기간</span>
                <span>6년 11개월 (2532일)</span>
              </td>
              <td>
                <span class="font-bold mx-4">개통</span> <span>개통</span>
              </td>
            </tr>
          </v-table>
        </div>

        <div>
          <ag-grid-vue
            style="
              width: 100%;
              height: 150px;
              border-color: #b2cee2;
              border-radius: 8px;
            "
            class="ag-theme-alpine"
            enable-browser-tooltips="true"
            :column-defs="columnDefs"
            :row-data="rowData"
            @grid-ready="onGridReady"
          >
          </ag-grid-vue>
        </div>

        <div class="grid grid-rows-2 grid-cols-2">
          <div>
            <v-table class="border border-[#b2cee2] rounded">
              <tr class="border-b h-12">
                <td><span class="font-bold mx-4">요금제 정보</span></td>
              </tr>
              <tr class="h-12">
                <td class="flex justify-between mx-4">
                  <span class="font-bold">가입요금제</span>
                  <span>5G 데이터 플러스</span>
                </td>
              </tr>
              <tr class="h-12">
                <td class="flex justify-between mx-4">
                  <span class="font-bold">기본료</span>
                  <span>66000원</span>
                </td>
              </tr>
              <tr class="h-12">
                <td class="flex justify-between mx-4">
                  <span class="font-bold">개시일</span>
                  <span>2023-10-29</span>
                </td>
              </tr>
              <tr class="h-12">
                <td class="flex justify-between mx-4">
                  <span class="font-bold">서비스 예약 여부</span>
                  <span>X</span>
                </td>
              </tr>
            </v-table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
td {
  font-size: 16px;
}
:deep() .ag-header-cell-label {
  display: flex;
  justify-content: center;
  align-items: center;
}
.v-stepper-header .v-divider {
  margin: 0px 0px 0px -16px;
}
.v-stepper-item {
  padding: 32px 8px 32px 0px;
}
:deep() .v-stepper-header .v-stepper-item.v-stepper-item--complete .v-avatar {
  background-color: #b2cee2;
  width: 15px !important;
  height: 15px !important;
}
:deep() .v-stepper-header .v-stepper-item .v-avatar {
  border: 1px solid #b2cee2;
  background-color: white;
  width: 15px !important;
  height: 15px !important;
}
:deep()
  .v-stepper-header
  .v-stepper-item.v-stepper-item--complete
  .v-avatar
  .v-icon {
  display: none;
}
.v-btn {
  background-color: #b2cee2;
}
.flex.stepItems-center {
  display: flex;
  align-items: center;
}

.v-stepper-item--selected .v-stepper-item__avatar.v-avatar {
  background: #1d557e;
}
.v-stepper-header,
.v-stepper.v-sheet {
  box-shadow: none;
}
.ml-4 {
  margin-left: 1rem;
}
.mt-4 {
  margin-top: 1rem;
}
.custom-avt.v-avatar {
  width: 15px;
  height: 15px;
}
:deep() .v-field__input {
  min-height: 33px;
  height: 33px;
  --v-field-input-padding-top: 5px;
  padding-bottom: 5px;
  color: #2a2a2a;
  font-size: 13px;
}

:deep() .v-field input,
:deep() .v-field-active,
:deep() .v-field--focused {
  border: none;
}
:deep() .v-select .v-select__selection-text,
:deep() .v-overlay-container {
  font-size: 13px;
  font-weight: 400;
}
.custom-font-size-select :deep() .v-select__selections {
  font-size: 13px;
}

:deep() .v-overlay-container .v-list-item__title {
  font-size: 13px;
}

.custom-font-size-select :deep() .v-label {
  font-size: 13px;
}

:deep() .custom-code .text-input-code input {
  border-right: none !important;
}
:deep() .mdi-menu-down::before {
  content: "\F0140" !important;
  color: #88a4b8 !important;
}
.custom-authenticate.v-btn {
  background-color: #f3f3f3;
}
</style>
