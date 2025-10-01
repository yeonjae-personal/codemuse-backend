<template>
  <Teleport to="body">
    <div class="chat-bot-swagger">
      <UseDraggable
        v-if="isResize && floatButtonLeft && floatButtonTop"
        :handle="actionBtn"
        :initial-value="{ x: floatButtonLeft, y: floatButtonTop }"
        class="fixed z-[99999]"
        stop-propagation
        @move="handleBtnMove"
        @end="handleBtnEnd"
      >
        <div class="flex flex-column gap-[24px] position-relative">
          <div
            class="chat-bot-list flex flex-column"
            :class="{ open: isShowListBtn }"
          >
            <!-- <div
              class="chat-bot-btn"
              @click="handleOpenChatFrame(CHATBOT_TYPE.AI_CHATBOT)"
            >
              <AIChatBotIcon width="18.5px" height="18.5px" />
              <span>AI Chatbot</span>
            </div> -->
            <div
              class="chat-bot-btn"
              @click="handleOpenChatFrame(CHATBOT_TYPE.USER_POCKET)"
            >
              <UserPocketIcon />
              <span>User Pocket</span>
            </div>
            <!-- <div
              class="chat-bot-btn"
              @click="handleOpenChatFrame(CHATBOT_TYPE.COMPARE)"
            >
              <CompareIcon />
              <span>Compare</span>
            </div> -->
          </div>

          <div
            ref="actionBtn"
            class="action-btn"
            :class="{ close: isShowListBtn }"
            @click="handleShowListBtn"
          >
            <template v-if="!isShowListBtn">
              <v-icon color="#fff" icon="mdi-dots-horizontal"></v-icon>
            </template>
            <template v-else>
              <ChatBotCloseIcon />
            </template>
          </div>
        </div>
      </UseDraggable>
      <div class="chat-frame-container">
        <template v-if="isShowAIFrame">
          <ChatBotFrame
            ref="chatBotFrame"
            :chat-id="CHAT_ID.AI_CHATBOT"
            :type="CHATBOT_TYPE.AI_CHATBOT"
            :chat-content="botMessageList"
            :suggestion-messages="SUGGESTION_MESSAGES"
            :init-coordinates="chatbotCoordinates"
            :z-index="indexAIFrame"
            :size="frameAISize"
            @on-close="handleCloseChatFrame"
            @on-rating="handleRating"
            @on-chat="handleChat"
            @on-view-all="handViewAllOffer"
            @on-show-less="handleShowLessOffer"
            @on-delete="handleClearChat"
            @on-moved="handleFrameMoved"
            @on-start="handleFrameStart"
            @on-end="handleFrameEnd"
            @on-resize="handleFrameResize"
            @on-click-bot-item="handleClickBotItem"
          />
        </template>
        <template v-if="isShowUserFrame">
          <ChatBotFrame
            ref="userFrame"
            :chat-id="CHAT_ID.USER_POCKET"
            :type="CHATBOT_TYPE.USER_POCKET"
            :init-coordinates="userCoordinates"
            :z-index="indexUserFrame"
            :size="frameUserSize"
            @on-close="handleCloseChatFrame"
            @on-moved="handleFrameMoved"
            @on-start="handleFrameStart"
            @on-end="handleFrameEnd"
            @on-resize="handleFrameResize"
          />
        </template>
        <template v-if="isShowCompareFrame">
          <ChatBotFrame
            ref="compareFrame"
            :chat-id="CHAT_ID.COMPARE"
            :type="CHATBOT_TYPE.COMPARE"
            :init-coordinates="compareCoordinates"
            :z-index="indexCompareFrame"
            :size="frameCompareSize"
            @on-close="handleCloseChatFrame"
            @on-moved="handleFrameMoved"
            @on-start="handleFrameStart"
            @on-end="handleFrameEnd"
            @on-resize="handleFrameResize"
          />
        </template>
      </div>
    </div>
  </Teleport>
</template>

