<script setup lang="ts">
import { AgGridVue } from "ag-grid-vue3";
import { useGlobal } from "@/store";
import { CommonUtil } from "@/utils/common-util";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import axios from "axios";
import ClasPage from "./SearchOrderPage.vue";
import CreateFuncModal from "./popup/CreateFuncModal.vue";
import CreateFuncPrmtModal from "./popup/CreateFuncPrmtModal.vue";

const globalStore = useGlobal();
const { translateMessage } = CommonUtil.useTranslatedMessage();
const formatDateValue = (params: any) => {
  if (!params.value) {
    return "";
  }
  return params.value.replace("T", " ");
};

const workCd = ref({ key: "ordr", value: "오더" });
workCd.value = workCd.value.key;
const radioValue = ref("VALD");
const radioItems = [
  { key: "전체", value: "ALL" },
  { key: "유효", value: "VALD" },
];
const funcClasNm = ref("");
const funcMtho = ref("");
const funcDscr = ref("");
const funcId = ref("");

const rowData_func = ref([]);
const gridApi_func = ref(null);
const editType_func = ref("fullRow");
const funcTotal = ref(0);

const rowData_funcPrmt = ref([]);
const gridApi_funcPrmt = ref(null);
const editType_funcPrmt = ref("fullRow");
const funcPrmtTotal = ref(0);

// 업무 inputvalue
const handleWorkCdUpdate = (value: any) => {
  workCd.value = value;
  radioValue.value = "VALD";
  resetData();
};

// 전체/유효 inputvalue
const handleRadioChange = (value: string) => {
  debugger;
  radioValue.value = value.target.value;
  resetData();
};

// 기능클래스 inputvalue
const handlefuncClasNmUpdate = (value: string) => {
  debugger;
  funcClasNm.value = value;
};

// 기능메소드 inputvalue
const handleFuncMthoUpdate = (value: string) => {
  debugger;
  funcMtho.value = value;
};

// 기능설명 inputvalue
const handleFuncDscrUpdate = (value: string) => {
  funcDscr.value = value;
};

const onFuncGridReady = (param: any) => {
  gridApi_func.value = param.api;
};

const onFuncPrmtGridReady = (param: any) => {
  gridApi_funcPrmt.value = param.api;
};
const updateFuncClasNmValue = async (value: string) => {
  if (!value) return;
  funcClasNm.value = value;
};

// 초기화
const resetData = () => {
  funcTotal.value = 0;
  rowData_func.value = [];

  funcPrmtTotal.value = 0;
  rowData_funcPrmt.value = [];
  gridApi_funcPrmt.value.setRowData([]);
};
// 기능컬럼
const columnDefs_func = ref([
  {
    checkboxSelection: true,
    headerCheckboxSelection: true,
    headerCheckboxSelectionFilteredOnly: true,
    width: 30,
  },
  {
    field: "funcClasNm",
    headerName: "기능클래스",
    tooltipField: "funcClasNm",
    width: 170,
    flex: 1,
  },
  {
    field: "funcMtho",
    headerName: "기능(영문)",
    tooltipField: "funcMtho",
    width: 270,
    flex: 1.7,
  },
  {
    field: "funcDscr",
    headerName: "기능(한글)",
    tooltipField: "funcDscr",
    width: 260,
    flex: 1.7,
  },
  {
    field: "validStartDtm",
    headerName: "유효시작일시",
    tooltipField: "validStartDtm",
    width: 170,
    flex: 1,
    valueFormatter: formatDateValue,
  },
  {
    field: "validEndDtm",
    headerName: "유효종료일시",
    tooltipField: "validEndDtm",
    width: 170,
    flex: 1,
    valueFormatter: formatDateValue,
  },
]);

// 기능파라미터컬럼
const columnDefs_funcPrmt = ref([
  {
    checkboxSelection: true,
    headerCheckboxSelection: true,
    headerCheckboxSelectionFilteredOnly: true,
    width: 30,
  },
  { field: "prmtClasNm", headerName: "입력파라미터", flex: 0.7 },
  { field: "prmtClasPathNm", headerName: "파라미터경로", flex: 1.3 },
]);

