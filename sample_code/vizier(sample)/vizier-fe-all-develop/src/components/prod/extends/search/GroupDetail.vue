<template>
  <div
    class="relative z-10 top-0 right-0 bg-white rounded-[12px] pt-6 pb-[12px]"
    :class="[{ 'active-form': isEdit || isDuplicate }]"
  >
    <div class="w-full overflow-x-auto h-full relative text-[12px] px-4">
      <div class="w-full h-full flex relative">
        <div class="content w-full justify-start h-full flex flex-col">
          <div class="w-full flex justify-between items-center px-2 pb-2">
            <div
              class="text-text-base text-base-vnb font-medium leading-[40px]"
            >
              {{
                category === GROUP_DETAIL_CATEFORY.DETAIL
                  ? isEdit
                    ? $t("product_platform.groupEdit")
                    : $t("product_platform.groupDetail")
                  : $t("product_platform.groupDuplicate")
              }}
            </div>
            <div
              v-if="
                (category === GROUP_DETAIL_CATEFORY.DETAIL ? !isEdit : false) &&
                currentTab !== OFFER_TABS_VALUE.HISTORY
              "
            >
              <BaseButton
                :color="ButtonColorType.Secondary"
                @click="handleEdit"
              >
                <EditIcon class="mr-[6px]" />
                {{ $t("product_platform.edit") }}
              </BaseButton>
            </div>
          </div>
          <v-form v-model="isFormValid" class="w-full flex-1 overflow-hidden">
            <BaseTabs
              ref="baseTabs"
              v-model="currentTab"
              :tabs="offerTabs"
              :class-loco="[
                isEdit || isDuplicate
                  ? 'max-h-[calc(100vh-350px)]'
                  : 'max-h-[calc(100vh-330px)]',
              ]"
              :show-arrows="!isEdit || !isDuplicate"
              :center-active="true"
            />
          </v-form>
          <div
            v-if="
              (isEdit && currentTab != OFFER_TABS_VALUE.HISTORY) || isDuplicate
            "
            class="flex justify-end pt-3 gap-2"
          >
            <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
              {{ $t("product_platform.cancel") }}
            </BaseButton>
            <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
              <save-icon class="mr-[6px]" />
              {{ $t("product_platform.save") }}
            </BaseButton>
          </div>
        </div>
      </div>
      <BasePagination
        v-if="!isEdit && !isDuplicate && currentTab === OFFER_TABS_VALUE.OFFER"
        :pagination="pagination"
        class="mt-5"
        @on-change-page="handleChangeOfferPage"
      />
    </div>
    <ShowDetailIcon
      class="absolute top-[160px] right-0 cursor-pointer text-[#525457] hover:text-[#303132] rotate-180"
      @click="onClose"
    />
  </div>
  <base-popup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.desc_update')
    "
    @on-close="closePopupSave"
    @on-submit="handleUpdateOfferDetails"
  />
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  useExtendSearchStore,
  useHistoryTabStore,
  useSnackbarStore,
} from "@/store";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import OfferTab from "@/components/prod/shared/OfferTab.vue";
import {
  DialogIconType,
  LargeItemCode,
  RequiredYn,
  ButtonColorType,
} from "@/enums";
import { GROUP_DETAIL_CATEFORY } from "@/constants/extendsManager";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";

const emit = defineEmits(["closeLoadingComponent", "reloadGroupSearch"]);
const {
  displayForm,
  isEdit,
  isDuplicate,
  isResetGroupDetail,
  selectedGroup,
  groupDetailData,
} = storeToRefs(useExtendSearchStore());
const { updateGroupDetail, resetValidationStatus, getGroupDetailInfo } =
  useExtendSearchStore();
const historyStore = useHistoryTabStore();
const useSnackbar = useSnackbarStore();
const { t } = useI18n();

const props = defineProps({
  category: {
    type: String,
    default: GROUP_DETAIL_CATEFORY.DETAIL,
  },
});

const currentTab = ref(
  displayForm.value.addOffer ? OFFER_TABS_VALUE.OFFER : OFFER_TABS_VALUE.GENERAL
);
const openPopup = ref(false);
const isCancel = ref(false);
const optionsType = ref<any[]>([]);
const isFormValid = ref<any>(false);
const baseTabs = ref<HTMLElement | any>(null);

const offerTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: groupDetailData.value?.generalTab,
        isEdit:
          props.category === GROUP_DETAIL_CATEFORY.DETAIL
            ? isEdit.value
            : isDuplicate.value,
        createItemCodeList: optionsType.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: groupDetailData.value?.additionalTab,
        isEdit:
          props.category === GROUP_DETAIL_CATEFORY.DETAIL
            ? isEdit.value
            : isDuplicate.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.OFFER,
      label: t("product_platform.offer_title"),
      component: OfferTab,
      props: {
        isEdit:
          props.category === GROUP_DETAIL_CATEFORY.DETAIL
            ? isEdit.value
            : isDuplicate.value,
        isDuplicate: isDuplicate.value,
        modelList: groupDetailData.value.offerTab,
        pagination: groupDetailData.value.offerTabPagination,
      },
    },
    {
      value: OFFER_TABS_VALUE.HISTORY,
      label: t("product_platform.history"),
      component: HistoryTab,
      props: {
        type: "group",
      },
    },
  ];
  if (isEdit.value || isDuplicate.value) {
    listTabs.splice(-1);
  }
  return listTabs;
});

