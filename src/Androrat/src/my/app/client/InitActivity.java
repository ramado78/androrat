package my.app.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class InitActivity extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent client = new Intent(this, Client.class);
        client.putExtra("IP", "172.17.0.18");
    	client.putExtra("PORT", new Integer(9999));
        startService(client);
        finish();
    }
}
