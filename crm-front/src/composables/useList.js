import apis from '@/api';
import ZsList from '@/components/ZSList';
import ZsTable from '@/components/ZSTable';

/**
 * 列表通用函数
 * @param {function} getTableDataParam 获取列表参数方法
 * @param {function} postRedOperaFn 删除、审核等方法
 * @param {array} column 列表列
 * @param {ref} formSearchRef 表单搜索ref
 * @param {boolean} isHasTree 是否在 需要从第一页获取数据
 * @param {boolean} isMountedLoad 是否在 mounted 生命周期加载列表数据
 * @returns
 */
export const useList = ({
  getTableDataParam = () => {},
  postRedOperaFn = () => {},
  column = [],
  formSearchRef,
  isHasTree = false,
  isMountedLoad = true
}) => {
  const { proxy } = getCurrentInstance();

  // 列表数据
  const tableData = reactive({
    list: [], // 列表数据
    listQuery: {
      page: 1,
      size: 20,
      total: 0
    }, // 分页信息
    loading: false, // 加载状态
    defaultSort: {
      prop: '',
      order: ''
    }, // 默认排序
    column: [] // table列
  });

  tableData.column = column;

  /**
   * 返回接口方法
   * @param {array} apiChain 获取接口的链式选择
   * @returns {function} 接口地址
   */
  const getApiFn = (apiChain) => {
    return apiChain.reduce((pre, cur) => pre[cur], apis);
  };

  // 列表接口返回的除列表以外的其他数据
  const dataMap = ref({});

  /**
   * 获取列表处理函数
   * @param {number} total 列表数据总数
   * @param {array} ListData 列表数据
   * @param {object} dataMap 列表其他数据
   */
  const handelListData = ({ total = 0, ListData = [], dataMap: _dataMap = {} }) => {
    dataMap.value = _dataMap;
    tableData.list = ListData;
    tableData.listQuery.total = Number(total);
  };

  // 获取列表数据
  const getTableData = () => {
    const { param, apiChain } = getTableDataParam();
    const fn = getApiFn(apiChain);

    fn({
      ...param,
      page: tableData.listQuery.page,
      size: tableData.listQuery.size,
      sortField: tableData.defaultSort.prop,
      sortOrder: tableData.defaultSort.order
    }).then(data =>
      handelListData(((success) => ({
        ...success && {
          total: data.data.total,
          ListData: data.data.list,
          dataMap: data.data
        }
      }))(data.code === 200 || data.success))
    ).catch(() =>
      handelListData({})
    );
  };

  // 搜索按钮点击事件
  const onSearch = () => {
    tableData.listQuery.page = 1;
    getTableData();
  };

  /**
   * 表格排序改变
   * @param {string} prop 排序列
   * @param {string} order 正序/倒叙
   */
  const tableSortChange = ({ prop, order }) => {
    tableData.defaultSort.prop = prop;
    tableData.defaultSort.order = order;
    onSearch();
  };

  /**
   * 自定义列表
   * @param {array} column table列顺序
   */
  const tableChange = (columns) => {
    tableData.column = columns;
  };

  /**
   * 分页改变
   * @param {number} page 当前页
   * @param {number} size 当前页获取多少数据
   */
  const pagination = ({ page, size }) => {
    if(tableData.listQuery.size !== size) {
      tableData.listQuery.page = 1;
    } else {
      tableData.listQuery.page = page;
    }
    tableData.listQuery.size = size;
    getTableData();
  };

  // 批量操作数据
  const batchList = ref([]);

  /**
   * 列表选择列改变
   * @param {array} selection 批量选择的数据
   */
  const selecteChange = (rows) => {
    batchList.value = rows;
  };

  // 清除搜索条件按钮点击事件
  const onReset = () => {
    formSearchRef.value.resetFields();
    onSearch();
  };

  /**
   * 删除/关闭等需要二次确认但无需打开详情的操作
   * @param {object} row 当前操作的列
   * @param {boolean} isBatch 是否为批量操作
   * @param {string} type 操作类型
   * @param {boolean} isRefresh 操作后是否刷新
   */
  const redOperaFn = async (row, isBatch, type, isRefresh) => {
    const data = await postRedOperaFn(row, isBatch, type, batchList.value);
    const success = data.code === 200 || data.success;

    if (isRefresh && success) {
      if (isHasTree.value) {
        onSearch();
      } else {
        getTableData();
      }
    }
  };

  // 当前点击的列表项
  const currentRow = shallowRef(null);
  // 展示的详情/表单组件
  const currentComponent = shallowRef('');

  /**
   * 打开详情/列表操作（删除、审核...）
   * @param {string} type 操作类型（首字母大写为打开组件否则为操作）
   * @param {object} row 当前操作的列
   * @param {string} operaName 操作类型
   * @param {boolean} isRefresh 操作后是否刷新
   */
  const openDetail = (type = '', { row = {} }, operaName = '操作', isRefresh = true) => {
    console.log('🚀 ~ openDetail ~ { row }, type:', row, type, operaName, isRefresh);
    if (/^[A-Z]/.test(type)) {
      currentRow.value = row;
      currentComponent.value = type;
    } else {
      const isBatch = type === 'batch';

      if (isBatch && batchList.value.length === 0) {
        return proxy.$modal.msgWarning('请选择批量操作数据！');
      }
      proxy.$modal.confirm(`确定${ operaName }吗?`).then(() => {
        redOperaFn(row, isBatch, type, isRefresh);
      });
    }
  };

  /**
   * 详情、新增、修改用户 页面关闭
   * @param {boolean} isRefresh 是否刷新列表
   */
  const detailClose = (isRefresh) => {
    currentComponent.value= '';
    if(isRefresh) {
      onSearch();
    }
  };

  // 生命周期
  onMounted(() => {
    if (isMountedLoad.value) {
      getTableData();
    }
  });

  // 暴露给模板
  return {
    List: markRaw(ZsList),
    Table: markRaw(ZsTable),
    dataMap,
    tableData,
    currentRow,
    currentComponent,
    batchList,
    isMountedLoad,
    tableSortChange,
    tableChange,
    pagination,
    selecteChange,
    onSearch,
    onReset,
    getTableData,
    openDetail,
    detailClose,
    shortcuts: [
      {
        text: '最近一周',
        value: () => {
          const end = new Date();
          const start = new Date();

          start.setDate(start.getDate() - 7);
          return [start, end];
        }
      },
      {
        text: '最近一个月',
        value: () => {
          const end = new Date();
          const start = new Date();

          start.setMonth(start.getMonth() - 1);
          return [start, end];
        }
      },
      {
        text: '最近三个月',
        value: () => {
          const end = new Date();
          const start = new Date();

          start.setMonth(start.getMonth() - 3);
          return [start, end];
        }
      }
    ] // 时间快捷选项
  };
};