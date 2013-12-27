package es.ereadme.androidspheroradious;

import es.ereadme.androidsperoradious.orbotix.robot.widgets.CalibrationImageButtonView;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.NoSpheroConnectedView;
import es.ereadme.androidsperoradious.orbotix.robot.widgets.SlideToSleepView;
import orbotix.sphero.Sphero;
import orbotix.view.calibration.CalibrationView;
import orbotix.view.calibration.ControllerActivity;
import orbotix.view.connection.SpheroConnectionView;
import android.os.Bundle;
import android.view.Menu;


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

    //Colors
    private int mRed = 0xff;
    private int mGreen = 0xff;
    private int mBlue = 0xff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
