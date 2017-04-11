package tydic.ws;

import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import tydic.frame.common.Log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.ws.Endpoint;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description <br>
 * @date 2012-03-19
 */
public class WebServiceServlet extends CXFNonSpringServlet {
    /**
     * 进行webService的部署，部署的URL为${rootPath}/webService/服务名
     * @param servletConfig
     * @throws ServletException
     */
    public void loadBus(ServletConfig servletConfig) throws ServletException {
        super.loadBus(servletConfig);
        //解析要发布的类集合
        String[] serviceCalsses = servletConfig.getInitParameter("serviceCalss")
                .replaceAll("\\n", "").replaceAll("\\s", "").split(",");
        for(String service:serviceCalsses){
            Class serviceCls;
            try {
                serviceCls=Class.forName(service);
            } catch (ClassNotFoundException e) {
                Log.error(service+" not found!");
                continue;
            }
            try {
                Endpoint.publish("/"+serviceCls.getSimpleName(),serviceCls.newInstance());
            } catch (InstantiationException e) {
                Log.error(null,e);
                continue;
            } catch (IllegalAccessException e) {
                Log.error(null,e);
                continue;
            }
        }

    }
}
