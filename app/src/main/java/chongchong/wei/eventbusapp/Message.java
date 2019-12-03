package chongchong.wei.eventbusapp;

/**
 * 包名：chongchong.wei.eventbusapp
 * 创建人：apple
 * 创建时间：2019-12-03 13:34
 * 描述：
 */
public class Message {
    private int id;
    private String msg;

    public Message(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
