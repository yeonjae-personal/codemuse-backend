import { httpClient } from "@/utils/http-common";

const useCmcdStore = defineStore("cmcd", {
  state: () => ({
    cmcdDetlId: {},
    error: null,
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
    async search(listCode: Array<string>) {
      try {
        const userParam = {
          cmcdGrpIds: listCode,
        };

        const cmcdInfo = await httpClient.post(
          "/api/comm/cmcd/v1/cmcdCmcdGrpIdSearch",
          userParam
        );

        if (cmcdInfo != null) {
          this.error = null;
          this.cmcdDetlId = cmcdInfo.data.data;
          return this.cmcdDetlId;
        }
      } catch (error: any) {
        this.error = error as null;
        throw error.response.data;
      }
    },
  },
});

export default useCmcdStore;
