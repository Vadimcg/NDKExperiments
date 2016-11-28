#include <jni.h>
#include <string>
#include <android/log.h>
#include <stdio.h>

extern "C" {

jstring Java_com_group_improve_ndkexperiments_MainActivity_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


JNIEXPORT jboolean JNICALL Java_com_group_improve_ndkexperiments_MainActivity_passBooleanReturnBoolean(JNIEnv *env, jobject,jboolean value){

    __android_log_print(ANDROID_LOG_INFO, "native", "%d in %dbytes  bool", value, sizeof(jboolean));
    return (!value);
}

JNIEXPORT jint JNICALL Java_com_group_improve_ndkexperiments_MainActivity_passIntReturnint(JNIEnv *env, jobject,jint value){

    __android_log_print(ANDROID_LOG_INFO, "native", "%d in %dbytes  int", value, sizeof(jint));
    return ++value;
}


JNIEXPORT jdouble JNICALL Java_com_group_improve_ndkexperiments_MainActivity_passDoubleReturnDouble(JNIEnv *env, jobject,jdouble value){

    __android_log_print(ANDROID_LOG_INFO, "native", "%d in %dbytes double", value, sizeof(jdouble));
    return value+0.5;
}


JNIEXPORT jbyte JNICALL Java_com_group_improve_ndkexperiments_MainActivity_passByteReturnByte(JNIEnv *env, jobject,jbyte value){

    __android_log_print(ANDROID_LOG_INFO, "native", "%d in %dbytes byte", value, sizeof(jbyte));
    return ++value;
}

}



