<!-- 系统管理（占位页） -->
<template>
  <div class="system-placeholder">
    <el-result
      icon="info"
      title="系统管理"
      sub-title="该模块正在建设中，系统配置功能敬请期待"
    >
      <template #extra>
        <el-button type="primary" @click="toDashboard">返回首页</el-button>
      </template>
    </el-result>
  </div>
</template>

<script setup name="System">
  import { useRouter } from 'vue-router';

  const router = useRouter();
  const toDashboard = () => router.push({ path: '/dashboard' });

</script>

<style lang="scss" scoped>
  .system-placeholder {
    padding: 40px 0;
  }
</style>
