//function proGrid()
//{
//	this.DHTML_TYPE="dhtmlXCombo";
//}
//proGrid.prototype=new dhtmlXCombo;
dhtmlXCombo.prototype.openSelect = function(){ 
	  if (this._disabled) return;
      this.closeAll();
      this._positList();
      this.DOMlist.style.display="block";
      this.callEvent("onOpen",[]);
	  if(this._tempSel) this._tempSel.deselect();
	  if(this._selOption) this._selOption.select();
	  if(this._selOption){
	 	var corr=this._selOption.content.offsetTop+this._selOption.content.offsetHeight-this.DOMlist.scrollTop-this.DOMlist.offsetHeight;
         if (corr>0) this.DOMlist.scrollTop+=corr;
            corr=this.DOMlist.scrollTop-this._selOption.content.offsetTop;
         if (corr>0) this.DOMlist.scrollTop-=corr;
	  }
	  /* if (this.autoOptionSize){
        	var x=this.DOMlist.offsetWidth; 
			
        	for ( var i=0; i<this.optionsArr.length; i++){
				if(i==0) alert("this.DOMlist.childNodes[i].scrollWidth ="+ this.DOMlist.childNodes[i].scrollWidth + "> x= "+ x);
        		if (this.DOMlist.childNodes[i].scrollWidth > x)
        			x=this.DOMlist.childNodes[i].scrollWidth;
			}
        			
			this.DOMlist.style.width=x+"px";
		}*/
		
		      
      if (_isIE) this._IEFix(true);
      this.DOMelem_input.focus();
	
//      if (this._filter) this.filterSelf();
   }
