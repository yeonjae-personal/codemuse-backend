<template>
  <UseDraggable
    v-if="props.initCoordinates"
    :id="props.chatId"
    class="position-fixed"
    :initial-value="props.initCoordinates"
    :handle="handle"
    :style="{ 'z-index': zIndex }"
    stop-propagation
    @start="handleFrameStart"
    @move="handleFrameMoved"
    @end="handleFrameEnd"
    @click="handleFrameStart"
  >
    <div
      ref="dragItem"
      class="chat-frame"
      :class="type === CHATBOT_TYPE.USER_POCKET ? 'userpocket' : ''"
      :style="{ width: frameWidth, height: frameHeight }"
    >
      <div ref="handle" class="chat-header">
        <div class="chat-header-icon">
          <template v-if="type === CHATBOT_TYPE.AI_CHATBOT">
            <AlChatBotLineIcon />
          </template>
          <template v-else-if="type === CHATBOT_TYPE.USER_POCKET">
            <UserPocketLineIcon />
          </template>
          <template v-else>
            <CompareLineIcon />
          </template>
        </div>
        <div class="chat-header-title text-[#fff] flex-grow-1">
          <template v-if="type === CHATBOT_TYPE.USER_POCKET">
            {{ getUserPocketTitle() }}
          </template>
          <template v-else>
            {{ type }}
          </template>
        </div>
        <div class="chat-header-icon-group">
          <template v-if="type === CHATBOT_TYPE.AI_CHATBOT">
            <div
              class="flex justify-center align-center w-[32px] h-[32px]"
              @click="handleNotify"
            >
              <BellIcon />
            </div>
            <div
              class="flex justify-center align-center w-[32px] h-[32px]"
              @click="handleDelete"
            >
              <TrashIcon />
            </div>
            <div
              class="flex justify-center align-center w-[32px] h-[32px]"
              @click="handleReset"
            >
              <ResetIcon />
            </div>
          </template>
          <div
            class="flex justify-center align-center w-[32px] h-[32px]"
            @click="handleClose"
          >
            <v-icon color="#fff" icon="mdi-window-close"></v-icon>
          </div>
        </div>
      </div>
      <div class="chat-content">
        <div ref="frameChat" class="content">
          <LocomotiveComponent
            v-if="type === CHATBOT_TYPE.AI_CHATBOT"
            scroll-content-class="flex flex-column gap-[8px]"
            scroll-container-class="py-[8px] px-[16px]"
            is-show-scrollbar
            always-end
          >
            <template v-for="(item, index) in chatContent" :key="index">
              <template v-if="item.owner === OWNER_MESSAGE.BOT">
                <div class="bot">
                  <div class="bot-logo">
                    <AIChatBotIcon width="22px" height="22px" />
                  </div>
                  <div class="flex flex-column gap-[4px] w-full">
                    <p class="h-[17px] text-[11px] font-weight-medium">
                      {{ item.sendTime }}
                    </p>
                    <div class="message">
                      <template v-if="item.htmlContent">
                        <div v-html="item.htmlContent"></div>
                      </template>
                      <template v-if="item.message">
                        <div v-html="item.message"></div>
                      </template>
                      <div class="rate rating">
                        <div
                          class="cursor-pointer"
                          @click="handleRate(RATING_MESSAGE.GOOD, index)"
                        >
                          <HappyFaceIcon />
                        </div>
                        <div
                          class="cursor-pointer"
                          @click="handleRate(RATING_MESSAGE.BAD, index)"
                        >
                          <SadFaceIcon />
                        </div>
                      </div>
                      <div class="rate rated">
                        <template v-if="item.react === RATING_MESSAGE.GOOD">
                          <div class="flex gap-[4px]">
                            <HappyFaceIcon />
                            <span class="text-[11px] font-weight-medium"
                              >Thanks!</span
                            >
                          </div>
                        </template>
                        <template v-else-if="item.react === RATING_MESSAGE.BAD">
                          <div class="flex gap-[4px]">
                            <SadFaceIcon />
                            <span class="text-[11px] font-weight-medium"
                              >Will Improve!</span
                            >
                          </div>
                        </template>
                      </div>
                    </div>
                    <template v-if="item.listItem?.length">
                      <div class="offer-contain">
                        <div
                          v-for="offer in item.listItem"
                          :key="offer.uuid"
                          class="offer-item"
                          @click.stop="handleRedirect(offer)"
                        >
                          <span>{{ offer.objName }}</span>
                          <v-icon
                            icon="mdi-chevron-right"
                            color="#6B6D70"
                            size="small"
                          ></v-icon>
                        </div>
                        <div
                          v-if="item?.listStatus === 'less'"
                          class="flex justify-center align-center px-[8px] py-[12px] gap-[8px] cursor-pointer"
                          @click="handleShowAll(index)"
                        >
                          <span class="text-[#1570EF] font-weight-medium"
                            >View All</span
                          >
                          <v-icon
                            icon="mdi-chevron-down"
                            color="#1570EF"
                            size="small"
                          ></v-icon>
                        </div>
                        <div
                          v-if="item?.listStatus === 'all'"
                          class="flex justify-center align-center px-[8px] py-[12px] gap-[8px] cursor-pointer"
                          @click="handleShowLess(index)"
                        >
                          <span class="text-[#1570EF] font-weight-medium"
                            >Show Less</span
                          >
                          <v-icon
                            icon="mdi-chevron-up"
                            color="#1570EF"
                            size="small"
                          ></v-icon>
                        </div>
                      </div>
                    </template>
                  </div>
                </div>
              </template>
              <template v-else>
                <div class="user">
                  <p class="h-[17px] text-[11px] font-weight-medium">
                    {{ item.sendTime }}
                  </p>
                  <div class="message">
                    <template v-if="item.htmlContent">
                      <div v-html="item.htmlContent"></div>
                    </template>
                    <template v-if="item.message">
                      <div v-text="item.message"></div>
                    </template>
                  </div>
                </div>
              </template>
            </template>
            <div class="h-[10px]"></div>
          </LocomotiveComponent>
          <div v-if="type === CHATBOT_TYPE.USER_POCKET">
            <UserPocket />
          </div>
        </div>
        <div ref="suggestMessage" class="suggestion-messages">
          <template v-for="(obj, index) in suggestionMessages" :key="index">
            <div
              class="flex rounded-[4px] px-[6px] py-[4px] border-[1px] cursor-pointer"
              :class="[
                { schedule: obj.feature === 'schedule' },
                { translate: obj.feature === 'translate' },
              ]"
              @click="
                () => {
                  obj.feature === 'schedule' || obj.feature === 'translate'
                    ? handleShowFeature(obj.link)
                    : handleSendChat(obj.message);
                }
              "
            >
              <span
                class="text-[11px] text-center text-[#6B6D70] font-weight-regular"
                >{{ obj.message }}
              </span>
            </div>
          </template>
        </div>
      </div>
      <template v-if="type === CHATBOT_TYPE.AI_CHATBOT">
        <div class="chat-footer">
          <div
            class="flex w-full align-center h-[48px] rounded-[8px] gap-[8px] px-[12px] border-[1px]"
          >
            <v-icon
              class="cursor-pointer"
              icon="mdi-menu"
              color="#3A3B3D"
              size="x-small"
            ></v-icon>
            <div class="chat-input">
              <input
                v-model="chatText"
                type="text"
                placeholder="Type your message"
                @keyup.enter="handleSendChat(chatText)"
              />
            </div>
            <v-icon
              class="cursor-pointer"
              icon="mdi-pound"
              color="#BDC1C7"
              size="x-small"
            ></v-icon>
            <div
              class="cursor-pointer bg-[#525457] h-[30px] w-[30px] rounded-[999px] p-[8px]"
              @click="handleSendChat(chatText)"
            >
              <SendIcon />
            </div>
          </div>
        </div>
      </template>
    </div>
  </UseDraggable>
