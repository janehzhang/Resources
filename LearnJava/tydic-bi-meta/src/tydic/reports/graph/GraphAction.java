package tydic.reports.graph;
/**
 by qx
 * @modifyDate  2013-9-12
 */
public class GraphAction {
    /**
     * 数据库操作类
     */
    private GraphDAO graphDAO;    

	public GraphDAO getGraphDAO() {
		return graphDAO;
	}

	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	//所有子节点
	public String getZoneLevel(String zoneCode) {
		String dimLevel=graphDAO.getZoneLevel(zoneCode);
		return dimLevel;
	}
}
