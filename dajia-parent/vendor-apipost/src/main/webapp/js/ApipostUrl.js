/**
 * ApipostUrl.js
 * 统一设置系统访问URL
 * @author silly(傻大)
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/28
 */
var Apipost = Apipost || {};
(function (Apipost) {
    var reqOptJson = {
        "customer-gateway": {
            "LOCAL-URL": "http://127.0.0.1:38081",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/customer/gateway",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/customer/gateway"
        }, "customer-security": {
            "LOCAL-URL": "http://127.0.0.1:38082",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/customer/security",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/customer/security"
        }, "customer-policy": {
            "LOCAL-URL": "http://127.0.0.1:38083",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/customer/policy",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/customer/policy"
        }, "customer-adapter": {
            "LOCAL-URL": "http://127.0.0.1:38084",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/customer/adapter",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/customer/adapter"
        }, "customer-revisit": {
            "LOCAL-URL": "http://127.0.0.1:38085",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/customer/revisit",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/customer/revisit"
        }, "customer-third": {
            "LOCAL-URL": "http://127.0.0.1:38086",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/customer/third",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/customer/third"
        }, "under-protection": {
            "LOCAL-URL": "http://127.0.0.1:18059/api_protection",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/underwriting/protection",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/underwriting/protection"
        }, "under-djcwechat": {
            "LOCAL-URL": "http://127.0.0.1:38083",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/underwriting/djcwechat",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/underwriting/djcwechat"
        }, "pcmanage": {// 再来人系统访问URL
            "LOCAL-URL": "http://127.0.0.1:18090",
            "TEST-URL": "https://life-customer-${env}.djbx.com/serve/underwriting/pcmanage",
            "PROD-URL": "https://life-underwaiting.djbx.com/serve/underwriting/pcmanage"
        }, "vendor": {// vendor系统访问URL
            "LOCAL-URL": "http://127.0.0.1:9900/vendor",
            "TEST-URL": "https://life-customer-${env}.djbx.com/vendor",
            "PROD-URL": "https://life-customer.djbx.com/vendor"
        }, "meddile": {// 中台统一接入
            "LOCAL-URL": "http://127.0.0.1:xx/xx",
            "TEST-URL": "https://life-insideservice-${env}.djbx.com/msfrest/json",
            "PROD-URL": "https://life-insideservice.djbx.com/msfrest/json"
        }, "meddile-gataway": {// 中台统一接入
            "LOCAL-URL": "http://127.0.0.1:xx/xx",
            "TEST-URL": "https://life-insideservice-${env}.djbx.com",
            "PROD-URL": "https://life-insideservice.djbx.com"
        }, "zjt-djbx": {// 集团
            "LOCAL-URL": "http://127.0.0.1:xx/xx",
            "TEST-URL": "https://zjt.djbx.com",
            "PROD-URL": "https://zj.djbx.com"
        }
    };
    var evn = ["local", "dev", "dat", "uat", "vir", "svir", "prod"];
    for (var service in reqOptJson) {
        var serviceObj = reqOptJson[service];
        for (var i = 0; i < evn.length; i < i++) {
            var serviceKey = service + "-" + evn[i];
            var requestUrl = serviceObj["TEST-URL"];
            if ("local" == evn[i]) {
                requestUrl = serviceObj["LOCAL-URL"];
            }
            if ("prod" == evn[i]) {
                requestUrl = serviceObj["PROD-URL"];
            }
            // 清除变量
            apt.globals.delete(serviceKey);
            apt.environment.delete(serviceKey);
            apt.globals.set(serviceKey, requestUrl.replace("${env}", evn[i]));
        }
    }
})(Apipost);

