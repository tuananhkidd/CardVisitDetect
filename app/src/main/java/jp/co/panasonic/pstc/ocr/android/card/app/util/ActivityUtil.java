package jp.co.panasonic.pstc.ocr.android.card.app.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * アクティビティユーティリティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class ActivityUtil {
	
	/** 変数・定数宣言 */
	
	
	/**
	 * 画面情報取得処理<br />
	 * @param context	コンテキスト(Context型)
	 * @return	ディスプレイ(Display型)
	 * 			画面横幅:display.getWidth(),
	 * 			画面縦幅:display.getHeight()
	 */
	public static Display getDefaultDisplay(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay();
	}
	
	/**
	 * 画面傾斜取得処理<br />
	 * @param context	コンテキスト(Context型)
	 * @return 向き(int型)
	 * 			Surface.ROTATION_0		傾きなし(0度)
	 * 			Surface.ROTATION_90		90度傾き
	 * 			Surface.ROTATION_180	180度傾き
	 * 			Surface.ROTATION_270	270度傾き
	 */
	public static int getOrientation(Context context) {
		return getDefaultDisplay(context).getOrientation();
	}

}
