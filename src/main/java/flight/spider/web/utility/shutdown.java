package flight.spider.web.utility;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.PooledDataSource;

import flight.spider.web.service.scheduleJobService;


public class shutdown implements ServletContextListener{

    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            // Get a reference to the Scheduler and shut it down
            // Sleep for a bit so that we don't get any errors
        	System.out.print("server shutdown");
        	for (Object o : C3P0Registry.getPooledDataSources()) {
  			  try {
  			    ((PooledDataSource) o).close();
  			  } catch (Exception e) {
  			    // oh well, let tomcat do the complaing for us.
  			  }
  			}
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void contextInitialized(ServletContextEvent arg0) {
    }
}