<template>
  <div class="bg-white relative pt-6 rounded-lg h-full">
    <div class="flex flex-col h-full">
      <div class="px-6">
        <div class="flex justify-between items-center">
          <div class="flex align-center gap-2 items-end">
            <h1 class="font-medium text-base text-text-base tracking-[0.5px]">
              {{ t("product_platform.duplicateGroup") }}
            </h1>
          </div>
        </div>
      </div>

      <template v-if="groupsOffer?.length">
        <BaseTotalSearchResult
          class-name="px-6 pt-3"
          :total-search="groupsOffer?.length"
          :total-items="groupsOffer?.length"
        />
        <div class="group-list overflow-auto mt-1">
          <LocomotiveComponent>
            <div class="py-[8px] grid gap-4 grid-cols-2 px-3">
              <div
                v-for="item in groupsOffer"
                :key="`Component-${item.objUuid}`"
                class="flex justify-center"
                :style="{ 'z-index': 999 }"
              >
                <cf-card-dropdown
                  :class-name="`default ${
                    groupsFinish.some((x) => x.objUuid === item.objUuid)
                      ? 'group-icon'
                      : 'group-icon-default'
                  } ${
                    selectedGroup?.objUuid === item.objUuid
                      ? 'border-active-yellow'
                      : ''
                  }`"
                  :title="item.objName"
                  type-bg="linear"
                  :active="item.objUuid === selectedGroup?.objUuid"
                  border-color-action="yellow"
                  :node="{
                    hideNodeLeft: true,
                    isActiveNodeLeft: false,
                    hideNodeRight: true,
                    isActiveNodeRight: false,
                  }"
                  hide-detail
                  editable
                  draggable
                  @on-click-card="onChooseCard(item)"
                  @dragstart="
                    handleDragUserPocket($event, {
                      userPocketType: LARGE_ITEM_CODE.GROUP,
                      ...item,
                    })
                  "
                >
                  <template #icon>
                    <span
                      class="flex justify-center align-center w-[40px] h-[40px]"
                    >
                      <FolderIcon
                        v-if="
                          groupsFinish.some((x) => x.objUuid === item.objUuid)
                        "
                      />
                      <FolderIconGray v-else />
                    </span>
                  </template>
                </cf-card-dropdown>
              </div>
            </div>
          </LocomotiveComponent>
        </div>
      </template>

      <div v-else class="flex-1">
        <NoData />
      </div>

      <div class="flex justify-end px-6 py-3 gap-2">
        <BaseButton
          :color="ButtonColorType.Secondary"
          :disabled="checkStatusFinishAll"
          @click="onSaveAll"
        >
          {{ $t("product_platform.saveAll") }}
        </BaseButton>

        <BaseButton :color="ButtonColorType.Secondary" @click="onSubmit">
          {{ $t("product_platform.finish") }}
        </BaseButton>
      </div>
    </div>

    <base-popup
      v-model="openPopupSaveAll"
      :cancel-button-text="$t('product_platform.btn_no')"
      :content="$t('product_platform.content-group-saveAll')"
      :icon="DialogIconType.Info"
      :submit-button-text="$t('product_platform.btn_yes')"
      @on-close="handleCloseSaveAll"
      @on-submit="handleSaveAll"
    />
    <base-popup
      v-model="openPopup"
      :cancel-button-text="$t('product_platform.btn_no')"
      :content="contentPopupFinish"
      :icon="DialogIconType.Info"
      :submit-button-text="$t('product_platform.btn_yes')"
      @on-close="handleCloseGroup"
      @on-submit="handleFinishGroup"
    />
  </div>
</template>

<script setup lang="ts">
import { cloneDeep } from "lodash-es";
import { useI18n } from "vue-i18n";
import {
  useOfferDuplicateProcessStore,
  useMenuStore,
  useExtendManagerStore,
  useSnackbarStore,
} from "@/store";
import { ButtonColorType, DialogIconType } from "@/enums";
import { configPath, findMenuItem } from "@/utils/config-path";
import { MenuItemID } from "@/enums/redirect";
import { LARGE_ITEM_CODE } from "@/constants/offer";
import useDragUserPocket from "@/composables/useDragUserPocket";

const offerDuplicate = useOfferDuplicateProcessStore();
const { menuTree } = storeToRefs(useMenuStore());
const { t } = useI18n();
const extendManagerStore = useExtendManagerStore();
const useSnackbar = useSnackbarStore();
const { handleDragUserPocket } = useDragUserPocket();

const contentPopupFinish = ref("");

const {
  groupsOffer,
  selectedGroup,
  groupsFinish,
  offerBeClonedUuid,
  offerDuplicateInRelationMode,
  groupDetailData,
  offerDuplicated,
} = storeToRefs(offerDuplicate);
const openPopup = ref(false);
const openPopupSaveAll = ref(false);

const onChooseCard = async (value) => {
  selectedGroup.value = value;
  // await offerDuplicate.getGroupDetailInfo(true);
  if (!value.detail) {
    await offerDuplicate.getGroupDetailInfo(true);
    value[`detail`] = cloneDeep(groupDetailData.value);
  } else {
    groupDetailData.value = cloneDeep(value.detail);
  }
};

const onSubmit = () => {
  contentPopupFinish.value =
    groupsFinish.value?.length === groupsOffer.value?.length
      ? t("product_platform.desc_finish")
      : t("product_platform.duplicate_finish_temp");
  openPopup.value = true;
};

const onSaveAll = () => {
  openPopupSaveAll.value = true;
};

const handleSaveAll = () => {
  groupsOffer.value = groupsOffer.value.map((gr) => {
    if (gr.detail?.offerTab?.length) {
      return {
        ...gr,
        detail: {
          ...gr.detail,
          offerTab: cloneDeep(gr.detail?.offerTab)?.map((offer, index) => {
            if (index === 0) {
              return {
                ...offer,
                itemRemoved: false,
              };
            }
            return offer;
          }),
        },
      };
    }
    return gr;
  });

  groupsFinish.value = cloneDeep(groupsOffer.value);

  openPopupSaveAll.value = false;
};

const handleFinishGroup = async () => {
  const data =
    groupsFinish.value
      ?.filter((gr) => !gr.detail?.offerTab[0]?.itemRemoved)
      .map((item) => ({
        groupUuid: item.objUuid,
        offerUuid: offerDuplicated.value?.objUuid,
        validStartDtm: item.validStartDtm,
        validEndDtm: item.validEndDtm,
      })) || [];

  if (data.length) {
    const res = await offerDuplicate.finishOfferGroupDuplicate(data);
    if (res && res.status === 200) {
      useSnackbar.showSnackbar(
        t("product_platform.successfully_saved"),
        "success"
      );
    }
  }
  finnishProcessDuplicate();
  openPopup.value = false;
};

const handleCloseGroup = () => {
  openPopup.value = false;
};

const handleCloseSaveAll = () => {
  openPopupSaveAll.value = false;
};

const checkStatusFinishAll = computed(() => {
  let itemRemoved = groupsOffer.value?.some(
    (gr) => gr.detail?.offerTab[0]?.itemRemoved
  );
  return (
    groupsFinish.value?.length === groupsOffer.value?.length && !itemRemoved
  );
});

const finnishProcessDuplicate = () => {
  extendManagerStore.$reset();
  offerDuplicateInRelationMode.value = true;
  const newRedirectObj = findMenuItem(
    menuTree.value,
    MenuItemID.RelationDuplicate
  );
  if (newRedirectObj) {
    newRedirectObj["path"] = configPath(newRedirectObj);
    replaceTab(newRedirectObj);
  }
};

watch(
  () => groupsFinish.value,
  async (val) => {
    if (val.length) {
      let lastItem = val[val.length - 1];
      if (lastItem) {
        let posision = groupsOffer.value.indexOf(lastItem);
        // check last item
        if (posision !== groupsOffer.value?.length - 1) {
          await onChooseCard(groupsOffer.value[posision + 1]);
        }
      }
    }
  },
  { deep: true }
);

onMounted(async () => {
  let result = await offerDuplicate.getListGroupByOffer({
    childOffrUuid: offerBeClonedUuid.value,
    onlyValidDtm: true,
    isPaged: false,
  });

  groupsOffer.value = result.data || [];

  if (groupsOffer.value.length > 0 && !selectedGroup.value) {
    selectedGroup.value = groupsOffer.value[0];
    await onChooseCard(groupsOffer.value[0]);
  }
});
const replaceTab = inject<any>("replaceTab");
</script>
<style scoped>
.filter {
  width: 100%;
  display: grid;
}

.sub-filter {
  grid-template-columns: 1fr 2fr;
}

.group-list {
  height: calc(100vh - 285px);
}
</style>
