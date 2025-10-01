import {
  getComponentDetailApi,
  getListComponentSearchApi,
} from "@/api/prod/componentApi";
import {
  getExtendsDependencyGroupTarget,
  getGroup,
  getMultiEntityDetail,
  getMultiEntitySearch,
  getMultiEntitySearchInfo,
  postMultiEntityDetail,
  putMultiEntityDetail,
} from "@/api/prod/extendsApi";
import {
  getProductStructureDetailRootApi,
  getProductsApi,
} from "@/api/prod/productApi";
import { getListResourceAdvancedApi } from "@/api/prod/resourceApi";
import { DETAIL_CATEGORY } from "@/constants/extendsManager";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import {
  MULTI_ENTITY_DETAIL_DATA,
  MULTI_ENTITY_DETAIL_USER_TITLE,
  MULTI_ENTITY_SUBTYPE,
} from "@/constants/multiEntity";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { RequestGetListComponentSearch } from "@/interfaces/prod/component";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { removeUndefinedProperties } from "@/utils/format-data";
import cloneDeep from "lodash-es/cloneDeep";

const paramsExtendsOfferSearchDefault: any = {
  ...new BaseSearchPaneParamClass(" "),
};
const paramsExtendsGroupSearchDefault: any = {
  ...new BaseSearchPaneParamClass(" "),
};
const paramsExtendsEntitySearchDefault: any = {
  itemCode: undefined,
  entityTypeCode: " ",
  multiEntityCode: undefined,
  multiEntityName: undefined,
  page: 1,
  size: 10,
};
const paramsExtendsComponentSearchDefault: any = {
  ...new BaseSearchPaneParamClass("", " "),
  baseUuid: null,
  offerUUID: null,
  onlyValidDtm: true,
};
const paramsExtendsResourceSearchDefault: any = {
  ...new BaseSearchPaneParamClass(" "),
};
const paramsMultiEntitySearchDefault: any = {
  ...new BaseSearchPaneParamClass("", " "),
};
const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const createMultiStore = (storeName) => {
  return defineStore(storeName, {
    state: () => ({
      entityDisplayForm: {
        entityDetail: false,
        groupSearch: false,
        offerSearch: false,
        componentSearch: false,
        resourceSearch: false,
      },
      selectedEntity: null as any,
      selectedEntityGroup: null as any,
      selectedEntityOffer: null,
      selectedEntityComponent: null,
      selectedEntityDetails: null as any,
      entitySearchNmCd: NM_CD_FIELDS[0].value,
      multiGroupSearchNmCd: NM_CD_FIELDS[0].value,
      multiOfferSearchNmCd: NM_CD_FIELDS[0].value,
      multiResourceSearchNmCd: NM_CD_FIELDS[0].value,
      multiComponentSearchNmCd: NM_CD_FIELDS[0].value,
      paramsSearchEntity: cloneDeep(paramsExtendsEntitySearchDefault),
      localSearch: cloneDeep(paramsExtendsEntitySearchDefault),
      inputValue: null as any,
      paramsSearchMultiGroup: cloneDeep(paramsExtendsGroupSearchDefault),
      paramsSearchMultiOffer: cloneDeep(paramsExtendsOfferSearchDefault),
      paramsSearchMultiComponent: cloneDeep(
        paramsExtendsComponentSearchDefault
      ),
      paramsMultiEntitySearch: cloneDeep(paramsMultiEntitySearchDefault),
      paramsSearchMultiResource: cloneDeep(paramsExtendsResourceSearchDefault),
      entityList: {
        items: [] as any[],
        pagination: cloneDeep(defaultPagination),
      },
      multiGroupList: {
        items: [] as any[],
        pagination: cloneDeep(defaultPagination),
      },
      multiOfferList: {
        items: [] as any[],
        pagination: cloneDeep(defaultPagination),
      },
      multiComponentList: {
        items: [] as any[],
        pagination: cloneDeep(defaultPagination),
      },
      multiResourceList: {
        items: [] as any[],
        pagination: cloneDeep(defaultPagination),
      },
      entityDetailData: {
        generalTab: [] as any,
        additionalTab: [] as any,
      },
      groupDetailData: {
        generalTab: [],
        additionalTab: [],
        historyTab: [],
        offerTab: [],
      },
      isEdit: false,
      isAdded: false,
      generalEnityCommonCode: null,
      additionalEnityCommonCode: null,
      multiEntityTypes: [] as any[],
      currentTab: OFFER_TABS_VALUE.GENERAL,
      groupItemCodeList: [] as any[],
      dragType: "",
    }),
    actions: {
      async getMultiEntityTypes() {
        const { data } = await getMultiEntitySearchInfo();
        this.multiEntityTypes = data as any[];
      },
      async getEntityList() {
        const params = {
          itemCode: this.paramsMultiEntitySearch.type,
          entityTypeCode: this.paramsMultiEntitySearch.subType,
          multiEntityCode:
            this.paramsMultiEntitySearch.searchBy === "objCode"
              ? this.paramsMultiEntitySearch.searchKey
              : undefined,
          multiEntityName:
            this.paramsMultiEntitySearch.searchBy === "objName"
              ? this.paramsMultiEntitySearch.searchKey
              : undefined,
          page: this.paramsMultiEntitySearch.page,
          size: this.paramsMultiEntitySearch.size,
        };
        removeUndefinedProperties(params);
        const { data } = await getMultiEntitySearch({ ...params });
        const { page, size, totalElements, elements, totalPages } = data;

        this.entityList.items = elements.map((multiEntity) => {
          const dto = new BaseItemSearchPaneDto(
            multiEntity.entityCode,
            multiEntity.entityName,
            multiEntity.entityCode,
            multiEntity.entityCode,
            multiEntity.validEndDtm,
            multiEntity.validStartDtm,
            true,
            false
          );
          return { ...multiEntity, ...dto };
        }) as any;
        this.entityList.pagination = {
          totalSearchItems: totalElements,
          currentPage: page,
          pageSize: size,
          totalItems: totalElements,
          totalPages: totalPages,
        };
      },
      async getGroupList() {
        try {
          const params = {
            itemCode: this.paramsSearchMultiGroup.type,
            objName:
              this.paramsSearchMultiGroup.searchBy === "objName"
                ? this.paramsSearchMultiGroup.searchKey
                : undefined,
            objCode:
              this.paramsSearchMultiGroup.searchBy === "objCode"
                ? this.paramsSearchMultiGroup.searchKey
                : undefined,
            childOffrUuid: undefined,
            onlyValidDtm: true,
            page: this.paramsSearchMultiGroup.page,
            size: this.paramsSearchMultiGroup.size,
          };
          const param = removeUndefinedProperties(params);
          const res = await getGroup(param);
          const { page, size, totalElements, elements, totalPages } = res.data;
          this.multiGroupList.items = elements.map((group) => {
            const dto = new BaseItemSearchPaneDto(
              group.objUuid,
              group.objName,
              group.objCode,
              group.itemCode,
              group.validEndDtm,
              group.validStartDtm,
              true,
              true
            );
            return { ...group, ...dto };
          }) as any;
          this.multiGroupList.pagination = {
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
      async getOfferList() {
        try {
          const params = {
            itemCode: this.paramsSearchMultiOffer.type,
            name:
              this.paramsSearchMultiOffer.searchBy === "objName"
                ? this.paramsSearchMultiOffer.searchKey
                : undefined,
            code:
              this.paramsSearchMultiOffer.searchBy === "objCode"
                ? this.paramsSearchMultiOffer.searchKey
                : undefined,
            onlyValidDtm: true,
            page: this.paramsSearchMultiOffer.page,
            size: this.paramsSearchMultiOffer.size,
          };
          const param: any = removeUndefinedProperties(params);
          const { data } = await getProductsApi(param);
          const { page, size, totalElements, elements, totalPages } = data;
          this.multiOfferList.items = elements.map((offer) => {
            const dto = new BaseItemSearchPaneDto(
              offer.objUuid,
              offer.objName,
              offer.objCode,
              offer.itemCode,
              offer.validEndDtm,
              offer.validStartDtm,
              true,
              true
            );
            return { ...offer, ...dto };
          }) as any;
          this.multiOfferList.pagination = {
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
      async getListComponentSearch(params: RequestGetListComponentSearch) {
        const { data } = await getListComponentSearchApi(params);
        const { page, size, totalElements, elements, totalPages } = data;
        this.multiComponentList.items = elements.map((component) => {
          const dto = new BaseItemSearchPaneDto(
            component.uuid,
            component.name,
            component.code,
            component.itemCode,
            component.endDate,
            component.startDate,
            true,
            true,
            false,
            component.itemType
          );
          return { ...component, ...dto };
        }) as any;
        this.multiComponentList.pagination = {
          ...this.multiComponentList.pagination,
          totalSearchItems: totalElements,
          currentPage: page,
          pageSize: size,
          totalItems: totalElements,
          totalPages: totalPages,
        };
      },
      async getListResourceSearch() {
        const params = {
          itemCode: this.paramsSearchMultiResource.type,
          onlyValidDtm: true,
          page: this.paramsSearchMultiResource.page,
          size: this.paramsSearchMultiResource.size,
          objName:
            this.paramsSearchMultiResource.searchBy === "objName"
              ? this.paramsSearchMultiResource.searchKey
              : undefined,
          objCode:
            this.paramsSearchMultiResource.searchBy === "objCode"
              ? this.paramsSearchMultiResource.searchKey
              : undefined,
        };
        removeUndefinedProperties(params);
        const { data } = await getListResourceAdvancedApi(params);
        const { page, size, totalElements, elements, totalPages } = data;
        this.multiResourceList.items = elements.map((resource) => {
          const dto = new BaseItemSearchPaneDto(
            resource.objUuid,
            resource.objName,
            resource.objCode,
            resource.itemCode,
            resource.validEndDtm,
            resource.validStartDtm,
            true,
            true,
            false
          );
          return { ...resource, ...dto };
        }) as any;
        this.multiResourceList.pagination = {
          totalSearchItems: totalElements,
          currentPage: page,
          pageSize: size,
          totalItems: totalElements,
          totalPages: totalPages,
        };
      },
      async getEntityDetailInfo() {
        const params = {
          entityCode: this.selectedEntity.entityCode,
          entityTypeCode: this.selectedEntity.entityTypeCode,
        };
        const { data } = await getMultiEntityDetail(params);
        this.selectedEntityDetails = data;
        if (this.selectedEntityDetails) {
          let generalData;
          if (
            [
              MULTI_ENTITY_SUBTYPE.BUSINESS_LINE,
              MULTI_ENTITY_SUBTYPE.DISCOUNT_TARGET,
              MULTI_ENTITY_SUBTYPE.SALE_COMPANY,
            ].includes(this.selectedEntity.entityTypeCode)
          ) {
            generalData = cloneDeep(
              MULTI_ENTITY_DETAIL_DATA[this.selectedEntity.entityTypeCode]
            );
          } else {
            generalData = cloneDeep(MULTI_ENTITY_DETAIL_USER_TITLE);
          }
          generalData.forEach((item) => {
            if (this.selectedEntityDetails[item.key]) {
              item.value = this.selectedEntityDetails[item.key];
            }
          });
          this.entityDetailData.generalTab = generalData;
          this.entityDetailData.additionalTab =
            this.selectedEntityDetails.additional;
          this.selectedEntityDetails.additional =
            this.selectedEntityDetails.additional.map((item) => ({
              ...item,
              attrVal:
                item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                  ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                      value.trim()
                    ) || []
                  : item.attrVal,
            }));
        }
      },
      async getGroupDetailInfo(params: any) {
        try {
          const { data } = await getExtendsDependencyGroupTarget(params);
          return data;
        } catch (error) {
          throw error;
        }
      },
      async getOfferDetail(params: any) {
        try {
          const res = await getProductStructureDetailRootApi(params);
          return res;
        } catch (error) {
          throw error;
        }
      },
      async getComponentDetail(params: any) {
        try {
          const param = { objUuid: params };
          const res = await getComponentDetailApi(param);
          return res;
        } catch (error) {
          throw error;
        }
      },
      resetParamListEntitySearch() {
        this.paramsSearchEntity = cloneDeep(paramsExtendsEntitySearchDefault);
        this.paramsMultiEntitySearch = cloneDeep(
          paramsMultiEntitySearchDefault
        );
        this.entitySearchNmCd = NM_CD_FIELDS[0].value;
        this.entityList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      },
      handleResetMultiEntitySearch() {
        this.localSearch = cloneDeep(paramsExtendsEntitySearchDefault);
        this.selectedEntityDetails = null;
        this.entityDisplayForm = {
          entityDetail: false,
          groupSearch: false,
          offerSearch: false,
          componentSearch: false,
          resourceSearch: false,
        };
      },
      resetParamListGroupSearch() {
        this.paramsSearchMultiGroup = cloneDeep(
          paramsExtendsGroupSearchDefault
        );
        this.multiGroupSearchNmCd = NM_CD_FIELDS[0].value;
        this.multiGroupList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      },
      resetParamListOfferSearch() {
        this.paramsSearchMultiOffer = cloneDeep(
          paramsExtendsOfferSearchDefault
        );
        this.multiOfferSearchNmCd = NM_CD_FIELDS[0].value;
        this.multiOfferList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      },
      resetParamListComponentSearch() {
        this.paramsSearchMultiComponent = cloneDeep(
          paramsExtendsComponentSearchDefault
        );
        this.multiComponentList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      },
      resetParamListResourceSearch() {
        this.paramsSearchMultiResource = cloneDeep(
          paramsExtendsResourceSearchDefault
        );
        this.multiResourceList = {
          items: [],
          pagination: cloneDeep(defaultPagination),
        };
      },
      async updateMultiEntity(category: any) {
        const selectedValue = cloneDeep(this.selectedEntityDetails);
        selectedValue.additional = selectedValue.additional.map((item: any) => {
          return {
            ...item,
            attrVal:
              item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                ? JSON.stringify(item.attrVal)
                : item.attrVal,
          };
        });
        if (category === DETAIL_CATEGORY.SEARCH) {
          return await putMultiEntityDetail(selectedValue);
        } else {
          return await postMultiEntityDetail(selectedValue);
        }
      },
      closeAllSearchPanel() {
        this.entityDisplayForm = {
          ...this.entityDisplayForm,
          groupSearch: false,
          offerSearch: false,
          componentSearch: false,
          resourceSearch: false,
        };
      },
    },
  });
};

export const useMultiEntitySearchStore = createMultiStore(
  "multiEntitySearchStore"
);
export const useMultiEntityCreateStore = createMultiStore(
  "multiEntityCreateStore"
);
