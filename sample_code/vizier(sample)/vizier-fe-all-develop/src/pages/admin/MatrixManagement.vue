<template>
  <FourColumns>
    <MatrixSearch
      v-if="isShowMatrixSearch"
      :is-shared="false"
      @on-close="handleCloseMatrixSearch"
    />
    <div v-if="matrixSelected" :class="[getWidth]">
      <MatrixContent />
    </div>
    <FactorSearch v-if="isShowFactorSearch" />
  </FourColumns>
</template>
<script lang="ts" setup>
import useMatrixStructureStore from "@/store/admin/matrixStructure.store";

const matrixStructureStore = useMatrixStructureStore();
const {
  matrixSelected,
  isShowMatrixSearch,
  isShowFactorSearch,
  builderFactorCols,
} = storeToRefs(matrixStructureStore);

const getWidth = computed(() => {
  // return isShowMatrixSearch ? 'col-span-3' : 'col-span-4'
  if (isShowMatrixSearch.value) {
    if (isShowFactorSearch.value) {
      return "col-span-2";
    }
    return "col-span-3";
  } else {
    if (isShowFactorSearch.value) {
      return "col-span-3";
    }
    return "col-span-4";
  }
});

const handleCloseMatrixSearch = () => {
  isShowMatrixSearch.value = false;
};

watch(
  () => getWidth.value,
  (val) => {
    switch (val) {
      case "col-span-2":
        builderFactorCols.value = 3;
        break;
      case "col-span-3":
        builderFactorCols.value = 5;
        break;
      case "col-span-4":
        builderFactorCols.value = 7;
        break;
    }
  }
);
</script>
