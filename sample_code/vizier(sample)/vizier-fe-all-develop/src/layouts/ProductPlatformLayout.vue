<template>
  <v-app>
    <div class="content-container font-base tracking-[0.25px]">
      <v-app-bar
        class="custom-header"
        color="bg-grey-lighten-3"
        elevation="0"
        flat
        height="66"
      >
        <v-app-bar-title class="mx-4" @mousemove="handleMouseIn">
          <HeaderMenu />
        </v-app-bar-title>
      </v-app-bar>
      <!-- TODO NEED MAKE COMPONENT -->
      <div>
        <SidebarMenu :mouse-event="eventMouse" />
        <v-main class="!pl-[96px]" @mousemove="handleMouseIn">
          <HeaderTab
            :model-value="menuList"
            @move-tab="onMoved"
            @remove-tab="removeTab"
            @handle-active-tab="handleActiveTab"
          />
          <MainPage :is-load-main-page="isLoadMainPage" />
        </v-main>
      </div>
    </div>
    <BaseSnackbar />
    <ChatBot />
  </v-app>
</template>

<!-- eslint-disable id-length -->
<script lang="ts" setup>
import { INITIAL_TABS } from "@/constants/index";
import { MenuItemID } from "@/enums/redirect";
import { useSnackbarStore, useMenuStore } from "@/store";
import { findMenuItem } from "@/utils/extend-utils";
import { useLocalStorage } from "@vueuse/core";
import { isEmpty } from "lodash-es";
import { useRouter } from "vue-router";

const { showSnackbar } = useSnackbarStore();
const menuStore = useMenuStore();
const { menuTree } = storeToRefs(menuStore);
const router = useRouter();
const menuList = useLocalStorage("tabMenu", INITIAL_TABS);
const eventMouse = ref();
const isLoadMainPage = ref(false);

const handleActiveTab = (id: number | string) => {
  const activeItem = menuList.value.find(
    (item) => item.id.toString() === id.toString()
  );
  if (activeItem) {
    menuList.value.forEach(
      (item) => (item.active = item.id.toString() === id.toString())
    );
    convertNewTabsList();
    if (activeItem.path !== router.currentRoute.value.path) {
      router.push(activeItem.path);
    }
  }
};

const onMoved = (i, newX, newY) => {
  const currentItem: any = menuList.value.find((item: any) => item.i === i);
  const swappedItem: any = menuList.value.find(
    (item: any) => item.x === newX && item.y === newY
  );
  if (newX === 0) {
    currentItem.x = 1;
    currentItem.y = 0;
  } else {
    if (currentItem && swappedItem) {
      const tempX = swappedItem.x;
      swappedItem.x = newX === 0 ? swappedItem.x : currentItem.x;
      swappedItem.y = 0;
      currentItem.x = newX === 0 ? currentItem.x : tempX;
      currentItem.y = 0;
    }
  }
  menuList.value = menuList.value
    .map((tab: any) => ({
      ...tab,
      x: Math.min(tab.x, menuList.value.length - 1),
      y: 0,
    }))
    .sort((a, b) => a.x - b.x);
};

const convertNewTabsList = () => {
  menuList.value = menuList.value.map((item: any, index) => ({
    ...item,
    x: item.x ?? index,
    y: 0,
    w: 1,
    h: 1,
    i: item.id,
    static: index === 0,
    loading: false,
  }));
};

const removeTab = (id: number | string) => {
  let removeIndex = menuList.value.findIndex(
    (item) => item.id.toString() === id.toString()
  );
  if (removeIndex === -1) return;
  menuStore.removeMenuTab(menuList.value[removeIndex as number]);
  const isActiveTab = menuList.value[removeIndex as number].active;
  menuList.value = menuList.value.filter(
    (item) => item.id.toString() !== id.toString()
  );
  if (isActiveTab) {
    const newActiveIndex = removeIndex - 1;
    menuList.value[newActiveIndex as number].active = true;
    router.push(menuList.value[newActiveIndex as number].path);
  }
  while (removeIndex < menuList.value.length) {
    menuList.value[removeIndex as number].x = removeIndex;
    removeIndex++;
  }
};

