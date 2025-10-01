const cfAccordionExample = `<script setup lang="ts">
import CfAccordion from '@/components/controls/CfAccordion.vue';
import CfButton from '@/components/controls/CfButton.vue';
import CfCard from '@/components/controls/CfCard.vue';
import CfAvatar from '@/components/controls/CfAvatar.vue';

const panelList = ref([
  { title: 'CF Button', type: 'cf-button', data: {} },
  { title: 'CF Card', type: 'cf-card', data: {} },
  { title: 'CF Avatar',type: 'cf-avatar',data: {} },
]);
</script>
<template>
  <div class="flex flex-col gap-4">
    <cf-accordion :panels="panelList">
      <template #default="{ panel }">
        <v-layout>
          <template v-if="panel.type === 'cf-button'">
            <cf-button label="CF Button"></cf-button>
          </template>
          <template v-else-if="panel.type === 'cf-card'">
            <div class="m-5">
              <cf-card
                hover
                title="Card title"
                subtitle="Subtitle"
                text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
              ></cf-card>
            </div>
          </template>
          <template v-else-if="panel.type === 'cf-avatar'">
            <cf-avatar image="https://cdn.vuetifyjs.com/images/john.jpg" size="50"></cf-avatar>
          </template>
        </v-layout>
      </template>
    </cf-accordion>
  </div>
</template>
`;
export default cfAccordionExample;
