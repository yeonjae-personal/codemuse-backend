<script setup lang="ts">
import { useGlobal } from "@/store";
import CfButton from "@/components/controls/CfButton.vue";
import { CommonUtil } from "@/utils/common-util";
import DatetimePicker from "@/components/controls/CfDatetimePicker.vue";
import axios from "axios";

const globalStore = useGlobal();

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const workType = props.data.workType;
const ordrEvetId = ref();
const ordrEvetCd = ref();
const ordrEvetCdNm = ref();
const ordrEvetDetlCd = ref();
const ordrEvetDetlCdNm = ref();
const callMthd = ref();

const validEndDtm = ref();
const validStartDtm = ref();
if (props.data.workType === "cust") {
  ordrEvetId.value = props.data.dataRow.custEvetId;
  ordrEvetCd.value = props.data.dataRow.custEvetCd;
  ordrEvetCdNm.value = props.data.dataRow.custEvetCdNm;
  ordrEvetDetlCd.value = props.data.dataRow.custEvetDetlCd;
  ordrEvetDetlCdNm.value = props.data.dataRow.custEvetDetlCdNm;
  callMthd.value = props.data.dataRow.callMthd;
}
if (props.data.workType === "ordr") {
  ordrEvetId.value = props.data.dataRow.ordrEvetId;
  ordrEvetCd.value = props.data.dataRow.ordrEvetCd;
  ordrEvetCdNm.value = props.data.dataRow.ordrEvetCdNm;
  ordrEvetDetlCd.value = props.data.dataRow.ordrEvetDetlCd;
  ordrEvetDetlCdNm.value = props.data.dataRow.ordrEvetDetlCdNm;
  callMthd.value = props.data.dataRow.callMthd;
}

if (props.data.dataRow.validEndDtm) {
  validEndDtm.value = props.data.dataRow.validEndDtm.replace("T", " ");
}
if (props.data.dataRow.validStartDtm) {
  validStartDtm.value = props.data.dataRow.validStartDtm.replace("T", " ");
}

const formAddTodo = ref<any>(null);

const { translateMessage } = CommonUtil.useTranslatedMessage();
const ordrEvetCdRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("order.msg_ordrEvetCdNm_required");
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

const isChangeStrtDt = () => {
  if (!props.data.dataRow.validStartDtm) {
    return !!validStartDtm.value;
  } else {
    if (!validStartDtm.value) {
      return true;
    }

    return (
      props.data.dataRow.validStartDtm.replace("T", " ") !== validStartDtm.value
    );
  }
};

const isChangeEndDtm = () => {
  if (!props.data.dataRow.validEndDtm) {
    return !!validEndDtm.value;
  } else {
    if (!validEndDtm.value) {
      return true;
    }

    return (
      props.data.dataRow.validEndDtm.replace("T", " ") !== validEndDtm.value
    );
  }
};

const isUpdatedAble = () => {
  if (props.data.workType === "cust") {
    return (
      props.data.dataRow.custEvetCd !== ordrEvetCd.value ||
      props.data.dataRow.custEvetCdNm !== ordrEvetCdNm.value ||
      props.data.dataRow.custEvetDetlCd !== ordrEvetDetlCd.value ||
      props.data.dataRow.custEvetDetlCdNm !== ordrEvetDetlCdNm.value ||
      props.data.dataRow.callMthd !== callMthd.value ||
      isChangeStrtDt() ||
      isChangeEndDtm()
    );
  }

  if (props.data.workType === "ordr") {
    return (
      props.data.dataRow.ordrEvetCd !== ordrEvetCd.value ||
      props.data.dataRow.ordrEvetCdNm !== ordrEvetCdNm.value ||
      props.data.dataRow.ordrEvetDetlCd !== ordrEvetDetlCd.value ||
      props.data.dataRow.ordrEvetDetlCdNm !== ordrEvetDetlCdNm.value ||
      props.data.dataRow.callMthd !== callMthd.value ||
      isChangeStrtDt() ||
      isChangeEndDtm()
    );
  }

  return false;
};

const updateOrder = async () => {
  if (validateEndDate()) return;
  const { valid } = await formAddTodo.value.validate();
  if (valid) {
    if (isUpdatedAble()) {
      try {
        let valdStrtDtmVal: string = "";
        if (validStartDtm.value) {
          valdStrtDtmVal = new Date(validStartDtm.value).toJSON().slice(0, 19);
        }
        let valdEndDtmVal = "";
        if (validEndDtm.value) {
          valdEndDtmVal = new Date(validEndDtm.value).toJSON().slice(0, 19);
        }
        const requestBodyOrdr = {
          ordrEvetId: ordrEvetId.value,
          ordrEvetCd: ordrEvetCd.value,
          ordrEvetCdNm: ordrEvetCdNm.value,
          ordrEvetDetlCd: ordrEvetDetlCd.value,
          ordrEvetDetlCdNm: ordrEvetDetlCdNm.value,
          callMthd: callMthd.value,
          validStartDtm: valdStrtDtmVal,
          validEndDtm: valdEndDtmVal,
        };

        const requestBodyCust = [
          {
            custEvetId: ordrEvetId.value,
            custEvetCd: ordrEvetCd.value,
            custEvetCdNm: ordrEvetCdNm.value,
            custEvetDetlCd: ordrEvetDetlCd.value,
            custEvetDetlCdNm: ordrEvetDetlCdNm.value,
            callMthd: callMthd.value,
            validStartDtm: valdStrtDtmVal,
            validEndDtm: valdEndDtmVal,
          },
        ];
        const customerBaseUrl = "cust";
        const orderBaseUrl = "ordr";
        let baseUrl = "";
        let requestBody;
        let response;
        if (workType === customerBaseUrl) {
          baseUrl = `http://dev.service-billing.com/cust/custevet/v1/custevet`;
          requestBody = requestBodyCust;
          response = await axios.post(baseUrl, requestBody);
        }
        if (workType === orderBaseUrl) {
          baseUrl = `http://dev.service-billing.com/ordr/ordrevet/v1`;
          requestBody = requestBodyOrdr;
          response = await axios.put(baseUrl, requestBody);
        }
        if (response.errorCode) {
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
              text: translateMessage("system.msg_success_update"),
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
        globalStore.setToastInfor(
          {
            title: translateMessage("common.msg_notification"),
            text: err.toString,
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
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("system.msg_inform_update"),
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
          class: "bottom-center",
        },
        5000
      );
    }
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
</script>
<template>
  <div class="mx-auto prose prose-indigo">
    <div class="flex flex-col mt-[30px]">
      <v-form ref="formAddTodo" class="w-100">
        <div>
          <div class="row-span-12 flex pt-5">
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
                :variant="undefined"
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
                :variant="undefined"
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
                :variant="undefined"
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
                :variant="undefined"
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
                :variant="undefined"
                item-title="value"
                :items="['GET', 'POST', 'PUT']"
                :model="callMthd"
                @update:model="callMthdChangeHandle"
              ></cf-dropdown>
              <!-- <cf-dropdown
                class="custom-file-input w-[419px] h-[41px]"
                :variant="undefined"
                item-title="value"
                :items="['GET', 'POST', 'PUT']"
                :model="callMthd"
                @update:model="callMthdChangeHandle"
              ></cf-dropdown> -->
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
            <cf-button label="저장" class="custom-btn" @click="updateOrder" />
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
  min-height: 0px;
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
