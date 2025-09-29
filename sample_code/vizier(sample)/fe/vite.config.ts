import vue from "@vitejs/plugin-vue";
import { fileURLToPath, URL } from "node:url";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { federation } from "@module-federation/vite";
import { defineConfig, loadEnv } from "vite";
import { version as pkgVersion } from "./package.json";
import svgLoader from "vite-svg-loader";

process.env.VITE_APP_VERSION = pkgVersion;
if (process.env.NODE_ENV === "production") {
  process.env.VITE_APP_BUILD_EPOCH = new Date().getTime().toString();
}

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd());

  const isLocal = mode === "localhost";

  const remoteEntryUrl = env.VITE_REMOTE_ENTRY;
  const hostDomain = env.VITE_HOST_DOMAIN;
  const port = Number(env.VITE_PORT);

  return {
    plugins: [
      vue(),
      svgLoader(),
      AutoImport({
        imports: [
          "vue",
          "vue-router",
          "@vueuse/head",
          "pinia",
          {
            "@/store": ["useStore"],
            "@vueuse/head": ["useHead"],
          },
        ],
        dts: "src/auto-imports.d.ts",
        vueTemplate: true,
        eslintrc: {
          enabled: true,
        },
      }),
      Components({
        dts: "src/components.d.ts",
      }),
      federation({
        name: "host",
        remotes: {
          remote: {
            type: "module",
            name: "remote",
            // entry: "http://module.service-billing.com/remoteEntry.js",
            entry: remoteEntryUrl,
            entryGlobalName: "remote",
            shareScope: "default",
          },
        },
        exposes: {},
        filename: "remoteEntry.js",
        shared: {
          vue: { singleton: true, eager: true },
          pinia: { singleton: true, eager: true },
        },
      }),
    ],
    server: {
      ...(isLocal
        ? {
            host: hostDomain,
            port: port,
          }
        : {}),
    },
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
    assetsInclude: ["**/*.xlsx"],
    build: {
      target: "esnext",
      manifest: true, // 파일에 해시값 추가
    },
    define: {
      global: "window", // Map global to window
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: "modern-compiler",
          silenceDeprecations: ["legacy-js-api"],
          additionalData: `
            @use "@/assets/scss/global.scss" as *;
            @import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap");
          `,
        },
      },
    },
  };
});
