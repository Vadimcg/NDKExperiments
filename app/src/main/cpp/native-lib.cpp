#include <jni.h>
#include <string>
#include <android/log.h>
#include <stdio.h>

extern "C" {
jstring
Java_com_group_improve_ndkexperiments_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jint Java_com_group_improve_ndkexperiments_MainActivity_NativeAddition(JNIEnv *pEnv, jobject pObj, jint pa, jint pb)
{
    return pa+pb;
}

jint Java_com_group_improve_ndkexperiments_MainActivity_NativeMultiplication(JNIEnv *pEnv, jobject pObj, jint pa,
                                                                             jint pb) {
    return pa*pb;
}

}


/*
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* pVm, void* reserved)
{
    JNIEnv* env;
    if ((*pVm)->GetEnv(pVm, (void **)&env, JNI_VERSION_1_6)) {
        return -1;
    }

    JNINativeMethod nm[2];
    nm[0].name = "NativeAddition";
    nm[0].signature = "(II)I";
    nm[0].fnPtr = NativeAddition;
    nm[1].name = "NativeMultiplication";
    nm[1].signature = "(II)I";
    nm[1].fnPtr = NativeMultiplication;
    jclass cls = (*env)->FindClass(env, "MainActivity");
            (*env)->RegisterNatives(env, cls, nm, 2);
    return JNI_VERSION_1_6;
}
*/
