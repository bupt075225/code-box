package com.example.leadme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Button;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.DataInfoUtils;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.baidu.speechsynthesizer.publicutility.SpeechLogger;

public class VoiceActivity extends ActionBarActivity implements
	OnClickListener,SpeechSynthesizerListener{

	protected static final String ACTIVITY_TAG="VoiceActivity";
	private SpeechSynthesizer speechSynthesizer;
	private EditText inputTextView;
	private Button startButton;
	
	/** 指定license路径，需要保证该路径的可读写权限 */
    private static final String LICENCE_FILE_NAME = Environment.getExternalStorageDirectory()
            + "/tts/baidu_tts_licence.dat";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice);
		
		Intent intentFromRoute = getIntent();
		String routeMsg = intentFromRoute.getStringExtra(DescriptingActivity.ROUTE_MSG);
		inputTextView = (EditText)findViewById(R.id.inputTextView);		
		startButton = (Button)findViewById(R.id.start);
		startButton.setOnClickListener(this);
		
		//向文本编辑框中填入路线描述页面发来的描述信息
		if(null != routeMsg)
		{
			inputTextView.setText(routeMsg);
		}
		
		// 部分版本不需要BDSpeechDecoder_V1
        try {
            System.loadLibrary("BDSpeechDecoder_V1");
        } catch (UnsatisfiedLinkError e) {
            SpeechLogger.logD("load BDSpeechDecoder_V1 failed, ignore");
        }
        System.loadLibrary("bd_etts");
	    System.loadLibrary("bds");
	    
	    if (!new File(LICENCE_FILE_NAME).getParentFile().exists()) {
            new File(LICENCE_FILE_NAME).getParentFile().mkdirs();
        }
	    // 复制license到指定路径
        InputStream licenseInputStream = getResources().openRawResource(R.raw.temp_license_20150608);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(LICENCE_FILE_NAME);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = licenseInputStream.read(buffer, 0, 1024)) >= 0) {
                SpeechLogger.logD("size written: " + size);
                fos.write(buffer, 0, size);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                licenseInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        speechSynthesizer =
            SpeechSynthesizer.newInstance(SpeechSynthesizer.SYNTHESIZER_AUTO, getApplicationContext(),
            "holder", this);
        speechSynthesizer.setApiKey("A09gQMkB2zo3dXXm09uYj13F", "0b470af6b14798c913beee3e3f60b010");
        // 设置授权文件路径
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, LICENCE_FILE_NAME);
        // TTS所需的资源文件，可以放在任意可读目录，可以任意改名
        String ttsTextModelFilePath =
                getApplicationContext().getApplicationInfo().dataDir + "/lib/libbd_etts_text.dat.so";
        String ttsSpeechModelFilePath =
                getApplicationContext().getApplicationInfo().dataDir + "/lib/libbd_etts_speech_female.dat.so";
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ttsTextModelFilePath);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ttsSpeechModelFilePath);
        DataInfoUtils.verifyDataFile(ttsTextModelFilePath);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_DATE);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_SPEAKER);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_GENDER);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_CATEGORY);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_LANGUAGE);
        speechSynthesizer.initEngine();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.voice, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.start:
            setParams();
            int ret = speechSynthesizer.speak(inputTextView.getText()
                    .toString());
            if (ret != 0) 
            {
                Log.w(VoiceActivity.ACTIVITY_TAG,"开始合成器失败" + errorCodeAndDescription(ret));
            }
            else 
            {
                Log.i(VoiceActivity.ACTIVITY_TAG, "开始工作，请等待数据...");
            }
            break;
        default:
            break;
        }
    }
	
	private void setParams() {
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, "1");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, "4");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE, "ZH");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
    }
	
	@Override
    public void onSpeechStart(SpeechSynthesizer synthesizer) {
        //nothing to do
    }
	
	@Override
	public void onBufferProgressChanged(SpeechSynthesizer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(SpeechSynthesizer arg0, SpeechError arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewDataArrive(SpeechSynthesizer arg0, byte[] arg1,
			boolean arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpeechFinish(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpeechPause(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpeechProgressChanged(SpeechSynthesizer arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSpeechResume(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartWorking(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSynthesizeFinish(SpeechSynthesizer arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private String errorCodeAndDescription(int errorCode) {
        String errorDescription = "错误码：";
        return errorDescription + "(" + errorCode + ")";
    }
}
