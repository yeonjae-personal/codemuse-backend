import {
  getNotificationList,
  readNotification,
} from "@/api/prod/notificationApi";

const useNotificationStore = defineStore("notification", {
  state: () => ({
    notifications: [] as any,
  }),

  actions: {
    async fetchNotification(params) {
      try {
        const response = await getNotificationList(params);
        this.notifications = response.data;
      } catch (error: any) {
        throw error;
      }
    },
    async readNotification(data) {
      try {
        return await readNotification(data);
      } catch (error: any) {
        throw error;
      }
    },
  },
});

export default useNotificationStore;
