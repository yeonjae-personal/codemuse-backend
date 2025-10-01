<template>
  <div
    class="grid grid-cols-4 w-full rounded-[12px] bg-white h-full rounded-lg border border-[#666]"
  >
    <div class="py-3">
      <OfferDetail
        :is-add="!onlyView"
        :type="createOfferType"
        is-create-offer
        @update-success="refreshPage"
      />
    </div>
    <div class="col-span-3">
      <ProductStructure
        :list-structure="listStructureItem"
        :product-detail="productCreateCard"
        :screen="STRUCTURE_ITEM_SCREEN.ADD_COMPONENT"
        :type="createOfferType"
        is-add
        @view-detail="viewDetail"
        @handle-click-drop="handleClickDrop"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import {
  useSnackbarStore,
  useHistoryTabStore,
  useCreateStructureStore,
  useProductsCreateStore,
  useMenuStore,
  useCategoryStore,
} from "@/store";
import {
  OFFER_TYPE,
  DATE_FORMAT,
  DETAIL_TAB_TYPE,
  OFFER_TYPES_CONVERT,
} from "@/constants/";
import { useI18n } from "vue-i18n";
import { LargeItemCode } from "@/enums";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { getUserInfor } from "@/constants/userInfor";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { configPath, findMenuItem } from "@/utils/config-path";
import customValidationStore from "@/store/admin/customValidation.store";
import { useCustomValidation } from "@/utils/custom-validation";
import moment from "moment-timezone";
import cloneDeep from "lodash-es/cloneDeep";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { MenuItemID } from "@/enums/redirect";

const { t } = useI18n();

const snackbarStore = useSnackbarStore();
const productCreateStore = useProductsCreateStore();
const categoryStore = useCategoryStore();
const userInfor = getUserInfor();
const historyStore = useHistoryTabStore();
const createStructureStore = useCreateStructureStore();
const categoryOfferCreateStore = useOfferCreateProcessStore();
const {
  offerCreateInCategoryMode,
  categorySelected,
  pathCategorySelected,
  reChooseCategoryMode,
  categoryTab,
  itemTypeOffer,
  offerCreateTemp,
} = storeToRefs(useOfferCreateProcessStore());

const { menuTree } = storeToRefs(useMenuStore());

const {
  getRuleByAdditionalFields,
  applyRules,
  applyRulesInit,
  updateAdditionalField,
} = useCustomValidation();
const { getListCustomValidation } = customValidationStore();
const { listRulesOriginal } = storeToRefs(customValidationStore());
const {
  selectedStructureData,
  hiddenGeneralData,
  offerCode,
  offerUuid,
  isEditProduct,
  structureData,
  showListStructure,
  showStructureDetail,
  showComponentDetail,
  isCreated,
  listStructureItem,
  onlyView,
} = storeToRefs(createStructureStore);

const itemTempCreated = ref<any>(null);
const { productLineCoordinates, initData, itemsType, createOfferType } =
  storeToRefs(productCreateStore);

const addTab = inject<any>("addTab");

// const offerCreateType = computed(() => {
//   if (createOfferType.value) {
//     return createOfferType.value;
//   } else if (selectedStructureData.value) {
//     const typeObj = selectedStructureData.value?.general?.find(
//       (item) => item.colName === "item_code"
//     );
//     return typeObj?.attrVal;
//   }
//   return null;
// });

const productCreateCard = computed(() => {
  const objName =
    selectedStructureData.value?.general?.find(
      (el: any) => el.colName === "obj_name"
    )?.attrVal || t("product_platform.offerPage.offerName");

  const validStartDtm =
    hiddenGeneralData.value?.find((el: any) => el.colName === "valid_start_dtm")
      ?.attrVal ||
    moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE);
  const validEndDtm =
    selectedStructureData.value?.general?.find(
      (el: any) => el.colName === "valid_end_dtm"
    )?.attrVal || null;
  return {
    objName,
    objCode: offerCode.value || t("product_platform.auto_generation"),
    validStartDtm,
    validEndDtm,
    additional: itemTempCreated.value?.additional,
    general: itemTempCreated.value?.general,
  };
});

