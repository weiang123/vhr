import router from './router'
import store from './store'
import {initMenu} from "./utils/menus";

//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        //设置标题头
        document.title = `${to.meta.title}`;
    }
    if (to.path.indexOf('/vhr') > -1) {
        //若进入vhr项目,则需要登录,动态获取菜单
        if (window.sessionStorage.getItem("user")) {
            initMenu(router, store);
            next();
        } else {
            next('/login?redirect=' + to.path);
        }
    }else {
          // 简单的判断IE10及以下不进入富文本编辑器，该组件不兼容
          if (navigator.userAgent.indexOf('MSIE') > -1 && to.path === '/editor') {
            Vue.prototype.$alert('vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看', '浏览器不兼容通知', {
                confirmButtonText: '确定'
            });
        } else {
            next();
        }
    }
})