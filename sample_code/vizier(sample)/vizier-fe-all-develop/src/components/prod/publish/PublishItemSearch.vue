<template>
  <SearchPane
    ref="searchPane"
    title="LB00000501"
    container-class="rounded-[12px]"
    :model-list="publishItemSearch.items"
    :type-select-require="false"
    :pagination="publishItemSearch.pagination"
    :pane-col="ColNumber.One"
    :item-height="82"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-change-page="handleChangePage"
  >
    <template #custom-form>
      <div class="grid gap-2 mt-2">
        <div class="filter gap-2">
          <BaseSelectScroll
            v-model="paramFilterPublishItemSearch.searchBy"
            :height="48"
            :options="PUBLISH_ITEM_CODE_TYPE"
            :show-error-massage="false"
            :default-item-select-all="false"
            :show-option-null="false"
          />
          <BaseInputSearch
            v-model="paramFilterPublishItemSearch.keyword"
            density="comfortable"
            label="search"
            variant="solo"
            hide-details
            single-line
            rounded="4"
            @handle-search="handleEnterSearch"
          />
        </div>
      </div>
    </template>
    <template #custom-search-item="{ item }">
      <CardPublishType
        :data="item"
        :actions="listActions(item)"
        :disable="checkDisable(item)"
        draggable
        in-search-mode
        @dragstart="handleDragStart($event, item)"
        @dragend="handleDragEnd"
      />
    </template>
  </SearchPane>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import {
  DRAG_PUBLISH_COMPOSE_ITEM_TYPE,
  PUBLISH_ITEM_CODE_TYPE,
} from "@/constants/publish";
import { usePublishManagerStore } from "@/store";
import { ComposeItem } from "@/interfaces/prod/publishInterface";
import useDragUserPocket from "@/composables/useDragUserPocket";
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import { ColNumber } from "@/enums";

const { t } = useI18n();
const publishManagerStore = usePublishManagerStore();
const { getPublishItemsSearch, resetParamPublishItemsSearch } =
  publishManagerStore;
const {
  paramFilterPublishItemSearch,
  publishItemSearch,
  dragItemType,
  composePackageItemList,
} = storeToRefs(publishManagerStore);
const { handleDragUserPocket } = useDragUserPocket();
const searchPane = ref();

const listActions = (item: ComposeItem) => [
  {
    name: t("product_platform.openinNewWindow"),
    icon: OpenInNewIcon,
    iconProps: {
      class: "text-text-lighter",
    },
    onClick: () => {
      if (handleRedirect) {
        handleRedirect(item);
      }
    },
  },
];

const handleSearch = async (size = 5, isClick = false, page) => {
  paramFilterPublishItemSearch.value.page = isClick ? 1 : page;
  paramFilterPublishItemSearch.value.size = size;
  await getPublishItemsSearch();
};

const handleResetSearch = () => {
  resetParamPublishItemsSearch();
};

const handleChangePage = async (page) => {
  paramFilterPublishItemSearch.value.page = page;
  await getPublishItemsSearch();
};

const handleDragStart = (event: DragEvent, item: any): void => {
  dragItemType.value = DRAG_PUBLISH_COMPOSE_ITEM_TYPE;
  handleDragUserPocket(event, item);
};

const handleDragEnd = () => {
  dragItemType.value = "";
};

const checkDisable = (item: ComposeItem) => {
  return (
    item.chngDataStusCode === "PACKED" ||
    composePackageItemList.value.some(
      (packItem) => packItem.chngDataCode === item.chngDataCode
    )
  );
};

const handleEnterSearch = () => {
  searchPane.value?.handleSearch();
};

const handleRedirect = inject<any>("handleRedirect");
</script>

<style lang="scss" scoped>
.filter {
  display: grid;
  grid-template-columns: 1fr 2fr;
}
</style>
