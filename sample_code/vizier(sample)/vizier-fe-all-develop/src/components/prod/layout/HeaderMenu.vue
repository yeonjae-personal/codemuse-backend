<template>
  <v-item-group selected-class="bg-bg-primary">
    <div class="flex flex-nowrap items-center">
      <div class="logo flex justify-center items-center h-full">
        <img src="../../../assets/images/logo.png" alt="logo" />
      </div>
      <div class="flex justify-center items-center ml-auto" style="width: 100%">
        <div class="notification" @click="navigateNotification">
          <span class="notification-icon">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              width="10"
              height="12"
              viewBox="0 0 10 12"
              fill="none"
            >
              <path
                d="M9.55919 8.3885C8.82422 7.76719 8.40276 6.85909 8.40276 5.89707V4.54177C8.40276 2.83116 7.13202 1.41507 5.48603 1.17786V0.652777C5.48603 0.38396 5.26821 0.166687 4.99985 0.166687C4.73158 0.166687 4.51376 0.38396 4.51376 0.652777V1.17786C2.86722 1.41507 1.59703 2.83116 1.59703 4.54177V5.89707C1.59703 6.85909 1.17557 7.76719 0.436155 8.39231C0.342815 8.47209 0.267884 8.57116 0.216527 8.68269C0.16517 8.79423 0.138608 8.91557 0.138672 9.03836C0.138672 9.50754 0.520262 9.88913 0.989353 9.88913H9.01035C9.47953 9.88913 9.86112 9.50754 9.86112 9.03836C9.86112 8.78948 9.7527 8.55425 9.55919 8.3885ZM4.99985 11.8336C5.88027 11.8336 6.61674 11.206 6.78587 10.3752H3.21383C3.38305 11.206 4.11952 11.8336 4.99985 11.8336Z"
                fill="white"
              />
            </svg>
          </span>
          <p>Version 1.3, Next update on Aug 27, 2025</p>
        </div>
        <div class="flex justify-center items-center ml-auto">
          <div
            v-click-outside="handleClickOutside"
            class="cursor-pointer border-base rounded-lg mr-4 ml-4"
          >
            <div
              class="p-2 flex justify-center items-center"
              @click="handleShowOptions"
            >
              <!-- TODO: -->
              <div v-if="isKorean">
                <img
                  src="../../../assets/images/KoreanFlag.png"
                  alt="Korean Flag"
                />
              </div>
              <div
                v-else-if="locale == 'vi'"
                class="w-[30px] h-[20px] text-text-base text-base flex items-center"
              >
                VN
              </div>
              <div
                v-else-if="locale == 'zh'"
                class="w-[30px] h-[20px] text-text-base text-base flex items-center"
              >
                CN
              </div>
              <div
                v-else-if="locale == 'ja'"
                class="w-[30px] h-[20px] text-text-base text-base flex items-center"
              >
                JP
              </div>
              <div v-else>
                <img
                  src="../../../assets/images/EnglandFlag.png"
                  alt="England Flag"
                />
              </div>
              <div class="ml-[6px]">
                <CaretDown />
              </div>
            </div>

            <div
              v-if="showOptions"
              class="language-options fixed top-[60px] bg-[#fff] rounded p-[10px]"
            >
              <v-list class="custom-list">
                <v-list-item
                  v-for="lang in languageList"
                  :key="lang.langCode"
                  class="!mb-0"
                  @click="changeLanguage(lang.langCode)"
                >
                  <v-list-item-title class="text-sm">
                    {{ lang.langName }}
                  </v-list-item-title>
                </v-list-item>
              </v-list>
            </div>
          </div>
          <Notification
            v-model:isShow="isDisplayNoti"
            @click-item="handleClickItemNoti"
          />
          <div
            class="p-[9px] flex justify-center items-center border-base rounded-lg cursor-pointer"
          >
            <SettingIcon />
          </div>

          <BaseButton
            :color="ButtonColorType.Primary"
            class="ml-4"
            width="120px"
            height="44px"
            @click="userStore.logout()"
          >
            <UserIcon class="mr-[6px]" />
            <span
              class="flex uppercase rounded-lg text-white text-[13px] leading-[13px] tracking-[0.5px] font-medium"
              >Logout</span
            >
          </BaseButton>
        </div>
      </div>
    </div>
    <!-- <div class="d-flex justify-center items-center ml-14">
      <RouterLink v-for="(item, index) in menus" :key="index" :to="item.path"
        class="mr-16 !text-[16px] !leading-[24px] button font-medium tracking-[0.5px]"
        :class="index === 0 ? 'text-text-base' : 'text-text-lightest'">
        {{ item.name }}
      </RouterLink>
    </div> -->
  </v-item-group>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import SettingIcon from "@/components/prod/icons/SettingIcon.vue";
