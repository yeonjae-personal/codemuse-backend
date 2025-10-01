<template>
  <div
    ref="cardItemRef"
    class="rounded-[12px] relative zoom-animation"
    :class="[!expired && 'shadow-card']"
    :draggable="true"
    @dragstart="handleDragStart"
    @dragend="handleDragEnd"
    @drag="handleDrag"
  >
    <v-card
      :ripple="false"
      class="mx-auto rounded-[12px] text-[13px] leading-[19.5px] relative !z-20 border-[2px]"
      :class="[
        isActive
          ? `!border-[${BORDER_CONFIG.ACTIVE}]`
          : !expired
            ? '!border-[#fff]'
            : '!border-[#f0f2f5]',
        expired ? 'v-custom-card-expired' : 'v-custom-card',
        'cursor-pointer',
        classCard,
      ]"
      :width="width"
      height="auto"
      @click="clickItem"
    >
      <div
        class="flex items-center mt-3 mx-3 max-h-10"
        :class="[!isShowInfo ? 'mb-3' : '']"
      >
        <div :class="[expired ? 'opacity-[32%]' : '']">
          <TypeOfProd :type-of-prod="offerType" />
        </div>
        <div
          class="w-full max-w-[calc(100%-60px)]"
          :class="[expired ? 'opacity-[32%]' : '']"
        >
          <div class="truncated-h1 ml-2 text-text-base">
            <CustomTooltip :content="item.objName">
              <span
                class="no-underline text-[13px] leading-7 font-medium text-ellipsis"
                v-html="highlightedName"
              />
            </CustomTooltip>
            <br />
          </div>
          <div class="item-info text-[11px] ml-2">
            <div class="inline-flex items-center">
              <template v-if="dateStartEnd[0] || dateStartEnd[1]">
                <div
                  class="text-white flex flex-nowrap justify-center items-center w-[11px] h-[11px] rounded-full"
                  :class="
                    isWithinRange(dateStartEnd[0], dateStartEnd[1])
                      ? 'bg-success'
                      : 'bg-error'
                  "
                >
                  <v-icon size="6" color="white">
                    {{
                      isWithinRange(dateStartEnd[0], dateStartEnd[1])
                        ? "mdi-check"
                        : "mdi-close"
                    }}
                  </v-icon>
                </div>
                <span
                  class="ml-[4px] leading-[9px]"
                  :class="
                    isWithinRange(dateStartEnd[0], dateStartEnd[1])
                      ? 'text-text-success'
                      : 'text-text-error'
                  "
                >
                  {{ formatDate(dateStartEnd[0]) }}
                  {{ dateStartEnd[1] && ` ~ ${formatDate(dateStartEnd[1])}` }}
                </span>
              </template>
            </div>
          </div>
        </div>
        <ChevronDown
          v-if="isDetailView"
          size="20"
          class="transition duration-300 ease-out flex-initial"
          :class="{ 'rotate-180': isShowInfo }"
          @click.stop="handleShowInfo"
        />
        <base-popover
          v-if="isShowActions"
          :options="actions"
          custom-location="bottom-left"
          @open-options="emit('open-options')"
        >
          <template #activator>
            <div :class="[expired ? 'opacity-[32%]' : '']">
              <DotsVerticalIcon />
            </div>
          </template>
        </base-popover>
      </div>
      <div
        v-if="
          (!props.isDetailView && props.isShowDetailInfor) ||
          (props.isDetailView && isShowInfo && props.isShowDetailInfor)
        "
        :class="[expired ? '!opacity-[32%]' : '']"
        class="info w-full px-3 my-3 transition duration-300 ease-out flex-initial"
      >
        <div
          class="h-[34px] w-full my-[7px] item-info flex justify-between items-center px-3 font-medium text-text-base font-size-base text-lighter bg-gradient-to-b from-white via-white/64 to-white/80 rounded-lg shadow-sm"
        >
          {{ $t("product_platform.offerEntity.objCode") }} &nbsp;<span
            class="font-size-base text-text-base"
            v-html="highlightedCode"
          >
          </span>
        </div>
        <div
          class="h-[34px] my-[7px] w-full item-info flex justify-between items-center px-3 font-medium text-text-base font-size-base text-lighter bg-gradient-to-b from-white via-white/64 to-white/80 rounded-lg shadow-sm"
        >
          <span>
            {{
              dinamicInfoCard[0]?.labelId &&
              $t(`${dinamicInfoCard[0]?.labelId}`)
            }}
          </span>
          <div class="font-size-base text-text-base text-ellipsis max-w-[80px]">
            <template
              v-if="dinamicInfoCard[0]?.fieldTypeCode === COLUMN_FIELD_TYPE.DM"
            >
              <CustomTooltip
                :content="
                  dinamicInfoCard[0]?.attrVal
                    ? displayValueTypeDM(dinamicInfoCard[0])
                    : ''
                "
                location="bottom"
                is-inline
              />
            </template>
            <template
              v-else-if="
                codeCmcdList &&
                dinamicInfoCard[0]?.fieldTypeCode === COLUMN_FIELD_TYPE.DL
              "
            >
              <CustomTooltip
                :content="
                  dinamicInfoCard[0]?.attrVal
                    ? getTextDisplay(
                        dinamicInfoCard[0]?.attrVal,
                        dinamicInfoCard[0]?.fieldTypeCode,
                        codeCmcdList[dinamicInfoCard[0]?.commGroupCode]
                      )
                    : ''
                "
                location="bottom"
                is-inline
              />
            </template>
            <template v-else>
              {{ dinamicInfoCard[0]?.attrVal }}
            </template>
          </div>
        </div>

        <div
          class="h-[34px] w-full my-[7px] item-info flex justify-between items-center px-3 font-medium text-text-base font-size-base text-lighter bg-gradient-to-b from-white via-white/64 to-white/80 rounded-lg shadow-sm"
        >
          <span>
            {{
              dinamicInfoCard[1]?.labelId &&
              $t(`${dinamicInfoCard[1]?.labelId}`)
            }}
          </span>
          <div class="font-size-base text-text-base text-ellipsis max-w-[80px]">
            <template
              v-if="dinamicInfoCard[1]?.fieldTypeCode === COLUMN_FIELD_TYPE.DM"
            >
              <CustomTooltip
                :content="
                  dinamicInfoCard[1]?.attrVal
                    ? displayValueTypeDM(dinamicInfoCard[1])
                    : ''
                "
                location="bottom"
                is-inline
              />
            </template>
            <template
              v-else-if="
                codeCmcdList &&
                dinamicInfoCard[1]?.fieldTypeCode === COLUMN_FIELD_TYPE.DL
              "
            >
              <CustomTooltip
                :content="
                  dinamicInfoCard[1]?.attrVal
                    ? getTextDisplay(
                        dinamicInfoCard[1]?.attrVal,
                        dinamicInfoCard[1]?.fieldTypeCode,
                        codeCmcdList[dinamicInfoCard[1]?.commGroupCode]
                      )
                    : ''
                "
                location="bottom"
                is-inline
              />
            </template>
            <template v-else>
              {{ dinamicInfoCard[1]?.attrVal }}
            </template>
          </div>
        </div>
      </div>
      <div class="absolute top-0 left-0 z-1">
        <!-- <BlurBlueHuge /> -->
      </div>
      <div class="absolute top-0 right-0 z-1">
        <!-- <BlurPurple /> -->
      </div>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { escapeRegExp } from "@/utils/format-data";
