/**
 * ApipostStrUtil.js
 * 字符串处理
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/12/19
 */
(function (Apipost) {
    Apipost.STR = {
        "hashCode": function (str) {
            var h = 0, off = 0;
            var len = str.length;
            for (var i = 0; i < len; i++) {
                h = 31 * h + str.charCodeAt(off++);
            }
            var t = -2147483648 * 2;
            while (h > 2147483647) {
                h += t
            }
            return h;
        }
    }
})(Apipost);