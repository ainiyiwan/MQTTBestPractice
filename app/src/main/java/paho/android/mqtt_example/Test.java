//package paho.android.mqtt_example;
//
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//
//import java.util.Properties;
//
///**
// * Created by Administrator on 2018/3/19.
// */
//
//
//    public static final Properties getSSLSettings() {
//        final Properties properties = new Properties();
//        properties.setProperty("com.ibm.ssl.keyStore", "D:/Temp/emq-certs/my/keystore.jks");
//        properties.setProperty("com.ibm.ssl.keyStoreType", "JKS");
//        properties.setProperty("com.ibm.ssl.keyStorePassword", "importkey");
//        properties.setProperty("com.ibm.ssl.trustStore", "D:/Temp/emq-certs/my/keystore.jks");
//        properties.setProperty("com.ibm.ssl.trustStoreType", "JKS");
//        properties.setProperty("com.ibm.ssl.trustStorePassword", "importkey");
//        return properties;
//    }
//
//    public static void main( String[] args )
//    {
//        String topic        = "/topic";
//        String content      = "[101, 1, \"hello\", 2]";
//        int qos             = 2;
//        String broker       = "ssl://192.168.1.94:8883";
//        String clientId     = "JavaClient";
//        MemoryPersistence persistence = new MemoryPersistence();
//
//        try {
//            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
//            MqttConnectOptions connOpts = new MqttConnectOptions();
//            connOpts.setCleanSession(true);
//            connOpts.setSSLProperties(getSSLSettings());
//            System.out.println("Connecting to broker: "+broker);
//            sampleClient.connect(connOpts);
//            System.out.println("Connected");
//            System.out.println("Publishing message: "+content);
//
//            for(int i=0; i<1000; i++){
//                MqttMessage message = new MqttMessage((content).getBytes());
//                message.setQos(qos);
//                sampleClient.publish(topic, message);
//            }
//
//            Thread.sleep(1000*60*10);
//            //sampleClient.disconnect();
//            //System.out.println("Disconnected");
//            //System.exit(0);
//        } catch(MqttException me) {
//            System.out.println("reason "+me.getReasonCode());
//            System.out.println("msg "+me.getMessage());
//            System.out.println("loc "+me.getLocalizedMessage());
//            System.out.println("cause "+me.getCause());
//            System.out.println("excep "+me);
//            me.printStackTrace();
//        }catch(Exception e){}
//    }
//
