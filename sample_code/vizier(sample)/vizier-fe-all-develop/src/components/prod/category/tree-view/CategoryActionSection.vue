<template>
  <div class="category-action-container">
    <div class="category-search-action-container">
      <div class="category-search-bar">
        <v-select
          v-model="searchCategoryField"
          class="custom-select"
          :items="selectOptionList"
          item-title="title"
          item-value="value"
          variant="outlined"
          width="120px"
        ></v-select>
        <v-text-field
          v-model="searchCategoryText"
          class="custom-text-field"
          height="48px"
          width="240px"
          label="product_platform.categoryNode"
          variant="outlined"
        >
          <template #append-inner>
            <v-btn
              class="category-search-btn"
              size="sm"
              flat
              icon="mdi-magnify"
              @click="setCategoryFilterObj"
            ></v-btn>
          </template>
        </v-text-field>
      </div>
      <div class="offer-search-bar">
        <v-select
          v-model="searchOfferField"
          class="custom-select"
          :items="selectOptionList"
          item-title="title"
          item-value="value"
          variant="outlined"
          width="120px"
        ></v-select>
        <v-text-field
          v-model="searchOfferText"
          class="custom-text-field"
          width="240px"
          label="Offer"
          variant="outlined"
        >
          <template #append-inner>
            <v-btn
              class="category-search-btn"
              size="sm"
              flat
              icon="mdi-magnify"
              @click="setOfferFilterObj"
            ></v-btn>
          </template>
        </v-text-field>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

const emit = defineEmits(["setCategoryFilterObj", "setOfferFilterObj"]);

const selectOptionList = ref([
  {
    title: "Name",
    value: "name",
  },
  {
    title: "Code",
    value: "code",
  },
]);

const searchCategoryText = ref("");
const searchCategoryField = ref("name");
const searchOfferText = ref("");
const searchOfferField = ref("name");

const setCategoryFilterObj = () => {
  let filterObj = {
    searchText: searchCategoryText.value,
    searchField: searchCategoryField.value,
  };
  emit("setCategoryFilterObj", filterObj);
};

const setOfferFilterObj = () => {
  let filterObj = {
    searchText: searchOfferText.value,
    searchField: searchOfferField.value,
  };
  emit("setOfferFilterObj", filterObj);
};
</script>

<style scoped>
.category-search-action-container {
  display: flex;
  gap: 16px;
}

.category-search-bar,
.offer-search-bar {
  display: flex;
  height: 48px;
  width: 360px;
  height: 48px;
  gap: 12px;
}

.custom-select :deep() .v-field__input {
  max-height: 48px !important;
}

.custom-select :deep() .v-field {
  max-height: 48px !important;
}

.custom-text-field :deep() .v-input__control {
  max-height: 48px !important;
}

.custom-select :deep() .v-field-label {
  top: 45% !important;
}

.custom-select :deep() .v-icon {
  top: -4px !important;
}

.custom-text-field :deep() .v-label {
  top: 45% !important;
}

.category-search-btn {
  color: #bdc1c7;
  margin-bottom: 5px;
}
</style>
