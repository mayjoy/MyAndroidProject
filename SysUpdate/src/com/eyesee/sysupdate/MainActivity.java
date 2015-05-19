package com.eyesee.sysupdate;

import java.io.File;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;

import com.eyesee.sysupdate.OtaUpgradeUtils.ProgressListener;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String GUID = "20150422150447";
	public static final String VERSION = "0.5";
	public static final String URL = "http://192.168.0.108:8080/controller/controller.shtml";
	public static final String DOWNLOADFILE_PATH = "mnt/sdcard/update.zip";
	public static final String CACHEFILE_PATH = "/cache/update.zip";
	
	private Context context = this;

	private String downLoadUrl;
	private String md5;
	private String language;

	HttpUtils http = new HttpUtils();

	@ViewInject(R.id.tv_button)
	private TextView tv_button;

	@ViewInject(R.id.ib_back)
	private ImageButton ib_back;

	@ViewInject(R.id.tv_version)
	private TextView tv_version;

	@ViewInject(R.id.tv_update_content)
	private TextView tv_update_content;

	@ViewInject(R.id.pb_download)
	private ProgressBar pb_download;

	@SuppressWarnings("rawtypes")
	private HttpHandler httpHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);// 控件和事件注入
		// 显示当前版本号
		tv_version.setText(getString(R.string.test_version)
				+ VERSION);
		// 获取语言环境
		language = getResources().getConfiguration().locale.getLanguage();
	}

	@OnClick({ R.id.ib_back, R.id.tv_button })
	public void onClick(View v) {// 按键事件监听器
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			if (httpHandler != null) {
				httpHandler.cancel();
			}
			break;
		case R.id.tv_button:
			checkUpdate();
			break;
		}
	}

	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		Log.d("mark", "检测升级");
		http.send(HttpMethod.GET, URL + "?guid=" + GUID + "&firmware="
				+ VERSION, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.d("mark", "检查更新失败,网络访问错误");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resoponse) {
				String result = resoponse.result;
				Log.d("mark", "访问成功:" + result);
				if (result.contains("匹配失败")) {// 不需要更新
					Toast.makeText(context, R.string.have_last_version,Toast.LENGTH_SHORT).show();
				} else {// 更新

					try {
						// 解析xml
						XmlPullParser parser = Xml.newPullParser();
						parser.setInput(new StringReader(result));
						int eventType = parser.getEventType();// 得到第一个事件类型
						while (eventType != XmlPullParser.END_DOCUMENT) {
							switch (eventType) {
							case XmlPullParser.START_TAG:
								if ("url".equals(parser.getName())) {// 获取url
									downLoadUrl = parser.nextText();
								} else if ("md5".equals(parser.getName())) {// 获取MD5
									md5 = parser.nextText();
								} else if ("description".equals(parser.getName())) {// 获取更新内容描述
									if (language.equals(parser.getAttributeValue(1))) {
										tv_update_content.setText(parser.nextText());
									}
								}
								break;

							}
							eventType = parser.next();// 进入下一个事件处理
						}

						//检测本地是否有升级文件
						File file = new File(DOWNLOADFILE_PATH);
						if(file.exists()){//已经存在
							Log.d("mark", "发现本地升级包");
							Toast.makeText(context, getString(R.string.validate_local_file), Toast.LENGTH_SHORT).show();
							checkMd5();
						}else{
							// 设置下载监听器
							tv_button.setText(R.string.download_update);
							tv_button.setOnClickListener(new DownLoadListener());
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mark", "更新文档解析失败");
					}
				}
			}
		});
	}
	
	/**
	 * 校验下载的文件的MD5
	 */
	private void checkMd5() {
		// 单独开一个子线程去校验MD5
		new Thread() {
			@Override
			public void run() {
				super.run();
				File file = new File(DOWNLOADFILE_PATH);
				String downloadFileMd5 = Md5Utils.getMd5ByFile(file);
				Log.d("mark", "downloadFileMd5:"+downloadFileMd5+";md5:"+md5);
				Message msg = Message.obtain();
				if (md5.equalsIgnoreCase(downloadFileMd5)) {
					msg.what = 0;
				} else {
					msg.what = 1;
				}
				handler.sendMessage(msg);
			}

		}.start();
	}

	/**
	 * 点击下载监听器
	 * @author mark
	 */
	private class DownLoadListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Log.d("mark", "dowload:url:" + downLoadUrl);
			httpHandler = http.download(downLoadUrl, DOWNLOADFILE_PATH,
					true, true, new RequestCallBack<File>() {

						@Override
						public void onStart() {

						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							pb_download.setMax((int) total);
							pb_download.setProgress((int) current);
							tv_button.setText(R.string.cancle);
							tv_button.setOnClickListener(new OnClickListener() {// 点击暂停

										@Override
										public void onClick(View v) {
											httpHandler.cancel();
											tv_button.setText(R.string.continue_download);
											tv_button.setOnClickListener(this);// 点击继续
										}
									});
						}

						@Override
						public void onSuccess(ResponseInfo<File> responseInfo) {
							tv_button.setText(R.string.validate);
							checkMd5();

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							tv_button.setText(R.string.fail_download);
						}
					});
		}

	}
	
	/**
	 * 处理MD5校验结果
	 */
	 @SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:// 数据校验成功
				Log.d("mark", "升级包校验成功");
				tv_button.setText(R.string.install_update);
				tv_button.setOnClickListener(new InstallClickListener());
				break;
			case 1://数据校验失败
				Log.d("mark", "升级包校验失败");
				tv_button.setText(R.string.validate_false);
				//删除文件,并重新设置下载监听器
				File file = new File(DOWNLOADFILE_PATH);
				if(file.delete()){
					tv_button.setOnClickListener(new DownLoadListener());
				};
				break;
			}
		}

	};
	
	/**
	 * 点击安装的监听器
	 * @author mark
	 */
	private class InstallClickListener implements OnClickListener {// 点击安装

		@Override
		public void onClick(View v) {
			
			tv_button.setText(R.string.installing);
			//开启子线程拷贝.
			new Thread(){
				@Override
				public void run() {
					boolean b = OtaUpgradeUtils.copyFile(new File(DOWNLOADFILE_PATH), new File(CACHEFILE_PATH), new ProgressListener() {
						
						@Override
						public void onVerifyFailed(int errorCode, Object object) {
							
						}
						
						@Override
						public void onProgress(int progress) {
							Log.d("mark", "进度:"+progress);
						}
						
						@Override
						public void onCopyProgress(int progress) {
							Log.d("mark", "拷贝进度:"+progress);
							//progressBar可以在子线程中更新
							pb_download.setMax(100);
							pb_download.setProgress(progress);
						}
						
						@Override
						public void onCopyFailed(int errorCode, Object object) {
							Log.d("mark", "拷贝到系统分区失败");
						}
					});
					
					if(b){
						OtaUpgradeUtils.installPackage(MainActivity.this, new File(CACHEFILE_PATH));
					}
				}
				
			}.start();
			
			
		}
	}
}
