var params = {
    periodTimePOList: {},
    userMangerss: {}
};

var final = {
    PeriodTimePOType: {
        1: "空置可被预约",
        2: "待审核",
        3: "已预约",
        4: "内部关闭",
        5: "已使用"
    },
    betTimeList: [
        {"key": "9:00-10:00", "value": "9:00-10:00"},
        {"key": "9:00-11:00", "value": "9:00-11:00"},
        {"key": "11:00-12:00", "value": "11:00-12:00"},
        {"key": "12:00-14:00", "value": "12:00-14:00"},
        {"key": "12:00-13:00", "value": "12:00-13:00"},
        {"key": "13:00-14:00", "value": "13:00-14:00"},
        {"key": "14:00-15:00", "value": "14:00-15:00"},
        {"key": "14:00-16:00", "value": "14:00-16:00"},
        {"key": "15:00-16:00", "value": "15:00-16:00"},
        {"key": "16:00-17:00", "value": "16:00-17:00"},
        {"key": "16:00-18:00", "value": "16:00-18:00"},
        {"key": "17:00-18:00", "value": "17:00-18:00"},
        {"key": "18:00-20:00", "value": "18:00-20:00"},
        {"key": "20:00-22:00", "value": "20:00-22:00"}
    ],
    type: [
        //1取消,2待审批,3待付款,4预约成功
        //1可预约,2待审核,3待付款,4已预约,5关闭,6正在使用
        {"key": "1", "value": "空置可被预约"},
        {"key": "2", "value": "待审核"},
        {"key": "3", "value": "已预约"},
        {"key": "4", "value": "内部关闭"},
        {"key": "5", "value": "已使用"},
    ],
    orderType: {
        1: "预约中",
        3: "预约到场",
        4: "预约取消",
        5: "未到场"
    },
    orderTypeDict: [
        {"key": "1", "value": "预约中"},
        {"key": "3", "value": "预约到场"},
        {"key": "4", "value": "预约取消"},
        {"key": "5", "value": "未到场"},
    ],
    VenuePOtype: {
        1: "篮球",
        2: "羽毛球",
        3: "网球",
        4: "足球",
        5: "笼室足球"
    },
    VhouseType: [
        {"key": "1", "value": "篮球"},
        {"key": "2", "value": "羽毛球"},
        {"key": "3", "value": "网球"},
        {"key": "4", "value": "足球"},
        {"key": "5", "value": "笼室足球"},
        {"key": "6", "value": "乒乓球"}
    ],
    groups: {
        1: "篮球组",
        2: "羽毛球组",
        3: "网球组",
        4: "足球组",
        5: "笼室足球组"
    },
    VenuePOtypeDict: [
        {"key": "1", "value": "篮球"},
        {"key": "2", "value": "羽毛球"},
        {"key": "3", "value": "网球"},
        {"key": "4", "value": "足球"},
        {"key": "5", "value": "笼室足球"}
    ],
    groupsDict: [
        {"key": "1", "value": "篮球组"},
        {"key": "2", "value": "羽毛球组"},
        {"key": "3", "value": "网球组"},
        {"key": "4", "value": "足球组"},
        {"key": "5", "value": "笼室足球组"}
    ]

};

var api = {
    serverHost:"https://admin.zjtyj.cn",
    //serverHost:"https://back.zhanchengwlkj.com/atxca",
    findPeriodTimeListById:"/front/findPeriodTimeListById",
    findPeriodTimeListById2:"https://admin.zjtyj.cn/front/findPeriodTimeListById",
    findChangDiList:"/front/findChangDiList",
    updatePianChangState:"/front/updatePianChangState",
    findChangGuanTypeList:"/front/findChangGuanTypeList",
    deleteChangGuanById:"/front/deleteChangGuanById",
    //findChangGuanById:"/front/findChangGuanById",
    findChangGuanById:"https://admin.zjtyj.cn/front/findChangGuanById",
    findPianChangList2:"/front/findPianChangList",
    findPianChangList:"https://admin.zjtyj.cn/front/findPianChangList",
    //updateChangGuan:"/front/updateChangGuan",
    updateChangGuan:"https://admin.zjtyj.cn/front/updateChangGuan",
    //updatePianChang:"/front/updatePianChang",
    updatePianChang:"https://admin.zjtyj.cn/front/updatePianChang",
    addCloseDateTimeByMore:"https://admin.zjtyj.cn/front/addCloseDateTimeByMore",
    //updatePeriodTime: "/sport/updatePeriodTime",
    updatePeriodTime:"https://admin.zjtyj.cn/front/updatePeriodTime",
    addPianChangByMore:"https://admin.zjtyj.cn/front/addPianChangByMore",

    findOrderListByPhone:"/front/findOrderListByPhone",
    findOrderListByName:"/front/findOrderListByName",
    refreshOrder:"/front/refreshOrder",

    findOrderListBySuccess:"/front/findOrderListBySuccess",
    daochuExcel2:"/front/daochuExcel2",


    findOrderList:"/front/findOrderList",
    findChangGuanList:"/front/findChangGuanList",
    deleteOrderById:"/front/deleteOrderById",

    queryVenueForBack: "/sport/queryVenueForBack",
    queryVhouseALL: "/sport/queryVhouseALL",
    queryVenueALL: "/sport/queryVenueALL",
    queryPeriodTimeALL: "/sport/queryPeriodTimeALL",
    pagequeryOrderPO: "/order/pagequeryOrderPO",
    updateVenue: "/sport/updateVenue",

    addVenue: "/sport/addVenue",
    addPeriodTime: "/sport/addPeriodTime",
    addVhouse: "/sport/addVhouse",
    addVenueOrder: "/sport/addVenueOrder",
    queryByReserveTimeAndAndPid: "/sport/queryByReserveTimeAndAndPid",

    updateVenueType: "/sport/updateVenueType",


    deletePeriodTime: "/sport/deletePeriodTime",
    deleteVenue: "/sport/deleteVenue",


    approvedPass: "/order/approvedPass",// 审批通过
    approvedFail: "/order/approvedFail",//审批拒绝
    payCheck: "/order/payCheck",//付款确认
    payCancel: "/order/payCancel",//取消订单(付款)

    //file
    qeryImages: "/files/qeryImages",
    uploadScrollimg: "/files/uploadScrollimg",
    uploadBuildingImage: "/files/uploadBuildingImage",


    // user
    loginUserManage: "/wxlog/loginUserManage",
    updateUserManage: "/wxlog/updateUserManage",

    //data
    queryTypePO: "/data/queryTypePO",
    daochu: "/data/daochu",
    daochuExcel: "/data/daochuExcel",
    queryForPicture: "/data/queryForPicture",
    querDateNum: "/order/querDateNum",
    queryOrderId:"/order/queryOrderId",
    qyerListoerxer:"/order/qyerListoerxer",
    updateOrder:"/order/updateOrder",
    //createOrderForBack:"/order/createOrderForBack",
    createOrder:"/front/createOrder",
};