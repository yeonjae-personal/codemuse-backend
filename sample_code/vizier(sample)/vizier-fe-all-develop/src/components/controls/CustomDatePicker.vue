<template>
  <div>
    <!-- Calendar -->
    <Calendar
      ref="calendarRef"
      class="custom-calendar"
      :attributes="calendarStore.attributes"
      :locale="locales"
      @dayclick="openDialog"
      @update:pages="onPagesUpdate"
      @daymouseenter="onDayMouseEnter"
      @daymouseleave="onDayMouseleave"
    >
      <template #day-popover="{ attributes }">
        <ul class="events-title">
          <li
            v-for="({ popover }, index) in attributes.sort(
              (a, b) => a.postition - b.postition
            )"
            :key="index"
          >
            <span :class="popover.color"></span>{{ popover.label }}
          </li>
        </ul>
      </template>
    </Calendar>
    <div
      v-if="popoverVisible"
      class="popover-container"
      :style="popoverPosition"
    >
      <div class="popover-arrow"></div>
      <div class="popover-contain">
        <ul>
          <li
            v-for="(item, index) in popoverData.list"
            :key="index"
            class="popover-item"
          >
            <span
              class="color-circle"
              :style="{ backgroundColor: item.color || `black` }"
            ></span>
            {{ item.title }}
          </li>
        </ul>
      </div>
    </div>
    <!-- Custom Dialog -->
    <div v-if="dialogVisible" class="dialog-container" :style="dialogPosition">
      <div class="dialog-arrow"></div>
      <div class="dialog-content">
        <button class="btn-close" @click="cancelEvent">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            width="19"
            height="20"
            viewBox="0 0 19 20"
            fill="none"
          >
            <path
              fill-rule="evenodd"
              clip-rule="evenodd"
              d="M5.77881 0.833313H13.5545C14.2924 0.833302 14.9014 0.833292 15.3975 0.873825C15.9128 0.915923 16.3864 1.00627 16.8313 1.23296C17.5212 1.58449 18.0822 2.14542 18.4337 2.83535C18.6604 3.28024 18.7507 3.75388 18.7928 4.26914C18.8334 4.76523 18.8333 5.37422 18.8333 6.1121V13.8879C18.8333 14.6257 18.8334 15.2347 18.7928 15.7308C18.7507 16.2461 18.6604 16.7197 18.4337 17.1646C18.0822 17.8545 17.5212 18.4155 16.8313 18.767C16.3864 18.9937 15.9128 19.084 15.3975 19.1261C14.9014 19.1667 14.2924 19.1667 13.5545 19.1666H5.77879C5.04091 19.1667 4.43192 19.1667 3.93583 19.1261C3.42057 19.084 2.94693 18.9937 2.50204 18.767C1.81211 18.4155 1.25118 17.8545 0.899644 17.1646C0.67296 16.7197 0.58261 16.2461 0.540512 15.7308C0.499979 15.2347 0.499989 14.6257 0.5 13.8878V6.11212C0.499989 5.37423 0.499979 4.76523 0.540512 4.26914C0.58261 3.75388 0.67296 3.28024 0.899644 2.83535C1.25118 2.14542 1.81211 1.58449 2.50204 1.23296C2.94693 1.00627 3.42057 0.915923 3.93583 0.873825C4.43192 0.833292 5.04092 0.833302 5.77881 0.833313ZM4.08512 2.70107C3.68324 2.7339 3.47772 2.79342 3.33435 2.86647C2.98939 3.04224 2.70892 3.3227 2.53316 3.66766C2.46011 3.81103 2.40059 4.01655 2.36776 4.41843C2.33405 4.83101 2.33333 5.36478 2.33333 6.14998V13.85C2.33333 14.6352 2.33405 15.1689 2.36776 15.5815C2.40059 15.9834 2.46011 16.1889 2.53316 16.3323C2.70892 16.6773 2.98939 16.9577 3.33435 17.1335C3.47772 17.2065 3.68324 17.2661 4.08512 17.2989C4.4977 17.3326 5.03147 17.3333 5.81667 17.3333H13.5167C14.3019 17.3333 14.8356 17.3326 15.2482 17.2989C15.6501 17.2661 15.8556 17.2065 15.999 17.1335C16.3439 16.9577 16.6244 16.6773 16.8002 16.3323C16.8732 16.1889 16.9327 15.9834 16.9656 15.5815C16.9993 15.1689 17 14.6352 17 13.85V6.14998C17 5.36478 16.9993 4.83101 16.9656 4.41843C16.9327 4.01655 16.8732 3.81103 16.8002 3.66766C16.6244 3.3227 16.3439 3.04224 15.999 2.86647C15.8556 2.79342 15.6501 2.7339 15.2482 2.70107C14.8356 2.66736 14.3019 2.66665 13.5167 2.66665H5.81667C5.03147 2.66665 4.4977 2.66736 4.08512 2.70107ZM6.26849 6.6018C6.62647 6.24382 7.20687 6.24382 7.56485 6.6018L9.66667 8.70362L11.7685 6.6018C12.1265 6.24382 12.7069 6.24382 13.0648 6.6018C13.4228 6.95978 13.4228 7.54018 13.0648 7.89816L10.963 9.99998L13.0648 12.1018C13.4228 12.4598 13.4228 13.0402 13.0648 13.3982C12.7069 13.7561 12.1265 13.7561 11.7685 13.3982L9.66667 11.2963L7.56485 13.3982C7.20687 13.7561 6.62647 13.7561 6.26849 13.3982C5.9105 13.0402 5.9105 12.4598 6.26849 12.1018L8.3703 9.99998L6.26849 7.89816C5.9105 7.54018 5.9105 6.95978 6.26849 6.6018Z"
              fill="#6B6D70"
            />
          </svg>
        </button>
        <div class="flex justify-between align-center -mt-1">
          <div class="text-[13px] date-info">
            {{ `${t("product_platform.dashboard.date")}` }}
            <span>{{ selectedDay }} ({{ dayOfWeek }})</span>
          </div>
          <div class="event-type">
            <EventType
              v-model="eventType"
              @update:model-value="updateEventType"
            />
          </div>
        </div>
        <!-- KeenSlider -->
        <div class="keen-slider-calendar">
          <div ref="sliderRef" class="keen-slider">
            <div
              v-for="slide in calendarStore.slides"
              :key="slide.position"
              class="keen-slider__slide"
            >
              <VTextField
                v-model="slide.title"
                class="title-input w-full h-[48px] rounded-lg mb-5"
                :label="$t('product_platform.dashboard.title')"
                variant="solo"
              />
              <VTextarea
                v-model="slide.content"
                class="content-input w-full h-[160px] rounded-lg"
                :label="$t('product_platform.dashboard.description')"
                variant="solo"
              />
            </div>
          </div>
          <CalendarPrevIcon
            class="z-[99999999]"
            :class="{
              arrow: true,
              'arrow--left': true,
              'arrow--disabled': current === 0,
            }"
            @click="slider.prev()"
          />
          <CalendarNextIcon
            v-if="slider"
            :class="{
              arrow: true,
              'arrow--right': true,
              'arrow--disabled':
                current === slider.track.details.slides.length - 1,
            }"
            @click="slider.next()"
          />

          <div v-if="slider" class="dots">
            <button
              v-for="(_slide, idx) in dotHelper"
              :key="idx"
              :class="{ dot: true, active: current === idx }"
              @click="slider.moveToIdx(idx)"
            ></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<!-- eslint-disable security/detect-object-injection -->
