package jp.co.panasonic.pstc.ocr.android.card.app.ocr;


/**
 * 認識時のコールバック<br>
 * キャンセル処理、プログレスバーの処理に利用します。<br>
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class RecogCallback {
		/**
		 * 進捗状況
		 */
		private int progress;
		
		/**
		 * キャンセルフラグ
		 */
		private boolean isCancel;
		
		/**
		 * コンストラクタ
		 */
		public RecogCallback() {
			progress = 0;
			isCancel = false;
		}

		/**
		 * 進捗状況を取得します。<br>
		 * 
		 * @return 文字認識の進捗状況
		 */
		public int getProgress() {
			return progress;
		}

		/**
		 * 進捗状況をセットします。<br>
		 * 
		 * @param progress 文字認識の進捗状況
		 */
		public void setProgress(int progress) {
			this.progress = progress;		
		}

		/**
		 * 文字認識のキャンセル状態を返します。<br>
		 * 
		 * @return true:キャンセル false:続行
		 */
		public boolean isCancel() {
			return isCancel;
		}

		/**
		 * 文字認識をキャンセルします。<br>
		 * 
		 * @param isCancel true:キャンセル false:続行
		 */
		public void setCancel(boolean isCancel) {
			this.isCancel = isCancel;
		}
}
