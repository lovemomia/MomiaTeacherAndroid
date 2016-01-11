package com.youxing.sogoteacher.app;

import com.youxing.common.app.MappingManager;
import com.youxing.common.app.YXApplication;

/**
 * Created by Jun Deng on 15/6/4.
 */
public class SGApplication extends YXApplication {

    @Override
    protected MappingManager mappingManager() {
        return new SGMappingManager();
    }
}
