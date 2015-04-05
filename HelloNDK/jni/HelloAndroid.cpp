#include <jni.h>


#if defined(WAIT_FOR_DEBUGGER)

void waitForDebugger()
{
	static volatile int debug = 1;
	while( debug )
	{}
}

#else

void waitForDebugger() {}

#endif;

#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     edu_stanford_cs231m_helloandroid_HelloAndroidActivity
 * Method:    square
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_edu_stanford_cs231m_helloandroid_HelloAndroidActivity_square
  (JNIEnv *jni, jobject thiz, jint n)
{
	return n * n;
}

#ifdef __cplusplus
}
#endif
