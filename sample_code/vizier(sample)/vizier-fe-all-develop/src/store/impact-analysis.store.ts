import cloneDeep from "lodash-es/cloneDeep";
import { getUiOfferStructure } from "@/api/prod/offerApi";
import {
  getUiImpactAnalysisItems,
  getUiImpactAnalysisRelation,
  getUiImpactAnalysisListView,
} from "@/api/prod/impactAnalysisApi";
import {
  RESOURCE_TYPE_FIELD,
  VIEW_MODE,
  TAB_FIELDS,
  TARGET_TYPE,
  SPACE,
} from "@/constants/";
import { OFFERS_SUB_TYPE } from "@/constants/component";
import {
  getProductStructureDetailRootApi,
  getResourceDetailApi,
} from "@/api/prod/productApi";
import { getComponentDetailInfoApi } from "@/api/prod/componentApi";
import { ParamsUiImpactAnalysisRelation } from "@/interfaces/prod/ImpactAnalysisInterface";
import { removeUndefinedProperties } from "@/utils/format-data";
import { SearchBy } from "@/enums";

const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};
export const paramTargetSearchDefault = {
  page: 1,
  size: 10,
  prodItemNm: undefined,
  prodItemCd: undefined,
  type: undefined,
  detlType: undefined,
  subType: undefined,
};

