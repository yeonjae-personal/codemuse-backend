<template>
  <div
    class="w-full h-[64px] rounded-[12px] flex align-center cursor-pointer zoom-animation overflow-x-hidden"
    :style="{
      borderColor: `${active ? BORDER_CONFIG.ACTIVE : bdColor}`,
      borderWidth: `${active ? '2px' : '1px'}`,
    }"
    @mouseover="isHoverItem = true"
    @mouseleave="isHoverItem = false"
  >
    <div
      class="flex align-center justify-center h-full rounded-[12px] z-1 pl-[12px] pr-[20px] py-[20px] type-icon !rounded-[10px]"
      :class="[isHover ? '!min-w-[138px]' : 'min-w-[48px]']"
      :style="{ backgroundColor: bgColor }"
      @mouseover="isHover = true"
      @mouseleave="isHover = false"
    >
      <span
        class="font-[800] text-[18px]"
        :style="{ color: textColor }"
        :class="[
          !isHover && 'uppercase pr-1',
          [
            PUBLISH_CODE_STATUS.PROD_TRANSFER,
            PUBLISH_CODE_STATUS.PUBLISH_REQUEST,
          ].includes(keyType) && 'text-center whitespace-pre-wrap',
        ]"
      >
        {{ isHover ? textType : keyType }}
      </span>
    </div>
    <div
      class="z-2 bg-white h-full w-full ml-[-15px] rounded-[12px] px-4 flex flex-col justify-center relative"
    >
      <div class="text-[#3a3b3d] text-[13px] font-[500] truncate">
        <abbr
          :title="isNew && !title ? $t('LB00000470') : title"
          class="text-[#3A3B3D] my-[0px] text-truncate !no-underline"
          v-html="isNew && !title ? $t('LB00000470') : highlightedName"
        >
        </abbr>
      </div>
      <div class="text-[#6b6d70] text-[11px] font-[400] whitespace-nowrap">
        <span>{{
          formatDate(startDate, DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
        }}</span>
        <span v-if="endDate">
          <span class="mx-1">-</span>
          {{
            formatDate(endDate, DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
          }}
        </span>
      </div>
      <template v-if="isNew">
        <div class="new-mark"></div>
      </template>
    </div>
  </div>
</template>
<script setup lang="ts">
import { BORDER_CONFIG, DATE_FORMAT } from "@/constants/index";
import { PUBLISH_CODE_STATUS } from "@/constants/publish";
import { escapeRegExp, formatDate } from "@/utils/format-data";

const props = defineProps({
  active: {
    type: Boolean,
    default: false,
  },
  isNew: {
    type: Boolean,
    default: false,
  },
  bgColor: {
    type: String,
    default: "",
  },
  textColor: {
    type: String,
    default: "",
  },
  bdColor: {
    type: String,
    default: "",
  },
  startDate: {
    type: String,
    default: "",
  },
  endDate: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
  textType: {
    type: String,
    default: "",
  },
  keyType: {
    type: String,
    default: "",
  },
  searchText: {
    type: String,
    default: "",
  },
  searchField: {
    type: String,
    default: "",
  },
  keyTypeDefault: {
    type: String,
    default: "",
  },
});
const isHover = ref(false);
const isHoverItem = ref(false);

const highlightedName = computed(() => {
  if (!props.searchText || props.searchField != props.keyTypeDefault)
    return props.title;
  const escapedSearchText = escapeRegExp(props.searchText);
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.title.replace(regex, '<span class="highlight">$1</span>');
});
</script>

<style lang="scss" scoped>
.type-icon {
  width: 0px !important;
  transition: min-width 0.2s linear;
  overflow: hidden;
  white-space: nowrap;
}
:deep() .highlight {
  background-color: yellow;
}
.new-mark {
  width: 8px;
  height: 8px;
  position: absolute;
  top: 6px;
  right: 7px;
  background: #ea4f3a;
  border-radius: 999px;
}
</style>
