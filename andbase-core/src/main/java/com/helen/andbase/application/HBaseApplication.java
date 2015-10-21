package com.helen.andbase.application;

import android.app.Application;
import android.graphics.Bitmap.Config;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.helen.andbase.R;
import com.helen.andbase.dao.ServiceAPI;
import com.helen.andbase.utils.AppManager;
import com.helen.andbase.utils.SystemEvent;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;

public class HBaseApplication extends Application implements SystemEvent.IEventListener{
	public static final int LOGOUT_ID = -1;
	@Override
	public void onCreate() {
		super.onCreate();
		SystemEvent.addListener(LOGOUT_ID,this);
		HCrashHandler.init(this);
		ServiceAPI.init(this);
	}

	protected void initImageLoader(int failImgId,int emptyImgId,int loadingImgId) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnFail(failImgId>0?failImgId:R.drawable.ic_launcher) // 加载图片出现问题，会显示该图片
		.showImageForEmptyUri(emptyImgId>0?failImgId:R.drawable.ic_launcher)//url为空的时候显示的图片
		.showImageOnLoading(loadingImgId>0?failImgId:R.drawable.ic_launcher)//图片加载过程中显示的图片
		.bitmapConfig(Config.RGB_565)
		.cacheOnDisk(true)//开启硬盘缓存
		.cacheInMemory(true)//内存缓存
		.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY)
				.defaultDisplayImageOptions(options)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileCount(100)
				.diskCacheSize(10*1024*1024)//缓存容量
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCache(new UnlimitedDiskCache(new File(Environment.getExternalStorageDirectory() + HConstant.DIR_IMAGE)))
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		L.writeLogs(false);//关闭日志
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onEvent(Message msg) {
		switch (msg.what){
			case LOGOUT_ID:
				new Thread() {
					@Override
					public void run() {
						Looper.prepare();
						Toast.makeText(getApplicationContext(),"暂无使用权限!",Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				}.start();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//退出程序
				AppManager.getInstance().ExitApp();
				break;
		}
	}

}