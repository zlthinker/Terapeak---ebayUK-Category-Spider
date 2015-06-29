package spider_test;

import io.netty.handler.codec.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.epiclouds.handlers.AbstractHandler;
import org.epiclouds.handlers.AbstractNettyCrawlerHandler;
import org.epiclouds.handlers.util.ProxyStateBean;
import org.epiclouds.spiders.dbstorage.data.impl.DBDataEntry;
import org.epiclouds.spiders.dbstorage.manager.abstracts.DBMangerInterface;
import org.epiclouds.spiders.dbstorage.manager.abstracts.StorageBean;
import org.epiclouds.spiders.dbstorage.manager.abstracts.StorageBean.OperationType;
import org.epiclouds.spiders.dbstorage.manager.impl.MongoDBSyncManger;
import org.epiclouds.spiders.spiderobject.abstracts.AbstractSpiderObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChildCategoryHandler extends AbstractNettyCrawlerHandler{
	private ConcurrentHashMap<String, String> childMap=new ConcurrentHashMap<String, String>();

	public ChildCategoryHandler(ProxyStateBean proxyAddr, String schema, String host,
			String url, HttpMethod md, Map<String, String> headers,
			Map<String, String> postdata, String charset,
			AbstractSpiderObject spider) {
		super(proxyAddr, schema, host, url, md, headers, postdata, charset, spider);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handle(String content) throws Exception {
		/*******zl modify start***************/
		Document doc=Jsoup.parse(content);
		if(doc==null||"".equals(content)){
			throw new Exception("return content is null!");
		}
		Elements eles=doc.select("div[class=\"cat-link\"]");
		String childCategory = null;
		String childLink = null;
		for(int i=0;i<eles.size();i++){
			Elements temp = eles.get(i).select("a[href]");
			childLink = temp.attr("href");
			childLink = childLink.replace("http://", "");
			childCategory = temp.text();
			childMap.put(childCategory, childLink);
			System.out.println(this.getSpider().getId() + i + ": " + childCategory + ", " + childLink);
			
		}
		/*******zl modify end***************/		
		
		// TODO Auto-generated method stub
		System.err.println(this.getSpider().getId()+" sogou length:"+content.length());
		stop();
	}

	@Override
	protected void onBefore() {
		// TODO Auto-generated method stub
	//	System.err.println(this.getSpider().getId()+" ChildCategory before");
	}

	@Override
	protected void onNormalFinished() {
		/*******zl modify start*******/
		System.out.println("onNormalFinished() is called.");
		AbstractSpiderObject spider = this.getSpider();
		List<AbstractSpiderObject> children = new ArrayList<AbstractSpiderObject>();
		for(Map.Entry<String, String> entry: this.getMap().entrySet() ) {
			//totalSpiderNum need to be changed
			TestSpider1 child = new TestSpider1(entry.getKey(), spider, 1, entry.getValue(), false);
			children.add(child);
			System.out.println("Spider " + spider.getId() + "'s child: "+ child.getId() + " is added.");
		}
		spider.setChildren(children);
		spider.setIsrun(new AtomicBoolean(false));
		/******zl modify end******/
		
		// TODO Auto-generated method stub
		System.err.println(this.getSpider().getId()+" ChildCategory normal finished.");
		if (childMap.size() < 1) 
			this.getSpider().finish();
	}

	@Override
	protected void onDataFinished() {
		/******zl modify start******/
	//	System.out.println("In ChildCategoryHandler: onDataFinished() is called.");
		try {
			DBMangerInterface DBManager = this.getSpider().getDbmanager();
		
		for(Map.Entry<String, String> entry: this.getMap().entrySet() ) {
			StorageBean.Builder builder = StorageBean.Builder.newBuilder(OperationType.INSERT, "ebayUK", "Categories");		
			DBDataEntry category = new DBDataEntry("childCategory", entry.getKey());
			DBDataEntry url = new DBDataEntry("URL", entry.getValue());
			builder.addDataEntry(category);
			builder.addDataEntry(url);
			StorageBean sb = builder.build();
			DBManager.execute(sb);		
			}
		System.err.println(this.getSpider().getId()+" ChildCategory data finished.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/******zl modify end*******/
		
		
	}

	public ConcurrentHashMap<String, String> getMap() {
		return childMap;
	}
}