<!-- eslint-disable id-length -->
<!-- eslint-disable security/detect-object-injection -->
<script setup>
import { formatDate } from "@/utils/format-data";
import { CHATBOT_TYPE, CHAT_ID, OWNER_MESSAGE } from "@/constants/chatbot";
import { SUGGESTION_MESSAGES } from "./mock";
import { useWindowSize } from "@vueuse/core";
import { UseDraggable } from "@vueuse/components";
import ChatBotFrame from "./ChatBotFrame.vue";
import {
  getRagStartResponse,
  getRagResponse,
  getChatHistory,
} from "@/api/prod/chatbotApi.ts";
import { useMenuStore, useProductsStore } from "@/store";
import { configPath, findMenuItem } from "@/utils/config-path";
import cloneDeep from "lodash-es/cloneDeep";
import { marked } from "marked";
import userPocketStore from "@/store/userPocket.store";
import { MenuItemID } from "@/enums/redirect";

const { countUserPocketItems, countUserPocketGroup } =
  storeToRefs(userPocketStore());
const productStore = useProductsStore();
const { menuTree } = storeToRefs(useMenuStore());
const { showProductDetail, selectedProduct, productDetails, productType } =
  storeToRefs(productStore);
const { width, height } = useWindowSize();

const INIT_COORDINATES = {
  x: width.value - 630,
  y: height.value - 680,
};
const INIT_SIZE = {
  frameWidth: null,
  frameHeight: null,
};

const frameUserPocketHeight = computed(() => {
  return (
    132 + countUserPocketItems.value * 58 + countUserPocketGroup.value * 47
  );
});
const DEFAULT_Z_INDEX = 990;
const actionBtn = ref();
const isDragActionBtn = ref(false);
const isShowListBtn = ref(false);
const isShowAIFrame = ref(false);
const isShowUserFrame = ref(false);
const isShowCompareFrame = ref(false);
const isResize = ref(true);
const indexAIFrame = ref(DEFAULT_Z_INDEX);
const indexUserFrame = ref(DEFAULT_Z_INDEX);
const indexCompareFrame = ref(DEFAULT_Z_INDEX);
const frameAISize = ref({ frameWidth: "480px", frameHeight: "640" });
const frameUserSize = ref({
  frameWidth: "348",
  frameHeight: frameUserPocketHeight.value.toString(),
});
const frameCompareSize = ref({ frameWidth: "480px", frameHeight: "640" });
const chatBotFrame = ref();
const userFrame = ref();
const compareFrame = ref();
const chatBotFrameMoved = ref(false);
const userFrameMoved = ref(false);
const compareFrameMoved = ref(false);
const chatbotCoordinates = ref({
  x: width.value - 630,
  y: height.value - 680,
});
const userCoordinates = ref({
  x: width.value - 365,
  y: 67,
});
const compareCoordinates = ref({
  x: width.value - 630,
  y: height.value - 680,
});
const botMessageList = ref([]);
const floatButtonLeft = ref(0);
const floatButtonTop = ref(0);
const floatButtonPercentLeft = ref(0.97);
const floatButtonPercentTop = ref(0.86);
const chatResponse = ref();
const structureData = ref();
const showStructureDetail = ref();
const showListStructure = ref();
const selectedComponent = ref();
const showComponentDetail = ref();
const conversationId = ref(null);

onMounted(async () => {
  conversationId.value = localStorage.getItem("conversationId");
  if (conversationId.value === null) {
    const response = await getRagStartResponse();
    conversationId.value = response.object.conversation_id;
    localStorage.setItem("conversationId", conversationId.value);
    botMessageList.value.push({
      owner: "bot",
      message: response.object.answer,
      htmlContent: "",
      sendTime: formatDate(new Date(), "hh:mm A"),
      react: null,
    });
  } else {
    await getPrevChat();
  }

  if (width.value && height.value) {
    floatButtonLeft.value = width.value * floatButtonPercentLeft.value;
    floatButtonTop.value = height.value * floatButtonPercentTop.value;
  }
});