<script setup>
import { UI_DASHBOARD } from "@/api/prod/path";
import { useSnackbarStore } from "@/store";
import { useCalendarStore } from "@/store/calendarStore";
import { formatDate } from "@/utils/format-data";
import { httpClient } from "@/utils/http-common";
import KeenSlider from "keen-slider";
import "keen-slider/keen-slider.min.css";
import { Calendar } from "v-calendar";
import "v-calendar/style.css";
import { useI18n } from "vue-i18n";
import EventType from "../prod/dashboard/EventType.vue";
import CalendarNextIcon from "../prod/icons/CalendarNextIcon.vue";
import CalendarPrevIcon from "../prod/icons/CalendarPrevIcon.vue";

const { locale, t } = useI18n();
const eventType = ref("");
const calendarStore = useCalendarStore();
const dialogVisible = ref(false);
const dialogPosition = ref({});
const sliderRef = ref(null);
const current = ref(0);
const slider = ref(null);
const result = ref();
const dsbdViewUuid = ref("32dda7e8-09ba-4331-8c23-000a49d95298");
const dayOfWeek = ref("");
const selectedDay = ref("");
const body = ref({});
const calendarKey = ref(0);
const calendarRef = ref(null);
const { showSnackbar } = useSnackbarStore();
const currentPage = ref("");
const popoverVisible = ref(false);
const popoverPosition = ref({});
const popoverData = ref({ title: "", content: "", color: "" });

