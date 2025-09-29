import { httpClient } from "@/utils/http-common";
import useGlobal from "./global.store";
import router from "@/router/index";
import { removeTabMenuWhenNoTabName } from "@/utils/common-util";

const useUserStore = defineStore("users", {
  state: () => ({
    users: [],
    user: {},
    userMenuList: [],
    module: {},
    error: null,
    isShowLogin: true,
  }),
  getters: {
    getUsers(state: any) {
      return state.users;
    },
    getError(state: any) {
      return state.error;
    },
  },
  actions: {
    async getUserInfo() {
      if (!this.user["userId"]) {
        this.user = JSON.parse(localStorage.getItem("userInfo") || "{}");
        this.userMenuList = this.user.userMenuList.userMenuList;
      }
      return this.user;
    },
    async getUserMenuList() {
      if (!this.user["userId"]) {
        this.user = JSON.parse(localStorage.getItem("userInfo") || "{}");
        this.userMenuList = this.user.userMenuList.userMenuList;
        this.modules = import.meta.glob("@/pages/**/*.vue");
      }
      return this.userMenuList;
    },
    async setMenuRouter(userMenuList) {
      userMenuList.forEach((ele) => {
        if (ele.children) this.setMenuRouter(ele.children);
        if (ele.scrnId) {
          const scrnLinkUrl = ele.scrnLinkUrl.substring(1);
          router.addRoute("tempui", {
            path: scrnLinkUrl,
            name: ele.menuNm,
            component: this.modules[ele.scrnPathNm],
            meta: { title: ele.menuNm },
          });
        }
      });
    },
    async login(user: any) {
      try {
        const userParam = {
          params: {
            userId: user.userId,
            userPw: user.password,
          },
        };
        const userInfo = await httpClient.post(
          "/api/comm/login",
          userParam.params
        );
        if (userInfo != null) {
          this.user = userInfo.data;
          this.userMenuList = this.user.userMenuList.userMenuList;
          localStorage.setItem("userInfo", JSON.stringify(userInfo.data));
          localStorage.removeItem("conversationId");
          removeTabMenuWhenNoTabName();
          this.error = null;
          router.replace("/functions/product-platform");
        }
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
    async logout() {
      try {
        const logout = confirm("로그아웃 하시겠습니까?");
        if (logout) {
          await httpClient.post("/api/comm/login/logout");
          localStorage.removeItem("userInfo");
          localStorage.removeItem("conversationId");
          removeTabMenuWhenNoTabName();
          window.location.href = "/";
        }
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
    async register(user: any) {
      try {
        const response = await httpClient.post("users/register", user);
        this.error = null;
        return response.data;
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
    async updateUser(userId: number, user: any) {
      try {
        const response = await httpClient.put(`users/${userId}`, user);
        this.error = null;
        return response.data;
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
    async deleteUser(userId: number) {
      try {
        const response = await httpClient.delete(`users/${userId}`);
        this.error = null;
        return response.data;
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
    async changeLevelForAllRecords(dataToUpdate: any) {
      try {
        const response = await httpClient.put("users/update", dataToUpdate);
        return response.data;
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
    async showModal() {
      const globalStore = useGlobal();
      const NoticePageModal = () =>
        import("@/pages/functions/notice/NoticePageModal.vue");
      const objectModal: any = {
        title: "",
        component: NoticePageModal,
        dataInput: {},
        width: "800",
      };
      await globalStore.openModal(objectModal);
    },
  },
});

export default useUserStore;