</template>
<script setup>
import {
  CHATBOT_TYPE,
  OWNER_MESSAGE,
  RATING_MESSAGE,
} from "@/constants/chatbot";
import { UseDraggable } from "@vueuse/components";
import { useResizeObserver } from "@vueuse/core";
import { getUserInfor } from "@/constants/userInfor";
import UserPocket from "./UserPocket.vue";

const chatText = ref(null);
const frameChat = ref();
const suggestMessage = ref(null);
const handle = ref(null);
const dragItem = ref(null);

useResizeObserver(dragItem, (entries) => {
  const entry = entries[0];
  const { height } = entry.contentRect;
  const heightResize = height - suggestMessage.value.clientHeight - 176;
  frameChat.value.style.height = heightResize + "px";
  const frame = document.getElementById(props.chatId);
  emit("onResize", {
    type: props.type,
    frameWidth: frame.clientWidth,
    frameHeight: frame.clientHeight,
  });
});

const props = defineProps({
  type: {
    validator(value) {
      return [
        CHATBOT_TYPE.AI_CHATBOT,
        CHATBOT_TYPE.USER_POCKET,
        CHATBOT_TYPE.COMPARE,
      ].includes(value);
    },
    type: String,
    default: "",
  },
  chatId: {
    type: String,
    require: true,
    default: "",
  },
  chatContent: {
    type: Array,
    default: () => [],
  },
  suggestionMessages: {
    type: Array,
    default: () => [],
  },
  initCoordinates: {
    type: Object,
    require: true,
    default: () => {},
  },
  zIndex: {
    type: Number,
    default: 0,
  },
  size: {
    type: Object,
    default: () => ({ frameWidth: "480px", frameHeight: "640px" }),
  },
});

