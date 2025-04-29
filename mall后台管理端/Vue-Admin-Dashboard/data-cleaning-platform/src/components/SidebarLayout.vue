<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="logo">
        <div class="logo-icon">DC</div>
        <span class="logo-text">数据清洗平台</span>
      </div>

      <div class="menu-category">主要功能</div>

      <ul class="menu-list">
        <router-link to="/" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">📊</div>
            <span class="menu-text">仪表盘</span>
          </li>
        </router-link>
        <li class="menu-item">
          <div class="menu-icon">📤</div>
          <span class="menu-text">数据导入</span>
        </li>
        <li class="menu-item">
          <div class="menu-icon">📈</div>
          <span class="menu-text">质量监控</span>
        </li>
        <router-link to="/product-quality-management" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">🛡️</div>
            <span class="menu-text">商品质量管理</span>
          </li>
        </router-link>
        <router-link to="/code-learning" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">📚</div>
            <span class="menu-text">代码学习助手</span>
          </li>
        </router-link>
      </ul>

      <div class="menu-category">系统管理</div>

      <ul class="menu-list">
        <router-link to="/user-management" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">👤</div>
            <span class="menu-text">用户管理</span>
          </li>
        </router-link>
        <router-link to="/role-management" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">🔒</div>
            <span class="menu-text">角色管理</span>
          </li>
        </router-link>
        <router-link to="/product-management" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">🛒</div>
            <span class="menu-text">商品管理</span>
          </li>
        </router-link>
        <router-link to="/order-management" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">📄</div>
            <span class="menu-text">订单管理</span>
          </li>
        </router-link>
        <router-link to="/merchant-credit-system" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">⭐</div>
            <span class="menu-text">商家信用评级</span>
          </li>
        </router-link>
        <router-link to="/dispute-management" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">⚖️</div>
            <span class="menu-text">争议与纠纷</span>
          </li>
        </router-link>
        <router-link to="/system-settings" custom v-slot="{ navigate, isActive }">
          <li @click="navigate" :class="['menu-item', { active: isActive }]">
            <div class="menu-icon">⚙️</div>
            <span class="menu-text">系统设置</span>
          </li>
        </router-link>
      </ul>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <header class="header">
        <div class="header-title">
          <slot name="title" />数据清洗管理
        </div>
        <div class="user-info">
          <span class="username">xiaowu</span>
          <div class="avatar">LA</div>
        </div>
      </header>

      <div class="content">
        <slot />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
// 侧边栏组件
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  width: 100%;
}

.sidebar {
  width: 230px;
  background-color: #1e1e2d;
  color: #a2a3b7;
  display: flex;
  flex-direction: column;
  padding: 0;
}

.logo {
  padding: 20px;
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.logo-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background-color: #6d5dfc;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-right: 10px;
}

.logo-text {
  font-size: 18px;
  color: white;
  font-weight: 500;
}

.menu-category {
  padding: 10px 20px;
  font-size: 12px;
  color: #565674;
  text-transform: uppercase;
}

.menu-list {
  list-style: none;
  padding: 0;
  margin: 0 0 20px 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.menu-item:hover {
  background-color: rgba(77, 89, 149, 0.06);
}

.menu-item.active {
  background-color: #6d5dfc;
  color: white;
}

.menu-icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.menu-text {
  font-size: 14px;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f5f8fa;
  overflow: auto;
}

.header {
  height: 65px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background-color: white;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.header-title {
  font-size: 18px;
  font-weight: 500;
}

.user-info {
  display: flex;
  align-items: center;
}

.username {
  margin-right: 10px;
  font-size: 14px;
  color: #555;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #6d5dfc;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 500;
}

.content {
  padding: 20px;
  flex: 1;
}
</style>
