import {
  getListPublishSearch,
  getPublishDetail,
  postPublishPackage,
  putPublishPackage,
  postPublishApproval,
  getListPublishItemsSearch,
  putApproveRejectAction,
  postApprovalRequestAction,
  postPublishRequestAction,
  postPublishCompose,
  postPublishValidate,
  getLoadDataProduction,
} from "@/api/prod/publishApi";
import {
  PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST,
  PUBLISH_TABS_VALUE,
  PUBLISH_CODE_STATUS,
  PUBLISH_FLOW_STATUS,
} from "@/constants/publish";
import { isExpiredTime } from "@/utils/format-data";
import { cloneDeep } from "lodash-es";
import { RequiredYn, SearchBy } from "@/enums";
import { ComposeItem } from "@/interfaces/prod/publishInterface";
import {
  BaseItemSearchPaneDto,
  BaseSearchPaneParamClass,
} from "@/types/common";

const publishSearchDefaultParams: any = {
  ...new BaseSearchPaneParamClass(" "),
};

const publishItemSearchDefaultParams: any = {
  searchBy: "chgUser",
  keyword: undefined,
  page: 1,
  size: 6,
};

const defaultPaginationPublishItemSearch = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};
const searchDefaultDto = {
  items: [] as any[],
  pagination: cloneDeep(defaultPaginationPublishItemSearch),
};
const searchComposeDefaultDto = {
  items: [] as ComposeItem[],
  pagination: cloneDeep(defaultPaginationPublishItemSearch),
};

