package com.xj.webview.sample.x5

import android.content.Context
import android.util.Log
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import com.tencent.smtt.sdk.TbsListener

object X5Helper {
    private const val TAG = "X5Helper"

    fun init(context: Context) {
        /* 设置允许移动网络下进行内核下载。默认不下载，会导致部分一直用移动网络的用户无法使用x5内核 */

        /* 设置允许移动网络下进行内核下载。默认不下载，会导致部分一直用移动网络的用户无法使用x5内核 */QbSdk.setDownloadWithoutWifi(true)


        QbSdk.setCoreMinVersion(QbSdk.CORE_VER_ENABLE_202112)
        /* SDK内核初始化周期回调，包括 下载、安装、加载 */

        /* SDK内核初始化周期回调，包括 下载、安装、加载 */QbSdk.setTbsListener(object : TbsListener {
            /**
             * @param stateCode 用户可处理错误码请参考[com.tencent.smtt.sdk.TbsCommonCode]
             */
            override fun onDownloadFinish(stateCode: Int) {
                Log.i(
                    TAG,
                    "onDownloadFinished: $stateCode"
                )
            }

            /**
             * @param stateCode 用户可处理错误码请参考[com.tencent.smtt.sdk.TbsCommonCode]
             */
            override fun onInstallFinish(stateCode: Int) {
                Log.i(
                    TAG,
                    "onInstallFinished: $stateCode"
                )
            }

            /**
             * 首次安装应用，会触发内核下载，此时会有内核下载的进度回调。
             * @param progress 0 - 100
             */
            override fun onDownloadProgress(progress: Int) {
                Log.i(TAG, "Core Downloading: $progress")
            }
        })

        /* 此过程包括X5内核的下载、预初始化，接入方不需要接管处理x5的初始化流程，希望无感接入 */

        /* 此过程包括X5内核的下载、预初始化，接入方不需要接管处理x5的初始化流程，希望无感接入 */QbSdk.initX5Environment(
            context.applicationContext,
            object : PreInitCallback {
                override fun onCoreInitFinished() {
                    // 内核初始化完成，可能为系统内核，也可能为系统内核
                }

                /**
                 * 预初始化结束
                 * 由于X5内核体积较大，需要依赖wifi网络下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
                 * 内核下发请求发起有24小时间隔，卸载重装、调整系统时间24小时后都可重置
                 * 调试阶段建议通过 WebView 访问 debugtbs.qq.com -> 安装线上内核 解决
                 * @param isX5 是否使用X5内核
                 */
                override fun onViewInitFinished(isX5: Boolean) {
                    Log.i(TAG, "onViewInitFinished: $isX5")
                    // hint: you can use QbSdk.getX5CoreLoadHelp(context) anytime to get help.
                }
            })
    }
}