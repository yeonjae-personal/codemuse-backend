<script lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { CommonUtil } from "@/utils/common-util";
import CfDropdown from "@/components/controls/CfDropdown.vue";
import CfInput from "@/components/controls/CfInput.vue";
import axios from "axios";
import CreateOrderEventModal from "@/pages/functions/subs/CreateOrderEventModal.vue";
import UpdateOrderEventModal from "@/pages/functions/subs/UpdateOrderEventModal.vue";
import useGlobalStore from "@/store/global.store";

export default defineComponent({
  name: "OrderEventPage",
  components: {
    CfInput,
    CfDropdown,
    AgGridVue,
  },
  props: {
    data: {
      type: Object,
      default: null,
    },
  },
  emits: ["closeDialog"],
  setup(props, { emit }) {
    const globalStore = useGlobalStore();
    const paginationNumberFormatter = ref(null);
    const ordrEvetCd = ref("");
    const ordrEvetCdNm = ref("");
    const ordrEvetDetlCdNm = ref("");
    const ordrEvetDetlCd = ref("");
    const isShowBtnCheckAndClose = ref(false);
    const isShowBtnInsAndEditAndDel = ref(true);
    const paginationPageSizeSelector: Array<number> = [];
    const workType = ref("ordr");
    const workTypeDropDown = ref<any>({ key: "ordr", value: "오더" });
    const disabledWorkType = ref(false);
    const rowData = ref<any[]>([]);
    const gridApi = ref<any>(null);
    const editType = ref("fullRow");
    const totalRecord = ref(0);
    const { translateMessage } = CommonUtil.useTranslatedMessage();
    const workTypeDefinition = [
      { key: "cust", value: "고객" },
      { key: "ordr", value: "오더" },
    ];
    const levels = ref(workTypeDefinition);

    if (props.data != null) {
      if (props.data.screenType === "popUp") {
        isShowBtnCheckAndClose.value = true;
        isShowBtnInsAndEditAndDel.value = false;
        disabledWorkType.value = true;
        ordrEvetCd.value = props.data.evetCd;
      }

      const workTypeProps = props.data.workType;
      if (typeof workTypeProps === "string") {
        workTypeDropDown.value = workTypeDefinition.filter(
          (el) => workTypeProps === el.key
        );
        workType.value = workTypeProps;
      } else {
        const actualWorkTypeValue = workTypeProps.key;
        workTypeDropDown.value = workTypeDefinition.filter(
          (el) => actualWorkTypeValue === el.key
        );
        workType.value = actualWorkTypeValue;
      }
    }

    const getBaseUrl = (): string | null => {
      if (!workType.value) {
        return null;
      }
      return workType.value;
    };
    const formatDateValue = (params: any) => {
      if (!params.value) {
        return "";
      }
      return params.value.replace("T", " ");
    };

    rowData.value = [];
    const columnDefs = ref([
      {
        field: "ordrEvetCd",
        headerName: "이벤트코드",
        flex: 3,
      },
      {
        field: "ordrEvetCdNm",
        headerName: "이벤트코드명",
        flex: 3,
      },
      {
        field: "ordrEvetDetlCd",
        headerName: "이벤트상세코드",
        flex: 3,
      },
      {
        field: "ordrEvetDetlCdNm",
        headerName: "이벤트상세코드명",
        flex: 3,
      },
      {
        field: "callMthd",
        headerName: "호출방식",
        flex: 3,
      },
      {
        field: "validStartDtm",
        headerName: "유효시작일시",
        flex: 3,
        valueFormatter: formatDateValue,
      },
      {
        field: "validEndDtm",
        headerName: "유효종료일시",
        flex: 3,
        valueFormatter: formatDateValue,
      },
    ]);

    const columnDefsCust = ref([
      {
        field: "custEvetCd",
        headerName: "이벤트코드",
        flex: 3,
      },
      {
        field: "custEvetCdNm",
        headerName: "이벤트코드명",
        flex: 3,
      },
      {
        field: "custEvetDetlCd",
        headerName: "이벤트상세코드",
        flex: 3,
      },
      {
        field: "custEvetDetlCdNm",
        headerName: "이벤트상세코드명",
        flex: 3,
      },
      {
        field: "callMthd",
        headerName: "호출방식",
        flex: 3,
      },
      {
        field: "validStartDtm",
        headerName: "유효시작일시",
        flex: 3,
        valueFormatter: formatDateValue,
      },
      {
        field: "validEndDtm",
        headerName: "유효종료일시",
        flex: 3,
        valueFormatter: formatDateValue,
      },
    ]);

    onBeforeMount(() => {
      paginationPageSizeSelector[0] = 10;
      paginationPageSizeSelector[1] = 20;
      paginationPageSizeSelector[2] = 50;
      paginationPageSizeSelector[3] = 100;
    });

    const addRow = () => {};
    const onCellValueChanged = (params: any) => {
      params.data[params.columnDefs.field] = params.value;
    };

    const updateWorkType = (val: any) => {
      workType.value = val.key;
      totalRecord.value = 0;
      rowData.value = [];
    };
    const fetchData = async () => {
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
        if (workType.value === "ordr") {
          await axios
            .get(
              `http://dev.service-billing.com/ordr/ordrevet/v1?ordrEvetCd=${ordrEvetCd.value}&ordrEvetCdNm=${ordrEvetCdNm.value}&ordrEvetDetlCd=${ordrEvetDetlCd.value}&ordrEvetDetlCdNm=${ordrEvetDetlCdNm.value}`
            )
            .then((response) => {
              if (response.status == 200 && !response.data.errorCode) {
                rowData.value = response.data;
                countLength = response.data.length;
              }
            });
        }
        if (workType.value === "cust") {
          await axios
            .get(
              `http://dev.service-billing.com/cust/custevet/v1/custevet?custEvetCd=${ordrEvetCd.value}&custEvetCdNm=${ordrEvetCdNm.value}&custEvetDetlCd=${ordrEvetDetlCd.value}&custEvetDetlCdNm=${ordrEvetDetlCdNm.value}`
            )
            .then((response) => {
              if (response.status == 200 && !response.data.errorCode) {
                rowData.value = response.data;
                countLength = response.data.length;
              }
            });
        }
        totalRecord.value = countLength;
      } catch (error) {
        rowData.value = [];
        console.error(error);
      }
    };

    const onGridReady = (params: any) => {
      gridApi.value = params.api;
    };

    const deleteSelectedRows = async () => {
      const selectedRows = gridApi.value.getSelectedRows();
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
      } else if (selectedRows.length === 0) {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("system.msg_unselect_row_delete"),
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
      const objectAlert: any = {
        title: translateMessage("common.msg_confirm"),
        text: translateMessage("system.msg_confirm_delete"),
        width: "400",
      };

      const result = await globalStore.openAlertConfirm(objectAlert);
      if (result) {
        const baseUrl = getBaseUrl();
        if (!baseUrl) {
          return;
        }
        const currentDate = new Date().toJSON().slice(0, 19);
        const requestBody: any = {
          validEndDtm: currentDate,
        };
        // 삭제할 데이터를 API로 전송
        await Promise.all(
          selectedRows.map(async (row: any) => {
            if (workType.value === "ordr") {
              requestBody.ordrEvetId = row.ordrEvetId;
              requestBody.ordrEvetCd = row.ordrEvetCd;
              requestBody.ordrEvetCdNm = row.ordrEvetCdNm;
              requestBody.ordrEvetDetlCd = row.ordrEvetDetlCd;
              requestBody.ordrEvetDetlCdNm = row.ordrEvetDetlCdNm;
              requestBody.callMthd = row.callMthd;
              requestBody.validStartDtm = row.validStartDtm;
              await axios
                .put(
                  `http://dev.service-billing.com/${baseUrl}/ordrevet/v1`,
                  requestBody
                )
                .then((response) => {
                  if (response.status == 200 && !response.data.errorCode) {
                    showToastSuccessDelete();
                    totalRecord.value = totalRecord.value - 1;
                    // API 호출이 성공하면 그리드에서 행 삭제
                    gridApi.value.applyTransaction({ remove: selectedRows });
                  } else {
                    showToastErrorDelete();
                  }
                });
            }
            if (workType.value === "cust") {
              const requestBodyCust = [
                {
                  custEvetId: row.custEvetId,
                  custEvetCd: row.custEvetCd,
                  custEvetCdNm: row.custEvetCdNm,
                  custEvetDetlCd: row.custEvetDetlCd,
                  custEvetDetlCdNm: row.custEvetDetlCdNm,
                  callMthd: row.callMthd,
                  validStartDtm: row.validStartDtm,
                  validEndDtm: currentDate,
                },
              ];

              await axios
                .post(
                  `http://dev.service-billing.com/${baseUrl}/custevet/v1/custevet`,
                  requestBodyCust
                )
                .then((response) => {
                  if (response.status == 200 && !response.data.errorCode) {
                    showToastSuccessDelete();
                    totalRecord.value = totalRecord.value - 1;
                    // API 호출이 성공하면 그리드에서 행 삭제
                    gridApi.value.applyTransaction({ remove: selectedRows });
                  } else {
                    showToastErrorDelete();
                  }
                });
            }
          })
        );
      } else {
        return false;
      }
    };
    const showToastSuccessDelete = () => {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("system.msg_success_delete"),
          border: "start",
          borderColor: "white",
          type: "success",
          icon: "$success",
          class: "bottom-center",
        },
        5000
      );
    };

    const showToastErrorDelete = () => {
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("system.msg_error_delete"),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
          class: "bottom-center",
        },
        5000
      );
    };

    const showModalCreateOrder = async () => {
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
      } else {
        const objectModal: any = {
          component: CreateOrderEventModal,
          dataInput: { workType: workType.value },
          width: "720",
          height: "650",
          type: "custom",
        };
        if (workType.value === "ordr") {
          objectModal.title = "오더이벤트 관리";
        } else {
          objectModal.title = "고객이벤트 관리";
        }
        const response = await globalStore.openModal(objectModal);
        if (response === 1) {
          fetchData();
        }
      }
    };

    const updateSelectedRows = async () => {
      const selectedRows = gridApi.value.getSelectedRows();
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
      } else if (selectedRows.length === 0) {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("system.msg_unselect_row_update"),
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
            class: "bottom-center",
          },
          5000
        );
        return;
      } else {
        const objectModal: any = {
          component: UpdateOrderEventModal,
          dataInput: { dataRow: selectedRows[0], workType: workType.value },
          width: "720",
          height: "500",
          type: "custom",
        };

        if (workType.value === "ordr") {
          objectModal.title = "오더이벤트 관리";
        } else {
          objectModal.title = "고객이벤트 관리";
        }
        const response = await globalStore.openModal(objectModal);
        if (response === 1) {
          fetchData();
        }
      }
    };

    const sysInputRules = ref([
      (value: string) => {
        if (value.length <= 10) return true;
        return translateMessage("order.msg_error_maxlength");
      },
      (value: string) => {
        if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
        return translateMessage("order.msg_error_language");
      },
    ]);

    const inputNameRules = ref([
      (value: string) => {
        if (value.length <= 50) return true;
        return translateMessage("order.msg_error_inputName_maxlength");
      },
    ]);

    const handleOrdrEvetCd = (val: string) => {
      ordrEvetCd.value = val.toUpperCase();
    };

    const handleOrdrEvetCdNm = (val: string) => {
      ordrEvetCdNm.value = val;
    };
    const handleOrdrEvetDetlCd = (val: string) => {
      ordrEvetDetlCd.value = val.toUpperCase();
    };
    const handleOrdrEvetDetlCdNm = (val: string) => {
      ordrEvetDetlCdNm.value = val;
    };

    const closeModal = () => {
      emit("closeDialog");
    };

    const sendSelectedRowToMain = () => {
      if (gridApi.value.getSelectedRows().length === 0) {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("system.msg_unselect_row"),
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
      const selectedRow = gridApi.value.getSelectedRows()[0];

      const baseUrl = getBaseUrl();
      if (baseUrl == "cust") {
        emit("closeDialog", {
          evetId: selectedRow.custEvetId,
          evetCd: selectedRow.custEvetCd,
          evetCdNm: selectedRow.custEvetCdNm,
          evetDetlCd: selectedRow.custEvetDetlCd,
          evetDetlCdNm: selectedRow.custEvetDetlCdNm,
        });
      }
      if (baseUrl == "ordr") {
        emit("closeDialog", {
          evetId: selectedRow.ordrEvetId,
          evetCd: selectedRow.ordrEvetCd,
          evetCdNm: selectedRow.ordrEvetCdNm,
          evetDetlCd: selectedRow.ordrEvetDetlCd,
          evetDetlCdNm: selectedRow.ordrEvetDetlCdNm,
        });
      }
    };
    return {
      inputNameRules,
      workTypeDefinition,
      sendSelectedRowToMain,
      closeModal,
      levels,
      columnDefs,
      rowData,
      editType,
      fetchData,
      paginationNumberFormatter,
      addRow,
      deleteSelectedRows,
      onGridReady,
      onCellValueChanged,
      showModalCreateOrder,
      updateSelectedRows,
      updateWorkType,
      sysInputRules,
      totalRecord,
      isShowBtnCheckAndClose,
      isShowBtnInsAndEditAndDel,
      formatDateValue,
      getBaseUrl,
      paginationPageSizeSelector,
      workType,
      workTypeDropDown,
      disabledWorkType,
      ordrEvetDetlCdNm,
      ordrEvetDetlCd,
      ordrEvetCd,
      ordrEvetCdNm,
      handleOrdrEvetCd,
      handleOrdrEvetCdNm,
      handleOrdrEvetDetlCd,
      handleOrdrEvetDetlCdNm,
      columnDefsCust,
    };
  },
});
</script>

