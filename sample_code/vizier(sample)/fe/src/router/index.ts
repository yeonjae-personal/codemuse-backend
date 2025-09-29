import { createRouter, createWebHistory } from "vue-router";
import { useCookies } from "vue3-cookies";
import { useUser } from "@/store";
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import ProductPlatformLayout from "@/layouts/ProductPlatformLayout.vue";

const { cookies } = useCookies();

const routes = [
  {
    path: "/",
    component: DefaultLayout,
    children: [
      {
        path: "",
        name: "Channel Framework",
        component: () => import("@/pages/HomePage.vue"),
        meta: {
          title: "Channel Framework",
        },
      },
    ],
  },
  {
    path: "/functions/product-platform",
    component: ProductPlatformLayout,
    children: [
      {
        path: "",
        name: "Product Platform",
        component: () => import("@/pages/prod/functions/DashboardPage.vue"),
        meta: {
          title: "Dash Board",
        },
      },
    ],
  },
  {
    path: "/temp-ui",
    component: ProductPlatformLayout,
    name: "tempui",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

const modules = import.meta.glob("@/pages/**/*.vue");

const setMenuRouter = (userMenuList) => {
  userMenuList.forEach((ele) => {
    if (ele.children) setMenuRouter(ele.children);

    if (ele.scrnId) {
      const scrnLinkUrl = ele.scrnLinkUrl.substring(1);
      router.addRoute("tempui", {
        path: scrnLinkUrl,
        name: ele.menuNm + ele.menuId,
        component: modules[ele.scrnPathNm],
        meta: { title: ele.menuNm },
      });
    }
  });
};

router.beforeEach(async (to, _from, next) => {
  // Reset user store
  const useUserStore = useUser();

  // 로그인 화면 이동
  if (!cookies.get("aToken")) {
    useUserStore.$reset();
    // if (to.path == "/functions/login") return next();
    if (to.path != "/") {
      alert("로그아웃 되었습니다.");
      return next("/");
    }
    return next();
  }

  // 메인화면 이동
  if (to.path == "/") {
    return next("/functions/product-platform");
  }

  if (router.getRoutes().length < 6) {
    const user = JSON.parse(localStorage.getItem("userInfo") || "{}");
    const userMenuList = user.userMenuList.userMenuList;
    setMenuRouter(userMenuList);
    return next(to);
  }

  // if (vuecoo.get('aToken') === null && $cookies.get('refresh_token') !== null) {
  // await refreshToken();
  // }

  // if (to.matched.some(record => record.meta.unauthorized) || this.$cookies.get('aToken')) {
  // return next();
  // }

  return next();
});

export default router;
