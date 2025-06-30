<!-- Start of Selection -->
<!-- 全局数据表格组件 -->
<template>
  <div v-loading="loading" class="main-table">
    <div v-if="isHead" class="table-head">
      <!-- 左侧按钮 · 自定义插槽 -->
      <div>
        <slot name="table-head" />
      </div>
      <!-- 右侧按钮 · 自定义列表 -->
      <div class="head-right">
        <!-- 右侧按钮 · 自定义插槽 -->
        <div style="margin-right: 16px">
          <slot name="head-right" />
        </div>
        <!-- 自定义列表弹出框 -->
        <el-popover v-model:visible="customPanel" placement="bottom-end" :width="500" trigger="click">
          <!-- 配置区域 -->
          <Draggable v-model="customList" item-key="prop">
            <template #item="{ element: item }">
              <el-checkbox v-model="item.show" style="width: 120px; height: 28px; margin-left: 0px">{{ item.label }}</el-checkbox>
            </template>
          </Draggable>
          <div style="margin-top: 8px; text-align: right">
            <el-button :size="size" @click="customPanel = false">取消</el-button>
            <el-button type="primary" :size="size" @click="confirm">确定</el-button>
          </div>
          <!-- 激活按钮 -->
          <template #reference>
            <el-button v-if="isCustomBtn" :size="size" @click="initCustomList">自定义列表</el-button>
          </template>
        </el-popover>
      </div>
    </div>
    <div v-viewer class="table-body">
      <el-table
        ref="tableRef"
        :key="tableKey"
        class="drop-table"
        :size="size"
        :data="list"
        :header-cell-style="isCustomHeaderBg ? customTableStyle : tableStyle"
        :height="height"
        :default-sort="defaultSort"
        :row-key="rowKey"
        :tree-props="treeProps"
        :default-expand-all="isExpandAll"
        :show-summary="isShowSum"
        :summary-method="getSummaries"
        :highlight-current-row="highlightCurrentRow"
        :row-class-name="rowClassName"
        @selection-change="handleSelectionChange"
        @select-all="handleSelectChange"
        @select="handleSelectChange"
        @sort-change="handleSortChange"
        @expand-change="expandChange"
        @current-change="handleCurrentChange"
      >
        <!-- 多选 -->
        <el-table-column v-if="isSelection" type="selection" label="序号" width="50" align="center" />
        <!-- 序号 -->
        <el-table-column v-if="isIndex" type="index" width="50" align="center" :fixed="indexFixed" />
        <!-- 操作 · 插槽 -->
        <slot name="table-opera" />
        <!-- 数据 -->
        <template v-for="(item, index) in columns">
          <template v-if="item.show">
            <!-- button -->
            <el-table-column
              v-if="item.type === 'button'"
              :key="'table-btn-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
              :prop="item.prop"
            >
              <template #default="scope">
                <el-button
                  link
                  :size="size"
                  :type="item.btnType || 'primary'"
                  :style="{ color: item.color || scope.row[item.isColor] }"
                  @click="buttonClick(item, scope.row)"
                >{{ scope.row[item.prop] }}</el-button>
              </template>
            </el-table-column>
            <!-- 自定义按钮 -->
            <el-table-column
              v-else-if="item.type === 'customButton'"
              :key="'table-btn-custom' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-button
                  v-if="scope.row[item.prop] === item.buttonShow"
                  link
                  :size="size"
                  style="border: none"
                  @click="buttonClick(item, scope.row)"
                >{{ handleCustomText(scope, item) }}</el-button
                >
                <span v-else>{{ handleCustomText(scope, item) }}</span>
              </template>
            </el-table-column>
            <!-- 走接口后修改switch状态 -->
            <el-table-column
              v-else-if="item.type === 'switch'"
              :key="'table-switch-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-switch
                  :value="scope.row[item.prop]"
                  inline-prompt
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="开"
                  inactive-text="关"
                  @change="switchChange($event, item, scope)"
                />
              </template>
            </el-table-column>
            <!-- 直接修改switch状态 -->
            <el-table-column
              v-else-if="item.type === 'syncSwitch'"
              :key="'table-sync-switch-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-switch
                  v-model="scope.row[item.prop]"
                  inline-prompt
                  active-color="#13ce66"
                  inactive-color="#ff4949"
                  active-text="是"
                  inactive-text="否"
                  :active-value="item.activeValue ? item.activeValue : 1"
                  :inactive-value="item.inactiveValue ? item.inactiveValue : 0"
                  :disabled="item.disabled ? item.rules.includes(scope.row[item.disabledName]) : false"
                  @change="switchChange($event, item, scope)"
                />
              </template>
            </el-table-column>
            <!-- icon -->
            <el-table-column
              v-else-if="item.type === 'icon'"
              :key="'table-icon-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-avatar :src="scope.row[item.prop]" />
              </template>
            </el-table-column>
            <!-- image -->
            <el-table-column
              v-else-if="item.type === 'image'"
              :key="'table-image-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-image v-if="scope.row[item.prop]" :src="image(scope.row[item.prop], 'min')" :data-src="image(scope.row[item.prop], 'max')" fit="cover" />
              </template>
            </el-table-column>
            <!-- avatar -->
            <el-table-column
              v-else-if="item.type === 'avatar'"
              :key="'table-avatar-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-avatar v-if="scope.row[item.prop]" :src="scope.row[item.prop]" :size="item.size || size" />
              </template>
            </el-table-column>
            <!-- pdf -->
            <el-table-column
              v-else-if="item.type === 'pdf'"
              :key="'table-pdf-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <!-- 若不固定高度可能会造成没有滚动条问题 -->
                <div v-if="scope.row[item.prop]" style="width: 100px; height: 142px; cursor: pointer" @click="pdfUrl = scope.row[item.prop]">
                  <VuePDF ref="pdf" :src="formatPdfSrc(scope.row[item.prop])" />
                </div>
              </template>
            </el-table-column>
            <!-- 文本 + tag -->
            <el-table-column
              v-else-if="item.type === 'tag'"
              :key="'table-tag-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
              :prop="item.prop"
            >
              <template #default="scope">
                <span class="table-cell">{{ item.relationship ? handelRelationshipText(scope, item) : scope.row[item.prop] }}</span>
                <template v-if="scope.row[item.tag] !== item.tagIsShow">
                  <el-tag :size="size" :type="item.tagType ? item.tagType : 'danger'" style="margin-left: 5px">{{ scope.row[item.tag] }}</el-tag>
                </template>
              </template>
            </el-table-column>
            <!-- tag -->
            <el-table-column
              v-else-if="item.type === 'tags'"
              :key="'table-tags-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <template v-if="scope.row[item.prop] !== ''">
                  <el-tag  v-if="scope.row[item.codes] == 2" :size="item.tagSize ? item.tagSize : 'small'" type="danger">{{ scope.row[item.prop] }}</el-tag>
                  <el-tag v-if="scope.row[item.codes] == 3" :size="item.tagSize ? item.tagSize : 'small'" type="warning">{{ scope.row[item.prop] }}</el-tag>
                </template>
              </template>
            </el-table-column>
            <!-- tagsList -->
            <el-table-column
              v-else-if="item.type === 'tagsList'"
              :key="'table-tagsList-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <template v-if="scope.row[item.prop].length && scope.row[item.prop].length !== 0">
                  <el-tag v-for="tag in item.limit || 2" :key="`tagsList-${tag}`" :size="item.tagSize || 'small'" :type="item.tagType || 'danger'">
                    {{ scope.row[item.prop][tag - 1] }}
                  </el-tag>
                </template>
              </template>
            </el-table-column>
            <!-- 文本内容，但展示内容在对象中（嵌套内容） -->
            <el-table-column
              v-else-if="item.type === 'nest'"
              :key="'table-nest-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <span class="table-cell">{{ scope.row[item.prop] ? scope.row[item.prop][item.children] : '' }}</span>
              </template>
            </el-table-column>
            <!-- 文本内容，但展示内容在数组中 -->
            <el-table-column
              v-else-if="item.type === 'array'"
              :key="'table-array-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <template v-if="scope.row[item.prop] && scope.row[item.prop].length !== 0">
                  <span
                    v-for="i in scope.row[item.prop].length > (item.limit || 2)
                      ? item.limit || 2
                      : scope.row[item.prop].length"
                    :key="`array-${i}`"
                    class="table-cell"
                  >
                    {{ `${i > 1 ? ',' : ''}${item.chain ? scope.row[item.prop][i - 1][item.chain] : scope.row[item.prop][i - 1]}` }}
                  </span>
                </template>
              </template>
            </el-table-column>
            <!-- input -->
            <el-table-column
              v-else-if="item.type === 'input'"
              :key="'table-input-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-input
                  v-model="scope.row[item.prop]"
                  class="borderNone"
                  :type="item.inputType ? item.inputType : 'text'"
                  :min="item.min"
                  :max="item.max"
                  @change="emit('input-change', { scope, prop: item.prop })"
                />
              </template>
            </el-table-column>
            <!-- select -->
            <el-table-column
              v-else-if="item.type === 'select'"
              :key="'table-select-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-select v-model="scope.row[item.prop]" class="borderNone" :placeholder="`请选择${item.placeholder}`">
                  <el-option v-for="i in item.options" :key="i.value" :label="i.label" :value="i.value" />
                </el-select>
              </template>
            </el-table-column>
            <!-- 文本内容但返回值为code，解析为name -->
            <el-table-column
              v-else-if="item.type === 'customText'"
              :key="'table-customText-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">{{ handleCustomText(scope, item) }}</template>
            </el-table-column>
            <!-- 文本内容，过多显示省略号（文字提示框展示全部文本内容） -->
            <el-table-column
              v-else-if="item.type === 'moreText'"
              :key="'table-moreText-' + index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
            >
              <template #default="scope">
                <el-tooltip :content="scope.row[item.prop]" placement="top-start">
                  <div class="more-text">{{ scope.row[item.prop] }}</div>
                </el-tooltip>
              </template>
            </el-table-column>
            <!-- 文本 文字颜色 -->
            <el-table-column
              v-else-if="item.type === 'color'"
              :key="'table-color-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
              :prop="item.prop"
            >
              <template #default="scope">
                <span v-if="scope.row[item.condition] === item.colorIsShow" class="table-cell" :style="{ color: item.color }">
                  {{ scope.row[item.prop] }}
                </span>
                <span v-else class="table-cell">{{ scope.row[item.prop] }}</span>
              </template>
            </el-table-column>
            <!-- 文本 文字颜色 -->
            <el-table-column
              v-else-if="item.type === 'customTextColor'"
              :key="'table-custom-text-color-' + index"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
              :prop="item.prop"
            >
              <template #default="scope">
                <span class="table-cell" :style="{ color: handelCustomColor(scope, item) }">
                  {{ item.isSuffix ? handelSuffix(scope, item) : scope.row[item.prop] }}
                </span>
              </template>
            </el-table-column>
            <!-- 多级表头 -->
            <el-table-column
              v-else-if="item.type === 'multi'"
              :key="'table-multi' + index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
            >
              <template v-for="(child, cIndex) in item.children">
                <el-table-column
                  v-if="child.type === 'customSort'"
                  :key="'table-multi-child-custom-sort' + cIndex"
                  :prop="child.prop"
                  :label="child.label"
                  :min-width="child.width"
                  :align="child.align"
                  :fixed="child.fixed"
                  :sortable="child.sortable"
                  :sort-method="(a, b) => handleSort(a, b, child.sortType, child.prop)"
                  :show-overflow-tooltip="isOverflowHidden"
                >
                  <template #default="scope">
                    <el-button v-if="child.isBtn" link @click="buttonClick(item, scope.row)" >{{ scope.row[child.prop] }}</el-button>
                    <div v-else :style="{ color: child.color || scope.row[child.isColor] }"> {{ scope.row[child.prop] }}</div>
                  </template>
                </el-table-column>
                <el-table-column
                  v-else
                  :key="'table-multi-child' + cIndex"
                  :prop="child.prop"
                  :label="child.label"
                  :min-width="child.width"
                  :align="child.align"
                  :fixed="child.fixed"
                  :sortable="child.sortable"
                  :show-overflow-tooltip="isOverflowHidden"
                >
                  <template #default="scope">
                    <div :style="{ color: child.color || scope.row[child.isColor] }">{{ scope.row[child.prop] }}</div>
                  </template>
                </el-table-column>
              </template>
            </el-table-column>
            <!-- 文本内容-自定义排序 -->
            <el-table-column
              v-else-if="item.type === 'customSort'"
              :key="'table-custom-sort' + index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
              :sort-method="(a, b) => handleSort(a, b, item.sortType, item.prop)"
              :show-overflow-tooltip="isOverflowHidden"
            >
              <template #default="scope">
                <div :style="{ color: item.color || scope.row[item.isColor] }">{{ scope.row[item.prop] }}</div>
              </template>
            </el-table-column>
            <!-- 文本内容 -->
            <el-table-column
              v-else
              :key="'table-' + index"
              :prop="item.prop"
              :label="item.label"
              :min-width="item.width"
              :align="item.align"
              :fixed="item.fixed"
              :sortable="item.sortable"
              :show-overflow-tooltip="isOverflowHidden"
            >
              <template #default="scope">
                <div :style="{ color: item.color || scope.row[item.isColor] }">{{ scope.row[item.prop] }}</div>
              </template>
            </el-table-column>
          </template>
        </template>
        <!-- 操作 · 插槽 -->
        <slot name="table-oper" />
      </el-table>
    </div>
    <!-- 分页 -->
    <div v-if="props.listQuery" class="table-foot" :class="{ 'border-hide': props.listQuery.borderHide }">
      <Pagination
        :page="props.listQuery.page"
        :size="props.listQuery.size"
        :layout="props.listQuery.layout"
        :background="props.listQuery.background"
        :total="props.listQuery.total"
        :page-sizes="props.listQuery.pageSizes ? props.listQuery.pageSizes : [10, 20, 30, 40, 50, 100]"
        :auto-scroll="false"
        @pagination="getList"
      />
    </div>
    <!-- PDF预览 -->
    <!-- <PreviewPdf v-model:url="pdfUrl" /> -->
  </div>
