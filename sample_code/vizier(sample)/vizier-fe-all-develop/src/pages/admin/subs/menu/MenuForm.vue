<template>
  <div class="flex justify-between items-center mb-2 h-[40px]">
    <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
      Menu List
    </h1>
    <SearchAndRefreshButton
      :is-show-refresh-button="false"
      @handle-search="handleSearch"
      @handle-refresh="handleResetSearch"
    />
  </div>
  <div class="grid gap-2">
    <v-row no-gutters class="gap-2">
      <v-col>
        <BaseInputSearch
          v-model="initInput"
          density="comfortable"
          label="Search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="handleSearch"
          @update:model-value="handleChangeInput"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script setup>
import { useSnackbarStore, useExtendManagerStore } from "@/store";
import SearchAndRefreshButton from "@/components/prod/common/SearchAndRefreshButton.vue";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import { useI18n } from "vue-i18n";

const { paramsExtendsFilterOfferSearch, selectedNmCdOfferSearch } = storeToRefs(
  useExtendManagerStore()
);

const { resetExtendParamFilterOfferSearch, getExtendsListOfferSearch } =
  useExtendManagerStore();

const props = defineProps({
  category: {
    type: String,
    require: true,
    default: "",
  },
  page: {
    type: Number,
    default: 1,
  },
});
const useSnackbar = useSnackbarStore();
const itemSelected = ref(null);
const initInput = ref(null);
const { t } = useI18n();

const handleSearch = async () => {
  if (!paramsExtendsFilterOfferSearch.value.detlType) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  try {
    paramsExtendsFilterOfferSearch.value = {
      ...paramsExtendsFilterOfferSearch.value,
      page: 1,
    };
    await getListOffer();
  } catch (error) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleResetSearch = () => {
  itemSelected.value = null;
  initInput.value = null;
  resetExtendParamFilterOfferSearch();
};

const getListOffer = async () => {
  try {
    await getExtendsListOfferSearch(props.category);
  } catch (error) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleChangeInput = (value) => {
  if (selectedNmCdOfferSearch.value === NM_CD_FIELDS[0].value) {
    paramsExtendsFilterOfferSearch.value.prodItemNm = value;
  } else {
    paramsExtendsFilterOfferSearch.value.prodItemCd = value;
  }
};
</script>

<style scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
