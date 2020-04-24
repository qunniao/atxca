$(function ($) {

    $("#login").click(function () {
        var userName = $("#userName").val();
        var userPassword = $("#userPassword").val();
        com.post(api.loginUserManage, {
            "userName": userName,
            "userPassWord": userPassword
        }, function (res) {
            if (res.code != 200) {
                alert(res.msg)
            }
            if (res.code == 200) {
                var ss = window.location.host;
                console.log(ss)
                if (ss == "localhost:8080") {
                    window.location.href = "/index.html?flages=yes123&userName=" + res.data.userName + "&userPassWord=" + res.data.userPassWord
                } else {
                    window.location.href = "/atxca/index.html?flages=yes123&userName=" + res.data.userName + "&userPassWord=" + res.data.userPassWord
                }


            }
            console.log(res)
        }, function (res) {
            console.log(res)
        }, false)
    })


});
