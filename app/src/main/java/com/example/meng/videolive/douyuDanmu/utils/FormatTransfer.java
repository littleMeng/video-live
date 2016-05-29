package com.example.meng.videolive.douyuDanmu.utils;

/**
 * Created by 小萌神_0 on 2016/5/29.
 */
public class FormatTransfer {
    public   static   byte [] toLH( int  n)
    {
        byte [] b =  new   byte [ 4 ];
        b[0 ] = ( byte ) (n &  0xff );
        b[1 ] = ( byte ) (n >>  8  &  0xff );
        b[2 ] = ( byte ) (n >>  16  &  0xff );
        b[3 ] = ( byte ) (n >>  24  &  0xff );
        return  b;
    }

    public   static   byte [] toHH( int  n)
    {
        byte [] b =  new   byte [ 4 ];
        b[3 ] = ( byte ) (n &  0xff );
        b[2 ] = ( byte ) (n >>  8  &  0xff );
        b[1 ] = ( byte ) (n >>  16  &  0xff );
        b[0 ] = ( byte ) (n >>  24  &  0xff );
        return  b;
    }

    public   static   byte [] toLH( short  n)
    {
        byte [] b =  new   byte [ 2 ];
        b[0 ] = ( byte ) (n &  0xff );
        b[1 ] = ( byte ) (n >>  8  &  0xff );
        return  b;
    }

    public   static   byte [] toHH( short  n)
    {
        byte [] b =  new   byte [ 2 ];
        b[1 ] = ( byte ) (n &  0xff );
        b[0 ] = ( byte ) (n >>  8  &  0xff );
        return  b;
    }
}
