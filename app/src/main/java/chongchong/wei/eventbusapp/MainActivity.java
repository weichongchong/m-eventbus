package chongchong.wei.eventbusapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import chongchong.wei.meventbus.EventBus;
import chongchong.wei.meventbus.Subscribe;
import chongchong.wei.meventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getInstance().register(this);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getInstance().send(new Message(1, "activity启动了"));
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(Message message) {
        Log.i("event_bus", message.getMsg());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unRegister(this);
    }
}
