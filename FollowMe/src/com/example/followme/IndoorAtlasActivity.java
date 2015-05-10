package com.example.followme;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

import com.indooratlas.android.CalibrationState;
import com.indooratlas.android.IndoorAtlas;
import com.indooratlas.android.IndoorAtlasException;
import com.indooratlas.android.IndoorAtlasFactory;
import com.indooratlas.android.IndoorAtlasListener;
import com.indooratlas.android.ServiceState;

public class IndoorAtlasActivity extends ActionBarActivity implements IndoorAtlasListener{

	private static final String TAG = "IndoorAtlasActivity";
	
	private ListView mLogView;
    private LogAdapter mLogAdapter;
    
	private IndoorAtlas mIndoorAtlas;
	private boolean mIsPositioning;
	private StringBuilder mSharedBuilder = new StringBuilder();
	
	private String mApiKey = "98fbe660-bc6e-425f-a9d0-d05f4ff5a6db";
    private String mApiSecret = "9egGb%%LMs7f5gr)6FZ0lHe8Z2ut5yb%aHS(aGc3nt!wLBaQemMxvLkjdzYCjHMwcpEOXRfG92(&1hc&IcgVgob84CNza7pWyWCXdjq!b4UDbAEeJVVS6CQy60!C0hLn";

    private String mVenueId = "163739aa-a4a7-48f2-9193-d065f19d34af";
    private String mFloorId = "ba39a97e-6330-46ae-84bf-37cdb516ce58";
    private String mFloorPlanId = "9b70bcf1-717b-4161-a236-1a43d825732e";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indoor_atlas);
		
		mLogView = (ListView) findViewById(R.id.list);
        mLogAdapter = new LogAdapter(this);
        mLogView.setAdapter(mLogAdapter);
        
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		initIndoorAtlas();
	}

	private void initIndoorAtlas() {
        try {
            log("Connecting with IndoorAtlas, apiKey: " + mApiKey);

            // obtain instance to positioning service, 
            // note that calibrating might begin instantly
            mIndoorAtlas = IndoorAtlasFactory.createIndoorAtlas(
                    getApplicationContext(),
                    (IndoorAtlasListener)this, // IndoorAtlasListener
                    mApiKey,
                    mApiSecret);

            log("IndoorAtlas instance created");
            togglePositioning();

        } catch (IndoorAtlasException ex) {
            Log.e("IndoorAtlas", "init failed", ex);
        }

    }
	
	private void tearDown() {
        if (mIndoorAtlas != null) {
            mIndoorAtlas.tearDown();
        }
    }
	
	private void stopPositioning() {
        mIsPositioning = false;
        if (mIndoorAtlas != null) {
            log("Stop positioning");
            mIndoorAtlas.stopPositioning();
        }
    }

    private void startPositioning() {
        if (mIndoorAtlas != null) {            
            log(String.format("startPositioning, venueId: %s, floorId: %s, floorPlanId: %s",
                    mVenueId,
                    mFloorId,
                    mFloorPlanId));
            try {
                mIndoorAtlas.startPositioning(mVenueId, mFloorId, mFloorPlanId);
                mIsPositioning = true;
            } catch (IndoorAtlasException e) {
                log("startPositioning failed: " + e);
            }
        } else {
            log("calibration not ready, cannot start positioning");
        }
    }

    private void togglePositioning() {
        if (mIsPositioning) {
            stopPositioning();
        } else {
            startPositioning();
        }
    }
    
    private void log(final String msg) {
        Log.d(TAG, msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLogAdapter.add(msg);
                mLogAdapter.notifyDataSetChanged();
            }
        });
    }
    
    /* IndoorAtlasListener interface */

    /**
     * This is where you will handle location updates.
     */
    public void onServiceUpdate(ServiceState state) {

        mSharedBuilder.setLength(0);
        mSharedBuilder.append("Location: ")
                .append("\n\troundtrip : ").append(state.getRoundtrip()).append("ms")
                .append("\n\tlat : ").append(state.getGeoPoint().getLatitude())
                .append("\n\tlon : ").append(state.getGeoPoint().getLongitude())
                .append("\n\tX [meter] : ").append(state.getMetricPoint().getX())
                .append("\n\tY [meter] : ").append(state.getMetricPoint().getY())
                .append("\n\tI [pixel] : ").append(state.getImagePoint().getI())
                .append("\n\tJ [pixel] : ").append(state.getImagePoint().getJ())
                .append("\n\theading : ").append(state.getHeadingDegrees())
                .append("\n\tuncertainty: ").append(state.getUncertainty());

        log(mSharedBuilder.toString());
    }

    static class LogAdapter extends BaseAdapter {

        private ArrayList<String> mLines = new ArrayList<String>();
        private LayoutInflater mInflater;

        public LogAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mLines.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text = (TextView) convertView;
            if (convertView == null) {
                text = (TextView) mInflater.inflate(android.R.layout.simple_list_item_1, parent,
                        false);
            }
            text.setText(mLines.get(position));
            return text;
        }

        public void add(String line) {
            mLines.add(0, line);
        }

        public void clear() {
            mLines.clear();
            notifyDataSetChanged();
        }
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	tearDown();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.indoor_atlas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id)
		{
			case R.id.action_clear_log:
				mLogAdapter.clear();
                return true;
			case R.id.action_toggle_positioning:
                togglePositioning();
                return true;
			case R.id.action_settings:
				return true;
            default:
                return super.onOptionsItemSelected(item);
		}		
	}

	/**
	 *  @deprecated this callback is deprecated as of version 1.4
	 */
	@Override
	public void onCalibrationFailed(String arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 *  @deprecated this callback is deprecated as of version 1.4
	 */
	@Override
	public void onCalibrationInvalid() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Notifies that calibration has reached level of quality 
	 * that provides best possible positioning accuracy
	 */
	@Override
	public void onCalibrationReady() {
		// TODO Auto-generated method stub
		log("Reached level of quality that provides best possible posigioning accuracy");		
	}

	/**
	 * Notifies about current calibration status
	 */
	@Override
	public void onCalibrationStatus(CalibrationState arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitializationFailed(String arg0) {
		// TODO Auto-generated method stub
		log("Creating a new positioning session fail");
	}

	@Override
	public void onNetworkChangeComplete(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceFailure(int arg0, String arg1) {
		// TODO Auto-generated method stub
		log("Communication with IndoorAtlas fail");
	}

	@Override
	public void onServiceInitialized() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceInitializing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceStopped() {
		// TODO Auto-generated method stub
		
	}
}