const usePublishManagerStore = defineStore("publishManagerStore", {
  state: () => ({
    isOpenValidatePopup: false,
    isCreatePublish: false,
    isEditStep1: false,
    isCreateStep1: false,
    isEditStep2: false,
    isCreateStep2: false,
    isEditStep3: false,
    isCreateStep3: false,
    isEditStep4: false,
    isCreateStep4: false,
    isLoad: false,
    isRedirectFromNotification: false,
    isPackageExpired: undefined as any,
    currentTab: PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES,
    publishGeneralAttributesData: null as any,
    publishApprovalFlowData: null as any,
    publishGeneralAttributesListForm: cloneDeep(
      PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST
    ),
    publishStepData: null as any,
    paramFilterPublishSearch: cloneDeep(publishSearchDefaultParams),
    publishSearch: cloneDeep(searchDefaultDto),
    publishSelected: {} as any,
    paramFilterPublishItemSearch: cloneDeep(publishItemSearchDefaultParams),
    publishItemSearch: cloneDeep(searchComposeDefaultDto),
    composePackageItemList: [] as ComposeItem[],
    publishSearchStatusList: [] as any,
    publishModeList: [] as any,
    tabProcessStatus: {
      step1: PUBLISH_FLOW_STATUS.DEACTIVE,
      step2: PUBLISH_FLOW_STATUS.DEACTIVE,
      step3: PUBLISH_FLOW_STATUS.DEACTIVE,
      step4: PUBLISH_FLOW_STATUS.DEACTIVE,
    },
    dragItemType: "",
    approvalItemSelected: {} as any,
    publishPackageSearchInput: "",
    publishDetail: null as any,
  }),
  actions: {
    resetParamPublishSearch() {
      this.paramFilterPublishSearch = cloneDeep(publishSearchDefaultParams);
      this.publishSearch = cloneDeep(searchDefaultDto);
      this.publishSelected = null;
      this.isLoad = false;
    },
    resetParamPublishItemsSearch() {
      this.paramFilterPublishItemSearch = cloneDeep(
        publishItemSearchDefaultParams
      );
      this.publishItemSearch = cloneDeep(searchDefaultDto);
    },
    async getPublishSearch() {
      try {
        const res = await getListPublishSearch({
          pubRqstStusCode: this.paramFilterPublishSearch.type,
          pubRqstTaskCodeName:
            this.paramFilterPublishSearch.searchBy === SearchBy.Name
              ? this.paramFilterPublishSearch.searchKey
              : undefined,
          pubRqstTaskCode:
            this.paramFilterPublishSearch.searchBy === SearchBy.Code
              ? this.paramFilterPublishSearch.searchKey
              : undefined,
          page: this.paramFilterPublishSearch?.page,
          size: this.paramFilterPublishSearch?.size,
        });
        const { page, size, totalElements, elements, totalPages } = res.data;
        if (elements) {
          this.publishSearch.items =
            elements.map((ele) => {
              const dto = new BaseItemSearchPaneDto(
                ele.pubRqstTaskCode,
                ele.pubRqstTaskCodeName,
                ele.pubRqstTaskCode
              );
              return {
                ...ele,
                ...dto,
                itemType: isExpiredTime(ele.exprDtm)
                  ? PUBLISH_CODE_STATUS.EXPIRED
                  : ele.pubRqstStusCode,
                pubRqstStusCode: isExpiredTime(ele.exprDtm)
                  ? PUBLISH_CODE_STATUS.EXPIRED
                  : ele.pubRqstStusCode,
              };
            }) || [];
          this.publishSearch.pagination = {
            totalSearchItems: totalElements,
            currentPage: page,
            pageSize: size,
            totalItems: totalElements,
            totalPages: totalPages,
          };
        }
      } catch (error: any) {
        throw error;
      }
    },

    async getPublishItemsSearch() {
      try {
        const res = await getListPublishItemsSearch({
          [this.paramFilterPublishItemSearch.searchBy]:
            this.paramFilterPublishItemSearch?.keyword,
          page: this.paramFilterPublishItemSearch?.page,
          size: this.paramFilterPublishItemSearch?.size,
        });
        const { page, size, totalElements, elements, totalPages } = res.data;
        if (elements) {
          this.publishItemSearch.items = elements;
          this.publishItemSearch.pagination = {
            totalSearchItems: totalElements,
            currentPage: page,
            pageSize: size,
            totalItems: totalElements,
            totalPages: totalPages,
          };
        }
      } catch (error: any) {
        throw error;
      }
    },

    async upsetPublishPackage(data: any, isAdd = false) {
      try {
        return isAdd
          ? await postPublishPackage(data)
          : await putPublishPackage({
              ...data,
              pubRqstTaskCode: data.pubRqstTaskMDto?.pubRqstTaskCode,
            });
      } catch (error) {
        throw error;
      }
    },

    async upsetPublishCompose(data: any) {
      try {
        const res = await postPublishCompose({
          ...data,
          pubRqstTaskCode: this.publishGeneralAttributesData.pubRqstTaskCode,
        });
        return res;
      } catch (error) {
        throw error;
      }
    },

    async upsetPublishApproval(data: any) {
      try {
        return await postPublishApproval(data);
      } catch (error) {
        throw error;
      }
    },

    async putApproveOrReject(data: any) {
      try {
        return await putApproveRejectAction(data);
      } catch (error) {
        throw error;
      }
    },
    async postApprovalRequest(data: any) {
      try {
        return await postApprovalRequestAction(data);
      } catch (error) {
        throw error;
      }
    },
    async postPublishRequest(data: any) {
      try {
        return await postPublishRequestAction(data);
      } catch (error) {
        throw error;
      }
    },

    async publishValidated() {
      try {
        return await postPublishValidate({
          ...this.publishDetail,
          pubRqstTaskCode: this.publishGeneralAttributesData.pubRqstTaskCode,
        });
      } catch (error) {
        throw error;
      }
    },

    async getPublishPackageDetail(
      pubRqstTaskCode: string,
      isRedirect: boolean = false
    ) {
      try {
        const { data } = await getPublishDetail(pubRqstTaskCode);

        if (data) {
          if (isRedirect) {
            this.publishSelected = {
              ...data?.pubRqstTaskMDto,
              itemUnique: data?.pubRqstTaskMDto?.pubRqstTaskCode,
            };
          }
          this.isPackageExpired = isExpiredTime(data.pubRqstTaskMDto.exprDtm);
          this.publishDetail = cloneDeep({
            ...data,
            pubPrcsTaskMDto: {
              ...data.pubPrcsTaskMDto,
              pubPrcsRsvDtm: data.pubPrcsTaskMDto?.pubPrcsRsvDtm || null,
            },
          });
          this.publishGeneralAttributesData = data.pubRqstTaskMDto;
          this.publishApprovalFlowData = data.pubAprvMDto;
          this.composePackageItemList = data.chngDataLstDtos;
          this.publishStepData = {
            ...data.pubPrcsTaskMDto,
            pubPrcsRsvDtm: data.pubPrcsTaskMDto?.pubPrcsRsvDtm || null,
          };
          this.publishGeneralAttributesListForm =
            PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST.map((item) => {
              return {
                ...item,
                editYn: this.verifyAllowEdit(
                  this.publishDetail.pubRqstTaskMDto.pubRqstStusCode !==
                    PUBLISH_CODE_STATUS.DELAY
                    ? this.publishDetail.pubRqstTaskMDto.pubRqstStusCode
                    : this.publishDetail.pubRqstTaskMDto.pubRqstBfrStusCode,
                  this.isPackageExpired,
                  item
                ),
                attrVal: data.pubRqstTaskMDto[item.colName],
              };
            });

          this.verifyStepStatus(
            this.publishDetail.pubRqstTaskMDto.pubRqstStusCode !==
              PUBLISH_CODE_STATUS.DELAY
              ? this.publishDetail.pubRqstTaskMDto.pubRqstStusCode
              : this.publishDetail.pubRqstTaskMDto.pubRqstBfrStusCode
          );
        }
      } catch (error) {
        throw error;
      }
    },

    resetDataStep1() {
      // this.publishGeneralAttributesData = null;
      // this.publishGeneralAttributesListForm = cloneDeep(
      //   PUBLISH_GENERAL_ATTRIBUTES_FORM_LIST
      // );
      this.isEditStep1 = false;
      this.isCreateStep1 = false;
    },
    checkAllActions() {
      return (
        this.isCreateStep1 ||
        this.isCreateStep2 ||
        this.isCreateStep3 ||
        this.isCreateStep4 ||
        this.isEditStep1 ||
        this.isEditStep2 ||
        this.isEditStep3 ||
        this.isEditStep4
      );
    },
    resetAllStepStatus() {
      this.isCreateStep1 = false;
      this.isCreateStep2 = false;
      this.isCreateStep3 = false;
      this.isCreateStep4 = false;
      this.isEditStep1 = false;
      this.isEditStep2 = false;
      this.isEditStep3 = false;
      this.isEditStep4 = false;
      this.isCreatePublish = false;
    },
    resetAllStepData() {
      this.resetAllStepStatus();
      // this.publishSelected = null;
      this.publishSearch = cloneDeep(searchDefaultDto);
      this.resetDataStep1();
    },
    resetTabProcessStatus() {
      this.tabProcessStatus = {
        step1: PUBLISH_FLOW_STATUS.DEACTIVE,
        step2: PUBLISH_FLOW_STATUS.DEACTIVE,
        step3: PUBLISH_FLOW_STATUS.DEACTIVE,
        step4: PUBLISH_FLOW_STATUS.DEACTIVE,
      };
    },
    verifyStepStatus(statusCode) {
      switch (statusCode) {
        case PUBLISH_CODE_STATUS.CREATED:
        case PUBLISH_CODE_STATUS.COMPOSED:
          this.tabProcessStatus.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step2 = PUBLISH_FLOW_STATUS.DEACTIVE;
          this.tabProcessStatus.step3 = PUBLISH_FLOW_STATUS.DEACTIVE;
          this.tabProcessStatus.step4 = PUBLISH_FLOW_STATUS.DEACTIVE;
          break;
        case PUBLISH_CODE_STATUS.VALIDATED:
          this.tabProcessStatus.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step3 =
            this.publishApprovalFlowData.pubAprvStepLDtos?.length > 1
              ? PUBLISH_FLOW_STATUS.COMPLETE
              : PUBLISH_FLOW_STATUS.DEACTIVE;
          this.tabProcessStatus.step4 = PUBLISH_FLOW_STATUS.DEACTIVE;
          break;
        case PUBLISH_CODE_STATUS.INPROGRESS:
          this.tabProcessStatus.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step3 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step4 = PUBLISH_FLOW_STATUS.DEACTIVE;
          break;
        case PUBLISH_CODE_STATUS.PUBLISH_REQUEST:
          this.tabProcessStatus.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step3 = PUBLISH_FLOW_STATUS.COMPLETE;
          this.tabProcessStatus.step4 = PUBLISH_FLOW_STATUS.COMPLETE;
          break;
        default:
          this.tabProcessStatus.step1 = PUBLISH_FLOW_STATUS.DEACTIVE;
          this.tabProcessStatus.step2 = PUBLISH_FLOW_STATUS.DEACTIVE;
          this.tabProcessStatus.step3 = PUBLISH_FLOW_STATUS.DEACTIVE;
          this.tabProcessStatus.step4 = PUBLISH_FLOW_STATUS.DEACTIVE;
          break;
      }
    },
    verifyAllowEdit(status, expire, item) {
      if (expire) {
        return status === PUBLISH_CODE_STATUS.DELAY
          ? RequiredYn.No
          : item.colName == "exprDtm"
            ? RequiredYn.Yes
            : RequiredYn.No;
      }
      switch (status) {
        case PUBLISH_CODE_STATUS.CREATED:
        case PUBLISH_CODE_STATUS.COMPOSED:
        case PUBLISH_CODE_STATUS.VALIDATED:
          return item.editYn;
        case PUBLISH_CODE_STATUS.INPROGRESS:
          return item.colName == "exprDtm" ? RequiredYn.Yes : RequiredYn.No;
        case PUBLISH_CODE_STATUS.DELAY:
        case PUBLISH_CODE_STATUS.PUBLISH_REQUEST:
          return RequiredYn.No;
        default:
          return item.editYn;
      }
    },
    async requestLoad() {
      try {
        return await getLoadDataProduction();
      } catch (error) {
        throw error;
      }
    },
  },
});

export default usePublishManagerStore;