<template>
  <div class="grid" style="">
    <div class="row-span-12 mt-4" style="height: auto">
      <div style="float: left" class="w-2/6 flex">
        <label class="pt-5 pl-4 text-lg">{{ $t("system.lbl_work") }}</label>
        <div class="w-[150px] pl-2">
          <cf-dropdown
            :items="workTypeDefinition"
            class="custom-file-input mt-3"
            variant="solo"
            item-title="value"
            item-value="key"
            :model="workTypeDropDown"
            :disabled="disabledWorkType"
            @update:model="updateWorkType"
          ></cf-dropdown>
        </div>
      </div>
    </div>
    <div class="row-span-12 flex" style="height: auto">
      <div class="w-11/12 flex pl-4">
        <div class="row-span-12 flex" style="height:">
          <div class="flex">
            <label for="ordrEvetCd" class="pt-3">이벤트코드 :</label>
            <div class="custom-height">
              <cf-input
                class="pl-2"
                :model="ordrEvetCd"
                :variant="undefined"
                special-action="toUpperCase"
                :rules="sysInputRules"
                @update:model="handleOrdrEvetCd"
              ></cf-input>
            </div>
          </div>
          <div class="flex">
            <label for="ordrEvetCdNm" class="pt-3 pl-2">이벤트코드명 :</label>
            <div class="custom-height">
              <cf-input
                class="pl-2"
                :model="ordrEvetCdNm"
                :variant="undefined"
                :rules="inputNameRules"
                @update:model="handleOrdrEvetCdNm"
              ></cf-input>
            </div>
          </div>

          <div class="flex">
            <label for="ordrEvetDetlCd" class="pt-3 pl-2"
              >이벤트상세코드 :</label
            >
            <div class="custom-height">
              <cf-input
                class="pl-2"
                :model="ordrEvetDetlCd"
                :variant="undefined"
                :rules="sysInputRules"
                special-action="toUpperCase"
                @update:model="handleOrdrEvetDetlCd"
              ></cf-input>
            </div>
          </div>
          <div class="flex">
            <label for="ordrEvetDetlCdNm" class="pt-3 pl-2"
              >이벤트상세코드명 :</label
            >
            <div class="custom-height">
              <cf-input
                class="pl-2"
                :model="ordrEvetDetlCdNm"
                :variant="undefined"
                :rules="inputNameRules"
                @update:model="handleOrdrEvetDetlCdNm"
              ></cf-input>
            </div>
          </div>
        </div>
      </div>
      <div class="w-1/12 flex justify-end">
        <div class="flex float-right">
          <cf-button
            label="검색"
            rounded="lg"
            class="float-right btn-ba"
            @click="fetchData"
          />
        </div>
      </div>
    </div>
    <div v-if="isShowBtnInsAndEditAndDel" class="flex">
      <div class="w-1/2 pt-9">
        <label for="input1" class="total pl-4">Total : {{ totalRecord }}</label>
      </div>
      <div class="w-1/2 flex justify-end">
        <div class="flex float-right pl-2">
          <cf-button
            label="신규"
            rounded="lg"
            class="custom-btn"
            @click="showModalCreateOrder"
          />
        </div>
        <div class="flex pl-2">
          <cf-button
            label="수정"
            rounded="lg"
            class="custom-btn"
            @click="updateSelectedRows"
          />
        </div>
        <div class="flex pl-2">
          <cf-button
            label="삭제"
            rounded="lg"
            class="custom-btn"
            @click="deleteSelectedRows"
          />
        </div>
      </div>
    </div>
    <div class="grid-container">
      <div v-if="workType === 'ordr'">
        <ag-grid-vue
          style="width: 100%; height: 520px"
          class="ag-theme-alpine pl-6"
          :column-defs="columnDefs"
          :row-data="rowData"
          row-selection="single"
          :edit-type="editType"
          :pagination="true"
          :pagination-page-size="10"
          :pagination-page-size-selector="paginationPageSizeSelector"
          @grid-ready="onGridReady"
          @cell-value-changed="onCellValueChanged"
        >
        </ag-grid-vue>
      </div>
      <div v-else>
        <ag-grid-vue
          style="width: 100%; height: 520px"
          class="ag-theme-alpine pl-6"
          :column-defs="columnDefsCust"
          :row-data="rowData"
          row-selection="single"
          :edit-type="editType"
          :pagination="true"
          :pagination-page-size="10"
          :pagination-page-size-selector="paginationPageSizeSelector"
          @grid-ready="onGridReady"
          @cell-value-changed="onCellValueChanged"
        >
        </ag-grid-vue>
      </div>
    </div>
    <div
      v-if="isShowBtnCheckAndClose"
      class="flex justify-end mt-[-30px]"
      style="height: auto"
    >
      <div class="pr-2">
        <cf-button
          label="확인"
          rounded="lg"
          class="custom-btn"
          @click="sendSelectedRowToMain"
        />
      </div>
      <div class="pr-2">
        <cf-button
          label="닫기"
          rounded="lg"
          class="custom-btn"
          @click="closeModal"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.common-button {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  border: none;
  color: #06070a !important;
  background-color: none;
  text-transform: unset;
}

