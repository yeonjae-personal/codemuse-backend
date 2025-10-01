<template>
  <div :class="['label-item zoom-animation', { 'is-active': isActiveLabel }]">
    <div class="label-item-content">
      <div :class="['w-full h-[24px]', { 'text-[#bdc1c7]': isNewLabel }]">
        <CustomTooltip :content="currentLabelName" location="bottom">
          <span
            :class="['label-item-content__title', { 'is-new': isNewLabel }]"
            v-html="highlightedText(LABEL_SEARCH_TYPE.NAME)"
          />
        </CustomTooltip>
      </div>
      <div class="label-item-content__code">
        <span v-html="highlightedText(LABEL_SEARCH_TYPE.CODE)"></span>
      </div>
    </div>
    <BasePopover
      v-if="actions.length > 0"
      :options="actions"
      custom-location="bottom-left"
    >
      <template #activator>
        <div class="label-item__popover">
          <DotsVerticalIcon />
        </div>
      </template>
    </BasePopover>
  </div>
  <BasePopup
    v-if="isShowPopup"
    v-model="isShowPopup"
    :content="t('product_platform.do_you_want_to_remove_label')"
    :icon="DialogIconType.Warning"
    :cancel-button-text="t('product_platform.btn_no')"
    :submit-button-text="t('product_platform.btn_yes')"
    @on-close="handleClosePopup"
    @on-submit="handleRemoveLabel"
  />
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import useLabelStore from "@/store/admin/label.store";
import { useSnackbarStore } from "@/store";
import { deleteLabel } from "@/api/prod/labelApi";
import { highlightText } from "@/utils/format-data";
import { DialogIconType } from "@/enums";
import { LabelLanguage } from "@/enums/labelManagement";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import type { ActionType } from "@/interfaces/prod";
import type { ILabelItem } from "@/interfaces/admin/label-management";
import { updateLabelI18n } from "@/utils/fetch-i18n";
import { BORDER_CONFIG } from "@/constants/index";

type SearchTypeObject = {
  field: string;
  value: string;
  keysCheck: Record<"code" | "name", string>;
};

type Props = { item: ILabelItem; searchTypeObj: SearchTypeObject };
const defaultBorderActive = ref(BORDER_CONFIG.ACTIVE);

const props = withDefaults(defineProps<Props>(), {
  searchTypeObj: () => ({
    value: "",
    field: "",
    keysCheck: { code: "", name: "" },
  }),
});

const { locale, t } = useI18n();

const { selectedLabel, isAddNew, isEditing, isOpenPopup, listLabel } =
  storeToRefs(useLabelStore());
const { getListLabel } = useLabelStore();
const { showSnackbar } = useSnackbarStore();

const isShowPopup = ref<boolean>(false);

const highlightedText = computed(() => (type: "code" | "name") => {
  const valueToHighlight =
    type === LABEL_SEARCH_TYPE.NAME
      ? currentLabelName.value || ""
      : props.item.labelId.includes("product_platform")
        ? t(props.item.labelId)
        : props.item.labelId;
  if (
    !props.searchTypeObj.value ||
    props.searchTypeObj.field !== props.searchTypeObj.keysCheck[type as string]
  ) {
    return valueToHighlight;
  }
  return highlightText(valueToHighlight, props.searchTypeObj.value);
});

const actions = computed<ActionType[]>(() => [
  {
    name: t("product_platform.actionRemove"),
    icon: TrashIcon,
    onClick: () => {
      if (isEditing.value || isAddNew.value) {
        isOpenPopup.value = true;
        return;
      }
      isShowPopup.value = true;
    },
  },
]);

const currentLabelName = computed<string>(() => {
  const label = props.item.items.find(
    ({ langCode }) => langCode === (locale.value || "en")
  );
  if (label && label.labelName) {
    return label.labelName;
  }
  const englishItem = props.item.items.find(
    ({ langCode }) => langCode === LabelLanguage.English
  );
  return englishItem?.labelName || t("product_platform.new_label");
});

const isActiveLabel = computed<boolean>(
  () => props.item.labelId === selectedLabel.value?.labelId
);

const isNewLabel = computed<boolean>(
  () => !props.item.labelId.startsWith("LB")
);

const handleRemoveLabel = async (): Promise<void> => {
  try {
    await deleteLabel({ labelId: props.item.labelId });
    selectedLabel.value = null;
    await getListLabel();
    updateLabelI18n(listLabel.value, props.item.labelId);
    showSnackbar(t("product_platform.remove_label_successfully"), "success");
  } catch (error: any) {
    if (error.errorCode === "400") {
      showSnackbar(error.errorMsg, "error");
    } else {
      showSnackbar(t("product_platform.internalServerError"), "error");
    }
  }
};

const handleClosePopup = (): void => {
  isShowPopup.value = false;
};
</script>

<style lang="scss" scoped>
.label-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border: 2px solid #f0f2f5;
  box-shadow: 0px 6px 16px 0px #2d307c0a;
  box-shadow: 0px -16px 16px 0px #395bc20a inset;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;

  &.is-active {
    border: 2px solid v-bind(defaultBorderActive);
  }

  &__popover {
    flex-shrink: 0;
  }
}

.label-item-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  width: calc(100% - 31px);
  height: 40px;

  &__code {
    font-weight: 400;
    font-size: 11px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #6b6d70;
  }

  &__title {
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.5px;
    color: #3a3b3d;

    &.is-new {
      color: #bdc1c7;
    }
  }
}

.text-ellipsis {
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

:deep() .highlight {
  background-color: yellow;
}
</style>
