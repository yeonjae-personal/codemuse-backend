<script setup lang="ts">
const props = defineProps({
  tabs: {
    type: Array<any>,
    default: [],
  },
  bgColor: {
    type: String,
    default: undefined,
  },
  alignTabs: {
    type: String as () => "title" | "center" | "end" | "start" | undefined,
    default: undefined,
  },
  selected: {
    type: String,
    default: undefined,
  },
  tabsClass: {
    type: String,
    default: undefined,
  },
  mode: {
    type: String as () => "card" | "no-card",
    default: "card",
  },
  isTrans: {
    type: Boolean,
    default: true,
  },
  isDisableTab: {
    type: Boolean,
    default: false,
  },
});

const tab = ref(props.selected);

const emit = defineEmits(["tabChange", "tabChangeWaring"]);
const emitUpdateModel = () => {
  if (props.isDisableTab) {
    emit("tabChangeWaring", tab.value, true);
  } else {
    emit("tabChange", tab.value);
  }
};

const handleChangeTabItem = (value) => {
  tab.value = props.isDisableTab ? props.selected : value;
};
watch(
  () => props.selected,
  (val) => {
    if (val) {
      tab.value = val;
    }
  }
);
</script>

<template>
  <div v-if="props.mode === 'no-card'" class="h-[calc(100%-44px)]">
    <v-tabs
      v-model="tab"
      :bg-color="props.bgColor"
      :align-tabs="props.alignTabs"
      :class="props.tabsClass"
      @update:model-value="emitUpdateModel"
    >
      <v-tab
        v-for="t in props.tabs"
        :key="t.value"
        :value="t.value"
        @click="handleChangeTabItem(t.value)"
        >{{
          !isTrans ? t.label : $t(`product_platform.categoryTab.${t.value}`)
        }}</v-tab
      >
    </v-tabs>
    <v-tabs-window v-model="tab" class="window-round">
      <v-window-item
        v-for="t in props.tabs"
        :key="t.value"
        :value="t.value"
        class="h-full"
      >
        <slot :name="t.slot"></slot>
      </v-window-item>
    </v-tabs-window>
  </div>
  <v-card v-else>
    <v-tabs
      v-model="tab"
      :bg-color="props.bgColor"
      :align-tabs="props.alignTabs"
      :class="props.tabsClass"
      @update:model-value="emitUpdateModel"
    >
      <v-tab v-for="t in props.tabs" :key="t.value" :value="t.value">{{
        $t(`product_platform.categoryTab.${t.value}`)
      }}</v-tab>
    </v-tabs>
    <v-card-text>
      <v-window v-model="tab">
        <v-window-item v-for="t in props.tabs" :key="t.value" :value="t.value">
          <slot :name="t.slot"></slot>
        </v-window-item>
      </v-window>
    </v-card-text>
  </v-card>
</template>
<style scoped lang="scss">
.category-tabs {
  background-color: $bg-color-3;
  height: 44px;
  :deep(.v-tab-item--selected) {
    background-color: $bg-color-1 !important;
    border-top: 3px $color-2 solid;
    .v-btn__content {
      color: $color-2 !important;
    }
  }
  :deep(.v-btn.v-tab) {
    background-color: $bg-color-2;
    width: 120px;
    height: 44px !important;
    border-radius: 8px 8px 0px 0px !important;
    margin-left: 6px;
    color: $color-1 !important;
    &:first-child {
      margin-left: 0px;
    }
  }
  :deep(.v-tab__slider) {
    display: none;
  }
  :deep(.v-btn__content) {
    text-transform: none;
    font-size: 15px;
    font-weight: 500;
    line-height: 22.5px;
    letter-spacing: 0.005em;
    text-align: center;
  }
  + .window-round {
    height: 100%;
    border-radius: 0px 12px 12px;
    background-color: $bg-color-1;
  }
}
</style>
