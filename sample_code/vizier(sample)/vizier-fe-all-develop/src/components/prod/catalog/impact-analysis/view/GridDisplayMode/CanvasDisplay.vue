<template>
  <canvas :id="props.id" ref="canvasElement"></canvas>
</template>

<script setup>
const canvasElement = ref(null);
const context = ref(null);
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
  listCoordinates: {
    type: Array,
    default: null, //Example: [[[leftX1,topX1], [leftY1,topY1]], [[leftX2,topX2], [leftY2,topY2]]]
  },
});

onMounted(() => {
  canvasElement.value.width = props.width;
  canvasElement.value.height = props.height;
  context.value = canvasElement.value?.getContext("2d");
  drawLine(props.listCoordinates);
});

onUpdated(() => {
  canvasElement.value.width = props.width;
  canvasElement.value.height = props.height;
  context.value = canvasElement.value?.getContext("2d");
  drawLine(props.listCoordinates);
});

const drawLine = (list) => {
  if (!context.value || !list) {
    return;
  }
  const curveNumber = 15;
  list.forEach((coordinates) => {
    if (!coordinates || !coordinates.length) {
      return;
    }
    coordinates.forEach((coordinate) => {
      let coordinateX = coordinate[0];
      let coordinateY = coordinate[1];
      let leftY = coordinateY[0];
      let topX = coordinateX[1];
      let topY = coordinateY[1];
      let diffTopXY = Math.abs(topX - topY);

      if (topX > topY) {
        if (diffTopXY > curveNumber * 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateX);
          context.value.quadraticCurveTo(
            props.width / 2,
            topX,
            props.width / 2,
            topX - curveNumber
          );
          context.value.lineTo(props.width / 2, topY + curveNumber);
          context.value.quadraticCurveTo(props.width / 2, topY, leftY, topY);
          context.value.lineWidth = 2;
          context.value.strokeStyle = "#bdc1c7";
          context.value.stroke();
        } else if (diffTopXY > 0 && diffTopXY < 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateX);
          context.value.lineTo(...coordinateY);
          context.value.lineWidth = 2;
          context.value.strokeStyle = "#bdc1c7";
          context.value.stroke();
        } else {
          context.value.beginPath();
          context.value.moveTo(...coordinateX);
          context.value.quadraticCurveTo(
            props.width / 2,
            topX,
            props.width / 2,
            topX - diffTopXY / 2
          );
          context.value.quadraticCurveTo(props.width / 2, topY, leftY, topY);
          context.value.lineWidth = 2;
          context.value.strokeStyle = "#bdc1c7";
          context.value.stroke();
        }
      } else {
        if (diffTopXY > curveNumber * 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateX);
          context.value.quadraticCurveTo(
            props.width / 2,
            topX,
            props.width / 2,
            topX + curveNumber
          );
          context.value.lineTo(props.width / 2, topY - curveNumber);
          context.value.quadraticCurveTo(props.width / 2, topY, leftY, topY);
          context.value.lineWidth = 2;
          context.value.strokeStyle = "#bdc1c7";
          context.value.stroke();
        } else if (diffTopXY > 0 && diffTopXY < 2) {
          context.value.beginPath();
          context.value.moveTo(...coordinateX);
          context.value.lineTo(...coordinateY);
        } else {
          context.value.beginPath();
          context.value.moveTo(...coordinateX);
          context.value.quadraticCurveTo(
            props.width / 2,
            topX,
            props.width / 2,
            topX + diffTopXY / 2
          );
          context.value.quadraticCurveTo(props.width / 2, topY, leftY, topY);
          context.value.lineWidth = 2;
          context.value.strokeStyle = "#bdc1c7";
          context.value.stroke();
        }
      }
    });
  });
};
</script>
