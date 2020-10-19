package io.qmeta.http.adaptor;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * default retrofit caller adaptor
 * 1. Return Response
 * 2. Return Raw Response with http raw information
 * 3. Execute with Callback
 */
public interface RetrofitCallAdaptor {
    /**
     * 1. caller execute call
     *
     * @param caller
     * @param <T>
     * @return
     */
    default <T> T execute(Call<T> caller) {
        try {
            Response<T> response = caller.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            ResponseBody errorBody = response.errorBody();
            throw new RemoteServiceCallException(errorBody == null ? "network connection failed!" : errorBody.toString());
        } catch (Exception e) {
            throw new RemoteServiceCallException(e, "network issue faced, please check the network ");
        }
    }

    default <T> Response<T> executeAndReturnRawResponse(Call<T> caller) {
        try {
            Response<T> response = caller.execute();
            if (response.isSuccessful()) {
                return response;
            }
            ResponseBody errorBody = response.errorBody();
            throw new RemoteServiceCallException(errorBody == null ? "network connection failed!" : errorBody.toString());
        } catch (Exception e) {
            throw new RemoteServiceCallException(e, "network issue faced, please check the network ");
        }
    }

    default <T> void executeWithCallBack(Call<T> caller, Callback<T> callback) {
        caller.enqueue(callback);
    }
}
