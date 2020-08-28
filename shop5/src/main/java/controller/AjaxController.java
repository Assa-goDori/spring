package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import logic.ShopService;

@RestController		//RestController : Controller + ResponseBody. 데이터를 view를 거치지 않고 직접전달할 때 사용(ajax)
					//ResponseBody : View 없이 직접 데이터를 클라이언트에 전송
@RequestMapping("ajax")
public class AjaxController {
	@Autowired
	ShopService service;
	
	@RequestMapping(value="graph1", produces="text/plain; charset=UTF8")	//보내줄 data의 타입과 형태를 지정
	public String graph1() {
		//map : ("홍길동",3), ("김삿갓",7), ... key와 value의 쌍으로 이루어진 데이터 set
		Map<String, Object> map = service.graph1();
		/*
		 * json : [{"name":"홍길동", "cnt":"3"}, {"name":"김삿갓", "cnt":"7"}, ... ]
		 */
		StringBuilder json = new StringBuilder("[");
		int i = 0;
		//json 형태의 문자열로 생성하기
		for(Map.Entry<String,Object> me : map.entrySet()) {
			json.append("{\"name\":\""+me.getKey() + "\"," + "\"cnt\":\""+me.getValue()+"\"}");
			i++;
			if(i<map.size())
				json.append(",");
		}
		json.append("]");
		return json.toString();	//RestController에서는 메서드 리턴타입이 String 일 때, view이름이 아니라 data정보를 return함
	}
	
	@RequestMapping(value="graph2", produces="text/plain; charset=UTF8")
	public String graph2() {
		//map : ("2020-07-01",7), ("2020-07-08",3), ... key와 value의 쌍으로 이루어진 데이터 set
		Map<String, Object> map = service.graph2();
		StringBuilder json = new StringBuilder("[");
		int i = 0;
		for(Map.Entry<String,Object> me : map.entrySet()) {
			json.append("{\"regdate\":\""+me.getKey() + "\"," + "\"cnt\":\""+me.getValue()+"\"}");
			i++;
			if(i<map.size())
				json.append(",");
		}
		json.append("]");
		return json.toString();
	}
	
	@RequestMapping(value="exchange1", produces="text/html; charset=UTF8")
	public String exchange1() {
		String url = "https://www.koreaexim.go.kr/site/program/financial/exchange?menuid=001001004002001";
		Document doc = null;
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		try {
			doc = Jsoup.connect(url).get();
			Elements e1 = doc.select(".tc");	//국가코드
			Elements e2 = doc.select(".tl2.bdl");//국가이름
			for(int i=0; i<e1.size(); i++) {
				if(e1.get(i).html().equals("USD")) {
					list.add(e1.get(i).html());		//USD정보 저장
					for(int j=1;j<=6;j++) {
						list.add(e1.get(i+j).html());
					}
					break;
				}
			}
			for(Element ele : e2) {
				if(ele.html().contains("미국")) {
					list2.add(ele.html());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		StringBuilder html = new StringBuilder();
		html.append("<table style='border: 1px solid white; border-collapse: collapse;'>");
		html.append("<caption>수출입은행 " + today + "</caption>");
		html.append("<tr><td colspan='3'>" + list2.get(0) + ":" + list.get(0) + "</td></tr>");
		html.append("<tr><th>기준율</th></tr>");
		html.append("<tr><td>" + list.get(3) + "</td></tr>");
		html.append("<tr><th>팔때</th></tr><tr><td>" + list.get(2) + "</td></tr>");
		html.append("<tr><th>받을때</th></tr><tr>	<td>" + list.get(1) + "</td></tr></table>");
		return html.toString();
	}
	
	@RequestMapping(value="exchange2", produces="text/html; charset=UTF8")
	public String exchange2() {
		Map<String,List<String>> map = new HashMap<>();
		StringBuilder html = null;
		try {
			String kebhana = Jsoup.connect("http://fx.kebhana.com/FER1101M.web").get().text();
			String strJson = kebhana.substring(kebhana.indexOf("{"));
			JSONParser jsonParser = new JSONParser();
			JSONObject json = (JSONObject)jsonParser.parse(strJson.trim());
			JSONArray array = (JSONArray)json.get("리스트");
			for(int i = 0; i < array.size(); i++) {
				JSONObject obj = (JSONObject)array.get(i);
				if(obj.get("통화명").toString().contains("미국") || obj.get("통화명").toString().contains("일본") || obj.get("통화명").toString().contains("유로") || obj.get("통화명").toString().contains("중국")) {
					String str = obj.get("통화명").toString();
					String[] sarr = str.split(" ");
					String key = sarr[0];
					String code = sarr[1];
					List<String> list = new ArrayList<String>();
					list.add(code);
					list.add(obj.get("매매기준율").toString());
					list.add(obj.get("현찰파실때").toString());
					list.add(obj.get("현찰사실때").toString());
					map.put(key, list);
					html = new StringBuilder();
					html.append("<table><caption>KEB하나은행( " + json.get("날짜").toString() + ")</caption>");
					html.append("<tr><th rowspan='2' style='vertical-align:middle;'>코드</th><th rowspan='2' style='vertical-align: middle;'>기준율</th><th colspan='2'>현찰</th></tr>");
					html.append("<tr><th>현찰파실때</th><th>현찰사실때</th></tr>");
					for(Map.Entry<String, List<String>> m : map.entrySet()) {
						html.append("<tr><td>" + m.getKey() + "(" + m.getValue().get(0) + ")</td><td>" + m.getValue().get(1) + "</td><td>" + m.getValue().get(2) + "</td><td>" + m.getValue().get(3) + "</td></tr>");
					}
					html.append("</table>");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html.toString();
	}
}