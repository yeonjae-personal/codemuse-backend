<template>
  <div class="tree-view-action-section">
    <div class="action-item single-btn-group">
      <BaseButton :color="ButtonColorType.Gray" @click="setCollapseAll">
        {{ $t("product_platform.collapseAll") }}
      </BaseButton>
      <BaseButton :color="ButtonColorType.Gray" @click="setExpandAll">
        {{ $t("product_platform.expandAll") }}
      </BaseButton>
    </div>
  </div>
</template>

<script setup>
import { ButtonColorType } from "@/enums";

const emit = defineEmits(["setCollapseAll", "setExpandAll", "setNodeSize"]);
const setCollapseAll = () => {
  emit("setCollapseAll", true);
};

const setExpandAll = () => {
  emit("setExpandAll");
};
</script>

<style scoped>
.tree-view-action-section {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
  margin-top: 5px;
  padding-left: 12px;
}

.single-btn-group {
  display: flex;
  gap: 8px;
}

.tree-action-button {
  background: #fff;
  color: #6b6d70;
  text-transform: unset !important;
  font-size: 13px;
  font-weight: 500;
  line-height: 13px;
  letter-spacing: 0.5px;
  text-align: left;
}

.multiple-btn-group {
  display: flex;
  position: relative;
}

.multiple-btn-group::before {
  position: absolute;
  width: 1px;
  background-color: #dce0e5;
  height: 24px;
  left: -8px;
  content: "";
  top: 25%;
}

.multiple-btn-grp-item:first-child {
  border-top-left-radius: 20px;
  border-bottom-left-radius: 20px;
}

.multiple-btn-grp-item:last-child {
  border-top-right-radius: 20px;
  border-bottom-right-radius: 20px;
}

.active-node-size {
  color: #ba1642;
}

.center-action-btn {
  border-left: none !important;
  border-right: none !important;
}
</style>
