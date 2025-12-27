/**
 * ApipostBase64Util.js
 * Base64加密
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/12/03
 */
(function (Apipost) {
    Apipost.Base64 = {
        "Hex2B64": function (cipherText) {
            var _wordArray = CryptoJS.enc.Hex.parse(cipherText);
            return CryptoJS.enc.Base64.stringify(_wordArray);
        },
        "Base64": function (cipherText) {
            var _wordArray = CryptoJS.enc.Utf8.parse(cipherText);
            return CryptoJS.enc.Base64.stringify(_wordArray);
        }
    }
})(Apipost);