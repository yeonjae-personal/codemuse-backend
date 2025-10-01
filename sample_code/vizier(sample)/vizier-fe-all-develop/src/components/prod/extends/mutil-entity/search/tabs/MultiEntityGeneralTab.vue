<template>
  <template v-if="detailList?.length > 0">
    <div class="text-text-lighter font-size-base font-medium">
      <DetailPane is-not-rounded-bottom>
        <template v-for="(item, index) in detailList" :key="index">
          <DetailPaneRow
            :label="$t(`product_platform.multiEntityDetailData.${item.key}`)"
            :tooltip-content="
              $t(`product_platform.multiEntityDetailData.${item.key}Desc`)
            "
            :is-always-show="!!item.labelDscr"
          >
            <template #value="{ klass }">
              <div
                v-if="
                  !isEdit ||
                  (props.category === DETAIL_CATEGORY.SEARCH &&
                    (item.key == 'itemCode' || item.key == 'entityTypeCode'))
                "
                :class="klass"
              >
                <CustomTooltip>
                  {{
                    item.key == "itemCode" || item.key == "entityTypeCode"
                      ? findType(item.value)
                      : getTextDisplay(
                          item.value,
                          item.fieldTypeCode,
                          groupCodeList[item.groupCode]
                        )
                  }}
                  <template #content>
                    {{
                      item.key == "itemCode" || item.key == "entityTypeCode"
                        ? findType(item.value)
                        : getTextDisplay(
                            item.value,
                            item.fieldTypeCode,
                            groupCodeList[item.groupCode]
                          )
                    }}
                  </template>
                </CustomTooltip>
              </div>
              <div v-else :class="klass">
                <div
                  v-if="item.editable"
                  class="text-text-base font-size-base font-normal w-full"
                >
                  <BaseDateTimePicker
                    v-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DP"
                    ref="datePicker"
                    v-model="selectedEntityDetails[item.key]"
                    :date="item.value"
                    :min-date="
                      item.key !== 'validStartDtm'
                        ? selectedEntityDetails['validStartDtm']
                        : currentDate
                    "
                    :required="item.isRequired"
                    :clearable="true"
                    enable-time-picker
                    :auto-apply="false"
                    input-mode
                    styles="absolute common-datetime-picker"
                  />
                  <div v-else-if="item.fieldTypeCode == COLUMN_FIELD_TYPE.DL">
                    <BaseSelectScroll
                      :ref="(el) => selectScrollList.push(el)"
                      v-model="selectedEntityDetails[item.key]"
                      :options="
                        item.key === 'itemCode'
                          ? multiEntityTypes
                          : item.key === 'entityTypeCode'
                            ? subTypeList
                            : groupCodeList[item.groupCode]
                      "
                      :required="item.isRequired"
                      :show-option-null="true"
                    />
                  </div>
                  <div v-else-if="item.fieldTypeCode == 'SRCH'">
                    <div
                      v-if="item.value"
                      class="flex justify-between align-center border border-[#DCE0E5] rounded-[8px] min-h-[32px] py-[6px] px-3"
                      :class="[
                        item.key !== dragType && isDragging
                          ? 'bg-[#f0f2f5] opacity-40'
                          : 'bg-white',
                      ]"
                      @drop="
                        drop(
                          $event,
                          index,
                          item,
                          MULTI_ENTITY_DROP_ACTION.CHANGE
                        )
                      "
                      @dragover="allowDrop($event, item.key)"
                      @click.stop="handleOpenAddItem(item.key)"
                    >
                      <CustomTooltip :content="item.value" location="bottom">
                        <span
                          class="text-truncate !no-underline cursor-pointer"
                        >
                          {{ item.value }}
                        </span>
                      </CustomTooltip>
                      <span
                        class="cursor-pointer"
                        @click="handleRemoveValue(item.key, index)"
                      >
                        <TrashIcon color="#C7291D" />
                      </span>
                    </div>
                    <ItemDrop
                      v-else
                      class-name="h-[32px] !text-[11px]"
                      :is-disabled="item.key !== dragType && isDragging"
                      @click.stop="handleOpenAddItem(item.key)"
                      @drop="
                        drop($event, index, item, MULTI_ENTITY_DROP_ACTION.ADD)
                      "
                      @dragover="allowDrop($event, item.key)"
                    />
                  </div>
                  <BaseInputText
                    v-else-if="item.fieldTypeCode === COLUMN_FIELD_TYPE.NF"
                    v-model="selectedEntityDetails[item.key]"
                    :styles="'input-edit'"
                    :required="item.isRequired"
                    :counter="
                      checkNumberIsInteger(item.attrMaxLength)
                        ? item.attrMaxLength
                        : Number(item.attrMaxLength)
                    "
                    :maxlength="getMaxLengthField(item, true)"
                    :rules="
                      useInputValidation({
                        required: item.isRequired,
                      })
                    "
                    @keypress="onlyNumber($event, Number(item.attrMaxLength))"
                    @input="
                      changeValueNumber($event, selectedEntityDetails[item.key])
                    "
                    @blur="handleBlurDecimal(selectedEntityDetails[item.key])"
                  />
                  <BaseInputText
                    v-else
                    v-model="selectedEntityDetails[item.key]"
                    :styles="'input-edit'"
                    :required="item.isRequired"
                    :counter="
                      checkNumberIsInteger(item.attrMaxLength)
                        ? item.attrMaxLength
                        : Number(item.attrMaxLength)
                    "
                    :maxlength="Number(item.attrMaxLength)"
                    :rules="
                      useInputValidation({
                        required: item.isRequired,
                        maxLength: item.attrMaxLength,
                      })
                    "
                  />
                </div>
                <div v-else class="font-normal">
                  {{
                    item.key == "itemCode" || item.key == "entityTypeCode"
                      ? findType(item.value)
                      : getTextDisplay(
                          item.key == "entityCode" &&
                            props.category === DETAIL_CATEGORY.CREATE
                            ? $t(`product_platform.auto_generation`)
                            : item.value,
                          item.fieldTypeCode,
                          groupCodeList[item.groupCode]
                        )
                  }}
                </div>
              </div>
            </template>
          </DetailPaneRow>
        </template>
      </DetailPane>
      <DetailPane
        v-if="
          selectedEntityDetails.entityTypeCode !==
          MULTI_ENTITY_SUBTYPE.USER_TITLE
        "
        is-not-rounded-top
        class="mt-2"
      >
        <DetailPaneRow is-overview :label="$t('product_platform.overview')">
          <template #value="{ overViewKlass, overtTextKlass }">
            <BaseTextArea
              v-if="isEdit"
              v-model="selectedEntityDetails.ovwCntn"
              :class="overtTextKlass"
              :rules="{
                maxLength: 500,
              }"
              :maxlength="500"
            />
            <div
              v-else
              :class="overViewKlass"
              v-html="displayTextArea(selectedEntityDetails.ovwCntn)"
            ></div>
          </template>
        </DetailPaneRow>
      </DetailPane>
    </div>
  </template>
  <template v-else>
    <div class="h-full w-full flex justify-center items-center">
      <NoData />
    </div>
  </template>
  <base-popup
    v-model="openPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="popupContent"
    @on-close="closePopupSave"
    @on-submit="handleDeleteItem"
  />
  <base-popup
    v-model="openPopupChange"
    :icon="DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="popupContent"
    @on-close="closePopupChange"
    @on-submit="handleChangeItem"
  />
