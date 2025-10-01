<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useGlobal } from "@/store";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import axios from "axios";
import SysCdPage from "@/pages/solution/SystemPage.vue";
import OrderEventPage from "@/pages/solution/OrderEventPage.vue";
import CfInput from "@/components/controls/CfInput.vue";

const globalStore = useGlobal();

const workCd = ref({ key: "ordr", value: "ordr" });
workCd.value = workCd.value.key;
const sndSysCd = ref("");
const rcpSysCd = ref("");
const evetCd = ref("");
const detlCd = ref("");
const formSearch = ref<any>(null);
const rowData_evetItem = ref([]);
const gridApi_evetItem = ref(null);
const editType_evetItem = ref("fullRow");
const evetItemTotal = ref(0);

// 초기화
const resetData = () => {
  evetItemTotal.value = 0;
  rowData_evetItem.value = [];
};

// 업무구분
const handleWorkCdUpdate = (value: any) => {
  workCd.value = value;
  evetItemTotal.value = 0;
  rowData_evetItem.value = [];
};

// 송신시스템코드 inputvalue
const handleSndSysCdUpdate = (value: string) => {
  sndSysCd.value = value.toUpperCase();
};

const sndSysCdUpdate = (event) => {
  sndSysCd.value = event.target.value.toUpperCase();
};

// 수신시스템코드 inputvalue
const handleRcpSysCdUpdate = (value: string) => {
  debugger;
  rcpSysCd.value = value.toUpperCase();
};

const rcpSysCdUpdate = (event) => {
  rcpSysCd.value = event.target.value.toUpperCase();
};

// 이벤트코드 inputvalue
const handleEvetCdUpdate = (value: string) => {
  evetCd.value = value.toUpperCase();
};

const evetCdUpdate = (event) => {
  evetCd.value = event.target.value.toUpperCase();
};

const updateSndSysCdValue = (value: string) => {
  if (!value) return;
  debugger;
  sndSysCd.value = value.toUpperCase();
};

const updateRcpSysCdValue = (value: string) => {
  if (!value) return;
  rcpSysCd.value = value.toUpperCase();
};

const updateEvetCdCdValue = async (value: string) => {
  if (!value) return;
  evetCd.value = value;
};

