<script setup lang="ts">
import { useGlobal } from "@/store";
import axios from "axios";
import DatetimePicker from "@/components/controls/CfDatetimePicker.vue";
import ClasPage from "@/pages/solution/SearchOrderPage.vue";

const formInput = ref<any>(null);
const InputPrmt = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const globalStore = useGlobal();
const funcId = ref("");
const funcClasId = ref("");
const funcClasNm = ref("");
const funcMtho = ref("");
const funcDscr = ref("");
const valdStrtDtm = ref("");
const validEndDtm = ref("");

debugger;
const workCd = InputPrmt.data.workCd;

// 수정
if (InputPrmt.data.buttonType == "update") {
  funcId.value = InputPrmt.data.dataRow.funcId;
  funcClasId.value = InputPrmt.data.dataRow.funcClasId;
  funcClasNm.value = InputPrmt.data.dataRow.funcClasNm;
  funcMtho.value = InputPrmt.data.dataRow.funcMtho;
  funcDscr.value = InputPrmt.data.dataRow.funcDscr;
  valdStrtDtm.value = InputPrmt.data.dataRow.valdStrtDtm;
  validEndDtm.value = InputPrmt.data.dataRow.validEndDtm;

  debugger;
}

const funcClasNmRules = [
  (value: string) => !!value || "기능클래스를 입력해 주세요.",
  (value: string) => /^[A-Za-z 0-9]+$/.test(value) || "영문만 입력해 주세요.",
];
const funcMthoRules = [
  (value: string) => !!value || "기능메소드를 입력해 주세요.",
  (value: string) => /^[A-Za-z 0-9]+$/.test(value) || "영문만 입력해 주세요.",
];
const funcDscrRules = [
  (value: string) => !!value || "기능설명을 입력해 주세요.",
];

const handleFuncClasNmUpdate = (value: string) => {
  funcClasNm.value = value;
};
const funcClasNmUpdate = (event) => {
  funcClasNm.value = event.target.value;
};

const handleFuncMthoUpdate = (value: string) => {
  funcMtho.value = value;
};
const funcMthoUpdate = (event) => {
  funcMtho.value = event.target.value;
};

const handleFuncDscrUpdate = (value: string) => {
  funcDscr.value = value;
};
const funcDscrUpdate = (event) => {
  funcDscr.value = event.target.value;
};

const validateStartDate = () => {
  if (valdStrtDtm.value && new Date(valdStrtDtm.value) < new Date()) {
    globalStore.setToastInfor(
      {
        text: "유효시작일시는 현재보다 과거일 수 없음",
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
        class: "bottom-center",
      },
      5000
    );
    return true;
  }
};

const validateEndDate = () => {
  if (
    validEndDtm.value &&
    new Date(validEndDtm.value) < new Date(valdStrtDtm.value)
  ) {
    globalStore.setToastInfor(
      {
        text: "유효종료일시는 유효시작일시보다 과거일 수 없음",
        border: "start",
        borderColor: "white",
        type: "error",
        icon: "$error",
        class: "bottom-center",
      },
      5000
    );
    return true;
  }
};

const handleValdStrtDtm = (val: string) => {
  valdStrtDtm.value = val;
  validateStartDate();
};

const handleValdEndDtm = (val: string) => {
  validEndDtm.value = val;
  validateEndDate();
};

