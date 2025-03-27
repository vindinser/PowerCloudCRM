module.exports = {
  // 继承推荐规范配置
  extends: [
    "stylelint-config-standard",
    "stylelint-config-recommended-scss",
    "stylelint-config-recommended-vue/scss",
    "stylelint-config-html/vue",
    "stylelint-config-recess-order",
  ],
  // 指定不同文件对应的解析器
  overrides: [
    {
      files: ["**/*.{vue,html}"],
      customSyntax: "postcss-html",
    },
    {
      files: ["**/*.{css,scss}"],
      customSyntax: "postcss-scss",
    },
  ],
  // 自定义规则
  rules: {
    // 允许 global 、export 、v-deep等伪类
    "selector-pseudo-class-no-unknown": [
      true,
      {
        ignorePseudoClasses: ["global", "export","v-deep", "deep"],
      },
    ],
    "scss/at-import-partial-extension": null,
    'scss/dollar-variable-pattern': null, //解决类名不允许下划线
    'scss/double-slash-comment-whitespace-inside':null,// 解决双斜杠注释后要有一个空格
    'selector-class-pattern': null,
    'block-no-empty': null,
    'no-empty-source': null,
    'no-descending-specificity': null, // 禁止在具有较高优先级的选择器后出现被其覆盖的较低优先级的选择器
    'selector-pseudo-element-no-unknown': [
      true,
      {
        ignorePseudoElements: ['v-deep'],
      },
    ],
    'selector-pseudo-class-no-unknown': [
      true,
      {
        ignorePseudoClasses: ['deep'],
      },
    ],
    'font-family-no-missing-generic-family-keyword': null,
    'no-duplicate-selectors': null,
    'selector-id-pattern': null, //指定id选择器的模式
    'custom-property-pattern': null, //为自定义属性指定模式。
    'no-invalid-double-slash-comments': null, //禁止使用双斜杠注释（关闭）
    'at-rule-no-unknown': [
      true,
      {
        ignoreAtRules: ['mixin', 'if', 'else', 'include'],
      },
    ],
    'property-no-unknown': [
      true,
      {
        ignoreProperties: ['line-clamp'],
      },
    ],
    'order/properties-order': [ // 规则顺序
      'position',
      'content',
      'top',
      'right',
      'bottom',
      'left',
      'float',
      'display',
      'margin',
      'margin-top',
      'margin-right',
      'margin-bottom',
      'margin-left',
      'margin-collapse',
      'margin-top-collapse',
      'margin-right-collapse',
      'margin-bottom-collapse',
      'margin-left-collapse',
      'border',
      'border-radius',
      'outline',
      'outline-offset',
      'padding',
      'padding-top',
      'padding-right',
      'padding-bottom',
      'padding-left',
      'width',
      'height',
      'max-width',
      'max-height',
      'min-width',
      'min-height',
      'clip',
      'font',
      'font-family',
      'font-size',
      'font-smoothing',
      'osx-font-smoothing',
      'font-style',
      'font-weight',
      'line-height',
      'letter-spacing',
      'word-spacing',
      'text-align',
      'text-decoration',
      'text-indent',
      'text-overflow',
      'text-rendering',
      'text-size-adjust',
      'text-shadow',
      'text-transform',
      'word-break',
      'word-wrap',
      'white-space',
      'vertical-align',
      'list-style',
      'list-style-type',
      'list-style-position',
      'list-style-image',
      'pointer-events',
      'opacity',
      'filter',
      'visibility',
      'size',
      'transform',
      'background',
      'background-color',
      'color',
      'clear',
      'cursor',
      'overflow',
      'overflow-x',
      'overflow-y',
      'z-index',
    ]
  },
};