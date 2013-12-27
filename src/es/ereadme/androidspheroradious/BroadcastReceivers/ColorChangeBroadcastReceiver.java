package es.ereadme.androidspheroradious.BroadcastReceivers;

import es.ereadme.androidsperoradious.orbotix.robot.app.ColorPickerActivity;
import orbotix.sphero.Sphero;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ColorChangeBroadcastReceiver extends BroadcastReceiver{
	Sphero robot;
	
	public ColorChangeBroadcastReceiver(Sphero mRobot){
		robot = mRobot;
	}
	
	@Override
    public void onReceive(Context context, Intent intent) {
        // update colors
        int red = intent.getIntExtra(ColorPickerActivity.EXTRA_COLOR_RED, 0);
        int green = intent.getIntExtra(ColorPickerActivity.EXTRA_COLOR_GREEN, 0);
        int blue = intent.getIntExtra(ColorPickerActivity.EXTRA_COLOR_BLUE, 0);

        // change the color on the ball
        robot.setColor(red, green, blue);
    }
}
