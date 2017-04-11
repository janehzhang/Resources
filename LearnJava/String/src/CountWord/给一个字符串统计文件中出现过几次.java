package CountWord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * indexOf(String str) 
          返回指定子字符串在此字符串中第一次出现处的索引。
 */
public class 给一个字符串统计文件中出现过几次 {

	public static void count(File file, String word){
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String str = null;
			int index = -1;
			int count = 0;
			while((str= br.readLine()) != null){
				while(str.length() >= word.length() && (index = str.indexOf(word)) > 0){  //屌的飞起
					count++;
					str = str.substring(index + word.length());
					
				}
			}	
			
			System.out.print(count);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		count(new File("d://test.txt"),"xyz");
		
	}

}
