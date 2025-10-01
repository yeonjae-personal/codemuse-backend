<template>
  <div class="flex align-center relative h-[32px] w-[190px]">
    <div class="cursor-all-scroll vue-draggable-handle">
      <DotGridIcon />
    </div>
    <div
      ref="dropdownRef"
      class="w-full flex align-center gap-1 border-[1px] rounded-[999px] min-h-[32px] min-w-[32px] pr-2 pl-3 factor-no-drag"
      :class="[active ? 'border-[#D9325A]' : 'border-[#F0F2F5]']"
      @click="handleClickCard"
    >
      <span
        class="flex-grow-1 text-[13px] text-[#3A3B3D] text-ellipsis factor-no-drag"
        :style="{ userSelect: 'none' }"
        >{{ data.factorName }}</span
      >
      <div
        v-if="editable"
        ref="actionBtn"
        class="relative leading-none z-10"
        @click.stop="handleOpenAction"
      >
        <button type="button">
          <DotsVerticalIcon height="14" />
        </button>
      </div>
    </div>
    <Teleport to="body">
      <v-list
        class="!py-0.5 rounded-lg list-actions"
        :style="actionsBoxPosition"
        :class="{ 'open-actions': isOpenActionBox }"
      >
        <v-list-item
          v-for="(item, index) in actions"
          :key="index"
          :value="index"
          class="!mb-0"
          :class="{ 'active-bg': item.active }"
          @click="item.onClick"
        >
          <v-list-item-title>
            <div class="flex items-center gap-[6px]">
              <component :is="item.icon" v-bind="item.iconProps"></component>
              {{ item.name }}
            </div>
          </v-list-item-title>
        </v-list-item>
      </v-list>
      <div class="wrapper" :class="{ open: isOpen }" :style="dialogPosition">
        <div v-if="isOpen">
          <LocomotiveComponent
            scroll-content-class=""
            scroll-container-class="max-h-[172px] !px-[0px]"
            is-stop-propagation-wheel
          >
            <template v-for="option in options" :key="option.factorValueCode">
              <div
                class="factor-no-drag option-items"
                :class="[{ 'bg-primary-lightest': option?.inUse }]"
                @click.stop.prevent="toggleCheckbox(option)"
              >
                <div class="overlay factor-no-drag"></div>
                <div
                  class="relative custom-checkbox factor-no-drag"
                  :class="{
                    checked: option?.inUse,
                  }"
                  :aria-checked="option?.inUse"
                  role="checkbox"
                  :aria-labelledby="`label-${option.factorValueCode}`"
                ></div>
                <label
                  class="w-[120px] text-text-base text-[13px] font-medium tracking-[0.25px] leading-[16.5px] text-ellipsis factor-no-drag"
                >
                  <CustomTooltip :content="option.factorValueName" />
                </label>
              </div>
            </template>
          </LocomotiveComponent>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["openOptions", "changeFactorValue", "onOpen"]);
const props = defineProps({
  data: {
    type: Object,
    default: () => {},
  },
  actions: {
    type: Array as PropType<any[]>,
    default: () => [],
  },
  active: {
    type: Boolean,
    default: false,
  },
  editable: {
    type: Boolean,
    default: false,
  },
  isOpen: {
    type: Boolean,
    default: false,
  },
  isOpenActionBox: {
    type: Boolean,
    default: false,
  },
});
const options = ref<any[]>(props.data.factorValues);
const dropdownRef = ref<HTMLDivElement | null>(null);
const actionBtn = ref<HTMLDivElement | null>(null);
const dialogPosition = ref<any>({ top: "0px", left: "0px", width: "0px" });
const actionsBoxPosition = ref<any>({ top: "0px", left: "0px", width: "0px" });

const toggleCheckbox = (value) => {
  value.inUse = !value.inUse;
  emit("changeFactorValue", options.value);
};

const handleClickCard = () => {
  if (props.isOpen) {
    emit("onOpen", false);
  } else {
    if (dropdownRef.value) {
      const rect = dropdownRef.value.getBoundingClientRect();
      dialogPosition.value = {
        top: `${rect.top + 30}px`,
        left: `${rect.left}px`,
        width: `${dropdownRef.value.clientWidth}px`,
      };
      emit("onOpen", true);
    }
  }
};

const handleOpenAction = () => {
  if (props.isOpenActionBox) {
    emit("openOptions", false);
  } else {
    if (actionBtn.value) {
      const rect = actionBtn.value.getBoundingClientRect();
      actionsBoxPosition.value = {
        top: `${rect.top + 30}px`,
        left: `${rect.left - 72}px`,
        width: `100px`,
      };
    }
    emit("openOptions", true);
  }
};

const handleClickOutside = (event: any): void => {
  if (!event.target.classList?.contains("factor-no-drag")) {
    emit("onOpen", false);
    emit("openOptions", false);
  }
};
const handleCloseOption = (event: any) => {
  if (!event.target.classList?.contains("factor-no-drag")) {
    emit("openOptions", false);
    emit("onOpen", false);
  }
};

onMounted(() => {
  document.addEventListener("wheel", handleCloseOption, true);
  document.addEventListener("click", handleClickOutside, true);
});

onBeforeUnmount(() => {
  document.removeEventListener("wheel", handleCloseOption, true);
  document.removeEventListener("click", handleClickOutside, true);
});
</script>

<style lang="scss" scoped>
.wrapper {
  background: #fff;
  display: grid;
  grid-template-rows: 0fr;
  overflow: hidden;
  transition: grid-template-rows 200ms;
  box-shadow: 2px 2px 16px 0px #0000001f;
}
.open {
  position: absolute;
  z-index: 10;
  grid-template-rows: 1fr;
  margin-top: 3px;
  border-top-right-radius: 0;
  border-top-left-radius: 0;
  border-bottom-right-radius: 10px;
  border-bottom-left-radius: 10px;
}
.custom-checkbox {
  width: 20px;
  height: 20px;
  background-color: #ffffff;
  border: 2px solid #dce0e5;
  border-radius: 6px;
}
.custom-checkbox.checked::after {
  content: url("@/assets/icons/checked.svg");
  font-size: 16px;
  color: white;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -40%);
}
.checked {
  background-color: #d9325a;
  border-color: #d9325a;
}
.checked-disabled {
  background: #fdced5;
  border-color: #fdced5;
}

.disabled {
  background: #f0f2f5;
  border-color: #e6e9ed;
}
.list-actions {
  box-shadow: 2px 2px 16px 0px #0000001f;
  max-height: 0px;
  opacity: 0;
  position: absolute;
  transition:
    opacity 0.25s ease,
    max-height 0.5s ease-in-out;
}
.list-actions::-webkit-scrollbar {
  height: 0px;
  width: 0px;
}
.open-actions {
  opacity: 1;
  max-height: 100px;
}
:deep()
  .v-list-item--density-default:not(.v-list-item--nav).v-list-item--one-line {
  padding: 0 12px;
  min-height: 32px;
  min-width: 100px;
}

:deep() .v-list-item-title {
  font-family: "Noto Sans KR", sans-serif !important;
  font-size: 13px;
}

.option-items {
  display: flex;
  align-items: center;
  position: relative;
  gap: 8px;
  padding: 12px;
  cursor: pointer;
  .overlay {
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0px;
    &:hover {
      background: #000;
      opacity: 0.04;
    }
  }
}
</style>
