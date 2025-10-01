<script setup lang="ts">
import axios from "axios";
import { AgGridVue } from "ag-grid-vue3";
import { useGlobal } from "@/store";
import { CommonOrdrUtil } from "@/utils/common-ordr";

import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { ITooltipParams } from "ag-grid-community";

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});

const emit = defineEmits(["closeDialog"]);
const globalStore = useGlobal();

if (props === null) {
  const objectAlert: any = {
    text: "데이터가 존재하지 않습니다.",
    width: "500",
  };
  globalStore.openAlertMessage(objectAlert);
  emit("closeDialog");
}

const workCd = props.data.workCd;
const evetFuncData = ref([]);
const evetItemIdData = ref(null);

if (props.data.dataRow[0].evetFuncId === undefined) {
  evetItemIdData.value = props.data.dataRow[0].evetItemId;
} else {
  evetFuncData.value = props.data.dataRow;
}

const rowData_evetFunc = ref<any[]>([]);
const gridApi_evetFunc = ref(null);
const evetFuncTotal = ref(0);

if (evetFuncData.value.length > 0) {
  evetFuncTotal.value = evetFuncData.value.length;
}
let copyEvetFuncData = new Array();
let copyRowData = new Object();

// 일시 format
const formatDateValue = (params: any) => {
  if (!params.value) {
    return "";
  }
  return params.value.replace("T", " ");
};

const toolTipValueGetter = (params: ITooltipParams) => formatDateValue(params);

// 이벤트기능컬럼
const columnDefs_evetFunc = ref([
  {
    field: "check",
    headerName: "",
    checkboxSelection: true,
    headerCheckboxSelection: true,
    headerCheckboxSelectionFilteredOnly: true,
    suppressHeaderMenuButton: true,
    width: 30,
    flex: 0.5,
  },
  {
    rowDrag: true,
    width: 40,
    suppressHeaderMenuButton: true,
    rowDragText: (params, dragItemCount) => {
      if (dragItemCount > 1) {
        return dragItemCount + "funcDscr";
      }
      return params.rowNode.data.funcDscr;
    },
  },
  // { field: 'evetFuncOdr', headerName: '순번', width: 70,  editable: true },
  {
    field: "evetFuncOdr",
    headerName: "순번",
    tooltipField: "evetFuncOdr",
    headerTooltip: "순번",
    width: 70,
    flex: 1,
  },
  //{ field: 'funcClss', headerName: '기능클래스', editable: true },
  //{ field: 'funcMtho', headerName: '기능메소드', editable: true },
  {
    field: "funcDscr",
    headerName: "기능설명",
    tooltipField: "funcDscr",
    headerTooltip: "기능설명",
    width: 310,
    flex: 4,
  },
  {
    field: "retYn",
    headerName: "반환여부",
    editable: true,
    cellEditor: "agSelectCellEditor",
    cellEditorParams: { values: ["N", "Y"] },
    cellRenderer: "checkboxRenderer",
    cellRendererParams: { onChange: handleCheckboxChange },
    tooltipField: "retYn",
    headerTooltip: "반환여부",
    width: 90,
    flex: 1.5,
  },
  {
    field: "validStartDtm",
    headerName: "유효시작일시",
    valueFormatter: formatDateValue,
    editable: true,
    //cellEditor: DatetimePicker,
    //cellEditor: 'agDateCellEditor',
    //cellEditorParams: { format: 'yyyy-MM-dd HH:mm:ss' },
    tooltipValueGetter: toolTipValueGetter,
    headerTooltip: "유효시작일시",
    flex: 2.5,
  },
  {
    field: "validEndDtm",
    headerName: "유효종료일시",
    valueFormatter: formatDateValue,
    editable: true,
    tooltipValueGetter: toolTipValueGetter,
    headerTooltip: "유효종료일시",
    flex: 2.5,
  },
]);