// 기능클래스 돋보기 버튼 클릭 시
const searchfuncClasNm = async () => {
  const objectModal: any = {
    title: "클래스 관리",
    component: ClasPage,
    dataInput: {
      funcClasNm: funcClasNm.value,
      workType: workCd.value,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);

  debugger;
  if (data) {
    updateFuncClasNmValue(data.clasNm);
  }
};

// 검색 버튼 클릭 시
const handleSearch = async () => {
  let arrEvetItem = new Array();
  let rowData = new Object();
  // 초기화
  resetData();

  try {
    let baseUrl = "";
    let response = "";

    if (workCd.value === "ordr") {
      baseUrl = "http://dev.service-billing.com/ordr/ordrfunc/v1/clas";
      response = await axios.get(
        ` ${baseUrl}?ordrClasNm=${funcClasNm.value}&ordrFuncMtho=${funcMtho.value}&ordrFuncDscr=${funcDscr.value}&srchDivsCd=${radioValue.value} `
      );
    } else if (workCd.value === "cust") {
      baseUrl = "http://dev.service-billing.com/cust/custfunc/v1/clas";
      response = await axios.get(
        ` ${baseUrl}?custClasNm=${funcClasNm.value}&custFuncMtho=${funcMtho.value}&custFuncDscr=${funcDscr.value}&srchDivsCd=${radioValue.value} `
      );
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
    debugger;

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
      funcTotal.value = response.data?.length;

      if (workCd.value === "ordr") {
        for (let i = 0; i < funcTotal.value; i++) {
          rowData = new Object();
          rowData.funcId = response.data[i as number].ordrFuncEntity.ordrFuncId;
          rowData.funcClasId =
            response.data[i as number].ordrFuncEntity.ordrFuncClasId;
          rowData.funcClasNm =
            response.data[i as number].ordrClasEntity.ordrClasNm;
          rowData.funcMtho =
            response.data[i as number].ordrFuncEntity.ordrFuncMtho;
          rowData.funcDscr =
            response.data[i as number].ordrFuncEntity.ordrFuncDscr;
          rowData.validStartDtm =
            response.data[i as number].ordrFuncEntity.validStartDtm;
          rowData.validEndDtm =
            response.data[i as number].ordrFuncEntity.validEndDtm;
          arrEvetItem.push(rowData);
        }
      } else if (workCd.value === "cust") {
        for (let i = 0; i < funcTotal.value; i++) {
          rowData = new Object();
          rowData.funcId = response.data[i as number].custFuncEntity.custFuncId;
          rowData.funcClasId =
            response.data[i as number].custFuncEntity.custFuncClasId;
          rowData.funcClasNm =
            response.data[i as number].custClasEntity.custClasNm;
          rowData.funcMtho =
            response.data[i as number].custFuncEntity.custFuncMtho;
          rowData.funcDscr =
            response.data[i as number].custFuncEntity.custFuncDscr;
          rowData.validStartDtm =
            response.data[i as number].custFuncEntity.validStartDtm;
          rowData.validEndDtm =
            response.data[i as number].custFuncEntity.validEndDtm;
          arrEvetItem.push(rowData);
        }
      } else {
        arrEvetItem.push(rowData);
      }
      debugger;
      rowData_func.value = arrEvetItem;
    }
  } catch (error) {
    debugger;
    const objectAlert: any = {
      text: error,
      width: "500",
    };

    await globalStore.openAlertMessage(objectAlert);
  }
};

// 기능 신규 버튼 클릭 시
const handleInsertButton = async () => {
  debugger;
  const objectModal: any = {
    title: "기능 관리",
    component: CreateFuncModal,
    dataInput: { buttonType: "insert", workCd: workCd.value },
    width: "800",
    height: "412",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data) {
    /*if(data.funcClasNm != null && data.funcClasNm != ""){
      handleSearch();
    }*/
  }
};

// 기능 수정 버튼 클릭 시
const handleUpdateButton = async () => {
  debugger;
  const selectedRows = gridApi_func.value.getSelectedRows();

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 수정할 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  const objectModal: any = {
    title: "기능 관리",
    component: CreateFuncModal,
    dataInput: {
      dataRow: selectedRows[0],
      buttonType: "update",
      workCd: workCd.value,
    },
    width: "800",
    height: "412",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data === "SUCCESS") {
    handleSearch();
  }
};

// 기능 삭제 버튼 클릭 시
const handleDeleteButton = async () => {
  const selectedRows = gridApi_func.value.getSelectedRows();

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 삭제할 대상을 선택하세요.",
      width: "500",
    };

    await globalStore.openAlertMessage(objectAlert);
    return;
  }
  const objectConfirm: any = {
    // title: translateMessage('common.msg_confirm'),
    text: translateMessage("삭제하시겠습니까?"),
    width: "500",
  };

  const result = await globalStore.openAlertConfirm(objectConfirm);
  if (result) {
    let dltResultData = new Object();
    dltResultData = selectedRows[0];

    try {
      let baseUrl = "";
      let custRequestBody = [];
      let ordrRequestBody = {};
      let response = "";

      if (workCd.value === "ordr") {
        ordrRequestBody = {
          ordrFuncId: dltResultData.funcId,
          ordrFuncClasId: dltResultData.funcClasId,
          ordrFuncMtho: dltResultData.funcMtho,
          ordrFuncDscr: dltResultData.funcDscr,
          validStartDtm: dltResultData.validStartDtm,
          validEndDtm: CommonOrdrUtil.getCurrentTime(),
        };

        baseUrl = "http://dev.service-billing.com/ordr/ordrfunc/v1";
        response = await axios.put(` ${baseUrl} `, ordrRequestBody);
      } else if (workCd.value === "cust") {
        custRequestBody = [
          {
            custFuncId: dltResultData.funcId,
            custFuncClasId: dltResultData.funcClasId,
            custFuncMtho: dltResultData.funcMtho,
            custFuncDscr: dltResultData.funcDscr,
            validStartDtm: dltResultData.validStartDtm,
            validEndDtm: CommonOrdrUtil.getCurrentTime(),
          },
        ];

        baseUrl = "http://dev.service-billing.com/cust/custfunc/v1/custfunc";
        response = await axios.post(` ${baseUrl} `, custRequestBody);
      } else {
        const objectAlert: any = {
          text: "URL을 다시 확인해 주세요.",
          width: "500",
        };

        await globalStore.openAlertMessage(objectAlert);
        return;
      }

      debugger;
      if (response.status === 200) {
        if (response.data.errorCode === "400") {
          const objectAlert: any = {
            text: response.data.errorMsg,
            width: "500",
          };

          await globalStore.openAlertMessage(objectAlert);
          return;
        }

        globalStore.setToastInfor(
          {
            text: "성공적으로 저장하였습니다.",
            type: "success",
            class: "bottom-center",
          },
          3000
        );
        handleSearch();
      }
    } catch (error) {
      globalStore.setToastInfor(
        {
          text: "저장에 실패하였습니다.",
          type: "error",
          class: "bottom-center",
        },
        3000
      );
    }
  } else {
    return;
  }
};

