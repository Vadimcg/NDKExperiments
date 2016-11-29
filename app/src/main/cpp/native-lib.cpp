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



JNIEXPORT jstring JNICALL Java_com_group_improve_ndkexperiments_MainActivity_passStringReturnString(JNIEnv *env, jobject pObj, jstring value){
    __android_log_print(ANDROID_LOG_INFO, "native", "print jstring: %s", value);

    const char *str;
    jboolean *isCopy;

    str = env->GetStringUTFChars(value, isCopy);

    __android_log_print(ANDROID_LOG_INFO, "native",
                        "print UTF-8string: %s, %d", str, isCopy);

    jsize length = env->GetStringUTFLength(value);

    __android_log_print(ANDROID_LOG_INFO, "native",
                        "UTF-8 string length (number of bytes): %d == %d", length, strlen(str));

    __android_log_print(ANDROID_LOG_INFO, "native",
                        "UTF-8 string ends with: %d %d", str[length], str[length+1]);

    env->ReleaseStringUTFChars(value, str);

    char nativeStr[100];

    env->GetStringUTFRegion(value, 0, length, nativeStr);
    __android_log_print(ANDROID_LOG_INFO, "native",
                        "jstring converted to UTF-8 string and copied to native buffer: %s", nativeStr);

    const char* newStr = "hello 安卓 Привет";
    jstring ret = env->NewStringUTF(newStr);
    jsize newStrLen = env->GetStringUTFLength(ret);

    __android_log_print(ANDROID_LOG_INFO, "native",
                        "UTF-8 string with Chinese and Russian characters: %s, string length (number of bytes) %d=%d",
                        newStr, newStrLen, strlen(newStr));

    return ret;
}




}



