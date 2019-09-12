package com.example.base.retrofit;

import com.example.base.rx.BaseResponse;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/22 09:23
 * @describe ：
 */
public interface ApiService {

    @GET("ajax_kj.php")
    Call<ResponseBody> getHistory(@Query("jsoncallback") String jsoncallback, @Query("dlt_qs") String number);
}
