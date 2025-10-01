<template>
  <div @mouseleave="handleLeave()">
    <v-navigation-drawer
      class="!border-r-[1px] !border-lighter pt-2 !w-[96px] !z-[989]"
      color="drawer"
      permanent
      theme="light"
      width="96"
    >
      <v-list density="compact" nav>
        <v-list-item
          v-for="item in menuTreeClone"
          :id="item.menuNm + item.menuId"
          :key="item.id"
          :class="{
            'active-menu': isActive(item),
          }"
          :value="item.id"
          class="custom-hover-menu my-0 py-2 relative main-item-menu"
          @click="() => handleClick(item)"
          @mouseover="handleHover(item, item.menuNm + item.menuId)"
        >
          <v-list-item-action class="d-flex flex-column align-center">
            <component
              :is="item.icon"
              :class="isActive(item) ? 'active-item' : 'text-text-base'"
            />
            <v-list-item-title
              :class="isActive(item) ? '!text-text-primary' : '!text-text-base'"
              class="text-center mt-1 !text-[13px] !leading-[19.5px] !font-medium !tracking-[0.1px]"
            >
              {{ item.menuNm }}
            </v-list-item-title>
          </v-list-item-action>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
    <v-card
      v-if="menuTrue?.length"
      ref="subMenu"
      class="!w-[220px] !left-[96px] !z-[988] sub-menu position-absolute"
      color="drawer"
      :style="{
        'max-height': height - 66 + 'px',
        height: 'auto',
      }"
    >
      <LocomotiveComponent
        scroll-container-class="!px-4"
        scroll-content-class="!py-4"
        @change-height="handleSubMenuChangeHeight"
      >
        <SubMenuCommon
          :id-parent="idParentMenu"
          :menu-data="menuTrue"
          :name-parent="nameParentMenu"
          @hide-submenu="handleHideSubmenu"
        />
      </LocomotiveComponent>
    </v-card>
  </div>
</template>
<script lang="ts" setup>
import { useMenuStore } from "@/store";
import { useRouter } from "vue-router";
import { configPath } from "@/utils/config-path";
import { MenuItem } from "@/interfaces/prod/menu";
import { useWindowSize } from "@vueuse/core";
import { MenuItemID } from "@/enums/redirect";

const router = useRouter();
const menuStore = useMenuStore();
const { height } = useWindowSize();
const { menuTree } = storeToRefs(menuStore);

const menuTrue = ref([] as MenuItem[]);
const activeItem = ref(null as any);
const idParentMenu = ref(null as any);
const nameParentMenu = ref(null as any);
const translateX = ref<string>("-300px");
const positionTopSubBar = ref<number | string>(0);
const bottomPosition = ref<string>("unset");
const subSideBarHeight = ref<number>(0);
const subMenu = ref<Element>();
const hoverItemOffset = ref<number>(0);
const subMenuOpacity = ref<string>("1");
const leaveOtherMenu = ref<boolean>(true);

const translateY = computed(() => positionTopSubBar.value + "px");
type AddTabFunction = (item: Object) => void;
const addTab = inject<AddTabFunction>("addTab");

const props = defineProps({
  mouseEvent: {
    type: MouseEvent,
    default: null,
  },
});

try {
  if (!addTab) {
    throw new Error("Ensure it is provided in parent component");
  }
} catch (error) {
  const errorMessage = (error as Error).message;
  console.error(errorMessage);
}

const menuTreeClone = computed(() =>
  removeTabFromMenu(menuTree.value, [
    MenuItemID.GroupDuplicate,
    MenuItemID.OfferDuplicate,
    MenuItemID.RelationDuplicate,
  ])
);

function isActive(item: any) {
  return item.menuId === activeItem.value;
}

function handleHover(item: any, id: any) {
  setTimeout(() => {
    menuTrue.value =
      item.children && item.children.length > 0 ? item.children : [];
  }, 300);
  if (activeItem.value != item.menuId) {
    handleLeave();
    activeItem.value = item.menuId;
    if (item.children && item.children.length > 0) {
      setTimeout(() => {
        idParentMenu.value = item.menuId;
        nameParentMenu.value = item.menuNm;
      }, 300);
      handleIn();
    } else {
      menuTrue.value = [];
      handleLeave();
    }
    setTimeout(() => {
      if (id) {
        const subBar = document.getElementById(id);
        if (subBar) {
          hoverItemOffset.value = subBar.offsetTop;
          handleChangePosition();
        }
      }
    }, 350);
  }
}

function handleLeave() {
  activeItem.value = null;
  subMenuOpacity.value = "0";
  translateX.value = "-300px";
}

function handleIn() {
  if (leaveOtherMenu.value) {
    setTimeout(() => {
      subMenuOpacity.value = "1";
      translateX.value = "0px";
    }, 300);
  }
}

