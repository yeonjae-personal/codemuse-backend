<template>
  <div class="add-buttons">
    <button @click.stop="handleAddMemo">
      <CustomTooltip :content="$t('product_platform.addMemo')" is-always-show>
        <span class="flex items-center justify-center">
          <PlusSmallIcon /> {{ $t("product_platform.memo") }}
        </span>
      </CustomTooltip>
    </button>
    <button @click.stop="handleAddCustomValidation">
      <CustomTooltip
        :content="$t('product_platform.addValidation')"
        is-always-show
      >
        <span class="flex items-center justify-center">
          <PlusSmallIcon /> {{ $t("product_platform.validation") }}
        </span>
      </CustomTooltip>
    </button>
  </div>
</template>
<script setup>
import PlusSmallIcon from "@/components/prod/icons/PlusSmallIcon.vue";
import customValidationStore from "@/store/admin/customValidation.store";

const props = defineProps({
  index: {
    type: Number,
    default: 0,
  },
});

const { addCustomValidationItem } = customValidationStore();
const handleAddMemo = () => {
  addCustomValidationItem(props.index, "memo");
};
const handleAddCustomValidation = () => {
  addCustomValidationItem(props.index, "validation");
};
</script>
<style lang="scss" scoped>
.add-buttons {
  visibility: hidden;
  display: flex;
  gap: 1px;
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  > button {
    background: #f7f8fa;
    color: #6b6d70;
    font-size: 11px;
    font-weight: 500;
    height: 20px;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 2px 6px;
    box-shadow: 0px 2px 6px 0px #00000014;
    border-radius: 0 0 2px 2px;
    &:hover {
      background: #fff;
    }
  }
}
</style>
