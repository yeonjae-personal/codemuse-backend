<template>
  <div
    ref="dropdownRef"
    :class="['custom-data-list', { 'is-open': isOpen }]"
    @click="showMenu"
  >
    <div class="custom-data-list-items">
      <div
        v-for="item in listOptionsValid"
        :key="item.value"
        :class="{ 'custom-data-list-item truncate': item.value !== 'none' }"
      >
        <CustomTooltip
          :content="item.label"
          :disabled="item.value === 'none'"
          location="bottom"
        />
      </div>
    </div>
    <div class="custom-data-list-right">
      <div class="custom-data-list-right__number">
        {{ listOptions.length }}
      </div>
      <ChevronDown
        :class="['custom-data-list-right__icon', { 'is-open': isOpen }]"
      />
    </div>
  </div>
  <teleport to="body">
    <div v-if="isOpen" class="custom-data-list-menu" :style="dialogPosition">
      <LocomotiveComponent
        v-if="options.length > 0"
        scroll-content-class=""
        scroll-container-class="!px-0 max-h-[132px]"
      >
        <div
          v-for="item in options"
          :key="item.id"
          class="custom-data-list-menu__item"
        >
          <div
            :class="[
              'custom-data-list-menu__box',
              {
                'is-check': isCheckItem(item.value),
              },
            ]"
          ></div>
          <div class="custom-data-list-menu__label">{{ item.label }}</div>
        </div>
      </LocomotiveComponent>
    </div>
  </teleport>
</template>

<script lang="ts" setup>
import ChevronDown from "@/components/prod/icons/ChevronDown.vue";

type Props = {
  options: any[];
  selectedOptions: any[];
};

const props = defineProps<Props>();

const dropdownRef = ref<HTMLDivElement | null>(null);
const dialogPosition = ref({ top: "0px", left: "0px" });
const isOpen = ref<boolean>(false);

const listOptions = computed(() =>
  props.selectedOptions
    .map((value) => props.options.find((option) => option.value === value))
    .filter((option) => option)
);

const listOptionsValid = computed(() => {
  if (listOptions.value.length > 3) {
    return [...listOptions.value.slice(0, 3), { value: "none", label: "..." }];
  }
  return listOptions.value;
});

const isCheckItem = (value: string): boolean => {
  return props.selectedOptions.includes(value);
};

const showMenu = () => {
  if (isOpen.value) {
    isOpen.value = false;
  } else {
    if (dropdownRef.value) {
      const rect = dropdownRef.value.getBoundingClientRect();
      dialogPosition.value = {
        top: `${rect.top + 40}px`,
        left: `${rect.left}px`,
      };
      isOpen.value = true;
    }
  }
};

const handleClickOutside = (event: any): void => {
  const dropdown = document.querySelector(".custom-data-list-menu");
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
.custom-data-list {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  padding: 6px 12px;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  background-color: #fff;
  transition: all 0.3s ease;
}

.custom-data-list-items {
  display: flex;
  align-items: center;
  gap: 6px;
}

.custom-data-list-item {
  flex-shrink: 0;
  width: 60px;
  padding: 2px 6px;
  font-size: 11px;
  font-weight: 500;
  color: #3a3b3d;
  background-color: #e7e7e7;
  border-radius: 4px;
  cursor: pointer;
}

.custom-data-list-right {
  display: flex;
  align-items: center;
  gap: 8px;

  &__number {
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 13px;
    line-height: 150%;
    color: #ba1642;
    font-weight: 500;
    background: #fff0f2;
  }

  &__icon {
    transition: all 0.2s ease;

    &.is-open {
      transform: rotate(180deg);
    }
  }
}

.custom-data-list-menu {
  position: fixed;
  width: 341px;
  background-color: #fff;
  border: 1px solid #dce0e5;
  border-radius: 8px;
  max-height: 156px;
  padding: 12px 14px;
  z-index: 9999;
  margin-top: 2px;

  &__item {
    display: flex;
    align-items: center;
    gap: 10px;

    &:not(:last-of-type) {
      padding-bottom: 8px;
    }
  }

  &__box {
    position: relative;
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    background: #dce0e5;
    border-radius: 4px;

    &.is-check {
      content: url(/src/assets/icons/checked.svg);
      font-size: 16px;
      color: white;
    }
  }

  &__label {
    font-size: 13px;
    font-weight: 500;
    color: #3a3b3d;
    line-height: 16.5px;
    font-family: Noto Sans KR;
    cursor: default;
  }
}
</style>
