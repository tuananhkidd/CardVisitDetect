package jp.co.panasonic.pstc.ocr.android.card.app;

import jp.co.panasonic.pstc.ocr.android.card.app.camera.CameraData;
import jp.co.panasonic.pstc.ocr.android.card.app.R;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.BufferType;

/**
 * OCR認識結果表示アクティビティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class ResultActivity extends Activity {
	
	/** 変数・定数宣言 */
	private EditText		resultEdit;			// 認識結果テキストボックス
	private Button			topButton;			// 「トップ画面へ」ボタン
	
	/** アクティビティ [START] ================================ */

	/**
	 * アクティビティ生成時処理<br />
	 * アクティビティが生成された場合にコールされる。
	 * @param savedInstanceState ステータス(Bundle型)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        Log.d("ResultActivity", "onCreate()");
        
		// カメラデータ取得
		CameraData cameraData = CameraData.getInstance();
		
		this.setContentView(R.layout.restext);
		String str = getString(R.string.app_name) + "認識結果";
		this.setTitle(str);
		
		topButton = ((Button)this.findViewById( R.id.gototop ));
		topButton.setOnClickListener(new ExecTopButtonOnClick());
		
		resultEdit = ((EditText)this.findViewById(R.id.result_txt));
		resultEdit.setText( cameraData.getOcrResult(), BufferType.NORMAL );
	
	}
	
	/** アクティビティ [END] ================================== */
	
	
	
	/** イベントリスナー [START] =============================== */
	
	/**
	 * 「トップ画面へ」ボタン押下イベント処理クラス
	 * 
	 * 
	 */
	private class ExecTopButtonOnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			
			// OCR認識結果表示アクティビティ終了
			finish();
		}
	}
	
	/** イベントリスナー [END] ================================= */

}
