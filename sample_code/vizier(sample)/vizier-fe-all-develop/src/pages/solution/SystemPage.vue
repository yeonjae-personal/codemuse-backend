<script lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { CommonUtil } from "@/utils/common-util";
import CfDropdown from "@/components/controls/CfDropdown.vue";
import CfInput from "@/components/controls/CfInput.vue";
import CreateSystemModal from "@/pages/functions/subs/CreateSystemModal.vue";
import UpdateSystemModal from "@/pages/functions/subs/UpdateSystemModal.vue";
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

    if (props.data != null) {
      if (props.data.screenType === "popUp") {
        isShowBtnInsAndEditAndDel.value = false;
        isShowBtnCheckAndClose.value = true;
        disabledWorkType.value = true;
        sysCd.value = props.data.sysCd;
      }
      if (props.data.screenType === "popUp2") {
        isShowBtnInsAndEditAndDel.value = false;
        isShowBtnCheckAndClose.value = true;
        disabledWorkType.value = true;
        sysCdNm.value = props.data.sysCdNm;
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
    const columnDefs = ref([
      {
        checkboxSelection: true,
        headerCheckboxSelection: true,
        headerCheckboxSelectionFilteredOnly: true,
        flex: 1,
      },

      {
        field: "sysCd",
        headerName: "시스템코드",
        flex: 3,
      },
      {
        field: "sysCdNm",
        headerName: "시스템명",
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
    rowData.value = [];

    onBeforeMount(() => {
      paginationPageSizeSelector[0] = 10;
      paginationPageSizeSelector[1] = 20;
      paginationPageSizeSelector[2] = 50;
      paginationPageSizeSelector[3] = 100;
    });

    const addRow = () => {};
    const onCellValueChanged = (params) => {
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
        rowData.value = null;
        const response = await axios.get(
          `http://dev.service-billing.com/${baseUrl}/sys/v1?sysCd=${sysCd.value}&sysCdNm=${sysCdNm.value}`
        );
        rowData.value = response.data;
        totalRecord.value = rowData.value?.length;
      } catch (error) {
        rowData.value = [];
        totalRecord.value = 0;
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

        try {
          const currentDate = new Date().toJSON().slice(0, 19);
          const requestBody: any = {
            validEndDtm: currentDate,
          };
          // 삭제할 데이터를 API로 전송
          await Promise.all(
            selectedRows.map(async (row) => {
              requestBody.sysId = row.sysId;
              requestBody.sysCd = row.sysCd;
              requestBody.sysCdNm = row.sysCdNm;
              requestBody.validStartDtm = row.validStartDtm;
              requestBody.validStartDtm = row.validEndDtm;
              await axios.put(
                `http://dev.service-billing.com/${baseUrl}/sys/v1`,
                requestBody
              );
              totalRecord.value = totalRecord.value - 1;
            })
          );

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
          // API 호출이 성공하면 그리드에서 행 삭제
          gridApi.value.applyTransaction({ remove: selectedRows });
        } catch (error) {
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
        }
      } else {
        return false;
      }
    };

    const showModalCreateTodo = async () => {
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
          component: CreateSystemModal,
          dataInput: { workType: workType.value },
          width: "720",
          height: "412",
          type: "custom",
        };
        if (workType.value === "ordr") {
          objectModal.title = "오더시스템 관리";
        } else {
          objectModal.title = "고객시스템 관리";
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
          component: UpdateSystemModal,
          dataInput: { dataRow: selectedRows[0], workType: workType.value },
          width: "720",
          height: "500",
          type: "custom",
        };
        if (workType.value === "ordr") {
          objectModal.title = "오더시스템 관리";
        } else {
          objectModal.title = "고객시스템 관리";
        }

        await globalStore.openModal(objectModal);
        fetchData();
      }
    };

    const sysCdRules = ref([
      (value: string) => {
        if (value.length <= 20) return true;
        return translateMessage("system.msg_error_maxlength");
      },
      (value: string) => {
        if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
        return translateMessage("system.msg_error_language");
      },
    ]);

    const sysCdNmRules = ref([
      (value: string) => {
        if (value.length <= 20) return true;
        return translateMessage("system.msg_error_maxlength");
      },
    ]);

    const handleSysCdUpdate = (val: string) => {
      sysCd.value = val.toUpperCase();
    };

    const handleSysCdNmUpdate = (val: string) => {
      sysCdNm.value = val;
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
      emit("closeDialog", {
        sysCd: selectedRow.sysCd,
        sysCdNm: selectedRow.sysCdNm,
      });
    };

    return {
      sysCd,
      sysCdNm,
      sendSelectedRowToMain,
      closeModal,
      handleSysCdNmUpdate,
      handleSysCdUpdate,
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
      showModalCreateTodo,
      updateSelectedRows,
      updateWorkType,
      sysCdRules,
      sysCdNmRules,
      totalRecord,
      isShowBtnCheckAndClose,
      isShowBtnInsAndEditAndDel,
      formatDateValue,
      getBaseUrl,
      paginationPageSizeSelector,
      workType,
      workTypeDropDown,
      workTypeDefinition,
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
            class="custom-file-input mt-3"
            variant="solo"
            :items="workTypeDefinition"
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
          <label for="sysCd" class="pt-3 pl-4">시스템코드 :</label>
          <div class="custom-height">
            <cf-input
              class="pl-2"
              :model="sysCd"
              variant="solo"
              :rules="sysCdRules"
              special-action="toUpperCase"
              @update:model="handleSysCdUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="flex pl-4">
          <label for="sysCdNm" class="pt-3">시스템명 :</label>
          <div class="custom-height">
            <cf-input
              class="pl-2"
              :model="sysCdNm"
              variant="solo"
              :rules="sysCdNmRules"
              @update:model="handleSysCdNmUpdate"
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
    <div v-if="isShowBtnInsAndEditAndDel" class="flex" style="height: auto">
      <div class="w-1/2 pt-9">
        <label for="input1" class="total pl-4">Total : {{ totalRecord }}</label>
      </div>
      <div class="w-1/2 flex justify-end" style="height: auto">
        <div class="flex float-right pl-2">
          <cf-button
            label="신규"
            rounded="lg"
            class="custom-btn"
            @click="showModalCreateTodo"
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
  height: 50px;
  width: 200px;
  min-height: 0px;
  display: flex;
  justify-content: left;
  min-width: 0px;
  padding: 10px;
}
.btn-ba {
  color: #f0ededf1 !important;
  background: #06070a;
  width: 124px;
  height: 50px !important;
}
.custom-btn {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
  margin-top: 2px;
  width: 90px;
  height: 46px;
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
</style>
