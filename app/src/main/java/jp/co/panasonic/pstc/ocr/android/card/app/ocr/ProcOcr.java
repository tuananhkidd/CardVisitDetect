package jp.co.panasonic.pstc.ocr.android.card.app.ocr;

import jp.co.panasonic.pstc.ocr.card.CardRecog;
import jp.co.panasonic.pstc.ocr.card.CardEngineFactory;
import jp.co.panasonic.pstc.ocr.card.IDRCardEngine;
import jp.co.panasonic.pstc.ocr.common.IDRException;
import jp.co.panasonic.pstc.ocr.common.TrapeCoordinate;

import android.graphics.Bitmap;



/**
 * OCR認識クラス
 * 
 * @author Panasonic Solution Technologies Co., Ltd.
 *
 */
public class ProcOcr {
    
	/** OCR認識処理 [START] ================================ */
	
	/**
	 * OCR認識処理<br />
	 * @param bitmap		画像データ(ByteBuffer型)
	 * @param rd		ロウバイト(int型)
	 * @param wb		横バイト(int型)
	 * @param hb		縦バイト(int型)
	 * @param callback コールバックオブジェクト
	 */
	public String ocrConvert(Bitmap bitmap, int rd, int wb, int hb, int res, RecogCallback callback) {
		String str = "";
		int progress = 2;
		int resolution = 300;
		IDRCardEngine engine = null;
		
		try {
			// エンジンオブジェクトの取得
			engine = CardEngineFactory.getEngine("/sdcard/data/jp.co.panasonic.pstc.ocr/cardDict");
			
			progress += 3;
			callback.setProgress(progress);
			
			// 認識対象画像のセット
			engine.SetImage(bitmap, 32, resolution, resolution);
			progress += 3;
			callback.setProgress(progress);

			StringBuffer strBuf = new StringBuffer(10240);

			try {
				// 台形補正
				TrapeCoordinate trapeCoord = engine.GetTrapezoidCoodinate();
				strBuf.append(String.format("4点検出結果%n(左上：%d, %d),(右上：%d, %d), (右下：%d, %d), (左下：%d, %d)%n", trapeCoord.getTopLeft().x, trapeCoord.getTopLeft().y, 
												trapeCoord.getTopRight().x, trapeCoord.getTopRight().y,
												trapeCoord.getBottomRight().x, trapeCoord.getBottomRight().y,
												trapeCoord.getBottomLeft().x, trapeCoord.getBottomLeft().y));
				progress += 20;
				callback.setProgress(progress);

				// 補正後サイズに合わせて再作成
				bitmap.recycle();
				bitmap = null;
				
				// 補正実行
				bitmap = engine.TrapezoidImage(trapeCoord, true);
			}catch (IDRException ex) {
				progress += 20;
				callback.setProgress(progress);					
				if(ex.getErrorNo() == IDRException.IDR_ERROR_DETECT_TRAPEZOID) {
					strBuf.append(String.format("%s%n","台形補正なし"));
				} else {
					strBuf.append(String.format("%s:0x%s%n", ex.toString(), Integer.toHexString(ex.getErrorNo())));						
				}
			}
	
			progress += 30;
			callback.setProgress(progress);
			
			// フィールド認識
			CardRecog recog = new CardRecog();
			engine.RecogFields(recog);

			// 認識結果テキスト
			strBuf.append(String.format("会社名：%s%n", recog.getCompany().getText() ));
			strBuf.append(String.format("会社名の読み仮名：%s%n", recog.getCompany_kana().getText() ));
			strBuf.append(String.format("所属：%s%n", recog.getAffiliation().getText() ));
			strBuf.append(String.format("役職：%s%n", recog.getExetive().getText() ));
			strBuf.append(String.format("姓：%s%n", recog.getFname().getText() ));
			strBuf.append(String.format("姓の読み仮名：%s%n", recog.getFname_kana().getText() ));
			strBuf.append(String.format("名：%s%n", recog.getLname().getText() ));
			strBuf.append(String.format("名の読み仮名：%s%n", recog.getLname_kana().getText() ));
			strBuf.append(String.format("事業所：%s%n", recog.getOffice1().getText() ));
			strBuf.append(String.format("郵便番号：%s%n", recog.getPostal1().getText() ));
			//strBuf.append(String.format("郵便番号：%s%n", zenkakuToHankaku(recog.getPostal1().getText()) ));
			strBuf.append(String.format("住所：%s%n", recog.getAddress1().getText() ));
			strBuf.append(String.format("ビル名：%s%n", recog.getBuilding1().getText() ));
			strBuf.append(String.format("電話番号：%s%n", recog.getTel1().getText() ));
			//strBuf.append(String.format("電話番号：%s%n", zenkakuToHankaku(recog.getTel1().getText()) ));
			strBuf.append(String.format("FAX番号：%s%n", recog.getFax1().getText() ));
			strBuf.append(String.format("事業所：%s%n", recog.getOffice2().getText() ));
			strBuf.append(String.format("郵便番号：%s%n", recog.getPostal2().getText() ));
			strBuf.append(String.format("住所：%s%n", recog.getAddress2().getText() ));
			strBuf.append(String.format("ビル名：%s%n", recog.getBuilding2().getText() ));
			strBuf.append(String.format("電話番号：%s%n", recog.getTel2().getText() ));
			strBuf.append(String.format("FAX番号：%s%n", recog.getFax2().getText() ));
			strBuf.append(String.format("携帯電話番号：%s%n", recog.getCellular1().getText() ));
			strBuf.append(String.format("携帯電話番号：%s%n", recog.getCellular2().getText() ));
			strBuf.append(String.format("E-Mailアドレス：%s%n", recog.getMail1().getText() ));
			//strBuf.append(String.format("E-Mailアドレス：%s%n", zenkakuToHankaku(recog.getMail1().getText()) ));
			strBuf.append(String.format("E-Mailアドレス：%s%n", recog.getMail2().getText() ));
			strBuf.append(String.format("URL：%s%n", recog.getUrl1().getText() ));
			//strBuf.append(String.format("URL：%s%n", zenkakuToHankaku(recog.getUrl1().getText()) ));
			strBuf.append(String.format("URL：%s%n", recog.getUrl2().getText() ));

			progress += 30;
			callback.setProgress(progress);
			str = strBuf.toString();
			strBuf = null;
			progress += callback.getProgress();
			callback.setProgress(progress);
			if(progress < 100) {
				callback.setProgress(100);
			}
		
		} catch (IDRException e) {
				callback.setProgress(100);
				return String.format("%s: 0x%s%n", e.toString(), Integer.toHexString(e.getErrorNo()));
		} catch (Exception ex) {
			return String.format("%s", ex.toString());
		} finally {
			try {
				if (engine != null) {
					// OCRエンジンにセットされている画像の破棄
					engine.FreeImage();
					// OCRエンジン終了
					engine.Release();
				}
			} catch (IDRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		return str;
	}
	/** OCR認識処理 [END] ================================== */

	/** 全角から半角変換処理 [START] ================================== */
	private static final char[] ZENKAKU_KATAKANA = { 'ァ', 'ア', 'ィ', 'イ', 'ゥ',
	  'ウ', 'ェ', 'エ', 'ォ', 'オ', 'カ', 'ガ', 'キ', 'ギ', 'ク', 'グ', 'ケ', 'ゲ',
	  'コ', 'ゴ', 'サ', 'ザ', 'シ', 'ジ', 'ス', 'ズ', 'セ', 'ゼ', 'ソ', 'ゾ', 'タ',
	  'ダ', 'チ', 'ヂ', 'ッ', 'ツ', 'ヅ', 'テ', 'デ', 'ト', 'ド', 'ナ', 'ニ', 'ヌ',
	  'ネ', 'ノ', 'ハ', 'バ', 'パ', 'ヒ', 'ビ', 'ピ', 'フ', 'ブ', 'プ', 'ヘ', 'ベ',
	  'ペ', 'ホ', 'ボ', 'ポ', 'マ', 'ミ', 'ム', 'メ', 'モ', 'ャ', 'ヤ', 'ュ', 'ユ',
	  'ョ', 'ヨ', 'ラ', 'リ', 'ル', 'レ', 'ロ', 'ヮ', 'ワ', 'ヰ', 'ヱ', 'ヲ', 'ン',
	  'ヴ', 'ヵ', 'ヶ' };

	private static final String[] HANKAKU_KATAKANA = { "ｧ", "ｱ", "ｨ", "ｲ", "ｩ",
	  "ｳ", "ｪ", "ｴ", "ｫ", "ｵ", "ｶ", "ｶﾞ", "ｷ", "ｷﾞ", "ｸ", "ｸﾞ", "ｹ",
	  "ｹﾞ", "ｺ", "ｺﾞ", "ｻ", "ｻﾞ", "ｼ", "ｼﾞ", "ｽ", "ｽﾞ", "ｾ", "ｾﾞ", "ｿ",
	  "ｿﾞ", "ﾀ", "ﾀﾞ", "ﾁ", "ﾁﾞ", "ｯ", "ﾂ", "ﾂﾞ", "ﾃ", "ﾃﾞ", "ﾄ", "ﾄﾞ",
	  "ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ", "ﾊ", "ﾊﾞ", "ﾊﾟ", "ﾋ", "ﾋﾞ", "ﾋﾟ", "ﾌ",
	  "ﾌﾞ", "ﾌﾟ", "ﾍ", "ﾍﾞ", "ﾍﾟ", "ﾎ", "ﾎﾞ", "ﾎﾟ", "ﾏ", "ﾐ", "ﾑ", "ﾒ",
	  "ﾓ", "ｬ", "ﾔ", "ｭ", "ﾕ", "ｮ", "ﾖ", "ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ", "ﾜ",
	  "ﾜ", "ｲ", "ｴ", "ｦ", "ﾝ", "ｳﾞ", "ｶ", "ｹ" };

	private static char[] HANKAKU_SYMBOL = { '！', '＃', '＄', '％', '＆', '（',
		  '）', '＊', '＋', '，', '−', '．', '／', '：', '；', '＜', '＝', '＞', '？',
		  '＠', '［', '］', '＾', '＿', '｛', '｜', '｝', 0 };
  
	private static final char ZENKAKU_KATAKANA_FIRST_CHAR = ZENKAKU_KATAKANA[0];

	private static final char ZENKAKU_KATAKANA_LAST_CHAR = ZENKAKU_KATAKANA[ZENKAKU_KATAKANA.length - 1];
  
	public static String zenkakuKatakanaToHankakuKatakana(char c) {
		if (c >= ZENKAKU_KATAKANA_FIRST_CHAR && c <= ZENKAKU_KATAKANA_LAST_CHAR) {
			return HANKAKU_KATAKANA[c - ZENKAKU_KATAKANA_FIRST_CHAR];
	    } else {
	      return String.valueOf(c);
	    }
	}

	public static String zenkakuKatakanaToHankakuKatakana(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char originalChar = s.charAt(i);
			String convertedChar = zenkakuKatakanaToHankakuKatakana(originalChar);
			sb.append(convertedChar);
		}
		return sb.toString();
	}	
  
	public static boolean isSymbol(char c) {
		for(int i = 0; HANKAKU_SYMBOL[i] != 0; i++) {
			if(c == HANKAKU_SYMBOL[i]) {
				return true;
			}
		}
		return false;
	}

	public static String zenkakuAlphabetToHankaku(String s) {
		StringBuffer sb = new StringBuffer(s);
	    for (int i = 0; i < sb.length(); i++) {
	    	char c = sb.charAt(i);
	    	if (c >= 'ａ' && c <= 'ｚ') {
	    		sb.setCharAt(i, (char) (c - 'ａ' + 'a'));
	    	} else if (c >= 'Ａ' && c <= 'Ｚ') {
	    		sb.setCharAt(i, (char) (c - 'Ａ' + 'A'));
	    	} else if (isSymbol(c) ) {
	    		sb.setCharAt(i, (char)(c - 'Ａ' + 'A'));
	    	}
	    }
	    return sb.toString();
	}

  	public static String zenkakuNumToHankaku(String s) {
  		StringBuffer sb = new StringBuffer(s);
		for (int i = 0; i < sb.length(); i++) {
			char c = sb.charAt(i);
			if (c >= '０' && c <= '９') {
				sb.setCharAt(i, (char)(c - '０' + '0'));
			}
		}
		return sb.toString();
	}

  	public static String zenkakuToHankaku(String s) {
  		s = zenkakuKatakanaToHankakuKatakana(s);
  		s = zenkakuAlphabetToHankaku(s);
  		return zenkakuNumToHankaku(s);
  	}
	/** 全角から半角変換処理 [END] ================================== */
}
