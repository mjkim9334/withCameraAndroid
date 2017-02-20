LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#opencv
OPENCVROOT:= C:\Users\User\Desktop\OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED

include ${OPENCVROOT}\sdk\native\jni\OpenCV.mk

LOCAL_SRC_FILES := main.cpp
LOCAL_LDLIBS += -llog
LOCAL_MODULE := loadimage


include $(BUILD_SHARED_LIBRARY)
include $(CLEAR_VARS)




include $(CLEAR_VARS)

#opencv
OPENCVROOT:= C:\Users\User\Desktop\OpenCV-android-sdk
OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED

include ${OPENCVROOT}\sdk\native\jni\OpenCV.mk

LOCAL_SRC_FILES := main.cpp
LOCAL_LDLIBS += -llog
LOCAL_MODULE := imageprocessing


include $(BUILD_SHARED_LIBRARY)
include $(CLEAR_VARS)