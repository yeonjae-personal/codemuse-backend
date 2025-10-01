<template>
  <div class="rounded-lg border border-[#666] h-full bg-white grid grid-cols-4">
    <div class="relative z-10 rounded-lg min-h-[300px] col-span-1">
      <div class="pt-6 pb-3 rounded h-full flex flex-col">
        <div class="w-full flex justify-between items-center px-6 pb-2">
          <div class="text-text-base text-base-vnb font-medium leading-[40px]">
            {{ $t("product_platform.duplicateOffer") }}
          </div>
        </div>
        <template v-if="viewMode === VIEW_MODE.GRID">
          <div v-if="computedProducts.length > 0">
            <div class="flex flex-column gap-10">
              <template
                v-for="element in computedProducts"
                :key="element.objUUID"
              >
                <div
                  v-if="element?.isDuplicatedItem"
                  class="flex flex-column gap-2 px-6"
                >
                  <p data-v-267bee53="" class="list-description-title">
                    Parent
                  </p>
                  <CardItem
                    :item="element"
                    :search-text="inputText"
                    :search-field="selectedValue"
                    :offer-type="element.itemCode"
                    :actions="listActions(element)"
                    :expired="isExpired(element?.validEndDtm)"
                    :is-show-detail-infor="false"
                    :code-cmcd-list="codeCmcdList"
                    :selected-product="selectedProduct"
                    is-duplicate
                    is-show-actions
                  />
                </div>
                <div v-else-if="element?.duplicateItem" class="px-6">
                  <CardItem
                    class="duplicate-item"
                    :item="element"
                    :search-text="inputText"
                    :search-field="selectedValue"
                    :offer-type="element.itemCode"
                    :expired="isExpired(element?.validEndDtm)"
                    :is-show-detail-infor="false"
                    :code-cmcd-list="codeCmcdList"
                    :selected-product="selectedProduct"
                    is-duplicate
                  />
                </div>
              </template>
              <LocomotiveComponent
                scroll-container-class="max-h-[calc(100vh-470px)] !px-0"
                scroll-content-class="flex flex-column flex-grow-1 gap-4 px-6 py-2"
                top-content-class="z-10 ml-6 mr-3 pb-2 bg-white"
                is-stop-propagation-wheel
              >
                <template #top-content-fixed>
                  <p data-v-267bee53="" class="list-description-title">
                    Siblings
                  </p>
                </template>
                <template
                  v-for="item in isDuplicatedItemSibling"
                  :key="item.prodUuid"
                >
                  <CardItem
                    :item="item"
                    :search-text="inputText"
                    :search-field="selectedValue"
                    :offer-type="selectedProduct.itemCode"
                    :is-show-detail-infor="false"
                    :code-cmcd-list="codeCmcdList"
                    :actions="listActions(item)"
                    :selected-product="selectedProduct"
                    is-duplicate
                    is-show-actions
                  />
                </template>
              </LocomotiveComponent>
            </div>
          </div>
          <NoData v-else />
        </template>
      </div>
    </div>
    <div v-if="showProductDetail" class="rounded-lg col-span-3">
      <ProductStructure
        :list-structure="structureData"
        :product-detail="productDetails"
        :screen="screenValue"
        :type="selectedProduct?.itemCode"
        duplicate
        @close-detail="closeDetail"
        @view-detail="viewDetail"
        @view-detail-duplicate="viewDetailDuplicate"
        @refresh-page="refreshPage"
        @duplicate-detail-success="duplicateSuccess"
        @duplicate-component-success="duplicateComponentSuccess"
        @handle-click-drop="handleClickDrop"
        @cancel-duplicate-detail="handleCancelDuplicate"
      />
    </div>
  </div>
  <BasePopup
    v-model="openPopupDuplicate"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.desc_exit_mode_duplicate')"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupExitDuplicate"
    @on-submit="handleExitDuplicate"
  />
  <BasePopup
    v-model="openPopupModifiedStructure"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.modified_structre')"
    :icon="DialogIconType.Warning"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupModifiedStructure"
    @on-submit="handlePopupModifiedStructure"
  />
</template>

<script setup lang="ts">
//THIRD-IMPORT
import { useI18n } from "vue-i18n";
import cloneDeep from "lodash-es/cloneDeep";
import moment from "moment";