// 이벤트기능 Data 셋팅
for (let i = 0; i < evetFuncTotal.value; i++) {
  copyRowData = new Object();
  copyRowData.evetFuncId = evetFuncData.value[i as number].evetFuncId;
  copyRowData.evetItemId = evetFuncData.value[i as number].evetItemId;
  copyRowData.funcId = evetFuncData.value[i as number].funcId;
  copyRowData.evetFuncOdr = evetFuncData.value[i as number].evetFuncOdr;
  copyRowData.funcClss = evetFuncData.value[i as number].funcClss;
  copyRowData.funcMtho = evetFuncData.value[i as number].funcMtho;
  copyRowData.funcDscr =
    evetFuncData.value[i as number].funcDscr +
    "(" +
    evetFuncData.value[i as number].funcMtho +
    ")";
  copyRowData.retYn = evetFuncData.value[i as number].retYn;
  copyRowData.validStartDtm = evetFuncData.value[i as number].validStartDtm;
  copyRowData.validEndDtm = evetFuncData.value[i as number].validEndDtm;

  copyEvetFuncData.push(copyRowData);
}
debugger;
rowData_evetFunc.value = copyEvetFuncData;

let baseUrl = "";
const rowData_func = ref([]);
const gridApi_func = ref(null);
const funcTotal = ref(0);
let rowData = new Object();
let arrEvetItem = new Array();

// 기능컬럼
const columnDefs_func = ref([
  {
    rowDrag: true,
    width: 40,
    suppressHeaderMenuButton: true,
    rowDragText: (params, dragItemCount) => {
      if (dragItemCount > 1) {
        return dragItemCount + "funcDscr";
      }
      return params.rowNode.data.funcDscr;
    },
  },
  {
    field: "funcDscr",
    headerName: "기능설명",
    flex: 1,
    tooltipField: "funcDscr",
    headerTooltip: "기능설명",
  },
]);

