<template>
  <el-config-provider :locale="zhCn">
    <router-view />
  </el-config-provider>
</template>

<script setup>
  import '@/core/copyright.js';
  import zhCn from 'element-plus/es/locale/lang/zh-cn';
</script>

<style scoped lang="scss"></style>
