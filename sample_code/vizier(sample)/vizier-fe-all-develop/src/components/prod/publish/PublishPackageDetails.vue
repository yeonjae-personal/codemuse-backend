<template>
  <div
    class="col-span-1 relative z-10 top-0 right-0 bg-white rounded-[12px] pt-6 pb-[12px]"
    :class="{ 'active-form': activeForm }"
  >
    <div class="w-full overflow-x-auto h-full relative text-[12px] px-4">
      <div class="w-full h-full flex relative">
        <div class="content w-full justify-start h-full flex flex-col">
          <div class="w-full flex justify-between items-center pb-2">
            <div
              class="w-full flex justify-between items-center px-2 h-10 mb-2"
            >
              <div
                class="text-text-base text-base font-medium tracking-[0.5px]"
              >
                {{ detailTilte }}
              </div>
            </div>
            <div v-if="showEditBtn && !isCreatePublish">
              <BaseButton
                :color="ButtonColorType.Secondary"
                @click="handleEdit"
              >
                <EditIcon class="mr-[6px]" :fill="'#BA1642'" />
                {{ $t("product_platform.edit") }}
              </BaseButton>
            </div>
          </div>
          <v-form ref="pudlishForm" class="w-full flex-1 overflow-hidden">
            <BaseTabs
              ref="baseTabs"
              v-model="currentTab"
              :tabs="publishTabs"
              :center-active="true"
              :class-loco="`max-h-[calc(100vh-375px)] !px-0`"
              flow-mode
              grow
            />
          </v-form>
          <div v-if="showSaveCancelBtn" class="flex justify-end pt-3 gap-2">
            <template v-if="currentTab !== PUBLISH_TABS_VALUE.PUBLISH">
              <BaseButton :color="ButtonColorType.Gray" @click="onCancel">
                {{ $t("product_platform.cancel") }}
              </BaseButton>
              <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
                <SaveIcon class="mr-[6px]" />
                {{ $t("product_platform.save") }}
              </BaseButton>
            </template>
            <template v-else>
              <BaseButton
                v-if="showApprovalRequestBtn"
                :color="ButtonColorType.Secondary"
                width="160px"
                @click="
                  () => {
                    openPopupApproval = true;
                  }
                "
              >
                <!-- <PublishRequestIcon class="mr-[6px]" /> -->
                {{ $t("product_platform.approval_request") }}
              </BaseButton>
              <BaseButton
                v-else-if="showPublishRequestBtn"
                :color="ButtonColorType.Secondary"
                width="160px"
                @click="handleRequestApproval"
              >
                <!-- <PublishRequestIcon class="mr-[6px]" /> -->
                {{ $t("product_platform.publish_request") }}
              </BaseButton>
            </template>
          </div>
          <div v-else-if="showValidateBtn" class="flex justify-end pt-3">
            <BaseButton :color="ButtonColorType.Secondary" @click="onValidate">
              {{ $t("LB00000502") }}
            </BaseButton>
          </div>
        </div>
      </div>
    </div>
  </div>
  <BasePopup
    v-model="openPopup"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="popupContent"
    @on-close="closePopupSave"
    @on-submit="handleSubmit"
  />
  <BasePopup
    v-model="openPopupApproval"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.approval_request_message')"
    @on-close="handleCancelApprovalRequest"
    @on-submit="handleSubmitApprovalRequest"
  />
  <BasePopup
    v-model="openPopupRequest"
    :icon="isCancel ? DialogIconType.Warning : DialogIconType.Info"
    :submit-button-text="$t('product_platform.btn_yes')"
    :cancel-button-text="$t('product_platform.btn_no')"
    :content="$t('product_platform.publish_request_message')"
    @on-close="handleCancelPublishRequest"
    @on-submit="handleSubmitPublishRequest"
  />
</template>

