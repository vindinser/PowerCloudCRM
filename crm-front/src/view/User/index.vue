<!-- 用户管理 -->
<template>
  <div>
    <el-button type="primary">新增用户</el-button>
    <el-button type="danger">批量删除</el-button>

    <el-table
      ref="multipleTableRef"
      :data="tableData"
      row-key="id"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column type="index" width="50" />
      <el-table-column property="loginAct" label="账号" min-width="120" />
      <el-table-column property="name" label="姓名" min-width="120" />
      <el-table-column property="phone" label="手机" min-width="120" />
      <el-table-column property="email" label="邮箱" min-width="150" />
      <el-table-column property="createTime" label="创建时间" min-width="200" />
      <el-table-column label="操作" min-width="150">
        <template #default="scope">
          操作
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="tableDataTotal"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup name="User">
import { getUsers } from '@/api/user.js';

const multipleSelection = ref([]);
const handleSelectionChange = (val) => {
  multipleSelection.value = val;
};

const pageSize = ref(10);
const currentPage = ref(1);
const tableData = ref([]);
const tableDataTotal = ref(0);
const getUserList = () => {
  getUsers({
    current: currentPage.value,
    size: pageSize.value
  }).then((res) => {
    if(res.code === 200) {
      tableData.value = res.data.list;
      tableDataTotal.value = res.data.total;
    } else {
      tableData.value = [];
      tableDataTotal.value = 0;
    }
  }).catch(() => {
    tableData.value = [];
    tableDataTotal.value = 0;
  });
};

const handleCurrentChange = (val) => {
  currentPage.value = val;
  getUserList();
};

const handleSizeChange = (val) => {
  pageSize.value = val;
  getUserList();
};

onMounted(() => {
  getUserList();
});
// watch(propData, (newVal, oldVal) => {})
</script>

<style lang="scss" scoped>

</style>