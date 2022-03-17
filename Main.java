package jsonTest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

	public static void main(String[] args) {
		// JSON 데이터를 JAVA Object로 변환
//		String jsonString = "{'age' : 20, 'name' : 'kji'}";
//		
//		Gson gson = new GsonBuilder().create();
//		
//		// fromJson (json구조, 변환을 원하는 class) 
//		Student student = gson.fromJson(jsonString, Student.class);
//		
//		student.getInfo();
		
		// JAVA Object를 JSON으로 변환
		Student student = new Student(20, "kji");
		
		Gson gson = new GsonBuilder().create();
		
		// toJson (json으로 변환하고 싶은 데이터)
		String json = gson.toJson(student);
		
		System.out.println(json);
	}

}
