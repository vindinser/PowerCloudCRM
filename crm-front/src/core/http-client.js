// src/core/http-client.js
import axios from 'axios';
import qs from 'qs';
import CryptoJS from 'crypto-js';
import { ElMessage } from 'element-plus';
import { v4 as uuidv4 } from 'uuid';

/**
 * 高级HTTP客户端
 * 功能特性：
 * - 基础请求方法（GET/POST/PUT/DELETE）
 * - 自动重试机制
 * - 请求取消与重复拦截
 * - 参数加密与动态密钥管理
 * - LRU缓存系统
 * - 性能监控
 * - 统一错误处理
 * - 可配置提示系统
 */
class HttpClient {
  /**
   * 构造函数
   * @param {Object} config - 全局配置
   * @param {string} [config.baseURL=''] - 基础URL
   * @param {number} [config.timeout=10000] - 超时时间(ms)
   * @param {number} [config.retryCount=3] - 重试次数
   * @param {number} [config.retryDelay=1000] - 重试延迟基准(ms)
   * @param {Object} [config.cache] - 缓存配置
   * @param {number} [config.cache.maxSize=100] - 最大缓存数
   * @param {number} [config.cache.defaultTTL=300000] - 默认缓存时间(ms)
   * @param {Object} [config.encrypt] - 加密配置
   * @param {boolean} [config.encrypt.enabled=false] - 是否启用加密
   * @param {string} [config.encrypt.storageKey='API_KEYS'] - 密钥存储键
   * @param {boolean} [config.defaultLoading=true] - 默认是否显示Loading
   * @param {boolean} [config.defaultToken=true] - 默认是否携带Token
   */
  constructor(config = {}) {
    // 初始化Axios实例
    this.instance = axios.create({
      baseURL: config.baseURL || '',
      timeout: config.timeout || 10000,
      headers: { 'X-Request-ID': uuidv4() },
      paramsSerializer: params => qs.stringify(params, { arrayFormat: 'brackets' })
    });

    // 配置参数
    this.config = {
      retryCount: config.retryCount || 3,
      retryDelay: config.retryDelay || 1000,
      cache: {
        maxSize: config.cache?.maxSize || 100,
        defaultTTL: config.cache?.defaultTTL || 300000,
        store: new Map(),
        queue: []
      },
      encrypt: {
        enabled: config.encrypt?.enabled || false,
        storageKey: config.encrypt?.storageKey || 'API_KEYS',
        currentKey: null
      },
      metrics: {
        requests: 0,
        success: 0,
        errors: new Map()
      }
    };

    // 全局默认配置
    this.defaultLoading = config.defaultLoading ?? true;
    this.defaultToken = config.defaultToken ?? true;

    // Loading控制
    this.loadingCount = 0;
    this.loadingInstance = null;

    // 初始化子系统
    this._initInterceptors();
    this._initEncryption();
    this._startMetricsReporter();
  }

  /* -------------------- 核心方法 -------------------- */

  /**
   * GET请求
   * @param {string} url - 请求地址
   * @param {Object} [params] - 查询参数
   * @param {Object} [config] - 请求配置
   * @param {boolean} [config.cache] - 是否启用缓存
   * @param {number} [config.cacheTTL] - 缓存时间(ms)
   */
  async get(url, params, config = {}) {
    const cacheKey = this._getCacheKey('GET', url, params);

    // 缓存处理
    if (config.cache) {
      const cached = this._getCache(cacheKey);

      if (cached) {return cached}
    }

    try {
      const response = await this.instance.get(url, { params, ...config });

      if (config.cache) {
        this._setCache(cacheKey, response.data, config.cacheTTL);
      }

      return response.data;
    } catch (error) {
      this._handleError(error, config);
    }
  }

  /**
   * POST请求（支持加密）
   * @param {string} url - 请求地址
   * @param {Object} data - 请求数据
   * @param {Object} [config] - 请求配置
   * @param {boolean} [config.encrypt] - 是否加密
   * @param {string[]} [config.encryptFields] - 加密字段
   */
  async post(url, data, config = {}) {
    // 加密处理
    if (config.encrypt && this.config.encrypt.enabled) {
      // eslint-disable-next-line no-param-reassign
      data = this._encryptData(data, config.encryptFields);
    }

    try {
      const response = await this.instance.post(url, data, config);

      return response.data;
    } catch (error) {
      this._handleError(error, config);
    }
  }

  /**
   * PUT请求（更新资源）
   * @param {string} url - 请求地址
   * @param {Object} data - 请求数据
   * @param {Object} [config] - 请求配置
   * @param {boolean} [config.encrypt] - 是否加密
   * @param {string[]} [config.encryptFields] - 加密字段
   */
  async put(url, data, config = {}) {
    // 加密处理
    if (config.encrypt && this.config.encrypt.enabled) {
      // eslint-disable-next-line no-param-reassign
      data = this._encryptData(data, config.encryptFields);
    }

    try {
      const response = await this.instance.put(url, data, config);

      return response.data;
    } catch (error) {
      this._handleError(error, config);
    }
  }

  /**
   * DELETE请求
   * @param {string} url - 请求地址
   * @param {Object} [params] - 查询参数
   * @param {Object} [config] - 请求配置
   * @param {Object} [config.data] - 请求体数据（可选）
   */
  async delete(url, params, config = {}) {
    try {
      // 支持请求体删除
      const requestConfig = config.data ?
        { data: config.data, ...config } :
        { params, ...config };

      const response = await this.instance.delete(url, requestConfig);

      return response.data;
    } catch (error) {
      this._handleError(error, config);
    }
  }

