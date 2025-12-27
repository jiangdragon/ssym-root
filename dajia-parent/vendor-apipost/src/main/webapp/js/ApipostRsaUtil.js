/**
 * ApipostRsaUtil.js
 * RSA加密
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
(function (Apipost) {
    Apipost.RSA = {
        "WechatPubKey": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDLYs/nSKhOUwqbnZsnWC+sC/u9jAruh95Rr8k93poQ4eWqJHhsNqUtdnKbI/ag/QeGMnxoKNcA+iFER0ga8FOfFCZUjErYkEyNikeUQuDJ3DEp3Po44z6D5TtYKXTTpAzy2KffJEe0N7+RKz7rkZniVNksB4rg3mqIAgS7OS3bQIDAQAB",
        "init": function (pubKey) {
            var encryptor = new JSEncrypt();
            encryptor.setPublicKey(pubKey);
            return encryptor.getKey();
        },
        "WechatEncrypt": function (str) {
            var _key = Apipost.RSA.init(Apipost.RSA.WechatPubKey);
            var length = ((_key.n.bitLength() + 7) >> 3) - 11;
            if (length <= 0) return false;
            var ret = "";
            var i = 0;
            while (i + length < str.length) {
                ret += _key.encrypt(str.substring(i, i + length));
                i += length;
            }
            ret += _key.encrypt(str.substring(i, str.length));

            var wordArray = CryptoJS.enc.Hex.parse(ret);
            return CryptoJS.enc.Base64.stringify(wordArray);
        }
    }
})(Apipost);