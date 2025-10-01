<template>
  <div class="bg-white p-4 pt-[24px] h-full rounded-lg relative">
    <div class="flex justify-between items-center pl-3 pr-3 pb-3 h-[52px]">
      <div class="flex align-center gap-2 items-end">
        <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
          {{ $t("product_platform.ruleAIReport") }}
        </h1>
      </div>
      <div v-if="!isLoadingValidate">
        <BaseButton
          :color="ButtonColorType.Secondary"
          :disabled="isLoadingReport"
          @click="handleReportClick"
        >
          {{ $t("product_platform.report") }}
        </BaseButton>
      </div>
    </div>
    <div v-if="isLoadingValidate || isLoadingReport" class="content-wrapper">
      <v-progress-linear indeterminate color="primary"></v-progress-linear>
    </div>
    <div v-else class="content-wrapper">
      <LocomotiveComponent
        scroll-container-class="!max-h-[calc(100vh-280px)]"
        scroll-content-class="h-full flex"
      >
        <div v-if="isShowReport" class="content-wrapper__content">
          <iframe
            id="rule-html-report"
            ref="ruleFrameRef"
            :srcdoc="ruleReportContent"
            referrerpolicy="origin"
            loading="lazy"
            title="Rule HTML Report"
          ></iframe>
        </div>
        <div v-else class="ai-summary-card" v-html="html"></div>
      </LocomotiveComponent>
    </div>
    <ArrowLeftIcon
      class="absolute top-[174px] right-[0] cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="handleClosePane"
    />
    <div class="rule-report-action">
      <BaseButton :color="ButtonColorType.Gray" @click="handleClosePane">
        {{ t("product_platform.cancel") }}
      </BaseButton>
    </div>
  </div>
  <BasePopup
    v-if="isShowPopupCancel"
    v-model="isShowPopupCancel"
    :content="t('product_platform.desc_cancel')"
    :icon="DialogIconType.Warning"
    :cancel-button-text="t('product_platform.btn_no')"
    :submit-button-text="t('product_platform.btn_yes')"
    @on-close="handleClosePopupCancel"
    @on-submit="handleSubmitPopupCancel"
  />
</template>
<script setup lang="ts">
import { useI18n } from "vue-i18n";
import { ButtonColorType, DialogIconType } from "@/enums";
import useRuleEngineStore from "@/store/admin/ruleEngine.store";
import LocomotiveComponent from "@/components/prod/common/LocomotiveComponent.vue";

declare global {
  interface Window {
    markdownit: (options: any) => any;
  }
}

const { t } = useI18n();

const ruleEngineStore = useRuleEngineStore();
const {
  ruleValidation,
  isShowRuleList,
  isShowRuleReport,
  isShowRuleDetail,
  isShowReport,
  ruleReportContent,
  isLoadingValidate,
  isExpanded,
  isLoadingReport,
} = storeToRefs(ruleEngineStore);
const { aiReport } = ruleEngineStore;

const isShowPopupCancel = ref<boolean>(false);
const html = ref("");
const ruleFrameRef = ref<HTMLIFrameElement | null>(null);

watch(
  ruleValidation,
  (value) => {
    if (value) {
      html.value = window
        .markdownit({
          breaks: true,
          gfm: true,
          html: true,
        })
        .render((ruleValidation.value as any).ai_summary_md || "");
    }
  },
  { deep: true, immediate: true }
);

watch(ruleReportContent, (value) => {
  if (!!value) {
    nextTick(() => {
      const iframe = ruleFrameRef.value;
      if (!iframe) return;
      iframe.onload = () => {
        if (!iframe.contentWindow) return;
        const doc = iframe.contentDocument || iframe.contentWindow.document;
        const styleTag = doc.createElement("style");
        styleTag.textContent = `
        .container {
          width: auto !important;
        }

        canvas {
          width: calc(100% - 32px) !important;
        }
      `;
        doc.head.appendChild(styleTag);
      };
    });
  }
});

const handleClosePane = () => {
  isShowPopupCancel.value = true;
};

const handleClosePopupCancel = () => {
  isShowPopupCancel.value = false;
};

const handleSubmitPopupCancel = (): void => {
  isShowRuleReport.value = false;
  ruleValidation.value = null;
  ruleReportContent.value = "";
  if (!isExpanded.value) {
    isShowRuleList.value = true;
    isShowRuleDetail.value = true;
  }
};

const handleReportClick = () => {
  isShowReport.value = true;
  aiReport();
};
</script>

<style lang="scss" scoped>
.rule-report-action {
  position: absolute;
  bottom: 12px;
  right: 24px;
}

.content-wrapper__content {
  width: 100%;
  height: calc(100vh - 280px);

  & > iframe {
    width: 100%;
    height: 100%;
    display: block;
  }
}

:deep(.ai-summary-card blockquote) {
  background: #f9fafb;
  border-left: 4px solid #3b82f6;
  padding: 12px;
}

:deep(.ai-summary-card table) {
  width: 100%;
  border-collapse: collapse;
}

:deep(.ai-summary-card th),
:deep(.ai-summary-card td) {
  border: 1px solid #e5e7eb;
  padding: 6px 8px;
}
</style>
