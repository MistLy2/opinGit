import Layout from "../layout/Index.vue";
import RouteView from "../components/RouteView.vue";

const layoutMap = [
    {
        path: "",
        name: "Index",
        meta: { title: "火眼金睛", icon: "View" },
        component: () => import("../views/Index.vue")
    },
    {
        path: "data",
        name: "Data",
        component: RouteView,
        meta: { title: "数据管理", icon: "DataLine" },
        children: [
            {
                path: "",
                name: "DataList",
                meta: { title: "数据列表" },
                component: () => import("../views/data/List.vue")
            },
            {
                path: "table",
                name: "DataTable",
                meta: { title: "数据表格", roles: ["admin"] },
                component: () => import("../views/data/Table.vue")
            }
        ]
    },
    {
        path: "admin",
        name: "Admin",
        meta: { title: "用户管理", icon: "User", roles: ["admin"] },
        component: RouteView,
        children: [
            {
                path: "",
                name: "AdminAuth",
                meta: { title: "用户列表" },
                component: () => import("../views/admin/AuthList.vue")
            },
            {
                path: "role",
                name: "AdminRole",
                meta: { title: "角色列表" },
                component: () => import("../views/admin/RoleList.vue")
            }
        ]
    },
    {
        path: "player",
        name: "Player",
        meta: { title: "视频播放", icon: "Film" },
        component: () => import("../views/common/XGPlayer.vue")
    },
    {
        path: "charts",
        name: "Charts",
        meta: { title: "数据图表", icon: "trend-charts" },
        component: () => import("../views/data/Charts.vue")
    },
    {
        path: "editor",
        name: "Editor",
        meta: { title: "富文本编辑器", icon: "Document" },
        component: () => import("../views/common/Editor.vue")
    },
    {
        path: "user",
        name: "User",
        hidden: true /* 不在侧边导航展示 */,
        meta: { title: "个人中心" },
        component: () => import("../views/admin/User.vue")
    },
    {
        path: "/error",
        name: "NotFound",
        hidden: true,
        meta: { title: "404" },
        component: () => import("../components/NotFound.vue")
    },
    {
        path: "/:w+",
        hidden: true,
        redirect: { name: "NotFound" }
    },


    // ================================
    {
        path: "news",
        name: "News",
        component: RouteView,
        meta: { title: "舆情侦探", icon: "DataLine" },
        // children: [
        //     {
        //         path: "",
        //         name: "detective",
        //         meta: { title: "舆情界面" },
        //         component: () => import("../views/detective/Station.vue")
        //     },
        //     {
        //         path: "upload",
        //         name: "NewsUpload",
        //         hidden: true /* 不在侧边导航展示 */,
        //         meta: { title: "舆情上传"},
        //         component: () => import("../views/detective/upload/Upload.vue")
        //     }
        // ]
        component: () => import("../views/detective/Station.vue")
    },

    {
        path: "upload",
        name: "NewsUpload",
        hidden: true /* 不在侧边导航展示 */,
        meta: { title: "舆情上传"},
        component: () => import("../views/detective/upload/Upload.vue")
    },

    {
        path: "record",
        name: "Record",
        hidden: true /* 不在侧边导航展示 */,
        meta: { title: "舆情记录"},
        component: () => import("../views/detective/record/Record.vue")
    },

    {
        path: "hot",
        name: "Hot",
        component: RouteView,
        meta: { title: "舆情热点", icon: "DataLine" },
        children: [
            {
                path: "",
                name: "DataList",
                meta: { title: "数据列表" },
                component: () => import("../views/hot/Hot.vue")
            },
            {
                path: "table",
                name: "DataTable",
                meta: { title: "数据表格", roles: ["admin"] },
                component: () => import("../views/data/Table.vue")
            }
        ]
    },
    {
        path: "user",
        name: "User",
        // hidden: true /* 不在侧边导航展示 */,
        component: RouteView,
        meta: { title: "个人中心", icon: "User" },
        children: [
            {
                path: "",
                name: "account",
                meta: { title: "账号管理" },
                component: () => import("../views/admin/User.vue")
            },
            {
                path: "points",
                name: "Points",
                meta: { title: "积分兑换"},
                component: () => import("../views/account/points/Points.vue")
            },
            {
                path: "help",
                name: "Help",
                meta: { title: "帮助中心"},
                component: () => import("../views/account/help/Help.vue")
            },
            {
                path: "leaderboard",
                name: "Leaderboard",
                meta: { title: "积分排行榜" },
                component: () => import("../views/account/leaderboard/Leaderboard.vue")
            },
        ]
    },

    // {
    //     path: "news",
    //     name: "Result",
    //     hidden: true /* 不在侧边导航展示 */,
    //     meta: { title: "舆情界面" },
    //     component: () => import("../views/detective/Station.vue")
    // },
];

const routes = [
    { path: "/login", name: "Login", meta: { title: "登录" }, component: () => import("../views/login/Login.vue") },
    { path: "/", name: "Layout", component: Layout, children: [...layoutMap] }
];

export { routes, layoutMap };
