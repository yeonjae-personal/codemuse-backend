<script setup lang="ts">
import Prism from "vue-prism-component";
import CfAccordion from "@/components/controls/CfAccordion.vue";
import CfButton from "@/components/controls/CfButton.vue";
import CfCard from "@/components/controls/CfCard.vue";
import CfAvatar from "@/components/controls/CfAvatar.vue";
import cfAccordionExample from "@/components/controls/examples/CfAccordionExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfAccordionExample);

const panelList = ref([
  { title: "CF Button", type: "cf-button", data: {} },
  { title: "CF Card", type: "cf-card", data: {} },
  {
    title: "CF Avatar",
    type: "cf-avatar",
    data: {},
  },
]);
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Accordion" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

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
              <cf-avatar
                image="https://cdn.vuetifyjs.com/images/john.jpg"
                size="50"
              ></cf-avatar>
            </template>
          </v-layout>
        </template>
      </cf-accordion>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
