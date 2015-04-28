#include <jni.h>
#include <jni.h>
#include <stdio.h>

extern "C" {
jstring Java_com_doublechen_androidtest_MainActivity_hello(
		JNIEnv* env, jobject thiz);
}

jstring Java_com_doublechen_androidtest_MainActivity_hello(
		JNIEnv* env, jobject thiz) {
	return env->NewStringUTF("hello world");
}
