/**
 * ApipostVendor.js
 * 获取当前环境增值服务用户信息
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
(function (Apipost) {
    var userJson = {
        "dev": {
            "G_VENDOR_ClIENT_ID": "dmp",// client_id
            "G_VENDOR_ClIENT_SECRET": "db21cf5b0b214b2aaedcc004f589e1fa",// client_secret
            "PublicKey": "040feb1b5990dec9bf6291da537711e459b4638e19432a4f2b4196235ae23207f146e112902bf50886a2f497489b4c81a33447851d68d84ebac5e2a32346412aab"
        }, "dat": {
            "G_VENDOR_ClIENT_ID": "dmp",// client_id
            "G_VENDOR_ClIENT_SECRET": "db21cf5b0b214b2aaedcc004f589e1fa",// client_secret
            "PublicKey": "040feb1b5990dec9bf6291da537711e459b4638e19432a4f2b4196235ae23207f146e112902bf50886a2f497489b4c81a33447851d68d84ebac5e2a32346412aab"
        }, "uat": {
            "G_VENDOR_ClIENT_ID": "dmp",// client_id
            "G_VENDOR_ClIENT_SECRET": "db21cf5b0b214b2aaedcc004f589e1fa",// client_secret
            "PublicKey": "040feb1b5990dec9bf6291da537711e459b4638e19432a4f2b4196235ae23207f146e112902bf50886a2f497489b4c81a33447851d68d84ebac5e2a32346412aab"
        }, "vir": {
            "G_VENDOR_ClIENT_ID": "dmp",// client_id
            "G_VENDOR_ClIENT_SECRET": "db21cf5b0b214b2aaedcc004f589e1fa",// client_secret
            "PublicKey": "040feb1b5990dec9bf6291da537711e459b4638e19432a4f2b4196235ae23207f146e112902bf50886a2f497489b4c81a33447851d68d84ebac5e2a32346412aab"
        }, "svir": {
            "G_VENDOR_ClIENT_ID": "dmp",// client_id
            "G_VENDOR_ClIENT_SECRET": "db21cf5b0b214b2aaedcc004f589e1fa",// client_secret
            "PublicKey": "040feb1b5990dec9bf6291da537711e459b4638e19432a4f2b4196235ae23207f146e112902bf50886a2f497489b4c81a33447851d68d84ebac5e2a32346412aab"
        }, "prod": {
            "G_VENDOR_ClIENT_ID": "dmp",// client_id
            "G_VENDOR_ClIENT_SECRET": "962338ae3f7342e7a7395690542b6bf4",// client_secret
            "PublicKey": "04458a4d150f335311032801f3de117ba2cb5cb8c0892f5e471362ec6bc1ca80b01c1f429fe521149268e283d9a8a8a3a4c4b192b10ba24796a344f5b05c439c0c"
        }
    }
    // 获取当前环境用户信息
    var _currentUser = userJson[apt.globals.get("CURRENT_EVN")];
    for (var prop in _currentUser) {
        apt.globals.delete(prop);// 清除变量
        apt.globals.set(prop, _currentUser[prop]);// 登录姓名
    }
    Apipost.Vendor = {
        "PublicKey": _currentUser.PublicKey
    };
})(Apipost);