package nbjava.learn.javaDymicProxylearn;

import nbjava.learn.javaDymicProxylearn.aspect.ProxyHandler;
import nbjava.learn.javaDymicProxylearn.biz.Calculator;
import nbjava.learn.javaDymicProxylearn.biz.CalculatorImpl;

import java.lang.reflect.Proxy;

public class AopTest {

    public static void main(String[] args) {
        Calculator calculator = new CalculatorImpl();
        Calculator calculatorProxy = (Calculator) Proxy.newProxyInstance(Calculator.class.getClassLoader(), new Class[]{Calculator.class}, new ProxyHandler(calculator));
        int result = calculatorProxy.calculate(4, 2);

        System.out.println(result);
    }
}
