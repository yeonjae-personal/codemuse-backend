<template>
  <BasePopup
    v-model="isOpenPopup"
    :size="DialogSizeType.Medium"
    title="Summary"
    @on-close="handleResetPopup"
  >
    <template #body>
      <div class="p-6 pb-0">
        <div
          class="summary-body-header d-flex justify-between items-center gap-5"
        >
          <div class="summary-body-header-item">
            <span class="summary-body-header-item__title">File Name:</span>
            <span
              class="text-ellipsis summary-body-header-item__name max-w-[400px]"
            >
              {{ fileName }}
            </span>
          </div>
          <div class="summary-body-header-item">
            <span class="summary-body-header-item__title"> Duration:</span>
            <span class="summary-body-header-item__name">
              {{ executionTime }}
            </span>
          </div>
        </div>
        <div class="summary-body-analysis my-3">
          <div class="summary-body-analysis-item">
            <div class="summary-body-analysis-item__title">Entity Count</div>
            <div class="summary-body-analysis-item__value">
              {{ totalItems }}
            </div>
          </div>
          <div class="summary-body-analysis-item">
            <div class="summary-body-analysis-item__title">Success</div>
            <div class="summary-body-analysis-item__value">
              {{ successItems }}
            </div>
          </div>
          <div class="summary-body-analysis-item">
            <div class="summary-body-analysis-item__title">Fail</div>
            <div class="summary-body-analysis-item__value is-error">
              {{ failItems }}
            </div>
          </div>
        </div>
        <keep-alive>
          <component
            :is="currentComponent"
            :data="listItems"
            @download-excel="onDownloadExcel"
          />
        </keep-alive>
      </div>
    </template>
    <template #footer>
      <div class="d-flex justify-end">
        <BaseButton
          v-if="isShowErrorReport"
          :color="ButtonColorType.Gray"
          @click="handleShowReportExecution"
        >
          Back
        </BaseButton>
        <BaseButton
          v-else
          :color="ButtonColorType.Secondary"
          width="115px"
          @click="handleShowReportError"
        >
          Error Report
        </BaseButton>
      </div>
    </template>
  </BasePopup>
</template>

<script setup lang="ts">
import { ButtonColorType, DialogSizeType } from "@/enums";
import ExecutionReport from "./ExecutionReport.vue";
import ErrorReport from "./ErrorReport.vue";

type Props = {
  modelValue: boolean;
  fileName: string;
  executionTime: string;
  data: any[];
  totalItems: number;
  successItems: number;
  failItems: number;
};

const props = defineProps<Props>();

const emit = defineEmits(["update:modelValue", "onResetPopup"]);

const isShowErrorReport = ref<boolean>(false);

const isOpenPopup = computed<boolean>({
  get: () => props.modelValue,
  set: (value: boolean) => emit("update:modelValue", value),
});

const currentComponent = computed(() =>
  isShowErrorReport.value ? ErrorReport : ExecutionReport
);

const listItems = computed<any[]>(() =>
  isShowErrorReport.value
    ? props.data.filter(({ result }) => result === "Fail")
    : props.data
);

const handleDownloadErrorReport = (): void => {
  //TODO:
};

const handleDownloadExecutionReport = (): void => {
  // TODO:
};

const onDownloadExcel = (): void => {
  if (isShowErrorReport.value) {
    handleDownloadErrorReport();
  } else {
    handleDownloadExecutionReport();
  }
};

const handleResetPopup = (): void => {
  emit("onResetPopup");
};

const handleShowReportError = (): void => {
  isShowErrorReport.value = true;
};

const handleShowReportExecution = (): void => {
  isShowErrorReport.value = false;
};
</script>

<style lang="scss" scoped>
.summary-body-header-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 4px 8px;
  border-radius: 4px;
  background-color: #f7f8fa;

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #6b6d70;
  }

  &__name {
    font-family: Noto Sans KR;
    font-weight: 400;
    font-size: 13px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #3a3b3d;
  }
}

.summary-body-analysis {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: 33px;
  border: 1px solid #e6e9ed;
  border-radius: 12px;
  padding: 12px 16px;
}

.summary-body-analysis-item {
  &:not(:last-child) {
    position: relative;

    &::before {
      content: "";
      position: absolute;
      right: -16px;
      top: 50%;
      transform: translateY(-50%);
      height: 40px;
      width: 1px;
      background-color: #e6e9ed;
    }
  }

  &__title {
    font-family: Noto Sans KR;
    font-weight: 500;
    font-style: Medium;
    font-size: 11px;
    line-height: 150%;
    letter-spacing: 0.25px;
    color: #6b6d70;
  }

  &__value {
    font-family: Noto Sans KR;
    font-weight: 700;
    font-style: Bold;
    font-size: 22px;
    line-height: 150%;
    letter-spacing: 0%;
    color: #3a3b3d;

    &.is-error {
      color: #c7291d;
    }
  }
}

:deep(.report-container) {
  max-height: 575px;
}

:deep(.report-container__title) {
  font-family: Noto Sans KR;
  font-weight: 500;
  font-size: 16px;
  line-height: 150%;
  letter-spacing: 0.5px;
  color: #3a3b3d;
}

:deep(.report-container__status--success) {
  border-radius: 4px;
  padding: 4px 8px;
  background-color: #ecfdf3;
  font-family: Noto Sans KR;
  font-weight: 400;
  font-style: Regular;
  font-size: 11px;
  line-height: 150%;
  letter-spacing: 0.25px;
  color: #079455;
}

:deep(.report-container__status--fail) {
  border-radius: 4px;
  padding: 4px 8px;
  background-color: #fef3f2;
  font-family: Noto Sans KR;
  font-weight: 400;
  font-style: Regular;
  font-size: 11px;
  line-height: 150%;
  letter-spacing: 0.25px;
  color: #c7291d;
}
</style>
