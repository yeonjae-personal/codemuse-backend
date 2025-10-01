<template>
  <div :class="['w-full relative grid grid-cols-3 h-full p-3', customClass]">
    <ProductStructureTree
      :drag-offer-type="dragOfferType"
      :is-dragging="isDragging"
      :list-structure="listStructure"
      :is-add="isAdd"
      :show-action-save="showActionSave"
      :show-right-pane="
        showComponentDetail ||
        showListStructure ||
        (showStructureDetail && !isAdd)
      "
      :type="type"
      :show-structure-detail="showStructureDetail"
      :product-detail="productDetail"
      :screen-value="screenValue"
      :duplicate="duplicate"
      :product-line-coordinates="productLineCoordinates"
      :filtered-rules="filteredRules"
      :show-list-structure="showListStructure"
      :is-created-structure="isCreatedStructure"
      @click-card="clickCard"
      @click-drop="handleClickDrop"
      @add="handleAdd"
      @edit="handleEdit"
      @cancel="handleCancel"
      @save="handleSave"
    />
    <ShowDetailIcon
      v-if="!isAdd && !duplicate"
      class="absolute top-[174px] left-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="closeDetail"
    />
    <OfferDetail
      v-if="showStructureDetail && !isAdd"
      :is-duplicate="isDuplicate ? true : false"
      :screen="screenValue"
      :type="type"
      @close-structure-detail-pane="closeStructureDetailPane"
      @cancel-update="cancelUpdate"
      @update-success="refreshPage"
    />
    <OfferComponentDetail
      v-if="showComponentDetail"
      :is-add="isAdd"
      :type="type"
      :duplicate="duplicate"
      @close-offer-component-detail="closeOfferComponentDetail"
    />
    <ListStructure
      v-if="showListStructure"
      :is-add="isAdd"
      :is-duplicate="isDuplicate"
      :screen="screenValue"
      :type="type"
      @close-list-structure="closeListStructure"
    />
  </div>
  <BasePopup
    v-model="openPopup"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="
      isCancel
        ? $t('product_platform.desc_cancel')
        : $t('product_platform.desc_update')
    "
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    @on-close="closePopupSave"
    @on-submit="handleSubmit"
  />
</template>

<script lang="ts" setup>
import moment from "moment-timezone";
import {
  useProductsStore,
  useSnackbarStore,
  useStructureStore,
  useHistoryTabStore,
  useCreateStructureStore,
  useProductsCreateStore,
  useMenuStore,
  useCategoryStore,
  useProductsDuplicateStore,
  useDuplicateStructureStore,
} from "@/store";
import { DialogIconType, LargeItemCode, TypeComponentCode } from "@/enums";
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import { formatDate } from "@/utils/format-data";
import {
  DATE_FORMAT,
  DETAIL_COMPONENT_NAME,
  DETAIL_TAB_TYPE,
  OFFER_TYPE,
} from "@/constants/";
import { useI18n } from "vue-i18n";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { ComponentSubType } from "@/enums/component";
import ShowDetailIcon from "@/components/prod/icons/ShowDetailIcon.vue";
import { getUserInfor } from "@/constants/userInfor";
import customValidationStore from "@/store/admin/customValidation.store";
import { filteredConditionRules } from "@/utils/custom-validation";
import useOfferCreateProcessStore from "@/store/offerCreateProcess.store";
import { configPath, findMenuItem } from "@/utils/config-path";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { MenuItemID } from "@/enums/redirect";

const { listRulesOriginal } = storeToRefs(customValidationStore());
const categoryOfferCreateStore = useOfferCreateProcessStore();
const { getListCustomValidation, setTotalAdditional } = customValidationStore();
const filteredRules = ref<any[]>([]);
const userInfor = getUserInfor();

const props = defineProps({
  productDetail: {
    type: Object,
    default: () => ({}),
  },
  listStructure: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
  },
  type: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  duplicate: {
    type: Boolean,
    default: false,
  },
  createInitData: {
    type: Object,
    default: () => ({}),
  },
});

