type Slide = {
  slideSeq: number;
  title: string;
  content: string;
  color: string;
};

type ApiData = {
  date: string;
  datas: {
    color: string;
    title: string;
    content: string;
    postition: number;
  }[];
};

export const useCalendarStore = defineStore("calendarStore", () => {
  const color = ref({
    red: "#ec3636",
    yellow: "#ffde2a",
    blue: "#48cafe",
  } as const);

  type ColorKeys = keyof typeof color.value;

  type Todo = {
    title: string;
    content: string;
    dates: Date[];
    color: ColorKeys;
    postition: number;
  };

  const selectedDate = ref<string>("");

  const setSelectedDate = (date: string): void => {
    selectedDate.value = date;
  };

  const fixedAttr = computed(() => [
    ...(selectedDate.value
      ? [
          {
            dates: selectedDate.value,
            highlight: {
              contentClass: "select-day",
              fillMode: "outline",
            },
          },
        ]
      : []),
    {
      key: "today",
      highlight: {
        borderRadius: "50%",
        contentClass: "current-day",
      },
      dates: new Date(),
    },
  ]);

  const todos = ref<Todo[]>([]);

  const dashboardAttributes = computed(() => {
    const result = todos.value.map((todo) => ({
      dates: todo.dates,
      dot: {
        style: {
          backgroundColor: color.value[todo.color] || "black",
        },
      },
      postition: todo.postition,
    }));
    return result;
  });

  const attributes = computed(() => [
    ...fixedAttr.value,
    ...dashboardAttributes.value,
  ]);

  const slides = ref<Slide[]>([
    { slideSeq: 0, title: "", content: "", color: "" },
    { slideSeq: 1, title: "", content: "", color: "" },
    { slideSeq: 2, title: "", content: "", color: "" },
  ]);

  const setTodos = (apiData: ApiData[]): void => {
    todos.value = apiData.flatMap(({ date, datas }) =>
      datas.map((item) => ({
        title: item.title,
        content: item.content,
        dates: [new Date(`${date}T00:00:00`)],
        color: item.color as ColorKeys,
        postition: item.postition,
      }))
    );
  };

  const resetSlides = (): void => {
    slides.value = slides.value.map((slide) => ({
      slideSeq: slide.slideSeq,
      title: "",
      content: "",
      color: "",
    }));
  };

  return {
    todos,
    attributes,
    slides,
    selectedDate,
    setSelectedDate,
    setTodos,
    resetSlides,
  };
});
