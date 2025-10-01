<!-- eslint-disable vue/no-mutating-props -->
<template>
  <div class="text-text-lighter font-size-base font-medium">
    <DetailPane :is-not-rounded-bottom="isShowApprovalFlow">
      <span class="text-text-base leading-[19.5px] !font-medium !text-[13px]"
        >{{ $t("product_platform.preparation") }}
      </span>
      <DetailPaneRow
        :label="$t('product_platform.package')"
        :value="
          getTextDisplay(
            detailGeneral.pubRqstStusCode,
            COLUMN_FIELD_TYPE.DL,
            groupCodeList
          )
        "
      />
      <DetailPaneRow
        :label="$t('product_platform.validation')"
        :value="showValidationStatus || '-'"
      />
      <DetailPaneRow :label="$t('product_platform.publish_mode')">
        <template #value="{ klass }">
          <div :class="klass">
            <template v-if="isEdit && !isShowPublishSchedule">
              <BaseSelectScroll
                ref="selectScroll"
                v-model="detailGeneral.pubPrcsTypeCode"
                :options="publishModeList"
                :default-item-select-all="false"
                :height="32"
              />
            </template>
            <template v-else>{{
              isShowApprovalFlow
                ? getTextDisplay(
                    detailGeneral.pubPrcsTypeCode,
                    COLUMN_FIELD_TYPE.DL,
                    publishModeList
                  )
                : "-"
            }}</template>
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>

    <DetailPane
      v-if="isShowApprovalFlow"
      class="mt-2 !rounded-[unset]"
      :is-not-rounded-bottom="isShowPublishSchedule"
    >
      <span class="text-text-base leading-[19.5px] !font-medium !text-[13px]"
        >{{ $t("product_platform.approval_flow") }}
      </span>
      <DetailPaneRow
        :label="$t('product_platform.dashboard.status')"
        :value="getStatusApprovalFlow || '-'"
      />
      <DetailPaneRow
        :label="$t('product_platform.dashboard.duration')"
        :value="getDurationApprovalFlow || '-'"
      />
      <DetailPaneRow
        :label="$t('product_platform.completed')"
        :value="getDateCompeletedApproval || '-'"
      />
    </DetailPane>

    <DetailPane
      v-if="
        isShowPublishSchedule &&
        detailGeneral.pubPrcsTypeCode === PUBLISH_MODE.MANUAL
      "
      class="mt-2 !rounded-[unset]"
      :is-not-rounded-bottom="isShowPublishExecution"
    >
      <span class="text-text-base leading-[19.5px] !font-medium !text-[13px]"
        >{{ $t("product_platform.publish_schedule") }}
      </span>
      <DetailPaneRow :label="$t('product_platform.scheduled')">
        <template #value="{ klass }">
          <div :class="klass">
            <template v-if="isEdit">
              <BaseDateTimePicker
                ref="datePicker"
                v-model="detailModal.pubPrcsRsvDtm"
                :min-date="currentDate"
                :max-date="detailGeneral?.duedDtm"
                :clearable="true"
                enable-time-picker
                :auto-apply="false"
                styles="absolute common-datetime-picker"
                required
              />
            </template>
            <template v-else>{{
              formatDate(
                detailModal?.pubPrcsRsvDtm,
                DATE_FORMAT.DATE_TYPE,
                DATE_FORMAT.DATE_TYPE
              ) || "-"
            }}</template>
          </div>
        </template>
      </DetailPaneRow>
    </DetailPane>

    <DetailPane v-if="isShowPublishExecution" class="mt-2" is-not-rounded-top>
      <span class="text-text-base leading-[19.5px] !font-medium !text-[13px]">
        {{ $t("product_platform.publish_execution") }}
      </span>
      <DetailPaneRow
        :label="$t('product_platform.start')"
        :value="
          (detailModal?.pubPrcsStartDtm &&
            formatDate(
              detailModal?.pubPrcsStartDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )) ||
          '-'
        "
      />
      <DetailPaneRow
        :label="$t('product_platform.end')"
        :value="
          (detailModal?.pubPrcsEndDtm &&
            formatDate(
              detailModal?.pubPrcsEndDtm,
              DATE_FORMAT.DATE_TYPE,
              DATE_FORMAT.DATE_TYPE
            )) ||
          '-'
        "
      />
      <DetailPaneRow
        :label="$t('product_platform.dashboard.duration')"
        :value="showDurationExecution || '-'"
      />
      <DetailPaneRow
        :label="$t('product_platform.dashboard.status')"
        :value="detailModal?.pubPrcsRslt || '-'"
      />
      <DetailPaneRow
        :label="$t('product_platform.message')"
        :value="detailModal?.pubPrcsMsg || '-'"
      />
    </DetailPane>
  </div>
  <!-- <template v-else>
    <div class="h-full w-full flex justify-center items-center">
      <NoData />
    </div>
  </template> -->
