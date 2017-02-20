#include <jni.h>
#include <opencv2/opencv.hpp>
#include <android/asset_manager_jni.h>
#include<com_kt_iot_mobile_ui_activity_OpencvActivity.h>
#include <android/log.h>

using namespace cv;
using namespace std;

extern "C" {


JNIEXPORT void JNICALL
Java_com_kt_iot_mobile_ui_activity_OpencvActivity_loadImage(
        JNIEnv * env , jobject , jstring imageFileName, jlong
addrImage ) {
Mat &img_input = *(Mat *) addrImage;

const char *nativeFileNameString = env->GetStringUTFChars(imageFileName, JNI_FALSE);

string baseDir("/storage/emulated/0/");
baseDir.append(nativeFileNameString);
const char *pathDir = baseDir.c_str();

img_input = imread(pathDir, IMREAD_COLOR);

}

JNIEXPORT void JNICALL
Java_com_kt_iot_mobile_ui_activity_OpencvActivity_imageprocessing( JNIEnv *env, jobject, jlong addrInputImage, jlong addrOutputImage) {

Mat &img_input = *(Mat *) addrInputImage;
Mat &img_output = *(Mat *) addrOutputImage;

cvtColor( img_input, img_input, CV_BGR2RGB);
cvtColor( img_input, img_output, CV_RGB2GRAY);
blur( img_output, img_output, Size(5, 5));
Canny( img_output, img_output, 50, 150, 5 );
}

}


