<template>
  <div :style="{ minWidth: panelWidth }">
    <v-expansion-panels v-model="pane">
      <v-expansion-panel class="custom-panel" :readonly="isReadOnly">
        <v-expansion-panel-title
          :color="panelBackgroundColor"
          :style="{ color: titleTextColor }"
          class="expansion-panel-title-container"
          @click="togglePanel"
        >
          {{ title }}
          <template #actions="{ expanded }">
            <div v-if="isShowActions" class="relative">
              <base-popover
                :options="listActions"
                custom-location="bottom-left"
                @open-options="emit('open-options')"
              >
                <template #activator>
                  <DotsVerticalIcon />
                </template>
              </base-popover>
            </div>
            <ChevronDown
              v-if="!hideAction"
              size="18"
              class="transition duration-150 ease-out"
              :class="{ 'rotate-180': expanded }"
              :fill="titleTextColor"
            />
          </template>
        </v-expansion-panel-title>
        <v-expansion-panel-text
          v-if="primaryText"
          :class="isEditMode && 'level-desc-layout'"
        >
          <BaseTextArea
            v-if="isEditMode"
            v-model="develDesc"
            class="font-size-base text-text-base"
            text-area-class="!min-h-[40px]"
            locomotive-container-class="!px-[10px] !max-h-[67px]"
            locomotive-content-class="!py-[10px]"
          />
          <div v-else class="expansion-panel-text-container">
            <p class="expansion-panel-primary-text">{{ primaryText }}</p>
            <p class="expansion-panel-sub-text">{{ subText }}</p>
          </div>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </div>
</template>

<script setup lang="ts">
import { useCategoryStore } from "@/store";

const emit = defineEmits(["open-options", "setValueTextDesc"]);

const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  primaryText: {
    type: String,
    default: "",
  },
  headerBgColor: {
    type: String,
    default: "#FFFFFF",
  },
  headerTxtColor: {
    type: String,
    default: "#000000",
  },
  activeBgColor: {
    type: String,
    default: "#FEE5E7",
  },
  activeTxtColor: {
    type: String,
    default: "#BA1642",
  },
  subText: {
    type: String,
    default: "",
  },
  hideAction: {
    type: Boolean,
    default: false,
  },
  isReadOnly: {
    type: Boolean,
    default: false,
  },
  isShowActions: {
    type: Boolean,
    default: false,
  },
  panelWidth: {
    type: String,
    default: "100%",
  },
  level: {
    type: Number,
    default: 1,
  },
  listActions: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
});
const categoryStore = useCategoryStore();

const isActived = ref(false);
const pane = ref<any[]>([]);
const develDesc = ref("");

const togglePanel = async () => {
  isActived.value = !isActived.value;
};

const getColor = (activeColor, defaultColor) => {
  return props.level > 2 && isActived.value ? activeColor : defaultColor;
};

const titleTextColor = computed(() =>
  getColor(props.activeTxtColor, props.headerTxtColor)
);

const panelBackgroundColor = computed(() =>
  getColor(props.activeBgColor, props.headerBgColor)
);

const isEditMode = computed(() => categoryStore.getIsEdit);

watch(
  () => isEditMode.value,
  (val) => {
    pane.value = val ? [0] : [];
    isActived.value = val;
    develDesc.value = props.subText
      ? props.primaryText.concat("\r\n", props.subText)
      : props.primaryText;
  },
  {
    immediate: true,
  }
);

watch(
  () => develDesc.value,
  (val) => {
    let text = val.replace("\n", "\r\n");
    emit("setValueTextDesc", { level: props.level, value: text });
  }
);
</script>

<style scoped lang="scss">
.custom-panel > :deep(.v-expansion-panel-title) {
  border-radius: 0 0 12px 12px;
  padding: 14px 16px;
}

.expansion-panel-title-container {
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
}

.expansion-panel-text-container {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.expansion-panel-primary-text {
  font-size: 13px;
  font-weight: 400;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: left;
  color: #3a3b3d;
}

.expansion-panel-sub-text {
  font-size: 11px;
  font-weight: 400;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: left;
  color: #6b6d70;
}

.custom-panel {
  filter: drop-shadow(0px 2px 16px rgba(0, 0, 0, 0.12));
  border-radius: 0px 0px 12px 12px;
}

.custom-panel :deep() .v-expansion-panel-title--active {
  min-height: min-content;
  border-radius: 0px 0px 12px 12px;
}

:deep(.level-desc-layout .v-expansion-panel-text__wrapper) {
  padding: 5px !important;
}
:deep(.level-desc-layout .v-field) {
  height: 60px !important;
}
:deep(.level-desc-layout .v-field__input) {
  height: 60px !important;
  padding: 10px;
}

:deep(.v-textarea .v-field__input) {
  -webkit-mask-image: unset;
}
</style>
