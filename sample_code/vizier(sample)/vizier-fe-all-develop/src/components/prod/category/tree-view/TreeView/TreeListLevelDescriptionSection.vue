<template>
  <div v-if="levelDescription.length > 0" class="flex ml-[24px] gap-[24px]">
    <template
      v-for="({ primaryText, subText }, index) in levelDescription"
      :key="index"
    >
      <template v-if="index < 2">
        <ExpansionPanel
          :title="$t(`product_platform.Level`) + ` ${index + 1}`"
          :primary-text="primaryText"
          :sub-text="subText ?? ''"
          header-bg-color="#BA1642"
          header-txt-color="#FFFFFF"
          panel-width="240px"
          :level="index + 1"
          @set-value-text-desc="setDescByLevel"
        />
      </template>
      <template v-else-if="index < levelDescription.length">
        <ExpansionPanel
          :title="$t(`product_platform.Level`) + ` ${index + 1}`"
          :primary-text="primaryText"
          :sub-text="subText ?? ''"
          header-bg-color="#F7F8FA"
          header-txt-color="#6B6D70"
          panel-width="240px"
          :level="index + 1"
          @set-value-text-desc="setDescByLevel"
        />
      </template>
    </template>
    <div>&nbsp;</div>
  </div>
</template>

<script setup>
import { useCategoryStore } from "@/store";
import ExpansionPanel from "../ExpansionPanel.vue";

let dataDesc = {};
const props = defineProps({
  levelDescription: {
    type: Array,
    require: true,
    default: () => [],
  },
});
const categoryStore = useCategoryStore();

const setDescByLevel = ({ level, value }) => {
  props.levelDescription.forEach((item, index) => {
    if (index + 1 === level) {
      dataDesc[`level${level}`] = value;
    } else {
      dataDesc[`level${index + 1}`] = dataDesc[`level${index + 1}`]
        ? dataDesc[`level${index + 1}`]
        : item.primaryText.concat("\r\n", item.subText);
    }
  });
  categoryStore.setDescriptionData(dataDesc);
};
</script>
