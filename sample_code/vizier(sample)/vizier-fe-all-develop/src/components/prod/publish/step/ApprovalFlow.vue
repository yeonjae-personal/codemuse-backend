<template>
  <div class="px-2">
    <ItemDrop
      v-if="isEdit"
      class-name="h-[56px] mb-4"
      @drop="drop($event)"
      @dragover="allowDrop($event)"
    />
    <div class="relative h-[23px] flex align-center justify-center">
      <div class="h-[1px] w-full bg-[#e6e9ed]"></div>
      <div
        class="absolute bg-[#fff] px-2 text-[15px] font-[500] text-[#3a3b3d]"
      >
        {{ titleFlow }}
      </div>
    </div>
    <div class="mt-4">
      <LocomotiveComponent
        scroll-container-class="!px-0 max-h-[calc(100vh-355px)]"
        scroll-content-class=""
        :is-stop-propagation-wheel="isStopPropagationWheelStatus"
        @is-wheel="handleWheelDetail"
      >
        <div class="content h-full overflow-hidden">
          <div class="flex flex-col gap-3 items-center px-[10px] py-1">
            <template v-for="(item, index) in items" :key="item.id">
              <ApprovalFlowExpand
                :item="item"
                :expand="index !== 0 && item.expand"
                :title="showTitle(item?.pubAprvStepCode || item?.aprvStepCode)"
                :is-active="approvalItemSelected?.sortNo === item?.sortNo"
                :aprv-dtm="index === 0 ? item?.aprvDtm : ''"
                :is-show-time-input="isEdit"
                :is-disabled-action="checkShowAction(item)"
                @handle-click="handleClickItem(item)"
                @show-reject="handleShowReject(item)"
                @show-approve="handleShowApprove(item)"
              />
            </template>
          </div>
        </div>
      </LocomotiveComponent>
    </div>
    <BasePopup
      v-if="isShowPopup"
      v-model="isShowPopup"
      :content="t('product_platform.change_drag_drop_approval')"
      :icon="DialogIconType.Warning"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="handleClosePopup"
      @on-submit="handleSubmitPopup"
    />
    <BasePopup
      v-model="confirmPopupReject"
      :cancel-button-text="$t('product_platform.cancel')"
      :submit-button-text="$t('product_platform.submit')"
      @on-close="handleCancelReject"
      @on-submit="handleSubmitConfirmReject"
    >
      <template #header>
        <v-card-title class="pt-4 !pr-3 !pb-0">
          <div class="flex justify-between align-center">
            <div
              class="text-[#3a3b3d] text-[16px] !font-[500] title-reason-popup"
            >
              {{ $t("product_platform.reject_reason") }}
            </div>
            <close-icon class="cursor-pointer" @click="handleCancelReject" />
          </div>
        </v-card-title>
      </template>
      <template #body>
        <BaseTextArea
          v-model="contentReject"
          class="mx-6 font-size-base text-text-base mt-4"
          :placeholder="$t('product_platform.reject_reason_placeholder')"
          :maxlength="500"
        />
      </template>
    </BasePopup>
    <BasePopup
      v-if="isShowPopupApprove"
      v-model="isShowPopupApprove"
      :content="t('product_platform.confirm_approve')"
      :icon="DialogIconType.Warning"
      :cancel-button-text="t('product_platform.btn_no')"
      :submit-button-text="t('product_platform.btn_yes')"
      @on-close="
        () => {
          isShowPopupApprove = false;
        }
      "
      @on-submit="handleSubmitPopupApprove"
    />
  </div>
</template>
<script lang="ts" setup>
import { useGroupCode } from "@/composables/useGroupCode";
import { DialogIconType } from "@/enums";
import {
  useApprovalStore,
  usePublishManagerStore,
  useSnackbarStore,
} from "@/store";
import { cloneDeep } from "lodash-es";
import { useI18n } from "vue-i18n";
import { getUserInfor } from "@/constants/userInfor";
import {
  CODE_ACTION_REJECT_APPROVE,
  PUBLISH_CODE_STATUS,
  CODE_REVIEW_APPROVAL,
} from "@/constants/publish";
import { isExpiredTime } from "@/utils/format-data";