//PROJECT-IMPORT
import {
  ACTION_TYPE,
  DATE_FORMAT,
  DETAIL_TAB_TYPE,
  VIEW_MODE,
} from "@/constants/";
import {
  useSnackbarStore,
  useExtendCreateStore,
  useMenuStore,
  useOfferDuplicateProcessStore,
  useCategoryStore,
  useProductsDuplicateStore,
  useDuplicateStructureStore,
} from "@/store";
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import { isExpired } from "@/utils/format-data";
import { ActionType } from "@/interfaces/prod";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { DialogIconType, LargeItemCode } from "@/enums";
import { getUserInfor } from "@/constants/userInfor";

//components
import OpenInNewIcon from "@/components/prod/icons/OpenInNewIcon.vue";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { getUiOfferAdvanced } from "@/api/prod/offerApi";
import customValidationStore from "@/store/admin/customValidation.store";
import { DETAIL_COMPONENT_NAME } from "@/constants/index";
import { configPath, findMenuItem } from "@/utils/config-path";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { MenuItemID } from "@/enums/redirect";
import useRedirect from "@/composables/useRedirect";
import { isEmpty } from "lodash-es";

const { moveOfferSearchPage } = useRedirect();
const { t } = useI18n();
const userInfor = getUserInfor();
const { setTotalAdditional } = customValidationStore();
const productStore = useProductsDuplicateStore();
const categoryStore = useCategoryStore();
const structureStore = useDuplicateStructureStore();
const snackbarStore = useSnackbarStore();
const { newOfferTabItem } = storeToRefs(useExtendCreateStore());
const { menuTree } = storeToRefs(useMenuStore());
const categoryOfferCreateStore = useOfferCreateProcessStore();
const {
  offerDuplicateInGroupMode,
  offerBeClonedUuid,
  offerDuplicated,
  groupsFinish,
} = storeToRefs(useOfferDuplicateProcessStore());
const {
  pagination,
  products,
  productsDuplicate,
  productDetails,
  productType,
  showProductDetail,
  viewMode,
  selectedProduct,
  inputText,
  selectedValue,
  itemTypeSelected,
  productLineCoordinates,
  itemsType,
  isDuplicatedItemSibling,
} = storeToRefs(productStore);

const {
  offerCreateInCategoryMode,
  pathCategorySelected,
  categoryDuplicateModeSelected,
  categoryEditModeSelected,
} = storeToRefs(useOfferCreateProcessStore());
const {
  structureData,
  showComponentDetail,
  selectedComponent,
  selectedStructureData,
  showListStructure,
  showStructureDetail,
  isEditProduct,
  showActionSave,
  isDuplicate,
  offerCode,
  offerUuid,
  listComponentDuplicate,
  isPendingFinish,
} = storeToRefs(structureStore);

const replaceTab = inject<any>("replaceTab");
const replaceTabName = inject<any>("replaceTabName");

const localSelectedType = ref();
const localInputText = ref<any>();
const screenValue = ref(STRUCTURE_ITEM_SCREEN.OFFER);
const selectedOfferIndex = ref(0);
const strutureDataItem = ref<any>(null);
const openPopupDuplicate = ref(false);
const openPopupModifiedStructure = ref(false);
const itemDetail = ref<any>(null);
const dplcTrgtUuid = ref(null);
const codeCmcdList = ref(null);
const listNameCtg = ref();
const isClickResetButton = ref(true);

const calcHeightItemFound = computed(() =>
  products?.value.length > 0 ? "auto" : "40px"
);

const computedProducts = computed(() => productsDuplicate.value);

const duplicateProductName = computed(() => {
  const objectName = selectedStructureData?.value?.general.find(
    (item: any) => item.colName === "obj_name"
  ).attrVal;
  if (objectName) {
    return objectName;
  } else {
    return t("product_platform.offerPage.offerName");
  }
});

const getProducts = async (page = 1) => {
  if (isClickResetButton.value) {
    return;
  }
  let request = {
    page: page,
    size: pagination.value.pageSize,
    objName: selectedValue.value === "name" ? localInputText.value : undefined,
    objCode: selectedValue.value === "code" ? localInputText.value : undefined,
    itemCode: localSelectedType.value,
  };
  if (viewMode.value === VIEW_MODE.GRID) {
    await productStore.getProducts(request);
  } else {
    await productStore.getProductsTable(request);
  }
};

