<template>
  <SearchPane
    ref="searchPaneMatrix"
    title="product_platform.matrixSearch"
    container-class="rounded-lg col-span-1"
    :pane-col="ColNumber.One"
    :item-height="88"
    :model-param="paramMatrixSearch"
    :model-list="listMatrix"
    :pagination="matrixSearchPagination"
    :show-float-icon-left="isEdit || isShared"
    :icon-left-class="'right-0'"
    @on-search="handleSearch"
    @on-reset="handleResetSearch"
    @on-change-page="handleChangePage"
    @on-click-float-left="onClose"
  >
    <template #search-button-append>
      <BaseButton
        v-if="!isShared"
        :color="ButtonColorType.Secondary"
        class="ml-2"
        @click="handleCreateMatrix"
      >
        <AddLabelIcon class="mr-[6px]" />
        {{ $t("product_platform.commonAdmin.create") }}
      </BaseButton>
    </template>
    <template #custom-form>
      <BaseInputSearch
        v-model="paramMatrixSearch.matrixCodeName"
        density="comfortable"
        label="matrixSearch"
        variant="solo"
        hide-details
        single-line
        rounded="4"
        class="mt-2"
        @keyup.enter="handleEnterSearch"
      />
    </template>
    <template #custom-search-item="{ item }">
      <MatrixItem
        :data="item"
        :active="item?.matrixCode === matrixSelected?.matrixCode"
        :search-text="paramMatrixSearch.matrixCodeName"
        :is-new="item?.isNew"
        :disable-input="isBuilder"
        :draggable="isShared"
        @on-click="handleClickMatrix"
        @update:matrix-code-name="handleInputMatrixNew($event, item)"
        @drag-start="(event) => handleDragStart(event, item)"
        @drag-end="handleDragEnd"
      />
    </template>
  </SearchPane>
  <base-popup
    v-model="openPopupCancel"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.updatingConfirmSaved')"
    @on-close="handleClosePopup"
    @on-submit="handleCancel"
  />
  <base-popup
    v-model="openPopupConfirm"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.updatingConfirmSaved')"
    @on-close="
      () => {
        openPopupConfirm = false;
      }
    "
    @on-submit="handleConfirm"
  />
</template>

<script setup lang="ts">
import useMatrixStructureStore from "@/store/admin/matrixStructure.store";
import { ButtonColorType, ColNumber, DialogIconType } from "@/enums";
import { v4 as uuidv4 } from "uuid";
import {
  useDragStore,
  useMatrixSearchPaneStore,
  useSnackbarStore,
} from "@/store";
import { useI18n } from "vue-i18n";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { TYPE_CATEGORY_OB_DRAG } from "@/constants/index";

defineOptions({
  inheritAttrs: false,
});

const emit = defineEmits(["onClose"]);
const props = defineProps({
  isShared: {
    type: Boolean,
    default: true,
  },
});

const { dragOfferType, isDragging, categoryDrag } = storeToRefs(useDragStore());
const matrixStructureStore = useMatrixStructureStore();
const matrixSearchPaneStore = useMatrixSearchPaneStore();
const useSnackbar = useSnackbarStore();
const { t } = useI18n();
const {
  isCreate,
  isEdit,
  isBuilder,
  matrixBuilderFactors,
  listTableMatrix,
  listTableMatrixTemp,
  headersTableMatrix,
} = storeToRefs(matrixStructureStore);
const { getHeaderTableMatrix, getListTableMatrix, resetDataTableMatrix } =
  matrixStructureStore;
const { handleDragUserPocket } = useDragUserPocket();

const selectedStore = computed(() => {
  if (props.isShared) {
    return matrixSearchPaneStore;
  }
  return matrixStructureStore;
});

const {
  paramMatrixSearch,
  listMatrix,
  matrixSelected,
  matrixSearchPagination,
  listTempItems,
} = storeToRefs(selectedStore.value);
const { getListMatrix, resetParamMatrixSearch } = selectedStore.value;
const openPopupCancel = ref(false);
const openPopupConfirm = ref(false);
const isSearchNew = ref(false);
const isReset = ref(false);
const changePaging = ref<any>(null);
const tempItemSelected = ref<any>(null);
const changeActionEditToCreate = ref(false);
const searchPaneMatrix = ref();

const handleEnterSearch = () => {
  if (searchPaneMatrix.value) {
    searchPaneMatrix.value?.handleSearch?.();
  }
};

const handleClickMatrix = async (event) => {
  if (props.isShared) {
    matrixSelected.value = event;
  } else {
    if (isBuilder.value) {
      useSnackbar.showSnackbar(
        t("product_platform.pleaseFinishBuild"),
        "error"
      );
      return;
    }
    if (event?.matrixCode === matrixSelected.value?.matrixCode) {
      return;
    }

    if (isEdit.value || (isCreate.value && !event?.isNew)) {
      openPopupConfirm.value = true;
      tempItemSelected.value = event;
      return;
    }
    matrixBuilderFactors.value = [];
    listTableMatrix.value = [];
    listTableMatrixTemp.value = [];
    headersTableMatrix.value = [];
    listTempItems.value = [];
    resetDataTableMatrix();
    matrixSelected.value = event;
    await getHeaderTableMatrix(event.matrixCode);
    await getListTableMatrix(event.matrixCode, { builderDtos: null });
  }
};

