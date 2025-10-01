<script setup lang="ts">
import { AxiosError } from "axios";
import useGlobalStore from "@/store/global.store";
import { useUser } from "@/store";
import { httpClient } from "@/utils/http-common";
import { CommonUtil } from "@/utils/common-util";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { useInputValidation } from "@/composables/useInputValidation";
import { OrgInfoRequest } from "../type";
import COMMU001P from "@/pages/userinfo/subs/COMMU001P.vue";
import COMM0001P from "@/pages/orgInfo/subs/COMM0001P.vue";
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
const parentLevel = ref<number | null>(null);

// field
const orgInfo = reactive<OrgInfoRequest>({
  orgCd: props.data.orgCd ?? "",
  orgNm: props.data.orgNm ?? "",
  orgKdCd: props.data.orgKdCd ?? "",
  orgLvCd: props.data.orgLvCd ?? "",
  tlmdId: props.data.tlmdId ?? "",
  tlmdNm: props.data.tlmdNm ?? "",
  hposOrgCd: props.data.hposOrgCd ?? "",
  hposOrgNm: props.data.hposOrgNm ?? "",
  orgStatCd: props.data.orgStatCd ?? "",
  orgCdPathNm: props.data.orgCdPathNm ?? "",
  orgNmPathNm: props.data.orgNmPathNm ?? "",
  rgstUsr: props.data.rgstUsr ?? "",
  rgstDtm: props.data.rgstDtm ?? "",
  updUsr: props.data.updUsr ?? "",
  updDtm: props.data.updDtm ?? "",
});

// options
const orgKdCdDropdown = ref<Options | undefined | null>(null);
const orgLvCdDropdown = ref<Options | undefined | null>(null);
const orgStatCdDropdown = ref<Options | undefined | null>(null);

const orgKdCdOptions = ref<Options[]>([]);
const orgLvCdOptions = ref<Options[]>([]);
const whofStatCdOptions = ref<Options[]>([]);

// state check duplicates
const menageDuplicate = ref("");
const isDuplicate = ref<boolean | null>(null);

const checkDuplicate = async () => {
  if (!props.data.isAddNew) {
    return;
  }
  const response = await httpClient.post(
    `/api/comm/org/orgInfo/v1/check-duplicated`,
    {
      orgCd: orgInfo.orgCd,
    }
  );
  isDuplicate.value = response.data.data;
  if (isDuplicate.value) {
    menageDuplicate.value = translateMessage("org_info.add.message_duplicated");
  } else {
    menageDuplicate.value = "";
  }
};

const checkLevel = () => {
  if (parentLevel.value === null && orgLvCdDropdown.value.value == "1") {
    return true;
  } else {
    if (parentLevel.value !== null) {
      if (Number(orgLvCdDropdown.value.value) === parentLevel.value + 1) {
        return true;
      }
    }
  }
  return false;
};

