const CfCardDropdownExample = `
<script setup lang="ts">
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
        typeOfProd="S"
        iconColor="#23B27F"
        :node="{
        hideNodeLeft: true,
        isActiveNodeLeft: false,
        hideNodeRight: false,
        isActiveNodeRight: false,
      }"
      >
      <template #detail>
      </template>
      </cf-card-dropdown>

      <cf-card-dropdown
        title="Data Allowance #2"
        description="OPAW00000901"
        type-bg="linear"
        border-color-action="pink"
        display-border-left="pink"
        typeOfProd="P"
        dropdownIcon="mdi-dots-vertical"
        iconColor="#FDCED5"
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
        iconColor="#FDCED5"
      >
      <template #detail>Detail Here</template>
      </cf-card-dropdown>

      <cf-card-dropdown
        title="Data Allowance #3"
        description="OPAW00000901"
        type-bg="linear"
        border-color-action="blue"
        display-border-left="blue"
        typeOfProd="D"
        dropdownIcon="mdi-dots-vertical"
        iconColor="#B2DDFF"
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
        typeOfProd="R"
        dropdownIcon="mdi-dots-vertical"
        iconColor="#E7C9FB"
        className="card-round-style"
        hideDetail
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
        typeOfProd="R"
        iconColor="#E7C9FB"
        className="card-round-style"
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
</template>
`;

export default CfCardDropdownExample;
