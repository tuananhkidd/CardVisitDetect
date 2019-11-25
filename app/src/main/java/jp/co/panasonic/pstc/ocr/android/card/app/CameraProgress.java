package jp.co.panasonic.pstc.ocr.android.card.app;

import jp.co.panasonic.pstc.ocr.android.card.app.camera.CameraData;
import jp.co.panasonic.pstc.ocr.android.card.app.ocr.ProcOcr;
import jp.co.panasonic.pstc.ocr.android.card.app.progress.ProgressAsyncTask;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

/**
 * カメラプログレスクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class CameraProgress extends ProgressAsyncTask {

	/** 変数・定数宣言 */
	private Activity	activity;		// アクティビティ
	private boolean	exitActivity;	// アクティビティ終了フラグ
	private boolean	result;			// 実行処理結果フラグ( true:成功, false:エラー )
	
	
	
	/** カウントアップスレッド [START] ============================ */
	
	/**
	 * コンストラクタ<br />
	 * @param activity		アクティビティ(Activity型)
	 * @param exitActivity	アクティビティ終了フラグ(boolean型)
	 */
	public CameraProgress(Activity activity, boolean exitActivity) {
		super(activity);
		this.activity = activity;
		this.exitActivity = exitActivity;
	}
	
	/**
	 * バックグラウンド実行処理<br />
	 */
	public void execBackground() {
		try{
			// OCR認識
			String ret = null;
			{
				// カメラ撮影データをバイト配列へ変換
				CameraData cameraData = CameraData.getInstance();
				Bitmap bitmap = cameraData.getBitmapData();
				int rb = bitmap.getRowBytes();
				int wb = bitmap.getWidth();
				int hb = bitmap.getHeight();
				int res = bitmap.getDensity();
				
				// OCR認識実行
				ProcOcr prococr = new ProcOcr();
				long sTime = System.currentTimeMillis();
				ret = prococr.ocrConvert( bitmap, rb, wb, hb, res, callback);
				long eTime = System.currentTimeMillis();
				cameraData.setProcTime(eTime - sTime);
				result = true;
				
				// GCをしておく
				System.gc();
				
	        	// カメラデータ初期化
				cameraData.init();
				
				// OCR認識結果
				if(result){
					// OCR認識結果を取得
					cameraData.setOcrResult(ret);
				}
			}
		}
		catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		
	}
	
	/**
	 * バックグラウンド後実行処理<br />
	 */
	public void execNext() {
		
		// OCR認識結果チェック
		if(result){
			// 成功
			
			// OCR認識結果表示アクティビティ表示
			Intent intent =  new Intent(
									activity.getApplicationContext(),
									jp.co.panasonic.pstc.ocr.android.card.app.ResultActivity.class
								);
			activity.startActivity(intent);
			
			// カメラアクティビティ終了フラグチェック
			if(exitActivity){
				activity.finish();
			}
		}
		else {
			// エラー
			
		}
		
	}
	
	/** カウントアップスレッド [END] ============================== */

}
