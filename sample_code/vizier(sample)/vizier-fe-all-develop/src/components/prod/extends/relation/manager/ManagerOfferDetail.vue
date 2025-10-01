<template>
  <div class="relative bg-white rounded-[12px] pt-6 w-full h-full">
    <div class="w-full overflow-x-auto h-full relative text-[12px] pl-4 pr-2">
      <div class="w-full h-full flex relative">
        <v-form v-model="form" class="w-full">
          <div class="content w-full justify-start h-full flex flex-col">
            <div class="w-full flex justify-between items-center pr-2 pb-2">
              <div
                class="text-text-base text-base-vnb font-medium leading-[40px]"
              >
                {{ $t("product_platform.targetDetails") }}
              </div>
            </div>
            <div class="w-full flex-1 overflow-hidden">
              <BaseTabs
                v-model="currentTab"
                :tabs="offerTabs"
                class="pr-2"
                :class-tabs="'max-h-[calc(100vh-295px)]'"
                :show-arrows="false"
                :center-active="true"
              />
            </div>
          </div>
        </v-form>
      </div>
    </div>
    <ShowDetailIcon
      class="absolute top-[174px] left-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="closeStructureDetailPane"
    />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { STRUCTURE_ITEM_SCREEN, OFFER_TABS_VALUE } from "@/constants/offer";
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import ShowDetailIcon from "@/components/prod/icons/ShowDetailIcon.vue";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { LargeItemCode } from "@/enums";

const props = defineProps({
  isAdd: {
    type: Boolean,
    default: false,
  },
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
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
const { sideDisplay, selectedStructureData } = storeToRefs(selectedStore.value);
const snackbarStore = useSnackbarStore();
const { t } = useI18n();
const form = ref(false);
const currentTab = ref(OFFER_TABS_VALUE.GENERAL);
const optionsType = ref([]);

const offerTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: selectedStructureData.value?.general,
        isAdd: props.isAdd,
        screen: props.screen,
        createItemCodeList: optionsType.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: selectedStructureData.value?.additional,
        isAdd: props.isAdd,
        screen: props.screen,
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

watch(selectedStructureData, () => {
  currentTab.value = OFFER_TABS_VALUE.GENERAL;
});

const closeStructureDetailPane = () => {
  sideDisplay.value.offerDetail = false;
};

onMounted(async () => {
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    optionsType.value = data;
  } catch (error: any) {
    snackbarStore.showSnackbar(
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