</template>

<script setup lang="ts">
import { useInputValidation } from "@/composables/useInputValidation";
import { DATE_FORMAT } from "@/constants/index";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { DialogIconType } from "@/enums";
import {
  useDragStore,
  useMultiEntityCreateStore,
  useMultiEntitySearchStore,
} from "@/store";
import { useI18n } from "vue-i18n";
import moment from "moment-timezone";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";
import { useGroupCode } from "@/composables/useGroupCode";
import {
  MULTI_ENTITY_DROP_ACTION,
  MULTI_ENTITY_DROP_ID,
  MULTI_ENTITY_DROP_TYPE,
  MULTI_ENTITY_SUBTYPE,
} from "@/constants/multiEntity";
import { displayTextArea } from "@/utils/format-data";
import {
  checkNumberIsInteger,
  formatDataTypeDecimal,
} from "@/utils/extend-utils";

const props = defineProps({
  isEdit: {
    type: Boolean,
    default: false,
  },
  category: {
    type: String,
    default: DETAIL_CATEGORY.SEARCH,
  },
  isResetValidation: {
    type: Boolean,
    default: false,
  },
  groupCodeList: {
    type: Object,
    default: () => {},
  },
});

const multiEntitySearchStore = useMultiEntitySearchStore();
const multiEntityCreateStore = useMultiEntityCreateStore();
const {
  dragType,
  entityDetailData,
  selectedEntityDetails,
  entityDisplayForm,
  multiEntityTypes,
} = storeToRefs(
  props.category === DETAIL_CATEGORY.SEARCH
    ? multiEntitySearchStore
    : multiEntityCreateStore
);
const { isDragging } = storeToRefs(useDragStore());
const { t } = useI18n();
const { getTextDisplay } = useGroupCode();
const dropData = ref();
const openPopup = ref();
const openPopupChange = ref();
const popupContent = ref("");
const delItemIndex = ref();
const changeItemIndex = ref();
const datePicker = ref();
const selectScrollList = ref<any>([]);