const refreshPage = async () => {
  await getProducts(pagination.value.currentPage);
  const selectedItem = products.value.find(
    (item: any) => item.objUUID === selectedProduct.value.objUUID
  );
  selectedProduct.value = selectedItem;
  productDetails.value = selectedItem;
  await structureStore.getStructure({
    objUuid: selectedProduct.value.objUUID,
  });
};

const duplicateSuccess = async (data) => {
  const params = {
    itemCode: localSelectedType.value,
    objCode: data.code,
  };
  const res = await getUiOfferAdvanced({
    ...params,
    size: 1,
    page: 1,
  });
  if (res?.data) {
    productDetails.value = cloneDeep(res.data.elements[0]);
    selectedProduct.value = cloneDeep(res.data.elements[0]);
  }
  offerCode.value = data.code || null;
  showListStructure.value = true;
  showActionSave.value = true;
  showStructureDetail.value = false;
};

const duplicateComponentSuccess = () => {
  if (offerBeClonedUuid.value) {
    handleNextProcessDuplicate();
  }
};

const handleNextProcessDuplicate = () => {
  offerDuplicateInGroupMode.value = true;
  offerDuplicated.value = selectedProduct.value;
  groupsFinish.value = [];
  const newRedirectObj = findMenuItem(
    menuTree.value,
    MenuItemID.GroupDuplicate
  );
  if (newRedirectObj) {
    newRedirectObj["path"] = configPath(newRedirectObj);
    replaceTab(newRedirectObj);
  }
};

const handleClickDrop = async () => {
  showListStructure.value = true;
  showComponentDetail.value = false;
  showStructureDetail.value = false;
};

const closePopupExitDuplicate = () => {
  openPopupDuplicate.value = false;
};

const handleExitDuplicate = () => {
  openPopupDuplicate.value = false;
  isDuplicate.value = false;
  showActionSave.value = false;
  viewDetail(itemDetail.value, true);
};

const closePopupModifiedStructure = async () => {
  openPopupModifiedStructure.value = false;
};

const handlePopupModifiedStructure = async () => {
  dplcTrgtUuid.value = itemDetail.value?.dplcTrgtUuid;
  if (dplcTrgtUuid.value && dplcTrgtUuid.value !== "-") {
    strutureDataItem.value = await structureStore.getStructure({
      objUuid: dplcTrgtUuid.value,
      onlyValidDtm: true,
    });
    isPendingFinish.value = true;
  }
  openPopupModifiedStructure.value = false;
  productDetails.value = itemDetail.value;
  selectedProduct.value = itemDetail.value;

  if (viewMode.value === VIEW_MODE.LIST) {
    await getDetailProductList(productDetails.value, itemTypeSelected.value);
  }
  await getStructureData(
    selectedProduct.value?.objUUID,
    strutureDataItem.value
  );
  showListStructure.value = true;
  showActionSave.value = true;
};

const getStructureData = async (objUuid: string, data: any = null) => {
  try {
    let res: any = null;
    if (data) {
      res = data;
    } else {
      res = await structureStore.getStructure({
        objUuid,
      });
    }
    if (res?.data) {
      structureData.value = res?.data;
      if (isDuplicate.value || dplcTrgtUuid.value) {
        structureData.value.benefit = filterDataValid(
          structureData.value.benefit
        );
        structureData.value.characteristics = filterDataValid(
          structureData.value.characteristics
        );
        structureData.value.price = filterDataValid(structureData.value.price);
        structureData.value.service = filterDataValid(
          structureData.value.service
        );
        listComponentDuplicate.value = [
          ...structureData.value.benefit,
          ...structureData.value.characteristics,
          ...structureData.value.price,
          ...structureData.value.service,
        ];
      } else {
        listComponentDuplicate.value = [];
      }
    } else {
      structureData.value = null;
      listComponentDuplicate.value = [];
    }
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  } finally {
    structureStore.resetDragDrop();
    showStructureDetail.value = false;
    showListStructure.value = false;
    showComponentDetail.value = false;
    selectedComponent.value = null;
    showProductDetail.value = true;
    dplcTrgtUuid.value = null;
  }
};

