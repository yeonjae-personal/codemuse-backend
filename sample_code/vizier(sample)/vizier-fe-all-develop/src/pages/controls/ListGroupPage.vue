<script setup lang="ts">
import CfCard from "@/components/controls/CfCard.vue";
import CfListGroup from "@/components/controls/CfListGroup.vue";
import cfListGroupExample from "@/components/controls/examples/CfListGroupExample";
import Prism from "vue-prism-component";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfListGroupExample);

const items = ref([
  {
    title: "Item #1",
    subtitle: "Lorem ipsum dolor sit amet consectetur adipisicing elit",
    prependAvatar: "https://randomuser.me/api/portraits/women/8.jpg",
  },
  {
    title: "Item #2",
    subtitle: "Lorem ipsum dolor sit amet consectetur adipisicing elit",
    prependAvatar: "https://randomuser.me/api/portraits/women/8.jpg",
  },
  {
    title: "Item #3",
    subtitle: "Lorem ipsum dolor sit amet consectetur adipisicing elit",
    prependAvatar: "https://randomuser.me/api/portraits/women/8.jpg",
  },
]);
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="ListGroup" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex gap-4 mb-2">
      <cf-card hover subtitle="Style lines: one">
        <cf-list-group
          :items="items"
          title="title"
          subtitle="subtitle"
          lines="one"
        ></cf-list-group>
      </cf-card>
      <cf-card hover subtitle="Style lines: two">
        <cf-list-group
          :items="items"
          title="title"
          subtitle="subtitle"
          lines="two"
        ></cf-list-group>
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
        <cf-list-group
          :items="items"
          title="title"
          subtitle="subtitle"
          lines="two"
        >
          <template #prepend="{ data }">
            <cf-avatar :image="data.item.prependAvatar"></cf-avatar>
          </template>
        </cf-list-group>
      </cf-card>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