const emit = defineEmits([
  "clickItem",
  "closeDetail",
  "closeStructureDetailPane",
  "view-detail",
  "refreshPage",
  "duplicateDetailSuccess",
  "duplicateComponentSuccess",
  "handleClickDrop",
  "view-detail-duplicate",
  "onChangeOfferType",
  "cancelDuplicateDetail",
]);

const { t } = useI18n();

const productStore = useProductsStore();
const productCreateStore = useProductsCreateStore();
const productsDuplicateStore = useProductsDuplicateStore();
const structureStore = useStructureStore();
const createStructureStore = useCreateStructureStore();
const duplicateStructureStore = useDuplicateStructureStore();
const snackbarStore = useSnackbarStore();
const historyStore = useHistoryTabStore();
const categoryStore = useCategoryStore();

const selectedStore = computed(() => {
  if (props.isAdd) {
    return createStructureStore;
  } else if (props.duplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});

const productStoreSelected = computed(() => {
  if (props.isAdd) {
    return productCreateStore;
  } else if (props.duplicate) {
    return productsDuplicateStore;
  }
  return productStore;
});

const {
  showListStructure,
  showComponentDetail,
  showStructureDetail,
  isCreated,
  isEditProduct,
  isCreatedStructure,
  selectedComponent,
  selectedStructureData,
  isDragging,
  dragOfferType,
  listAdd,
  listUpdate,
  offerUuid,
  showActionSave,
  isDuplicate,
  listComponentDuplicate,
  isPendingFinish,
} = storeToRefs(selectedStore.value);

const {
  selectedProduct,
  getDetailFromOtherPage,
  showProductDetail,
  productLineCoordinates,
} = storeToRefs(productStoreSelected.value);

const {
  offerCreateInCategoryMode,
  categoryEditModeSelected,
  pathCategorySelected,
  reChooseCategoryMode,
  categorySelected,
  isFormOfferEdit,
  isFormOfferDuplicate,
  itemTypeOffer,
} = storeToRefs(useOfferCreateProcessStore());

const { menuTree } = storeToRefs(useMenuStore());

const openPopup = ref(false);
const isCancel = ref(false);
const optionsType = ref<any[]>([]);
const listNameCtg = ref();

const addTab = inject<any>("addTab");

const handleCanvasMatrix = () => {
  const rootOffer: any = document.getElementById(
    props.type ? "rootOffer" + props.type : "rootOffer"
  );
  const comProductListEl: any = document.getElementsByClassName(
    props.type ? "structure-" + props.type : "structure"
  );
  if (comProductListEl?.length && rootOffer) {
    for (const el of comProductListEl) {
      const comProElCoor = {
        leftStartPoint: rootOffer?.clientWidth / 2,
        topStartPoint: 0,
        leftMovePoint: el.offsetLeft + el.clientWidth / 2,
        topMovePoint: 120,
        lineWidth: 2,
        strokeStyle: "#909399",
      };
      productLineCoordinates.value.push(comProElCoor);
    }
  }
};

const screenValue = computed(() => {
  if (
    props.screen === STRUCTURE_ITEM_SCREEN.DISCOUNT ||
    (props.screen === STRUCTURE_ITEM_SCREEN.ADD_COMPONENT &&
      props.type === OFFER_TYPE.DISCOUNT)
  ) {
    return STRUCTURE_ITEM_SCREEN.DISCOUNT;
  } else {
    return STRUCTURE_ITEM_SCREEN.OFFER;
  }
});

const isOpen3rdTab = computed(
  () =>
    showStructureDetail.value ||
    showListStructure.value ||
    showComponentDetail.value
);

const customClass = computed(() => {
  if (isOpen3rdTab.value) return "px-0";
  else if (!isOpen3rdTab.value) return "pl-0 pr-3";
  return "";
});

const isValidOverlapTime = computed(() => {
  if (props.type === OFFER_TYPE.DISCOUNT) {
    const listAddDR = listAdd.value.filter(
      (item: any) => item?.itemCode === ComponentSubType.DiscountRate
    );
    const listPriceItem =
      props.listStructure?.find(
        (item: any) => item?.typeOffer === TypeComponentCode.Price
      )?.listItems || [];
    const listDR =
      listPriceItem.find(
        (item: any) => item?.type?.itemCode === ComponentSubType.DiscountRate
      )?.items || [];
    const listTotalDR = [...listAddDR, ...listDR];
    for (let i = 0; i < listTotalDR.length - 1; i++) {
      for (let j = i + 1; j < listTotalDR.length; j++) {
        if (isOverlap(listTotalDR[i as number], listTotalDR[j as number])) {
          return false;
        }
      }
    }
  } else {
    const listAddRC = listAdd.value.filter(
      (item: any) => item?.itemCode === ComponentSubType.RecurringCharge
    );
    const listPriceItem =
      props.listStructure?.find(
        (item: any) => item?.typeOffer === TypeComponentCode.Price
      )?.listItems || [];
    const listRC =
      listPriceItem.find(
        (item: any) => item?.type?.itemCode === ComponentSubType.RecurringCharge
      )?.items || [];
    const listTotalRC = [...listAddRC, ...listRC];
    for (let i = 0; i < listTotalRC.length - 1; i++) {
      for (let j = i + 1; j < listTotalRC.length; j++) {
        if (isOverlap(listTotalRC[i as number], listTotalRC[j as number])) {
          return false;
        }
      }
    }
  }
  return true;
});

const isOverlap = (firstVal: any, secondVal: any) => {
  const firstRelationStartDate = firstVal.relationValidStartDtm || null;
  const firstRelationEndDate = firstVal.relationValidEndDtm || null;
  const secondRelationStartDate = secondVal.relationValidStartDtm || null;
  const secondRelationEndDate = secondVal.relationValidEndDtm || null;
  if (!firstRelationStartDate || !secondRelationStartDate) return true;
  if (!firstRelationEndDate && !secondRelationEndDate) return true;
  if (
    (!!firstRelationEndDate &&
      moment(secondRelationStartDate, [
        DATE_FORMAT.DATE_TYPE,
        DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
      ]).diff(
        moment(firstRelationEndDate, [
          DATE_FORMAT.DATE_TYPE,
          DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
        ])
      ) > 0) ||
    (!!secondRelationEndDate &&
      moment(firstRelationStartDate, [
        DATE_FORMAT.DATE_TYPE,
        DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
      ]).diff(
        moment(secondRelationEndDate, [
          DATE_FORMAT.DATE_TYPE,
          DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE,
        ])
      ) > 0)
  )
    return false;

  return true;
};

const clickCard = async () => {
  if (props.isAdd) {
    if (!isCreated.value) {
      await initData();
    } else {
      showListStructure.value = false;
      showComponentDetail.value = false;
    }
    return;
  } else if (isDuplicate.value) {
    showStructureDetail.value = true;
    showComponentDetail.value = false;
  } else {
    await getOfferDetail();
  }
  showStructureDetail.value = true;
  showProductDetail.value = true;
  showListStructure.value = false;
  getDetailFromOtherPage.value = false;
};

const getOfferDetail = async (param = null) => {
  const params = { objUuid: props.productDetail?.objUuid };
  const res = await productStoreSelected.value.getProductStructureDetailRoot(
    param ? param : params
  );

  if (res?.data) {
    setTotalAdditional(
      DETAIL_COMPONENT_NAME.OFFER_SEARCH,
      res.data.additional.map((item: any) => ({
        ...item,
        attrVal:
          item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
            ? JSON.parse(item?.attrVal)?.filter((value: any) => value.trim()) ||
              []
            : item.attrVal,
      }))
    );
    let ctg_id = res?.data?.general.find((i) => i.colName === "ctgr_node_uuid")
      ?.attrVal;
    if (ctg_id) {
      listNameCtg.value =
        await categoryOfferCreateStore.getCtgPathRequest(ctg_id);
    }
    handleResponse(res.data);
  }
  historyStore.fetchHistory({
    objUuid: props.productDetail.objUUID,
  });
};

const cancelUpdate = async () => {
  if (props.isAdd) {
    await initData(true);
  } else {
    if (isDuplicate.value) {
      emit("cancelDuplicateDetail");
    }
    await getOfferDetail();
  }
};

const handleResponse = (data: any, keepAlive = false) => {
  if (data) {
    selectedComponent.value = null;
    let itemCtgDetail = data.general?.find(
      (i) => i.colName === "ctgr_node_uuid"
    );

    data.general = data.general
      .sort((cur, next) => cur.sortNo - next.sortNo)
      .map((i) => {
        if (
          i.colName === "chg_dept_name" &&
          (isDuplicate.value || props.isAdd)
        ) {
          return {
            ...i,
            attrVal: userInfor.chgDeptName,
          };
        }
        if (i.colName === "chg_user" && (isDuplicate.value || props.isAdd)) {
          return {
            ...i,
            attrVal: userInfor.chgUser,
          };
        }
        if (i.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
          return {
            ...i,
            attrVal: i.attrVal,
          };
        }
        return i;
      })
      .filter((i) => i.colName !== "ctgr_node_uuid");

    let itemTypeVal = data.general.find((i) => i.colName === "item_code");

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
        itemCtgDetail?.attrVal && !categoryEditModeSelected?.value
          ? listNameCtg.value
          : pathCategorySelected?.value,
      valName:
        itemCtgDetail?.attrVal && !categoryEditModeSelected?.value
          ? listNameCtg.value[listNameCtg.value?.length - 1] || null
          : props.isAdd
            ? categorySelected.value?.ctgrNodeName
            : categoryEditModeSelected?.value?.ctgrNodeName || null,
      labelId: "product_platform.categoryNode",
      attrVal:
        itemCtgDetail?.attrVal && !categoryEditModeSelected?.value
          ? itemCtgDetail?.attrVal
          : offerCreateInCategoryMode && categoryEditModeSelected.value
            ? props.isAdd
              ? categorySelected?.value?.ctgrNodeUuid
              : categoryEditModeSelected?.value?.ctgrNodeUuid
            : null,
    });

    selectedStructureData.value = {
      general: [
        ...data.general,
        ...data?.additional
          .filter((i) => i.dispTab === DETAIL_TAB_TYPE.GENERAL)
          .map((item: any) => ({
            ...item,
            attrVal:
              item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                ? JSON.parse(item?.attrVal)?.filter((val: any) => val.trim()) ||
                  []
                : item.attrVal,
          })),
      ],
      additional: data?.additional
        .filter((i) => i.dispTab === DETAIL_TAB_TYPE.ADDITIONAL)
        .map((item: any) => ({
          ...item,
          attrVal:
            item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
              ? Array.isArray(item?.attrVal)
                ? item?.attrVal
                : JSON.parse(item?.attrVal)?.filter((value: any) =>
                    value.trim()
                  ) || []
              : item.attrVal,
        })),
    };
  } else {
    selectedStructureData.value = {
      generalDetails: [],
      additionalParams: [],
    };
  }
  if (!isDuplicate.value && !keepAlive) {
    isEditProduct.value = props.isAdd && !isCreated.value;
    // showListStructure.value = false;
    showComponentDetail.value = false;
    showStructureDetail.value = true;
  }
};

