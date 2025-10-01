<script setup lang="ts">
import CfTextarea from "@/components/controls/CfTextarea.vue";
import cfTextareaExample from "@/components/controls/examples/CfTextareaExample";
import Prism from "vue-prism-component";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfTextareaExample);

const description = ref("");

const handleUpdateModel = (val: string) => {
  console.log(val);
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Textarea" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex flex-col gap-4">
      <div class="flex flex-wrap gap-4">
        <cf-textarea label="Label"></cf-textarea>
        <cf-textarea label="Label" variant="outlined"></cf-textarea>
        <cf-textarea label="Label" variant="underlined"></cf-textarea>
        <cf-textarea label="Label" variant="solo"></cf-textarea>
        <cf-textarea label="Label" variant="solo-filled"></cf-textarea>
        <cf-textarea label="Label" variant="solo-inverted"></cf-textarea>
      </div>
      <div class="flex flex-wrap gap-4">
        <cf-textarea
          label="Label"
          variant="solo"
          prepend-icon="$vuetify"
        ></cf-textarea>
        <cf-textarea
          :model="description"
          label="Label"
          variant="solo"
          bg-color="amber-lighten-4"
          color="orange orange-darken-4"
          @update:model="handleUpdateModel"
        ></cf-textarea>
      </div>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