const handleSave = async () => {
  const { valid } = await form.value.validate();

  if (valid) {
    await checkDuplicate();
    if (isDuplicate.value) {
      return;
    }

    if (!checkLevel()) {
      const objectAlert: any = {
        text: "조직레벨 값이 정확하지 않습니다.",
        width: "500",
      };

      globalStore.openAlertMessage(objectAlert);

      return;
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
      orgInfo.orgKdCd = orgKdCdDropdown.value.value;
      orgInfo.orgLvCd = orgLvCdDropdown.value.value;
      orgInfo.orgStatCd = orgStatCdDropdown.value.value;

      orgInfo.rgstUsr = user.value.name;
      orgInfo.rgstDtm = CommonOrdrUtil.getCurrentTime();
      orgInfo.updUsr = user.value.name;
      orgInfo.updDtm = CommonOrdrUtil.getCurrentTime();

      if (props.data.isAddNew) {
        response = await httpClient.post(`/api/comm/org/orgInfo/v1`, orgInfo);
      } else {
        response = await httpClient.put(`/api/comm/org/orgInfo/v1`, orgInfo);
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

onMounted(async () => {
  // call api  setup data dropdown -  mock option
  orgKdCdOptions.value = [
    { label: "인사조직", value: "O1" },
    { label: "가상조직", value: "O2" },
    { label: "개발조직", value: "O3" },
    { label: "관리조직", value: "O4" },
  ];

  orgLvCdOptions.value = [
    { label: "Level 1", value: "1" },
    { label: "Level 2", value: "2" },
    { label: "Level 3", value: "3" },
    { label: "Level 4", value: "4" },
    { label: "Level 5", value: "5" },
    { label: "Level 6", value: "6" },
  ];

  whofStatCdOptions.value = [
    { label: "해지", value: "T" },
    { label: "유효", value: "C" },
  ];

  if (!props.data.isAddNew) {
    orgKdCdDropdown.value = orgKdCdOptions.value.find(
      (item) => item.value === props.data.orgKdCd
    );

    orgLvCdDropdown.value = orgLvCdOptions.value.find(
      (item) => item.value === props.data.orgLvCd
    );

    orgStatCdDropdown.value = whofStatCdOptions.value.find(
      (item) => item.value === props.data.orgStatCd
    );

    parentLevel.value = Number(props.data.orgLvCd);
  }
});

const closeDialog = () => {
  emit("closeDialog");
};

const handleDeleteUser = () => {
  orgInfo.tlmdId = "";
  orgInfo.tlmdNm = "";
};

const handleDeleteOrg = () => {
  orgInfo.hposOrgCd = "";
  orgInfo.hposOrgNm = "";
};
const showModalSelectUser = async () => {
  const objectModal: any = {
    component: COMMU001P,
    dataInput: {},
    width: "1200px",
  };
  const data = await globalStore.openModal(objectModal);

  if (data) {
    orgInfo.tlmdId = data.userId;
    orgInfo.tlmdNm = data.userNm;
  }
};

const showModalSelectOrg = async () => {
  const objectModal: any = {
    component: COMM0001P,
    dataInput: {},
    width: "1200px",
  };
  const data = await globalStore.openModal(objectModal);

  if (data) {
    orgInfo.hposOrgCd = data.orgCd;
    orgInfo.hposOrgNm = data.orgNm;
    parentLevel.value = Number(data.orgLvCd);
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
              ><v-label>{{ $t("org_info.add.org_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="orgInfo.orgNm"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="100"
                >
                </v-text-field>
              </div>
            </v-col>
          </v-row>
          <!-- row 2 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("org_info.add.org_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="orgInfo.orgCd"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="20"
                  :rules="
                    useInputValidation({
                      required: true,
                      maxLength: 20,
                      onlyNumbers: true,
                      noSpecialChars: true,
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
                  :label="$t('org_info.add.btn_check_duplicates')"
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
              ><v-label>{{ $t("org_info.add.org_kd_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="orgKdCdDropdown"
                :items="orgKdCdOptions"
                :rules="useInputValidation({ required: true })"
                item-title="label"
                item-value="value"
                density="compact"
                variant="outlined"
                width="60%"
              ></v-combobox>
            </v-col>
          </v-row>

          <!-- row 4 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("org_info.add.org_lv_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="orgLvCdDropdown"
                :items="orgLvCdOptions"
                :rules="useInputValidation({ required: true })"
                item-title="label"
                item-value="value"
                density="compact"
                variant="outlined"
                width="60%"
              ></v-combobox>
            </v-col>
          </v-row>

          <!--  row 6 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <v-label>{{ $t("org_info.add.tlmd_id") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="orgInfo.tlmdNm"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="20"
                :rules="useInputValidation({ maxLength: 20 })"
                readonly
              >
                <template #append-inner>
                  <v-icon color="success" @click="showModalSelectUser">
                    mdi-magnify
                  </v-icon>
                  <v-icon color="red" @click="handleDeleteUser">
                    mdi-trash-can-outline
                  </v-icon>
                </template>
              </v-text-field>
            </v-col>
          </v-row>

          <!-- row 7-->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <v-label>{{ $t("org_info.add.hpos_org_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="orgInfo.hposOrgNm"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="10"
                :rules="useInputValidation({ maxLength: 10 })"
                readonly
              >
                <template #append-inner>
                  <v-icon color="success" @click="showModalSelectOrg">
                    mdi-magnify
                  </v-icon>
                  <v-icon color="red" @click="handleDeleteOrg">
                    mdi-trash-can-outline
                  </v-icon>
                </template>
              </v-text-field>
            </v-col>
          </v-row>
          <!-- row 8  -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("org_info.add.org_stat_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="orgStatCdDropdown"
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
