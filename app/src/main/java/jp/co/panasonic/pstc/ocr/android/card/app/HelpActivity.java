package jp.co.panasonic.pstc.ocr.android.card.app;

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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * ヘルプアクティビティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class HelpActivity extends Activity {
	
	/** 変数・定数宣言 */
	private LinearLayout	displayLayout;		// 画面全体レイアウト
	
	private TableLayout	headerLayout;		// ヘッダーレイアウト
	private TableRow		headerRow;			// ヘッダー行
	private TextView		headerText;			// ヘッダーラベル
	
	private TableLayout	mainLayout;			// メインレイアウト
	private TableRow		titleRow;			// タイトル行
	private TextView		titleText;			// タイトルラベル
	private TableRow		corpRow;			// 会社名行
	private TextView		corpText;			// 会社名ラベル
	
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
    	Log.d("SettingActivity", "onCreate()");

    	// 画面サイズ取得
		Display disp = ActivityUtil.getDefaultDisplay(this);
		int width  = disp.getWidth();
		int height = disp.getHeight();
		
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
				headerText.setText("ヘルプ画面");
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
				mainLayoutParams.setMargins(15, 15, 15, 15);
				mainLayout.setLayoutParams(mainLayoutParams);
				
				// タイトル行
				titleRow = new TableRow(this);
				titleRow.setGravity(Gravity.CENTER_VERTICAL);
				mainLayout.addView(titleRow);
				
				// タイトルラベル
				titleText = new TextView(this);
				titleText.setText("活字認識サンプル");
				titleText.setTextColor(Color.BLACK);
				titleText.setTextSize(16.0f);
				titleText.setGravity(Gravity.CENTER_VERTICAL);
				TableRow.LayoutParams titleTextParams = new TableRow.LayoutParams( ( width - 130 ), 60);
				titleTextParams.setMargins(15, 15, 15, 15);
				titleText.setLayoutParams(titleTextParams);
				titleRow.addView(titleText);
				
				// 会社名行
				corpRow = new TableRow(this);
				corpRow.setGravity(Gravity.CENTER_VERTICAL);
				mainLayout.addView(corpRow);
				
				// 会社名ラベル
				corpText = new TextView(this);
				corpText.setText("(C) パナソニック ソリューションテクノロジー株式会社 2011-2012");
				corpText.setTextColor(Color.BLACK);
				corpText.setTextSize(16.0f);
				corpText.setGravity(Gravity.CENTER_VERTICAL);
				TableRow.LayoutParams corpTextParams = new TableRow.LayoutParams( ( width - 130 ), 60);
				corpTextParams.setMargins(15, 20, 15, 15);
				corpText.setLayoutParams(corpTextParams);
				corpRow.addView(corpText);
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
			
			// ヘルプアクティビティ終了
			finish();
		}
	}
	
	/** イベントリスナー [END] ================================= */

}
