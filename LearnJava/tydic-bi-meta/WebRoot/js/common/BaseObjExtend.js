/**
 * JAVASCRIPT 基础内置对象扩展（可以是新加函数，可以是重写）
 */

/**
 * 日期扩展，一个日期对象转换成固定格式返回，传一个参数，fmt格式
 */
Date.prototype.toString=function(){
    var format=arguments[0];
    format=format?format:"";
    switch (format.toLowerCase()) {
        case 'yyyy-mm-dd' :
            return this.getFullYear()+'-'+(this.getMonth()+1)+'-'+this.getDate();
            break;
        case 'yyyy-mm-dd hh:mm:ss' :
            return this.getFullYear()+'-'+(this.getMonth()+1)+'-'+this.getDate()+' '+this.getHours()+':'+this.getMinutes()+':'+this.getSeconds();
            break;
        case 'yyyy/mm/dd':
            return this.getFullYear()+'/'+(this.getMonth()+1)+'/'+this.getDate();
            break;
        case 'dd/mm/yyyy':
            return this.getDate() +'/'+(this.getMonth()+1)+'/'+this.getFullYear();
            break;
        case 'mm/dd/yyyy':
            return this.getMonth()+'/'+(this.getMonth()+1) +'/'+this.getFullYear();
            break;
        case 'hh:mm:ss' :
            return this.getHours()+':'+(this.getMonth()+1)+':'+this.getSeconds();
            break;
        case 'yyyy年mm月dd日' :
            return this.getFullYear()+'年'+(this.getMonth()+1)+'月'+this.getDate()+'日';
            break;
        default :
            return this.toLocaleString();
    }
};

/**
 * 将数组转换成字符串,包含处理嵌套
 */
Array.prototype.valueOf=function(){
    var res="";
    for(var i=0;i<this.length;i++){
        if(i)
            res+=","+(this[i] && typeof this[i]=="object" && this[i].valueOf ?this[i].valueOf():this[i]);
        else
            res=(this[i] && typeof this[i]=="object" && this[i].valueOf ?this[i].valueOf():this[i]);
    }
    return res;
};

//删除数组中的一项 i:索引号
Array.prototype.remove=function(i){
//	return this.splice(i,1);
    if(i<0)return null;
    if(this.length<=i)return null;
    if(i==0){
        return this.shift();
    }else if(i==this.length-1){
        return this.pop();
    }
    var result=this[i];
    for(var j=i;j<this.length-1;j++){
        this[j]=this[j+1];
    }
    this.length-=1;
    return result;
};
// 交换数组元素位置
Array.prototype.swap=function(s,d){
    var t=this[s];
    this[s]=this[d];
    this[d]=t;
};
/**
 * 克隆
 */
Array.prototype.clone=function(){
    var cl=[];
    for(var i=0;i<this.length;i++)
        if(typeof cl[i]=="object" && cl[i].clone && typeof cl[i].clone=="function")
            cl[i]=this[i].clone();
        else
            cl[i]=this[i];
    return cl;
};
/**
 *
 */
Array.prototype.unique = function () {
    var newArray = [],
        temp = {};
    for (var i = 0; i < this.length; i++) {
        temp[typeof(this[i]) + this[i]] = this[i];
    }
    for (var j in temp) {
        newArray.push(temp[j]);
    }
    return newArray;
};

/**
 * 根据值查询
 * @param value
 */
Array.prototype.findByValue = function (value) {
    var _self = this;
    for (var i = 0; i < _self.length; i++) {
        if (_self[i] == value) {
            return true;
        }
    }
    return false;
};
/**
 * 删除值
 * @param val
 */
Array.prototype.removeByValue = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) {
            this.splice(i, 1);
            break;
        }
    }
};

/**
 * 全局替换
 * @param AFindText
 * @param ARepText
 */
String.prototype.replaceAll = function (AFindText, ARepText) {
    var raRegExp = new RegExp(AFindText, "g");
    return this.replace(raRegExp, ARepText);
};
/**
 * 返回字符串长途，包含处理中文
 */
String.prototype.cnLength = function(){
//    return this.replace(/[^\x00-\xff]/g, "**").length; //实现方式一
    var arr=this.match(/[^\x00-\xff]/ig);
    return this.length+(arr==null?0:arr.length); //实现方式二
};
//作用：从字符串左边截取 n 个字符，并支持全角半角字符的区分
String.prototype.left = function(num,mode){
    if(!/\d+/.test(num))return(this);
    var str = this.substr(0,num);
    if(!mode) return str;
    var n = str.cnLength() - str.length;
    num = num - parseInt(n/2);
    return this.substr(0,num);
};
//去除字符串中前后空格
String.prototype.trim = function(){
    var rp1=/(\s*)(.*\b)(\s*)/g;
    var rp2=/(\s*)/g;
    var str=this.replace(rp1,"$2");
    return str.replace(rp2,"")==""?"":str;
};
//去除字符串左边(前)空格
String.prototype.ltrim = function(){
    var rp1=/(\s*)(.*\b)(\s*)/g;
    var rp2=/(\s*)/g;
    var str=this.replace(rp1,"$2$3");
    return str.replace(rp2,"")==""?"":str;
};
//去除字符串右边(后)空格
String.prototype.rtrim = function(){
    var rp1=/(\s*)(.*\b)(\s*)/g;
    var rp2=/(\s*)/g;
    var str = this.replace(rp1,"$1$2");
    return str.replace(rp2,"")==""?"":str;
};