<script setup lang="ts">
import { Tab } from "@/interfaces/prod";
import { useI18n } from "vue-i18n";
import { usePublishManagerStore, useSnackbarStore } from "@/store";
import { DialogIconType, ButtonColorType, RequiredYn } from "@/enums";
import {
  isExpiredTime,
  isNullOrUndefinedOrEmptyOrBlank,
} from "@/utils/format-data";
import GeneralAttibutes from "@/components/prod/publish/step/GeneralAttibutes.vue";
import ComposePackage from "@/components/prod/publish/step/ComposePackage.vue";
import ApprovalFlow from "@/components/prod/publish/step/ApprovalFlow.vue";
import PublishStep from "@/components/prod/publish/step/PublishStep.vue";
import { ComposeItem } from "@/interfaces/prod/publishInterface";
import {
  PUBLISH_FLOW_STATUS,
  PUBLISH_TABS_VALUE,
  PUBLISH_CODE_STATUS,
  PUBLISH_MODE,
  CODE_ACTION_REJECT_APPROVE,
} from "@/constants/publish";
import { isEqual } from "lodash-es";

const emit = defineEmits(["onCancelCreate", "onUpsetSuccess"]);
const props = defineProps({
  isCreatePublish: {
    type: Boolean,
    default: false,
  },
});

const publishManagerStore = usePublishManagerStore();
const {
  isEditStep1,
  isEditStep2,
  isEditStep3,
  isEditStep4,
  isCreateStep1,
  isCreateStep2,
  isCreateStep3,
  isCreateStep4,
  tabProcessStatus,
  currentTab,
  publishGeneralAttributesListForm,
  publishGeneralAttributesData,
  publishApprovalFlowData,
  publishSearchStatusList,
  publishModeList,
  composePackageItemList,
  publishSelected,
  publishStepData,
  dragItemType,
  isOpenValidatePopup,
  approvalItemSelected,
  isCreatePublish: createPublish,
  publishDetail,
} = storeToRefs(publishManagerStore);

const {
  getPublishPackageDetail,
  upsetPublishPackage,
  upsetPublishApproval,
  checkAllActions,
  resetAllStepStatus,
  upsetPublishCompose,
  postApprovalRequest,
  postPublishRequest,
  publishValidated,
} = publishManagerStore;
const { t } = useI18n();
const useSnackbar = useSnackbarStore();
const pudlishForm = ref<HTMLElement | any>(null);
const baseTabs = ref<HTMLElement | any>(null);
const openPopup = ref(false);
const openPopupApproval = ref(false);
const openPopupRequest = ref(false);
const isCancel = ref(false);
const preTabCurrentValue = ref("");

const publishTabs = computed<Tab[]>(() => [
  {
    value: PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES,
    label: t("product_platform.generalAttributes"),
    status: tabProcessStatus.value.step1,
    disable: props.isCreatePublish,
    component: GeneralAttibutes,
    props: {
      isEdit: isEditStep1.value,
      isCreate: isCreateStep1.value,
      detailModal: publishGeneralAttributesData.value,
      detailList: publishGeneralAttributesListForm.value,
      groupCodeList: publishSearchStatusList.value,
    },
    onClick(event) {
      if (checkTabConditionChange()) {
        preTabCurrentValue.value = event.value;
        openPopup.value = true;
      } else {
        currentTab.value = event.value;
        isEditStep1.value =
          !props.isCreatePublish && checkAllActions() && showEditBtn.value;
        isEditStep2.value = false;
        isEditStep3.value = false;
        isEditStep4.value = false;
      }
    },
  },
  {
    value: PUBLISH_TABS_VALUE.COMPOSE_PACKAGE,
    label: t("product_platform.composePackage"),
    status: tabProcessStatus.value.step2,
    disable: props.isCreatePublish,
    component: ComposePackage,
    props: {
      isEdit: isEditStep2.value,
      isCreate: isCreateStep2.value,
      dataList: composePackageItemList.value,
      dragType: dragItemType.value,
    },
    onClick(event) {
      if (checkTabConditionChange()) {
        preTabCurrentValue.value = event.value;
        openPopup.value = true;
      } else {
        currentTab.value = event.value;
        isEditStep2.value =
          !props.isCreatePublish && checkAllActions() && showEditBtn.value;
        isEditStep1.value = false;
        isEditStep3.value = false;
        isEditStep4.value = false;
      }
    },
    events: {
      "add-new-item": handleAddComposeItem,
      "remove-item": handleRemoveComposeItem,
      "open-tab": handleRedirect,
    },
  },
  {
    value: PUBLISH_TABS_VALUE.APPROVAL_FLOW,
    label: t("product_platform.approvalFlow"),
    status: tabProcessStatus.value.step3,
    disable: props.isCreatePublish,
    component: ApprovalFlow,
    props: {
      isEdit: isEditStep3.value,
      isCreate: isCreateStep3.value,
    },
    onClick(event) {
      if (checkTabConditionChange()) {
        preTabCurrentValue.value = event.value;
        openPopup.value = true;
      } else {
        getPublishPackageDetail(
          publishGeneralAttributesData.value?.pubRqstTaskCode
        );
        approvalItemSelected.value = null;
        currentTab.value = event.value;
        isEditStep3.value =
          !props.isCreatePublish && checkAllActions() && showEditBtn.value;
        isEditStep1.value = false;
        isEditStep2.value = false;
        isEditStep4.value = false;
      }
    },
  },
  {
    value: PUBLISH_TABS_VALUE.PUBLISH,
    label: t("product_platform.publishStep"),
    status: tabProcessStatus.value.step4,
    disable: props.isCreatePublish,
    component: PublishStep,
    props: {
      isEdit: isEditStep4.value,
      isCreate: isCreateStep4.value,
      detailModal: publishStepData.value,
      detailGeneral: publishGeneralAttributesData.value,
      detailAppr: publishApprovalFlowData.value,
      groupCodeList: publishSearchStatusList.value,
      publishModeList: publishModeList.value,
      isShowApprovalFlow: checkIsShowApprovalFlowPublish.value,
      isShowPublishSchedule: checkIsShowPublishSchedule.value,
      isShowPublishExecution: checkIsShowPublishExecution.value,
    },
    onClick(event) {
      if (checkTabConditionChange()) {
        preTabCurrentValue.value = event.value;
        openPopup.value = true;
      } else {
        getPublishPackageDetail(
          publishGeneralAttributesData.value?.pubRqstTaskCode
        );
        approvalItemSelected.value = null;
        currentTab.value = event.value;
        isEditStep4.value =
          !props.isCreatePublish && checkAllActions() && showEditBtn.value;
        isEditStep1.value = false;
        isEditStep2.value = false;
        isEditStep3.value = false;
      }
    },
  },
]);

