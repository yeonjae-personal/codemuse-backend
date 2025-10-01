<script lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { CommonUtil } from "@/utils/common-util";
import useGlobalStore from "@/store/global.store";

export default defineComponent({
  name: "OrdrEventDetlPopup",
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
  setup(props, { emit }) {
    const globalStore = useGlobalStore();
    const selectedEvetRow = ref(props.data.dataRow);
    const evetAttrId = ref("");
    const workType = ref(props.data.workCd);
    const attrGridApi = ref<any>(null);
    const attrDetlGridApi = ref<any>(null);
    const editType = ref("doubleClick");
    const attrTotalRecord = ref(0);
    const attrDetlTotalRecord = ref(0);
    const { translateMessage } = CommonUtil.useTranslatedMessage();
    const isAttrButtonDisabled = ref(false);
    const isAttrDetlButtonDisabled = ref(false);
    const saveEvetAttrList = ref<any[]>([]);
    const saveEvetAttrDetlList = ref<any[]>([]);
    const updateEvetAttrList = ref<any[]>([]);
    const updateEvetAttrDetlList = ref<any[]>([]);
    const attrRowData = ref<any[]>([]);
    const attrDetlRowData = ref<any[]>([]);

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

    const statusCellRenderer = (params: any) => {
      let iconClass = "";
      switch (params.value) {
        case "add":
          iconClass = "mdi mdi-plus-thick";
          break;
        case "delete":
          iconClass = "mdi mdi-minus-thick";
          break;
        case "update":
          iconClass = "mdi mdi-pencil";
          break;
      }

      return `<span class="${iconClass}" style="font-size: 20px;"></span>`;
    };

    attrRowData.value = [];
    attrDetlRowData.value = [];
    const ordrColumnDefs = ref([
      {
        field: "status",
        headerName: "상태",
        width: 70,
        cellRenderer: statusCellRenderer,
      },
      {
        field: "check",
        headerName: "선택",
        checkboxSelection: true,
        width: 70,
      },
      {
        field: "hposOrdrEvetAttr",
        headerName: "상위이벤트속성",
        editable: true,
        flex: 3,
      },
      {
        field: "ordrEvetAttrEngNm",
        headerName: "이벤트속성",
        editable: true,
        flex: 3,
      },
      {
        field: "ordrEvetAttrKornNm",
        headerName: "이벤트속성명",
        editable: true,
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
    const ordrDetlColumnDefs = ref([
      {
        field: "status",
        headerName: "상태",
        cellRenderer: statusCellRenderer,
        width: 70,
      },
      {
        field: "check",
        headerName: "선택",
        checkboxSelection: true,
        width: 70,
      },
      {
        field: "ordrEvetAttrEngNm",
        headerName: "이벤트속성컬럼",
        editable: true,
      },
      {
        field: "ordrEvetAttrKornNm",
        headerName: "이벤트속성컬럼명",
        editable: true,
      },
      {
        field: "attrClmnSno",
        headerName: "속성컬럼순서",
        editable: true,
      },
      {
        field: "mapgAttrEngNm",
        headerName: "매핑속성",
        editable: true,
      },
      {
        field: "mapgAttrKornNm",
        headerName: "매핑속성명",
        editable: true,
      },
      {
        field: "mndtYn",
        headerName: "필수여부",
        cellEditor: "agSelectCellEditor",
        editable: true,
        cellEditorParams: {
          values: ["Y", "N"],
        },
      },
      {
        field: "vrfyYn",
        headerName: "검증여부",
        cellEditor: "agSelectCellEditor",
        editable: true,
        cellEditorParams: {
          values: ["Y", "N"],
        },
      },
      {
        field: "vrfyCntn",
        headerName: "검증수식",
        editable: true,
        tooltipField: "vrfyCntn",
      },
      {
        field: "dataType",
        headerName: "데이터타입",
        cellEditor: "agSelectCellEditor",
        editable: true,
        cellEditorParams: {
          values: ["Int", "String"],
        },
      },
      {
        field: "validStartDtm",
        headerName: "유효시작일시",
        valueFormatter: formatDateValue,
      },
      {
        field: "validEndDtm",
        headerName: "유효종료일시",
        valueFormatter: formatDateValue,
      },
    ]);
    const custColumnDefs = ref([
      {
        field: "status",
        headerName: "상태",
        width: 70,
        cellRenderer: statusCellRenderer,
      },
      {
        field: "check",
        headerName: "선택",
        checkboxSelection: true,
        width: 70,
      },
      {
        field: "hposCustEvetAttr",
        headerName: "상위이벤트속성",
        editable: true,
        flex: 3,
      },
      {
        field: "custEvetAttrEngNm",
        headerName: "이벤트속성",
        editable: true,
        flex: 3,
      },
      {
        field: "custEvetAttrKornNm",
        headerName: "이벤트속성명",
        editable: true,
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
    const custDetlColumnDefs = ref([
      {
        field: "status",
        headerName: "상태",
        cellRenderer: statusCellRenderer,
        width: 70,
      },
      {
        field: "check",
        headerName: "선택",
        checkboxSelection: true,
        width: 70,
      },
      {
        field: "custEvetAttrEngNm",
        headerName: "이벤트속성컬럼",
        editable: true,
      },
      {
        field: "custEvetAttrKornNm",
        headerName: "이벤트속성컬럼명",
        editable: true,
      },
      {
        field: "attrClmnSno",
        headerName: "속성컬럼순서",
        editable: true,
      },
      {
        field: "mapgAttrEngNm",
        headerName: "매핑속성",
        editable: true,
      },
      {
        field: "mapgAttrKornNm",
        headerName: "매핑속성명",
        editable: true,
      },
      {
        field: "mndtYn",
        headerName: "필수여부",
        cellEditor: "agSelectCellEditor",
        editable: true,
        cellEditorParams: {
          values: ["Y", "N"],
        },
      },
      {
        field: "vrfyYn",
        headerName: "검증여부",
        cellEditor: "agSelectCellEditor",
        editable: true,
        cellEditorParams: {
          values: ["Y", "N"],
        },
      },
      {
        field: "vrfyCntn",
        headerName: "검증수식",
        editable: true,
        tooltipField: "vrfyCntn",
      },
      {
        field: "dataType",
        headerName: "데이터타입",
        cellEditor: "agSelectCellEditor",
        editable: true,
        cellEditorParams: {
          values: ["Int", "String"],
        },
      },
      {
        field: "validStartDtm",
        headerName: "유효시작일시",
        valueFormatter: formatDateValue,
      },
      {
        field: "validEndDtm",
        headerName: "유효종료일시",
        valueFormatter: formatDateValue,
      },
    ]);

    const getCurrentLocalDateTime = () => {
      const currentDate = new Date();
      const year = currentDate.getFullYear();
      const month = String(currentDate.getMonth() + 1).padStart(2, "0");
      const day = String(currentDate.getDate()).padStart(2, "0");
      const hours = String(currentDate.getHours()).padStart(2, "0");
      const minutes = String(currentDate.getMinutes()).padStart(2, "0");
      const seconds = String(currentDate.getSeconds()).padStart(2, "0");

      return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`;
    };

    const fetchAttrData = async () => {
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
          const data = await CommonOrdrUtil.execute(
            "GET",
            "/api/ordr/ordrevetattr/v1/ordrevetattr",
            {
              ordrEvetItemId: selectedEvetRow.value[0].evetItemId,
              //ordrEvetItemId : "1"
            }
          );
          attrRowData.value = data;
          countLength = data.length;
        }
        if (workType.value === "cust") {
          const data = await CommonOrdrUtil.execute(
            "GET",
            "/api/cust/custevetattr/v1/custevetattr",
            {
              custEvetItemId: selectedEvetRow.value[0].evetItemId,
            }
          );
          attrRowData.value = data;
          countLength = data.length;
        }
        attrTotalRecord.value = countLength;
      } catch (error) {
        attrRowData.value = [];
        console.error(error);
      }
    };

    const fetchAttrDetlData = async () => {
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
          const data = await CommonOrdrUtil.execute(
            "GET",
            "/api/ordr/ordrevetattr/v1/ordrevetattrdetl",
            {
              ordrEvetAttrId: evetAttrId.value,
            }
          );
          attrDetlRowData.value = data;
          countLength = data.length;
        }

        if (workType.value === "cust") {
          const data = await CommonOrdrUtil.execute(
            "GET",
            "/api/cust/custevetattr/v1/custevetattrdetl",
            {
              custEvetAttrId: evetAttrId.value,
            }
          );
          attrDetlRowData.value = data;
          countLength = data.length;
        }
        attrDetlTotalRecord.value = countLength;
      } catch (error) {
        attrDetlRowData.value = [];
        console.error(error);
      }
    };

    const saveAttrData = async () => {
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
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/ordr/ordrevetattr/v1/ordrevetattr",
            saveEvetAttrList.value
          );
          attrRowData.value = data;
          countLength = data.length;
        }
        if (workType.value === "cust") {
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/cust/custevetattr/v1/custevetattr",
            saveEvetAttrList.value
          );
          attrRowData.value = data;
          countLength = data.length;
        }
        attrTotalRecord.value = countLength;
      } catch (error) {
        attrRowData.value = [];
        console.error(error);
      }
    };

    const saveAttrDetlData = async () => {
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
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/ordr/ordrevetattr/v1/ordrevetattrdetl",
            saveEvetAttrDetlList.value
          );
          attrDetlRowData.value = data;
          countLength = data.length;
        }
        if (workType.value === "cust") {
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/cust/custevetattr/v1/custevetattrdetl",
            saveEvetAttrDetlList.value
          );
          attrDetlRowData.value = data;
          countLength = data.length;
        }
        attrDetlTotalRecord.value = countLength;
      } catch (error) {
        attrDetlRowData.value = [];
        console.error(error);
      }
    };

    const updateAttrData = async () => {
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
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/ordr/ordrevetattr/v1/ordrevetattr",
            updateEvetAttrList.value
          );
          attrRowData.value = data;
          countLength = data.length;
        }
        if (workType.value === "cust") {
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/cust/custevetattr/v1/custevetattr",
            updateEvetAttrList.value
          );
          attrRowData.value = data;
          countLength = data.length;
        }
        attrTotalRecord.value = countLength;
      } catch (error) {
        attrRowData.value = [];
      }
    };

    const updateAttrDetlData = async () => {
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
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/ordr/ordrevetattr/v1/ordrevetattrdetl",
            updateEvetAttrDetlList.value
          );
          attrDetlRowData.value = data;
          countLength = data.length;
        }
        if (workType.value === "cust") {
          const data = await CommonOrdrUtil.execute(
            "POST",
            "/api/cust/custevetattr/v1/custevetattrdetl",
            updateEvetAttrDetlList.value
          );
          attrDetlRowData.value = data;
          countLength = data.length;
        }
        attrDetlTotalRecord.value = countLength;
      } catch (error) {
        attrDetlRowData.value = [];
        console.error(error);
      }
    };

    const onEvetAttrRowSelect = async () => {
      if (!attrGridApi.value) return;
      const gridApi_rowEvetAttr = attrGridApi.value.getSelectedRows();

      if (gridApi_rowEvetAttr.length !== 0) {
        if (workType.value === "ordr") {
          handleEvetAttrId(gridApi_rowEvetAttr[0].ordrEvetAttrId);
        } else if (workType.value === "cust") {
          handleEvetAttrId(gridApi_rowEvetAttr[0].custEvetAttrId);
        }
        saveEvetAttrDetlList.value = [];
        fetchAttrDetlData();
      }
      //isAttrButtonDisabled.value = !isAttrButtonDisabled.value;
      //isAttrDetlButtonDisabled.value = !isAttrButtonDisabled.value;;
    };

    const onAttrGridReady = (params: any) => {
      attrGridApi.value = params.api;
    };

    const onAttrDetlGridReady = (params: any) => {
      attrDetlGridApi.value = params.api;
    };

    const checkForEmptyAttrCells = (row: any) => {
      let columns = attrGridApi.value.getAllGridColumns();
      let allEmpty = true;
      for (let i = 0; i < columns.length; i++) {
        let colDef = columns[i as number].getColDef();
        let field = colDef.field;
        if (colDef.editable) {
          if (
            row[field as string] !== "" &&
            row[field as string] !== null &&
            row[field as string] !== undefined
          ) {
            allEmpty = false;
          }
        }
      }
      return !allEmpty;
    };

    const checkForEmptyAttrDetlCells = (row: any) => {
      if (!attrDetlGridApi.value) return;
      let columns = attrDetlGridApi.value.getAllGridColumns();
      let allEmpty = true;
      for (const column of columns) {
        const colDef = column.getColDef();
        const field = colDef.field;
        if (colDef.editable) {
          if (
            row[field as string] !== "" &&
            row[field as string] !== null &&
            row[field as string] !== undefined
          ) {
            allEmpty = false;
          }
        }
      }
      return !allEmpty;
    };

    const onAddAttrRow = () => {
      if (workType.value === "ordr") {
        const newRow = {
          ordrEvetItemId: selectedEvetRow.value[0].evetItemId,
          status: "add",
        };
        attrGridApi.value.applyTransaction({ add: [newRow] });
        saveEvetAttrList.value = [...saveEvetAttrList.value, newRow];
      } else if (workType.value === "cust") {
        const newRow = {
          custEvetItemId: selectedEvetRow.value[0].evetItemId,
          status: "add",
        };
        attrGridApi.value.applyTransaction({ add: [newRow] });
        saveEvetAttrList.value = [...saveEvetAttrList.value, newRow];
      }
    };

    const onAddAttrDetlRow = () => {
      if (workType.value === "ordr") {
        const newRow = { ordrEvetAttrId: evetAttrId.value, status: "add" };
        attrDetlGridApi.value.applyTransaction({ add: [newRow] });
        saveEvetAttrDetlList.value = [...saveEvetAttrDetlList.value, newRow];
      } else if (workType.value === "cust") {
        const newRow = { custEvetAttrId: evetAttrId.value, status: "add" };
        attrDetlGridApi.value.applyTransaction({ add: [newRow] });
        saveEvetAttrDetlList.value = [...saveEvetAttrDetlList.value, newRow];
      }
    };

    const onDeleteAttrRow = () => {
      const gridApi_rowEvetAttr = attrGridApi.value.getSelectedRows();
      if (gridApi_rowEvetAttr.length !== 0) {
        gridApi_rowEvetAttr[0].status = "delete";
        if (workType.value === "ordr") {
          if (gridApi_rowEvetAttr[0].ordrEvetAttrId) {
            attrGridApi.value.applyTransaction({ update: gridApi_rowEvetAttr });
          } else {
            attrGridApi.value.applyTransaction({ remove: gridApi_rowEvetAttr });
          }
        } else if (workType.value === "cust") {
          if (gridApi_rowEvetAttr[0].custEvetAttrId) {
            attrGridApi.value.applyTransaction({ update: gridApi_rowEvetAttr });
          } else {
            attrGridApi.value.applyTransaction({ remove: gridApi_rowEvetAttr });
          }
        }
        gridApi_rowEvetAttr[0].validEndDtm = getCurrentLocalDateTime();
        gridApi_rowEvetAttr[0].validStartDtm =
          gridApi_rowEvetAttr[0].validStartDtm.replace(" ", "T");
        updateEvetAttrList.value = [
          ...updateEvetAttrList.value,
          gridApi_rowEvetAttr[0],
        ];
        attrDetlRowData.value.forEach((row) => {
          row.validEndDtm = getCurrentLocalDateTime();
          row.validStartDtm = row.validStartDtm.replace(" ", "T");
          updateEvetAttrDetlList.value = [...updateEvetAttrDetlList.value, row];
        });
        attrDetlRowData.value = [];
        attrDetlTotalRecord.value = 0;
      }
    };

    const onDeleteAttrDetlRow = () => {
      const gridApi_rowEvetAttrDetl = attrDetlGridApi.value.getSelectedRows();
      if (gridApi_rowEvetAttrDetl.length !== 0) {
        gridApi_rowEvetAttrDetl[0].status = "delete";
        if (workType.value === "ordr") {
          if (gridApi_rowEvetAttrDetl[0].ordrEvetAttrDetlId) {
            attrDetlGridApi.value.applyTransaction({
              update: gridApi_rowEvetAttrDetl,
            });
          } else {
            attrDetlGridApi.value.applyTransaction({
              remove: gridApi_rowEvetAttrDetl,
            });
          }
        } else if (workType.value === "cust") {
          if (gridApi_rowEvetAttrDetl[0].custEvetAttrDetlId) {
            attrDetlGridApi.value.applyTransaction({
              update: gridApi_rowEvetAttrDetl,
            });
          } else {
            attrDetlGridApi.value.applyTransaction({
              remove: gridApi_rowEvetAttrDetl,
            });
          }
        }
        gridApi_rowEvetAttrDetl[0].validEndDtm = getCurrentLocalDateTime();
        gridApi_rowEvetAttrDetl[0].validStartDtm =
          gridApi_rowEvetAttrDetl[0].validStartDtm.replace(" ", "T");
        updateEvetAttrDetlList.value = [
          ...updateEvetAttrDetlList.value,
          gridApi_rowEvetAttrDetl[0],
        ];
      }
    };

    const onClickSaveButton = () => {
      if (saveEvetAttrList.value.length !== 0) {
        saveEvetAttrList.value = saveEvetAttrList.value.filter((row) =>
          checkForEmptyAttrCells(row)
        );
        saveAttrData();
      }
      if (saveEvetAttrDetlList.value.length !== 0) {
        saveEvetAttrDetlList.value = saveEvetAttrDetlList.value.filter((row) =>
          checkForEmptyAttrDetlCells(row)
        );
        saveAttrDetlData();
      }
      if (updateEvetAttrList.value.length !== 0) {
        updateEvetAttrList.value = updateEvetAttrList.value.filter((row) =>
          checkForEmptyAttrCells(row)
        );
        updateAttrData();
      }
      if (updateEvetAttrDetlList.value.length !== 0) {
        updateEvetAttrDetlList.value = updateEvetAttrDetlList.value.filter(
          (row) => checkForEmptyAttrDetlCells(row)
        );
        updateAttrDetlData();
      }

      emit("closeDialog");
      globalStore.setToastInfor(
        {
          text: "성공적으로 저장하였습니다.",
          type: "success",
          variant: "tonal",
          class: "bottom-center",
        },
        3000
      );
    };

    const handleEvetAttrId = (val: string) => {
      evetAttrId.value = val;
    };

    const onClickCancleButton = () => {
      emit("closeDialog");
    };

    const onAttrCellValueChanged = (params: any) => {
      params.data[params.colDef.field] = params.value;

      let existingRow = new Object();
      if (workType.value === "ordr") {
        existingRow = updateEvetAttrList.value.find(
          (item) => item.ordrEvetAttrId === params.data.ordrEvetAttrId
        );
      } else if (workType.value === "cust") {
        existingRow = updateEvetAttrList.value.find(
          (item) => item.ordrEvetAttrId === params.data.custEvetAttrId
        );
      }

      //update할 대상 리스트에 없을 때만 추가
      if (!existingRow) {
        params.data.validStartDtm = params.data.validStartDtm.replace(" ", "T");
        updateEvetAttrList.value = [...updateEvetAttrList.value, params.data];
      }
    };

    const onAttrDetlCellValueChanged = (params: any) => {
      params.data[params.colDef.field] = params.value;
      let existingRow = new Object();
      if (workType.value === "ordr") {
        existingRow = updateEvetAttrDetlList.value.find(
          (item) => item.ordrEvetAttrDetlId === params.data.ordrEvetAttrDetlId
        );
      } else if (workType.value === "cust") {
        existingRow = updateEvetAttrDetlList.value.find(
          (item) => item.custEvetAttrDetlId === params.data.custEvetAttrDetlId
        );
      }

      //update할 대상 리스트에 없을 때만 추가
      if (!existingRow) {
        params.data.validStartDtm = params.data.validStartDtm.replace(" ", "T");
        updateEvetAttrDetlList.value = [
          ...updateEvetAttrDetlList.value,
          params.data,
        ];
      }
    };

    const onAttrCellEditingStarted = (params: any) => {
      if (workType.value === "ordr") {
        if (params.data.ordrEvetAttrId) {
          params.node.setDataValue("status", "update");
        } else {
          params.node.setDataValue("status", "add");
        }
      } else if (workType.value === "cust") {
        if (params.data.custEvetAttrId) {
          params.node.setDataValue("status", "update");
        } else {
          params.node.setDataValue("status", "add");
        }
      }
    };

    const onAttrDetlCellEditingStarted = (params: any) => {
      if (workType.value === "ordr") {
        if (params.data.ordrEvetAttrDetlId) {
          params.node.setDataValue("status", "update");
        } else {
          params.node.setDataValue("status", "add");
        }
      } else if (workType.value === "cust") {
        if (params.data.custEvetAttrDetlId) {
          params.node.setDataValue("status", "update");
        } else {
          params.node.setDataValue("status", "add");
        }
      }
    };

    const onCellEditingStopped = () => {};

    return {
      ordrColumnDefs,
      ordrDetlColumnDefs,
      custColumnDefs,
      custDetlColumnDefs,
      attrRowData,
      attrDetlRowData,
      editType,
      fetchAttrData,
      fetchAttrDetlData,
      onAttrGridReady,
      onAttrDetlGridReady,
      onAttrCellValueChanged,
      onAttrDetlCellValueChanged,
      attrTotalRecord,
      attrDetlTotalRecord,
      formatDateValue,
      getBaseUrl,
      workType,
      selectedEvetRow,
      handleEvetAttrId,
      onEvetAttrRowSelect,
      evetAttrId,
      attrGridApi,
      attrDetlGridApi,
      onAddAttrRow,
      onAddAttrDetlRow,
      onDeleteAttrRow,
      onDeleteAttrDetlRow,
      isAttrButtonDisabled,
      isAttrDetlButtonDisabled,
      onClickCancleButton,
      onClickSaveButton,
      onAttrCellEditingStarted,
      onAttrDetlCellEditingStarted,
      onCellEditingStopped,
    };
  },

  mounted() {
    this.fetchAttrData();
  },
});
</script>

<template>
  <div class="grid" style="">
    <div class="row-span-12 mt-4" style="height: auto"></div>
    <div class="flex" style="height: auto">
      <div class="w-1/2 pt-9">
        <label for="input1" class="total pl-4"
          >Total : {{ attrTotalRecord }}</label
        >
      </div>
      <div class="w-1/2 flex justify-end" style="height: auto">
        <div class="flex float-right pl-2">
          <cf-button
            :disabled="isAttrButtonDisabled"
            label="+행추가"
            rounded="lg"
            class="custom-btn"
            @click="onAddAttrRow"
          />
        </div>
        <div class="flex pl-2">
          <cf-button
            :disabled="isAttrButtonDisabled"
            label="-행삭제"
            rounded="lg"
            class="custom-btn"
            @click="onDeleteAttrRow"
          />
        </div>
      </div>
    </div>
    <div class="grid-container">
      <div v-if="workType === 'ordr'">
        <ag-grid-vue
          style="height: 520px"
          class="ag-theme-alpine pl-6"
          :column-defs="ordrColumnDefs"
          :row-data="attrRowData"
          row-selection="single"
          :edit-type="editType"
          @grid-ready="onAttrGridReady"
          @cell-value-changed="onAttrCellValueChanged"
          @row-selected="onEvetAttrRowSelect"
          @cell-editing-started="onAttrCellEditingStarted"
        >
        </ag-grid-vue>
      </div>
      <div v-else>
        <ag-grid-vue
          style="height: 520px"
          class="ag-theme-alpine pl-6"
          :column-defs="custColumnDefs"
          :row-data="attrRowData"
          row-selection="single"
          :edit-type="editType"
          @grid-ready="onAttrGridReady"
          @cell-value-changed="onAttrCellValueChanged"
          @row-selected="onEvetAttrRowSelect"
          @cell-editing-started="onAttrCellEditingStarted"
        >
        </ag-grid-vue>
      </div>
    </div>
    <div class="flex" style="height: auto">
      <div class="w-1/2 pt-9">
        <label for="input1" class="total pl-4"
          >Total : {{ attrDetlTotalRecord }}</label
        >
      </div>
      <div class="w-1/2 flex justify-end" style="height: auto">
        <div class="flex float-right pl-2">
          <cf-button
            :disabled="isAttrDetlButtonDisabled"
            label="+행추가"
            rounded="lg"
            class="custom-btn"
            @click="onAddAttrDetlRow"
          />
        </div>
        <div class="flex pl-2">
          <cf-button
            :disabled="isAttrDetlButtonDisabled"
            label="-행삭제"
            rounded="lg"
            class="custom-btn"
            @click="onDeleteAttrDetlRow"
          />
        </div>
      </div>
    </div>
    <div class="grid-container">
      <div v-if="workType === 'ordr'">
        <ag-grid-vue
          style="height: 520px"
          class="ag-theme-alpine pl-6"
          :column-defs="ordrDetlColumnDefs"
          :row-data="attrDetlRowData"
          :tooltip-show-delay="500"
          row-selection="single"
          :edit-type="editType"
          @grid-ready="onAttrDetlGridReady"
          @cell-value-changed="onAttrDetlCellValueChanged"
          @cell-editing-started="onAttrDetlCellEditingStarted"
        >
        </ag-grid-vue>
      </div>
      <div v-else>
        <ag-grid-vue
          style="height: 520px"
          class="ag-theme-alpine pl-6"
          :column-defs="custDetlColumnDefs"
          :row-data="attrDetlRowData"
          :tooltip-show-delay="500"
          row-selection="single"
          :edit-type="editType"
          @grid-ready="onAttrDetlGridReady"
          @cell-value-changed="onAttrDetlCellValueChanged"
          @cell-editing-started="onAttrDetlCellEditingStarted"
        >
        </ag-grid-vue>
      </div>
    </div>
    <div class="justify-end flex" style="height: auto">
      <div class="pr-2">
        <cf-button
          label="저장"
          rounded="lg"
          class="custom-btn"
          @click="onClickSaveButton"
        />
      </div>
      <div class="pr-2">
        <cf-button
          label="닫기"
          rounded="lg"
          class="custom-btn"
          @click="onClickCancleButton"
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
  height: 45px;
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
  /* width: 124px;
  height: 45px !important; */
  color: #f0ededf1 !important;
  background: #06070a;
}

.custom-btn {
  /* width: 90px;
  height: 46px !important; */
  color: #000000;
  border: 1px solid #828282;
  background-color: white;
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
