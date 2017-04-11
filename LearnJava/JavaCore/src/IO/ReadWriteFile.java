package IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class ReadWriteFile {

	public static void readAndWrite(){
		try {
			//要是D盘没有这个文件，会怎么？都是java.io.FileNotFoundException: D:\a.txt (系统找不到指定的文件。),
			//InputStream is = new FileInputStream("D:/test.txt");	      //FIleNotFoundException
			File file = new File("D:/test.txt");
			if(!file.exists()){
				System.out.println("no file");
				return;
			}
			InputStream is = new FileInputStream(file);
			OutputStream out = new FileOutputStream("D:/test2.txt");
			int code = 0;
			byte[] buffer = new byte[1024];					//IOException
				while( (code = is.read(buffer)) != -1){		//read返回的是所读字符的编码，读到-1表示结尾
					out.write(buffer);
					
				}
			
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) {
		for(int i = 1; i <= 3; i++){
			readAndWrite();					//连续运行三次，文件的重新建了
			System.out.print(i);
		}
		
	}

}