const popupContent = computed(() => {
  return preTabCurrentValue.value
    ? t("product_platform.updatingConfirmSaved")
    : isCancel.value
      ? t("product_platform.desc_cancel")
      : t("product_platform.desc_update");
});

const isInvalid = computed(() => {
  return publishGeneralAttributesListForm.value.some(
    (item: any) =>
      item.requiredYn === RequiredYn.Yes &&
      isNullOrUndefinedOrEmptyOrBlank(
        publishGeneralAttributesData.value[item.colName]
      )
  );
});

const checkIsShowApprovalFlowPublish = computed(() => {
  const checkDelayValidated =
    publishGeneralAttributesData.value?.pubRqstStusCode ===
      PUBLISH_CODE_STATUS.DELAY &&
    publishGeneralAttributesData.value?.vldateDtm;

  return (
    currentTab.value === PUBLISH_TABS_VALUE.PUBLISH &&
    [
      PUBLISH_CODE_STATUS.INPROGRESS,
      PUBLISH_CODE_STATUS.PUBLISH_REQUEST,
      checkDelayValidated,
    ].includes(
      publishGeneralAttributesData.value?.pubRqstStusCode ===
        PUBLISH_CODE_STATUS.DELAY
        ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
        : publishGeneralAttributesData.value?.pubRqstStusCode
    )
  );
});

const checkIsShowPublishSchedule = computed(() => {
  return (
    publishStepData.value?.pubPrcsTypeCode !== PUBLISH_MODE.AUTO &&
    [
      PUBLISH_CODE_STATUS.INPROGRESS,
      PUBLISH_CODE_STATUS.PUBLISH_REQUEST,
    ].includes(
      publishGeneralAttributesData.value?.pubRqstStusCode ===
        PUBLISH_CODE_STATUS.DELAY
        ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
        : publishGeneralAttributesData.value?.pubRqstStusCode
    ) &&
    !publishApprovalFlowData.value.pubAprvStepLDtos?.find(
      (aprr) => aprr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REQUEST
    )
  );
});

const checkIsShowPublishExecution = computed(() => {
  return (
    currentTab.value === PUBLISH_TABS_VALUE.PUBLISH &&
    [PUBLISH_CODE_STATUS.PUBLISH_REQUEST].includes(
      publishGeneralAttributesData.value?.pubRqstStusCode ===
        PUBLISH_CODE_STATUS.DELAY
        ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
        : publishGeneralAttributesData.value?.pubRqstStusCode
    ) &&
    checkIsShowPublishSchedule.value
  );
});

