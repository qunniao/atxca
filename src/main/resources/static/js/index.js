$(function () {

    var userPassWord = getQueryVariable("userPassWord");
    var userName = getQueryVariable("userName");

    params.userMangerss["userName"]=userName;
    params.userMangerss["userPassWord"]=userPassWord;
    function getQueryVariable(variable)
    {
        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i=0;i<vars.length;i++) {
            var pair = vars[i].split("=");
            if(pair[0] == variable){return pair[1];}
        }
        return(false);
    }
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

    $("#PageContent").load("page/indexItem.html");

    $("#idx1").click(function () {
        clearCss("idx1", "page/indexItem")
    });

    $("#idx2").click(function () {
        clearCss("idx2", "page/reservation")
    });
    $("#idx3").click(function () {
        clearCss("idx3","page/baobiao")
    });

    $("#idx4").click(function () {
        clearCss("idx4","page/updataUser")
    });

    $("#idx5").click(function () {
        clearCss("idx5","page/OrderHistory")
    });

    function clearCss(name, paageName) {
        $("#idx0").removeClass("active");
        $("#idx1").removeClass("active");
        $("#idx2").removeClass("active");
        $("#idx3").removeClass("active");
        $("#idx4").removeClass("active");
        $("#idx5").removeClass("active");

        $("#" + name).addClass("active");
        $("#PageContent").html(null);
        $("#PageContent").load(paageName + ".html");
    }

    $("#logout").click(function () {
        window.frames[0].location.href="login.html";
    });


});