package spider_test;

import java.net.InetSocketAddress;
import java.util.Map;

import io.netty.handler.codec.http.HttpMethod;

import org.epiclouds.handlers.AbstractHandler;
import org.epiclouds.handlers.util.ProxyStateBean;
import org.epiclouds.spiders.spiderobject.abstracts.AbstractSpiderObject;
import org.epiclouds.spiders.spiderobject.manager.abstracts.SpiderObjectManagerInterface;

public class TestSpider1 extends AbstractSpiderObject{

	/******zl modify start******/
	private boolean isTop;
	private String URL;
	public TestSpider1(String id,
			AbstractSpiderObject parent, int totalSpiderNum, String URL, boolean isTop) {
		super(id, parent, totalSpiderNum);
		this.URL = URL;
		this.isTop = isTop;
	}
	/******zl modify end******/
	
	public TestSpider1(String id,
			AbstractSpiderObject parent, int totalSpiderNum) {
		super( id,parent, totalSpiderNum);
	}
	
	public TestSpider1(){
		super("",null, 0);
	}


	@Override
	public AbstractHandler createSpiderHandler() {
		// TODO Auto-generated method stub
		if (isTop) {
			return new SogouHandler(new ProxyStateBean(new 
				InetSocketAddress("106.3.38.50", 
						4080), "yuanshuju:yuanshuju"),
						"http", URL, "/", HttpMethod.GET, null, null, "utf-8", this);
		} else {
			return new ChildCategoryHandler(new ProxyStateBean(new 
					InetSocketAddress("106.3.38.50", 
							4080),"yuanshuju:yuanshuju"),
							"http", URL, "/", HttpMethod.GET, null, null, "utf-8", this);
			
			
		}

	}
	
	public void setisTop(boolean isTop) {
		this.isTop = isTop;
	}
	public boolean getisTop() {
		return isTop;
	}
	
}
