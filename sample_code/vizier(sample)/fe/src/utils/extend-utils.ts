/* eslint-disable id-length */
import { NM_CD_FIELDS } from "@/constants/impactAnalysis";

interface SearchParams {
  type: string;
  searchBy: string;
  keyword: string;
}

export const searchByKeyword = (
  name: string,
  code: string,
  typeSearch: string,
  searchObj: SearchParams
) => {
  if (!searchObj) {
    return false;
  }

  const { type, searchBy, keyword } = searchObj;
  if (type && searchBy && keyword) {
    if (typeSearch === type) {
      return searchByNmCd(searchBy, keyword, name, code);
    }
    return false;
  }
};

const searchByNmCd = (
  searchBy: string,
  keyword: string,
  name: string,
  code: string
) => {
  if (searchBy === NM_CD_FIELDS[0].value) {
    if (!name) {
      return false;
    }
    return name.includes(keyword);
  } else {
    if (!code) {
      return false;
    }
    return code.includes(keyword);
  }
};

export const transformList = (data: any, objKey: string, t: any) => {
  if (!data) return [];
  return Object?.keys(data).map((key) => {
    return {
      label: t(`product_platform.${objKey}.${key}`),
      value: data[key as string] || "-",
      raw: key,
    };
  });
};

export const findMenuItem = (arr, path) => {
  for (const element of arr) {
    const currentPath = "/temp-ui" + element.scrnLinkUrl;
    if (currentPath === path) {
      return {
        ...element,
        path: currentPath,
      };
    }

    if (element.children) {
      const children = element.children.map((item) => ({
        ...item,
        menuNm: `${element.menuNm} ${item.menuNm}`,
        rawName: `${element.menuNm} ${item.menuNm}`.replace(/\s+/g, ""),
        tabName:
          element.menuLv === "2"
            ? `${element.menuNm} ${item.menuNm}`
            : element.menuNm,
      }));
      const result = findMenuItem(children, path);
      if (result) {
        return result;
      }
    }
  }

  return null;
};

export const sortByName = (after, before) => {
  const nameA = after.name.toUpperCase(); // ignore upper and lowercase
  const nameB = before.name.toUpperCase(); // ignore upper and lowercase
  if (nameA < nameB) {
    return -1;
  }
  if (nameA > nameB) {
    return 1;
  }

  // names must be equal
  return 0;
};

export const onClickOutside = (callback, selector) => {
  document.addEventListener("click", (event: any) => {
    const dialog = document.querySelector(selector);
    if (!dialog?.contains(event.target)) {
      callback();
    }
  });
};

// DFS - Deep First Search Cycle Detection
export function DFSDetectCycle(edges) {
  const graph = new Map();

  for (const [u, v] of edges) {
    if (!graph.has(u)) graph.set(u, []);
    graph.get(u).push(v);
  }

  const visited = new Map();

  function dfs(node) {
    if (visited.get(node) === 1) return true;
    if (visited.get(node) === 2) return false;

    visited.set(node, 1);

    for (const neighbor of graph.get(node) || []) {
      if (dfs(neighbor)) return true;
    }

    visited.set(node, 2);
    return false;
  }

  for (const node of graph.keys()) {
    if (!visited.has(node)) {
      if (dfs(node)) return true;
    }
  }

  return false;
}

export function checkDuplicateEdges(edges) {
  const edgeSet = new Set();

  for (const [u, v] of edges) {
    const edge = `${u},${v}`;
    if (edgeSet.has(edge)) {
      return true;
    }
    edgeSet.add(edge);
  }

  return false;
}

export function checkNumberIsInteger(number) {
  return Number.isInteger(Number(number));
}

export function formatDataTypeDecimal(
  valueInput,
  isDecimal = false,
  valueMaxLength = null as any
) {
  const vowelsRegex = /^\d+$/;
  if (isDecimal && valueMaxLength) {
    const firstValue = Number((valueMaxLength + "").split(".")[0]);
    const secondValue = Number((valueMaxLength + "").split(".")[1]);

    // handle decimal type number
    let result = valueInput
      .split("")
      .filter((item, index) => {
        //validate only one dot character
        if (
          valueInput.split("").filter((char) => char === ".").length > 1 &&
          item === "." &&
          index === valueInput.split("").length - 1
        ) {
          return false;
        }

        // validate dot > 1 and index not is a last character
        if (
          valueInput.split("").filter((char) => char === ".").length > 1 &&
          item === "."
        ) {
          const indexOfDotFirst = valueInput.split("").indexOf(".");
          if (index !== indexOfDotFirst) {
            return false;
          }
        }

        // validate dot first character
        if (item === "." && index === 0) {
          return false;
        }

        // validate enter dot before dot existed
        if (
          item !== "." &&
          index === firstValue - secondValue &&
          valueInput.split("").indexOf(".") > firstValue - secondValue
        ) {
          return false;
        }

        // validate enter dot length > 1
        if (
          index === firstValue - secondValue &&
          item !== "." &&
          !valueInput.split("").some((char) => char === ".")
        ) {
          return false;
        }

        // validate enter value index > firstValue and not exist dot character
        // validate enter value index > firstValue and last character is .
        if (
          index === firstValue &&
          (item === "." || !valueInput.includes("."))
        ) {
          return false;
        }

        // handle validate max `secondValue` character after dot
        const indexOfDotFirst = valueInput.split("").indexOf(".");
        if (indexOfDotFirst > 0 && index > indexOfDotFirst + secondValue) {
          return false;
        }

        return item === "." || vowelsRegex.test(item);
      })
      .join()
      .replaceAll(",", "");

    // handle remove dot if index === firstValue - secondValue
    if (
      result.length > firstValue - secondValue &&
      !valueInput.split("").some((char) => char === ".")
    ) {
      result = result
        .split("")
        .splice(0, firstValue - secondValue)
        .join()
        .replaceAll(",", "");
    }

    return result;
  }
  return valueInput
    .split("")
    .filter((value) => vowelsRegex.test(value))
    .join()
    .replaceAll(",", "");
}