// 기능클래스 돋보기 버튼 클릭 시
const searchFuncClasNm = async () => {
  debugger;
  const objectModal: any = {
    title: "클래스 관리",
    component: ClasPage,
    dataInput: {
      funcClasNm: funcClasNm.value,
      workType: workCd,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data.clasNm !== null && data.clasNm !== "") {
    funcClasId.value = data.clasId;
    funcClasNm.value = data.clasNm;
  }
};

// 저장 버튼 클릭 시
const handleUpdateButton = async () => {
  debugger;
  const { valid } = await formInput.value.validate();

  if (valid) {
    try {
      let baseUrl = "";
      let custRequestBody: any[] = [];
      let ordrRequestBody = {};
      let response: any = "";
      const clasIDResult: any[] = await onClasIDSearch();
      debugger;
      if (clasIDResult.length === 0 && funcClasId.value === "") {
        const objectAlert: any = {
          text: "클래스ID가 존재하지 않습니다.",
          width: "500",
        };

        await globalStore.openAlertMessage(objectAlert);
        return;
      }

      // 업데이트
      if (InputPrmt.data.buttonType == "update") {
        // 오더
        if (workCd === "ordr") {
          ordrRequestBody = {
            ordrFuncId: funcId.value,
            ordrFuncClasId: funcClasId.value,
            ordrFuncMtho: funcMtho.value,
            ordrFuncDscr: funcDscr.value,
            valdStrtDtm: valdStrtDtm.value,
            validEndDtm: validEndDtm.value,
          };

          baseUrl = "http://dev.service-billing.com/ordr/ordrfunc/v1";
          response = await axios.put(` ${baseUrl} `, ordrRequestBody);

          // 고객
        } else if (workCd === "cust") {
          custRequestBody = [
            {
              custFuncId: funcId.value,
              custFuncClasId: funcClasId.value,
              custFuncMtho: funcMtho.value,
              custFuncDscr: funcDscr.value,
              valdStrtDtm: valdStrtDtm.value,
              validEndDtm: validEndDtm.value,
            },
          ];

          baseUrl = "http://dev.service-billing.com/cust/custfunc/v1/custfunc";
          response = await axios.post(` ${baseUrl} `, custRequestBody);
        }

        // 등록
      } else if (InputPrmt.data.buttonType == "insert") {
        // 오더
        if (workCd === "ordr") {
          ordrRequestBody = {
            ordrFuncClasId: funcClasId.value,
            ordrFuncMtho: funcMtho.value,
            ordrFuncDscr: funcDscr.value,
            valdStrtDtm: valdStrtDtm.value,
            validEndDtm: validEndDtm.value,
          };

          baseUrl = "http://dev.service-billing.com/ordr/ordrfunc/v1";
          response = await axios.post(` ${baseUrl} `, ordrRequestBody);

          // 고객
        } else if (workCd === "cust") {
          custRequestBody = [
            {
              custFuncClasId: funcClasId.value,
              custFuncMtho: funcMtho.value,
              custFuncDscr: funcDscr.value,
              valdStrtDtm: valdStrtDtm.value,
              validEndDtm: validEndDtm.value,
            },
          ];

          baseUrl = "http://dev.service-billing.com/cust/custfunc/v1/custfunc";
          response = await axios.post(` ${baseUrl} `, custRequestBody);
        }
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
          alert(response.data.errorMsg);
          return;
        }

        globalStore.setToastInfor(
          {
            text: "성공적으로 저장하였습니다.",
            type: "success",
            variant: "tonal",
            class: "bottom-center",
          },
          3000
        );

        debugger;
        emit("closeDialog", "SUCCESS");
      }
    } catch (error) {
      globalStore.setToastInfor(
        {
          text: "저장에 실패하였습니다.",
          type: "error",
          variant: "tonal",
          class: "bottom-center",
        },
        3000
      );
      emit("closeDialog");
    }
  }
};

// 클래스ID 조회
const onClasIDSearch = function (): Promise<any[]> {
  return new Promise((resolve, reject) => {
    let baseUrl = "";

    // 오더
    if (workCd === "ordr") {
      baseUrl = "http://dev.service-billing.com/ordr/ordrclas/v1?";
      axios
        .get(` ${baseUrl}ordrClasNm=${funcClasNm.value} `)
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
              funcClasId.value = response.data[0].ordrClasId;
            } else {
              const objectAlert: any = {
                text: "기능클래스가 존재하지 않습니다. 기능클래스를 다시 확인해 주세요.",
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }
            resolve(response.data);
          }
          reject(false);
        })
        .catch((error) => {
          alert("fail : " + error);
          reject(false);
        });

      // 고객
    } else if (workCd === "cust") {
      debugger;
      baseUrl = "http://dev.service-billing.com/cust/custclas/v1?";
      axios
        .get(` ${baseUrl}custClasNm=${funcClasNm.value} `)
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

            if (response.data?.length > 0) {
              funcClasId.value = response.data[0].custClasId;
            } else {
              const objectAlert: any = {
                text: "기능클래스가 존재하지 않습니다. 기능클래스를 다시 확인해 주세요.",
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }
          }
          resolve(response.data);
        })
        .catch((error) => {
          alert("fail : " + error);
          reject(false);
        });
    }
  });
};

