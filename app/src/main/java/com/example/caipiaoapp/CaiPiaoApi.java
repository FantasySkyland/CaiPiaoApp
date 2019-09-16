package com.example.caipiaoapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zdy On 2019/9/14.
 */
public interface CaiPiaoApi {
    @GET("lottery/history")
    Call<HistoryBean> getHistory(@Query("key") String key,
                                 @Query("lottery_id") String lottery_id
            , @Query("page") int page, @Query("page_size") int page_size);
}