const handleConfirm = async () => {
  matrixBuilderFactors.value = [];
  listTableMatrix.value = [];
  listTableMatrixTemp.value = [];
  headersTableMatrix.value = [];
  matrixSelected.value = tempItemSelected.value;
  await getHeaderTableMatrix(tempItemSelected.value?.matrixCode);
  await getListTableMatrix(tempItemSelected.value?.matrixCode, {
    builderDtos: null,
  });
  openPopupConfirm.value = false;
  isEdit.value = false;
  if (isCreate.value) {
    await handleResponse();
    isCreate.value = false;
  }
  if (changeActionEditToCreate.value) {
    await createNewMatrix();
    changeActionEditToCreate.value = false;
  }
};

const onClose = () => {
  emit("onClose");
};

const handleCancel = async () => {
  if (isSearchNew.value) {
    await handleResponse();
  } else if (isReset.value) {
    resetParamMatrixSearch();
    await handleResponse();
  } else if (changePaging.value) {
    paramMatrixSearch.value.page = changePaging.value;
    await handleResponse();
  }
  openPopupCancel.value = false;
  isCreate.value = false;
};

const handleClosePopup = () => {
  if (changePaging.value) {
    matrixSearchPagination.value.currentPage = paramMatrixSearch.value.page;
    changePaging.value = null;
  }
  openPopupCancel.value = false;
};

const handleSearch = async (size, isClick = false, page = 1) => {
  if (size) {
    paramMatrixSearch.value.size = size;
    matrixSearchPagination.value.pageSize = size;
  }
  paramMatrixSearch.value.page = isClick ? 1 : page;
  if (props.isShared) {
    await handleResponse();
  } else {
    if (isBuilder.value) {
      useSnackbar.showSnackbar(
        t("product_platform.pleaseFinishBuild"),
        "error"
      );
      return;
    }
    if (isCreate.value) {
      openPopupCancel.value = true;
      isSearchNew.value = true;
      return;
    }
    await handleResponse();
  }
};

const handleResetSearch = async () => {
  if (props.isShared) {
    resetParamMatrixSearch();
  } else {
    if (isBuilder.value) {
      useSnackbar.showSnackbar(
        t("product_platform.pleaseFinishBuild"),
        "error"
      );
      return;
    }
    if (isCreate.value) {
      openPopupCancel.value = true;
      isReset.value = true;
      return;
    }
    isEdit.value = false;
    resetParamMatrixSearch();
  }
};

const handleResponse = async () => {
  await getListMatrix();
};

const handleCreateMatrix = async () => {
  if (isBuilder.value) {
    useSnackbar.showSnackbar(t("product_platform.pleaseFinishBuild"), "error");
    return;
  }
  if (isEdit.value && matrixSelected.value) {
    changeActionEditToCreate.value = true;
    openPopupConfirm.value = true;
    return;
  }
  if (isCreate.value) {
    return;
  } else {
    createNewMatrix();
  }
};

const createNewMatrix = async () => {
  await handleChangePage(1);
  const newMatrix = {
    matrixCodeName: "",
    matrixCode: uuidv4(),
    isNew: true,
  };
  listMatrix.value.unshift(newMatrix);
  listMatrix.value.pop();
  matrixSelected.value = newMatrix;
  isCreate.value = true;
  isEdit.value = true;
  matrixBuilderFactors.value = [];
  listTableMatrix.value = [];
  listTableMatrixTemp.value = [];
  headersTableMatrix.value = [];
  resetDataTableMatrix();
  await getHeaderTableMatrix();
};

const handleChangePage = async (page) => {
  if (isBuilder.value) {
    useSnackbar.showSnackbar(t("product_platform.pleaseFinishBuild"), "error");
    return;
  }
  if (isCreate.value) {
    openPopupCancel.value = true;
    changePaging.value = page;
    return;
  }
  paramMatrixSearch.value.page = page;
  await handleResponse();
};
const handleInputMatrixNew = (event, item) => {
  item.matrixCodeName = event;
};

const handleDragStart = (event: any, item: any): void => {
  event.target.classList?.remove("zoom-animation");
  dragOfferType.value = COLUMN_FIELD_TYPE.OB;
  categoryDrag.value = TYPE_CATEGORY_OB_DRAG.MATRIX;
  isDragging.value = true;
  handleDragUserPocket(event, {
    userPocketType: COLUMN_FIELD_TYPE.OB,
    ...item,
  });
};

const handleDragEnd = (event) => {
  event.target.classList?.add("zoom-animation");

  isDragging.value = false;
  dragOfferType.value = "";
  categoryDrag.value = "";
};
</script>

<style lang="scss" scoped></style>