import { formatDate } from "@/utils/format-data";
import { useGroupCode } from "@/composables/useGroupCode";
import useDragUserPocket from "@/composables/useDragUserPocket";
import { BORDER_CONFIG, OFFER_TYPE } from "@/constants/";
import TypeOfProd from "@/components/prod/catalog/offer/TypeOfProd.vue";
import ChevronDown from "@/components/prod/icons/ChevronDown.vue";
// import BlurPurple from "@/components/prod/icons/BlurPurple.vue";
import { RequiredYn } from "@/enums";
import DotsVerticalIcon from "@/components/prod/icons/DotsVerticalIcon.vue";
import { ActionType } from "@/interfaces/prod";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { LARGE_ITEM_CODE } from "@/store/userPocket.store";

const props = defineProps({
  item: {
    type: Object,
    default: () => ({}),
  },
  codeCmcdList: {
    type: Object as PropType<any>,
    default: () => ({}),
  },
  searchText: {
    type: String,
    default: "",
  },
  searchField: {
    type: String,
    default: "name",
  },
  isDetailView: {
    type: Boolean,
    default: false,
  },
  classCard: {
    type: String,
    default: "",
  },
  closeInfo: {
    type: Boolean,
    default: false,
  },
  expired: {
    type: Boolean,
    default: false,
  },
  offerType: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
  actions: {
    type: Array as PropType<ActionType[]>,
    default: () => [],
  },
  isShowActions: {
    type: Boolean,
    default: false,
  },
  isShowDetailInfor: {
    type: Boolean,
    default: true,
  },
  isDuplicate: {
    type: Boolean,
    default: false,
  },
  selectedProduct: {
    type: Object,
    default: () => {},
  },
  width: {
    type: [String, Number],
    default: "100%",
  },
});

const emit = defineEmits(["view-detail", "open-options"]);

const { t } = useI18n();

const { getTextDisplay } = useGroupCode();
const { handleDragUserPocket } = useDragUserPocket();

const isShowInfo = ref(false);
const cardItemRef = ref<HTMLDivElement | null>(null);

const highlightedName = computed(() => {
  if (!props.searchText || props.searchField != "name")
    return props.item["objName"];
  const escapedSearchText = escapeRegExp(props.searchText);
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.item["objName"].replace(
    regex,
    '<span class="highlight">$1</span>'
  );
});
const highlightedCode = computed(() => {
  if (!props.searchText || props.searchField != "code")
    return props.item["objCode"];
  const escapedSearchText = escapeRegExp(props.searchText);
  // eslint-disable-next-line security/detect-non-literal-regexp
  const regex = new RegExp(`(${escapedSearchText})`, "gi");
  return props.item["objCode"].replace(
    regex,
    '<span class="highlight">$1</span>'
  );
});

