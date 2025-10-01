<script lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { httpClient } from "@/utils/http-common";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { CommonUtil } from "@/utils/common-util";
import useGlobalStore from "@/store/global.store";
import SysCdPage from "@/pages/solution/SystemPage.vue";
import CreateOrderModal from "@/pages/functions/subs/CreateOrderModal.vue";
import DetailOrderModal from "@/pages/functions/subs/DetailOrderModal.vue";
import UpdateOrderModal from "@/pages/functions/subs/UpdateOrderModal.vue";

export default defineComponent({
  name: "OrderPage",
  components: {
    AgGridVue,
  },
  props: {
    data: {
      type: Object,
      default: null,
    },
  },
  emits: ["closeDialog"],
  setup(_props, { emit }) {
    const globalStore = useGlobalStore();
    const sndSysCd = ref("");
    const workCd = ref({ key: "ordr", value: "오더" });
    const workType = ref("ordr");
    const ordrItemEngNm = ref("");
    const ordrItemKornNm = ref("");
    const rowData = ref([]);
    const gridApi = ref();
    const totalRecord = ref(0);
    const { translateMessage } = CommonUtil.useTranslatedMessage();
    const paginationPageSizeSelector: Array<number> = [];
    const formSearch = ref<any>(null);
    const getBaseUrl = (): string | null => {
      if (!workType.value) {
        return null;
      }
      return workType.value;
    };
    const columnDefs = ref([
      {
        field: "sysCd",
        headerName: "시스템코드",
        flex: 4,
        cellStyle: { textAlign: "center", color: "#828282" },
        headerClass: "header-center",
      },
      {
        field: "ordrItemEngNm",
        headerName: "항목",
        flex: 4,
        cellStyle: { textAlign: "left", color: "#828282" },
        headerClass: "header-center",
      },
      {
        field: "ordrItemKornNm",
        headerName: "항목명",
        flex: 4,
        cellStyle: { textAlign: "left", color: "#828282" },
        headerClass: "header-center",
      },
    ]);
    const showModalDetailOrder = async () => {
      const selectedRows = gridApi.value.getSelectedRows();
      if (selectedRows.length === 0) {
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
          component: DetailOrderModal,
          dataInput: { workType: workType.value, selectedRows },
          width: "1216",
          height: "800",
          type: "custom",
        };
        objectModal.title = "오더 항목 상세 관리";
        await globalStore.openModal(objectModal);
        fetchData();
      }
    };
    const showModalCreateOrder = async () => {
      const objectModal: any = {
        component: CreateOrderModal,
        dataInput: { workType: workType.value },
        width: "720",
        height: "650",
        type: "custom",
      };
      objectModal.title = "오더항목 관리";
      await globalStore.openModal(objectModal);
      fetchData();
    };
    const showModalUpdateOrder = async () => {
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
          component: UpdateOrderModal,
          dataInput: { dataRow: selectedRows[0], workType: workType.value },
          width: "720",
          height: "650",
          type: "custom",
        };
        objectModal.title = "오더항목 관리";
        await globalStore.openModal(objectModal);
        fetchData();
      }
    };
    const deleteSelectedRows = async () => {
      const selectedRows = gridApi.value.getSelectedRows();
      if (selectedRows.length === 0) {
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
      }
      const objectAlert: any = {
        text: translateMessage("order.msg_confirm_delete"),
        width: "400",
      };

      const result = await globalStore.openAlertConfirm(objectAlert);
      if (result) {
        try {
          await Promise.all(
            selectedRows.map(async (row: any) => {
              await httpClient.delete(
                `/api/ordr/ordritem/v1/ordritemdel/${row.ordrItemId}`
              );
            })
          );
          totalRecord.value = totalRecord.value - 1;

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
          gridApi.value.applyTransaction({ remove: selectedRows });
        } catch (error) {
          globalStore.setToastInfor(
            {
              title: translateMessage("common.msg_notification"),
              text: translateMessage("order.msg_error_delete"),
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
    onBeforeMount(() => {
      paginationPageSizeSelector[0] = 10;
      paginationPageSizeSelector[1] = 20;
      paginationPageSizeSelector[2] = 50;
      paginationPageSizeSelector[3] = 100;
    });

    const onCellValueChanged = (params: any) => {
      params.data[params.columnDefs.field] = params.value;
    };
    const fetchData = async () => {
      try {
        let countLength = 0;
        await httpClient
          .get(
            `/api/ordr/ordritem/v1/ordritem?sysCd=${sndSysCd.value}&ordrItemEngNm=${ordrItemEngNm.value}&ordrItemKornNm=${ordrItemKornNm.value}`
          )
          .then((response) => {
            if (response.status == 200 && !response.data.errorCode) {
              rowData.value = response.data || [];
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

    const onInputSysCd = () => {
      sndSysCd.value = sndSysCd.value.replace(/[^a-zA-Z]/g, "").toUpperCase();
    };
    const onInputOderItem = () => {
      ordrItemEngNm.value = ordrItemEngNm.value
        .toLowerCase()
        .replace(/[^a-z_]/g, "");
    };
    const closeModal = () => {
      emit("closeDialog");
    };
    const updateSndSysCdValue = (value: string) => {
      if (!value) return;
      sndSysCd.value = value.toUpperCase();
    };

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

      if (data) {
        updateSndSysCdValue(data.sysCd);
      }
    };

    return {
      closeModal,
      columnDefs,
      rowData,
      fetchData,
      onGridReady,
      onCellValueChanged,
      totalRecord,
      sndSysCd,
      workCd,
      ordrItemEngNm,
      ordrItemKornNm,
      onInputSysCd,
      onInputOderItem,
      paginationPageSizeSelector,
      searchSndSysCd,
      formSearch,
      showModalDetailOrder,
      showModalCreateOrder,
      showModalUpdateOrder,
      deleteSelectedRows,
    };
  },
});
</script>

<template>
  <div class="max-w-7xl mx-auto bg-white p-6 ml-5">
    <h1 class="text-2xl font-bold mb-4">오더항목 관리</h1>
    <div class="flex justify-between space-x-4 mb-4">
      <div class="flex justify-start">
        <div class="flex items-center before:space-x-2">
          <label
            for="system-code"
            class="whitespace-nowrap text-base font-medium mr-1"
            >시스템코드 :</label
          >
          <v-form ref="formSearch">
            <div class="mr-2 w-[150px] h-[40px]">
              <v-text-field
                v-model="sndSysCd"
                variant="outlined"
                color="#E0E0E0"
                base-color="#E0E0E0"
                maxlength="20"
                @input="onInputSysCd"
              ></v-text-field>
            </div>
          </v-form>

          <v-btn
            prepend-icon="mdi-magnify mdi-24px"
            class="w-[40px] h-[40px] !min-w-[40px] border-[#E0E0E0] rounded-lg search-btn p-0 mr-2"
            @click="searchSndSysCd"
          >
          </v-btn>
        </div>
        <div class="flex items-center space-x-2">
          <label for="item" class="whitespace-nowrap text-base font-medium mr-1"
            >항목 :</label
          >
          <v-text-field
            v-model="ordrItemEngNm"
            maxlength="50"
            variant="outlined"
            color="#E0E0E0"
            base-color="#E0E0E0"
            class="mr-2 w-[150px] h-[40px]"
            @input="onInputOderItem"
          ></v-text-field>
        </div>
        <div class="flex items-center space-x-2">
          <label
            for="item-name"
            class="whitespace-nowrap text-base font-medium mr-1"
            >항목명 :</label
          >
          <v-text-field
            v-model="ordrItemKornNm"
            maxlength="50"
            variant="outlined"
            color="#E0E0E0"
            base-color="#E0E0E0"
            class="mr-2 w-[150px] h-[40px]"
          ></v-text-field>
        </div>
      </div>
      <div class="flex justify-end">
        <button
          class="w-[100px] bg-black text-white px-8 py-2 flex text-center rounded-lg common-button"
          @click="fetchData"
        >
          검색
        </button>
      </div>
    </div>
    <div class="flex justify-between items-center mb-4">
      <span class="text-base font-medium mt-[18px]"
        >Total: {{ totalRecord }}</span
      >
      <div class="flex items-center space-x-2">
        <button
          class="bg-white text-base font-medium px-4 py-2 rounded-lg custom-btn"
          @click="showModalDetailOrder"
        >
          항목상세
        </button>
        <button
          class="bg-white text-base font-medium px-4 py-2 rounded-lg custom-btn"
          @click="showModalCreateOrder"
        >
          신규
        </button>
        <button
          class="bg-white text-base font-medium px-4 py-2 rounded-lg custom-btn"
          @click="showModalUpdateOrder"
        >
          수정
        </button>
        <button
          class="bg-white text-base font-medium px-4 py-2 rounded-lg custom-btn"
          @click="deleteSelectedRows"
        >
          삭제
        </button>
      </div>
    </div>
    <div class="overflow-x-auto">
      <ag-grid-vue
        style="
          width: 100%;
          height: 519px;
          border-color: #b2cee2;
          border-radius: 8px;
        "
        class="ag-theme-alpine"
        :column-defs="columnDefs"
        :row-data="rowData"
        row-selection="single"
        :pagination="true"
        :pagination-page-size="10"
        :pagination-page-size-selector="paginationPageSizeSelector"
        @grid-ready="onGridReady"
      >
      </ag-grid-vue>
    </div>
  </div>
</template>

<style scoped>
:deep() .v-btn__prepend {
  margin-inline-start: 15px !important;
}
.custom-btn {
  border: 1px solid #828282;
}
.custom-btn:hover {
  background-color: #730024 !important;
  color: white !important;
}
:deep() .v-field__field {
  border-radius: 8px;
  border-color: #e0e0e0;
}
:deep() .v-field__input {
  min-height: 40px;
  width: 140px;
  --v-field-input-padding-top: 5px;
  padding-bottom: 5px;
  color: #2a2a2a;
  font-size: 13px;
}
:deep() .ag-header-cell-label {
  display: flex;
  align-items: center;
  justify-content: center;
}
:deep() .ag-header-cell-text {
  font-size: 16px;
  font-weight: 600;
  color: #828282;
}
:deep() .v-form::before {
  display: none;
}
.common-button:hover {
  background-color: #730024 !important;
}
:deep() .v-card .v-card-text {
  font-size: 20px !important;
  font-weight: 500;
  color: #000000;
}
:deep() .ag-cell-focus {
  border: none !important;
}
</style>
