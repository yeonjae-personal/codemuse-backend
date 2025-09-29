// Init block event security browser
document.addEventListener("keydown", function (event) {
  if (event.key === "F12") {
    event.preventDefault();
  }

  if (event.ctrlKey) {
    if (["u", "s"].includes(event.key)) {
      event.preventDefault();
    }
  }

  if (event.ctrlKey && event.shiftKey) {
    if (["J", "I", "C"].includes(event.key)) {
      event.preventDefault();
    }
  }
});

document.addEventListener("contextmenu", function (event) {
  event.preventDefault();
});