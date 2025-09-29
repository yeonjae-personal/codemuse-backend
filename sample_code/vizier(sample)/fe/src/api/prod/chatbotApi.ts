import axios from "axios";
import * as API_PATH from "@/api/prod/path";

const getChatbotResponse = (params: String) => {
  return axios
    .get("https://dev.vizier-service.com/" + API_PATH.UI_CHATBOT_REQUEST, {
      params: {
        message: params,
      },
    })
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
};
//test
const getRagStartResponse = () => {
  return axios
    .put("https://rag.vizier-service.com/" + API_PATH.UI_RAG_START, {
      user_id: "lg-cns",
      canvas_id: "5e620e1a-1f57-44e9-ad0d-e6af64659935",
      stream: false,
    })
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
};

const getRagResponse = async (query: String, conversation_id: String) => {
  return axios
    .put("https://rag.vizier-service.com/" + API_PATH.UI_RAG_CONVERSATION, {
      user_id: "lg-cns",
      canvas_id: "5e620e1a-1f57-44e9-ad0d-e6af64659935",
      stream: false,
      query: query,
      conversation_id: conversation_id,
    })
    .then((response) => {
      return response.data;
    });
};

const getChatHistory = async (conversation_id: String) => {
  return axios
    .get(
      "https://rag.vizier-service.com/" + API_PATH.UI_RAG_HISTORY + conversation_id,
      {
        params: {
          user_id: "lg-cns",
          canvas_id: "5e620e1a-1f57-44e9-ad0d-e6af64659935",
          withscores: false,
        },
      }
    )
    .then((response) => {
      return response.data;
    })
    .catch((error) => {
      throw error;
    });
};

export {
  getChatbotResponse,
  getRagStartResponse,
  getRagResponse,
  getChatHistory,
};
