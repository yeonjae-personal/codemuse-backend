<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="history-conatainer p-2 bg-white h-full rounded-[12px] relative">
    <div
      class="text-text-lighter mb-[27px] ml-[22px] mt-5 font-medium text-[15px]"
    >
      {{ $t("product_platform.customValidationHistory") }}
    </div>

    <div class="text-sm">
      <div
        v-if="historyStore.history"
        class="text-sm ml-1.5 mr-2 relative text-text-base"
      >
        <LocomotiveComponent
          scroll-content-class=" border-l-[1px] border-b-0 border-lighter text-text-base"
          scroll-container-class="!max-h-[calc(100vh_-_230px)]"
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
                    createEventDisplay?.toggle
                      ? 'rounded-tl-lg'
                      : 'rounded-l-lg'
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
                          createEventDisplay.toggle =
                            !createEventDisplay.toggle;
                      }
                    "
                  >
                    <ChevronDown
                      size="20"
                      class="transition duration-300 ease-out"
                      :class="{ 'rotate-180': createEventDisplay?.toggle }"
                    />
                  </div>
                </div>
              </div>
              <div v-if="createEventDisplay?.toggle">
                <div class="px-4 py-2 font-size-base font-normal">
                  <div class="py-1.5 flex gap-3">
                    <div
                      class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                    >
                      {{ $t("product_platform.chgDeptName") }}
                    </div>
                    <div class="tracking-[0.25px]">
                      {{ createEventDisplay.chgDeptName }}
                    </div>
                  </div>

                  <div class="py-1.5 flex gap-3">
                    <div
                      class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                    >
                      {{ $t("product_platform.chgPerson") }}
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
              :class="
                isChangeSelected(change) ? 'border-warning' : 'border-lighter'
              "
            >
              <div
                class="flex h-12 cursor-pointer flex-nowrap items-center border-b-[1px]"
                :class="
                  change.toggle ? 'border-lighter' : 'border-b-transparent'
                "
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
                      size="20"
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
                    <div
                      class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                    >
                      {{ $t("product_platform.chgDeptName") }}
                    </div>
                    <div class="tracking-[0.25px]">
                      {{ change.chgDeptName }}
                    </div>
                  </div>
                  <div class="py-1.5 flex gap-3">
                    <div
                      class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                    >
                      {{ $t("product_platform.chgPerson") }}
                    </div>
                    <div class="tracking-[0.25px]">{{ change.chgUser }}</div>
                  </div>
                </div>
                <div
                  class="px-4 text-text-base font-size-base pt-3 pb-4 font-normal"
                >
                  <span
                    v-if="
                      change.attributes?.filter((item) => item.condType === 'C')
                        .length
                    "
                    class="font-medium"
                    >{{ t("product_platform.condition") }}</span
                  >

                  <template v-if="change.changeTypeName === 'Attribute'">
                    <div
                      v-for="field in change.attributes?.filter(
                        (item) => item.condType === 'C'
                      )"
                      :key="field.workNo"
                      class="py-1.5 flex gap-3"
                    >
                      <div
                        class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                      >
                        {{ t(field.workTypeCode) }}
                      </div>
                      <div class="flex items-center flex-wrap space-x-2">
                        <span>{{ $t(`${field.labelId}`) }}</span>
                        <!-- <ChevronRightIcon class="mx-2" />
                        <span>{{ field.itemCodeName }}</span>
                        <ChevronRightIcon class="mx-2" />
                        <span>{{ field.objName }}</span> -->
                      </div>
                    </div>
                  </template>

                  <span
                    v-if="
                      change.attributes?.filter((item) => item.condType === 'A')
                        .length
                    "
                    class="font-medium"
                    >{{ t("product_platform.action") }}</span
                  >

                  <template v-if="change.changeTypeName === 'Attribute'">
                    <div
                      v-for="field in change.attributes?.filter(
                        (item) => item.condType === 'A'
                      )"
                      :key="field.workNo"
                      class="py-1.5 flex gap-3"
                    >
                      <div
                        class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                      >
                        {{ t(field.workTypeCode) }}
                      </div>
                      <div class="flex items-center flex-wrap space-x-2">
                        <span>{{ field?.itemCodeName || "" }}</span>
                        <span class="flex items-center justify-center">
                          <ArrowNarrowRightIcon /> </span
                        ><span>{{ $t(`${field.labelId}`) }}</span>
                        <!-- <ChevronRightIcon class="mx-2" />
                        <span>{{ field.itemCodeName }}</span>
                        <ChevronRightIcon class="mx-2" />
                        <span>{{ field.objName }}</span> -->
                      </div>
                    </div>
                  </template>

                  <template v-if="change.changeTypeName === 'Value'">
                    <span
                      v-if="
                        change.values?.filter((item) => item.condType === 'C')
                          .length
                      "
                      class="font-medium"
                      >{{ t("product_platform.condition") }}</span
                    >
                    <div
                      v-for="field in change.values?.filter(
                        (item) => item.condType === 'C'
                      )"
                      :key="field.workNo"
                      class="py-1.5 flex gap-3"
                    >
                      <div
                        class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                      >
                        {{ $t(`${field.labelId}`) }}
                      </div>
                      <div
                        class="field-value tracking-[0.25px] flex flex-wrap items-center gap-x-2 gap-y-1 text-text-lighter font-medium"
                      >
                        <span>{{ field.beforeValue }}</span>
                        <span class="flex items-center justify-center">
                          <ArrowNarrowRightIcon />
                        </span>
                        <span>{{ field.afterValue }}</span>
                      </div>
                    </div>

                    <span
                      v-if="
                        change.values?.filter((item) => item.condType === 'A')
                          .length
                      "
                      class="font-medium"
                      >{{ t("product_platform.action") }}</span
                    >
                    <div
                      v-for="field in change.values?.filter(
                        (item) => item.condType === 'A'
                      )"
                      :key="field.workNo"
                      class="py-1.5 flex gap-3"
                    >
                      <div
                        class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                      >
                        {{ $t(`${field.labelId}`) }}
                      </div>
                      <div
                        class="field-value tracking-[0.25px] flex flex-wrap items-center gap-x-2 gap-y-1 text-text-lighter font-medium"
                      >
                        <span>{{ field.beforeValue }}</span>
                        <span class="flex items-center justify-center">
                          <ArrowNarrowRightIcon />
                        </span>
                        <span>{{ field.afterValue }}</span>
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
                  endEventDisplay?.toggle
                    ? 'border-lightest'
                    : 'border-b-transparent'
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
                  :class="
                    endEventDisplay?.toggle ? 'rounded-tl-lg' : 'rounded-l-lg'
                  "
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
                      size="20"
                      class="transition duration-300 ease-out"
                      :class="{ 'rotate-180': endEventDisplay?.toggle }"
                    />
                  </div>
                </div>
              </div>
              <div v-if="endEventDisplay?.toggle">
                <div class="px-4 py-2 font-size-base font-normal">
                  <div class="py-1.5 flex gap-3">
                    <div
                      class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                    >
                      {{ $t("product_platform.chgDeptName") }}
                    </div>
                    <div class="tracking-[0.25px]">
                      {{ endEventDisplay.chgDeptName }}
                    </div>
                  </div>

                  <div class="py-1.5 flex gap-3">
                    <div
                      class="w-[45%] flex-shrink-0 text-text-lighter font-medium"
                    >
                      {{ $t("product_platform.chgPerson") }}
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
        </LocomotiveComponent>
      </div>
      <ShowDetailIcon
        class="absolute top-[160px] left-0 cursor-pointer text-[#525457] hover:text-[#303132]"
        @click="closeStructureDetailPane()"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
