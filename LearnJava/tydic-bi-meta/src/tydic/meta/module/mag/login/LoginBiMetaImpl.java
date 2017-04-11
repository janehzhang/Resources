package tydic.meta.module.mag.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;

import tydic.meta.acc.Des;
import tydic.meta.acc.IsAccCheckAction;
import tydic.frame.common.Log;
import tydic.meta.common.Common;
import tydic.meta.web.session.SessionManager;
import tydic.portalCommon.util.IAccCheckedItemService;
import tydic.portalCommon.util.AccCheckedItemService;
import tydic.portalCommon.util.AccCheckedItemBean;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description bi元数据管理系统登录。 <br>
 * @date 2011-10-22
 */
public class LoginBiMetaImpl extends LoginCommonImpl{

	private ThreadLocal<String> threadLocal = new ThreadLocal<String>();
	private IsAccCheckAction IsAccCheckAction = new IsAccCheckAction();
	
    /**
     * 进行先决行验证。验证验证码
     * @param loginMessage
     * @return  验证失败返回一个实体对象，返回NULL代表验证成功。
     */
    public LoginResult beginLogin(Map<String, Object> loginMessage){
        //获取界面输入框的验证码
        String inputCode = loginMessage.get("validateCode") == null ? "" : loginMessage.get("validateCode").toString();
        //获取保存到session中的验证码
        String sessionCode = (String) SessionManager.getAttribute("randomCode");
        String loginId = loginMessage.get("loginId") == null ? "" : loginMessage.get("loginId").toString();
        loginId = decode(loginId);
        loginMessage.put("loginId", loginId);
        //密码
        String password = loginMessage.get("password") == null ? "" : loginMessage.get("password").toString();
        threadLocal.set(sessionCode);
        if(null == sessionCode){
            return LoginResult.ERROR_VALIDATEOVERDUE;
        }else if(!sessionCode.equalsIgnoreCase(inputCode)){	//验证码比较不区分大小写
            return LoginResult.ERROR_VALIDATECODE;
        }else if(loginId.equals("") || password.equals("")){
            return LoginResult.ERROR_USER_PASSWD;
        }
        return null;
    }
    
