<script setup lang="ts">
import SearchImg from "@/assets/search.png";
import { useGlobal } from "@/store";
import sysCdPage from "@/pages/solution/SystemPage.vue";
import eventPage from "@/pages/solution/OrderEventPage.vue";
import axios from "axios";

debugger;

const formInput = ref<any>(null);
const InputPrmt = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const globalStore = useGlobal();

const evetItemId = ref("");
const sndSysCd = ref("");
const sndSysCdNm = ref("");
const rcpSysCd = ref("");
const rcpSysCdNm = ref("");
const evetId = ref("");
const evetCd = ref("");
const evetCdNm = ref("");
const evetDetlCd = ref("");
const evetDetlCdNm = ref("");
const validStartDtm = ref("");
const validEndDtm = ref("");

debugger;
const workCd = InputPrmt.data.workCd;

// 수정
if (InputPrmt.data.buttonType == "update") {
  evetItemId.value = InputPrmt.data.dataRow.evetItemId;
  sndSysCd.value = InputPrmt.data.dataRow.sndSysCd;
  sndSysCdNm.value = InputPrmt.data.dataRow.sndSysCdNm;
  rcpSysCd.value = InputPrmt.data.dataRow.rcpSysCd;
  rcpSysCdNm.value = InputPrmt.data.dataRow.rcpSysCdNm;
  evetCd.value = InputPrmt.data.dataRow.evetCd;
  evetCdNm.value = InputPrmt.data.dataRow.evetCdNm;
  evetDetlCd.value = InputPrmt.data.dataRow.evetDetlCd;
  evetDetlCdNm.value = InputPrmt.data.dataRow.evetDetlCdNm;
  validStartDtm.value = InputPrmt.data.dataRow.validStartDtm;
  validEndDtm.value = InputPrmt.data.dataRow.validEndDtm;

  debugger;
}

const sndSysCdRules = [
  (value: string) => !!value || "송신시스템코드를 입력해 주세요.",
  (value: string) => /^[A-Za-z 0-9]+$/.test(value) || "영문만 입력해 주세요.",
];
const rcpSysCdRules = [
  (value: string) => !!value || "수신시스템코드를 입력해 주세요.",
  (value: string) => /^[A-Za-z 0-9]+$/.test(value) || "영문만 입력해 주세요.",
];
const evetCdRules = [
  (value: string) => !!value || "이벤트코드를 입력해 주세요.",
  (value: string) => /^[A-Za-z 0-9]+$/.test(value) || "영문만 입력해 주세요.",
];
const evetDetlCdRules = [
  (value: string) => !!value || "이벤트상세코드를 입력해 주세요.",
  (value: string) => /^[A-Za-z 0-9]+$/.test(value) || "영문만 입력해 주세요.",
];

const handleSndSysCdUpdate = (value: String) => {
  sndSysCd.value = value.toUpperCase();

  if (sndSysCd.value === "") {
    sndSysCdNm.value = "";
  }
};
const sndSysCdUpdate = (event) => {
  sndSysCd.value = event.target.value.toUpperCase();
};

const handleRcpSysCdUpdate = (value: String) => {
  rcpSysCd.value = value.toUpperCase();

  if (rcpSysCd.value === "") {
    rcpSysCdNm.value = "";
  }
};
const rcpSysCdUpdate = (event) => {
  rcpSysCd.value = event.target.value.toUpperCase();
};

const handleEvetCdUpdate = (value: String) => {
  evetCd.value = value.toUpperCase();

  if (evetCd.value === "") {
    evetCdNm.value = "";
    evetDetlCd.value = "";
    evetDetlCdNm.value = "";
  }
};
const evetCdUpdate = (event) => {
  evetCd.value = event.target.value.toUpperCase();
};

const handleEvetDetlCdUpdate = (value: String) => {
  evetDetlCd.value = value.toUpperCase();
};
const evetDetlCdUpdate = (event) => {
  evetDetlCd.value = event.target.value.toUpperCase();
};

