import { httpClient } from "@/utils/http-common";
import { NOTIFICATION_READ_URL, NOTIFICATION_URL } from "./path";

const getNotificationList = (params: any) => {
  return httpClient.get(NOTIFICATION_URL, { params });
};

const readNotification = (data: any) => {
  return httpClient.put(`${NOTIFICATION_READ_URL}/${data.userNotiUuid}`);
};

export { getNotificationList, readNotification };