defineProps({
  isEdit: {
    type: Boolean,
    default: false,
  },
  isCreate: {
    type: Boolean,
    default: false,
  },
});

const { t } = useI18n();
const userInfor = getUserInfor();
const useSnackbar = useSnackbarStore();

const publishManagerStore = usePublishManagerStore();

const {
  publishApprovalFlowData,
  publishGeneralAttributesData,
  approvalItemSelected,
  publishSelected,
  isRedirectFromNotification,
} = storeToRefs(usePublishManagerStore());

const { approvalStepList } = storeToRefs(useApprovalStore());
const { groupCodeData, search } = useGroupCode();

const itemSelectionAction = ref({}) as any;
const dropData = ref();

const isStopPropagationWheelStatus = ref(false);
const isShowPopupApprove = ref(false);
const confirmPopupReject = ref(false);
const isShowPopup = ref(false);

const titleFlow = computed(() =>
  publishApprovalFlowData.value?.pubAprvStepLDtos?.length > 1
    ? publishApprovalFlowData.value?.aprvFlowTmptName
    : t("product_platform.no_approval_flow")
);
const contentReject = ref("");

const items = computed(() => {
  return publishApprovalFlowData.value?.pubAprvStepLDtos || [];
});

const checkShowAction = (item) => {
  if (
    isExpiredTime(publishGeneralAttributesData.value?.exprDtm) ||
    isExpiredTime(publishGeneralAttributesData.value?.duedDtm)
  ) {
    return false;
  }
  let arrUser = item.pubAprvSubStepLDtos?.map((usr) => usr.aprvUser);

  const itemFirst = items.value?.filter(
    (apr) =>
      apr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REQUEST &&
      apr.pubAprvStepCode !== CODE_REVIEW_APPROVAL.DESIGN
  )[0];

  return (
    arrUser.includes(userInfor.chgUser) &&
    [PUBLISH_CODE_STATUS.INPROGRESS].includes(
      publishGeneralAttributesData.value?.pubRqstStusCode ===
        PUBLISH_CODE_STATUS.DELAY
        ? publishGeneralAttributesData.value?.pubRqstBfrStusCode
        : publishGeneralAttributesData.value?.pubRqstStusCode
    ) &&
    item.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REQUEST &&
    item.sortNo === itemFirst?.sortNo
  );
};
const handleCancelReject = () => {
  confirmPopupReject.value = false;
  contentReject.value = "";
};

const handleWheelDetail = (event) => {
  isStopPropagationWheelStatus.value = event;
};
const handleClosePopup = () => {
  isShowPopup.value = false;
};

const handleClickItem = (item) => {
  approvalItemSelected.value = item;
  item.expand = !item.expand;
};
const handleShowReject = (item) => {
  itemSelectionAction.value = item;
  confirmPopupReject.value = true;
};

const handleShowApprove = (item) => {
  itemSelectionAction.value = item;
  isShowPopupApprove.value = true;
};

const handleSubmitPopup = () => {
  isShowPopup.value = false;
  approvalStepList.value = [];
  approvalStepList.value.push(dropData.value);
};

const handleSubmitConfirmReject = async () => {
  if (contentReject.value) {
    const subStepRequest = itemSelectionAction.value?.pubAprvSubStepLDtos.find(
      (itemApp) => itemApp.aprvUser === userInfor.chgUser
    );
    if (subStepRequest) {
      const { status } = await publishManagerStore.putApproveOrReject({
        ...subStepRequest,
        aprvStusCode: CODE_ACTION_REJECT_APPROVE.REJECT,
        aprvStusDscr: contentReject.value,
      });
      if (status === 200) {
        await publishManagerStore.getPublishPackageDetail(
          publishSelected.value?.pubRqstTaskCode
        );
        useSnackbar.showSnackbar(
          t("product_platform.successfully_reject"),
          "success"
        );
        confirmPopupReject.value = false;
        contentReject.value = "";
        approvalItemSelected.value = {} as any;
      }
    }
  } else {
    useSnackbar.showSnackbar(
      t("product_platform.required_field_missing"),
      "error"
    );
  }
};
const handleSubmitPopupApprove = async () => {
  const subStepRequest = itemSelectionAction.value?.pubAprvSubStepLDtos.find(
    (itemApp) => itemApp.aprvUser === userInfor.chgUser
  );
  if (subStepRequest) {
    const { status } = await publishManagerStore.putApproveOrReject({
      ...subStepRequest,
      aprvStusCode: CODE_ACTION_REJECT_APPROVE.APPROVE,
    });
    if (status === 200) {
      await publishManagerStore.getPublishPackageDetail(
        publishSelected.value?.pubRqstTaskCode
      );
      useSnackbar.showSnackbar(
        t("product_platform.successfully_approver"),
        "success"
      );
      isShowPopupApprove.value = false;
      approvalItemSelected.value = {} as any;
    }
  }
};

