<template>
  <div class="filter gap-2">
    <BaseSelectScroll
      v-model="initSelectNameCode"
      :options="NM_CD_FIELDS"
      :height="48"
      :default-item-select-all="false"
      @update:model-value="handleChangeType"
    />
    <BaseInputSearch
      v-model="initInpup"
      density="comfortable"
      label="search"
      variant="solo"
      hide-details
      single-line
      rounded="4"
      @keyup.enter="handleSearch"
    />
  </div>
</template>

<script lang="ts" setup>
import { NM_CD_FIELDS } from "@/constants/";

const emit = defineEmits([
  "onSearch",
  "onChangeType",
  "update:prodItemNm",
  "update:prodItemCd",
  "update:selectValue",
  "update:selectNameCode",
]);

const props = defineProps({
  prodItemNm: {
    type: String,
    default: "",
  },
  prodItemCd: {
    type: String,
    default: "",
  },
  selectNameCode: {
    type: String,
    default: "name",
  },
});

const initSelectNameCode = computed({
  get() {
    return props.selectNameCode;
  },
  set(val) {
    emit("update:selectNameCode", val);
  },
});

const initInpup = computed({
  get() {
    if (initSelectNameCode.value === NM_CD_FIELDS[0].value) {
      return props.prodItemNm;
    } else {
      return props.prodItemCd;
    }
  },
  set(val) {
    if (initSelectNameCode.value === NM_CD_FIELDS[0].value) {
      emit("update:prodItemNm", val);
      emit("update:prodItemCd", undefined);
    } else {
      emit("update:prodItemCd", val);
      emit("update:prodItemNm", undefined);
    }
  },
});

const handleSearch = () => {
  emit("onSearch");
};

const handleChangeType = (value: string): void => {
  emit("onChangeType", value);
};
</script>

<style scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
