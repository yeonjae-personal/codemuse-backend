<template>
  <FourColumns>
    <TableTypeSearch v-if="isShowTableTypeSearch" />
    <TableTypeDetails v-if="isShowTableTypeDetail" />
    <div v-if="tableSelected" :class="[getWidth]">
      <TableContent ref="tableContent" />
    </div>
  </FourColumns>
</template>
<script lang="ts" setup>
import useTableStructureStore from "@/store/admin/tableStructure.store";

const tableStructureStore = useTableStructureStore();
const { isShowTableTypeSearch, isShowTableTypeDetail, tableSelected } =
  storeToRefs(tableStructureStore);
const tableContent = ref<HTMLElement | any>();

const getWidth = computed(() => {
  // return isShowMatrixSearch ? 'col-span-3' : 'col-span-4'
  if (isShowTableTypeSearch.value) {
    if (isShowTableTypeDetail.value) {
      return "col-span-2";
    }
    return "col-span-3";
  } else {
    if (isShowTableTypeDetail.value) {
      return "col-span-3";
    }
    return "col-span-4";
  }
});

watch(
  () => [isShowTableTypeSearch.value, isShowTableTypeDetail.value],
  () => {
    if (tableContent.value) {
      nextTick(() => {
        tableContent.value?.calcColumnAdd();
      });
    }
  }
);
</script>