const closeDetail = () => {
  closeStructureDetailPane();
  emit("closeDetail");
};

const closeOfferComponentDetail = () => {
  showComponentDetail.value = false;
};

const handleAdd = () => {
  if (!isCreated.value) {
    snackbarStore.showSnackbar(
      t("product_platform.create_offer_first"),
      "error"
    );
    return;
  }
  closeStructureDetailPane();
  showListStructure.value = true;
  showActionSave.value = true;
};

const handleEdit = () => {
  if (isDuplicate.value) {
    snackbarStore.showSnackbar(
      t("product_platform.save_offer_duplicate"),
      "error"
    );
  } else {
    closeStructureDetailPane();
    showListStructure.value = true;
    showActionSave.value = true;
  }
};

const filteredRulesByOffer = async () => {
  const objUuid = props.productDetail?.objUUID || offerUuid.value;
  if (!objUuid) return;

  const [response] = await Promise.all([
    productStoreSelected.value.getProductStructureDetailRoot({ objUuid }),
    getListCustomValidation({ item: "O", type: props.type }),
  ]);

  filteredRules.value = filteredConditionRules(
    listRulesOriginal.value,
    response?.data?.additional
  );
};

const handleCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};
const handleSave = async () => {
  if (!isValidOverlapTime.value) {
    snackbarStore.showSnackbar(t("product_platform.overlapDate"), "error");
    return;
  }
  isCancel.value = false;
  openPopup.value = true;
};

