package chongchong.wei.meventbus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 包名：chongchong.wei.meventbus
 * 创建人：apple
 * 创建时间：2019-12-02 16:21
 * 描述
 */
public class EventBus implements IBus {
    /**
     * 订阅方法
     * key 订阅类
     * value 类中的订阅方法集合
     */
    private final Map<Object, List<SubscriptionMethod>> mSubcription;
    private final EventBusHelper eventBusHelper;

    private EventBus() {
        eventBusHelper = new EventBusHelper();
        this.mSubcription = new HashMap<>();
    }

    private static class Instance {
        private static final EventBus instance = new EventBus();
    }

    public static EventBus getInstance() {
        return Instance.instance;
    }

    @Override
    public void register(Object subscriber) {
        List<SubscriptionMethod> methodList = mSubcription.get(subscriber);
        if (methodList == null || methodList.size() == 0) {
            methodList = new ArrayList<>();
            Class<?> clazz = subscriber.getClass();
            while (clazz != null) {
                String className = clazz.getName();
                if (className.startsWith("java.") || className.startsWith("javax.") || className.startsWith("android.")) {
                    break;
                }
                eventBusHelper.findAnnotationMethod(methodList, clazz);
                //查找父类，有继承关系
                clazz = clazz.getSuperclass();
            }
            mSubcription.put(subscriber, methodList);
        }
    }

    @Override
    public void send(Object event) {
        if (mSubcription.size() == 0) {
            return;
        }
        Set<Object> set = mSubcription.keySet();
        Iterator<Object> iterable = set.iterator();
        while (iterable.hasNext()) {
            Object next = iterable.next();
            List<SubscriptionMethod> methodList = mSubcription.get(next);
            if (methodList == null) {
                continue;
            }
            int size = methodList.size();
            for (int i = 0; i < size; i++) {
                SubscriptionMethod method = methodList.get(i);
                //method.getType()是获取方法参数类型，这里是判断发布的对象类型是否与订阅方法的参数类型一致
                if (method.getType().isAssignableFrom(event.getClass())) {
                    eventBusHelper.invokeWithThread(next, method, event);
                }
            }
        }
    }

    @Override
    public void unRegister(Object target) {
        List<SubscriptionMethod> methodList = mSubcription.get(target);
        if (methodList == null) return;
        methodList.clear();
        mSubcription.remove(target);
    }
}
