package my.app.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	
	public final String TAG = BootReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG,"BOOT Complete received by Client !");
		
		String action = intent.getAction();
		
		if(action.equals(Intent.ACTION_BOOT_COMPLETED)) { //android.intent.action.BOOT_COMPLETED
			Intent serviceIntent = new Intent(context, InitActivity.class);
			serviceIntent.setAction(Intent.ACTION_MAIN);
			serviceIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			serviceIntent.setAction(BootReceiver.class.getSimpleName());
			
			context.startActivity(serviceIntent);
		}
	}

}