const closeListStructure = () => {
  if (isDuplicate.value && !showActionSave.value) {
    emit("view-detail-duplicate");
  } else if (!isDuplicate.value && !showActionSave.value) {
    emit("view-detail", offerUuid.value);
  }
  showListStructure.value = false;
};

const closePopupSave = () => {
  openPopup.value = false;
};

const handleSubmit = async () => {
  if (isCancel.value) {
    if (isDuplicate.value) {
      isDuplicate.value = false;
    }
    listAdd.value = [];
    showActionSave.value = false;
    closePopupSave();
    closeListStructure();
  } else {
    let data = [] as any[];
    let listComponentDuplicateMapping: any[] = [];
    if (isDuplicate.value || isPendingFinish.value) {
      props.listStructure?.forEach((item) => {
        if (item?.componentList?.length) {
          listComponentDuplicate.value.push(...item.componentList);
        }
      });
    }
    if (listUpdate.value.length && listComponentDuplicate.value.length) {
      listComponentDuplicateMapping = listComponentDuplicate.value.filter(
        (itemDuplicate) =>
          !listUpdate.value.some(
            (itemUpdate) => itemUpdate.objUUID === itemDuplicate.objUUID
          )
      );
    } else {
      listComponentDuplicateMapping = listComponentDuplicate.value;
    }

    [
      ...listAdd.value,
      ...listUpdate.value,
      ...listComponentDuplicateMapping,
    ].forEach((item) => {
      data.push({
        offerUuid: props.isAdd
          ? offerUuid.value
          : selectedProduct.value?.objUUID,
        objUuid: item?.objUUID || item.objUuid,
        relationValidStartDtm: isDuplicate.value
          ? formatDate(new Date())
          : item?.relationValidStartDtm
            ? item?.relationValidStartDtm
            : null,
        relationValidEndDtm: isDuplicate.value
          ? null
          : item?.relationValidEndDtm
            ? item?.relationValidEndDtm
            : null,
        workTypeCode: item.workTypeCode,
      });
    });
    try {
      const res = await productStoreSelected.value.updateProductStructure({
        offerUuid:
          props.isAdd || isDuplicate.value
            ? offerUuid.value
            : selectedProduct.value?.objUUID,
        componentList: data,
        chgUser: userInfor.chgUser,
        chgDeptName: userInfor.chgDeptName,
      });
      if (res && res?.status === 200) {
        snackbarStore.showSnackbar(
          t("product_platform.successfully_saved"),
          "success"
        );
        showActionSave.value = false;
        isCreatedStructure.value = true;
        structureStore.resetDragDrop();
        closeListStructure();
        closePopupSave();
        if (!isPendingFinish.value) {
          emit("duplicateComponentSuccess");
        }
      }
    } catch (err: any) {
      if (err?.errorMsg && Array.isArray(JSON.parse(err.errorMsg))) {
        snackbarStore.showSnackbar(t("product_platform.overlapDate"), "error");
      } else {
        snackbarStore.showSnackbar(
          err?.errorMsg || t("product_platform.something_went_wrong"),
          "error"
        );
      }
    }
  }
};

