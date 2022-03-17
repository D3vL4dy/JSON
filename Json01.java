package jsonTest;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;

public class Json01 {

	public static void main(String[] args) {
		// JSONObject 형식의 파일을 FileWriter를 가지고 저장

		JSONObject obj = new JSONObject();
		obj.put("name", "mine-it-record");
		obj.put("mine", "GN");
		obj.put("year", 2021);
		
		try {
			FileWriter file = new FileWriter("d:/d_other/json.txt");
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.print(obj);
		
	
		
	}

}



