import { v4 as uuidv4 } from "uuid";
import cloneDeep from "lodash-es/cloneDeep";
import isEqual from "lodash-es/isEqual";
import useSnackbarStore from "../snackbar.store";
import useCmcdStore from "../cmcd.store";
import { getLabelList } from "@/api/prod/labelApi";
import type {
  ILabelItem,
  ILabelSearchParams,
  IPagination,
} from "@/interfaces/admin/label-management";
import {
  getItemDetail,
  getLisItemsView,
  getListAttributeCode,
  getLoupItems,
  saveItemView,
} from "@/api/prod/attributeApi";
import type {
  IAttributeCode,
  IAttributeCodeParams,
  ILoupItems,
} from "@/interfaces/admin/attribute-management";
import { useI18n } from "vue-i18n";
import { TABS_OFFER_DETAILS } from "@/constants/offer";
import {
  DEFAULT_PAGINATION,
  DEFAULT_SEARCH_PARAMS,
  DEFAULT_VALUE_ITEM_DETAIL,
} from "@/constants/admin/attribute";
import { getUserInfor } from "@/constants/userInfor";

export interface IItemView {
  id: string;
  name: string;
  code: string;
  isSelected: boolean;
  isOpenChild: boolean;
  isNew: boolean;
  isUsed: boolean;
  parentCode: string;
  parentName: string;
  largeItemCode: string;
  largeItemName: string;
  middleItemCode: string;
  middleItemName: string;
  children: IItemView[];
}

export interface IAdditionalItem {
  id: string;
  code: string;
  labelId: string;
  labelName: string;
  fieldType: string;
  maxLength: number;
  attributeCode: string;
  dispTab: string;
  isDispCard: boolean;
  isRequired: boolean;
  isAdvancedSearch: boolean;
  isUsed: boolean;
  sort: number;
  isExpaned: boolean;
  isNew?: boolean;
}

export interface ILoupItem {
  id: string;
  code: string;
  name: string;
  isNew: boolean;
  strcTypeCode: "S" | "M";
}

export interface IGeneralInfo {
  item: string;
  itemCode: string;
  itemName: string;
  used: boolean;
  isNew: boolean;
  sortNo: number | null;
  upperItems: ILoupItem[];
  lowerItems: ILoupItem[];
  largeItemCode: string;
  largeItemName: string;
  middleItemCode: string;
  middleItemName: string;
}

export interface ITreeViewInfo {
  nodeName: string;
  nodeId: string;
  responsibleDept: string;
  responsibleUser: string;
  creationDate: string;
  overview: string;
  ctgrTabUuid: string;
}

export interface IItemDetail {
  generalInfo: IGeneralInfo;
  additionalInfo: IAdditionalItem[];
  treeViewInfo: ITreeViewInfo;
}

interface Option {
  name: string;
  value: string;
}

