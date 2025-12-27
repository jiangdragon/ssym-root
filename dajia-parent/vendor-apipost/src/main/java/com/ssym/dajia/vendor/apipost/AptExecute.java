package com.ssym.dajia.vendor.apipost;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author WB20200724005
 * @Description
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/11/27
 */
public class AptExecute {

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.print("parameter must be two more");
            return;
        }
        // 类
        Class<?> clazz = Class.forName(args[0]);
        Method[] methods = clazz.getMethods();
        // 方法名
        String methodeName = args[1];
        int paramLen = args.length - 2;
        Method method = Arrays.stream(methods)
                .filter(t -> methodeName.equals(t.getName()) && paramLen == t.getParameterCount())
                .findFirst().orElse(null);

        if (ObjectUtil.isEmpty(method)) {
            System.out.print("Not Find methode:" + methodeName);
            return;
        }
        // 判断是否为静态方法
        Object instance = null;
        if (!Modifier.isStatic(method.getModifiers())) {
            instance = clazz.getDeclaredConstructor().newInstance();
        }
        // 方法调用
        Object[] params = new Object[]{};
        if (paramLen > 0) {
            params = Arrays.copyOfRange(args, 2, args.length);
        }
        String returnStr = JSONUtil.toJsonStr(method.invoke(instance, params));

        System.out.print(returnStr);
    }
}
