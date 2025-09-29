const cfTabsExample = `<script setup lang="ts">
import CfTabs from '@/components/controls/CfTabs.vue';

const exTabs = ref([
  { value: 'one', label: 'One', slot: 'one' },
  { value: 'two', label: 'Two', slot: 'two' },
  { value: 'three', label: 'Three', slot: 'three' },
]);
</script>
<template>
  <div class="flex flex-col gap-4">
    <cf-tabs :tabs="exTabs" bg-color="primary" align-tabs="title" selected="one">
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
    <cf-tabs :tabs="exTabs" bg-color="primary" align-tabs="center" selected="three">
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
      :tabsClass="['category-tabs']"
    >
      <template v-for="t in exTabs" :key="t.value" #[t.slot]>
        <div v-if="t.slot === 'one'">One</div>
        <div v-if="t.slot === 'two'">Two</div>
        <div v-if="t.slot === 'three'">Three</div>
      </template>
    </cf-tabs>
  </div>
</template>
`;
export default cfTabsExample;
