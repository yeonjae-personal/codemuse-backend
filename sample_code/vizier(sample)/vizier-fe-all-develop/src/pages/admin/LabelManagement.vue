<template>
  <FourColumns>
    <LabelSearch />
    <LabelDetail v-if="isShowDetailLabel" />
  </FourColumns>
</template>

<script lang="ts" setup>
import isEmpty from "lodash-es/isEmpty";
import useLabelStore from "@/store/admin/label.store";
import LabelDetail from "./subs/label/LabelDetail.vue";
import LabelSearch from "./subs/label/LabelSearch.vue";

const { selectedLabel } = storeToRefs(useLabelStore());
const { getListLanguageLabel } = useLabelStore();

const isShowDetailLabel = computed(() => !isEmpty(selectedLabel.value));

onBeforeMount(() => {
  getListLanguageLabel();
});
</script>

<style lang="scss" scoped></style>
