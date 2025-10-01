<script setup lang="ts">
import CfButton from "@/components/controls/CfButton.vue";
import Prism from "vue-prism-component";
import cfModalExample from "@/components/controls/examples/CfModalExample";
import { useGlobal } from "@/store";
import BadgePage from "./BadgePage.vue";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfModalExample);

const globalStore = useGlobal();

const showModal = () => {
  globalStore.openModal({
    title: "",
    component: BadgePage,
    data: {},
    width: "800",
  });
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Modal" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <cf-button label="Show Dialog" @click="showModal"></cf-button>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
