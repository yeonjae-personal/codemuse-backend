export const formatFileSize = (bytes: number, decimalPoint = 2): string => {
  if (bytes === 0) return "0 Bytes";
  const byte = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
  const index = Math.floor(Math.log(bytes) / Math.log(byte));

  return `${(bytes / Math.pow(byte, index)).toFixed(decimalPoint)} ${
    sizes[index as number]
  }`;
};

export const isValidFileType = (
  file: File,
  allowedTypes: string[]
): boolean => {
  return !allowedTypes.includes(file.type);
};

export const isValidFileSize = (file: File, maxSizeInMB: number): boolean => {
  const maxSizeInBytes = maxSizeInMB * 1024 * 1024;
  return file.size >= maxSizeInBytes;
};
