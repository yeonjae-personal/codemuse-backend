<template>
  <v-row class="gap-2 align-center" no-gutters>
    <v-col style="max-width: 240px">
      <BaseSelectScroll
        ref="selectScroll"
        v-model="gridViewParams.category"
        :options="SELECT_LIST_DETAIL"
        class-name="form-item w-full text-[13px]"
        :placeholder="$t('product_platform.type')"
        default-item-select-all
        :height="48"
        @update:model-value="handleChangeItem"
      />
    </v-col>
    <v-col style="max-width: 120px">
      <BaseSelectScroll
        v-model="gridViewParams.type"
        :options="NM_CD_FIELDS"
        class-name="w-full"
        :height="48"
        :default-item-select-all="false"
        @update:model-value="handleChangeType"
      />
    </v-col>
    <v-col style="max-width: 240px">
      <BaseInputSearch
        v-model="gridViewParams.value"
        class="w-[240px]"
        density="comfortable"
        label="search"
        variant="solo"
        hide-details
        single-line
        rounded="4"
        @keyup.enter="handleFetchDataTable"
      />
    </v-col>
    <SearchAndRefreshButton
      @handle-search="handleFetchDataTable"
      @handle-refresh="handleResetValue"
    />
  </v-row>
</template>

<script setup lang="ts">
import cloneDeep from "lodash-es/cloneDeep";
import { useExtendManagerStore } from "@/store";
import {
  GRID_PARAMS_DEFAULT,
  SELECT_LIST_DETAIL,
} from "@/constants/extendsManager";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import { SPACE } from "@/constants/index";

const { paramListView, listView, selectedItem, isResetSelectTable } =
  storeToRefs(useExtendManagerStore());
const { getRelationDataTable } = useExtendManagerStore();

const gridViewParams = inject("gridViewParams", {
  category: SPACE,
  value: "",
  type: "name",
});

const selectScroll = ref();

watch(isResetSelectTable, (value) => {
  if (value) {
    resetValidateSelect();
    nextTick(() => {
      isResetSelectTable.value = false;
    });
  }
});

onBeforeMount(() => {
  gridViewParams.category = paramListView.value.category;
  gridViewParams.value = paramListView.value.value;
  gridViewParams.type = paramListView.value.type;
});

const validateSelect = (): void => {
  selectScroll.value?.validate();
};

const resetValidateSelect = (): void => {
  selectScroll.value?.resetValidate();
};

defineExpose({ validateSelect, resetValidateSelect });

const handleFetchDataTable = async (): Promise<void> => {
  paramListView.value.page = 1;
  if (!selectedItem.value || !selectedItem.value?.prodUuid) return;
  paramListView.value.category = gridViewParams.category;
  paramListView.value.type = gridViewParams.type;
  paramListView.value.value = gridViewParams.value;
  await getRelationDataTable();
};

const handleResetValue = (): void => {
  paramListView.value = cloneDeep(GRID_PARAMS_DEFAULT);
  paramListView.value.uuid = selectedItem.value.prodUuid;
  listView.value.items = [];
  gridViewParams.category = SPACE;
  gridViewParams.value = "";
  gridViewParams.type = "name";
  if (!selectedItem.value || !selectedItem.value?.prodUuid) return;
  getRelationDataTable();
};

const handleChangeItem = (value: string): void => {
  gridViewParams.category = value === SPACE ? " " : value;
  gridViewParams.value = "";
  paramListView.value.page = 1;
  paramListView.value.size = 10;
};

const handleChangeType = (_value: string): void => {
  gridViewParams.value = "";
};
</script>

<style lang="scss" scoped>
:deep(.highlight) {
  background-color: yellow !important;
}
</style>
