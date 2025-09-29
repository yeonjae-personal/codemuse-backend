<script setup lang="ts">
import ShowDetailIcon from "@/components/prod/icons/ShowDetailIcon.vue";
const props = defineProps({
  title: {
    type: String,
    default: "",
  },
});
const emits = defineEmits(["closePanel"]);

const closeSidePanel = () => {
  emits("closePanel");
};
</script>
<template>
  <div
    class="relative z-10 top-0 right-0 bg-white flex flex-col w-[420px] flex-shrink-0 h-full"
  >
    <header class="p-4">
      <div class="w-full flex justify-between items-center h-8">
        <div class="text-text-base text-lg">{{ props.title }}</div>
        <slot name="buttons"></slot>
      </div>
    </header>
    <div>
      <slot name="tabs"></slot>
    </div>
    <main class="flex-grow-1 px-4 custom-height">
      <slot name="body"></slot>
    </main>
    <footer class="px-4 pt-4">
      <slot name="footer"></slot>
    </footer>
    <ShowDetailIcon
      class="absolute top-[160px] left-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="closeSidePanel"
    />
  </div>
</template>
<style scoped>
.custom-height {
  max-height: calc(100vh - 400px);
}
</style>
