import { getUiImpactAnalysisSelectItem } from "@/api/prod/impactAnalysisApi";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import { EXTENDS_VIEW, SEARCH_CATEGORY } from "@/constants/extendsManager";
import {
  NM_CD_FIELDS,
  TARGET_TYPE,
  TARGET_TYPE_CODE,
} from "@/constants/impactAnalysis";
import { getProductStructureDetailRootApi } from "@/api/prod/productApi";
import { OFFER_CODE_TYPE, OFFER_TABS_VALUE } from "@/constants/offer";
import { PRODUCT_ITEM_CODE } from "@/constants/resource";
import { MenuItemID } from "@/enums/redirect";
import type { MenuItem } from "@/interfaces/prod/menu";
import {
  useCategoryStore,
  useCmCodeStore,
  useComponentStore,
  useExtendManagerStore,
  useExtendSearchStore,
  useHistoryTabStore,
  useImpactAnalysisStore,
  useLabelStore,
  useMenuStore,
  useMultiEntitySearchStore,
  useProductsStore,
  usePublishManagerStore,
  useRelationSearchStore,
  useResourceStore,
  useStructureStore,
  useFactorStore,
} from "@/store";
import useMatrixStructureStore from "@/store/admin/matrixStructure.store";
import {
  FactorItem,
  TableItem,
  type AddTabFunction,
  type CategoryItem,
  type ComponentItem,
  type MatrixItem,
  type MultiEntityItem,
  type OfferItem,
  type ResourceItem,
} from "@/types/common";
import { configPath, findMenuItem } from "@/utils/config-path";
import cloneDeep from "lodash-es/cloneDeep";
import { OFFER_TYPE, TABS_NAME_COLLECTION } from "../constants/index";
import { LargeItemCode, SearchBy } from "@/enums";
import { LABEL_SEARCH_TYPE } from "@/constants/admin/label";
import type { IAttributeCode } from "@/interfaces/admin/attribute-management";
import type { ILabelItem } from "@/interfaces/admin/label-management";
import { PUBLISH_TABS_VALUE } from "@/constants/publish";
import useTableStructureStore from "@/store/admin/tableStructure.store";

