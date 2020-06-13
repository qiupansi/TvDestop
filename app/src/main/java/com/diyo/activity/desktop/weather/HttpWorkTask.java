package com.diyo.activity.desktop.weather;

import android.os.AsyncTask;

/**
 * 自定义异步处理类
 * @param <T>
 */
public class HttpWorkTask<T> extends AsyncTask<Void, Void, T> {
    private ParseCallBack<T> parseCallBack;
    private PostCallBack<T> postCallBack;

    public HttpWorkTask(ParseCallBack<T> parseCallBack,
                        PostCallBack<T> postCallBack) {
        super();
        this.parseCallBack = parseCallBack;
        this.postCallBack = postCallBack;
    }

    @Override
    protected T doInBackground(Void... params) {
        if (parseCallBack != null) {
            return parseCallBack.onParse();
        }
        return null;
    }

    @Override
    protected void onPostExecute(T result) {
        if (postCallBack != null) {
            postCallBack.onPost(result);
        }
    }

    public interface ParseCallBack<T> {
        public T onParse();
    }

    public interface PostCallBack<T> {
        public void onPost(T result);
    }

}
