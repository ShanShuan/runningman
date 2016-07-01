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
	//���˶�����ļ���
	private List<LatLng> positionList=new ArrayList<LatLng>();
	//��ʾ�˶�ͳ�ƽ���
	Handler handler=new Handler();
	int sleepTime=2000;
	Runnable runnable;
	private LinearLayout linearLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ��ʼ��
		try {
			view = inflater.inflate(R.layout.fragment_sport, null);
			mapView = (MapView) view.findViewById(R.id.mapView);
			tvAction=(TextView) view.findViewById(R.id.tv_fragment_sport_action);
			linearLayout=(LinearLayout) view.findViewById(R.id.ll_sport_recorder);
			addListener();
			baiduMap = mapView.getMap();
			// ��ͼ�����¼��������꣬��ʾ��ͼ
			baiduMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public boolean onMapPoiClick(MapPoi arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void onMapClick(LatLng position) {
					Log.i("����λ�õ�γ�Ⱥ;���", position.latitude + ","
							+ position.longitude);
//					// �ƶ����ĵ�
//					moveMapCenter(position);
//					baiduMap.clear();
//					// ��ʾһ��ͼƬ
//					showImage(position);
					
					
					//ģ���û��˶�
					positionList.add(position);
					//��������������� ����
					if(positionList.size()>=2){
						PolylineOptions polylineOptions=new PolylineOptions();
						//�����õ��ĵ�
						polylineOptions.points(positionList);
						baiduMap.clear();
						baiduMap.addOverlay(polylineOptions);
					}

				}
			});
			locationClient = new LocationClient(getActivity());
			// ���ö�λ�Ĳ���
			LocationClientOption option = new LocationClientOption();
			// ��Gps
			option.setOpenGps(true);
			// ������������ ����ȫ���ǣ�
			option.setCoorType("bd09ll");
			// ÿ��һ��ʱ��õ�һ������ ʱ��С��1000��ʾֵ��һ��
			option.setScanSpan(1);
			// �ٴ����¶�λ
//			 locationClient.requestLocation();
			locationClient.setLocOption(option);
			// �ðٶȿ���еĽӿ�ָ��ʵ����
			MyBdLocationListener listener = new MyBdLocationListener();
			locationClient.registerLocationListener(listener);
			locationClient.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}
	
	
	
	
	
	/**
	 *  ��ʼ��ť   ��Ӽ���
	 */
	private void addListener() {
		tvAction.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					String text=((TextView)arg0).getText().toString();
					if("����".equals(text)){
						//����
						linearLayout.setVisibility(View.GONE);
						baiduMap.clear();
						positionList.clear();
						handler.removeCallbacks(runnable);
						tvAction.setText("��ʼ");
						count=3;
					}else{
					//��xml���view
					View view=View.inflate(getActivity(), R.layout.activity_show_counter, null);
					//����һ��dailog
					dialog=new AlertDialog.Builder(getActivity()).create();
					//Ϊdialog����view
					dialog.setView(view);
					//��ʾdialog
					dialog.show();
					count=3;
					final TextView tv=(TextView) view.findViewById(R.id.tv_show_count);
					final Handler handler=new Handler();
					
					//ÿ������count-1��������ʾ����
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
								//��ʾ�˶�ͳ�ƵĽ���
								showRecorder();
							}
						}
						
						
					}, 2000);
					
					tvAction.setText("����");
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
		});
	}

	
	/**
	 * ��ʾ �˶��������Ϣ
	 */
	private void showRecorder() {
		linearLayout.setVisibility(View.VISIBLE);
		//�ҵ�ͳ�ƽ����ϵĿؼ�
		//meter��ͳ��ʱ���
		final Chronometer meter=(Chronometer) view.findViewById(R.id.chronometer1);
		meter.start();
		meter.setBase(SystemClock.elapsedRealtime());
		
		
		final TextView tvDistance=(TextView) view.findViewById(R.id.tv_distance);
		
		final TextView tvSpeed=(TextView) view.findViewById(R.id.tv_recorder_speed);
		
		//ÿ��2���������
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
						//��ת�ɹ���
						distance=distance/1000;
						//��ʾ����
						String strDistance=String.valueOf(distance);
						//��ʾ�ٶ�
						//�õ�ʱ���ַ���
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
	 * �ƶ����ĵ�λ��
	 * 
	 * @param currentPosition
	 */
	private void moveMapCenter(LatLng currentPosition) {
		MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(
				currentPosition, 17);// ��һ����������� �ڶ��������Ŵ󼶱�(4-17)
		baiduMap.animateMapStatus(update);
	}

	
	
	
	
	
	
	/**
	 * ��ʾͼƬ
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

	
	
	
	
	
	// ʵ����
	class MyBdLocationListener implements BDLocationListener {
		// ��λ�ɹ���ִ��
		@Override
		public void onReceiveLocation(BDLocation arg0) {
			try {
				// ����
				double longitude = arg0.getLongitude();
				// γ��
				double latitude = arg0.getLatitude();
				Log.i("123", "����=" + longitude + ",γ��=" + latitude);
				// �жϳ�����û�еõ�����
				// �õ�����4.9E-324
				if (latitude == 4.9E-324) {
					// ģ��һ��λ��
					latitude = 32.541534545;
					longitude = 118.15152545;
				}
//				LatLng argo=new LatLng(longitude, latitude);
//				positionList.add(argo);
				// �ƶ���ͼ���ĵ�Ϊ��ǰλ��
				LatLng currentPosition = new LatLng(latitude, longitude);
				moveMapCenter(currentPosition);
				// �ڵ�ǰλ����ʾһ��ͼ
				showImage(currentPosition);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
