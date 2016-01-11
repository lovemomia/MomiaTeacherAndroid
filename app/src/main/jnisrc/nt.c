#include<stdio.h>
#include <jni.h>
#include <md5.h>
#include <nt.h>
#include <string.h>
#include <android/log.h>

#define  LOG_TAG    "nt"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)


//#define  SYS_TOKEN  "5ae5e94a429e0c1f4010bccce4073844"
#define  SYS_TOKEN  "578890d82212ae548d883bc7a201cdf4"
#define  SYS_TOKEN_MOVIE  "3e25960a79dbc69b674cd4ec67a72c62"
#define  SYS_TOKEN_HOTEL  ""

/*
 * Class:     com_youxing_common_utils_NativeTool
 * Method:    a
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_youxing_common_utils_NativeTool_a(
		JNIEnv * env, jclass clazz) {
	return JNI_TRUE;
}

char* Jstring2CStr(JNIEnv* env, jbyteArray barr) {
	char* rtn = NULL;
	jsize alen = (*env)->GetArrayLength(env, barr);
	jbyte* ba = (*env)->GetByteArrayElements(env, barr, JNI_FALSE);
	if (alen > 0) {
		rtn = (char*) malloc(alen + 1);
		memcpy(rtn, ba, alen);
		rtn[alen] = 0;
	}
	(*env)->ReleaseByteArrayElements(env, barr, ba, 0);
	return rtn;
}

/*
 * Class:     com_youxing_common_utils_NativeTool
 * Method:    ne
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_youxing_common_utils_NativeTool_ne(JNIEnv* env,
		jclass clazz, jbyteArray jInfo, jint customer) {
	char* cstr = Jstring2CStr(env, jInfo);
	if (cstr == NULL) {
		LOGI("native encrypt failed");
		return NULL;
	}
	char* token = SYS_TOKEN;
	if (customer == 1) {
	    token = SYS_TOKEN_MOVIE;
	} else if (customer == 2) {
     	token = SYS_TOKEN_HOTEL;
    }
	int slength = strlen(cstr);
	int tlength = strlen(token);
	char* fcstr = (char*) malloc(slength + tlength + 1);
	fcstr[slength + tlength] = 0;
	sprintf(fcstr, "%s%s", cstr, token);
	free(cstr);

//	LOGI("%s", fcstr);

	MD5_CTX context = { 0 };
	MD5Init(&context);
	MD5Update(&context, fcstr, strlen(fcstr));
	unsigned char dest[16] = { 0 };
	MD5Final(dest, &context);

	int i;
	char destination[32] = { 0 };
	for (i = 0; i < 16; i++) {
		sprintf(destination, "%s%02x", destination, dest[i]);
	}
//	LOGI("%s", destination);
	free(fcstr);

	jthrowable exc = (*env)->ExceptionOccurred(env);
	if(exc) {
	      (*env)->ExceptionClear(env);
	      return NULL;
	}

	return (*env)->NewStringUTF(env, destination);
}
