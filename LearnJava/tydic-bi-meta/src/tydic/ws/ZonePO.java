package tydic.ws;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 地域PO <br>
 * @date 2012-03-22
 */
public class ZonePO {

    private long zoneID;
    private long zoneParId;
    private String zoneName;
    private String zoneDesc;
    private String zoneCode;

    public long getZoneID() {
        return zoneID;
    }

    public void setZoneID(long zoneID) {
        this.zoneID = zoneID;
    }

    public long getZoneParId() {
        return zoneParId;
    }

    public void setZoneParId(long zoneParId) {
        this.zoneParId = zoneParId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneDesc() {
        return zoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }
}
