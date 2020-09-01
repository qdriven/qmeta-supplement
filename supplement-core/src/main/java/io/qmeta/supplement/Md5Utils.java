package io.qmeta.supplement;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Md5Utils {
  //  public static byte[] md5_2(String s) {
  //    MessageDigest algorithm;
  //    try {
  //      algorithm = MessageDigest.getInstance("MD5");
  //      algorithm.reset();
  //      algorithm.update(s.getBytes("UTF-8"));
  //      byte[] messageDigest = algorithm.digest();
  //      return messageDigest;
  //    } catch (Exception e) {
  //      log.error("MD5 Error...", e);
  //    }
  //    return null;
  //  }

  public static byte[] md5(String s) {

    return MD5.create().digest(s);
  }

  private static final String toHex(byte hash[]) {
    if (hash == null) {
      return null;
    }
    StringBuffer buf = new StringBuffer(hash.length * 2);
    int i;

    for (i = 0; i < hash.length; i++) {
      if ((hash[i] & 0xff) < 0x10) {
        buf.append("0");
      }
      buf.append(Long.toString(hash[i] & 0xff, 16));
    }
    return buf.toString();
  }

  public static String hash(String s) {
    try {
      return new String(toHex(md5(s)).getBytes("UTF-8"), "UTF-8");
    } catch (Exception e) {
      log.error("not supported charset...{}", e);
      return s;
    }
  }
}