const viewDetail = async () => {
  try {
    const res = await createStructureStore.getStructure({
      objUuid: offerUuid.value,
    });
    if (res?.data) {
      structureData.value = res?.data;
      listStructureItem.value = res?.data;
    } else {
      structureData.value = null;
      listStructureItem.value = [];
    }
  } catch (err: any) {
    snackbarStore.showSnackbar(
      err?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  } finally {
    createStructureStore.resetDragDrop();
    showListStructure.value = false;
    showStructureDetail.value = true;
  }
};

const handleClickDrop = async () => {
  showListStructure.value = true;
  showStructureDetail.value = false;
  showComponentDetail.value = false;
};

const callInitDate = async (itemCode, isResetCategoryData = false) => {
  await productCreateStore.initProductCreate({
    itemCode: itemCode,
  });
  initData.value.general.forEach((row) => {
    if (row.colName === "item_code") {
      row.editYn = "Y";
      createOfferType.value = row.attrVal;
    }
  });
  if (isResetCategoryData) {
    setCategoryIntoGeneral(createOfferType.value);
  }
  offerCreateTemp.value = cloneDeep(initData.value);
  handleResponse(initData.value);
  listStructureItem.value = initData.value.structures;
};

const handleResponse = async (data: any) => {
  if (data) {
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
        if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
          return {
            ...item,
            attrVal: item.attrVal,
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
      attrPath: pathCategorySelected?.value,
      valName: categorySelected?.value?.ctgrNodeName,
      labelId: "product_platform.categoryNode",
      attrVal:
        offerCreateInCategoryMode && categorySelected.value
          ? categorySelected?.value?.ctgrNodeUuid
          : null,
    });

    const baseData = {
      general: [
        ...data.general,
        ...data?.additional.filter(
          (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
        ),
      ],
      additional: data?.additional
        .filter((item) => item.dispTab === DETAIL_TAB_TYPE.ADDITIONAL)
        .map((item: any) => ({
          ...item,
          attrVal:
            item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
              ? item?.attrVal?.filter((val: any) => val.trim()) || []
              : item.attrVal,
        })),
    };
    await getListCustomValidation({
      item: "O",
      type: data.general.find((item) => item.colName === "item_code").attrVal,
    });

    const listRules = getRuleByAdditionalFields(
      [...data?.additional],
      listRulesOriginal.value
    );

    const newField = updateAdditionalField(baseData, listRules, {
      showRule: true,
      pageType: "O",
      type: createOfferType.value,
    });

    selectedStructureData.value = {
      general: newField.generalList,
      additional: newField.additionalList,
    };

    // Apply init rules
    const listItems = [
      ...selectedStructureData.value?.additional,
      ...selectedStructureData.value?.general.filter(
        (item) => item.dispTab === "G"
      ),
    ];
    nextTick(() => {
      const { generalList, additionalList } = applyRulesInit(
        listItems,
        listRulesOriginal.value,
        selectedStructureData.value
      );
      selectedStructureData.value.general = generalList;
      selectedStructureData.value.additional = additionalList;
    });
  } else {
    selectedStructureData.value = {
      generalDetails: [],
      additionalParams: [],
    };
  }
};

const refreshPage = async (param) => {
  const res = await productCreateStore.getProductStructureDetailRoot(param);
  if (res?.data) {
    itemTempCreated.value = res?.data;
    handleResponse(res.data);
  }
  historyStore.fetchHistory({
    objUuid: param.objUuid,
  });
  onlyView.value = true;
};

const findObjectById = (arr, targetId) => {
  for (let obj of arr) {
    if (obj.ctgrNodeUuid === targetId) {
      return obj;
    }

    if (obj.children) {
      let found = findObjectById([...obj.children], targetId);
      if (found) return found;
    }
  }
  return null;
};

const redirectCategoryPage = async () => {
  // categoryOfferCreateStore.resetProcess();
  // isFormOfferEdit.value = false;
  reChooseCategoryMode.value = true;
  itemTypeOffer.value = createOfferType.value;
  const menu = findMenuItem(menuTree.value, MenuItemID.TreeView);
  if (!menu) return;
  menu["path"] = configPath(menu);

  let itemSelectedUuid = selectedStructureData.value?.general?.find(
    (item) => item.colName === "ctgr_node_uuid"
  )?.attrVal;
  await categoryStore.fetchTabsCategory();

  categoryStore.setCategoryTab(
    itemsType.value
      .find((item) => item.itemCode === itemTypeOffer.value)
      ?.cmcdDetlNm?.toUpperCase()
      .replace("-", "")
  );

  await categoryStore.getTreeCategory();
  if (categorySelected.value) {
    let itemSelect = findObjectById(
      categoryStore.getTreeData,
      categorySelected.value?.ctgrNodeUuid
    );
    if (itemSelect) {
      categoryStore.setSelectedCategoryTreeNode(itemSelect);
    }
  } else {
    let itemSelect = findObjectById(
      categoryStore.getTreeData,
      itemSelectedUuid
    );
    if (itemSelect) {
      categoryStore.setSelectedCategoryTreeNode(itemSelect);
    }
  }
  addTab(menu);
};

const getListOfferType = async () => {
  if (!itemsType.value?.length) {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    itemsType.value = data || [];
  }
};

const dataTypeString = computed(() =>
  JSON.stringify(selectedStructureData.value)
);

const setCategoryIntoGeneral = (offerType) => {
  categorySelected.value = null;
  categoryTab.value = offerType
    ? OFFER_TYPES_CONVERT[offerType as string]?.toUpperCase().replace("-", "")
    : OFFER_TYPE.PRICEPLAN;
  productLineCoordinates.value = [];
  if (!categorySelected.value) {
    categoryStore.$reset();
  }
};

watch(
  () => selectedStructureData.value,
  (newVal) => {
    const newType = newVal?.general?.find(
      (item) => item.colName === "item_code"
    )?.attrVal;
    if (newType !== createOfferType.value) {
      callInitDate(newType, true);
    }
  },
  { deep: true }
);

watch(
  () => categorySelected.value,
  async (newVal) => {
    if (newVal) {
      await categoryOfferCreateStore.getCtgPathRequest(newVal?.ctgrNodeUuid);
      if (offerCreateTemp.value) {
        offerCreateTemp.value = selectedStructureData.value;
        handleResponse(offerCreateTemp.value);
      }
    }
  },
  {
    deep: true,
    immediate: true,
  }
);

watch(dataTypeString, (newValueStr, oldValueStr) => {
  let newValue = JSON.parse(newValueStr);
  let oldValue = JSON.parse(oldValueStr);

  nextTick(() => {
    const { generalList, additionalList } = applyRules(
      oldValue,
      newValue,
      listRulesOriginal.value,
      selectedStructureData.value
    );
    if (selectedStructureData.value) {
      selectedStructureData.value.general = generalList;
      selectedStructureData.value.additional = additionalList;
    }
  });
});

onMounted(() => {
  onlyView.value = false;
  isEditProduct.value = !isCreated.value;
  getListOfferType();
  if (!isCreated.value) {
    const categoryTabName =
      categoryTab.value?.length === 2
        ? categoryTab.value
        : OFFER_TYPE[categoryTab.value];
    if (!createOfferType.value) {
      callInitDate(categoryTabName ? categoryTabName : OFFER_TYPE.PRICEPLAN);
    }
  }
});

provide("redirectCategoryPage", redirectCategoryPage);
</script>
