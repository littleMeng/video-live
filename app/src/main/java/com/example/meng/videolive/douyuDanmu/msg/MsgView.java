package com.example.meng.videolive.douyuDanmu.msg;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 小萌神_0 on 2016/5/29.
 */
public class MsgView {
    private Map<String, Object> messageList;

    public MsgView(String data){
        this.messageList = parseRespond(data);
    }

    /**
     * 获取弹幕信息对象
     * @return
     */
    public Map<String, Object> getMessageList() {
        return messageList;
    }

    /**
     * 设置弹幕信息对象
     * @param messageList
     */
    public void setMessageList(Map<String, Object> messageList) {
        this.messageList = messageList;
    }

    /**
     * 解析弹幕服务器接收到的协议数据
     * @param data
     * @return
     */
    public Map<String, Object> parseRespond(String data){
        Map<String, Object> rtnMsg = new HashMap<String, Object>();
        String dataWithoutLast;

        //处理数据字符串末尾的'/0字符'
        dataWithoutLast = TextUtils.substring(data, 0, TextUtils.lastIndexOf(data, '/'));

        //对数据字符串进行拆分
        String[] buff = dataWithoutLast.split("/");

        //分析协议字段中的key和value值
        for(String tmp : buff){
            //获取key值
            String key = TextUtils.substring(tmp, 0, TextUtils.indexOf(tmp, "@="));
            //获取对应的value值
            Object value = TextUtils.substring(tmp, TextUtils.indexOf(tmp, "@=") + 2, tmp.length());

            //如果value值中包含子序列化值，则进行递归分析
            if (TextUtils.indexOf((String)value, "@A") > 0) {
                value = ((String)value).replaceAll("@S", "/").replaceAll("@A", "@");
                value = this.parseRespond((String)value);
            }

            //将分析后的键值对添加到信息列表中
            rtnMsg.put(key, value);
        }
        return rtnMsg;
    }

    /**
     * 调试信息输出
     * @return
     */
    public String printStr() {
        return messageList.toString();
    }
}
