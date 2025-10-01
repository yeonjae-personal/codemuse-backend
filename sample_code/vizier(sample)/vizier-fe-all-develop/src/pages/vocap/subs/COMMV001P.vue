<script setup lang="ts">
import { v4 as uuidv4 } from "uuid";
import { AxiosError } from "axios";
import { CommonOrdrUtil } from "@/utils/common-ordr";
import { CommonUtil } from "@/utils/common-util";
import useGlobalStore from "@/store/global.store";
import { useUser } from "@/store";
import { httpClient } from "@/utils/http-common";
import { useInputValidation } from "@/composables/useInputValidation";
import COMMV002P from "@/pages/vocap/subs/COMMV002P.vue";
import COMMD002P from "@/pages/vocap/subs/COMMD002P.vue";

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

// setup data
const formAddTodo = ref<any>(null);
const stndYnOption = ref(["Y", "N"]);

// field
const vocaNm = ref(""); //용어명
const vocaCstcInfo = ref(""); //용어구성정보
const vocaEngAbb = ref(""); //용어영문약자
const vocaEngNm = ref(""); //용어영문명
const stndYn = ref(null); //표준여부(공통코드)
const domnNm = ref(""); //도메인명
const domnDivsCd = ref(""); //도메인구분코드(공통코드)
const domnLen = ref(""); //데이터길이
const vocaDscr = ref(""); //설명
const vocaDivsCd = ref(""); //용어구분코드()
const domnId = ref(""); //도메인ID
const vocaId = ref(""); //용어ID

const mesageDuplicate = ref("");
const isDuplicate = ref(false);

const showTermAnalysisModal = async () => {
  const objectModal: any = {
    title: "용어 분석 팝업",
    component: COMMV002P,
    dataInput: { analWord: vocaCstcInfo.value },
    width: "600",
  };
  const resultData = await globalStore.openModal(objectModal);
  vocaCstcInfo.value = resultData.vocaCstcInfo;
  vocaEngAbb.value = resultData.vocaEngAbb;
  vocaEngNm.value = resultData.vocaEngNm;
};

const showDomainModal = async () => {
  const objectModal: any = {
    // title: "용어 분석 팝업",
    component: COMMD002P,
    // dataInput: { analWord: vocaCstcInfo.value },
    width: "768",
  };
  const resultData = await globalStore.openModal(objectModal);
  if (!resultData) {
    return;
  }
  domnId.value = resultData.domnId;
  domnNm.value = resultData.domnNm;
  domnDivsCd.value = resultData.domnDivsCd;
  domnLen.value = resultData.domnLen;
};

const checkDuplicate = async () => {
  const response = await httpClient.post(`/api/comm/voca/v1/engAbb`, {
    vocaEngAbb: vocaEngAbb.value,
    vocaId: vocaId.value,
  });
  isDuplicate.value = response.data.data.length > 0;

  if (isDuplicate.value) {
    mesageDuplicate.value = translateMessage(
      "term.COMMV001P.message_duplicated"
    );
  } else {
    mesageDuplicate.value = "";
  }
};

const saveTerm = async () => {
  const { valid } = await formAddTodo.value.validate();

  if (valid) {
    await checkDuplicate();
    if (isDuplicate.value) {
      return;
    }

    // show confirm dialog
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
        vocaId: vocaId.value ? vocaId.value : uuidv4().substring(0, 20),
        vocaNm: vocaNm.value,
        vocaCstcInfo: vocaCstcInfo.value,
        vocaEngAbb: vocaEngAbb.value,
        vocaEngNm: vocaEngNm.value,
        vocaDscr: vocaDscr.value,
        vocaDivsCd: vocaDivsCd.value,
        domnId: domnId.value,
        rgstUsr: user.value.name,
        rgstDtm: CommonOrdrUtil.getCurrentTime(),
        updUsr: user.value.name,
        stndYn: stndYn.value,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      let response;
      if (!vocaId.value) {
        response = await httpClient.post(`/api/comm/voca/v1`, requestBody);
      } else {
        response = await httpClient.put(`/api/comm/voca/v1`, requestBody);
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

onMounted(() => {
  vocaId.value = props.data.vocaId;
  vocaNm.value = props.data.vocaNm;
  vocaCstcInfo.value = props.data.vocaCstcInfo;
  vocaEngAbb.value = props.data.vocaEngAbb;
  vocaEngNm.value = props.data.vocaEngNm;
  stndYn.value = props.data.stndYn;
  domnNm.value = props.data.domnNm; // type
  vocaDivsCd.value = "VO"; //  dom name
  domnLen.value = props.data.domnLen; // length
  vocaDscr.value = props.data.vocaDscr;
  domnDivsCd.value = props.data.domnDivsCd;
});

//수정모드일 경우 중복조회 버튼 비활성화
const btnDisable = computed(() => {
  if (props.data.vocaId) {
    return true;
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
          <v-row>
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("term.COMMV001P.voca_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="vocaNm"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="150"
                :rules="useInputValidation({ required: true, maxLength: 150 })"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("term.COMMV001P.voca_cstc_info") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4 flex-dynamic">
                <v-text-field
                  v-model="vocaCstcInfo"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="100"
                  readonly
                  :rules="
                    useInputValidation({ required: true, maxLength: 100 })
                  "
                ></v-text-field>
                <v-btn color="#e6007e" @click="showTermAnalysisModal">
                  <div class="flex gap-2">
                    <v-icon :icon="'mdi-swap-horizontal'"></v-icon>
                  </div>
                </v-btn>
              </div>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("term.COMMV001P.voca_eng_abb") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="vocaEngAbb"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="50"
                  :rules="useInputValidation({ required: true, maxLength: 50 })"
                  :error-messages="mesageDuplicate"
                  readonly
                >
                  <template
                    v-if="isDuplicate !== null && isDuplicate === false"
                    #append-inner
                  >
                    <v-icon color="success"> mdi-check-circle </v-icon>
                  </template>
                </v-text-field>
                <cf-button
                  :label="$t('term.COMMV001P.btn_duplicate_inquiry')"
                  :disabled="btnDisable"
                  @click="checkDuplicate"
                />
              </div>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("term.COMMV001P.voca_eng_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="vocaEngNm"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="250"
                :rules="useInputValidation({ required: true, maxLength: 250 })"
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("term.COMMV001P.stnd_yn") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-combobox
                v-model="stndYn"
                width="30%"
                :items="stndYnOption"
                :rules="useInputValidation({ required: true })"
                density="compact"
                variant="outlined"
                clearable
              ></v-combobox>
            </v-col>
          </v-row>
          <!-- ----- -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <v-label>{{ $t("term.COMMV001P.domn_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="domnNm"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  readonly
                ></v-text-field>
                <cf-button
                  :label="$t('term.COMMV001P.btn_select_domn')"
                  @click="showDomainModal"
                />
              </div>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <v-label>{{ $t("term.COMMV001P.domn_divs_cd") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="domnDivsCd"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                readonly
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <v-label>{{ $t("term.COMMV001P.domn_len") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="domnLen"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="20"
                :rules="useInputValidation({ maxLength: 10 })"
                readonly
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("term.COMMV001P.voca_dscr") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-textarea
                v-model="vocaDscr"
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
          <cf-button :label="$t('common.btn_save')" @click="saveTerm" />
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
