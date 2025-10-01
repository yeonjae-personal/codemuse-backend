<template>
  <img
    :width="width"
    :height="height"
    src="../../../assets/images/chatbot.png"
    alt="chat-pot logo"
  />
</template>

<script setup>
defineProps({
  width: {
    type: String,
    default: "20px",
  },
  height: {
    type: String,
    default: "20px",
  },
});
</script>