// 송신 시스템코드 돋보기 버튼 클릭 시
const searchSndSysCd = async () => {
  debugger;
  console.log("== TEST송신시스템 : " + sndSysCd.value);
  const objectModal: any = {
    title: "시스템 관리",
    component: sysCdPage,
    dataInput: {
      sysCd: sndSysCd.value,
      workType: workCd,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data.sysCd !== null && data.sysCd !== "") {
    console.log("=== 시스템관리 송신시스템코드 : " + data);
    sndSysCd.value = data.sysCd;
    sndSysCdNm.value = data.sysCdNm;
  }
};

// 수신 시스템코드 돋보기 버튼 클릭 시
const searchRcpSysCd = async () => {
  debugger;
  console.log("== TEST수신시스템 : " + rcpSysCd.value);
  const objectModal: any = {
    title: "시스템 관리",
    component: sysCdPage,
    dataInput: {
      sysCd: rcpSysCd.value,
      workType: workCd,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data.sysCd !== null && data.sysCd !== "") {
    console.log("=== 시스템관리 수신시스템코드 : " + data);
    rcpSysCd.value = data.sysCd;
    rcpSysCdNm.value = data.sysCdNm;
  }
};

// 이벤트코드 돋보기 버튼 클릭 시
const searchEvetCd = async () => {
  const objectModal: any = {
    title: "이벤트 관리",
    component: eventPage,
    dataInput: {
      evetCd: evetCd.value,
      workType: workCd,
      screenType: "popUp",
    },
    width: "920",
    type: "custom",
  };
  const data = await globalStore.openModal(objectModal);
  debugger;
  if (data.sysCd !== null && data.sysCd !== "") {
    console.log("=== 시스템관리 수신시스템코드 : " + data);
    evetCd.value = data.evetCd;
    evetCdNm.value = data.evetCdNm;
    evetDetlCd.value = data.evetDetlCd;
    evetDetlCdNm.value = data.evetDetlCdNm;
  }
};

// 이벤트ID 조회
const onEvetIDSearch = function () {
  return new Promise((resolve, reject) => {
    debugger;
    let baseUrl = "";
    //const response = "";

    if (workCd === "ordr") {
      baseUrl = "http://dev.service-billing.com/ordr/ordrevet/v1?";
      axios
        .get(
          ` ${baseUrl}ordrEvetCd=${evetCd.value}&ordrEvetDetlCd=${evetDetlCd.value} `
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

            if (response.data?.length > 0) {
              if (workCd === "ordr") {
                evetId.value = response.data[0].ordrEvetId;
              } else if (workCd === "cust") {
                evetId.value = response.data[0].custEvetId;
              }
            } else {
              const objectAlert: any = {
                text: "이벤트가 존재하지 않습니다. 이벤트코드를 다시 확인해 주세요.",
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
          console.log(error);
          alert("fail : " + error);
          reject(false);
        });
    } else if (workCd === "cust") {
      baseUrl = "http://dev.service-billing.com/cust/custevet/v1/custevet?";
      axios
        .get(
          ` ${baseUrl}custEvetCd=${evetCd.value}&custEvetDetlCd=${evetDetlCd.value} `
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

            if (response.data?.length > 0) {
              if (workCd === "ordr") {
                evetId.value = response.data[0].ordrEvetId;
              } else if (workCd === "cust") {
                evetId.value = response.data[0].custEvetId;
              }
            } else {
              const objectAlert: any = {
                text: "이벤트가 존재하지 않습니다. 이벤트코드를 다시 확인해 주세요.",
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }
          }
          resolve(response.data);
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

    debugger;
  });
};

// 이벤트항목 조회
const onEvetItemSearch = function () {
  return new Promise((resolve, reject) => {
    let baseUrl = "";

    if (workCd === "ordr") {
      baseUrl =
        "http://dev.service-billing.com/ordr/ordrevetitem/v1/ordrevetitem?";
      axios
        .get(
          ` ${baseUrl}sndSysCd=${sndSysCd.value}&rcpSysCd=${rcpSysCd.value}&ordrEvetId=${evetId.value} `
        )
        .then((response) => {
          if (response.status === 200) {
            if (response.data.errorCode === "400") {
              alert(response.data.errorMsg);
              return;
            }

            if (response.data?.length > 0) {
              const objectAlert: any = {
                text: "이벤트 항목이 이미 존재합니다. 다시 입력해 주세요.",
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            resolve(true);
          }
          reject(false);
        })
        .catch((error) => {
          console.log(error);
          alert("fail : " + error);
          reject(false);
        });
    } else if (workCd === "cust") {
      baseUrl =
        "http://dev.service-billing.com/cust/custevetitem/v1/custevetitem?";
      axios
        .get(
          ` ${baseUrl}sndSysCd=${sndSysCd.value}&rcpSysCd=${rcpSysCd.value}&custEvetId=${evetId.value} `
        )
        .then((response) => {
          if (response.status === 200) {
            if (response.data.errorCode === "400") {
              alert(response.data.errorMsg);
              return;
            }

            if (response.data?.length > 0) {
              const objectAlert: any = {
                text: "이벤트 항목이 이미 존재합니다. 다시 입력해 주세요.",
                width: "500",
              };

              globalStore.openAlertMessage(objectAlert);
              return;
            }

            resolve(true);
          }
          reject(false);
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

// 저장 버튼 클릭 시
const handleUpdateButton = async () => {
  const { valid } = await formInput.value.validate();
  debugger;

  if (valid) {
    try {
      let baseUrl = "";
      let requestBody: any[] = [];

      // 이벤트ID 조회
      const evetIDResult: any = await onEvetIDSearch();
      debugger;

      if (evetIDResult.length === 0 && evetId.value === "") {
        const objectAlert: any = {
          text: "이벤트ID가 존재하지 않습니다.",
          width: "500",
        };

        await globalStore.openAlertMessage(objectAlert);
        return;
      }

      debugger;
      // 이벤트항목 조회
      await onEvetItemSearch();
      debugger;

      // 오더
      if (workCd === "ordr") {
        baseUrl =
          "http://dev.service-billing.com/ordr/ordrevetitem/v1/ordrevetitem";

        // 오더이벤트항목 수정
        if (InputPrmt.data.buttonType == "update") {
          requestBody = [
            {
              ordrEvetItemId: evetItemId.value,
              sndSysCd: sndSysCd.value,
              rcpSysCd: rcpSysCd.value,
              ordrEvetId: evetId.value, // TODO : 이벤트조회
              validStartDtm: validStartDtm.value,
              validEndDtm: validEndDtm.value,
            },
          ];

          // 오더이벤트항목 등록
        } else if (InputPrmt.data.buttonType == "insert") {
          requestBody = [
            {
              sndSysCd: sndSysCd.value,
              rcpSysCd: rcpSysCd.value,
              ordrEvetId: evetId.value, // TODO : 이벤트조회
              validStartDtm: validStartDtm.value,
              validEndDtm: validEndDtm.value,
            },
          ];
        }

        // 고객
      } else if (workCd === "cust") {
        baseUrl =
          "http://dev.service-billing.com/cust/custevetitem/v1/custevetitem";

        // 고객이벤트항목 수정
        if (InputPrmt.data.buttonType == "update") {
          requestBody = [
            {
              custEvetItemId: evetItemId.value,
              sndSysCd: sndSysCd.value,
              rcpSysCd: rcpSysCd.value,
              custEvetId: evetId.value, // TODO : 이벤트조회
              validStartDtm: validStartDtm.value,
              validEndDtm: validEndDtm.value,
            },
          ];

          // 고객이벤트항목 등록
        } else if (InputPrmt.data.buttonType == "insert") {
          requestBody = [
            {
              sndSysCd: sndSysCd.value,
              rcpSysCd: rcpSysCd.value,
              custEvetId: evetId.value, // TODO : 이벤트조회
              validStartDtm: validStartDtm.value,
              validEndDtm: validEndDtm.value,
            },
          ];
        }
      } else {
        const objectAlert: any = {
          text: "URL을 다시 확인해 주세요.",
          width: "500",
        };

        await globalStore.openAlertMessage(objectAlert);
        return;
      }

      const response = await axios.post(` ${baseUrl} `, requestBody);
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
      console.log(error);
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
  emit("closeDialog", sndSysCd.value);
};
</script>

<template>
  <div class="mx-auto prose prose-indigo">
    <div class="flex flex-col mt-[65px]">
      <v-form ref="formInput">
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label
              for="sndSysCd"
              class="w-[190px] h-[50px] font-semibold text-l"
            >
              <span class="font-semibold text-[#FF0404]">*</span>송신시스템코드
            </label>
            <cf-input
              v-model="sndSysCd"
              :model="sndSysCd"
              :rules="sndSysCdRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="20"
              oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
              class="w-[186px]"
              @update:model="handleSndSysCdUpdate"
              @input="sndSysCdUpdate"
            ></cf-input>
            <v-btn
              class="searchImg-btn !w-[20px] !h-[40px]"
              @click="searchSndSysCd"
            >
              <img :src="SearchImg" alt="search" class="m-0 my-2.5 w-[20px]" />
            </v-btn>
            <cf-input
              v-model="sndSysCdNm"
              variant="outlined"
              class="w-[190px]"
              style="margin-left: 4px"
              :disabled="true"
            ></cf-input>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label
              for="rcpSysCd"
              class="w-[190px] h-[50px] font-semibold text-l"
            >
              <span class="font-semibold text-[#FF0404]">*</span>수신시스템코드
            </label>
            <cf-input
              v-model="rcpSysCd"
              :model="rcpSysCd"
              :rules="rcpSysCdRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="20"
              oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
              class="w-[186px]"
              @update:model="handleRcpSysCdUpdate"
              @input="rcpSysCdUpdate"
            ></cf-input>
            <v-btn
              class="searchImg-btn !w-[20px] !h-[40px]"
              @click="searchRcpSysCd"
            >
              <img :src="SearchImg" alt="search" class="m-0 my-2.5 w-[20px]" />
            </v-btn>
            <cf-input
              v-model="rcpSysCdNm"
              variant="outlined"
              class="w-[190px]"
              style="margin-left: 4px"
              :disabled="true"
              @update:model="handleRcpSysCdNmUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div class="flex flex-wrap ml-[56px] mr-[56px] align-center">
            <label for="evetCd" class="w-[190px] h-[50px] font-semibold text-l"
              ><span class="font-semibold text-[#FF0404]">*</span
              >이벤트코드</label
            >
            <cf-input
              v-model="evetCd"
              :model="evetCd"
              :rules="evetCdRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="10"
              oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
              class="w-[186px]"
              @update:model="handleEvetCdUpdate"
              @input="evetCdUpdate"
            ></cf-input>
            <v-btn
              class="searchImg-btn !w-[20px] !h-[40px]"
              @click="searchEvetCd"
            >
              <img :src="SearchImg" alt="search" class="m-0 my-2.5 w-[20px]" />
            </v-btn>
            <cf-input
              v-model="evetCdNm"
              variant="outlined"
              class="w-[190px]"
              style="margin-left: 4px"
              :disabled="true"
              @update:model="handleEvetCdNmUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="row-span-12 flex">
          <div class="flex ml-[56px] align-center">
            <label for="input2" class="w-[190px] h-[50px] font-semibold text-l"
              ><span class="font-semibold text-[#FF0404]">*</span
              >이벤트상세코드</label
            >
            <cf-input
              v-model="evetDetlCd"
              :model="evetDetlCd"
              :rules="evetDetlCdRules"
              special-action="toUpperCase"
              variant="outlined"
              maxlength="10"
              :disabled="true"
              oninput="javascript: this.value = this.value.replace(/[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/g, '' );"
              class="w-[220px]"
              @update:model="handleEvetDetlCdUpdate"
              @input="evetDetlCdUpdate"
            ></cf-input>
            <cf-input
              v-model="evetDetlCdNm"
              variant="outlined"
              class="w-[220px]"
              style="margin-left: 4px"
              :disabled="true"
              @update:model="handleEvetDetlCdNmUpdate"
            ></cf-input>
          </div>
        </div>
        <div class="flex justify-end mr-[56px] mt-[56px]">
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
</style>
