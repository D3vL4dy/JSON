package jsonTest;

import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json02 {

	public static void main(String[] args) throws IOException {
		// FileReader로 JSON 파일을 읽은 뒤 
		// 파일을 simple-json에서 제공하는 JSONParser를 사용하여 파싱

		JSONParser parser = new JSONParser();
		try {
			FileReader reader = new FileReader("d:/d_other/json.txt");
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			reader.close();
			System.out.print(jsonObject);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

}
