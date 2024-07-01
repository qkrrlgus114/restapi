<template>
  <div class="admin-panel">
    <nav class="admin-nav">
      <router-link class="nav-tab" to="/admin/log/1">요청기록확인</router-link>
      <router-link class="nav-tab" to="/admin/ban">유저추방</router-link>
      <router-link class="nav-tab" to="/admin/settings">설정</router-link>
    </nav>
    <router-view></router-view>
  </div>
</template>

<style scoped>
.admin-panel {
  font-family: "Segoe UI", Arial, sans-serif;
}

.admin-nav {
  display: flex;
  justify-content: center;
  background: #f7f7f7;
  padding: 10px;
  border-bottom: 2px solid #e0e0e0;
}

.nav-tab {
  text-decoration: none;
  color: #333;
  padding: 10px 20px;
  margin-right: 8px;
  border-radius: 4px;
  transition: background-color 0.2s, color 0.2s;
}

.nav-tab:hover,
.nav-tab.router-link-active {
  background-color: #e0e0e0;
  color: #000;
}

.router-link-active {
  font-weight: bold;
  color: #3498db;
}
</style>
