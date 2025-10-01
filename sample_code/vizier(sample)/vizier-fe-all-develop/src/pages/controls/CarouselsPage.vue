<script setup lang="ts">
import cfCarousels from "@/components/controls/CfCarousels.vue";
// import Description from "../../components/Description/index.vue";
import Prism from "vue-prism-component";
import cfCarouselExample from "@/components/controls/examples/CfCarouselExample";

const sourceCode = ref(cfCarouselExample);

const items = ref([
  {
    src: "https://cdn.vuetifyjs.com/images/cards/docks.jpg",
  },
  {
    src: "https://cdn.vuetifyjs.com/images/cards/hotel.jpg",
  },
  {
    src: "https://cdn.vuetifyjs.com/images/cards/sunshine.jpg",
  },
]);

const items2 = ref([
  {
    colors: "indigo",
    slides: "First",
  },
  {
    colors: "warning",
    slides: "Second",
  },
  {
    colors: "pink darken-2",
    slides: "Third",
  },
  {
    colors: "red lighten-1",
    slides: "Fourth",
  },
  {
    colors: "deep-purple accent-4",
    slides: "Fifth",
  },
]);
</script>

<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Carousel" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
    <cf-carousels :items="items2" cycle data-interval="1000">
      <template #default="{ item }">
        <div class="d-flex fill-height justify-center align-center">
          <div class="text-h2">{{ item.slides }} Slide</div>
        </div>
      </template>
    </cf-carousels>
    <br />
    <cf-carousels :items="items">
      <template #default="{ item }">
        <div class="d-flex fill-height justify-center align-center">
          <img :src="item.src" alt="img" class="w-full" />
        </div>
      </template>
    </cf-carousels>
    <br />
    <cf-carousels :items="items" hide-delimiters>
      <template #default="{ item }">
        <div class="d-flex fill-height justify-center align-center">
          <img :src="item.src" alt="img" class="w-full" />
        </div>
      </template>
    </cf-carousels>
    <br />
    <cf-carousels :items="items" show-arrows="hover">
      <template #default="{ item }">
        <div class="d-flex fill-height justify-center align-center">
          <img :src="item.src" alt="img" class="w-full" />
        </div>
      </template>
    </cf-carousels>
    <br />
    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
