<script setup lang="ts">
import { useGlobal } from "@/store";
import DatetimePicker from "@/components/controls/CfDatetimePicker.vue";
import { CommonUtil } from "@/utils/common-util";
import axios from "axios";

// #region Define Store
const globalStore = useGlobal();

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const workType = props.data.workType;

// #region Define init value
const formAddTodo = ref<any>(null);
// const sysCd = ref("");
// const sysCdNm = ref("");
const validEndDtm = ref("");
const validStartDtm = ref("");
const ordrEvetCd = ref("");
const ordrEvetCdNm = ref("");
const ordrEvetDetlCd = ref("");
const ordrEvetDetlCdNm = ref("");
const callMthd = ref("GET");

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();
const ordrEvetCdRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("order.msg_ordrEvetCd_required");
  },
  (value: string) => {
    if (value && value.length <= 10) return true;
    return translateMessage("order.msg_error_ordrEvetCd_maxlength");
  },
  (value: string) => {
    if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
    return translateMessage("order.msg_error_language");
  },
]);

const ordrEvetCdNmRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("order.msg_ordrEvetCdNm_required");
  },
  (value: string) => {
    if (value && value.length <= 50) return true;
    return translateMessage("order.msg_error_ordrEvetCdNm_maxlength");
  },
]);

const ordrEvetDetlCdRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("order.msg_ordrEvetDetlCd_required");
  },
  (value: string) => {
    if (value && value.length <= 10) return true;
    return translateMessage("order.msg_error_ordrEvetCd_maxlength");
  },
  (value: string) => {
    if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
    return translateMessage("order.msg_error_language");
  },
]);

const ordrEvetDetlCdNmRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("order.msg_ordrEvetDetlCdNm_required");
  },
  (value: string) => {
    if (value && value.length <= 50) return true;
    return translateMessage("order.msg_error_ordrEvetCdNm_maxlength");
  },
]);

const saveOrder = async () => {
  if (validateStartDate() || validateEndDate()) return;
  const { valid } = await formAddTodo.value.validate();
  if (valid) {
    try {
      const customerBaseUrl = "cust";
      const orderBaseUrl = "ordr";
      let response;
      let valdStrtDtmVal: any = null;
      if (validStartDtm.value) {
        valdStrtDtmVal = new Date(validStartDtm.value).toJSON().slice(0, 19);
      }
      let valdEndDtmVal: any = null;
      if (validEndDtm.value) {
        valdEndDtmVal = new Date(validEndDtm.value).toJSON().slice(0, 19);
      }

      if (workType === customerBaseUrl) {
        response = await axios.post(
          `http://dev.service-billing.com/cust/custevet/v1/custevet`,
          new Array({
            custEvetCd: ordrEvetCd.value,
            custEvetCdNm: ordrEvetCdNm.value,
            custEvetDetlCd: ordrEvetDetlCd.value,
            custEvetDetlCdNm: ordrEvetDetlCdNm.value,
            callMthd: callMthd.value,
            validStartDtm: valdStrtDtmVal,
            validEndDtm: valdEndDtmVal,
          })
        );
      }

      if (workType === orderBaseUrl) {
        response = await axios.post(
          `http://dev.service-billing.com/ordr/ordrevet/v1`,
          {
            ordrEvetCd: ordrEvetCd.value,
            ordrEvetCdNm: ordrEvetCdNm.value,
            ordrEvetDetlCd: ordrEvetDetlCd.value,
            ordrEvetDetlCdNm: ordrEvetDetlCdNm.value,
            callMthd: callMthd.value,
            validStartDtm: valdStrtDtmVal,
            validEndDtm: valdEndDtmVal,
          }
        );
      }

      if (response.data.errorCode) {
        //error cod 있을경우
        globalStore.setToastInfor(
          {
            title: translateMessage("system.msg_error_create"),
            text: response.data.errorMsg,
            border: "start",
            borderColor: "white",
            type: "error",
            icon: "$error",
            class: "bottom-center",
          },
          5000
        );
      } else {
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: translateMessage("system.msg_success_create"),
            border: "start",
            borderColor: "white",
            type: "success",
            icon: "$success",
            class: "bottom-center",
          },
          5000
        );
      }
      emit("closeDialog", response.data);
    } catch (err: any) {
      console.log(err);
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: err.toString(),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
          class: "bottom-center",
        },
        5000
      );
      emit("closeDialog", err.toString());
    }
  }
};

