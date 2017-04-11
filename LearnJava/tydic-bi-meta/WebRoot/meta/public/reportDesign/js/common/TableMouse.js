 var SelectRowkey;
 function tr_onmouseover(element) {
	    var oTR = getElement(element, "TR");
	    if (oTR != null && !eval(oTR.active)) {
	    	oTR.setAttribute("oldBgc",oTR.style.backgroundColor);
	        oTR.style.backgroundColor = "#C4E5F4";
	    }
	}
	function tr_onmouseoutR(element) {
	    var oTR = getElement(element, "TR");
	    if (oTR != null && !eval(oTR.active)) {
	        oTR.style.backgroundColor = oTR.getAttribute("oldBgc");
	    }
	}
	function tr_onclick(element,htmlurl) {
	    var oTR = getElement(element, "TR");
	    var oTABLE = getElement(oTR, "TABLE");
	    if (oTABLE != null) {
	        if (typeof(oTABLE.activeRow) != "undefined" && oTABLE.activeRow != null) {
	            oTABLE.activeRow.style.backgroundColor = "";
	            oTABLE.activeRow.style.color = "";
	            oTABLE.activeRow.active = "false";
	        }
	        if (oTR != null) {
	            oTR.style.backgroundColor = "highlight";
	            oTR.style.color = "highlighttext";
	            oTR.active = "true";
	            oTABLE.activeRow = oTR;
	            SelectRowkey=oTR.cells.item(0).innerText;
	     	  //document.getElementById("hh").value=oTR.cells.item(0).innerText;;
	          //alert(document.getElementById("hh").value);
               if(htmlurl!=null && htmlurl!='')winopen(htmlurl);
	        }
	    }
	}
	function getElement(src, tagName) {
	    var obj = src;
	    while (obj != null && obj.tagName != tagName) {
	        obj = obj.parentElement;
	    }
	    return obj;
	}
	