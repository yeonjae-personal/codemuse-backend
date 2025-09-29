const cfCardExample = `<script setup lang="ts">
const handleCardButtonClick = (button: any) => {
  console.log('Button clicked:', button);
};
</script>
<template>
  <div class="flex flex-wrap gap-4">
    <cf-card text="Lorem ipsum dolor sit amet consectetur adipisicing elit." hover></cf-card>
    <cf-card
      text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      variant="outlined"
    ></cf-card>
    <cf-card
      text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      variant="tonal"
    ></cf-card>
    <cf-card
      title="Card title"
      text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
    ></cf-card>
    <cf-card
      title="Card title"
      subtitle="Subtitle"
      text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
    ></cf-card>
    <cf-card
      title="Card title"
      subtitle="Subtitle"
      text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      :buttons="[{ text: 'Button A' }]"
      @click="handleCardButtonClick"
    ></cf-card>
    <cf-card
      title="Card title"
      subtitle="Subtitle"
      text="Lorem ipsum dolor sit amet consectetur adipisicing elit."
      loading
      :buttons="[{ text: 'Button A' }, { text: 'Button B' }]"
      @click="handleCardButtonClick"
    ></cf-card>
  </div>
</template>
`;
export default cfCardExample;
