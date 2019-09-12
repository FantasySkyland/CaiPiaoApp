package com.example.base.util.cacheUtil;

import android.util.Log;

import com.example.base.base.App;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * 文件缓存基类
 * 
 */
public class CacheFile {

  private final static String TAG = "CacheFile";
  /** 文件根目录 */
  public static final String ROOT_FILE_PATH = getAlbumStorageDir("CacheFile").getPath();
  /** 聊股缓存文件 **/
  public static final String CACHE_FILE_PATH = CacheFile.ROOT_FILE_PATH + "/cache";

  /**
   * 文件缓存的超时时间
   */
  long timeout;

  public CacheFile() {
    this(Long.MAX_VALUE);
  }

  public CacheFile(final long timeout) {
    this.timeout = timeout;
  }

  static DiskLruCache cache;

  /**
   * 获取文件缓存的工具类对象
   * 
   * @return DiskLruCache
   */
  public synchronized static DiskLruCache getCache() {
    if (CacheFile.cache == null) {
      try {
        // sdcard must be mounted
          CacheFile.cache =
              DiskLruCache.open(new File(CacheFile.CACHE_FILE_PATH), 1, 1, 1024 * 1024 * 8);
      } catch (Exception e) {
        Log.e(CacheFile.TAG, "", e);
      }
    }
    return CacheFile.cache;
  }

  /**
   * read cache from disk LRU cache by key
   * 
   * @param key String
   * @return String
   */
  public String readCache(final String key) {
    try {
      DiskLruCache cacheUtil = CacheFile.getCache();
      // check null
      if (cacheUtil == null) {
        return null;
      }

      DiskLruCache.Snapshot snapshot = cacheUtil.get(key);
      // if not timeout, return cache content

      if (snapshot != null && System.currentTimeMillis() - snapshot.getMtime(0) < timeout) {
        return snapshot.getString(0);
      }
    } catch (IOException e) {
      Log.e(CacheFile.TAG, "", e);
    }
    return null;
  }

  /**
   * save cache value to disk LRU cache by key
   * 
   * @param key String
   * @param value String
   */
  public static void saveCache(final String key, final String value) {

    try {
      DiskLruCache cacheUtil = CacheFile.getCache();
      // check null
      if (cacheUtil == null) {
        return;

      }
      // save content
      DiskLruCache.Editor edit = cacheUtil.edit(key);
      if(edit!=null){
        edit.set(0, value);
        edit.commit();
      }
    } catch (IOException e) {
      Log.e(CacheFile.TAG, "", e);
    }
  }

  public static void clearCache(final String key) {

    try {
      DiskLruCache cacheUtil = CacheFile.getCache();
      // check null
      if (cacheUtil == null) {
        return;
      }
      cacheUtil.remove(key);
    } catch (IOException e) {
      Log.e(CacheFile.TAG, "", e);
    }
  }



  /**
   * 检查文件目录，如果存在的话，创建该目录
   * 
   * @param path 文件目录路径
   * @return File
   */
  public static File checkDirExist(final String path) {
    File dir = new File(path);
    return CacheFile.checkDirExist(dir);
  }

  public static File checkDirExist(final File dir) {
    if (!dir.exists() && dir.mkdirs()) {
      Log.d(CacheFile.TAG, "failed to access dir: " + dir.getAbsolutePath());
    }
    return dir;
  }

  /**
   * 读取缓存对象
   * 
   * @param key String
   * @param cls Class<T>
   * @return T
   */
  public <T> T getCacheClass(final String key, final Class<T> cls) {
	  String jsonStr = null;
    try {
      // create real key by userSpecial
      String realKey =  CacheFile.getCommonKey(key, cls);
      // read cache content
      jsonStr = readCache(md5Encode(realKey));
      if (jsonStr != null) {
        // json to T
        Gson gson = new Gson();
        T info = gson.fromJson(jsonStr, cls);
        return info;
      }
    } catch (Exception e) {
      Log.e(CacheFile.TAG, "key: " + key+"-jsonStr:"+jsonStr, e);
    }

    return null;
  }
  /**
   * 读取缓存对象
   *
   * @param key String
   * @param cls Class<T>
   * @return T
   */
  public <T> List<T> getCacheClassList(final String key, final Class<T> cls) {
    String jsonStr = null;
    try {
      // create real key by userSpecial
      Type type = new ParameterizedTypeImpl(cls);

      String realKey =  key;
      // read cache content
      jsonStr = readCache(md5Encode(realKey));
      if (jsonStr != null) {
        // json to T
        Gson gson = new Gson();
        List<T> info  =  gson.fromJson(jsonStr, type);
        return info;
      }
    } catch (Exception e) {
      Log.e(CacheFile.TAG, "key: " + key+"-jsonStr:"+jsonStr, e);
    }

    return null;
  }

