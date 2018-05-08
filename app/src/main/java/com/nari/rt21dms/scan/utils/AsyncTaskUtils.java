package com.nari.rt21dms.scan.utils;

import android.os.AsyncTask;

/**
 * Created by yinl01 on 2017/6/24.
 */

public class AsyncTaskUtils {

    public static <T> void doAsync(final IDataCallBack<T> callBack) {
        new AsyncTask<Void, Void, T>() {

            protected void onPreExecute() {
                callBack.onTaskBefore();
            }

            @Override
            protected T doInBackground(Void... params) {
                // TODO
                return callBack.onTasking(params);
            }

            protected void onPostExecute(T result) {
                if (callBack != null)
                    callBack.onTaskAfter(result);
            }

        }.execute();
    }

    public interface IDataCallBack<T> {
        /**
         * 任务执行之前
         */
        void onTaskBefore();

        /**
         * 任务执行中...
         */
        T onTasking(Void... params);

        /**
         * 任务执行之后
         */
        void onTaskAfter(T result);
    }
}