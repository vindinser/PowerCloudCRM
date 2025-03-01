## PowerCloudCRM-前端

> 动力云客项目是一款商业的集营销销售为一体的客户关系管理系统，其采用信息化、数字化方式来进行营销销售及客户管理；
>
> 云客即指海量客户，通过技术方式实现的这一套系统，可用于自动化分析销售、市场营销、客户服务等各个流程，建立起以客户为中心的信息化管理，从而支持更加有效的市场营销、销售与服务等各个环节，提高效率，提高效益。



作者：zs

 版本：1.0.0

 版权：zs



### 技术栈

- 环境：node 20+（v20.10.0）

- 编程： Vue3 + Javascript
- 构建工具：Vite
- 路由 | 状态管理：Vue-router、Pinina
- UI Element：ElementUI Plus



###  项目结构

```
  .
  ├── README.md
  ├── build                          打包脚本相关
  │   ├── config                     配置文件
  │   ├── generate                   生成器
  │   ├── script                     脚本
  │   └── vite                       vite配置
  ├── public                         公共静态资源目录
  ├── src
  │   ├── apis                       请求中心
  │   ├── assets                     资源目录（图片、scss等）
  │   ├── pages                      页面目录
  │   ├── App.vue                    主应用
  │   ├── style.scss                 全局样式
  │   └── main.js                    主入口
  ├── .env.[mode]                    （全局）环境变量配置文件
  ├── index.html                     项目入口
  ├── package.json                   包描述（说明）文件
  ├── package-lock.json              包详细描述（说明）文件
  └── vite.config.js                 vite配置
```



### 项目运行

``` bash
  # 安装依赖
  npm i
  
  # 启动项目
  npm run dev
  npm start
  
  # 构建项目
  npm run build
```