const validateStartDate = () => {
  if (validStartDtm.value && new Date(validStartDtm.value) < new Date()) {
    globalStore.setToastInfor(
      {
        text: "유효시작일시는 현재보다 과거일 수 없습니다",
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
    new Date(validEndDtm.value) < new Date(validStartDtm.value)
  ) {
    globalStore.setToastInfor(
      {
        text: "유효종료일시는 유효시작일시보다 과거일 수 없습니다",
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

const emit = defineEmits(["closeDialog"]);
const closeModal = () => {
  emit("closeDialog");
};

const ordrEvetCdChangeHandle = (val: string) => {
  ordrEvetCd.value = val.toUpperCase();
};

const ordrEvetCdNmChangeHandle = (val: string) => {
  ordrEvetCdNm.value = val;
};

const ordrEvetDetlCdChangeHandle = (val: string) => {
  ordrEvetDetlCd.value = val.toUpperCase();
};
const ordrEvetDetlCdNmChangeHandle = (val: string) => {
  ordrEvetDetlCdNm.value = val;
};
const callMthdChangeHandle = (val: string) => {
  callMthd.value = val;
};
const endDateHandle = (val: string) => {
  validEndDtm.value = val;
  validateEndDate();
};
const startDateHandle = (val: string) => {
  validStartDtm.value = val;
  validateStartDate();
};
</script>
<template>
  <div class="mx-auto prose prose-indigo">
    <div class="flex flex-col mt-[30px]">
      <v-form ref="formAddTodo" class="w-100">
        <div>
          <div class="row-span-12 flex">
            <div class="flex ml-[26px] mr-[26px] align-center">
              <label
                for="sysCd"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >이벤트코드</label
              >
              <cf-input
                :model="ordrEvetCd"
                class="sysInput w-[419px] h-[41px]"
                variant="undefined"
                special-action="toUpperCase"
                :rules="ordrEvetCdRules"
                @update:model="ordrEvetCdChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] align-center">
              <label
                for="ordrEvetCdNm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >이벤트코드명</label
              >
              <cf-input
                :model="ordrEvetCdNm"
                class="sysInput w-[419px] h-[41px]"
                variant="undefined"
                :rules="ordrEvetCdNmRules"
                @update:model="ordrEvetCdNmChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] mr-[26px] align-center">
              <label
                for="ordrEvetDetlCd"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >이벤트상세코드</label
              >
              <cf-input
                :model="ordrEvetDetlCd"
                class="sysInput w-[419px] h-[41px]"
                variant="undefined"
                special-action="toUpperCase"
                :rules="ordrEvetDetlCdRules"
                @update:model="ordrEvetDetlCdChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] mr-[26px] align-center">
              <label
                for="ordrEvetDetlCdNm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >이벤트상세코드명</label
              >
              <cf-input
                :model="ordrEvetDetlCdNm"
                class="sysInput w-[419px] h-[41px]"
                variant="undefined"
                :rules="ordrEvetDetlCdNmRules"
                @update:model="ordrEvetDetlCdNmChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] mr-[26px] align-center">
              <label
                for="callMthd"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >호출방식</label
              >
              <cf-dropdown
                class="custom-file-input w-[419px] h-[41px]"
                variant="undefined"
                item-title="value"
                :items="['GET', 'POST', 'PUT']"
                :model="callMthd"
                @update:model="callMthdChangeHandle"
              ></cf-dropdown>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] mr-[26px] align-center">
              <label
                for="validStartDtm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                >유효시작일시</label
              >
              <div>
                <DatetimePicker
                  :model="validStartDtm"
                  class="w-[419px] h-[41px]"
                  variant="outlined"
                  @update:model="startDateHandle"
                />
              </div>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] align-center">
              <label
                for="validEndDtm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                >유효종료일시</label
              >
              <div class="w-[400px] h-200">
                <DatetimePicker
                  :model="validEndDtm"
                  class="w-[419px] h-[41px]"
                  variant="outlined"
                  @update:model="endDateHandle"
                />
              </div>
            </div>
          </div>
          <div class="flex justify-end mr-[26px] mt-[30px]">
            <cf-button label="저장" class="custom-btn" @click="saveOrder" />
            <cf-button label="닫기" class="custom-btn" @click="closeModal" />
          </div>
        </div>
      </v-form>
    </div>
  </div>
</template>

<style scoped>
.v-card-title {
  background-color: #e3e3e3;
}
.v-input__details {
  display: none;
}
.total {
  height: 24px;
  width: 124px;
}
.custom-btn {
  background-color: transparent;
  border-radius: 8px;
  border: 1px solid #828282;
  color: #000000;
  height: 46px !important;
  font-weight: 500;
  font-size: 20px;
  padding: 8px;
  width: 90px;
  margin-right: 10px;
}
.v-input__control {
  width: 419px;
}
.sysInput :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  border: 1px solid #d9d9d9;
  /* border-radius: 5px; */
  height: 41px !important;
  min-height: 41px;
  padding-bottom: 12px;
}
.sysInput :deep(.v-input__details) {
  min-height: 16px;
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
  height: 41px !important;
}

.custom-file-input
  :deep(.v-input__control .v-field .v-field__field .v-field__input) {
  display: flex;
  justify-content: left;
  padding: 10px 8px 16px 16px;
  height: 40px;
  min-height: 0px;
}
.custom-file-input {
  border: 1px solid #d9d9d9;
}
.custom-file-input :deep(.mdi-menu-down::before) {
  content: "\F0140" !important;
}
</style>
