import { createHead } from "@vueuse/head";
import { createVuetify } from "vuetify";
import * as components from "vuetify/components";
import * as directives from "vuetify/directives";
import VueDatePicker from "@vuepic/vue-datepicker";
import { globalCookiesConfig } from "vue3-cookies";
import { VTreeview } from "vuetify/labs/VTreeview";
import "prismjs";
import App from "./App.vue";
import router from "./router";
import i18n from "./i18n";

import "vuetify/styles";
import "prismjs/themes/prism.css";
import "@vuepic/vue-datepicker/dist/main.css";
import "@mdi/font/css/materialdesignicons.min.css";
import "./assets/index.postcss";
import "./styles/settings.scss";

globalCookiesConfig({
  expireTimes: "1d",
  path: "/",
  domain: "service-billing.com",
  secure: false,
  sameSite: "None",
});

const vuetify = createVuetify({
  components: { ...components, VTreeview },
  directives,
});

const head = createHead();
const app = createApp(App);
const pinia = createPinia();
pinia.use(({ store }) => {
  store.router = markRaw(router);
});

app.use(i18n);
app.use(pinia);
app.use(router);
app.use(head);
app.use(vuetify);
app.component("VueDatePicker", VueDatePicker);
app.mount("#app");
