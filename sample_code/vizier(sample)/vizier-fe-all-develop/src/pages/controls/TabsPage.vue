<script setup lang="ts">
import CfTabs from "@/components/controls/CfTabs.vue";
import cfTabsExample from "@/components/controls/examples/CfTabsExample";
import Prism from "vue-prism-component";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfTabsExample);

const exTabs = ref([
  { value: "one", label: "One", slot: "one" },
  { value: "two", label: "Two", slot: "two" },
  { value: "three", label: "Three", slot: "three" },
]);

const tabChangeHandle = (event: any) => {
  console.log(event);
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Tabs" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex flex-col gap-4">
      <cf-tabs
        :tabs="exTabs"
        bg-color="primary"
        align-tabs="title"
        selected="one"
        @tab-change="tabChangeHandle"
      >
        <template v-for="t in exTabs" :key="t.value" #[t.slot]>
          <div v-if="t.slot === 'one'">One</div>
          <div v-if="t.slot === 'two'">Two</div>
          <div v-if="t.slot === 'three'">Three</div>
        </template>
      </cf-tabs>
      <cf-tabs :tabs="exTabs" bg-color="red" align-tabs="start" selected="two">
        <template v-for="t in exTabs" :key="t.value" #[t.slot]>
          <div v-if="t.slot === 'one'">One</div>
          <div v-if="t.slot === 'two'">Two</div>
          <div v-if="t.slot === 'three'">Three</div>
        </template>
      </cf-tabs>
      <cf-tabs
        :tabs="exTabs"
        bg-color="primary"
        align-tabs="center"
        selected="three"
      >
        <template v-for="t in exTabs" :key="t.value" #[t.slot]>
          <div v-if="t.slot === 'one'">One</div>
          <div v-if="t.slot === 'two'">Two</div>
          <div v-if="t.slot === 'three'">Three</div>
        </template>
      </cf-tabs>
      <cf-tabs :tabs="exTabs" align-tabs="end" selected="one">
        <template v-for="t in exTabs" :key="t.value" #[t.slot]>
          <div v-if="t.slot === 'one'">One</div>
          <div v-if="t.slot === 'two'">Two</div>
          <div v-if="t.slot === 'three'">Three</div>
        </template>
      </cf-tabs>
      <cf-tabs
        :tabs="exTabs"
        align-tabs="start"
        selected="one"
        :tabs-class="['category-tabs']"
      >
        <template v-for="t in exTabs" :key="t.value" #[t.slot]>
          <div v-if="t.slot === 'one'">One</div>
          <div v-if="t.slot === 'two'">Two</div>
          <div v-if="t.slot === 'three'">Three</div>
        </template>
      </cf-tabs>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
