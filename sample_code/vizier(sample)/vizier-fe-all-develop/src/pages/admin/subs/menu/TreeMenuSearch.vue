<template>
  <div class="container relative bg-white w-[358px] rounded-[12px]">
    <div class="flex flex-col h-full overflow-y-auto">
      <div class="pl-5 pr-4">
        <div class="flex justify-between items-center h-[47px] mt-1">
          <h1
            class="font-medium text-[15px] leading-[22.5px] tracking-[0.005em] txt-menu-tree"
          >
            {{ $t("product_platform.menuEntity.menuList") }}
          </h1>
        </div>
      </div>
      <v-treeview
        :key="treeKey"
        :items="treeData"
        :expand-icon="ExpandIcon"
        :collapse-icon="CollapseIcon"
        :opened="openMenuOnload"
        item-value="menuId"
        item-text="menuNm"
        activatable
        :open-all="isOpenAll"
        item-children="children"
        :activated="itemActive"
        @update:activated="changeItemActive"
      >
        <template #title="{ item }">
          <div class="menu-item-id">
            <span class="font-medium text-[15px] ml-2 txt-menu-tree">
              {{ item.menuNm }}
            </span>
          </div>
        </template>
      </v-treeview>
    </div>
  </div>
</template>
<script setup>
import { VTreeview } from "vuetify/labs/VTreeview";
import ExpandIcon from "@/components/prod/icons/ExpandIcon.vue";
import CollapseIcon from "@/components/prod/icons/CollapseIcon.vue";
import { useMenuStoreInfo } from "@/store";

const menuStoreInfo = useMenuStoreInfo();
const { menuItemsInfoPopup } = storeToRefs(menuStoreInfo);
const treeListTemp = ref([]);
const treeKey = ref(0);
const itemActive = ref(null);
const isOpenAll = ref(false);

const emit = defineEmits(["setItemSelected"]);
const props = defineProps({
  isSearch: {
    type: Boolean,
    default: false,
  },
});

const addParentIds = (list, grandParentId = null, parentId = null) => {
  let menuItem = list.map((item) => {
    treeListTemp.value = [
      ...treeListTemp.value,
      {
        ...item,
        actvYn: item.actvYn === "Y",
        authCtrlYn: item.authCtrlYn === "Y",
      },
    ];
    const itemMap = {
      ...item,
      actvYn: item.actvYn === "Y",
      authCtrlYn: item.authCtrlYn === "Y",
    };
    const newItem = { ...itemMap, parentId, grandParentId };

    if (item.childrens.length > 0) {
      newItem.children = addParentIds(
        item.childrens,
        item.menuLvNo == 1 ? item.menuId : grandParentId,
        item.menuId
      );
    }
    return newItem;
  });

  return menuItem;
};

const newItem = [];
const getListOpened = (list) => {
  list.forEach((item) => {
    if (item.childrens.length > 0) {
      newItem.push(item.menuId);
      getListOpened(item.childrens);
    }
  });

  return [...new Set(newItem)];
};
const changeItemActive = (item) => {
  if (item[0]) {
    itemActive.value = item[0];
    let itemActiveSelect = treeListTemp.value?.find(
      (item) => item.menuId === item[0]
    );
    if (itemActiveSelect) {
      emit("setItemSelected", itemActiveSelect);
    }
  }
};

const treeData = computed(() => {
  return addParentIds(menuItemsInfoPopup.value);
});
const openMenuOnload = computed(() => {
  if (props.isSearch) {
    isOpenAll.value = true;
    treeKey.value++;
    if (menuItemsInfoPopup.value.length > 0) {
      return getListOpened(menuItemsInfoPopup.value);
    }
  } else {
    isOpenAll.value = false;
    let arr = [];
    if (menuItemsInfoPopup.value.length > 0) {
      menuItemsInfoPopup.value.forEach((itemLv0) => {
        arr.push(itemLv0.menuId);
        if (itemLv0.childrens) {
          itemLv0.childrens.forEach((itemLv1) => {
            arr.push(itemLv1.menuId);
          });
        }
      });

      return arr;
    }
  }
});

watch(
  () => menuItemsInfoPopup,
  () => {
    itemActive.value = null;
  },
  { deep: true, immediate: true }
);

onMounted(async () => {
  await menuStoreInfo.fetchMenuTreePopup({
    levelNoFrom: "1",
    levelNoTo: "2",
    actvYn: "Y",
  });
  treeListTemp.value = [];
});
</script>
<style scoped>
.content {
  padding-bottom: 1rem;
  max-height: calc(100vh - 453px);
}

:deep().v-field {
  border-radius: 8px;
  box-shadow: none !important;
}

.v-text-field:deep() {
  font-size: 13px !important;
}

.v-field__input {
  font-size: 13px;
}

:deep(.v-label) {
  font-size: 13px !important;
}

:deep(input) {
  font-size: 13px;
  color: #3a3b3d;
}

.custom-margin-total-search-result {
  margin-bottom: 4px !important;
}

/** TreeView */
:deep(.v-treeview-item:hover) {
  color: #ba1642;
  background-color: #fff0f2;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

:deep(.v-list-item-title) {
  font-weight: 500;
}

:deep(.v-list-item-title:hover) {
  color: #ba1642;
  font-weight: bold;
}

:deep(.v-list-item--active) {
  color: #ba1642;
  font-weight: bold;
}

:deep(.txt-menu-tree) {
  font-family: "Noto Sans KR";
}
</style>
