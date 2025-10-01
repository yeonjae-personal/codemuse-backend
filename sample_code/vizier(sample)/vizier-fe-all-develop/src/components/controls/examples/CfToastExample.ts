const cfToastExample = `<script setup lang="ts">
import { useGlobal } from '@/store';
import CfToast from '@/components/controls/CfToast.vue';
import CfButton from '@/components/controls/CfButton.vue';

const globalStore = useGlobal();

const showAlert = (position: string) => {
  globalStore.setToastInfor(
    {
      title: 'CF Title',
      text: 'Channel Framework',
      border: 'start',
      borderColor: 'info',
      type: 'info',
      icon: '$vuetify',
      class: position,
    },
    3000
  );
};
</script>
<template>
  <div class="flex flex-col gap-4">
    <div class="flex w-1/5 gap-2">
      <cf-button label="Show Toast(Top Left)" rounded="xl" @click="showAlert('top-left')" />
      <cf-button label="Show Toast(Top Center)" rounded="xl" @click="showAlert('top-center')" />
      <cf-button label="Show Toast(Top Right)" rounded="xl" @click="showAlert('top-right')" />
    </div>
    <div class="flex w-1/5 gap-2">
      <cf-button label="Show Toast(Bottom Left)" rounded="xl" @click="showAlert('bottom-left')" />
      <cf-button
        label="Show Toast(Bottom Center)"
        rounded="xl"
        @click="showAlert('bottom-center')"
      />
      <cf-button
        label="Show Toast(Bottom Right)"
        rounded="xl"
        @click="showAlert('bottom-right')"
      />
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-toast
        class="relative"
        icon="$vuetify"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-toast>
      <cf-toast
        class="relative"
        icon="$vuetify"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="outlined"
      ></cf-toast>
      <cf-toast
        class="relative"
        icon="$vuetify"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="tonal"
      ></cf-toast>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-toast
        class="relative"
        type="success"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-toast>
      <cf-toast
        class="relative"
        type="info"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-toast>
      <cf-toast
        class="relative"
        type="warning"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-toast>
      <cf-toast
        class="relative"
        type="error"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-toast>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-toast
        class="relative"
        type="success"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="tonal"
      ></cf-toast>
      <cf-toast
        class="relative"
        type="info"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="tonal"
      ></cf-toast>
      <cf-toast
        class="relative"
        type="warning"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="tonal"
      ></cf-toast>
      <cf-toast
        class="relative"
        type="error"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="tonal"
      ></cf-toast>
    </div>
    <div class="flex flex-wrap gap-4">
      <cf-toast
        class="relative"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        border="start"
        border-color="deep-purple accent-4"
      ></cf-toast>
      <cf-toast
        class="relative"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        border="top"
        border-color="success"
      ></cf-toast>
      <cf-toast
        class="relative"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        border="bottom"
        border-color="warning"
      ></cf-toast>
      <cf-toast
        class="relative"
        title="Toast title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        border="end"
        border-color="error"
      ></cf-toast>
    </div>
  </div>
</template>
`;
export default cfToastExample;
