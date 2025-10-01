export default function useDragUserPocket() {
  const handleDragUserPocket = <T = any>(event: DragEvent, item: T): void => {
    event.dataTransfer?.setData("item", JSON.stringify(item));
  };

  return { handleDragUserPocket };
}
