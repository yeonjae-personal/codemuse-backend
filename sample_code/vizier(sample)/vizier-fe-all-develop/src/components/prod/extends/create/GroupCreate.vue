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
              {{ $t("product_platform.groupCreate") }}
            </div>
          </div>
          <div>
            <cf-card-dropdown
              class-name="default group-icon"
              :width="388"
              is-disable-zoom
              :title="
                groupNewName
                  ? groupNewName
                  : $t(`product_platform.${groupName}`)
              "
              :description="$t(`product_platform.auto_generation`)"
              type-bg="linear"
              :node="{
                hideNodeLeft: true,
                isActiveNodeLeft: false,
                hideNodeRight: true,
                isActiveNodeRight: false,
              }"
              hide-detail
            >
              <template #icon>
                <span
                  class="flex justify-center align-center w-[40px] h-[40px]"
                >
                  <FolderIcon />
                </span>
              </template>
              <template #childCount>
                <div
                  class="text-text-primary text-[11px] w-6 h-6 flex items-center justify-center bg-primary-lighter rounded !border-[1px] !border-primary-lighter"
                >
                  {{ groupDetailData.offerTab.length }}
                </div>
              </template>
            </cf-card-dropdown>
          </div>
          <v-form
            v-model="isFormValid"
            class="w-full flex-1 overflow-hidden mt-2"
          >
            <BaseTabs
              ref="baseTabs"
              v-model="currentTab"
              :tabs="groupCreateTabs"
              :center-active="true"
              :class-loco="'max-h-[calc(100vh-410px)]'"
            />
          </v-form>
          <div v-if="!isViewMode" class="flex justify-end pt-3 gap-2">
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
    </div>
  </div>
  <base-popup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.groupCreateNew')
    "
    @on-close="closePopupSave"
    @on-submit="handleUpdateOfferDetails"
  />
</template>
<script setup lang="ts">
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import OfferTab from "@/components/prod/shared/OfferTab.vue";
import useCmcdStore from "@/store/cmcd.store";
import { useI18n } from "vue-i18n";
import { useExtendCreateStore, useSnackbarStore } from "@/store";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import {
  DialogIconType,
  LargeItemCode,
  RequiredYn,
  ButtonColorType,
} from "@/enums";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { MenuItemID } from "@/enums/redirect";

const groupName = "Group Name";
const removeTab = inject<any>("removeTab");

const emit = defineEmits(["closeLoadingComponent", "onCancel"]);
const {
  groupDetailData,
  isShowAddOffer,
  generalGroupCode,
  additionalGroupCode,
  isViewMode,
  currentTab,
  groupTypes,
} = storeToRefs(useExtendCreateStore());
const { updateGroupDetail, getGroupCreateInfo, resetValidationStatus } =
  useExtendCreateStore();
const useSnackbar = useSnackbarStore();
const { search } = useCmcdStore();
const { t } = useI18n();
const openPopup = ref(false);
const isCancel = ref(false);
const isFormValid = ref<any>(false);
const baseTabs = ref<HTMLElement | any>(null);

const groupCreateTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: groupDetailData.value?.generalTab,
        isEdit: !isViewMode.value,
        createItemCodeList: groupTypes.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: groupDetailData.value?.additionalTab,
        isEdit: !isViewMode.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.OFFER,
      label: t("product_platform.offer_title"),
      component: OfferTab,
      props: {
        isEdit: !isViewMode.value,
        isDuplicate: true,
        modelList: groupDetailData.value.offerTab,
      },
    },
  ];
  return listTabs;
});

const groupNewName = computed(() => {
  if (
    groupDetailData.value.generalTab.find((grp) => grp?.colName === "obj_name")
  ) {
    return groupDetailData.value.generalTab.find(
      (grp) => grp?.colName === "obj_name"
    ).attrVal;
  } else {
    return "";
  }
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
    emit("onCancel");
    emit("closeLoadingComponent");
    removeTab(MenuItemID.GroupCreate);
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
        emit("closeLoadingComponent");
        isViewMode.value = true;
        isShowAddOffer.value = false;
        useSnackbar.showSnackbar("Successfully created", "success");
        groupDetailData.value.offerTab.forEach((ofr) => {
          ofr.isAdd = false;
        });
      }
    } catch (error: any) {
      useSnackbar.showSnackbar(error.errorMsg, "error");
    }
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
  }
  closePopupSave();
};

const closePopupSave = () => {
  resetValidationStatus();
  openPopup.value = false;
};

const typeElement = computed(() => {
  const itemCode = groupDetailData?.value?.generalTab?.find(
    (el: any) => el.colName === "item_code"
  );
  return itemCode ? itemCode?.attrVal : "OG";
});

watch(
  () => typeElement.value,
  async (newVal: any) => {
    if (newVal) {
      await getGroupCreateInfo(newVal);
    }
  }
);
const getInitCreateGroup = async () => {
  currentTab.value = OFFER_TABS_VALUE.GENERAL;
  await getGroupCreateInfo("OG", true);
  if (groupDetailData.value.generalTab?.length) {
    const groupGeneralCodes: any[] = [];
    groupDetailData.value.generalTab.forEach((element: any) => {
      if (element.commGroupCode) {
        groupGeneralCodes.push(element.commGroupCode);
      }
    });
    generalGroupCode.value = await search([...new Set(groupGeneralCodes)]);
  }
  if (groupDetailData.value.additionalTab?.length) {
    const groupAdditionalCodes: any[] = [];
    groupDetailData.value.additionalTab.forEach((element: any) => {
      if (element.commGroupCode) {
        groupAdditionalCodes.push(element.commGroupCode);
      }
    });
    additionalGroupCode.value = await search([
      ...new Set(groupAdditionalCodes),
    ]);
  }
};

watch(
  () => currentTab.value,
  (newVal: String) => {
    emit("closeLoadingComponent");
    if (newVal === OFFER_TABS_VALUE.OFFER && !isViewMode.value) {
      isShowAddOffer.value = true;
    } else {
      isShowAddOffer.value = false;
    }
  }
);

onMounted(async () => {
  if (!groupDetailData.value?.generalTab?.length) getInitCreateGroup();
  if (!groupTypes.value?.length) {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Group,
    });
    groupTypes.value = data;
  }
});

defineExpose({ getInitCreateGroup });
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
