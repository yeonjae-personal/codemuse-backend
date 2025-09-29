<script setup lang="ts">
import { CommonUtil } from "@/utils/common-util";
import { useI18n } from "vue-i18n";

const { locale } = useI18n();
const { translateMessage } = CommonUtil.useTranslatedMessage();

const menuCategories = ref([
  {
    title: "Components",
    items: [
      { label: "Accordion", link: "/components/accordion" },
      { label: "Alert", link: "/components/alert" },
      { label: "Avatar", link: "/components/avatar" },
      { label: "Badge", link: "/components/badge" },
      { label: "Button", link: "/components/button" },
      { label: "Card", link: "/components/card" },
      { label: "ListGroup", link: "/components/list-group" },
      { label: "Tabs", link: "/components/tabs" },
      { label: "Modal", link: "/components/modal" },
      { label: "Toast", link: "/components/toast" },
      { label: "Footer", link: "/components/footer" },
      { label: "Carousel", link: "/components/Carousel" },
      { label: "Spinner", link: "/components/Spinner" },
      { label: "CardDropdown", link: "/components/card-dropdown" },
    ],
  },
  {
    title: "Form",
    items: [
      { label: "Input", link: "/components/input" },
      { label: "File Input", link: "/components/file-input" },
      { label: "Select", link: "/components/select" },
      { label: "Textarea", link: "/components/textarea" },
      { label: "Checkbox", link: "/components/checkbox" },
      { label: "Radio", link: "/components/radio" },
    ],
  },
  {
    title: "Function",
    items: [
      {
        label: translateMessage("todos.lbl_title_page"),
        link: "/functions/todos",
      },
      {
        label: translateMessage("user_management.lbl_title_page"),
        link: "/functions/user-management",
      },
      {
        label: "Message Management",
        link: "/functions/message-management",
      },
      {
        label: "Notice",
        link: "/functions/notice",
      },
      {
        label: "Employee",
        link: "/functions/employee",
      },
      {
        label: "모바일 신규가입 (시연)",
        link: "/functions/register",
      },
      {
        label: "가입정보조회",
        link: "/functions/entrInfo",
      },
      {
        label: "Product Platform",
        link: "/functions/product-platform",
        target: "_blank",
      },
    ],
  },
  {
    title: "Solution",
    items: [
      {
        label: "이벤트항목기능 관리",
        link: "/functions/evetItemAndFucnPage",
      },
      {
        label: "시스템 관리",
        link: "/solution/system",
      },
      {
        label: "이벤트 관리",
        link: "/solution/orderEvent",
      },
      {
        label: "클래스 관리",
        link: "/solution/searchOrder",
      },

      {
        label: "Sample",
        link: "/solution/test",
      },
      {
        label: "오더항목 관리",
        link: "/solution/order",
      },
      {
        label: "기능 관리",
        link: "/solution/funcMgmtPage",
      },
      {
        label: "Order Management",
        link: "/solution/OrderMgmtPage",
      },
      {
        label: "Order Registration",
        link: "/solution/OrderRegistrationPage",
      },
      {
        label: "Customer Attribute Management",
        link: "/solution/customer-attribute",
      },
    ],
  },
  {
    title: "도메인/용어/단어",
    items: [
      {
        label: "Vocap",
        link: "/vocap",
      },
      {
        label: "Domain",
        link: "/domain",
      },
      {
        label: "Code",
        link: "/code",
      },
      {
        label: "System message",
        link: "/sysmgs",
      },
      {
        label: "User management",
        link: "/user",
      },
      {
        label: "Oganization management",
        link: "/org",
      },
    ],
  },
]);

watch(
  () => locale.value,
  () => {
    updateMenuCategories();
  }
);

const updateMenuCategories = () => {
  menuCategories.value = [
    {
      title: "Components",
      items: [
        { label: "Accordion", link: "/components/accordion" },
        { label: "Alert", link: "/components/alert" },
        { label: "Avatar", link: "/components/avatar" },
        { label: "Badge", link: "/components/badge" },
        { label: "Button", link: "/components/button" },
        { label: "Card", link: "/components/card" },
        { label: "ListGroup", link: "/components/list-group" },
        { label: "Tabs", link: "/components/tabs" },
        { label: "Modal", link: "/components/modal" },
        { label: "Toast", link: "/components/toast" },
        { label: "Carousel", link: "/components/Carousel" },
        { label: "Spinner", link: "/components/Spinner" },
        { label: "CardDropdown", link: "/components/card-dropdown" },
      ],
    },
    {
      title: "Form",
      items: [
        { label: "Input", link: "/components/input" },
        { label: "File Input", link: "/components/file-input" },
        { label: "Select", link: "/components/select" },
        { label: "Textarea", link: "/components/textarea" },
        { label: "Checkbox", link: "/components/checkbox" },
        { label: "Radio", link: "/components/radio" },
      ],
    },
    {
      title: "Function",
      items: [
        {
          label: translateMessage("todos.lbl_title_page"),
          link: "/functions/todos",
        },
        {
          label: translateMessage("user_management.lbl_title_page"),
          link: "/functions/user-management",
        },
        {
          label: "Message Management",
          link: "/functions/message-management",
        },
        {
          label: "Notice",
          link: "/functions/notice",
        },
        {
          label: "Employee",
          link: "/functions/employee",
        },
        {
          label: "Product Platform",
          link: "/functions/product-platform",
          target: "_blank",
        },
      ],
    },
    {
      title: "Solution",
      items: [
        {
          label: "이벤트 항목 기능",
          link: "/functions/evetItemAndFucnPage",
        },
      ],
    },
  ];
};
</script>

<template>
  <ul class="mt-6">
    <li
      v-for="category in menuCategories"
      :key="category.title"
      class="mb-12 lg:mb-8"
    >
      <h5 class="lg:mb-3 font-semibold text-slate-900 dark:text-slate-200">
        {{ category.title }}
      </h5>
      <ul
        class="space-y-6 lg:space-y-2 border-l border-slate-100 dark:border-slate-800"
      >
        <!-- data-active="true" -->
        <li v-for="item in category.items" :key="item.link">
          <!-- class="block border-l pl-4 -ml-px text-sky-500 border-current font-semibold dark:text-sky-400" -->
          <a
            class="block border-l pl-4 -ml-px border-transparent hover:border-slate-400 dark:hover:border-slate-500 text-slate-700 hover:text-slate-900 dark:text-slate-400 dark:hover:text-slate-300"
          >
            <router-link :to="item.link" :target="item?.target || '_self'">{{
              item.label
            }}</router-link>
          </a>
        </li>
      </ul>
    </li>
  </ul>
</template>

<style scoped></style>
