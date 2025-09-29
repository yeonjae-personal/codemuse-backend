import Stomp from "stompjs";
import SockJS from "sockjs-client";

let socket = null as any;
let stompClient = null as any;
const env = import.meta.env.VITE_API_PROD_URL;

export const connectWebSocket = (key, callback) => {
  const wsUrl = `${env}prod/ws`;

  if (stompClient && stompClient.connected) {
    stompClient.disconnect();
  }

  socket = new SockJS(wsUrl);
  stompClient = Stomp.over(socket);

  stompClient.connect(
    {},
    () => {
      stompClient.subscribe(key, (data) => {
        const res = JSON.parse(data.body);
        if (res) {
          callback(res);
        }
      });
    },
    (error) => {
      console.error("Connect failed: " + error);
    }
  );
};

// export function sendMessage(message) {
//   if (stompClient && stompClient.connected) {
//     stompClient.send("/app/sendNotification", {}, JSON.stringify(message));
//   } else {
//     console.log("Chưa kết nối tới server!");
//   }
// }