// 기능 조회
const onFuncSearch = function () {
  return new Promise((resolve, reject) => {
    if (workCd === "ordr") {
      debugger;
      baseUrl = "http://dev.service-billing.com/ordr";
      axios
        .get(` ${baseUrl}/ordrfunc/v1? `)
        .then((response) => {
          if (response.status === 200) {
            if (response.data.errorCode === "400") {
              const objectAlert: any = {
                text: response.data.errorMsg,
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            if (response.data?.length > 0) {
              // 기능 total 셋팅
              funcTotal.value = response.data?.length;
              debugger;
              for (let i = 0; i < funcTotal.value; i++) {
                rowData = new Object();

                rowData.evetItemId =
                  evetItemIdData.value === null
                    ? rowData_evetFunc.value[0].evetItemId
                    : evetItemIdData.value;
                rowData.funcId = response.data[i as number].ordrFuncId;
                rowData.funcDscr =
                  response.data[i as number].ordrFuncDscr +
                  "(" +
                  response.data[i as number].ordrFuncMtho +
                  ")";
                rowData.retYn = "N";
                arrEvetItem.push(rowData);
              }
              debugger;
              rowData_func.value = arrEvetItem;
            }
            resolve(response.data);
          } else {
            reject(false);
          }
        })
        .catch((error) => {
          console.log(error);
          const objectAlert: any = {
            text: error,
            width: "500",
          };

          globalStore.openAlertMessage(objectAlert);
          reject(false);
        });
    } else if (workCd === "cust") {
      baseUrl = "http://dev.service-billing.com/cust";
      axios
        .get(` ${baseUrl}/custfunc/v1/custfunc? `)
        .then((response) => {
          if (response.status === 200) {
            if (response.data.errorCode === "400") {
              const objectAlert: any = {
                text: response.data.errorMsg,
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            if (response.data?.length > 0) {
              // 기능 total 셋팅
              funcTotal.value = response.data?.length;
              for (let i = 0; i < funcTotal.value; i++) {
                const rowData: any = {};
                rowData.evetItemId =
                  evetItemIdData.value === null
                    ? rowData_evetFunc.value[0].evetItemId
                    : evetItemIdData.value;
                rowData.funcId = response.data[i as number].custFuncId;
                rowData.funcDscr =
                  response.data[i as number].custFuncDscr +
                  "(" +
                  response.data[i as number].custFuncMtho +
                  ")";
                rowData.retYn = "N";
                arrEvetItem.push(rowData);
              }
              debugger;
              rowData_func.value = arrEvetItem;
            }

            resolve(response.data);
          } else {
            reject(false);
          }
        })
        .catch((error) => {
          console.log(error);
          alert("fail : " + error);
          reject(false);
        });
    } else {
      const objectAlert: any = {
        text: "URL을 다시 확인해 주세요.",
        width: "500",
      };

      globalStore.openAlertMessage(objectAlert);
      return;
    }
  });
};
debugger;
// 기능 검색
onFuncSearch();

// 이벤트기능 수정
const updateEvetFunc = function (rowData) {
  return new Promise((resolve, reject) => {
    let evetFuncUrl = "";
    let requestBody = [];

    if (workCd === "ordr") {
      debugger;

      requestBody = rowData.map(
        ({ evetFuncId, evetItemId, funcId, evetFuncOdr, ...rest }) => ({
          ordrEvetFuncId: evetFuncId,
          ordrEvetItemId: evetItemId,
          ordrFuncId: funcId,
          ordrEvetFuncOdr: evetFuncOdr,
          retYn: rest.retYn,
          validStartDtm: rest.validStartDtm,
          validEndDtm: rest.validEndDtm,
        })
      );

      evetFuncUrl = "http://dev.service-billing.com/ordr";
      axios
        .post(` ${evetFuncUrl}/ordrevetfunc/v1/ordrevetfunc `, requestBody)
        .then((response) => {
          if (response.status === 200) {
            debugger;
            if (response.data.errorCode === "400") {
              const objectAlert: any = {
                text: response.data.errorMsg,
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            resolve(true);
            globalStore.setToastInfor(
              {
                text: "성공적으로 저장하였습니다.",
                type: "success",
                class: "bottom-center",
              },
              3000
            );

            debugger;
            /*emit("closeDialog", { 
            result: "SUCCESS", 
            evetItemId: evetItemIdData.value === null ? rowData_evetFunc.value[0].evetItemId : evetItemIdData.value,
          });*/
          } else {
            reject(false);
          }
        })
        .catch((error) => {
          console.log(error);
          reject(false);
          globalStore.setToastInfor(
            {
              text: "저장에 실패하였습니다.",
              type: "error",
              class: "bottom-center",
            },
            3000
          );
          emit("closeDialog");
        });
    } else if (workCd === "cust") {
      debugger;
      requestBody = rowData.map(
        ({ evetFuncId, evetItemId, funcId, evetFuncOdr, ...rest }) => ({
          custEvetFuncId: evetFuncId,
          custEvetItemId: evetItemId,
          custFuncId: funcId,
          custEvetFuncOdr: evetFuncOdr,
          retYn: rest.retYn,
          validStartDtm: rest.validStartDtm,
          validEndDtm: rest.validEndDtm,
        })
      );

      evetFuncUrl = "http://dev.service-billing.com/cust";
      axios
        .post(` ${evetFuncUrl}/custevetfunc/v1/custevetfunc `, requestBody)
        .then((response) => {
          if (response.status === 200) {
            if (response.data.errorCode === "400") {
              const objectAlert: any = {
                text: response.data.errorMsg,
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            resolve(true);
            globalStore.setToastInfor(
              {
                text: "성공적으로 저장하였습니다.",
                type: "success",
                class: "bottom-center",
              },
              3000
            );

            debugger;
            /*emit("closeDialog", { 
              result: "SUCCESS", 
              evetItemId: evetItemIdData.value === null ? rowData_evetFunc.value[0].evetItemId : evetItemIdData.value,
            });*/
          } else {
            reject(false);
          }
        })
        .catch((error) => {
          console.log(error);
          reject(false);
          globalStore.setToastInfor(
            {
              text: "저장에 실패하였습니다.",
              type: "error",
              class: "bottom-center",
            },
            3000
          );
          emit("closeDialog");
        });
    } else {
      const objectAlert: any = {
        text: "URL을 다시 확인해 주세요.",
        width: "500",
      };

      globalStore.openAlertMessage(objectAlert);
      return;
    }
  });
};

const updateEvetFuncAdd = function (rowData) {
  return new Promise((resolve, reject) => {
    let evetFuncUrl = "";
    let requestBody = [];

    if (workCd === "ordr") {
      debugger;

      requestBody = rowData.map(
        ({ evetFuncId, evetItemId, funcId, evetFuncOdr, ...rest }) => ({
          ordrEvetFuncId: evetFuncId,
          ordrEvetItemId: evetItemId,
          ordrFuncId: funcId,
          ordrEvetFuncOdr: evetFuncOdr,
          retYn: rest.retYn,
          validStartDtm: rest.validStartDtm,
          validEndDtm: rest.validEndDtm,
        })
      );

      evetFuncUrl = "http://dev.service-billing.com/ordr";
      axios
        .post(` ${evetFuncUrl}/ordrevetfunc/v1/ordrevetfunc `, requestBody)
        .then((response) => {
          if (response.status === 200) {
            debugger;
            if (response.data.errorCode === "400") {
              const objectAlert: any = {
                text: response.data.errorMsg,
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            resolve(true);
            globalStore.setToastInfor(
              {
                text: "성공적으로 저장하였습니다.",
                type: "success",
                class: "bottom-center",
              },
              3000
            );

            debugger;
            emit("closeDialog", {
              result: "SUCCESS",
              evetItemId:
                evetItemIdData.value === null
                  ? rowData_evetFunc.value[0].evetItemId
                  : evetItemIdData.value,
            });
          } else {
            reject(false);
          }
        })
        .catch((error) => {
          console.log(error);
          reject(false);
          globalStore.setToastInfor(
            {
              text: "저장에 실패하였습니다.",
              type: "error",
              class: "bottom-center",
            },
            3000
          );
          emit("closeDialog");
        });
    } else if (workCd === "cust") {
      debugger;
      requestBody = rowData.map(
        ({ evetFuncId, evetItemId, funcId, evetFuncOdr, ...rest }) => ({
          custEvetFuncId: evetFuncId,
          custEvetItemId: evetItemId,
          custFuncId: funcId,
          custEvetFuncOdr: evetFuncOdr,
          retYn: rest.retYn,
          validStartDtm: rest.validStartDtm,
          validEndDtm: rest.validEndDtm,
        })
      );

      evetFuncUrl = "http://dev.service-billing.com/cust";
      axios
        .post(` ${evetFuncUrl}/custevetfunc/v1/custevetfunc `, requestBody)
        .then((response) => {
          if (response.status === 200) {
            if (response.data.errorCode === "400") {
              const objectAlert: any = {
                text: response.data.errorMsg,
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            resolve(true);
            globalStore.setToastInfor(
              {
                text: "성공적으로 저장하였습니다.",
                type: "success",
                class: "bottom-center",
              },
              3000
            );

            debugger;
            /*emit("closeDialog", { 
              result: "SUCCESS", 
              evetItemId: evetItemIdData.value === null ? rowData_evetFunc.value[0].evetItemId : evetItemIdData.value,
            });*/
          } else {
            reject(false);
          }
        })
        .catch((error) => {
          console.log(error);
          reject(false);
          globalStore.setToastInfor(
            {
              text: "저장에 실패하였습니다.",
              type: "error",
              class: "bottom-center",
            },
            3000
          );
          emit("closeDialog");
        });
    } else {
      const objectAlert: any = {
        text: "URL을 다시 확인해 주세요.",
        width: "500",
      };

      globalStore.openAlertMessage(objectAlert);
      return;
    }
  });
};

// 기능에서 이벤트기능순서로 Drop
const addGridDropZone = () => {
  debugger;
  const dropZonParams = gridApi_evetFunc.value.getRowDropZoneParams();
  /*const dropZonParams = gridApi_evetFunc.value.getRowDropZoneParams({
    onDragStop: (params) => {
      debugger;
      const func_funcId = params.node.data.funcId;
      const evetFuncData = gridApi_evetFunc.value.getModel().rowsToDisplay.map(node => node.data);
      const evetFunc_funcId = evetFuncData.value.filter(row => func_funcId.includes(row.funcId));
      if(evetFunc_funcId.length > 1) return;
    }
  });*/
  debugger;
  gridApi_func.value.addRowDropZone(dropZonParams);
};

const onFuncGridReady = (params: any) => {
  gridApi_func.value = params.api;
};

const onEvetFuncGridReady = (params: any) => {
  debugger;
  gridApi_evetFunc.value = params.api;
  //params.api.setRowData(rowData_evetFunc.value);
  addGridDropZone();
};

// 드래그가 종료될때
const onEvetFuncDragEnd = (params: any) => {
  debugger;
  params.api.forEachNode((node, index) => {
    node.setDataValue("evetFuncOdr", index + 1);
  });

  evetFuncTotal.value = gridApi_evetFunc.value.getDisplayedRowCount();
};

// 행삭제 버튼 클릭 시
const handleDeleteButton = async () => {
  debugger;

  const selectedNodes = gridApi_evetFunc.value.getSelectedNodes();
  const selectedRowData = selectedNodes.map((node) => node.data);

  const rowsToDelete = selectedRowData.filter(
    (row) => row.evetFuncId === undefined
  );
  const remainingData = selectedRowData.filter((row) => row.evetFuncId);

  if (selectedRowData.length > 0) {
    const objectConfirm: any = {
      // title: translateMessage('common.msg_confirm'),
      text: "삭제하시겠습니까?",
      width: "500",
    };

    const result = await globalStore.openAlertConfirm(objectConfirm);

    if (result) {
      if (rowsToDelete.length > 0) {
        // 행추가 후 행삭제 처리 시
        const idsToDelete = rowsToDelete.map((row) => row.evetFuncOdr);
        // 순번 재정렬
        const evetFuncRows = gridApi_evetFunc.value
          .getModel()
          .rowsToDisplay.map((node) => node.data);
        const updatedRows = evetFuncRows.filter(
          (row) => !idsToDelete.includes(row.evetFuncOdr)
        );
        rowData_evetFunc.value = reassignIds(updatedRows);

        evetFuncTotal.value = rowData_evetFunc.value.length;
      }
      if (remainingData.length > 0) {
        // 기존데이터 행삭제 처리 시 유효종료일시 만기처리
        debugger;

        remainingData.forEach((row) => {
          debugger;
          row.validEndDtm = "";
          row.validEndDtm += CommonOrdrUtil.getCurrentTime();
        });

        const result = await updateEvetFunc(remainingData);
        debugger;
        if (result) {
          onEvetFuncSearch(remainingData[0].evetItemId);
        }
      }
    }
  } else {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 삭제할 대상을 선택하세요.",
      width: "500",
    };

    globalStore.openAlertMessage(objectAlert);
    return;
  }
};

// 순번 재정렬
const reassignIds = (rows) => {
  debugger;
  return rows.map((row, index) => {
    return { ...row, evetFuncOdr: index + 1 };
  });
};

function handleCheckboxChange(event) {
  const selectedRow = event.data;
  //selectedRow.status = event.target.checked ? 'Y' : 'N';
  gridApi_evetFunc.value.updateRowData({ update: [selectedRow] });
}

// 이벤트기능 조회
const onEvetFuncSearch = async (value) => {
  let arrEvetFunc = new Array();
  let funcRowData = new Object();
  rowData_evetFunc.value = [];
  let rowNum = 1;
  evetFuncTotal.value = 0;

  try {
    let baseUrl = "";
    let response = "";

    if (workCd === "ordr") {
      baseUrl = "http://dev.service-billing.com/ordr/ordrevetfunc";
      response = await axios.get(
        ` ${baseUrl}/v1/evetfuncprmtclas?ordrEvetItemId=${value} `
      );
    } else if (workCd === "cust") {
      baseUrl = "http://dev.service-billing.com/cust/custevetfunc";
      response = await axios.get(
        ` ${baseUrl}/v1/evetfuncprmtclas?custEvetItemId=${value} `
      );
    } else {
      const objectAlert: any = {
        text: "URL을 다시 확인해 주세요.",
        width: "500",
      };

      const result = await globalStore.openAlertMessage(objectAlert);
      if (result) {
        evetFuncTotal.value = 0;
        rowData_evetFunc.value = [];
      }
      return;
    }

    if (response.status === 200) {
      debugger;
      if (response.data.errorCode === "400") {
        const objectAlert: any = {
          text: response.data.errorMsg,
          width: "500",
        };

        await globalStore.openAlertMessage(objectAlert);
        return;
      }

      // total 셋팅
      evetFuncTotal.value = response.data?.length;
      if (workCd === "ordr") {
        for (let i = 0; i < evetFuncTotal.value; i++) {
          funcRowData = new Object();
          funcRowData.evetFuncId =
            response.data[i as number].ordrEvetFuncDto.ordrEvetFuncId;
          funcRowData.evetItemId =
            response.data[i as number].ordrEvetFuncDto.ordrEvetItemId;
          funcRowData.funcId =
            response.data[i as number].ordrEvetFuncDto.ordrFuncId;
          funcRowData.evetFuncOdr = rowNum++;
          funcRowData.funcClss =
            response.data[i as number].ordrSvcClasDto.ordrClasNm;
          funcRowData.funcMtho =
            response.data[i as number].ordrFuncDto.ordrFuncMtho;
          funcRowData.funcDscr =
            response.data[i as number].ordrFuncDto.ordrFuncDscr +
            "(" +
            response.data[i as number].ordrFuncDto.ordrFuncMtho +
            ")";
          funcRowData.retYn = response.data[i as number].ordrEvetFuncDto.retYn;
          funcRowData.validStartDtm =
            response.data[i as number].ordrEvetFuncDto.validStartDtm;
          funcRowData.validEndDtm =
            response.data[i as number].ordrEvetFuncDto.validEndDtm;
          arrEvetFunc.push(funcRowData);
        }
      } else if (workCd === "cust") {
        for (let i = 0; i < evetFuncTotal.value; i++) {
          funcRowData = new Object();
          funcRowData.evetFuncId =
            response.data[i as number].custEvetFuncDto.custEvetFuncId;
          funcRowData.evetItemId =
            response.data[i as number].custEvetFuncDto.custEvetItemId;
          funcRowData.funcId =
            response.data[i as number].custEvetFuncDto.custFuncId;
          funcRowData.evetFuncOdr = rowNum++;
          funcRowData.funcClss =
            response.data[i as number].custSvcClasDto.custClasNm;
          funcRowData.funcMtho =
            response.data[i as number].custFuncDto.custFuncMtho;
          funcRowData.funcDscr =
            response.data[i as number].custFuncDto.custFuncDscr +
            "(" +
            response.data[i as number].custFuncDto.custFuncMtho +
            ")";
          funcRowData.retYn = response.data[i as number].custEvetFuncDto.retYn;
          funcRowData.validStartDtm =
            response.data[i as number].custEvetFuncDto.validStartDtm;
          funcRowData.validEndDtm =
            response.data[i as number].custEvetFuncDto.validEndDtm;
          arrEvetFunc.push(funcRowData);
        }
      } else {
        arrEvetFunc.push(funcRowData);
      }

      rowData_evetFunc.value = arrEvetFunc;
    }
  } catch (error) {
    const objectAlert: any = {
      text: error,
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
  }
};

// 저장버튼 클릭 시
const handleUpdateButton = async () => {
  debugger;

  const selectedRows = gridApi_evetFunc.value.getSelectedRows();
  const gridApiRows = ref([]);

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 대상을 선택하세요.",
      width: "500",
    };
    globalStore.openAlertMessage(objectAlert);
    return;
  } else {
    debugger;
    gridApiRows.value = selectedRows.map((item) => {
      if (!item.hasOwnProperty("evetFuncId")) {
        return { ...item, evetFuncId: "" };
      }
      return item;
    });
    debugger;
    updateEvetFuncAdd(gridApiRows.value);
    /*const result = await updateEvetFunc(gridApiRows.value);
    if(result){
      emit("closeDialog", { 
        result: "SUCCESS", 
        evetItemId: evetItemIdData.value === null ? rowData_evetFunc.value[0].evetItemId : evetItemIdData.value,
      });
    }*/
  }
};

// 닫기버튼 클릭 시
const handleCloseButton = () => {
  emit("closeDialog", {
    result: "SUCCESS",
    evetItemId:
      evetItemIdData.value === null
        ? rowData_evetFunc.value[0].evetItemId
        : evetItemIdData.value,
  });
};

const onCellValueChanged = (params) => {
  debugger;
  const { colDef, newValue } = params;
  debugger;
  if (colDef.field === "evetFuncOdr") {
    const node = params.node;
    if (newValue) {
      node.setSelected(true);
    }
  }
};
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md cf-todos">
    <div class="grid-container">
      <div class="grid-wrapper">
        <div class="panel panel-primary" style="margin-right: 10px">
          <div class="flex items-center">
            <h4>기능</h4>
            <span class="total-text">Total : {{ funcTotal }}</span>
          </div>
          <div class="panel-body">
            <ag-grid-vue
              style="width: 400px; height: 500px"
              class="ag-theme-alpine"
              :column-defs="columnDefs_func"
              :row-data="rowData_func"
              row-selection="single"
              :suppress-row-click-selection="true"
              :tooltip-show-delay="500"
              @grid-ready="onFuncGridReady"
            ></ag-grid-vue>
          </div>
        </div>
        <div class="panel panel-primary" style="margin-right: 10px">
          <div class="flex items-center">
            <h4>이벤트기능순서</h4>
            <span class="total-text">Total : {{ evetFuncTotal }}</span>
            <div class="flex gap-2 items-center" style="margin-left: auto">
              <cf-button
                class="button"
                label="-행삭제"
                @click="handleDeleteButton"
              />
            </div>
          </div>
          <div class="panel-body">
            <ag-grid-vue
              id="evetFuncGrid"
              style="width: 1000px; height: 500px"
              class="ag-theme-alpine"
              :column-defs="columnDefs_evetFunc"
              :row-data="rowData_evetFunc"
              row-selection="multiple"
              :suppress-row-click-selection="true"
              :row-drag-managed="true"
              :tooltip-show-delay="500"
              @row-drag-end="onEvetFuncDragEnd"
              @grid-ready="onEvetFuncGridReady"
              @row-selected="onEvetItemRowSelect"
              @cell-value-changed="onCellValueChanged"
            ></ag-grid-vue>
          </div>
        </div>
      </div>
    </div>
    <div class="flex items-center">
      <div class="flex gap-2 items-center" style="margin-left: auto">
        <cf-button class="button" label="저장" @click="handleUpdateButton" />
        <cf-button class="button" label="닫기" @click="handleCloseButton" />
      </div>
    </div>
  </div>
</template>

<style scoped>
.total-text {
  margin-left: 20px;
  margin-bottom: -30px;
  font-size: 13px;
}
.button {
  color: #000000;
  background: #ffffff;
  border: 1px solid #828282;
  margin-bottom: -10px;
}
.button:hover {
  color: #000000;
  background: #dddddd;
  border: 1px solid #828282;
}
.grid-wrapper {
  display: flex;
  flex: 1 1 auto;
  margin-top: 5px;
}
.grid-wrapper .panel {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.grid-wrapper .panel-body {
  flex: 1 1 auto;
  overflow: hidden;
  padding: 0;
  display: flex;
}
</style>
