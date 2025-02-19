package com.xtree.live.socket;

import android.util.Log;

import com.xtree.live.BuildConfig;

public class ChatClientFactory {
    /**
     * @param clientType 0 socket 1 mqtt
     * @return
     */
    static ChatClient createClient(int clientType, String scheme, String host, String token, ChatWebSocketHandler handler) throws Exception {
//        if(clientType == 0){
//            String url = scheme + "://" + host + "/mqtt";
//            if(AppManager.debuggable()){
//                LogUtil.d("ChatWebSocketManager#ChatMqttClient", url);
//            }
//            ChatMqttClient client = new ChatMqttClient( url, token, new MemoryPersistence());
//            client.setConnectOptions(getMqttConnectOptions(token));
//            client.setHandler(handler);
//            return client;
//        }else {
        String url = scheme + "://" + host + "/wss/?userToken=" + token;
        if (BuildConfig.DEBUG) Log.d("ChatWebSocketManager", url);
//        return new ChatWebSocketClient(URI.create(url), Helper.getHeaders(), handler);
        return null;
//        }
    }

//    @NonNull
//    private static MqttConnectOptions getMqttConnectOptions(ChatToken token) {
//        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
//        ChatToken.Mqtt mqtt = token.mqtt;
//        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_DEFAULT);
//        mqttConnectOptions.setUserName(mqtt.username);
//        mqttConnectOptions.setAutomaticReconnect(true);
//        mqttConnectOptions.setCleanSession(false);
//        mqttConnectOptions.setPassword(mqtt.password.toCharArray());
//        mqttConnectOptions.setKeepAliveInterval(mqtt.keepAlive);
//        return mqttConnectOptions;
//    }


}
