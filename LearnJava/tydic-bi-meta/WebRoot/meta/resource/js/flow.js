/******************************************************
 *Copyrights @ 2011��Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename��
 *        flow.js
 *Description��
 *        ��������ӿ�js�ļ�
 *Dependent��
 *        Flow.swf
 *Author:
 *        ̷����
 *Finished��
 *       2011-11-08
 */

/**
 * ����flash�ڲ�����������޷����ջص������Ĺٷ�bug
 * @param {HTMLObjectElement} instance flashʵ������
 * @param {String} name Ҫ�����յĺ�����
 */
window["__flash__removeCallback"] = function(instance, name) {
    try {
        if (instance) {
            instance[name] = null;
        }
    } catch (flashEx) {

    }
};

/**
 * ���ߺ���,��ȡflashʵ������
 * @param {String} movieName flashʵ����������
 * @return {HTMLObjectElement} flashʵ������
 */
var getFlashMovieObject = function (movieName){
	if(window.document[movieName]){
		return window.document[movieName];
	}else if(navigator.appName.indexOf("Microsoft")==-1){
		if(document.embeds && document.embeds[movieName])
		return document.embeds[movieName];
	}else{
		return document.getElementById(movieName);
	}
};

/**
 * �������js�ӿ���
 * @class Flow �������js�ӿ���
 * @param {Object} config ��ʼ��������Ϣ
 */
