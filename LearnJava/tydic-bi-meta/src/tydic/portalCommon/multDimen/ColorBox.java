package tydic.portalCommon.multDimen;

public class ColorBox {

	
	/**
	 * 颜色
	 * RGB
	 * @param i
	 * @return
	 */
	public static String getColor(int i){
		
		String[] colors = new String[]{
				"0033FF",
				"FF9933",
				"00FFCC",
				"FFFF00",
				"669900",
				"006666",
				"993399",
				"FF6666",
				"33CC99",
				"FF9999",
				"9900FF",
				"CC00FF",
				"6699FF",
				"33FFFF",
				"3300CC",
				"FF3300",
				"99FF00",
				"66FF00",
				"FF99FF",
				"99FF99",
				"66CCFF",
				"333333"
			};
		if(i>21){
			i = i%21;
		}
		return "#"+colors[i];
	}
}
