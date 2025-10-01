<script setup lang="ts">
import { TABS_OFFER_DETAILS } from "@/constants/offer";
import { useI18n } from "vue-i18n";
import {
  useStructureStore,
  useSnackbarStore,
  useCreateStructureStore,
  useDuplicateStructureStore,
} from "@/store";
import { OFFER_TYPE } from "@/constants/";
import { LargeItemCode } from "@/enums";
import GeneralTab from "@/components/prod/shared/GeneralTab.vue";
import AdditionalTab from "@/components/prod/shared/AdditionalTab.vue";
import { COMPONENTS_LAGRE_TYPE } from "@/constants/component";
import { getListItemCodeApi } from "@/api/prod/commonApi";
import MultiEntityTab from "@/components/prod/shared/MultiEntityTab.vue";
import ResourceTab from "@/components/prod/shared/ResourceTab.vue";
import { IResourceItem } from "@/interfaces/prod/resource";

const emits = defineEmits(["close-offer-component-detail"]);
const props = defineProps({
  type: {
    type: String,
    default: OFFER_TYPE.PRICEPLAN,
  },
  isAdd: {
    type: Boolean,
    default: false,
  },
  duplicate: {
    type: Boolean,
    default: false,
  },
});
const { t } = useI18n();
const structureStore = useStructureStore();
const createStructureStore = useCreateStructureStore();
const duplicateStructureStore = useDuplicateStructureStore();
const useSnackbar = useSnackbarStore();

const selectedStore = computed(() => {
  if (props.isAdd) {
    return createStructureStore;
  } else if (props.duplicate) {
    return duplicateStructureStore;
  }
  return structureStore;
});

const { multiEntityTypes, resourceTypes, componentEntityList } = storeToRefs(
  selectedStore.value
);
const {
  getMultiEntityTypes,
  getListComponentEntity,
  getComponentResourceList,
} = selectedStore.value;
const {
  selectedComponent,
  selectedComponentData,
  listComponentResource,
  paramsResourceFilter,
  totalResourceListItem,
} = storeToRefs(selectedStore.value);
const optionsSubType = ref<any[]>([]);
const currentTab = ref(TABS_OFFER_DETAILS.GENERAL);

const getComponentTabs = computed(() => {
  let initTabs: any[] = [
    {
      value: TABS_OFFER_DETAILS.GENERAL,
      label: t("product_platform.general"),
      component: GeneralTab,
      props: {
        modelValue: selectedComponentData.value?.general,
        createItemLargeCodeList: COMPONENTS_LAGRE_TYPE,
        createItemCodeList: optionsSubType.value,
        isDuplicate: props.duplicate,
      },
    },
    {
      value: TABS_OFFER_DETAILS.ADDITIONAL,
      label: t("product_platform.additional"),
      component: AdditionalTab,
      props: {
        modelValue: selectedComponentData.value?.additional,
        isDuplicate: props.duplicate,
      },
    },
  ];

  if (selectedComponent.value && resourceTypes.value?.length) {
    initTabs = [
      ...initTabs,
      {
        value: TABS_OFFER_DETAILS.RESOURCE,
        label: t("product_platform.resource_title"),
        component: ResourceTab,
        props: {
          isEdit: false,
          modelList: listComponentResource.value,
          relationItem: selectedComponent.value,
          typeAllowDrop: resourceTypes.value,
          isDuplicate: props.duplicate,
        },
        onClick: async () => {
          await getResourceListLocal();
        },
      },
      // {
      //   value: TABS_OFFER_DETAILS.RESOURCE,
      //   label: t("product_platform.resource_title"),
      //   component: OfferComponentDetailTabResource,
      //   props: {
      //     isAdd: props.isAdd,
      //     isDuplicate: props.duplicate,
      //   },
      // },
    ];
  }
  const multiComponent = multiEntityTypes.value?.find(
    (item) => item.value === selectedComponent.value?.itemCode
  );
  if (multiComponent) {
    initTabs.push({
      value: "Multi-Entity",
      label: t(`product_platform.multiEntity`),
      // component: OfferComponentDetailTabMultiEntity,
      // props: {
      //   isAdd: props.isAdd,
      //   isDuplicate: props.duplicate,
      // },
      component: MultiEntityTab,
      props: {
        isEdit: props.isAdd,
        multiEntityTypes: multiEntityTypes.value,
        modelList: componentEntityList.value,
        typeSelected: selectedComponent.value?.itemCode,
        isDuplicate: props.duplicate,
      },
      onClick: async () => {
        await getListComponentEntity();
      },
    });
  }
  return initTabs;
});

const closeOfferComponentDetail = () => {
  emits("close-offer-component-detail");
};

const handleChangePageResource = (page: number) => {
  paramsResourceFilter.value.page = page;
  getResourceListLocal();
};

watch(
  selectedComponent,
  () => {
    currentTab.value = TABS_OFFER_DETAILS.GENERAL;
    paramsResourceFilter.value.page = 1;
  },
  {
    deep: true,
  }
);

const getResourceListLocal = async () => {
  paramsResourceFilter.value.objUuid = selectedComponent.value?.objUuid || null;
  paramsResourceFilter.value.size = 8;
  const res = await getComponentResourceList(paramsResourceFilter.value);
  listComponentResource.value = [];
  if (res?.data) {
    totalResourceListItem.value = res.data?.totalElements;
    listComponentResource.value =
      res.data?.elements.map((resource: IResourceItem) => ({
        ...resource,
        startDate: resource.relationStartDate,
        endDate: resource.relationEndDate,
      })) || [];
  }
};

onMounted(async () => {
  await getMultiEntityTypes();
  try {
    const { data } = await getListItemCodeApi({
      lItemCode: LargeItemCode.Component,
    });
    optionsSubType.value = data;
  } catch (error: any) {
    useSnackbar.showSnackbar(
      error?.errorMsg || t("product_platform.something_went_wrong"),
      "error"
    );
  }
});
</script>
<template>
  <div class="relative z-10 top-0 right-0 bg-white col-span-1 rounded-lg">
    <div class="w-full relative text-[12px] h-full flex flex-col pl-4 pr-2">
      <div class="w-full flex justify-between items-center pr-2 pt-3 pb-2">
        <div class="text-text-base text-base-vnb font-medium leading-[40px]">
          {{ $t("product_platform.component_details") }}
        </div>
      </div>
      <div class="flex flex-col content w-full justify-center h-full">
        <div class="w-full flex-grow flex flex-col">
          <BaseTabs
            v-model="currentTab"
            :tabs="getComponentTabs"
            :show-arrows="true"
            :center-active="true"
            :class-loco="
              currentTab === TABS_OFFER_DETAILS.RESOURCE
                ? 'max-h-[calc(100vh-330px)]'
                : 'max-h-[calc(100vh-300px)]'
            "
          />
          <BasePagination
            v-if="
              totalResourceListItem > 0 &&
              currentTab === TABS_OFFER_DETAILS.RESOURCE
            "
            :pagination="{
              currentPage: paramsResourceFilter.page,
              totalPages: Math.ceil(
                totalResourceListItem / paramsResourceFilter.size
              ),
              pageSize: paramsResourceFilter.size,
            }"
            class="mb-3 mt-5"
            @on-change-page="handleChangePageResource"
          />
        </div>
      </div>
    </div>

    <ShowDetailIcon
      class="absolute top-[160px] left-0 cursor-pointer text-[#525457] hover:text-[#303132]"
      @click="closeOfferComponentDetail"
    />
  </div>
</template>
