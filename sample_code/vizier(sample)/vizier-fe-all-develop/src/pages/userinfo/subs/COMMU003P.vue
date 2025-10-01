<script setup lang="ts">
import { AxiosError } from "axios";
import useGlobalStore from "@/store/global.store";
import { useUser } from "@/store";
import { httpClient } from "@/utils/http-common";
import { CommonUtil } from "@/utils/common-util";
import { useInputValidation } from "@/composables/useInputValidation";
import { UserInfoRequest } from "../type";
import COMM0001P from "@/pages/orgInfo/subs/COMM0001P.vue";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { Options } from "@/types/common";

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
// field
const userInfo = reactive<UserInfoRequest>({
  userNm: props.data.userNm ?? "",
  userId: props.data.userId ?? "",
  pw: props.data.pw ?? "",
  userKdCd: props.data.userKdCd ?? "",
  orgCd: props.data.orgCd ?? "",
  orgNm: props.data.orgNm ?? "",
  whofStatCd: props.data.whofStatCd ?? "",
  rgstUsr: user.value.name,
  rgstDtm: props.data.rgstDtm ?? "",
  updUsr: user.value.name,
  updDtm: props.data.updDtm ?? "",
});

const confirmPw = ref("");

// options
const userKdCdDropdown = ref<Options | null | undefined>(null);
const whofStatCdDropdown = ref<Options | null | undefined>(null);

const userKdCdOptions = ref<Options[]>([]);
const whofStatCdOptions = ref<Options[]>([]);

// state check duplicate
const menageDuplicate = ref("");
const isDuplicate = ref<boolean | null>(null);

const checkDuplicate = async () => {
  const response = await httpClient.post(
    `/api/comm/user/userInfo/v1/check-duplicated`,
    {
      userId: userInfo.userId,
    }
  );
  isDuplicate.value = response.data.data;
  if (isDuplicate.value) {
    menageDuplicate.value = translateMessage(
      "user_info.add.message_duplicated"
    );
  } else {
    menageDuplicate.value = "";
  }
};

const handleSave = async () => {
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
      text: "저장하시겠습니까?",
      width: "500",
      class: "custom-btn",
    };

    const result = await globalStore.openAlertConfirm(objectAlert);
    if (!result) {
      return;
    }

    try {
      loading.value = true;
      let response;
      if (!userKdCdDropdown.value || !whofStatCdDropdown.value) return;
      userInfo.userKdCd = userKdCdDropdown.value.value;
      userInfo.whofStatCd = whofStatCdDropdown.value.value;

      userInfo.rgstUsr = user.value.name;
      userInfo.rgstDtm = CommonOrdrUtil.getCurrentTime();
      userInfo.updUsr = user.value.name;
      userInfo.updDtm = CommonOrdrUtil.getCurrentTime();

      if (props.data.isAddNew) {
        response = await httpClient.post(
          `/api/comm/user/userInfo/v1`,
          userInfo
        );
      } else {
        response = await httpClient.put(`/api/comm/user/userInfo/v1`, userInfo);
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

const rules = {
  min: (value: string) => value.length >= 6 || "Min 6 characters",
  match: (value: string) => value === userInfo.pw || "Passwords do not match",
};

onMounted(async () => {
  // call api loading option here
  userKdCdOptions.value = [
    { label: "사원", value: "S1" },
    { label: "선임", value: "S2" },
    { label: "책임", value: "S3" },
  ];

  whofStatCdOptions.value = [
    { label: "해지", value: "T" },
    { label: "유효", value: "C" },
  ];

  // setup dropdown when edit

  if (!props.data.isAddNew) {
    if (props.data.userKdCd) {
      userKdCdDropdown.value = userKdCdOptions.value.find(
        (item) => item.value === props.data.userKdCd
      );
    }

    if (props.data.whofStatCd) {
      whofStatCdDropdown.value = whofStatCdOptions.value.find(
        (item) => item.value === props.data.whofStatCd
      );
    }
  }
});

const closeDialog = () => {
  emit("closeDialog");
};

const handleDeleteOrgCd = () => {
  userInfo.orgNm = "";
  userInfo.orgCd = "";
};

const showModalSelectOrgCd = async () => {
  const objectModal: any = {
    component: COMM0001P,
    dataInput: {},
    width: "1200px",
  };
  const data = await globalStore.openModal(objectModal);

  if (data) {
    userInfo.orgCd = data.orgCd;
    userInfo.orgNm = data.orgNm;
  }
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
          <!-- row 1 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("user_info.add.user_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="userInfo.userNm"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="100"
                  :rules="
                    useInputValidation({
                      required: true,
                      maxLength: 100,
                      engKorRule: true,
                    })
                  "
                  :readonly="!props.data.isAddNew"
                >
                </v-text-field>
              </div>
            </v-col>
          </v-row>
          <!-- row 2 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("user_info.add.user_id") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="userInfo.userId"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="20"
                  :rules="
                    useInputValidation({
                      required: true,
                      maxLength: 20,
                      engNumRule: true,
                    })
                  "
                  :error-messages="menageDuplicate"
                  :readonly="!props.data.isAddNew"
                >
                  <template
                    v-if="isDuplicate !== null && isDuplicate === false"
                    #append-inner
                  >
                    <v-icon color="success"> mdi-check-circle </v-icon>
                  </template>
                </v-text-field>
                <cf-button
                  :label="$t('user_info.add.btn_check_duplicates')"
                  :disabled="!props.data.isAddNew"
                  @click="checkDuplicate"
                />
              </div>
            </v-col>
          </v-row>

          <!-- row 3 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("user_info.add.pw") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="userInfo.pw"
                density="compact"
                :type="'password'"
                :variant="'outlined'"
                :counter="50"
                :rules="[
                  ...useInputValidation({ required: true, maxLength: 50 }),
                  rules.min,
                ]"
              ></v-text-field>
            </v-col>
          </v-row>

          <!-- row 4 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("user_info.add.confirm_pw") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="confirmPw"
                density="compact"
                :type="'password'"
                :variant="'outlined'"
                :counter="50"
                :rules="[
                  ...useInputValidation({ required: true, maxLength: 50 }),
                  rules.min,
                  rules.match,
                ]"
              ></v-text-field>
            </v-col>
          </v-row>

          <!-- row 5  -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("user_info.add.user_kd_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="userKdCdDropdown"
                :items="userKdCdOptions"
                :rules="useInputValidation({ required: true })"
                item-title="label"
                item-value="value"
                density="compact"
                variant="outlined"
                width="60%"
              ></v-combobox>
            </v-col>
          </v-row>

          <!-- row 8-->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span>
              <v-label>{{ $t("user_info.add.org_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="userInfo.orgNm"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="100"
                :rules="useInputValidation({ required: true, maxLength: 100 })"
                readonly
              >
                <template #append-inner>
                  <v-icon color="success" @click="showModalSelectOrgCd">
                    mdi-magnify
                  </v-icon>
                  <v-icon color="red" @click="handleDeleteOrgCd">
                    mdi-trash-can-outline
                  </v-icon>
                </template>
              </v-text-field>
            </v-col>
          </v-row>

          <!-- row 10  -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("user_info.add.whof_stat_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="whofStatCdDropdown"
                :items="whofStatCdOptions"
                :rules="useInputValidation({ required: true })"
                item-title="label"
                item-value="value"
                density="compact"
                variant="outlined"
                width="60%"
              ></v-combobox>
            </v-col>
          </v-row>
        </div>
        <div class="d-flex justify-end mt-4 gap-4">
          <cf-button :label="$t('common.btn_save')" @click="handleSave" />
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
