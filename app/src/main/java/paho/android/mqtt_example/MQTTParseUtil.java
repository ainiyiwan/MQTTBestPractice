package paho.android.mqtt_example;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * ================================================
 * 作    者：Luffy（张阳）
 * 版    本：1.0
 * 创建日期：2018/4/26
 * 描    述：MQTT解析工具类
 * 修订历史：
 * ================================================
 */
public class MQTTParseUtil {


    private static String MSGTYPE;//消息类型

    public static MQTTBean parseMessage(String payload) {
        try {
            JSONArray jsonArray = new JSONArray(payload);
            for (int i = 0; i < jsonArray.length(); i++) {
                if (i == 0) {
                    MSGTYPE = jsonArray.getString(i);
                } else if (i == 1) {
                    JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                    return parseMessageType(jsonArray1);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private static MQTTBean parseMessageType(JSONArray jsonArray) {
        try {
            MQTTBean bean = new MQTTBean();
            if (MSGTYPE.equals("101")) {
                int MSG_101_LENGTH = 5;//这里是指定长度
                if (jsonArray.length() < MSG_101_LENGTH) {
                    Log.i("TAG","无效数据");
                    return null;
                } else if (jsonArray.length() == MSG_101_LENGTH) {
                    bean.setMsg1(jsonArray.getString(0));
                    bean.setMsg2(jsonArray.getString(1));
                    bean.setMsg3(jsonArray.getString(2));
                    bean.setMsg4(jsonArray.getString(3));
                    bean.setMsg5(jsonArray.getString(4));
                    return bean;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
