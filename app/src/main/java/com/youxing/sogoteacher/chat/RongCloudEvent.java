package com.youxing.sogoteacher.chat;

import android.content.Context;
import android.net.Uri;

import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.sogoteacher.model.IMGroup;
import com.youxing.sogoteacher.model.IMGroupModel;
import com.youxing.sogoteacher.model.IMUserModel;
import com.youxing.sogoteacher.model.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

/**
 * 融云SDK事件监听
 * <p/>
 * Created by Jun Deng on 16/1/22.
 */
public class RongCloudEvent implements RongIM.UserInfoProvider, RongIM.GroupInfoProvider {

    private static RongCloudEvent instance;

    public static RongCloudEvent instance() {
        if (instance == null) {
            synchronized (RongCloudEvent.class) {
                if (instance == null) {
                    instance = new RongCloudEvent();
                }
            }
        }
        return instance;
    }

    public static void init() {
        if (instance == null) {
            synchronized (RongCloudEvent.class) {
                if (instance == null) {
                    instance = new RongCloudEvent();
                }
            }
        }
    }

    public RongCloudEvent() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
    }

    private Map<String, User> userCache = new HashMap<>();
    private Map<String, IMGroup> groupCache = new HashMap<>();

    public Map<String, User> getUserCache() {
        return userCache;
    }

    public Map<String, IMGroup> getGroupCache() {
        return groupCache;
    }

    @Override

    public Group getGroupInfo(final String groupId) {
        IMGroup group = groupCache.get(groupId);
        if (group != null) {
            Group rcGroup = new Group(String.valueOf(group.getGroupId()), group.getGroupName(), Uri.parse(""));
            return rcGroup;

        } else {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", groupId));
            HttpService.get(Constants.domain() + "/im/group", params, CacheType.DISABLE, IMGroupModel.class, new RequestHandler() {
                @Override
                public void onRequestFinish(Object response) {
                    IMGroupModel model = (IMGroupModel) response;

                    Group rcGroup = new Group(String.valueOf(model.getData().getGroupId()), model.getData().getGroupName(), Uri.parse(""));
                    RongIM.getInstance().refreshGroupInfoCache(rcGroup);

                    groupCache.put(groupId, model.getData());
                }

                @Override
                public void onRequestFailed(BaseModel error) {
                }
            });
        }
        return null;
    }

    @Override
    public UserInfo getUserInfo(final String userId) {
        User user = userCache.get(userId);
        if (user != null) {
            UserInfo rcUser = new UserInfo(String.valueOf(user.getId()), user.getNickName(), Uri.parse(user.getAvatar()));
            return rcUser;

        } else {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("uid", userId));
            HttpService.get(Constants.domain() + "/im/user", params, CacheType.DISABLE, IMUserModel.class, new RequestHandler() {
                @Override
                public void onRequestFinish(Object response) {
                    IMUserModel model = (IMUserModel) response;

                    UserInfo rcUser = new UserInfo(String.valueOf(model.getData().getId()), model.getData().getNickName(), Uri.parse(model.getData().getAvatar()));
                    RongIM.getInstance().refreshUserInfoCache(rcUser);

                    userCache.put(userId, model.getData());
                }

                @Override
                public void onRequestFailed(BaseModel error) {
                }
            });
        }
        return null;
    }
}
