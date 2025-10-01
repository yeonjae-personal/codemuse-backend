<template>
  <div
    v-if="props.showCustomValidate"
    ref="selectRef"
    class="custom-validation-wrapper"
  >
    <button
      type="button"
      :class="[
        'info-btn-condition validation-button',
        {
          'is-visible': !hasCondition,
          'is-selected': selectedAttr?.attrId === props.attr,
          'is-full': hasCondition && !hasAction,
        },
      ]"
      @click="openDialog($event)"
    ></button>
    <button
      type="button"
      :class="[
        'info-btn-action validation-button',
        {
          'is-visible': !hasAction,
          'is-selected': selectedAttr?.attrId === props.attr,
          'is-full': !hasCondition && hasAction,
        },
      ]"
      @click="openDialog($event)"
    ></button>
  </div>
  <teleport to="body">
    <div
      v-if="dialogVisible"
      ref="dialogRef"
      class="custom-validation-dialog"
      :style="dialogPosition"
    >
      <ValidationView
        @close-dialog="closeDialog()"
        @change-slide="handleChangeSlide($event)"
      />
    </div>
  </teleport>
</template>
<script setup lang="ts">
import customValidationStore from "@/store/admin/customValidation.store";
import ValidationView from "./ValidationView.vue";

type Props = {
  showCustomValidate?: boolean;
  attr: string;
  passData: any;
  isAdd: boolean;
  isDuplicate?: boolean;
};

const { setSelectedAttr, getListCustomValidationItem } =
  customValidationStore();
const { selectedAttr, customValidationItemsView } = storeToRefs(
  customValidationStore()
);
const props = defineProps<Props>();

const dialogPosition = ref({ top: "0px", left: "0px" });
const dialogVisible = ref(false);
const selectRef = ref<HTMLSelectElement | null>(null);
const dialogRef = ref<HTMLDivElement | null>(null);
const rect = ref<DOMRect | null>(null);

const hasCondition = computed(() => {
  return props.passData?.types?.includes("C");
});

const hasAction = computed(() => {
  return props.passData?.types?.includes("A");
});

const handleChangeSlide = (value: number): void => {
  calculatePosition(value);
};

const openDialog = async (event) => {
  await getListCustomValidationItem({
    item: props.passData.pageType,
    type: props.passData.type,
    subType: props.passData.subType,
    attrUuid: props.attr,
  });
  if (!selectRef.value) return;

  rect.value = event.target.getBoundingClientRect();
  calculatePosition();
  dialogVisible.value = true;
};

const calculatePosition = (index: number = 0): void => {
  let position;
  const xAxis = props.isDuplicate
    ? Number(rect.value?.left) + 5 || 0
    : props.isAdd
      ? Number(rect.value?.right) + 5 || 0
      : Number(rect.value?.left) + 5 || 0;

  const yAxis = rect.value?.top || 0;
  const item = customValidationItemsView.value[index as number];
  const countItem = Math.max(item.conditions.length, item.actions.length);

  let buffer = countItem > 2 ? 520 : countItem === 2 ? 415 : 300;
  const belowSpace = window.innerHeight - yAxis;
  const bufferLeft = xAxis < 920 ? 0 : 925;
  buffer -= yAxis < 525 ? 100 : 0;
  if (belowSpace < 200) {
    position = {
      top: `${yAxis - buffer - 100}px`,
      left: `${xAxis - bufferLeft}px`,
    };
  } else if (belowSpace < 525) {
    position = {
      top: `${yAxis - buffer}px`,
      left: `${xAxis - bufferLeft}px`,
    };
  } else if (belowSpace > 525 && belowSpace < 560) {
    const offset = countItem > 2 ? 140 : countItem === 2 ? 70 : 0;
    position = {
      top: `${yAxis - offset}px`,
      left: `${xAxis - bufferLeft}px`,
    };
  } else if (belowSpace > 560 && belowSpace < 680) {
    const offset = countItem > 2 ? 80 : countItem === 2 ? 50 : 0;
    position = {
      top: `${yAxis - offset}px`,
      left: `${xAxis - bufferLeft}px`,
    };
  } else {
    position = {
      top: `${yAxis + 25}px`,
      left: `${xAxis - bufferLeft}px`,
    };
  }
  dialogPosition.value = position;
};

const closeDialog = () => {
  dialogVisible.value = false;
};

watch(dialogVisible, (value) => {
  setSelectedAttr(value ? props.attr : "");
});

const handleClickOutside = (event: any): void => {
  const dropdown = document.querySelector(".custom-data-list-menu");
  if (!dropdown && dialogRef.value && !dialogRef.value.contains(event.target)) {
    closeDialog();
  } else if (
    dialogRef.value &&
    !dialogRef.value.contains(event.target) &&
    dropdown &&
    !dropdown.contains(event.target)
  ) {
    closeDialog();
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>
<style scoped lang="scss">
.custom-validation-wrapper {
  position: absolute;
  top: 10px;
  right: -10px;
  display: flex;
  flex-direction: column;
  gap: 4px;

  .validation-button {
    position: absolute;
    padding: 10px 8px;
    cursor: pointer;
    background-color: transparent;

    &:active {
      background-color: transparent;
    }

    &::before {
      position: absolute;
      right: 7px;
      content: "";
      width: 4px;
      height: 4px;
      border-radius: 50%;
    }

    &.is-selected {
      &::before {
        width: 6px;
        height: 6px;
      }
    }

    &.is-visible {
      visibility: hidden;
    }
  }

  .info-btn-condition {
    top: -8px;
    right: -6px;

    &::before {
      background-color: #b4caf1;
    }

    &:hover,
    &.is-selected {
      &::before {
        background-color: #4054b2;
      }
    }

    &.is-full {
      padding-bottom: 24px;
    }
  }

  .info-btn-action {
    top: 8px;
    right: -6px;

    &::before {
      top: 2px;
      background-color: #fdced5;
    }

    &:hover,
    &.is-selected {
      &::before {
        background-color: #d9325a;
      }
    }

    &.is-full {
      top: -9px;
      padding-top: 24px;

      &::before {
        top: 17px;
      }
    }
  }
}
/* Dialog */
.custom-validation-dialog {
  width: 920px;
  box-shadow: 0px 8px 32px -6px #7b7878;
  position: absolute;
  border-radius: 16px;
  background: white;
  border: 1px solid #ccc;
  padding: 15px;
  z-index: 1000;
  background: linear-gradient(
    188.32deg,
    rgba(206, 206, 206, 0.24) 49.61%,
    rgba(206, 206, 206, 0.12) 93.62%
  );
  backdrop-filter: blur(5px);
}
</style>
