$(function () {
    console.log("baobiao!!!!!!!!!!!!!!!!");
    $('#startTime').fdatepicker({
        format: 'yyyy-mm-dd'
    });
    $('#endTime').fdatepicker({
        format: 'yyyy-mm-dd'
    });
    var displayFunc = (function () {

        var renderList = function (tableId, listData) {
            var $tableWrap = $('#' + tableId);
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
                ["场地名称:" + v.vname],
                ["场馆名称:" + v.name],
                ["预约次数:" + v.peoplenum],
                ["总金额:" + v.price],

                // ["所属场馆:" + list[(v.type-1)].name],
                // ["所属组别:" + final.groups[v.groups]],
                // ["状态:" + v.status]
            ];

            $.each(propertyArr, function (index, value) {
                $('<td></td>').html(value[0]).appendTo($tr);
            });
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


    $("#bdx1").click(function () {
        var startTime = $("#startTime").val() + " 00:00:00";
        var endTime = $("#endTime").val() + " 23:59:59";
        var opt = {"startTime": startTime, "endTime": endTime};
        console.log(opt);
        com.post(api.daochu, opt, function (res) {
            console.log(res);
            if (res.data.length > 0) {
                var box2 = $("#bdx2");
                box2.empty()
                var _btn = $('<button type="button" class="btn btn-success">导出</button>');
                box2.append(_btn)
                _btn.click(function () {
                    doachufun(opt)
                });
            }
            displayFunc.init("baobiaoList", res.data)
        }, function () {
        }, false);

    });

    function doachufun(opt) {
        com.post(api.daochuExcel, opt, function (res) {
            console.log(res);
            window.open(res.data)
        }, function () {
        }, false)
    }

    $("#bdx3").click(function () {
        $('#startTime').val(null);
        $('#endTime').val(null);
        $("#bdx2").empty();
        $("#baobiaoList").html(null);
    });

    //-----------------------------------------------------------
    // var date = new Date();
    // console.log(date.getFullYear());
    // console.log(date.getMonth() + 1);
    // console.log( date.getDate());
    // var YYYYMM = date.getFullYear() + '-' + date.getMonth() + 1
    //
    // var d = moment(YYYYMM,"YYYY-MM"); //按照指定的年月字符串和格式解析出一个moment的日期对象
    // var firstDate = d.startOf("month"); //通过startOf函数指定取月份的开始即第一天
    // var lastDate = d.endOf("month");
    // // console.log(firstDate,lastDate);
    // var  start = moment().subtract('month', 0).format('YYYY-MM') + '-01'
    // var end = moment(start).subtract('month', 1).add('days', -1).format('YYYY-MM-DD')
    var start = moment().add('month', 0).format('YYYY-MM') + '-01'
    var end = moment(start).add('month', 1).add('days', -1).format('YYYY-MM-DD')

    var startTime = start + " 00:00:00"
    var endTime = end + " 23:59:59"
    var opt = {"startTime": startTime, "endTime": endTime};
    console.log(opt)
    var data1 = [];
    com.post(api.queryForPicture, opt, function (res) {
        data1 = res.data;
    }, function () {
    }, false);
    // drawPicture("mountNode1", data1)


    var date = new Date();
    // console.log(date.getFullYear());
    // console.log(date.getMonth() + 1);
    // console.log(date.getDate());

    var YYYYMM = date.getFullYear() + '-' + "01";
    var start2 = moment(YYYYMM).add('month', 0).format('YYYY-MM') + '-01'
    var YYYYMM2 = date.getFullYear() + '-' + "12";
    var end2 = moment(YYYYMM2).add('month', 1).add('days', -1).format('YYYY-MM-DD');
    var data2 = [];
    var opt2 = {"startTime": start2, "endTime": end2};
    com.post(api.queryForPicture, opt2, function (res) {
        data2 = res.data;
    }, function () {
    }, false);
    // drawPicture("mountNode2", data2)

    function drawPicture(container, data) {
        var chart = new G2.Chart({
            container: container,
            forceFit: true,
            height: 500
        });
        chart.source(data);
        chart.scale('value', {
            min: 0
        });
        chart.scale('year', {
            range: [0, 1]
        });
        chart.tooltip({
            crosshairs: {
                type: 'line'
            }
        });
        chart.line().position('year*value');
        chart.point().position('year*value').size(4).shape('circle').style({
            stroke: '#fff',
            lineWidth: 1
        });
        chart.render();
    }
});

