<!-- 用户详情 -->
<template>
  <ZSDetail isFoot width="75%" title="用户详情" @closed="emit('closed')">
    <el-descriptions title="用户基本信息" border style="margin-top: 20px" :column="2">
      <el-descriptions-item label="ID">
        {{ userInfo.id || '--' }}
      </el-descriptions-item>
      <el-descriptions-item :rowspan="3" :width="140" label="头像" label-width="0" align="center">
        <el-avatar style="width: 100px; height: 100px" shape="square">{{ userInfo.name || 'ZS' }}</el-avatar>
        <!-- <el-image style="width: 100px; height: 100px" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" /> -->
      </el-descriptions-item>
      <el-descriptions-item v-for="item, index in descriptions" :key="index" label-width="120" :label="item.label" :span="2">
        <span v-if="item.isCode">{{ userInfo[item.value] ? '是' : '否' }}</span>
        <span v-else-if="['create', 'edit'].includes(item.value)">
          {{ userInfo[`${ item.value }Info`] ? (userInfo[`${ item.value }Info`].name || '--') : '--' }}
        </span>
        <span v-else>{{ userInfo[item.value] || '--' }}</span>
      </el-descriptions-item>
    </el-descriptions>
    <template #foot>
      <el-button @click="emit('closed')">关闭</el-button>
    </template>
  </ZSDetail>
</template>

<script setup name="UserDetail">
  import ZSDetail from '@/components/ZSDetail';
  import { getUser } from '@/api/user.js';

  const emit = defineEmits(['closed']);
  const { row } = defineProps({
    row: {
      type: Object,
      default: () => ({})
    }
  });

  const descriptions = [
    // { label: 'ID', value: 'id' },
    { label: '账号', value: 'loginAct' },
    { label: '姓名', value: 'name' },
    { label: '密码', value: 'loginPwd' },
    { label: '手机号', value: 'phone' },
    { label: '邮箱', value: 'email' },
    { label: '账号未过期', value: 'accountNoExpired', isCode: true },
    { label: '密码未过期', value: 'credentialsNoExpired', isCode: true },
    { label: '账号未锁定', value: 'accountNoLocked', isCode: true },
    { label: '账号是否启用', value: 'accountEnabled', isCode: true },
    { label: '创建时间', value: 'createTime' },
    { label: '创建人', value: 'create' },
    { label: '修改时间', value: 'editTime' },
    { label: '修改人', value: 'edit' },
    { label: '最近登录时间', value: 'lastLoginTime' }
  ];

  const userInfo = ref({});
  const getUserDetail = async () => {
    const res = await getUser(row.id);

    if(res.code === 200) {
      userInfo.value = res.data;
    }
  };

  onMounted(() => {
    getUserDetail();
  });
  // watch(propData, (newVal, oldVal) => {})

  // defineExpose({}) // 将方法、数据暴露给父组件
</script>

<style lang="scss" scoped>

</style>