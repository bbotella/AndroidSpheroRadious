package es.ereadme.androidspheroradious;

import orbotix.robot.base.Robot;
import orbotix.sphero.ConnectionListener;
import orbotix.sphero.Sphero;
import orbotix.view.calibration.CalibrationView;
import orbotix.view.calibration.ControllerActivity;
import orbotix.view.connection.SpheroConnectionView;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import es.ereadme.androidsperoradious.orbotix.robot.app.ColorPickerActivity;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.CalibrationImageButtonView;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.JoystickView;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.NoSpheroConnectedView;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.SlideToSleepView;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.NoSpheroConnectedView.OnConnectButtonClickListener;
import es.ereadme.androidspheroradious.BroadcastReceivers.ColorChangeBroadcastReceiver;


public class MainActivity extends ControllerActivity {
	/** ID to start the StartupActivity for result to connect the Robot */
    private final static int STARTUP_ACTIVITY = 0;
    private static final int BLUETOOTH_ENABLE_REQUEST = 11;
    private static final int BLUETOOTH_SETTINGS_REQUEST = 12;

    /** ID to start the ColorPickerActivity for result to select a color */
    private final static int COLOR_PICKER_ACTIVITY = 1;
    private boolean mColorPickerShowing = false;

    /** The Robot to control */
    private Sphero mRobot;

    /** One-Touch Calibration Button */
    private CalibrationImageButtonView mCalibrationImageButtonView;

    /** Calibration View widget */
    private CalibrationView mCalibrationView;

    /** Slide to sleep view */
    private SlideToSleepView mSlideToSleepView;

    /** No Sphero Connected Pop-Up View */
    private NoSpheroConnectedView mNoSpheroConnectedView;

    /** Sphero Connection View */
    private SpheroConnectionView mSpheroConnectionView;
    
    private ColorChangeBroadcastReceiver mColorChangeReceiver;

    //Colors
    private int mRed = 0xff;
    private int mGreen = 0xff;
    private int mBlue = 0xff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // Set up the Sphero Connection View
        mSpheroConnectionView = (SpheroConnectionView) findViewById(R.id.sphero_connection_view);
        mSpheroConnectionView.addConnectionListener(new ConnectionListener() {
        	@Override
            public void onConnected(Robot robot) {
                // Set Robot
                mRobot = (Sphero) robot; // safe to cast for now
                mColorChangeReceiver = new ColorChangeBroadcastReceiver(mRobot);
                //Set connected Robot to the Controllers
                setRobot(mRobot);

                // Make sure you let the calibration view knows the robot it should control
                mCalibrationView.setRobot(mRobot);

                // Make connect sphero pop-up invisible if it was previously up
                mNoSpheroConnectedView.setVisibility(View.GONE);
                mNoSpheroConnectedView.switchToConnectButton();
            }

            @Override
            public void onConnectionFailed(Robot sphero) {
                // let the SpheroConnectionView handle or hide it and do something here...
            }

            @Override
            public void onDisconnected(Robot sphero) {
                mSpheroConnectionView.startDiscovery();
            }
        });
        
        //Add the JoystickView as a Controller
        addController((JoystickView) findViewById(R.id.joystick));

        // Add the calibration view
        mCalibrationView = (CalibrationView) findViewById(R.id.calibration_view);

        // Set up sleep view
        mSlideToSleepView = (SlideToSleepView) findViewById(R.id.slide_to_sleep_view);
        mSlideToSleepView.hide();
        // Send ball to sleep after completed widget movement
        mSlideToSleepView.setOnSleepListener(new SlideToSleepView.OnSleepListener() {
            @Override
            public void onSleep() {
                mRobot.sleep(0);
            }
        });
        
     // Initialize calibrate button view where the calibration circle shows above button
        // This is the default behavior
        mCalibrationImageButtonView = (CalibrationImageButtonView) findViewById(R.id.calibration_image_button);
        mCalibrationImageButtonView.setCalibrationView(mCalibrationView);
        // You can also change the size and location of the calibration views (or you can set it in XML)
        mCalibrationImageButtonView.setRadius(100);
        mCalibrationImageButtonView.setOrientation(CalibrationView.CalibrationCircleLocation.ABOVE);

        // Grab the No Sphero Connected View
        mNoSpheroConnectedView = (NoSpheroConnectedView) findViewById(R.id.no_sphero_connected_view);
        mNoSpheroConnectedView.setOnConnectButtonClickListener(new OnConnectButtonClickListener() {

            @Override
            public void onConnectClick() {
                mSpheroConnectionView.setVisibility(View.VISIBLE);
                mSpheroConnectionView.startDiscovery();
            }

            @Override
            public void onSettingsClick() {
                // Open the Bluetooth Settings Intent
                Intent settingsIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                MainActivity.this.startActivityForResult(settingsIntent, BLUETOOTH_SETTINGS_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mColorPickerShowing) {
            mColorPickerShowing = false;
            return;
        }
        Log.d("", "registering Color Change Listener");
        IntentFilter filter = new IntentFilter(ColorPickerActivity.ACTION_COLOR_CHANGE);
        registerReceiver(mColorChangeReceiver, filter);
    }

    /** Called when the user presses the back or home button */
    @Override
    protected void onPause() {
        super.onPause();
        if (mColorPickerShowing) return;
        // Disconnect Robot properly
        if (mRobot != null) {
            mRobot.disconnect();
        }
        try {
            unregisterReceiver(mColorChangeReceiver); // many times throws exception on leak
        } catch (Exception e) {
        }
    }
    
    
    
}
