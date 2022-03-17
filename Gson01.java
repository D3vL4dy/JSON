package jsonTest;

import com.google.gson.Gson;

public class Gson01 {

	public static void main(String[] args) {
		Gson gson = new Gson();
		int[] ints = {1, 2, 3, 4, 5};
		String[] strings = {"abc", "def", "ghi"};

		// 직렬화
		gson.toJson(ints);     // ==> [1,2,3,4,5]
		gson.toJson(strings);  // ==> ["abc", "def", "ghi"]

		// 역직렬화
		int[] ints2 = gson.fromJson("[1,2,3,4,5]", int[].class); 
		// ==> ints2 will be same as ints

	}

}
