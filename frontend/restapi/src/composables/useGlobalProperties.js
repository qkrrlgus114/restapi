import { getCurrentInstance } from "vue";

export function useGlobalProperties() {
  const { appContext } = getCurrentInstance();
  return appContext.config.globalProperties;
}