// 기능 신규 버튼 클릭 시
const handleInsertButtonPrmt = async () => {
  debugger;
  const selectedRows = gridApi_func.value.getSelectedRows();
  let titlePopupPrmt = "";

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "기능 목록에서 파라미터 추가 할 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  if (workCd.value === "ordr") {
    titlePopupPrmt = "오더기능 파라미터 관리";
  } else {
    titlePopupPrmt = "고객기능 파라미터 관리";
  }

  const objectModal: any = {
    title: titlePopupPrmt,
    component: CreateFuncPrmtModal,
    dataInput: {
      dataRow: selectedRows[0],
      buttonType: "insert",
      workCd: workCd.value,
    },
    width: "800",
    height: "412",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data === "SUCCESS") {
    onFuncRowSelect();
  }
};

// 기능 수정 버튼 클릭 시
const handleUpdateButtonPrmt = async () => {
  debugger;
  const selectedRows = gridApi_func.value.getSelectedRows();
  const selectedPrmtRows = gridApi_funcPrmt.value.getSelectedRows();
  let titlePopupPrmt = "";

  if (workCd.value === "ordr") {
    titlePopupPrmt = "오더파라미터 관리";
  } else {
    titlePopupPrmt = "고객파라미터 관리";
  }

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "기능 목록에서 파라미터 추가 할 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  if (selectedPrmtRows.length === 0) {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 수정할 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  const objectModal: any = {
    title: titlePopupPrmt,
    component: CreateFuncPrmtModal,
    dataInput: {
      dataRow: selectedRows[0],
      dataPrmtRow: selectedPrmtRows[0],
      buttonType: "update",
      workCd: workCd.value,
    },
    width: "800",
    height: "412",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data === "SUCCESS") {
    onFuncRowSelect();
  }
};

