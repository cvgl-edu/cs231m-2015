LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

ifeq (1, $(NDK_DEBUG))
LOCAL_CFLAGS += -DWAIT_FOR_DEBUGGER
endif

LOCAL_MODULE    := HelloAndroid
LOCAL_SRC_FILES := HelloAndroid.cpp

include $(BUILD_SHARED_LIBRARY)
