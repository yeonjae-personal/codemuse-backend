const BASE_URL = "http://localhost:3000";

/**
 * Get random value from array
 * @param {string[]} array - array of string
 * @returns {string} - random value from array
 *
 * @example
 * const sysMsgLangCdOption =  ["KORN", "ENG"];
 * const getRandomValue =(array: string[])  => {
 * const randomIndex = Math.floor(Math.random() * array.length);
 * return array[randomIndex];
 * }
 **/
export const getRandomValue = (array: string[]) => {
  const randomIndex = Math.floor(Math.random() * array.length);
  return array[randomIndex as number];
};

/**
 * en: Format the path by removing the first 4 characters, which is "/api"
 * @param path
 * @returns url path
 */
export const formatPath = (path: string): string => {
  if (path.startsWith("/api")) {
    return path.slice(4); // Remove the first 4 characters, which is "/api"
  }
  return path;
};

/**
 * en: Get the full url by combining the base url and path
 * @param path
 * @returns full url
 */
export const getFullUrl = (path: string): string => {
  const formattedPath = formatPath(path);
  return `${BASE_URL}${formattedPath}`;
};
