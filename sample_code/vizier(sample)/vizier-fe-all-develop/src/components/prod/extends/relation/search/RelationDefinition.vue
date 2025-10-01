<template>
  <div
    class="relative bg-white rounded-[12px] pt-6 pb-3 w-full h-full"
    :class="[
      {
        'active-form':
          (isEdit || isDuplicate) &&
          [RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(page),
      },
    ]"
  >
    <div class="w-full overflow-x-auto h-full relative text-[12px] px-4">
      <div class="w-full h-full flex flex-column relative">
        <v-form v-model="isFormValid" class="w-full h-full">
          <div class="content w-full justify-start h-full flex flex-col">
            <div class="w-full flex justify-between items-center pb-2 px-2">
              <div
                class="text-text-base text-base-vnb font-medium leading-10 tracking-[0.5px] h-[40px]"
              >
                {{
                  $t(
                    isAdd
                      ? "product_platform.relationCreate"
                      : isDuplicate
                        ? "product_platform.relationDuplicate"
                        : isEdit && page === RELATION_PAGE.SEARCH
                          ? "product_platform.relationEdit"
                          : "product_platform.relationDetails"
                  )
                }}
              </div>
              <BaseButton
                v-if="
                  [RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(page) &&
                  currentTab !== OFFER_TABS_VALUE.HISTORY &&
                  !isEdit &&
                  !isDuplicate &&
                  !isAdd
                "
                :color="ButtonColorType.Secondary"
                @click="handleEdit"
              >
                <EditIcon class="mr-[6px]" />
                {{ $t("product_platform.edit") }}
              </BaseButton>
            </div>
            <ExtendsAccordion
              v-if="isAdd"
              is-disable-zoom
              type="normal"
              width="100%"
              class="!py-0 mb-2"
              :title="
                relationNewName
                  ? relationNewName
                  : $t(`product_platform.relationNewName`)
              "
              :is-show-list-product="false"
              :is-show-icon-append="false"
            >
            </ExtendsAccordion>
            <BaseTabs
              v-if="
                !isAdd ||
                (isAdd &&
                  (relationDetail?.generalTab || relationDetail?.additionalTab))
              "
              ref="baseTabs"
              v-model="currentTab"
              :tabs="offerTabs"
              :center-active="true"
              show-arrows
              :class-loco="[
                '!px-0',
                isAdd
                  ? 'max-h-[calc(100vh-390px)]'
                  : isEdit || isDuplicate
                    ? 'max-h-[calc(100vh-350px)]'
                    : 'max-h-[calc(100vh-330px)]',
              ]"
            />
          </div>
        </v-form>
        <div
          v-if="
            [RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(page) &&
            ((isEdit && currentTab != OFFER_TABS_VALUE.HISTORY) ||
              isDuplicate ||
              (isAdd && !isViewMode))
          "
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
    <ShowDetailIcon
      class="absolute top-[174px] cursor-pointer text-[#525457] hover:text-[#303132]"
      :class="
        ![RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(page)
          ? 'left-0'
          : 'right-0 rotate-180'
      "
      @click="onClose"
    />
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
      @on-submit="handleConfirmSave"
    />
  </div>
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  useSnackbarStore,
  useExtendManagerStore,
  useRelationSearchStore,
  useHistoryTabStore,
  useRelationCreateStore,
  useRelationManagerDuplicateStore,
} from "@/store";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import {
  ButtonColorType,
  DialogIconType,
  LargeItemCode,
  RequiredYn,
} from "@/enums";
import { RELATION_PAGE } from "@/constants/extendsManager";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import HistoryTab from "@/components/prod/shared/HistoryTab.vue";
import { MenuItemID } from "@/enums/redirect";

const props = defineProps({
  page: {
    type: String,
    default: RELATION_PAGE.MANAGER,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
});

const removeTab = inject<any>("removeTab");

const historyStore = useHistoryTabStore();
const extendManagerStore = useExtendManagerStore();
const relationSearchStore = useRelationSearchStore();
const relationCreateStore = useRelationCreateStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();

const selectedStore = computed<any>(() => {
  switch (props.page) {
    case RELATION_PAGE.SEARCH:
      return relationSearchStore;
    case RELATION_PAGE.CREATE:
      return relationCreateStore;
    case RELATION_PAGE.DUPLICATE:
      return relationManagerDuplicateStore;
    default:
      return extendManagerStore;
  }
});

const {
  sideDisplay,
  relationDetail,
  isShowRelationDetail,
  isEdit,
  isDuplicate,
  currentTab,
  selectedRelation,
  isViewMode,
  structureActiveMapFollower,
  structureActiveMapLeader,
} = storeToRefs(selectedStore.value);
const {
  upsetRelation,
  getExtendsDependencyRelationDefinitionDetail,
  getRelationSearch,
} = selectedStore.value;
const useSnackbar = useSnackbarStore();
const { t } = useI18n();

const optionsType = ref<any[]>([]);
const openPopup = ref(false);
const isCancel = ref(false);
const baseTabs = ref<HTMLElement | any>(null);
const isFormValid = ref<any>(false);

const typeElement = computed(() => {
  const itemCode = relationDetail?.value?.generalTab?.find(
    (el: any) => el.colName === "item_code"
  );
  return itemCode ? itemCode?.attrVal : "OR";
});

const relationNewName = computed(() => {
  if (
    relationDetail.value?.generalTab?.find((rel) => rel?.colName === "obj_name")
  ) {
    return relationDetail.value.generalTab.find(
      (rel) => rel?.colName === "obj_name"
    ).attrVal;
  } else {
    return "";
  }
});

const offerTabs = computed(() => {
  const listTabs = [
    {
      value: OFFER_TABS_VALUE.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        isEdit:
          [RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(props.page) &&
          !isViewMode.value &&
          (isEdit.value || isDuplicate.value || props.isAdd),
        modelValue: relationDetail.value?.generalTab,
        createItemCodeList: optionsType.value,
      },
    },
    {
      value: OFFER_TABS_VALUE.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        isEdit:
          [RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(props.page) &&
          !isViewMode.value &&
          (isEdit.value || isDuplicate.value || props.isAdd),
        modelValue: relationDetail.value?.additionalTab,
      },
    },
    {
      value: OFFER_TABS_VALUE.HISTORY,
      label: t("product_platform.history"),
      component: HistoryTab,
    },
  ];
  if (isEdit.value || isDuplicate.value || props.isAdd) {
    listTabs.splice(-1);
  }
  return listTabs;
});

const isInvalid = computed(
  () =>
    !!relationDetail.value.generalTab?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.toString().trim().length === 0 ||
          item.attrVal === null) &&
        item.colName !== "obj_code"
    ) ||
    !!relationDetail.value.additionalTab?.find(
      (item: any) =>
        item.requiredYn === RequiredYn.Yes &&
        (item.attrVal?.toString().trim().length === 0 || item.attrVal === null)
    )
);

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

