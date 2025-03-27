module.exports = {
  // 基础配置
  printWidth: 100,         // 每行代码最大长度
  tabWidth: 2,            // 缩进空格数
  useTabs: false,         // 使用空格而不是tab缩进
  semi: true,             // 默认使用分号
  semiStyle: 'last',      // 分号位置
  // 配置特殊情况下不使用分号
  insertPragma: false,    
  proseWrap: 'preserve',
  singleQuote: true,      // 使用单引号
  trailingComma: 'none',  // 不使用尾逗号
  bracketSpacing: true,   // 对象括号空格 { foo: bar }
  
  // 箭头函数配置
  arrowParens: 'always',  // 箭头函数参数始终使用括号 (x) => x

  // Vue 相关
  vueIndentScriptAndStyle: true,  // 缩进 Vue 文件中的 <script> 和 <style> 标签
  htmlWhitespaceSensitivity: 'css', // HTML 空格敏感度
  
  // 换行配置
  endOfLine: 'auto',      // 自动识别换行符
  bracketSameLine: false, // 多行HTML标签的'>'放在最后一行的末尾，而不是单独放一行

  // 特殊文件配置
  overrides: [
    {
      files: ['*.js', '*.ts', '*.vue'],
      options: {
        semi: false,      // 对于这些文件类型，禁用分号
        parser: 'babel'   // 使用 babel 解析器
      }
    },
    {
      files: '*.vue',
      options: {
        parser: 'vue'
      }
    },
    {
      files: '*.ts',
      options: {
        parser: 'typescript'
      }
    }
  ]
}