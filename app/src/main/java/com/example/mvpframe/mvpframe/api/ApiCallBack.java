package com.example.mvpframe.mvpframe.api;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by libo on 2017/3/30.
 */

/**
 * 封装泛型实体类的观察者，统一处理结果
 */
public abstract class ApiCallBack<M> extends Subscriber<M>{
    public abstract void onSuccess(M model);

    public abstract void onFailure(String msg);

    public abstract void onFinish();

    @Override
    public void onCompleted() {
        onFinish();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "服务器异常，请稍后再试";
            }
            onFailure(msg);
        } else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);
    }
}
