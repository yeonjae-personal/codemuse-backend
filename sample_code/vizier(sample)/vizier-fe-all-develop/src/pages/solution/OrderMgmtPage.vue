<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useGlobal } from "@/store";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import axios from "axios";
import SysCdPage from "@/pages/solution/SystemPage.vue";
import OrderEventPage from "@/pages/solution/OrderEventPage.vue";
import DetailLogManagementModal from "@/pages/solution/popup/DetailLogManagementModal.vue";
import CfInput from "@/components/controls/CfInput.vue";
import CfDateRangePicker from "@/components/controls/CfDateRangePicker.vue";

const globalStore = useGlobal();

const workCd = ref({ key: "ordr", value: "ordr" });
workCd.value = workCd.value.key;
const sndSysCd = ref("");
const rcpSysCd = ref("");
const evetCd = ref("");
const encnId = ref();
const entrId = ref();
const statCd = ref({ key: "", value: "All" });
statCd.value = statCd.value.key;
const strtDt = ref("");
const endDt = ref("");
const formSearch = ref<any>(null);
const dateRange = ref();
dateRange.value = [new Date(), new Date()];
const rowData_evetItem = ref([]);
const gridApi_evetItem = ref(null);
const editType_evetItem = ref("fullRow");
const evetItemTotal = ref(0);
let flagInitDate = false;
let flagOrder = true;

