import cloneDeep from "lodash-es/cloneDeep";
import { formatDate, removeUndefinedProperties } from "@/utils/format-data";
import { getUiImpactAnalysisItems } from "@/api/prod/impactAnalysisApi";
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";
import {
  EXTENDS_VIEW,
  EXTEND_CATEGORY,
  GRID_PARAMS_DEFAULT,
  SEARCH_CATEGORY,
} from "@/constants/extendsManager";
import {
  getExtendsDependencyFollower,
  getExtendsDependencyLeader,
  getExtendsDependencyTarget,
  putExtendsDependencyValidity,
  postExtendsDependencyTarget,
  getExtendsDependencyRelationDefinition,
  getExtendsDependencyCountTarget,
  getGroupDetail,
  getGroup,
  getRelationSearchAdvanced,
  getRelationListDataTable,
} from "@/api/prod/extendsApi";
import { getProductStructureDetailRootApi } from "@/api/prod/productApi";
import {
  ParamsUIExtendsDependencyAddGroup,
  ParamsUIExtendsDependencyAddOffer,
} from "@/interfaces/prod/extends";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import {
  ADVENCED_SEARCH_PARAMS_DEFAULT,
  DETAIL_TAB_TYPE,
  SPACE,
  VIEW_MODE,
} from "@/constants/";
import { getUserInfor } from "@/constants/userInfor";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";
import { SearchBy } from "@/enums";