const isInvalid = computed(
  () =>
    !!groupDetailData.value.generalTab?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.toString().trim().length === 0 ||
          item.attrVal === null) &&
        item.colName !== "obj_code"
    ) ||
    !!groupDetailData.value.additionalTab?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.toString().trim().length === 0 || item.attrVal === null)
    )
);

const itemUuid = computed(() => {
  return groupDetailData.value.generalTab?.find(
    (item) => item.colName === "obj_uuid"
  )?.attrVal;
});

const onClose = () => {
  selectedGroup.value = null;
  if (props.category === GROUP_DETAIL_CATEFORY.DETAIL) {
    displayForm.value.groupDetail = false;
    displayForm.value.addOffer = false;
    isEdit.value = false;
  } else {
    displayForm.value.groupDuplicate = false;
    displayForm.value.addOffer = false;
    isDuplicate.value = false;
  }
};

const handleEdit = () => {
  isEdit.value = true;
  openOfferSearch();
};

const openOfferSearch = () => {
  if (
    (props.category === GROUP_DETAIL_CATEFORY.DETAIL &&
      currentTab.value === OFFER_TABS_VALUE.OFFER &&
      isEdit.value) ||
    (props.category === GROUP_DETAIL_CATEFORY.DUPLICATE &&
      currentTab.value === OFFER_TABS_VALUE.OFFER)
  ) {
    displayForm.value.addOffer = true;
  }
};
const getGroupDetail = async (isAdd = false) => {
  await getGroupDetailInfo(isAdd);
  historyStore.fetchHistory({
    objUuid: selectedGroup.value?.objUuid,
  });
};

const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};

const onSubmit = async () => {
  isCancel.value = false;
  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  if (!isFormValid.value || isInvalid.value) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    openPopup.value = false;
    return;
  } else {
    openPopup.value = true;
  }
};

const handleUpdateOfferDetails = async () => {
  if (isCancel.value) {
    isEdit.value = false;
    displayForm.value.addOffer = false;
    if (props.category !== GROUP_DETAIL_CATEFORY.DETAIL) {
      displayForm.value.groupDuplicate = false;
      isDuplicate.value = false;
    }
    await getGroupDetail(isDuplicate.value);
    openPopup.value = false;
    displayForm.value.groupDetail = true;
    emit("closeLoadingComponent");
  } else {
    try {
      const generalPayload = {
        general: groupDetailData.value.generalTab.filter(
          (item) => !item.dispTab
        ),
        additional: [
          ...groupDetailData.value.generalTab.filter((item) => item.dispTab),
          ...groupDetailData.value.additionalTab,
        ].map((item: any) => {
          let val = item.attrVal;
          if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
            val = JSON.stringify(item.attrVal);
          }
          return {
            ...item,
            attrVal: val,
          };
        }),
        offerRel: groupDetailData.value.offerTab.filter(
          (item) => item?.workTypeCode
        ),
      } as any;
      const res = await updateGroupDetail(generalPayload);
      if (res && res.status === 200) {
        useSnackbar.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        currentTab.value = OFFER_TABS_VALUE.GENERAL;

        emit("reloadGroupSearch");
        emit("closeLoadingComponent");
        displayForm.value.groupDetail = true;
        await getGroupDetail();
      }
    } catch (error: any) {
      useSnackbar.showSnackbar(error.errorMsg, "error");
    }
    isEdit.value = false;
    resetValidationStatus();
    openPopup.value = false;
    displayForm.value.groupDuplicate = false;
    displayForm.value.addOffer = false;
    isDuplicate.value = false;
    isResetGroupDetail.value = true;
  }
};

const closePopupSave = () => {
  resetValidationStatus();
  openPopup.value = false;
  if (props.category !== GROUP_DETAIL_CATEFORY.DETAIL) {
    displayForm.value.groupDuplicate = true;
    isDuplicate.value = true;
  }
  displayForm.value.addOffer = false;
};
watch(
  () => currentTab.value,
  (newVal) => {
    emit("closeLoadingComponent");
    if (
      (props.category === GROUP_DETAIL_CATEFORY.DETAIL &&
        newVal === OFFER_TABS_VALUE.OFFER &&
        isEdit.value) ||
      (props.category === GROUP_DETAIL_CATEFORY.DUPLICATE &&
        newVal === OFFER_TABS_VALUE.OFFER)
    ) {
      displayForm.value.addOffer = true;
    } else {
      displayForm.value.addOffer = false;
    }
  }
);

watch(
  () => itemUuid.value,
  () => {
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
  },
  {
    deep: true,
  }
);

onUnmounted(() => {
  // update offer current page to 1
  groupDetailData.value.offerTabPagination.currentPage = 1;
});

/**
 * @returns void
 * @description handle change page for offer tab
 */
const pagination = computed(() => {
  const { offerTab, offerTabPagination } = groupDetailData.value;
  const total = offerTab.length;
  const pageSize = 8;
  const totalPages = Math.ceil(total / pageSize);
  return {
    currentPage: offerTabPagination.currentPage,
    totalPages,
    pageSize,
  };
});

/**
 * @param page
 * @returns void
 * @description handle change page for offer tab
 */
const handleChangeOfferPage = (page: number) => {
  groupDetailData.value.offerTabPagination.currentPage = page;
};

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
