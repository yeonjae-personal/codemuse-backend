<template>
  <div class="flex gap-[8px]">
    <BaseSelectScroll
      v-model="selectDetlType"
      :options="detailList"
      :placeholder="$t('product_platform.type')"
      :height="48"
      default-item-select-all
      class="w-full"
    />
    <BaseSelectScroll
      v-model="selectSubType"
      :options="selectDetailTypeItems"
      :placeholder="$t('product_platform.sub_type')"
      :height="48"
      default-item-select-all
      class="w-full"
    />
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["update:detlType", "update:subType"]);
const props = defineProps({
  detlType: {
    type: String,
    default: () => {},
  },
  subType: {
    type: String,
    default: () => {},
  },
  detailList: {
    type: Array,
    default: () => [],
  },
});
const selectDetlType = computed({
  get() {
    return props.detlType;
  },
  set(val) {
    emit("update:detlType", val);
  },
});
const selectSubType = computed({
  get() {
    return props.subType;
  },
  set(val) {
    emit("update:subType", val);
  },
});
const selectDetailTypeItems = computed(() => {
  let list: any = props.detailList?.find(
    (item: any) => item.value === selectDetlType.value
  );
  return list?.subOptions || [];
});
</script>
