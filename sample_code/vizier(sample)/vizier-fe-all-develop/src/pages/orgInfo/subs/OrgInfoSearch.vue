<script setup lang="ts">

import { useInputValidation } from "@/composables/useInputValidation";
import { OrgSearchRequest } from "../type";
import { CommonUtil } from "@/utils/common-util";
import useGlobalStore from "@/store/global.store";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

const { translateMessage } = CommonUtil.useTranslatedMessage();
const globalStore = useGlobalStore();
const emit = defineEmits(["search"]);
const searchRequest = reactive<OrgSearchRequest>({
  orgInfo: '',
  orgKdCd: '',
  orgStatCd: ''
});

const orgStatCd = ref({ label: "", value: "" });

const orgStatCdOption = ref([
  { label: "유효", value: "C" },
  { label: "해지", value: "T" },
]);

const orgKdCd = ref({ label: "", value: "" });

const orgKdCdOption = ref([
  { label: "인사조직", value: "O1" },
  { label: "가상조직", value: "O2" },
  { label: "개발조직", value: "O3" },
  { label: "관리조직", value: "O4" },

]);

const handleClickSearch = () => {
  if (!orgStatCd.value.value && !orgKdCd.value.value && !searchRequest.orgInfo) {
    const objectAlert: any = {
      text: translateMessage("user_info.search.message_no_search_condition"),
      width: "500",
    };

    globalStore.openAlertMessage(objectAlert);
    return;
  }
  searchRequest.orgStatCd = orgStatCd.value.value;
  searchRequest.orgKdCd = orgKdCd.value.value;
  emit("search", searchRequest);
};
</script>

<template>
    <v-sheet border elevation="2" style="width: 100%" class="my-4 mr-4 px-4">
      <v-row no-gutters>
        <v-col >
          <div class="flex flex-nowrap">
            <label class="mt-6">
              {{ $t("org_info.search.lbl_org_info") }}
            </label>
            <div class="flex-grow mt-4 custom-height md:w-2/4 px-3">
              <v-text-field
                v-model="searchRequest.orgInfo"
                variant="outlined"
                :rules="
                useInputValidation({
                  engKorNumRule: true,
                })
              "
                :single-line="true"
                density="compact"
                class="mt-1 mr-4"
                type="text"
              ></v-text-field>
            </div>
          </div>
        </v-col>
        <v-col>
          <div
            class="flex flex-grow gap-2 flex-nowrap items-center justify-start"
          >
            <label class="no-wrap-label">{{
              $t("org_info.search.lbl_org_kd_cd")
            }}</label>
            <v-form ref="formSearch">
              <div class="flex w-[200px]">
                <v-combobox
                  v-model="orgKdCd"
                  :items="orgKdCdOption"
                  class="mt-6 custom-height"
                  item-title="label"
                  item-value="value"
                  density="compact"
                  variant="outlined"
                ></v-combobox>
              </div>
            </v-form>
          </div>
        </v-col>

        <v-col>
          <div
            class="flex flex-grow gap-2 flex-nowrap items-center justify-start"
          >
            <label class="no-wrap-label">{{
              $t("org_info.search.lbl_org_stat_cd")
            }}</label>
            <v-form ref="formSearch">
              <div class="flex w-[200px]">
                <v-combobox
                  v-model="orgStatCd"
                  :items="orgStatCdOption"
                  class="mt-6 custom-height"
                  item-title="label"
                  item-value="value"
                  density="compact"
                  variant="outlined"
                ></v-combobox>
              </div>
            </v-form>
          </div>
        </v-col>
        <v-col md="1" class="d-flex justify-end align-center sm:pb-4">
          <cf-button
            :label="$t(`org_info.search.btn_search`)"
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

.custom-height :deep(.v-combobox__selection){
  position: absolute;
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