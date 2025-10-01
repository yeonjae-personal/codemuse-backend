<template>
  <div class="field-wrapper">
    <div
      class="field-header"
      :class="{ expaned: item.isExpaned, disabled: !item.isUsed }"
      @click="handleExpaned"
    >
      <span class="field-sort">{{ props.order }}</span>
      <span class="field-label">
        {{
          props.item.labelId
            ? t(item.labelId)
            : t("product_platform.label_name")
        }}
      </span>
      <BasePopover
        v-if="props.item.isNew"
        :options="actions"
        custom-location="bottom-left"
      >
        <template #activator>
          <div class="field-header__popover">
            <DotsVerticalIcon />
          </div>
        </template>
      </BasePopover>
      <div class="item-draggable">
        <DotGridIcon />
      </div>
    </div>
    <div v-if="props.item.isExpaned" class="field-content">
      <div class="field-item">
        <div class="field-label">{{ t("product_platform.label_id") }}</div>
        <div :class="['field-value', { 'is-active': isShowLabelSearch }]">
          <BaseInputText
            v-model="fieldItem.labelId"
            :required="true"
            :readonly="true"
            :styles="isShowLabelSearch ? 'is-active' : ''"
          >
            <template #append-inner>
              <div class="icon-search">
                <SearchIcon
                  class="cursor-pointer"
                  fill="#6B6D70"
                  @click.stop="handleOpenLabelSearch"
                />
              </div>
            </template>
          </BaseInputText>
        </div>
      </div>
      <div class="field-item h-auto">
        <div class="field-label">{{ t("product_platform.field_type") }}</div>
        <div class="field-value">
          <BaseSelectScroll
            v-model="fieldItem.fieldType"
            :options="fieldTypes"
            :default-item-select-all="false"
            required
            class="w-full"
            @update:model-value="onChangeFieldType"
          />
        </div>
      </div>
      <div v-if="isShowAttributeCode" class="field-item">
        <div class="field-label">
          {{ t("product_platform.attribute_code") }}
        </div>
        <div
          :class="['field-value', { 'is-active': isShowAttributeCodeSearch }]"
        >
          <BaseInputText
            v-model="fieldItem.attributeCode"
            :required="true"
            :readonly="true"
            :styles="isShowAttributeCodeSearch ? 'is-active' : ''"
          >
            <template #append-inner>
              <div class="icon-search">
                <SearchIcon
                  class="cursor-pointer"
                  fill="#6B6D70"
                  @click.stop="handleOpenAttributeCodeSearch"
                />
              </div>
            </template>
          </BaseInputText>
        </div>
      </div>
      <div v-if="isShowMaxLength" class="field-item">
        <div class="field-label">{{ t("product_platform.max_length") }}</div>
        <div class="field-value">
          <BaseInputText v-model="fieldItem.maxLength" :required="true" />
        </div>
      </div>
      <div class="field-item">
        <div class="field-label">{{ t("product_platform.disp_tab") }}</div>
        <div class="field-value radio-field">
          <BaseRadio
            v-model="fieldItem.dispTab"
            :yes-label="t('product_platform.general_tab')"
            :no-label="t('product_platform.additional_tab')"
            yes-value="G"
            no-value="A"
            class="flex"
            density="compact"
          />
        </div>
      </div>
      <div class="field-item-custom">
        <div class="field-item">
          <div class="field-label-custom">
            {{ t("product_platform.mandatory") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isRequired"
              hide-details
              color="#D9325A"
              base-color="#E6E9ED"
              inset
              density="compact"
            />
          </div>
        </div>
        <div class="field-item">
          <div class="field-label-custom">
            {{ t("product_platform.advanced_search_y_n") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isAdvancedSearch"
              hide-details
              color="#D9325A"
              base-color="#E6E9ED"
              inset
              density="compact"
              :disabled="isDisabledAdvancedSearch"
            />
          </div>
        </div>
      </div>
      <div class="field-item-custom">
        <div class="field-item">
          <div class="field-label-custom">
            {{ t("product_platform.useYn") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isUsed"
              hide-details
              color="#D9325A"
              base-color="#E6E9ED"
              inset
              density="compact"
            />
          </div>
        </div>
        <div v-if="isShowDispCard" class="field-item field-custom">
          <div class="field-label">
            {{ t("product_platform.disp_card_y_n") }}
          </div>
          <div class="field-value-custom">
            <v-switch
              v-model="fieldItem.isDispCard"
              hide-details
              color="#D9325A"
              base-color="#E6E9ED"
              density="compact"
              :disabled="isDisabledDispCard"
              inset
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import BaseInputText from "@/components/prod/common/BaseInputText.vue";
import DotGridIcon from "@/components/prod/icons/DotGridIcon.vue";
import TrashIcon from "@/components/prod/icons/TrashIcon.vue";
import attributeManagementStore, {
  IAdditionalItem,
} from "@/store/admin/attributeManagement.store";
import type { ActionType } from "@/interfaces/prod";
import BaseSelectScroll from "@/components/prod/common/BaseSelectScroll.vue";

const {
  listFieldType,
  itemDetail,
  selectedAdditionalId,
  isShowLabelSearch,
  isShowAttributeCodeSearch,
  selectedCommCode,
  commCodeSearchParams,
} = storeToRefs(attributeManagementStore());
const {
  showLabelSearch,
  showAttributeCodeSearch,
  updateIsExpaned,
  getListCommCode,
} = attributeManagementStore();

const props = defineProps({
  item: {
    type: Object as PropType<IAdditionalItem>,
    required: true,
  },
  order: {
    type: Number,
    default: 1,
  },
});

const { t } = useI18n();

const fieldItem = ref<IAdditionalItem>(props.item);

const fieldTypes = computed(() => {
  const options = isShowDispCard.value
    ? listFieldType.value.filter((item) => item.value !== "OB")
    : listFieldType.value;

  return options.map((item) => ({
    cmcdDetlId: item.value,
    cmcdDetlNm: item.name,
  }));
});

const isDisabledDispCard = computed<boolean>(
  () => props.item.fieldType === "TA"
);

const isShowAttributeCode = computed(() => {
  return (
    props.item.fieldType && ["DM", "DL", "OB"].includes(props.item.fieldType)
  );
});

const isShowMaxLength = computed(() => {
  return (
    props.item.fieldType &&
    !["DP", "DM", "DL", "OB"].includes(props.item.fieldType)
  );
});

const isShowDispCard = computed<boolean>(
  () => itemDetail.value.generalInfo.item === "Offer"
);

const isDisabledAdvancedSearch = computed<boolean>(
  () => fieldItem.value.fieldType === "OB"
);

const onChangeFieldType = () => {
  if (["DM", "DL", "OB"].includes(props.item.fieldType)) {
    selectedCommCode.value = null;
    commCodeSearchParams.value.page = 1;
    commCodeSearchParams.value.fieldType = props.item.fieldType;
    getListCommCode();
  }
};

const actions = computed<ActionType[]>(() => [
  {
    name: t("product_platform.actionRemove"),
    icon: TrashIcon,
    onClick: () => {
      const { additionalInfo } = itemDetail.value;
      itemDetail.value.additionalInfo = additionalInfo.filter(
        ({ id }) => id !== props.item.id
      );
      if (selectedAdditionalId.value === props.item.id) {
        selectedAdditionalId.value = "";
      }
    },
  },
]);

watch(
  () => props.item,
  (value) => {
    fieldItem.value = value;
  },
  { deep: true }
);

watch(isDisabledAdvancedSearch, (value) => {
  if (value) {
    fieldItem.value.isAdvancedSearch = false;
  } else {
    fieldItem.value.isAdvancedSearch = true;
  }
});

watch(isShowAttributeCode, (value) => {
  if (!value) {
    itemDetail.value.additionalInfo.forEach((item) => {
      if (item.id === props.item.id) {
        item.attributeCode = "";
      }
    });
    isShowAttributeCodeSearch.value = false;
  }
});

watch(isShowMaxLength, (value) => {
  if (!value) {
    itemDetail.value.additionalInfo.forEach((item) => {
      if (item.id === props.item.id) {
        item.maxLength = 0;
      }
    });
  }
});

watch(selectedAdditionalId, (value) => {
  if (!value) {
    isShowAttributeCodeSearch.value = false;
    isShowLabelSearch.value = false;
  }
});

const handleExpaned = () => {
  updateIsExpaned(props.item.id);
};

const handleOpenLabelSearch = () => {
  showLabelSearch(props.item.id);
};

const handleOpenAttributeCodeSearch = () => {
  showAttributeCodeSearch(props.item.id);
  commCodeSearchParams.value.page = 1;
  commCodeSearchParams.value.fieldType = props.item.fieldType;
  getListCommCode();
};
</script>
<style lang="scss" scoped>
.field-wrapper {
  width: calc(100% - 2px);
  min-height: 48px;
  border-radius: 16px;
  border: 1px solid #dce0e5;
  background: #ffffff;
  .field-header {
    background: #fff;
    padding: 12px;
    height: 48px;
    display: flex;
    align-items: center;
    column-gap: 8px;
    border-radius: 16px;
    position: relative;
    transition: all 0.2s linear;
    cursor: pointer;

    &.disabled {
      background-color: #e9ebf0;
    }

    &.expaned {
      border-radius: 16px 16px 0 0 !important;
      background: #f7f8fa;
    }

    .item-draggable {
      position: absolute;
      width: 14px;
      height: 22px;
      border-radius: 2px;
      left: -7px;
      top: 50%;
      transform: translateY(-50%);
      background-color: #e6e9ed;
      display: flex;
      align-items: center;
      cursor: grab;
    }
    .field-sort {
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background-color: #fff0f2;
      display: flex;
      justify-content: center;
      align-items: center;
      font-weight: 500;
      font-size: 11px;
      color: #ba1642;
    }
    .field-label {
      font-weight: 500;
      font-size: 13px;
      display: flex;
      align-items: center;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      flex: 1;
      color: #3a3b3d;
    }
    .field-button {
      width: 20px;
      height: 20px;
    }

    &__popover {
      flex-shrink: 0;
    }
  }

  .field-content {
    flex: 1;
    padding: 12px 16px;
    display: flex;
    flex-direction: column;
    row-gap: 12px;
    border-top: 1px solid #e6e9ed;
    border-radius: 0 0 16px 16px;
    .field-item {
      min-height: 32px;
      display: flex;
      column-gap: 8px;
      .field-label {
        flex: 0 0 104px;
        font-weight: 500;
        font-size: 13px;
        display: flex;
        align-items: center;
        color: #6b6d70;
        white-space: nowrap;
      }
      .field-value {
        flex: 1;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        display: flex;
        align-items: center;
        font-size: 13px;
        letter-spacing: 0.25px;
        color: #3a3b3d;

        &.is-active {
          box-shadow: 0px 0px 0px 4px #d9325a29;
          border-radius: 8px;
        }
      }
      .field-label-custom {
        flex: 0 0 auto;
        min-width: 104px;
        max-width: 130px;
        font-weight: 500;
        font-size: 13px;
        display: flex;
        align-items: center;
        color: #6b6d70;
      }
      .field-value-custom {
        display: flex;
        align-items: center;
        flex: 1;
        justify-content: flex-end;
        width: 36px;
        height: 32px;
      }
      .radio-field {
        transform: translateX(-4px);
      }
    }
    .field-item-custom {
      display: flex;
      align-items: center;
      column-gap: 8px;
      row-gap: 12px;
      justify-content: space-between;
      flex-wrap: wrap;
    }
    .field-custom {
      display: flex;
      flex: 1;
      justify-content: space-between;
      margin-left: 15px;
    }
  }
  .icon-search {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 32px;
  }
}
:deep(.v-switch--inset .v-switch__track) {
  height: 20px;
  min-width: 36px;
  opacity: 1;
}
:deep(.v-switch--inset .v-switch__thumb) {
  height: 16px;
  width: 16px;
  left: 2px;
  right: unset;
  transform: none;
}
:deep(.v-switch--inset .v-selection-control--dirty .v-switch__thumb) {
  left: unset;
  right: 2px;
}
//text field
:deep(.v-field__field) {
  height: 30px;
}
:deep(.v-field__input) {
  min-height: 0;
  height: 30px;
  padding: 0 16px;
}

:deep(.custom-text-field .v-field) {
  height: 30px;
  padding-right: 0;
}
:deep(.custom-text-field .v-input__control) {
  height: 30px;
}
:deep(.custom-text-field .v-field__append-inner:has(.icon-search)) {
  border-left: 1px solid #dce0e5;
}

// Radio button
:deep(.v-input__control) {
  width: 100%;
}

:deep(.v-selection-control-group .v-selection-control-group--inline) {
  justify-content: space-between;
  flex-wrap: wrap;
}
// :deep(.v-radio-group .v-input__control) {
//   height: 30px;
//   justify-content: center;
// }
// :deep(.v-selection-control-group) {
//   flex-direction: row;
//   flex-wrap: nowrap;
//   height: 30px;
// }
// :deep(.v-selection-control__wrapper) {
//   margin-right: 5px;
// }
:deep(.v-selection-control-group .v-label) {
  opacity: 1;
}
</style>
