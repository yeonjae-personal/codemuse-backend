const cfListGroupExample = `<script setup lang="ts">
import CfCard from '@/components/controls/CfCard.vue';
import CfListGroup from '@/components/controls/CfListGroup.vue';

const items = ref([
  {
    title: 'Item #1',
    subtitle: 'Lorem ipsum dolor sit amet consectetur adipisicing elit',
    prependAvatar: 'https://randomuser.me/api/portraits/women/8.jpg',
  },
  {
    title: 'Item #2',
    subtitle: 'Lorem ipsum dolor sit amet consectetur adipisicing elit',
    prependAvatar: 'https://randomuser.me/api/portraits/women/8.jpg',
  },
  {
    title: 'Item #3',
    subtitle: 'Lorem ipsum dolor sit amet consectetur adipisicing elit',
    prependAvatar: 'https://randomuser.me/api/portraits/women/8.jpg',
  },
]);
</script>
<template>
  <div class="flex gap-4 mb-2">
    <cf-card hover subtitle="Style lines: one">
      <cf-list-group :items="items" title="title" subtitle="subtitle" lines="one"></cf-list-group>
    </cf-card>
    <cf-card hover subtitle="Style lines: two">
      <cf-list-group :items="items" title="title" subtitle="subtitle" lines="two"></cf-list-group>
    </cf-card>
    <cf-card hover subtitle="Style lines: three">
      <cf-list-group
        :items="items"
        title="title"
        subtitle="subtitle"
        lines="three"
      ></cf-list-group>
    </cf-card>
  </div>
  <div class="flex gap-4">
    <cf-card hover>
      <cf-list-group :items="items" title="title" subtitle="subtitle" lines="two">
        <template #prepend="{ data }">
          <cf-avatar :image="data.item.prependAvatar"></cf-avatar>
        </template>
      </cf-list-group>
    </cf-card>
  </div>
</template>
`;
export default cfListGroupExample;
