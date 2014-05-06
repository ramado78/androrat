package my.app.Library;

import my.app.client.ClientListener;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;


public abstract class PhotoTaker extends Service {
	ClientListener ctx;
	int chan ;
	
	public PhotoTaker(ClientListener c, int chan) {
		Log.i("PhotoTaker", "Constructing object Phototaker");
		this.chan = chan ;
		ctx = c;
	}

	public boolean takePhoto() {
   
        return true;
	}
	
	
};
