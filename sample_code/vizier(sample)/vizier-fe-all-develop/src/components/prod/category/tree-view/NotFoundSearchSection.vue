<template>
  <div class="not-found-notification-container">
    <div class="not-found-img-container">
      <img
        width="161"
        height="161"
        class="not-found-search-img"
        src="../../../../assets/icons/not-found-search.svg"
      />
    </div>
    <div class="not-found-text-container">
      <p class="not-found-text">검색한 결과가 없습니다.</p>
    </div>
  </div>
</template>

<style scoped>
.not-found-notification-container {
  width: 190px;
  height: 203px;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.not-found-text {
  font-family: "Noto Sans KR";
  font-size: 18px;
  font-weight: 500;
  line-height: 27px;
  letter-spacing: 0.5px;
  text-align: center;
  text-wrap: nowrap;
}

.not-found-text-container,
.not-found-img-container {
  width: 100%;
}

.not-found-search-img {
  margin: auto;
}

.not-found-search-img {
  width: 160px;
  height: 160px;
}
</style>
