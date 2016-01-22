package com.youxing.sogoteacher.chat.provider;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.widget.provider.TextMessageItemProvider;
import io.rong.message.TextMessage;

/**
 * Created by Jun Deng on 16/1/22.
 */
@ProviderTag( messageContent = TextMessage.class , showSummaryWithName = false)
public class MyTextMessageItemProvider extends TextMessageItemProvider {
}
