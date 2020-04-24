


$(function () {
    console.log("indexItem!!!!");

    var pagepms = {
        pageNum: 1,
        pageSize: 15,
        totalPages: null,//总页数
        totalElements: null//总条数
    };



    var vidss = null;
    $.fn.bindData = function (d, k, v, all, cb, o) {
        if (this[0] != undefined && this[0].nodeName.toLowerCase() == "select" && d != undefined) {
            this.empty();

            var optionHTML = "";

            if (all) {
                optionHTML = "<option value=''>全部</option>";
            }

            for (var i = 0; i < d.length; i++) {
                if (typeof d[i] == 'object') {
                    if (k == undefined || v == undefined || k == "" || v == "") {
                        console.log("该控件无法绑定列表数据");
                        return this;
                    }
                    var _o = o ? "other='" + d[i][o] + "'" : "";
                    optionHTML = optionHTML + "<option value='" + d[i][k] + "'" + _o + ">" + d[i][v] + "</option>";
                } else {
                    optionHTML = optionHTML + "<option value='" + d[i] + "' multikey='multikey'>" + d[i] + "</option>";
                }
            }

            $(optionHTML).appendTo(this);

            if (cb) {
                cb();
            }
        } else {
            console.log("该控件无法绑定列表数据");
        }
        return this;
    };
    var list = chagnguan();
    // var typePOList= getTypePO();
    // console.log(list)
    $("#adx1").bindData(list, "id", "name", true);
    $("#VenuePOtypess").bindData(list, "id", "name", true);

    $("#adx2").bindData(final.groupsDict, "key", "value", true);
    $("#groupss").bindData(final.groupsDict, "key", "value", true);

    var displayFunc = (function () {

        var renderList = function (tableId, listData) {
            var $tableWrap = $('#' + tableId);
            var _th = $('<tr></tr>');
            var td1 = $('<td> <strong>片场编号</strong> </td>');
            var td2 = $('<td><strong>场馆组</strong> </td>');
            var td3 = $('<td><strong>场馆</strong> </td>');
            var td4 = $('<td><strong>片场</strong> </td>');
            var td5 = $('<td><strong>查看</strong> </td>');
            var td7 = $('<td><strong>编辑</strong> </td>');
            var td6 = $('<td><strong>删除</strong> </td>');
            _th.append(td1).append(td2).append(td3).append(td4).append(td5).append(td7).append(td6);
            _th.appendTo($tableWrap);
            if (listData && listData.length) {
                $.each(listData, function (i, v) {
                    var $liLine = creatLine(v);
                    $liLine.appendTo($tableWrap);
                });
                // $tr.appendTo($tableWrap);
            }
        };

        var creatLine = function (v) {
            var $tr = $('<tr></tr>');
            var propertyArr = [
                ["片场编号:" + v.vnumber],
                ["场馆组:" + final.groups[v.groups]],
                ["场馆:" + list[(v.type - 1)].name],
                ["片场:" + v.vname],
                // ["状态:" + v.status]
            ];

            $.each(propertyArr, function (index, value) {
                $('<td></td>').html(value[0]).appendTo($tr);
            });

            var btn = $('<td><button type="button" class="btn btn-primary">查看</button></td>');
            var btn3 = $('<td><button type="button" class="btn btn-primary">编辑</button></td>');
            var btn2 = $('<td><button type="button" class="btn btn-primary">删除</button></td>');
            $tr.append(btn).append(btn3).append(btn2);

            btn.click(function () {
                editdepartment(v)
            });

            btn2.click(function () {
                delts(v)
            });

            btn3.click(function () {
                editfunc(v)
            })
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

    function editdepartment(v) {
        //查看
        params.vid = v.vid;
        $("#PageContent").load("test.html");

        /*layer.open({
            type: 1,
            skin: 'layui-layer-molv',
            title: "场地时间价格",
            content: $('#viewPopUp'),
            // area: ["100%", ( $(window).height() * 0.84 < 720 ? $(window).height() * 0.84 : 720 ) + "px"],
            area: ["60%", "60%"],
            //maxmin: true,
            zIndex: 9997,
            btn: ['确定'],
            success: function (layer, index) {
                params.periodTimePOList = v.periodTimePOList;
                $('#viewPopUp').load("test.html");
            },
            end: function () {
                params.periodTimePOList = {};
                $('#viewPopUp').html(null);
                // $popup.attr('data-gid', null);
            }
        });*/
    }

    $("#adx4").click(function () {
        $("#adx0").val("");
        $("#adx1").val("");
        $("#adx2").val("");
        $("#adx3").val("");
    });

    $("#adx5").click(function () {
        var flagse = {
            1: "vName",
            2: "type",
            3: "groups",
            4: "status"
        };

        var opt = {
            "1": $("#adx0").val(),
            "2": $("#adx1").val(),
            "3": $("#adx2").val(),
            "4": $("#adx3").val()
        };
        var options = {};

        $.each(opt, function (i, value) {
            if (value != null && value != "undefined" && value != '') {
                options[flagse[i]] = value;
            }
        });

        pageinit(options);
    });

    //增加场地
    $("#adx6").click(function () {
        $("#vhouseType").empty();
        $("#groups").empty();

        $("#VenuePOtype").bindData(chagnguan(), "id", "name", true);
        $("#groups").bindData(final.groupsDict, "key", "value", true);

        $("#vName").val(null);
        $("#addVenue").show();
    });

    $("#exits").click(function () {
        $("#addVenue").hide();
    });

    $("#savess").click(function () {

        var opt = {
            "type": $("#VenuePOtype").val(),
            "groups": $("#groups").val(),
            "vName": $("#vName").val()
        }
        var flasg = true;
        $.each(opt, function (i, value) {
            if (value == '' || value == null || value == "undefined") {
                flasg = false;
            }
        });
        if (flasg == false) {
            alert("请检查填写信息!");
            return
        }
        console.log()
        com.post(api.addVenue, opt, function (res) {
            pageinit(null);
            pagess();
            $("#addVenue").hide();
        }, function () {
        }, false)
    });






    function delts(v) {
        com.post(api.deleteVenue, {"vid": v.vid}, function (res) {
            pageinit();
        }, function () {
        }, false)
    }

    pageinit();
    pagess();

    function pageinit(opt) {
        opt = $.extend(opt, pagepms);

        com.post(api.queryVenueForBack, opt, function (res) {
            console.log(res);
            displayFunc.init("pageList2", res.data.content);
            pagepms.totalPages = res.data.totalPages;
            pagepms.totalElements = res.data.totalElements;
        }, function () {
        }, false)
    }

    function pagess() {
        $("#pagination_14").whjPaging({
            css: 'css-4',
            totalSize: pagepms.totalElements,
            totalPage: pagepms.totalPages,
            currPage: 2,
            callBack: function (currPage, pageSize) {
                console.log('currPage:' + currPage + '     pageSize:' + pageSize);
                pagepms.pageNum = currPage;
                pagepms.pageSize = pageSize;
                pageinit();
            }
        });
    }

    function hideModal(name) {
        $("#" + name).hide();
    }

    chagnguan();


    $("#savessas").click(function () {
        var opts = {
            "type": $("#VenuePOtypess").val(),
            "groups": $("#groupss").val(),
            "status": $("#statuss").val(),
            "vName": $("#vNames").val(),
            "vid":vidss,
        }
        com.post(api.updateVenue,opts,function(res){
            $("#editModal").hide();
        },null,false)

    });


    $("#exitsas").click(function () {
        $("#editModal").hide();
    });



    function editfunc(v) {
        vidss = v.vid;
        console.log(v);
        $("#VenuePOtypess").find("option[value='" + v.type + "']").attr("selected", true);
        $("#groupss").find("option[value='" + v.groups + "']").attr("selected", true);
        $("#statuss").find("option[value='" + v.status + "']").attr("selected", true);
        $("#vNames").val(v.vname);
        $("#editModal").show();
    }



    function chagnguan() {

        var list;
        com.post(api.queryVhouseALL, null, function (res) {
            list = res.data.content;
            console.log(res)
        }, function () {
        }, false);

        return list;
    }



    function getVhouse() {
        var typePOLists = [];
        com.post(api.queryTypePO, null, function (res) {
            typePOLists = res.data;
        }, function () {
        }, false);
        return typePOLists;
    }
});