//反转句子（永远的痛，要出发笔试）

/*
 char[] data = str.toCharArray(); 		// 获得字符串对应的char数组
 String str = new String( char data[]);  // 数组转成字符串
 Arrays.toString(char[] a) 
 subString();    //js的就substr()
 
 substring(int beginIndex,int endIndex)   //该子字符串从指定的 beginIndex(从0开始)处开始(包括)，直到索引 endIndex - 1 处的字符。"smiles".substring(1, 5) returns "mile"     
 char charAt(int index);  //js的一样，获得某个索引处的字符
 int indexOf(int ch);  
 int indexOf(String str) 	//返回指定子字符串在此字符串中第一次出现处的索引。   
 byte[] getBytes()  
 
 
 //改字符编码
 tempStr = new String(str.getBytes("ISO-8859-1"), "GBK"); 
 
 //用StringBuffer比较方便
 StringBuffer:
 sb.append(char 或者  String)
 StringBuffer delete(int start,int end)  //Removes the characters in a substring of this sequence. 
 StringBuffer insert(int offset,char[] str)
 StringBuffer reverse()   
 
 
 StringBuffer用append() 来连接,
 String用  + 来连接
      
                           
 Arrays.binarySearch(a[], key); 
 Arrays.sort(a[]);				
 Arrays.toString(a[]);			
*/

/*
 * Integer.toString(num);     //int -> String
 * Integer.parseInt(str);     //String -> int 
 */