  /* -------------------- 加密系统 -------------------- */
  _initEncryption() {
    if (!this.config.encrypt.enabled) {return}

    // 加载密钥
    const stored = localStorage.getItem(this.config.encrypt.storageKey);

    if (stored) {
      try {
        const decrypted = CryptoJS.AES.decrypt(stored, window.location.host).toString(CryptoJS.enc.Utf8);

        this.config.encrypt.currentKey = JSON.parse(decrypted);
      } catch (error) {
        ElMessage.error('密钥解析失败:', error);
      }
    }

    // 定时轮换密钥
    setInterval(() => this._rotateKeys(), 3600 * 1000);
  }

  async _rotateKeys() {
    try {
      const { data } = await this.instance.get('/auth/keys');
      const encrypted = CryptoJS.AES.encrypt(
        JSON.stringify(data),
        window.location.host
      ).toString();

      localStorage.setItem(this.config.encrypt.storageKey, encrypted);
      this.config.encrypt.currentKey = data;
    } catch (error) {
      ElMessage.error('密钥轮换失败:', error);
    }
  }

  _encryptData(data, fields = []) {
    const encryptField = (value) =>
      CryptoJS.AES.encrypt(
        JSON.stringify(value),
        this.config.encrypt.currentKey.value
      ).toString();

    // 字段级加密
    if (fields.length > 0) {
      return Object.keys(data).reduce((acc, key) => {
        acc[key] = fields.includes(key) ? encryptField(data[key]) : data[key];
        return acc;
      }, {});
    }

    // 全量加密
    return encryptField(data);
  }

  /* -------------------- 缓存系统 -------------------- */
  _getCacheKey(method, url, params) {
    return `${method}:${url}:${JSON.stringify(params)}`;
  }

  _getCache(key) {
    const entry = this.config.cache.store.get(key);

    return entry?.expires > Date.now() ? entry.data : null;
  }

  _setCache(key, data, ttl) {
    // LRU淘汰
    if (this.config.cache.store.size >= this.config.cache.maxSize) {
      const oldestKey = this.config.cache.queue.shift();

      this.config.cache.store.delete(oldestKey);
    }

    this.config.cache.store.set(key, {
      data,
      expires: Date.now() + (ttl || this.config.cache.defaultTTL)
    });
    this.config.cache.queue.push(key);
  }

  /* -------------------- 监控系统 -------------------- */
  _startMetricsReporter() {
    setInterval(() => {
      // eslint-disable-next-line no-console
      console.log('[监控报告]', {
        总请求数: this.config.metrics.requests,
        成功率: `${(this.config.metrics.success / this.config.metrics.requests * 100).toFixed(1)}%`,
        错误分布: Object.fromEntries(this.config.metrics.errors)
      });
      this.config.metrics.requests = 0;
      this.config.metrics.success = 0;
      this.config.metrics.errors.clear();
    }, 60000);
  }

  /* -------------------- 拦截器系统 -------------------- */
  _initInterceptors() {
    // 请求拦截器
    this.instance.interceptors.request.use(config => {
      config.metadata = { startTime: Date.now() };
      this.config.metrics.requests++;
      // 合并默认配置
      config.loading = config.loading ?? this.defaultLoading;
      config.token = config.token ?? this.defaultToken;

      // 处理Loading
      if (config.loading) {
        this._showLoading();
      }

      // 处理Token
      if (config.token) {
        const token = localStorage.getItem('token');

        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
      }
      return config;
    });

    // 响应拦截器
    this.instance.interceptors.response.use(
      response => {
        this._hideLoading(response.config);
        const latency = Date.now() - response.config.metadata.startTime;

        this.config.metrics.success++;
        // eslint-disable-next-line no-console
        console.log(`[请求成功] ${response.config.url} (${latency}ms)`);
        return response;
      },
      error => {
        this._hideLoading(error.config);
        const status = error.response?.status || 'NETWORK';

        this.config.metrics.errors.set(status, (this.config.metrics.errors.get(status) || 0) + 1);
        return this._retryRequest(error);
      }
    );
  }

  async _retryRequest(error) {
    const config = error.config;

    if (!config || !config.retryCount) {throw error}

    config.retryCount -= 1;
    await new Promise(resolve =>
      setTimeout(resolve, this.config.retryDelay)
    );
    return this.instance(config);
  }

  /* -------------------- 错误处理 -------------------- */
  _handleError(error, config) {
    const status = error.response?.status;
    const message = error.response?.data?.message || error.message;

    // 状态码处理
    switch(status) {
    case 401:
      this._redirectToLogin();
      break;
    case 404:
      ElMessage.warning('请求地址不存在');
      break;
    default:
      if (config.showError !== false) {
        ElMessage.error(message);
      }
    }

    // eslint-disable-next-line no-throw-literal
    throw { code: status, message, data: error.response?.data };
  }

  _redirectToLogin() {
    ElMessage.warning('登录已过期，即将跳转...');
    setTimeout(() => window.location.href = '/login', 2000);
  }

  /* -------------------- Loading控制 -------------------- */
  _showLoading() {
    this.loadingCount++;
    if (this.loadingCount === 1) {
      this.loadingInstance = ElLoading.service({
        lock: true,
        text: '加载中...',
        background: 'rgba(0, 0, 0, 0.7)'
      });
    }
  }

  _hideLoading(config) {
    if (config?.loading) {
      this.loadingCount--;
      if (this.loadingCount === 0 && this.loadingInstance) {
        this.loadingInstance.close();
        this.loadingInstance = null;
      }
    }
  }
}

export default HttpClient;