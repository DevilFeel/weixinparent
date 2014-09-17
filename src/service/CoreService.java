package service;

import hyit.app.dao.factory.DAOFactory;
import hyit.app.vo.RoommateInfo;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import message.resp.Article; 
import message.resp.NewsMessage;
import message.resp.TextMessage;
import util.MessageUtil;


public class CoreService {
	public static String TransactSQLInjection(String str)

    {

          return str.replaceAll(".*([';]+|(--)+).*", " ");

    }
	public String getCustomizeMenu(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("感谢您的关注！").append("\n\n");
		//buffer.append("想关注你或者你孩子在学校的学习").append("\n");
		buffer.append("【家长】请回复“卡号” + “您孩子的一卡通号码完成定制”。 \n如：卡号21121101").append("\n\n");
		buffer.append("【新同学】可以回复“身份证” + “你的身份证号码查询你自己的宿舍号以及你宿舍的所有成员哦”。 \n如：身份证320381xxx").append("\n\n");
		buffer.append("或回复“宿舍号” + “你的宿舍号”，也可以查看你的宿舍成员哦\n如：宿舍号41421").append("\n\n");
		return buffer.toString();
	}
	
	public String getFirstCustomize(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好，您已经定制成功!\n你可以回复“身份证” + “你的身份证号码查询你自己的宿舍号以及你宿舍的所有成员哦”。 \n如：身份证320381xxx").append("\n\n");
		buffer.append("也可以回复“宿舍号” + “您孩子的宿舍号”，查看你的宿舍成员哦\n例如：宿舍号41421").append("\n\n");
		return buffer.toString();
	}
	public String getSecondCustomize(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好，你还没有定制成功，请回复\n“卡号” + "
				+ "“关注的学生的一卡通号”进行定制。\n如：卡号21121101").append("\n\n");
		buffer.append("\n你可以回复“身份证” + “你的身份证号码查询你自己的宿舍号以及你宿舍的所有成员哦”。 \n如：身份证320381xxx").append("\n\n");
		buffer.append("也可以回复“宿舍号” + “您孩子的宿舍号”，查看你的宿舍成员哦\n例如：宿舍号41421").append("\n\n");
		return buffer.toString();
	}
	public static String processRequest(HttpServletRequest request) {
		String respMessage = null;
		String dbUrl = "jdbc:mysql://localhost:3306/attendanceparent";
		String dbUser = "root";
		String dbPwd = "nicai";
		
		try{
			String respContent = "请求处理异常，请稍后尝试！";
			
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			String fromUserName = requestMap.get("FromUserName");
			String toUserName = requestMap.get("ToUserName");
			String msgType = requestMap.get("MsgType");
			
			//回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			//回复图文消息
			NewsMessage newsMessage = new NewsMessage();  
            newsMessage.setToUserName(fromUserName);  
            newsMessage.setFromUserName(toUserName);  
            newsMessage.setCreateTime(new Date().getTime());  
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);  
            newsMessage.setFuncFlag(0); 
            List<Article> articleList = new ArrayList<Article>();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
			//回复文本消息
			if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
				//String reqContent =requestMap.get("Content").trim();  //去收尾空格
				String reqContent =requestMap.get("Content").replace(" ", ""); //去掉所有空格
				//定制
				if(reqContent.startsWith("卡号")){
					String reqCardId = reqContent.substring(2);
					if(reqCardId.length()==8){
					reqCardId = TransactSQLInjection(reqCardId);
					
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection conn = null;
					conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
					Statement stmt;
					stmt = conn.createStatement();
					ResultSet result =stmt.executeQuery("select * from user_info where openid = '" + fromUserName + "'");
					boolean flag = false;
					while(result.next()){
						flag = true;
						stmt = conn.createStatement();
						stmt.executeUpdate("update user_info set cardid ='"+reqCardId+"' where openid = '" + fromUserName + "' ");
						Article article1 = new Article();
						article1.setTitle("卡号绑定成功，欢迎来到\n淮阴工学院计算机工程学院");
						article1.setDescription("");
						article1.setPicUrl("http://liuwenru491001.wicp.net/weixinparent/images/01.jpg");
						article1.setUrl("http://liuwenru491001.wicp.net/weixinparent/introduction.html");

						Article article2 = new Article();
						article2.setTitle("【计算机▪迎新专栏】新生安全须知");
						article2.setDescription("");
						article2.setPicUrl("http://liuwenru491001.wicp.net/weixinparent/images/04.jpg");
						article2.setUrl("http://liuwenru491001.wicp.net/weixinparent/safety.html");

//						Article article3 = new Article();
//						article3.setTitle("淮阴工学院计算机工程学院");
//						article3.setDescription("");
//						article3.setPicUrl("http://1.weixinparent.sinaapp.com/images/03.jpg");
//						article3.setUrl("http://ced.hyit.edu.cn/Manager/NewsShow.aspx?newsID=155");
//						
//						Article article4 = new Article();
//						article4.setTitle("淮阴工学院计算机工程学院");
//						article4.setDescription("");
//						article4.setPicUrl("http://1.weixinparent.sinaapp.com/images/03.jpg");
//						article4.setUrl("http://ced.hyit.edu.cn/Manager/NewsShow.aspx?newsID=155");
						
						articleList.add(article1);
						articleList.add(article2);
//						articleList.add(article3);
//						articleList.add(article4);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						//respMessage = MessageUtil.newsMessageToXml(newsMessage);
						respContent = MessageUtil.newsMessageToXml(newsMessage);
						//respContent ="您输入的一卡通号码 已更新完成。\n" 
							//	+ "您还可以查看<a href=\"http://ced.hyit.edu.cn/Manager/NewsShow.aspx?newsID=155\">计算机工程学院简介</a>";
					}
					if(!flag){
						//定制新的学号
						Article article1 = new Article();
						article1.setTitle("卡号绑定成功，欢迎来到\n淮阴工学院计算机工程学院");
						article1.setDescription("");
						article1.setPicUrl("http://liuwenru491001.wicp.net/weixinparent/images/01.jpg");
						article1.setUrl("http://liuwenru491001.wicp.net/weixinparent/introduction.html");

						Article article2 = new Article();
						article2.setTitle("【计算机▪迎新专栏】新生安全须知");
						article2.setDescription("");
						article2.setPicUrl("http://liuwenru491001.wicp.net/weixinparent/images/04.jpg");
						article2.setUrl("http://liuwenru491001.wicp.net/weixinparent/safety.html");
//
//						Article article3 = new Article();
//						article3.setTitle("淮阴工学院计算机工程学院");
//						article3.setDescription("");
//						article3.setPicUrl("http://1.weixinparent.sinaapp.com/images/03.jpg");
//						article3.setUrl("http://ced.hyit.edu.cn/Manager/NewsShow.aspx?newsID=155");
//						
//						Article article4 = new Article();
//						article4.setTitle("淮阴工学院计算机工程学院");
//						article4.setDescription("");
//						article4.setPicUrl("http://1.weixinparent.sinaapp.com/images/03.jpg");
//						article4.setUrl("http://ced.hyit.edu.cn/Manager/NewsShow.aspx?newsID=155");
						
						articleList.add(article1);
						articleList.add(article2);
//						articleList.add(article3);
//						articleList.add(article4);
						newsMessage.setArticleCount(articleList.size());
						newsMessage.setArticles(articleList);
						//respMessage = MessageUtil.newsMessageToXml(newsMessage);
						respContent = MessageUtil.newsMessageToXml(newsMessage);
						//respContent = MessageUtil.newsMessageToXml(newsMessage);
						//respContent ="您输入的一卡通号码 已完成定制。\n" 
							//	+ "您还可以查看<a href=\"http://ced.hyit.edu.cn/Manager/NewsShow.aspx?newsID=155\">计算机工程学院简介</a>";
						stmt = conn.createStatement();
						stmt.executeUpdate("insert into user_info(openid, cardid, reqtime) values('"
									+ fromUserName 
									+ "','" 
									+ reqCardId
									+ "','"
									+ sdf.format(new Date())
									+"')");
						}
						result.close();
						stmt.close();
						conn.close();
					}
					else{
						respContent="您输入的一卡通号码位数不是8位，请重新输入";
						textMessage.setContent(respContent);
						respContent = MessageUtil.textMessageToXml(textMessage);
					}
					//textMessage.setContent(respContent);
					//respMessage = MessageUtil.textMessageToXml(textMessage);
					respMessage = respContent;
				}
				else if(reqContent.startsWith("身份证")){
					String reqIdentity = reqContent.substring(3);
					if(reqIdentity.length() == 18){
							//获得宿舍成员
							RoommateInfo roommate = null;
							roommate = DAOFactory.getIIRoommateInfoDAOInstance().getByIdentificationCard(reqIdentity);
							if(roommate == null){
								respContent = "抱歉，没有该身份证的信息。";
							}
							else{
								List<RoommateInfo> list = null;
								try {
									list = DAOFactory.getIIRoommateInfoDAOInstance()
											.getRoommateListByBuildingNumber(roommate);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								String StrNumber = String.valueOf(roommate.getDormitory());
								
								String Str = "您的宿舍成员为：\n";
								Iterator<RoommateInfo> iter = list.iterator();
								while(iter.hasNext()){
									roommate = iter.next();
									Str = Str + roommate.getName() +"," + roommate.getClassname() +"," + roommate.getMiddleSchool() +"\n"  ;
								}
								
								respContent ="你的宿舍号是：【" + StrNumber + "】\n" + Str;								
							}

							//respContent = "舍友功能即将上线，敬请期待!";
							textMessage.setContent(respContent);
							respContent = MessageUtil.textMessageToXml(textMessage);
					}
					else{
						respContent = "您输入的身份证不是18位，请重新输入。";
						textMessage.setContent(respContent);
						respContent = MessageUtil.textMessageToXml(textMessage);
					}
				}
				else if(reqContent.startsWith("宿舍号")){
					String reqDormitory = reqContent.substring(3); //获得输入的宿舍楼+宿舍号
					if(reqDormitory.length() == 5){
						Class.forName("com.mysql.jdbc.Driver").newInstance();
						Connection conn = null;
						conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
						Statement stmt;
						stmt = conn.createStatement();
						ResultSet result =stmt.executeQuery("select * from user_info where openid = '" + fromUserName + "'");
						boolean flag = false;
						//已经绑定
						while(result.next()){
							flag = true;
							//获得宿舍成员
							RoommateInfo roommate = new RoommateInfo();
							roommate.setDormitory(Integer.parseInt(reqDormitory));
							List<RoommateInfo> list = null;
							try {
								list = DAOFactory.getIIRoommateInfoDAOInstance()
										.getRoommateListByBuildingNumber(roommate);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String Str = "您的宿舍成员为：\n";
							Iterator<RoommateInfo> iter = list.iterator();
							while(iter.hasNext()){
								roommate = iter.next();
								Str = Str + roommate.getName() +"," + roommate.getClassname() +"," + roommate.getMiddleSchool() +"\n"  ;
							}
							respContent = Str;
							//respContent = "舍友功能即将上线，敬请期待!";
							textMessage.setContent(respContent);
							respContent = MessageUtil.textMessageToXml(textMessage);
						}
						//未绑定
						if(!flag){
							CoreService StrTemp = new CoreService();
							respContent = StrTemp.getSecondCustomize();
							textMessage.setContent(respContent);
							respContent = MessageUtil.textMessageToXml(textMessage);
						}
					}
					else{
						respContent="请输入5位数的宿舍号，\n例如：宿舍号41421";
						textMessage.setContent(respContent);
						respContent = MessageUtil.textMessageToXml(textMessage);
					}
					
				}
				else{
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection conn = null;
					conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
					Statement stmt;
					stmt = conn.createStatement();
					ResultSet result =stmt.executeQuery("select * from user_info where openid = '" + fromUserName + "'");
					boolean flag = false;
					while(result.next()){
						flag = true;
						CoreService StrTemp = new CoreService();
						respContent = StrTemp.getFirstCustomize();
						textMessage.setContent(respContent);
						respContent = MessageUtil.textMessageToXml(textMessage);
					}
					if(!flag){
						CoreService StrTemp = new CoreService();
						respContent = StrTemp.getSecondCustomize();
						textMessage.setContent(respContent);
						respContent = MessageUtil.textMessageToXml(textMessage);
					}
					
				}
				
			}
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
				String eventType = requestMap.get("Event");
				if(eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
					CoreService StrTemp = new CoreService();
					respContent = StrTemp.getCustomizeMenu();
					textMessage.setContent(respContent);
					respContent = MessageUtil.textMessageToXml(textMessage);			
				}
			}
			
			//textMessage.setContent(respContent);
			//respMessage = MessageUtil.textMessageToXml(textMessage);
			respMessage = respContent;
		} catch (Exception e){
			e.printStackTrace();
		}
		return respMessage;
	}
}

