<template>
  <div
    ref="tabsContainer"
    class="!w-full h-full flex flex-col"
    :class="classTabsBar"
  >
    <div v-if="flowMode" class="step-flow-swapper grid grid-cols-4 gap-4">
      <div
        v-for="(tab, index) in tabs"
        :key="index"
        :class="[
          `item-flow-${index + 1} ${
            tabSelected === tab.value
              ? 'active-' + tab?.status?.toLowerCase()
              : ''
          }`,
          tab?.status?.toLowerCase(),
        ]"
        @click="handleClick(tab)"
      >
        <div class="flow-name text-center text-[11px]">{{ tab.label }}</div>
      </div>
    </div>
    <v-tabs
      v-else
      v-model="tabSelected"
      color="#BA1642"
      align-tabs="center"
      :grow="grow"
      :show-arrows="showArrows"
      :center-active="centerActive"
      class="flex-grow-0"
    >
      <v-tab
        v-for="tab in tabs"
        :key="tab.value"
        :min-width="localTabWidth"
        :value="tab.value"
        class="!capitalize border-b border-darker !px-[6.5px]"
        @click="tab.onClick"
      >
        <div
          :class="`w-full font-size-base my-3 ${
            tabSelected === tab.value
              ? 'text-text-primary'
              : 'text-text-lightest'
          }`"
        >
          {{ tab.label }}
        </div>
      </v-tab>
    </v-tabs>

    <v-card-text class="!p-0 mt-3 flex-grow">
      <v-tabs-window v-model="tabSelected" class="h-full">
        <v-window-item
          v-for="(tab, index) in tabs"
          :key="tab.value"
          :value="tab.value"
          class="h-full overflow-hidden custom-scroll"
          :class="classTabs"
          :eager="
            [OFFER_TABS_VALUE.GENERAL, OFFER_TABS_VALUE.ADDITIONAL].includes(
              tab.value
            )
          "
        >
          <LocomotiveComponent
            v-if="!isUnusedLoco"
            v-show="isMounted"
            :is-show-scrollbar="isShowScroll"
            :scroll-top-content-class="[
              'flex justify-center',
              locoScrollContent,
            ]"
            :scroll-container-class="['pl-[10px]', tab.props?.class || '']"
            scroll-content-class="grow w-full"
            :class="classLoco"
            :dynamic-scroll-key="tab.props?.dynamicScrollKey"
            :is-dynamic-scroll="tab.props?.isDynamicScroll"
          >
            <component
              :is="tab.component"
              :ref="(el) => (componentRefs[index] = el)"
              v-bind="tab.props"
              v-on="tab.events ?? {}"
            />
          </LocomotiveComponent>
          <component
            :is="tab.component"
            v-else
            v-bind="tab.props"
            v-on="tab.events ?? {}"
          />
        </v-window-item>
      </v-tabs-window>
    </v-card-text>
  </div>
</template>

<script setup lang="ts">
import { Tab } from "@/interfaces/prod";
import { OFFER_TABS_VALUE } from "@/constants/offer";
import { PUBLISH_FLOW_STATUS } from "@/constants/publish";

const props = defineProps({
  modelValue: {
    type: String,
    default: "",
  },
  tabs: {
    type: Array as () => Array<Tab>,
    default: () => [],
  },
  classTabs: {
    type: String,
    default: "",
  },
  classTabsBar: {
    type: String,
    default: "tabs",
  },
  grow: {
    type: Boolean,
    default: true,
  },
  showArrows: {
    type: Boolean,
    default: false,
  },
  tabWidth: {
    type: String,
    default: "",
  },
  centerActive: {
    type: Boolean,
    default: false,
  },
  classLoco: {
    type: [String, Array],
    default: "",
  },
  locoScrollContent: {
    type: String,
    default: "",
  },
  isShowScroll: {
    type: Boolean,
    default: false,
  },
  isUnusedLoco: {
    type: Boolean,
    default: false,
  },
  flowMode: {
    type: Boolean,
    default: false,
  },
});

const localTabWidth = ref(props.tabWidth || "auto");
const emit = defineEmits(["update:modelValue"]);
const tabsContainer = ref<HTMLDivElement | null>(null);
const isMounted = ref<boolean>(false);
const componentRefs = ref<any>([]);

const calculateTabWidth = () => {
  if (tabsContainer.value) {
    const containerWidth = tabsContainer.value.offsetWidth;
    const WIDTH_TWO_ARROWS = 64;
    localTabWidth.value = `${(containerWidth - WIDTH_TWO_ARROWS) / 3}px`;
  }
};

const tabSelected = computed({
  get: () => props.modelValue,
  set: (value) => {
    emit("update:modelValue", value);
  },
});

const handleClick = (tab: Tab) => {
  if (tab?.disable && tab?.status !== PUBLISH_FLOW_STATUS.COMPLETE) return;
  tab?.onClick?.(tab);
};

watch(() => props.tabs.length, calculateTabWidth, { immediate: true });

onMounted(() => {
  calculateTabWidth();
  nextTick(() => {
    isMounted.value = true;
  });
});

defineExpose({
  componentRefs: componentRefs.value,
});
</script>

<style scoped lang="scss">
:deep() .v-slide-group__next,
:deep() .v-slide-group__prev {
  color: #6b6d70;
  font-size: 14px;
  min-width: 32px !important;
  max-width: 32px !important;
}
.step-flow-swapper {
  > div {
    position: relative;
    padding-top: 36px;
    height: 78px;
    cursor: pointer;
    &::before {
      display: block;
      width: 24px;
      height: 24px;
      line-height: 24px;
      text-align: center;
      border-radius: 50%;
      color: #3a3b3d;
      background-color: #f0f2f5;
      position: absolute;
      top: 5px;
      left: 50%;
      transform: translateX(-50%);
      transition:
        background-color 0.3s ease,
        color 0.3s ease,
        box-shadow 0.3s ease;
    }

    &:not(:last-child) {
      &::after {
        content: "";
        display: block;
        height: 2px;
        width: 60%;
        position: absolute;
        top: 15px;
        left: 80%;
        background-color: #f0f2f5;
        transition: background-color 0.5s ease;
      }
    }

    &.active-deactive  {
      &::before {
        color: #ba1642 !important;
        background-color: #fee5e7 !important;
        box-shadow: 0px 0px 0px 4px #fff0f2;
      }
      .flow-name {
        color: #ba1642;
      }
    }
    &.active-complete {
      &::before {
        color: #ba1642 !important;
        background-color: #fee5e7 !important;
        box-shadow: 0px 0px 0px 4px #fff0f2;
      }
      .flow-name {
        color: #ba1642;
      }
    }

    &.complete {
      &::before {
        color: #fff;
        background-color: #17b26a;
      }
      &::after {
        background-color: #17b26a;
        animation: fill-line 0.5s ease forwards;
      }
      .flow-name {
        color: #bdc1c7;
      }
    }
  }
}

.item-flow {
  &-1::before {
    content: "1";
  }
  &-2::before {
    content: "2";
  }
  &-3::before {
    content: "3";
  }
  &-4::before {
    content: "4";
  }
}

.flow-name {
  font-size: 11px;
  text-align: center;
  font-weight: 500;
  color: #6b6d70;
}
@keyframes fill-line {
  from {
    width: 0%;
    left: 75%;
  }
  to {
    width: 60%;
    left: 75%;
  }
}
</style>
