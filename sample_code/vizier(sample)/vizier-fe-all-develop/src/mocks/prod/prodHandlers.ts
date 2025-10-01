import { http, HttpResponse } from "msw";
import { API_URL } from "../../constants";
import { targetSearchOfferData } from "./impact-analysis/items-offer";
import { relationData } from "./impact-analysis/relation";
import { structureData } from "./impact-analysis/structure";
import {
  expandDependencyFollowerData,
  expandDependencyRelationDefinitionData,
  expandDependencyTargetGroupData,
} from "./extends/extends";
import {
  initOfferCmcdCmcdGrpIdSearch,
  initOfferCmcdCmcdGrpIdSearchSecond,
  productStructureData,
} from "./catalog/data/search";
import { dashBoardData } from "./dashboard";
import isEqual from "lodash-es/isEqual";
import { catalogOfferHistory } from "./catalog/data/offer";

/**
 * Mock data for product-platform
 */
export const prodHandlers = [

  /**
   * Mock api returns dashboard data
   *
   * method: GET
   * url: http://localhost:3000/prod/ui/dashboard
   * response: dashboard
   *
   */
  http.get(`${API_URL}/prod/ui/dashboard`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(dashBoardData));
      }, 1000);
    });
  }),

  /**
   * Mock api trả về danh sách offer
   *
   * method: GET
   * url: http://dev.service-billing.com/prod/ui/impact-analysis/items?detlType=XXX&type=OFFER
   * response: targetSearchOfferData
   */
  http.get(`${API_URL}/prod/ui/impact-analysis/items`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);

    const detlType = urlSearchParams.get("detlType");
    const type = urlSearchParams.get("type");

    switch (type) {
      case "OFFER":
        if (detlType === "PricePlan") {
          return new Promise((resolve) => {
            setTimeout(() => {
              resolve(HttpResponse.json(targetSearchOfferData));
            }, 1000);
          });
        }

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(targetSearchOfferData));
          }, 1000);
        });
    }
  }),

  /**
   * When click expand dependency follower
   *
   * method: GET
   * url: http://dev.service-billing.com/prod/ui/extends/dependency/follower?targetCd=PPMS00026
   * response: expandDependencyFollowerData
   */
  http.get(`${API_URL}/prod/ui/extends/dependency/follower`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const targetCd = urlSearchParams.get("targetCd");

    switch (targetCd) {
      case "PPMS00026":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(expandDependencyFollowerData));
          }, 1000);
        });

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(expandDependencyFollowerData));
          }, 1000);
        });
    }
  }),

  /**
   * When click expand dependency relation definition
   *
   * method: GET
   * url: http://dev.service-billing.com/prod/ui/extends/dependency/relation/definition?dpdcRelCode=LCFRA
   * response: expandDependencyRelationDefinitionData
   */
  http.get(
    `${API_URL}/prod/ui/extends/dependency/relation/definition`,
    ({ request }) => {
      const url = new URL(request.url);
      const urlSearchParams = new URLSearchParams(url.search);
      const dpdcRelCode = urlSearchParams.get("dpdcRelCode");

      switch (dpdcRelCode) {
        case "LCFRA":
          return new Promise((resolve) => {
            setTimeout(() => {
              resolve(
                HttpResponse.json(expandDependencyRelationDefinitionData)
              );
            }, 1000);
          });

        default:
          return new Promise((resolve) => {
            setTimeout(() => {
              resolve(
                HttpResponse.json(expandDependencyRelationDefinitionData)
              );
            }, 1000);
          });
      }
    }
  ),

  /**
   * Mock for /prod/ui/structure
   *
   * method: GET
   * url: http://dev.service-billing.com/prod/ui/structure?baseProdItemCd=PPMS00069
   *
   * response: structureData
   */
  http.get(`${API_URL}/prod/ui/products/structure`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const baseProdItemCd = urlSearchParams.get("baseProdItemCd");

    switch (baseProdItemCd) {
      case "PPMS00069":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(productStructureData));
          }, 1000);
        });

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(productStructureData));
          }, 1000);
        });
    }
  }),

  /**
   * Mock for /prod/ui/structure
   *
   * method: GET
   * url: http://dev.service-billing.com/prod/ui/structure?baseProdItemCd=PPMS00069
   *
   * response: structureData
   */
  http.get(`${API_URL}/prod/ui/products/structure`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const offerUUID = urlSearchParams.get("offerUUID");

    switch (offerUUID) {
      case "PPMS00069":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(productStructureData));
          }, 1000);
        });

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(productStructureData));
          }, 1000);
        });
    }
  }),

  /**
   * When init create Offer (PricePlan, AddOn, Discount)
   *
   * method: POST
   * url: http://dev.service-billing.com/comm/cmcd/v1/cmcdCmcdGrpIdSearch
   * response: init data
   */
  http.post(
    `${API_URL}/comm/cmcd/v1/cmcdCmcdGrpIdSearch`,
    async ({ request }) => {
      const requestBody = await request.json();
      const generalTask = {
        cmcdGrpIds: ["G00002", "G00006", "G00007"],
      };
      const isGeneralTask = isEqual(requestBody, generalTask);
      if (isGeneralTask) {
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(initOfferCmcdCmcdGrpIdSearch));
          }, 1000);
        });
      }

      return new Promise((resolve) => {
        setTimeout(() => {
          resolve(HttpResponse.json(initOfferCmcdCmcdGrpIdSearchSecond));
        }, 1000);
      });
    }
  ),

  // http://localhost:3000/prod/ui/extends/dependency/target?targetCd=PPMS00069
  http.get(`${API_URL}/prod/ui/extends/dependency/target`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const targetCd = urlSearchParams.get("targetCd");

    switch (targetCd) {
      case "PPMS00069":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(expandDependencyTargetGroupData));
          }, 1000);
        });

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(expandDependencyTargetGroupData));
          }, 1000);
        });
    }
  }),

  http.get(`${API_URL}/prod/ui/structure?baseProdItemCd=*`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(structureData.slice(0, 5)));
      }, 1000);
    });
  }),

  http.get(`${API_URL}/prod/ui/impact-analysis/relation?prodUuid*`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(relationData));
      }, 1000);
    });
  }),

  // http://dev.service-billing.com/prod/ui/history/detail
  http.get(`${API_URL}/prod/ui/history/detail`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(catalogOfferHistory));
      }, 1000);
    });
  }),
];
