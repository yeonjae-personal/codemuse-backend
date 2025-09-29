<template>
  <v-sheet
    class="d-flex justify-center flex-wrap text-center"
    height="1000"
    max-width="800"
    width="100%"
    rounded
  >
    <div>
      <h2 class="text-h4 font-weight-black text-orange">
        Welcome to {{ menuId ? menuId : "Title" }} page
      </h2>

      <div class="text-h5 font-weight-medium mb-2">
        <v-switch
          v-model="isToggled"
          class="mf-8"
          label="Show Menu Detail"
          color="orange accent-4"
          @change="handleSwitchChange"
        ></v-switch>
      </div>

      <RouterLink to="/functions/product-platform">
        <v-btn color="orange" variant="text">Go to Home page</v-btn>
      </RouterLink>
    </div>
  </v-sheet>
  <!-- TODO: Add ResizeLayout component -->
  <!-- <ResizeLayout/> -->
</template>

<script setup lang="ts">
import { useRoute } from "vue-router";
import { useMenuStore } from "@/store";
// TODO: Import ResizeLayout component
// import ResizeLayout from "./ResizeLayout.vue";

const menuStore = useMenuStore();

// Access the current route
const route = useRoute();
const isToggled = ref(false);
const menuId = ref("");

function handleSwitchChange() {
  menuStore.setIsShowDetailLayout(isToggled.value);
}

// Watch for changes in route parameters
watch(
  () => route.params.menuId,
  (newMenuId) => {
    menuId.value = newMenuId ? (newMenuId as string) : "Title";
  }
);
</script>
