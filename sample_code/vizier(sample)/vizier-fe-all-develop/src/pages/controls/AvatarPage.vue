<script setup lang="ts">
import Prism from "vue-prism-component";
import CfAvatar from "@/components/controls/CfAvatar.vue";
import cfAvatarExample from "@/components/controls/examples/CfAvatarExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfAvatarExample);
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Avatar" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex flex-col gap-4">
      <div class="flex gap-4">
        <cf-avatar color="surface-variant"></cf-avatar>
        <cf-avatar color="primary" text="CF"></cf-avatar>
        <cf-avatar icon="$vuetify"></cf-avatar>
        <cf-avatar
          image="https://cdn.vuetifyjs.com/images/john.jpg"
        ></cf-avatar>
        <cf-avatar
          image="https://cdn.vuetifyjs.com/images/john.jpg"
          size="50"
        ></cf-avatar>
      </div>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
