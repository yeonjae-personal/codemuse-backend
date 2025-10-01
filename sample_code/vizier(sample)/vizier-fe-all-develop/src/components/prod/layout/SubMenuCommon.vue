<template>
  <v-list v-model:opened="open">
    <template v-for="(item, lv1Index) in menuData" :key="'lv-1-' + lv1Index">
      <v-list-item
        v-if="!item.children || item.children.length === 0"
        :key="item.menuId"
        :class="[lv1Index !== 0 ? '!mt-2' : '']"
        :title="item.menuNm"
        :value="item.menuId"
        class="custom-submenu-hover custom-submenu-hover-root"
        @click="() => handleClick(item)"
      />

      <v-list-group
        v-else
        :key="item"
        :class="lv1Index !== 0 ? '!mt-2' : ''"
        :value="item.menuId"
      >
        <template #activator="{ isOpen: isOpen, props: activatorProps }">
          <v-list-item
            :title="item.menuNm"
            class="custom-submenu-hover custom-submenu-hover-root"
            v-bind="activatorProps"
          >
            <template #append>
              <ChevronDown
                size="18"
                class="transition duration-150 ease-out"
                :class="{ 'rotate-180': isOpen }"
              />
            </template>
          </v-list-item>
        </template>

        <template
          v-for="(subItem, lv2Index) in item.children"
          :key="'lv-2-' + lv2Index"
        >
          <v-list-item
            v-if="!subItem.children || subItem.children.length === 0"
            :key="subItem.menuNm"
            :title="subItem.menuNm"
            :value="subItem.menuId"
            class="custom-submenu-hover !m-0"
            @click="() => handleClick(subItem, item)"
          />
          <v-list-group
            v-else
            :key="subItem.menuId"
            :class="lv2Index === 0 ? '' : '!mt-2'"
            :value="subItem.menuId"
          >
            <template #activator="{ props: activatorProps }">
              <v-list-item
                v-bind="activatorProps"
                :title="subItem.menuNm"
                class="custom-submenu-hover custom-submenu-hover-root"
              ></v-list-item>
            </template>

            <v-list-item
              v-for="(entry, lv3Index) in subItem.children"
              :key="entry.menuId"
              :class="[lv3Index === 0 ? '' : '!mt-2']"
              :prepend-icon="entry.icon"
              :title="entry.menuNm"
              :value="entry.menuId"
              class="custom-submenu-hover !m-0"
              @click="() => handleClick(entry, subItem)"
            />
          </v-list-group>
        </template>
      </v-list-group>
    </template>
  </v-list>
</template>

<script lang="ts" setup>
import clone from "lodash-es/clone";
import { useRouter } from "vue-router";
import { useMenuStore } from "@/store";
import { configPath } from "@/utils/config-path";

type AddTabFunction = (item: Object) => void;
const addTab = inject<AddTabFunction>("addTab");
try {
  if (!addTab) {
    throw new Error("Ensure it is provided in parent component");
  }
} catch (error) {
  const errorMessage = (error as Error).message;
  console.error(errorMessage);
}

const props = defineProps({
  menuData: {
    type: Array<any>,
    default: () => [],
  },
  idParent: {
    type: String,
    default: null,
  },
  nameParent: {
    type: String,
    default: null,
  },
});

const menuList = inject<any>("menuList");
const emit = defineEmits(["send-data", "hide-submenu", "expanded"]);

const router = useRouter();

const menuStore = useMenuStore();

const open = ref([] as any[]);

function handleClick(item: any, parent?: any) {
  const instance = clone(item);
  instance.path = configPath(instance);
  instance.menuNm = props.nameParent;
  instance.rawName = props.nameParent;
  instance.tabName = "";
  if (parent) {
    instance.tabName +=
      item.menuLv === "2" ? item.menuNm : `${parent.menuNm} ${item.menuNm}`;
    instance.menuNm += ` ${parent.menuNm} ${item.menuNm}`;
    instance.rawName += `${parent.menuNm}${item.menuNm}`.replace(/\s+/g, "");
    menuStore.setOpenId([parent?.menuId]);
  } else {
    instance.menuNm += ` ${item.menuNm}`;
    instance.rawName += item.menuNm.replace(/\s+/g, "");
    instance.tabName += item.menuNm;
  }
  if (addTab) {
    addTab(instance);
  }
  if (menuList?.length < 5) {
    router.push(configPath(item));
    menuStore.setActiveMenu(item);
    menuStore.setActiveMenuTree(props.menuData);
    menuStore.setParentId(props.idParent);
    emit("hide-submenu", props.idParent);
    menuStore.updateSelectedMenuDetail(item);
  }
}
</script>
<style scoped lang="scss">
.v-list-item.custom-submenu-hover {
  padding: 8px !important;
  color: #3a3b3d;
  padding-inline-start: 12px !important;
}

.v-list-item.custom-submenu-hover-root {
  padding: 16px 12px !important;
}

.custom-submenu-hover:hover {
  background: #fff0f2 !important;
  border-radius: 9px !important;
}

.custom-submenu-hover:deep() .v-list-item__overlay {
  background: none !important;
}

.custom-submenu-hover:deep() .v-list-item__content .v-list-item-title {
  font-size: 13px !important;
  line-height: 19.5px !important;
  font-weight: 500 !important;
  letter-spacing: 0.1px !important;
  color: #6b6d70 !important;
}

.custom-submenu-hover-root:deep() .v-list-item__content .v-list-item-title {
  color: #3a3b3d !important;
}

.custom-submenu-hover:hover:deep() .v-list-item__content .v-list-item-title {
  color: #ba1642 !important;
}

.custom-submenu-active {
  background: #fff0f2 !important;
  border-radius: 8px !important;
}

.custom-submenu-active:deep() .v-list-item__content .v-list-item-title {
  color: #ba1642 !important;
}

.custom-submenu-active:hover {
  background: #fff0f2 !important;
}

.custom-submenu-active:hover:deep() .v-list-item__overlay {
  opacity: 0 !important;
}

:deep(.custom-submenu-hover:hover) {
  &:hover {
    .v-list-item__append {
      svg {
        fill: #ba1642 !important;
      }
    }
  }
}

:deep(
    .custom-submenu-hover.v-list-item--density-default.v-list-item--one-line
  ) {
  min-height: 40px !important;
}

:deep(.v-list-group__items) {
  margin-left: 20px !important;
  border-left: 1px solid #e6e9ed;
  padding-left: 8px !important;
}
</style>