const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const detailList = computed(() => {
  return entityDetailData.value.generalTab;
});
const getMaxLengthField = (item, getAllValue = false) => {
  let maxLength = item.attrMaxLength;
  if (maxLength) {
    if (checkNumberIsInteger(maxLength)) {
      return maxLength;
    }
    return getAllValue
      ? Number(maxLength) + 1
      : Number((maxLength + "").split(".")[0]) + 1;
  }
  return null;
};

const onlyNumber = ($event, maxLength = 0) => {
  let keyCode = $event.keyCode ? $event.keyCode : $event.which;

  if (maxLength && !checkNumberIsInteger(maxLength)) {
    if (keyCode !== 46 && (keyCode < 48 || keyCode > 57)) {
      $event.preventDefault();
    }
  } else {
    if (keyCode < 48 || keyCode > 57) {
      $event.preventDefault();
    }
  }
};

const changeValueNumber = (event, item) => {
  let valueOld = event.target.value;
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);

  item = formatDataTypeDecimal(valueOld, isDecimal, item.attrMaxLength);
};

const handleBlurDecimal = (item) => {
  let isDecimal = !checkNumberIsInteger(item.attrMaxLength);
  if (isDecimal) {
    let arr = item.split("");

    if (arr[arr.length - 1] === ".") {
      item = arr
        .filter((xxx) => xxx !== ".")
        .join()
        .replaceAll(",", "");
    }
  }
};

const subTypeList = computed(() => {
  if (selectedEntityDetails.value.itemCode) {
    const list: any = multiEntityTypes.value.find(
      (item: any) => item.value === selectedEntityDetails.value.itemCode
    );
    return list?.subOptions;
  }
  return [];
});

const handleOpenAddItem = (code: string) => {
  switch (code) {
    case MULTI_ENTITY_DROP_TYPE.GROUP:
      entityDisplayForm.value.offerSearch = false;
      entityDisplayForm.value.componentSearch = false;
      entityDisplayForm.value.resourceSearch = false;
      entityDisplayForm.value.groupSearch = true;
      break;
    case MULTI_ENTITY_DROP_TYPE.OFFER:
      entityDisplayForm.value.componentSearch = false;
      entityDisplayForm.value.groupSearch = false;
      entityDisplayForm.value.resourceSearch = false;
      entityDisplayForm.value.offerSearch = true;
      break;
    case MULTI_ENTITY_DROP_TYPE.COMPONENT:
      entityDisplayForm.value.groupSearch = false;
      entityDisplayForm.value.offerSearch = false;
      entityDisplayForm.value.resourceSearch = false;
      entityDisplayForm.value.componentSearch = true;
      break;
    case MULTI_ENTITY_DROP_TYPE.RESOURCE:
      entityDisplayForm.value.groupSearch = false;
      entityDisplayForm.value.offerSearch = false;
      entityDisplayForm.value.componentSearch = false;
      entityDisplayForm.value.resourceSearch = true;
      break;
  }
};

const handleRemoveValue = (code, index) => {
  switch (code) {
    case MULTI_ENTITY_DROP_TYPE.GROUP:
      popupContent.value = t("product_platform.delGroupCode");
      delItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.GROUP] = null;
      break;
    case MULTI_ENTITY_DROP_TYPE.OFFER:
      popupContent.value = t("product_platform.delOfferCode");
      delItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.OFFER] = null;
      break;
    case MULTI_ENTITY_DROP_TYPE.COMPONENT:
      popupContent.value = t("product_platform.delComponentCode");
      delItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.COMPONENT] = null;
      break;
    case MULTI_ENTITY_DROP_TYPE.RESOURCE:
      popupContent.value = t("product_platform.delResourceCode");
      delItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.RESOURCE] = null;
      break;
  }
  openPopup.value = true;
};

