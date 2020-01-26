import Vue from "vue";
import Router from "vue-router";
import store from "../store/store";
import { comRouter, comRouterGuard } from "./com";
import { adminRouter, adminRouterGuard } from "./admin";

Vue.use(Router);

let router = new Router({
  routes: comRouter.concat(adminRouter)
});

// 路由设定
router.beforeEach((to, from, next) => {
  store.commit("setPath", to.path);

  // 进行路由判断，让"通过复制url进入指定页面"的用户看到正确的tab状态
  comRouterGuard(to, next);
  adminRouterGuard(to, next);

  next();
});

export default router;
