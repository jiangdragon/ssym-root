/**
 * ApipostSM4Util.js
 * SM4加密
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
(function (Apipost) {
    Apipost.SM4 = {
        "WechatKey": "86C63180C2806ED1F47B859DE501215B",
        "WechatEncrypt": function () {
            var cipherText = arguments[0];
            var _padding = "pkcs#7";
            var _iv = "";
            if (arguments.length == 3) {
                _padding = arguments[1];
                _iv = arguments[2];
            }

            var encrypted = sm4.encrypt(cipherText, Apipost.SM4.WechatKey, {
                padding: _padding,
                mode: "cbc",
                iv: _iv
            });
            return encrypted;
        }
    }
})(Apipost);