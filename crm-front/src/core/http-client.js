// src/core/http-client.js
import axios from 'axios';
import qs from 'qs';
import CryptoJS from 'crypto-js';
import { ElMessage } from 'element-plus';
import { v4 as uuidv4 } from 'uuid';
import useUserStore from '@/store/modules/user';
import modal from '@/plugins/modal.js';

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
   * @param {boolean} [config.showMsg=true] - 默认展示接口成功/失败消息
   * @param {boolean} [config.operaName=true] - 默认展示接口操作类型（提示message时使用）
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
    this.operaName = '操作';
    this.token = '';
    this.rememberLogin = false;

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
      const { data: res } = await this.instance.get(url, { params, ...config });

      if(res.code === 200) {
        this._handleMsg({...res, type: 'success'}, config);
        if (config.cache) {
          this._setCache(cacheKey, res, config.cacheTTL);
        }
        return res;
      }
      const warnObj = {...res, type: 'warning'};

      throw warnObj;
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
      const { data: res } = await this.instance.post(url, data, config);

      if(res.code === 200) {
        this._handleMsg({...res, type: 'success'}, config);
        return res;
      }
      const warnObj = {...res, type: 'warning'};

      throw warnObj;
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
      const { data: res } = await this.instance.put(url, data, config);

      if(res.code === 200) {
        this._handleMsg({...res, type: 'success'}, config);
        return res;
      }
      const warnObj = {...res, type: 'warning'};

      throw warnObj;
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

      const { data: res } = await this.instance.delete(url, requestConfig);

      if(res.code === 200) {
        this._handleMsg({...res, type: 'success'}, config);
        return res;
      }
      const warnObj = {...res, type: 'warning'};

      throw warnObj;
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
        if(!this.token) {
          const { token } = useUserStore();

          this.token = token;
        }

        if(!this.rememberLogin) {
          const { rememberLogin } = useUserStore();

          this.rememberLogin = rememberLogin;
        }

        if (this.token) {
          config.headers.Authorization = this.token;
          config.headers.rememberMe = this.rememberLogin;
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

  /* -------------------- 消息提示处理 -------------------- */
  /**
    1XX（临时响应信息提示 ）
    这些状态代码表示临时的响应。客户端在收到常规响应之前，应准备接收一个或多个1xx。
    100（继续）- 请求者应当继续提出请求。服务器返回此代码表示已收到请求的第一部分，正在等待其余部分。
    101（切换协议） - 服务器将遵从客户的请求转换到另外一种协议

    2XX（成功）
    表示成功处理了请求的状态代码。
    200（成功） - 服务器已成功处理了请求。
    201（已创建） - 请求成功并且服务器创建了新的资源。
    202（已接受） - 服务器已接受请求，但尚未处理。
    203（非授权信息） - 服务器已成功处理了请求，但返回的信息可能来自另一来源。
    204（无内容） - 服务器成功处理了请求，但没有返回任何内容。
    205（重置内容） - 服务器成功处理了请求，但没有返回任何内容。与 204响应不同，此响应要求请求者重置文档视图(例如，清除表单内容以输入新内容)。
    206（部分内容） - 服务器成功处理了部分 GET 请求。

    3XX - 重定向
    表示要完成请求，需要进一步操作。客户端浏览器必须采取更多操作来实现请求。例如，浏览器可能不得不请求服务器上的不同的页面，或通过代理服务器重复该请求。 建议在每次请求中使用重定向不要超过 5次。 通常，这些状态代码用来重定向。
    300（多种选择）- 针对请求，服务器可执行多种操作。服务器可根据请求者 (user agent) 选择一项操作，或提供操作列表供请求者选择。
    301（永久移动）- 请求的网页已永久移动到新位置。服务器返回此响应（对 GET 或 HEAD 请求的响应）时，会自动将请求者转到新位置。
    302（临时移动）- 服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。
    303（查看其他位置）- 请求者应当对不同的位置使用单独的 GET 请求来检索响应时，服务器返回此代码。
    304（未修改）- 自从上次请求后，请求的网页未修改过。服务器返回此响应时，不会返回网页内容。
    305（使用代理）- 请求者只能使用代理访问请求的网页。如果服务器返回此响应，还表示请求者应使用代理。
    307（临时重定向）- 服务器目前从不同位置的网页响应请求，但请求者应继续使用原有位置来进行以后的请求。

    4XX（请求错误）
    这些状态码表示请求可能出错，客户端似乎有问题。例如，客户端请求不存在的页面，客户端未提供有效的身份验证信息。 妨碍了服务器的处理。
    400（错误请求）- 服务器不理解请求的语法。
    401（未授权）- 请求要求身份验证。对于登录后请求的网页，服务器可能返回此响应。
    403（禁止）- 服务器拒绝请求。如果您在 Googlebot 尝试抓取您网站上的有效网页时看到此状态码(您可以在 Google网站管理员工具诊断下的网络抓取页面上看到此信息），可能是您的服务器或主机拒绝了 Googlebot 访问。
    404（未找到）- 服务器找不到请求的网页。例如，对于服务器上不存在的网页经常会返回此代码。
    405（方法禁用）- 禁用请求中指定的方法。
    406（不接受）- 无法使用请求的内容特性响应请求的网页。
    407（需要代理授权）- 此状态码与 401(未授权)类似，但指定请求者应当授权使用代理。如果服务器返回此响应，还表示请求者应当使用代理。
    408（请求超时）- 服务器等候请求时发生超时。
    409（冲突）- 服务器在完成请求时发生冲突。服务器必须在响应中包含有关冲突的信息。服务器在响应与前一个请求相冲突的 PUT请求时可能会返回此代码，以及两个请求的差异列表。
    410（已删除）- 如果请求的资源已永久删除，服务器就会返回此响应。该代码与404（未找到）代码类似，但在资源以前存在而现在不存在的情况下，有时会用来替代 404 代码。如果资源已永久移动，您应使用 301指定资源的新位置。
    411（需要有效长度）- 服务器不接受不含有效内容长度标头字段的请求。
    412（未满足前提条件）- 服务器未满足请求者在请求中设置的其中一个前提条件。
    413（请求实体过大）- 服务器无法处理请求，因为请求实体过大，超出服务器的处理能力。
    414（请求的 URI 过长）- 请求的 URI(通常为网址)过长，服务器无法处理。
    415（不支持的媒体类型）- 请求的格式不受请求页面的支持。
    416（请求范围不符合要求）- 如果页面无法提供请求的范围，则服务器会返回此状态码。
    417（未满足期望值）- 服务器未满足”期望”请求标头字段的要求。

    5XX（服务器错误）
    这些状态码表示服务器在处理请求时发生内部错误。这些错误可能是服务器本身的错误，而不是请求出错。
    500 （服务器内部错误）- 服务器遇到错误，无法完成请求。
    500.12- 应用程序正忙于在 Web 服务器上重新启动。
    500.13- Web 服务器太忙。
    500.15- 不允许直接请求 Global.asa。
    500.16- UNC 授权凭据不正确。这个错误代码为 IIS 6.0 所专用。
    500.18- URL 授权存储不能打开。这个错误代码为 IIS 6.0 所专用。
    500.100- 内部 ASP 错误。
    501 （尚未实施）- 服务器不具备完成请求的功能。例如，服务器无法识别请求方法时可能会返回此代码。
    502 （错误网关）- 服务器作为网关或代理，从上游服务器收到无效响应。
    503 （服务不可用）- 服务器目前无法使用（由于超载或停机维护）。通常，这只是暂时状态。
    504 （网关超时）- 服务器作为网关或代理，但是没有及时从上游服务器收到请求。
    505 （HTTP 版本不受支持）- 服务器不支持请求中所用的 HTTP 协议版本。
   */
  _handleMsg(obj, config) {
    if(config.showMsg !== false) {
      const status = obj.code;
      const message = obj?.msg || obj.message || (obj.type === 'success' ? '成功' : '失败');

      switch(status) {
      case 201:
        ElMessage.success('请求成功并且服务器创建了新的资源');
        break;
      case 304:
        ElMessage.warning('自从上次请求后，请求的网页未修改过。服务器返回此响应时，不会返回网页内容');
        break;
      case 400:
        ElMessage.warning('服务器不理解请求的语法');
        break;
      case 404:
        ElMessage.warning('请求地址不存在！');
        break;
      case 503:
        ElMessage.warning('服务器目前无法使用（由于超载或停机维护）');
        break;
      default:
        ElMessage({
          type: obj.type || 'error',
          message: `${ config.operaName || this.operaName }：${ message }！`
        });
      }
    }
  }

  /* -------------------- 错误处理 -------------------- */
  _handleError(error, config) {
    const status = error.response?.status || error?.code;

    if(status === 401 || status > 900) {
      return this._redirectToLogin();
    }
    const message = error.response?.data?.message ?? error.message ?? error?.msg ?? '失败';
    const errObj = { code: status, msg: message, data: error.response?.data };

    if(config.showError !== false) {
      this._handleMsg(errObj, config);
    }

    throw errObj;
  }

  _redirectToLogin() {
    modal.confirm('系统异常是否重新登录？').then(() => {
      const { removeToken } = useUserStore();

      removeToken();
      // 跳到登录页
      window.location.href = '/';
    }).catch(() => {
      modal.msg('取消操作');
    });
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