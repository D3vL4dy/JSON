package jsonTest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json03 {

	public static void main(String[] args) {
		System.out.println("[json-simple 라이브러리 사용해 [JSONArray - JSONObject] 데이터 생성 및 파싱]");

		/*
		 * [설 명] 
		 * 1. json-simple는 google에서 제공해주는 json사용 라이브러리 입니다 
		 * 2. jsonObject.put(key, value); 형태로 데이터를 삽입합니다 
		 * 3. jsonObjectParse.get(key); 형태로 데이터를 추출합니다 
		 * 4. jsonArray.add(value); 형태로 데이터를 삽입합니다 
		 * 5. jsonArray.get(배열 번지); 형태로 데이터를 추출합니다
		 * 6. JSONParser 는 json 데이터 파싱을 도와주는 객체입니다
		 */

		// ==== JSONArray 객체를 생성하고 데이터를 삽입합니다 ====
		JSONArray jsonArray = new JSONArray();

		JSONObject jsonObject_One = new JSONObject();
		
		jsonObject_One.put("name", "투케이");
		jsonArray.add(jsonObject_One.toString());

		JSONObject jsonObject_Two = new JSONObject();
		
		jsonObject_Two.put("name", "케이투");
		jsonArray.add(jsonObject_Two.toString());

		System.out.println("JSONArray 데이터 : " + jsonArray.toString());

		// ==== JSONArray 데이터를 파싱합니다 ====
		JSONArray jsonArrayParse = (JSONArray) jsonArray;
		
		for (int i = 0; i < jsonArrayParse.size(); i++) {
			
			System.out.println("전체 arr[" + i + "] : " + jsonArrayParse.get(i));
			String data = String.valueOf(jsonArrayParse.get(i)); // 데이터 string 형태 저장
			
			JSONParser parser = new JSONParser(); // 파싱 객체 생성
			Object obj = null;
			
			try {
				obj = parser.parse(data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			JSONObject jsonObject_Parse = (JSONObject) obj;
			
			System.out.println("파싱 이름 : " + jsonObject_Parse.get("name"));
			System.out.println("");
		}
		System.out.println("");

	}

}