const detailTilte = computed(() => {
  switch (currentTab.value) {
    case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
      return checkStatus(isEditStep1.value, isCreateStep1.value);
    case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
      return checkStatus(isEditStep2.value, isCreateStep2.value);
    case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
      return checkStatus(isEditStep3.value, isCreateStep3.value);
    case PUBLISH_TABS_VALUE.PUBLISH:
      return checkStatus(isEditStep4.value, isCreateStep4.value);
  }
});

const activeForm = computed(() => {
  switch (currentTab.value) {
    case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
      return isEditStep1.value || isCreateStep1.value;
    case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
      return isEditStep2.value || isCreateStep2.value;
    case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
      return isEditStep3.value || isCreateStep3.value;
    case PUBLISH_TABS_VALUE.PUBLISH:
      return isEditStep4.value || isCreateStep4.value;
  }
});

const showSaveCancelBtn = computed(() => {
  switch (currentTab.value) {
    case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
      return isEditStep1.value || isCreateStep1.value;
    case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
      return (
        (isEditStep2.value || isCreateStep2.value) &&
        composePackageItemList.value?.length
      );
    case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
      return (
        (isEditStep3.value || isCreateStep3.value) &&
        publishApprovalFlowData.value?.pubAprvStepLDtos?.length > 1
      );
    case PUBLISH_TABS_VALUE.PUBLISH:
      return isEditStep4.value || isCreateStep4.value;
  }
});

const showValidateBtn = computed(() => {
  return (
    currentTab.value === PUBLISH_TABS_VALUE.COMPOSE_PACKAGE &&
    !isEditStep2.value &&
    !isCreateStep2.value &&
    [
      PUBLISH_CODE_STATUS.COMPOSED,
      PUBLISH_CODE_STATUS.CREATED,
      PUBLISH_CODE_STATUS.VALIDATED,
    ].includes(
      publishGeneralAttributesData.value?.pubRqstStusCode ===
        PUBLISH_CODE_STATUS.DELAY
        ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
        : publishGeneralAttributesData.value?.pubRqstStusCode
    ) &&
    composePackageItemList.value?.length
  );
});

const showEditBtn = computed(() => {
  if (showSaveCancelBtn.value) {
    return false;
  }
  if (isExpiredTime(publishGeneralAttributesData.value?.exprDtm)) {
    return currentTab.value === PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES;
  }
  return verifyAllowEdit(
    publishGeneralAttributesData.value?.pubRqstStusCode ===
      PUBLISH_CODE_STATUS.DELAY
      ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
      : publishGeneralAttributesData.value?.pubRqstStusCode
  );
});

const showApprovalRequestBtn = computed(() => {
  return [PUBLISH_CODE_STATUS.VALIDATED].includes(
    publishGeneralAttributesData.value?.pubRqstStusCode ===
      PUBLISH_CODE_STATUS.DELAY
      ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
      : publishGeneralAttributesData.value?.pubRqstStusCode
  );
});

const showPublishRequestBtn = computed(() => {
  return [PUBLISH_CODE_STATUS.INPROGRESS].includes(
    publishGeneralAttributesData.value?.pubRqstStusCode ===
      PUBLISH_CODE_STATUS.DELAY
      ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
      : publishGeneralAttributesData.value?.pubRqstStusCode
  );
});

const verifyAllowEdit = (status) => {
  // const isReject = props.detailAppr?.pubAprvStepLDtos?.some(
  //     (apr) => apr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REJECT
  //   );
  switch (status) {
    case PUBLISH_CODE_STATUS.CREATED:
    case PUBLISH_CODE_STATUS.COMPOSED:
      return (
        (currentTab.value === PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES &&
          !isEditStep1.value) ||
        (currentTab.value === PUBLISH_TABS_VALUE.COMPOSE_PACKAGE &&
          !isEditStep2.value)
      );
    case PUBLISH_CODE_STATUS.VALIDATED:
      const checkApprovalList =
        currentTab.value === PUBLISH_TABS_VALUE.PUBLISH &&
        publishApprovalFlowData.value?.pubAprvStepLDtos?.length > 1;
      return (
        (currentTab.value === PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES &&
          !isEditStep1.value) ||
        (currentTab.value === PUBLISH_TABS_VALUE.COMPOSE_PACKAGE &&
          !isEditStep2.value) ||
        (currentTab.value === PUBLISH_TABS_VALUE.APPROVAL_FLOW &&
          !isEditStep3.value) ||
        checkApprovalList
      );
    case PUBLISH_CODE_STATUS.INPROGRESS:
    case PUBLISH_CODE_STATUS.EXPIRED:
      if (
        currentTab.value === PUBLISH_TABS_VALUE.PUBLISH &&
        checkIsShowPublishSchedule.value &&
        !publishApprovalFlowData.value?.pubAprvStepLDtos?.some(
          (apr) => apr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REJECT
        )
      ) {
        return true;
      }
      return (
        currentTab.value === PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES &&
        !isEditStep1.value
      );
    default:
      return false;
  }
};

