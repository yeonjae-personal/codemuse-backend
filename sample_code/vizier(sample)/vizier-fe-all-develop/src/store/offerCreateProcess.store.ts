import { getCtgPath } from "@/api/prod/categoryApi";

const useOfferCreateProcessStore = defineStore("offerCreateProcessStore", {
  state: () => ({
    offerCreateInCategoryMode: false,
    reChooseCategoryMode: false,
    isFormOfferEdit: false,
    isFormOfferDuplicate: false,
    categoryNodeUuid: null as any,
    offerCreateTemp: null as any,
    categorySelected: null as any,
    categoryDuplicateModeSelected: null as any,
    categoryEditModeSelected: null as any,
    categoryTab: null as any,
    pathCategorySelected: [],
    itemTypeOffer: null as any,
  }),
  actions: {
    resetProcess() {
      this.offerCreateInCategoryMode = false;
      this.reChooseCategoryMode = false;
      this.isFormOfferEdit = false;
      this.isFormOfferDuplicate = false;
      this.categoryNodeUuid = null;
      this.categorySelected = null; //value category selected in offer create page
      this.categoryEditModeSelected = null; //value category selected in offer edit page
      this.categoryDuplicateModeSelected = null; //value category selected in offer duplicate page
      this.offerCreateTemp = null;
      this.categoryTab = null;
      this.itemTypeOffer = null;
      this.pathCategorySelected = [];
    },
    async getCtgPathRequest(nodeId) {
      const res = await getCtgPath({
        ctgrNodeUuid: nodeId,
      });
      if (res.data) {
        this.pathCategorySelected = res.data;
        return res.data;
      }
    },
  },
});

export default useOfferCreateProcessStore;
