
// Windows temporarily needs this file, https://github.com/module-federation/vite/issues/68

    const importMap = {
      
        "pinia": async () => {
          let pkg = await import("__mf__virtual/host__prebuild__pinia__prebuild__.js")
          return pkg
        }
      ,
        "vue": async () => {
          let pkg = await import("__mf__virtual/host__prebuild__vue__prebuild__.js")
          return pkg
        }
      
    }
      const usedShared = {
      
          "pinia": {
            name: "pinia",
            version: "2.1.7",
            scope: ["default"],
            loaded: false,
            from: "host",
            async get () {
              usedShared["pinia"].loaded = true
              const {"pinia": pkgDynamicImport} = importMap 
              const res = await pkgDynamicImport()
              const exportModule = {...res}
              // All npm packages pre-built by vite will be converted to esm
              Object.defineProperty(exportModule, "__esModule", {
                value: true,
                enumerable: false
              })
              return function () {
                return exportModule
              }
            },
            shareConfig: {
              singleton: true,
              requiredVersion: "^2.1.7"
            }
          }
        ,
          "vue": {
            name: "vue",
            version: "3.3.10",
            scope: ["default"],
            loaded: false,
            from: "host",
            async get () {
              usedShared["vue"].loaded = true
              const {"vue": pkgDynamicImport} = importMap 
              const res = await pkgDynamicImport()
              const exportModule = {...res}
              // All npm packages pre-built by vite will be converted to esm
              Object.defineProperty(exportModule, "__esModule", {
                value: true,
                enumerable: false
              })
              return function () {
                return exportModule
              }
            },
            shareConfig: {
              singleton: true,
              requiredVersion: "^3.3.10"
            }
          }
        
    }
      const usedRemotes = [
      ]
      export {
        usedShared,
        usedRemotes
      }
      