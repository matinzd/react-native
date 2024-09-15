/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react.osslibraryexample

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.fbreact.specs.NativeSampleModuleSpec
import com.facebook.react.ReactActivity
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule


@ReactModule(name = NativeSampleModuleSpec.NAME)
public class NativeSampleModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

   private var pendingPromise: Promise? = null

  private var permissionLauncher = reactContext.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
    if (isGranted) {
      Log.d("NativeSampleModule", "Permission granted")
      pendingPromise?.resolve(true)
    } else {
      Log.d("NativeSampleModule", "Permission denied")
      pendingPromise?.reject("PERMISSION_DENIED", "Permission denied")
    }
  }

  override fun getName(): String = NAME

  private companion object {
    const val NAME = "NativeSampleModule"
  }

  @ReactMethod
  public fun requestSamplePermission(promise: Promise) {
    permissionLauncher.launch(android.Manifest.permission.CAMERA)
    pendingPromise = promise
  }

  @ReactMethod
  public fun getRandomNumber(): Int {
    return (0..99).random()
  }
}