const checkStatus = (editStatus, createStatus) => {
  return createStatus
    ? t("product_platform.publishPackageCreate")
    : editStatus
      ? t("product_platform.publishPackageEdit")
      : t("product_platform.publishPackageDetails");
};

const handleCancelApprovalRequest = () => {
  openPopupApproval.value = false;
  handleCancel();
};
const handleCancelPublishRequest = () => {
  openPopupRequest.value = false;
  handleCancel();
};

const handleSubmitPublishRequest = async () => {
  const { status } = await postPublishRequest({
    pubRqstTaskCode: publishGeneralAttributesData.value?.pubRqstTaskCode,
    pubPrcsTaskMDto: {
      pubPrcsRsvDtm: publishStepData.value?.pubPrcsRsvDtm,
    },
  });
  if (status === 200) {
    useSnackbar.showSnackbar(
      t("product_platform.request_publish_success"),
      "success"
    );
    await getPublishPackageDetail(publishSelected.value?.pubRqstTaskCode);
    emit("onUpsetSuccess");
    if (props.isCreatePublish) {
      isCreateStep1.value = false;
      isCreateStep2.value = false;
      isCreateStep3.value = false;
      isCreateStep4.value = false;
      tabProcessStatus.value.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
      tabProcessStatus.value.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
      tabProcessStatus.value.step3 = PUBLISH_FLOW_STATUS.COMPLETE;
      currentTab.value = PUBLISH_TABS_VALUE.PUBLISH;
    } else isEditStep4.value = false;
  }
  openPopupRequest.value = false;
};

const handleSubmitApprovalRequest = async () => {
  const { status } = await postApprovalRequest({
    pubRqstTaskCode: publishGeneralAttributesData.value?.pubRqstTaskCode,
    pubRqstTaskMDto: publishGeneralAttributesData.value,
  });

  if (status === 200) {
    useSnackbar.showSnackbar(
      t("product_platform.request_approve_success"),
      "success"
    );
    await getPublishPackageDetail(publishSelected.value?.pubRqstTaskCode);
    emit("onUpsetSuccess");
    if (props.isCreatePublish) {
      isCreateStep1.value = false;
      isCreateStep2.value = false;
      isCreateStep3.value = false;
      isCreateStep4.value = false;
      tabProcessStatus.value.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
      tabProcessStatus.value.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
      tabProcessStatus.value.step3 = PUBLISH_FLOW_STATUS.COMPLETE;
      currentTab.value = PUBLISH_TABS_VALUE.PUBLISH;
    } else isEditStep4.value = false;
  }

  openPopupApproval.value = false;
};

const handleEdit = () => {
  switch (currentTab.value) {
    case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
      isEditStep1.value = true;
      break;
    case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
      isEditStep2.value = true;
      break;
    case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
      isEditStep3.value = true;
      break;
    case PUBLISH_TABS_VALUE.PUBLISH:
      isEditStep4.value = true;
      break;
  }
};

const onCancel = () => {
  isCancel.value = true;
  openPopup.value = true;
};

const onSubmit = async () => {
  isCancel.value = false;
  const { valid } = await pudlishForm.value?.validate();

  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  if (!valid || isInvalid.value) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }

  if (
    isEditStep3.value &&
    currentTab.value === PUBLISH_TABS_VALUE.APPROVAL_FLOW &&
    publishApprovalFlowData.value?.pubAprvStepLDtos?.length > 1 &&
    publishApprovalFlowData.value?.pubAprvStepLDtos
      ?.filter((flw) => flw.sortNo > 1)
      ?.some((aprItem) => !Number(aprItem.lmtTm))
  ) {
    useSnackbar.showSnackbar(
      t("product_platform.message_time_limit_approval_flow"),
      "error"
    );
    return;
  }

  openPopup.value = true;
};

