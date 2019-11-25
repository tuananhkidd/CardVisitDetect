package jp.co.panasonic.pstc.ocr.android.card.app;

import java.io.IOException;
import java.io.InputStream;

import jp.co.panasonic.pstc.ocr.android.card.app.camera.CameraData;
import jp.co.panasonic.pstc.ocr.android.card.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * カメラ撮影画像プレビューアクティビティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class CameraPicturePreviewActivity extends Activity {

	/** 変数・定数宣言 */
	private Activity		activity;					// アクティビティ
	
	private Button			cameraButton;				// 「再撮影」ボタン
	private Button			convertButton;				// 「変換」ボタン
	
	/** アクティビティ [START] ================================ */

	/**
	 * アクティビティ生成時処理<br />
	 * アクティビティが生成された場合にコールされる。
	 * @param savedInstanceState ステータス(Bundle型)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        Log.d("CameraPicturePreviewActivity", "onCreate()");
        
        this.setContentView( R.layout.preview );
		String str = getString(R.string.app_name) + "プレビュー";
		this.setTitle(str);
		
		// カメラデータ取得
		CameraData cameraData = CameraData.getInstance();
		Bitmap bitmap = cameraData.getBitmapData();
		
		ImageView previewImage = ((ImageView)this.findViewById( R.id.Camerapreview ));
		previewImage.setBackgroundColor( Color.argb(255, 220, 220, 220) );

		// 
		Resources resources = getResources();
		Configuration config = resources.getConfiguration();
		switch(config.orientation) {
		case Configuration.ORIENTATION_PORTRAIT:
			Log.d( "8558", "getConfiguration:vertical");
			previewImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			Log.d( "8558", "getConfiguration:horizontal");
			previewImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
			break;
		}
		previewImage.invalidate();

    	// カメラ撮影画像ビットマップ表示
    	previewImage.setImageBitmap(bitmap);

    	// ボタンの設定
    	Button button;
		if( cameraData.isSdcard() ){
			cameraButton = ((Button)this.findViewById( R.id.Retake ));
			cameraButton.setText("再選択");
			button =  ((Button)this.findViewById( R.id.Reselect ));
			button.setVisibility(View.INVISIBLE);
		} else {
			cameraButton = ((Button)this.findViewById( R.id.Retake ));
			button =  ((Button)this.findViewById( R.id.Reselect ));
			button.setVisibility(View.INVISIBLE);
		}
		cameraButton.setOnClickListener( new ExecCameraButtonOnClick() );
		
		convertButton =  ((Button)this.findViewById( R.id.Recognize ));
		convertButton.setOnClickListener( new ExecConvertButtonOnClick() );
		
		// アクティビティ
		activity = this;
	}

	/** アクティビティ [END] ================================== */
	
	
	
	/** イベントリスナー [START] =============================== */
	
	/**
	 * 「カメラ撮影」ボタン押下イベント処理クラス
	 * 
	 * 
	 */
	private class ExecCameraButtonOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			// SDカード表示フラグチェック
			CameraData cameraData = CameraData.getInstance();
			if( cameraData.isSdcard() ){
				// SDカード画像
				
				// フォトギャラリー表示
				Intent intent = new Intent( Intent.ACTION_PICK );
				intent.setType("image/*");
				startActivityForResult(intent, MainActivity.PHOTO_GALLERY);
			}
			else {
				// カメラ画像
				
				// カメラアクティビティ表示
				Intent intent =  new Intent( 
											getApplicationContext(),
											jp.co.panasonic.pstc.ocr.android.card.app.camera.CameraActivity.class
								);
				startActivity(intent);
				
				// カメラ撮影画像プレビューアクティビティ終了
				finish();
			}

		}
	}
	
	/**
	 * 「変換」ボタン押下イベント処理クラス
	 * 
	 * 
	 */
	private class ExecConvertButtonOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			
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
	
	/** イベントリスナー [END] ================================= */
	
	
	
	/** 他アクティビティ結果取得 [START] =========================== */
	
	/**
	 * カメラアクティビティ呼出結果取得処理<br />
	 * @param requestCode	リクエストコード(int型)
	 * @param resultCode	リザルトコード(int型)
	 * @param intent		インテント(Intent型)
	 */
	@Override
	protected void onActivityResult(
							int requestCode,
							int resultCode,
							Intent intent) {
		Log.d("CameraPicturePreviewActivity", "onActivityResult()");
		
		switch (requestCode) {
		case MainActivity.PHOTO_GALLERY:
			// フォトギャラリー
			if( resultCode != RESULT_OK ) return;
			
			// カメラデータ取得
			CameraData cameraData = CameraData.getInstance();
			
			// カメラデータ変換
			{
				// カメラデータ初期化(必須)
				cameraData.init();
				
				// 画像データを取得
				Bitmap bitmap = null;
				{
					// 画像URIを取得
					Uri photoUri = intent.getData();
					if( photoUri == null ) return;
					
					// ビットマップ画像を取得
					try{
						BitmapFactory.Options opt = new BitmapFactory.Options();
						opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
						InputStream is = getContentResolver().openInputStream(photoUri);
						// 情報を取得する
						opt.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(is, null, opt);
						opt.inJustDecodeBounds = false;
						// サイズが大きい場合は1/2にリサイズ
						if(opt.outWidth == 3264) opt.inSampleSize = 2; 
						if(opt.outWidth == 2560) opt.inSampleSize = 2; 
						if(opt.outWidth == 3507) opt.inSampleSize = 2; 
						if(opt.outWidth == 2480) opt.inSampleSize = 2;
						// 画像のロード
						is = getContentResolver().openInputStream(photoUri);
						bitmap = BitmapFactory.decodeStream(is, null, opt);
					}catch (IOException e) {
						
					}
				}
				if( bitmap == null ) return;
				
				// カメラデータに格納
				cameraData.setBitmapData(bitmap);
			}
			
			// プレビュー表示フラグチェック
			if( cameraData.isPreview() ){
				// プレビュー表示あり
				
				// カメラ撮影画像プレビュー表示
				Intent cameraIntent = new Intent( 
											getApplicationContext(),
											jp.co.panasonic.pstc.ocr.android.card.app.CameraPicturePreviewActivity.class
										);
				startActivity(cameraIntent);
				
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
			
			break;
		default:
			break;
		}
		
	}
	
	/** 他アクティビティ結果取得 [END] ============================= */

}