  private  class ParameterizedTypeImpl implements ParameterizedType {
    Class clazz;

    public ParameterizedTypeImpl(Class clz) {
      clazz = clz;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[]{clazz};
    }

    @Override
    public Type getRawType() {
      return List.class;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }
  }
  /**
   * 数据对象保存
   *
   * @param info
   */
  public static <T> void saveClass(final String key, final T info
  ) {
    new Thread(new Runnable() {

      @Override
      public void run() {
        // 非UI线程保存缓存
        try {
          Class<?> cls = info.getClass();
          // create real key by userSpecial
          String realKey = CacheFile.getRealKey(key, cls );
          // create json content
          Gson gson = new Gson();
          String jsonStr = gson.toJson(info);
          Log.e("CacheFileSave: ", jsonStr);
          // save to cache file

          CacheFile.saveCache(md5Encode(realKey), jsonStr);
        } catch (Exception e) {
          Log.e(CacheFile.TAG, "key: " + key, e);
        } catch (Error e) {
          Log.e(CacheFile.TAG, "key: " + key, e);

        }
      }
    }).start();

  }
  /**
   * 数据对象保存
   *
   * @param info
   */
  public static <T> void saveClassList(final String key, final List<T> info) {
    new Thread(new Runnable() {

      @Override
      public void run() {
        // 非UI线程保存缓存
        try {
          // create real key by userSpecial

          String realKey = key ;
          // create json content
          Gson gson = new Gson();
          String jsonStr = gson.toJson(info);
          Log.e("CacheFileSave: ", jsonStr);
          // save to cache file

          CacheFile.saveCache(md5Encode(realKey), jsonStr);
        } catch (Exception e) {
          Log.e(CacheFile.TAG, "key: " + key, e);
        } catch (Error e) {
          Log.e(CacheFile.TAG, "key: " + key, e);

        }
      }
    }).start();

  }

  /**
   * 数据对象保存<br>
   * 
   * 
   * @param key String
   * @param cls Class
   * @param jsonText String
   */
  public static <T > void saveText(final String key,
                                   final Class<? > cls, final String jsonText) {
    try {
      // create real key by userSpecial
      String realKey = CacheFile.getRealKey(key, cls);

      // save to cache file
      CacheFile.saveCache(md5Encode(realKey), jsonText);
    } catch (Exception e) {
      Log.e(CacheFile.TAG, "key: " + key, e);

    }
  }

  /**
   * 返回由key, cls userSpecial三个属性生成复合的key，使用该key保存对象；
   *
   * @param key String
   * @param cls Class
   * @return String
   */
  public static String getRealKey(final String key, final Class<?> cls
   ) {
    return  CacheFile.getCommonKey(key, cls);
  }

  /**
   * 获取被多个用户共享的key
   *
   * @param key String
   * @param cls Class<T>
   * @return String
   */
  public static <T > String getCommonKey(final String key, final Class<T> cls) {
    return key + cls.getSimpleName();
  }



  /**
   * @param albumName 文件夹名字
   * @return
   */
  public static File getAlbumStorageDir(String albumName) {
    // Get the directory for the user's public pictures directory.
    File file = new File(App.getInstant().getCacheDir(), albumName);
    if (!file.mkdirs()) {
      Log.e("file", "Directory not created");
    }
    return file;
  }

  /**
   * 32位MD5加密
   * @param content -- 待加密内容
   * @return
   */
  public static String md5Encode(String content) {
    byte[] hash;
    try {
      hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("NoSuchAlgorithmException",e);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UnsupportedEncodingException", e);
    }
    //对生成的16字节数组进行补零操作
    StringBuilder hex = new StringBuilder(hash.length * 2);
    for (byte b : hash) {
      if ((b & 0xFF) < 0x10){
        hex.append("0");
      }
      hex.append(Integer.toHexString(b & 0xFF));
    }
    return hex.toString();
  }
}
