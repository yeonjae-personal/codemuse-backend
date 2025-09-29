import { http, HttpResponse } from "msw";
import { API_URL } from "@/constants";
import {
  catalogSearchItemCodeAddOn,
  catalogSearchItemCodeDiscount,
  catalogSearchItemCodePricePlan,
  offerDetail,
  offerStructure,
} from "./data/offer";
import { initCreateOfferPricePlan } from "../extends/extends";
import { productDetail, resultSaveOffer } from "./data/search";

export const offerHandlers = [
  /**
   *
   * Mock for /prod/ui/offer/xx /detail
   *
   * method: GET
   * url:  http://dev.service-billing.com/prod/ui/offers
   *
   * http://dev.service-billing.com/prod/ui/offers?page=1&size=10&itemCode=PP
   *
   */
  http.get(`${API_URL}/prod/ui/offers`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const itemCode = urlSearchParams.get("itemCode");

    switch (itemCode) {
      case "PP":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(catalogSearchItemCodePricePlan));
          }, 1000);
        });

      case "OA":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(catalogSearchItemCodeAddOn));
          }, 1000);
        });

      case "DC":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(catalogSearchItemCodeDiscount));
          }, 1000);
        });
      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(catalogSearchItemCodePricePlan));
          }, 1000);
        });
    }
  }),
  /**
   *
   * Mock for http://dev.service-billing.com/prod/ui/offer/detail
   *
   * method: GET
   * url:  hhttp://dev.service-billing.com/prod/ui/offer/detail
   *
   * http://dev.service-billing.com/prod/ui/offer/detail?offerUUID=7b477c20-fca5-4d76-a9c9-849a9962c5a9
   *
   */
  http.get(`${API_URL}/prod/ui/offer/detail`, ({}) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(offerDetail));
      }, 1000);
    });
  }),

  /**
   * Mock for /prod/ui/structure
   *
   * method: GET
   * http://dev.service-billing.com/prod/ui/offer/structure?offerUUID=16f739af-2d99-4b9b-bd36-d7072d2c68db
   *
   * response: structureData
   */
  http.get(`${API_URL}/prod/ui/offer/structure`, ({}) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(offerStructure));
      }, 1000);
    });
  }),
  /**
   * When init create Offer (PricePlan, AddOn, Discount)
   *
   * method: GET
   * url: http://dev.service-billing.com/prod/ui/offer/create-info?itemCode=PP
   * response: init data
   */
  http.get(`${API_URL}/prod/ui/offer/create-info`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const itemCode = urlSearchParams.get("itemCode");

    switch (itemCode) {
      case "PP":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(initCreateOfferPricePlan));
          }, 1000);
        });

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(initCreateOfferPricePlan));
          }, 1000);
        });
    }
  }),
  // save offer http://dev.service-billing.com/prod/ui/offer
  http.post(`${API_URL}/prod/ui/offer`, async ({}) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(resultSaveOffer));
      }, 1000);
    });
  }),
  // update offer http://dev.service-billing.com/prod/ui/offer
  http.put(`${API_URL}/prod/ui/offer`, async ({}) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json(resultSaveOffer));
      }, 1000);
    });
  }),

  http.get(`${API_URL}/prod/ui/offer/*/detail?dcntCd*`, () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve(HttpResponse.json([]));
      }, 1000);
    });
  }),

  /**
   *
   * Mock for /prod/ui/offer/product/detail
   *
   * method: GET
   * url:  http://dev.service-billing.com/prod/ui/offer/product/detail?prodCd=PPMS00069&prodType=PRICEPLAN
   *
   * response: productDetailData
   */

  http.get(`${API_URL}/prod/ui/offer/product/detail`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const prodCd = urlSearchParams.get("prodCd");

    switch (prodCd) {
      case "PPMS00069":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(productDetail));
          }, 1000);
        });

      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(productDetail));
          }, 1000);
        });
    }
  }),
];
