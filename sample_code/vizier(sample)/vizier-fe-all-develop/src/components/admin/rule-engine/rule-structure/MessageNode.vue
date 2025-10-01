<template>
  <div id="message-node" :class="['message-node', { 'is-success': passed }]">
    <div
      ref="messageRef"
      class="message-node__title"
      @click="handleToggleMessage"
    >
      {{ t("product_platform.message") }}
    </div>
    <!-- Message -->
    <div v-if="isShowMessage" ref="messageMenuRef" class="message-node__menu">
      <BaseInputText
        v-model="ruleMsg"
        :placeholder="t('product_platform.message')"
        :readonly="!isEditRuleStructure"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";

const { isEditRuleStructure, ruleMsg, passed } =
  storeToRefs(useRuleEngineStore());

const { t } = useI18n();
const messageMenuRef = ref<HTMLElement | null>(null);
const messageRef = ref<HTMLElement | null>(null);
const isShowMessage = ref<boolean>(false);

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});

const handleClickOutside = (event: Event): void => {
  if (
    messageMenuRef.value &&
    messageRef.value &&
    !messageMenuRef.value.contains(event.target as Node) &&
    !messageRef.value.contains(event.target as Node)
  ) {
    isShowMessage.value = false;
  }
};

const handleToggleMessage = (): void => {
  isShowMessage.value = !isShowMessage.value;
};
</script>

<style lang="scss" scoped>
.message-node {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 71px;
  margin-top: 1px;
  background-color: #fff0f2;
  border: 1px solid #d9325a;
  border-radius: 99px;
  padding: 8px;
  transition: all 0.2s linear;
  cursor: pointer;

  &::before {
    content: "";
    position: absolute;
    top: -8px;
    left: 50%;
    transform: translateX(-50%);
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 8px solid #bdc1c7;
  }

  &.is-success {
    &::before {
      border-top-color: #17b26a;
    }
  }

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #ba1642;
  }

  &__menu {
    position: absolute;
    left: 50%;
    top: -76px;
    width: 400px;
    padding: 12px;
    transform: translateX(-50%);
    border-radius: 12px;
    background-color: #fff;
    box-shadow:
      0px 18px 88px -4px #18274b1f,
      0px 8px 32px -6px #18274b0f;
    z-index: 99;
  }
}
</style>
