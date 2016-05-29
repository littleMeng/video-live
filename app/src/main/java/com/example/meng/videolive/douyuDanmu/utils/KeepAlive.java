package com.example.meng.videolive.douyuDanmu.utils;

import com.example.meng.videolive.douyuDanmu.client.DyBulletScreenClient;

/**
 * Created by 小萌神_0 on 2016/5/29.
 */
public class KeepAlive extends Thread {
    @Override
    public void run()
    {
        //获取弹幕客户端
        DyBulletScreenClient danmuClient = DyBulletScreenClient.getInstance();

        //判断客户端就绪状态
        while(danmuClient.getReadyFlag())
        {
            //发送心跳保持协议给服务器端
            danmuClient.keepAlive();
            try
            {
                //设置间隔45秒再发送心跳协议
                Thread.sleep(45000);        //keep live at least once per minute
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
