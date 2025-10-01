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
const formAddTodo = ref<any>(null);
const useYnOption = ref([
  { label: "Y", value: "Y" },
  { label: "N", value: "N" },
]);

// field
const cmcdGrpId = ref("");
const cmcdDetlId = ref("");
const cmcdDetlNm = ref("");
const cmcdSortRank = ref(null);
const useYn = ref(null);

// state check duplicate
const mesageDuplicate = ref("");
const isDuplicate = ref<boolean | null>(null);

const checkDuplicate = async () => {
  const response = await httpClient.post(`/api/comm/cmcd/v1/cmcdDetlId`, {
    cmcdGrpId: cmcdGrpId.value,
    cmcdDetlId: cmcdDetlId.value,
  });

  isDuplicate.value = response.data.data.length > 0;

  if (isDuplicate.value) {
    mesageDuplicate.value = translateMessage(
      "code.cmcdDetail.message_duplicated"
    );
  } else {
    mesageDuplicate.value = "";
  }
};

const handleSaveCmcdDetail = async () => {
  const { valid } = await formAddTodo.value.validate();

  if (valid) {
    await checkDuplicate();
    if (isDuplicate.value) {
      return;
    }

    const objectAlert: any = {
      title: translateMessage("common.msg_confirm"),
      text: translateMessage("code.cmcdDetail.msg_confirm_update"),
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
        cmcdGrpId: cmcdGrpId.value,
        cmcdDetlId: cmcdDetlId.value,
        cmcdDetlNm: cmcdDetlNm.value,
        cmcdSortRank: cmcdSortRank.value,
        useYn: useYn.value.value,

        rgstUsr: user.value.name,
        rgstDtm: CommonOrdrUtil.getCurrentTime(),
        updUsr: user.value.name,
        updDtm: CommonOrdrUtil.getCurrentTime(),
      };

      let response;
      response = await httpClient.post(`/api/comm/cmcddetl/v1`, requestBody);

      globalStore.setToastInfor(
        {
          title: translateMessage("common.msg_notification"),
          text: translateMessage("code.cmcdDetail.msg_success_update"),
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

const handleClose = () => {
  emit("closeDialog");
};

onMounted(async () => {
  cmcdGrpId.value = props.data.cmcdGrpId;
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
              <v-label>{{ $t("code.cmcdDetail.cmcd_grp_id") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="cmcdGrpId"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                width="75%"
                readonly
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("code.cmcdDetail.cmcd_detl_id") }}</v-label>
            </v-col>
            <!-- row 2 -->
            <v-col class="no-padding" cols="12" md="9">
              <div class="flex gap-4">
                <v-text-field
                  v-model="cmcdDetlId"
                  density="compact"
                  :type="'input'"
                  :variant="'outlined'"
                  :counter="50"
                  :rules="useInputValidation({ required: true, maxLength: 50 })"
                  :error-messages="mesageDuplicate"
                >
                  <template
                    v-if="isDuplicate !== null && isDuplicate === false"
                    #append-inner
                  >
                    <v-icon color="success"> mdi-check-circle </v-icon>
                  </template>
                </v-text-field>
                <cf-button
                  :label="$t('code.cmcdDetail.btn_check_duplicates')"
                  @click="checkDuplicate"
                />
              </div>
            </v-col>
          </v-row>

          <!-- row 3-->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("code.cmcdDetail.cmcd_detl_nm") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="cmcdDetlNm"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="50"
                :rules="useInputValidation({ required: true, maxLength: 50 })"
                width="75%"
              ></v-text-field>
            </v-col>
          </v-row>
          <!-- row 4-->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("code.cmcdDetail.cmcd_sort_rank") }}</v-label>
            </v-col>
            <v-col class="no-padding" cols="12" md="9">
              <v-text-field
                v-model="cmcdSortRank"
                density="compact"
                :type="'input'"
                :variant="'outlined'"
                :counter="18"
                :rules="useInputValidation({ required: true, maxLength: 18 })"
                width="75%"
              ></v-text-field>
            </v-col>
          </v-row>
          <!-- row 5 -->
          <v-row class="p-0">
            <v-col class="no-padding" cols="12" md="3">
              <span class="required">*</span
              ><v-label>{{ $t("code.cmcdDetail.use_yn") }}</v-label>
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
                width="30%"
                clearable
              ></v-combobox>
            </v-col>
          </v-row>
        </div>
        <div class="d-flex justify-end mt-4 gap-4">
          <cf-button
            :label="$t('code.cmcdDetail.btn_save')"
            @click="handleSaveCmcdDetail"
          />
          <v-btn @click="handleClose">
            {{ $t("code.cmcdDetail.btn_cancel") }}
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
