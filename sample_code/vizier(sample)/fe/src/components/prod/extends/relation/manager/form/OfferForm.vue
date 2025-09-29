<template>
  <v-form ref="form" @submit.prevent="">
    <div class="flex justify-between items-center mb-2 h-[40px]">
      <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
        {{ $t("product_platform.offer_search") }}
      </h1>
      <SearchAndRefreshButton
        @handle-search="handleSearch"
        @handle-refresh="handleResetSearch"
      />
    </div>
    <div class="grid gap-2">
      <v-row no-gutters class="gap-2">
        <v-col>
          <BaseSelectScroll
            ref="selectScroll"
            v-model="paramsExtendsFilterOfferSearch.subType"
            :options="offerItemCodeList"
            :placeholder="$t('product_platform.type')"
            :show-required-icon="false"
            :height="48"
            :show-option-null="false"
            required
            default-item-select-all
            styles="w-full"
          />
        </v-col>
      </v-row>

      <div class="filter gap-2">
        <BaseSelectScroll
          v-model="selectedNmCdOfferSearch"
          :options="optionsSearchType"
          :height="48"
          :default-item-select-all="false"
          @update:model-value="handleChangeOfferNmCd"
        />
        <BaseInputSearch
          v-model="initInput"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="handleSearch"
          @update:model-value="handleChangeInput"
        />
      </div>
    </div>
  </v-form>
</template>

<script setup lang="ts">
import { OFFER_CODE_TYPE, OPTIONS_SELECT_SEARCH } from "@/constants/offer";
import {
  useSnackbarStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
} from "@/store";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import { SPACE } from "@/constants/index";
import { useI18n } from "vue-i18n";

// const {
//   resetExtendParamFilterOfferSearch,
//   getExtendsListOfferSearch,
//   resetListTable,
// } = useExtendManagerStore();

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
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
});
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();

const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);

const {
  paramsExtendsFilterOfferSearch,
  selectedNmCdOfferSearch,
  offerItemCodeList,
  selectedItem,
} = storeToRefs(selectedStore.value);
const { t } = useI18n();
const {
  resetExtendParamFilterOfferSearch,
  getExtendsListOfferSearch,
  resetListTable,
} = selectedStore.value;

const gridViewParams = inject("gridViewParams", {
  category: SPACE,
  value: "",
  type: "name",
});

const useSnackbar = useSnackbarStore();
const itemSelected = ref();
const initInput = ref<any>(
  paramsExtendsFilterOfferSearch.value.prodItemCd || ""
);
const selectScroll = ref();
const form = ref();

const optionsSearchType = computed(() => {
  return OPTIONS_SELECT_SEARCH;
});

const handleSearch = async () => {
  selectScroll.value.validate();
  const { valid } = await form.value.validate();
  const isSubTypeNotAvailable = !paramsExtendsFilterOfferSearch.value.subType;
  if (!valid || isSubTypeNotAvailable) {
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
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleResetSearch = () => {
  selectScroll.value.resetValidate();
  form.value.resetValidation();
  itemSelected.value = null;
  selectedItem.value = null;
  initInput.value = null;
  resetExtendParamFilterOfferSearch();
  resetListTable();
  gridViewParams.category = SPACE;
  gridViewParams.value = "";
  gridViewParams.type = "name";
};

const getListOffer = async () => {
  try {
    await getExtendsListOfferSearch(props.category);
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleChangeOfferNmCd = () => {
  if (selectedNmCdOfferSearch.value === NM_CD_FIELDS[0].value) {
    paramsExtendsFilterOfferSearch.value.prodItemCd = undefined;
    paramsExtendsFilterOfferSearch.value.prodItemNm = initInput.value;
  } else {
    paramsExtendsFilterOfferSearch.value.prodItemCd = initInput.value;
    paramsExtendsFilterOfferSearch.value.prodItemNm = undefined;
  }
};

const handleChangeInput = (value) => {
  if (selectedNmCdOfferSearch.value === NM_CD_FIELDS[0].value) {
    paramsExtendsFilterOfferSearch.value.prodItemNm = value;
  } else {
    paramsExtendsFilterOfferSearch.value.prodItemCd = value;
  }
};

watch(
  () => paramsExtendsFilterOfferSearch.value.page,
  async () => {
    await getListOffer();
  }
);

watch(
  () => paramsExtendsFilterOfferSearch.value,
  (value) => {
    const isSearchWithCode = selectedNmCdOfferSearch.value === OFFER_CODE_TYPE;
    const prodItemCd = value.prodItemCd || "";
    const prodItemNm = value.prodItemNm || "";
    initInput.value = isSearchWithCode ? prodItemCd : prodItemNm;
  },
  { deep: true, immediate: true }
);
</script>

<style scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