var Flow = function(config){
	
	var flowComp;//flashʵ������
	var id = 0;//����id����
	var handlers = new Object();//�¼�������
	var isReady = false;//�Ƿ��Ѿ��������
	var isArrowEvent = true;
	
	/**
	 * ���ߺ���,����һ��������id��
	 * @return {Number} ������id 
	 */
	function generateId(){ 
		id++;
		return id; 
	};
	
	(function(config,flow){
		var div = config['div'];
		//�жϴ������Ԫ�ض�����string�������string�͵���id��ȡԪ��
		div = (typeof div == 'string') ? document.getElementById(div) : div;
		//����һ�����id
		var compId = div.id+"_comp";
		//����������ӵ�window�����У�����flex����
		window[compId] = flow;
		
		//ƴװһ���ַ�������ӵ�Ŀ��div����ʾ
		var objHtml = "<object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' type='application/x-shockwave-flash'"+
							"codebase='http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0' "+
							"width='100%' height='100%' id='"+compId+"' >"+
							"<param name='movie' value='"+config['swf']+"?object="+compId+"' />"+
							"<param name='quality' value='high' />"+
							"<param name='bgcolor' value='#ffffff' />"+
							"<param name='allowScriptAccess' value='always' />"+
							"<param name='allowFullScreen' value='true' />"+
							"<param name='wmode' value='opaque'>"+
							"<embed src='"+config['swf']+"?object="+compId+"' quality='high' wmode='opaque' "+
							"width='100%' height='100%' id='"+compId+"' name='"+compId+"' allowScriptAccess='always' "+
							"type='application/x-shockwave-flash' pluginspage='http://www.macromedia.com/go/getflashplayer' />"+
						"</object>";
		div.innerHTML = objHtml;
		
		//�ڴ����
		objHtml=null;
		delete objHtml;
		
		//��ȡflashʵ������
		flowComp = getFlashMovieObject(compId);
		var isIE = document.all?true:false;
		if(isIE){
			div.attachEvent("onmousedown",function(){
				if(event.button==2&&isReady){
					//��ֹ�¼�ð��,��������flash�Ҽ��˵�
					event.cancelBubble = true;
					event.returnValue = false;
					div.setCapture();
					//֪ͨflex��ʾ�Ҽ��˵�
					flowComp.showcontextmenu();
					return false;
				}
			});
			div.attachEvent("onmouseup",function(){
				div.releaseCapture();
			});
			div.oncontextmenu=function(){
				return false;
			}
		}else{
			div.addEventListener("mousedown",function(event){
				if(event.button==2&&isReady){
					//��ֹ�¼�ð��,��������flash�Ҽ��˵�
					if(event.preventDefault){
						event.preventDefault();
					}
					if(event.stopPropagation){
						event.stopPropagation();
					}
					
					//֪ͨflex��ʾ�Ҽ��˵�
					flowComp.showcontextmenu();
				}
			});
		}
		
		
		//���onmousedown�¼�����
		
		
	})(config,this);//���⹹�캯������ִ��
	
	/**
	 * ��flash��ɼ��ز���ɳ�ʼ��ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 */
	this.onReady=function(){
		if(config['onReady']){
			config['onReady'](this);
		}
		isReady = true;
	};
	
	/**
	 * ���û��ڻ����ϵ���Ҽ�ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @return {Boolean} �������ֵΪflase,��չʾ�Ҽ�
	 */
	this.onCanvasContextMenu = function(){
		handlers.menu=new Object();
		if(isArrowEvent&&config['onCanvasContextMenu']){
			var rtn = [];
			var temp = config['onCanvasContextMenu']();
			for(var i=0;i<temp.length;i++){
				var obj = temp[i];
				var newId = generateId();
				obj.mid = newId;
				if(obj.handler){
					handlers.menu[newId]=obj.handler;
				}
				rtn.push(obj);
			}
			return rtn;
		}else {
			return false;
		}
	};
	
	/**
	 * ���û��ڽڵ��ϵ���Ҽ�ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} nodeId ������Ľڵ�id
	 * @return {Boolean} �������ֵΪflase,��չʾ�Ҽ�
	 */
	this.onNodeContextMenu = function(nodeId){
		handlers.menu=new Object();
		if(isArrowEvent&&config['onNodeContextMenu']){
			var rtn = [];
			var temp = config['onNodeContextMenu'](nodeId);
			for(var i=0;i<temp.length;i++){
				var obj = temp[i];
				var newId = generateId();
				obj.mid = newId;
				if(obj.handler){
					handlers.menu[newId]=obj.handler;
				}
				rtn.push(obj);
			}
			return rtn;
		}else {
			return false;
		}
	};
	
	/**
	 * ���û����������ϵ���Ҽ�ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} fromNodeId �������������from�ڵ�id
	 * @param {String} toNodeId �������������to�ڵ�id
	 * @return {Boolean} �������ֵΪflase,��չʾ�Ҽ�
	 */
	this.onLinkContextMenu = function(fromNodeId,toNodeId){
		handlers.menu=new Object();
		if(isArrowEvent&&config['onLinkContextMenu']){
			var rtn = [];
			var temp = config['onLinkContextMenu'](fromNodeId,toNodeId);
			for(var i=0;i<temp.length;i++){
				var obj = temp[i];
				var newId = generateId();
				obj.mid = newId;
				if(obj.handler){
					handlers.menu[newId]=obj.handler;
				}
				rtn.push(obj);
			}
			return rtn;
		}else {
			return false;
		}
	};
	
	/**
	 * ���û�����Ҽ��˵���ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 */
	this.onContextMenuClick = function(mid){
		isArrowEvent&&handlers.menu[mid]();
	};
	
	/**
	 * ���û��ڽڵ���˫�����ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} nodeId ������Ľڵ�id
	 */
	this.onNodeDblClick = function(nodeId){
		if(isArrowEvent&&config['onNodeDblClick']){
			config['onNodeDblClick'](nodeId);
		}
	};
	
	/**
	 * ���û�����������˫�����ʱ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} fromUID �������������from�ڵ�id
	 * @param {String} toUID �������������to�ڵ�id
	 */
	this.onLinkDblClick = function(fromUID,toUID){
		if(isArrowEvent&&config['onLinkDblClick']){
			config['onLinkDblClick'](fromUID,toUID);
		}
	};
	
	/**
	 * ��ĳ���ڵ㱻�û�ִ��ɾ������ǰ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} nodeId ���û�ִ��ɾ�������Ľڵ�id
	 * @return {Boolean} true��ɾ��,false����ɾ��
	 */
	this.onBeforeDeleteNode = function(nodeId){
		if(isArrowEvent&&config['onBeforeDeleteNode']){
			return config['onBeforeDeleteNode'](nodeId);
		}else{
			return true;
		}
	}
	
	/**
	 * ��ĳ���ڵ㱻�û�ִ��ɾ��������Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} nodeId ���û�ִ��ɾ�������Ľڵ�id
	 * @return {Boolean} true��ɾ��,false����ɾ��
	 */
	this.onAfterDeleteNode = function(nodeId){
		if(isArrowEvent&&config['onAfterDeleteNode']){
			config['onAfterDeleteNode'](nodeId);
		}
	}
	
	
	/**
	 * ���û��������ӽڵ�ȷ��ǰ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} fromUID �����ߵ���ʼ�ڵ�id
	 * @param {String} toUID   �����ߵĽ����ڵ�id
	 * @return {Boolean} true��ȷ������,false��������
	 */
	this.onBeforeLink = function(fromUID,toUID){
		if(isArrowEvent&&config['onBeforeLink']){
			return config['onBeforeLink'](fromUID,toUID);
		}else{
			return true;
		}
	}
	
	/**
	 * ���û��������ӽڵ�ȷ����Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} fromUID �����ߵ���ʼ�ڵ�id
	 * @param {String} toUID   �����ߵĽ����ڵ�id
	 */
	this.onAfterLink = function(fromUID,toUID){
		if(isArrowEvent&&config['onAfterLink']){
			config['onAfterLink'](fromUID,toUID);
		}
	}
	
	/**
	 * ���û�����ɾ��������ǰ��Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} fromUID �����ߵ���ʼ�ڵ�id
	 * @param {String} toUID   �����ߵĽ����ڵ�id
	 * @return {Boolean} true��ȷ��ɾ��,false����ɾ��
	 */
	this.onBeforeDeleteLink = function(fromUID,toUID){
		if(isArrowEvent&&config['onBeforeDeleteLink']){
			return config['onBeforeDeleteLink'](fromUID,toUID);
		}else{
			return true;
		}
	}
	
	/**
	 * ���û�����ɾ�������ߺ�Flex���õĻص�����
	 * @memberOf {Flow} 
	 * @param {String} fromUID �����ߵ���ʼ�ڵ�id
	 * @param {String} toUID   �����ߵĽ����ڵ�id
	 */
	this.onAfterDeleteLink = function(fromUID,toUID){
		if(isArrowEvent&&config['onAfterDeleteLink']){
			config['onAfterDeleteLink'](fromUID,toUID);
		}
	}
	
	/**
	 * ���ؽڵ�����
	 * @memberOf {Flow} 
	 * @param {Object} data Ҫ���صĽڵ�����
	 */
	this.loadNodes = function(data){
		var div = config['div'];
		div = (typeof div == 'string') ? document.getElementById(div) : div;
		this.removeAllNode();
		var figureWidth = this.getFigureWidth();
		var figureHeight = this.getFigureHeight();
		var canvasWidth = this.getCanvasWidth();
		var canvasHeight = this.getCanvasHeight();
		if(config['layout']=='random'){//�������
			for(var i=0;i<data.nodes.length;i++){
				var node = data.nodes[i];
				node.x=(canvasWidth-figureWidth)*Math.random()+figureWidth;
				node.y=(canvasHeight-figureHeight)*Math.random()+figureHeight;
			}
		}else if(config['layout']=='queue'){//�����Ͳ���
			var temp = new Array();
			var countArray = new Array();
			for(var i=0;i<data.nodes.length;i++){
				var node = data.nodes[i];
				if(!temp[Math.abs(node.level)]){
					temp[Math.abs(node.level)]=new Array();
				}
				if(node.level<0){
					if(!temp[Math.abs(node.level)].negativeCount){
						temp[Math.abs(node.level)].negativeCount=0;
					}
					temp[Math.abs(node.level)].negativeCount++;
				}else{
					if(!temp[Math.abs(node.level)].positiveCount){
						temp[Math.abs(node.level)].positiveCount = 0;
					}
					temp[Math.abs(node.level)].positiveCount++;
				}
				temp[Math.abs(node.level)].push(node);
			}
			for(var i=0;i<temp.length;i++){
				var nodes = temp[i];
				var negativeIndex = 0;//�������
				var positiveIndex = 0;//�������
				for(var j=0;j<nodes.length;j++){
					var node = nodes[j];
					node.x = canvasWidth/2+(node.level*2)*figureWidth;
					if(node.level<0){
						node.y = (canvasHeight-figureHeight)/(nodes.negativeCount+1)*(negativeIndex+1);
						negativeIndex++;
					}else{
						node.y = (canvasHeight-figureHeight)/(nodes.positiveCount+1)*(positiveIndex+1);
						positiveIndex++;
					}
				}
			}
		}else{
			
		}
		
		flowComp.loadNodes(data);//���ص�flash���
	};
	
	/**
	 * ��ȡ���нڵ�����
	 * @memberOf {Flow} 
	 * @return {Object} data �ڵ�����
	 */
	this.getAllNodes = function (){
		return flowComp.getAllNodes();
	};
	
	/**
	 * ɾ�����нڵ�����
	 * @memberOf {Flow} 
	 */
	this.removeAllNode = function(){
		flowComp.removeAllNode();
	};
	
	/**
	 * ���һ���ڵ㵽���������
	 * @memberOf {Flow} 
	 * @param {Object} data
	 */
	this.addNode = function(data){
		flowComp.addNode(data);
	};
	
	/**
	 * ���һ�����ӵ����������
	 * @memberOf {Flow} 
	 * @param {String} fromUID ��ʼ�ڵ�id
	 * @param {String} toUID �����ڵ�id
	 */
	this.addLink = function(fromUID,toUID){
		flowComp.addLink(fromUID,toUID,null);
	};
	
	/**
	 * ���ݽڵ�idѡ�нڵ�
	 * @memberOf {Flow} 
	 * @param {String} uid Ŀ��ڵ�id
	 */
	this.selectNode = function(uid){
		flowComp.selectNode(uid);
	};
	
	/**
	 * ���ýڵ�Ĭ�ϱ���ɫ
	 * @memberOf {Flow} 
	 * @param {Number} color 16���Ƶ���ɫֵ
	 */
	this.setNodeDefaultColor = function(color){
		flowComp.setNodeDefaultColor(color);
	};
	
	/**
	 * ����ָ���Ľڵ㱳��ɫ
	 * @memberOf {Flow} 
	 * @param {String} nodeId Ŀ��ڵ��id
	 * @param {Number} color 16���Ƶ���ɫֵ
	 */
	this.setNodeColor = function(nodeId,color){
		flowComp.setNodeColor(nodeId,color);
	};
	
	/**
	 * ���ݽڵ�id,��ȡ�ڵ�����
	 * @memberOf {Flow} 
	 * @param {String} uid Ŀ��ڵ��id
	 * @return {Object} �ڵ�����
	 */
	this.getNode=function(uid){
		return flowComp.getNode(uid);
	};
	
	/**
	 * ������ʼ�ڵ�id�ͽ����ڵ�id,��ȡ����������
	 * @memberOf {Flow} 
	 * @param {String} fromUID ��ʼ�ڵ��id
	 * @param {String} toUID �����ڵ��id
	 * @return {Object} ����������
	 */
	this.getLink=function(fromUID,toUID){
		return flowComp.getLink(fromUID,toUID);
	};
	
	/**
	 * ���ݽڵ�id����������ɫ
	 * @memberOf {Flow} 
	 * @param {String} uid Ŀ��ڵ��id
	 * @param {Number} color 16������ɫֵ
	 */
	this.setNodeGlowColor=function(uid,color){
		flowComp.setNodeGlowColor(uid,color);
	};
	
	/**
	 * ʹĿ��ڵ㿪ʼ����
	 * @memberOf {Flow} 
	 * @param {String} uid Ŀ��ڵ��id
	 */
	this.nodeGlowPlay=function(uid){
		flowComp.nodeGlowPlay(uid);
	};
	
	/**
	 * ʹĿ��ڵ�ֹͣ����
	 * @memberOf {Flow} 
	 * @param {String} uid Ŀ��ڵ��id
	 */
	this.nodeGlowStop=function(uid){
		flowComp.nodeGlowStop(uid);
	};
	
	/**
	 * ������ʼ�ڵ�id�ͽ����ڵ�id���������ߵ�������ɫ
	 * @memberOf {Flow} 
	 * @param {String} fromUID ��ʼ�ڵ��id
	 * @param {String} toUID �����ڵ��id
	 * @param {Number} color 16������ɫֵ
	 */
	this.setLinkGlowColor=function(fromUID,toUID,color){
		flowComp.setLinkGlowColor(fromUID,toUID,color);
	};
	
	/**
	 * ʹ��ʼ�ڵ�id�ͽ����ڵ�id��ʾ�������߿�ʼ����
	 * @memberOf {Flow} 
	 * @param {String} fromUID ��ʼ�ڵ��id
	 * @param {String} toUID �����ڵ��id
	 */
	this.linkGlowPlay=function(fromUID,toUID){
		flowComp.linkGlowPlay(fromUID,toUID);
	};
	
	/**
	 * ʹ��ʼ�ڵ�id�ͽ����ڵ�id��ʾ��������ֹͣ����
	 * @memberOf {Flow} 
	 * @param {String} fromUID ��ʼ�ڵ��id
	 * @param {String} toUID �����ڵ��id
	 */
	this.linkGlowStop=function(fromUID,toUID){
		flowComp.linkGlowStop(fromUID,toUID);
	};
	
	/**
	 * ��������Palette
	 * @memberOf {Flow} 
	 */
	this.hidePalette=function(){
		flowComp.hidePalette();
	}
	
	/**
	 * չʾ����Palette
	 * @memberOf {Flow} 
	 */
	this.showPalette=function(){
		flowComp.showPalette();
	}
	
	/**
	 * ��ȡ��������еĻ������
	 * @memberOf {Flow} 
	 * @return {Number} ��������еĻ������
	 */
	this.getCanvasWidth = function(){
	    return flowComp.getCanvasWidth();
	}
	
	/**
	 * ��ȡ��������еĻ����߶�
	 * @memberOf {Flow} 
	 * @return {Number} ��������еĻ����߶�
	 */
	this.getCanvasHeight = function(){
		return flowComp.getCanvasHeight();
	}
	
	/**
	 * ��ȡ��������еĽڵ�ͼ�Ŀ��
	 * @memberOf {Flow} 
	 * @return {Number} ��������еĽڵ�ͼ�Ŀ��
	 */
	this.getFigureWidth = function(){
		return flowComp.getFigureWidth();
	}
	
	/**
	 * ��ȡ��������еĽڵ�ͼ�ĸ߶�
	 * @memberOf {Flow} 
	 * @return {Number} ��������еĽڵ�ͼ�ĸ߶�
	 */
	this.getFigureHeight = function(){
		return flowComp.getFigureHeight();
	}
	
	/**
	 * չʾtoolbar�е�ѡ��ť
	 * @memberOf {Flow}
	 */
	this.showSelectBtn = function(){
		flowComp.showSelectBtn();
	}
	
	/**
	 * ����toolbar�е�ѡ��ť
	 * @memberOf {Flow}
	 */
	this.hideSelectBtn = function(){
		return flowComp.hideSelectBtn();
	}
	
	/**
	 * չʾtoolbar�е����Ӱ�ť
	 * @memberOf {Flow}
	 */
	this.showLinkBtn = function(){
		return flowComp.showLinkBtn();
	}
	
	/**
	 * ����toolbar�е����Ӱ�ť
	 * @memberOf {Flow}
	 */
	this.hideLinkBtn = function(){
		return flowComp.hideLinkBtn();
	}
	
	/**
	 * չʾtoolbar�еı༭��ť
	 * @memberOf {Flow}
	 */
	this.showEditBtn = function(){
		return flowComp.showEditBtn();
	}
	
	/**
	 * ����toolbar�еı༭��ť
	 * @memberOf {Flow}
	 */
	this.hideEditBtn = function(){
		return flowComp.hideEditBtn();
	}
	
	/**
	 * չʾtoolbar�е�ɾ����ť
	 * @memberOf {Flow}
	 */
	this.showRemoveBtn = function(){
		return flowComp.showRemoveBtn();
	}
	
	/**
	 * ����toolbar�е�ɾ����ť
	 * @memberOf {Flow}
	 */
	this.hideRemoveBtn = function(){
		return flowComp.hideRemoveBtn();
	}
	
	/**
	 * ���½ڵ������
	 * @memberOf {Flow}
	 * @param uid Ŀ��ڵ��uid
	 * @param key ������
	 * @param value ����ֵ
	 */
	this.updateNodeAttr = function(uid,key,value){
		isArrowEvent = false;
		var temp = flowComp.getNode(uid);
		flowComp.removeNode(uid);
		temp[key]=value;
		temp.uid = uid;
		flowComp.addNode(temp);
		isArrowEvent = true;
	}
	
	/**
	 * ɾ���ڵ������
	 * @memberOf {Flow}
	 * @param uid Ŀ��ڵ��uid
	 * @param key ������
	 */
	this.deleteNodeAttr = function(uid,key){
		isArrowEvent = false;
		var temp = flowComp.getNode(uid);
		flowComp.removeNode(uid);
		temp[key]=null;
		delete temp[key];
		temp.uid = uid;
		flowComp.addNode(temp);
		isArrowEvent = true;
	}
	
	/**
	 * �������ӵ�����
	 * @memberOf {Flow}
	 * @param fromUID ���ӵ���ʼ�ڵ�UID
	 * @param toUID ���ӵĽ����ڵ�UID
	 * @param key ������
	 * @param value ����ֵ
	 */
	this.updateLinkAttr = function(fromUID,toUID,key,value){
		isArrowEvent = false;
		var temp = flowComp.getLink(fromUID,toUID);
		flowComp.removeLink(fromUID,toUID);
		temp[key]=value;
		flowComp.addLink(fromUID,toUID,temp);
		isArrowEvent = true;
	}
	
	/**
	 * ɾ�����ӵ�����
	 * @memberOf {Flow}
	 * @param fromUID ���ӵ���ʼ�ڵ�UID
	 * @param toUID ���ӵĽ����ڵ�UID
	 * @param key ������
	 */
	this.deleteLinkAttr = function(ufromUID,toUID,key){
		isArrowEvent = false;
		var temp = flowComp.getLink(fromUID,toUID);
		flowComp.removeLink(fromUID,toUID);
		temp[key]=null;
		delete temp[key];
		flowComp.addLink(fromUID,toUID,temp);
		isArrowEvent = true;
	}
	
	/**
	 * ��ȡ�����еĽڵ�
	 * @memberOf {Flow}
	 * @return {Array} �ڵ�����
	 */
	this.getGlowingNodes = function(){
		return flowComp.getGlowingNodes();
	}
	
	/**
	 * ��ȡ�����е�������
	 * @memberOf {Flow}
	 * @return {Array} ����������
	 */
	this.getGlowingLinks = function(){
		return flowComp.getGlowingLinks();
	}
	
	/**
	 * ɾ��������
	 * @memberOf {Flow}
	 */
	this.removeLink = function(fromUID,toUID){
		flowComp.removeLink(fromUID,toUID)
	}
	
	/**
	 * ɾ���ڵ�
	 * @memberOf {Flow}
	 */
	this.removeNode = function(uid){
		flowComp.removeFigureNode(uid);
	}
};