<template>
  <div class="p-4 mx-auto prose md:px-6 prose-indigo sm:rounded-md">
    <!-- <description name="Spinner" /> -->

    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Examples
    </h2>
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
    <h2 class="truncate text-base font-medium leading-7 text-slate-900">
      Source Code
    </h2>
    <prism language="html">{{ sourceCode }}</prism>
  </div>
</template>

<script lang="ts" setup>
import CfSpinner from "@/components/controls/CfSpinner.vue";
import Prism from "vue-prism-component";
import cfSpinnerExample from "@/components/controls/examples/CfSpinnerExample";

const sourceCode = ref(cfSpinnerExample);
const interval = ref<any>(null);
const value = ref(0);

function startProgressLoop() {
  interval.value = setInterval(() => {
    value.value = (value.value + 10) % 110;
  }, 1000);
}

onMounted(startProgressLoop);
onBeforeUnmount(() => {
  if (interval.value) clearInterval(interval.value);
});
</script>