const emit = defineEmits([
  "onClose",
  "onNotify",
  "onDelete",
  "onReset",
  "onRating",
  "onChat",
  "onMoved",
  "onStart",
  "onEnd",
  "onViewAll",
  "onShowLess",
  "onClickBotItem",
  "onResize",
]);

const frameWidth = computed(() => props.size.frameWidth + "px");
const frameHeight = computed(() => props.size.frameHeight + "px");

const handleNotify = () => {
  emit("onNotify");
};
const handleDelete = () => {
  emit("onDelete");
};
const handleReset = () => {
  emit("onReset");
};
const handleClose = () => {
  emit("onClose", props.type);
};

const handleShowFeature = (link) => {
  window.open(link, "MsgWindow", "width=1100,height=796");
};

const handleSendChat = (text) => {
  chatText.value = null;
  if (text) {
    emit("onChat", text);
  }
  nextTick(() => {
    scrollToElement();
  });
};

const handleRate = (value, index) => {
  emit("onRating", { react: value, index: index });
};

const handleFrameStart = () => {
  emit("onStart", props.type);
};

const handleFrameMoved = (event) => {
  emit("onMoved", { type: props.type, cor: event });
};

const handleFrameEnd = (event) => {
  emit("onEnd", { type: props.type, event: event, frame: dragItem.value });
};

const scrollToElement = () => {
  if (frameChat.value) {
    // const height = frameChat.value.scrollHeight;
    // frameChat.value.scrollTo({ top: height, behavior: "smooth" });
  }
};

const handleExposeByType = () => {
  switch (props.type) {
    case CHATBOT_TYPE.AI_CHATBOT:
      return { bot: dragItem.value };
    case CHATBOT_TYPE.USER_POCKET:
      return { user: dragItem.value };
    case CHATBOT_TYPE.COMPARE:
      return { compare: dragItem.value };
  }
};

const handleRedirect = (item) => {
  emit("onClickBotItem", item);
};

const handleShowAll = (index) => {
  emit("onViewAll", index);
};

const handleShowLess = (index) => {
  emit("onShowLess", index);
};

const getUserPocketTitle = () => {
  const currentUser = getUserInfor();
  return `${currentUser?.chgUser?.toUpperCase()} Pocket`;
};

defineExpose({
  handleExposeByType,
});

