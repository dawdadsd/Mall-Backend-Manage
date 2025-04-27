<script setup lang="ts">
import SidebarLayout from './components/SidebarLayout.vue';
import { useRoute } from 'vue-router';

const route = useRoute();
</script>

<template>
  <SidebarLayout>
    <template #title>{{ route.meta.title || '数据清洗管理' }}</template>
    <router-view />
  </SidebarLayout>
</template>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
}

* {
  box-sizing: border-box;
}
</style>
