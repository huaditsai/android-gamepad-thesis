LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS := -llog
LOCAL_MODULE    := AndroidGamePad
LOCAL_SRC_FILES := AndroidGamePad.c

include $(BUILD_SHARED_LIBRARY)
