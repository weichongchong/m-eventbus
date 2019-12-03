package chongchong.wei.meventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 包名：chongchong.wei.meventbus
 * 创建人：apple
 * 创建时间：2019-12-03 14:53
 * 描述：
 */
class EventBusHelper {
    private final Handler mHandler;
    private final ExecutorService mExecutorService;

    public EventBusHelper() {
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mExecutorService = Executors.newCachedThreadPool();
    }

    public void findAnnotationMethod(List<SubscriptionMethod> methodList, Class<?> clazz) {
        Method[] m = clazz.getDeclaredMethods();
        int size = m.length;
        for (int i = 0; i < size; i++) {
            Method method = m[i];
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if (annotation == null) {
                continue;
            }
            Type genericReturnType = method.getGenericReturnType();
            if (!"void".equals(genericReturnType.toString())) {
                throw new EventBusException("方法返回值必须是void");
            }
            int modifiers = method.getModifiers();
            if ((modifiers & Modifier.PUBLIC) != 1) {
                throw new EventBusException("方法修饰符必须是public，且是非静态，非抽象");
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new EventBusException("方法参数个数必须是一个");
            }

            //这里就需要实例化订阅方法对象了
            SubscriptionMethod subscriptionMethod = new SubscriptionMethod();
            subscriptionMethod.setMethod(method);
            subscriptionMethod.setType(parameterTypes[0]);
            subscriptionMethod.setThreadMode(annotation.threadMode());
            methodList.add(subscriptionMethod);
        }
    }


    public void invokeWithThread(final Object next, final SubscriptionMethod method, final Object event) {
        //进行线程切换
        switch (method.getThreadMode()) {
            case POSTING:
                invoke(next, method, event);
                break;
            case MAIN:
                //通过Looper判断当前线程是否是主线程
                //也可以通过线程名判断 "main".equals(Thread.currentThread().getName())
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    invoke(next, method, event);
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            invoke(next, method, event);
                        }
                    });
                }
                break;
            case BACKGROUND:
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    mExecutorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            invoke(next, method, event);
                        }
                    });
                } else {
                    invoke(next, method, event);
                }
                break;
            case ASYNC:
                mExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        invoke(next, method, event);
                    }
                });
                break;
        }
    }

    private void invoke(Object next, SubscriptionMethod method, Object event) {
        Method m = method.getMethod();
        try {
            m.invoke(next, event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
