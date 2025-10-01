import { getMenuTreeApi } from "@/api/admin/adminApi";
import { MenuSearchParam } from "@/interfaces/admin/admin";

const useMenuStoreInfo = defineStore("menu", {
  state: () => ({
    menuItemsInfo: [],
    menuItemsInfoPopup: [],
  }),

  actions: {
    async fetchMenuTree(params?: MenuSearchParam) {
      try {
        const response = await getMenuTreeApi(params);
        this.menuItemsInfo = response.data;
      } catch (error: any) {
        throw error;
      }
    },
    async fetchMenuTreePopup(params: MenuSearchParam) {
      try {
        const response = await getMenuTreeApi(params);
        this.menuItemsInfoPopup = response.data;
      } catch (error: any) {
        throw error;
      }
    },
  },
});

export default useMenuStoreInfo;
