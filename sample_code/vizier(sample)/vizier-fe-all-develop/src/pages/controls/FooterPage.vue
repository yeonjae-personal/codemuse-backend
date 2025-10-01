<script setup lang="ts">

import CfFooter from "@/components/controls/CfFooter.vue";
import cfButton from "@/components/controls/CfButton.vue";
import Prism from "vue-prism-component";
import cfFooterExample from "@/components/controls/examples/CfFooterExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfFooterExample);
const links = ref([
  { title: "Home" },
  { title: "About" },
  { title: "Team" },
  { title: "Service" },
  { title: "Blog" },
  { title: "Contact us" },
]);
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Footer" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
    <div class="flex gap-2 w-3/5">
      <cf-footer>
        <v-row justify="center" class="p-2">
          <cf-button
            v-for="link in links"
            :key="link.title"
            :label="link.title"
            class="mx-1 my-1"
          />
        </v-row>
        <v-row justify="center">
          <v-col class="text-center" cols="12">
            <p>
              Phasellus feugiat arcu sapien, et iaculis ipsum elementum sit
              amet. Mauris cursus commodo interdum. Praesent ut risus eget metus
              luctus accumsan id ultrices nunc. Sed at orci sed massa
              consectetur dignissim a sit amet dui. Duis commodo vitae velit et
              faucibus. Morbi vehicula lacinia malesuada. Nulla placerat augue
              vel ipsum ultrices, cursus iaculis dui sollicitudin. Vestibulum eu
              ipsum vel diam elementum tempor vel ut orci. Orci varius natoque
              penatibus et magnis dis parturient montes, nascetur ridiculus mus.
            </p>
          </v-col>
          <v-row justify="center">
            <v-col class="text-center" cols="12">
              {{ new Date().getFullYear() }} — <strong>Vuetify</strong>
            </v-col>
          </v-row>
        </v-row>
      </cf-footer>
    </div>
    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
