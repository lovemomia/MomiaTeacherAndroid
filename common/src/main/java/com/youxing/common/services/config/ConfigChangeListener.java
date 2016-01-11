package com.youxing.common.services.config;


/**
 * 配置项更新Listener
 * <p>
 * 如需关注某个配置项的变化，需要创建Listener
 */
public interface ConfigChangeListener {

	/**
	 * 名字为 key 的配置项变化时被调用
	 * 
	 * @param from 变化前的值，需要调用者通过
	 * @param to 变化后的值，需要调用者通过
	 */
	void onConfigChange(String key, Object from, Object to);

}