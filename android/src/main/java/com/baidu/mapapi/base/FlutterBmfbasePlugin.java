package com.baidu.mapapi.base;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapapi.common.BaiduMapSDKException;

/** FlutterBmfbasePlugin */
public class FlutterBmfbasePlugin implements FlutterPlugin, MethodCallHandler {

  private static final String METHOD_SET_API_KEY = "flutter_bmfbase/sdk/setApiKey";
  private static final String METHOD_GET_NATIVE_SDK_VERSION = "flutter_bmfbase/sdk/getNativeBaseVersion";
  private static final String METHOD_SET_PRIVACY_API_KEY = "flutter_bmfbase/sdk/setAgreePrivacy";

  private static final String METHOD_GET_ANDROID_VERSION = "flutter_bmfbase/sdk/getAndroidSdkVersion";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_bmfbase");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals(METHOD_GET_NATIVE_SDK_VERSION)) {
      Map<String, String> versionMap = new HashMap<>();
      versionMap.put("version", VersionInfo.getApiVersion());
      versionMap.put("platform", "Android");
      result.success(versionMap);
    } else if(call.method.equals(METHOD_SET_API_KEY)){
      if(call.hasArgument("BMF_COORD_TYPE")){
        int nCoordType = call.argument("BMF_COORD_TYPE");

        /**
         * flutter端的CoordType比android多了一个WGS-84，
         * 因此获取到的nCoordType要减1才能与android端匹配
         */
        nCoordType = nCoordType - 1;

        if(nCoordType >= 0 && CoordType.values().length > nCoordType){
          CoordType coordType = CoordType.values()[nCoordType];
          SDKInitializer.setCoordType(coordType);
        }

      }
    } else if (call.method.equals(METHOD_SET_PRIVACY_API_KEY)) {
      if (call.hasArgument("isAgree")) {
        boolean isAgree = call.argument("isAgree");
        if (BmfMapApplication.mContext != null) {
          try {
            SDKInitializer.setAgreePrivacy(BmfMapApplication.mContext, isAgree);
          } catch (BaiduMapSDKException e) {
            e.getMessage();
          }
        }
      }
    } else if (call.method.equals(METHOD_GET_ANDROID_VERSION)) {
      result.success(android.os.Build.VERSION.SDK_INT);
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }
}
