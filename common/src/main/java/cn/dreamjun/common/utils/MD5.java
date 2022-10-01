package cn.dreamjun.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

@Slf4j
public class MD5 {
    static String TAG = "MD5";

    public static String createMd5(InputStream fos) {
        MessageDigest mMDigest;
        byte buffer[] = new byte[1024];
        int len;
        try {
            mMDigest = MessageDigest.getInstance("MD5");
            while ((len = fos.read(buffer, 0, 1024)) != -1) {
                mMDigest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger mBInteger = new BigInteger(1, mMDigest.digest());
        log.info("create_MD5=" + mBInteger.toString(16));
        return mBInteger.toString(16);
    }
}
