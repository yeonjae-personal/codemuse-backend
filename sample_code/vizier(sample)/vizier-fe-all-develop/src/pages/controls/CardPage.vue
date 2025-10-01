<script setup lang="ts">
import Prism from "vue-prism-component";
import CfCard from "@/components/controls/CfCard.vue";
import cfCardExample from "@/components/controls/examples/CfCardExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfCardExample);

const handleCardButtonClick = (_button: any) => {
  //
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Card" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex flex-wrap gap-4">
      <cf-card
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        hover
      ></cf-card>
      <cf-card
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="outlined"
      ></cf-card>
      <cf-card
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        variant="tonal"
      ></cf-card>
      <cf-card
        title="Card title"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-card>
      <cf-card
        title="Card title"
        subtitle="Subtitle"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      ></cf-card>
      <cf-card
        title="Card title"
        subtitle="Subtitle"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        :buttons="[{ text: 'Button A' }]"
        @click="handleCardButtonClick"
      ></cf-card>
      <cf-card
        title="Card title"
        subtitle="Subtitle"
        text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
        loading
        :buttons="[{ text: 'Button A' }, { text: 'Button B' }]"
        @click="handleCardButtonClick"
      ></cf-card>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
