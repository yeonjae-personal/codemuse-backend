<script setup lang="ts">
import { useGlobal } from "@/store";
import axios from "axios";
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
const funcClasNm = ref<any>("");
const funcClasPath = ref<any>("");
const funcPrmtRelId = ref("");

debugger;
const workCd = InputPrmt.data.workCd;

// 수정
if (InputPrmt.data.buttonType == "update") {
  funcId.value = InputPrmt.data.dataRow.funcId;
  funcClasId.value = InputPrmt.data.dataPrmtRow.prmtClasId;
  funcClasNm.value = InputPrmt.data.dataPrmtRow.prmtClasNm;
  funcClasPath.value = InputPrmt.data.dataPrmtRow.prmtClasPathNm;
  funcPrmtRelId.value = InputPrmt.data.dataPrmtRow.funcPrmtRelId;
}

if (InputPrmt.data.buttonType == "insert") {
  funcId.value = InputPrmt.data.dataRow.funcId;
}
debugger;

const funcClasNmRules = [
  (value: string) => !!value || "입력파라미터를 입력해 주세요.",
];
const funcClasPathRules = [];

const handleFuncClasNmUpdate = (value: String) => {
  funcClasNm.value = value;
  if (funcClasNm.value == "") {
    funcClasPath.value = "";
  }
};
const funcClasNmUpdate = (event) => {
  funcClasNm.value = event.target.value;
};

const handleFuncClasPathUpdate = (value: String) => {
  funcClasPath.value = value;
};
const funcClasPathUpdate = (event) => {
  funcClasPath.value = event.target.value;
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
  if (data !== undefined) {
    funcClasId.value = data.clasId;
    funcClasNm.value = data.clasNm;
    funcClasPath.value = data.clasPath;
  }
};

// 저장 버튼 클릭 시
const handleUpdateButton = async () => {
  debugger;
  const { valid } = await formInput.value.validate();

  if (valid) {
    try {
      let baseUrl = "";
      let custRequestBody = {};
      let ordrRequestBody = {};
      let response: any = "";

      // 업데이트
      if (InputPrmt.data.buttonType == "update") {
        // 오더
        if (workCd === "ordr") {
          ordrRequestBody = {
            ordrFuncPrmtRelId: funcPrmtRelId.value,
            ordrFuncId: funcId.value,
            ordrPrmtClasId: funcClasId.value,
          };

          baseUrl = "http://dev.service-billing.com/ordr/ordrfuncprmtrel/v1";
          response = await axios.put(` ${baseUrl} `, ordrRequestBody);

          // 고객
        } else if (workCd === "cust") {
          custRequestBody = {
            custFuncPrmtRelId: funcPrmtRelId.value,
            custFuncId: funcId.value,
            custPrmtClasId: funcClasId.value,
          };

          baseUrl = "http://dev.service-billing.com/cust/custfuncprmtrel/v1";
          response = await axios.put(` ${baseUrl} `, custRequestBody);
        }

        // 등록
      } else if (InputPrmt.data.buttonType == "insert") {
        // 오더
        if (workCd === "ordr") {
          ordrRequestBody = {
            ordrFuncId: funcId.value,
            ordrPrmtClasId: funcClasId.value,
          };

          baseUrl = "http://dev.service-billing.com/ordr/ordrfuncprmtrel/v1";
          response = await axios.post(` ${baseUrl} `, ordrRequestBody);

          // 고객
        } else if (workCd === "cust") {
          custRequestBody = {
            custFuncId: funcId.value,
            custPrmtClasId: funcClasId.value,
          };

          baseUrl = "http://dev.service-billing.com/cust/custfuncprmtrel/v1";
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
              <span class="font-semibold text-[#FF0404]">*</span>입력파라미터
            </label>
            <cf-input
              v-model="funcClasNm"
              :model="funcClasNm"
              :rules="funcClasNmRules"
              variant="outlined"
              maxlength="20"
              oninput="javascript: this.value = this.value.replace(/[^a-zA-Z0-9]/g, '' );"
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
              for="funcClasPath"
              class="w-[190px] h-[50px] font-semibold text-l"
            >
              <span class="font-semibold text-[#FF0404]">*</span>파라미터경로
            </label>
            <cf-input
              v-model="funcClasPath"
              :model="funcClasPath"
              :rules="funcClasPathRules"
              variant="outlined"
              disabled="true"
              class="w-[410px]"
              @update:model="handleFuncClasPathUpdate"
              @input="funcClasPathUpdate"
            ></cf-input>
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
</style>
