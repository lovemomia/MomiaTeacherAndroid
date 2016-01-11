package com.youxing.common.services.http;

import com.youxing.common.model.BaseModel;

/**
 * Created by Jun Deng on 15/6/3.
 */
public interface RequestHandler {

    /**
     * 请求成功并返回<br>
     * response.error = null<br>
     *
     * @param response
     *            请求的返回值
     *
     */
    void onRequestFinish(Object response);

    /**
     * 请求成功并返回<br>
     * response.error为异常原因<br>
     *
     * @param error
     *            请求的错误信息
     *
     */
    void onRequestFailed(BaseModel error);
}
