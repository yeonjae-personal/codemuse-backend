const cfModalExample = `<script setup lang="ts">
import { useGlobal } from '@/store';
import CfButton from '@/components/controls/CfButton.vue';
import BadgePage from './BadgePage.vue';

const globalStore = useGlobal();

const showModal = () => {
  globalStore.openModal({
    title: '',
    component: BadgePage,
    data: {},
    width: '800',
  });
};
</script>
<template>
  <cf-button label="Show Dialog" @click="showModal"></cf-button>
</template>
`;
export default cfModalExample;
