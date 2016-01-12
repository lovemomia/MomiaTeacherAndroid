package com.youxing.sogoteacher.app;

import com.youxing.common.app.MappingManager;

/**
 * Created by Jun Deng on 15/8/10.
 */
public class SGMappingManager extends MappingManager {

    public SGMappingManager() {
        // 个人信息
        putPage("personinfo", new MappingPage("personinfo", true));

        // 成为助教
        putPage("applyteacher", new MappingPage("applyteacher", true));

        // 添加工作经历
        putPage("editexp", new MappingPage("editexp", true));

        // 添加教育经历
        putPage("editedu", new MappingPage("editedu", true));
    }

}
