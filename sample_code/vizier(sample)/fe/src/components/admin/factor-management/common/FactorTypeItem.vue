<template>
  <div
    class="rounded-[12px] flex flex-column items-center py-3 cursor-pointer p-3 zoom-animation"
    :class="[
      disable ? 'bg-[#E9EBF0]' : active ? 'bg-[#FFF0F2]' : 'bg-white ',
      active
        ? `!border-[${BORDER_CONFIG.ACTIVE}] border-[2px]`
        : '!border-[#e6e9ed] border-[1px]',
    ]"
    :style="{ width: width, height: height }"
    @mouseover="isHover = true"
    @mouseleave="isHover = false"
    @click="emit('selected-item')"
  >
    <div
      v-if="showIcon"
      :class="disable && 'opacity-[32%]'"
      class="min-h-[28px] flex align-center"
    >
      <ItemIcon />
    </div>
    <div
      class="flex align-center justify-center text-[#3A3B3D] text-text-base font-size-base font-weight-[400] text-ellipsis text-center h-full"
      :class="[disable && 'opacity-[32%]', showIcon && 'mt-[6px]']"
    >
      <CustomTooltip :content="title">
        <span class="!no-underline text-ellipsis" v-html="highlightedName" />
      </CustomTooltip>
    </div>
  </div>
</template>

<script setup lang="ts">
import DemographicIcon from "@/components/prod/icons/DemographicIcon.vue";
import UsageIcon from "@/components/prod/icons/UsageIcon.vue";
import DevicePlanIcon from "@/components/prod/icons/DevicePlanIcon.vue";
import PaymentIcon from "@/components/prod/icons/PaymentIcon.vue";
import BehavioralIcon from "@/components/prod/icons/BehavioralIcon.vue";
import MarketingIcon from "@/components/prod/icons/MarketingIcon.vue";
import LocationIcon from "@/components/prod/icons/LocationIcon.vue";
import CustomerIcon from "@/components/prod/icons/CustomerIcon.vue";
import ProductServiceIcon from "@/components/prod/icons/ProductServiceIcon.vue";
import FeedbackIcon from "@/components/prod/icons/FeedbackIcon.vue";
import ContractIcon from "@/components/prod/icons/ContractIcon.vue";
import ServiceAvai from "@/components/prod/icons/ProductServiceIcon.vue";
import CustomerEnga from "@/components/prod/icons/CustomerIcon.vue";
import LocationBased from "@/components/prod/icons/LocationIcon.vue";
import { escapeRegExp } from "@/utils/format-data";
import { BORDER_CONFIG } from "@/constants/index";

const emit = defineEmits(["selected-item"]);
const props = defineProps({
  typeCode: {
    type: String,
    default: "",
  },
  title: {
    type: String,
    default: "",
  },
  searchText: {
    type: String,
    default: "",
  },
  width: {
    type: String,
    default: "",
  },
  height: {
    type: String,
    default: "78px",
  },
  active: {
    type: Boolean,
    default: false,
  },
  disable: {
    type: Boolean,
    default: false,
  },
  showIcon: {
    type: Boolean,
    default: true,
  },
});

const highlightedName = computed(() => {
  if (!props.searchText) return props.title;
  const escapedSearchText = escapeRegExp(props.searchText);
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.title.replace(regex, '<span class="highlight">$1</span>');
});

const renderIcon = (code) => {
  if (code) {
    const objIcon = {
      FCTY000001: DemographicIcon,
      FCTY000002: UsageIcon,
      FCTY000003: DevicePlanIcon,
      FCTY000004: PaymentIcon,
      FCTY000005: BehavioralIcon,
      FCTY000006: MarketingIcon,
      FCTY000007: LocationIcon,
      FCTY000008: CustomerIcon,
      FCTY000009: ProductServiceIcon,
      FCTY000010: FeedbackIcon,
      FCTY000011: ContractIcon,
      FCTY000012: ServiceAvai,
      FCTY000013: CustomerEnga,
      FCTY000014: LocationBased,
    };
    return objIcon[code as string];
  }
  return null;
};
const ItemIcon = computed(() => {
  return renderIcon(props.typeCode);
});
const isHover = ref(false);
</script>
<style scoped>
.text-ellipsis {
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}
:deep() .highlight {
  background-color: yellow;
}
</style>