const closeStructureDetailPane = () => {
  if (!props.isAdd) {
    selectedComponent.value = null;
    selectedStructureData.value = null;
    showStructureDetail.value = false;
  }
  showComponentDetail.value = false;
};

const refreshPage = async (res) => {
  if (isDuplicate.value) {
    emit("duplicateDetailSuccess", res);
  } else {
    await getOfferDetail(res);
    emit("refreshPage");
  }
};

const handleClickDrop = async () => {
  if (props.isAdd && !isCreated.value) {
    snackbarStore.showSnackbar(
      t("product_platform.create_offer_first"),
      "error"
    );
    return;
  }
  if (props.isAdd) {
    showActionSave.value = true;
  }
  emit("handleClickDrop");
};

const initData = async (isReset: boolean = false) => {
  if (!selectedStructureData.value || isReset) {
    try {
      const res = await productStoreSelected.value.initProductCreate({
        itemCode: props.type,
      });
      handleResponse(res?.data);
    } catch (err: any) {
      snackbarStore.showSnackbar(
        err?.errorMsg || t("product_platform.something_went_wrong"),
        "error"
      );
    }
  }
};

watch(
  () => categoryEditModeSelected.value,
  async (newVal) => {
    if (newVal) {
      await categoryOfferCreateStore.getCtgPathRequest(newVal?.ctgrNodeUuid);
      let dataMap = {
        ...selectedStructureData.value,
        general: selectedStructureData.value?.general.map((i) => {
          if (i.colName === "ctgr_node_uuid") {
            return {
              ...i,
              valName: newVal?.ctgrNodeName,
              attrVal: newVal?.ctgrNodeUuid,
              attrPath: pathCategorySelected.value,
            };
          }
          return i;
        }),
      };

      handleResponse(dataMap, true);
    }
  },
  {
    deep: true,
    immediate: true,
  }
);

