package com.makfc.xposed_appledaily_subscription_bypass

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers.callMethod
import de.robv.android.xposed.XposedHelpers.findAndHookMethod
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class Main : IXposedHookLoadPackage {
    fun log(text: String) {
        XposedBridge.log("${BuildConfig.APPLICATION_ID}: $text")
    }

    override fun handleLoadPackage(lpparam: LoadPackageParam) {
        when (lpparam.packageName) {
            "com.nextmedia" -> {
                log("Loaded app: " + lpparam.packageName)
                val methodHookReturnFalse = object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        log("beforeHookedMethod: ${param.method}")
                        param.result = false
                    }
                }

                // Subscription Bypass
                findAndHookMethod(
                    "omo.redsteedstudios.sdk.internal.OMOAppSettings", lpparam.classLoader,
                    "isSubscriptionEnabled", methodHookReturnFalse
                )

                // Disable AD
                findAndHookMethod(
                    "com.nextmedia.network.model.motherlode.article.ArticleListModel", lpparam.classLoader,
                    "isEnableAD", methodHookReturnFalse
                )

                // Make the splash screen load faster
                findAndHookMethod(
                    "com.nextmedia.activity.SplashScreenActivity", lpparam.classLoader,
                    "requestAdTags", object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            log("beforeHookedMethod: ${param.method}")
                            log("callMethod: c")
//                            /* renamed from: c */
//                            public final void mo43672c() {
//                                if (!isFinishing()) {
//                                    setRequestedOrientation(5);
//                                    this.f11952c.setVisibility(8);
//                                    checkAppUpdate();
//                                    checkDeprecatingOS();
//                                }
//                            }
                            callMethod(param.thisObject, "c")
                            param.result = null
                        }
                    }
                )

                // Make the splash screen load faster
                findAndHookMethod(
                    "com.nextmedia.activity.SplashScreenActivity", lpparam.classLoader,
                    "requestSplashAd", object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            log("beforeHookedMethod: ${param.method}")
                            log("callMethod: startMainActivity")
                            callMethod(param.thisObject, "startMainActivity")
                            param.result = null
                        }
                    }
                )

                // Make the TextView can Process Text
                findAndHookMethod(
                    "android.widget.TextView", lpparam.classLoader,
                    "canProcessText", object : XC_MethodHook() {
                        override fun beforeHookedMethod(param: MethodHookParam) {
                            log("beforeHookedMethod: ${param.method}")
                            param.result = true
                        }
                    }
                )
            }
        }
    }
}