const handleSubmit = async () => {
  if (isCancel.value) {
    openPopup.value;
    handleCancel();
    if (props.isCreatePublish) {
      publishSelected.value = null;
    } else {
      await getPublishPackageDetail(publishSelected.value.pubRqstTaskCode);
    }
  } else if (preTabCurrentValue.value) {
    currentTab.value = preTabCurrentValue.value;
    resetAllStepStatus();
    await getPublishPackageDetail(publishSelected.value.pubRqstTaskCode);
    switch (currentTab.value) {
      case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
        isEditStep1.value = true;
        break;
      case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
        isEditStep2.value = showEditBtn.value;
        break;
      case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
        isEditStep3.value = showEditBtn.value;
        break;
      case PUBLISH_TABS_VALUE.PUBLISH:
        isEditStep4.value = showEditBtn.value;
        break;
    }
    preTabCurrentValue.value = "";
  } else {
    switch (currentTab.value) {
      case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
        handleSubmitStep1();
        break;
      case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
        handleSubmitStep2();
        break;
      case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
        handleSubmitStep3();
        break;
      case PUBLISH_TABS_VALUE.PUBLISH:
        return;
    }
  }
  openPopup.value = false;
};

const handleSubmitStep1 = async () => {
  const { data, status } = await upsetPublishPackage(
    {
      pubRqstTaskMDto: publishGeneralAttributesData.value,
    },
    props.isCreatePublish
  );
  if (status === 200) {
    useSnackbar.showSnackbar(
      t("product_platform.successfully_saved"),
      "success"
    );
    await getPublishPackageDetail(
      props.isCreatePublish ? data : publishSelected.value.pubRqstTaskCode
    );
    emit("onUpsetSuccess");
    if (props.isCreatePublish) {
      isCreateStep1.value = false;
      isCreateStep2.value = showEditBtn.value;
      publishSelected.value.itemUnique = data;
      publishSelected.value.pubRqstTaskCode = data;
    } else {
      isEditStep1.value = false;
    }
    currentTab.value = PUBLISH_TABS_VALUE.COMPOSE_PACKAGE;
    tabProcessStatus.value.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
  }
};
const handleSubmitStep2 = async () => {
  try {
    const { status } = await upsetPublishCompose({
      chngDataLstDtos: composePackageItemList.value,
    });
    if (status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
      await getPublishPackageDetail(
        props.isCreatePublish
          ? publishGeneralAttributesData.value.pubRqstTaskCode
          : publishSelected.value.pubRqstTaskCode
      );
      emit("onUpsetSuccess");
      isCreateStep2.value = false;
      isEditStep2.value = false;
      createPublish.value = false;
    }
  } catch (error: any) {
    useSnackbar.showSnackbar(
      t("product_platform.something_went_wrong"),
      "success"
    );
  }
};
const handleSubmitStep3 = async () => {
  const mappedData = {
    pubRqstTaskCode: publishSelected.value?.pubRqstTaskCode,
    pubAprvMDto: publishApprovalFlowData.value,
  };

  const { data, status } = await upsetPublishApproval(mappedData);

  if (status === 200) {
    useSnackbar.showSnackbar(
      t("product_platform.successfully_saved"),
      "success"
    );
    await getPublishPackageDetail(
      data || publishSelected.value?.pubRqstTaskCode
    );
    emit("onUpsetSuccess");
    if (props.isCreatePublish) {
      isCreateStep1.value = false;
      isCreateStep2.value = false;
      isCreateStep3.value = false;
      isCreateStep4.value = true;
      tabProcessStatus.value.step1 = PUBLISH_FLOW_STATUS.COMPLETE;
      tabProcessStatus.value.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
      tabProcessStatus.value.step3 = PUBLISH_FLOW_STATUS.COMPLETE;
      currentTab.value = PUBLISH_TABS_VALUE.PUBLISH;
    } else {
      isEditStep3.value = false;
      tabProcessStatus.value.step3 = PUBLISH_FLOW_STATUS.COMPLETE;
      currentTab.value = PUBLISH_TABS_VALUE.PUBLISH;
      isEditStep4.value = false;
    }
    approvalItemSelected.value = null;
  }
};

