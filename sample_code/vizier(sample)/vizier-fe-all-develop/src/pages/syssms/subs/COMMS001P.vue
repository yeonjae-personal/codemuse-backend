<script setup lang="ts">
import { AxiosError } from "axios";
import useGlobalStore from "@/store/global.store";
import { useUser } from "@/store";
import { httpClient } from "@/utils/http-common";
import { CommonUtil } from "@/utils/common-util";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { useInputValidation } from "@/composables/useInputValidation";

const props = defineProps({
  data: {
    type: Object,
    default: null,
  },
});
const emit = defineEmits(["closeDialog"]);
const { translateMessage } = CommonUtil.useTranslatedMessage();

const globalStore = useGlobalStore();
const userStore = useUser();
const loading = ref(false);
const user = computed(() => {
  return userStore.user;
});

// setup
const form = ref<any>(null);
const sysMsgLangCdOption = ref([
  { label: "한글", value: "KORN" },
  { label: "영어", value: "ENG" },
]);

// field
const sysMsgId = ref("");
const sysMsgLangCd = ref<any>("");
const sysMsgCntn = ref("");

// state check duplicate
const mesageDuplicate = ref("");
const isDuplicate = ref<boolean | null>(null);

const checkDuplicate = async () => {
  const response = await httpClient.post(
    `/api/comm/sysmsg/v1/check-duplicated`,
    {
      sysMsgId: sysMsgId.value,
      sysMsgLangCd: sysMsgLangCd.value.value,
    }
  );

  isDuplicate.value = response.data.data;

  if (isDuplicate.value) {
    mesageDuplicate.value = translateMessage("sys_msg.add.message_duplicated");
  } else {
    mesageDuplicate.value = "";
  }
};

const handleSaveSysMsg = async () => {
  const { valid } = await form.value.validate();

  if (valid) {
    if (props.data.isAddNew) {
      await checkDuplicate();
      if (isDuplicate.value) {
        return;
      }
    }

    const objectAlert: any = {
      title: translateMessage("common.msg_confirm"),
      text: translateMessage("sys_msg.add.msg_confirm_create"),
      width: "500",
      class: "custom-btn",
    };

    const result = await globalStore.openAlertConfirm(objectAlert);
    if (!result) {
      return;
    }

    try {
      loading.value = true;
      const requestBody = {
        sysMsgId: sysMsgId.value,
        sysMsgLangCd: sysMsgLangCd.value.value,
        sysMsgCntn: sysMsgCntn.value,

        rgstUsr: user.value.name,
        rgstDtm: CommonOrdrUtil.getCurrentTime(),
        updUsr: user.value.name,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      let response;
      if (props.data.isAddNew) {
        response = await httpClient.post(`/api/comm/sysmsg/v1`, requestBody);
      } else {
        response = await httpClient.put(`/api/comm/sysmsg/v1`, requestBody);
      }
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: "저장되었습니다",
          border: "start",
          borderColor: "white",
          type: "success",
          icon: "$success",
        },
        5000
      );
      emit("closeDialog", response.data.data);
    } catch (error: unknown) {
      let message = "";
      if (error instanceof AxiosError) {
        message = error.message;
      }
      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_inform_update"),
          text: message,
          border: "start",
          borderColor: "white",
          type: "error",
          icon: "$error",
        },
        5000
      );
    } finally {
      loading.value = false;
    }
  }
  if (!valid) {
    return;
  }
};

const fetchData = async () => {
  try {
    loading.value = true;
    // const response = await httpClient.get(`/api/comm/sysmsg/v1/init`);
    // const data = response.data;
    // sysMsgLangCdOption.value = data.sysMsgLangCdOption;

    // need confirm loading data for sysMsgLangCdOption
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
  }
};

const initMsgLandCdDropdown = (value: string) => {
  return [
    { label: "한글", value: "KORN" },
    { label: "영어", value: "ENG" },
  ].find((item) => item.value === value);
};

onMounted(async () => {
  await fetchData();
  sysMsgId.value = props.data.sysMsgId;
  sysMsgLangCd.value = initMsgLandCdDropdown(props.data.sysMsgLangCd);
  sysMsgCntn.value = props.data.sysMsgCntn;
});

const closeDialog = () => {
  emit("closeDialog");
};
</script>
<template>
  <v-dialog v-model="loading" max-width="320" persistent contained>
    <v-list class="py-2" color="primary" elevation="12" rounded="lg">
      <v-list-item title="Application is loading...">
        <template #prepend>
          <div class="pe-4">
            <v-icon color="pink" size="x-large"></v-icon>
          </div>
        </template>

        <template #append>
          <v-progress-circular
            color="pink"
            indeterminate="disable-shrink"
            size="30"
            width="2"
          ></v-progress-circular>
        </template>
      </v-list-item>
    </v-list>
  </v-dialog>
  <div class="mx-auto prose prose-indigo sm:rounded-md">
    <div class="flex flex-col w-100">
      <v-form ref="form" class="w-100">
        <div class="flex flex-col">
          <!-- row 1 시스템메시지ID 시스템메시지언어코드 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("sys_msg.add.sys_msg_id") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="d-flex justify-space-between gap-4">
                <v-text-field
                  v-model="sysMsgId"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="20"
                  :rules="useInputValidation({ required: true, maxLength: 20 })"
                  :error-messages="mesageDuplicate"
                  :disabled="!props.data.isAddNew"
                >
                  <template
                    v-if="isDuplicate !== null && isDuplicate === false"
                    #append-inner
                  >
                    <v-icon color="success"> mdi-check-circle </v-icon>
                  </template>
                </v-text-field>
                <div class="flex w-[120px]">
                  <v-combobox
                    v-model="sysMsgLangCd"
                    :items="sysMsgLangCdOption"
                    :rules="useInputValidation({ required: true })"
                    item-title="label"
                    item-value="value"
                    density="compact"
                    variant="outlined"
                    :width="50"
                    :disabled="!props.data.isAddNew"
                  ></v-combobox>
                </div>
                <cf-button
                  :label="$t('sys_msg.add.btn_check_duplicates')"
                  @click="checkDuplicate"
                />
              </div>
            </v-col>
          </v-row>

          <!-- row 2 시스템메시지내용 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("sys_msg.add.sys_msg_cntn") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-textarea
                v-model="sysMsgCntn"
                :counter="2000"
                :rules="useInputValidation({ required: true, maxLength: 2000 })"
                density="compact"
                rows="3"
                variant="outlined"
                auto-grow
                shaped
              ></v-textarea>
            </v-col>
          </v-row>
        </div>
        <div class="d-flex justify-end mt-4 gap-4">
          <cf-button :label="$t('common.btn_save')" @click="handleSaveSysMsg" />
          <v-btn @click="closeDialog">
            {{ $t("common.btn_close") }}
          </v-btn>
        </div>
      </v-form>
    </div>
  </div>
</template>

<style scoped>
.no-padding {
  padding: 0 12px !important;
}

.required {
  color: rgb(var(--v-theme-error));
}
</style>
