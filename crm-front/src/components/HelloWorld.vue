<template>
  <h1>{{ msg }}</h1>

  <div class="card">
    <el-button type="primary" @click="count++">count is {{ count }}</el-button>
    <el-button type="primary" @click="testAxios">test</el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios';

defineProps({
  msg: String,
})

const count = ref(0)

const testAxios = async () => {
  const data = await axios.post('http://127.0.0.1:8080/api/login', {
    loginAct: 'zs',
    loginPwd: '123456'
  }).then(({res}) => {
    console.log("🚀 ~ testAxios ~ res:", res);
  }).catch((err) => {
    console.log("🚀 ~ testAxios ~ err:", err);
  });
}
</script>

<style scoped lang="scss">
.read-the-docs {
  color: #888;
}
</style>
