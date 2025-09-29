interface SnackbarState {
  visible: boolean;
  message: string;
  type: "success" | "warning" | "info" | "error";
}

export type { SnackbarState };
