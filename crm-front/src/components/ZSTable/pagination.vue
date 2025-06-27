<template>
  <div :class="{ hidden: hidden }" class="pagination-container">
    <el-pagination
      v-model:current-page="currentPage"
      v-model:page-size="pageSize"
      :background="background"
      :layout="layout"
      :page-sizes="pageSizes"
      :total="total"
      v-bind="$attrs"
    />
    <!-- @size-change="handleSizeChange"
      @current-change="handleCurrentChange" -->
  </div>
</template>

<script setup name="ZSPagination">
  const props = defineProps({
    total: {
      required: true,
      type: Number
    },
    page: {
      type: Number,
      default: 1
    },
    size: {
      type: Number,
      default: 10
    },
    pageSizes: {
      type: Array,
      default: () => [10, 20, 30, 50, 100]
    },
    layout: {
      type: String,
      default: 'total, sizes, prev, pager, next, jumper'
    },
    background: {
      type: Boolean,
      default: true
    },
    autoScroll: {
      type: Boolean,
      default: true
    },
    hidden: {
      type: Boolean,
      default: false
    }
  });

  const emit = defineEmits(['pagination']);

  const handleCurrentChange = (page) => {
    emit('pagination', { page, size: props.size });
  };

  const handleSizeChange = (size) => {
    emit('pagination', { page: props.page, size });
  };

  const currentPage = computed({
    get() {
      return props.page;
    },
    set(val) {
      handleCurrentChange(val);
    }
  });

  const pageSize = computed({
    get() {
      return props.size;
    },
    set(val) {
      handleSizeChange(val);
    }
  });

</script>

<style scoped>
  .pagination-container.hidden {
    display: none;
  }

  .el-pagination {
    margin: 0;
    padding: 0;
  }
</style>
