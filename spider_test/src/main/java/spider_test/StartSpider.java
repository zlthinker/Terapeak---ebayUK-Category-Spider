package spider_test;

import org.epiclouds.spiders.bootstrap.imp.Bootstrap;
import org.epiclouds.spiders.command.abstracts.ConsoleCommandBean;
import org.epiclouds.spiders.spiderobject.abstracts.SpiderObjectInterface;
import org.epiclouds.spiders.spiderobject.manager.abstracts.SpiderObjectManagerInterface;

import com.alibaba.fastjson.JSONObject;

public class StartSpider {
	final private String  addMessage="";
	final private String  deleteMessage="";
	public static SpiderObjectManagerInterface spiderManager;
	
	public static void main(String[] args) throws Exception {
		Bootstrap bt=new Bootstrap();
		bt.setBootSpiderClass(TestSpider1.class).start();
		TestSpider1 spider=new TestSpider1("sogo1",null,0, "www.ebay.co.uk/sch/allcategories/all-categories", true);
		TestSpider1 spider2=new TestSpider1("sogo2",null,0);
		spiderManager=bt.getSingle().getSpiderManager();
		try{
			spiderManager.delete("sogo1");
			System.out.println("Sogo1 was deleted.");
			spiderManager.add(spider);
			spiderManager.start("sogo1");
			Thread.sleep(20*1000);
	//		spiderManager.stop("sogo1");
	//		System.out.println("Sogo1 has stopped.");
	//		spiderManager.update(spider);
			//spiderManager.add(spider2);
			//spiderManager.delete("sogo1");
			//Thread.sleep(100*1000);
	//		spiderManager.stop("sogo2");
	//		spiderManager.start("sogo2");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public ConsoleCommandBean  getCommandBean(String message){
		ConsoleCommandBean commandBean=JSONObject.parseObject(message, ConsoleCommandBean.class);
		return commandBean;
	}
	
	public String detele(){
		return "";
	}
	
	
	public String add(){
		return "";
	}
	
	public String start(){
		return "";
	}

}