const attributeManagementStore = defineStore("attributeManagementStore", () => {
  const { showSnackbar } = useSnackbarStore();
  const cmndStore = useCmcdStore();
  const { t } = useI18n();
  const listItems = ref<IItemView[]>([]);
  const listTypes = ref<IItemView[]>([]);
  const listLabel = ref<ILabelItem[]>([]);
  const listFieldType = ref<Option[]>([]);
  const listCommCode = ref<IAttributeCode[]>([]);
  const selectedLabel = ref<ILabelItem | null>(null);
  const selectedCommCode = ref<IAttributeCode | null>(null);
  const selectedItemViewCode = ref("");
  const selectedTypeViewCode = ref<string[]>([]);
  const isEditModeItemView = ref(false);
  const isEditModeItemEdit = ref(false);
  const selectedItem = ref<IItemView | null>();
  const selectedTab = ref<string>(TABS_OFFER_DETAILS.GENERAL);
  const itemDetail = ref<IItemDetail>(cloneDeep(DEFAULT_VALUE_ITEM_DETAIL));
  const listLabelID = ref<String[]>([]);
  const originItemDetail = ref<IItemDetail | null>(null);
  const loupItems = ref<ILoupItems | null>(null);
  const itemDetailClone = ref<IItemDetail | null>(null);
  const selectedAdditionalId = ref("");
  const isShowLabelSearch = ref<boolean>(false);
  const isShowAttributeCodeSearch = ref<boolean>(false);

  const labelSearchParams = reactive<ILabelSearchParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );
  const searchParamsHistory = ref<ILabelSearchParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );

  const commCodeSearchParams = reactive<IAttributeCodeParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );
  const searchParamsCommonCodeHistory = ref<IAttributeCodeParams>(
    cloneDeep(DEFAULT_SEARCH_PARAMS)
  );

  const labelPagination = reactive<IPagination>(cloneDeep(DEFAULT_PAGINATION));

  const commCodePagination = reactive<IPagination>(
    cloneDeep(DEFAULT_PAGINATION)
  );

  const getListFieldTypes = async (): Promise<void> => {
    try {
      const code = "U00001";
      const response = await cmndStore.search([code]);
      listFieldType.value = response[code as string].map(({ cmcdDetlId }) => {
        return { name: cmcdDetlId, value: cmcdDetlId };
      });
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const updateItemDetail = (data, isEditMode = false) => {
    const userInfor = getUserInfor();
    const general = data.general;
    const treeView = data.treeView || {};
    const itemDetailMapping = {
      generalInfo: {
        item: general.largeItemName,
        largeItemCode: general.largeItemCode,
        largeItemName: general.largeItemName,
        middleItemCode: general.middleItemCode,
        middleItemName: general.middleItemName,
        itemCode: general.itemCode,
        itemName: general.itemName,
        used: general.useYn === "Y",
        isNew: !general.itemCode,
        sortNo: general.sortNo,
        upperItems: general.upperItems.map((item) => ({
          id: item.itemCode,
          code: item.itemCode,
          name: item.itemName,
          isNew: false,
          strcTypeCode: item.strcTypeCode || "M",
        })),
        lowerItems: general.lowerItems.map((item) => ({
          id: item.itemCode,
          code: item.itemCode,
          name: item.itemName,
          isNew: false,
          strcTypeCode: item.strcTypeCode || "M",
        })),
      },
      additionalInfo: data.additionals.map((item) => ({
        id: item.attrUuid,
        code: item.itemCode,
        labelId: item.labelId,
        labelName: item.labelId ? t(item.labelId) : "",
        fieldType: item.fieldTypeCode,
        maxLength: !["DP", "DM", "DL", "OB"].includes(item.fieldTypeCode)
          ? item.attrMaxLength
          : 0,
        attributeCode: ["DL", "DM", "OB"].includes(item.fieldTypeCode)
          ? item.commGroupCode
          : "",
        dispTab: item.dispTab,
        isDispCard: item.dispCardYn === "Y",
        isRequired: item.requiredYn === "Y",
        isAdvancedSearch: item.advSearchYn === "Y",
        isUsed: item.useYn === "Y",
        isExpaned: item.isExpaned || false,
        sort: item.sortNo,
        isNew: item.isNew || false,
      })),
      treeViewInfo: {
        nodeName: treeView.ctgrNodeName || "",
        nodeId: treeView.ctgrNodeUuid || "",
        responsibleDept: treeView.chgDeptName || userInfor?.chgDeptName,
        responsibleUser: treeView.chgUser || userInfor?.chgUser,
        creationDate: treeView.rgstDtm || "",
        overview: treeView.ctgrOvwCntn || "",
        ctgrTabUuid: treeView.ctgrTabUuid || "",
      },
    };
    itemDetail.value = itemDetailMapping;
    //TODO
    listLabelID.value = data.additionals.map((item) => item.labelId);
    if (isEditMode) {
      originItemDetail.value = cloneDeep(itemDetail.value);
    }
  };

  const getListItemsApi = async (): Promise<void> => {
    try {
      const response = await getLisItemsView();
      listItems.value = response.data.map((item) => {
        const child =
          item.items.length > 0
            ? item.items[0]
            : item.middleItems.length > 0
              ? item.middleItems[0]
              : null;
        return {
          id: item.code,
          name: item.name,
          code: item.code,
          isSelected: false,
          isOpenChild: false,
          isNew: false,
          isUsed: false,
          parentCode: "",
          parentName: "",
          largeItemCode: item.code,
          largeItemName: item.name,
          middleItemCode: child?.middleItemCode || "",
          middleItemName: child?.middleItemName || "",
          children: [],
        };
      });
      listTypes.value = response.data
        .map((item) => [...item.items, ...item.middleItems])
        .flat()
        .map((item) => ({
          id: item.itemCode,
          name: item.itemName,
          code: item.itemCode,
          parentCode: item.largeItemCode,
          parentName: item.largeItemName,
          largeItemCode: item.largeItemCode,
          largeItemName: item.largeItemName,
          middleItemCode: item.middleItemCode,
          middleItemName: item.middleItemName,
          isSelected: false,
          isOpenChild: false,
          isNew: false,
          isUsed: item?.items?.length ? true : item.useYn === "Y",
          children:
            item?.items?.map((item) => ({
              id: item.itemCode,
              name: item.itemName,
              code: item.itemCode,
              parentCode: item.middleItemCode,
              parentName: item.middleItemName,
              largeItemCode: item.largeItemCode,
              largeItemName: item.largeItemName,
              middleItemCode: item.middleItemCode,
              middleItemName: item.middleItemName,
              isSelected: false,
              isOpenChild: false,
              isNew: false,
              isUsed: item.useYn === "Y",
              children: [],
            })) || [],
        }));
      updateSelectedItem();
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const getItemDetailApi = async (payload) => {
    try {
      const response = await getItemDetail(payload);
      updateItemDetail(response.data, true);
      cloneDetailItem({});
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const getLoupItemsApi = async (largeItemCode: string) => {
    try {
      const response = await getLoupItems({ largeItem: largeItemCode });
      loupItems.value = response.data;
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const getListLabel = async (
    isPagingClick: boolean = false
  ): Promise<void> => {
    try {
      const paramsSearch = { ...labelSearchParams };
      if (isPagingClick) {
        Object.assign(paramsSearch, {
          ...searchParamsHistory.value,
          page: labelSearchParams.page,
          size: labelSearchParams.size,
        });
      } else {
        searchParamsHistory.value = { ...labelSearchParams };
      }
      const response = await getLabelList(paramsSearch);
      listLabel.value = response.data.elements;
      labelPagination.currentPage = labelSearchParams.page;
      labelPagination.pageSize = labelSearchParams.size;
      labelPagination.totalItems = response.data.totalElements;
      labelPagination.totalSearchItems = response.data.totalElements;
      labelPagination.totalPages = Math.ceil(
        response.data.totalElements / labelSearchParams.size
      );
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const getListCommCode = async (
    isPagingClick: boolean = false
  ): Promise<void> => {
    try {
      const paramsSearch = { ...commCodeSearchParams };
      if (isPagingClick) {
        Object.assign(paramsSearch, {
          ...searchParamsCommonCodeHistory.value,
          page: commCodeSearchParams.page,
          size: commCodeSearchParams.size,
        });
      } else {
        searchParamsCommonCodeHistory.value = { ...commCodeSearchParams };
      }
      const response = await getListAttributeCode(paramsSearch);
      listCommCode.value = response.data.data;
      commCodePagination.currentPage = commCodeSearchParams.page;
      commCodePagination.pageSize = commCodeSearchParams.size;
      commCodePagination.totalItems = response.data.totalItems;
      commCodePagination.totalSearchItems = response.data.totalItems;
      commCodePagination.totalPages = Math.ceil(
        response.data.totalItems / commCodeSearchParams.size
      );
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
  };

  const handleResetSearchLabel = (): void => {
    selectedLabel.value = null;
    Object.assign(labelSearchParams, DEFAULT_SEARCH_PARAMS);
    Object.assign(labelPagination, DEFAULT_PAGINATION);
    getListLabel();
  };

  const handleResetSearchCommCode = (): void => {
    selectedCommCode.value = null;
    Object.assign(commCodeSearchParams, DEFAULT_SEARCH_PARAMS);
    Object.assign(commCodePagination, DEFAULT_PAGINATION);
    getListCommCode();
  };

  const setOpenType = (id) => {
    const currentItem = listItems.value.find((item) => item.id === id);
    if (currentItem && !currentItem.isOpenChild) {
      selectedItemViewCode.value = currentItem.code;
    } else {
      selectedItemViewCode.value = "";
    }
    listItems.value = listItems.value.map((item) => {
      return item.id === id
        ? { ...item, isOpenChild: !item.isOpenChild }
        : { ...item, isOpenChild: false };
    });
  };

  const setOpenChild = (id) => {
    listTypes.value = listTypes.value.map((item) => {
      return item.id === id
        ? { ...item, isOpenChild: !item.isOpenChild }
        : item;
    });
    const currentItem = listTypes.value.find((item) => item.id == id);
    if (currentItem) {
      if (currentItem.isOpenChild) {
        selectedTypeViewCode.value.push(currentItem.code);
      } else {
        selectedTypeViewCode.value = selectedTypeViewCode.value.filter(
          (item) => item !== currentItem.code
        );
      }
    }
  };

  const setSelectedType = (id) => {
    const currentType = listTypes.value.find((item) => item.id === id);
    if (currentType?.children.length === 0) {
      listTypes.value = listTypes.value.map((item) => ({
        ...item,
        isSelected: item.id === id,
        children: item.children.map((child) => ({
          ...child,
          isSelected: false,
        })),
      }));
      selectedItem.value = currentType;
    }
  };

  const setSelectedSubType = (id) => {
    const currentTypeItem = listTypes.value.find((item) =>
      item.children.find((child) => child.id === id)
    );

    listTypes.value = listTypes.value.map((item) => {
      return {
        ...item,
        isSelected: item.code === currentTypeItem?.code,
        children: item.children.map((child) => ({
          ...child,
          isSelected: child.id === id,
        })),
      };
    });
    const listSubType = listTypes.value.map((item) => item.children).flat();
    selectedItem.value = listSubType.find((item) => item.id === id);
  };

  const addSubType = (id) => {
    const currentType = listTypes.value.find((item) => item.id === id);
    if (currentType) {
      const subTypeItemNew = {
        id: uuidv4(),
        name: "",
        code: "",
        parentCode: currentType.code || "",
        parentName: currentType.name,
        largeItemCode: currentType.largeItemCode,
        largeItemName: currentType.largeItemName,
        middleItemCode: currentType.code,
        middleItemName: currentType.name,
        isSelected: true,
        isOpenChild: false,
        isNew: true,
        isUsed: true,
        sortNo: null,
        children: [],
      };
      listTypes.value = listTypes.value.map((item) => {
        return item.id === id
          ? {
              ...item,
              isSelected: true,
              isOpenChild: true,
              children: [
                ...item.children.map((child) => ({
                  ...child,
                  isSelected: false,
                })),
                subTypeItemNew,
              ],
            }
          : {
              ...item,
              isSelected: false,
              children: item.children.map((child) => ({
                ...child,
                isSelected: false,
              })),
            };
      });
      selectedItem.value = subTypeItemNew;
      isEditModeItemView.value = true;
      isEditModeItemEdit.value = true;
      originItemDetail.value = null;
      updateItemDetail({
        general: {
          itemCode: "",
          itemName: "",
          useYn: "Y",
          largeItemCode: currentType.largeItemCode,
          largeItemName: currentType.largeItemName,
          middleItemCode: currentType.code,
          middleItemName: currentType.name,
          sortNo: null,
          upperItems: [],
          lowerItems: [],
        },
        additionals: [
          {
            id: uuidv4(),
            code: "",
            labelId: "",
            labelName: "",
            fieldType: "",
            maxLength: null,
            attributeCode: "",
            dispTab: "G",
            isDispCard: false,
            requiredYn: "Y",
            advSearchYn: "Y",
            useYn: "Y",
            isExpaned: true,
            sort: 0,
            isNew: true,
          },
        ],
      });
    }
  };

  const addType = (id) => {
    const userInfor = getUserInfor();
    const currentItem = listItems.value.find((item) => item.id === id);
    if (currentItem) {
      const typeItemNew = {
        id: uuidv4(),
        name: "",
        code: "",
        parentCode: currentItem.code || "",
        parentName: currentItem.name,
        largeItemCode: currentItem.largeItemCode,
        largeItemName: currentItem.largeItemName,
        middleItemCode: currentItem.middleItemCode,
        middleItemName: currentItem.middleItemName,
        isSelected: true,
        isOpenChild: false,
        isNew: true,
        isUsed: true,
        sortNo: null,
        children: [],
      };
      //Update selected code
      selectedItemViewCode.value = currentItem.code;

      // Update list item
      listItems.value = listItems.value.map((item) => {
        return {
          ...item,
          isOpenChild: item.id == id,
        };
      });
      // Update other item selected = false
      listTypes.value = listTypes.value.map((item) => {
        return { ...item, isSelected: false };
      });
      // add new item
      listTypes.value.push(typeItemNew);
      selectedItem.value = typeItemNew;
      isEditModeItemView.value = true;
      isEditModeItemEdit.value = true;
      originItemDetail.value = null;
      updateItemDetail({
        general: {
          itemCode: "",
          itemName: "",
          useYn: "Y",
          largeItemCode: currentItem.largeItemCode,
          largeItemName: currentItem.largeItemName,
          middleItemCode: currentItem.middleItemCode,
          middleItemName: currentItem.middleItemName,
          sortNo: null,
          upperItems: [],
          lowerItems: [],
        },
        additionals: [
          {
            id: uuidv4(),
            code: "",
            labelId: "",
            labelName: "",
            fieldType: "",
            maxLength: null,
            attributeCode: "",
            dispTab: "G",
            isDispCard: false,
            requiredYn: "Y",
            advSearchYn: "Y",
            useYn: "Y",
            isExpaned: true,
            sort: 0,
            isNew: true,
          },
        ],
        treeView: {
          ctgrNodeUuid: "",
          ctgrNodeName: "",
          chgDeptName: userInfor?.chgDeptName,
          chgUser: userInfor?.chgUser,
          ctgrOvwCntn: "",
        },
      });
    }
  };

  const checkIsExistAddNewItem = () => {
    return listTypes.value
      .map((item) => [item, ...item.children])
      .flat()
      .some((item) => item.isNew);
  };

  const addAdditionalItem = () => {
    const newItem = {
      id: uuidv4(),
      code: "",
      labelId: "",
      labelName: "",
      fieldType: "",
      maxLength: 0,
      attributeCode: "",
      dispTab: "G",
      isDispCard: false,
      isRequired: true,
      isAdvancedSearch: true,
      isUsed: true,
      sort: 0,
      isExpaned: true,
      isNew: true,
    };
    itemDetail.value.additionalInfo.push(newItem);
    return newItem;
  };

  const addLoupItem = (type) => {
    const newItem: ILoupItem = {
      id: uuidv4(),
      code: "",
      name: "",
      isNew: true,
      strcTypeCode: "M",
    };
    if (type === "upper") {
      itemDetail.value.generalInfo.upperItems.push(newItem);
    } else {
      itemDetail.value.generalInfo.lowerItems.push(newItem);
    }
  };

  const removeLoupItem = (id, type) => {
    if (type === "upper") {
      itemDetail.value.generalInfo.upperItems =
        itemDetail.value.generalInfo.upperItems.filter(
          (item) => item.id !== id
        );
    } else {
      itemDetail.value.generalInfo.lowerItems =
        itemDetail.value.generalInfo.lowerItems.filter(
          (item) => item.id !== id
        );
    }
  };

  const updateIsExpaned = (id: string) => {
    itemDetail.value.additionalInfo = itemDetail.value.additionalInfo.map(
      (item) => {
        return item.id === id ? { ...item, isExpaned: !item.isExpaned } : item;
      }
    );
  };

  const showLabelSearch = (id: string) => {
    selectedAdditionalId.value = id;
    isShowLabelSearch.value = true;
    isShowAttributeCodeSearch.value = false;
  };

  const showAttributeCodeSearch = (id: string) => {
    selectedAdditionalId.value = id;
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = true;
  };

  const isValidItemDetail = () => {
    const maxNumFieldAllowed = 2;
    let dpCount = 0;
    let nonDpCount = 0;

    const error = {
      isEmptyField: false,
      isNotValidMaxLength: false,
      isNotValidTA: false,
      isMaxDPField: false,
      isMaxNonDPField: false,
      isDuplicateField: false,
      isNoneSelected: false,
      isNotValidCode: false,
    };

    if (isEqual(itemDetail.value, DEFAULT_VALUE_ITEM_DETAIL)) {
      return { ...error, isNoneSelected: true };
    }

    for (const item of itemDetail.value.additionalInfo) {
      if (!item.labelId || !item.fieldType) {
        return { ...error, isEmptyField: true };
      }

      if (
        itemDetail.value.additionalInfo.find(
          (addItem) =>
            addItem.labelId === item.labelId && addItem.id !== item.id
        )
      ) {
        return { ...error, isDuplicateField: true };
      }

      switch (item.fieldType) {
        case "TA":
          if (item.isDispCard) return { ...error, isNotValidTA: true };
          if (item.maxLength < 0)
            return { ...error, isNotValidMaxLength: true };
          break;

        case "DP":
          if (item.isDispCard) dpCount++;
          break;
        case "TF":
        case "NF":
        case "RF":
        case "DL":
        case "DM":
          if (item.isDispCard) nonDpCount++;
          if (["DM", "DL"].includes(item.fieldType)) {
            if (!item.attributeCode) return { ...error, isEmptyField: true };
          } else {
            if (item.maxLength < 1)
              return { ...error, isNotValidMaxLength: true };
          }
          break;
        default:
          break;
      }
    }

    if (dpCount > maxNumFieldAllowed) return { ...error, isMaxDPField: true };
    if (nonDpCount > maxNumFieldAllowed)
      return { ...error, isMaxNonDPField: true };

    if (!itemDetail.value.generalInfo.itemCode) {
      return { ...error, isEmptyField: true };
    }
    if (!/^[a-zA-Z]{2,}$/.test(itemDetail.value.generalInfo.itemCode)) {
      return { ...error, isNotValidCode: true };
    }
    if (
      !itemDetail.value.generalInfo.itemCode ||
      !itemDetail.value.generalInfo.itemName ||
      (!itemDetail.value.treeViewInfo.nodeName &&
        itemDetail.value.generalInfo.largeItemCode === "O")
    ) {
      return { ...error, isEmptyField: true };
    }

    return error;
  };

  const saveAllAttributes = async (): Promise<boolean> => {
    const error = isValidItemDetail();
    if (error.isNoneSelected) {
      showSnackbar(
        t("product_platform.please_add_new_or_select_one_attribute"),
        "error"
      );
      return false;
    }
    if (error.isEmptyField) {
      showSnackbar(t("product_platform.required_field_missing"), "error");
      return false;
    }
    if (error.isNotValidCode) {
      showSnackbar(t("product_platform.code_error"), "error");
      return false;
    }
    if (error.isNotValidMaxLength) {
      showSnackbar(t("product_platform.max_length_error"), "error");
      return false;
    }
    if (error.isNotValidTA) {
      showSnackbar(t("product_platform.disp_card_ta_error"), "error");
      return false;
    }
    if (error.isMaxDPField) {
      showSnackbar(t("product_platform.disp_card_max_2_dp"), "error");
      return false;
    }
    if (error.isMaxNonDPField) {
      showSnackbar(t("product_platform.disp_card_max_2_none_dp"), "error");
      return false;
    }
    if (error.isDuplicateField) {
      showSnackbar(t("product_platform.duplicate_label_id"), "error");
      return false;
    }
    try {
      const data = transformItemDetailToItemRequest(itemDetail.value);
      const response = await saveItemView(data);
      if (response.status === 200) {
        showSnackbar(t("product_platform.saveSuccessfully"), "success");
        const payload = {
          code: itemDetail.value.generalInfo.itemCode,
          largeItemCode: itemDetail.value.generalInfo.largeItemCode,
          middleItemCode: itemDetail.value.generalInfo.middleItemCode,
        };
        await getListItemsApi();
        await getItemDetailApi(payload);
        if (
          itemDetail.value.generalInfo.largeItemName ===
          itemDetail.value.generalInfo.middleItemName
        ) {
          setSelectedType(itemDetail.value.generalInfo.itemCode);
        } else {
          setSelectedSubType(itemDetail.value.generalInfo.itemCode);
        }
        return true;
      }
    } catch (error: any) {
      showSnackbar(error.errorMsg, "error");
    }
    return false;
  };

  const transformItemDetailToItemRequest = (data) => {
    const userInfor = getUserInfor();
    const treeView = {
      ctgrNodeUuid: data.treeViewInfo.nodeId || null,
      ctgrNodeName: data.treeViewInfo.nodeName || "",
      chgDeptName: data.treeViewInfo.responsibleDept || userInfor?.chgDeptName,
      chgUser: data.treeViewInfo.responsibleUser || userInfor?.chgUser,
      ctgrOvwCntn: data.treeViewInfo.overview,
      ctgrTabUuid: data.treeViewInfo.ctgrTabUuid,
    };
    return {
      general: {
        itemCode: data.generalInfo.itemCode,
        itemName: data.generalInfo.itemName,
        useYn: data.generalInfo.used ? "Y" : "N",
        largeItemCode: data.generalInfo.largeItemCode,
        largeItemName: data.generalInfo.largeItemName,
        middleItemCode: data.generalInfo.middleItemCode,
        middleItemName: data.generalInfo.middleItemName,
        sortNo: data.generalInfo.sortNo,
        upperItems: data.generalInfo.upperItems
          .filter((item) => item.code)
          .map((item) => ({
            itemCode: item.code,
            itemName: item.name,
            strcTypeCode: item.strcTypeCode,
          })),
        lowerItems: data.generalInfo.lowerItems
          .filter((item) => item.code)
          .map((item) => ({
            itemCode: item.code,
            itemName: item.name,
            strcTypeCode: item.strcTypeCode,
          })),
      },
      additionals: data.additionalInfo.map((item) => ({
        attrUuid: item.isNew ? null : item.id,
        itemCode: data.generalInfo.itemCode,
        fieldTypeCode: item.fieldType,
        commGroupCode: item.attributeCode || null,
        sortNo: null,
        useYn: item.isUsed ? "Y" : "N",
        attrMaxLength: item.maxLength || null,
        requiredYn: item.isRequired ? "Y" : "N",
        labelId: item.labelId,
        dispTab: item.dispTab,
        dispCardYn: item.isDispCard ? "Y" : "N",
        advSearchYn: item.isAdvancedSearch ? "Y" : "N",
      })),
      treeView: data.generalInfo.largeItemCode === "O" ? treeView : null,
    };
  };

  const resetStore = (): void => {
    listItems.value = [];
    listTypes.value = [];
    listLabel.value = [];
    listFieldType.value = [];
    listCommCode.value = [];
    selectedLabel.value = null;
    selectedCommCode.value = null;
    selectedItemViewCode.value = "";
    selectedTypeViewCode.value = [];
    isEditModeItemView.value = false;
    isEditModeItemEdit.value = false;
    selectedItem.value = null;
    selectedTab.value = TABS_OFFER_DETAILS.GENERAL;
    itemDetail.value = cloneDeep(DEFAULT_VALUE_ITEM_DETAIL);
    originItemDetail.value = null;
    loupItems.value = null;
    itemDetailClone.value = null;
    selectedAdditionalId.value = "";
    isShowLabelSearch.value = false;
    isShowAttributeCodeSearch.value = false;
  };

  const cloneDetailItem = (arg: { reset?: boolean }) => {
    if (arg.reset) {
      itemDetail.value = JSON.parse(JSON.stringify(itemDetailClone.value));
      itemDetailClone.value = null;
      updateItemsView({
        name: itemDetail.value?.generalInfo.itemName,
        code: itemDetail.value?.generalInfo.itemCode,
        isUsed: itemDetail.value?.generalInfo.used,
      });
    } else {
      itemDetailClone.value = JSON.parse(JSON.stringify(itemDetail.value));
    }
  };

  const resetItemDetail = () => {
    if (checkIsExistAddNewItem()) {
      listTypes.value = listTypes.value
        .filter((item) => !item.isNew)
        .map((item) => {
          return {
            ...item,
            children: item.children.filter((child) => !child.isNew),
          };
        });
    }
  };

  const checkDetailItemIsDirty = () => {
    const itemDetailString = JSON.stringify(itemDetail.value);
    const itemDetailCloneString = JSON.stringify(itemDetailClone.value);
    return itemDetailString !== itemDetailCloneString;
  };

  const updateItemsView = (item) => {
    const getFieldValue = (itemType, fieldName) => {
      return (itemType.code === item.code || itemType.isNew) &&
        itemType.isSelected
        ? item[fieldName as string]
        : itemType[fieldName as string];
    };

    listTypes.value = listTypes.value.map((itemType) => {
      const newName = getFieldValue(itemType, "name");
      const isUsedType = getFieldValue(itemType, "isUsed");
      return {
        ...itemType,
        name: newName,
        isUsed: isUsedType,
        children: itemType.children.map((child) => {
          const newName = getFieldValue(child, "name");
          const isUsedChild = getFieldValue(child, "isUsed");
          return { ...child, name: newName, isUsed: isUsedChild };
        }),
      };
    });
  };

  const updateSelectedItem = () => {
    const selectedCode = selectedItem.value?.code;
    const selectedParentCode = selectedItem.value?.parentCode;
    listItems.value = listItems.value.map((item) => {
      return {
        ...item,
        isOpenChild: item.code === selectedItemViewCode.value,
        isSelected: item.code === selectedItemViewCode.value,
      };
    });
    listTypes.value = listTypes.value.map((type) => {
      return {
        ...type,
        isOpenChild: selectedTypeViewCode.value.includes(type.code),
        isSelected:
          type.code === selectedCode || type.code === selectedParentCode,
        children: type.children.map((child) => {
          return { ...child, isSelected: child.code === selectedCode };
        }),
      };
    });
  };

  return {
    listItems,
    listTypes,
    listLabel,
    listCommCode,
    listFieldType,
    selectedLabel,
    selectedCommCode,
    labelSearchParams,
    labelPagination,
    commCodeSearchParams,
    commCodePagination,
    selectedItemViewCode,
    selectedAdditionalId,
    isEditModeItemView,
    isEditModeItemEdit,
    selectedItem,
    selectedTab,
    itemDetail,
    isShowLabelSearch,
    isShowAttributeCodeSearch,
    loupItems,
    originItemDetail,
    listLabelID,
    setOpenType,
    setOpenChild,
    setSelectedType,
    setSelectedSubType,
    addSubType,
    addType,
    checkIsExistAddNewItem,
    updateItemDetail,
    addAdditionalItem,
    addLoupItem,
    removeLoupItem,
    getListLabel,
    getListCommCode,
    resetStore,
    showLabelSearch,
    showAttributeCodeSearch,
    getListItemsApi,
    updateIsExpaned,
    cloneDetailItem,
    checkDetailItemIsDirty,
    saveAllAttributes,
    resetItemDetail,
    getItemDetailApi,
    getLoupItemsApi,
    updateItemsView,
    updateSelectedItem,
    getListFieldTypes,
    handleResetSearchLabel,
    handleResetSearchCommCode,
  };
});

export default attributeManagementStore;