const paramsExtendsFilterOfferSearchDefault = {
  ...new BaseSearchPaneParamClass(),
};
const paramsExtendsFilterGroupSearchDefault = {
  ...new BaseSearchPaneParamClass(),
};
const paramsHightlightSearchDefault = {
  type: undefined,
  searchBy: NM_CD_FIELDS[0].value,
  keyword: undefined,
};
const defaultPagination = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};
const detailViewDataDefault = {
  isShowLeaderCol: false,
  isShowFollowerCol: false,
  focusColumnLeaderList: [] as any,
  focusColumnFollowerList: [] as any,
  expandColumnLeaderList: [] as any,
  expandColumnFollowerList: [] as any,
  leaderCoordinates: [] as any,
  followerCoordinates: [] as any,
  focusLeaderCoordinates: null as any,
  focusFollowerCoordinates: null as any,
  leaderLineCoordinates: [] as any,
  followerLineCoordinates: [] as any,
  listGroupFollowerFocusRemove: [] as any,
  listGroupLeaderFocusRemove: [] as any,
};
const paramsExtendsTargetPostDefault = {
  addOffrDpdcLst: new Array<ParamsUIExtendsDependencyAddOffer>(),
  updateOffrDpdcLst: new Array<ParamsUIExtendsDependencyAddOffer>(),
  insertGroupOfferLst: new Array<ParamsUIExtendsDependencyAddGroup>(),
  chgUser: "",
  chgDeptName: "",
};
const structureActiveMap = {
  relation: null as any,
  relationGroup: null as any,
  offer: { relCode: null, offrUuid: null },
  group: { relCode: null, grpUuid: null, targetUuid: null },
};
const structureFocusActiveMap = {
  offer: { uuid: null, index: null, area: null },
  group: { uuid: null, index: null, area: null },
};
const createRelationStore = (storeName) => {
  return defineStore(storeName, {
    state: () => ({
      sideDisplay: {
        offerSearch: true,
        targetSearch: false,
        relationSearch: false,
        relationDetail: false,
        targetDetail: false,
        offerDetail: false,
      },
      viewMode: VIEW_MODE.GRID,
      currentTab: OFFER_TABS_VALUE.DEFINITION,
      extendsView: EXTENDS_VIEW.SIMPLE,
      detailViewData: cloneDeep(detailViewDataDefault),
      targetSearch: null as any,
      structureActiveMapLeader: cloneDeep(structureActiveMap),
      structureActiveMapFollower: cloneDeep(structureActiveMap),
      structureActiveFocusColumn: cloneDeep(structureFocusActiveMap),
      relationDetail: {
        additionalTab: null as any,
        generalTab: null as any,
        historyTab: [],
      },
      targetDetail: {
        generalTab: {} as any[],
        additionalTab: [] as any[],
        offerTab: [] as any[],
        offerTabPagination: cloneDeep(defaultPagination),
        historyTab: [] as any[],
      },
      selectedStructure: null,
      selectedStructureData: {
        general: [] as any[],
        additional: [] as any[],
      },
      selectedNmCdOfferSearch: SearchBy.Name,
      selectedNmCdTargetSearch: NM_CD_FIELDS[0].value,
      selectedNmCdRelationSearch: cloneDeep({
        ...paramsExtendsFilterOfferSearchDefault,
        type: undefined,
        searchBy: NM_CD_FIELDS[0].value,
      }),
      paramsExtendsFilterOfferSearch: cloneDeep(
        paramsExtendsFilterOfferSearchDefault
      ),
      paramsExtendsTargetSearchOffer: cloneDeep(
        paramsExtendsFilterOfferSearchDefault
      ),
      paramsExtendsTargetSearchGroup: cloneDeep(
        paramsExtendsFilterGroupSearchDefault
      ) as any,
      paramsHightlightSearch: cloneDeep(paramsHightlightSearchDefault) as any,
      paramsExtendsRelationSearch: cloneDeep(ADVENCED_SEARCH_PARAMS_DEFAULT),
      extendOfferSearch: {
        items: [],
        pagination: cloneDeep(defaultPagination),
      },
      extendOfferTargetSearch: {
        items: [],
        pagination: cloneDeep(defaultPagination),
      },
      extendGroupTargetSearch: {
        items: [],
        pagination: cloneDeep(defaultPagination),
      },
      extendRelationSearch: {
        items: [],
        pagination: cloneDeep(defaultPagination),
      },
      paramsExtendsTargetPost: cloneDeep(paramsExtendsTargetPostDefault),
      selectedItem: null as any,
      selectedTargetSearchItem: null as any,
      expandLeaderItem: null as any,
      expandFollowerItem: null as any,
      expandLeaderItemIndex: null as any,
      expandFollowerItemIndex: null as any,
      relationCodeList: null as any,
      groupCodeList: null,
      isEdit: false,
      isDuplicate: false,
      isApplied: false,
      isSearchHightLight: false,
      activeGroupUuid: null,
      followerOffsetTop: 0 as any,
      offerItemCodeList: [] as any[],
      groupItemCodeList: [] as any[],
      allowOfferDrop: [] as any[],
      allowGroupDrop: [] as any[],
      listFocusLeaderRemove: [] as any[],
      listFocusFollowerRemove: [] as any[],
      isSearchGroupParentOffer: false,
      selectedRelation: null as any,
      initInput: null as any,
      listView: {
        itemsPerPage: 10,
        currentPage: 1,
        totalItems: 0,
        totalPages: 0,
        pageSize: 10,
        items: [],
      },
      paramListView: cloneDeep(GRID_PARAMS_DEFAULT),
      isLoading: false,
      isResetSelectTable: false,
      isDuplicateInitData: false,
    }),
    getters: {
      isGridMode(): boolean {
        return this.viewMode === VIEW_MODE.GRID;
      },
    },
    actions: {
      async getExtendsListOfferSearch(
        category: any,
        onlyValidDtm: boolean = false
      ) {
        try {
          if (category === SEARCH_CATEGORY.OFFER) {
            const params = {
              type: "O",
              subType: this.paramsExtendsFilterOfferSearch.type,
              prodItemNm:
                this.paramsExtendsFilterOfferSearch.searchBy === SearchBy.Name
                  ? this.paramsExtendsFilterOfferSearch.searchKey
                  : undefined,
              prodItemCd:
                this.paramsExtendsFilterOfferSearch.searchBy === SearchBy.Code
                  ? this.paramsExtendsFilterOfferSearch.searchKey
                  : undefined,
              page: this.paramsExtendsFilterOfferSearch.page,
              size: this.paramsExtendsFilterOfferSearch.size,
              onlyValidDtm: onlyValidDtm,
            };
            removeUndefinedProperties(params);
            const res = await getUiImpactAnalysisItems({
              ...params,
            });
            const { page, size, totalElements, elements, totalPages } =
              res.data;
            this.extendOfferSearch.items = elements.map((offer) => {
              const dto = new BaseItemSearchPaneDto(
                offer.objUuid,
                offer.objName,
                offer.objCode,
                offer.itemCode,
                offer.validEndDtm,
                offer.validStartDtm,
                true,
                false
              );
              return { ...offer, ...dto };
            }) as any;
            this.extendOfferSearch.pagination = {
              totalSearchItems: totalElements,
              currentPage: page,
              pageSize: size,
              totalItems: totalElements,
              totalPages: totalPages,
            };
          } else if (category === SEARCH_CATEGORY.TARGET) {
            const params = {
              type: "O",
              subType: this.paramsExtendsTargetSearchOffer.type,
              prodItemNm:
                this.paramsExtendsTargetSearchOffer.searchBy === SearchBy.Name
                  ? this.paramsExtendsTargetSearchOffer.searchKey
                  : undefined,
              prodItemCd:
                this.paramsExtendsTargetSearchOffer.searchBy === SearchBy.Code
                  ? this.paramsExtendsTargetSearchOffer.searchKey
                  : undefined,
              page: this.paramsExtendsTargetSearchOffer.page,
              size: this.paramsExtendsTargetSearchOffer.size,
              onlyValidDtm: onlyValidDtm,
            };
            removeUndefinedProperties(params);
            const res = await getUiImpactAnalysisItems(params);

            const { page, size, totalElements, elements, totalPages } =
              res.data;
            this.extendOfferTargetSearch.items = elements.map((offer) => {
              const dto = new BaseItemSearchPaneDto(
                offer.objUuid,
                offer.objName,
                offer.objCode,
                offer.itemCode,
                offer.validEndDtm,
                offer.validStartDtm,
                true,
                false
              );
              return { ...offer, ...dto };
            }) as any;
            this.extendOfferTargetSearch.pagination = {
              totalSearchItems: totalElements,
              currentPage: page,
              pageSize: size,
              totalItems: totalElements,
              totalPages: totalPages,
            };
          }
        } catch (error) {
          throw error;
        }
      },
      async getOfferBeingDuplicated(uuid) {
        try {
          removeUndefinedProperties(this.paramsExtendsFilterOfferSearch);
          const res = await getUiImpactAnalysisItems({
            ...this.paramsExtendsFilterOfferSearch,
            size: 7,
          });
          if (res?.data && res.data?.elements?.length) {
            this.selectedItem = {
              ...res.data.elements[0],
              offerBeClonedUuid: uuid,
            };
            await this.getGroupBySelectedItem(false, true);
            this.isDuplicateInitData = true;
          }
        } catch (error) {
          throw error;
        }
      },
      async getGroupListTargetSearch(
        childOffrUuid?: string,
        onlyValidDtm: boolean = false
      ) {
        try {
          const params = {
            itemCode: this.paramsExtendsTargetSearchGroup.type,
            objName:
              this.paramsExtendsTargetSearchGroup.searchBy === "objName"
                ? this.paramsExtendsTargetSearchGroup.searchKey
                : undefined,
            objCode:
              this.paramsExtendsTargetSearchGroup.searchBy === "objCode"
                ? this.paramsExtendsTargetSearchGroup.searchKey
                : undefined,
            childOffrUuid: childOffrUuid,
            onlyValidDtm: onlyValidDtm,
            page: this.paramsExtendsTargetSearchGroup.page,
            size: this.paramsExtendsTargetSearchGroup.size,
          };
          removeUndefinedProperties(params);
          const res = await getGroup({ ...params });
          const { page, size, totalElements, elements, totalPages } = res.data;
          this.extendGroupTargetSearch.items = elements.map((group) => {
            const dto = new BaseItemSearchPaneDto(
              group.objUuid,
              group.objName,
              group.objCode,
              group.itemCode,
              group.validEndDtm,
              group.validStartDtm,
              true,
              false
            );
            return { ...group, ...dto };
          }) as any;
          this.extendGroupTargetSearch.pagination = {
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
      async getRelationSearch() {
        removeUndefinedProperties(this.paramsExtendsRelationSearch);
        const res = await getRelationSearchAdvanced(
          this.paramsExtendsRelationSearch
        );
        // const { pageSize, rowSize, rows, currentPage, list, totalRows } =
        //   res.data;
        const { page, size, totalElements, elements, totalPages } = res.data;
        this.extendRelationSearch.items = elements as any;
        this.extendRelationSearch.pagination = {
          totalSearchItems: totalElements,
          currentPage: page,
          pageSize: size,
          totalItems: totalElements,
          totalPages: totalPages,
        };
      },
      async getGroupBySelectedItem(includeGroup, offerDuplicateMode) {
        if (this.selectedItem) {
          const prodUuid = this.selectedItem["prodUuid"];
          const { data } = await getExtendsDependencyTarget({
            offerUuid: prodUuid,
            onlyValidDtm: offerDuplicateMode,
          });
          const countData = await getExtendsDependencyCountTarget({
            targetUuid: prodUuid,
            includeGroup: this.extendsView === EXTENDS_VIEW.SIMPLE,
          });
          const { data: selectedLeaderList } = await getExtendsDependencyLeader(
            {
              targetUuid: offerDuplicateMode
                ? this.selectedItem.offerBeClonedUuid
                : this.selectedItem.prodUuid,
              onlyValidDtm: offerDuplicateMode,
              includeGroup: includeGroup,
            }
          );
          const { data: selectedFollowerList } =
            await getExtendsDependencyFollower({
              targetUuid: offerDuplicateMode
                ? this.selectedItem.offerBeClonedUuid
                : this.selectedItem.prodUuid,
              onlyValidDtm: offerDuplicateMode,
              includeGroup: includeGroup,
            });
          this.selectedItem = {
            ...this.selectedItem,
            countData: countData.data,
            leaderList: this.convertRelationData(
              selectedLeaderList,
              offerDuplicateMode
            ),
            followerList: this.convertRelationData(
              selectedFollowerList,
              offerDuplicateMode
            ),
          };
          this.detailViewData.focusColumnLeaderList = [
            this.selectedItem,
            { name: "dropBox" },
            ...data.leaderGrp.map((group) => ({
              ...group,
              followerList: this.convertRelationData(
                group.followerList,
                offerDuplicateMode
              ),
            })),
          ] as any;
          this.detailViewData.focusColumnFollowerList = [
            this.selectedItem,
            { name: "dropBox" },
            ...data.followerGrp.map((group) => ({
              ...group,
              leaderList: this.convertRelationData(
                group.leaderList,
                offerDuplicateMode
              ),
            })),
          ] as any;
          if (offerDuplicateMode) {
            this.paramsExtendsTargetPost.addOffrDpdcLst =
              this.paramsExtendsTargetPost.addOffrDpdcLst.concat([
                ...selectedLeaderList
                  .filter(
                    (item) =>
                      item.parentUuid === this.selectedItem.offerBeClonedUuid
                  )
                  .map((item: any) => ({
                    baseUuid: item.targetUuid,
                    trgtUuid: this.selectedItem.objUuid,
                    dpdcRelUuid: item.dpdcRelUuid,
                    validStartDtm: formatDate(new Date()),
                    validEndDtm: null,
                  })),
                ...selectedFollowerList
                  .filter(
                    (item) =>
                      item.parentUuid === this.selectedItem.offerBeClonedUuid
                  )
                  .map((item: any) => ({
                    baseUuid: this.selectedItem.objUuid,
                    trgtUuid: item.targetUuid,
                    dpdcRelUuid: item.dpdcRelUuid,
                    validStartDtm: formatDate(new Date()),
                    validEndDtm: null,
                  })),
              ]);
          }
        }
      },
      convertRelationData(data, isDuplicate) {
        if (!data?.length) {
          return [];
        }
        const uniqueRelArray = data
          .map((item: any) => item.dpdcRelUuid)
          .filter(
            (value: any, index: any, self: any) => self.indexOf(value) === index
          );
        const relList = uniqueRelArray.map((value: string) => {
          const child = data.filter((item: any) => {
            if (item.dpdcRelUuid === value) {
              if (item.parentUuid === this.selectedItem.offerBeClonedUuid) {
                item["allowRemove"] = true;
              }
              item["isDuplicate"] = isDuplicate;
              return item;
            }
          });
          return {
            dpdcRelUuid: value,
            dpdcRelCode: child[0].dpdcRelCode,
            dpdcRelName: child[0].dpdcRelName,
            relationValidStartDate: child[0].relationValidStartDate,
            relationValidEndDate: child[0].relationValidEndDate,
            child: child,
            isAdded: isDuplicate,
          };
        });
        return relList;
      },
      async getLeaderList(
        groupUuid: String | null = null,
        offerDuplicateMode = false,
        indexItem = 0,
        includeGroup = true
      ) {
        if (!this.selectedItem) {
          return;
        }
        const { data } = await getExtendsDependencyLeader({
          targetUuid: groupUuid
            ? groupUuid
            : this.selectedItem[
                offerDuplicateMode ? "offerBeClonedUuid" : "prodUuid"
              ],
          onlyValidDtm: offerDuplicateMode,
          includeGroup: includeGroup,
        });

        const uniqueRelArray = data
          .map((item: any) => item.dpdcRelUuid)
          .filter(
            (value: any, index: any, self: any) => self.indexOf(value) === index
          );
        const relList = uniqueRelArray.map((value: string) => {
          const child = data.filter((item: any) => {
            if (item.dpdcRelUuid === value) {
              if (groupUuid) {
                item["disable"] = !item.referenceUuids.includes(groupUuid);
              }
              if (item.parentUuid === this.selectedItem.offerBeClonedUuid) {
                item["allowRemove"] = true;
              }
              item["isDuplicate"] = offerDuplicateMode;
              return item;
            }
          });
          return {
            dpdcRelUuid: value,
            dpdcRelCode: child[0].dpdcRelCode,
            dpdcRelName: child[0].dpdcRelName,
            relationValidStartDate: child[0].relationValidStartDate,
            relationValidEndDate: child[0].relationValidEndDate,
            child: child,
            isAdded: offerDuplicateMode,
          };
        });
        // if (groupUuid) {
        //   relList.map((rel: any) => {
        //     let disable = true;
        //     rel.child.forEach((el: any) => {
        //       if (!el.disable) {
        //         disable = false;
        //       }
        //     });
        //     rel["disable"] = !!groupUuid ? disable : false;
        //     return rel;
        //   });
        // }
        if (this.detailViewData.focusColumnFollowerList[indexItem as number]) {
          this.detailViewData.focusColumnFollowerList[indexItem as number][
            "isFetch"
          ] = true;
          this.detailViewData.focusColumnFollowerList[indexItem as number][
            "leaderList"
          ] = cloneDeep(relList);
        }

        if (offerDuplicateMode && data?.length && indexItem === 0) {
          this.paramsExtendsTargetPost.addOffrDpdcLst =
            this.paramsExtendsTargetPost.addOffrDpdcLst.concat(
              data
                .filter(
                  (item) =>
                    item.parentUuid === this.selectedItem.offerBeClonedUuid
                )
                .map((item: any) => ({
                  baseUuid: item.targetUuid,
                  trgtUuid: this.selectedItem.objUuid,
                  dpdcRelUuid: item.dpdcRelUuid,
                  validStartDtm: formatDate(new Date()),
                  validEndDtm: null,
                }))
            );
        }
      },
      async getFollowerList(
        groupUuid: String | null = null,
        offerDuplicateMode = false,
        indexItem = 0,
        includeGroup = true
      ) {
        if (!this.selectedItem) {
          return;
        }
        const { data } = await getExtendsDependencyFollower({
          targetUuid: groupUuid
            ? groupUuid
            : this.selectedItem[
                offerDuplicateMode ? "offerBeClonedUuid" : "prodUuid"
              ],
          onlyValidDtm: offerDuplicateMode,
          includeGroup: includeGroup,
        });

        const uniqueRelArray = data
          .map((item: any) => item.dpdcRelUuid)
          .filter(
            (value: any, index: any, self: any) => self.indexOf(value) === index
          );
        const relList = uniqueRelArray.map((value: string) => {
          const child = data.filter((item: any) => {
            if (item.dpdcRelUuid === value) {
              if (groupUuid) {
                item["disable"] = !item.referenceUuids.includes(groupUuid);
              }
              if (item.parentUuid === this.selectedItem.offerBeClonedUuid) {
                item["allowRemove"] = true;
              }
              item["isDuplicate"] = offerDuplicateMode;
              return item;
            }
          });
          return {
            dpdcRelUuid: value,
            dpdcRelCode: child[0].dpdcRelCode,
            dpdcRelName: child[0].dpdcRelName,
            relationValidStartDate: child[0].relationValidStartDate,
            relationValidEndDate: child[0].relationValidEndDate,
            child: child,
            isAdded: offerDuplicateMode,
          };
        });
        // if (groupUuid) {
        //   relList.map((rel: any) => {
        //     let disable = true;
        //     rel.child.forEach((el: any) => {
        //       if (!el.disable) {
        //         disable = false;
        //       }
        //     });
        //     rel["disable"] = !!groupUuid ? disable : false;
        //     return rel;
        //   });
        // }
        if (this.detailViewData.focusColumnLeaderList[indexItem as number]) {
          this.detailViewData.focusColumnLeaderList[indexItem as number][
            "isFetch"
          ] = true;
          this.detailViewData.focusColumnLeaderList[indexItem as number][
            "followerList"
          ] = cloneDeep(relList);
        }

        if (offerDuplicateMode && data?.length && indexItem === 0) {
          this.paramsExtendsTargetPost.addOffrDpdcLst =
            this.paramsExtendsTargetPost.addOffrDpdcLst.concat(
              data
                .filter(
                  (item) =>
                    item.parentUuid === this.selectedItem.offerBeClonedUuid
                )
                .map((item: any) => ({
                  baseUuid: this.selectedItem.objUuid,
                  trgtUuid: item.targetUuid,
                  dpdcRelUuid: item.dpdcRelUuid,
                  validStartDtm: formatDate(new Date()),
                  validEndDtm: null,
                }))
            );
        }
      },
      async getRelationDataTable() {
        try {
          this.isLoading = true;
          const { data } = await getRelationListDataTable(this.paramListView);
          this.listView.items = data.elements.map(
            (item: any, index: number) => ({
              ...item,
              no: index + 1 + (data.page - 1) * data.size,
            })
          );
          this.listView.itemsPerPage = data.size;
          this.listView.currentPage = data.page;
          this.listView.totalItems = data.totalElements;
          this.listView.totalPages = data.totalPages;
          this.listView.pageSize = data.size;
        } catch (error) {
          throw error;
        } finally {
          this.isLoading = false;
        }
      },
      addParamsExtendsTargetPost(param: ParamsUIExtendsDependencyAddOffer) {
        this.paramsExtendsTargetPost.addOffrDpdcLst.push(param);
      },
      updateParamsExtendsTargetPost(param: ParamsUIExtendsDependencyAddOffer) {
        this.paramsExtendsTargetPost.updateOffrDpdcLst.push(param);
      },
      addGroupRelationOffer(param: ParamsUIExtendsDependencyAddGroup) {
        const indexFind =
          this.paramsExtendsTargetPost.insertGroupOfferLst.findIndex(
            (item) => item.groupUuid === param.groupUuid && item.isNew
          );
        if (indexFind === -1) {
          this.paramsExtendsTargetPost.insertGroupOfferLst.push(param);
        }
      },
      async postExtendTarger(duplicateMode = false) {
        if (duplicateMode) {
          this.paramsExtendsTargetPost.insertGroupOfferLst = [
            ...this.detailViewData.focusColumnLeaderList.filter(
              (item) => item?.offerGroupUuid
            ),
            ...this.detailViewData.focusColumnFollowerList.filter(
              (item) => item?.offerGroupUuid
            ),
          ]
            .reduce((acc, current) => {
              if (
                !acc.some(
                  (item) => item.offerGroupUuid === current.offerGroupUuid
                )
              ) {
                acc.push(current);
              }
              return acc;
            }, [])
            .map((group) => ({
              groupUuid: group?.offerGroupUuid,
              groupName: group?.offerGroupName,
              offerUuid: this.selectedItem?.objUuid,
              validStartDtm: group?.validStartDtm || formatDate(new Date()),
              validEndDtm: group?.validStartDtm,
            }));
        }
        this.paramsExtendsTargetPost.chgUser = getUserInfor().chgUser;
        this.paramsExtendsTargetPost.chgDeptName = getUserInfor().chgDeptName;
        try {
          const res = await postExtendsDependencyTarget(
            this.paramsExtendsTargetPost
          );
          return res;
        } catch (error) {
          throw error;
        }
      },
      async updateExtendOffer(param: any) {
        try {
          const res = await putExtendsDependencyValidity(param);
          return res;
        } catch (error) {
          throw error;
        }
      },
      removeItemExtendsTargetPost(item: any, location: String) {
        this.paramsExtendsTargetPost.addOffrDpdcLst =
          this.paramsExtendsTargetPost.addOffrDpdcLst.filter((addItem) => {
            if (location === EXTEND_CATEGORY.FOLLOWER) {
              return addItem.trgtUuid !== item?.targetUuid;
            } else {
              return addItem.baseUuid !== item?.targetUuid;
            }
          });
      },
      removeRelationExtendsTargetPost(uuid: string) {
        this.paramsExtendsTargetPost.addOffrDpdcLst =
          this.paramsExtendsTargetPost.addOffrDpdcLst.filter(
            (item: any) => item.objUuid != uuid
          );
      },
      removeGroupRelationOffer(uuid: string, categories: string) {
        if (categories === EXTEND_CATEGORY.FOLLOWER) {
          this.paramsExtendsTargetPost.addOffrDpdcLst =
            this.paramsExtendsTargetPost.addOffrDpdcLst.filter(
              (rel) => rel.trgtUuid !== uuid || rel?.parentUuid !== uuid
            );
        } else {
          this.paramsExtendsTargetPost.addOffrDpdcLst =
            this.paramsExtendsTargetPost.addOffrDpdcLst.filter(
              (rel) => rel.baseUuid !== uuid || rel?.parentUuid !== uuid
            );
        }
        const findIndexFollower =
          categories === EXTEND_CATEGORY.FOLLOWER
            ? this.detailViewData.focusColumnLeaderList.findIndex(
                (item) => item?.offerGroupUuid === uuid && item.isNew
              )
            : this.detailViewData.focusColumnFollowerList.findIndex(
                (item) => item?.offerGroupUuid === uuid && item.isNew
              );
        if (findIndexFollower === -1) {
          this.paramsExtendsTargetPost.insertGroupOfferLst =
            this.paramsExtendsTargetPost.insertGroupOfferLst.filter(
              (item) => item.groupUuid !== uuid
            );
        }
      },
      resetParamsExtendsTargetPost() {
        this.paramsExtendsTargetPost = cloneDeep(
          paramsExtendsTargetPostDefault
        );
      },
      resetLeaderFollowerList() {
        this.detailViewData.expandColumnFollowerList = [];
        this.detailViewData.expandColumnLeaderList = [];
      },
      resetExtendParamFilterOfferSearch() {
        this.selectedNmCdOfferSearch = NM_CD_FIELDS[0].value;
        this.paramsExtendsFilterOfferSearch = cloneDeep(
          paramsExtendsFilterOfferSearchDefault
        );
        this.extendOfferSearch.items = [];
      },
      resetParamsExtendsTargetSearchGroup() {
        this.selectedNmCdTargetSearch = NM_CD_FIELDS[0].value;
        this.paramsExtendsTargetSearchGroup = cloneDeep(
          paramsExtendsFilterGroupSearchDefault
        );
        this.paramsExtendsTargetSearchGroup.type = SPACE;
        this.extendGroupTargetSearch.items = [];
      },
      resetParamsExtendsTargetSearchOffer() {
        this.selectedNmCdTargetSearch = NM_CD_FIELDS[0].value;
        this.paramsExtendsTargetSearchOffer = cloneDeep(
          paramsExtendsFilterOfferSearchDefault
        );
        this.extendOfferTargetSearch.items = [];
      },
      resetHightlightParamSearch() {
        this.paramsHightlightSearch = cloneDeep(paramsHightlightSearchDefault);
      },
      resetRelationParamSearch() {
        this.selectedNmCdRelationSearch = cloneDeep({
          ...paramsExtendsFilterOfferSearchDefault,
          type: undefined,
          searchBy: NM_CD_FIELDS[0].value,
        });
        this.paramsExtendsRelationSearch = cloneDeep(
          ADVENCED_SEARCH_PARAMS_DEFAULT
        );
        this.extendRelationSearch.items = [];
      },
      resetDetailViewData() {
        this.detailViewData = cloneDeep(detailViewDataDefault);
        this.expandLeaderItem = null;
        this.expandFollowerItem = null;
        this.expandFollowerItemIndex = null;
        this.expandLeaderItemIndex = null;
      },
      resetListTable() {
        this.paramListView = cloneDeep(GRID_PARAMS_DEFAULT);
        this.listView.items = [];
        this.isResetSelectTable = true;
      },
      calculateLeaderCoordinates() {
        const list: Array<Array<any>> = [];
        if (this.detailViewData.focusFollowerCoordinates != null) {
          const expandItemCoor = [
            56,
            this.detailViewData.focusFollowerCoordinates + 33,
          ];
          this.detailViewData.leaderCoordinates.forEach((coor) => {
            const leaderCoor = [0, coor + 33];
            list.push([leaderCoor, expandItemCoor]);
          });
          this.detailViewData.leaderLineCoordinates = list as any;
        }
      },
      calculateFollowerCoordinates() {
        const list: Array<Array<any>> = [];
        if (this.detailViewData.focusLeaderCoordinates != null) {
          const expandItemCoor = [
            0,
            this.detailViewData.focusLeaderCoordinates + 33,
          ];
          this.detailViewData.followerCoordinates.forEach((coor) => {
            const followerCoor = [56, coor + 33];
            list.push([expandItemCoor, followerCoor]);
          });
          this.detailViewData.followerLineCoordinates = list as any;
        }
      },
      async getExtendsDependencyRelationDefinitionDetail(dpdcRelCode: string) {
        try {
          const { data } =
            await getExtendsDependencyRelationDefinition(dpdcRelCode);
          this.relationDetail.generalTab = [
            ...data.general,
            ...data.additional.filter(
              (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
            ),
          ];
          this.relationDetail.additionalTab = data.additional.filter(
            (item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL
          );
        } catch (error) {
          throw error;
        }
      },
      async getExtendsDependencyRelationTargetDetail(targetCd: string) {
        try {
          const res = await getExtendsDependencyTarget(targetCd);
          this.targetDetail.generalTab = res.data;
        } catch (error) {
          throw error;
        }
      },
      async getProductStructureDetailRoot(params: any) {
        try {
          const res = await getProductStructureDetailRootApi(params);
          return res;
        } catch (error) {
          throw error;
        }
      },
      async getGroupDetailInfo(params: any) {
        try {
          const { data } = await getGroupDetail(params);
          if (data) {
            data.general = data.general
              .sort((after, before) => after.sortNo - before.sortNo)
              .map((item) => {
                if (item.fieldTypeCode === COLUMN_FIELD_TYPE.DP) {
                  return {
                    ...item,
                    attrVal: item.attrVal,
                  };
                }
                return item;
              });
            this.targetDetail.generalTab = [
              ...data.general,
              ...data?.additional.filter(
                (item) => item.dispTab === DETAIL_TAB_TYPE.GENERAL
              ),
            ];
            this.targetDetail.additionalTab = data?.additional
              .filter((item) => item.dispTab !== DETAIL_TAB_TYPE.GENERAL)
              .map((item: any) => ({
                ...item,
                attrVal:
                  item.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                    ? JSON.parse(item?.attrVal)?.filter((value: any) =>
                        value.trim()
                      ) || []
                    : item.attrVal,
              }));
            this.targetDetail.offerTab = data.childOffr;
          }
        } catch (error) {
          throw error;
        }
      },
      setSelectedStructureData(structureDataVal: any) {
        this.selectedStructureData = structureDataVal;
      },
      resetStructureActiveMap() {
        this.structureActiveMapLeader = cloneDeep(structureActiveMap);
        this.structureActiveMapFollower = cloneDeep(structureActiveMap);
        this.structureActiveFocusColumn = cloneDeep(structureFocusActiveMap);
        this.activeGroupUuid = null;
      },
      closeAllDetail() {
        this.sideDisplay.relationDetail = false;
        this.sideDisplay.offerDetail = false;
        this.sideDisplay.targetDetail = false;
      },
    },
  });
};
const useExtendManagerStore = createRelationStore("extendManagerStore");
const useRelationManagerDuplicateStore = createRelationStore(
  "relationManagerDuplicateStore"
);

export { useExtendManagerStore, useRelationManagerDuplicateStore };
