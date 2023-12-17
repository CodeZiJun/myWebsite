import {createRouter, createWebHistory} from "vue-router";
import {unauthorized} from "@/net";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/home',
            name: 'home',
            component: () => import('@/views/Home.vue'),

        },
        {
            path: '/',
            name: 'welcome',
            component: () => import('@/views/WelcomeView.vue'),
            children: [
                {
                    path: '',
                    name: 'welcome-login',
                    component: () => import('@/views/welcome/LoginPage.vue')
                }, {
                    path: '/register',
                    name: 'welcome-register',
                    component: () => import('@/views/welcome/ResgisterPage.vue')
                }, {
                    path: '/reset',
                    name: 'welcome-reset',
                    component: () => import('@/views/welcome/ResetPage.vue')
                }
            ]
        }, {
            path: '/index',
            name: 'index',
            meta: {
                description: '职工管理系统'
            },
            component: () => import('@/views/IndexView.vue'),
            children: [
                {
                    path: 'service',
                    name: 'index-service',
                    meta: {
                        description: '办事大厅',
                        parentRouteName: 'index'
                    },
                    component: () => import('@/views/index/service/ServicePage.vue'),
                    children: [
                        {
                            path: 'proof',
                            name: 'index-service-proof',
                            meta: {
                                description: '教职工证明',
                                parentRouteName: 'index-service'
                            },
                            component: () => import('@/views/index/service/informationPrint/ProofInformationDownload.vue')
                        }, {
                            path: 'letter',
                            name: 'index-service-letter',
                            meta: {
                                description: '电子调档函',
                                parentRouteName: 'index-service'
                            },
                            component: () => import('@/views/index/service/informationPrint/TransferLetterDownload.vue')
                        }, {
                            path: 'advanced',
                            name: 'index-service-advanced',
                            meta: {
                                description: '高基统计报表',
                                parentRouteName: 'index-service'
                            },
                            component: () => import('@/views/index/service/informationPrint/AdvancedStatistics.vue')
                        }, {
                            path: 'talent',
                            name: 'index-service-talent',
                            meta: {
                                description: '人才统计报表',
                                parentRouteName: 'index-service'
                            },
                            component: () => import('@/views/index/service/informationPrint/TalentResourceStatistics.vue')
                        }
                    ]
                }, {
                    path: 'information',
                    name: 'index-information',
                    meta: {
                        description: '详细信息',
                        parentRouteName: 'index'
                    },
                    component: () => import('@/views/index/information/InformationPage.vue'),
                    children: [
                        {
                            path: 'staffResource',
                            name: 'index-information-staffResource',
                            meta: {
                                description: '职工资源目录',
                                parentRouteName: 'index-information'
                            },
                            component: () => import('@/views/index/information/resourceInformation/staffResource.vue')
                        },{
                            path: 'chartsView',
                            name: 'index-information-chartsView',
                            meta: {
                                description: '图表可视化',
                                parentRouteName: 'index-information'
                            },
                            component: () => import('@/views/index/information/resourceInformation/chartsView.vue')
                        },{
                            path: 'self',
                            name: 'index-information-self',
                            meta: {
                                description: '个人信息',
                                parentRouteName: 'index-information'
                            },
                            component: () => import('@/views/index/information/selfInformation/myInformation.vue')
                        }
                    ]
                },{
                    path: 'salary',
                    name: 'index-salary',
                    meta: {
                        description: '薪资查询',
                        parentRouteName: 'index'
                    },
                    component: () => import('@/views/index/salary/SalaryPage.vue'),
                    children: [
                        {
                            path: 'mySalary',
                            name: 'index-salary-mySalary',
                            meta: {
                                description: '我的薪资',
                                parentRouteName: 'index-salary'
                            },
                            component: () => import('@/views/index/salary/mySalary/mySalaryDetails.vue')
                        }
                    ]
                },{
                    path: '/admin',
                    name: 'index-admin',
                    meta: {
                        description: '管理页面',
                        parentRouteName: 'index'
                    },
                    component: () => import('@/views/index/admin/adminPage.vue'),
                    children: [
                        {
                            path: 'staff',
                            name: 'index-admin-staff',
                            meta: {
                                description: '账户管理',
                                parentRouteName: 'index-admin'
                            },
                            component: () => import('@/views/index/admin/children/staffManager.vue')
                        }, {
                            path: 'department',
                            name: 'index-admin-department',
                            meta: {
                                description: '部门管理',
                                parentRouteName: 'index-admin'
                            },
                            component: () => import('@/views/index/admin/children/departmentManager.vue')
                        }, {
                            path: 'position',
                            name: 'index-admin-position',
                            meta: {
                                description: '职位管理',
                                parentRouteName: 'index-admin'
                            },
                            component: () => import('@/views/index/admin/children/positionManager.vue')
                        }, {
                            path: 'commission',
                            name: 'index-admin-commission',
                            meta: {
                                description: '任命管理',
                                parentRouteName: 'index-admin'
                            },
                            component: () => import('@/views/index/admin/children/commissionPage.vue')
                        }
                    ]
                }, {
                    path: 'profile',
                    name: 'index-profile',
                    meta: {
                        description: '我的资料',
                        parentRouteName: 'index'
                    },
                    component: () => import('@/views/index/userData/Profile.vue'),
                    children: [
                        {
                            path: '',
                            name: 'index-profile-myInfo',
                            meta: {
                                description: '我的信息',
                                parentRouteName: 'index-profile'
                            },
                            component: () => import('@/views/index/userData/profileCard/myInfo.vue')
                        }, {
                            path: 'avatar',
                            name: 'index-profile-updateAvatar',
                            meta: {
                                description: '设置头像',
                                parentRouteName: 'index-profile'
                            },
                            component: () => import('@/views/index/userData/profileCard/updateAvatar.vue')
                        }, {
                            path: 'resetPassword',
                            name: 'index-profile-resetPassword',
                            meta: {
                                description: '修改密码',
                                parentRouteName: 'index-profile'
                            },
                            component: () => import('@/views/index/userData/profileCard/resetPassword.vue')
                        }
                    ]
                }
            ]
        }
    ]
})

router.beforeEach((to, from, next) => {
    const isUnauthorized = unauthorized()
    if(to.name.startsWith('welcome-') && !isUnauthorized) {
        next('/index')
    } else if (to.fullPath.startsWith('/index') && isUnauthorized){
        next('/')
    } else if (to.fullPath.startsWith('/admin') && isUnauthorized) {
        next('/')
    } else {
        next()
    }
})
export default router