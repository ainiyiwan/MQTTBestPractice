package paho.android.mqtt_example;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import paho.android.mqtt_example.util.MyAdapter;
import paho.android.mqtt_example.util.User;

/**
 * This is an implementation of MQTT Paho library MQTT Service which allows you to
 * connect to MQTT server via WebSockets, TCP or TLS with a stored certificate in resources folder
 * How to use:
 * Just uncomment desired BROKER and launch the application, then check logs for MQTT data
 */

public class MainActivity extends AppCompatActivity {


    //UI
    private RecyclerView recyclerView;
    private List<User> users = new ArrayList<>();
    private MyAdapter adapter;

    /**
     * Test servers from http://test.mosquitto.org and tps://www.hivemq.com/try-out/
     */

    public static final String BROKER = "ssl://192.168.1.94:8883";
    //pblic static final String BROKER = "tcp://test.mosquitto.org:1883";
//    public static final String BROKER = "ws://192.168.1.94:8883";

    //# Means subscribe to everything
    public static final String TOPIC = "/live/football/global";

    //Optional
    public static final String USERNAME = "YOUR_USERNAME";
    public static final String PASSWORD = "YOUR_PASSWORD";


    public MqttAndroidClient CLIENT;
//    public MqttAsyncClient CLIENT;
    public MqttConnectOptions MQTT_CONNECTION_OPTIONS;
    String clientId = "Androidzhangyang";
    private ScheduledExecutorService scheduler;
    private boolean isReconnecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(paho.android.mqtt_example.R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());

        for (int i = 0; i < 500; i++) {
            users.add(new User());
        }
        recyclerView = findViewById(R.id.recycler);
        adapter = new MyAdapter(R.layout.item_recycler, users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        MqttSetup(this);
        MqttConnect();
        CLIENT.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.e("mqtt","失去连接，开始重连");
                startReconnect();
                isReconnecting = true;
            }

            //background notification
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.e("mqtt:topic:" + topic, "message:" + message.toString());
                String payload = message.toString();
                MQTTBean bean = MQTTParseUtil.parseMessage(payload);
                MQTTBean bean1 = MQTTParseUtil.parseMessage(payload);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }

    private void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        Log.w("scheduler", scheduler.toString());
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {

                if (!CLIENT.isConnected()) {
                    MqttConnect();
                }
            }
        }, 0 * 1000, 5 * 1000, TimeUnit.MILLISECONDS);
    }

    void MqttSetup(Context context) {
        clientId = clientId + System.currentTimeMillis();

        CLIENT = new MqttAndroidClient(getBaseContext(), BROKER, clientId);
//        try {
//            CLIENT = new MqttAsyncClient(BROKER, clientId, null);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
        MQTT_CONNECTION_OPTIONS = new MqttConnectOptions();
//        MQTT_CONNECTION_OPTIONS.setAutomaticReconnect(true);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        MQTT_CONNECTION_OPTIONS.setKeepAliveInterval(20);

        /**
         * Depending on your MQTT broker, you might want to set these
         */

        //MQTT_CONNECTION_OPTIONS.setUserName(USERNAME);
        //MQTT_CONNECTION_OPTIONS.setPassword(PASSWORD.toCharArray());


        /**
         * SSL broker requires a certificate to authenticate their connection
         * Certificate can be found in resources folder /res/raw/
         */
        if (BROKER.contains("ssl")) {
            SocketFactory.SocketFactoryOptions socketFactoryOptions = new SocketFactory.SocketFactoryOptions();
            try {
                socketFactoryOptions.withCaInputStream(context.getResources().openRawResource(R.raw.cert3));
                MQTT_CONNECTION_OPTIONS.setSocketFactory(new SocketFactory(socketFactoryOptions));
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
                e.printStackTrace();
            }
        }
//        if (BROKER.contains("ssl")) {
//            MQTT_CONNECTION_OPTIONS.setSSLProperties(getSSLSettings());
//        }
//        if (BROKER.contains("ssl")) {
//            try {
//                MQTT_CONNECTION_OPTIONS.setSocketFactory(HttpsUtils.getSslSocketFactory(null, getAssets().open("zhy_client.bks"), "importkey"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


    }

    public final Properties getSSLSettings() {
        String path = "file:///android_asset/keystore.jks";
        final Properties properties = new Properties();
        properties.setProperty("com.ibm.ssl.keyStore", path);
        properties.setProperty("com.ibm.ssl.keyStoreType", "JKS");
        properties.setProperty("com.ibm.ssl.keyStorePassword", "importkey");
        properties.setProperty("com.ibm.ssl.trustStore", path);
        properties.setProperty("com.ibm.ssl.trustStoreType", "JKS");
        properties.setProperty("com.ibm.ssl.trustStorePassword", "importkey");
        return properties;
    }

    void MqttConnect() {
        try {
            final IMqttToken token = CLIENT.connect(MQTT_CONNECTION_OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    isReconnecting = false;
                    if (scheduler != null && !scheduler.isShutdown()){
                        scheduler.shutdown();
                    }
                    Log.i("mqtt:token", "connected, token:" + asyncActionToken.toString());
                    subscribe(TOPIC, (byte) 1);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.i("mqtt:", "not connected" + asyncActionToken.toString());
                    if (!isReconnecting) {
                        startReconnect();
                        isReconnecting = true;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduler.shutdown();
    }

    void subscribe(String topic, byte qos) {

        try {
            IMqttToken subToken = CLIENT.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt:", "subscribed" + asyncActionToken.toString());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {

                    Log.d("mqtt:", "subscribing error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void publish(String topic, String msg) {

        //0 is the Qos
        MQTT_CONNECTION_OPTIONS.setWill(topic, msg.getBytes(), 0, false);
        try {
            IMqttToken token = CLIENT.connect(MQTT_CONNECTION_OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt:", "send done" + asyncActionToken.toString());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("mqtt:", "publish error" + asyncActionToken.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void unsubscribe(String topic) {

        try {
            IMqttToken unsubToken = CLIENT.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.d("mqtt:", "unsubcribed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {


                    Log.d("mqtt:", "couldnt unregister");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    void disconnect() {
        try {
            IMqttToken disconToken = CLIENT.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d("mqtt:", "disconnected");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {


                    Log.d("mqtt:", "couldnt disconnect");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