// methods
function handleClick(item: any) {
  if (item.menuNm === "Dashboard") {
    if (addTab) {
      addTab({
        ...item,
        rawName: item.menuNm,
      });
    }
    item.path = "/functions/product-platform";
  }
  idParentMenu.value = item.menuId;
  nameParentMenu.value = item.menuNm;
  if (!item.children) {
    router.push(item.path);
    menuStore.updateSelectedMenuDetail(item);
  }
}

function handleHideSubmenu(idParent: any) {
  activeItem.value = idParent;
}

function checkMenu(menu: any, path: any, root: any, parent?: any) {
  if (menu.children?.length) {
    for (const item of menu.children) {
      if (!checkMenu(item, path, root, menu)) {
        return false;
      }
    }
  } else if (path === configPath(menu)) {
    menuStore.setActiveMenu(menu);
    // activeItem.value = root.menuId;
    // menuTrue.value = root.children;
    idParentMenu.value = root.menuId;
    menuStore.setOpenId(parent ? [parent.menuId] : [root.menuId]);
    menuStore.setActiveMenuTree(root.children);
    menuStore.setParentId(root.menuId);
    return false;
  }
  return true;
}

function initMenu() {
  if (menuTree.value.length > 0) {
    const path = router.currentRoute.value.fullPath;
    for (const item of menuTree.value) {
      if (configPath(item) === path) {
        activeItem.value = item.menuId;
        break;
      } else if (item.children?.length) {
        for (const child of item.children) {
          if (!checkMenu(child, path, item)) {
            break;
          }
        }
      }
    }
  }
}

const removeTabFromMenu = (listTab, tabIdList: any[]) => {
  if (!listTab?.length) return listTab;
  return listTab.map((tab) => {
    let targetIndex = tab.children?.findIndex((child) =>
      tabIdList.includes(child.menuId)
    );
    if (targetIndex !== -1 && targetIndex != undefined) {
      return {
        ...tab,
        children: tab.children.filter(
          (_childTab, index) => index !== targetIndex
        ),
      };
    } else {
      return { ...tab, children: removeTabFromMenu(tab.children, tabIdList) };
    }
  });
};

const handleSubMenuChangeHeight = (event: any) => {
  subSideBarHeight.value = event;
  handleChangePosition(event);
};

const handleChangePosition = (subBarHeight?: any) => {
  const position =
    hoverItemOffset.value +
    8 +
    (subBarHeight ? subBarHeight : subSideBarHeight.value);
  if (position < height.value - 66) {
    bottomPosition.value = "unset";
    positionTopSubBar.value = 75 + hoverItemOffset.value;
  } else {
    positionTopSubBar.value = height.value - position + hoverItemOffset.value;
    // bottomPosition.value = "0px";
  }
};
watch(
  () => props.mouseEvent,
  (newVal) => {
    if (newVal) {
      handleLeave();
    }
  },
  {
    deep: true,
  }
);
// lifecycle
onMounted(async () => {
  await menuStore.fetchTreeData();
  initMenu();
});
</script>

<style lang="scss" scoped>
/* A light blue color */
.custom-hover-menu:hover,
.custom-hover-menu:active {
  background-color: #f7f8fa !important;
}

.custom-hover-menu:hover:deep() .v-list-item__overlay {
  opacity: 0 !important;
}

.bg-drawer {
  background-color: #fff !important;
}

.active-menu {
  background-color: #fff0f2 !important;
}

.active-menu:hover {
  background-color: #fff0f2 !important;
}

.active-menu::before {
  content: "";
  position: absolute;
  width: 6px;
  height: 40px;
  background: #d9325a;
  border-radius: 12px 0 0 12px;
  right: 0;
}
.main-item-menu:hover {
  background-color: #fff0f2 !important;
  &::before {
    content: "";
    position: absolute;
    width: 6px;
    height: 40px;
    background: #d9325a;
    border-radius: 12px 0 0 12px;
    right: 0;
  }
  .v-list-item-action {
    svg {
      color: rgb(186 22 66 / 1);
    }

    .v-list-item-title {
      color: rgb(186 22 66 / 1) !important;
    }
  }
}
.v-list {
  padding: 0 !important;
}

.v-list-item--density-compact.v-list-item--one-line {
  min-height: 88px !important;
}

.sub-menu {
  border-width: 1px;
  border-left-width: 0px;
  border-style: solid;
  border-color: #ff8fa1;
  border-top-right-radius: 12px;
  border-bottom-right-radius: 12px;
  transition: transform 280ms ease-in;
  transform: translateX(v-bind(translateX));
  top: v-bind(translateY);
  bottom: v-bind(bottomPosition);
  box-shadow: 2px 4px 16px 0px #0b1c3e33;
  div {
    opacity: v-bind(subMenuOpacity);
  }
}
:deep(.v-navigation-drawer__content) {
  overflow-y: hidden !important;
}
.active-item {
  color: rgb(186 22 66 / 1);
  fill: rgb(186 22 66 / 1);
}
</style>
