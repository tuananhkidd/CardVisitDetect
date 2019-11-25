package jp.co.panasonic.pstc.ocr.android.card.app.camera;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * カメラデータクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class CameraData {
	
	/** 変数・定数宣言 */
	private static CameraData instance;	// カメラデータインスタンス
	
	// カメラ関連
	private Bitmap		bitmapData;	// ビットマップデータ
	private boolean	preview;	// プレビュー表示フラグ
	
	// SDカード関連
	private boolean	sdcard;		// SDカード表示フラグ
	
	// OCR認識
	private String	ocrResult;		// OCR認識結果
	
	
	// 処理時間
	private long procTime;
	
	/**
	 * インスタンス取得処理<br />
	 * @return	インスタンス(BusinessCardData型)
	 */
	public static CameraData getInstance() {
		if( instance == null ){
			synchronized( CameraData.class ) {
				if( instance == null ) instance = new CameraData();
			}
		}
		return instance;
	}
	
	/**
	 * 初期化処理
	 */
	public void init() {
		// ビットマップデータ
		if( bitmapData != null ) bitmapData.recycle();
		bitmapData = null;
		// OCR認識結果
		ocrResult = null;
	}

	/**
	 * ビットマップデータアクセサ
	 */
	public Bitmap getBitmapData() {
		return bitmapData;
	}
	public void setBitmapData(Bitmap bitmapData) {
		if (this.bitmapData != null) {
			this.bitmapData.recycle();
			this.bitmapData = null;			
		}
		this.bitmapData = bitmapData;
	}
	
	/**
	 * プレビュー表示フラグアクセサ
	 */
	public boolean isPreview() {
		return preview;
	}
	public void setPreview(boolean preview) {
		this.preview = preview;
	}
	
	/**
	 * SDカード表示フラグアクセサ
	 */
	public boolean isSdcard() {
		return sdcard;
	}
	public void setSdcard(boolean sdcard) {
		this.sdcard = sdcard;
	}

	/**
	 * OCR認識結果アクセサ
	 */
	public String getOcrResult() {
		return ocrResult;
	}
	public void setOcrResult(String ocrResult) {
		this.ocrResult = ocrResult;
	}

	/**
	 * OCR処理時間を取得します。<br>
	 * 
	 * @return OCR処理時間
	 */
	public long getProcTime() {
		return procTime;
	}

	/**
	 * OCR処理時間をセットします。<br>
	 */
	public void setProcTime(long procTime) {
		this.procTime = procTime;
	}

	/**
	 * 破棄処理
	 */
	public void destroy() {
		// ビットマップデータ
		if( bitmapData != null ) bitmapData.recycle();
		bitmapData = null;
		// OCR認識結果
		ocrResult = null;
		// インスタンス破棄
		instance = null;
	}

	private static int		m_maxImageSize = 2592 * 1944; //3264 * 2448;

	/**
	 * 画像の最大サイズを取得
	 * 
	 * @return 画像の最大サイズ
	 */
	public static int getMaxImageSize() {
		return m_maxImageSize;
	}

	/**
	 * 画像の最大サイズをセット
	 * 
	 * @param pix
	 */
	public static void setMaxImageSize(int pix) {
		m_maxImageSize = pix;
	}
	
	/**
	 * OCRエンジンに渡す画像用バッファー
	 */
	protected static ByteBuffer buffer = null;

	/**
	 * バッファーの取得
	 * 
	 * @return 
	 */
	public static ByteBuffer GetBuffer()
	{
		if (buffer == null) {
			Log.d("CameraData",  "XXXXXXXXXXXXX Direct Allocate XXXXXXXXXXXXXXXXXX");

			while (true) {
				int s = CameraData.getMaxImageSize();
				Log.d("CameraData",  "Max image size is " + s);
				try {
					buffer = ByteBuffer.allocateDirect(4 * s + 1024);
					break;
				} catch (java.lang.OutOfMemoryError ex) {
				}
				s *= 0.85;
				CameraData.setMaxImageSize(s);
			}
		}
		return buffer;
	}

}
