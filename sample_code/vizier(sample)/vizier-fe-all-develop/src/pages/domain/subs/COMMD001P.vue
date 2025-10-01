<script setup lang="ts">
import { v4 as uuidv4 } from "uuid";
import { AxiosError } from "axios";
import useGlobalStore from "@/store/global.store";
import { useUser } from "@/store";
import { httpClient } from "@/utils/http-common";
import { CommonUtil } from "@/utils/common-util";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { useInputValidation } from "@/composables/useInputValidation";
import COMMV002P from "@/pages/vocap/subs/COMMV002P.vue";

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
const formAddTodo = ref<any>(null);
const useYnOption = ref([
  { label: "Y", value: "Y" },
  { label: "N", value: "N" },
]);

const domnGrpCdOption = ref([]);
const domnDivsCdOption = ref([]);

// field
const domnId = ref("");
const domnNm = ref("");
const domnEngNm = ref("");
const domnGrpCd = ref(null);
const domnDivsCd = ref(null);
const useYn = ref(null);
const domnLen = ref("");
const domnDscr = ref("");

// state check duplicate
const mesageDuplicate = ref("");
const isDuplicate = ref<boolean | null>(null);

const showTermAnalysisModal = async () => {
  const objectModal: any = {
    title: "용어 분석 팝업",
    component: COMMV002P,
    dataInput: { analWord: domnNm.value },
    width: "600",
  };
  const resultData = await globalStore.openModal(objectModal);
  domnEngNm.value = resultData.vocaEngAbb;
};

const checkDuplicate = async () => {
  const response = await httpClient.post(`/api/comm/domn/v1/domnName`, {
    domnNm: domnNm.value,
    domnId: domnId.value,
  });

  isDuplicate.value = response.data.data.length > 0;

  if (isDuplicate.value) {
    mesageDuplicate.value = translateMessage("domain.add.message_duplicated");
  } else {
    mesageDuplicate.value = "";
  }
};

const handleSaveDomain = async () => {
  const { valid } = await formAddTodo.value.validate();

  if (valid) {
    await checkDuplicate();
    if (isDuplicate.value) {
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
      const requestBody = {
        domnId: domnId.value ? domnId.value : uuidv4().substring(0, 20),
        domnNm: domnNm.value,
        domnEngNm: domnEngNm.value,
        domnGrpCd: domnGrpCd.value.value,
        domnDivsCd: domnDivsCd.value.value,
        useYn: useYn.value.value,
        domnLen: domnLen.value,
        domnDscr: domnDscr.value,
        rgstUsr: user.value.name,
        rgstDtm: CommonOrdrUtil.getCurrentTime(),
        updUsr: user.value.name,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      let response;
      if (!domnId.value) {
        response = await httpClient.post(`/api/comm/domn/v1`, requestBody);
      } else {
        response = await httpClient.put(`/api/comm/domn/v1`, requestBody);
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
    const response = await httpClient.get(`/api/comm/domn/v1/detl`);
    const data = response.data.data;
    domnDivsCdOption.value = data.domn_divs_cd;
    domnGrpCdOption.value = data.domn_grp_cd;
  } catch (error) {
    console.error("Error fetching data:", error);
  } finally {
    loading.value = false;
  }
};

onMounted(async () => {
  await fetchData();
  domnId.value = props.data.domnId;
  domnNm.value = props.data.domnNm;
  domnEngNm.value = props.data.domnEngNm;
  useYn.value = useYnOption.value.find(
    (item) => item.value === props.data.useYn
  );
  domnLen.value = props.data.domnLen;
  domnDscr.value = props.data.domnDscr;
  if (props.data.domnGrpCd) {
    domnGrpCd.value = {
      label: props.data.domnGrpNm,
      value: props.data.domnGrpCd,
    };
  }
  if (props.data.domnDivsCd) {
    domnDivsCd.value = {
      label: props.data.domnDivsNm,
      value: props.data.domnDivsCd,
    };
  }
});
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
      <v-form ref="formAddTodo" class="w-100">
        <div class="flex flex-col">
          <!-- row 1 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("domain.add.domn_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="domnNm"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="150"
                  :rules="
                    useInputValidation({ required: true, maxLength: 150 })
                  "
                  :error-messages="mesageDuplicate"
                >
                  <!-- <template
                    v-if="isDuplicate !== null && isDuplicate === false"
                    v-slot:append-inner
                  > -->
                  <template
                    v-if="isDuplicate !== null && isDuplicate === false"
                    #append-inner
                  >
                    <v-icon color="success"> mdi-check-circle </v-icon>
                  </template>
                </v-text-field>
                <cf-button
                  :label="$t('domain.add.btn_check_duplicates')"
                  @click="checkDuplicate"
                />
              </div>
            </v-col>
          </v-row>

          <!-- row 2 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("domain.add.domn_eng_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4 flex-dynamic">
                <v-text-field
                  v-model="domnEngNm"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="250"
                  :rules="
                    useInputValidation({ required: true, maxLength: 250 })
                  "
                  readonly
                ></v-text-field>
                <v-btn color="#e6007e" @click="showTermAnalysisModal">
                  <div class="flex gap-2">
                    <v-icon :icon="'mdi-swap-horizontal'"></v-icon>
                  </div>
                </v-btn>
              </div>
            </v-col>
          </v-row>

          <!-- row 3 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("domain.add.domn_grp_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="domnGrpCd"
                :items="domnGrpCdOption"
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
              ><v-label>{{ $t("domain.add.domn_divs_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="domnDivsCd"
                :items="domnDivsCdOption"
                item-title="label"
                item-value="value"
                :rules="useInputValidation({ required: true })"
                density="compact"
                variant="outlined"
                clearable
                width="60%"
              ></v-combobox>
            </v-col>
          </v-row>
          <!-- row 5 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("domain.add.use_yn") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="useYn"
                :items="useYnOption"
                :rules="useInputValidation({ required: true })"
                item-title="label"
                item-value="value"
                density="compact"
                variant="outlined"
                width="50%"
                clearable
              ></v-combobox>
            </v-col>
          </v-row>
          <!--  row 6 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <v-label>{{ $t("domain.add.domn_len") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="domnLen"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="10"
                :rules="useInputValidation({ maxLength: 10 })"
              ></v-text-field>
            </v-col>
          </v-row>
          <!-- row 7-->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("domain.add.domn_dscr") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-textarea
                v-model="domnDscr"
                :counter="1000"
                :rules="useInputValidation({ required: true, maxLength: 1000 })"
                density="compact"
                rows="3"
                variant="outlined"
                auto-grow
                shaped
              ></v-textarea>
            </v-col>
          </v-row>
        </div>
        <div class="flex flex-row-reverse mt-4">
          <cf-button :label="$t('common.btn_save')" @click="handleSaveDomain" />
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