type AddTabFunction = (item: any) => void;
const addTab: AddTabFunction = (item: any) => {
  const tabAdded = {
    id: item.menuId,
    name: item.menuNm,
    path: item.path,
    rawName: item.rawName,
    tabName: item.tabName,
    active: false,
    x: item.x ?? menuList.value.length,
    y: 0,
    w: 1,
    h: 1,
    i: item.menuId,
    static: false,
    loading: false,
  };
  const isTabExisted = menuList.value.find((el) => el.id === tabAdded.id);
  if (!isTabExisted) {
    if (menuList.value.length > 5) {
      showSnackbar(
        "The screen can open up to maximum of 6.\nPlease close any unnecessary screens and proceed.",
        "warning"
      );
      return;
    }
    menuList.value.push(tabAdded);
  }
  handleActiveTab(tabAdded.id);
};
type ReplaceTabFunction = (
  item: any,
  isClearStore?: boolean,
  modifyTabName?: string
) => void;
const replaceTab: ReplaceTabFunction = (
  item: any,
  isClearStore = false,
  modifyTabName
) => {
  const tabAdded = {
    id: item.menuId,
    name: modifyTabName || item.menuNm,
    path: item.path,
    rawName: item.rawName,
    tabName: item.tabName,
    active: false,
    x: item.x ?? menuList.value.length,
    y: 0,
    w: 1,
    h: 1,
    i: item.id,
    static: false,
    loading: false,
  };
  let currentActiveTabIndex = menuList.value.findIndex((tab) => tab.active);
  const isTabExisted = menuList.value.find((el) => el.id === tabAdded.id);
  if (isClearStore) {
    menuStore.removeMenuTab(menuList.value[currentActiveTabIndex as number]);
  }
  if (!isTabExisted) {
    if (currentActiveTabIndex !== -1) {
      tabAdded.x = menuList.value[currentActiveTabIndex as number].x;
      menuList.value[currentActiveTabIndex as number] = tabAdded;
    } else {
      menuList.value.push(tabAdded);
    }
  } else {
    if (currentActiveTabIndex !== -1) {
      menuList.value = menuList.value.filter(
        (_tab, index) => index !== currentActiveTabIndex
      );
      while (currentActiveTabIndex < menuList.value.length) {
        menuList.value[currentActiveTabIndex as number].x =
          currentActiveTabIndex;
        currentActiveTabIndex++;
      }
    }
  }
  handleActiveTab(tabAdded.id);
};
type ReplaceNameTabFunction = (tabId: string, tabName: string) => void;
const replaceTabName: ReplaceNameTabFunction = (tabId, tabNewName: string) => {
  const currentMenu = menuList.value.find((item) => item.id === tabId);
  if (currentMenu) {
    currentMenu.name = tabNewName;
  }
};
const changeTabNotClose = (tabId: string, status) => {
  const currentMenu = menuList.value.find((item) => item.id === tabId);
  if (currentMenu) {
    currentMenu["notClose"] = status;
  }
};
const activeCurrentTab = () => {
  const currentMenu = menuList.value.find((item) => item.active);
  if (isEmpty(currentMenu)) {
    handleActiveTab(MenuItemID.DashBoard);
  }
  if (currentMenu?.path !== router.currentRoute.value.path) {
    router.push(currentMenu?.path || "");
  }
};
const handleMouseIn = (event) => {
  eventMouse.value = event;
};

onBeforeMount(() => {
  let isInProcessDuplicate = false;
  menuList.value.forEach((tab) => {
    if (tab.id === MenuItemID.OfferDuplicate) {
      removeTab(tab.id);
      isInProcessDuplicate = true;
    } else if (tab.id === MenuItemID.RelationDuplicate) {
      removeTab(tab.id);
      isInProcessDuplicate = true;
    } else if (tab.id === MenuItemID.GroupDuplicate) {
      removeTab(MenuItemID.GroupDuplicate);
      isInProcessDuplicate = true;
    }
  });
  if (isInProcessDuplicate) {
    handleActiveTab(MenuItemID.DashBoard);
    setTimeout(() => {
      isLoadMainPage.value = true;
    }, 1000);
  } else {
    activeCurrentTab();
    isLoadMainPage.value = true;
  }
});

onMounted(() => {});

onUpdated(() => {
  const currentPath = router.currentRoute.value.path;
  const currentMenu = menuList.value.find((item) => item.path === currentPath);
  if (currentMenu) {
    const currentMenuActive = menuList.value.find((item) => item.active);
    if (currentMenuActive?.path !== currentPath) {
      menuList.value.forEach(
        (item) => (item.active = item.path === currentPath)
      );
    }
  } else {
    // add menu
    const menuItem = findMenuItem(menuTree.value, currentPath);
    addTab(menuItem);
  }
});

watch(
  () => menuList.value,
  () => {
    activeCurrentTab();
  },
  { deep: true }
);

provide("menuList", menuList);
provide("addTab", addTab);
provide("replaceTab", replaceTab);
provide("removeTab", removeTab);
provide("replaceTabName", replaceTabName);
provide("changeTabNotClose", changeTabNotClose);
</script>

<style scoped>
.custom-header {
  border-bottom: 1px solid #e6e9ed;
  z-index: 989 !important;
}
</style>
