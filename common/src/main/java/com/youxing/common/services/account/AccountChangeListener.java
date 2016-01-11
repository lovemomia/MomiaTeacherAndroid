package com.youxing.common.services.account;

/**
 * Created by Jun Deng on 15/8/10.
 */
public interface AccountChangeListener {

    /**
     * 账户发生变化时触发（登录、退出、账户变更）
     *
     * @param service
     */
    void onAccountChange(AccountService service);

}
