<template>
  <div class="flex px-[22px] gap-[24px] mx-auto relative" style="z-index: 1">
    <div
      class="description-panel"
      :class="{ 'panel-group': category === EXTENDS_VIEW.DETAIL }"
    >
      <p class="description-title">
        {{ $t("product_platform.relation.leaderTitle") }}
      </p>
    </div>
    <div
      class="description-panel active-panel"
      :class="{ '!min-w-[376px]': category === EXTENDS_VIEW.DETAIL }"
    >
      <p class="description-title">
        {{ $t("product_platform.relation.leaderFollowerTitle") }}
      </p>
    </div>
    <div
      class="description-panel"
      :class="{ 'panel-group': category === EXTENDS_VIEW.DETAIL }"
    >
      <p class="description-title">
        {{ $t("product_platform.relation.followerTitle") }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { EXTENDS_VIEW } from "@/constants/extendsManager";

defineProps({
  category: {
    type: String,
    default: "",
  },
});
</script>

<style lang="scss" scoped>
.description-panel {
  min-width: 400px;
  padding: 0px 12px;
  background-color: #f7f8fa;
  border-radius: 0px 0px 12px 12px;
  box-shadow: 0px 2px 16px 0px #0000001f;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.active-panel {
  background: #ba1642;
  .description-title {
    color: #fff !important;
  }
}
.panel-group {
  min-width: 632px;
}
</style>
