<script setup lang="ts">
import { useGlobal } from "@/store";
import CfButton from "@/components/controls/CfButton.vue";
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
const ordrClasNm = ref();
const ordrClasPath = ref();
const ordrClasId = ref("");
if (props.data.workType === "cust") {
  ordrClasId.value = props.data.dataRow.custClasId;
  ordrClasNm.value = props.data.dataRow.custClasNm;
  ordrClasPath.value = props.data.dataRow.custClasPath;
}
if (props.data.workType === "ordr") {
  ordrClasId.value = props.data.dataRow.ordrClasId;
  ordrClasNm.value = props.data.dataRow.ordrClasNm;
  ordrClasPath.value = props.data.dataRow.ordrClasPath;
}

// #region Define init value
const formAddTodo = ref<any>(null);

// #region Define events
const { translateMessage } = CommonUtil.useTranslatedMessage();

const ordrClasNmRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("search_order.msg_ordrClasNm_required");
  },
  (value: string) => {
    if (value && value.length <= 20) return true;
    return translateMessage("search_order.msg_error_ordrClasNm_maxlength");
  },
  (value: string) => {
    if (value.length === 0 || /^[A-Za-z 0-9]+$/.test(value)) return true;
    return translateMessage("search_order.msg_error_language");
  },
]);

const ordrClasPathRules = ref([
  (value: string) => {
    if (value) return true;
    return translateMessage("search_order.msg_ordrClasPath_required");
  },
  (value: string) => {
    if (value.length <= 100) return true;
    return translateMessage("search_order.msg_error_ordrClasPath_maxlength");
  },
  (value: string) => {
    if (value.length === 0 || /^[A-Za-z. 0-9]+$/.test(value)) return true;
    return translateMessage("search_order.msg_error_clasPath_language");
  },
]);

const isUpdatedAble = () => {
  if (props.data.workType === "cust") {
    return (
      props.data.dataRow.custClasNm !== ordrClasNm.value ||
      props.data.dataRow.custClasPath !== ordrClasPath.value
    );
  }

  if (props.data.workType === "ordr") {
    return (
      props.data.dataRow.ordrClasNm !== ordrClasNm.value ||
      props.data.dataRow.ordrClasPath !== ordrClasPath.value
    );
  }

  return false;
};

const updateSearchOrder = async () => {
  const { valid } = await formAddTodo.value.validate();
  if (valid) {
    if (isUpdatedAble()) {
      try {
        const requestBodyOrdr = {
          ordrClasId: ordrClasId.value,
          ordrClasNm: ordrClasNm.value,
          ordrClasPath: ordrClasPath.value,
        };
        const requestBodyCust = {
          custClasId: ordrClasId.value,
          custClasNm: ordrClasNm.value,
          custClasPath: ordrClasPath.value,
        };

        const customerBaseUrl = "cust";
        const orderBaseUrl = "ordr";
        let baseUrl = "";
        let requestBody;
        let response;
        if (workType === customerBaseUrl) {
          baseUrl = `http://dev.service-billing.com/cust/custclas/v1`;
          requestBody = requestBodyCust;
          response = await axios.put(baseUrl, requestBody);
        }
        if (workType === orderBaseUrl) {
          baseUrl = `http://dev.service-billing.com/ordr/ordrclas/v1`;
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
            text: err.toString(),
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

const ordrClasNmChangeHandle = (val: string) => {
  ordrClasNm.value = val;
};

const ordrClasPathChangeHandle = (val: string) => {
  ordrClasPath.value = val.toLowerCase();
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
                >클래스명</label
              >
              <cf-input
                :model="ordrClasNm"
                class="sysInput w-[419px] h-[41px]"
                :variant="undefined"
                :rules="ordrClasNmRules"
                @update:model="ordrClasNmChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="row-span-12 flex pt-5">
            <div class="flex ml-[26px] align-center">
              <label
                for="ordrClasNm"
                class="w-[191px] h-[22px] font-semibold text-xl"
                ><span class="font-semibold text-[#FF0404]">*</span
                >클래스경로명</label
              >
              <cf-input
                :model="ordrClasPath"
                class="sysInput w-[419px] h-[41px]"
                :variant="undefined"
                :rules="ordrClasPathRules"
                special-action="toLowerCase"
                @update:model="ordrClasPathChangeHandle"
              ></cf-input>
            </div>
          </div>
          <div class="flex justify-end mr-[26px] mt-[30px]">
            <cf-button
              label="저장"
              class="custom-btn"
              @click="updateSearchOrder"
            />
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
</style>
