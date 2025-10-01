<template>
  <div
    ref="dropdownRef"
    :class="['custom-select-list', { 'is-open': isOpen }]"
    @click="handleShowMenu"
  >
    <div v-if="selectedValue" class="custom-select-list-item truncate">
      <CustomTooltip
        :content="selectedTitle"
        :disabled="isOpen"
        location="bottom"
      />
    </div>
    <div v-else class="custom-select-list-item__placeholder">
      {{ placeholder }}
    </div>
    <div class="custom-select-list-right">
      <div
        v-if="slots.typeCode && selectedValue"
        class="custom-select-list-right__custom"
      >
        <slot name="typeCode"></slot>
      </div>
      <BasePopover
        :options="popoverOptions"
        custom-location="bottom-left"
        class="flex-initial"
        @open-options="() => (isOpen = false)"
      >
        <template #activator>
          <DotsVerticalIcon />
        </template>
      </BasePopover>
    </div>
  </div>
  <div
    v-if="isOpen && !isFullOptionSelected"
    ref="target"
    class="custom-select-list-menu"
  >
    <div v-if="slots.header" class="custom-select-list-menu__header">
      <slot name="header"></slot>
    </div>

    <LocomotiveComponent
      v-if="options.length > 0"
      scroll-content-class="pb-[3px] rounded-[8px]"
      scroll-container-class="!px-0 max-h-[206px]"
      is-stop-propagation-wheel
    >
      <div
        v-for="item in options"
        v-show="!item.isDisabled"
        :key="item.value"
        :class="[
          'custom-select-list-menu__item',
          { 'is-selected': selectedValue === item.value },
        ]"
        @click="handleSelectItem(item.value)"
      >
        {{ item.name }}
      </div>
    </LocomotiveComponent>
  </div>
</template>

<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import get from "lodash-es/get";
import DeleteIcon from "@/components/prod/icons/DeleteIcon.vue";
type Option = {
  value: string;
  name: string;
  isDisabled?: boolean;
};

type Props = {
  options: Option[];
  modelValue: String;
  placeholder: String;
};

const emit = defineEmits(["update:modelValue", "removeItem"]);
const props = defineProps<Props>();
const slots = useSlots();
const { t } = useI18n();

const target = ref<HTMLDivElement>();
const dropdownRef = ref<HTMLDivElement | null>(null);
const dialogTopPosition = ref<string>("");
const isOpen = ref<boolean>(false);

const selectedValue = computed<string>({
  get: () => props.modelValue as string,
  set: (value: string) => {
    emit("update:modelValue", value);
  },
});

const selectedTitle = computed<string>(() => {
  if (props.modelValue && props.options.length) {
    const selectedOption = props.options.find(
      (item: Option) => item.value === props.modelValue
    );
    return get(selectedOption, "name", "");
  }
  return "";
});

const isFullOptionSelected = computed<boolean>(() =>
  props.options.every(({ isDisabled }) => isDisabled)
);

const popoverOptions = computed(() => [
  {
    name: t("product_platform.actionRemove"),
    icon: DeleteIcon,
    iconProps: { class: "text-text-lighter" },
    onClick: () => {
      emit("removeItem");
    },
  },
]);

watch(isOpen, (value) => {
  if (!value) {
    const scrollElement = document.querySelector(".general-wrapper");
    scrollElement!.setAttribute("style", "height: 'auto'");
  }
});

const handleSelectItem = (value: string): void => {
  selectedValue.value = value;
  isOpen.value = false;
};

const handleShowMenu = (): void => {
  if (isOpen.value) {
    isOpen.value = false;
  } else {
    nextTick(() => {
      const scrollElement = document.querySelector(".general-wrapper");
      const scrollRect = scrollElement?.getBoundingClientRect();
      const bodyHeight = document.body.clientHeight;
      if (scrollRect!.bottom + 60 > bodyHeight) {
        scrollElement!.setAttribute("style", "height: 'auto'");
      } else {
        scrollElement!.setAttribute("style", `height: ${bodyHeight - 290}px`);
      }
      if (dropdownRef.value && !isFullOptionSelected.value) {
        const bodyHeight = document.body.clientHeight;
        const rect = dropdownRef.value.getBoundingClientRect();
        const availableOptions = props.options.filter(
          ({ isDisabled }) => !isDisabled
        );
        const offsetValues = [105, 145, 185, 225, 265];
        const offsetTop =
          offsetValues[
            Math.min(availableOptions.length - 1, offsetValues.length - 1)
          ];
        if (bodyHeight < rect.bottom + 295) {
          dialogTopPosition.value = `${
            dropdownRef.value.offsetTop - offsetTop
          }px`;
        } else {
          dialogTopPosition.value = `${dropdownRef.value.offsetTop + 48}px`;
        }
        isOpen.value = true;
      }
    });
  }
};

const handleClickOutside = (event: any): void => {
  const dropdown = document.querySelector(".custom-select-list-menu");
  if (
    dropdownRef.value &&
    !dropdownRef.value.contains(event.target) &&
    dropdown &&
    !dropdown.contains(event.target)
  ) {
    isOpen.value = false;
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>

<style lang="scss" scoped>
.custom-select-list {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  height: 40px;
  width: 330px;
  padding: 7px 12px 7px 14px;
  background-color: #fff;
  border-radius: 99px;
  transition: all 0.2s ease;
  border: 1px solid #e6e9ed;
  cursor: pointer;

  &.is-open {
    border-color: #ff8fa1;
  }
}

.custom-select-list-item {
  flex-shrink: 0;
  max-width: 264px;
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 400;
  color: #3a3b3d;
  cursor: pointer;

  &__placeholder {
    font-family: Noto Sans KR;
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #bdc1c7;
    cursor: pointer;
  }
}

.custom-select-list-right {
  display: flex;
  align-items: center;
  gap: 8px;

  &__custom {
    border-radius: 4px;
    font-size: 13px;
    line-height: 150%;
    color: #6b6d70;
  }
}

.custom-select-list-menu {
  position: absolute;
  left: auto;
  top: v-bind(dialogTopPosition);
  width: 330px;
  background-color: #fff;
  box-shadow: 2px 2px 16px 0px #0000001f;
  border-radius: 8px;
  max-height: 260px;
  z-index: 9999;

  &__item {
    padding: 12px;
    font-size: 13px;
    color: #6b6d70;
    background-color: #fff;
    line-height: 16.5px;
    font-family: Noto Sans KR;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
      color: #ba1642;
      background-color: #fff0f2;
    }

    &.is-selected {
      color: #ba1642;
      background-color: #fff0f2;
    }
  }
}
</style>
