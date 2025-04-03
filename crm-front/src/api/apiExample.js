import http from '@/core/http.js';

// 获取商品列表（自动缓存10分钟）
const getProducts = async () => {
  return http.get('/api/products', { category: 'electronics' }, {
    cache: true,
    cacheTTL: 600000,
    retryCount: 2
  });
};

// 使用示例
getProducts().then(products => {
  console.log('缓存商品数据:', products);
});

// 用户注册（密码加密）
const registerUser = async (userData) => {
  return http.post('/api/users', userData, {
    encrypt: true,
    encryptFields: ['password', 'creditCard'],
    showSuccess: false, // 禁用成功提示
    headers: {
      'X-Request-Source': 'WebApp'
    }
  });
};

// 使用示例
registerUser({
  name: 'John',
  email: 'john@example.com',
  password: 'P@ssw0rd',
  creditCard: '4111111111111111'
}).then(user => {
  console.log('注册成功:', user);
});

// 更新用户信息（失败自动重试）
const updateUserProfile = async (userId, profile) => {
  return http.put(`/api/users/${userId}`, profile, {
    retryCount: 3,
    retryDelay: 2000,
    onRetry: (attempt) => {
      console.log(`第${attempt}次重试...`);
    }
  });
};

// 使用示例
updateUserProfile(123, {
  avatar: 'new-avatar.jpg',
  preferences: { theme: 'dark' }
}).then(() => {
  console.log('资料更新成功');
});

// 方式1：查询参数删除
const deleteArticle = async (articleId) => {
  return http.delete('/api/articles', { id: articleId }, {
    successMessage: '文章删除成功'
  });
};

// 方式2：请求体删除（适用于复杂数据）
const bulkDeleteComments = async (commentIds) => {
  return http.delete('/api/comments', null, {
    data: { ids: commentIds },
    encrypt: true
  });
};

// 使用示例
// 删除单个文章
deleteArticle(456).then(() => {
  console.log('文章已删除');
});

// 批量删除评论
bulkDeleteComments([101, 102, 103]).then(() => {
  console.log('评论批量删除完成');
});

// 获取敏感金融数据（自动缓存+加密）
const getFinancialData = async (accountId) => {
  return http.get(`/api/finance/${accountId}`, null, {
    cache: true,
    cacheTTL: 300000, // 5分钟缓存
    encrypt: true,
    encryptFields: ['balance', 'transactions'],
    headers: {
      'X-Auth-Token': localStorage.getItem('token')
    }
  });
};

// 使用示例
getFinancialData('ACC-2023').then(data => {
  console.log('加密的财务数据:', data);
});

// 扩展加密方法
class CustomHttpClient extends HttpClient {
  _encryptData(data, fields) {
    // 使用国密SM4算法
    if (this.config.encrypt.algorithm === 'SM4') {
      return this._sm4Encrypt(data, fields);
    }
    return super._encryptData(data, fields);
  }

  _sm4Encrypt(data, fields) {
    // 实现具体加密逻辑
  }
}

// 初始化实例
const secureClient = new CustomHttpClient({
  encrypt: {
    enabled: true,
    algorithm: 'SM4'
  }
});

// 删除用户时自动清理缓存
const deleteUser = async (userId) => {
  await http.delete(`/api/users/${userId}`);

  // 清理相关缓存
  const cacheKey = http._getCacheKey('GET', '/api/users', {});

  http._clearCache(cacheKey);

  return true;
};

const uploadLargeFile = async (file) => {
  const formData = new FormData();

  formData.append('file', file);

  return http.post('/api/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
    onUploadProgress: (progressEvent) => {
      const percent = Math.round(
        progressEvent.loaded * 100 / progressEvent.total
      );

      console.log(`上传进度: ${percent}%`);
    }
  });
};

// 统一错误处理中间件
const withErrorHandling = (fn) => {
  return async (...args) => {
    try {
      return await fn(...args);
    } catch (error) {
      switch(error.code) {
      case 401:
        store.dispatch('logout');
        break;
      case 404:
        router.push('/not-found');
        break;
      default:
        console.error('请求错误:', error);
      }
      throw error;
    }
  };
};

// 包装请求方法
const safeGetUser = withErrorHandling((id) => {
  return http.get(`/api/users/${id}`);
});