export const userImagesStore = defineStore("imageStore", () => {
  const dsbdViewUuid = ref("");
  const dsbdViewDscrCntn = ref("");
  const titleTop10 = ref("");
  const titleCalendar = ref("");
  const titleItemCountRate = ref("");
  const titleRecentWork = ref("");
  const titleItemVolumn = ref("");
  const titleMonthlyReportItem = ref("");
  const titleMonthlyReportUser = ref("");
  const uploadedImagesExtend = ref({
    dsbdViewUuid: dsbdViewUuid.value,
    requests: [
      { imageSeq: 0, imageBase64: "", imageName: "", imagePath: "" },
      { imageSeq: 1, imageBase64: "", imageName: "", imagePath: "" },
      { imageSeq: 2, imageBase64: "", imageName: "", imagePath: "" },
    ],
  });
  function setUploadedImagesExtend(apiImages) {
    uploadedImagesExtend.value.requests.forEach((image) => {
      const apiImage = apiImages.find((img) => img.imageSeq === image.imageSeq);
      if (apiImage) {
        image.imageBase64 = "";
        image.imageName = apiImage.imageName;
        image.imagePath = apiImage.imagePath;
      } else {
        image.imageBase64 = "";
        image.imageName = "";
        image.imagePath = "";
      }
    });
  }

  const resetUploadedImagesExtend = (idx) => {
    const imageToDelete = uploadedImagesExtend.value.requests.find(
      (image) => image.imageSeq === idx
    );
    if (imageToDelete) {
      imageToDelete.imageBase64 = "";
      imageToDelete.imageName = "";
      imageToDelete.imagePath = "";
    }
  };
  const activeSlideIndex = ref(0);
  const setActiveSlideIndex = (index: number) => {
    activeSlideIndex.value = index;
  };

  const setDsbdViewUuid = (str: string) => {
    dsbdViewUuid.value = str;
  };
  const setDsbdViewDscrCntn = (str: string) => {
    dsbdViewDscrCntn.value = str;
  };
  const setTitleTop10 = (str: string) => {
    titleTop10.value = str;
  };
  const setTitleCalendar = (str: string) => {
    titleCalendar.value = str;
  };
  const setTitleItemCountRate = (str: string) => {
    titleItemCountRate.value = str;
  };
  const setTitleRecentWork = (str: string) => {
    titleRecentWork.value = str;
  };
  const setTitleItemVolumn = (str: string) => {
    titleItemVolumn.value = str;
  };
  const setTitleMonthlyReportItem = (str: string) => {
    titleMonthlyReportItem.value = str;
  };
  const setTitleMonthlyReportUser = (str: string) => {
    titleMonthlyReportUser.value = str;
  };

  function deleteImage(imageSeq) {
    const imageToDelete = uploadedImagesExtend.value.requests.find(
      (image) => image.imageSeq === imageSeq
    );
    if (imageToDelete) {
      imageToDelete.imageBase64 = "";
      imageToDelete.imageName = "";
      imageToDelete.imagePath = "";
    }
  }

  return {
    deleteImage,
    activeSlideIndex,
    setActiveSlideIndex,
    dsbdViewUuid,
    setDsbdViewUuid,
    uploadedImagesExtend,
    dsbdViewDscrCntn,
    setDsbdViewDscrCntn,
    titleTop10,
    titleCalendar,
    titleItemCountRate,
    titleRecentWork,
    titleItemVolumn,
    titleMonthlyReportItem,
    titleMonthlyReportUser,
    setTitleTop10,
    setTitleCalendar,
    setTitleItemCountRate,
    setTitleRecentWork,
    setTitleItemVolumn,
    setTitleMonthlyReportItem,
    setTitleMonthlyReportUser,
    resetUploadedImagesExtend,
    setUploadedImagesExtend,
  };
});
