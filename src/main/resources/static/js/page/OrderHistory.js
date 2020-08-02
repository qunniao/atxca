$(function () {
    var pagepms = {
        pageNum: 1,
        pageSize: 15,
        totalPages: null,//总页数
        totalElements: null//总条数
    };

    $('#bdx3').fdatepicker({
        format: 'yyyy-mm-dd'
    });
    $('#usertimes').fdatepicker({
        format: 'yyyy-mm-dd'

    }).on('changeDate', function(ev) {
        checkMoney();
    }).data('datepicker');

    var orderIddd = null;
    $('#bdx4').bindData(final.betTimeList, "key", "value", true);
    $('#bdx5').bindData(final.orderTypeDict, "key", "value", true);
    $("#orderTypes").bindData(final.orderTypeDict, "key", "value", true);

    com.post(api.queryVenueALL,{"pageSize":500},function(res){
        $("#bdx45").bindData(res.data.content, "vid", "vname", true);
    },null,false);

    var displayFunc = (function () {

        var renderList = function (tableId, listData) {
            var $tableWrap = $('#' + tableId);
            var _th = $('<tr></tr>');
            //var td9 = $('<td> <strong>片场编号</strong> </td>');
            var td10 = $('<td> <strong>片场名称</strong> </td>');
            var td11 = $('<td> <strong>场馆名</strong> </td>');

            // var td1 = $('<td> <strong>订单编号</strong> </td>');
            var td2 = $('<td><strong>联系人姓名</strong> </td>');
            var td3 = $('<td><strong>联系人电话</strong> </td>');
            var td4 = $('<td><strong>预约日期</strong> </td>');
            var td5 = $('<td><strong>预约时间段</strong> </td>');
            var td6 = $('<td><strong>价格</strong> </td>');
            var td7 = $('<td><strong>订单状态</strong> </td>');
            var td8 = $('<td><strong>备注</strong> </td>');
            //  var td9 = $('<td><strong>创建时间</strong> </td>');
            var td45 = $('<td><strong>操作</strong> </td>');

            _th.append(td10).append(td11).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7).append(td45);
            _th.appendTo($tableWrap);

            if (listData && listData.length) {
                $.each(listData, function (i, v) {
                    var $liLine = creatLine(v);
                    $liLine.appendTo($tableWrap);
                });
            }
        };

        var creatLine = function (v) {

            var $tr = $('<tr></tr>');
            var propertyArr = [
                ["" + v.venuePO.vname],
                ["" + v.venuePO.typePO.name],
                ["" + v.name],
                ["" + v.phone],
                ["" + v.reserveTime],
                ["" + v.betTime],
                ["" + v.price],
                ["" + final.orderType[v.type]],
                //  ["" + v.createTime],
                // ["备注:" + v.remakes]
            ];
            // var propertyArr = [
            //     ["片场编号:" + v.venuePO.vnumber],
            //     ["片场名称" + v.venuePO.vname],
            //     ["订单编号:" + v.orderId],
            //     ["联系人姓名:" + v.name],
            //     ["联系人电话:" + v.phone],
            //     ["预约时间段:" + v.betTime],
            //     ["预约日期:" + v.reserveTime],
            //     ["价格:" + v.price],
            //     ["订单状态:" + final.orderType[v.type]],
            //     // ["备注:" + v.remakes]
            // ];

            $.each(propertyArr, function (index, value) {

                $('<td></td>').html(value[0]).appendTo($tr);
            });

            var btn = $('<td><button type="button" class="btn btn-success">确认</button></td>');
            var btn2 = $('<td><button type="button" class="btn btn-primary">取消</button></td>');
            var btn3 = $('<td><button type="button" class="btn btn-primary">付款</button></td>');
            var btn4 = $('<td><button type="button" class="btn btn-primary">编辑</button></td>');
            $tr.append(btn4);
            btn4.click(function () {
                orderIddd = v.orderId;
                updateModal(v)
            });

            if (final.orderType[v.type] == "预约待审核") {
                $tr.append(btn).append(btn2);

                btn.click(function () {
                    agree(v)
                });

                btn2.click(function () {
                    refuse(v)
                });
            }

            if (final.orderType[v.type] == "预约成功") {
                $tr.append(btn3);
                btn3.click(function () {
                    shouModal(v)
                });
            }

            return $tr;
        };

        return {
            init: function (tableId, listData) {
                $('#' + tableId).html(null);
                renderList(tableId, listData);
            },
            drawLine: function (v) {
                return creatLine(v);
            }
        };
    })();
    pageinit();

    //查询
    $("#bdx7").click(function () {
        var flagse = {
            1: "name",
            2: "phone",
            3: "orderId",
            4: "reserveTime",
            5: "betTime",
            6: "type",
            7: "vid"
        };

        var opt = {
            "1": $("#bdx11").val(),
            "2": $("#bdx1").val(),
            "3": $("#bdx2").val(),
            "4": $("#bdx3").val(),
            "5": $("#bdx4").val(),
            "6": $("#bdx5").val(),
            "7": $("#bdx45").val()
        };
        var options = {};

        $.each(opt, function (i, value) {

            if (value != null && value != "undefined" && value != '') {
                options[flagse[i]] = value;
            }
        });
        pageinit(options);

    });

    $("#bdx6").click(function () {
        clearFiter(4, "bdx")
    });

    function pageinit(opt) {
        console.log("pageinit");
        opt = $.extend(opt, pagepms);
        com.post(api.pagequeryOrderPO, opt, function (res) {
            console.log(res.data.content);
            displayFunc.init("reservationPage1", res.data.content);
            pagepms.totalPages = res.data.totalPages;
            pagepms.totalElements = res.data.totalElements;
        }, function () {
        }, false)
    }

    $("#reservationPage2").whjPaging({
        css: 'css-4',
        totalSize: pagepms.totalElements,
        totalPage: pagepms.totalPages,
        callBack: function (currPage, pageSize) {
            pagepms.pageNum = currPage;
            pagepms.pageSize = pageSize;
            pageinit();
        }
    });

    function clearFiter(size, name) {

        for (var i = 0; i < size + 1; i++) {
            $("#" + name + i).val(null);
        }
        $('#bdx5').empty();
        $('#bdx5').bindData(final.orderTypeDict, "key", "value", true);
        $("#bdx45 option:first").prop("selected", 'selected');
    }

    //待审核确认
    function agree(v) {
        com.post(api.approvedPass, {"orderId": v.orderId}, function (res) {
            pageinit()
        }, function () {
        }, false)
    }

    //待审核拒绝
    function refuse(v) {
        com.post(api.approvedFail, {"orderId": v.orderId}, function (res) {
            pageinit()
        }, function () {
        }, false);
    }

    //显示
    function shouModal(v) {
        $("#reservationModal").show();
        var ss = $("#sadfs");

        $("#orderId").html(v.orderId);
        $("#siteName").html(v.pid);
        $("#betTime").html(v.betTime);
        $("#reserveTime").html(v.reserveTime);
        $("#price").html(v.price);
        $("#userName").html(v.name);
        $("#phone").html(v.phone);
        $("#printTime").html(getNowTime());

        $("#affirm").click(function () {
            PayYes(v)
        });
        $("#abrogation").click(function () {
            PayNo(v)
        });
    }

    //收款确认
    function PayYes(v) {
        com.post(api.payCheck, {"orderId": v.orderId}, function (res) {
            hideModul()
        }, function () {
        }, false);
    }

    //收款确认拒绝
    function PayNo(v) {
        com.post(api.payCancel, {"orderId": v.orderId}, function (res) {
            hideModul()
        }, function () {
        }, false);
    }

    $("#exits").click(function () {
        hideModul()
    });


    $("#exits22").click(function () {
        $("#updateModal").hide();
    });
    //保存编辑
    $("#savess22").click(function () {
        console.log("保存编辑")
        var userName = $("#userNames").val();
        var userphone = $("#userphones").val();
        var price = $("#prices").val();
        var orderTypes = $("#orderTypes").val();
        var opt = {
            name: userName,
            phone: userphone,
            price: price,
            type: orderTypes,
            orderId: orderIddd
        };

        com.post(api.updateOrder, opt, function (res) {
            $("#PageContent").html(null);
            $("#PageContent").load("page/reservation.html");
            $("#updateModal").hide();
        }, null, false)

    });

    function updateModal(v) {
        $("#userNames").val(v.name);
        $("#userphones").val(v.phone);
        $("#prices").val(v.price);
        $("#orderTypes").find("option[value='" + v.type + "']").attr("selected", true);
        $("#updateModal").show();
    }

    function getNowTime() {
        var myDate = new Date;
        var year = myDate.getFullYear(); //获取当前年
        var mon = myDate.getMonth() + 1; //获取当前月
        var date = myDate.getDate(); //获取当前日
        // var h = myDate.getHours();//获取当前小时数(0-23)
        // var m = myDate.getMinutes();//获取当前分钟数(0-59)
        // var s = myDate.getSeconds();//获取当前秒
        var week = myDate.getDay();
        var weeks = ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
        return year + "年" + mon + "月" + date + "日" + weeks[week];
    }

    function hideModul() {
        pageinit();
        $("#reservationModal").hide()
    }

    var list3 = null;
    //添加显示模态框
    $("#bdx88").click(function () {
        var list1 = null;
        var list2 = null;
        list3=null;
        com.post(api.queryVhouseALL, null, function (res) {
            list1 = res.data.content;
        }, function () {
        }, false);

        com.post(api.queryVenueALL, null, function (res) {
            list2 = res.data.content;
        }, null, false);


        $("#vhouseid").bindData(list1, "id", "name", false);
        $("#venusesid").bindData(list2, "vid", "vname", false);
        //$("#pbettime").bindData(list3, "pid", "betTime", false);
        //首先找到Id为d的元素

        console.log("list1[0]:"+list1[0].id);
        var opt = {
            "id": list1[0].id
        }
        com.post(api.findPeriodTimeListById, opt, function (res) {
            list3 = res.payload;
        }, function () {
        }, false);
        console.log("list3:"+list3);
        var d=document.getElementById('pbettime');
        for(var i=0;i<list3.length;i++){
            //创建一个节点
            var a=document.createElement('input');
            a.type="checkbox";
            a.name="type"+list3[i].pid;
            a.id="type"+list3[i].pid;
            a.value=list3[i].pid;
            //a.style="margin-right:20px";
            d.appendChild(a);

            var b=document.createElement('span');
            b.innerHTML=list3[i].betTime;
            b.setAttribute("class","type_span");
            b.style="margin-right:20px";
            d.appendChild(b);
        }

        var name = $("#ylname").val(null);
        var phone = $("#ulphone").val(null);
        var price = $("#userprice").val(null);
        var usertimes = $("#usertimes").val(null);
        $("#addsModal").show();
    });

    $('#vhouseid').change(function () {
        var type = $('#vhouseid').val();
        console.log("vhouseid:"+type);
        $('#venusesid').empty();
        $('#pbettime').empty();
        var ss = $("vhouseid").val();

        var opt = {
            "type": type
        }
        var list = null;
        com.post(api.queryVenueALL, opt, function (res) {
            list = res.data.content;
        }, function () {
        }, false);

        $("#venusesid").bindData(list, "vid", "vname", false);
        $("#pbettime").bindData(list3, "pid", "betTime", false);



        list3=null;
        var opt = {
            "id": type
        }
        com.post(api.findPeriodTimeListById, opt, function (res) {
            list3 = res.payload;
        }, function () {
        }, false);
        console.log("list3:"+list3);
        var d=document.getElementById('pbettime');
        for(var i=0;i<list3.length;i++){
            //创建一个节点
            var a=document.createElement('input');
            a.type="checkbox";
            a.name="type"+list3[i].pid;
            a.id="type"+list3[i].pid;
            a.value=list3[i].pid;
            //a.style="margin-right:20px";
            d.appendChild(a);

            var b=document.createElement('span');
            b.innerHTML=list3[i].betTime;
            b.setAttribute("class","type_span");
            b.style="margin-right:20px";
            d.appendChild(b);



        }

    });

    $('#pbettime').click(function () {
        checkMoney();
    });



    function checkMoney() {
        console.log("checkMoney");

        var value = $("#usertimes").val().trim();
        console.log("日期："+value);
        if (value == "") {
            return;
        }
        var day = new Date(value).getDay();
        console.log("day："+day);

        var money=0;
        for(var i=0;i<list3.length;i++){
            var check = document.getElementById("type"+list3[i].pid).checked;
            console.log(check.checked);//false
            if(!check)continue;

            var text="";
            switch (day) {

                case 1:
                    text = "星期一";
                case 2:
                    text = "星期二";
                case 3:
                    text = "星期三";
                case 4:
                    text = "星期四";
                case 5:
                    text = "星期五";
                    money += list3[i].price;
                    console.log("星期几:" + text);
                    break;
                case 6:
                    text = "星期六";
                case 0:
                    text = "星期日";
                    money += list3[i].weekPrice;
                    console.log("星期几2:" + text);
                    break;
            }
        }
        console.log("money:"+money);
        $("#money").text(money+"元");
    }




    //添加显示模态框
    $("#savess23").click(function () {
        console.log("保存!!!!!!");

        var name = $("#ylname").val();
        if(""==name){
            alert("请输入姓名");
            return;
        }
        var phone = $("#ulphone").val();
        if(""==name){
            alert("请输入电话号码");
            return;
        }
        var pid="";
        for(var i=0;i<list3.length;i++){
            var item = document.getElementById("type"+list3[i].pid);
            if(null == item)continue;
            var check = document.getElementById("type"+list3[i].pid).checked;
            console.log(check.checked);//false
            if(!check)continue;
            pid+=$("#type"+list3[i].pid).val()+",";
        }
        console.log("pid:"+pid);


        var usertimes = $("#usertimes").val();
        if(""==usertimes){
            alert("请选择日期");
            return;
        }

        if(""==pid){
            alert("请选择时间段");
            return;
        }

        var venusesid = $("#venusesid").val();
        console.log("场地id："+venusesid);
        var opt = {
            "name": name,
            "phone": phone,
            "name": name,
            "pids": pid,
            "reserveTime": usertimes,
            "vid":venusesid
        }


        com.post(api.createOrderForBack, opt, function (res) {
            if(res.code!=200){
                alert(res.msg)
            }
            $("#addsModal").hide();
        }, function () {
        }, false)


    });

    //添加
    $("#exits23").click(function () {
        $("#addsModal").hide();
    })
});