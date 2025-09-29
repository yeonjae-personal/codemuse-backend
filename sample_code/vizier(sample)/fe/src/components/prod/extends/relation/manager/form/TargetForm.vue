<template>
  <v-form ref="form" @submit.prevent="">
    <div class="flex justify-between items-center mb-2 h-[40px]">
      <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
        {{
          targetSearch === TARGET_TYPES.OFFER
            ? $t("product_platform.offer_search")
            : $t("product_platform.groupSearch")
        }}
      </h1>
      <SearchAndRefreshButton
        @handle-search="handleSearch"
        @handle-refresh="handleResetSearch"
      />
    </div>
    <div class="grid gap-2">
      <v-row no-gutters class="gap-2">
        <v-col v-if="targetSearch === TARGET_TYPES.GROUP">
          <BaseSelectScroll
            v-model="paramsExtendsTargetSearchGroup.itemCode"
            :options="groupItemCodeList"
            :height="48"
            :placeholder="$t('product_platform.selectBoxItem')"
            default-item-select-all
            styles="w-full"
          />
        </v-col>
        <v-col v-if="targetSearch === TARGET_TYPES.OFFER">
          <BaseSelectScroll
            v-model="paramsExtendsTargetSearchOffer.subType"
            :options="offerItemCodeList"
            :height="48"
            :placeholder="$t('product_platform.type')"
            default-item-select-all
            styles="w-full"
          />
        </v-col>
      </v-row>

      <div class="filter gap-2">
        <BaseSelectScroll
          v-model="selectedNmCdTargetSearch"
          :options="optionsSearchType"
          :height="48"
          :default-item-select-all="false"
          @update:model-value="handleChangeOfferNmCd"
        />
        <BaseInputSearch
          v-model="initInpup"
          density="comfortable"
          label="search"
          variant="solo"
          hide-details
          single-line
          rounded="4"
          @handle-search="handleSearch"
        />
      </div>
    </div>
  </v-form>
</template>

<script setup lang="ts">
import { OPTIONS_SELECT_SEARCH } from "@/constants/offer";
import { TARGET_TYPES } from "@/constants/extendsManager";
import {
  useSnackbarStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
} from "@/store";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import { SPACE } from "@/constants/index";

const props = defineProps({
  category: {
    type: String,
    default: "",
    require: true,
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
  targetSearch,
  paramsExtendsTargetSearchOffer,
  paramsExtendsTargetSearchGroup,
  selectedNmCdTargetSearch,
  groupItemCodeList,
  offerItemCodeList,
  isSearchGroupParentOffer,
  selectedItem,
} = storeToRefs(selectedStore.value);

const {
  getExtendsListOfferSearch,
  getGroupListTargetSearch,
  resetParamsExtendsTargetSearchGroup,
  resetParamsExtendsTargetSearchOffer,
} = selectedStore.value;

const useSnackbar = useSnackbarStore();
const itemSelected = ref();
const initInpup = ref();
const form = ref();

const optionsSearchType = computed(() => {
  return OPTIONS_SELECT_SEARCH;
});

const handleSearch = async () => {
  await form.value.validate();

  try {
    if (targetSearch.value === TARGET_TYPES.OFFER) {
      if (selectedNmCdTargetSearch.value === NM_CD_FIELDS[0].value) {
        paramsExtendsTargetSearchOffer.value.prodItemNm = initInpup.value;
      } else {
        paramsExtendsTargetSearchOffer.value.prodItemCd = initInpup.value;
      }
      paramsExtendsTargetSearchOffer.value = {
        ...paramsExtendsTargetSearchOffer.value,
        page: 1,
      };
      if (paramsExtendsTargetSearchOffer.value?.subType === " ") {
      }
      await getListOffer();
    } else {
      if (selectedNmCdTargetSearch.value === NM_CD_FIELDS[0].value) {
        paramsExtendsTargetSearchGroup.value.offrGrpNm = initInpup.value;
      } else {
        paramsExtendsTargetSearchGroup.value.offrGrpCd = initInpup.value;
      }
      paramsExtendsTargetSearchGroup.value = {
        ...paramsExtendsTargetSearchGroup.value,
        page: 1,
      };
      await getListGroup();
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleResetSearch = () => {
  form.value.resetValidation();
  itemSelected.value = null;
  initInpup.value = null;
  resetParamsExtendsTargetSearchGroup();
  resetParamsExtendsTargetSearchOffer();
  if (isSearchGroupParentOffer.value) {
    paramsExtendsTargetSearchGroup.value.childOffrUuid =
      selectedItem.value.objUuid;
  }
};

const getListOffer = async () => {
  try {
    await getExtendsListOfferSearch(props.category);
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};
const getListGroup = async () => {
  try {
    await getGroupListTargetSearch();
  } catch (error: any) {
    useSnackbar.showSnackbar(error?.errorMsg, "error");
  }
};

const handleChangeOfferNmCd = () => {
  if (targetSearch.value === TARGET_TYPES.OFFER) {
    if (selectedNmCdTargetSearch.value === NM_CD_FIELDS[0].value) {
      paramsExtendsTargetSearchOffer.value.prodItemCd = undefined;
      paramsExtendsTargetSearchOffer.value.prodItemNm = initInpup.value;
    } else {
      paramsExtendsTargetSearchOffer.value.prodItemCd = initInpup.value;
      paramsExtendsTargetSearchOffer.value.prodItemNm = undefined;
    }
  } else {
    if (selectedNmCdTargetSearch.value === NM_CD_FIELDS[0].value) {
      paramsExtendsTargetSearchGroup.value.offrGrpCd = undefined;
      paramsExtendsTargetSearchGroup.value.offrGrpNm = initInpup.value;
    } else {
      paramsExtendsTargetSearchGroup.value.offrGrpCd = initInpup.value;
      paramsExtendsTargetSearchGroup.value.offrGrpNm = undefined;
    }
  }
};

watch(
  () => targetSearch.value,
  () => {
    initInpup.value = null;
    resetParamsExtendsTargetSearchGroup();
    resetParamsExtendsTargetSearchOffer();
  }
);

watch(
  () => paramsExtendsTargetSearchOffer.value.page,
  async () => {
    await getListOffer();
  }
);

watch(
  () => paramsExtendsTargetSearchGroup.value.page,
  async () => {
    await getListGroup();
  }
);
watch(
  () => paramsExtendsTargetSearchGroup.value.childOffrUuid,
  async () => {
    initInpup.value = null;
  }
);

onMounted(() => {
  paramsExtendsTargetSearchOffer.value.subType = SPACE;
  paramsExtendsTargetSearchGroup.value.itemCode = SPACE;
});
</script>

<style scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
