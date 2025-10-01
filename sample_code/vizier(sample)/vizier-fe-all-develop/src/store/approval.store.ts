import { getListApprovalSearch } from "@/api/prod/approvalApi";
import { CODE_REVIEW_APPROVAL } from "@/constants/publish";
import { cloneDeep } from "lodash-es";
const approvalFlowSearchDefaultParams: any = {
  searchBy: "aprvFlowTmptName",
  keyword: undefined,
  page: 1,
  size: 5,
};
const defaultPaginationApprovalFlowSearch = {
  totalSearchItems: 0,
  currentPage: 1,
  pageSize: 10,
  totalItems: 0,
  totalPages: 0,
};

const useApprovalStore = defineStore("approvalStore", {
  state: () => ({
    paramFilterApprovalFlowSearch: cloneDeep(approvalFlowSearchDefaultParams),
    approvalFlowSearch: {
      items: [] as any[],
      pagination: cloneDeep(defaultPaginationApprovalFlowSearch),
    },
    approvalStepList: [] as any[],
  }),
  actions: {
    resetParamApprovalFlowSearch() {
      this.paramFilterApprovalFlowSearch = cloneDeep(
        approvalFlowSearchDefaultParams
      );
      this.approvalFlowSearch = {
        items: [] as any[],
        pagination: cloneDeep(defaultPaginationApprovalFlowSearch),
      };
    },

    async getApprovalFlowSearch() {
      try {
        const res = await getListApprovalSearch({
          [this.paramFilterApprovalFlowSearch.searchBy]:
            this.paramFilterApprovalFlowSearch?.keyword,
          page: this.paramFilterApprovalFlowSearch?.page,
          size: this.paramFilterApprovalFlowSearch?.size,
        });
        const { page, size, totalElements, elements, totalPages } = res?.data;
        if (elements) {
          this.approvalFlowSearch.items = elements.map((ele) => {
            return {
              numReview:
                ele.aprvFlowTmptStepLs?.filter((aprv) =>
                  [
                    CODE_REVIEW_APPROVAL.DESIGN,
                    CODE_REVIEW_APPROVAL.PRICING_REVIEW,
                    CODE_REVIEW_APPROVAL.IT_REVIEW,
                  ].includes(aprv.aprvStepCode)
                )?.length || 0,
              numApproval:
                ele.aprvFlowTmptStepLs?.filter((aprv) =>
                  [
                    CODE_REVIEW_APPROVAL.PRICING_REVIEW,
                    CODE_REVIEW_APPROVAL.IT_REVIEW,
                    CODE_REVIEW_APPROVAL.EXCUTE_APPROVAL,
                    CODE_REVIEW_APPROVAL.COMPLETE_APPROVAL,
                  ].includes(aprv.aprvStepCode)
                )?.length || 0,
              ...ele,
            };
          });
          this.approvalFlowSearch.pagination = {
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
  },
});

export default useApprovalStore;
