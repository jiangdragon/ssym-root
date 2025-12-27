/**
 * ApipostWechat.js
 * 获取当前环境小程序用户信息
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
(function () {
    var userJson = {
        "dev": {
            "G_OPEN_ID": "orXDd4kSbJBgwQbjt_9MRhDYcuIE",// 登录open_id
            "G_UNION_ID": "okvy5s1a83Kg6ZFvm5sgIMLfE9mA",// 登录union_id
            "G_NICK_NAME": "silly",// 登录昵称
            "G_PHONE": "15811009846",// 登录手机号
            "G_NAME": "江龙",// 登录姓名
            "G_ID_NO": "411321198412202955",// 登录证件号
            "G_ID_TYPE": "0"// 登录证件类型 0-身份证
        }, "dat": {
            "G_OPEN_ID": "orXDd4kSbJBgwQbjt_9MRhDYcuIE",// 登录open_id
            "G_UNION_ID": "okvy5s1a83Kg6ZFvm5sgIMLfE9mA",// 登录union_id
            "G_NICK_NAME": "silly",// 登录昵称
            "G_PHONE": "15811009846",// 登录手机号
            "G_NAME": "江龙",// 登录姓名
            "G_ID_NO": "411321198412202955",// 登录证件号
            "G_ID_TYPE": "0"// 登录证件类型 0-身份证
        }, "uat": {
            "G_OPEN_ID": "orXDd4kSbJBgwQbjt_9MRhDYcuIE",// 登录open_id
            "G_UNION_ID": "okvy5s1a83Kg6ZFvm5sgIMLfE9mA",// 登录union_id
            "G_NICK_NAME": "silly",// 登录昵称
            "G_PHONE": "15811009846",// 登录手机号
            "G_NAME": "江龙",// 登录姓名
            "G_ID_NO": "411321198412202955",// 登录证件号
            "G_ID_TYPE": "0"// 登录证件类型 0-身份证
        }, "vir": {
            "G_OPEN_ID": "orXDd4kSbJBgwQbjt_9MRhDYcuIE",// 登录open_id
            "G_UNION_ID": "okvy5s1a83Kg6ZFvm5sgIMLfE9mA",// 登录union_id
            "G_NICK_NAME": "silly",// 登录昵称
            "G_PHONE": "15811009846",// 登录手机号
            "G_NAME": "江龙",// 登录姓名
            "G_ID_NO": "411321198412202955",// 登录证件号
            "G_ID_TYPE": "0"// 登录证件类型 0-身份证
        }, "svir": {
            "G_OPEN_ID": "orXDd4kSbJBgwQbjt_9MRhDYcuIE",// 登录open_id
            "G_UNION_ID": "okvy5s1a83Kg6ZFvm5sgIMLfE9mA",// 登录union_id
            "G_NICK_NAME": "silly",// 登录昵称
            "G_PHONE": "15811009846",// 登录手机号
            "G_NAME": "江龙",// 登录姓名
            "G_ID_NO": "411321198412202955",// 登录证件号
            "G_ID_TYPE": "0"// 登录证件类型 0-身份证
        }, "prod": {
            "G_OPEN_ID": "o2R-b5Z-k8naetsTlS0YIBluePlA",// 登录open_id
            "G_UNION_ID": "okvy5s1a83Kg6ZFvm5sgIMLfE9mA",// 登录union_id
            "G_NICK_NAME": "silly",// 登录昵称
            "G_PHONE": "15811009846",// 登录手机号
            "G_NAME": "江龙",// 登录姓名
            "G_ID_NO": "411321198412202955",// 登录证件号
            "G_ID_TYPE": "0"// 登录证件类型 0-身份证
        }
    }
    // 获取当前环境用户信息
    var _currentUser = userJson[apt.globals.get("CURRENT_EVN")];
    for (var prop in _currentUser) {
        apt.globals.delete(prop);// 清除变量
        apt.globals.set(prop, _currentUser[prop]);// 登录姓名
    }
})();