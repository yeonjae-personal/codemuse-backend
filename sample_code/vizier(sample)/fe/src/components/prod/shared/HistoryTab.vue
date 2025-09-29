<template>
  <div
    v-if="historyStore.history"
    class="text-sm border-l-[1px] border-b-0 border-lighter ml-1.5 mr-2 relative text-text-base"
  >
    <!-- start created  -->
    <div class="history-line relative mb-8 ml-[22px]">
      <span class="time-line mb-4 font-size-base font-medium">{{
        createEventDisplay?.workDate
      }}</span>
      <div
        class="mt-4 border-[1px] rounded-lg"
        :class="
          selectedItem?.type === 'created'
            ? 'border-info-created'
            : 'border-lighter'
        "
      >
        <div
          class="flex h-12 cursor-pointer flex-nowrap items-center border-b-[1px]"
          :class="
            createEventDisplay?.toggle
              ? 'border-lightest'
              : 'border-b-transparent'
          "
          @click="
            () => {
              historyStore.setSelectedItem({
                type: 'created',
              });
            }
          "
        >
          <div
            class="bg-info-lighter h-full flex justify-center items-center px-3"
            :class="
              createEventDisplay?.toggle ? 'rounded-tl-lg' : 'rounded-l-lg'
            "
          >
            <PlusIcon />
          </div>
          <div
            class="font-size-base font-medium px-3 w-full flex justify-between items-center"
          >
            <div>{{ t("product_platform.historyTabs.created") }}</div>
            <div
              class="w-4 h-4"
              @click="
                (e) => {
                  e.stopPropagation();
                  if (createEventDisplay)
                    createEventDisplay.toggle = !createEventDisplay.toggle;
                }
              "
            >
              <ChevronDown
                size="18"
                class="transition duration-300 ease-out"
                :class="{ 'rotate-180': createEventDisplay?.toggle }"
              />
            </div>
          </div>
        </div>
        <div v-if="createEventDisplay?.toggle">
          <div class="px-4 py-2 font-size-base font-normal">
            <div class="py-1.5 flex gap-3">
              <div class="w-[45%] flex-shrink-0 text-text-lighter font-medium">
                {{ t("product_platform.chgDeptName") }}
              </div>
              <div class="tracking-[0.25px]">
                {{ createEventDisplay.chgDeptName }}
              </div>
            </div>

            <div class="py-1.5 flex gap-3">
              <div class="w-[45%] flex-shrink-0 text-text-lighter font-medium">
                {{ t("product_platform.chgPerson") }}
              </div>
              <div class="tracking-[0.25px]">
                {{ createEventDisplay.chgUser }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- end created  -->

    <!-- start changed  -->
    <div
      v-for="dailyChange in changesEventDisplay"
      :key="dailyChange.workDate"
      class="history-line relative mb-8 ml-[22px]"
    >
      <span class="time-line mb-4 font-size-base font-medium">{{
        dailyChange.workDate
      }}</span>
      <div
        v-for="(change, index) in dailyChange.records"
        :key="index"
        class="mt-4 border-[1px] rounded-lg"
        :class="isChangeSelected(change) ? 'border-warning' : 'border-lighter'"
      >
        <div
          class="flex h-12 cursor-pointer flex-nowrap items-center border-b-[1px]"
          :class="change.toggle ? 'border-lighter' : 'border-b-transparent'"
          @click="handleToggleChange(change)"
        >
          <div
            class="bg-warning-lighter h-full flex justify-center items-center px-3"
            :class="change.toggle ? 'rounded-tl-lg' : 'rounded-l-lg'"
          >
            <RefreshIcon />
          </div>
          <div
            class="font-size-base font-medium px-3 w-full flex justify-between items-center"
          >
            <div>{{ getChangeTypeName(change) }}</div>
            <div
              class="w-4 h-4"
              @click="
                () => {
                  change.toggle = !change.toggle;
                }
              "
            >
              <ChevronDown
                size="18"
                class="transition duration-300 ease-out"
                :class="{ 'rotate-180': change.toggle }"
              />
            </div>
          </div>
        </div>
        <div v-if="change.toggle">
          <div
            class="px-4 py-2 font-size-base font-normal border-b-[1px] border-lighter"
          >
            <div class="py-1.5 flex gap-3">
              <div class="w-[45%] flex-shrink-0 text-text-lighter font-medium">
                {{ t("product_platform.chgDeptName") }}
              </div>
              <div class="tracking-[0.25px]">
                {{ change.chgDeptName }}
              </div>
            </div>
            <div class="py-1.5 flex gap-3">
              <div class="w-[45%] flex-shrink-0 text-text-lighter font-medium">
                {{ t("product_platform.chgPerson") }}
              </div>
              <div class="tracking-[0.25px]">{{ change.chgUser }}</div>
            </div>
          </div>
          <div class="px-4 text-text-base font-size-base pt-3 pb-4 font-normal">
            <span class="font-medium">{{
              t("product_platform.historyTabs.details")
            }}</span>

            <template v-if="change.changeTypeName === 'Structure'">
              <div
                v-for="field in change.structures"
                :key="field.workNo"
                class="py-1.5 flex gap-3"
              >
                <div
                  class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                >
                  {{ findLabel(field.workTypeCode) }}
                </div>
                <div class="flex items-center flex-wrap space-x-2">
                  <span>{{ field.mctgrItemName }}</span>
                  <ChevronRightIcon fill="#3A3B3D" size="14" class="mx-2" />
                  <span>{{ field.itemCodeName }}</span>
                  <ChevronRightIcon fill="#3A3B3D" size="14" class="mx-2" />
                  <span>{{ field.objName }}</span>
                </div>
              </div>
            </template>

            <template v-else>
              <div
                v-for="field in change.fields"
                :key="field.workNo"
                class="py-1.5 flex gap-3"
              >
                <div
                  class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                >
                  {{ getLabelMultiLang(field.labelId) }}
                </div>
                <div
                  v-if="field.commGroupCode"
                  class="tracking-[0.25px] flex flex-wrap items-center space-x-2 text-text-lighter font-medium"
                >
                  <span class="break-all">{{
                    getCmcdDetlNm(
                      field.commGroupCode,
                      field.beforeValue,
                      field.fieldTypeCode || ""
                    )
                  }}</span>
                  <span class="flex items-center justify-center">
                    <ArrowNarrowRightIcon />
                  </span>
                  <span class="break-all">{{
                    getCmcdDetlNm(
                      field.commGroupCode,
                      field.afterValue,
                      field.fieldTypeCode || ""
                    )
                  }}</span>
                </div>
                <div
                  v-else
                  class="tracking-[0.25px] flex flex-wrap items-center space-x-2 text-text-lighter font-medium"
                >
                  <span class="break-all">{{ field.beforeValue }}</span>
                  <span class="flex items-center justify-center">
                    <ArrowNarrowRightIcon />
                  </span>
                  <span class="break-all">{{ field.afterValue }}</span>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>
    <!-- end changed  -->

    <!-- start ended  -->
    <div
      v-if="endEventDisplay !== null"
      class="history-line relative mb-8 ml-[22px]"
    >
      <span class="time-line mb-4 font-size-base font-medium">{{
        endEventDisplay?.workDate
      }}</span>
      <div
        class="mt-4 border-[1px] rounded-lg"
        :class="
          selectedItem?.type === 'ended'
            ? 'border-lighter-fade-out'
            : 'border-lighter'
        "
      >
        <div
          class="flex h-12 cursor-pointer flex-nowrap items-center border-lightest border-b-[1px]"
          :class="
            endEventDisplay?.toggle ? 'border-lightest' : 'border-b-transparent'
          "
          @click="
            () => {
              historyStore.setSelectedItem({
                type: 'ended',
              });
            }
          "
        >
          <div
            class="bg-darker h-full flex justify-center items-center px-3"
            :class="endEventDisplay?.toggle ? 'rounded-tl-lg' : 'rounded-l-lg'"
          >
            <HourGlass />
          </div>
          <div
            class="font-size-base font-medium px-3 w-full flex justify-between items-center"
          >
            <div>{{ t("product_platform.historyTabs.ended") }}</div>
            <div
              class="w-4 h-4"
              @click="
                (e) => {
                  e.stopPropagation();
                  if (endEventDisplay)
                    endEventDisplay.toggle = !endEventDisplay.toggle;
                }
              "
            >
              <ChevronDown
                size="18"
                class="transition duration-300 ease-out"
                :class="{ 'rotate-180': endEventDisplay?.toggle }"
              />
            </div>
          </div>
        </div>
        <div v-if="endEventDisplay?.toggle">
          <div class="px-4 py-2 font-size-base font-normal">
            <div class="py-1.5 flex gap-3">
              <div class="w-[45%] flex-shrink-0 text-text-lighter font-medium">
                {{ t("product_platform.chgDeptName") }}
              </div>
              <div class="tracking-[0.25px]">
                {{ endEventDisplay.chgDeptName }}
              </div>
            </div>

            <div class="py-1.5 flex gap-3">
              <div class="w-[45%] flex-shrink-0 text-text-lighter font-medium">
                {{ t("product_platform.chgPerson") }}
              </div>
              <div class="tracking-[0.25px]">
                {{ endEventDisplay.chgUser }}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- end ended  -->
  </div>
  <div v-else class="h-full w-full flex justify-center items-center">
    <NoData />
  </div>
</template>
<script setup lang="ts">
import { Change } from "@/interfaces/prod/HistoryTab";
import isEqual from "lodash-es/isEqual";
import { useHistoryTabStore } from "@/store";
import { useGroupCode } from "@/composables/useGroupCode";
import { useI18n } from "vue-i18n";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
const { t } = useI18n();

const props = defineProps({
  type: {
    type: String,
    default: "offer",
  },
});

const { groupCodeData, search } = useGroupCode();
const historyStore = useHistoryTabStore();
const { selectedItem, filterNullCommGroupCodes } = storeToRefs(historyStore);

const createEventDisplay = computed(() => {
  return historyStore.history?.created;
});

const changesEventDisplay = computed(() => {
  return historyStore.history?.changed;
});

const endEventDisplay = computed(() => {
  return historyStore.history?.ended;
});

const handleToggleChange = (item: Change) => {
  historyStore.setSelectedItem({
    type: "changed",
    change: item,
  });
};

const getChangeTypeName = (change: Change): string => {
  return change.changeTypeName === "Structure"
    ? t("product_platform.historyTabs.changedStructure")
    : t("product_platform.historyTabs.changedAttribute");
};

const isChangeSelected = (item: Change): boolean => {
  return isEqual(item, selectedItem.value?.change);
};

const findLabel = (workTypeCode: string) => {
  try {
    if (workTypeCode) {
      const commonCode = groupCodeData.value["G00044"].find(
        (item: any) => item.cmcdDetlId === workTypeCode
      );
      return commonCode.cmcdDetlNm;
    }
    return "";
  } catch (error) {
    console.error(
      "workTypeCode: " + workTypeCode + " is not found in groupCodeData"
    );
    return "";
  }
};

const getLabelMultiLang = (labelId: string) => {
  let result;
  switch (props.type) {
    case "component":
      result = t(`product_platform.componentEntity.${labelId}`);
      break;
    case "resource":
      result = t(`product_platform.resourceEntity.${labelId}`);
      break;
    case "group":
      result = t(`product_platform.groupEntity.${labelId}`);
      break;
    default:
      result = t(`product_platform.offerEntity.${labelId}`);
  }
  if (result.includes("product_platform.")) {
    result = t(labelId);
  }

  if (labelId === "ctgrNodeUuid") {
    return t("product_platform.categoryNode");
  }
  if (labelId === "ovwCntn") {
    return t("product_platform.overview");
  }

  return result;
};

const getCmcdDetlNm = (
  cmcdGrpId: string,
  cmcdDetlId: string,
  fieldTypeCode: string
): string | undefined => {
  const group: any[] | undefined = groupCodeData.value[cmcdGrpId as string];
  if (!group) return undefined;
  const item: any | undefined = group.find(
    (item) => item.cmcdDetlId === cmcdDetlId
  );

  if (fieldTypeCode === COLUMN_FIELD_TYPE.DM) {
    const mapValue = JSON.parse(cmcdDetlId);
    const newArray = [] as any;
    if (mapValue && mapValue.length > 0) {
      mapValue.forEach((element: any) => {
        const itemFound = group.find((value) => value.cmcdDetlId === element);
        if (itemFound) {
          newArray.push(itemFound.cmcdDetlNm);
        }
      });
      return newArray.length > 0 ? `${newArray.join(", ")}` : undefined;
    } else {
      return undefined;
    }
  } else {
    return item ? item.cmcdDetlNm : undefined;
  }
};

onMounted(() => {
  try {
    search(filterNullCommGroupCodes.value);
  } catch (error) {
    console.error("Error in onMounted: " + error);
  }
});
</script>
<style scoped>
.history-line::before {
  content: "";
  position: absolute;
  width: 12px;
  height: 12px;
  left: -28px;
  background-color: white;
  border: 2px solid #e6e9ed;
  top: 6px;
  border-radius: 50%;
}
.border-b-transparent {
  border-bottom-color: transparent;
}
</style>
