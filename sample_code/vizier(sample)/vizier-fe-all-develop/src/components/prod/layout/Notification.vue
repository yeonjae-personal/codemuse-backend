<template>
  <v-menu
    v-model="menu"
    :close-on-content-click="false"
    width="358px"
    location="bottom right"
    :z-index="9999"
  >
    <template #activator="{ props }">
      <div
        v-bind="props"
        class="p-[9px] mr-4 flex justify-center items-center border-base rounded-lg cursor-pointer relative w-[40px]"
      >
        <BellNotiIcon />
        <div v-if="checkHasBeenItemNew" class="new-mark"></div>
      </div>
    </template>

    <v-list class="mt-1 layout-noti">
      <LocomotiveComponent
        scroll-container-class="max-h-[363px] !px-0 ma-0"
        scroll-content-class=""
        :is-stop-propagation-wheel="true"
        :is-show-scrollbar="false"
      >
        <div v-if="notifications.length">
          <v-list-item
            v-for="item in notifications"
            :key="item.userNotiUuid"
            class="item-noti !h-[68px]"
            :class="[item.notiReadYn === YES_NO_VALUE.YES && 'item-readed']"
            @click="emit('clickItem', item)"
          >
            <div class="pt-[2px] pb-2 px-3">
              <div
                class="text-ellipsis text-[13px] text-[#3a3b3d] font-[400] w-full"
              >
                {{ showTitleNoti(item) }}
              </div>
              <div
                class="font-[500] text-[13px] text-[#1570ef] mt-2 flex align-center gap-2 cursor-pointer"
              >
                <template v-if="item.notiType === NOTI_MODULES.PUBLISH_APPROVE">
                  {{ $t("product_platform.go_to_approval") }}
                  <ArrowRightIconBlue />
                </template>
              </div>
            </div>
          </v-list-item>
        </div>

        <div v-else>
          <NoData />
        </div>
      </LocomotiveComponent>
    </v-list>
  </v-menu>
</template>
<script setup lang="ts">
import { NOTI_MODULES, YES_NO_VALUE } from "@/constants/index";
import { useNotificationStore } from "@/store";
import { useI18n } from "vue-i18n";
const props = defineProps({
  isShow: {
    type: Boolean,
    default: false,
  },
});

const { t } = useI18n();

const { notifications } = storeToRefs(useNotificationStore());
const emit = defineEmits(["update:isShow", "clickItem"]);
const menu = computed({
  get: () => props.isShow,
  set: () => {
    emit("update:isShow", !props.isShow);
  },
});

const checkHasBeenItemNew = computed(() => {
  return notifications.value?.some(
    (noti) => noti.notiReadYn === YES_NO_VALUE.NO
  );
});

const showTitleNoti = (item) => {
  switch (item.notiType) {
    case NOTI_MODULES.PUBLISH_APPROVE:
      return `[${item?.imageUrl}] ${t(
        "product_platform.notification_approval_is_waiting"
      )}`;

    default:
      return item.linkUrl;
  }
};
</script>
<style lang="scss" scoped>
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

.item-readed {
  background-color: white !important;
}
</style>
