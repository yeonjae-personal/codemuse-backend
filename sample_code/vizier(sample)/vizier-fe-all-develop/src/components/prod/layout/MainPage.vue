<template>
  <div class="pt-[16px] bg-base body">
    <div
      :class="isShowDetailLayout ? 'content-detail' : ''"
      class="content overflow-x-auto custom-scroll"
    >
      <router-view v-slot="{ Component }">
        <KeepAlive v-if="isLoadMainPage" :include="componentName" :max="6">
          <component :is="Component"></component>
        </KeepAlive>
      </router-view>
      <GlobalLoading />
      <div v-if="isShowDetailLayout" class="d-flex flex-column">
        <v-sheet
          class="flex-grow-1 d-flex flex-column text-center rounded-lg"
          elevation="2"
        >
          <v-divider></v-divider>
          <BaseForm v-if="isInMenuManagerPage" :is-add-mode="false" />
          <DetailMenuItem v-else />
        </v-sheet>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useMenuStore, useProductsStore } from "@/store";
import { useRoute } from "vue-router";

import GlobalLoading from "./GlobalLoading.vue";
import DetailMenuItem from "./DetailMenuItem.vue";
import BaseForm from "./BaseForm.vue";

defineProps({
  isLoadMainPage: {
    type: Boolean,
    default: false,
  },
});
const route = useRoute();
const menuStore = useMenuStore();
const productStore = useProductsStore();
const menuList = inject<any>("menuList");

const { isShowDetailLayout } = storeToRefs(menuStore);
const { selectedProduct } = storeToRefs(productStore);

const isInMenuManagerPage = computed(() => route.path === "/menu-manager");
const items = ref<any[]>([]);

const componentName = computed(() =>
  menuList.value.map((item: any) => item.rawName)
);
// const routeName = computed(() => {
//   return route.path;
// });

// const handleBreadCrums = (val: string) => {
//   switch (val) {
//     case "/functions/product-platform/products":
//       return [
//         {
//           title: "Offer",
//           disabled: false,
//         },
//         {
//           title: "Search",
//           disabled: false,
//           href: "/functions/product-platform/products",
//         },
//       ];
//     case "/functions/product-platform/discounts":
//       return [
//         {
//           title: "Offer",
//           disabled: false,
//         },
//         {
//           title: "Search",
//           disabled: false,
//         },
//         {
//           title: "Discount",
//           disabled: false,
//           href: "/functions/product-platform/discounts",
//         },
//       ];
//     case "/functions/product-platform/create-product":
//       return [
//         {
//           title: "Offer",
//           disabled: false,
//         },
//         {
//           title: "Create",
//           disabled: false,
//         },
//         {
//           title: "PricePlan",
//           disabled: false,
//           href: "/functions/product-platform/create-product",
//         },
//       ];
//     case "/functions/product-platform/category":
//       return [
//         {
//           title: "Category",
//           disabled: false,
//         },
//         {
//           title: "Tree-View",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/components":
//       return [
//         {
//           title: "Component",
//           disabled: false,
//         },
//         {
//           title: "Search",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/create-component":
//       return [
//         {
//           title: "Component",
//           disabled: false,
//         },
//         {
//           title: "Create",
//           disabled: false,
//           href: "/functions/product-platform/create-component",
//         },
//       ];
//     case "/functions/product-platform/impact-analysis":
//       return [
//         {
//           title: "Catalog",
//           disabled: false,
//         },
//         {
//           title: "Impact Analysis",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/resources":
//       return [
//         {
//           title: "Resource",
//           disabled: false,
//         },
//         {
//           title: "Search",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/create-resource":
//       return [
//         {
//           title: "Resource",
//           disabled: false,
//         },
//         {
//           title: "Create",
//           disabled: false,
//           href: "/functions/product-platform/create-resource",
//         },
//       ];
//     case "/functions/product-platform/relation/manager":
//       return [
//         {
//           title: "Extends",
//           disabled: false,
//         },
//         {
//           title: "Relation",
//           disabled: false,
//         },
//         {
//           title: "Manager",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/group/search":
//       return [
//         {
//           title: "Extends",
//           disabled: false,
//         },
//         {
//           title: "Group",
//           disabled: false,
//         },
//         {
//           title: "Search",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/group/create":
//       return [
//         {
//           title: "Extends",
//           disabled: false,
//         },
//         {
//           title: "Group",
//           disabled: false,
//         },
//         {
//           title: "Create",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/admin/message-management":
//       return [
//         {
//           title: "Admin",
//           disabled: false,
//         },
//         {
//           title: "System Message Management",
//           disabled: false,
//         },
//       ];

//     case "/functions/product-platform/admin/domain-management":
//       return [
//         {
//           title: "Admin",
//           disabled: false,
//         },
//         {
//           title: "Domain Management",
//           disabled: false,
//         },
//       ];

//     case "/functions/product-platform/admin/code-management":
//       return [
//         {
//           title: "Admin",
//           disabled: false,
//         },
//         {
//           title: "Common Code Management",
//           disabled: false,
//         },
//       ];

//     case "/functions/product-platform/admin/terminology-management":
//       return [
//         {
//           title: "Admin",
//           disabled: false,
//         },
//         {
//           title: "Terminology Management",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/admin/menu-management":
//       return [
//         {
//           title: "Admin",
//           disabled: false,
//         },
//         {
//           title: "Menu Management",
//           disabled: false,
//         },
//       ];
//     case "/functions/product-platform/admin/screen-management":
//       return [
//         {
//           title: "Admin",
//           disabled: false,
//         },
//         {
//           title: "Screen Management",
//           disabled: false,
//         },
//       ];
//     default:
//       return [];
//   }
// };

// watch(
//   routeName,
//   (val) => {
//     items.value = handleBreadCrums(val);
//   },
//   { immediate: true }
// );
watch(selectedProduct, (val) => {
  if (val) {
    const newItem = {
      title: val.name,
      disabled: false,
    };
    if (items.value.length > 2) {
      items.value[items.value.length - 1] = newItem;
    } else {
      items.value.push(newItem);
    }
  } else {
    items.value.pop();
  }
});
</script>
<style scoped>
.bg-base.body {
  padding-right: 16px !important;
  padding-left: 16px !important;
  padding-bottom: 16px !important;
}

.py-6 {
  padding-top: 0 !important;
}

.py-6 .v-breadcrumbs.mb-3 {
  margin-bottom: 0 !important;
}

.body {
  height: calc(100vh - 105px);
}

.content-detail {
  display: grid;
  grid-template-columns: 1fr auto;
}

.content {
  /* height: calc(100vh - 170px); */
  height: 100%;
}

.v-breadcrumbs-item:last-child {
  color: #3a3b3d;
}
</style>
