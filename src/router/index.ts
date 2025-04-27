import { createRouter, createWebHistory } from 'vue-router';
import type { RouteLocationNormalized, NavigationGuardNext } from 'vue-router';

// 导入视图组件
import Dashboard from '../views/Dashboard.vue';
import UserManagement from '../views/UserManagement.vue';
import RoleManagement from '../views/RoleManagement.vue';
import SystemSettings from '../views/SystemSettings.vue';
import ProductManagement from '../views/ProductManagement.vue';
import OrderManagement from '../views/OrderManagement.vue';
import ProductQualityManagement from '../views/ProductQualityManagement.vue';
import MerchantCreditSystem from '../views/MerchantCreditSystem.vue';
import DisputeManagement from '../views/DisputeManagement.vue';

const routes = [
  {
    path: '/',
    name: 'Dashboard',
    component: Dashboard,
    meta: { title: '仪表盘' }
  },
  {
    path: '/user-management',
    name: 'UserManagement',
    component: UserManagement,
    meta: { title: '用户管理' }
  },
  {
    path: '/role-management',
    name: 'RoleManagement',
    component: RoleManagement,
    meta: { title: '角色管理' }
  },
  {
    path: '/product-management',
    name: 'ProductManagement',
    component: ProductManagement,
    meta: { title: '商品管理' }
  },
  {
    path: '/order-management',
    name: 'OrderManagement',
    component: OrderManagement,
    meta: { title: '订单管理' }
  },
  {
    path: '/product-quality-management',
    name: 'ProductQualityManagement',
    component: ProductQualityManagement,
    meta: { title: '商品质量管理' }
  },
  {
    path: '/merchant-credit-system',
    name: 'MerchantCreditSystem',
    component: MerchantCreditSystem,
    meta: { title: '商家信用评级' }
  },
  {
    path: '/dispute-management',
    name: 'DisputeManagement',
    component: DisputeManagement,
    meta: { title: '争议与纠纷' }
  },
  {
    path: '/system-settings',
    name: 'SystemSettings',
    component: SystemSettings,
    meta: { title: '系统设置' }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由守卫，设置页面标题
router.beforeEach((to: RouteLocationNormalized, _from: RouteLocationNormalized, next: NavigationGuardNext) => {
  document.title = `${to.meta.title ? to.meta.title + ' - ' : ''}数据清洗平台`;
  next();
});

export default router;
