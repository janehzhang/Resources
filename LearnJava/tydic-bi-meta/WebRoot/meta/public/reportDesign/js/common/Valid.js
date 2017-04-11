



function isEmail(emailStr){
  if(emailStr==null)return false;
  var re = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/g;//匹配email地址的正则表达式
  if(re.test(emailStr))return true;
  return false;
}

function isURL(url){
  if(url==null)return false;
  var re = /^[a-zA-z]+:\/\/(\w+(-\w+)*)(\.(\w+(-\w+)*))*/g;//匹配url地址的正则表达式
  if(re.test(url))return true;
  return false;
}

function isIP(ip){
  if(ip==null)return false;
  var re=/(\d+)\.(\d+)\.(\d+)\.(\d+)/g;//匹配IP地址的正则表达式
  if(re.test(ip))return true;
  return false;
}

// Function Name: isValidEmail
// Function Description: 判断输入是否是有效的电子邮件
// Creation Date: 2004-7-13 9:59
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidEmail(str)
{
var result=str.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidDatetime
// Function Description: 判断输入是否是有效的长日期格式 - "YYYY-MM-DD HH:MM:SS"
// Creation Date: 2004-7-13 9:59
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidDatetime(str)
{
var result=str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/);
if(result==null) return false;
var d= new Date(result[1], result[3]-1, result[4], result[5], result[6], result[7]);
return (d.getFullYear()==result[1]&&(d.getMonth()+1)==result[3]&&d.getDate()==result[4]&&d.getHours()==result[5]&&d.getMinutes()==result[6]&&d.getSeconds()==result[7]);
}

// Function Name: isValidInteger
// Function Description: 判断输入是否是一个整数
// Creation Date: 2004-7-13 10:01
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidInteger(str)
{
var result=str.match(/^(-|\+)?\d+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidPositiveInteger
// Function Description: 判断输入是否是一个正整数
// Creation Date: 2004-7-13 10:01
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidPositiveInteger(str)
{
var result=str.match(/^\d+$/);
if(result==null) return false;
if(parseInt(this)>0) return true;
return false;
}

// Function Name: isValidNegativeInteger
// Function Description: 判断输入是否是一个负整数
// Creation Date: 2004-7-13 10:28
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidNegativeInteger(str)
{
var result=str.match(/^-\d+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidNumber
// Function Description: 判断输入是否是一个数字
// Creation Date: 2004-7-13 10:01
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidNumber(str)
{
return !isNaN(str);
}

// Function Name: isValidLetters
// Function Description: 判断输入是否是一个由 A-Z / a-z 组成的字符串
// Creation Date: 2004-7-13 10:10
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidLetters(str)
{
var result=str.match(/^[a-zA-Z]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidDigits
// Function Description: 判断输入是否是一个由 0-9 组成的数字
// Creation Date: 2004-7-13 10:10
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidDigits(str)
{
var result=str.match(/^[0-9][0-9]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidAlphanumeric
// Function Description: 判断输入是否是一个由 0-9 / A-Z / a-z 组成的字符串
// Creation Date: 2004-7-13 10:14
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidAlphanumeric(str)
{
var result=str.match(/^[a-zA-Z0-9]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidString
// Function Description: 判断输入是否是一个由 0-9 / A-Z / a-z / . / _ 组成的字符串
// Creation Date: 2004-7-13 10:20
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidString(str)
{
var result=str.match(/^[a-zA-Z0-9\s.\-_]+$/);
if(result==null) return false;
return true;
}

// Function Name: isValidPostalcode
// Function Description: 判断输入是否是一个有效的邮政编码
// Creation Date: 2004-7-13 10:22
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidPostalcode(str)
{
var result=str.match(/(^[0-9]{6}$)/);
if(result==null) return false;
return true;
}

// Function Name: isValidPhoneNo
// Function Description: 判断输入是否是一个有效的电话号码
// Creation Date: 2004-7-13 10:22
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidPhoneNo(str)
{
var result=str.match(/(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)/);
if(result==null) return false;
return true;
}

// Function Name: isValidMobileNo
// Function Description: 判断输入是否是一个有效的手机号码
// Creation Date: 2004-7-13 10:23
// Last Modify By: N/A
// Last Modify Date: N/A
function isValidMobileNo(str)
{
var result=str.match(/(^0{0,1}13[0-9]{9}$)/);
if(result==null) return false;
return true;
}
//校验输入是否是电话号码正确！
function isTel(telStr)
{
	var No="0123456789()+-";
	if(telStr.length==0)
	{
	  return false;
	}

	for(i=0;i<telStr.length;i++)
	{
		var Checkstr=telStr.charAt(i);
		if(No.indexOf(Checkstr)==-1)
		{
		  return false;
		}
	}
	return true;
}
