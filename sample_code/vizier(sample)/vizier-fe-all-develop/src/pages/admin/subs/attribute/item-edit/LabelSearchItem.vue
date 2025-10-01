<template>
  <div
    :class="[
      'label-item',
      { 'is-disabled': disabled },
      { 'is-active': isActiveLabel },
    ]"
    @click="handleClickItem"
  >
    <div class="label-item-content">
      <div
        class="label-item-content__title"
        v-html="highlightedText(LABEL_SEARCH_TYPE.NAME)"
      ></div>
      <div class="label-item-content__code text-ellipsis">
        <CustomTooltip :content="currentLabelName" location="bottom">
          <span v-html="highlightedText(LABEL_SEARCH_TYPE.CODE)" />
        </CustomTooltip>
      </div>
    </div>
    <BasePopover
      v-if="actions.length > 0"
      :options="actions"
      custom-location="bottom-left"
    >
      <template #activator>
        <div class="label-item__popover">
          <DotsVerticalIcon :class="{ 'is-disabled-icon': disabled }" />
        </div>
      </template>
    </BasePopover>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import { highlightText } from "@/utils/format-data";
import { LabelLanguage } from "@/enums/labelManagement";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";
import type { ILabelItem } from "@/interfaces/admin/label-management";

type SearchTypeObject = {
  field: string;
  value: string;
  keysCheck: Record<"code" | "name", string>;
};

type Props = {
  item: ILabelItem;
  searchTypeObj: SearchTypeObject;
  disabled: boolean;
};

const props = withDefaults(defineProps<Props>(), {
  searchTypeObj: () => ({
    value: "",
    field: "",
    keysCheck: { code: "", name: "" },
  }),
});

const emit = defineEmits<{
  (event: "switch-page", item: ILabelItem): void;
  (event: "selected", item: ILabelItem): void;
}>();

const { locale, t } = useI18n();

const { selectedLabel } = storeToRefs(attributeManagementStore());

const highlightedText = computed(() => (type: "code" | "name") => {
  const valueToHighlight =
    type === LABEL_SEARCH_TYPE.NAME
      ? currentLabelName.value || ""
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
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    onClick: () => {
      emit("switch-page", props.item);
    },
  },
]);

const currentLabelName = computed<string>(() => {
  const label = props.item.items.find(
    ({ langCode }) => langCode === (locale.value || "en")
  );
  if (label && label.labelName !== "") {
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

const handleClickItem = (): void => {
  emit("selected", props.item);
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
  box-shadow:
    0px 6px 16px 0px #2d307c0a,
    0px -16px 16px 0px #395bc20a inset;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;

  &.is-active,
  &:hover {
    border: 2px solid #ff8fa1;
  }

  &__popover {
    flex-shrink: 0;
  }
}

.label-item-content {
  height: 40px;

  &__code {
    font-weight: 400;
    font-size: 11px;
    line-height: 17px;
    letter-spacing: 0.25px;
    color: #6b6d70;
  }

  &__title {
    font-weight: 400;
    font-size: 13px;
    line-height: 20px;
    letter-spacing: 0.5px;
    color: #3a3b3d;
  }
}

.is-disabled {
  background-color: #e9ebf0;
}

.is-disabled-icon {
  opacity: 0.32;
}

.text-ellipsis {
  max-width: 318px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}

:deep() .highlight {
  background-color: yellow;
}
</style>