watch(
  () => [width.value, height.value],
  (val) => {
    isResize.value = false;
    nextTick(() => {
      floatButtonLeft.value = val[0] * floatButtonPercentLeft.value;
      floatButtonTop.value = val[1] * floatButtonPercentTop.value;
      isResize.value = true;
    });
  }
);

const getPrevChat = async () => {
  const response = await getChatHistory(conversationId.value);
  const dataList = response.data;
  dataList.forEach((item) => {
    const role = item.message.role;
    const content = marked(item.message.content.replace(/\\n/g, "<br>"));
    const createdAt = item.created_at;
    botMessageList.value.push({
      owner: role === "user" ? OWNER_MESSAGE.USER : OWNER_MESSAGE.BOT,
      message: "",
      htmlContent: content,
      sendTime: formatDate(new Date(createdAt), "hh:mm A"),
      react: null,
    });
  });
};

const showOfferSearchPage = async (element) => {
  productType.value = element.type;
  await productStore.getProducts({
    page: 1,
    size: 10,
    objCode: element.objCode,
    itemCode: element.itemCode,
  });
  closeDetail();
};

const closeDetail = () => {
  showProductDetail.value = false;
  productDetails.value = null;
  selectedProduct.value = null;
  selectedComponent.value = null;
  structureData.value = null;
  showStructureDetail.value = false;
  showListStructure.value = false;
  showComponentDetail.value = false;
};

const handleClickBotItem = async (event) => {
  if (selectedProduct.value?.objUUID !== event.objUUID) {
    const offerPath = findMenuItem(menuTree.value, MenuItemID.OfferSearch);
    if (!offerPath) return;
    await showOfferSearchPage(event);
    offerPath["path"] = configPath(offerPath);
    addTab(offerPath);
  }
};

const handleShowListBtn = () => {
  if (!isDragActionBtn.value) isShowListBtn.value = !isShowListBtn.value;
};