const handleConfirmSave = async () => {
  if (isCancel.value) {
    if (isDuplicate.value) {
      isShowRelationDetail.value = true;
      isDuplicate.value = false;
    } else {
      isEdit.value = false;
    }
    if (props.isAdd) {
      await getExtendsDependencyRelationDefinitionDetail(null, false, true);
      removeTab(MenuItemID.RelationCreate);
    } else {
      await getExtendsDependencyRelationDefinitionDetail(
        selectedRelation.value.objUuid
      );
      await historyStore.fetchHistory({
        objUuid: selectedRelation.value?.objUuid,
      });
    }
  } else {
    try {
      const payload = {
        general: relationDetail.value.generalTab.filter(
          (item) => !item.dispTab
        ),
        additional: [
          ...relationDetail.value.generalTab.filter((item) => item.dispTab),
          ...relationDetail.value.additionalTab,
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
      } as any;
      const { data, status } = await upsetRelation(payload);

      if (status == 200) {
        useSnackbar.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        currentTab.value = OFFER_TABS_VALUE.GENERAL;
        await getExtendsDependencyRelationDefinitionDetail(
          props.isAdd || isDuplicate.value
            ? data
            : selectedRelation.value.objUuid
        );
        if (isDuplicate.value) {
          selectedRelation.value.objUuid = data;
        }
        if (props.page === RELATION_PAGE.SEARCH) {
          await historyStore.fetchHistory({
            objUuid: selectedRelation.value?.objUuid,
          });
          await getRelationSearch();
          isEdit.value = false;
          isDuplicate.value = false;
        } else if (props.page === RELATION_PAGE.CREATE) {
          isViewMode.value = true;
        }
      }
    } catch (error: any) {
      useSnackbar.showSnackbar(error.errorMsg, "error");
    }
  }
  openPopup.value = false;
};

const closePopupSave = () => {
  openPopup.value = false;
};

const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};

const onClose = () => {
  if (![RELATION_PAGE.SEARCH, RELATION_PAGE.CREATE].includes(props.page)) {
    sideDisplay.value.relationDetail = false;
    structureActiveMapFollower.value.relation = null;
    structureActiveMapLeader.value.relation = null;
  } else {
    isShowRelationDetail.value = false;
    isEdit.value = false;
    isDuplicate.value = false;
  }
};

const handleEdit = () => {
  isEdit.value = true;
};

watch(
  () => relationDetail.value.generalTab,
  () => {
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
  }
);

watch(
  () => typeElement.value,
  async (newVal: any) => {
    if (newVal && props.isAdd) {
      await getExtendsDependencyRelationDefinitionDetail(
        null,
        false,
        true,
        newVal
      );
    }
  }
);

onMounted(async () => {
  currentTab.value = OFFER_TABS_VALUE.GENERAL;
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Benefit,
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
