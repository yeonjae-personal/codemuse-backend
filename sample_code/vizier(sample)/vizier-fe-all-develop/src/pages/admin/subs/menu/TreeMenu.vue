<template>
  <div class="container relative bg-white py-6 pr-2 rounded-[12px] w-[420px]">
    <div class="flex flex-col h-full">
      <div class="pl-5 pr-4">
        <div class="flex justify-between items-center mb-2 h-[40px]">
          <h1
            class="font-medium text-[15px] leading-[22.5px] tracking-[0.005em]"
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
            <span class="font-medium text-[15px] ml-2">
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
const { menuItemsInfo } = storeToRefs(menuStoreInfo);
const treeListTemp = ref([]);
const treeKey = ref(0);
const itemActive = ref(null);
const isOpenAll = ref(false);

const emit = defineEmits(["setItemSelected"]);
const props = defineProps({
  isSearch: { type: Boolean, default: false },
});

const addParentIds = (
  list,
  grandParentId = null,
  parentId = null,
  parentNm = null,
  parentLvNo = null
) => {
  let menuItem = list.map((item) => {
    treeListTemp.value = [
      ...treeListTemp.value,
      {
        ...item,
        actvYn: item.actvYn === "Y",
        authCtrlYn: item.authCtrlYn === "Y",
        parentNm,
        parentLvNo,
        parentId,
      },
    ];
    const itemMap = {
      ...item,
      actvYn: item.actvYn === "Y",
      authCtrlYn: item.authCtrlYn === "Y",
      parentNm,
      parentLvNo,
    };
    const newItem = {
      ...itemMap,
      parentId,
      grandParentId,
      parentNm,
      parentLvNo,
    };

    if (item.childrens.length > 0) {
      newItem.children = addParentIds(
        item.childrens,
        item.menuLvNo == 1 ? item.menuId : grandParentId,
        item.menuId,
        item.menuNm,
        item.menuLvNo
      );
    }
    return newItem;
  });

  return menuItem;
};
const newItem = [];
const getListOpened = (list) => {
  list.forEach((item) => {
    newItem.push(item.menuId);
    if (item.childrens.length > 0) {
      getListOpened(item.childrens);
    }
  });

  return [...new Set(newItem)];
};

const treeData = computed(() => {
  return addParentIds(menuItemsInfo.value);
});
const openMenuOnload = computed(() => {
  if (props.isSearch) {
    treeKey.value++;
    isOpenAll.value = true;
    if (menuItemsInfo.value.length > 0) {
      return getListOpened(menuItemsInfo.value);
    }
  } else {
    let arr = [];
    isOpenAll.value = false;
    if (menuItemsInfo.value.length > 0) {
      menuItemsInfo.value.forEach((itemLv0) => {
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

const changeItemActive = (item) => {
  if (item[0]) {
    itemActive.value = item[0];
    let itemActiveSelect = treeListTemp.value?.find(
      (i) => i.menuId === item[0]
    );
    if (itemActiveSelect) {
      emit("setItemSelected", itemActiveSelect);
    }
  }
};

watch(
  () => menuItemsInfo,
  () => {
    treeListTemp.value = [];
    itemActive.value = null;
    emit("setItemSelected", null);
    addParentIds(menuItemsInfo.value);
  },
  { deep: true, immediate: true }
);

onMounted(async () => {
  await menuStoreInfo.fetchMenuTree();
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

:deep(.v-list-item:not(:has(.v-list-item--active))) {
  font-weight: 500;
  color: #6b6d70;
  font-family: "Noto Sans KR";
}

:deep(.v-list-item-title:hover) {
  color: #ba1642;
  font-weight: bold;
}

:deep(.v-list-item--active) {
  color: #ba1642 !important;
  font-weight: bold;
  font-family: "Noto Sans KR";
}

:deep(.menu-item-id::before) {
  /* content: '';
  left: 0px;
  top: 0;
  bottom: 0;
  width: 100%;
  position: absolute; */
}
</style>