const colorMapping = {
  red: "#ec3636",
  yellow: "#ffde2a",
  blue: "#48cafe",
};

const getMappedColor = (color) => {
  return colorMapping[color] || color;
};
const onDayMouseEnter = (dayData) => {
  const eventData = [];
  calendarStore.todos.forEach((todo) => {
    if (
      todo.dates.some(
        (date) =>
          formatDate(date, "YYYY-MM-DD", "YYYY-MM-DD") ===
          formatDate(dayData.date, "YYYY-MM-DD", "YYYY-MM-DD")
      )
    ) {
      eventData.push(todo);
    }
  });
  if (eventData.length > 0) {
    popoverVisible.value = true;

    popoverData.value = {
      title: "Events",
      list: eventData.slice(0, 3).map((item) => ({
        title: item.title,
        color: getMappedColor(item.color),
      })),
    };
    const calendarElement = document.querySelector(".vc-container");
    if (!calendarElement) {
      console.warn("Calendar element not found");
      return;
    }
    locale.value === "en" ? "en-US" : "ko-KR";
    const formattedDate = new Intl.DateTimeFormat(locale.value, {
      weekday: "long",
      year: "numeric",
      month: "short",
      day: "numeric",
    }).format(dayData.date);

    const dateElement = calendarElement.querySelector(
      `[aria-label="${formattedDate}"]`
    );

    if (dateElement) {
      const rect = dateElement.getBoundingClientRect();
      popoverPosition.value = {
        top: `${rect.top + window.scrollY + 40}px`,
        left: `${rect.left + window.scrollX - 28}px`,
      };
    } else {
      console.warn("Date element not found for:", formattedDate);
    }
  }
};

const onDayMouseleave = () => {
  popoverVisible.value = false;
};

