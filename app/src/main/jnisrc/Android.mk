LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_LDLIBS := -llog -lz

LOCAL_MODULE    := nt
LOCAL_SRC_FILES := nt.c \
                md5.c

include $(BUILD_SHARED_LIBRARY)