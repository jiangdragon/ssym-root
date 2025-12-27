/**
 * ApipostAesUtil.js
 * AES加密
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
(function (Apipost) {
    Apipost.AES = {
        "WechatKey": "#uy7U*iwheYtrBi_",
        "WechatIV": "#uy7U*iwYtrBi_Iv",
        "WechatEncrypt": function () {
            var cipherText = arguments[0];
            var _key = CryptoJS.enc.Utf8.parse(Apipost.AES.WechatKey);
            var _iv = CryptoJS.enc.Utf8.parse(Apipost.AES.WechatIV);
            if (arguments.length == 3) {
                _key = CryptoJS.enc.Utf8.parse(arguments[1]);
                _iv = CryptoJS.enc.Utf8.parse(arguments[2]);
            }
            var _word = CryptoJS.enc.Utf8.parse(cipherText);
            var encrypted = CryptoJS.AES.encrypt(_word, _key, {
                iv: _iv, mode: CryptoJS.mode.CBC, padding: CryptoJS.pad.Pkcs7
            });
            return encrypted.toString();
        },
        "WechatJTKey": "sde@5f98H*^hsff%dfs$r344&df8543*er"
    }
})(Apipost);