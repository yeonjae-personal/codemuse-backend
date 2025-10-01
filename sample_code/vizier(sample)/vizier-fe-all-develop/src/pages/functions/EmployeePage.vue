<script lang="ts">
import { useGlobal } from "@/store";
import { AgGridVue } from "ag-grid-vue3";
import { httpClient } from "@/utils/http-common";
import { CommonUtil } from "@/utils/common-util";
import CreateEmployeeModal from "./subs/CreateEmployeeModal.vue";

import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

export default defineComponent({
  name: "CarGrid",
  components: {
    AgGridVue,
  },
  setup() {
    const globalStore = useGlobal();

    const columnDefs = ref([
      {
        checkboxSelection: true,
        headerCheckboxSelection: true,
        headerCheckboxSelectionFilteredOnly: true,
        width: 30,
      },
      { field: "num", headerName: "Num" },
      { field: "name", headerName: "Name" },
      { field: "birthdate", headerName: "Birthdate" },
      { field: "address", headerName: "Address" },
      { field: "gender", headerName: "Gender" },
    ]);
    const rowData = ref([]);
    const gridApi = ref<any>(null);
    const editType = ref("fullRow");

    //직원 조회(다건)
    const fetchData = async () => {
      try {
        const response = await httpClient.get(`/api/comm/employees`);
        rowData.value = response.data;
      } catch (error) {
        console.error(error);
      }
    };

    const { translateMessage } = CommonUtil.useTranslatedMessage();

    const addRow = () => {
      if (!gridApi.value) return;
      const newRow = {
        name: "",
        num: "",
        birthdate: "",
        address: "",
        gender: "",
      };
      gridApi.value.applyTransaction({ add: [newRow] });
    };

    //직원 삭제
    const deleteSelectedRows = async () => {
      const objectAlert: any = {
        title: translateMessage("common.msg_confirm"),
        text: translateMessage("todos.msg_confirm_delete"),
        width: "400",
      };

      const result = await globalStore.openAlertConfirm(objectAlert);
      if (result) {
        const selectedRows = gridApi.value.getSelectedRows();

        try {
          // 삭제할 데이터를 API로 전송
          await Promise.all(
            selectedRows.map(async (row) => {
              await httpClient.delete(`/api/comm/employees/${row.num}`);
            })
          );

          globalStore.setToastInfor(
            {
              title: translateMessage("common.msg_notification"),
              text: translateMessage("employee.msg_success_delete"),
              border: "start",
              borderColor: "white",
              type: "success",
              icon: "$success",
            },
            5000
          );
          // API 호출이 성공하면 그리드에서 행 삭제
          gridApi.value.applyTransaction({ remove: selectedRows });
        } catch (error) {
          console.error("삭제 중 오류 발생:", error);
        }
      } else {
        return false;
      }
    };

    const onGridReady = (params) => {
      gridApi.value = params.api;
      fetchData();
    };

    const onCellValueChanged = (params) => {
      params.data[params.colDef.field] = params.newValue;
    };

    const showModalCreateTodo = async () => {
      const objectModal: any = {
        title: "직원 등록",
        component: CreateEmployeeModal,
        dataInput: null,
        width: "800",
      };
      await globalStore.openModal(objectModal);
      fetchData();
    };

    return {
      columnDefs,
      rowData,
      editType,
      fetchData,
      addRow,
      deleteSelectedRows,
      onGridReady,
      onCellValueChanged,
      showModalCreateTodo,
    };
  },
});
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md cf-todos">
    <h1 class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">
      직원 관리
    </h1>
    <div class="grid-container">
      <ag-grid-vue
        style="width: 100%; height: 500px"
        class="ag-theme-alpine"
        :column-defs="columnDefs"
        :row-data="rowData"
        row-selection="multiple"
        :edit-type="editType"
        :pagination="true"
        :pagination-page-size="20"
        @grid-ready="onGridReady"
        @cell-value-changed="onCellValueChanged"
      >
      </ag-grid-vue>
    </div>
    <div class="button-container">
      <cf-button label="등록" rounded="lg" @click="showModalCreateTodo" />
      <cf-button label="삭제" rounded="lg" @click="deleteSelectedRows" />
    </div>
  </div>
</template>

<style>
.grid-container {
  position: relative;
  margin-bottom: 50px;
}

.button-container {
  position: absolute;
  bottom: 10px;
  right: 35px;
  display: flex;
  gap: 10px;
}
</style>
