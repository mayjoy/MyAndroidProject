package com.eyesee.sysupdate;

import java.io.StringReader;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static String GUID = "20150422150447";
	public static String VERSION = "0.5";
	public static String URL = "http://192.168.0.108:8080/controller/controller.shtml";
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		//显示当前版本号
		tv_version.setText(getResources().getString(R.string.test_version)+VERSION);
		//获取语言环境
		language = getResources().getConfiguration().locale.getLanguage();
	}
	
	@OnClick({R.id.ib_back,R.id.tv_button})
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
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

		http.send(HttpMethod.GET, URL+"?guid="+GUID+"&firmware="+VERSION, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Log.d("mark", "检查更新失败,网络访问错误");
			}

			@Override
			public void onSuccess(ResponseInfo<String> resoponse) {
				String result = resoponse.result;
				Log.d("mark", "访问成功:"+result);
				if(result.contains("匹配失败")){//不需要更新
					Toast.makeText(context, R.string.have_last_version, Toast.LENGTH_SHORT).show();
				}else{//更新
					//解析xml
					try {
						XmlPullParser parser = Xml.newPullParser();
						parser.setInput(new StringReader(result));
						int eventType = parser.getEventType();//得到第一个事件类型
						while (eventType != XmlPullParser.END_DOCUMENT){
							switch (eventType) {
							case XmlPullParser.START_TAG:
								if("url".equals(parser.getName())){
									downLoadUrl = parser.nextText();
								}else if("md5".equals(parser.getName())){
									md5 = parser.nextText();
								}else if("description".equals(parser.getName())){
									if(language.equals(parser.getAttributeValue(1))){
										tv_update_content.setText(parser.nextText());
									}
								}
								break;

							}
							eventType=parser.next();//进入下一个事件处理
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("mark", "更新文档解析失败");
					}
				}
			}
		});
	}
}
