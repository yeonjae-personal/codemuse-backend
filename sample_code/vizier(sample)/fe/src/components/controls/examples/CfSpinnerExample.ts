const cfCardExample = `<script setup lang="ts">
import CfSpinner from "@/components/controls/CfSpinner.vue";


const interval = ref({});
const value = <any>ref(0);

onBeforeUnmount(() => {
  clearInterval(interval.value);
});

onMounted(() => {
  interval.value = setInterval(() => {
    if (value.value === 100) {
      value.value = 0;
    }
    value.value += 10;
  }, 1000);
});
</script>
<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <div>
      <cf-spinner indeterminate color="primary"> </cf-spinner>
      <cf-spinner indeterminate color="red"> </cf-spinner>
      <cf-spinner indeterminate color="purple"> </cf-spinner>
      <cf-spinner indeterminate color="green"> </cf-spinner>
      <cf-spinner indeterminate color="amber"> </cf-spinner>
      <cf-spinner indeterminate color="pink"> </cf-spinner>
    </div>
    <br />
    <div>
      <cf-spinner :size="50" color="primary" indeterminate> </cf-spinner>
      <cf-spinner :width="3" color="red" indeterminate> </cf-spinner>
      <cf-spinner :size="70" :width="7" color="purple" indeterminate>
      </cf-spinner>
      <cf-spinner :width="3" color="green" indeterminate> </cf-spinner>
      <cf-spinner :size="50" color="amber" indeterminate> </cf-spinner>
    </div>
    <br />
    <div>
      <cf-spinner
        :rotate="360"
        :size="100"
        :width="15"
        :model-value="value"
        color="teal"
      >
        {{ value }}
      </cf-spinner>
      <cf-spinner
        :rotate="-90"
        :size="100"
        :width="15"
        :model-value="value"
        color="primary"
      >
        {{ value }}
      </cf-spinner>
      <cf-spinner
        :rotate="90"
        :size="100"
        :width="15"
        :model-value="value"
        color="red"
      >
        {{ value }}
      </cf-spinner>
      <cf-spinner
        :rotate="180"
        :size="100"
        :width="15"
        :model-value="value"
        color="pink"
      >
        {{ value }}
      </cf-spinner>
    </div>
  </div>
</template>

`;
export default cfCardExample;
