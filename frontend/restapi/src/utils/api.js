import axios from "./axios";
import router from "@/router";
import { useMainStore } from "@/store/store.js";

const handleApiError = (error) => {
  const store = useMainStore();

  if (
    error.response.status === 401 &&
    error.response.data.error === "쿠키가 존재하지 않습니다."
  ) {
    store.logout();
    router.push("/login");
  } else {
    alert(error.response ? error.response.data.message : error.message);
  }
};

const apiGet = async (url) => {
  try {
    const response = await axios.get(url);
    return response.data;
  } catch (error) {
    handleApiError(error);
    throw error;
  }
};

const apiPost = async (url, data = {}) => {
  try {
    const response = await axios.post(url, data);
    return response.data;
  } catch (error) {
    handleApiError(error);
    throw error;
  }
};

const apiPatch = async (url, data = {}) => {
  try {
    const response = await axios.patch(url, data);
    return response.data;
  } catch (error) {
    handleApiError(error);
    throw error;
  }
};

const apiDelete = async (url) => {
  try {
    const response = await axios.delete(url);
    return response.data;
  } catch (error) {
    handleApiError(error);
    throw error;
  }
};

const getBaseURL = () => {
  return axios.defaults.baseURL;
};

export { apiGet, apiPost, getBaseURL, apiPatch, apiDelete };
