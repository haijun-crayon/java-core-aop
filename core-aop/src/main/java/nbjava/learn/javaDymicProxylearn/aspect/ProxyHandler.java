package nbjava.learn.javaDymicProxylearn.aspect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyHandler implements InvocationHandler {

    private Object proxied;

    public ProxyHandler(Object proxied) {
        this.proxied = proxied;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //在转调具体目标对象之前，可以执行一些功能处理
        System.out.println("调用之间");
        //转调具体目标对象的方法
        Object result = method.invoke(proxied, args);
        System.out.println("调用之后，返回结果之间");
        return result;
        //在转调具体目标对象之后，可以执行一些功能处理
    }
}
