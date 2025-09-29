<template>
  <canvas
    :id="props.id"
    ref="canvasElement"
    :width="width"
    :height="height"
  ></canvas>
</template>

<script setup lang="ts">
import { CANVAS_DIRECTION } from "@/constants/index";

const canvasElement = ref<HTMLCanvasElement | any>();
const context = ref<CanvasRenderingContext2D | any>();
const props = defineProps({
  id: {
    type: String,
    require: true,
    default: "",
  },
  width: {
    type: Number,
    require: true,
    default: 0,
  },
  height: {
    type: Number,
    require: true,
    default: 0,
  },
  direction: {
    type: String,
    default: CANVAS_DIRECTION.HORIZONTAL,
  },
  listCoordinates: {
    type: Array,
    default: () => [], //Example: [[{leftX1: ..., topX1: ...., lineWidth: ..., strokeStyle: ...}, {leftY1: ..., topY1: ...., lineWidth: ..., strokeStyle: ...}]]
  },
  midPoint: {
    type: Number,
    default: 0,
  },
});

onMounted(() => {
  onDrawing();
});
onUpdated(() => {
  onDrawing();
});

const onDrawing = () => {
  canvasElement.value.width = props.width as number;
  canvasElement.value.height = props.height as number;
  context.value = canvasElement.value?.getContext("2d", {
    willReadFrequently: true,
  });
  context.value.imageSmoothingEnabled = true;
  drawLine(props.listCoordinates);
};

const drawLine = (list) => {
  if (!context.value || !list?.length) {
    return;
  }
  const curveNumber = 15;
  list.forEach((coordinate) => {
    let coordinateStartPoint = [
      coordinate.leftStartPoint,
      coordinate.topStartPoint,
    ];
    let coordinateMovePoint = [
      coordinate.leftMovePoint,
      coordinate.topMovePoint,
    ];
    let leftX = coordinateStartPoint[0];
    let leftY = coordinateMovePoint[0];
    let topX = coordinateStartPoint[1];
    let topY = coordinateMovePoint[1];
    let midPoint = props.midPoint
      ? props.midPoint
      : props.direction === CANVAS_DIRECTION.HORIZONTAL
        ? (props.width as number) / 2
        : (props.height as number) / 2;
    let lineWidth = coordinate.lineWidth;
    let color = coordinate.strokeStyle;
    let diffTopXY = Math.abs(topX - topY);
    let diffLeftXY = Math.abs(leftX - leftY);
    if (props.direction === CANVAS_DIRECTION.VERTICAL) {
      if (leftX > leftY) {
        if (diffLeftXY > curveNumber * 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.lineTo(leftX, midPoint);
          context.value.lineTo(leftY + curveNumber * 2, midPoint);
          context.value.quadraticCurveTo(
            leftY - curveNumber,
            midPoint,
            leftY,
            topY + midPoint / 2
          );
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else if (diffLeftXY >= 0 && diffLeftXY < 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.lineTo(...coordinateMovePoint);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else {
          //   context.value.beginPath();
          //   context.value.moveTo(...coordinateStartPoint);
          //   context.value.quadraticCurveTo(
          //     midPoint,
          //     topX,
          //     midPoint,
          //     topX - diffLeftXY / 2
          //   );
          //   context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          //   context.value.lineWidth = lineWidth;
          //   context.value.strokeStyle = color;
          //   context.value.stroke();
        }
      } else {
        if (diffLeftXY > curveNumber * 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.lineTo(leftX, midPoint);
          context.value.lineTo(leftY - curveNumber * 2, midPoint);

          context.value.quadraticCurveTo(
            leftY + curveNumber,
            midPoint,
            leftY,
            topY + midPoint / 2
          );
          //   context.value.lineTo(midPoint, topY - curveNumber);
          //   context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else if (diffLeftXY >= 0 && diffLeftXY < 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.lineTo(...coordinateMovePoint);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else {
          // context.value.beginPath();
          // context.value.moveTo(...coordinateStartPoint);
          // context.value.quadraticCurveTo(
          //   midPoint,
          //   topX,
          //   midPoint,
          //   topX + diffLeftXY / 2
          // );
          // context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          // context.value.lineWidth = lineWidth;
          // context.value.strokeStyle = color;
          // context.value.stroke();
        }
      }
    } else {
      if (topX > topY) {
        if (diffTopXY > curveNumber * 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.quadraticCurveTo(
            midPoint,
            topX,
            midPoint,
            topX - curveNumber
          );
          context.value.lineTo(midPoint, topY + curveNumber);
          context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else if (diffTopXY > 0 && diffTopXY < 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.lineTo(...coordinateMovePoint);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.quadraticCurveTo(
            midPoint,
            topX,
            midPoint,
            topX - diffTopXY / 2
          );
          context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        }
      } else {
        if (diffTopXY > curveNumber * 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.quadraticCurveTo(
            midPoint,
            topX,
            midPoint,
            topX + curveNumber
          );
          context.value.lineTo(midPoint, topY - curveNumber);
          context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else if (diffTopXY > 0 && diffTopXY < 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.lineTo(...coordinateMovePoint);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        } else {
          context.value.beginPath();
          context.value.moveTo(...coordinateStartPoint);
          context.value.quadraticCurveTo(
            midPoint,
            topX,
            midPoint,
            topX + diffTopXY / 2
          );
          context.value.quadraticCurveTo(midPoint, topY, leftY, topY);
          context.value.lineWidth = lineWidth;
          context.value.strokeStyle = color;
          context.value.stroke();
        }
      }
    }
  });
};
onMounted(() => {
  onDrawing();
});
watch(
  () => [props.listCoordinates, props.width, props.height],
  (val: any) => {
    if (val[0]?.length && canvasElement.value) {
      onDrawing();
    }
  },
  { deep: true }
);
</script>
