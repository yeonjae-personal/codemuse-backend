<template>
  <tr class="d-flex items-center table-group-header">
    <td
      v-for="header in headers"
      :key="header.key"
      :style="{ width: header.width }"
      :class="[
        'table-group-header-item d-flex justify-center',
        header.class,
        header.children?.length ? 'is-group h-[112px]' : 'h-[112px]',
      ]"
    >
      <div
        :class="[
          'text-truncate py-[18px] h-full d-flex items-center',
          header.key === 'no' ? 'px-6' : 'px-4',
          getTableHeaderClasses(header),
        ]"
        :style="{ maxWidth: header.width, textAlign: header.align }"
      >
        <CustomTooltip :content="header.title" :disabled="isDisabledTooltip" />
      </div>
      <div
        v-if="header.children?.length"
        class="d-flex items-center h-[56px] table-group-children"
      >
        <template v-for="children in header.children" :key="children.key">
          <div
            :class="[
              'text-truncate h-full table-group-children-item',
              { 'py-[18px] px-4': header.children?.length },
              getTableHeaderClasses(children),
            ]"
            :style="{ width: children.width, textAlign: children.align }"
          >
            <CustomTooltip
              :content="children.title"
              :disabled="isDisabledTooltip"
            />
          </div>
        </template>
      </div>
    </td>
  </tr>
</template>

<script setup lang="ts">
import type { TableHeader } from "@/types/common";

type Props = {
  headers: TableHeader[];
  isDisabledTooltip?: boolean;
};

withDefaults(defineProps<Props>(), {
  headers: () => [] as any[],
  isDisabledTooltip: false,
});

const getTableHeaderClasses = (header: TableHeader): string => {
  return [
    !header.width ? "flex-1 text-truncate" : "",
    header.align === "center" ? "justify-center" : "",
    header.align === "left" ? "justify-start" : "",
    header.align === "right" ? "justify-end" : "",
  ]
    .filter(Boolean)
    .join(" ");
};
</script>

<style lang="scss" scoped>
.table-group-header {
  background: #f7f8fa;
}

.table-group-header-item {
  flex-direction: column;
  justify-content: center;
  font-family: Noto Sans KR;
  font-weight: 500;
  font-size: 13px;
  line-height: 20px;
  letter-spacing: 0.25px;
  color: #3a3b3d;

  &:not(:last-of-type) {
    border-right: 1px solid #f0f2f5;
  }
}

.table-group-children {
  border-top: 1px solid #f0f2f5;
}

.table-group-children-item {
  &:not(:last-of-type) {
    border-right: 1px solid #f0f2f5;
  }
}
</style>