const filterDataValid = (data: any = []) => {
  const now = new Date();
  const dataValid = data
    .filter(
      (item) =>
        item.relationValidEndDtm === null ||
        new Date(item.relationValidEndDtm) >= now
    )
    .map((element: any) => {
      element.relationValidStartDtm = moment(now).format(DATE_FORMAT.DATE_TYPE);
      if (element?.relationValidEndDtm) {
        element.workTypeCode = ACTION_TYPE.ADD_EXPIRED;
      } else {
        element.workTypeCode = ACTION_TYPE.ADD;
      }
      return element;
    });
  return dataValid;
};

const viewDetail = async (element: any, exitDuplicate: Boolean = false) => {
  if (
    (element && selectedProduct.value?.objUuid !== element?.objUuid) ||
    isDuplicate.value
  ) {
    // check structure
    const objUuid = element?.objUuid;
    const res = await structureStore.getStructure({
      objUuid,
      onlyValidDtm: isDuplicate.value,
    });
    let validStructureData = false;
    if (res?.data) {
      res?.data?.forEach((item) => {
        if (item?.componentList?.length) {
          validStructureData = true;
        }
      });
      if (!validStructureData && !isDuplicate.value) {
        strutureDataItem.value = res;
        itemDetail.value = element;
        openPopupModifiedStructure.value = true;
        return;
      }
      if (viewMode.value === VIEW_MODE.LIST && showProductDetail) {
        getDetailProductList(element, itemTypeSelected.value);
      }
      // check structure
      productDetails.value = element;
      selectedProduct.value = element;
      itemDetail.value = element;

      const isInvalidStructureData =
        !res?.data.benefit?.length &&
        !res?.data.characteristics?.length &&
        !res?.data.price?.length &&
        !res?.data.service?.length;
      if (isInvalidStructureData && !isDuplicate.value) {
        strutureDataItem.value = res;
        itemDetail.value = element;
      }
    }
    await getStructureData(element?.objUUID, res);
  } else if (exitDuplicate && element) {
    await getStructureData(element?.objUUID);
  } else if (!element) {
    await getStructureData(selectedProduct.value?.objUUID);
  }
};

const getDetailProductList = async (element, selectedType) => {
  const detailProductList = await productStore.getProducts({
    page: 1,
    size: 10,
    objName: undefined,
    objCode: element.objCode,
    itemCode: selectedType,
  });
  localInputText.value = detailProductList?.data?.list?.[0].objCode;
};

const viewDetailDuplicate = async () => {
  await getStructureData(offerUuid.value);
  isDuplicate.value = false;
};

const confirmViewDetail = async (element: any, index) => {
  if (element?.duplicateItem) return;
  selectedOfferIndex.value = index + 1;
  categoryEditModeSelected.value = null;
  categoryStore.$reset();
  if (isDuplicate.value) {
    openPopupDuplicate.value = true;
    itemDetail.value = element;
  } else {
    showActionSave.value = false;
    viewDetail(element);
  }
};

const closeDetail = async () => {
  if (viewMode.value === VIEW_MODE.LIST && showProductDetail.value) {
    inputText.value = undefined;
    localInputText.value = undefined;
    await getProducts();
  }
  showProductDetail.value = false;
  productDetails.value = null;
  selectedProduct.value = null;
  selectedComponent.value = null;
  selectedStructureData.value = null;
  structureData.value = null;
  showStructureDetail.value = false;
  showListStructure.value = false;
  showComponentDetail.value = false;
  isDuplicate.value = false;
  productLineCoordinates.value = [];
};

const listActions = (item: any): ActionType[] => {
  return [
    {
      name: t("product_platform.openinNewWindow"),
      icon: OpenInNewIcon,
      iconProps: {
        class: "text-text-lighter",
      },
      onClick: async () => {
        moveOfferSearchPage({
          itemCode: item.itemCode,
          itemCodeName: "",
          objCode: item?.objCode || "",
          objUuid: item?.objUuid || "",
          offerType: item.itemCode || "",
        });
      },
    },
  ];
};

