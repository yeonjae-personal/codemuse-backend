<template>
  <div class="offers-section">
    <div class="w-[370px] h-[0px]">&nbsp;</div>
    <template v-if="mode === 'focused'">
      <GridFocusedDisplayMode
        :category-name="categoryName"
        :selected-item="selectedItem"
      />
    </template>
    <template v-else-if="mode === 'extended'">
      <GridExtendedDisplayMode
        :category-name="categoryName"
        :item-list="itemList"
        :selected-item="selectedItem"
      />
    </template>
    <div class="w-[370px] height-[5px]">&nbsp;</div>
  </div>
</template>

<script setup>
import GridFocusedDisplayMode from "./GridFocusedDisplayMode.vue";
import GridExtendedDisplayMode from "./GridExtendedDisplayMode.vue";

defineProps({
  mode: {
    type: String,
    default: "normal",
  },
  itemList: {
    type: Array,
    default: null,
  },
  categoryName: {
    type: String,
    default: "",
  },
  selectedItem: {
    type: Object,
    default: null,
  },
});
</script>
