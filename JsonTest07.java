package json;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

// GSON : JSON Object를 쉽게 JSON Object로 변환해주는 라이브러리

public class JsonTest07 {

	public static void main(String[] args) throws ParseException {
		Student student = new Student(20210720, "홍길동", 21,"컴퓨터공학과");
		
		// Gson 객체 생성
		Gson gson = new Gson();
		
		// Java객체를 JSON형태로 반환(String)
		String jsonStr = gson.toJson(student);
		
		// JSONParser 객체 생성
		JSONParser parser = new JSONParser();
		
		// String을 JSONObject로 변환
		JSONObject jsonObj = (JSONObject)parser.parse(jsonStr); 
		
		// JSONObject 출력
		System.out.println(jsonObj);
	}

}

class Student{
	private int stuNum;
	private String name;
	private int age;
	private String major;
	
	
	public Student(int stuNum, String name, int age, String major) {
		super();
		this.stuNum = stuNum;
		this.name = name;
		this.age = age;
		this.major = major;
	}
	
	
}
