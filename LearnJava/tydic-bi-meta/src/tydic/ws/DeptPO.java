package tydic.ws;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 用户部门的PO对象 <br>
 * @date 2012-03-22
 */
public class DeptPO {
    //部门ID
    private int deptId;
    //部门名称
    private String deptName;
    //部门父级ID
    private int deptParId;

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getDeptParId() {
        return deptParId;
    }

    public void setDeptParId(int deptParId) {
        this.deptParId = deptParId;
    }
}
