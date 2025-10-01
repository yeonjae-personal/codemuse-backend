<script setup lang="ts">
import Prism from "vue-prism-component";
import CfBadge from "@/components/controls/CfBadge.vue";
import cfBadgeExample from "@/components/controls/examples/CfBadgeExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfBadgeExample);
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Badge" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex gap-6">
      <cf-badge icon="$vuetify" icon-size="x-large"></cf-badge>
      <cf-badge dot icon="$vuetify" icon-size="x-large"></cf-badge>
      <cf-badge floating icon="$vuetify" icon-size="x-large"></cf-badge>
      <cf-badge inline icon="$vuetify" icon-size="x-large"></cf-badge>
      <cf-badge content="99" icon="$vuetify" icon-size="x-large"></cf-badge>
      <cf-badge
        content="99"
        color="error"
        icon="$vuetify"
        icon-size="x-large"
      ></cf-badge>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