// 송신 시스템코드 돋보기 버튼 클릭 시
const searchSndSysCd = async () => {
  await formSearch.value.resetValidation();
  const objectModal: any = {
    title: "시스템 관리",
    component: SysCdPage,
    dataInput: {
      sysCd: sndSysCd.value,
      workType: workCd.value,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data) {
    updateSndSysCdValue(data.sysCd);
  }
};

// 수신 시스템코드 돋보기 버튼 클릭 시
const searchRcpSysCd = async () => {
  const objectModal: any = {
    title: "시스템관리",
    component: SysCdPage,
    dataInput: {
      sysCd: rcpSysCd.value,
      workType: workCd.value,
      screenType: "popUp",
    },
    width: "900",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  if (data) {
    updateRcpSysCdValue(data.sysCd);
  }
};

const searchOrdereventCd = async () => {
  const objectModal: any = {
    title: "이벤트 관리",
    component: OrderEventPage,
    dataInput: {
      evetCd: evetCd.value,
      workType: workCd.value,
      screenType: "popUp",
    },
    width: "1200",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  if (data) {
    updateEvetCdCdValue(data.evetCd);
    evetCd.value = data.evetCd;
  }
};

// 이벤트항목컬럼
const columnDefs_evetItem = ref([
  { field: "sndSysCd", headerName: "SenderSystem", flex: 1 },
  { field: "rcpSysCd", headerName: "ReceptionSystem", flex: 1 },
  { field: "evetCdNm", headerName: "Event", flex: 1.5 },
  { field: "evetDetlCdNm", headerName: "EventDetail", flex: 1.5 },
]);

// 검색버튼 클릭 시
const handleSearch = async () => {
  let arrEvetItem = new Array();
  let rowData = new Object();
  rowData_evetItem.value = [];
  let response = "";

  if (sndSysCd.value === "") {
    const objectAlert: any = {
      text: "The Sender system is essential.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  } else if (rcpSysCd.value === "") {
    const objectAlert: any = {
      text: "The Reception system is essential.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  const { valid } = await formSearch.value.validate();

  if (valid) {
    let baseUrl = "";
    if (workCd.value === "ordr") {
      baseUrl =
        "http://dev.service-billing.com/ordr/ordrevetitem/v1/evetitemevet?";
    } else if (workCd.value === "cust") {
      baseUrl =
        "http://dev.service-billing.com/cust/custevetitem/v1/evetitemevet?";
    } else {
      const objectAlert: any = {
        text: "URL을 다시 확인해 주세요.",
        width: "500",
      };

      const result = await globalStore.openAlertMessage(objectAlert);
      if (result) {
        resetData();
      }
      return;
    }

    try {
      if (workCd.value === "ordr") {
        response = await axios.get(
          ` ${baseUrl}sndSysCd=${sndSysCd.value}&rcpSysCd=${rcpSysCd.value}&ordrEvetCd=${evetCd.value}`
        );
      } else if (workCd.value === "cust") {
        response = await axios.get(
          ` ${baseUrl}sndSysCd=${sndSysCd.value}&rcpSysCd=${rcpSysCd.value}&ordrEvetCd=${evetCd.value}`
        );
      }

      if (response.status === 200) {
        if (response.data.errorCode === "400") {
          const objectAlert: any = {
            text: response.data.errorMsg,
            width: "500",
          };

          await globalStore.openAlertMessage(objectAlert);
          return;
        }

        // total 셋팅
        evetItemTotal.value = response.data?.length;

        if (workCd.value === "ordr") {
          for (let i = 0; i < evetItemTotal.value; i++) {
            rowData = new Object();
            rowData.sndSysCd = response.data[i as number].ordrEvetDto.sndSysCd;
            rowData.rcpSysCd = response.data[i as number].ordrEvetDto.rcpSysCd;
            rowData.evetCdNm =
              response.data[i as number].ordrEvetDto.ordrEvetCd +
              "(" +
              response.data[i as number].ordrEvetDto.ordrEvetCdNm +
              ")";
            rowData.evetDetlCdNm =
              response.data[i as number].ordrEvetDto.ordrEvetDetlCd +
              "(" +
              response.data[i as number].ordrEvetDto.ordrEvetDetlCdNm +
              ")";
            arrEvetItem.push(rowData);
          }
        } else if (workCd.value === "cust") {
          for (let i = 0; i < evetItemTotal.value; i++) {
            rowData = new Object();
            rowData.sndSysCd = response.data[i as number].custEvetDto.sndSysCd;
            rowData.rcpSysCd = response.data[i as number].custEvetDto.rcpSysCd;
            rowData.evetCdNm =
              response.data[i as number].custEvetDto.custEvetCd +
              "(" +
              response.data[i as number].custEvetDto.custEvetCdNm +
              ")";
            rowData.evetDetlCdNm =
              response.data[i as number].custEvetDto.custEvetDetlCd +
              "(" +
              response.data[i as number].custEvetDto.custEvetDetlCdNm +
              ")";
            arrEvetItem.push(rowData);
          }
        } else {
          arrEvetItem.push(rowData);
        }
        rowData_evetItem.value = arrEvetItem;
      }
    } catch (error) {
      console.error(error);
    }
  }
};

const handleExecuteButton = async () => {};

const handleResetButton = async () => {};

const onEvetItemGridReady = (params: any) => {
  gridApi_evetItem.value = params.api;
};
</script>

<template>
  <div class="p-4">
    <h1 class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">
      Order Management
    </h1>

    <div class="mt-4" style="height: auto">
      <div class="flex">
        <label class="pt-5">workType :</label>
        <div class="w-[150px] pl-2">
          <cf-select-box
            :items="[
              { key: 'cust', value: 'cust' },
              { key: 'ordr', value: 'ordr' },
            ]"
            class="custom-file-input mt-3"
            variant="solo"
            item-title="value"
            item-value="key"
            :model="workCd"
            @update:model="handleWorkCdUpdate"
          ></cf-select-box>
        </div>
      </div>
    </div>

    <div class="flex items-center" style="height: auto; flex-direction: row">
      <div class="flex gap-4 items-center" style="flex-grow: 1">
        <span
          ><span style="color: #ff0404; font-weight: bold">*</span>SendSystem
          :</span
        >
        <v-form ref="formSearch">
          <div class="flex w-[100px] h-[40px]">
            <cf-input
              id="sndSysCd"
              v-model="sndSysCd"
              class="custom-height-input"
              variant="outlined"
              color="#E0E0E0"
              base-color="#E0E0E0"
              :model="sndSysCd"
              specialaction="toUpperCase"
              maxlength="20"
              autocomplete="off"
              oninput="javascript: this.value = this.value.replace(/[^a-zA-Z0-9]/g, '' );"
              @update:model="handleSndSysCdUpdate"
              @input="sndSysCdUpdate"
            ></cf-input>
          </div>
        </v-form>
        <v-btn size="40" @click="searchSndSysCd">
          <span class="i-mdi-search" style="font-size: 24px"></span>
        </v-btn>
        <span
          ><span style="color: #ff0404; font-weight: bold">*</span>ReceiptSystem
          :</span
        >
        <div class="flex w-[100px] h-[40px]">
          <cf-input
            id="rcpSysCd"
            v-model="rcpSysCd"
            class="custom-height-input"
            variant="outlined"
            color="#E0E0E0"
            base-color="#E0E0E0"
            :model="rcpSysCd"
            special-action="toUpperCase"
            maxlength="20"
            autocomplete="off"
            oninput="javascript: this.value = this.value.replace(/[^a-zA-Z0-9]/g, '' );"
            @update:model="handleRcpSysCdUpdate"
            @input="rcpSysCdUpdate"
          ></cf-input>
        </div>
        <v-btn size="40" @click="searchRcpSysCd">
          <span class="i-mdi-search" style="font-size: 24px"></span>
        </v-btn>
        <span>Event :</span>
        <div class="flex w-[100px] h-[40px]">
          <cf-input
            id="evetCd"
            v-model="evetCd"
            class="custom-height-input"
            variant="outlined"
            color="#E0E0E0"
            base-color="#E0E0E0"
            :model="evetCd"
            special-action="toUpperCase"
            maxlength="10"
            autocomplete="off"
            oninput="javascript: this.value = this.value.replace(/[^a-zA-Z0-9]/g, '' );"
            @update:model="handleEvetCdUpdate"
            @input="evetCdUpdate"
          ></cf-input>
        </div>
        <v-btn size="40" @click="searchOrdereventCd">
          <span class="i-mdi-search" style="font-size: 24px"></span>
        </v-btn>
        <div class="flex w-[100px] h-[40px]">
          <cf-input
            id="detlCd"
            v-model="detlCd"
            class="custom-height-input"
            variant="outlined"
            color="#E0E0E0"
            base-color="#E0E0E0"
            :model="detlCd"
            disabled="true"
          ></cf-input>
        </div>
      </div>
      <div style="justify-content: flex-end">
        <cf-button class="search-button" label="Search" @click="handleSearch" />
      </div>
    </div>

    <div>
      <span class="total-text">Total : {{ evetItemTotal }}</span>
    </div>

    <div class="flex justify-between">
      <div>
        <div>
          <ag-grid-vue
            style="width: 540px; height: 388px"
            class="ag-theme-alpine"
            :column-defs="columnDefs_evetItem"
            :row-data="rowData_evetItem"
            :edit-type="editType_evetItem"
            row-selection="single"
            @grid-ready="onEvetItemGridReady"
          ></ag-grid-vue>
        </div>
        <div>
          <span class="font-extrabold">Event Parameter Example Value</span>
        </div>
        <div>
          <cf-textarea
            style="width: 540px; height: auto"
            class="custom-height-event"
            variant="outlined"
            readonly
          ></cf-textarea>
        </div>
      </div>
      <div class="flex" style="flex-direction: column">
        <div>
          <cf-textarea
            style="width: 540px; height: auto"
            class="custom-height-request-body"
            label="Request Body"
            variant="outlined"
            readonly
          ></cf-textarea>
        </div>

        <div class="flex justify-between pt-1">
          <div>
            <cf-button
              class="custom-button"
              label="Execute"
              @click="handleExecuteButton"
            />
          </div>
          <div>
            <cf-button
              class="custom-button"
              label="Reset"
              @click="handleResetButton"
            />
          </div>
        </div>
        <div style="margin-top: auto">
          <div>
            <cf-textarea
              style="width: 540px; height: auto"
              class="custom-height-responses-body"
              label="Responses Body"
              variant="outlined"
              readonly
            ></cf-textarea>
          </div>
          <div>
            <cf-textarea
              style="width: 540px; height: auto"
              class="custom-height-responses-code"
              variant="outlined"
              label="Responses Code"
              readonly
            ></cf-textarea>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.search-button {
  background: black;
  width: 100px;
  height: 40px !important;
}
.search-button:hover {
  background: #4d4d4d;
}
.total-text {
  margin-bottom: -123px;
  font-size: 13px;
}
.custom-file-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  padding: 10px 8px 16px 16px;
  height: 40px;
  min-height: 0px;
  border-radius: 8px;
}
.custom-file-input :deep(.mdi-menu-down::before) {
  content: "\F0140" !important;
}
.custom-height-request-body
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 493px;
}
.custom-height-request-body :deep(.v-input__details) {
  display: none;
}
.custom-height-responses-body
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 132px;
}
.custom-height-responses-code
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 58px;
}
.custom-height-responses-code :deep(.v-input__details) {
  display: none;
}
.custom-height-event
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 340px;
}
.custom-height-event :deep(.v-input__details) {
  display: none;
}
.custom-button {
  color: #000000;
  background: #ffffff;
  border: 1px solid #828282;
  width: 268px;
  height: 40px;
}
.custom-button:hover {
  color: #000000;
  background: #dddddd;
  border: 1px solid #828282;
}
.custom-height-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 40px;
  width: 100px;
  min-height: 0px;
  min-width: 0px;
  border: 1px solid #e0e0e0;
}
</style>
padding-top
