<script setup lang="ts">

import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

const emit = defineEmits(["search"]);

const srchWord = ref("");
const vocaDivsCd = ref<string[]>([]);
const stndYn = ref({ label: "전체", value: "" });

const dropdownOptions = ref([
  { label: "전체", value: "" },
  { label: "Y", value: "Y" },
  { label: "N", value: "N" },
]);



const handleClickSearch = () => {
  emit("search", {
    srchWord: srchWord.value,
    vocaDivsCd: vocaDivsCd.value,
    stndYn: stndYn.value.value,
  });
};

</script>
<template>
  <v-container :style="{ padding: '0px' }">
    <v-sheet border elevation="2" style="width: 100%" class="my-4 px-4">
      <v-row no-gutters>
        <v-col lg="4" md="4" sm="6" xs="12">
          <div class="flex flex-nowrap">
            <label class="mt-6">
              {{ $t("term.lbl_search_title") }}
            </label>
            <div class="flex-grow mt-4 custom-height px-3">
              <v-text-field
                v-model="srchWord"
                variant="outlined"
                :single-line="true"
                density="compact"
                type="text"
              ></v-text-field>
            </div>
          </div>
        </v-col>
        <v-col lg="5" md="8" sm="12" xs="12">
          <div class="flex justify-center mt-3 sm:justify-start">
            <label class="mt-4">
              {{ $t("term.lbl_search_target") }}
            </label>
            <v-checkbox
              v-model="vocaDivsCd"
              :label="$t('term.lbl_vocab')"
              value="WO"
            ></v-checkbox>

            <v-checkbox
              v-model="vocaDivsCd"
              :label="$t('term.lbl_term')"
              value="VO"
            ></v-checkbox>
            <v-checkbox
              v-model="vocaDivsCd"
              :label="$t('term.lbl_domain')"
              value="DO"
            ></v-checkbox>

            <v-checkbox
              v-model="vocaDivsCd"
              :label="$t('term.lbl_code')"
              value="CO"
            ></v-checkbox>
          </div>
        </v-col>
        <v-col lg="3" md="12" sm="12" xs="12">
          <div class="flex gap-2 flex-nowrap items-center justify-end">
            <label class="no-wrap-label">{{
              $t("term.lbl_standard_status")
            }}</label>
            <v-form ref="formSearch">
              <div class="flex w-[76px]">
                <v-combobox
                  v-model="stndYn"
                  class="mt-6 custom-height"
                  :items="dropdownOptions"
                  item-title="label"
                  item-value="value"
                  density="compact"
                  :single-line="true"
                  variant="outlined"
                ></v-combobox>
              </div>
            </v-form>
            <cf-button
              :label="$t(`term.lbl_search`)"
              @click="handleClickSearch"
            />
          </div>
        </v-col>
      </v-row>
    </v-sheet>
  </v-container>
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
