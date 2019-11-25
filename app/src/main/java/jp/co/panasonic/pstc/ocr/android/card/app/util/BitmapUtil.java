package jp.co.panasonic.pstc.ocr.android.card.app.util;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;

/**
 * ビットマップユーティリティクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class BitmapUtil {
	
	/** 変数・定数宣言 */
	
	
	/**
	 * ビットマップデータ変換処理<br />
	 * @param data		データ(byte[]型)
	 * @param offset	オフセット(int型)
	 * @param length	データ長(int型)
	 * @return Bitmap	ビットマップデータ
	 */
	public static Bitmap convBitmap(byte[] data,int offset, int length) {
		return BitmapFactory.decodeByteArray( data, offset, length );
	}
	
	/**
	 * ビットマップデータ縮小変換処理<br />
	 * @param data		画像データ(byte[]型)
	 * @param config	ファイルフォーマット(Config型)	Bitmap.Config.ARGB_8888など
	 * @param resize	リサイズ(int型)					8と指定すると、1/8となる
	 * @return Bitmap	縮小後のビットマップデータ
	 */
	public static Bitmap convBitmap(byte[] data, Config config, int resize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		if(resize != 0) options.inSampleSize = resize;
		options.inPreferredConfig = config;
		Bitmap bitmap = BitmapFactory.decodeByteArray( data, 0, data.length, options );
//		return bitmap.copy( config, true );
		return bitmap;
	}
	
	/**
	 * ビットマップデータ自動リサイズ処理<br />
	 * @param bitmap		ビットマップデータ(Bitmap型)
	 * @param baseWidth	ベース横サイズ幅(int型)
	 * @param baseHeight	ベース縦サイズ幅(int型)
	 * @return	リサイズ後のビットマップデータ(Bitmap型)
	 */
	public static Bitmap resizeAutoBitmap(Bitmap bitmap, int baseWidth, int baseHeight, long byteSize) {
		
		// 画像データ縮小サイズ設定
		int resizeRes     = bitmap.getRowBytes();
		int resizeWidth   = bitmap.getWidth()  / 20;
		int resizeHeight  = bitmap.getHeight() / 20;
		
		// 画像データ縮小
		int  buffWidth  = bitmap.getWidth();
		int  buffHeight = bitmap.getHeight();
		long buffKByteSize = buffWidth * buffHeight;
		buffKByteSize = buffKByteSize * ( resizeRes / 1024 );
		boolean resize = false;
		while(!resize){
			if( buffKByteSize <= byteSize ) resize = true;
			buffWidth  = buffWidth  - resizeWidth;
			buffHeight = buffHeight - resizeHeight;
			buffKByteSize = buffWidth * buffHeight;
			buffKByteSize = buffKByteSize * ( resizeRes / 1024 );
		}
		return BitmapUtil.resizeBitmap( bitmap, buffWidth, buffHeight );
	}
	
	/**
	 * ビットマップデータリサイズ処理<br />
	 * @param bitmap		ビットマップデータ(Bitmap型)
	 * @param resizeWidth	リサイズ幅(int型)
	 * @param resizeHeight	リサイズ縦(int型)
	 * @return Bitmap	リサイズ後のビットマップデータ(Bitmap型)
	 */
	public static Bitmap resizeBitmap(Bitmap bitmap, int resizeWidth, int resizeHeight) {
		Bitmap ret = Bitmap.createBitmap( resizeWidth, resizeHeight, Bitmap.Config.ARGB_8888 );
		Canvas canvas = new Canvas(ret);
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		drawable.setBounds( 0, 0, resizeWidth, resizeHeight );
		drawable.draw(canvas);
		return ret;
	}
	
	/**
	 * ビットマップデータ回転処理<br />
	 * @param bitmap	ビットマップデータ(Bitmap型)
	 * @param rotate	傾き(int型)
	 * @return	Bitmap	回転後のビットマップデータ(Bitmap型)
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		Matrix matrix = new Matrix();
		matrix.postScale(1.0f, 1.0f);
		matrix.postRotate(rotate);
		return Bitmap.createBitmap( bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}
	
	/**
	 * SDカードへファイル出力処理<br />
	 * @param data		画像データ(byte[]型)
	 * @param path		ファイルパス(String型)
	 */
	public static void writeSDCard(byte[] data, String path) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(path);
			fos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try{
					fos.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * ビットマップデータをバイト配列へ変換する処理<br />
	 * @param bitmap	ビットマップデータ(Bitmap型)
	 * @return	画像データ(byte[]型)
	 */
	public static byte[] getByteArray(Bitmap bitmap) {
		byte[] data = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try{
			bitmap.compress( CompressFormat.JPEG, 100, bos );
			bos.flush();
			data = bos.toByteArray();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		finally{
			if( bos != null ){
				try{
					bos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

}