// 닫기 버튼 클릭 시
const emit = defineEmits(["closeDialog"]);
const handleCancleButton = () => {
  emit("closeDialog", funcClasNm.value);
};
</script>

<template>
  <div class="mx-auto prose prose-indigo">
    <div class="flex flex-col mt-[65px]">
      <v-form ref="formInput">
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label
              for="funcClasNm"
              class="w-[190px] h-[50px] font-semibold text-l"
            >
              <span class="font-semibold text-[#FF0404]">*</span>기능클래스
            </label>
            <cf-input
              v-model="funcClasNm"
              :model="funcClasNm"
              :rules="funcClasNmRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="20"
              oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
              class="w-[346px]"
              @update:model="handleFuncClasNmUpdate"
              @input="funcClasNmUpdate"
            ></cf-input>
            <v-btn
              class="searchImg-btn !w-[20px] !h-[40px]"
              @click="searchFuncClasNm"
            >
              <v-icon class="i-mdi-search" style="font-size: x-large"></v-icon>
            </v-btn>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label
              for="funcMtho"
              class="w-[190px] h-[50px] font-semibold text-l"
            >
              <span class="font-semibold text-[#FF0404]">*</span>기능메소드
            </label>
            <cf-input
              v-model="funcMtho"
              :model="funcMtho"
              :rules="funcMthoRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="100"
              class="w-[410px]"
              oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
              @update:model="handleFuncMthoUpdate"
              @input="funcMthoUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label
              for="funcDscr"
              class="w-[190px] h-[50px] font-semibold text-l"
              ><span class="font-semibold text-[#FF0404]">*</span
              >기능설명</label
            >
            <cf-input
              v-model="funcDscr"
              :model="funcDscr"
              :rules="funcDscrRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="100"
              class="w-[410px]"
              @update:model="handleFuncDscrUpdate"
              @input="funcDscrUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div
            class="flex flex-wrap ml-[56px] mr-[56px] mb-[20px] align-center"
          >
            <label for="input2" class="w-[190px] h-[50px] font-semibold text-l"
              >유효시작일시</label
            >
            <div class="datepicker">
              <DatetimePicker
                :model="valdStrtDtm"
                class="w-[410px] h-[43px]"
                variant="outlined"
                @update:model="handleValdStrtDtm"
              />
            </div>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label for="input2" class="w-[190px] h-[50px] font-semibold text-l"
              >유효종료일시</label
            >
            <div class="datepicker">
              <DatetimePicker
                :model="validEndDtm"
                class="w-[410px] h-[43px]"
                variant="outlined"
                @update:model="handleValdEndDtm"
              />
            </div>
          </div>
        </div>
        <div class="flex justify-end mr-[86px] mt-[56px]">
          <cf-button
            label="저장"
            class="popup-button"
            @click="handleUpdateButton"
          />
          <cf-button
            label="닫기"
            class="popup-button"
            @click="handleCancleButton"
          />
        </div>
      </v-form>
    </div>
  </div>
</template>

<style scoped>
.custom-btn {
  background-color: transparent;
  border-radius: 8px;
  border: 1px solid #828282;
  color: #000000;
  height: 43px !important;

  font-weight: 400;
  font-size: 17px;
  padding: 8px;

  width: 77px;
  margin-right: 10px;
}

.popup-button {
  color: #000000;
  background: #ffffff;
  border: 1px solid #828282;
  margin-right: 9px;
  margin-bottom: 20px;
  font-size: 16px;
  width: 77px;
  height: 38px !important;
}
.popup-button:hover {
  color: #000000;
  background: #dddddd;
  border: 1px solid #828282;
}

.searchImg-btn {
  border: 1px solid #acacac;
  margin-bottom: 22px;
  box-shadow: none;
}

:deep() .v-field__input {
  min-height: 40px;
  height: 40px;
  --v-field-input-padding-top: 5px;
  padding-bottom: 5px;
  color: #2a2a2a;
  font-size: 15px;
}

.cf-datepicker-input :deep(.dp--menu-wrapper) {
  top: 60px !important;
  left: 200px !important;
  margin: 0 auto;
  display: block;
  position: fixed;
  width: 320px;
  padding: 0;
}
.cf-datepicker-input :deep(.dp__arrow_bottom) {
  height: 0px !important;
  width: 0px !important;
}

.cf-datepicker-input :deep(input) {
  height: 43px !important;
}
</style>
