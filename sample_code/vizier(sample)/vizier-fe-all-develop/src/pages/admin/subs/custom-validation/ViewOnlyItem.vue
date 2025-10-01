<template>
  <div class="validation-item-wrapper" @blur="handleBlur">
    <div class="condition-attributes">
      <AttributeTypeViewOnly type="DM" />
      <div class="text-div">AND</div>
      <AttributeTypeViewOnly type="NF" />
    </div>
    <div class="action-attributes -mt-5">
      <div class="text-div font-medium text-[13px] text-[#6b6d70] pb-1">
        Price
      </div>
      <AttributeTypeViewOnly type="DP" />
    </div>
  </div>
</template>

<script setup>
import AttributeTypeViewOnly from "./AttributeTypeViewOnly.vue";

const handleBlur = () => {
  showActionButtons.value = false;
};
</script>

<style lang="scss" scoped>
.validation-item-wrapper {
  pointer-events: none;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0px 2px 4px 0px #00000005;
  position: relative;
  display: flex;
  justify-content: space-evenly;
  border: 0.5px solid transparent;

  .condition-attributes {
    display: flex;
    flex-direction: column;
    row-gap: 16px;
    min-height: 140px;
    .text-div {
      font-size: 13px;
      font-weight: 500;
      line-height: 20px;
      text-align: center;
      text-transform: capitalize;
      color: #6b6d70;
    }
  }

  &:hover {
    .add-buttons {
      visibility: visible;
    }
  }
  .action-buttons {
    display: flex;
    border-radius: 6px;
    border: 1px solid #dce0e5;
    box-shadow: 0px 4px 8px 0px #00000014;
    position: absolute;
    top: -20px;
    right: 16px;
    background: #fff;
    > button {
      display: flex;
      justify-content: center;
      align-items: center;
      width: 32px;
      height: 32px;
      border-right: 1px solid #dce0e5;
      &:first-child {
        border-radius: 6px 0 0 6px;
      }
      &:last-child {
        border: none;
        border-radius: 0 6px 6px 0;
      }
      &:hover {
        background: #f7f8fa;
      }
    }
  }
  .add-buttons {
    visibility: hidden;
    display: flex;
    gap: 1px;
    position: absolute;
    bottom: -21px;
    left: 50%;
    transform: translateX(-50%);
    > button {
      background: #f7f8fa;
      color: #6b6d70;
      font-size: 11px;
      font-weight: 500;
      height: 20px;
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 2px 6px;
      box-shadow: 0px 2px 6px 0px #00000014;
      border-radius: 0 0 2px 2px;
      &:hover {
        background: #fff;
      }
    }
  }
}
.selected {
  border: 0.5px solid #3a3b3d;
}
</style>