function formatDateToYYYYMMDD(dateString: any) {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

strtDt.value = formatDateToYYYYMMDD(new Date());
endDt.value = formatDateToYYYYMMDD(new Date());

const formatDateValue = (params: any) => {
  if (!params.value) {
    return "";
  }
  return params.value.replace("T", " ");
};

// 초기화
const resetData = () => {
  evetItemTotal.value = 0;
  rowData_evetItem.value = [];
  statCd.value = "";
  flagInitDate = !flagInitDate;
  handleUpdateDt([new Date(), new Date()]);
};

// 업무구분
const handleWorkCdUpdate = (value: any) => {
  workCd.value = value;
  evetItemTotal.value = 0;
  rowData_evetItem.value = [];
  statCd.value = "";
  flagOrder = !flagOrder;
  flagInitDate = !flagInitDate;
  handleUpdateDt([new Date(), new Date()]);
  updateColumnHeaders();
};

//ProcessStatus
const handleStatCdUpdate = (value: any) => {
  statCd.value = value;
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

const handleEncnId = (val: number) => {
  encnId.value = val;
};

const encnIdUpdate = (event: any) => {
  encnId.value = event.target.value;
};

const handleEntrId = (val: number) => {
  entrId.value = val;
};

const entrIdUpdate = (event: any) => {
  entrId.value = event.target.value;
};

const handleUpdateDt = (val: any) => {
  if (val) {
    strtDt.value = formatDateToYYYYMMDD(val[0]);
    endDt.value = formatDateToYYYYMMDD(val[1]);
  }
  dateRange.value = val;
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
  {
    checkboxSelection: true,
    headerCheckboxSelection: true,
    headerCheckboxSelectionFilteredOnly: true,
    width: 30,
  },
  { field: "sndSysCd", headerName: "SenderSystem", width: 130 },
  { field: "rcpSysCd", headerName: "ReceptionSystem", width: 150 },
  { field: "evetCdNm", headerName: "Event", width: 130 },
  { field: "evetDetlCdNm", headerName: "EventDetail", width: 130 },
  { field: "encnId", headerName: "EntryContractId", width: 140 },
  { field: "entrId", headerName: "ContractId", width: 130 },
  { field: "statCd", headerName: "Status", width: 130 },
  {
    field: "dtm",
    headerName: "ProcessDateTime",
    valueFormatter: formatDateValue,
    width: 170,
  },
  {
    field: "chanDtm",
    headerName: "ChangeDateTime",
    valueFormatter: formatDateValue,
    width: 170,
  },
  { field: "prssRsltCntn", headerName: "ProcessResultContent", width: 200 },
  { field: "failRsnCntn", headerName: "FailureReasonContent", width: 200 },
]);

const updateColumnHeaders = () => {
  if (flagOrder) {
    columnDefs_evetItem.value[5].headerName = "EntryContractId";
    columnDefs_evetItem.value[6].headerName = "ContractId";
  } else {
    columnDefs_evetItem.value[5].headerName = "CustomerId";
    columnDefs_evetItem.value[6].headerName = "BillAccountId";
  }
};

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
  } else if (dateRange.value == null) {
    const objectAlert: any = {
      text: "The Process Date is essential.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  const { valid } = await formSearch.value.validate();

  if (valid) {
    let baseUrl = "";
    if (workCd.value === "ordr") {
      baseUrl = "http://dev.service-billing.com/ordr/ordrevet/v1/ordrpgrs?";
    } else if (workCd.value === "cust") {
      baseUrl = "http://dev.service-billing.com/cust/custevet/v1/custordrpgrs?";
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
          ` ${baseUrl}sndSysCd=${sndSysCd.value}&rcpSysCd=${rcpSysCd.value}&ordrEvetId=${evetCd.value}&encnId=${encnId.value}
&entrId=${entrId.value}&ordrStatCd=${statCd.value}&prssStrtDt=${strtDt.value}&prssEndDt=${endDt.value}`
        );
      } else if (workCd.value === "cust") {
        response = await axios.get(
          ` ${baseUrl}sndSysCd=${sndSysCd.value}&rcpSysCd=${rcpSysCd.value}&custEvetId=${evetCd.value}&custId=${encnId.value}
&billAcntId=${entrId.value}&ordrStatCd=${statCd.value}&prssStrtDt=${strtDt.value}&prssEndDt=${endDt.value}`
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
            rowData.ordrId = response.data[i as number].ordrId;
            rowData.evetItemId = response.data[i as number].ordrEvetItemId;
            rowData.sndSysCd = response.data[i as number].sndSysCd;
            rowData.rcpSysCd = response.data[i as number].rcpSysCd;
            rowData.evetCdNm =
              response.data[i as number].ordrEvetCdNm +
              "(" +
              response.data[i as number].ordrEvetCd +
              ")";
            rowData.evetDetlCdNm =
              response.data[i as number].ordrEvetDetlCdNm +
              "(" +
              response.data[i as number].ordrEvetDetlCd +
              ")";
            rowData.encnId = response.data[i as number].encnId;
            rowData.entrId = response.data[i as number].entrId;
            rowData.statCd = response.data[i as number].ordrStatCd;
            rowData.dtm = response.data[i as number].ordrDtm;
            rowData.chanDtm = response.data[i as number].ordrChanDtm;
            rowData.prssRsltCntn = response.data[i as number].ordrPrssRsltCntn;
            rowData.failRsnCntn = response.data[i as number].ordrFailRsnCntn;
            arrEvetItem.push(rowData);
          }
        } else if (workCd.value === "cust") {
          for (let i = 0; i < evetItemTotal.value; i++) {
            rowData = new Object();
            rowData.ordrId = response.data[i as number].custOrdrId;
            rowData.evetItemId = response.data[i as number].custEvetItemId;
            rowData.sndSysCd = response.data[i as number].sndSysCd;
            rowData.rcpSysCd = response.data[i as number].rcpSysCd;
            rowData.evetCdNm =
              response.data[i as number].custEvetCdNm +
              "(" +
              response.data[i as number].custEvetCd +
              ")";
            rowData.evetDetlCdNm =
              response.data[i as number].custEvetDetlCdNm +
              "(" +
              response.data[i as number].custEvetDetlCd +
              ")";
            rowData.encnId = response.data[i as number].encnId;
            rowData.entrId = response.data[i as number].entrId;
            rowData.statCd = response.data[i as number].ordrStatCd;
            rowData.dtm = response.data[i as number].ordrDtm;
            rowData.chanDtm = response.data[i as number].ordrChanDtm;
            rowData.prssRsltCntn =
              response.data[i as number].custOrdrPrssRsltCntn;
            rowData.failRsnCntn =
              response.data[i as number].custOrdrFailRsnCntn;
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

const onEvetItemGridReady = (params: any) => {
  gridApi_evetItem.value = params.api;
};

// When click the DetailLog button
const handleDetailLogButton = async () => {
  const selectedRows = gridApi_evetItem.value.getSelectedRows();
  let titlePopup = "";

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  if (workCd.value === "ordr") {
    titlePopup = "Order Detail Log Management";
  } else {
    titlePopup = "Customer Detail Log Management";
  }

  const objectModal: any = {
    title: titlePopup,
    iconClose: true,
    component: DetailLogManagementModal,
    dataInput: {
      dataRow: selectedRows,
      workCd: workCd.value,
    },
    width: "1216",
    height: "800",
    type: "custom",
  };
  await globalStore.openModal(objectModal);
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

    <div class="flex flex-col gap-4">
      <div class="flex gap-4 items-center">
        <span
          ><span style="color: #ff0404; font-weight: bold">*</span>SendSystem
          :</span
        >

        <v-form ref="formSearch">
          <div class="flex w-[100px]">
            <cf-input
              id="sndSysCd"
              v-model="sndSysCd"
              label=""
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
        <span>ReceiptSystem :</span>
        <div class="flex w-[100px]">
          <cf-input
            id="rcpSysCd"
            v-model="rcpSysCd"
            label=""
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
        <div class="flex w-[100px]">
          <cf-input
            id="evetCd"
            v-model="evetCd"
            label=""
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
      </div>
    </div>

    <div class="row-span-12 mt-4 flex" style="height: auto">
      <div class="row-span-12 flex" style="height:">
        <div class="flex">
          <label v-if="flagOrder" for="encnId" class="pt-5"
            >EntryContractId :</label
          >
          <label v-else for="encnId" class="pt-5">CustomerId :</label>
          <div class="custom-height">
            <cf-input
              v-model="encnId"
              class="pl-2 pt-3"
              :model="encnId"
              oninput="javascript: this.value = this.value.replace(/[^0-9]/g, '' );"
              maxlength="12"
              variant="undefined"
              autocomplete="off"
              @update:model="handleEncnId"
              @input="encnIdUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="flex">
          <label v-if="flagOrder" for="entrId" class="pt-5 pl-2"
            >ContractId :</label
          >
          <label v-else for="entrId" class="pt-5 pl-2">BillAccountId :</label>
          <div class="custom-height">
            <cf-input
              v-model="entrId"
              class="pl-2 pt-3"
              :model="entrId"
              oninput="javascript: this.value = this.value.replace(/[^0-9]/g, '' );"
              maxlength="12"
              variant="undefined"
              @update:model="handleEntrId"
              @input="entrIdUpdate"
            ></cf-input>
          </div>

          <div class="flex">
            <label class="pt-5 pl-2">ProcessStatus :</label>
            <div class="w-[100px] pl-2">
              <cf-select-box
                v-model="statCd"
                :items="[
                  { key: '', value: 'All' },
                  { key: 'S', value: 'Success' },
                  { key: 'F', value: 'Failure' },
                  { key: 'R', value: 'Receipt' },
                ]"
                class="custom-file-input mt-3"
                variant="solo"
                item-title="value"
                item-value="key"
                @update:model="handleStatCdUpdate"
              ></cf-select-box>
            </div>
          </div>

          <div class="flex">
            <label class="pt-5 pl-2"
              ><span style="color: #ff0404; font-weight: bold">*</span
              >ProcessDate :</label
            >
            <div class="pt-3 pl-2">
              <CfDateRangePicker
                v-model="dateRange"
                :model="dateRange"
                :is-init-date="flagInitDate"
                class="w-[220px] h-[40px]"
                variant="outlined"
                @update:model="handleUpdateDt"
              ></CfDateRangePicker>
            </div>
          </div>
        </div>
      </div>
      <div class="flex justify-end">
        <div class="flex float-right pt-3 pl-2">
          <cf-button
            class="search-button"
            label="Search"
            @click="handleSearch"
          />
        </div>
      </div>
    </div>

    <div class="flex items-center">
      <span class="total-text">Total : {{ evetItemTotal }}</span>
      <div class="flex gap-2 items-center" style="margin-left: auto">
        <cf-button
          class="popup-button"
          label="DetailLog"
          @click="handleDetailLogButton"
        />
      </div>
    </div>
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
        row-selection="single"
        @grid-ready="onEvetItemGridReady"
      ></ag-grid-vue>
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

.popup-button {
  color: #000000;
  background: #ffffff;
  border: 1px solid #828282;
  margin-bottom: 10px;
  margin-top: 108px;
  height: 40px;
}
.popup-button:hover {
  color: #000000;
  background: #dddddd;
  border: 1px solid #828282;
}

.total-text {
  margin-bottom: -123px;
  font-size: 13px;
}
.custom-height
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 40px;
  width: 100px;
  min-height: 0px;
  min-width: 0px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 5px !important;
  max-width: 100px;
}

.custom-height :deep(.v-input__details .v-messages__message) {
  max-width: 100px;
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
</style>
