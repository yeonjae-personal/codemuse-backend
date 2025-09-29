<template>
  <div
    :id="category"
    class="flex flex-column expand-manager-column"
    :class="{ 'align-end': category === EXTEND_CATEGORY.LEADER }"
  >
    <div
      class="flex flex-column gap-[8px]"
      :class="{ 'align-end': category === EXTEND_CATEGORY.LEADER }"
      :style="{ width: extendsView === EXTENDS_VIEW.SIMPLE ? '370px' : '100%' }"
    >
      <div
        v-if="extendsView === EXTENDS_VIEW.DETAIL"
        class="w-[284px] text-[#6B6D70] text-[13px] font-weight-medium"
      >
        {{
          category === EXTEND_CATEGORY.LEADER
            ? $t("product_platform.relation.leader")
            : $t("product_platform.relation.follower")
        }}
      </div>
      <ItemDrop
        v-if="isEdit"
        :class="extendsView === EXTENDS_VIEW.DETAIL ? 'max-w-[284px]' : ''"
        @click="handleOpenRelationSearch"
        @drop="drop($event)"
        @dragover="allowDrop($event)"
      >
        <span v-if="locale == 'en'">Drag <b>Relation</b> item here</span>
        <span v-else><b>관계</b> 아이템을 끌어다 놓으세요</span>
      </ItemDrop>
      <div
        class="flex flex-column w-full"
        :style="{
          marginTop:
            extendsView === EXTENDS_VIEW.DETAIL
              ? category === EXTEND_CATEGORY.LEADER
                ? followerOffsetTop - (isEdit ? 64 : 0) + 'px'
                : (isEdit ? 8 : 0) + 'px'
              : '',
        }"
      >
        <div
          v-for="(relation, index) in relationList"
          :key="relation.dpdcRelCode"
          :class="relation?.disable ? 'Disable' : 'expandList'"
        >
          <ExtendAccordionGroupRow
            :relation="relation"
            :rel-index="index"
            :category="category"
            :active-relation="setActiveRelation(relation)"
            :active-relation-group="
              relation.dpdcRelCode === activeObj.relationGroup?.dpdcRelCode
            "
            :search-obj="searchObj"
            :active-obj="activeObj"
            :is-remove="relation.isAdded"
            :offer-duplicate-mode="offerDuplicateMode"
            :expired="isExpired(relation?.relationValidEndDate)"
            @on-remove="handleRemove"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  EXTEND_CATEGORY,
  EXTENDS_VIEW,
  SELECT_LIST_TYPE,
} from "@/constants/extendsManager";
import {
  useDragStore,
  useExtendManagerStore,
  useRelationManagerDuplicateStore,
  useSnackbarStore,
} from "@/store";
import { searchByKeyword } from "@/utils/extend-utils";
import { useI18n } from "vue-i18n";
import { isExpired } from "@/utils/format-data";

const useSnackbar = useSnackbarStore();
const { dragOfferType } = storeToRefs(useDragStore());
const { t, locale } = useI18n();
const extendManagerStore = useExtendManagerStore();
const relationManagerDuplicateStore = useRelationManagerDuplicateStore();
const props = defineProps({
  items: {
    type: Array,
    require: true,
    default: () => [],
  },
  category: {
    type: String,
    default: "",
    validator(value) {
      return (
        EXTEND_CATEGORY.LEADER === value || EXTEND_CATEGORY.FOLLOWER === value
      );
    },
  },
  searchObj: {
    type: Object,
    default: new Object(),
  },
  activeObj: {
    type: Object,
    default: new Object(),
  },
  offerDuplicateMode: {
    type: Boolean,
    default: false,
  },
});
const selectedStore = computed(() =>
  props.offerDuplicateMode ? relationManagerDuplicateStore : extendManagerStore
);
const { extendsView, isEdit, sideDisplay, followerOffsetTop } = storeToRefs(
  selectedStore.value
);
const { removeRelationExtendsTargetPost } = selectedStore.value;
const dropRelation = ref();
const relationList = ref<any>(props.items);

const handleOpenRelationSearch = () => {
  if (isEdit.value) {
    sideDisplay.value.relationSearch = true;
    if (props.offerDuplicateMode) {
      sideDisplay.value.targetSearch = false;
    }
  }
};

const handleRemove = (uuid) => {
  const index = relationList.value.findIndex(
    (item) => item.dpdcRelUuid === uuid
  );
  if (index !== -1) {
    relationList.value.splice(index, 1);
  }
  removeRelationExtendsTargetPost(uuid);
};

const allowDrop = (event) => {
  if (dragOfferType.value == SELECT_LIST_TYPE.RELATION) {
    event.preventDefault();
    return true;
  }
};

const drop = (event) => {
  event.preventDefault();
  dropRelation.value = event.dataTransfer.getData("item")
    ? JSON.parse(event.dataTransfer.getData("item"))
    : null;
  if (dropRelation.value) {
    let isDupplicate = false;
    relationList.value.forEach((element) => {
      if (element.dpdcRelUuid === dropRelation.value.objUuid) {
        useSnackbar.showSnackbar(
          t("product_platform.relation.duplicateRelationMsg"),
          "error"
        );
        isDupplicate = true;
        return;
      }
    });
    if (!isDupplicate) {
      relationList.value.unshift({
        dpdcRelCode: dropRelation.value.objCode,
        dpdcRelName: dropRelation.value.objName,
        dpdcRelUuid: dropRelation.value.objUuid,
        child: [],
        isAdded: true,
      });
    }
  }
};

const setActiveRelation = (item) => {
  return (
    searchByKeyword(
      item.dpdcRelName,
      item.dpdcRelCode,
      SELECT_LIST_TYPE.RELATION,
      props.searchObj as any
    ) || item.dpdcRelCode === props.activeObj.relation?.dpdcRelCode
  );
};

const setLineCanvas = () => {
  setCoordinates(props.category);
  setCanvasHeight();
};

watch(
  () => isEdit.value,
  (val) => {
    if (val) {
      setLineCanvas();
    }
  }
);

onMounted(() => {
  setLineCanvas();
});

onUpdated(() => {
  setLineCanvas();
});

watch(
  () => props.items,
  (newVal) => {
    relationList.value = newVal;
  },
  { deep: true }
);

const { setCanvasHeight } = inject<any>("handleCalCanvasHeight");
const { setCoordinates } = inject<any>("handleCalCoordinates");
</script>

<style lang="scss" scoped>
.dashed-line {
  border-top: 2px #bdc1c7 dashed;
}
</style>