const fetchData = async (current) => {
  try {
    const response = await httpClient.get(`${UI_DASHBOARD}/calendar`, {
      params: {
        date: current || currentPage.value,
        dsbdViewUuid: dsbdViewUuid.value,
      },
    });
    result.value = response.data;
    calendarStore.setTodos(result.value);
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

onUpdated(() => {
  nextTick(() => {
    if (calendarRef.value && selectedDay.value) {
      const [year, month] = selectedDay.value.split("-").map(Number);
      calendarRef.value.move(new Date(year, month - 1), {
        transition: "none",
        animate: false,
      });
    }
    if (calendarRef.value && currentPage.value) {
      const [year, month] = currentPage.value.split("-").map(Number);
      calendarRef.value.move(new Date(year, month - 1), {
        transition: "none",
        animate: false,
      });
    }
  });
});

const openDialog = (day) => {
  const selected = new Date(day.date);
  const getDayOfWeek = (date) => {
    const daysOfWeek = [
      t("product_platform.calendar.Sun"),
      t("product_platform.calendar.Mon"),
      t("product_platform.calendar.Tue"),
      t("product_platform.calendar.Wed"),
      t("product_platform.calendar.Thu"),
      t("product_platform.calendar.Fri"),
      t("product_platform.calendar.Sat"),
    ];
    return daysOfWeek[date.getDay()];
  };
  const dateFormat = formatDate(day.date, "YYYY-MM-DD", "YYYY-MM-DD");
  dayOfWeek.value = getDayOfWeek(selected);
  selectedDay.value = dateFormat;
  dialogVisible.value = true;
  const selectedDateFormatted = dateFormat;
  const eventData = result.value.find(
    (event) => event.date === selectedDateFormatted
  );
  const todos = calendarStore.todos;
  const currentDayTodos = todos.filter((todo) =>
    todo.dates.some(
      (date) =>
        formatDate(date, "YYYY-MM-DD", "YYYY-MM-DD") === selectedDateFormatted
    )
  );
  if (eventData) {
    calendarStore.slides = calendarStore.slides.map((slide, index) => {
      const todo = currentDayTodos[index];
      return {
        slideSeq: slide.slideSeq,
        title: todo?.title,
        content: todo?.content,
        color: todo?.color,
      };
    });
    eventType.value = calendarStore.slides[0].color;
  } else {
    eventType.value = "";
    calendarStore.resetSlides();
  }
  nextTick(() => {
    const calendarElement = document.querySelector(".vc-container");
    if (!calendarElement) {
      console.warn("Calendar element not found");
      return;
    }
    locale.value === "en" ? "en-US" : "ko-KR";
    const formattedDate = new Intl.DateTimeFormat(locale.value, {
      weekday: "long",
      year: "numeric",
      month: "short",
      day: "numeric",
    }).format(day.date);

    const dateElement = calendarElement.querySelector(
      `[aria-label="${formattedDate}"]`
    );
    if (dateElement) {
      const rect = dateElement.getBoundingClientRect();
      dialogPosition.value = {
        top: rect.top + window.scrollY - 120 + "px",
        left: Math.max(rect.left + window.scrollX - 570, 0) + "px",
      };
    } else {
      console.warn("Date element not found for:", formattedDate);
    }
  });
  nextTick(() => {
    if (slider.value) slider.value.moveToIdx(0);
  });
};

const initializeSlider = () => {
  const sliderElement = document.querySelector(".keen-slider-calendar");

  if (sliderElement) {
    slider.value = new KeenSlider(sliderRef.value, {
      initial: current.value,
      loop: true,
      drag: false,
      slides: {
        perView: 1,
      },
      slideChanged: (item) => {
        const index = item.track.details.rel;
        current.value = index;
        eventType.value = calendarStore.slides[index].color || "";
      },
    });
    slider.value?.moveToIdx(0);
  }
};

const closeDialog = async () => {
  const existSlide = calendarStore.slides.some(
    (item) => item.title !== "" || item.content !== ""
  );
  if (existSlide === true) {
    body.value = {
      date: `${selectedDay.value}`,
      dsbdViewUuid: "32dda7e8-09ba-4331-8c23-000a49d95298",
      datas: calendarStore.slides,
    };
    try {
      const requestBody = body.value;
      const response = await httpClient.post(
        `${UI_DASHBOARD}/calendar`,
        requestBody
      );
      if (response.status === 200) {
        fetchData(currentPage.value);
        showSnackbar(t("product_platform.dashboard.saveSuccessful"), "success");
      }
    } catch (err) {
      showSnackbar(`Error: ${err.errorMsg}`, "error");
    }
  }
  dialogVisible.value = false;
  setTimeout(() => {
    calendarStore.resetSlides();
  }, 300);
};

const cancelEvent = () => {
  closeDialog();
};

const onPagesUpdate = (page) => {
  currentPage.value = `${page[0].id.slice(0, 7)}-01`;
};

watch(
  () => currentPage.value,
  async (newValue) => {
    try {
      const response = await httpClient.get(`${UI_DASHBOARD}/calendar`, {
        params: {
          date: newValue,
          dsbdViewUuid: dsbdViewUuid.value,
        },
      });
      result.value = response.data;
      calendarStore.setTodos(result.value);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  }
);

watch(dialogVisible, (newVal) => {
  if (newVal) {
    setTimeout(() => {
      initializeSlider();
    }, 0);
  }
});

const updateEventType = (color) => {
  calendarStore.slides[current.value].color = color;
};

const locales = reactive({
  id: locale,
  firstDayOfWeek: 1,
  masks: {
    weekdays: "WWW",
  },
});
const dotHelper = computed(() =>
  slider.value
    ? [...Array(slider.value.track.details.slides.length).keys()]
    : []
);
watch(
  () => calendarStore.todos,
  () => {
    calendarKey.value++;
  }
);
watch(
  () => locale.value,
  (newValue) => {
    locales.masks.title = newValue === "en" ? "MMMM YYYY" : "YYYY년 MMM";
  }
);
</script>

<style scoped lang="scss">
:deep(.custom-calendar) {
  width: 100%;
  margin-top: 15px;
  font-family: Noto Sans KR;
  font-size: 15px;
  font-weight: 500;
  line-height: 22.5px;
  letter-spacing: 0.005em;
}

:deep(.custom-calendar .vc-arrow) {
  width: 28px;
  height: 30px;
  border-radius: 8px;
  border: 1px solid rgba(220, 224, 229, 1);
  background-color: #ffff;
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.custom-calendar .vc-header .vc-title) {
  font-family: Noto Sans KR;
  font-size: 15px;
  font-weight: 500;
  line-height: 22.5px;
  letter-spacing: 0.005em;
  text-align: center;
}

:deep(.custom-calendar .vc-highlight-bg-solid) {
  background-color: rgba(217, 50, 90, 1);
}

:deep(.custom-calendar.vc-bordered) {
  border: none;
}

:deep(.custom-calendar .vc-weekday) {
  font-family: Noto Sans KR;
  font-size: 13px;
  font-weight: 500;
  line-height: 19.5px;
  letter-spacing: 0.25px;
  text-align: center;
}

.dialog-container {
  box-shadow: 0px 8px 32px -6px #7b7878;
  position: absolute;
  width: 560px;
  border-radius: 8px;
  z-index: 1000;
  padding: 40px 52px 12px;
  transform: translateY(-50%);
  background: linear-gradient(
    188.32deg,
    rgba(206, 206, 206, 0.24) 49.61%,
    rgba(206, 206, 206, 0.12) 93.62%
  );
  backdrop-filter: blur(5px);
}

.dialog-arrow {
  position: absolute;
  width: 20px;
  height: 16px;
  border-left: 10px solid transparent;
  border-right: 11px solid transparent;
  border-bottom: 13px solid rgba(247, 248, 250, 1);
  right: -13px;
  bottom: 25px;
  z-index: 1001;
  transform: rotate(90deg);
}

.keen-slider-calendar {
  margin-top: 12px;
}
.keen-slider__slide {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 0;
}
.current-date {
  color: #ba1642 !important;
  font-size: 11px !important;
  font-weight: 400;
  width: 105px;
  height: 24px !important;
  padding-left: 7px !important;
}
:deep(.v-field) {
  box-shadow: none;
  border: 1px solid var(--border-border-base, rgba(220, 224, 229, 1));
  border-radius: 8px;
}
.arrow {
  position: absolute;
  top: 68%;
  width: 48px;
  height: 48px;
  cursor: pointer;
  transform: translateY(-50%);
}

.arrow--left {
  left: 4px;
  opacity: 1;
  transition: opacity 0.3s ease;
}

.arrow--left:hover {
  opacity: 1;
}

.arrow--right {
  right: 2px;
  opacity: 1;
  transition: opacity 0.3s ease;
}
.arrow--right.arrow-visible {
  opacity: 1;
}
.dialog-container .dots {
  display: flex;
  padding: 0;
  justify-content: center;
}
.dialog-container .dot {
  border: none;
  width: 6px;
  height: 6px;
  background: #e6e9ed;
  border-radius: 50%;
  margin: 0 5px;
  cursor: pointer;
}
.dialog-container .dot:focus {
  outline: none;
}
.dialog-container .dot.active {
  background: #ba1642;
  width: 24px;
  height: 6px;
  border-radius: 8px;
}
:deep(.v-label.v-field-label) {
  font-size: 13px !important;
  font-weight: 400 !important;
  color: #bdc1c7 !important;
}
:deep(.v-label.v-field-label.v-field-label--floating) {
  font-size: 9px !important;
}
:deep(.v-chip__content) {
  color: #ba1642;
}
:deep(.vc-popover-content-wrapper .vc-popover-content) {
  background: #f0f2f5;
  border: none;
  border-radius: 8px;
  padding: 8px 12px;
}
.date-info {
  background: #fff;
  border-radius: 35px;
  min-width: 171px;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 36px;
  font-weight: 400;
  span {
    font-weight: 500;
    margin-left: 6px;
  }
}
.btn-close {
  position: absolute;
  right: 22px;
  top: 22px;
}
.event-type {
  width: 108px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border-radius: 27px;
}

:deep(.select-day) {
  background-color: white;
  box-shadow: 0 0 0 2px rgb(59, 131, 246, 0.4);
}
:deep(.select-day:focus-within) {
  background-color: white;
  box-shadow: 0 0 0 2px rgb(59, 131, 246, 0.4);
}
:deep(.vc-highlight) {
  width: 21px;
  height: 21px;
}
:deep(.vc-day-content) {
  width: 21px;
  height: 21px;
}
:deep(.v-field__input) {
  font-size: 13px;
  font-weight: 400;
  color: #3a3b3d;
  outline: none !important;
}
.events-title {
  font-family: "Noto Sans KR";
  font-size: 13px;
  font-weight: 400;
  line-height: 19.5px;
  li {
    margin-bottom: 6px;
    display: flex;
    align-items: center;
    span {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      margin-right: 6px;
    }
    .yellow {
      background: #ffde2a;
    }
    .red {
      background: #ec3636;
    }
    .blue {
      background: #48cafe;
    }
    .black {
      background: #000;
    }
  }
  li:last-child {
    margin-bottom: 0;
  }
}
.v-calendar .vc-transition {
  transition: none !important;
  animation: none !important;
}
.popover-container {
  position: absolute;
  background: #f0f2f5;
  border-radius: 8px;
  z-index: 1000;
  padding: 8px;
  box-shadow: 0px 8px 32px -6px #7b7878;
  -webkit-backdrop-filter: blur(5px);
  backdrop-filter: blur(5px);
  min-width: 80px;
}

.popover-arrow {
  position: absolute;
  width: 14px;
  height: 12px;
  border-left: 10px solid transparent;
  border-right: 11px solid transparent;
  border-bottom: 13px solid rgb(247, 248, 250);
  background: #f0f2f5;
  top: -12px;
  left: 34%;
  z-index: 1001;
}

.popover-container h4,
.popover-container p,
.popover-container small {
  margin: 0;
  font-size: 13px;
  font-weight: 400;
  color: #3a3b3d;
}

.popover-container ul {
  list-style: none;
  padding: 0;
  margin: 8px 0;
}

.popover-container li {
  margin: 4px 0;
  font-size: 14px;
  color: #3a3b3d;
}
.color-circle {
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
}
:deep(.content-input .v-field__input) {
  resize: none;
}
</style>
