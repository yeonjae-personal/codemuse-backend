import { ParamsUiCategoryOffer } from "@/interfaces/prod/CategoryInterface";
import {
  getUiCategoryLevelDescription,
  getUiCategoryOffer,
  getUiCategoryProducts,
  getUiCategoryTree,
  getOfferTreeSearch,
  getOfferListTree,
  getListTabs,
  postUiCategoryTree,
} from "@/api/prod/categoryApi";
import { CATEGORY_VIEW_MODE, OFFER_FIELD } from "@/constants/";
import cloneDeep from "lodash-es/cloneDeep";
import { OFFER_TABS_VALUE } from "@/constants/offer";

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const useCategoryStore = defineStore("category", {
  state: () => ({
    tabs: [],
    offerTypes: [] as any[],
    dinamicTabs: {},
    categoryField: "ctgrNodeName",
    searchCategoryText: "",
    searchOfferTextObj: {
      searchText: "",
      searchField: "name",
    },
    currentTabType: null,
    currentTabUuid: null,
    dragOfferUuid: "",
    loadingPagination: false,
    offerItemFormUserPocket: null,
    isRedirectFormUserPocket: false,
  }),
  getters: {
    getTabData(state: any) {
      return this.dinamicTabs[state.currentTabType];
    },
    getCategoryCurrentTab(state: any) {
      return state.currentTabType;
    },
    getCategoryView(state: any) {
      return this.dinamicTabs[state.currentTabType].tabView;
    },
    getIsEdit(state: any) {
      return this.dinamicTabs[state.currentTabType].isEdit;
    },
    getCurrentTab(state: any) {
      return this.dinamicTabs[state.currentTabType].currentTab;
    },
    getOpenOfferPanel(state: any) {
      return this.dinamicTabs[state.currentTabType].openOfferPanel;
    },
    getSearchStatus(state: any) {
      return this.dinamicTabs[state.currentTabType].isSearch;
    },
    getSearchProductStatus(state: any) {
      return this.dinamicTabs[state.currentTabType].isSearchProduct;
    },
    getSearchListParams(state: any) {
      return this.dinamicTabs[state.currentTabType].searchListParams;
    },
    getSearchTreeParams(state: any) {
      return this.dinamicTabs[state.currentTabType].searchTreeParams;
    },
    getSearchCategoryFilterObj(state: any) {
      return this.dinamicTabs[state.currentTabType].searchCategoryFilterObj;
    },
    getSearchOfferFilterObj(state: any) {
      return this.dinamicTabs[state.currentTabType].searchOfferFilterObj;
    },
    getListData(state: any) {
      return this.dinamicTabs[state.currentTabType].listData;
    },
    getTreeData(state: any) {
      return this.dinamicTabs[state.currentTabType].treeData;
    },
    getTreeDataOld(state: any) {
      return this.dinamicTabs[state.currentTabType].treeDataOld;
    },
    getProductData(state: any) {
      return this.dinamicTabs[state.currentTabType].productData;
    },
    getListOfferUpdateNotSave(state: any) {
      return this.dinamicTabs[state.currentTabType].listOfferUpdateNotSave;
    },
    getDescriptionData(state: any) {
      return this.dinamicTabs[state.currentTabType].descriptionData;
    },
    getIsShowTreeData(state: any) {
      return this.dinamicTabs[state.currentTabType].isShowTreeData;
    },
    getItemIdShowOffer(state: any) {
      return this.dinamicTabs[state.currentTabType].itemIdShowOffer;
    },
    getSelectedCategoryTreeNode(state: any) {
      return this.dinamicTabs[state.currentTabType].selectedCategoryTreeNode;
    },
    getSelectedOfferBoxValue(state: any) {
      return this.dinamicTabs[state.currentTabType].selectedOfferBoxValue;
    },
    getCategoryOfferTreeSearchResultData(state: any) {
      return this.dinamicTabs[state.currentTabType]
        .categoryOfferTreeSearchResult;
    },
    getCategoryOfferListTreeData(state: any) {
      return this.dinamicTabs[state.currentTabType].categoryOfferListTree;
    },
    getCategoryOfferTreeSearchParam(state: any) {
      return this.dinamicTabs[state.currentTabType]
        .categoryOfferTreeSearchParam;
    },
    getNodeSize(state: any) {
      return this.dinamicTabs[state.currentTabType].nodeSize;
    },
    getShowCategoryDetail(state: any) {
      return this.dinamicTabs[state.currentTabType].showCategoryDetail;
    },
    getChildTreeViewStatus(state: any) {
      return this.dinamicTabs[state.currentTabType].childTreeViewStatus;
    },
    getUuidTab(state: any) {
      return this.dinamicTabs[state.currentTabType].searchTreeParams
        .ctgrTabUuid;
    },
    getFileNameExport(state: any) {
      return state.tabs.find(
        (item) =>
          item.ctgrTabUuid ===
          this.dinamicTabs[state.currentTabType].searchTreeParams.ctgrTabUuid
      )?.ctgrTabName;
    },
    getSaveStatus(state: any) {
      return this.dinamicTabs[state.currentTabType].saveStatus;
    },
    getIsFetchData(state: any) {
      return this.dinamicTabs[state.currentTabType].isFetchData;
    },
    getIsEmptyOfferList(state: any) {
      return this.dinamicTabs[state.currentTabType].isEmptyOfferList;
    },
    getEditSearch(state: any) {
      return this.dinamicTabs[state.currentTabType].editSearch;
    },
    getIsSearchProductOfNode(state: any) {
      return this.dinamicTabs[state.currentTabType].isSearchProductOfNode;
    },
    getTableDataList(state:any) {
      return this.dinamicTabs[state.currentTabType].tableDataList;
    }
  },
  actions: {
    setCategoryTab(tab: string) {
      this.currentTabType = tab;
    },
    setSelectedCategoryTreeNode(treeNode: Object) {
      this.dinamicTabs[this.currentTabType].selectedCategoryTreeNode = treeNode;
    },
    setSelectedCategoryTreeNodeProperty(status: boolean) {
      this.dinamicTabs[this.currentTabType].selectedCategoryTreeNode.isUpdate =
        status;
    },
    setCategoryViewType(view: string) {
      this.dinamicTabs[this.currentTabType].tabView = view;
    },
    setIsEdit(status: boolean) {
      this.dinamicTabs[this.currentTabType].isEdit = status;
    },
    setCurrentTab(tab: string) {
      this.dinamicTabs[this.currentTabType].currentTab = tab;
    },
    setOpenOfferPanel(status: boolean) {
      this.dinamicTabs[this.currentTabType].openOfferPanel = status;
    },
    setSearchCategoryName(ctgrNodeName: String) {
      this.dinamicTabs[this.currentTabType].searchListParams.ctgrNodeName =
        ctgrNodeName;
    },
    setSearchStatus(status: boolean) {
      this.dinamicTabs[this.currentTabType].isSearch = status;
    },
    setSearchProductStatus(status: boolean) {
      this.dinamicTabs[this.currentTabType].isSearchProduct = status;
    },
    setSearchOfferName(offerNm: String) {
      this.dinamicTabs[this.currentTabType].searchListParams.offerNm = offerNm;
    },
    setSearchOfferCode(offerCd: String) {
      this.dinamicTabs[this.currentTabType].searchListParams.offerCd = offerCd;
    },
    setSelectedOfferBoxValue(value: any) {
      this.dinamicTabs[this.currentTabType].selectedOfferBoxValue = value;
    },
    setSearchOfferPageSize(page: Number, size: Number) {
      this.dinamicTabs[this.currentTabType].searchListParams.page = page;
      this.dinamicTabs[this.currentTabType].searchListParams.size = size;
    },
    setSearchListParams(params: ParamsUiCategoryOffer) {
      this.dinamicTabs[this.currentTabType].searchListParams = params;
    },
    setSearchCategoryFilterObj(searchText: String) {
      this.dinamicTabs[this.currentTabType].searchCategoryFilterObj.searchText =
        searchText;
    },
    setSearchCategoryFilterObjAction(action: String) {
      this.dinamicTabs[this.currentTabType].searchCategoryFilterObj.action =
        action;
    },
    setSearchOfferFilterObjField(searchField: String) {
      this.dinamicTabs[
        this.currentTabType
      ].searchCategoryFilterObj.searchField = searchField;
    },
    setSearchOfferFilterObjValue(searchText: String) {
      this.dinamicTabs[this.currentTabType].searchOfferFilterObj.searchText =
        searchText;
    },
    setTreeListData(data: any) {
      this.dinamicTabs[this.currentTabType].listData = cloneDeep(data);
    },
    setTreeData(data: any) {
      this.dinamicTabs[this.currentTabType].treeData = cloneDeep(data);
    },
    setTreeDataOld(data: any) {
      this.dinamicTabs[this.currentTabType].treeDataOld = cloneDeep(data);
    },
    setIsShowTreeData(status: Boolean) {
      this.dinamicTabs[this.currentTabType].isShowTreeData = status;
    },
    setDescriptionData(data: any) {
      this.dinamicTabs[this.currentTabType].descriptionData = data;
    },
    setIsEmptyOfferList(value: Boolean) {
      this.dinamicTabs[this.currentTabType].isEmptyOfferList = value;
    },
    async fetchTabsCategory() {
      const response = await getListTabs();
      if (response) {
        this.tabs = response.data;
        response.data.forEach((item) => {
          this.dinamicTabs = {
            ...this.dinamicTabs,
            [item.ctgrTabName.toUpperCase().replace("-", "")]: {
              tabView: CATEGORY_VIEW_MODE.TREE,
              isEdit: false,
              isEmptyOfferList: false,
              openOfferPanel: true,
              currentTab: OFFER_TABS_VALUE.GENERAL,
              selectedOfferBoxValue: OFFER_FIELD[0].value,
              searchTreeParams: {
                ctgrTabUuid: item.ctgrTabUuid,
              },
              searchListParams: {
                page: 1,
                size: 10,
                ctgrTabUuid: item.ctgrTabUuid,
                ctgrNodeName: "",
                offerCd: "",
                offerNm: "",
              },
              searchCategoryFilterObj: {
                searchText: "",
                searchField: "name",
                action: false,
              },
              searchOfferFilterObj: {
                searchText: "",
                searchField: "name",
              },
              treeData: [],
              treeDataOld: [],
              listData: {},
              productData: [],
              listOfferUpdateNotSave: [],
              descriptionData: {},
              isSearch: false,
              isSearchProduct: false,
              isShowTreeData: true,
              itemIdShowOffer: null,
              selectedCategoryTreeNode: null,
              categoryOfferTreeSearchResult: {},
              categoryOfferListTree: {
                items: [],
                pagination: cloneDeep(defaultPagination),
                ctgrNodeUuid: "",
              },
              categoryOfferTreeSearchParam: {
                ctgrTabUuid: item.ctgrTabUuid,
                offerNm: "",
                offerCd: "",
              },
              tableDataList: {
                currentPage: 1,
                totalPages: 0,
                totalElements: 0,
                pageSize: 10,
                elements: [],
              },
              showOfferPaging: false,
              nodeSize: "small",
              showCategoryDetail: false,
              childTreeViewStatus: false,
              categoryUpdateList: [] as any,
              saveStatus: false,
              isFetchData: false,
              editSearch: false,
              isSearchProductOfNode: false,
            },
          };
        });
      }
    },
    async getListOffer() {
      this.loadingPagination = true;
      const response = await getUiCategoryOffer(
        this.dinamicTabs[this.currentTabType].searchListParams
      );
      this.dinamicTabs[this.currentTabType].listData = response.data;
      this.loadingPagination = false;
    },
    async getTreeCategory() {
      this.dinamicTabs[this.currentTabType].treeDataOld = cloneDeep(
        this.dinamicTabs[this.currentTabType].treeData
      );
      const response = await getUiCategoryTree(
        this.dinamicTabs[this.currentTabType].searchTreeParams
      );
      this.dinamicTabs[this.currentTabType].treeData = response.data;
    },
    async getTreeProduct(params: any) {
      params.itemCode =
        this.dinamicTabs[this.currentTabType].searchTreeParams.itemCode;
      const response = await getUiCategoryProducts(params);
      if (
        this.dinamicTabs[this.currentTabType].selectedCategoryTreeNode?.offerRel
          ?.length
      ) {
        this.dinamicTabs[
          this.currentTabType
        ].selectedCategoryTreeNode.offerRel.forEach((offer) => {
          if (!offer?.oldCtgrNodeUuid) {
            response.data = response.data.filter(
              (item) => item?.prodUuid !== offer?.objUuid
            );
          } else {
            response.data = [...response.data, offer];
          }
        });
      }
      this.dinamicTabs[this.currentTabType].productData = response.data;
    },
    setProductData(params: any) {
      this.dinamicTabs[this.currentTabType].productData = params;
    },
    setListOfferUpdateNotSave(params: any) {
      this.dinamicTabs[this.currentTabType].listOfferUpdateNotSave =
        cloneDeep(params);
    },
    async getLevelDescription() {
      try {
        const response = await getUiCategoryLevelDescription(
          this.dinamicTabs[this.currentTabType].searchTreeParams
        );
        this.dinamicTabs[this.currentTabType].descriptionData = response.data;
      } catch (error) {
        throw error;
      }
    },
    async getCategoryOfferTreeSearchResult() {
      const response = await getOfferTreeSearch(
        this.dinamicTabs[this.currentTabType].categoryOfferTreeSearchParam
      );
      this.dinamicTabs[this.currentTabType].categoryOfferTreeSearchResult =
        response.data;
    },
    async getCategoryOfferListTree(params: any) {
      const { data } = await getOfferListTree(params);
      const { elements, page, size, totalElements, totalPages } = data;
      this.dinamicTabs[this.currentTabType].categoryOfferListTree.items =
        elements;
      this.dinamicTabs[this.currentTabType].categoryOfferListTree.pagination = {
        totalSearchItems: totalElements,
        currentPage: page,
        pageSize: size,
        totalPages: totalPages,
      };
      this.dinamicTabs[this.currentTabType].categoryOfferListTree.ctgrNodeUuid =
        params.ctgrNodeUuid;
    },
    async getCategoryTableList() {
      
    },
    setItemIdShowOffer(id: String) {
      this.dinamicTabs[this.currentTabType].itemIdShowOffer = id;
    },
    setPropertiesForTree(item: any, property: String, value: any) {
      this.setPropertiesMatchTarget(
        this.dinamicTabs[this.currentTabType].treeData,
        item,
        property,
        value
      );
    },
    setPropertiesMatchTarget(
      list: Array<any>,
      item: any,
      property: string,
      value: any
    ) {
      if (!item || !item.hasOwnProperty(property)) {
        return;
      } else {
        list.map((el: any) => {
          if (el.ctgrNodeUuid === item.ctgrNodeUuid) {
            el[property as string] = value;
          } else {
            if (el.children) {
              this.setPropertiesMatchTarget(el.children, item, property, value);
            }
          }
        });
      }
    },
    setCategoryOfferSearchParam(searchText: string, offerField: string) {
      if (offerField === "offerNm") {
        this.dinamicTabs[
          this.currentTabType
        ].categoryOfferTreeSearchParam.offerNm = searchText;
        this.dinamicTabs[
          this.currentTabType
        ].categoryOfferTreeSearchParam.offerCd = "";
        this.searchOfferTextObj = {
          searchText: searchText,
          searchField: "name",
        };
      } else {
        this.dinamicTabs[
          this.currentTabType
        ].categoryOfferTreeSearchParam.offerCd = searchText;
        this.dinamicTabs[
          this.currentTabType
        ].categoryOfferTreeSearchParam.offerNm = "";
        this.searchOfferTextObj = {
          searchText: searchText,
          searchField: "code",
        };
      }
    },
    setCategoryOfferSearchParamPageNo(pageNo: number) {
      this.dinamicTabs[this.currentTabType].categoryOfferTreeSearchParam.page =
        pageNo;
    },
    setNodeSize(size: string) {
      this.dinamicTabs[this.currentTabType].nodeSize = size;
    },
    setOfferSearchData(param: any) {
      this.dinamicTabs[this.currentTabType].categoryOfferTreeSearchResult =
        param;
    },
    setShowCategoryDetail(param: any) {
      this.dinamicTabs[this.currentTabType].showCategoryDetail = param;
    },
    setChildTreeViewStatus(status: boolean) {
      this.dinamicTabs[this.currentTabType].childTreeViewStatus = status;
    },
    setSaveStatus(status: boolean) {
      this.dinamicTabs[this.currentTabType].saveStatus = status;
    },
    setIsFetchData(status: boolean) {
      this.dinamicTabs[this.currentTabType].isFetchData = status;
    },
    setEditSearch(status: boolean) {
      this.dinamicTabs[this.currentTabType].editSearch = status;
    },
    setIsSearchProductOfNode(status: boolean) {
      this.dinamicTabs[this.currentTabType].isSearchProductOfNode = status;
    },
    async updateCategory(updateList) {
      const params = {
        ctgrTabUuid: this.getUuidTab,
        description: this.dinamicTabs[this.currentTabType].descriptionData,
        categoryTree: updateList,
      };
      return await postUiCategoryTree(params);
    },
  },
});

export default useCategoryStore;
