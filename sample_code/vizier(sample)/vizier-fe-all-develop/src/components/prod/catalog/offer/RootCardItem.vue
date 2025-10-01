<template>
  <div>
    <v-card
      :ripple="false"
      class="custom-card !rounded-[12px] max-w-[320px]"
      :class="[isDetailView ? 'cursor-default' : 'cursor-pointer', classCard]"
      min-width="320"
      height="auto"
      @click="clickItem"
    >
      <div class="bg-[#BA1642] h-12 flex justify-center items-center">
        <div class="truncated-h1">
          <CustomTooltip :content="productName" :disabled="!productName">
            <span
              class="no-underline !text-white font-medium text-base"
              :class="!productName ? 'opacity-[72%]' : ''"
            >
              {{ productName }}
            </span>
          </CustomTooltip>
        </div>
      </div>
      <div>
        <div
          class="flex justify-between px-4 py-2 border-lightest border-b-[1px] leading-[19.5px]"
        >
          <span
            class="text-text-lighter font-medium font-size-base font-size-base"
            >{{ $t("product_platform.offerEntity.objCode") }}
          </span>
          <span
            class="text-text-base font-size-base font-medium font-size-base"
          >
            {{ productCode }}
          </span>
        </div>

        <template v-if="dinamicInfoCard.length">
          <div
            class="flex justify-between px-4 py-2 border-lightest border-b-[1px] leading-[19.5px] h-[36px]"
          >
            <span class="text-text-lighter font-medium font-size-base"
              ><span>{{
                dinamicInfoCard[0] ? $t(`${dinamicInfoCard[0]?.labelId}`) : ""
              }}</span></span
            >
            <span
              class="text-text-base font-size-base font-medium text-ellipsis"
            >
              <template
                v-if="
                  dinamicInfoCard[0]?.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                "
              >
                <CustomTooltip
                  :content="
                    dinamicInfoCard[0]?.attrVal
                      ? displayValueTypeDM(dinamicInfoCard[0])
                      : ''
                  "
                  location="bottom"
                />
              </template>
              <template
                v-else-if="
                  groupCodeData &&
                  dinamicInfoCard[0]?.fieldTypeCode === COLUMN_FIELD_TYPE.DL
                "
              >
                <CustomTooltip
                  :content="
                    dinamicInfoCard[0]?.attrVal
                      ? getTextDisplay(
                          dinamicInfoCard[0]?.attrVal,
                          dinamicInfoCard[0]?.fieldTypeCode,
                          groupCodeData[dinamicInfoCard[0]?.commGroupCode]
                        )
                      : ''
                  "
                  location="bottom"
                />
              </template>
              <template v-else>
                {{ dinamicInfoCard[0]?.attrVal }}
              </template></span
            >
          </div>
          <div
            class="flex justify-between px-4 py-2 border-lightest border-b-[1px] leading-[19.5px] h-[36px]"
          >
            <span class="text-text-lighter font-medium font-size-base"
              ><span>{{
                dinamicInfoCard[1] ? $t(`${dinamicInfoCard[1]?.labelId}`) : ""
              }}</span></span
            >
            <span
              class="text-text-base font-size-base font-medium text-ellipsis"
            >
              <template
                v-if="
                  dinamicInfoCard[1]?.fieldTypeCode === COLUMN_FIELD_TYPE.DM
                "
              >
                <CustomTooltip
                  :content="
                    dinamicInfoCard[1]?.attrVal
                      ? displayValueTypeDM(dinamicInfoCard[1])
                      : ''
                  "
                  location="bottom"
                />
              </template>
              <template
                v-else-if="
                  groupCodeData &&
                  dinamicInfoCard[1]?.fieldTypeCode === COLUMN_FIELD_TYPE.DL
                "
              >
                <CustomTooltip
                  :content="
                    dinamicInfoCard[1]?.attrVal
                      ? getTextDisplay(
                          dinamicInfoCard[1]?.attrVal,
                          dinamicInfoCard[1]?.fieldTypeCode,
                          groupCodeData[dinamicInfoCard[1]?.commGroupCode]
                        )
                      : ''
                  "
                  location="bottom"
                />
              </template>
              <template v-else>
                {{ dinamicInfoCard[1]?.attrVal }}
              </template></span
            >
          </div>
        </template>
        <template v-else>
          <div
            class="flex justify-between px-4 py-2 border-lightest border-b-[1px] leading-[19.5px] h-[36px]"
          >
            <span class="text-text-lighter font-medium font-size-base"></span>
            <div class="text-text-base font-size-base font-medium"></div>
          </div>
          <div
            class="flex justify-between px-4 py-2 border-lightest border-b-[1px] leading-[19.5px] h-[36px]"
          >
            <div class="text-text-lighter font-medium font-size-base"></div>
            <div class="text-text-base font-size-base font-medium"></div>
          </div>
        </template>

        <div class="px-4 py-2 text-[10px]">
          <div
            class="inline-flex items-center h[24px] py-[6px] px-[8px] rounded custom-time min-w-[88px]"
            :class="
              isWithinRange(dateStartEnd[0], dateStartEnd[1])
                ? 'background-success border-green'
                : dateStartEnd[0] || dateStartEnd[1]
                  ? 'background-error border-red'
                  : ''
            "
          >
            <div
              class="text-white flex flex-nowrap justify-center items-center w-[10px] h-[10px] rounded-full"
              :class="
                isWithinRange(dateStartEnd[0], dateStartEnd[1])
                  ? 'bg-success'
                  : dateStartEnd[0] || dateStartEnd[1]
                    ? 'bg-error'
                    : ''
              "
            >
              <v-icon
                v-if="dateStartEnd[0] || dateStartEnd[1]"
                size="6"
                color="white"
              >
                {{
                  isWithinRange(dateStartEnd[0], dateStartEnd[1])
                    ? "mdi-check"
                    : dateStartEnd[0] || dateStartEnd[1]
                      ? "mdi-close"
                      : ""
                }}
              </v-icon>
            </div>
            <span
              class="ml-[4px] leading-[10px]"
              :class="
                isWithinRange(dateStartEnd[0], dateStartEnd[1])
                  ? 'text-[#079455]'
                  : dateStartEnd[0] || dateStartEnd[1]
                    ? 'text-[#c7291d]'
                    : ''
              "
            >
              <template v-if="dateStartEnd[0] || dateStartEnd[1]">
                {{ formatDate(dateStartEnd[0]) }}
                {{ dateStartEnd[1] && ` ~ ${formatDate(dateStartEnd[1])}` }}
              </template>
            </span>
          </div>
        </div>
      </div>
    </v-card>
  </div>
