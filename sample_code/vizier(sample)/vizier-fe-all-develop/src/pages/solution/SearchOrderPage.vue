<script lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { CommonUtil } from "@/utils/common-util";
import CfDropdown from "@/components/controls/CfDropdown.vue";
import CfInput from "@/components/controls/CfInput.vue";
import CreateSeachOrderModal from "@/pages/functions/subs/CreateSeachOrderModal.vue";
import UpdateSearchOrderModal from "@/pages/functions/subs/UpdateSearchOrderModal.vue";
import useGlobalStore from "@/store/global.store";
import axios from "axios";

export default defineComponent({
  name: "SystemPage",
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
    const sysCd = ref("");
    const ordrClasPath = ref("");
    const ordrClasNm = ref("");
    const custClasNm = ref("");
    const custClasPath = ref("");
    const sysCdNm = ref("");
    const isShowBtnCheckAndClose = ref(false);
    const isShowBtnInsAndEditAndDel = ref(true);
    const paginationPageSizeSelector: Array<number> = [];
    const workType = ref("ordr");
    const workTypeDropDown = ref({ key: "ordr", value: "오더" });
    const disabledWorkType = ref(false);
    const rowData = ref(null);
    const gridApi = ref(null);
    const editType = ref("fullRow");
    const totalRecord = ref(0);
    const { translateMessage } = CommonUtil.useTranslatedMessage();
    const workTypeDefinition = [
      { key: "cust", value: "고객" },
      { key: "ordr", value: "오더" },
    ];
    const levels = ref(workTypeDefinition);
    const columnDefs = ref(null);

    if (props.data != null) {
      if (props.data.screenType === "popUp") {
        isShowBtnCheckAndClose.value = true;
        isShowBtnInsAndEditAndDel.value = false;
        disabledWorkType.value = true;
        ordrClasNm.value = props.data.funcClasNm;
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

    onBeforeMount(() => {
      updateColumnDefs();
      paginationPageSizeSelector[0] = 10;
      paginationPageSizeSelector[1] = 20;
      paginationPageSizeSelector[2] = 50;
      paginationPageSizeSelector[3] = 100;
    });

    const updateColumnDefs = () => {
      const columOrdrDefs = [
        {
          field: "ordrClasNm",
          headerName: "클래스명",
          flex: 3,
        },
        {
          field: "ordrClasPath",
          headerName: "클래스경로명",
          flex: 3,
        },
      ];

      const columnDefsCust = [
        {
          field: "custClasNm",
          headerName: "클래스명",
          flex: 3,
        },
        {
          field: "custClasPath",
          headerName: "클래스경로명",
          flex: 3,
        },
      ];

      if (workType.value === "ordr") {
        columnDefs.value = columOrdrDefs;
      } else {
        columnDefs.value = columnDefsCust;
      }
    };

    const addRow = () => {};
    const onCellValueChanged = (params) => {
      params.data[params.columnDefs.field] = params.value;
    };

    const updateWorkType = (val: any) => {
      workType.value = val.key;
      totalRecord.value = 0;
      rowData.value = [];
      updateColumnDefs();
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
              `http://dev.service-billing.com/ordr/ordrclas/v1?ordrClasNm=${ordrClasNm.value}&ordrClasPath=${ordrClasPath.value}`
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
              `http://dev.service-billing.com/cust/custclas/v1?custClasNm=${ordrClasNm.value}&custClasPath=${ordrClasPath.value}`
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
        rowData.value = null;
        console.error(error);
      }
    };

    const onGridReady = (params) => {
      gridApi.value = params.api;
    };

    const deleteSelectedRows = async () => {
      const selectedRows = gridApi.value.getSelectedRows();
      const baseUrl = getBaseUrl();
      if (!baseUrl) {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("search_order.msg_unsearch"),
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
            text: translateMessage("search_order.msg_unselect_row_delete"),
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
        text: translateMessage("search_order.msg_confirm_delete"),
        width: "400",
      };

      const result = await globalStore.openAlertConfirm(objectAlert);
      if (result) {
        const baseUrl = getBaseUrl();
        if (!baseUrl) {
          return;
        }

        // 삭제할 데이터를 API로 전송
        await Promise.all(
          selectedRows.map(async (row: any) => {
            if (workType.value === "ordr") {
              await axios
                .delete(
                  `http://dev.service-billing.com/ordr/ordrclas/v1/${row.ordrClasId}`
                )
                .then((response) => {
                  if (response.status === 200 && response.data === 1) {
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
              await axios
                .delete(
                  `http://dev.service-billing.com/cust/custclas/v1/${row.custClasId}`
                )
                .then((response) => {
                  if (response.status === 200 && response.data === 1) {
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

    const showModalCreateSearchOrder = async () => {
      const baseUrl = getBaseUrl();
      if (!baseUrl) {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("search_order.msg_unsearch"),
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
          component: CreateSeachOrderModal,
          dataInput: { workType: workType.value },
          width: "720",
          height: "412",
          type: "custom",
        };
        if (workType.value === "ordr") {
          objectModal.title = "오더클래스 관리";
        } else {
          objectModal.title = "고객클래스 관리";
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
            text: translateMessage("search_order.msg_unsearch"),
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
            text: translateMessage("search_order.msg_unselect_row_update"),
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
          component: UpdateSearchOrderModal,
          dataInput: { dataRow: selectedRows[0], workType: workType.value },
          width: "720",
          height: "500",
          type: "custom",
        };

        if (workType.value === "ordr") {
          objectModal.title = "오더클래스 관리";
        } else {
          objectModal.title = "고객클래스 관리";
        }

        const response = await globalStore.openModal(objectModal);
        if (response === 1) {
          fetchData();
        }
      }
    };

    const ordrClasNmRules = ref([
      (value: string) => {
        if (value.length <= 20) return true;
        return translateMessage("search_order.msg_error_ordrClasNm_maxlength");
      },
      (value: string) => {
        if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
        return translateMessage("search_order.msg_error_language");
      },
    ]);

    const ordrClasPathRules = ref([
      (value: string) => {
        if (value.length <= 100) return true;
        return translateMessage(
          "search_order.msg_error_ordrClasPath_maxlength"
        );
      },
      (value: string) => {
        if (value.length === 0 || /^[A-Za-z. 0-9]+$/.test(value)) return true;
        return translateMessage("search_order.msg_error_clasPath_language");
      },
    ]);

    const handleOrdrClasNmUpdate = (val: string) => {
      ordrClasNm.value = val;
    };

    const handleOrdrClasPathUpdate = (val: string) => {
      ordrClasPath.value = val.toLowerCase();
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
          clasId: selectedRow.custClasId,
          clasNm: selectedRow.custClasNm,
          clasPath: selectedRow.custClasPath,
        });
      }
      if (baseUrl == "ordr") {
        emit("closeDialog", {
          clasId: selectedRow.ordrClasId,
          clasNm: selectedRow.ordrClasNm,
          clasPath: selectedRow.ordrClasPath,
        });
      }
    };

    return {
      workTypeDefinition,
      ordrClasNm,
      ordrClasPath,
      custClasNm,
      custClasPath,
      sysCd,
      sysCdNm,
      sendSelectedRowToMain,
      closeModal,
      handleOrdrClasPathUpdate,
      handleOrdrClasNmUpdate,
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
      showModalCreateSearchOrder,
      updateSelectedRows,
      updateWorkType,
      ordrClasNmRules,
      ordrClasPathRules,
      totalRecord,
      isShowBtnCheckAndClose,
      isShowBtnInsAndEditAndDel,
      formatDateValue,
      getBaseUrl,
      paginationPageSizeSelector,
      workType,
      workTypeDropDown,
      disabledWorkType,
    };
  },
});
</script>

<template>
  <div class="grid" style="">
    <div class="row-span-12 mt-4" style="height: auto">
      <div style="float: left" class="w-2/6 flex">
        <label class="pt-5 pl-4 text-lg">{{ $t("system.lbl_work") }}</label>
        <div class="w-40 pl-2">
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
      <div class="w-3/4 flex">
        <div class="flex">
          <label for="sysCd" class="pt-3 pl-4">클래스명 :</label>
          <div class="custom-height">
            <cf-input
              class="pl-2"
              :model="ordrClasNm"
              variant="solo"
              :rules="ordrClasNmRules"
              @update:model="handleOrdrClasNmUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="flex pl-4">
          <label for="sysCdNm" class="pt-3">클래스경로명 :</label>
          <div class="custom-height">
            <cf-input
              class="pl-2"
              :model="ordrClasPath"
              variant="solo"
              :rules="ordrClasPathRules"
              special-action="toLowerCase"
              @update:model="handleOrdrClasPathUpdate"
            ></cf-input>
          </div>
        </div>
      </div>
      <div class="w-1/4 flex justify-end">
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
    <div
      v-if="isShowBtnInsAndEditAndDel"
      class="flex pt-[80px]"
      style="height: auto"
    >
      <div class="w-1/2 pt-9">
        <label for="input1" class="total pl-4">Total : {{ totalRecord }}</label>
      </div>
      <div class="w-1/2 flex justify-end" style="height: auto">
        <div class="flex float-right pl-2">
          <cf-button
            label="신규"
            rounded="lg"
            class="custom-btn"
            @click="showModalCreateSearchOrder"
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
      <ag-grid-vue
        style="width: 100%; height: 519px"
        class="ag-theme-alpine pl-6 justify-content: center"
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
.custom-height :deep(.v-field__input) {
  height: 40px;
  width: 150px;
  min-height: 0px;
  display: flex;
  justify-content: left;
  min-width: 0px;
  padding: 10px;
  border-radius: 8px;
}
.btn-ba {
  color: #f0ededf1 !important;
  background: #06070a;
  height: 40px !important;
  width: 100px;
}
.custom-btn {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
  margin-top: 2px;
  height: 40px !important;
  width: 90px;
}
.custom-btn-tb {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
  margin-top: 2px;
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

.ag-theme-alpine :deep(.ag-header-cell-label) {
  display: flow !important;
  text-align: center !important;
  padding-top: 10px !important;
}
</style>