const handleOpenChatFrame = async (param) => {
  isShowListBtn.value = false;
  switch (param) {
    case CHATBOT_TYPE.AI_CHATBOT:
      if (isShowAIFrame.value) {
        handleFrameStart(CHATBOT_TYPE.AI_CHATBOT);
        break;
      }
      if (userFrame.value && compareFrame.value) {
        if (!userFrameMoved.value) {
          //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, true);
        }
        if (!compareFrameMoved.value) {
          chatDisplayLogic(CHATBOT_TYPE.COMPARE, true);
        }
      } else {
        if (userFrame.value && !userFrameMoved.value) {
          //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, true);
        } else if (compareFrame.value && !compareFrameMoved.value) {
          chatDisplayLogic(CHATBOT_TYPE.COMPARE, true);
        }
      }
      isShowAIFrame.value = true;
      await nextTick();
      handleFrameStart(CHATBOT_TYPE.AI_CHATBOT);
      break;
    case CHATBOT_TYPE.USER_POCKET:
      if (isShowUserFrame.value) {
        handleFrameStart(CHATBOT_TYPE.USER_POCKET);
        break;
      }
      isShowUserFrame.value = true;
      await nextTick();
      handleFrameStart(CHATBOT_TYPE.USER_POCKET);
      break;
    case CHATBOT_TYPE.COMPARE:
      if (isShowCompareFrame.value) {
        handleFrameStart(CHATBOT_TYPE.COMPARE);
        break;
      }
      if (userFrame.value && chatBotFrame.value) {
        if (!userFrameMoved.value) {
          //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, true);
        }
        if (!chatBotFrameMoved.value) {
          chatDisplayLogic(CHATBOT_TYPE.AI_CHATBOT, true);
        }
      } else {
        if (chatBotFrame.value && !chatBotFrameMoved.value) {
          chatDisplayLogic(CHATBOT_TYPE.AI_CHATBOT, true);
        } else if (userFrame.value && !userFrameMoved.value) {
          //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, true);
        }
      }
      isShowCompareFrame.value = true;
      await nextTick();
      handleFrameStart(CHATBOT_TYPE.COMPARE);
      break;
  }
};
const handleCloseChatFrame = (value) => {
  switch (value) {
    case CHATBOT_TYPE.AI_CHATBOT:
      frameAISize.value = cloneDeep(INIT_SIZE);
      if (userFrame.value && compareFrame.value) {
        if (
          chatbotCoordinates.value.x > compareCoordinates.value.x &&
          chatbotCoordinates.value.x > userCoordinates.value.x
        ) {
          if (!compareFrameMoved.value && !userFrameMoved.value) {
            //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
            chatDisplayLogic(CHATBOT_TYPE.COMPARE, false);
          } else if (!userFrameMoved.value) {
            resetCoordinates(CHATBOT_TYPE.USER_POCKET);
          } else if (!compareFrameMoved.value) {
            resetCoordinates(CHATBOT_TYPE.COMPARE);
          }
        } else if (
          chatbotCoordinates.value.x > compareCoordinates.value.x &&
          chatbotCoordinates.value.x < userCoordinates.value.x
        ) {
          if (!compareFrameMoved.value) {
            //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
          }
        } else if (
          chatbotCoordinates.value.x < compareCoordinates.value.x &&
          chatbotCoordinates.value.x > userCoordinates.value.x
        ) {
          if (!userFrameMoved.value) {
            //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
          }
        }
      } else {
        if (
          userFrame.value &&
          !userFrameMoved.value &&
          chatbotCoordinates.value.x > userCoordinates.value.x
        ) {
          //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
        } else if (
          compareFrame.value &&
          !compareFrameMoved.value &&
          chatbotCoordinates.value.x > compareCoordinates.value.x
        ) {
          chatDisplayLogic(CHATBOT_TYPE.COMPARE, false);
        }
      }
      isShowAIFrame.value = false;
      chatBotFrameMoved.value = false;
      chatbotCoordinates.value = cloneDeep(INIT_COORDINATES);
      break;
    case CHATBOT_TYPE.USER_POCKET:
      isShowUserFrame.value = false;
      userFrameMoved.value = false;
      userCoordinates.value = { x: width.value - 365, y: 67 };
      break;
    case CHATBOT_TYPE.COMPARE:
      frameCompareSize.value = cloneDeep(INIT_SIZE);
      if (chatBotFrame.value && userFrame.value) {
        if (
          compareCoordinates.value.x > userCoordinates.value.x &&
          compareCoordinates.value.x > chatbotCoordinates.value.x
        ) {
          if (!userFrameMoved.value && !chatBotFrameMoved.value) {
            chatDisplayLogic(CHATBOT_TYPE.AI_CHATBOT, false);
            //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
          } else if (!chatBotFrameMoved.value) {
            resetCoordinates(CHATBOT_TYPE.AI_CHATBOT);
          } else if (!userFrameMoved.value) {
            resetCoordinates(CHATBOT_TYPE.USER_POCKET);
          }
        } else if (
          compareCoordinates.value.x > userCoordinates.value.x &&
          compareCoordinates.value.x < chatbotCoordinates.value.x
        ) {
          if (!compareFrameMoved.value) {
            //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
          }
        } else if (
          compareCoordinates.value.x < userCoordinates.value.x &&
          compareCoordinates.value.x > chatbotCoordinates.value.x
        ) {
          if (!chatBotFrameMoved.value) {
            chatDisplayLogic(CHATBOT_TYPE.AI_CHATBOT, false);
          }
        }
      } else {
        if (
          chatBotFrame.value &&
          !chatBotFrameMoved.value &&
          compareCoordinates.value.x > chatbotCoordinates.value.x
        ) {
          chatDisplayLogic(CHATBOT_TYPE.AI_CHATBOT, false);
        } else if (
          userFrame.value &&
          !userFrameMoved.value &&
          compareCoordinates.value.x > userCoordinates.value.x
        ) {
          //chatDisplayLogic(CHATBOT_TYPE.USER_POCKET, false);
        }
      }
      isShowCompareFrame.value = false;
      compareFrameMoved.value = false;
      compareCoordinates.value = cloneDeep(INIT_COORDINATES);
      break;
  }
};
const chatDisplayLogic = async (type, value) => {
  switch (type) {
    case CHATBOT_TYPE.AI_CHATBOT:
      if (value) {
        isShowAIFrame.value = false;
        chatbotCoordinates.value = {
          ...chatbotCoordinates.value,
          x: chatbotCoordinates.value.x - 504,
        };
        await nextTick();
        isShowAIFrame.value = true;
      } else {
        isShowAIFrame.value = false;
        chatbotCoordinates.value = {
          ...chatbotCoordinates.value,
          x: chatbotCoordinates.value.x + 504,
        };
        await nextTick();
        isShowAIFrame.value = true;
      }
      break;
    case CHATBOT_TYPE.USER_POCKET:
      if (value) {
        isShowUserFrame.value = false;
        userCoordinates.value = {
          ...userCoordinates.value,
          x: userCoordinates.value.x - 504,
        };
        await nextTick();
        isShowUserFrame.value = true;
      } else {
        isShowUserFrame.value = false;
        userCoordinates.value = {
          ...userCoordinates.value,
          x: userCoordinates.value.x + 504,
        };
        await nextTick();
        isShowUserFrame.value = true;
      }
      break;
    case CHATBOT_TYPE.COMPARE:
      if (value) {
        isShowCompareFrame.value = false;
        compareCoordinates.value = {
          ...compareCoordinates.value,
          x: compareCoordinates.value.x - 504,
        };
        await nextTick();
        isShowCompareFrame.value = true;
      } else {
        isShowCompareFrame.value = false;
        compareCoordinates.value = {
          ...compareCoordinates.value,
          x: compareCoordinates.value.x + 504,
        };
        await nextTick();
        isShowCompareFrame.value = true;
      }
      break;
  }
};
const resetCoordinates = async (event) => {
  switch (event) {
    case CHATBOT_TYPE.AI_CHATBOT:
      isShowAIFrame.value = false;
      chatbotCoordinates.value = cloneDeep(INIT_COORDINATES);
      await nextTick();
      isShowAIFrame.value = true;
      break;
    case CHATBOT_TYPE.USER_POCKET:
      isShowUserFrame.value = false;
      //userCoordinates.value = cloneDeep(INIT_COORDINATES);
      await nextTick();
      isShowUserFrame.value = true;
      break;
    case CHATBOT_TYPE.COMPARE:
      isShowCompareFrame.value = false;
      compareCoordinates.value = cloneDeep(INIT_COORDINATES);
      await nextTick();
      isShowCompareFrame.value = true;
      break;
  }
};
const handleFrameMoved = (event) => {
  const { type, cor } = event;
  const { x, y } = cor;
  switch (type) {
    case CHATBOT_TYPE.AI_CHATBOT:
      chatBotFrameMoved.value = true;
      chatbotCoordinates.value.x = x;
      chatbotCoordinates.value.y = y;
      break;
    case CHATBOT_TYPE.USER_POCKET:
      userFrameMoved.value = true;
      userCoordinates.value.x = x;
      userCoordinates.value.y = y;
      break;
    case CHATBOT_TYPE.COMPARE:
      compareFrameMoved.value = true;
      compareCoordinates.value.x = x;
      compareCoordinates.value.y = y;
      break;
  }
};

