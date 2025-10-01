<template>
  <SearchPane
    ref="searchPaneRef"
    title="product_platform.label_search"
    container-class="rounded-lg"
    :pane-col="ColNumber.Two"
    :item-height="62"
    :pagination="pagination"
    :model-list="listLabel || []"
    @on-search="handleSearch"
    @on-change-page="handleChangePage"
  >
    <template #custom-header-search-pane>
      <div class="label-search-header px-6">
        <h3 class="label-search-header__title">
          {{ t("product_platform.label_search") }}
        </h3>
        <LabelAction />
      </div>
      <LabelSearchFilter />
    </template>
    <template #custom-search-item="{ item }">
      <div @click="handleSelectLabel(item)">
        <LabelItem
          :item="item"
          :search-type-obj="{
            field: searchParams.type,
            value: searchParams.value,
            keysCheck: {
              code: LABEL_SEARCH_TYPE.CODE,
              name: LABEL_SEARCH_TYPE.NAME,
            },
          }"
        />
      </div>
    </template>
  </SearchPane>
  <BasePopup
    v-if="isOpenPopup"
    v-model="isOpenPopup"
    :icon="DialogIconType.Warning"
    :submit-button-text="t('product_platform.btn_yes')"
    :cancel-button-text="t('product_platform.btn_no')"
    :content="t('product_platform.desc_cancel')"
    @on-submit="handleSubmit"
    @on-close="handleClosePopup"
  />
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import useLabelStore from "@/store/admin/label.store";
import { ColNumber, DialogIconType } from "@/enums";
import LabelAction from "./LabelAction.vue";
import LabelSearchFilter from "./LabelSearchFilter.vue";
import LabelItem from "./LabelItem.vue";
import SearchPane from "@/components/prod/shared/SearchPane.vue";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import { ILabelItem } from "@/interfaces/admin/label-management";

const { t } = useI18n();
const { searchParams, getListLabel } = useLabelStore();
const {
  listLabel,
  listLabelTemp,
  selectedLabel,
  isOpenPopup,
  isEditing,
  isAddNew,
  pagination,
  componentKey,
} = storeToRefs(useLabelStore());

const searchPaneRef = ref<InstanceType<typeof SearchPane>>();

onMounted(() => {
  if (listLabel.value.length === 0) {
    searchPaneRef.value?.calcTotalItem();
    searchParams.size = searchPaneRef.value?.totalItem || 14;
    getListLabel();
  }
});

const handleSearch = async (
  pageSize?: number,
  isClick: false,
  page = 1
): Promise<void> => {
  searchParams.size = pageSize || 14;
  searchParams.page = isClick ? 1 : page;
  getListLabel();
};

const handleChangePage = async (page: number): Promise<void> => {
  if (isEditing.value || isAddNew.value) {
    isOpenPopup.value = true;
    return;
  }
  searchParams.page = page;
  await getListLabel(true);
};

const handleSubmit = (): void => {
  if (isAddNew.value) {
    if (listLabel.value.length === 14) {
      listLabel.value = listLabelTemp.value;
    } else {
      listLabel.value.shift();
    }
    isAddNew.value = false;
  }
  isEditing.value = false;
  selectedLabel.value = null;
  isOpenPopup.value = false;
};

const handleClosePopup = (): void => {
  isOpenPopup.value = false;
};

const handleSelectLabel = (label: ILabelItem): void => {
  if (isEditing.value || isAddNew.value) {
    isOpenPopup.value = true;
    return;
  }

  if (label.labelId !== selectedLabel.value?.labelId) {
    componentKey.value++;
    selectedLabel.value = cloneDeep(label);
  } else {
    selectedLabel.value = null;
  }

  isEditing.value = false;
};
</script>

<style lang="scss" scoped>
.label-search {
  width: 100%;
  height: 100%;
  background-color: #fff;
  position: relative;
  padding: 24px;
  border-radius: 12px;
}

.label-search-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;

  &__title {
    font-weight: 500;
    font-size: 15px;
    line-height: 150%;
    letter-spacing: 0.5%;
  }
}
</style>