import { useGroupCode } from "@/composables/useGroupCode";
import { Change } from "@/interfaces/prod/HistoryCustomValidation";
import customValidationStore from "@/store/admin/customValidation.store";
import useHistoryCustomValidationStore from "@/store/admin/historyCustomValidation.store";
import isEqual from "lodash-es/isEqual";

import { useI18n } from "vue-i18n";
const { t } = useI18n();

const props = defineProps({
  validCode: {
    type: Object as PropType<any>,
    default: null,
  },
});

const { search } = useGroupCode();
const { showHistory } = storeToRefs(customValidationStore());
const historyStore = useHistoryCustomValidationStore();
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
  return change.changeTypeName === "Attribute"
    ? t("product_platform.historyTabs.changedAttribute")
    : t("product_platform.historyTabs.changedValue");
};

const isChangeSelected = (item: Change): boolean => {
  return isEqual(item, selectedItem.value?.change);
};

onMounted(() => {
  try {
    search(filterNullCommGroupCodes.value);
  } catch (error) {
    console.error("Error in onMounted: " + error);
  }
  historyStore.fetchHistory({
    validCode: props.validCode?.id,
  });
});

watch(
  () => props.validCode,
  () => {
    historyStore.fetchHistory({
      validCode: props.validCode?.id,
    });
  }
);

const closeStructureDetailPane = () => {
  showHistory.value = false;
};
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
  top: 4px;
  border-radius: 50%;
}
.base-color {
  color: #6b6d70;
}

.field-value {
  word-break: break-word;
}
</style>
