package jp.co.panasonic.pstc.ocr.android.card.app.camera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * カメラプレビュークラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	
	/** 変数・定数宣言 */
	protected	Camera			camera;		// カメラ
	private	SurfaceHolder	holder;		// ホルダー
	
	
	/**
	 * コンストラクタ<br />
	 * @param context	コンテキスト(Context型)
	 */
	public CameraPreview(Context context) {
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	/**
	 * サーフェス生成時処理<br />
	 * @param holder	ホルダー(SurfaceHolder型)
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if( camera != null ) return;
		camera = Camera.open();
		try {
			camera.setPreviewDisplay(holder);
		}
		catch (IOException e) {
			camera.release();
			camera = null;
		}
	}
	
	/**
	 * サーフェス変更時処理<br />
	 * @param holder	ホルダー(SurfaceHolder型)
	 * @param format	フォーマット(int型)
	 * @param width	幅(int型)
	 * @param height	高さ(int型)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		camera.stopPreview();
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(width, height);

		// サポートされているカメラサイズのリストを取得
		List<Size> supportedPictureSizes  = parameters.getSupportedPictureSizes();
		Size pictureSize = null;
		if ( supportedPictureSizes != null &&
				supportedPictureSizes.size() > 0 ){ 
			pictureSize = supportedPictureSizes.get(0);
			//画像サイズを機種で制限する
			int maxSize = 3264;
			int nHeight = 2448; // 8M(3:4)
			if(Build.MODEL.compareTo("SO-01B") == 0) { // Xperia
				maxSize = 3264;
				nHeight = 2448;
			}else if(Build.MODEL.compareTo("SC-02B") == 0) { // Galaxy S
				maxSize = 2560;
				nHeight = 1920;
			}
			if(maxSize > 0){
				for(Size size : supportedPictureSizes){    			
					if(maxSize == Math.max(size.width,size.height)
							&& nHeight == Math.min(size.width, size.height)){
						pictureSize = size;
						break;
					}						
				}
			}
		}
		
		// リストが取得できなかったときはデフォルトサイズを設定
		if(pictureSize == null) {
			parameters.setPictureSize(2048, 1536); // 3M Pixel
		}else {
			parameters.setPictureSize(pictureSize.width, pictureSize.height);
			parameters.setPreviewSize(640, 480); // pictureSizeと同じアスペクト比にする
		}

		camera.setParameters(parameters);
		camera.startPreview();
	}
	
	/**
	 * サーフェス破棄時処理<br />
	 * @param holder	ホルダー(SurfaceHolder型)
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}
	
	/** 各種インターフェイス */
	protected void setPictureFormat(int format) {}
	protected void setPreviewSize(int width, int height) {}
	protected void setAntibanding(String antibanding) {}
	protected void setColorEffect(String effect) {}
	protected void setFlashMode(String flash_mode) {}
	protected void setFocusMode(String focus_mode) {}
	protected void setSceneMode(String scene_mode) {}
	protected void setWhiteBalance(String white_balance) {}
}
