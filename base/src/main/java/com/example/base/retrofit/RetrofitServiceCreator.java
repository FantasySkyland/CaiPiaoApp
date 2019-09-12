package com.example.base.retrofit;

import android.content.Context;
import android.os.Build;

import com.example.base.BuildConfig;
import com.example.base.util.LogUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/22 09:13
 * @describe ：
 */
public class RetrofitServiceCreator {
    public static final String API_BASE_URL = "https://www.cjcp.com.cn/";
    private static final String TAG = "RetrofitServiceCreator";
    public static String userAgent = null;
    public final static int CONNECT_TIMEOUT = 15;
    public final static int READ_TIMEOUT = 15;
    public final static int WRITE_TIMEOUT = 15;

    public static <S> S createServcie(final Context context, Class<S> serviceClass) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            clientBuilder
                    //.addInterceptor(new CommonInterceptor())
                    //.addInterceptor(new AddTokenInterceptor())
                    .addInterceptor(new PrintLogInterceptor());
                    //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
//        else {
//            clientBuilder.addInterceptor(new CommonInterceptor())
//                    .addInterceptor(new AddTokenInterceptor(context));
//        }

        clientBuilder.retryOnConnectionFailure(false);
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = clientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }
    public static <S> S createServcie(Class<S> serviceClass) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            clientBuilder
                    //.addInterceptor(new CommonInterceptor())
                    //.addInterceptor(new AddTokenInterceptor(context))
                    .addInterceptor(new PrintLogInterceptor());
            //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
//        else {
//            clientBuilder.addInterceptor(new CommonInterceptor())
//                    .addInterceptor(new AddTokenInterceptor(context));
//        }

        clientBuilder.retryOnConnectionFailure(false);
        clientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS).connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = clientBuilder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava 适配器
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }
    /**
     * 所有请求都需要带的参数，会影响统计
     *
     * @param builder
     */
    private static String addGenericHeaders(Request.Builder builder) {
        String uuid = UUID.randomUUID().toString();
        builder
                //.header("device-id", AppLogic.getInstance().getDeviceId())
                .header("app-num", "2")  //android app
                .header("app-version", BuildConfig.VERSION_NAME) //版本号
                .header("appLanguages", Locale.getDefault().getLanguage()) //系统语言
                .header("requestLanguages", Locale.getDefault().getLanguage()) //需要的语言
                .header("urid", uuid); //用于追踪调试

        if (null == userAgent) {
            String ua = String.format(Locale.ENGLISH,
                    "Enayeh/%s-%d (%s)(%s)/%s", BuildConfig.DEBUG ? "debug" : "release", BuildConfig.VERSION_CODE,
                    Build.BRAND, Build.MODEL, Build.VERSION.RELEASE);
            try{
                userAgent = URLEncoder.encode(ua, "utf-8");
            }catch (UnsupportedEncodingException ex){
                LogUtils.e(TAG, "illegal UA String" + ua);
                userAgent =  String.format(Locale.ENGLISH,
                        "Enayeh/%s-%d %s", BuildConfig.DEBUG ? "debug" : "release", BuildConfig.VERSION_CODE, Build.VERSION.RELEASE);
            }

        }
        builder.removeHeader("User-Agent").addHeader("User-Agent", userAgent);
        return uuid;
    }


    /**
     * Header 添加 token
     */
    public static class AddTokenInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = "11";
            if ("".equals(token)) {
                throw new UnsupportedEncodingException("uri: " + chain.request().url().toString() + ", Your auth token is null.");
            }
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("token", token+"");
            Request request = requestBuilder.build();
            Response resp = chain.proceed(request);
            return resp;
        }
    }


    /**
     * http 日志拦截器，打印日志
     */
    public static class PrintLogInterceptor implements Interceptor {



        @Override
        public Response intercept(Chain chain) throws IOException {
            String token = chain.request().header("token");
            String urid = chain.request().header("urid");
            String body = bodyToString(chain.request());
            String url = chain.request().url().toString();
            if (isJson(body)){
                printJson("ClientRequestBody",body,"request ,"+url);
            }
//            Log.d("ClientRequestBody", String.format(Locale.ENGLISH,
//                    " body: %s",
//                    body));
            Response resp = chain.proceed(chain.request());
//            LogUtils.d("ClientRequest", String.format(Locale.ENGLISH,
//                    "token: %s, device-id: %s, url: %s, uuid: %s, ",
//                    token, AppLogic.getInstance().getDeviceId(), url,
//                    urid ));
            LogUtils.d("ClientRequest", String.format(Locale.ENGLISH,
                    "token: %s, device-id: %s, url: %s, uuid: %s, ",
                    "", "", url,
                    ""));
            if (resp.isSuccessful() ){
                if (isJson(bodyToString(resp))){
                    printJson("ClientResponseBody",bodyToString(resp),"resp ,"+url);
                }
            }else {
                LogUtils.d("ClientRequestError", String.format(Locale.ENGLISH,
                        " httpError code %d",
                        resp.code()));
            }
            return resp;
        }
    }
    /**
     * http 通用设置拦截器 不打印日志
     */
    public static class CommonInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            long start = System.currentTimeMillis();
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .method(original.method(), original.body());
            addGenericHeaders(requestBuilder);
            Request request = requestBuilder.build();
            //
            Response resp = chain.proceed(request);
            String url = resp.request().url().toString();
            int httpCode = resp.code();
            long end = System.currentTimeMillis();
//            if (resp.isSuccessful()){
//                String content = bodyToString(resp);
//                JSONObject body = getJson(content);
//                if (body!=null && body.has("code")){
//                    try {
//                        int code = body.getInt("code");
//                        if (code != 0){
//                           // EventLoggerUtil.logNetWorkFailed(resp.request().url().toString(), String.valueOf(resp.code()), String.valueOf(code), null);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }else {
//            }

           // EventLoggerUtil.logNetWork(url,String.valueOf(httpCode),end-start);
            //
            return resp;
        }
    }
    private static JSONObject getJson(String content){
        try {
            return new JSONObject(content);
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isJson(String content){
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(content);
            return  jsonElement.isJsonObject();
        } catch (Exception e) {
            return false;
        }
    }
    private static String bodyToString(final Request request) {
        if (null != request.body()) {
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
                //buffer.readString(Charset.defaultCharset());
                return buffer.readString(Charset.defaultCharset());
            } catch (Exception ex) {
                return "not work";
            }
        } else {
            return "";
        }
    }

    private static String bodyToString(final Response resp) {
        if (null != resp.body()) {
            try {
                Buffer buffer = resp.body().source().buffer().clone();
                return buffer.readString(Charset.defaultCharset());
            } catch (Exception ex) {
                return "not work";
            }
        } else {
            return "";
        }
    }

    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            LogUtils.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            LogUtils.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        printLine(tag, true);
        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            LogUtils.d(tag, "║ " + line);
        }
        printLine(tag, false);

    }
}
