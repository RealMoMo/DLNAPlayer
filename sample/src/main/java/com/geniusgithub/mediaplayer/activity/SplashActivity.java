package com.geniusgithub.mediaplayer.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.geniusgithub.mediaplayer.AllShareApplication;
import com.geniusgithub.mediaplayer.R;
import com.geniusgithub.mediaplayer.base.BaseActivity;
import com.geniusgithub.mediaplayer.main.MainFrameActivity;
import com.geniusgithub.mediaplayer.util.AlwaysLog;
import com.geniusgithub.mediaplayer.util.PermissionsUtil;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

	private static final  String TAG = SplashActivity.class.getSimpleName();
	private Handler mHandle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_layout);
		ButterKnife.bind(this);
		initData();
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	private void initData(){
		mHandle = new Handler();
		boolean flag = AllShareApplication.getInstance().getEnterFlag();
		if (flag){
			AlwaysLog.i(TAG, "Enter Main Directry");
			goMainActivity();
		}else{
			AlwaysLog.i(TAG, "Enter Main delay");


			mHandle.postDelayed(new Runnable() {
				@Override
				public void run() {
					goMainActivity();
				}
			}, 2000);
		}

	}
	


	@Override
	public void onBackPressed() {
		exit();
	}
	
	
	private void exit(){
		finish();
		System.exit(0);
	}

	private void goMainActivity(){
		if (PermissionsUtil.hasNecessaryRequiredPermissions(this)){
			Intent intent = new Intent();
			intent.setClass(this, MainFrameActivity.class);
			startActivity(intent);
			finish();
		}else{
			requestNecessaryRequiredPermissions();
		}



	}

	private final int REQUEST_STORAGE_PERMISSION =  0X0001;

	private void requestNecessaryRequiredPermissions(){
		requestSpecialPermissions(PermissionsUtil.STORAGE, REQUEST_STORAGE_PERMISSION);
	}


	private void requestSpecialPermissions(String permission, int requestCode){
		String []permissions = new String[]{permission};
		requestPermissions(permissions, requestCode);
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

		switch(requestCode){
			case REQUEST_STORAGE_PERMISSION:
				doStoragePermission(grantResults);
				break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				break;
		}


	}

	private void doStoragePermission(int[] grantResults){
		if (grantResults[0] == PackageManager.PERMISSION_DENIED){
			AlwaysLog.e(TAG, "doStoragePermission is denied!!!" );
			Dialog dialog = PermissionsUtil.createPermissionSettingDialog(this, "存储权限");
			dialog.show();
		}else if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
			AlwaysLog.i(TAG, "doStoragePermission, is granted!!!" );
			goMainActivity();
		}

	}




}