const useImpactAnalysisStore = defineStore("impactAnalysisStore", {
  state: () => ({
    searchPattern: TARGET_TYPE.OFFER,
    componentItemList: [],
    targetSearchList: {
      items: [],
      pagination: cloneDeep(defaultPagination),
    },
    offerItemList: [],
    resourceItemList: [],
    offerCoordinates: [],
    parentItem: {},
    siblingList: [],
    paramTargetSearch: cloneDeep(paramTargetSearchDefault),
    paramListView: {
      page: 1,
      size: 10,
      offerUuid: null,
      componentUuid: null,
      resourceUuid: null,
      lctgrItemCode: SPACE,
      objCode: undefined,
      objName: undefined,
    },
    listView: {
      itemsPerPage: 10,
      currentPage: 1,
      totalItems: 0,
      totalPages: 0,
      pageSize: 1,
      items: [],
    },
    paramSearchStructure: {
      baseProdItemCd: "",
      trgtProdItemCd: "",
    },
    componentToOfferCoordinates: [],
    componentToResourceCoordinates: [],
    resourceCoordinates: [],
    offerComponentCoordinates: [],
    resourceComponentCoordinates: [],
    selectedSearchItem: null,
    shouldResetState: false,
    isShowStatus: {
      offer: false,
      component: false,
      resource: false,
    },
    componentSearchSubType: null,
    selectedNmCd: SearchBy.Name,
    tabView: VIEW_MODE.GRID,
    nodeFocusStatus: {
      leftNode: false,
      rightNode: false,
    },
    isLoading: false,
    loadingPagination: false,
    selectedGridNmCd: SearchBy.Name,
    redirectFormPocket: false,
  }),
  getters: {
    getSearchPattern(state: any) {
      return state.searchPattern;
    },
    getIsShowStatus(state: any) {
      return state.isShowStatus;
    },
    getParamTargetSearch(state: any) {
      return state.paramTargetSearch;
    },
    getOfferCoordinates(state: any) {
      return state.offerCoordinates;
    },
    getComponentToOfferCoordinates(state: any) {
      return state.componentToOfferCoordinates;
    },
    getComponentToResourceCoordinates(state: any) {
      return state.componentToResourceCoordinates;
    },
    getResourceCoordinates(state: any) {
      return state.resourceCoordinates;
    },
    getOfferComponentCoordinates(state: any) {
      return state.offerComponentCoordinates;
    },
    getResourceComponentCoordinates(state: any) {
      return state.resourceComponentCoordinates;
    },
    getTargetSearchList(state: any) {
      return state.targetSearchList;
    },
    getSelectedSearchItem(state: any) {
      return state.selectedSearchItem;
    },
    getComponentItemList(state: any) {
      return state.componentItemList;
    },
    getResourceItemList(state: any) {
      return state.resourceItemList;
    },
    getOfferItemList(state: any) {
      return state.offerItemList;
    },
    getParentItem(state: any) {
      return state.parentItem;
    },
    getSiblingList(state: any) {
      return state.siblingList;
    },
    getShouldReset(state: any) {
      return state.shouldResetState;
    },
    getParamSearchStructure(state: any) {
      return state.paramSearchStructure;
    },
    getSelectedSearchItemType(state: any) {
      const componentTypeList: Array<any> = [];

      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      for (const [_key, value] of Object.entries(OFFERS_SUB_TYPE)) {
        value.forEach((childItem: any) => componentTypeList.push(childItem));
      }
      return TAB_FIELDS.map((item) => item.value).includes(
        state.selectedSearchItem.subType
      )
        ? TARGET_TYPE.OFFER
        : componentTypeList
              .map((item) => item.subType_nm)
              .includes(state.selectedSearchItem.subType)
          ? TARGET_TYPE.COMPONENT
          : RESOURCE_TYPE_FIELD.map((item) => item.title).includes(
                state.selectedSearchItem.subType
              )
            ? TARGET_TYPE.RESOURCE
            : "";
    },
    getComponentSearchSubType(state: any) {
      return state.componentSearchSubType;
    },
    getSelectedNmCd(state: any) {
      return state.selectedNmCd;
    },
    getTabView(state: any) {
      return state.tabView;
    },
    getNodeFocusStatus(state: any) {
      return state.nodeFocusStatus;
    },
  },
  actions: {
    setSearchPattern(param: any) {
      this.searchPattern = param;
    },
    setSelectedSearchItemDetail(param: any) {
      this.selectedSearchItem["detail"] = param;
    },
    setOfferSelectedItemDetail() {
      switch (this.getSelectedSearchItemType) {
        case TARGET_TYPE.OFFER:
          getProductStructureDetailRootApi({
            objUuid: this.selectedSearchItem.prodUuid,
          }).then(
            (response) => {
              this.setSelectedSearchItemDetail(response.data);
            },
            (err) => console.log(err)
          );
          break;
        case TARGET_TYPE.COMPONENT:
          getComponentDetailInfoApi({
            objUuid: this.selectedSearchItem.prodUuid,
          }).then(
            (response) => {
              this.setSelectedSearchItemDetail(response.data);
            },
            (err) => console.log(err)
          );
          break;
        case TARGET_TYPE.RESOURCE:
          getResourceDetailApi({
            objUuid: this.selectedSearchItem.prodUuid,
          }).then(
            (response) => {
              this.setSelectedSearchItemDetail(response.data);
            },
            (err) => console.log(err)
          );
          break;
      }
    },
    setTargetSearchList(param: any) {
      this.targetSearchList = cloneDeep(param);
    },
    setTargetSearchType(param: string) {
      this.paramTargetSearch.type = param;
    },
    setTargetSearchSubType(param: string) {
      this.paramTargetSearch.subType = param;
    },
    setTargetSearchDetailType(param: string) {
      this.paramTargetSearch.detlType = param;
    },
    setTargetSearchName(param: string) {
      this.paramTargetSearch.prodItemNm = param;
    },
    setTargetSearchCode(param: string) {
      this.paramTargetSearch.prodItemCd = param;
    },
    setTargetSearchPageNo(param: number) {
      this.paramTargetSearch.page = param;
    },
    setTargetSearchPageSize(param: number) {
      this.paramTargetSearch.size = param;
    },
    setListViewPage(page: number, size: number) {
      this.paramListView.page = page;
      this.paramListView.size = size;
    },
    setParamSearchStructure(param: any) {
      this.paramSearchStructure = { ...param };
    },
    setOfferCoordinates(params: any, index: number = 0) {
      this.offerCoordinates[index as number] = cloneDeep(params);
    },
    setComponentToOfferCoordinates(params: any, index: number = 0) {
      this.componentToOfferCoordinates[index as number] = cloneDeep(params);
    },
    setComponentToResourceCoordinates(params: any, index: number = 0) {
      this.componentToResourceCoordinates[index as number] = cloneDeep(params);
    },
    setResourceCoordinates(params: any, index: number = 0) {
      this.resourceCoordinates[index as number] = cloneDeep(params);
    },
    removeOfferCoordinates(index: number = 0) {
      this.offerCoordinates[index as number] = [];
    },
    removeComponentToOfferCoordinates(index: number = 0) {
      this.componentToOfferCoordinates[index as number] = [];
    },
    removeComponentToResourceCoordinates(index: number = 0) {
      this.componentToResourceCoordinates[index as number] = [];
    },
    removeResourceCoordinates(index: number = 0) {
      this.resourceCoordinates[index as number] = [];
    },
    removeOfferComponentCoordinates(index: number = 0) {
      this.offerComponentCoordinates[index as number] = [];
    },
    removeResourceComponentCoordinates(index: number = 0) {
      this.resourceComponentCoordinates[index as number] = [];
    },
    setInitOfferComponentCoordinates() {
      switch (this.searchPattern) {
        case TARGET_TYPE.RESOURCE:
          const arrFlat = this.componentToResourceCoordinates.flat(2);
          if (
            arrFlat.length &&
            arrFlat.length != this.offerComponentCoordinates.length
          ) {
            arrFlat.forEach(() => {
              this.offerComponentCoordinates.push([]);
            });
          }
          break;
      }
    },
    setInitResourceComponentCoordinates() {
      switch (this.searchPattern) {
        case TARGET_TYPE.OFFER:
          const arrFlat = this.componentToOfferCoordinates.flat(2);
          if (
            arrFlat.length &&
            arrFlat.length != this.resourceComponentCoordinates.length
          ) {
            arrFlat.forEach(() => {
              this.resourceComponentCoordinates.push([]);
            });
          }
          break;
      }
    },

    actionOfferComponentCoordinates(index: number = 0) {
      const list: Array<Array<any>> = [];
      switch (this.searchPattern) {
        case TARGET_TYPE.OFFER:
          if (
            this.offerCoordinates.length &&
            this.componentToOfferCoordinates.length
          ) {
            this.offerCoordinates[index as number].forEach(
              (offerTop: Array<number>) => {
                let offerCoordinates: Array<number> = [];
                offerTop.forEach((top) => {
                  offerCoordinates = [0, top + 33];
                });
                if (!this.offerCoordinates[index as number]) {
                  return;
                }
                this.componentToOfferCoordinates[index as number].forEach(
                  (componentTop: Array<number>) => {
                    let componentCoordinates: Array<number> = [];
                    componentTop.forEach((comTop) => {
                      componentCoordinates = [56, comTop + 33];
                      list.push([offerCoordinates, componentCoordinates]);
                    });
                  }
                );
              }
            );
          }
          if (list.length) {
            this.offerComponentCoordinates = [cloneDeep(list)];
          }
          break;
        case TARGET_TYPE.COMPONENT:
          if (
            this.offerCoordinates.length &&
            this.componentToOfferCoordinates.length
          ) {
            this.componentToOfferCoordinates[index as number].forEach(
              (comTop: Array<number>) => {
                let comCoordinates: Array<number> = [];
                comTop.forEach((top) => {
                  comCoordinates = [56, top + 33];
                });
                if (!this.offerCoordinates[index as number]) {
                  return;
                }
                this.offerCoordinates[index as number].forEach(
                  (offerTop: Array<number>) => {
                    let offerCoordinates: Array<number> = [];
                    offerTop.forEach((top) => {
                      offerCoordinates = [0, top + 33];
                      list.push([comCoordinates, offerCoordinates]);
                    });
                  }
                );
              }
            );
          }
          if (list.length) {
            this.offerComponentCoordinates = [cloneDeep(list)];
          }
          break;
        case TARGET_TYPE.RESOURCE:
          if (
            this.offerCoordinates.length &&
            this.componentToOfferCoordinates.length
          ) {
            this.componentToOfferCoordinates[index as number].forEach(
              (offerTop: Array<number>) => {
                let offerCoordinates: Array<number> = [];
                offerTop.forEach((top) => {
                  offerCoordinates = [56, top + 33];
                });
                if (!this.offerCoordinates[index as number]) {
                  return;
                }
                this.offerCoordinates[index as number].forEach(
                  (componentTop: Array<number>) => {
                    let componentCoordinates: Array<number> = [];
                    componentTop.forEach((comTop) => {
                      componentCoordinates = [0, comTop + 33];
                      list.push([offerCoordinates, componentCoordinates]);
                    });
                  }
                );
              }
            );
          }
          if (list.length) {
            this.offerComponentCoordinates[index as number] = cloneDeep(list);
          }
          break;
      }
    },
    actionResourceComponentCoordinates(index: number = 0) {
      const list: Array<Array<any>> = [];
      switch (this.searchPattern) {
        case TARGET_TYPE.OFFER:
          if (
            this.resourceCoordinates.length &&
            this.componentToResourceCoordinates.length
          ) {
            this.componentToResourceCoordinates[index as number].forEach(
              (offerTop: Array<number>) => {
                let offerCoordinates: Array<number> = [];
                offerTop.forEach((top) => {
                  offerCoordinates = [0, top + 33];
                });
                if (!this.resourceCoordinates[index as number]) {
                  return;
                }
                this.resourceCoordinates[index as number].forEach(
                  (componentTop: Array<number>) => {
                    let componentCoordinates: Array<number> = [];
                    componentTop.forEach((comTop) => {
                      componentCoordinates = [56, comTop + 33];
                      list.push([offerCoordinates, componentCoordinates]);
                    });
                  }
                );
              }
            );
          }
          if (list.length) {
            this.resourceComponentCoordinates[index as number] =
              cloneDeep(list);
          }
          break;
        case TARGET_TYPE.COMPONENT:
          if (
            this.resourceCoordinates.length &&
            this.componentToResourceCoordinates.length
          ) {
            this.componentToResourceCoordinates[index as number].forEach(
              (comTop: Array<number>) => {
                let comCoordinates: Array<number> = [];
                comTop.forEach((top) => {
                  comCoordinates = [0, top + 33];
                });
                if (!this.resourceCoordinates[index as number]) {
                  return;
                }
                this.resourceCoordinates[index as number].forEach(
                  (resourceTop: Array<number>) => {
                    let resourceCoordinates: Array<number> = [];
                    resourceTop.forEach((comTop) => {
                      resourceCoordinates = [56, comTop + 33];
                      list.push([comCoordinates, resourceCoordinates]);
                    });
                  }
                );
              }
            );
          }
          if (list.length) {
            this.resourceComponentCoordinates[index as number] =
              cloneDeep(list);
          }
          break;
        case TARGET_TYPE.RESOURCE:
          if (
            this.resourceCoordinates.length &&
            this.componentToResourceCoordinates.length
          ) {
            this.resourceCoordinates[index as number].forEach(
              (offerTop: Array<number>) => {
                let offerCoordinates: Array<number> = [];
                offerTop.forEach((top) => {
                  offerCoordinates = [56, top + 33];
                });
                if (!this.componentToResourceCoordinates[index as number]) {
                  return;
                }
                this.componentToResourceCoordinates[index as number].forEach(
                  (componentTop: Array<number>) => {
                    let componentCoordinates: Array<number> = [];
                    componentTop.forEach((comTop) => {
                      componentCoordinates = [0, comTop + 33];
                      list.push([offerCoordinates, componentCoordinates]);
                    });
                  }
                );
              }
            );
          }
          if (list.length) {
            this.resourceComponentCoordinates[index as number] =
              cloneDeep(list);
          }
          break;
      }
    },
    setIsShowOffer(param: any) {
      this.isShowStatus.offer = param;
    },
    setIsShowComponent(param: any) {
      this.isShowStatus.component = param;
    },
    setIsShowResource(param: any) {
      this.isShowStatus.resource = param;
    },
    setSelectedSearchItem(param: any) {
      this.selectedSearchItem = param;
    },
    setComponentItemList(param: any) {
      this.componentItemList = cloneDeep(param);
    },
    setResourceItemList(param: any) {
      this.resourceItemList = cloneDeep(param);
    },
    setOfferItemList(param: any) {
      this.offerItemList = cloneDeep(param);
    },
    setShouldReset(param: any) {
      this.shouldResetState = param;
    },
    async actionGetTargetSearchList() {
      try {
        const param = removeUndefinedProperties(this.paramTargetSearch);
        const { data } = await getUiImpactAnalysisItems(param);
        const { elements, page, size, totalElements, totalPages } = data;
        this.targetSearchList.items = elements;
        this.targetSearchList.pagination = {
          totalSearchItems: totalElements,
          currentPage: page,
          pageSize: size,
          totalItems: totalElements,
          totalPages: totalPages,
        };
      } catch (error) {
        throw error;
      }
    },
    async actionGetRelation(param: ParamsUiImpactAnalysisRelation) {
      const { data } = await getUiImpactAnalysisRelation(param);
      this.parentItem = data?.parent;
      this.siblingList = data?.siblings;
    },
    async actionGetListView() {
      if (!this.selectedSearchItem || !this.selectedSearchItem.prodUuid) return;
      try {
        this.loadingPagination = true;
        this.paramListView = {
          ...this.paramListView,
          offerUuid: null,
          componentUuid: null,
          resourceUuid: null,
        };
        switch (this.searchPattern) {
          case TARGET_TYPE.OFFER:
            this.paramListView.offerUuid = this.selectedSearchItem.prodUuid;
            break;
          case TARGET_TYPE.COMPONENT:
            this.paramListView.componentUuid = this.selectedSearchItem.prodUuid;
            break;
          case TARGET_TYPE.RESOURCE:
            this.paramListView.resourceUuid = this.selectedSearchItem.prodUuid;
            break;
          default:
            return;
        }
        const param = removeUndefinedProperties(this.paramListView);
        const { data } = await getUiImpactAnalysisListView(param);
        if (!data || !data.elements.length) {
          this.listView.items = [];
          return;
        }
        this.listView.currentPage = data.page;
        this.listView.totalItems = data.totalElements;
        this.listView.pageSize = data.size;
        this.listView.totalPages = data.totalPages;
        this.listView.items = data.elements.map((item: any, index: number) => {
          const newItem = {
            no: index + 1 + (data.page - 1) * data.size,
          };
          Object.entries(item).forEach(([key, value]) => {
            newItem[key as string] = value || "-";
          });
          return newItem;
        });
      } catch (error) {
        throw error;
      } finally {
        this.loadingPagination = false;
      }
    },
    async actionGetOfferItemList(index: number = 0) {
      const data = await getUiOfferStructure(this.paramSearchStructure);
      this.offerItemList[index as number] = data;
    },
    async actionGetComponentItemList(index: number = 0) {
      const data = await getUiOfferStructure(this.paramSearchStructure);
      this.componentItemList[index as number] = data;
    },
    async actionGetResourceItemList(index: number = 0) {
      const data = await getUiOfferStructure(this.paramSearchStructure);
      this.resourceItemList[index as number] = data;
    },
    resetState() {
      this.resourceCoordinates = [];
      this.componentToResourceCoordinates = [];
      this.resourceComponentCoordinates = [];
      this.offerComponentCoordinates = [];
      this.componentToOfferCoordinates = [];
      this.offerCoordinates = [];
      this.resourceCoordinates = [];
      this.isShowStatus = {
        offer: false,
        component: false,
        resource: false,
      };
      this.resourceItemList = [];
      this.componentItemList = [];
      this.offerItemList = [];
      this.selectedSearchItem = null;
      this.paramSearchStructure = {
        baseProdItemCd: "",
        trgtProdItemCd: "",
      };
      this.nodeFocusStatus = {
        leftNode: false,
        rightNode: false,
      };
      this.resetParamListView();
    },
    resetParamListView() {
      this.paramListView.page = 1;
      this.paramListView.size = 10;
      this.paramListView.offerUuid = null;
      this.paramListView.componentUuid = null;
      this.paramListView.resourceUuid = null;
      this.paramListView.lctgrItemCode = SPACE;
      this.paramListView.objCode = undefined;
      this.paramListView.objName = undefined;
      this.selectedGridNmCd = SearchBy.Name;
      this.listView.items = [];
      this.listView.itemsPerPage = 10;
      this.listView.currentPage = 1;
      this.listView.totalItems = 0;
      this.listView.totalPages = 0;
      this.listView.pageSize = 1;
    },
    setComponentSearchSubType(param: String) {
      this.componentSearchSubType = param;
    },
    setSelectedNmCd(param: String) {
      this.selectedNmCd = param;
    },
    setTabView(param: String) {
      this.tabView = param;
    },
    setNodeFocusStatusLeft(param: boolean) {
      this.nodeFocusStatus.leftNode = param;
    },
    setNodeFocusStatusRight(param: boolean) {
      this.nodeFocusStatus.rightNode = param;
    },
    resetTargetSearch() {
      this.resetParamListView();
      this.paramTargetSearch = cloneDeep(paramTargetSearchDefault);
      this.targetSearchList.pagination = cloneDeep(defaultPagination);
      this.targetSearchList.items = [];
    },
  },
});

export default useImpactAnalysisStore;