</template>

<script setup name="ZSTable">
  import Sortable from 'sortablejs';
  import Draggable from 'vuedraggable';
  import Pagination from './pagination';
  import { VuePDF, usePDF } from '@tato30/vue-pdf';
  // import PreviewPdf from './PreviewPdf.vue'

  const props = defineProps({
    // 组件内尺寸
    size: {
      type: String,
      default: ''
    },
    // 行数据的 Key，用来优化 Table 的渲染(渲染树形数据时，必须要指定 row-key)
    rowKey: {
      type: String
    },
    // 渲染嵌套数据的配置选项
    treeProps: {
      type: Object,
      default: () => ({ children: 'child', hasChildren: 'hasChildren' })
    },
    // 是否默认展开所有行
    defaultExpandAll: {
      type: Boolean,
      default: false
    },
    // 树形列表是否只展开一行
    expendonlyOne: {
      type: Boolean,
      default: false
    },
    // 表格配置，表格头，表格列
    columns: {
      type: Array,
      default: () => []
    },
    // 默认的排序
    defaultSort: {
      type: Object,
      default: () => {}
    },
    // 表格数据
    list: {
      type: Array,
      default: () => []
    },
    // 表格分页
    listQuery: {
      type: Object,
      default: () => {}
    },
    // 是否加载序号
    isIndex: {
      type: Boolean,
      default: true
    },
    // 索引列是否固定
    indexFixed: {
      type: String,
      default: ''
    },
    // 是否加载多选功能
    isSelection: {
      type: Boolean,
      default: false
    },
    // 是否加载头部按钮部分
    isHead: {
      type: Boolean,
      default: true
    },
    // 是否加载底部分页部分
    isFoot: {
      type: Boolean,
      default: true
    },
    isFootBorder: {
      type: Boolean,
      default: true
    },
    // 高度 el-table的高度
    height: {
      type: [Number, String],
      default: '100%'
    },
    // 加载动画
    loading: {
      type: Boolean,
      default: false
    },
    isOverflowHidden: {
      type: Boolean,
      default: false
    },
    // 树形是否默认全部展开
    isExpandAll: {
      type: Boolean,
      default: false
    },
    // 是否加载自定义列表按钮
    isCustomBtn: {
      type: Boolean,
      default: true
    },
    // 自定义文字选项列表
    customTextList: {
      type: Object,
      default: () => {}
    },
    // 是否支持行拖拽
    isRowDrag: {
      type: Boolean,
      default: false
    },
    // 是否支持列拖拽
    isColDrag: {
      type: Boolean,
      default: false
    },
    // 是否展示合计行
    isShowSum: {
      type: Boolean,
      default: false
    },
    highlightCurrentRow: {
      type: Boolean,
      default: false
    },
    rowClassName: {
      type: String,
      default: ''
    },
    // 是否自定义表头背景颜色
    isCustomHeaderBg: {
      type: Boolean,
      default: false
    },
    // 是否自定义处理排序
    isCustomSort: {
      type: Boolean,
      default: false
    },
    sumTitle: {
      type: String,
      defaule: '合计'
    }
  });

  const emit = defineEmits([
    'custom-list',
    'selection-change',
    'selecte-change',
    'sort-change',
    'expand-change',
    'pagination',
    'switch-change',
    'button-click',
    'input-change',
    'drag-sort',
    'current-change'
  ]);

  const { proxy } = getCurrentInstance();
  const tableRef = ref(null);

  // state
  const tableKey = ref(0);
  const customPanel = ref(false);
  const customList = ref([]);
  const tableStyle = { 'background-color': '#F2F4F7', color: '#333' };
  const expandRows = ref([]);
  // const pdfUrl = ref('');
  const headerColor = ref({});

  // computed
  const image = computed(() => {
    return (event, type) => {
      if (!event) {
        return null;
      }
      const arr = event.split(',');

      if (arr[0].includes('http://') || arr[0].includes('https://')) {
        return type === 'max' ? arr[0].replace('1080', '') : arr[0];
      }
      return type === 'max' ? proxy.$url.lookPhoto + arr[0] : proxy.$url.thumbnail_1080 + arr[0];
    };
  });

  // watch
  watch(
    () => props.columns,
    () => {
      tableKey.value++; // 为了保证table 每次都会重新渲染
    },
    { deep: true }
  );

  // methods
  const isArray = (arr) => {
    return Object.prototype.toString.call(arr) === '[object Array]';
  };

  const deepClone = (obj) => {
    // eslint-disable-next-line no-undefined
    if ([null, undefined, NaN, false].includes(obj)) {
      return obj;
    }
    if (typeof obj !== 'object' && typeof obj !== 'function') {
      return obj;
    }
    let o = isArray(obj) ? [] : {};

    for (const i in obj) {
      if (Object.prototype.hasOwnProperty.call(obj, i)) {
        o[i] = typeof obj[i] === 'object' ? deepClone(obj[i]) : obj[i];
      }
    }
    return o;
  };

  const initCustomList = () => {
    customList.value = deepClone(props.columns);
  };

  const confirm = () => {
    emit('custom-list', customList.value);
    customPanel.value = false;
  };

  const handleSelectionChange = (rows) => {
    emit('selection-change', rows);
  };

  const handleSelectChange = (selection, row) => {
    emit('selecte-change', { selection, row });
  };

  const handleSortChange = ({ prop, order }) => {
    emit('sort-change', { prop, order: order || '' });
  };

  const retractTreeExpend = (isClear) => {
    expandRows.value.forEach((item) => {
      tableRef.value.toggleRowExpansion(item, false);
    });
    if (!isClear) {
      return;
    }
    nextTick(() => {
      expandRows.value = [];
    });
  };

  const expandChange = (row, isExpand) => {
    if (isExpand) {
      emit('expand-change', row);
      if(props.expendonlyOne) {
        retractTreeExpend();
      }
      nextTick(() => {
        expandRows.value.push(row);
      });
    } else {
      expandRows.value = expandRows.value.filter((item) => item[props.rowKey] !== row[props.rowKey]);
    }
  };

  const getList = (val) => {
    if (val.page > Math.ceil(props.listQuery.total / val.limit)) {
      return;
    }
    emit('pagination', val);
  };

  const switchChange = (event, item, scope) => {
    emit('switch-change', { prop: item.prop, value: event, scope });
  };

  const buttonClick = (item, row) => {
    emit('button-click', { prop: item.prop, row });
  };

  const refreshTable = () => {
    tableRef.value.doLayout();
  };

  // eslint-disable-next-line no-undefined
  const toggleSelection = (rows, selected = undefined) => {
    if (rows) {
      const arr = props.list.filter((item) => rows.includes(item.carId));

      arr.forEach((row) => {
        tableRef.value.toggleRowSelection(row, selected);
      });
    } else {
      tableRef.value.clearSelection();
    }
  };

  // eslint-disable-next-line no-undefined
  const toggleSelectionPro = (rows, keyName = 'carId', selected = undefined) => {
    if (rows) {
      const arr = props.list.filter((item) => rows.includes(item[keyName]));

      arr.forEach((row) => {
        tableRef.value.toggleRowSelection(row, selected);
      });
    } else {
      tableRef.value.clearSelection();
    }
  };

  const handleCustomText = ({ row }, { prop, nameKey = 'name', valueKey = 'value' }) => {
    const options = props.customTextList[prop];

    if (!options || !Array.isArray(options)) {return ''}
    const parObj = options.find((item) => item[valueKey] === row[prop]);

    return parObj ? parObj[nameKey] || '' : '';
  };

  const handelRelationshipText = ({ row }, { prop, relationship }) => {
    return ((val, relationshipVal) => relationship.expected[relationshipVal] || val)(
      row[prop],
      row[relationship.prop]
    );
  };

  const rowDrag = () => {
    nextTick(() => {
      const table = tableRef.value.$el.querySelectorAll(
        '.el-table__body-wrapper > table > tbody'
      )[0];

      Sortable.create(table, {
        onEnd: ({ newIndex, oldIndex }) => emit('drag-sort', { newIndex, oldIndex })
      });
    });
  };

  const handleCurrentChange = (val) => {
    if(props.highlightCurrentRow) {
      emit('current-change', val);
    }
  };

  const setCurrent = (row) => {
    tableRef.value.setCurrentRow(row);
  };

  const setCurrentKey = (key, id) => {
    if (!id || props.list.length === 0) {
      return;
    }
    for (const item of props.list) {
      if (item[key] === id) {
        setCurrent(item);
        break;
      }
    }
  };

  const handelCustomColor = ({ row }, { target, rule }) => {
    const valTarget = ((val) =>
      typeof val === 'number' ? val : Number((val || '').replace(/[^0-9.]/g, '')))(row[target]);
    let returnColor = '';

    if (valTarget && rule) {
      for (const item of rule) {
        if (valTarget > item.min && valTarget <= item.max) {
          returnColor = item.color;
          break;
        }
      }
    }
    return returnColor;
  };

  const handelSuffix = ({ row }, { prop, suffix }) => {
    const val = String(row[prop] || '');

    return val ? `${val.replace(/[^0-9.]/g, '')}${suffix}` : '--';
  };

  // 处理PDF路径
  const formatPdfSrc = (url) => {
    usePDF(url);
  };

  const customTableStyle = ({ column }) => {
    return { 'background-color': headerColor.value[column.property] || '#F2F4F7', color: '#333' };
  };

  const handelCustomHeaderColor = () => {
    const newHeaderColor = {};

    for (const item of props.columns) {
      const headerBg = item.headerBg;

      if (!headerBg) {continue}
      newHeaderColor[item.prop] = headerBg;
      if (item.children && item.children.length > 0) {
        item.children.forEach((child) => {
          newHeaderColor[child.prop] = child.headerBg || headerBg;
        });
      }
    }
    headerColor.value = newHeaderColor;
  };

  const getSummaries = ({ columns, data }) => {
    return columns.reduce((sums, column, index) => {
      if (index === 0) {
        sums[index] = props.sumTitle || '合计';
      } else {
        const prop = column.property;

        sums[index] = prop ?
          data.reduce((pre, cur) => pre + (Number(cur[prop]) || 0), 0) || '--' :
          'N/A';
      }
      return sums;
    }, []);
  };

  const handleSort = (c, d, sortType, key) => {
    let a = c[key];
    let b = d[key];

    if (props.isCustomSort && sortType === 'numeric') {
      const numA = Number((a || '').replace(/\D/g, ''));
      const numB = Number((b || '').replace(/\D/g, ''));
      const validA = !isNaN(numA);
      const validB = !isNaN(numB);

      if (!validA && !validB) {return 0}
      if (!validA) {return 1}
      if (!validB) {return -1}
      return numA - numB;
    }
    return String(a || '').localeCompare(String(b || ''));
  };

  onMounted(() => {
    if (props.isRowDrag || props.isColDrag) {
      document.body.ondrop = (event) => {
        event.preventDefault();
        event.stopPropagation();
      };
      if(props.isRowDrag ) {
        rowDrag();
      }
    }
    if (props.isCustomHeaderBg) {
      handelCustomHeaderColor();
    }
  });

  defineExpose({
    refreshTable,
    toggleSelection,
    toggleSelectionPro,
    setCurrent,
    setCurrentKey,
    retractTreeExpend
  });
