package tydic.meta.acc;

public class IsAccCheckAction {
	private IsAccCheckDao IsAccCheckDao = new IsAccCheckDao();
	public boolean IsAccCheck(String userName){
		boolean flag = false;
		String isAccCheck = "";
		if(!(IsAccCheckDao.IsAccCheck(userName) == null))
			isAccCheck = IsAccCheckDao.IsAccCheck(userName);
		if(isAccCheck.equals("1"))
			flag = true;
		return flag;
	}
}
