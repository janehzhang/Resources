/*
 * Created on 2005-11-3
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tydic.meta.common.yhd.sql;



/**
 * @author aps-tlm
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SQLStringManage {
	/*
	 * ����С����λ��Ϊ4λ
	 */
	private static int digit = 4;
	/*
	 * ��double�����ݺ��С���㱣����λ��ת�����ַ���
	 */
	public static String manageDouble(double data){
		//�������Ϊ������򷵻ؿ��ַ���
		String temp = String.valueOf(data);
		if (temp.equals("Infinity") || temp.equals("-Infinity") || temp.equals("NaN")){
			return "";
		}
		int flag = temp.indexOf("E");
		if (flag != -1){
			String dat = temp.substring(0,flag);
			//��ѧ������λ��
			int count = Integer.parseInt(temp.substring(flag + 1));
			//����λ
			String dat1 = dat.substring(0,dat.indexOf("."));
			//С��λ
			String dat2 = dat.substring(dat.indexOf(".") + 1);
			//С��λ�ĳ���
			int length = dat2.length();
			//�����ѧ����Ϊ����0��������������£�����3.0E7;
			if (count > 0){
				if ( count < length){
					//1.2235344434348123678E13
					if ( length - count > digit){
						temp = dat1 + dat2.substring(0,count) + round("0." + dat2.substring(count)).substring(1);
					}//1.2235344434348123E13
					else{
						temp = dat1 + dat2.substring(0,count) + "." + dat2.substring(count);
					}
				}else{
					while ( count - length != 0){
						dat2 = dat2 + "0";
						count--;
					}
					temp = dat1 + dat2 + ".0";
				}
			}//�����ѧ����ΪС��0��������������£�����3.0E��4;
			else{
				count = 0 - count;
				//1.23445567778E-9
				if (count > digit + 1){
					return "0";
				}else{
					while ( count - 1 != 0){
						dat1 = "0" + dat1;
						count--;
					}
					temp = round("0." + dat1);
				}
			}
			return temp;
		}else{
			return round(temp);
		}
	}
	/*
	 * ��float�����ݺ��С���㱣����λ��ת�����ַ���
	 */
	public static String manageFloat(float data){
		String temp = String.valueOf(data);
		int flag = temp.indexOf("E");
		if (flag != -1){
			String str = temp.substring(flag);
			String str1 = temp.substring(0,flag);
			return round(str1) + str;
		}else{
			return round(temp);
		}
	}
	/*
	 * ���ַ����е�����ֵ�����ݽ��б�����λС�������������ַ���
	 */
	public static String manageString(String data){
		try{
			if (data.equals("Infinity") || data.equals("-Infinity") || data == null || data.equals("")){
				return "0";
			}else if (data.equals("NaN")){
				return "";
			}
			double temp = Double.parseDouble(data);
			String str = manageDouble(temp);
			return str;
		}catch(ClassCastException se){
			System.out.println(se.getMessage());
			return "";
		}
	}
	/*
	 * ȥ��С�����������õ���
	 */
	private static String trimLastZero(String data){
		int length = data.length();
		for (int j = 0;j < digit - 1 ; j++){
			if (data.charAt(length - 1) != '0'){
				break;
			}
			length--;
		}
		return data.substring(0,length);
	}
	/*
	 * ������λС������
	 */
	private static String process(String data){
		if (data == null){
			return "0";
		}
		int flag = data.indexOf(".") + 1;
		int length = data.length();
		if (length - flag <= digit){
			return data;
		}else{
			data = data.substring(0,flag + digit);
			return trimLastZero(data);
		}
	}
	/*
	 * ��С���������������
	 */
	private static String round(String data){
		int flag = data.indexOf(".") + 1;
		//С�����ֵĳ���С�ڵ�����λ��ֱ�ӷ��ظ�����
		if (data.substring(flag).length() <= digit){
			return data;
		}
		//������λС����ĵ���λ���ַ����е�λ��
		int offset = flag + digit;
		int ch = (int)data.charAt(offset);
		//5��ASCLL��Ϊ53
		if (ch >= 53){
			//��λС������
			int decimal = Integer.parseInt(data.substring(flag,flag + digit));
			//��������
			int integral = Integer.parseInt(data.substring(0,flag - 1));
			//���С��λ��1��������һλ�������������ּ�1
			if (String.valueOf(decimal + 1).length() > digit){
				integral += 1;
				data = String.valueOf(integral) + ".0";
				return data;
			}else{
				String deci = String.valueOf(decimal + 1);
				int length = digit - deci.length();
				while(length != 0){
					deci = "0" + deci;
					length--;
				}
				data = String.valueOf(integral) + "." + deci;
				return trimLastZero(data);
			}
		}else{
			return trimLastZero(data.substring(0,flag + digit));
		}
	}
	public double getDouble(double data){
		if (String.valueOf(data).equals("Infinity")){
			return 0;
		}else{
			return data;
		}
	}
	/*
	 * ȡ����ֵ
	 */
	public static double absolute(double dou){
		if (dou < 0){
			dou = -dou;
		}
		return dou;
	}
}
