package com.youxing.sogoteacher.app;

import com.youxing.common.app.MappingManager;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class SGMappingManager extends MappingManager {

    public SGMappingManager() {
        // 个人信息
        putPage("personinfo", new MappingPage("personinfo", true));

    }

}