watch(props.chatContent, () => {
  nextTick(() => {
    scrollToElement();
  });
});
</script>

<style lang="scss" scoped>
.chat-frame {
  position: relative;
  resize: both;
  // max-width: 480px;
  // max-height: 640px;
  width: 480px;
  height: 640px;
  min-width: 330px;
  // min-height: 380px;
  border-radius: 16px;
  background-color: #fff;
  border: 2px solid #86daed66;
  box-shadow:
    -12px 12px 100px 0px #585b6452,
    0px 0px 40px 0px #5fc2cc3d;
  overflow: hidden;
}
.userpocket {
  resize: none;
}
.chat-header {
  display: flex;
  align-items: center;
  height: 44px;
  padding: 16px;
  gap: 12px;
  background: linear-gradient(124.94deg, #1e4984 0%, #6bdada 100%);
  box-shadow: 0px 4px 16px 0px #0000001f;
  border-radius: 16px 16px 0px 0px;
  cursor: move;

  .chat-header-title {
    font-size: 16px;
    font-family: "Noto Sans KR";
  }
}
.chat-header-icon-group {
  display: flex;
  > div {
    cursor: pointer;
    :deep(svg) {
      width: 20px;
      height: 20px;
      path {
        fill: #fff;
      }
    }
  }
}
.chat-content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  gap: 8px;
  .content {
    display: flex;
    flex-direction: column;
    gap: 32px;
  }
  .bot {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    .bot-logo {
      height: 32px;
      width: 32px;
      padding: 4px;
      border-radius: 12px;
      background-color: #fff;
      box-shadow: 0px 4px 4px 0px #0000001f;
      margin-top: 21px;
    }
    .message {
      position: relative;
      background: #f0f2f5;
      color: #3a3b3d;
      border-radius: 6px 16px 16px 16px;
      &:hover {
        .rating {
          opacity: 1;
        }
      }
    }
    .rate {
      position: absolute;
      display: flex;
      background: #fff;
      box-shadow: 0px 2px 12px 0px #0000000f;
      padding: 6px;
      border-radius: 999px;
    }
    .rating {
      opacity: 0;
      right: 10px;
      top: -20px;
      gap: 6px;
      transition: 0.3s;
    }
    .rated {
      bottom: -20px;
      right: 10px;
    }
  }
  .user {
    display: flex;
    gap: 4px;
    flex-direction: column;
    position: relative;
    align-items: flex-end;
    .message {
      background: linear-gradient(124.94deg, #1e4984 0%, #6bdada 100%);
      color: #fff;
      border-radius: 16px 6px 16px 16px;
    }
  }
  .message {
    max-width: 65%;
    padding: 12px;
    font-size: 15px;
    font-weight: 400;
  }
  .suggestion-messages {
    padding: 8px 16px;
    display: flex;
    gap: 6px;
    flex-wrap: wrap;
    .schedule {
      background: #ecfdf3;
      border-color: #abefc6;
      > span {
        color: #079455 !important;
      }
    }
    .translate {
      background: #e8f4fc;
      border-color: #b2ddff;
      > span {
        color: #1570ef !important;
      }
    }
  }
}
.chat-footer {
  display: flex;
  width: 100%;
  padding: 16px 16px 0px;
  margin-bottom: 16px;
  gap: 12px;
  border-top: 1px #f0f2f5 solid;
  .chat-input {
    display: flex;
    flex-grow: 1;
    input {
      width: 100%;
      height: 20px;
      font-size: 13px;
      outline: none;
    }
  }
}
.offer-contain {
  font-family: Noto Sans KR;
  display: flex;
  flex-direction: column;
  background: white;
  font-size: 13px;
  border-radius: 16px;
  box-shadow: 0px 2px 16px 0px #00000014;
}
.offer-item {
  border-bottom: 1px #e6e9ed solid;
  display: flex;
  gap: 8px;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  max-width: 240px;
  cursor: pointer;
  color: #3a3b3d;
  font-weight: 400;
}
</style>