// 기능 삭제 버튼 클릭 시
const handleDeleteButtonPrmt = async () => {
  const selectedRows = gridApi_func.value.getSelectedRows();
  const selectedPrmtRows = gridApi_funcPrmt.value.getSelectedRows();

  if (selectedRows.length === 0) {
    const objectAlert: any = {
      text: "기능 목록에서 파라미터 추가 할 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  if (selectedPrmtRows.length === 0) {
    const objectAlert: any = {
      text: "선택된 row가 없습니다. 삭제할 대상을 선택하세요.",
      width: "500",
    };
    await globalStore.openAlertMessage(objectAlert);
    return;
  }

  const objectConfirm: any = {
    text: translateMessage("삭제하시겠습니까?"),
    width: "500",
  };

  const result = await globalStore.openAlertConfirm(objectConfirm);
  if (result) {
    let dltResultData = new Object();
    dltResultData = selectedRows[0];

    try {
      if (workCd.value === "ordr") {
        await axios
          .delete(
            `http://dev.service-billing.com/ordr/ordrfuncprmtrel/v1/${dltResultData.funcPrmtRelId}`
          )
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

              globalStore.setToastInfor(
                {
                  text: "성공적으로 삭제되었습니다.",
                  type: "success",
                  class: "bottom-center",
                },
                3000
              );
              onFuncRowSelect();
            }
          });
      } else if (workCd.value === "cust") {
        await axios
          .delete(
            `http://dev.service-billing.com/cust/custfuncprmtrel/v1/${dltResultData.funcPrmtRelId}`
          )
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

              globalStore.setToastInfor(
                {
                  text: "성공적으로 삭제되었습니다.",
                  type: "success",
                  class: "bottom-center",
                },
                3000
              );
              onFuncRowSelect();
            }
          });
      } else {
        const objectAlert: any = {
          text: "URL을 다시 확인해 주세요.",
          width: "500",
        };

        await globalStore.openAlertMessage(objectAlert);
        return;
      }
    } catch (error) {
      globalStore.setToastInfor(
        {
          text: "삭제에 실패하였습니다.",
          type: "error",
          class: "bottom-center",
        },
        3000
      );
    }
  } else {
    return;
  }
};

// 기능 row select
const onFuncRowSelect = () => {
  const gridApi_rowFunc = gridApi_func.value.getSelectedRows();

  if (gridApi_rowFunc.length !== 0) {
    // 기능ID
    funcId.value = gridApi_rowFunc[0].funcId;

    onFuncPrmtSearch(funcId.value);
  }
};

