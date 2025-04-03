// src/core/http.js
import HttpClient from './http-client';

export default new HttpClient({
  baseURL: import.meta.env.VITE_APP_BASE_API,
  timeout: 15000,
  retryCount: 3,
  cache: {
    maxSize: 200,
    defaultTTL: 600000 // 10分钟
  },
  encrypt: {
    enabled: true,
    storageKey: 'PROD_KEYS'
  }
});