</template>

<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { formatDate } from "@/utils/format-data";
import { STRUCTURE_ITEM_SCREEN } from "@/constants/offer";
import { RequiredYn } from "@/enums";
import {
  useCreateStructureStore,
  useDuplicateStructureStore,
  useStructureStore,
} from "@/store";
import { OFFER_TYPE } from "@/constants/index";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import { useGroupCode } from "@/composables/useGroupCode";

const props = defineProps({
  item: {
    type: Object,
    default: () => ({}),
  },
  codeCmcdList: {
    type: Object,
    default: () => ({}),
  },
  isDetailView: {
    type: Boolean,
    default: false,
  },
  classCard: {
    type: String,
    default: "",
  },
  screen: {
    type: String,
    default: STRUCTURE_ITEM_SCREEN.OFFER,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  duplicate: {
    type: Boolean,
    default: false,
  },
  type: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
});
const structureStore = useStructureStore();
const createStructureStore = useCreateStructureStore();
const duplicateStructureStore = useDuplicateStructureStore();
const { getTextDisplay, search, groupCodeData } = useGroupCode();

const selectedStore = computed(() => {
  if (props.isAdd) {
    return createStructureStore;
  } else if (props.duplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});
const emit = defineEmits(["view-detail"]);

const { t } = useI18n();
const { isDuplicate, selectedStructureData } = storeToRefs(selectedStore.value);

const productName = computed(() => {
  if (isDuplicate.value) {
    const objectName = selectedStructureData?.value?.general.find(
      (item: any) => item.colName === "obj_name"
    ).attrVal;

    if (objectName) {
      return objectName;
    } else {
      return t("product_platform.offerPage.offerName");
    }
  } else {
    if (!props.item?.objName) {
      return t("product_platform.offerPage.offerName");
    }
    return props.item?.objName;
  }
});

const handleTypeCmcd = async () => {
  let arr = props.item?.additional?.filter(
    (i) => i.dispCardYn === RequiredYn.Yes
  );
  const commonCodeList = arr?.reduce((codeArr, item) => {
    if (
      [COLUMN_FIELD_TYPE.DL, COLUMN_FIELD_TYPE.DM].includes(
        item.fieldTypeCode
      ) &&
      item?.commGroupCode
    ) {
      codeArr.push(item.commGroupCode);
    }
    return codeArr;
  }, []);
  if (commonCodeList?.length) {
    await search(commonCodeList);
  }
};

const displayValueTypeDM = (item) => {
  let arrs =
    JSON.parse(item?.attrVal)?.filter((value: any) => value.trim()) || [];
  if (arrs.length && groupCodeData.value) {
    let textFirstValue =
      groupCodeData.value[item.commGroupCode]?.find(
        (i) => i.cmcdDetlId === arrs[0]
      )?.cmcdDetlNm || "";
    return arrs.length > 1
      ? `${textFirstValue} (${t("product_platform.other")} ${arrs?.length - 1})`
      : textFirstValue;
  }

  return "";
};

const dateStartEnd = computed(() => {
  let itemsTypeDP = props.item?.additional?.filter(
    (i) => i.fieldTypeCode === COLUMN_FIELD_TYPE.DP
  );
  if (itemsTypeDP?.length) {
    return itemsTypeDP?.map((i) => i.attrVal);
  }
  return "";
});

const dinamicInfoCard = computed(() => {
  let itemsTypeDP = props.item?.additional?.filter(
    (i) => i.dispCardYn === RequiredYn.Yes
  );
  if (itemsTypeDP?.length) {
    return itemsTypeDP?.filter((i) => i.fieldTypeCode !== COLUMN_FIELD_TYPE.DP);
  }
  return [];
});

const productCode = computed(() => {
  if (isDuplicate.value || props.isAdd) {
    return props.item?.objCode
      ? props.item?.objCode
      : t("product_platform.auto_generation");
  }
  return props.item?.objCode;
});

watch(
  () => props.item,
  async (val) => {
    if (val) {
      await handleTypeCmcd();
    }
  },
  {
    deep: true,
    immediate: true,
  }
);

const isWithinRange = (startDate: any, endDate: any) => {
  if (!startDate && !endDate) {
    return false;
  }
  const startTimestamp = new Date(startDate).getTime();
  const currentTimestamp = new Date().getTime();
  const endTimestampValue = endDate ? new Date(endDate).getTime() : Infinity;
  return (
    startTimestamp <= currentTimestamp && currentTimestamp <= endTimestampValue
  );
};

const clickItem = () => {
  if (props.isDetailView) return;
  emit("view-detail");
};
</script>
<style scoped>
.item-info::before {
  list-style-type: circle;
  content: "";
  display: inline-block;
  width: 4px;
  height: 4px;
  background-color: #000;
  border-radius: 50%;
  margin-right: 10px;
  margin-left: 12px;
}

.truncated-h1 {
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: calc(100% - 75px);
  font-size: 13px;
  height: 24px;
  color: #fff !important;
  font-weight: 900;
}

:deep() .highlight {
  background-color: yellow;
}

.no-underline {
  text-decoration: none;
}

.custom-card {
  box-shadow: 0px 0px 24px 0px #0000001f;
}

.text-shadow-red {
  text-shadow: 1px 1px 1px #f55138;
}

.text-shadow-blue {
  text-shadow: 1px 1px 1px #1b7df5;
}

.three-dots:hover {
  box-shadow: 3px 3px 3px 0px rgba(211, 211, 211, 1);
}
.text-ellipsis {
  max-width: 80px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  cursor: pointer;
}
</style>