const handleFrameStart = (event) => {
  const maxIndex = Math.max(
    indexAIFrame.value,
    indexUserFrame.value,
    indexCompareFrame.value
  );
  switch (event) {
    case CHATBOT_TYPE.AI_CHATBOT:
      indexAIFrame.value = maxIndex;
      indexUserFrame.value = maxIndex - 1;
      indexCompareFrame.value = maxIndex - 1;
      break;
    case CHATBOT_TYPE.USER_POCKET:
      indexUserFrame.value = maxIndex;
      indexAIFrame.value = maxIndex - 1;
      indexCompareFrame.value = maxIndex - 1;
      break;
    case CHATBOT_TYPE.COMPARE:
      indexCompareFrame.value = maxIndex;
      indexUserFrame.value = maxIndex - 1;
      indexAIFrame.value = maxIndex - 1;
      break;
  }
};

const handleFrameEnd = (param) => {
  const { event, frame, type } = param;
  const { x, y } = event;
  switch (type) {
    case CHATBOT_TYPE.AI_CHATBOT:
      if (width.value <= x + frame.clientWidth || x <= 95) {
        isShowAIFrame.value = false;
        chatbotCoordinates.value.x =
          x <= 95 ? 105 : width.value - frame.clientWidth - 10;
      }
      if (height.value <= y + frame.clientHeight || y <= 0) {
        isShowAIFrame.value = false;
        chatbotCoordinates.value.y =
          y <= 0 ? 10 : height.value - frame.clientHeight - 10;
      }
      nextTick(() => {
        isShowAIFrame.value = true;
      });
      break;
    case CHATBOT_TYPE.USER_POCKET:
      if (width.value <= x + frame.clientWidth || x <= 95) {
        isShowUserFrame.value = false;
        userCoordinates.value.x =
          x <= 95 ? 105 : width.value - frame.clientWidth - 10;
      }
      if (height.value <= y + frame.clientHeight || y <= 0) {
        isShowUserFrame.value = false;
        userCoordinates.value.y =
          y <= 0 ? 10 : height.value - frame.clientHeight - 10;
      }
      nextTick(() => {
        isShowUserFrame.value = true;
      });
      break;
    case CHATBOT_TYPE.COMPARE:
      if (width.value <= x + frame.clientWidth || x <= 95) {
        isShowCompareFrame.value = false;
        compareCoordinates.value.x =
          x <= 95 ? 105 : width.value - frame.clientWidth - 10;
      }
      if (height.value <= y + frame.clientHeight || y <= 0) {
        isShowCompareFrame.value = false;
        compareCoordinates.value.y =
          y <= 0 ? 10 : height.value - frame.clientHeight - 10;
      }
      nextTick(() => {
        isShowCompareFrame.value = true;
      });
      break;
  }
};

