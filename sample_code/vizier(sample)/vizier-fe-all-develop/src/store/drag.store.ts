const useDragStore = defineStore("dragStore", () => {
  const isDragging = ref(false);
  const dragOfferType = ref("");
  const listAdd = ref<any[]>([]);
  const listUpdate = ref<any[]>([]);
  const categoryDrag = ref("");

  const resetDragDrop = () => {
    isDragging.value = false;
    dragOfferType.value = "";
    categoryDrag.value = "";
    listAdd.value = [];
    listUpdate.value = [];
  };

  const addDataToList = (newItem: any) => {
    listAdd.value.push({
      ...newItem,
      trgtItemDetlTypeCdNm: "New",
    });
  };

  const updateDataFromList = (updateItem: any) => {
    const index = listUpdate.value.findIndex(
      (item: any) => item.objUUID === updateItem?.objUUID
    );
    if (index === -1) {
      listUpdate.value.push(updateItem);
    } else {
      listUpdate.value.splice(index, 1, updateItem);
    }
  };

  const updateAddData = (updateItem: any) => {
    const index = listAdd.value.findIndex(
      (item: any) => item.objUUID === updateItem?.objUUID
    );
    listAdd.value.splice(index, 1, updateItem);
  };

  const removeDataFromAdd = (removeItem: any) => {
    const index = listAdd.value.findIndex(
      (item: any) => item.objUUID === removeItem?.objUUID
    );
    listAdd.value.splice(index, 1);
  };

  return {
    isDragging,
    dragOfferType,
    categoryDrag,
    listAdd,
    listUpdate,
    resetDragDrop,
    addDataToList,
    updateDataFromList,
    removeDataFromAdd,
    updateAddData,
  };
});

export default useDragStore;
