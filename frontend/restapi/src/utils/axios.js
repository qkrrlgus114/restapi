import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  // baseURL: "https://restapi.store",
  withCredentials: true,
});

export default axiosInstance;