const handleRating = (event) => {
  botMessageList.value[event.index] = {
    ...botMessageList.value[event.index],
    react: event.react,
  };
};

const handleChat = async (event) => {
  handleChatRAGAction(event, OWNER_MESSAGE.USER);
  handleChatRAGAction(event, OWNER_MESSAGE.BOT);
};

const handleChatRAGAction = async (param, user) => {
  const now = new Date();
  switch (user) {
    case OWNER_MESSAGE.USER:
      const chatObj = {
        owner: user,
        message: param,
        htmlContent: null,
        sendTime: formatDate(now, "hh:mm A"),
        react: null,
      };
      botMessageList.value.push(chatObj);
      break;
    case OWNER_MESSAGE.BOT:
      const idx = botMessageList.value.push({
        owner: OWNER_MESSAGE.BOT,
        message: null,
        htmlContent: "답변을 생성하고 있습니다...",
        sendTime: formatDate(now, "hh:mm A"),
        react: null,
      });
      await handleRAGResponse(param);
      const responseObj = botMessageList.value[idx - 1];
      let result = chatResponse.value.object.answer;
      const markdownContent = result.replace(/\\n/g, "<br>");
      responseObj.htmlContent = marked(markdownContent);

      break;
  }
};

const handViewAllOffer = (event) => {
  botMessageList.value[event].listItem =
    chatResponse.value.resultList.dataList.list;
  botMessageList.value[event].listStatus = "all";
};