.custom-file-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  display: flex;
  justify-content: left;
  padding: 10px 8px 16px 16px;
  height: 40px;
  min-height: 0px;
  border-radius: 8px;
}

.custom-file-input :deep(.mdi-menu-down::before) {
  content: "\F0140" !important;
}

.custom-height
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  height: 40px;
  width: 120px;
  min-height: 0px;
  display: flex;
  justify-content: left;
  min-width: 0px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 5px !important;
  max-width: 120px;
}
.custom-height :deep(.v-input__details .v-messages__message) {
  max-width: 110px;
}

.btn-ba {
  width: 100px;
  height: 40px !important;
  color: #f0ededf1 !important;
  background: #06070a;
}

.custom-btn {
  width: 90px;
  height: 40px !important;
  color: #000000;
  border: 1px solid #828282;
  background-color: white;
}

.btn-confirm {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
}

.total {
  width: 100px;
  height: 40px;
  font: Inter;
  font-weight: 500;
  size: 18px;
  line-height: 19.8px;
  letter-spacing: -2%;
}

.ag-row-first .ag-cell-normal-height {
  display: none !important;
}

.paging {
  font: Inter;
  font-weight: 600;
  size: 20px;
  line-height: 22px;
  width: 13px;
  height: 22px;
  color: #c0c0c0;
}

.activePaging {
  color: black;
}

.v-card-title {
  background-color: blue;
}
</style>
