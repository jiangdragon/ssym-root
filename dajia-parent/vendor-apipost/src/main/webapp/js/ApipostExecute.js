/**
 * ApipostExecute.js
 * 目录执行计划
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
(function () {
    var ApipostCofig = {
        "FILE_PATH": "C:/Users/Administrator/AppData/Local/apipost/ExternalPrograms",
        // ["local", "dev", "dat", "uat", "vir", "svir", "prod"]
        "CURRENT_EVN": "dev",// ["local", "dev", "dat", "uat", "vir", "svir", "prod"]
        "JS_FILE": ["ApipostUrl.js", "ApipostWechat.js", "ApipostVendor.js", "ApipostBase64Util.js", "ApipostAesUtil.js", "ApipostRsaUtil.js", "ApipostSM4Util.js"]
    };
    var _js_content = new Array(ApipostCofig.JS_FILE.length);
    for (var i = 0, len = ApipostCofig.JS_FILE.length; i < len; i++) {
        var fileName = ApipostCofig.JS_FILE[i].replace("${env}", ApipostCofig.CURRENT_EVN);
        _js_content[i] = await fs.readFileSync(ApipostCofig.FILE_PATH + "/" + fileName, "utf-8");
    }
    if (_js_content.length > 0) {
        apt.globals.set("CURRENT_EVN", ApipostCofig.CURRENT_EVN);
        eval(_js_content.join(""));
    }
})();