const handleCancel = async () => {
  switch (currentTab.value) {
    case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
      return cancelStep1();
    case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
      return cancelStep2();
    case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
      return cancelStep3();
    case PUBLISH_TABS_VALUE.PUBLISH:
      return cancelStep4();
  }
  closePopupSave();
};
const cancelStep1 = async () => {
  if (isEditStep1.value) {
    isEditStep1.value = false;
  } else if (isCreateStep1.value) {
    isCreateStep1.value = false;
    emit("onCancelCreate");
  }
};
const cancelStep2 = async () => {
  if (isEditStep2.value) {
    isEditStep2.value = false;
  } else if (isCreateStep2.value) {
    isCreateStep2.value = false;
  }
};

const cancelStep3 = async () => {
  if (isEditStep3.value) {
    isEditStep3.value = false;
  } else if (isCreateStep3.value) {
    isCreateStep3.value = false;
  }
};

const cancelStep4 = async () => {
  if (isEditStep4.value) {
    isEditStep4.value = false;
  } else if (isCreateStep4.value) {
    isCreateStep4.value = false;
  }
};
const closePopupSave = async () => {
  openPopup.value = false;
};

const handleAddComposeItem = (event: any) => {
  composePackageItemList.value.unshift({ ...event, isAdd: true });
};
const handleRemoveComposeItem = (event: ComposeItem) => {
  composePackageItemList.value = composePackageItemList.value.filter(
    (item) => item.chngDataCode !== event.chngDataCode
  );
};
const onValidate = async () => {
  try {
    const { data, status } = await publishValidated();
    if (status === 200) {
      if (data === RequiredYn.Yes) {
        await getPublishPackageDetail(publishSelected.value.pubRqstTaskCode);
        emit("onUpsetSuccess");
        currentTab.value = PUBLISH_TABS_VALUE.APPROVAL_FLOW;
        tabProcessStatus.value.step2 = PUBLISH_FLOW_STATUS.COMPLETE;
        if (props.isCreatePublish) {
          isCreateStep3.value = true;
        } else {
          isEditStep3.value = true;
        }
      } else {
        isOpenValidatePopup.value = true;
      }
    }
  } catch (error) {
    useSnackbar.showSnackbar(
      t("product_platform.something_went_wrong"),
      "error"
    );
  }
};

const checkTabConditionChange = () => {
  switch (currentTab.value) {
    case PUBLISH_TABS_VALUE.GENERAL_ATTRIBUTES:
      return (
        (isEditStep1.value || isCreateStep1.value) &&
        !isEqual(
          publishGeneralAttributesData.value,
          publishDetail.value.pubRqstTaskMDto
        )
      );
    case PUBLISH_TABS_VALUE.COMPOSE_PACKAGE:
      return (
        (props.isCreatePublish ||
          !isEqual(
            composePackageItemList.value,
            publishDetail.value.chngDataLstDtos
          )) &&
        (isEditStep2.value || isCreateStep2.value)
      );
    case PUBLISH_TABS_VALUE.APPROVAL_FLOW:
      return (
        !isEqual(
          publishApprovalFlowData.value,
          publishDetail.value.pubAprvMDto
        ) &&
        (isEditStep3.value || isCreateStep3.value)
      );
    case PUBLISH_TABS_VALUE.PUBLISH:
      return (
        (isEditStep4.value || isCreateStep4.value) &&
        (!isEqual(publishStepData.value, publishDetail.value.pubPrcsTaskMDto) ||
          !isEqual(
            publishGeneralAttributesData.value,
            publishDetail.value.pubRqstTaskMDto
          ))
      );
  }
};

const handleRequestApproval = () => {
  if (baseTabs.value?.componentRefs?.length) {
    baseTabs.value?.componentRefs.forEach((el) => el?.validationAllSelect?.());
  }
  if (!publishStepData.value?.pubPrcsRsvDtm) {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
    return;
  }
  openPopupRequest.value = true;
};

const handleRedirect = inject<any>("handleRedirect");
</script>

<style scoped lang="scss">
.active-form {
  border: 1px solid var(--border-border-primary, #d9325a);
  box-shadow: 0px 0px 0px 4px #d9325a29;
}
</style>