const transitionDuplicateItem = () => {
  nextTick(() => {
    const dupItemDom = document.getElementsByClassName("duplicate-item");
    if (dupItemDom[0]) {
      dupItemDom[0].classList.add("dup-item-show");
    }
  });
};

const getOfferDetail = async (item: any) => {
  const params = { objUuid: item.objUuid };
  const res: any = await productStore.getProductStructureDetailRoot(params);
  if (res?.data) {
    setTotalAdditional(
      DETAIL_COMPONENT_NAME.OFFER_SEARCH,
      res.data.additional.map((item: any) => ({
        ...item,
        attrVal:
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
            ? JSON.parse(item?.attrVal)?.filter((val: any) => val.trim()) || []
            : item.attrVal,
      }))
    );
    res.data.general.forEach((item) => {
      if (item.colName === "obj_name") {
        item.attrVal = null;
      } else if (item.colName === "valid_end_dtm") {
        item.attrVal = null;
      } else if (item.colName === "obj_code") {
        item.attrVal = null;
      }
    });
    res.data.general.push({
      colName: "dplcTrgtUuid",
      fieldTypeCode: "HD",
      editYn: null,
      sortNo: null,
      useYn: null,
      attrMaxLength: null,
      requiredYn: null,
      labelId: null,
      attrVal: item.objUuid,
    });
    let ctg_id = res?.data?.general.find(
      (item) => item.colName === "ctgr_node_uuid"
    )?.attrVal;
    if (ctg_id) {
      listNameCtg.value =
        await categoryOfferCreateStore.getCtgPathRequest(ctg_id);
    }
    handleResponse(res.data);
  }
};