    public Map<String,Object> accCheck(Map<String, Object> loginMessage){
    	Map<String,Object> AccRspMap = new HashMap<String,Object>();
    	//获取当前日期			
		Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String createTime=sdf.format(c.getTime());
		
		//设置本机IP
		String localIP = getIpAddr(WebContextFactory.get().getHttpServletRequest());
				
		//设置服务器IP
		String netIP = "";
		try{
			InetAddress addr = InetAddress.getLocalHost();
			netIP=addr.getHostAddress();//获得本机IP
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	AccCheckedItemBean bean = new AccCheckedItemBean();
    	bean.setReqCode("CSAS"+createTime);
    	bean.setActionCode("1");
    	bean.setSysCode("CSAS");
    	bean.setSysPwd("3C731E9ED83B6DA51C032C8B5148D70CAC63DBD6");
    	bean.setServiceCode("queryUnifyStaffInfo");
    	bean.setReqTime(createTime);
    	bean.setVersion("3.0");
    	bean.setSUBSTAFF_ID((String)loginMessage.get("loginId"));
    	bean.setSTAFF_PASSWD((String)loginMessage.get("password"));
    	bean.setIP_ADDR(localIP);
    	bean.setNET_IP(netIP);
    	bean.setQRY_TYPE("3");
    	bean.setACT_CODE("LOGIN");
    	bean.setAD_SYN("1");
    	
    	IAccCheckedItemService iacis=new AccCheckedItemService();
    	AccRspMap=iacis.createAccCheckedItem(bean);
    	
    	return AccRspMap;
    }
    
    public String getIpAddr(HttpServletRequest request) {
    	String ip = request.getHeader("x-forwarded-for");   
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("http_client_ip");   
    	}   
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getRemoteAddr();   
    	}   
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    		ip = request.getHeader("Proxy-Client-IP");   
    	}   
    	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
    		ip = request.getHeader("WL-Proxy-Client-IP");   
    	}   if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {    
    		ip = request.getHeader("HTTP_X_FORWARDED_FOR");   
    	}   // 如果是多级代理，那么取第一个ip为客户ip    
    	if (ip != null && ip.indexOf(",") != -1) {    
    		ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();   
    	}   
    	return ip; 
    }

    /**
     * Bi 元数据管理登录。
     * @param loginMessage
     * @return
     */
    public LoginResult login(Map<String, Object> loginMessage){
        LoginResult beforeLoginRs=beginLogin(loginMessage);
                
        String sessionCode = threadLocal.get();
        if(beforeLoginRs==null){
        	 //密码
            String password = loginMessage.get("password") == null ? "" : loginMessage.get("password").toString();
        	loginMessage.put("password", Des.decode(password, threadLocal.get()));
        	
        	boolean flag = IsAccCheckAction.IsAccCheck((String)loginMessage.get("loginId"));
        	String accCheckResponse = "";
        	String accCheckRspCode = "";
        	//ACC认证
        	if(flag){
        		Map<String,Object> AccRspMap = new HashMap<String,Object>();
        		AccRspMap = accCheck(loginMessage);
        		accCheckResponse = (String)AccRspMap.get("Response");
        		accCheckRspCode = (String)AccRspMap.get("RspCode");
        	}        		                
                        
            //获取保存到session中的验证码
            String loginId = loginMessage.get("loginId") == null ? "" : loginMessage.get("loginId").toString();
           
            List<Map<String, Object>> rs = null;
            //正则表达式判断是否是Email
            Pattern pattern = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
            Pattern ptel = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher matcher = pattern.matcher(loginId);
            Matcher matchertel = ptel.matcher(loginId);
            password = Common.getMD5(password.getBytes());//用户输入的密码加盐处理
            boolean isEmailMatch = true;//是否用Email匹配
            
            if(matcher.matches()){//匹配用Email登录

                rs = super.getLoginDAO().queryUserByEmail(loginId);
            }else if(matchertel.matches()){//匹配手机

                rs = super.getLoginDAO().queryUserByTel(loginId);
                isEmailMatch = false;
            } else{//尝试用中文名
                rs = super.getLoginDAO().queryUserByNamecn(loginId);
                isEmailMatch = false;
            }
            if(rs == null){
                return LoginResult.ERROR_USER_PASSWD;
            }
            if(rs.size() == 0){
                return LoginResult.ERROR_USER_PASSWD;
            }
            if(rs.size() >1){
                Log.warn("根据用户传入信息 匹配出多个用户！");
                return LoginResult.ERROR_NAME_REPEAT;
            }
            Map<String,Object>  userData  =  rs.get(0);
            String realPassword = userData.get("USER_PASS").toString();
            String userPassword = Common.getMD5(((String)loginMessage.get("password")).getBytes());
            if("00".equals(accCheckResponse)){
            	return super.afterLoginVaildate(rs);
            }
            if("04".equals(accCheckResponse)){
            	if("3".equals(accCheckRspCode)){ //ACC认证失败，密码错误或者用户为登记ACC
            		return LoginResult.ERROR_ACC_CHECK_PASSWD;
            	}
            	if("32".equals(accCheckRspCode)){ //ACC认证失败，统一账号被冻结
            		return LoginResult.ERROR_ACC_LOCKING;
            	}
            }
            if(!userPassword.equals(realPassword)){//密码匹配成功
                return LoginResult.ERROR_USER_PASSWD;
            }
            return super.afterLoginVaildate(rs);
        }else{
            return beforeLoginRs;
        }
    }
    
    /**  
     * BASE64解码  
     * @param str  
     * @return string  
     */    
    public String decode(String str){
		    byte[] bt = null;    
		    String res = "";
		    try {    
		        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();    
		        bt = decoder.decodeBuffer( str );    
		        res = new String(bt,"UTF-8");
		    } catch (IOException e) {    
		        e.printStackTrace();    
		    }    
		   
	        return res;    
	    } 
}