const handleShowLessOffer = (event) => {
  botMessageList.value[event].listItem =
    chatResponse.value.resultList.dataList.list.slice(0, 5);
  botMessageList.value[event].listStatus = "less";
};

const handleRAGResponse = async (event) => {
  try {
    const response = await getRagResponse(event, conversationId.value);
    chatResponse.value = response;
  } catch (error) {
    console.error(error);
  }
};

const handleClearChat = () => {
  botMessageList.value = [];
};

const handleBtnMove = (event) => {
  const { x, y } = event;
  floatButtonLeft.value = x;
  floatButtonTop.value = y;
  isDragActionBtn.value = true;
};

const handleBtnEnd = (event) => {
  const { x, y } = event;
  const { clientWidth, clientHeight } = actionBtn.value;
  calPercentLeftTop(x, y);
  if (width.value <= x + clientWidth || x <= 95) {
    isResize.value = false;
    floatButtonLeft.value =
      x <= 95 ? clientWidth + 95 : width.value - clientWidth;
  }
  if (height.value <= y + clientHeight || y <= 0) {
    isResize.value = false;
    floatButtonTop.value = y <= 0 ? clientHeight : height.value - clientHeight;
  }
  setTimeout(() => {
    isResize.value = true;
    isDragActionBtn.value = false;
  }, 100);
};

const calPercentLeftTop = (x, y) => {
  floatButtonPercentLeft.value = x / width.value;
  floatButtonPercentTop.value = y / height.value;
};
const handleFrameResize = (event) => {
  switch (event.type) {
    case CHATBOT_TYPE.AI_CHATBOT:
      frameAISize.value.frameWidth = event.frameWidth;
      frameAISize.value.frameHeight = event.frameHeight;
      break;
    case CHATBOT_TYPE.USER_POCKET:
      frameUserSize.value.frameWidth = event.frameWidth;
      frameUserSize.value.frameHeight = event.frameHeight;
      break;
    case CHATBOT_TYPE.COMPARE:
      frameCompareSize.value.frameWidth = event.frameWidth;
      frameCompareSize.value.frameHeight = event.frameHeight;
      break;
  }
};

//Resize user pocket
watch(
  () => countUserPocketItems.value,
  (newCountItem, prevCountItem) => {
    if (newCountItem !== prevCountItem) {
      const height = (
        132 +
        countUserPocketGroup.value * 47 +
        newCountItem * 58
      ).toString();
      frameUserSize.value.frameHeight = height;
    }
  }
);

const addTab = inject("addTab");
</script>

<style lang="scss" scoped>
.chat-bot-swagger {
  .chat-bot-list {
    position: absolute;
    z-index: 10;
    bottom: 0px;
    right: -25px;
    background-color: transparent;
    opacity: 0;
    height: 0;
    overflow: hidden;
    gap: 8px;
    transition:
      height 0.2s linear,
      opacity 0.2s linear;

    &.open {
      overflow: visible;
      opacity: 1;
      // height: 180px;
      // height: 120px;
      height: 60px;
    }
  }
}

.chat-bot-btn {
  display: flex;
  align-items: center;
  background-color: #fff;
  width: 180px;
  height: 48px;
  border-radius: 8px;
  border-left: 2px #2c6393 solid;
  border-bottom: 2px #2c6393 solid;
  padding: 8px 12px;
  gap: 12px;
  cursor: pointer;
  box-shadow: 0px 2px 40px 0px #2c639352;
}

.action-btn {
  position: absolute;
  z-index: 10;
  right: -25px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(124.94deg, #1e4984 0%, #6bdada 100%);
  box-shadow: 0px 0px 24px 0px #01b4db66;
  width: 56px;
  height: 56px;
  border-radius: 999px;
  margin-left: auto;
  cursor: pointer;

  &.close {
    background: #fff !important;
  }
}

.chat-frame-container {
  display: flex;
  gap: 24px;
  z-index: 9;
}
</style>
