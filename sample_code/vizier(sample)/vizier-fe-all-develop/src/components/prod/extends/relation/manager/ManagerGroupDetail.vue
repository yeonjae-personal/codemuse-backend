<template>
  <div class="relative bg-white rounded-[12px] pt-6 pb-4 w-full h-full">
    <div class="w-full overflow-x-auto h-full relative text-[12px]">
      <div class="w-full h-full flex relative">
        <div class="content w-full justify-start h-full flex flex-col">
          <div class="w-full flex justify-between items-center px-6 pb-2">
            <div
              class="text-text-base text-base-vnb font-medium leading-[40px]"
            >
              {{ $t("product_platform.targetDetails") }}
            </div>
          </div>
          <div class="w-full flex-1 overflow-hidden">
            <BaseTabs
              v-model="currentTab"
              :class-loco="'max-h-[calc(100vh-330px)] !px-2'"
              class-tabs-bar="px-4"
              :tabs="groupTab"
              :center-active="true"
              show-arrows
            />
          </div>
        </div>
      </div>
      <BasePagination
        v-if="currentTab === OFFER_TABS_VALUE.OFFER"
        :pagination="pagination"
        class="mt-5"
        @on-change-page="handleChangeOfferPage"
      />
    </div>
    <ShowDetailIcon
      class="absolute top-[174px] left-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="onClose"
    />
  </div>
</template>
<script setup lang="ts">
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import OfferTab from "@/components/prod/shared/OfferTab.vue";
import { useI18n } from "vue-i18n";
import {
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { LargeItemCode } from "@/enums";

const props = defineProps({
  isAdd: {
    type: Boolean,
    default: false,
  },
  screen: {
    type: String,
    default: "Relation",
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
const useSnackbar = useSnackbarStore();
const { sideDisplay, targetDetail } = storeToRefs(selectedStore.value);
const { t } = useI18n();
const optionsType = ref<any[]>([]);
const currentTab = ref(OFFER_TABS_VALUE.GENERAL);

const groupTab = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: targetDetail.value?.generalTab,
        createItemCodeList: optionsType.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: targetDetail.value?.additionalTab,
      },
    },
    {
      value: OFFER_TABS_VALUE.OFFER,
      label: t("product_platform.offer_title"),
      component: OfferTab,
      props: {
        modelList: targetDetail.value.offerTab,
        pagination: targetDetail.value.offerTabPagination,
      },
    },
    {
      value: OFFER_TABS_VALUE.HISTORY,
      label: t("product_platform.history"),
      component: HistoryTab,
    },
  ];

  return listTabs;
});

const pagination = computed(() => {
  const { offerTab, offerTabPagination } = targetDetail.value;
  const total = offerTab.length;
  const pageSize = 8;
  const totalPages = Math.ceil(total / pageSize);
  return {
    currentPage: offerTabPagination.currentPage,
    totalPages,
    pageSize,
  };
});

const onClose = () => {
  sideDisplay.value.targetDetail = false;
};

const handleChangeOfferPage = (page: number) => {
  targetDetail.value.offerTabPagination.currentPage = page;
};

watch(
  () => targetDetail.value.generalTab,
  () => {
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
  }
);

onMounted(async () => {
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Group,
    });
    optionsType.value = data;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>
<style scoped>
.active {
  background-color: #f14f4f;
  transition: all ease-in 0.7s;
}
p {
  margin-top: 12px;
  margin-bottom: 12px;
}
.selected-attribute {
  background-color: #faefef;
  border: 1px solid #e96565 !important;
  transition: all ease-in 0.8s;
}
.v-tab.v-tab.v-btn {
  color: #bdc1c7;
  font-size: 13px;
}
:deep().v-tabs-window {
  height: 100%;
}
:deep().v-window__container {
  height: 100%;
}
.offer-edit-datetime-picker {
  width: 120px;
}
.offer-edit-datetime-picker :deep().dp__pointer {
  height: 32px;
}
</style>
