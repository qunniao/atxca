$(function () {
    var flags = "update";
    var PeriodTimePO = {
        betTime: $("#betTime"),
        type: $("#type"),
        price: $("#price"),
        weekPrice: $("#weekPrice")
    };
    console.log("test!!!!!!!!!!");
    var dates = getYMDFormatDate();
    var listdatas = null;
    var flagese = false;
    var tempTimes = null;

    $('#idx7').fdatepicker({
        format: 'yyyy-mm-dd'
    });

    $("#betTime").bindData(final.betTimeList, "key", "value", true);
    $("#type").bindData(final.type, "key", "value", true);

    $("#fTime").bindData(final.betTimeList, "key", "value", true);
    $("#ftype").bindData(final.type, "key", "value", true);

    var vid = params.vid;
    var displayFunc = (function () {

        var renderList = function (tableId, listData) {
            var $tableWrap = $('#' + tableId);

            var _th = $('<tr></tr>');
            var td1 = $('<td> <strong>时间段</strong> </td>');
            var td2 = $('<td><strong>周1-5价格</strong> </td>');
            var td3 = $('<td><strong>周六日价格</strong> </td>');
            var td4 = $('<td><strong>状态</strong> </td>');
            var td5 = $('<td><strong>编辑</strong> </td>');

            _th.append(td1).append(td2).append(td3).append(td4).append(td5);
            _th.appendTo($tableWrap);
            if (listData && listData.length) {
                $.each(listData, function (i, v) {
                    var $liLine = creatLine(v);
                    $liLine.appendTo($tableWrap);
                });
            }
        };
        var creatLine = function (v) {

            if (flagese) {
                // console.log(tempTimes, v.pid);
                var opts = {"reserveTime": tempTimes, "pid": v.pid};
                com.post(api.qyerListoerxer, opts, function (res) {
                    var sgv = res.data;
                    if (sgv != null) {
                        if (sgv.type == 1) {
                            v.type = 2
                        } else if (sgv.type == 2) {
                            v.type = 3
                        }
                    }
                }, null, false);

                com.post(api.queryByReserveTimeAndAndPid, opts, function (res) {
                    var data = res.data;
                    if (data != null) {
                        console.log("type:" + res.data.type)
                        v.type = res.data.type;
                    }
                }, null, false);


            }

            var $tr = $('<tr></tr>');
            var propertyArr = [
                [v.betTime],
                [v.price],
                [v.weekPrice],
                [final.PeriodTimePOType[v.type]]
            ];
            // var propertyArr = [
            //     ["时间段:" + v.betTime],
            //     ["周1-5价格:" + v.price],
            //     ["周六日价格:" + v.weekPrice],
            //     ["状态:" + final.PeriodTimePOType[v.type]]
            // ];


            $.each(propertyArr, function (index, value) {
                // <button type="submit" class="btn btn-primary">Submit</button>


                if (index == 3) {
                    var cls = "";

                    if (v.type1 == 1) {
                        cls = "lead";
                    } else if (v.type == 2) {
                        cls = "text-danger";
                    } else if (v.type == 3) {
                        cls = "text-success";
                    } else if (v.type == 4) {
                        cls = "text-muted";
                    } else if (v.type == 5) {
                        cls = "text-primary";
                    }
                    var span = $('<span></span>').addClass(cls).html(value[0]);
                    var tdd = $('<td></td>');
                    tdd.append(span);
                    tdd.appendTo($tr);
                } else {
                    $('<td></td>').html(value[0]).appendTo($tr);
                }
            });

            // if (!flagese) {
            var btn = $('<td><button type="submit" class="btn btn-primary">编辑</button></td>');
            $tr.append(btn);

            btn.click(function () {
                editdepartment(v)
            });
            // }
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

    init();

    function init(opts) {
        opts = $.extend({"vid": vid}, opts);
        com.post(api.queryPeriodTimeALL, opts, function (res) {
            displayFunc.init('pageList4', res.data);
        }, null, false)
    }

    var pids;

    function editdepartment(v) {
        pids = v.pid;


        $("#betTime").find("option[value='" + v.betTime + "']").attr("selected", true);
        var price = $("#price").val(v.price);
        var weekPrice = $("#weekPrice").val(v.weekPrice);
        $("#type").find("option[value='" + v.type + "']").attr("selected", true);

        //有时间
        if (flagese) {
            $("#betTime").attr("disabled", "disabled");
            $("#price").attr("disabled", "disabled");
            $("#weekPrice").attr("disabled", "disabled");
        } else {
            $("#betTime").removeAttr("disabled");
            $("#price").removeAttr("disabled");
            $("#weekPrice").removeAttr("disabled");
        }

        $("#modal").show();
        init()


    }

    $("#saves").click(function () {
        if (flags == "add") {
            savePeriodTime();
            return
        }

        if (flagese) {
            svaeretimeType();
            return
        }

        var betTime = $("#betTime").val();
        var price = $("#price").val();
        var weekPrice = $("#weekPrice").val();
        var type = $("#type").val();

        var opt = {
            "betTime": betTime,
            "price": price,
            "weekPrice": weekPrice,
            "type": type,
            "pid": pids
        }
        var flag = true;
        $.each(opt, function (index, value) {

            if (value == '' || value == null || value == "undefined") {
                alert("请检查填写信息!");
                flag = false;
            }
        });

        if (flag == false) return;
        com.post(api.updatePeriodTime, opt, function (res) {
            init()
        }, null, false);


        $("#modal").hide();

    });

    $('#exit').click(function () {
        $("#modal").hide();
    });

    //查询
    $("#idx5").click(function () {
        flagese = false;
        listdatas = [];
        var flagse = {
            1: "betTime",
            2: "price",
            3: "type",
        };

        var opt = {
            "1": $("#fTime").val(),
            "2": $("#idx1").val(),
            "3": $("#ftype").val()
        };
        var options = {
            "vid": vid,
        };

        $.each(opt, function (i, value) {
            if (value != null && value != "undefined" && value != '') {
                options[flagse[i]] = value;
            }
        });
        var times = $("#idx7").val();
        if (times != null && times != "") {
            flagese = true;
            tempTimes = times;
        }
        init(options);

    });

    //增加
    $('#idx6').click(function () {
        $("#modal").show();

        PeriodTimePO.betTime.empty();
        PeriodTimePO.type.empty();

        PeriodTimePO.betTime.bindData(final.betTimeList, "key", "value", true);
        PeriodTimePO.type.bindData(final.type, "key", "value", true);

        var price = PeriodTimePO.price.val(null);
        var weekPrice = PeriodTimePO.weekPrice.val(null);
        flags = "add"
    });

    function savePeriodTime() {
        var opt = {
            "betTime": PeriodTimePO.betTime.val(),
            "type": $("#type").val(),
            "price": $("#price").val(),
            "weekPrice": $("#weekPrice").val(),
            "vid": vid
        };
        com.post(api.addPeriodTime, opt, function (res) {
            init();
        }, function () {
        }, false);

        $("#modal").hide();
    }

    $("#idx8").click(function () {
        clearFilter()
    });

    function svaeretimeType() {
        var opts = {
            "reserveTime": tempTimes,
            "pid": pids,
            "type": $("#type").val(),
        }
        console.log(opts)
        com.post(api.addVenueOrder, opts, function (res) {
            console.log(res);
            init()
            $("#modal").hide();
        }, function () {
        }, false)
    }

    function clearFilter() {
        $("#idx7").val(null);
        $("#idx11").val(null);
        $("#fTime option:first").prop("selected", 'selected');
        $("#ftype option:first").prop("selected", 'selected');
    }

    function getYMDhms_FormatDate() {
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
        var date = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();
        var hour = nowDate.getHours() < 10 ? "0" + nowDate.getHours() : nowDate.getHours();
        var minute = nowDate.getMinutes() < 10 ? "0" + nowDate.getMinutes() : nowDate.getMinutes();
        var second = nowDate.getSeconds() < 10 ? "0" + nowDate.getSeconds() : nowDate.getSeconds();
        return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
    }

    function getYMDFormatDate() {
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = nowDate.getMonth() + 1 < 10 ? "0" + (nowDate.getMonth() + 1) : nowDate.getMonth() + 1;
        var date = nowDate.getDate() < 10 ? "0" + nowDate.getDate() : nowDate.getDate();
        // var hour = nowDate.getHours()< 10 ? "0" + nowDate.getHours() : nowDate.getHours();
        // var minute = nowDate.getMinutes()< 10 ? "0" + nowDate.getMinutes() : nowDate.getMinutes();
        // var second = nowDate.getSeconds()< 10 ? "0" + nowDate.getSeconds() : nowDate.getSeconds();
        return year + "-" + month + "-" + date;
    }
});