export default function useRedirect() {
  const { menuTree } = storeToRefs(useMenuStore());

  const addTabFunction = inject<AddTabFunction>(
    "addTab",
    (_item: MenuItem) => {}
  );

  const showComponentSearchPage = async (
    element: ComponentItem
  ): Promise<void> => {
    const { getListComponentSearchAdvance } = useComponentStore();
    const {
      componentSelected,
      resourceParamsFilter,
      showDetail,
      paramsFilterComponent,
      componentSearch,
      isEdit,
      showOfferSearch,
      isInOfferMode,
      showEntitySearch,
      selectedOffer,
      showResourceAdd,
    } = storeToRefs(useComponentStore());
    isEdit.value = false;
    isInOfferMode.value = false;
    showOfferSearch.value = false;
    showEntitySearch.value = false;
    showResourceAdd.value = false;
    const params: any = {
      mctgrItemCode: element.middleType,
      itemCode: element.offerType,
      objCode: element.code,
      page: 1,
      size: 14,
    };
    paramsFilterComponent.value.type = element.middleType;
    paramsFilterComponent.value.subType = element.offerType;
    paramsFilterComponent.value.searchKey = element.code;
    paramsFilterComponent.value.searchBy = SearchBy.Code;
    paramsFilterComponent.value.objUuid = null;
    selectedOffer.value = null;
    await getListComponentSearchAdvance(params);
    componentSelected.value = componentSearch.value.items[0];
    resourceParamsFilter.value.componentUUID = element.itemId;
    showDetail.value = true;
  };

  const showOfferSearchPage = async (element: OfferItem): Promise<void> => {
    const { structureData } = storeToRefs(useStructureStore());
    const {
      products,
      selectedProduct,
      productDetails,
      productType,
      showProductDetail,
      getDetailFromOtherPage,
      inputText,
      itemTypeSelected,
      selectedValue,
      productLineCoordinates,
    } = storeToRefs(useProductsStore());
    const { getStructure } = useStructureStore();
    const { getProducts } = useProductsStore();
    productType.value = element.itemCode;
    let itemCode = "";
    if (element?.itemCodeName) {
      const { data } = await getListItemCodeApi({
        lItemCode: LargeItemCode.Offer,
      });
      const currentItemCode = data.find(
        ({ itemName }) => itemName === element.itemCodeName
      );
      if (currentItemCode) {
        itemCode = currentItemCode.itemCode;
      }
    }
    await getProducts({
      page: 1,
      size: 7,
      objCode: element.objCode,
      itemCode: element.offerType ? element.offerType : itemCode,
    });
    productDetails.value = products.value[0];
    selectedProduct.value = products.value[0];
    inputText.value = products.value[0].objCode;
    itemTypeSelected.value = products.value[0].itemCode;
    selectedValue.value = OFFER_CODE_TYPE;
    const response = await getStructure({ objUuid: element?.objUuid });
    if (response?.data) {
      structureData.value = response?.data;
    } else {
      structureData.value = null;
    }
    productLineCoordinates.value = [];
    showProductDetail.value = true;
    getDetailFromOtherPage.value = true;
  };

  const showResourceSearchPage = async ({
    objCode,
    itemCode,
  }: ResourceItem): Promise<void> => {
    const { getResourceAdvancedList } = useResourceStore();
    const {
      showResourceDuplicate,
      resourceSelected,
      listResourceSearch,
      resourceParamsFilter: resourceParams,
      showDetail: showDetailResource,
      isEdit: isEditResource,
      showComponentSearch,
      isComponentGroupSearch,
      componentSelected,
    } = storeToRefs(useResourceStore());
    showComponentSearch.value = false;
    isComponentGroupSearch.value = false;
    componentSelected.value = null;
    resourceParams.value.itemCode = itemCode;
    resourceParams.value.keyword = objCode;
    resourceParams.value.resourceType = PRODUCT_ITEM_CODE;
    resourceParams.value.page = 1;
    resourceParams.value.size = 14;
    const params = { objCode, itemCode, page: 1, size: 14 };
    await getResourceAdvancedList(params);
    resourceSelected.value = listResourceSearch.value.items[0];
    showResourceDuplicate.value = false;
    isEditResource.value = false;
    showDetailResource.value = true;
  };

  const showMultiEntitySearchPage = async (
    item: MultiEntityItem
  ): Promise<void> => {
    const { getEntityList, getEntityDetailInfo } = useMultiEntitySearchStore();
    const {
      paramsMultiEntitySearch,
      selectedEntity,
      entityList,
      entityDisplayForm,
      entitySearchNmCd,
      isEdit: isEditMultiEntity,
      currentTab,
      selectedEntityDetails,
    } = storeToRefs(useMultiEntitySearchStore());
    entitySearchNmCd.value = SearchBy.Code;
    paramsMultiEntitySearch.value.page = 1;
    paramsMultiEntitySearch.value.size = 14;
    paramsMultiEntitySearch.value.type = item.itemCode;
    paramsMultiEntitySearch.value.subType = item.entityTypeCode;
    paramsMultiEntitySearch.value.searchBy = "objCode";
    paramsMultiEntitySearch.value.searchKey = item.multiEntityCode;
    currentTab.value = OFFER_TABS_VALUE.GENERAL;
    await getEntityList();
    selectedEntity.value = entityList.value.items[0];
    await getEntityDetailInfo();
    isEditMultiEntity.value = false;
    entityDisplayForm.value.entityDetail = true;
    setTimeout(() => {
      selectedEntityDetails.value = entityList.value.items[0];
    }, 500);
  };

  const showGroupSearchPage = async (item: any): Promise<void> => {
    const {
      displayForm,
      selectedGroup,
      paramsSearchGroup,
      selectedOffer,
      isOfferSearchGroup,
      isEdit,
    } = storeToRefs(useExtendSearchStore());
    const { getGroupList, getGroupDetailInfo } = useExtendSearchStore();
    const itemValue = {
      ...item,
      objUuid: item.itemId,
      objName: item.name,
      objCode: item.code,
      itemCode: item.type,
      offerGroupTypeCode: item.offerGroupTypeCode,
    };
    isOfferSearchGroup.value = false;
    isEdit.value = false;
    selectedGroup.value = itemValue;
    paramsSearchGroup.value.type = item.type;
    paramsSearchGroup.value.searchBy = SearchBy.Code;
    paramsSearchGroup.value.searchKey = item.code;
    selectedOffer.value = null;
    await getGroupList();
    await getGroupDetailInfo();
    displayForm.value.offerSearch = false;
    displayForm.value.groupDetail = true;
    displayForm.value.groupDuplicate = false;
    displayForm.value.addOffer = false;
  };

  const showRelationManagerPage = async (item: any): Promise<void> => {
    const {
      getExtendsListOfferSearch,
      resetStructureActiveMap,
      getGroupBySelectedItem,
      resetDetailViewData,
    } = useExtendManagerStore();
    const {
      paramsExtendsFilterOfferSearch,
      selectedNmCdOfferSearch,
      extendOfferSearch,
      selectedItem,
      extendsView,
    } = storeToRefs(useExtendManagerStore());
    selectedNmCdOfferSearch.value = SearchBy.Code;
    paramsExtendsFilterOfferSearch.value.searchBy = SearchBy.Code;
    paramsExtendsFilterOfferSearch.value.size = 7;
    paramsExtendsFilterOfferSearch.value.page = 1;
    paramsExtendsFilterOfferSearch.value.type = item.offerType;
    paramsExtendsFilterOfferSearch.value.subType = item.itemType;
    paramsExtendsFilterOfferSearch.value.searchKey = item.code;
    await getExtendsListOfferSearch(SEARCH_CATEGORY.OFFER);
    resetStructureActiveMap();
    resetDetailViewData();
    const relationItem = extendOfferSearch.value.items[0];
    selectedItem.value = relationItem;
    await getGroupBySelectedItem(
      extendsView.value === EXTENDS_VIEW.SIMPLE,
      false
    );
  };

  const showRelationSearchPage = async (item: any): Promise<void> => {
    const relationSearchStore = useRelationSearchStore();
    const historyStore = useHistoryTabStore();
    const {
      paramsExtendsRelationSearch,
      selectedNmCdRelationSearch,
      selectedRelation,
      isShowRelationDetail,
      initInput,
      extendRelationSearch,
      isEdit,
    } = storeToRefs(relationSearchStore);
    const { getRelationSearch, getExtendsDependencyRelationDefinitionDetail } =
      relationSearchStore;
    isEdit.value = false;
    selectedNmCdRelationSearch.value = NM_CD_FIELDS[1].value;
    initInput.value = item.code;
    paramsExtendsRelationSearch.value.objCode = item.code;
    paramsExtendsRelationSearch.value.objName = "";
    paramsExtendsRelationSearch.value.page = 1;
    paramsExtendsRelationSearch.value.size = 20;
    await getRelationSearch();
    selectedRelation.value = extendRelationSearch.value?.items?.[0];
    await getExtendsDependencyRelationDefinitionDetail(
      selectedRelation.value?.objUuid
    );
    await historyStore.fetchHistory({
      objUuid: selectedRelation.value?.objUuid,
    });
    isShowRelationDetail.value = true;
  };

  const showImpactAnalysisPage = async (item: any): Promise<void> => {
    const { targetSearchList, redirectFormPocket } = storeToRefs(
      useImpactAnalysisStore()
    );
    const impactAnalysisStore = useImpactAnalysisStore();
    redirectFormPocket.value = true;
    impactAnalysisStore.resetState();
    impactAnalysisStore.setShouldReset(false);
    impactAnalysisStore.setTargetSearchType(item.itemType);
    impactAnalysisStore.setTargetSearchCode(item.code);
    const { data } = await getUiImpactAnalysisSelectItem();
    const patternName =
      data?.find((patternItem) => patternItem.value === item.itemType)?.label ||
      "";
    impactAnalysisStore.setSearchPattern(patternName);
    if (item.itemType === "C") {
      impactAnalysisStore.setTargetSearchDetailType(item.middleType);
    } else {
      impactAnalysisStore.setTargetSearchDetailType(null);
    }
    impactAnalysisStore.setTargetSearchSubType(item.type);
    impactAnalysisStore.setSelectedNmCd(SearchBy.Code);
    impactAnalysisStore.setTargetSearchPageNo(1);
    impactAnalysisStore.setTargetSearchPageSize(6);
    await impactAnalysisStore.actionGetTargetSearchList();
    targetSearchList.value.items[0].active = true;
    const findItem = targetSearchList.value.items[0];
    switch (item.itemType) {
      case TARGET_TYPE_CODE.OFFER:
        impactAnalysisStore.setSearchPattern(TARGET_TYPE.OFFER);
        impactAnalysisStore.setShouldReset(true);
        impactAnalysisStore.resetState();
        nextTick(() => {
          impactAnalysisStore.setSelectedSearchItem(findItem);
          impactAnalysisStore.setIsShowOffer(true);
          impactAnalysisStore.setIsShowComponent(false);
          impactAnalysisStore.setIsShowResource(false);
        });
        break;
      case TARGET_TYPE_CODE.COMPONENT:
        impactAnalysisStore.setSearchPattern(TARGET_TYPE.COMPONENT);
        impactAnalysisStore.setShouldReset(true);
        impactAnalysisStore.resetState();
        nextTick(() => {
          impactAnalysisStore.setSelectedSearchItem(findItem);
          impactAnalysisStore.setIsShowOffer(true);
          impactAnalysisStore.setIsShowComponent(true);
          impactAnalysisStore.setIsShowResource(true);
        });
        break;
      case TARGET_TYPE_CODE.RESOURCE:
        impactAnalysisStore.setSearchPattern(TARGET_TYPE.RESOURCE);
        impactAnalysisStore.setShouldReset(true);
        impactAnalysisStore.resetState();
        nextTick(() => {
          impactAnalysisStore.setSelectedSearchItem(findItem);
          impactAnalysisStore.setIsShowOffer(false);
          impactAnalysisStore.setIsShowComponent(false);
          impactAnalysisStore.setIsShowResource(true);
        });
        break;
    }
  };

  const convertOfferType = (itemCode) => {
    if (!itemCode) {
      return "";
    }
    switch (itemCode) {
      case OFFER_TYPE.PRICEPLAN:
        return "P";
      case OFFER_TYPE.ADDON:
        return "A";
      case OFFER_TYPE.DISCOUNT:
        return "D";
      case OFFER_TYPE.DEVICE:
        return "V";
      default:
        return "";
    }
  };

  const showGroupWithOfferPage = async (item: any): Promise<void> => {
    const {
      displayForm,
      paramsSearchOffer,
      paramsSearchGroup,
      offerList,
      selectedOffer,
      isOfferSearchGroup,
      isResetValue,
    } = storeToRefs(useExtendSearchStore());
    const { getOfferList, getGroupList } = useExtendSearchStore();
    paramsSearchOffer.value.page = 1;
    paramsSearchOffer.value.size = 7;
    paramsSearchOffer.value.searchBy = SearchBy.Code;
    paramsSearchOffer.value.searchKey = item.code;
    paramsSearchOffer.value.type = item.offerType;
    selectedOffer.value = item;
    selectedOffer.value.objUuid = item?.itemId;
    await getOfferList(SEARCH_CATEGORY.OFFER);
    const { data } = await getProductStructureDetailRootApi({
      objUuid: item?.itemId,
    });
    offerList.value.items[0]["detail"] = data;
    paramsSearchGroup.value.childOffrUuid = item?.itemId;
    paramsSearchGroup.value.offerGroupTypeCode = convertOfferType(item.code);
    isOfferSearchGroup.value = true;
    isResetValue.value = false;
    paramsSearchGroup.value.page = 1;
    paramsSearchGroup.value.size = displayForm.value.offerSearch ? 7 : 14;
    await getGroupList();
    displayForm.value.offerSearch = true;
    displayForm.value.groupDetail = false;
  };

  const showCategoryPage = async (cat: CategoryItem): Promise<void> => {
    const categoryStore = useCategoryStore();
    await categoryStore.fetchTabsCategory();
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Offer,
    });
    const offerTypes = data || [];
    categoryStore.setCategoryTab(
      offerTypes
        .find((item) => item.itemCode === cat.itemCode)
        ?.cmcdDetlNm?.toUpperCase()
        .replace("-", "")
    );
    await categoryStore.getTreeCategory();
    const itemSelect = findObjectById(categoryStore.getTreeData, cat.objUuid);
    if (itemSelect) {
      categoryStore.setSelectedCategoryTreeNode(itemSelect);
    }
  };

  const showMatrixBuilderPage = async (mtx: MatrixItem): Promise<void> => {
    const matrixStructureStore = useMatrixStructureStore();
    const {
      paramMatrixSearch,
      listMatrix,
      matrixSelected,
      matrixBuilderFactors,
      listTableMatrix,
      listTableMatrixTemp,
      headersTableMatrix,
      listTempItems,
    } = storeToRefs(matrixStructureStore);
    const {
      getListMatrix,
      resetDataTableMatrix,
      getHeaderTableMatrix,
      getListTableMatrix,
    } = matrixStructureStore;
    paramMatrixSearch.value.matrixCodeName = mtx.matrixCodeName;
    await getListMatrix();
    matrixSelected.value = listMatrix.value.find(
      (item) => item.matrixCode === mtx.matrixCode
    );
    matrixBuilderFactors.value = [];
    listTableMatrix.value = [];
    listTableMatrixTemp.value = [];
    headersTableMatrix.value = [];
    listTempItems.value = [];
    resetDataTableMatrix();
    await getHeaderTableMatrix(mtx.matrixCode);
    await getListTableMatrix(mtx.matrixCode, { builderDtos: null });
  };

  const showFactorSearchPage = async (factor: FactorItem): Promise<void> => {
    const factorStore = useFactorStore();
    const {
      paramFilter,
      factorsType,
      factorTypeSelected,
      paramFilterDetail,
      factorTypeDetail,
      factorSelected,
      factorDetail,
      currentTab,
    } = storeToRefs(factorStore);
    const { getListFactorsType, getDetailFactorType } = factorStore;
    paramFilter.value.factorTypeCode = factor.factorTypeCode;
    await getListFactorsType();
    if (factorsType.value?.length) {
      factorTypeSelected.value = factorsType.value[0];
      paramFilterDetail.value.factorTypeCode = factor.factorTypeCode;
      paramFilterDetail.value.factorCode = factor.factorCode;
      paramFilterDetail.value.size = 8;
      paramFilterDetail.value.page = 1;
      await getDetailFactorType();
      currentTab.value = TABS_NAME_COLLECTION.FACTOR;
      factorSelected.value = factorTypeDetail.value.factorLst[0];
      factorDetail.value = factorTypeDetail.value.factorLst[0];
    } else {
      return;
    }
  };

  const showPublishApprovalPage = async (item): Promise<void> => {
    const publishStore = usePublishManagerStore();
    const {
      currentTab,
      isRedirectFromNotification,
      paramFilterPublishSearch,
      publishSelected,
    } = storeToRefs(publishStore);

    const { getPublishPackageDetail, getPublishSearch } = publishStore;

    if (item.linkUrl) {
      await getPublishPackageDetail(item.linkUrl, true);
    }

    paramFilterPublishSearch.value.type =
      publishSelected.value?.pubRqstStusCode || null;
    paramFilterPublishSearch.value.searchBy = SearchBy.Name;
    paramFilterPublishSearch.value.searchKey = item.imageUrl;
    paramFilterPublishSearch.value.page = 1;
    paramFilterPublishSearch.value.size = 6;
    await getPublishSearch();

    isRedirectFromNotification.value = true;
    currentTab.value = PUBLISH_TABS_VALUE.APPROVAL_FLOW;
  };

  const findObjectById = (arr, targetId) => {
    for (const obj of arr) {
      if (obj.ctgrNodeUuid === targetId) {
        return obj;
      }

      if (obj.children) {
        const found = findObjectById([...obj.children], targetId);
        if (found) return found;
      }
    }
    return null;
  };

  const moveOfferSearchPage = async (item: OfferItem): Promise<void> => {
    const { showProductDetail } = storeToRefs(useProductsStore());
    const offerPath = findMenuItem(menuTree.value, MenuItemID.OfferSearch);
    if (!offerPath) return;
    showProductDetail.value = false;
    await showOfferSearchPage(item);
    offerPath["path"] = configPath(offerPath);
    addTabFunction(offerPath);
  };

  const moveComponentSearchPage = async (
    item: ComponentItem
  ): Promise<void> => {
    const componentPath = findMenuItem(
      menuTree.value,
      MenuItemID.ComponentSearch
    );
    if (!componentPath) return;
    await showComponentSearchPage(item);
    componentPath["path"] = configPath(componentPath);
    addTabFunction(componentPath);
  };

  const moveResourceSearchPage = async (item: ResourceItem): Promise<void> => {
    const resourcePath = findMenuItem(
      menuTree.value,
      MenuItemID.ResourceSearch
    );
    if (!resourcePath) return;
    await showResourceSearchPage(item);
    resourcePath["path"] = configPath(resourcePath);
    addTabFunction(resourcePath);
  };

  const moveMultiEntitySearchPage = async (
    item: MultiEntityItem
  ): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.MultiEntitySearch);
    if (!menu) return;
    await showMultiEntitySearchPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveGroupSearchPage = async (item: any): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.GroupSearch);
    if (!menu) return;
    await showGroupSearchPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveRelationManagerPage = async (item: any): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.RelationManager);
    if (!menu) return;
    await showRelationManagerPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveRelationSearchPage = async (item: any): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.RelationSearch);
    if (!menu) return;
    await showRelationSearchPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveImpactAnalysisPage = async (item: any): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.ImpactAnalysis);
    if (!menu) return;
    await showImpactAnalysisPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveTreeViewPage = (item: any): void => {
    const menu = findMenuItem(menuTree.value, MenuItemID.TreeView);
    if (!menu) return;
    menu["path"] = configPath(menu);
    addTabFunction(menu);
    const { offerItemFormUserPocket, isRedirectFormUserPocket } =
      storeToRefs(useCategoryStore());
    isRedirectFormUserPocket.value = true;
    offerItemFormUserPocket.value = item;
  };

  const moveGroupWithOfferPage = async (item: any): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.GroupSearch);
    if (!menu) return;
    await showGroupWithOfferPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveCategoryPage = async (item: CategoryItem): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.TreeView);
    if (!menu) return;
    await showCategoryPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveMatrixBuilderPage = async (item: MatrixItem): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.MatrixBuilder);
    if (!menu) return;
    await showMatrixBuilderPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveCreateOfferPage = (): void => {
    const menu = findMenuItem(menuTree.value, MenuItemID.OfferCreate);
    if (!menu) return;
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveCommonCodePage = async (code: IAttributeCode): Promise<void> => {
    const { searchParams: codeParams } = storeToRefs(useCmCodeStore());
    const { fetchData } = useCmCodeStore();
    const menu = findMenuItem(menuTree.value, MenuItemID.CommonCode);
    if (!menu) return;
    codeParams.value.srchType = "grpNm";
    codeParams.value.srchWord = code.cmcdGrpNm;
    await fetchData();
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveLabelManagementPage = async (label?: ILabelItem): Promise<void> => {
    const { searchParams, getListLabel } = useLabelStore();
    const { selectedLabel, isEditing } = storeToRefs(useLabelStore());
    const menu = findMenuItem(menuTree.value, MenuItemID.LabelSearch);
    if (!menu) return;
    if (label) {
      searchParams.type = LABEL_SEARCH_TYPE.CODE;
      searchParams.value = label.labelId;
      selectedLabel.value = cloneDeep(label);
      isEditing.value = false;
    } else {
      searchParams.type = LABEL_SEARCH_TYPE.NAME;
      searchParams.value = "";
      selectedLabel.value = null;
    }
    await getListLabel();
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const movePublishApprovalPage = async (item: any): Promise<void> => {
    const publishPath = findMenuItem(menuTree.value, MenuItemID.PublishManager);
    if (!publishPath) return;
    await showPublishApprovalPage(item);
    publishPath["path"] = configPath(publishPath);
    addTabFunction(publishPath);
  };

  const moveFactorSearchPage = async (item: FactorItem): Promise<void> => {
    const menu = findMenuItem(menuTree.value, MenuItemID.FactorSearch);
    if (!menu) return;
    await showFactorSearchPage(item);
    menu["path"] = configPath(menu);
    addTabFunction(menu);
  };

  const moveTableStrucTurePage = async (table: TableItem): Promise<void> => {
    const tableStructureStore = useTableStructureStore();
    const {
      currentTab,
      tableTypeSearchParams,
      tableTypeSearchList,
      selectedTableType,
      tableTypeDetailParams,
      tableSelected,
      tableTabList,
      isShowTableTypeDetail,
      isRedirectTo,
    } = storeToRefs(tableStructureStore);
    const { getListTableTypeDetail, getListTableType } = tableStructureStore;
    tableStructureStore.$reset();
    tableTypeSearchParams.value.tableTypeCode = table.tableTypeCode;
    await getListTableType();
    isRedirectTo.value = true;

    const menu = findMenuItem(menuTree.value, MenuItemID.TableStructure);
    if (!menu) return;
    menu["path"] = configPath(menu);
    addTabFunction(menu);
    setTimeout(async () => {
      if (tableTypeSearchList.value?.length) {
        selectedTableType.value = tableTypeSearchList.value[0];
        tableTypeDetailParams.value.page = 1;
        tableTypeDetailParams.value.tableTypeCode =
          selectedTableType.value.tableTypeCode;
        tableTypeDetailParams.value.tableName = table.tableName;
        await getListTableTypeDetail();
        isShowTableTypeDetail.value = true;
        currentTab.value = TABS_NAME_COLLECTION.TABLE;
        tableSelected.value = cloneDeep(tableTabList.value[0]);
      } else {
        return;
      }
    }, 700);
  };

  return {
    moveOfferSearchPage,
    moveComponentSearchPage,
    moveResourceSearchPage,
    moveMultiEntitySearchPage,
    moveGroupSearchPage,
    moveRelationManagerPage,
    moveRelationSearchPage,
    moveImpactAnalysisPage,
    moveTreeViewPage,
    moveGroupWithOfferPage,
    moveCategoryPage,
    moveMatrixBuilderPage,
    moveCreateOfferPage,
    moveCommonCodePage,
    moveLabelManagementPage,
    movePublishApprovalPage,
    moveFactorSearchPage,
    moveTableStrucTurePage,
  };
}
