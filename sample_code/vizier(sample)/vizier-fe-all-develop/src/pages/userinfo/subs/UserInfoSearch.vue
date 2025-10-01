<script setup lang="ts">
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";
import { useInputValidation } from "@/composables/useInputValidation";
import useGlobalStore from "@/store/global.store";
import { CommonUtil } from "@/utils/common-util";

const emit = defineEmits(["search"]);
const globalStore = useGlobalStore();
const { translateMessage } = CommonUtil.useTranslatedMessage();

const userId = ref("");
const userNm = ref("");
const orgInfo = ref("");

const handleClickSearch = async () => {
  if (!userId.value && !userNm.value && !orgInfo.value) {
    const objectAlert: any = {
      text: translateMessage("user_info.search.message_no_search_condition"),
      width: "500",
    };

    globalStore.openAlertMessage(objectAlert);
    return;
  }
  emit("search", {
    userId: userId.value,
    userNm: userNm.value,
    orgInfo: orgInfo.value,
  });
};

const validateUserNm = (value: string) =>
  /^[A-Za-z가-힣\s]*$/.test(value) ||
  "Only English and Korean characters are allowed.";

const validateOrgInfo = (value: string) =>
  /^[A-Za-z가-힣0-9\s]*$/.test(value) ||
  "Only English, Korean characters, and numbers are allowed.";
</script>
<template>
  <v-sheet border elevation="2" style="width: 100%" class="my-4 mr-4 px-4">
    <v-row no-gutters>
      <v-col>
        <div class="flex flex-nowrap">
          <label class="mt-6">
            {{ $t("user_info.search.lbl_user_id") }}
          </label>
          <div class="flex-grow mt-4 custom-height md:w-2/4 px-3">
            <v-text-field
              v-model="userId"
              variant="outlined"
              :single-line="true"
              density="compact"
              class="mt-1 mr-4"
              type="text"
              :rules="
                useInputValidation({
                  engNumRule: true,
                })
              "
            ></v-text-field>
          </div>
        </div>
      </v-col>
      <v-col>
        <div class="flex flex-nowrap">
          <label class="mt-6">
            {{ $t("user_info.search.lbl_user_nm") }}
          </label>
          <div class="flex-grow mt-4 custom-height md:w-2/4 px-3">
            <v-text-field
              v-model="userNm"
              variant="outlined"
              :single-line="true"
              density="compact"
              class="mt-1 mr-4"
              type="text"
              :rules="[validateUserNm]"
            ></v-text-field>
          </div>
        </div>
      </v-col>

      <v-col>
        <div class="flex flex-nowrap">
          <label class="mt-6">
            {{ $t("user_info.search.btn_org_info") }}
          </label>
          <div class="flex-grow mt-4 custom-height md:w-2/4 px-3">
            <v-text-field
              v-model="orgInfo"
              variant="outlined"
              :single-line="true"
              density="compact"
              class="mt-1 mr-4"
              type="text"
              :rules="[validateOrgInfo]"
            ></v-text-field>
          </div>
        </div>
      </v-col>
      <v-col md="1" class="d-flex justify-end align-center sm:pb-4">
        <cf-button
          :label="$t(`user_info.search.btn_search`)"
          @click="handleClickSearch"
        />
      </v-col>
    </v-row>
  </v-sheet>
</template>

<style scoped>
.container-0 :deep(.v-container) {
  padding: 0px !important;
}

.custom-height :deep(.v-field__input) {
  height: 36px;
  min-height: 0px;
  display: flex;
  justify-content: left;
  min-width: 0px;
  padding: 10px;
}
.v-checkbox .v-label {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.min-width-col {
  min-width: 440px;
}
.no-wrap-label {
  min-width: 70px;
  white-space: nowrap;
  overflow: hidden;
}
</style>
