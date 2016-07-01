package com.tarena.allrun.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.android.bbalbs.common.a.c;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.tarena.allrun.R;
import com.tarena.allrun.util.BaiduMapUtil;

public class SportFragment extends Fragment {
	View view;
	MapView mapView;
	LocationClient locationClient;
	BaiduMap baiduMap;
	AlertDialog dialog;
	TextView tvAction;
	protected int count;
	//放运动坐标的集合
	private List<LatLng> positionList=new ArrayList<LatLng>();
	//显示运动统计界面
	Handler handler=new Handler();
	int sleepTime=2000;
	Runnable runnable;
	private LinearLayout linearLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 初始化
		try {
			view = inflater.inflate(R.layout.fragment_sport, null);
			mapView = (MapView) view.findViewById(R.id.mapView);
			tvAction=(TextView) view.findViewById(R.id.tv_fragment_sport_action);
			linearLayout=(LinearLayout) view.findViewById(R.id.ll_sport_recorder);
			addListener();
			baiduMap = mapView.getMap();
			// 地图单击事件，得坐标，显示地图
			baiduMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onMapClick(LatLng position) {
					Log.i("单击位置的纬度和经度", position.latitude + ","
							+ position.longitude);
//					// 移动中心点
//					moveMapCenter(position);
//					baiduMap.clear();
//					// 显示一个图片
//					showImage(position);
					
					
					//模拟用户运动
					positionList.add(position);
					//如果有两个点以上 画线
					if(positionList.size()>=2){
						PolylineOptions polylineOptions=new PolylineOptions();
						//画线用到的点
						polylineOptions.points(positionList);
						baiduMap.clear();
						baiduMap.addOverlay(polylineOptions);
					}

				}
			});
			locationClient = new LocationClient(getActivity());
			// 设置定位的参数
			LocationClientOption option = new LocationClientOption();
			// 打开Gps
			option.setOpenGps(true);
			// 设置坐标类型 （安全考虑）
			option.setCoorType("bd09ll");
			// 每隔一段时间得到一次坐标 时间小于1000表示值得一次
			option.setScanSpan(1);
			// 再次重新定位
//			 locationClient.requestLocation();
			locationClient.setLocOption(option);
			// 让百度框架中的接口指向实现类
			MyBdLocationListener listener = new MyBdLocationListener();
			locationClient.registerLocationListener(listener);
			locationClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	
	
	
	
	
	/**
	 *  开始按钮   添加监听
	 */
	private void addListener() {
		tvAction.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					String text=((TextView)arg0).getText().toString();
					if("结束".equals(text)){
						//隐藏
						linearLayout.setVisibility(View.GONE);
						baiduMap.clear();
						positionList.clear();
						handler.removeCallbacks(runnable);
						tvAction.setText("开始");
						count=3;
					}else{
					//把xml变成view
					View view=View.inflate(getActivity(), R.layout.activity_show_counter, null);
					//创建一个dailog
					dialog=new AlertDialog.Builder(getActivity()).create();
					//为dialog设置view
					dialog.setView(view);
					//显示dialog
					dialog.show();
					count=3;
					final TextView tv=(TextView) view.findViewById(R.id.tv_show_count);
					final Handler handler=new Handler();
					
					//每隔两秒count-1，并且显示出来
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							tv.setText(count+"");
							count=count-1;
							if(count>0){
							handler.postDelayed(this, 2000);
							}else{
								dialog.dismiss();
								dialog=null;
								//显示运动统计的界面
								showRecorder();
							}
						}
						
						
					}, 2000);
					
					tvAction.setText("结束");
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * 显示 运动距离等信息
	 */
	private void showRecorder() {
		linearLayout.setVisibility(View.VISIBLE);
		//找到统计界面上的控件
		//meter是统计时间的
		final Chronometer meter=(Chronometer) view.findViewById(R.id.chronometer1);
		meter.start();
		meter.setBase(SystemClock.elapsedRealtime());
		
		
		final TextView tvDistance=(TextView) view.findViewById(R.id.tv_distance);
		
		final TextView tvSpeed=(TextView) view.findViewById(R.id.tv_recorder_speed);
		
		//每隔2秒计算数据
		runnable=new Runnable() {
			
			@Override
			public void run() {
					try {
						double distance=0;
						if(positionList.size()>=2){
						for (int i = 0; i < positionList.size()-1; i++) {
							double long1=positionList.get(i).longitude;
							double lat1=positionList.get(i).latitude;
							double long2=positionList.get(i+1).longitude;
							double lat2=positionList.get(i+1).latitude;
							distance=distance+BaiduMapUtil.GetDistance(long1, lat1, long2, lat2);
							
						}
						//米转成公里
						distance=distance/1000;
						//显示距离
						String strDistance=String.valueOf(distance);
						//显示速度
						//得到时间字符串
						String time=meter.getText().toString();
						String[] array=time.split(":");
						double  minutes=Double.parseDouble(array[0]);
						double  second=Integer.parseInt(array[1]);
						double hour=minutes/60+second/60/60;
						double speed=distance/hour;
						String strSpeed=String.valueOf(speed);
						if(strSpeed.contains(".")){
							strSpeed=strSpeed.substring(0, strSpeed.indexOf(".")+3);
						}
						tvSpeed.setText(strSpeed);
						if(strDistance.contains(".")){
							int positionIndex=strDistance.indexOf(".");
							strDistance=strDistance.substring(0, positionIndex+3);
						}
						tvDistance.setText(strDistance);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}finally{
						handler.postDelayed(this, sleepTime);
					}
			}
		};
		handler.postDelayed(runnable, sleepTime);
		}
	
	
	
	
	/**
	 * 移动中心点位置
	 * 
	 * @param currentPosition
	 */
	private void moveMapCenter(LatLng currentPosition) {
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(
				currentPosition, 17);// 第一个参数坐标点 第二个参数放大级别(4-17)
		baiduMap.animateMapStatus(update);
	}

	
	
	
	
	
	
	/**
	 * 显示图片
	 * 
	 * @param currentPosition
	 */
	private void showImage(LatLng currentPosition) {
		MarkerOptions options = new MarkerOptions();
		options.position(currentPosition);
		options.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka));
		baiduMap.addOverlay(options);
	}

	
	
	
	
	
	// 实现类
	class MyBdLocationListener implements BDLocationListener {
		// 定位成功后，执行
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			try {
				// 经度
				double longitude = arg0.getLongitude();
				// 纬度
				double latitude = arg0.getLatitude();
				Log.i("123", "经度=" + longitude + ",纬度=" + latitude);
				// 判断程序有没有得到坐标
				// 得到的是4.9E-324
				if (latitude == 4.9E-324) {
					// 模拟一个位置
					latitude = 32.541534545;
					longitude = 118.15152545;
				}
//				LatLng argo=new LatLng(longitude, latitude);
//				positionList.add(argo);
				// 移动地图中心点为当前位置
				LatLng currentPosition = new LatLng(latitude, longitude);
				moveMapCenter(currentPosition);
				// 在当前位置显示一张图
				showImage(currentPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