import CaretDown from "@/components/prod/icons/CaretDown.vue";
import UserIcon from "@/components/prod/icons/UserIcon.vue";
import { useUser, useNotificationStore } from "@/store";
import { NOTI_MODULES, YES_NO_VALUE } from "@/constants/index";
import { getUserInfor } from "@/constants/userInfor";
import { LanguageProvider, languageProvider } from "@/types/common";
import { ILanguageList } from "@/interfaces/admin/label-management";
import { connectWebSocket } from "@/utils/socket";
import { ButtonColorType } from "@/enums";
import useRedirect from "@/composables/useRedirect";
// import socket from "@/utils/socket";

const { locale } = useI18n();
const userStore = useUser();
const notificationStore = useNotificationStore();
const { movePublishApprovalPage } = useRedirect();
const userInfor = getUserInfor();
const showOptions = ref(false);
const isDisplayNoti = ref(false);
const { languageList } = inject<LanguageProvider>(languageProvider, {
  languageList: ref<ILanguageList[]>([]),
});

const isKorean = computed<boolean>(() => locale.value == "ko");

const handleShowOptions = () => {
  showOptions.value = !showOptions.value;
};

const changeLanguage = (language) => {
  locale.value = language;
  handleShowOptions();
};

const handleClickOutside = () => {
  showOptions.value = false;
};

const fetchListNotification = async () => {
  if (userInfor.chgUser) {
    await notificationStore.fetchNotification({
      userId: userInfor.chgUser,
    });
  }
};

const handleRedirectApprovalPublish = (item) => {
  movePublishApprovalPage(item);
};

const handleClickItemNoti = async (item) => {
  switch (item.notiType) {
    case NOTI_MODULES.PUBLISH_APPROVE:
      handleRedirectApprovalPublish(item);
      break;
  }
  if (item.notiReadYn === YES_NO_VALUE.NO) {
    await notificationStore.readNotification({
      userNotiUuid: item.userNotiUuid,
    });
    fetchListNotification();
  }
  isDisplayNoti.value = false;
};

const navigateNotification = () => {
  const src = "/files/release-notes.html";
  window.open(src, "_blank");
};
onMounted(() => {
  fetchListNotification();

  const key = `/topic/user/${userInfor.chgUser}`;

  connectWebSocket(key, (message) => {
    if (message) {
      fetchListNotification();
      if (!isDisplayNoti.value) {
        isDisplayNoti.value = true;
      }
    }
  });
});
</script>

<style scoped>
:deep(.v-btn .v-btn__underlay) {
  background-color: unset;
}

:deep(.v-btn__content) {
  font-size: 16px;
}

.language-options v-list-item {
  cursor: pointer;
}

.v-icon__svg {
  width: 24px;
  height: 24px;
  cursor: pointer;
}

.icon-cog:hover {
  background-color: #c3c8d1;
  border-radius: 50%;
}

.custom-list .v-list-item-title {
  font-size: 13px;
}

.notification {
  cursor: pointer;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 6px 12px;
  gap: 16px;
  border-radius: 8px;
  border: 1px;
  background: linear-gradient(124.94deg, #1e4984 26.5%, #6bdada 100%);
  width: 50.5%;
  margin-left: 29%;
}

.notification p {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
  color: white;
}

.mr-4 {
  margin-right: 16px;
  @media (max-width: 1327px) {
    margin-right: 4px !important;
  }
}

.ml-4 {
  margin-left: 16px;
  @media (max-width: 1327px) {
    margin-left: 4px !important;
  }
}

.new-mark {
  width: 6px;
  height: 6px;
  position: absolute;
  top: 4px;
  right: 4px;
  background: #ea4f3a;
  border-radius: 999px;
}
:deep(.v-list-item) {
  margin: 4px !important;
}
:deep(.v-list-item__content) {
  height: 100% !important;
}

.item-noti {
  background: #e8f4fc;
  padding-inline: unset !important;
  border-radius: 8px !important;
  font-family: "Noto Sans KR", sans-serif !important;
}

.layout-noti {
  background-color: #fff;
  padding: 4px;
  border-radius: 12px !important;
  box-shadow: 2px 2px 64px 0px #00000014 !important;
}

:deep(.v-list-item--density-default.v-list-item--one-line) {
  min-height: 24px;
  padding-inline: 8px !important;
}
</style>
