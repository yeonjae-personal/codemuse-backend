<script setup lang="ts">
import { WIDTH_BUTTON } from "@/constants/index";
import { ButtonColorType } from "@/enums";
</script>

<template>
  <BaseButton :color="ButtonColorType.Gray" :width="WIDTH_BUTTON.ICON">
    <SearchIcon fill="#6B6D70" />
  </BaseButton>
</template>
