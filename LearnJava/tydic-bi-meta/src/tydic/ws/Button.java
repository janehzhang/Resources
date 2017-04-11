package tydic.ws;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 菜单按钮PO对象 <br>
 * @date 2012-03-19
 *
 */
public class Button {

	private String buttonDesc;
	private String buttonId;

	public Button(){

	}

    public String getButtonDesc(){
        return buttonDesc;
    }

    public void setButtonDesc(String buttonDesc){
        this.buttonDesc = buttonDesc;
    }

    public String getButtonId(){
        return buttonId;
    }

    public void setButtonId(String buttonId){
        this.buttonId = buttonId;
    }
}