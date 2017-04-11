<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>
<script type="text/javascript">
	var isUpload='<%=request.getAttribute("isUpload")%>';			//模型id
	if(isUpload=="OK"){
		window.parent.uploadOver(true);
	}else if(isUpload=="NO"){
		alert("解析模型文件失败！请重新上传！");
		window.parent.uploadOver(false);
	}
</script>
</head>
<body style="background: #ffffff;">
<div>
    <form enctype="multipart/form-data"
          action='<%=rootPath %>/upload?fileUploadCalss=tydic.meta.module.tbl.imptab.PdmUploadImpl'
          id="_uploadForm" method="post">
        <input style="width:150px; border: 1px solid #88afe8;margin-top:10px;margin-left:5%;"
               class="dhxlist_txt_textarea" name="upload" id="_fileupload" type="file"/>
        <input type="button" name="提交" onclick="submitFile()" value="提交"/>
    </form>
    <div id="showMsg" style="border-top:1px solid #88afe8;width: 90%;margin:10px auto;padding-top: 10px;">
        请选择一个PowerDesigner设计的模型文件,最多支持解析500个模型
    </div>
</div>
<script type="text/javascript">
    var validateFile=function(value){
        if(!/\S+\.pdm/i.test(value)){
            alert("请选择一个PDM的后缀文件！");
            return false;
        }
        return true;
    }
    //添加form验证
    dhtmlxValidation.addValidation("_uploadForm",[
        {
            target:"_fileupload",rule:"NotEmpty"
        }
    ]);
    var submitFile=function(){
        var fileName = document.getElementById("_fileupload").value;
        if(validateFile(fileName)){
            window.parent.dhx.showProgress("提示","正在解析您的PDM文件，可能比较耗时，请您耐心等候！");
            document.getElementById("_uploadForm").submit();
        }
    }
</script>
</body>
</html>
