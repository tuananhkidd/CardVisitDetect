package jp.co.panasonic.pstc.ocr.android.card.app.progress;

import jp.co.panasonic.pstc.ocr.android.card.app.ocr.RecogCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * プログレスバックグラウンドスレッドクラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class ProgressAsyncTask extends AsyncTask<Object, Integer, Object> {

	/** 変数・定数宣言 */
	private Activity		activity;		// 呼出元アクティビティ
	private ProgressDialog	progressDialog;	// プログレスダイアログ
	private Handler		handler;		// ハンドラー
	
	private String	progressTitle;			// プログレスバータイトル
	private String	progressMessage;		// プログレスバーメッセージ
	private int	progressCountUp;		// プログレスバーカウントアップ値
	private int	progressCountUpMills;	// プログレスバーカウントアップ間隔(ミリ秒)
	private int	progressMaxCount;		// プログレスバー最大値
	
	protected RecogCallback callback; // プログレスコールバック
	
	
	/**
	 * コンストラクタ<br />
	 * @param activity	アクティビティ(Activity型)
	 */
	public ProgressAsyncTask(Activity activity) {  
		this.activity = activity;
		this.progressDialog = null;
		this.callback = new RecogCallback();
	}
	
	/**
	 * バックグラウンド準備処理<br />
	 */
	@Override  
	protected void onPreExecute() {
        Log.d("ProgressAsyncTask", "onPreExecute()");
		
        // プログレスバー設定
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle(progressTitle);
		progressDialog.setMessage(progressMessage);
		progressDialog.setIndeterminate(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMax(progressMaxCount);
		progressDialog.show();
		
		// ハンドラー設定  
		handler = new Handler(); 
	}
	
	/**
	 * バックグラウンド処理<br />
	 * @param obj	オブジェクトリスト(Object型)
	 */
	@Override  
	protected Object doInBackground(Object... obj) {
        Log.d("ProgressAsyncTask", "doInBackground()");
        
        // カウントアップスレッド開始
        CountUpThread counter = new CountUpThread();
        counter.start();
        
        // バックグラウンド処理実行
        execBackground();
        
        // カウントアップ完了まで待機
        while( counter.getCount() < progressMaxCount ){}
        
        // カウントアップスレッド終了
        try {
        	counter.stop();
        } catch (java.lang.UnsupportedOperationException uoe) {
        	// 特に何もしない
        }
		
		return new String("success");
	}
	
	/**
	 * バックグラウンド実行処理<br />
	 */
	public void execBackground() {	}
	
	/**
	 * プログレス更新処理<br />
	 * @param progress	プログレス(Integer型)
	 */
	@Override  
	protected void onProgressUpdate(Integer... progress) {
        Log.d("ProgressAsyncTask", "onProgressUpdate()");
        
        // プログレスバーを更新
        progressDialog.setProgress( progress[0]);
	}
	
	/**
	 * バックグラウンド後処理<br />
	 * @param result	結果(Object型)
	 */
	@Override  
	protected void onPostExecute(Object result) {
        Log.d("ProgressAsyncTask", "onPostExecute()");
		
        // プログレスダイアログを非表示
        progressDialog.dismiss();
        
        // 結果を表示
        if (result == null) {
        	// エラー
        	Toast.makeText( activity, "This processing was error!", Toast.LENGTH_LONG ).show();
        }
        
        // ハンドラーにUIスレッドへの値設定処理をPOST
        handler.post(new Runnable() {
        	public void run() {
        		
        		// バックグラウンド後実行
        		execNext();
        		
        		// アクティビティ終了
        		activity.finish();
        	}
        });
		
	}
	
	/**
	 * バックグラウンド後実行処理<br />
	 */
	public void execNext() {}

	/** アクセサ [START] =================================== */
	
	/**
	 * プログレスバータイトルアクセサ
	 */
	public String getProgressTitle() {
		return progressTitle;
	}
	public void setProgressTitle(String progressTitle) {
		this.progressTitle = progressTitle;
	}
	
	/**
	 * プログレスバーメッセージアクセサ
	 */
	public String getProgressMessage() {
		return progressMessage;
	}
	public void setProgressMessage(String progressMessage) {
		this.progressMessage = progressMessage;
	}

	/**
	 * プログレスバーカウントアップ値アクセサ
	 */
	public int getProgressCountUp() {
		return progressCountUp;
	}
	public void setProgressCountUp(int progressCountUp) {
		this.progressCountUp = progressCountUp;
	}
	
	/**
	 * プログレスバーカウントアップ間隔(ミリ秒)アクセサ
	 */
	public int getProgressCountUpMills() {
		return progressCountUpMills;
	}
	public void setProgressCountUpMills(int progressCountUpMills) {
		this.progressCountUpMills = progressCountUpMills;
	}

	/**
	 * プログレスバー最大値アクセサ
	 */
	public int getProgressMaxCount() {
		return progressMaxCount;
	}
	public void setProgressMaxCount(int progressMaxCount) {
		this.progressMaxCount = progressMaxCount;
	}
	
	/** アクセサ [END] ===================================== */


	
	/** カウントアップスレッド [START] ============================ */
	
	class CountUpThread extends Thread {
		
		/** 変数・定数宣言 */
		private int count;	// カウンタ
		
		
		/**
		 * スレッド実行処理<br />
		 */
		@Override
		public void run() {
			count = 0;
			int count2 = 0;
			while (true) {
				
				// プログレスバー最大値チェック
				if( count < progressMaxCount ){
					// 最大値に達していない場合、プログレスバー更新
					progressDialog.setProgress(callback.getProgress());
					count = callback.getProgress();
					count2 += progressCountUp;
					if(count2 > progressMaxCount) {
						count = 101;
					}
				}
				
				// 待機(ミリ秒)
				try{ Thread.sleep(progressCountUpMills); }catch(InterruptedException e){}
			}
		}

		/**
		 * カウンタアクセサ
		 */
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}

	}
	
	/** カウントアップスレッド [END] ============================== */

}
