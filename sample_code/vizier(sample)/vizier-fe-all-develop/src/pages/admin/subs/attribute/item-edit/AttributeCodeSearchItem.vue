<!-- eslint-disable vue/no-v-html -->
<template>
  <div
    :class="[
      'label-item',
      { 'is-active': isActiveLabel, 'pb-3': isShowExpand },
    ]"
    @click="handleClickItem"
  >
    <div class="d-flex items-center space-between gap-[10px]">
      <div class="label-item-content flex-grow-1">
        <div
          class="label-item-content__title"
          v-html="highlightedText(LABEL_SEARCH_TYPE.NAME)"
        />
        <div class="label-item-content__code text-ellipsis">
          <CustomTooltip :content="item.cmcdGrpNm">
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
            <DotsVerticalIcon />
          </div>
        </template>
      </BasePopover>
    </div>

    <transition name="collapse">
      <div
        v-if="isShowExpand"
        :class="['label-item-detail', isShowExpand && 'mt-[8px]']"
      >
        <div class="label-item-detail__item">
          <div class="label-item-detail__title w-[64px]">
            {{ t("product_platform.code") }}
          </div>
          <div class="label-item-detail__title w-[64px]">
            {{ t("product_platform.dashboard.description") }}
          </div>
        </div>
        <template v-if="item.items.length > 0">
          <div
            v-for="code in item.items"
            :key="code.cmcdDetlId"
            class="label-item-detail__item"
          >
            <div class="label-item-detail__value min-w-[64px]">
              {{ code.cmcdDetlId }}
            </div>
            <div class="label-item-detail__value w-[calc(100%-64px)] flex-1">
              {{ code.cmcdDetlNm }}
            </div>
          </div>
        </template>
        <NoData v-else />
      </div>
    </transition>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import attributeManagementStore from "@/store/admin/attributeManagement.store";
import { highlightText } from "@/utils/format-data";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import type { ActionType } from "@/interfaces/prod";
import type { IAttributeCode } from "@/interfaces/admin/attribute-management";

type SearchTypeObject = {
  field: string;
  value: string;
  keysCheck: Record<"code" | "name", string>;
};

type Props = { item: IAttributeCode; searchTypeObj: SearchTypeObject };

const props = withDefaults(defineProps<Props>(), {
  searchTypeObj: () => ({
    value: "",
    field: "",
    keysCheck: { code: "", name: "" },
  }),
});

const emit = defineEmits<{
  (event: "switch-page", item: IAttributeCode): void;
  (event: "selected", item: any): void;
}>();

const { t } = useI18n();

const { selectedCommCode } = storeToRefs(attributeManagementStore());

const isShowExpand = ref<boolean>(false);

const highlightedText = computed(() => (type: "code" | "name") => {
  const valueToHighlight =
    type === LABEL_SEARCH_TYPE.NAME
      ? props.item.cmcdGrpNm
      : props.item.cmcdGrpId;
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

const isActiveLabel = computed<boolean>(
  () => props.item.cmcdGrpId === selectedCommCode.value?.cmcdGrpId
);

const handleClickItem = (): void => {
  isShowExpand.value = !isShowExpand.value;
  emit("selected", props.item);
};
</script>

<style lang="scss" scoped>
.collapse-enter-active,
.collapse-leave-active {
  transition: max-height 300ms ease;
}

.collapse-enter-from,
.collapse-leave-to {
  max-height: 0;
}

/* Assuming the maximum height your element can reach is 1000px */
.collapse-enter-to,
.collapse-leave-from {
  max-height: 1000px;
}

.label-item {
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
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #6b6d70;
  }

  &__title {
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.5px;
    color: #3a3b3d;
  }
}

.label-item-detail {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding: 12px;
  border-radius: 4px;
  background: #f7f8fa;

  &__item {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    text-transform: uppercase;
    color: #6b6d70;
  }

  &__value {
    font-family: Noto Sans KR;
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #3a3b3d;
    text-align: left;
    word-break: break-all;
  }
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