const dateStartEnd = computed(() => {
  let itemsTypeDP = props.item?.additional?.filter(
    (i) => i.fieldTypeCode === COLUMN_FIELD_TYPE.DP
  );
  if (itemsTypeDP?.length) {
    return itemsTypeDP.map((i) =>
      i.dispCardYn === RequiredYn.Yes ? i.attrVal : ""
    );
  }
  return [];
});

const dinamicInfoCard = computed(() => {
  let itemsTypeDP = props.item?.additional?.filter(
    (i) => i.dispCardYn === RequiredYn.Yes
  );
  if (itemsTypeDP?.length) {
    return itemsTypeDP.filter((i) => i.fieldTypeCode !== COLUMN_FIELD_TYPE.DP);
  }
  return [];
});

const isActive = computed(() => {
  return (
    (props.isDuplicate && props.item.duplicateItem) ||
    (!props.isDuplicate &&
      props.item.objUUID === props.selectedProduct?.objUUID)
  );
});

const isWithinRange = (startDate: any, endDate: any) => {
  const startTimestamp = new Date(startDate).getTime();
  const currentTimestamp = new Date().getTime();
  const endTimestampValue = endDate ? new Date(endDate).getTime() : Infinity;
  return (
    startTimestamp <= currentTimestamp && currentTimestamp < endTimestampValue
  );
};

const clickItem = () => {
  if (props.isDetailView) {
    isShowInfo.value = true;
  }
  emit("view-detail", props.item);
};

const handleShowInfo = () => {
  isShowInfo.value = !isShowInfo.value;
};

const displayValueTypeDM = (item) => {
  let arrs =
    JSON.parse(item?.attrVal)?.filter((value: any) => value.trim()) || [];
  if (arrs.length && props.codeCmcdList) {
    let textFirstValue =
      props.codeCmcdList[item.commGroupCode]?.find(
        (i) => i.cmcdDetlId === arrs[0]
      )?.cmcdDetlNm || "";
    return arrs.length > 1
      ? `${textFirstValue} (${t("product_platform.other")} ${arrs?.length - 1})`
      : textFirstValue;
  }

  return "";
};

const handleDragStart = (event: any): void => {
  const elementRect = cardItemRef.value?.getBoundingClientRect();
  event.target.classList.remove("zoom-animation");
  const hiddenDragImg = (event.target as HTMLElement).cloneNode(
    true
  ) as HTMLElement;
  hiddenDragImg.id = "hiddenDragImg";
  hiddenDragImg.style.opacity = "0";
  const dragImg = (event.target as HTMLElement).cloneNode(true) as HTMLElement;
  dragImg.id = "dragImg";
  dragImg.style.position = "absolute";
  dragImg.style.width = `${
    typeof props.width === "number" ? props.width : elementRect?.width
  }px`;
  dragImg.style.zIndex = "1000";
  document.body.appendChild(hiddenDragImg);
  document.body.appendChild(dragImg);
  event.dataTransfer?.setDragImage(hiddenDragImg, 0, 0);
  handleDragUserPocket(event, {
    userPocketType: LARGE_ITEM_CODE.OFFER,
    ...props.item,
  });
};

const handleDrag = (event: DragEvent): void => {
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  if (dragImage) {
    dragImage.style.left = `${event.clientX}px`;
    dragImage.style.top = `${event.clientY}px`;
  }
};

const handleDragEnd = (event: any): void => {
  event.target.classList.add("zoom-animation");
  const hiddenDragImg = document.getElementById("hiddenDragImg") as HTMLElement;
  const dragImage = document.getElementById("dragImg") as HTMLElement;
  dragImage?.remove();
  hiddenDragImg?.remove();
};

watch(
  () => props.isDetailView,
  (val) => {
    if (val && isActive.value) {
      isShowInfo.value = true;
    } else {
      isShowInfo.value = false;
    }
  }
);

watch(
  () => props.closeInfo,
  () => {
    isShowInfo.value = false;
  }
);

onMounted(() => {
  if (props.isDetailView && isActive.value) {
    isShowInfo.value = true;
  } else {
    isShowInfo.value = false;
  }
});
</script>

<style scoped>
.truncated-h1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: calc(100% - 14px);
  font-size: 16px;
  height: 24px;
}

:deep() .highlight {
  background-color: yellow;
}

.no-underline {
  text-decoration: none;
}

:deep() .v-card--variant-elevated {
  box-shadow: unset !important;
  border-radius: 12px !important;
}

.v-custom-card {
  background: #ffffff;
  box-shadow:
    1px 1px 12px 0px #0000001f,
    -2px -2px 24px 0px #0000000a inset !important;
  cursor: unset;
}

.v-custom-card-expired {
  background: #f0f2f5;
  box-shadow:
    1px 1px 12px 0px #0000001f,
    box-shadow -2px -2px 24px 0px #0000000a inset !important;
}

:deep() .v-card:hover > .v-card__overlay {
  opacity: 0;
}

.selected-card {
  border: 2px solid #fdced5;
}
</style>
