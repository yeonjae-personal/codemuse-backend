<template>
  <tr class="d-flex">
    <td
      v-for="(header, index) in headers"
      :id="header.key"
      :key="header.key"
      :style="{ '--header-width': header.width }"
      :class="[
        'data-table-header-item d-flex',
        header.class,
        !header.width ? 'flex-1 text-truncate' : '',
        header.align && header.align === 'center' ? 'justify-center' : '',
        header.align && header.align === 'left' ? 'justify-start' : '',
        header.align && header.align === 'right' ? 'justify-end' : '',
        { 'flex-1': isDynamicTable },
        { '!flex-grow-0 !w-[80px]': header.key === 'no' },
        'w-[--header-width]',
        isDynamicTable ? '!min-w-[--header-width]' : 'min-w-auto',
      ]"
    >
      <div
        :class="[
          'text-truncate inline-block py-[18px] px-4',
          { '!w-[80px] py-[18px] px-6': header.key === 'no' },
          { 'inline-flex gap-1 items-center': header.filter },
        ]"
        :style="{ maxWidth: header.width, textAlign: header.align }"
      >
        <CustomTooltip
          :content="header.title"
          :disabled="isDisabledTooltip || header.key === 'no'"
        />
        <div
          v-if="header.filter"
          :class="[
            'menu-filter',
            {
              'is-visible': header?.filter,
              'is-active': header?.isActiveMenu,
            },
          ]"
        >
          <v-menu
            v-model="header.isActiveMenu"
            :close-on-content-click="false"
            :eager="false"
            :location="
              index === headers.length - 1 ? 'bottom start' : 'bottom center'
            "
            :z-index="9999"
          >
            <template #activator="{ props: menuProps, isActive }">
              <FilterTableIcon
                v-bind="menuProps"
                :fill="isActive || header?.isActiveMenu ? '#D9325A' : '#BDC1C7'"
                class="cursor-pointer"
                @click="header.isActiveMenu = !Boolean(isActive)"
              />
            </template>
            <v-list class="mt-3 ml-1 w-[180px] card-filter">
              <LocomotiveComponent
                scroll-container-class="max-h-[240px] !px-0 m-0"
                :is-stop-propagation-wheel="true"
              >
                <v-list-item
                  v-for="item in optionFiltered[header.key]"
                  :key="item.value"
                  :class="item.isChecked && 'bg-item-checked'"
                >
                  <v-list-item-title class="inline-flex align-center !h-[40px]">
                    <v-checkbox
                      v-model="item.isChecked"
                      :true-icon="TrueIcon"
                      :false-icon="FalseIcon"
                      density="compact"
                      class="custom-checkbox"
                    >
                      <template #label>
                        <span
                          :class="[
                            '!max-w-[120px] text-truncate',
                            { 'is-check': item.isChecked },
                          ]"
                        >
                          <CustomTooltip :content="item.name" is-inline />
                        </span>
                      </template>
                    </v-checkbox>
                  </v-list-item-title>
                </v-list-item>
              </LocomotiveComponent>
            </v-list>
          </v-menu>
        </div>
      </div>
    </td>
  </tr>
</template>

<script setup lang="ts">
import TrueIcon from "@/components/prod/icons/TrueIcon.vue";
import FalseIcon from "@/components/prod/icons/FalseIcon.vue";
import type { TableHeader, TableOptionColumnFilter } from "@/types/common";

type Props = {
  headers: TableHeader[];
  isDisabledTooltip?: boolean;
  isDynamicTable?: boolean;
};

withDefaults(defineProps<Props>(), {
  headers: () => [] as any[],
  isDisabledTooltip: false,
  isDynamicTable: false,
});

const optionFiltered = inject<Ref<Record<string, TableOptionColumnFilter[]>>>(
  "optionFiltered",
  ref({})
);
</script>

<style lang="scss" scoped>
.data-table-header-item {
  display: flex;
  align-items: center;
  gap: 4px;
  background: #f7f8fa;
  font-family: Noto Sans KR;
  font-weight: 500;
  font-size: 13px;
  line-height: 20px;
  letter-spacing: 0.25px;
  color: #3a3b3d;

  .menu-filter {
    visibility: hidden;

    &.is-active {
      visibility: visible;
    }
  }

  &:hover {
    .menu-filter {
      &.is-visible {
        visibility: visible;
      }
    }
  }
}

:deep().v-select__selection-text {
  font-size: 12px;
}

:deep(.v-select__selection-text) {
  font-size: 12px;
  font-weight: 400;
  line-height: 16.5px;
  letter-spacing: 0.25px;
  text-align: left;
}

:deep(.v-field__outline__start),
:deep(.v-field__outline__end) {
  border-color: #e6e9ed;
}

:deep(.v-field--appended) {
  padding-right: 12px !important;
}

:deep(
    .v-field.v-field--active.v-field--appended.v-field--center-affix.v-field--dirty.v-field--no-label.v-field--variant-outlined.v-theme--light.v-locale--is-ltr
  ) {
  height: 32px;
}

:deep() .v-field {
  height: 32px;
  display: flex;
  align-items: center;
  width: 59px;
}

:deep(.v-field__input) {
  padding: 8px 0px 8px 10px;
  min-height: 14px;
}

:deep() .v-input .v-input__control .v-field {
  border: 1px solid #e6e9ed;
  outline: none;
  border-radius: 4px;
}

:deep() .v-field .v-field__append-inner .v-icon {
  height: 16px;
  width: 16px;
  min-width: 16px;
}

:deep() .v-select {
  width: unset;
}

:deep() .v-list {
  box-shadow: 1px 1px 10px 0px #0000001f !important;
}

:deep() .v-list .v-label {
  opacity: unset !important;
}

:deep(.custom-checkbox .v-label) {
  font-family: Noto Sans KR;
  font-weight: 400;
  font-size: 13px;
  line-height: 150%;
  letter-spacing: 0.25px;
  color: #3a3b3d;

  .is-check {
    color: #ba1642;
  }
}

:deep(.v-list-item) {
  &:hover {
    background-color: #fff0f2;
  }

  .v-input {
    height: 40px;
  }

  .v-label {
    margin-left: 8px;
  }
}

.card-filter {
  border-radius: 8px !important;
  box-shadow: 2px 2px 16px 0px #0000001f !important;
}

:deep() .bg-item-checked {
  background-color: #fff0f2;
}
</style>