const getListOfferType = async () => {
  const { data } = await getListItemCodeApi({
    lItemCode: LargeItemCode.Offer,
  });
  optionsType.value = data || [];
};

onMounted(async () => {
  await Promise.all([filteredRulesByOffer(), getListOfferType()]);
  handleCanvasMatrix();
  if (!props.isAdd) {
  }
  if (getDetailFromOtherPage.value) {
    await clickCard();
  }
});

watch(
  () => props.listStructure,
  () => {
    nextTick(() => {
      handleCanvasMatrix();
    });
  },
  { deep: true }
);

watch(showListStructure, (value) => {
  if (value) {
    nextTick(() => {
      filteredRulesByOffer();
    });
  }
});

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
  reChooseCategoryMode.value = true;
  if (props.duplicate) {
    isFormOfferDuplicate.value = true;
  } else {
    isFormOfferEdit.value = true;
  }
  itemTypeOffer.value = selectedStructureData.value?.general.find(
    (i) => i.colName === "item_code"
  )?.attrVal;
  const menu = findMenuItem(menuTree.value, MenuItemID.TreeView);
  if (!menu) return;
  menu["path"] = configPath(menu);
  let itemSelectedUuid = selectedStructureData.value?.general?.find(
    (i) => i.colName === "ctgr_node_uuid"
  )?.attrVal;

  if (categoryEditModeSelected.value) {
    categoryStore.setSelectedCategoryTreeNode({
      ...categoryEditModeSelected.value,
    });
  } else {
    if (itemSelectedUuid) {
      await categoryStore.fetchTabsCategory();

      categoryStore.setCategoryTab(
        optionsType.value
          .find((i) => i.itemCode === itemTypeOffer.value)
          ?.cmcdDetlNm?.toUpperCase()
          .replace("-", "")
      );
      await categoryStore.getTreeCategory();
      let itemSelect = findObjectById(
        categoryStore.getTreeData,
        itemSelectedUuid
      );
      if (itemSelect) {
        categoryStore.setSelectedCategoryTreeNode(itemSelect);
      }
    }
  }
  addTab(menu);
};

provide("redirectCategoryPage", redirectCategoryPage);
</script>
