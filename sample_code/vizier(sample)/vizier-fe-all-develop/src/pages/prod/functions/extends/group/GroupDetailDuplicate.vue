<template>
  <div
    class="relative z-10 top-0 right-0 bg-white rounded-[12px] pt-6 pb-[12px]"
  >
    <div class="w-full overflow-x-auto h-full relative text-[12px] px-4">
      <div class="w-full h-full flex relative">
        <div class="content w-full justify-start h-full flex flex-col">
          <div class="w-full flex justify-between items-center pr-4 pl-2 pb-2">
            <div
              class="text-text-base text-base-vnb font-medium leading-[40px]"
            >
              {{ $t("product_platform.groupDetail") }}
            </div>
          </div>
          <v-form v-model="isFormValid" class="w-full flex-1 overflow-hidden">
            <BaseTabs
              v-model="currentTab"
              :tabs="offerTabs"
              :show-arrows="false"
              :center-active="true"
              :class-loco="'max-h-[calc(100vh-333px)]'"
            />
          </v-form>
          <div class="flex justify-end pt-3">
            <BaseButton :color="ButtonColorType.Secondary" @click="onSave">
              <SaveIcon class="mr-[6px]" />
              {{ $t("product_platform.save") }}
            </BaseButton>
          </div>
        </div>
      </div>
    </div>

    <base-popup
      v-model="openPopup"
      :cancel-button-text="$t('product_platform.btn_no')"
      :content="$t('product_platform.desc_update')"
      :icon="DialogIconType.Info"
      :submit-button-text="$t('product_platform.btn_yes')"
      @on-close="handleCloseGroup"
      @on-submit="handleSaveGroup"
    />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  useExtendSearchStore,
  useOfferDuplicateProcessStore,
  useSnackbarStore,
} from "@/store";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import OfferTabDuplicate from "./OfferTabDuplicate.vue";
import { ButtonColorType, DialogIconType, LargeItemCode } from "@/enums";
import { GROUP_DETAIL_CATEFORY } from "@/constants/extendsManager";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { formatDate } from "@/utils/format-data";
import { cloneDeep } from "lodash-es";
const { selectedGroup, groupDetailData, groupsFinish, groupsOffer } =
  storeToRefs(useOfferDuplicateProcessStore());
const useSnackbar = useSnackbarStore();
const { updateOfferGroupDuplicate } = useExtendSearchStore();
const { t } = useI18n();

defineProps({
  category: {
    type: String,
    default: GROUP_DETAIL_CATEFORY.DETAIL,
  },
});

const currentTab = ref(OFFER_TABS_VALUE.OFFER);
const optionsType = ref<any[]>([]);
const isFormValid = ref<any>(false);
const openPopup = ref<any>(false);
const offerTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: groupDetailData.value?.generalTab,
        isEdit: false,
        createItemCodeList: optionsType.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: groupDetailData.value?.additionalTab,
        isEdit: false,
      },
    },
    {
      value: OFFER_TABS_VALUE.OFFER,
      label: t("product_platform.offer_title"),
      component: OfferTabDuplicate,
    },
  ];
  return listTabs;
});

const onSave = () => {
  openPopup.value = true;
};
const handleSaveGroup = async () => {
  // try {
  //   const generalPayload = {
  //     general: groupDetailData.value.generalTab,
  //     additional: groupDetailData.value.additionalTab,
  //     offerRel: groupDetailData.value.offerTab.filter((item) => {
  //       item.validStartDtm = formatDate(new Date());
  //       return item?.itemNew && !item.itemRemoved;
  //     }),
  //   } as any;
  //   if (generalPayload.offerRel?.length) {
  //     const res = await updateOfferGroupDuplicate(generalPayload);
  //     if (res && res.status === 200) {
  //       useSnackbar.showSnackbar(
  //         t("product_platform.successfully_saved"),
  //         "success"
  //       );
  //     }
  //   }

  // } catch (error: any) {
  //   useSnackbar.showSnackbar(error.errorMsg, "error");
  // }

  groupsOffer.value.forEach((gr) => {
    if (gr.objUuid === selectedGroup.value?.objUuid) {
      gr.detail.offerTab = cloneDeep(groupDetailData.value.offerTab);
    }
  });

  if (
    groupsFinish.value.some((gr) => gr.objUuid === selectedGroup.value?.objUuid)
  ) {
    groupsFinish.value = groupsFinish.value.filter(
      (frFinish) => frFinish.objUuid !== selectedGroup.value?.objUuid
    );
  }
  groupsFinish.value.push(selectedGroup.value);
  openPopup.value = false;
};
const handleCloseGroup = () => {
  openPopup.value = false;
};

watch(
  () => selectedGroup.value,
  () => {
    currentTab.value = OFFER_TABS_VALUE.OFFER;
  },
  { deep: true }
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
.active-form {
  border: 1px solid var(--border-border-primary, #d9325a);
  box-shadow: 0px 0px 0px 4px #d9325a29;
}
</style>
