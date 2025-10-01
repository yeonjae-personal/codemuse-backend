<script setup lang="ts">

import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-alpine.css";

const emit = defineEmits(["search"]);

// Data
const srchWord = ref("");
const useYn = ref({ label: "전체", value: "" });

const useYnOption = ref([
  { label: "전체", value: "" },
  { label: "Y", value: "Y" },
  { label: "N", value: "N" },
]);
 
const handleClickSearch = () => {
  emit("search", {
    srchWord: srchWord.value,
    useYn: useYn.value.value,
  });
};
</script>

<template>
    <v-sheet border elevation="2" style="width: 100%" class="my-4 mr-4 px-4">
      <v-row no-gutters>
        <v-col md="4" sm="6" xs="12">
          <div class="flex flex-nowrap">
            <label class="mt-6">
              {{ $t("domain.domain_search.lbl_srch_word") }}
            </label>
            <div class="flex-grow mt-4 custom-height md:w-2/4 px-3">
              <v-text-field
                v-model="srchWord"
                variant="outlined"
                :single-line="true"
                density="compact"
                class="mt-1 mr-4"
                type="text"
              ></v-text-field>
            </div>
          </div>
        </v-col>
        <v-col md="5" sm="6">
          <div
            class="flex flex-grow gap-2 flex-nowrap items-center justify-start"
          >
            <label class="no-wrap-label">{{
              $t("domain.domain_search.lbl_use_yn")
            }}</label>
            <v-form ref="formSearch">
              <div class="flex w-[100px]">
                <v-combobox
                  v-model="useYn"
                  :items="useYnOption"
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
        <v-col md="3" class="d-flex justify-end align-center sm:pb-4">
          <cf-button
            :label="$t(`domain.domain_search.btn_search`)"
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

/** custom css  for dropdown */

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