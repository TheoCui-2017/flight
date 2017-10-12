package flight.spider.web.processor;


import java.net.URLEncoder;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;


/**
 *orbitzProcessor
 *
 */
public class orbitzProcessor implements PageProcessor {

	public String theCode = "";
	private Site site = Site.me().addHeader("User-Agent",  
            "ozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36 Core/1.47.516.400 QQBrowser/9.4.8188.400");
    private boolean detail = false;
    public orbitzProcessor() {
        
    }
    
    public orbitzProcessor(String startUrl) {
        this.site = Site.me().setDomain(UrlUtils.getDomain(startUrl));
    }

    @Override
    public void process(Page page) {
    	if(!detail){
    		theCode = page.getHtml().xpath("//div[@id='originalContinuationId']/text()").toString();
    		System.out.println("--------------------------------------------------------");
            System.out.println(theCode);
            page.addTargetRequest("https://www.orbitz.com/Flight-Search-Paging?c="+theCode+"&is=1&sp=asc&cz=200&cn=0");
            detail = true;
    	}else{
    		JSONObject obj = (JSONObject) JSONObject.parse(page.getJson().jsonPath("content").toString());
    		System.out.println( obj.get("index"));
    		JSONArray index=obj.getJSONArray("index");
    		for(Object ind : index){
    			String offerId = URLEncoder.encode(ind.toString());
    			System.out.println("https://www.orbitz.com/Flight-Search-Details?c="+theCode+"&offerId="+offerId);
    			page.addTargetRequest("https://www.orbitz.com/Flight-Search-Details??c="+theCode+"&offerId="+offerId);
    		}
    	}
    }

    @Override
    public Site getSite() {
        //settings
        return site;
    }
}
