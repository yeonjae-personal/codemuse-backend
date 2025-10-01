<script setup lang="ts">
import Prism from "vue-prism-component";
import CfFileInput from "@/components/controls/CfFileInput.vue";
import cfFileInputExample from "@/components/controls/examples/CfFileInputExample";
// import Description from "@/components/Description/index.vue";

const sourceCode = ref(cfFileInputExample);

const detectChange = (_event: any) => {
  //
};
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="File Input" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>

    <div class="flex flex-col gap-4">
      <cf-file-input label="Default"></cf-file-input>
      <div class="flex flex-wrap gap-4">
        <cf-file-input
          label="Variant outlined"
          variant="outlined"
        ></cf-file-input>
        <cf-file-input
          label="Variant underlined"
          variant="underlined"
        ></cf-file-input>
      </div>
      <div class="flex flex-wrap gap-4">
        <cf-file-input label="Variant solo" variant="solo"></cf-file-input>
        <cf-file-input
          label="Variant solo-filled"
          variant="solo-filled"
        ></cf-file-input>
        <cf-file-input
          label="Variant solo-inverted"
          variant="solo-inverted"
        ></cf-file-input>
      </div>
      <div class="flex flex-wrap gap-4">
        <cf-file-input
          label="Show icon clearable"
          variant="solo"
          clearable
        ></cf-file-input>
        <cf-file-input
          label="Disabled File input"
          variant="solo"
          disabled
        ></cf-file-input>
      </div>
      <div class="flex flex-wrap gap-4">
        <cf-file-input
          label="Accept type"
          variant="solo"
          accept="image/*"
        ></cf-file-input>
        <cf-file-input
          label="Change prepend-icon"
          variant="solo"
          accept="image/*"
          prepend-icon="mdi-camera"
        ></cf-file-input>
      </div>
      <div class="flex flex-wrap gap-4">
        <cf-file-input
          label="Show size"
          variant="solo"
          show-size
          @update:model="detectChange"
        ></cf-file-input>
      </div>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
