package com.Util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class LocationUtil {
        private final static boolean DEBUG = true;
        private final static String TAG = "LocationUtil";
        private static LocationUtil mInstance;
        private BDLocation mLocation = null;
        private MLocation  mBaseLocation = new MLocation();

        public static LocationUtil getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new LocationUtil(context);
            }
            return mInstance;
        }

        Context mContext;
        String mProvider;
        public BDLocationListener myListener = new MyLocationListener();
        private LocationClient mLocationClient;

        public LocationUtil(Context context) {
            mLocationClient = new LocationClient(context.getApplicationContext());
            initParams();
            mLocationClient.registerLocationListener(myListener);
        }

        public void startMonitor() {
            if (DEBUG) Log.d(TAG, "start monitor location");
            if (!mLocationClient.isStarted()) {
                mLocationClient.start();
            }
            if (mLocationClient != null && mLocationClient.isStarted()) {
                mLocationClient.requestLocation();
            } else {
                mLocationClient.stop();
                mLocationClient.start();
                mLocationClient.requestLocation();
                Log.d("LocSDK3", "locClient is null or not started");
            }
        }

        public void stopMonitor() {
            if (DEBUG) Log.d(TAG, "stop monitor location");
            if (mLocationClient != null && mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
        }

        public BDLocation getLocation() {
            if (DEBUG) Log.d(TAG, "get location");
            return mLocation;
        }

        public MLocation getBaseLocation() {
            if (DEBUG) Log.d(TAG, "get location");
            return mBaseLocation;
        }

        private void initParams() {
            System.out.println("------------Inside initParams--------------------");
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            //option.setPriority(LocationClientOption.NetWorkFirst);
            option.setAddrType("all");//返回的定位结果包含地址信息
            option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
            option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
            option.disableCache(true);//禁止启用缓存定位
            mLocationClient.setLocOption(option);

        }


        public class MyLocationListener implements BDLocationListener {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (location == null) {
                    System.out.println("location is null");
                    mLocationClient.requestLocation();
                }
                mLocation = location;
                mBaseLocation.latitude = mLocation.getLatitude();
                mBaseLocation.longitude = mLocation.getLongitude();

                StringBuffer sb = new StringBuffer(256);
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                if (location.getLocType() == BDLocation.TypeGpsLocation){
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                }
                if (DEBUG) Log.d(TAG, "" + sb);
            }
        }

        public class MLocation {
            public double latitude;
            public double longitude;
        }
}
