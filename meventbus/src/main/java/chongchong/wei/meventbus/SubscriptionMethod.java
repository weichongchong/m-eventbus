package chongchong.wei.meventbus;

import java.lang.reflect.Method;

/**
 * 包名：chongchong.wei.meventbus
 * 创建人：apple
 * 创建时间：2019-12-02 16:18
 * 描述：订阅者对象
 */
public class SubscriptionMethod {
    private Method method;
    private Class<?> type;
    private ThreadMode threadMode;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }
}