</script>

<style lang="scss" scoped>
  .main-table {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;

    .table-head {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 4px 16px 0;
      overflow: hidden;

      .head-right {
        display: flex;
        align-items: center;
        flex-wrap: nowrap;
        flex-shrink: 0;
      }

      .el-checkbox__label {
        color: #303133 !important;
      }
    }

    .table-body {
      height: 100%;
      flex: 1;
      padding: 4px 16px 16px;
      overflow: hidden;

      .el-image {
        width: 60px;
        height: 30px;
      }

      :deep(.borderNone) {
        .el-input__wrapper {
          box-shadow: none;
        }

        input::-webkit-outer-spin-button,
        input::-webkit-inner-spin-button {
          -webkit-appearance: none !important;
        }
        input[type='number'] {
          -moz-appearance: textfield !important;
          appearance: none;
        }
      }
      .more-text {
        white-space: nowrap; //给文本设置不换行在一行中显示
        overflow: hidden; //文本超出的部分隐藏
        text-overflow: ellipsis; //文本超出的部分用省略号代替
      }
    }

    .table-foot {
      box-sizing: border-box;
      width: 100%;
      padding: 16px;
      overflow: auto;
      box-shadow: inset 0px 1px 0px #e5e6e8;

      &.border-hide {
        padding-top: 0;
        box-shadow: none;
      }
    }
  }
</style>
