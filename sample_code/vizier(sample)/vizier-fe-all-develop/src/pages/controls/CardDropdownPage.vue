<script setup lang="ts">

import Prism from "vue-prism-component";
import CfCardDropdownExample from "@/components/controls/examples/CfCardDropdownExample";

const sourceCode = ref(CfCardDropdownExample);
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
    <div class="flex flex-column gap-[10px]">
      <cf-card-dropdown
        title="Data Allowance #1"
        description="OPAW00000901"
        type-bg="linear"
        border-color-action="green"
        display-border-left="green"
        type-of-prod="S"
        icon-color="#23B27F"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: false,
          isActiveNodeRight: false,
        }"
        show-icon-status
      >
        <template #detail> </template>
      </cf-card-dropdown>

      <cf-card-dropdown
        title="Data Allowance #2"
        description="OPAW00000901"
        type-bg="linear"
        border-color-action="pink"
        display-border-left="pink"
        type-of-prod="P"
        dropdown-icon="mdi-dots-vertical"
        icon-color="#FDCED5"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: false,
          isActiveNodeRight: false,
        }"
      >
        <template #detail>Detail Here</template>
      </cf-card-dropdown>

      <cf-card-dropdown
        title="Data Allowance #2"
        description="OPAW00000901"
        type-bg="linear"
        border-color-action="pink"
        display-border-left="pink"
        icon-color="#FDCED5"
      >
        <template #detail>Detail Here</template>
      </cf-card-dropdown>

      <cf-card-dropdown
        title="Data Allowance #3"
        description="OPAW00000901"
        type-bg="linear"
        border-color-action="blue"
        display-border-left="blue"
        type-of-prod="D"
        dropdown-icon="mdi-dots-vertical"
        icon-color="#B2DDFF"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
      >
        <template #detail>Detail Here</template>
      </cf-card-dropdown>

      <cf-card-dropdown
        title="Data Allowance #4"
        description="OPAW00000901"
        type-bg="light"
        border-color-action="purple"
        type-of-prod="R"
        dropdown-icon="mdi-dots-vertical"
        icon-color="#E7C9FB"
        class-name="card-round-style"
        hide-detail
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
      >
        <template #detail>Detail Here</template>
      </cf-card-dropdown>
      <cf-card-dropdown
        title="Data Allowance #4"
        description="OPAW00000901"
        type-bg="light"
        border-color-action="purple"
        type-of-prod="R"
        icon-color="#E7C9FB"
        class-name="card-round-style"
        :node="{
          hideNodeLeft: true,
          isActiveNodeLeft: false,
          hideNodeRight: true,
          isActiveNodeRight: false,
        }"
      >
        <template #detail>Detail Here</template>
      </cf-card-dropdown>
    </div>

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>
