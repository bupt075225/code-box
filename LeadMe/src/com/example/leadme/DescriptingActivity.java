package com.example.leadme;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class DescriptingActivity extends ActionBarActivity {

	public final static String ROUTE_MSG = "com.demo.leadme.ROUTEMSG";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripting);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.descripting, menu);
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
	
	//Called when the user click button
	public void texToSpeech(View view)
	{
		Intent intentTts = new Intent(this, VoiceActivity.class);
		EditText routeDescription = (EditText)findViewById(R.id.routeInputView);
		String routeMessage = routeDescription.getText().toString();
		intentTts.putExtra(ROUTE_MSG, routeMessage);
		startActivity(intentTts);
	}
}
