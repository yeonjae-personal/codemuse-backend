<script setup lang="ts">
import COMMV001P from "@/pages/vocap/subs/COMMV001P.vue";
import COMMW001P from "@/pages/vocap/subs/COMMW001P.vue";
import useGlobalStore from "@/store/global.store";
import { useI18n } from "vue-i18n";

const globalStore = useGlobalStore();
const { t: translateMessage } = useI18n();
const props = defineProps({
  params: {
    type: Object as PropType<any>,
    default: () => {},
  },
});

const btnClick = () => {
  const selectedRow = props.params.data;

  switch (selectedRow.vocaDivsCd) {
    case "VO":
      showModalAddOrUpdateVo(false);
      break;
    case "WO":
      handleShowModalAddOrUpdateWo(false);
      break;
  }
};

const showModalAddOrUpdateVo = async (_isAddNew: boolean = true) => {
  const objectModal: any = {
    title: translateMessage("term.COMMV001P.title"),
    component: COMMV001P,
    dataInput: props.params.data,
    width: "600",
  };
  await globalStore.openModal(objectModal);
};

const handleShowModalAddOrUpdateWo = async (_isAddNew: boolean = true) => {
  const objectModal: any = {
    title: translateMessage("term.COMMV001P.title"),
    component: COMMW001P,
    dataInput: props.params.data,
    width: "600",
  };
  try {
    await globalStore.openModal(objectModal);
  } catch (ex) {
    alert("An error occurred!");
  }
};
</script>

<template>
  <div class="contactCell">
    <div class="iconContainer">
      <button class="button-secondary" @click="btnClick">
        <img
          class="icon"
          src="https://cdn-icons-png.flaticon.com/512/1786/1786987.png"
          alt="linkedin"
        />
      </button>
    </div>
  </div>
</template>

<style>
.icon {
  height: 16px;
  width: 16px;
  padding-top: 1px;
}
button img {
  margin-top: 0 !important;
  margin-bottom: 0 !important;
  display: block;
  margin: auto;
}
.iconContainer button {
  appearance: none;
  display: inline-block;
  /* margin: 8px; */
  width: 30px;
  height: 30px;
  padding: 0.5em 0.5em 0.5em;
  white-space: nowrap;
  border-radius: 6px;
  box-shadow:
    0 0 0 4px transparent,
    0 1px 2px 0 #0c111d11;
  outline: none;
  background-color: var(--ag-background-color);
  border: 1px solid #d0d5dd;
  cursor: pointer;
}

:global(.ag-theme-quartz-dark) .iconContainer button {
  background-color: #141d2c;
  border-color: #344054;
}

.iconLink {
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
