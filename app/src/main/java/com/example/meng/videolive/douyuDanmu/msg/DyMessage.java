package com.example.meng.videolive.douyuDanmu.msg;

import com.example.meng.videolive.douyuDanmu.utils.FormatTransfer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Created by 小萌神_0 on 2016/5/29.
 */
public class DyMessage {
    //弹幕客户端类型设置
    public final static int DY_MESSAGE_TYPE_CLIENT = 689;

    /**
     * 生成登录请求数据包
     * @param roomId
     * @return
     */
    public static byte[] getLoginRequestData(int roomId){
        //编码器初始化
        DyEncoder enc = new DyEncoder();
        //添加登录协议type类型
        enc.addItem("type", "loginreq");
        //添加登录房间ID
        enc.addItem("roomid", roomId);

        //返回登录协议数据
        return DyMessage.getByte(enc.getResult());
    }

    /**
     * 解析登录请求返回结果
     * @param respond
     * @return
     */
    public static boolean parseLoginRespond(byte[] respond){
        boolean rtn = false;

        //返回数据不正确（仅包含12位信息头，没有信息内容）
        if(respond.length <= 12){
            return rtn;
        }

        //解析返回信息包中的信息内容
        String dataStr = new String(respond, 12, respond.length - 12);

        //针对登录返回信息进行判断
//        if(StringUtils.contains(dataStr, "type@=loginres")){
//            rtn = true;
//        }
        if(dataStr.contains("type@=loginres")){
            rtn = true;
        }

        //返回登录是否成功判断结果
        return rtn;
    }

    /**
     * 生成加入弹幕分组池数据包
     * @param roomId
     * @param groupId
     * @return
     */
    public static byte[] getJoinGroupRequest(int roomId, int groupId){
        //编码器初始化
        DyEncoder enc = new DyEncoder();
        //添加加入弹幕池协议type类型
        enc.addItem("type", "joingroup");
        //添加房间id信息
        enc.addItem("rid", roomId);
        //添加弹幕分组池id信息
        enc.addItem("gid", groupId);

        //返回加入弹幕池协议数据
        return DyMessage.getByte(enc.getResult());
    }

    /**
     * 生成心跳协议数据包
     * @param timeStamp
     * @return
     */
    public static byte[] getKeepAliveData(int timeStamp){
        //编码器初始化
        DyEncoder enc = new DyEncoder();
        //添加心跳协议type类型
        enc.addItem("type", "keeplive");
        //添加心跳时间戳
        enc.addItem("tick", timeStamp);

        //返回心跳协议数据
        return DyMessage.getByte(enc.getResult());
    }

    /**
     * 通用方法，将数据转换为小端整数格式
     * @param data
     * @return
     */
    private static byte[] getByte(String data){
        ByteArrayOutputStream boutput = new ByteArrayOutputStream();
        DataOutputStream doutput = new DataOutputStream(boutput);

        try
        {
            boutput.reset();
            doutput.write(FormatTransfer.toLH(data.length() + 8), 0, 4);        // 4 bytes packet length
            doutput.write(FormatTransfer.toLH(data.length() + 8), 0, 4);        // 4 bytes packet length
            doutput.write(FormatTransfer.toLH(DY_MESSAGE_TYPE_CLIENT), 0, 2);   // 2 bytes message type
            doutput.writeByte(0);                                               // 1 bytes encrypt
            doutput.writeByte(0);                                               // 1 bytes reserve
            doutput.writeBytes(data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return boutput.toByteArray();
    }
}
