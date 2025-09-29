<script setup lang="ts">
import CfCard from "./CfCard.vue";
import CfButton from "./CfButton.vue";
import CfSpinner from "@/components/controls/CfSpinner.vue";

export interface ListGroup {
  title: string | number;
  subtitle?: string | number;
  prependAvatar?: string;
}

const props = defineProps({
  items: {
    type: Array<any>,
    default: [],
  },
  lines: {
    type: String as () => "one" | "two" | "three" | undefined,
    default: undefined,
  },
  title: {
    type: String,
    default: undefined,
  },
  subtitle: {
    type: String,
    default: undefined,
  },
  subheader: {
    type: String,
    default: undefined,
  },
  maxRecord: {
    type: Number,
    default: 0,
  },
  numberIncreased: {
    type: Number,
    default: 0,
  },
});

const count = ref<number>(0);

const emit = defineEmits(["click", "seeMoreClick"]);

const seeMoreClick = () => {
  const lastItem = props.items[props.items.length - 1];
  count.value += props.numberIncreased;
  emit("seeMoreClick", {
    lastItem: lastItem.id,
    count: count.value,
  });
};

onMounted(() => {
  count.value = props.numberIncreased;
});
</script>

<template>
  <div class="flex flex-row-reverse">
    <span
      v-if="props.maxRecord >= 2"
      class="text-gray-500 text-lg text-pink-600"
      >{{ props.maxRecord }}{{ $t("common.lbl_items") }}</span
    >
    <span
      v-if="props.maxRecord === 1"
      class="text-gray-500 text-lg text-pink-600"
      >{{ props.maxRecord }}{{ $t("common.lbl_item") }}</span
    >
    <span
      v-if="props.maxRecord <= 0"
      class="text-gray-500 text-lg text-pink-600"
      >0{{ $t("common.lbl_item") }}</span
    >
    <span class="text-gray-500 text-lg">{{ $t("common.lbl_all") }}:&nbsp;</span>
  </div>
  <div v-if="items.length > 0">
    <cf-card>
      <v-list :lines="$props.lines" class="cf-list">
        <v-list-subheader v-if="props.subheader">{{
          props.subheader
        }}</v-list-subheader>
        <v-list-item
          v-for="(item, index) in props.items"
          :key="index"
          color="primary"
        >
          <template #prepend>
            <slot name="prepend" :data="{ item, index }"></slot>
          </template>
          <template #append>
            <slot name="append" :data="{ item, index }"></slot>
          </template>
        </v-list-item>
      </v-list>
      <div
        v-if="count < props.maxRecord"
        class="flex flex-row mb-3 ml-3 w-[130px]"
      >
        <cf-button
          :label="$t('common.btn_see_more')"
          rounded="0"
          @click="seeMoreClick"
        />
      </div>
    </cf-card>
  </div>
  <div v-else class="flex justify-center">
    <cf-spinner indeterminate color="pink"> </cf-spinner>
  </div>
</template>

<style scoped>
.cf-list :deep(img) {
  margin: 0;
}
.cf-list :deep(.v-list-item:hover) {
  cursor: pointer;
  background: #f6f6f6;
}
</style>