const allowDrop = async (event: DragEvent) => {
  event.preventDefault();
};

const drop = (event: any) => {
  dropData.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;

  if (publishApprovalFlowData.value?.pubAprvStepLDtos?.length > 1) {
    isShowPopup.value = true;
  } else {
    if (dropData.value) {
      approvalStepList.value = [
        {
          ...dropData.value,
          pubAprvSubStepLDtos: dropData.value?.aprvFlowTmptSubStepLs,
        },
      ];
    }
    approvalItemSelected.value = null;
  }
  event.preventDefault();
};

const showTitle = (value) => {
  const listCmcd = cloneDeep(groupCodeData.value["G00065"]) || [];
  if (listCmcd.length > 0) {
    return listCmcd?.find((code) => code.cmcdDetlId === value)?.cmcdDetlNm;
  }
  return "";
};

watch(
  () => approvalStepList.value,
  (newVal) => {
    publishApprovalFlowData.value = {
      ...publishApprovalFlowData.value,
      aprvFlowTmptCode: newVal[0]?.aprvFlowTmptCode,
      aprvFlowTmptName: newVal[0]?.aprvFlowTmptName,
      pubAprvDscr: newVal[0]?.aprvFlowTmptDscr,
    };

    if (newVal.length) {
      if (publishApprovalFlowData.value.pubAprvStepLDtos?.length > 0) {
        publishApprovalFlowData.value.pubAprvStepLDtos = [
          publishApprovalFlowData.value?.pubAprvStepLDtos[0],
          ...newVal[0]?.aprvFlowTmptStepLs
            ?.filter((apr) => apr.sortNo > 1)
            ?.map((step) => ({
              ...step,
              sortNo: step.sortNo,
              pubAprvStepCode: step.aprvStepCode,
              lmtTm: step.lmtTm,
              useYn: step.useYn,

              pubAprvSubStepLDtos: step.aprvFlowTmptSubStepLs?.map(
                (subStep) => ({
                  ...subStep,
                  sortNo: subStep.sortNo,
                  subSortNo: subStep.subSortNo,
                  aprvUser: subStep.aprvUser,
                  aprvUserDeptCd: subStep.aprvUserDeptCd,
                })
              ),
            })),
        ];
      }
    }
  },
  { deep: true }
);

watch(
  () => publishSelected,
  (newVal) => {
    if (newVal) {
      approvalItemSelected.value = {} as any;
    }
  },
  { deep: true }
);
watch(
  () => isRedirectFromNotification.value,
  (newVal) => {
    if (newVal) {
      const itemFirst = publishApprovalFlowData.value?.pubAprvStepLDtos?.filter(
        (apr) =>
          apr.aprvStusCode === CODE_ACTION_REJECT_APPROVE.REQUEST &&
          apr.pubAprvStepCode !== CODE_REVIEW_APPROVAL.DESIGN
      )[0];

      if (itemFirst) {
        publishApprovalFlowData.value.pubAprvStepLDtos =
          publishApprovalFlowData.value?.pubAprvStepLDtos?.map((item) => {
            if (itemFirst?.sortNo === item.sortNo) {
              return {
                ...item,
                expand: true,
              };
            }
            return item;
          });
      }
    }
  },
  { deep: true, immediate: true }
);

onMounted(async () => {
  await search(["G00065"]);
});
</script>
<style lang="scss" scoped>
.title-reason-popup {
  font-family: "Noto Sans KR", sans-serif !important;
}
</style>
