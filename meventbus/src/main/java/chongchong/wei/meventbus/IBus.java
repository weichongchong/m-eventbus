package chongchong.wei.meventbus;

/**
 * 包名：chongchong.wei.meventbus
 * 创建人：apple
 * 创建时间：2019-12-03 13:55
 * 描述：定义EventBus具有的行为
 */
public interface IBus {

    /**
     * 订阅
     */
    void register(Object subscriber);

    /**
     * 取消订阅,防止内存泄露
     */
    void unRegister(Object subscriber);

    /**
     * 发射数据
     *
     * @param data
     */
    void send(Object data);
}