</template>
<script setup lang="ts">
import { DATE_FORMAT } from "@/constants/index";
import moment from "moment-timezone";
import { formatDate, isExpiredTime } from "@/utils/format-data";
import { useGroupCode } from "@/composables/useGroupCode";
import { COLUMN_FIELD_TYPE } from "@/enums/columnTypes";
import DetailPane from "../../layout/DetailPane.vue";
import {
  CODE_ACTION_REJECT_APPROVE,
  CODE_REVIEW_APPROVAL,
  PUBLISH_MODE,
} from "@/constants/publish";
import { useI18n } from "vue-i18n";
import { formatFunctionTiming } from "@/utils/date-time-format";

defineEmits(["update:detailModal"]);

const props = defineProps({
  isEdit: {
    type: Boolean,
    default: false,
  },
  isCreate: {
    type: Boolean,
    default: false,
  },
  detailModal: {
    type: Object,
    default: () => {},
  },
  detailGeneral: {
    type: Object,
    default: () => {},
  },
  detailAppr: {
    type: Object,
    default: () => {},
  },
  groupCodeList: {
    type: Array,
    default: () => [],
  },
  publishModeList: {
    type: Array,
    default: () => [],
  },
  isShowApprovalFlow: {
    type: Boolean,
    default: false,
  },
  isShowPublishSchedule: {
    type: Boolean,
    default: false,
  },
  isShowPublishExecution: {
    type: Boolean,
    default: false,
  },
});

const { t } = useI18n();
const { getTextDisplay } = useGroupCode();
const datePicker = ref();
const currentDate = computed(() =>
  moment().format(DATE_FORMAT.DATE_FORMAT_WITHOUT_TIME_REVERSE)
);

const showValidationStatus = computed(() => {
  const dateValidate = props.detailGeneral?.vldateDtm;

  return dateValidate ? t("product_platform.completed") : null;
});

const getStatusApprovalFlow = computed(() => {
  if (props.detailAppr?.pubAprvStepLDtos?.length) {
    const isReject = props.detailAppr?.pubAprvStepLDtos?.some(
      (apr) => apr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REJECT
    );
    const itemRequestStatus = props.detailAppr?.pubAprvStepLDtos?.some(
      (apr) =>
        apr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REQUEST &&
        apr.pubAprvStepCode !== CODE_REVIEW_APPROVAL.DESIGN
    );
    if (isReject) return t("LB00000516");
    if (itemRequestStatus) {
      let checkExpired = isExpiredTime(props.detailGeneral?.duedDtm);
      return checkExpired
        ? t("product_platform.status_delay")
        : t("product_platform.status_in_progress");
    }
    return t("product_platform.completed");
  }
  return null;
});

const getDurationApprovalFlow = computed(() => {
  let timeApprovalRequest = props.detailAppr?.pubAprvRqsttDtm;

  if (props.detailAppr?.pubAprvStepLDtos?.length) {
    let lastItem =
      props.detailAppr?.pubAprvStepLDtos[
        props.detailAppr?.pubAprvStepLDtos?.length - 1
      ];

    // :COMPLETED -> caculation from clicked approval request button to approve last item REQ in step 3
    if (
      [
        CODE_ACTION_REJECT_APPROVE.APPROVE,
        CODE_ACTION_REJECT_APPROVE.REJECT,
      ].includes(lastItem?.aprvStusCode)
    ) {
      const start = moment(lastItem?.aprvDtm);
      const end = moment(timeApprovalRequest);

      return `${start.diff(end, "days")} days`;
    } else {
      // :INPROGRESS -> caculation from clicked approval request button to today
      const today = moment();
      return `${today.diff(timeApprovalRequest, "days")} days`;
    }
  }
  return null;
});

const getDateCompeletedApproval = computed(() => {
  if (props.detailAppr?.pubAprvStepLDtos?.length) {
    return formatDate(
      props.detailAppr?.pubAprvStepLDtos[
        props.detailAppr?.pubAprvStepLDtos?.length - 1
      ]?.aprvDtm,
      DATE_FORMAT.DATE_TYPE,
      DATE_FORMAT.DATE_TYPE
    );
  }
  return null;
});

const showDurationExecution = computed(() => {
  if (!props.detailModal?.pubPrcsRsvDtm || !props.detailModal?.pubPrcsEndDtm) {
    return null;
  }
  const start = moment(props.detailModal?.pubPrcsRsvDtm);
  const end = moment(props.detailModal?.pubPrcsEndDtm);
  const duration = moment.duration(end.diff(start)).asMilliseconds();

  return formatFunctionTiming(duration);
});

const validationAllSelect = () => {
  datePicker.value.validation?.();
};
const resetValidationAllSelect = () => {
  datePicker.value.resetValidation?.();
};

defineExpose({
  validationAllSelect,
  resetValidationAllSelect,
});
</script>
<style scoped>
.common-datetime-picker :deep().dp__pointer {
  height: 32px;
}
</style>