// 기능 파라미터 조회
const onFuncPrmtSearch = async (funcId) => {
  let funcList = new Array();
  let funcPrmtRowData = new Object();
  rowData_funcPrmt.value = [];
  funcPrmtTotal.value = 0;

  try {
    let baseUrl = "";
    //let response = "";

    if (workCd.value === "ordr") {
      baseUrl = "http://dev.service-billing.com/ordr/ordrfunc";
    } else if (workCd.value === "cust") {
      baseUrl = "http://dev.service-billing.com/cust/custfunc";
    } else {
      const objectAlert: any = {
        text: "URL을 다시 확인해 주세요.",
        width: "500",
      };

      const result = await globalStore.openAlertMessage(objectAlert);
      if (result) {
        funcPrmtTotal.value = 0;
        rowData_funcPrmt.value = [];
      }
      return;
    }

    const response = await axios.get(` ${baseUrl}/v1/prmt/${funcId} `);
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
      funcPrmtTotal.value = response.data?.length;

      if (workCd.value === "ordr") {
        for (let i = 0; i < funcPrmtTotal.value; i++) {
          funcPrmtRowData = new Object();
          funcPrmtRowData.funcPrmtRelId =
            response.data[i as number].ordrFuncPrmtRelId;
          funcPrmtRowData.funcId = response.data[i as number].ordrFuncId;
          funcPrmtRowData.prmtClasId =
            response.data[i as number].ordrPrmtClasId;
          funcPrmtRowData.prmtClasNm =
            response.data[i as number].ordrPrmtClasNm;
          funcPrmtRowData.prmtClasPathNm =
            response.data[i as number].ordrPrmtClasPathNm;
          funcList.push(funcPrmtRowData);
        }
      } else if (workCd.value === "cust") {
        for (let i = 0; i < funcPrmtTotal.value; i++) {
          funcPrmtRowData = new Object();
          funcPrmtRowData.funcPrmtRelId =
            response.data[i as number].custFuncPrmtRelId;
          funcPrmtRowData.funcId = response.data[i as number].custFuncId;
          funcPrmtRowData.prmtClasId =
            response.data[i as number].custPrmtClasId;
          funcPrmtRowData.prmtClasNm =
            response.data[i as number].custPrmtClasNm;
          funcPrmtRowData.prmtClasPathNm =
            response.data[i as number].custPrmtClasPathNm;
          funcList.push(funcPrmtRowData);
        }
      } else {
        funcList.push(funcPrmtRowData);
      }

      rowData_funcPrmt.value = funcList;
    }
  } catch (error) {
    const objectAlert: any = {
      text: error,
      width: "500",
    };

    const result = await globalStore.openAlertMessage(objectAlert);
    if (result) {
      funcPrmtTotal.value = 0;
      rowData_funcPrmt.value = [];
    }
  }
};
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md cf-todos">
    <h1 class="mt-3 text-3xl font-extrabold tracking-tight text-slate-900">
      기능 관리
    </h1>
    <div class="flex flex-col gap-4">
      <div class="flex gap-4 items-center">
        <span>업무 :</span>
        <div class="flex w-[100px]">
          <cf-select-box
            label=""
            :items="[
              { key: 'cust', value: '고객' },
              { key: 'ordr', value: '오더' },
            ]"
            item-title="value"
            item-value="key"
            :rules="workCdRules"
            :model="workCd"
            @update:model="handleWorkCdUpdate"
          ></cf-select-box>
        </div>
        <cf-radio
          v-model="radioValue"
          inline
          :radio-items="radioItems"
          label="key"
          value="value"
          @change="handleRadioChange"
        ></cf-radio>
      </div>
    </div>
    <div class="flex flex-col gap-4">
      <div class="flex gap-4 items-center">
        <span>기능클래스 :</span>
        <div class="flex w-[170px]">
          <cf-input
            id="funcClasNm"
            v-model="funcClasNm"
            label=""
            :rules="funcClasNmRules"
            :model="funcClasNm"
            maxlength="20"
            autocomplete="off"
            oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
            @update:model="handlefuncClasNmUpdate"
          ></cf-input>
        </div>
        <v-btn size="40" @click="searchfuncClasNm">
          <span class="i-mdi-search" style="font-size: 24px"></span>
        </v-btn>
        <span>기능메소드 :</span>
        <div class="flex w-[180px]">
          <cf-input
            id="funcMtho"
            v-model="funcMtho"
            label=""
            :model="funcMtho"
            autocomplete="off"
            maxlength="100"
            oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
            @update:model="handleFuncMthoUpdate"
            @blur="funcMthoBlur"
          ></cf-input>
        </div>
        <span>기능설명 :</span>
        <div class="flex w-[180px]">
          <cf-input
            id="funcDscr"
            v-model="funcDscr"
            label=""
            :model="funcDscr"
            autocomplete="off"
            maxlength="100"
            @update:model="handleFuncDscrUpdate"
          ></cf-input>
        </div>
        <cf-button class="search-button" label="검색" @click="handleSearch" />
      </div>
    </div>
    <div class="flex items-center">
      <h4 class="">기능</h4>
      <span class="total-text">Total : {{ funcTotal }}</span>
      <div class="flex gap-2 items-center" style="margin-left: auto">
        <cf-button
          class="popup-button"
          label="신규"
          @click="handleInsertButton"
        />
        <cf-button
          class="popup-button"
          label="수정"
          @click="handleUpdateButton"
        />
        <cf-button
          class="popup-button"
          label="삭제"
          @click="handleDeleteButton"
        />
      </div>
    </div>
    <div class="grid-container">
      <ag-grid-vue
        style="width: 100%; height: 330px"
        class="ag-theme-alpine"
        :column-defs="columnDefs_func"
        :row-data="rowData_func"
        :edit-type="editType_func"
        :pagination="true"
        :pagination-page-size-selector="[5, 10, 20]"
        :pagination-page-size="5"
        row-selection="single"
        :tooltip-show-delay="500"
        @grid-ready="onFuncGridReady"
        @row-clicked="onFuncRowClick"
        @row-selected="onFuncRowSelect"
        @cell-value-changed="onFuncCellValueChanged"
      ></ag-grid-vue>
    </div>
    <div class="flex items-center">
      <h4>기능 파라미터</h4>
      <span class="total-text">Total : {{ funcPrmtTotal }}</span>
      <div class="flex gap-2 items-center" style="margin-left: auto">
        <cf-button
          class="popup-button"
          label="신규"
          @click="handleInsertButtonPrmt"
        />
        <cf-button
          class="popup-button"
          label="수정"
          @click="handleUpdateButtonPrmt"
        />
        <cf-button
          class="popup-button"
          label="삭제"
          @click="handleDeleteButtonPrmt"
        />
      </div>
    </div>
    <div class="grid-container">
      <ag-grid-vue
        style="width: 100%; height: 330px"
        class="ag-theme-alpine"
        :column-defs="columnDefs_funcPrmt"
        :row-data="rowData_funcPrmt"
        :edit-type="editType_funcPrmt"
        row-selection="single"
        @grid-ready="onFuncPrmtGridReady"
        @cell-value-changed="onCellValueChanged"
      ></ag-grid-vue>
    </div>
  </div>
</template>

<style scoped>
.workSelect {
  padding-bottom: 5px;
  padding-top: 5px;
  font-size: 13px;
}
.search-button {
  background: black;
  margin-left: auto;
}
.search-button:hover {
  background: #4d4d4d;
}
.popup-button {
  color: #000000;
  background: #ffffff;
  border: 1px solid #828282;
  margin-bottom: -10px;
}
.popup-button:hover {
  color: #000000;
  background: #dddddd;
  border: 1px solid #828282;
}
.total-text {
  margin-left: 20px;
  margin-bottom: -30px;
  font-size: 13px;
}
.searchImg-btn {
  width: 30px;
  height: 30px;
  border: 1px solid #828282;
}
.custom-authenticate.v-btn {
  background-color: #f3f3f3;
}
.custom-btn {
  color: #000000 !important;
  background: #ffffff;
  border: 1px solid #828282;
  margin-top: 2px;
  height: 40px !important;
  width: 90px;
}
</style>