const handleChoosePopupChangeContent = (code, index) => {
  switch (code) {
    case MULTI_ENTITY_DROP_TYPE.GROUP:
      popupContent.value = t("product_platform.would_change_group");
      changeItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.GROUP] = null;
      break;
    case MULTI_ENTITY_DROP_TYPE.OFFER:
      popupContent.value = t("product_platform.would_change_offer");
      changeItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.OFFER] = null;
      break;
    case MULTI_ENTITY_DROP_TYPE.COMPONENT:
      popupContent.value = t("product_platform.would_change_component");
      changeItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.COMPONENT] = null;
      break;
    case MULTI_ENTITY_DROP_TYPE.RESOURCE:
      popupContent.value = t("product_platform.would_change_resource");
      changeItemIndex.value = index;
      selectedEntityDetails.value[MULTI_ENTITY_DROP_ID.RESOURCE] = null;
      break;
  }
  openPopupChange.value = true;
};

const drop = (event: DragEvent, index: number, item: any, action: string) => {
  event.preventDefault();
  dropData.value = event.dataTransfer?.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (action === MULTI_ENTITY_DROP_ACTION.ADD) {
    handleAddDropItem(index);
  } else {
    handleChoosePopupChangeContent(item.key, index);
  }
};

const allowDrop = (event: DragEvent, itemType: string) => {
  if (itemType == dragType.value) {
    event.preventDefault();
  }
};

const handleAddDropItem = (index: number) => {
  if (dropData.value) {
    switch (dragType.value) {
      case MULTI_ENTITY_DROP_TYPE.GROUP:
        entityDetailData.value.generalTab[index as number].value =
          dropData.value.objName;
        selectedEntityDetails.value.groupUuid = dropData.value.objUuid;
        break;
      case MULTI_ENTITY_DROP_TYPE.OFFER:
        entityDetailData.value.generalTab[index as number].value =
          dropData.value.objName;
        selectedEntityDetails.value.offerUuid = dropData.value.objUuid;
        break;
      case MULTI_ENTITY_DROP_TYPE.COMPONENT:
        entityDetailData.value.generalTab[index as number].value =
          dropData.value.name;
        selectedEntityDetails.value.cpntUuid = dropData.value.uuid;
        break;
      case MULTI_ENTITY_DROP_TYPE.RESOURCE:
        entityDetailData.value.generalTab[index as number].value =
          dropData.value.objName;
        selectedEntityDetails.value.rscUuid = dropData.value.objUuid;
        break;
    }
  }
};

const handleDeleteItem = async () => {
  entityDetailData.value.generalTab[delItemIndex.value].value = null;
  closePopupSave();
};

const handleChangeItem = () => {
  handleAddDropItem(changeItemIndex.value);
  closePopupChange();
};

const closePopupSave = () => {
  openPopup.value = false;
  delItemIndex.value = null;
};

const closePopupChange = () => {
  openPopupChange.value = false;
  changeItemIndex.value = null;
};

const findType = (type: string): string => {
  let typeName = "-";
  multiEntityTypes.value.forEach((item: any) => {
    if (item.value === type) {
      typeName = item.label;
    } else {
      if (item.subOptions.length > 0) {
        item.subOptions.find((subItem) => {
          if (subItem.value === type) {
            typeName = subItem.label;
          }
        });
      }
    }
  });
  return `${typeName} (${type})`;
};

const validationAllSelect = () => {
  if (selectScrollList.value?.length) {
    selectScrollList.value.forEach((comp: any) => {
      comp?.validate?.();
    });
  }
};
const resetValidationAllSelect = () => {
  if (selectScrollList.value?.length) {
    selectScrollList.value.forEach((comp: any) => {
      comp?.resetValidate?.();
    });
  }
};

defineExpose({
  validationAllSelect,
  resetValidationAllSelect,
});
</script>
<style scoped>
:deep().v-field {
  height: 32px;
  display: flex;
  align-items: center;
}
:deep().v-field__field {
  width: 100%;
  text-overflow: ellipsis;
}
.common-datetime-picker :deep().dp__pointer {
  height: 32px;
}
</style>
