<template>
  <div
    class="col-span-1 relative z-10 top-0 right-0 bg-white rounded-[12px] pt-6 pb-[12px]"
    :class="{ 'active-form': isEdit && !isAdd }"
  >
    <div class="w-full overflow-x-auto h-full relative text-[12px] px-4">
      <div class="w-full h-full flex relative">
        <div class="content w-full justify-start h-full flex flex-col">
          <div class="w-full flex justify-between items-center pb-2">
            <div
              class="w-full flex justify-between items-center px-2 h-10 mb-2"
            >
              <div
                class="text-text-base text-base font-medium tracking-[0.5px]"
              >
                {{
                  isAdd
                    ? $t("product_platform.multiEntityCreate")
                    : isEdit
                      ? $t("product_platform.multiEntityEdit")
                      : $t("product_platform.multiEntityDetail")
                }}
              </div>
            </div>
            <div v-if="category === DETAIL_CATEGORY.SEARCH && !isEdit">
              <BaseButton
                :color="ButtonColorType.Secondary"
                @click="handleEdit"
              >
                <EditIcon class="mr-[6px]" />
                {{ $t("product_platform.edit") }}
              </BaseButton>
            </div>
          </div>
          <cf-card-dropdown
            v-if="isAdd"
            is-disable-zoom
            class-name="default entity-icon !w-full mb-2"
            :title="
              multiEntityNewName
                ? multiEntityNewName
                : $t(`product_platform.${MULTI_ENTITY_NAME}`)
            "
            :description="$t(`product_platform.auto_generation`)"
            :display-border-left="'dark-blue'"
            :border-color-action="'dark-blue'"
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
              <span class="flex justify-center align-center w-[32px] h-[32px]">
                <MultiEntityIcon />
              </span>
            </template>
          </cf-card-dropdown>
          <v-form ref="multiForm" class="w-full flex-1 overflow-hidden">
            <BaseTabs
              ref="baseTabs"
              v-model="currentTab"
              :tabs="multiEntityTabs"
              :center-active="true"
              :class-loco="`max-h-[calc(100vh-${
                props.category === DETAIL_CATEGORY.CREATE ? '415px' : '360px'
              })] !px-0`"
              class-tabs-bar="none"
              grow
            />
          </v-form>
          <div
            v-if="isEdit && currentTab !== OFFER_TABS_VALUE.HISTORY"
            class="flex justify-end pt-3 gap-2"
          >
            <BaseButton :color="ButtonColorType.Gray" @click="handleCancel">
              {{ $t("product_platform.cancel") }}
            </BaseButton>
            <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
              <SaveIcon class="mr-[6px]" />
              {{ $t("product_platform.save") }}
            </BaseButton>
          </div>
        </div>
      </div>
    </div>
    <ShowDetailIcon
      v-if="category === DETAIL_CATEGORY.SEARCH"
      class="absolute top-[160px] right-0 cursor-pointer text-[#525457] hover:text-[#303132] rotate-180"
      @click="onClose"
    />
  </div>
  <BasePopup
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
import cloneDeep from "lodash-es/cloneDeep";
import { useGroupCode } from "@/composables/useGroupCode";
import {
  useMultiEntityCreateStore,
  useMultiEntitySearchStore,
  useSnackbarStore,
} from "@/store";
import MultiEntityAdditionalTab from "@/components/prod/extends/mutil-entity/search/tabs/MultiEntityAdditionalTab.vue";
import MultiEntityGeneralTab from "@/components/prod/extends/mutil-entity/search/tabs/MultiEntityGeneralTab.vue";
import { DialogIconType, ButtonColorType, RequiredYn } from "@/enums";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";
import { getMultiEntityCreateInfo } from "@/api/prod/extendsApi";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import {
  INIT_MULTI_ENTITY_CREATE,
  MULTI_ENTITY_DETAIL_DATA,
  MULTI_ENTITY_DETAIL_USER_TITLE,
  MULTI_ENTITY_SUBTYPE,
} from "@/constants/multiEntity";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { MenuItemID } from "@/enums/redirect";

const props = defineProps({
  category: {
    type: String,
    default: DETAIL_CATEGORY.SEARCH,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
});

const removeTab = inject<any>("removeTab");
const emit = defineEmits(["onCancel"]);
const { search, groupCodeData } = useGroupCode();
const multiEntitySearchStore = useMultiEntitySearchStore();
const multiEntityCreateStore = useMultiEntityCreateStore();
const {
  selectedEntity,
  entityDisplayForm,
  isEdit,
  entityDetailData,
  multiEntityTypes,
  selectedEntityDetails,
  currentTab,
  isAdded,
} = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const {
  getEntityDetailInfo,
  getMultiEntityTypes,
  updateMultiEntity,
  getEntityList,
  closeAllSearchPanel,
  resetParamListGroupSearch,
  resetParamListOfferSearch,
  resetParamListComponentSearch,
  resetParamListResourceSearch,
} =
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore;

const useSnackbar = useSnackbarStore();
const { t } = useI18n();
const MULTI_ENTITY_NAME = "entityName";

const multiForm = ref<HTMLElement | any>(null);
const baseTabs = ref<HTMLElement | any>(null);
const openPopup = ref(false);
const isCancel = ref(false);

const multiEntityTabs = computed(() => [
  {
    value: OFFER_TABS_VALUE.GENERAL,
    label: t("product_platform.general"),
    component: MultiEntityGeneralTab,
    props: {
      category: props.category,
      isEdit: isEdit.value,
      groupCodeList: groupCodeData.value,
    },
  },
  {
    value: OFFER_TABS_VALUE.ADDITIONAL,
    label: t("product_platform.additional"),
    component: MultiEntityAdditionalTab,
    props: {
      category: props.category,
      isEdit: isEdit.value,
      groupCodeList: groupCodeData.value,
    },
  },
]);

const isInvalid = computed(() => {
  const generalTabInvalid = entityDetailData.value?.generalTab?.some(
    (item: any) =>
      item.isRequired &&
      (!selectedEntityDetails.value[item.key]?.toString().trim() ||
        selectedEntityDetails.value[item.key] === null)
  );
  const additionalTabInvalid = selectedEntityDetails.value?.additional?.some(
    (item: any) =>
      item.requiredYn === RequiredYn.Yes &&
      (!item.attrVal?.toString().trim() || item.attrVal === null)
  );
  return generalTabInvalid || additionalTabInvalid;
});

const multiEntityNewName = computed(() => {
  if (selectedEntityDetails?.value?.entityName) {
    return selectedEntityDetails?.value?.entityName;
  } else {
    return "";
  }
});

const onClose = () => {
  entityDisplayForm.value.entityDetail = false;
  isEdit.value = false;
};

const handleEdit = () => {
  isEdit.value = true;
};

const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};

const onSubmit = async () => {
  isCancel.value = false;
  const { valid } = await multiForm.value?.validate();
  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  if (!valid || isInvalid.value) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  openPopup.value = true;
  resetParamListGroupSearch();
  resetParamListOfferSearch();
  resetParamListComponentSearch();
  resetParamListResourceSearch();
};

const handleUpdateOfferDetails = async () => {
  if (isCancel.value) {
    if (props.category === DETAIL_CATEGORY.SEARCH) {
      isEdit.value = false;
      closeAllSearchForm();
    } else {
      returnInitCreateStatus();
      closeAllSearchPanel();
      setTimeout(() => {
        removeTab(MenuItemID.MultiEntityCreate);
      }, 100);
    }
  } else {
    try {
      const res = await updateMultiEntity(props.category);
      if (res && res.status === 200) {
        useSnackbar.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        if (props.category === DETAIL_CATEGORY.SEARCH) {
          await getEntityList();
          await getEntityDetailInfo();
          isEdit.value = false;
          closeAllSearchForm();
        } else {
          selectedEntity.value = {
            ...res.data,
            entityTypeCode: selectedEntityDetails.value?.entityTypeCode,
          };
          isEdit.value = false;
          isAdded.value = true;
          await getEntityDetailInfo();
          closeAllSearchForm();
        }
      }
    } catch (error: any) {
      useSnackbar.showSnackbar(error.errorMsg, "error");
    }
  }
  openPopup.value = false;
  resetParamListGroupSearch();
  resetParamListOfferSearch();
  resetParamListComponentSearch();
  resetParamListResourceSearch();
};

const closePopupSave = async () => {
  openPopup.value = false;
  if (props.category === DETAIL_CATEGORY.SEARCH && !isEdit.value) {
    await getEntityDetailInfo();
    closeAllSearchForm();
  }
};

const returnInitCreateStatus = () => {
  currentTab.value = OFFER_TABS_VALUE.GENERAL;
  selectedEntityDetails.value = cloneDeep(INIT_MULTI_ENTITY_CREATE);
  handleInitCreateEntity(MULTI_ENTITY_SUBTYPE.BUSINESS_LINE);
  handleChooseSubType(selectedEntityDetails.value.itemCode);
  emit("onCancel");
};

const handleInitCreateEntity = async (type: string) => {
  const { data } = await getMultiEntityCreateInfo({ entityTypeCode: type });
  if (
    [
      MULTI_ENTITY_SUBTYPE.BUSINESS_LINE,
      MULTI_ENTITY_SUBTYPE.DISCOUNT_TARGET,
      MULTI_ENTITY_SUBTYPE.SALE_COMPANY,
    ].includes(type)
  ) {
    entityDetailData.value.generalTab = cloneDeep(
      MULTI_ENTITY_DETAIL_DATA[type as string]?.map((item) => ({
        ...item,
        label: t(item.label),
      }))
    );
  } else {
    entityDetailData.value.generalTab = cloneDeep(
      MULTI_ENTITY_DETAIL_USER_TITLE
    );
  }
  entityDetailData.value.additionalTab = data;

  selectedEntityDetails.value.additional = data.map((item) => ({
    ...item,
    attrVal:
      item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
        ? JSON.parse(item?.attrVal)?.filter((value) => value.trim()) || []
        : item.attrVal,
  }));
};

const handleChooseSubType = (type: string) => {
  const list: any = multiEntityTypes.value.find(
    (item: any) => item.value === type
  );
  if (list) {
    selectedEntityDetails.value.entityTypeCode = list.subOptions[0]?.value;
  }
};

const closeAllSearchForm = () => {
  entityDisplayForm.value.groupSearch = false;
  entityDisplayForm.value.offerSearch = false;
  entityDisplayForm.value.componentSearch = false;
  entityDisplayForm.value.resourceSearch = false;
};

const collectCommonCode = async () => {
  const groupCodeCommon: string[] = [];
  entityDetailData.value.generalTab?.forEach((item: any) => {
    if (item.groupCode) {
      groupCodeCommon.push(item.groupCode);
    }
  });
  entityDetailData.value.additionalTab?.forEach((item: any) => {
    if (item.commGroupCode) {
      groupCodeCommon.push(item.commGroupCode);
    }
  });
  await search(groupCodeCommon);
};

watch(
  () => selectedEntityDetails.value?.itemCode,
  (newVal) => {
    if (newVal) {
      closeAllSearchForm();
      handleChooseSubType(newVal);
    }
  }
);

watch(
  () => selectedEntityDetails.value?.entityTypeCode,
  async (newVal) => {
    if (newVal && props.category === DETAIL_CATEGORY.CREATE) {
      selectedEntityDetails.value = cloneDeep({
        ...selectedEntityDetails.value,
        entityCode: null,
        entityName: null,
        validStartDtm: null,
        validEndDtm: null,
        ovwCntn: null,
        bsnLineTypeCode: null,
        groupUuid: null,
        offerUuid: null,
        cpntUuid: null,
        rscUuid: null,
        chrgTypeCode: null,
        mvnoBsnoYn: null,
      });
      await handleInitCreateEntity(newVal);
      await collectCommonCode();
    }
  }
);

onBeforeMount(async () => {
  if (
    props.category === DETAIL_CATEGORY.CREATE &&
    !selectedEntityDetails.value
  ) {
    await getMultiEntityTypes();
    returnInitCreateStatus();
  } else {
    await collectCommonCode();
  }
});
</script>

<style scoped lang="scss">
.active-form {
  border: 1px solid var(--border-border-primary, #d9325a);
  box-shadow: 0px 0px 0px 4px #d9325a29;
}
</style>
