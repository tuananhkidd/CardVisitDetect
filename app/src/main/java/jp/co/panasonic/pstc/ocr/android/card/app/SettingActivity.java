package jp.co.panasonic.pstc.ocr.android.card.app;

import jp.co.panasonic.pstc.ocr.android.card.app.camera.CameraData;
import jp.co.panasonic.pstc.ocr.android.card.app.util.ActivityUtil;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 設定アクティビティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class SettingActivity extends Activity {
	
	/** 変数・定数宣言 */
	private LinearLayout	displayLayout;		// 画面全体レイアウト
	
	private TableLayout	headerLayout;		// ヘッダーレイアウト
	private TableRow		headerRow;			// ヘッダー行
	private TextView		headerText;			// ヘッダーラベル
	
	private TableLayout	mainLayout;			// メインレイアウト
	private TableLayout	settingLayout;		// 設定レイアウト
	private TableRow		previewRow;			// プレビュー行
	private TextView		previewTitleText;	// プレビュータイトルラベル
	private CheckBox		previewCheckBox;	// プレビューチェックボックス
	
	private TableLayout	futterLayout;		// フッターレイアウト
	private TableRow		buttonRow;			// ボタン行
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
//		Toast.makeText(this, "SettingActivity#onCreate()", Toast.LENGTH_SHORT).show();
    	Log.d("SettingActivity", "onCreate()");

    	// 画面サイズ取得
		Display disp = ActivityUtil.getDefaultDisplay(this);
		int width  = disp.getWidth();
		int height = disp.getHeight();
		
		// カメラデータ取得
		CameraData cameraData = CameraData.getInstance();
		
		// 画面全体レイアウト
		{
			// タイトルバー非表示
			requestWindowFeature( Window.FEATURE_NO_TITLE );
			
			// 画面全体レイアウト設定
			displayLayout = new LinearLayout(this);
			displayLayout.setOrientation(LinearLayout.VERTICAL);
			displayLayout.setBackgroundColor( Color.argb(255, 255, 255, 255) );
			LinearLayout.LayoutParams displayLayoutParams = new LinearLayout.LayoutParams(width, height);
			displayLayout.setLayoutParams(displayLayoutParams);
			setContentView(displayLayout);
			
			// ヘッダーレイアウト
			int headerWidth, headerHeight;
			{
				headerWidth  = width;
				headerHeight = 30;
				headerLayout = new TableLayout(this);
				headerLayout.setGravity(Gravity.CENTER_VERTICAL);
				headerLayout.setBackgroundColor( Color.argb(128, 200, 200, 255) );
				TableLayout.LayoutParams headerLayoutParams = new TableLayout.LayoutParams( headerWidth, headerHeight );
				headerLayout.setLayoutParams(headerLayoutParams);
				
				// ヘッダー行
				headerRow = new TableRow(this);
				headerRow.setGravity(Gravity.CENTER);
				headerLayout.addView(headerRow);
				
				// ヘッダーラベル
				headerText = new TextView(this);
				headerText.setText("設定画面");
				headerText.setTextColor( Color.BLUE );
				TableRow.LayoutParams headerTextParams = new TableRow.LayoutParams();
				headerText.setLayoutParams(headerTextParams);
				headerRow.addView(headerText);
			}
			
			// フッターレイアウト
			int futterWidth, futterHeight;
			{
				futterWidth  = width;
				futterHeight = 70;				
				futterLayout = new TableLayout(this);
				futterLayout.setGravity(Gravity.CENTER_VERTICAL);
				futterLayout.setBackgroundColor( Color.argb(128, 200, 200, 255) );
				TableLayout.LayoutParams futterLayoutParams = new TableLayout.LayoutParams( futterWidth, futterHeight );
				futterLayout.setLayoutParams(futterLayoutParams);
				
				// ボタン行
				buttonRow = new TableRow(this);
				buttonRow.setGravity(Gravity.CENTER);
				futterLayout.addView(buttonRow);
				
				// 「トップ画面へ」ボタン
				topButton = new Button(this);
				topButton.setText("トップ画面へ");
				TableRow.LayoutParams topButtonParams = new TableRow.LayoutParams(200, 60);
				topButton.setLayoutParams(topButtonParams);
				buttonRow.addView(topButton);
				topButton.setOnClickListener( new ExecTopButtonOnClick() );
			}
			
			// メインレイアウト
			int mainWidth, mainHeight;
			{
				mainWidth  = width;
				mainHeight = height - headerHeight - futterHeight - 30;
				mainLayout = new TableLayout(this);
				mainLayout.setGravity(Gravity.CENTER_VERTICAL);
				mainLayout.setBackgroundColor( Color.argb(255, 255, 255, 255) );
				TableLayout.LayoutParams mainLayoutParams = new TableLayout.LayoutParams( mainWidth, mainHeight );
				mainLayout.setLayoutParams(mainLayoutParams);
				
				// 設定レイアウト
				int settingWidth  = mainWidth  - 30;
				int settingHeight = mainHeight - 30;
				settingLayout = new TableLayout(this);
				settingLayout.setGravity(Gravity.CENTER);
				settingLayout.setBackgroundColor( Color.argb(255, 255, 255, 160) );
				TableLayout.LayoutParams settingLayoutParams = new TableLayout.LayoutParams( settingWidth, settingHeight );
				settingLayoutParams.setMargins(15, 15, 15, 15);
				settingLayout.setLayoutParams(settingLayoutParams);
				mainLayout.addView(settingLayout);
				
				// プレビュー行
				previewRow = new TableRow(this);
				previewRow.setGravity(Gravity.CENTER_VERTICAL);
				settingLayout.addView(previewRow);
				
				// プレビュータイトルラベル
				int previewWidth  = width - 130;
				int previewHeight = 60;
				previewTitleText = new TextView(this);
				previewTitleText.setText("認識する前にプレビューを表示");
				previewTitleText.setTextColor(Color.BLACK);
				previewTitleText.setTextSize(16.0f);
				previewTitleText.setGravity(Gravity.CENTER_VERTICAL);
				TableRow.LayoutParams previewTitleParams = new TableRow.LayoutParams( previewWidth, previewHeight );
				previewTitleParams.setMargins(15, 15, 15, 15);
				previewTitleText.setLayoutParams(previewTitleParams);
				previewRow.addView(previewTitleText);
				
				// プレビューチェックボックス
				previewCheckBox = new CheckBox(this);
				previewCheckBox.setGravity(Gravity.CENTER_VERTICAL);
				previewCheckBox.setChecked( cameraData.isPreview() );
				previewRow.addView(previewCheckBox);
			}
			
			// 画面全体レイアウト表示
			{
				// ヘッダーレイアウト
				displayLayout.addView(headerLayout);
				
				// メインレイアウト
				displayLayout.addView(mainLayout);
				
				// フッターレイアウト
				displayLayout.addView(futterLayout);
			}
			
		}

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
			
			// プレビュー表示フラグ設定
			CameraData cameraData = CameraData.getInstance();
			cameraData.setPreview( previewCheckBox.isChecked() );
			
			// 設定アクティビティ終了
			finish();
		}
	}
	
	/** イベントリスナー [END] ================================= */

}
