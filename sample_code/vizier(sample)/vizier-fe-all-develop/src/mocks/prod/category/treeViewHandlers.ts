import { http, HttpResponse } from "msw";
import { API_URL } from "@/constants";
import { pricePlanData, treePricePlanData, categoryProducts } from "./tree-view";

export const treeViewHandlers = [
  /**
   * method: GET
   * url:  /prod/ui/category/tree/offers/search?itemCode=price+pla
   *
   * http://10.63.161.230:8080/prod/ui/category/tree/offers/search?itemCode=PP&offerNm=&offerCd=&page=1
   *
   */
  http.get(`${API_URL}/prod/ui/category/tree/offers/search`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const itemCode = urlSearchParams.get("itemCode");

    switch (itemCode) {
      case "PP":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(pricePlanData));
          }, 1000);
        });

      
      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(pricePlanData));
          }, 1000);
        });
    }
  }),

  http.get(`${API_URL}/prod/ui/category/tree`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const itemCode = urlSearchParams.get("itemCode");

    switch (itemCode) {
      case "PP":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(treePricePlanData));
          }, 1000);
        });

      
      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(treePricePlanData));
          }, 1000);
        });
    }
  }),
  // http://localhost:3000/prod/ui/category/description?itemCode=PP
  http.get(`${API_URL}/prod/ui/category/description`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const itemCode = urlSearchParams.get("itemCode");

    switch (itemCode) {
      case "PP":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(treePricePlanData));
          }, 1000);
        });

      
      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(treePricePlanData));
          }, 1000);
        });
    }
  }),
  // http://localhost:3000/prod/ui/category/products?catgUuid=0230027b-b136-4011-b0fa-cedd9e2437df&itemCode=PP
  http.get(`${API_URL}/prod/ui/category/products`, ({ request }) => {
    const url = new URL(request.url);
    const urlSearchParams = new URLSearchParams(url.search);
    const catgUuid = urlSearchParams.get("catgUuid");

    switch (catgUuid) {
      case "PP":
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(categoryProducts));
          }, 1000);
        });

      
      default:
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve(HttpResponse.json(categoryProducts));
          }, 1000);
        });
    }
  }),

];