const handleResponse = (data: any) => {
  if (data) {
    selectedComponent.value = null;
    let itemCtgDetail = data.general.find(
      (item) => item.colName === "ctgr_node_uuid"
    );
    data.general = data.general
      .sort((cur, next) => cur.sortNo - next.sortNo)
      .map((item) => {
        if (item.colName === "chg_dept_name") {
          return {
            ...item,
            attrVal: userInfor.chgDeptName,
          };
        }
        if (item.colName === "chg_user") {
          return {
            ...item,
            attrVal: userInfor.chgUser,
          };
        }
        return item;
      })
      .filter((item) => item.colName !== "ctgr_node_uuid");
    let itemTypeVal = data.general.find((item) => item.colName === "item_code");

    data.general.splice(data.general.indexOf(itemTypeVal) + 1, 0, {
      colName: "ctgr_node_uuid",
      fieldTypeCode: "TF",
      editYn: "Y",
      sort: "1",
      useYn: "Y",
      attrMaxLength: "100",
      requiredYn: "Y",
      disabled: true,
      attrPath:
        itemCtgDetail?.attrVal && !categoryDuplicateModeSelected?.value
          ? listNameCtg.value
          : pathCategorySelected?.value,
      valName:
        itemCtgDetail?.attrVal && !categoryDuplicateModeSelected?.value
          ? listNameCtg.value[listNameCtg.value?.length - 1] || null
          : categoryDuplicateModeSelected?.value?.ctgrNodeName || null,
      labelId: "product_platform.categoryNode",
      attrVal:
        itemCtgDetail?.attrVal && !categoryDuplicateModeSelected?.value
          ? itemCtgDetail?.attrVal
          : offerCreateInCategoryMode && categoryDuplicateModeSelected.value
            ? categoryDuplicateModeSelected?.value?.ctgrNodeUuid
            : null,
    });

    selectedStructureData.value = {
      general: [
        ...data.general,
        ...data?.additional
          .filter((item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL)
          .map((item: any) => ({
            ...item,
            attrVal:
              item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                ? JSON.parse(item?.attrVal)?.filter((val: any) => val.trim()) ||
                  []
                : item.attrVal,
          })),
        ,
      ],
      additional: data?.additional
        .filter((item) => item.dispTab === DETAIL_TAB_TYPE.ADDITIONAL)
        .map((item: any) => ({
          ...item,
          attrVal:
            item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
              ? JSON.parse(item?.attrVal)?.filter((val: any) => val.trim()) ||
                []
              : item.attrVal,
        })),
    };
  } else {
    selectedStructureData.value = {
      generalDetails: [],
      additionalParams: [],
    };
  }
  isEditProduct.value = true;
  showComponentDetail.value = false;
  showStructureDetail.value = true;
};

const handleChangeCategoryNodeDate = async () => {
  const itemCtgDetail = selectedStructureData.value.general.find(
    (item) => item.colName === "ctgr_node_uuid"
  );
  itemCtgDetail.attrPath = await categoryOfferCreateStore.getCtgPathRequest(
    categoryDuplicateModeSelected?.value?.ctgrNodeUuid
  );
  itemCtgDetail.valName = categoryDuplicateModeSelected?.value?.ctgrNodeName;
  itemCtgDetail.attrVal = categoryDuplicateModeSelected?.value?.ctgrNodeUuid;
};

const handleCancelDuplicate = () => {
  removeTab(MenuItemID.OfferDuplicate);
};

watch(
  () => newOfferTabItem.value,
  (element) => {
    confirmViewDetail(element, 0);
    inputText.value = element.objName;
    itemTypeSelected.value = element.itemCode;
    productType.value = element.itemCode;
  },
  { deep: true }
);

watch(
  () => duplicateProductName.value,
  (newVal) => {
    if (productsDuplicate.value[1]) {
      productsDuplicate.value[1].objName = newVal;
    }
  }
);

onBeforeMount(async () => {
  replaceTabName(MenuItemID.OfferDuplicate, "Catalog Duplicate Offer");
  await productStore.getSibling(selectedProduct.value.objUuid);
  if (isEmpty(selectedStructureData.value)) {
    await getOfferDetail(selectedProduct.value);
  }
  if (!isEmpty(categoryDuplicateModeSelected?.value)) {
    handleChangeCategoryNodeDate();
  }
  if (isEmpty(structureData.value)) {
    await viewDetail(selectedProduct.value);
    showProductDetail.value = true;
    showStructureDetail.value = true;
  }
});

onMounted(async () => {
  if (isDuplicate.value) {
    transitionDuplicateItem();
  }
  if (inputText.value) {
    localInputText.value = inputText.value;
  }
  if (itemTypeSelected.value) {
    localSelectedType.value = itemTypeSelected.value;
  }
  try {
    if (!itemsType.value?.length) {
      const { data } = await getListItemCodeApi({
        lItemCode: LargeItemCode.Offer,
      });
      itemsType.value = data;
    }
  } catch (error: any) {
    snackbarStore.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
const removeTab = inject<any>("removeTab");
</script>

<style scoped>
.custom-text-field .v-label {
  font-size: 13px;
}

select option {
  background-color: #fff;
  color: #363636;
  height: 40px;
}

.show-details {
  margin-left: unset !important;
  margin-top: 12px;
}

.option-show-details {
  min-width: 70px !important;
}

select option:hover {
  background-color: #ededed;
  color: red;
}

select option:checked {
  background-color: #ededed;
}

.select {
  max-width: 130px;
  height: 40px;
}

.active-icon {
  background-color: #fff;
  color: #ba1642;
}

.v-input__icon--append-inner {
  cursor: pointer;
}

.v-field {
  border-radius: 8px;
}

.v-text-field:deep() {
  font-size: 13px !important;
}

.v-field--variant-solo {
  box-shadow: none !important;
}

.v-field__input {
  font-size: 13px;
}

:deep(.v-label) {
  font-size: 13px !important;
}

:deep().v-input.offer-search-long {
  width: 320px !important;
}

:deep().v-input.offer-search-short {
  width: 164px !important;
}

:deep() table {
  height: v-bind(calcHeightItemFound);
}

:deep() .v-data-table-rows-no-data {
  text-align: left;
  border-bottom-width: thin;
}

:deep(.custom-table) {
  max-height: 600px;
}

.duplicate-item {
  transform: translateY(-78px);
  opacity: 0;
  transition:
    transform 0.2s ease-in-out,
    opacity 0.1s ease;
}

.duplicate-item.dup-item-show {
  transform: translateY(0px);
  opacity: 1;
}

.list-description-title {
  color: #6b6d70;
  font-size: 14px;
  font-weight: 500;
  line-height: 21px;
  letter-spacing: 0.25px;
  height: 21px;
}
</style>
