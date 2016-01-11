package com.youxing.common.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jun Deng on 15/8/7.
 */
public class MappingManager {

    public Map<String, MappingPage> map = new HashMap<String, MappingPage>();

    public void putPage(String host, MappingPage page) {
        map.put(host, page);
    }

    public MappingPage getPage(String host) {
        return map.get(host);
    }

    public static class MappingPage {
        public String host;
        public boolean needLogin;

        public MappingPage(String host, boolean needLogin) {
            this.host = host;
            this.needLogin = needLogin;
        }
    }

}
