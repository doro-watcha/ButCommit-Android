package com.goddoro.butcommit.utils

//import android.app.Activity
//import android.content.Intent
//import android.hardware.display.DisplayManager
//import android.hardware.display.VirtualDisplay
//import android.media.projection.MediaProjection
//import android.media.projection.MediaProjectionManager
//import android.util.Log
//import android.view.Surface
//import android.view.SurfaceView
//import androidx.core.app.ActivityCompat.startActivityForResult
//
//
//class ScreenShotUtil(val activity: Activity){
//
//    private val TAG = ScreenShotUtil::class.java.simpleName
//
//    private val STATE_RESULT_CODE = "result_code"
//    private val STATE_RESULT_DATA = "result_data"
//
//    private val REQUEST_MEDIA_PROJECTION = 1
//
//    private val mScreenDensity = 0
//    private val mResultData: Intent? = null
//    private val mResultCode = 0
//    private val mSurface: Surface? = null
//    private var mMediaProjection: MediaProjection? = null
//    private var mVirtualDisplay: VirtualDisplay? = null
//    private val mSurfaceView: SurfaceView? = null
//    private val mMediaProjectionManager: MediaProjectionManager? = null
//
//
//    private fun setUpMediaProjection() {
//        mMediaProjection = mMediaProjectionManager!!.getMediaProjection(mResultCode, mResultData)
//    }
//
//    private fun tearDownMediaProjection() {
//        if (mMediaProjection != null) {
//            mMediaProjection.stop()
//            mMediaProjection = null
//        }
//    }
//
//    private fun startScreenCapture() {
//        if (mSurface == null ||) {
//            return
//        }
//        if (mMediaProjection != null) {
//            setUpVirtualDisplay()
//        } else if (mResultCode !== 0 && mResultData != null) {
//            setUpMediaProjection()
//            setUpVirtualDisplay()
//        } else {
//            Log.i(TAG, "Requesting confirmation")
//            // This initiates a prompt dialog for the user to confirm screen projection.
//            startActivityForResult(
//                mMediaProjectionManager!!.createScreenCaptureIntent(),
//                REQUEST_MEDIA_PROJECTION
//            )
//        }
//    }
//
//    private fun setUpVirtualDisplay() {
//
//        mVirtualDisplay = mMediaProjection!!.createVirtualDisplay(
//            "ScreenCapture",
//            mSurfaceView.width, mSurfaceView.height, mScreenDensity,
//            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//            mSurface, null, null
//        )
//
//    }
//
//    private fun stopScreenCapture() {
//        if (mVirtualDisplay == null) {
//            return
//        }
//        mVirtualDisplay.release()
//        mVirtualDisplay = null
//    }
//}