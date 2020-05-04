import Vue from 'vue'
import Router from 'vue-router'
import Login from './views/Login.vue'
import Home from './views/Home.vue'
import FriendChat from './views/chat/FriendChat.vue'
import HrInfo from './views/HrInfo.vue'
import showPage from './views/showpg/index.vue'

Vue.use(Router)

export default new Router({
    routes: [{
            path: '/',
            name: '展示页',
            component: showPage,
            meta: {
                title: "WEI | 巍昂 - 一个神奇的小伙子"
            }
        },
        {
            path: '/login',
            name: '登录',
            component: Login,
            hidden: true
        },
        {
            path: '/vhr/home',
            name: 'VHR首页',
            component: Home,
            hidden: true,
            meta: {
                roles: ['admin', 'user']
            },
            children: [{
                path: '/vhr/chat',
                name: '在线聊天',
                component: FriendChat,
                hidden: true
            }, {
                path: '/hrinfo',
                name: '个人中心',
                component: HrInfo,
                hidden: true
            }]
        },
        {
            path: '/crz/cont/:username/:txImg',
            name: '出入证',
            component: () => import('./views/crz/content.vue'),
            meta: {
                title: '云社区集中出入管理系统'
            }
        },
        {
            path: '/crz/front',
            name: '出入证',
            component: () => import('./views/crz/front.vue'),
            meta: {
                title: '云社区集中出入管理系统'
            },

        },
        {
            path: '/admin',
            redirect: '/vhr/home'
        },
        {
            path: '*',
            redirect: '/'
        }




    ]
})