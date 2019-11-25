package jp.co.panasonic.pstc.ocr.android.card.app.camera;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import jp.co.panasonic.pstc.ocr.android.card.app.CameraProgress;
import jp.co.panasonic.pstc.ocr.android.card.app.util.BitmapUtil;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * カメラアクティビティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class CameraActivity extends Activity {
	
	/** 変数・定数宣言 */
	private Activity		activity;		// アクティビティ
	private CameraPreview	cameraPreview;	// カメラプレビュー
	
	
	/** アクティビティ [START] ================================ */

	/**
	 * アクティビティ生成時処理<br />
	 * アクティビティが生成された場合にコールされる。
	 * @param savedInstanceState ステータス(Bundle型)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	Log.d("MainActivity", "onCreate()");
    	
    	// タイトルバー非表示
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		// フルスクリーン表示
		getWindow().addFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN );
		
		// カメラプレビュー表示
		cameraPreview = new CameraPreview(this);
		setContentView(cameraPreview);
		
		// アクティビティ保持
		this.activity = this;
		
		// SDカード表示フラグをFALSEへ設定
		CameraData cameraData = CameraData.getInstance();
		cameraData.setSdcard(false);
	}

	/** アクティビティ [END] ================================== */
	
	
	
	/** イベントリスナー [START] =============================== */
	
	/**
	 * タッチイベント処理<br />
	 * @param event	モーションイベント(MotionEvent型)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// タッチイベント取得
		if( event.getAction() == MotionEvent.ACTION_DOWN ){
			
			// カメラ撮影
			autoFocus();
		}
		return true;
	}

	/** イベントリスナー [END] ================================= */
	
	
	
	/** カメライベント [START] ================================ */
	
	/**
	 * オートフォーカス処理
	 */
	public void autoFocus() {
		cameraPreview.camera.autoFocus(autoFocus);
	}
	
	/**
	 * オートフォーカスコールバック処理
	 */
	AutoFocusCallback autoFocus = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, final Camera camera) {
			
			// ピクチャーコールバック
			PictureCallback jpeg = new PictureCallback() {
				
				/**
				 * ピクチャートークン処理<br />
				 * @param data		データ(byte[]型)
				 * @param camera	カメラ(Camera型)
				 */
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					
					// カメラ画像をファイル保存
					{
						FileOutputStream fos = null;
						try {
							Date dt = new Date();
							long tm = dt.getTime() / 1000 + (365 * 2 - 92 + 3) * 24 * 3600;
							fos = new FileOutputStream("/sdcard/data/jp.co.panasonic.jp.pstc.ocr" + tm + ".jpg");
							fos.write(data);
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							if (fos != null) {
								try {
									fos.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}

					// カメラデータ取得
					CameraData cameraData = CameraData.getInstance();
					
					// カメラデータ変換
					{
						// カメラデータ初期化(必須)
						cameraData.init();
						
						int resize = 0;
						
						// BITMAPデータに変換( 1/2リサイズ )
						Bitmap cameraBitmap = BitmapUtil.convBitmap( data, Bitmap.Config.ARGB_8888, resize );
						
						// カメラデータに格納
						cameraData.setBitmapData(cameraBitmap);
					}
					
					// プレビュー表示フラグチェック
					if( cameraData.isPreview() ){
						// プレビュー表示あり
						
						// カメラ撮影画像プレビュー表示
						Intent intent = new Intent( 
												getApplicationContext(),
												jp.co.panasonic.pstc.ocr.android.card.app.CameraPicturePreviewActivity.class
										);
						startActivity(intent);
						
						// カメラアクティビティ終了
		        		finish();
					}
					else {
						// プレビュー表示なし
						
						// プログレスバー表示＆バックグラウンド処理開始
						CameraProgress progress = new CameraProgress(activity, true);
						progress.setProgressTitle("Processing");		// プログレスタイトル
						progress.setProgressMessage("Please Wait...");	// プログレスメッセージ
						progress.setProgressCountUp(1);					// カウントアップ値
						progress.setProgressCountUpMills(250);			// カウントアップ間隔(ミリ秒)
						progress.setProgressMaxCount(100);				// 最大カウント値
						progress.execute("request");
					}

				}
			};
			camera.takePicture(null, null, jpeg);
		}

	};
	
	/** カメライベント [END] ================================== */

}
