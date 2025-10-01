<template>
  <div
    class="w-[40px] h-[40px] ml-[0px] flex justify-center items-center text-[13px] font-black bg-white rounded-lg flex-shrink-0 shadow-md"
  >
    <device-icon v-if="props.typeOfProd === OFFER_TYPE.DEVICE" />
    <span
      v-else
      class="leading-4 text-[17px] font-bold"
      :class="getProductByType(props.typeOfProd).class"
      >{{
        getProductByType(props.typeOfProd).icon === "defaultIcon"
          ? typeOfProd.charAt(0)
          : getProductByType(props.typeOfProd).icon
      }}</span
    >
  </div>
</template>
<script setup lang="ts">
import { OFFER_TYPE } from "@/constants/index";

const props = defineProps({
  typeOfProd: {
    type: String || null,
    default: () => null,
  },
});

const getProductByType = (type: string) => {
  const productTypes = {
    [OFFER_TYPE.PRICEPLAN]: { class: "text-[#EB7A3D]", icon: "P" },
    [OFFER_TYPE.ADDON]: { class: "text-[#9947D3]", icon: "A" },
    [OFFER_TYPE.DISCOUNT]: { class: "text-[#23B27F]", icon: "D" },
    [OFFER_TYPE.RENTAL]: { class: "text-[#666666]", icon: "R" },
    default: { class: "text-[#666666]", icon: "defaultIcon" }, // Default case when none of the above matches
  };

  return productTypes[type as string] || productTypes.default;
};
</script>
<style scoped></style>
