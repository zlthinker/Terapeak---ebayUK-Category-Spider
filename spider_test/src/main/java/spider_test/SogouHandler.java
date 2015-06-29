package spider_test;

import io.netty.handler.codec.http.HttpMethod;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.epiclouds.handlers.AbstractHandler;
import org.epiclouds.handlers.AbstractNettyCrawlerHandler;
import org.epiclouds.handlers.util.ProxyStateBean;
import org.epiclouds.spiders.dbstorage.data.impl.DBDataEntry;
import org.epiclouds.spiders.dbstorage.manager.abstracts.DBMangerInterface;
import org.epiclouds.spiders.dbstorage.manager.abstracts.StorageBean;
import org.epiclouds.spiders.dbstorage.manager.abstracts.StorageBean.OperationType;
import org.epiclouds.spiders.spiderobject.abstracts.AbstractSpiderObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.epiclouds.spiders.dbstorage.manager.impl.MongoDBSyncManger;

public class SogouHandler extends AbstractNettyCrawlerHandler{

	private ConcurrentHashMap<String, String> childMap=new ConcurrentHashMap<String, String>();
	
	public SogouHandler(ProxyStateBean proxyAddr, String schema, String host,
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
		Elements eles=doc.select("a[class=\"ch\"]");
		String category = null;
		String link = null;
		for(int i=0;i<eles.size();i++){
		//	Elements temp = eles.get(i).select("a[class]");
			link = eles.get(i).attr("href");
			link = link.replace("http://", "");
			category = eles.get(i).text();
			childMap.put(category, link);
			System.out.println(this.getSpider().getId() + i + ": Name " + category + ", URL " + link);
			
		}
		/*******zl modify end***************/		
		
		// TODO Auto-generated method stub
		System.err.println(this.getSpider().getId()+" sogou length:"+content.length());
		stop();
	}

	@Override
	protected void onBefore() {
		// TODO Auto-generated method stub
		System.err.println(this.getSpider().getId()+" sogou before");
	}

	@Override
	protected void onNormalFinished() {
		/*******zl modify start*******/
//		System.out.println("onNormalFinished() is called.");
		AbstractSpiderObject spider = this.getSpider();
		List<AbstractSpiderObject> children = new ArrayList<AbstractSpiderObject>();
		for(Map.Entry<String, String> entry: this.getMap().entrySet() ) {
			//totalSpiderNum need to be changed
			TestSpider1 child = new TestSpider1(entry.getKey(), spider, 1, entry.getValue(), false);
			children.add(child);
			System.out.println("Spider " + spider.getId() + "'s child: "+ child.getId() + " is added.");
		}
	//	spider.setChildren(children);
	//	spider.setIsrun(new AtomicBoolean(false));
		
		/******zl modify end******/
		
		// TODO Auto-generated method stub
		System.err.println(this.getSpider().getId()+"normal finished1");
		if (childMap.size() < 1) 
			this.getSpider().finish();
	}

	@Override
	protected void onDataFinished() {
		/******zl modify start******/
//		System.out.println("In SogouHandler: onDataFinished() is called.");
		try {
			DBMangerInterface DBManager = this.getSpider().getDbmanager();
		
		for(Map.Entry<String, String> entry: this.getMap().entrySet() ) {
			StorageBean.Builder builder = StorageBean.Builder.newBuilder(OperationType.INSERT, "ebayUK", "Categories");		
			DBDataEntry category = new DBDataEntry("topCategory", entry.getKey());
			DBDataEntry url = new DBDataEntry("URL", entry.getValue());
			builder.addDataEntry(category);
			builder.addDataEntry(url);
			StorageBean sb = builder.build();
			DBManager.execute(sb);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("onDataFinished(): Data is stored in MongoDB.");
		/******zl modify end*******/
		
		System.err.println(this.getSpider().getId()+" data finished");
	}
	
	public ConcurrentHashMap<String, String> getMap() {
		return childMap;
	}

}
