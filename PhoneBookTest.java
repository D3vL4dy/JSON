package jsonTest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 *  문제) 이름, 주소, 전화번호를 멤버로 갖는 Phone클래스를 만들고, 
 *      Map을 이용하여 정보를 관리하는 프로그램을 작성하시오.
 *      
 *      전화번호 정보는 Map에 저장하여 관리한다.
 *      (key값은 입력받은 사람의 '이름'으로 하고, 
 *       value값은 'Phone클래스의 인스턴스'로 한다.)
 *       
 *       아래 메뉴의 기능을 모두 작성하시오.
 *       (삭제,검색 기능은 ==> '이름'을 입력받아 처리한다.)
 *    
 *  추가조건)
 *  1) '6. 전화번호 저장' 메뉴를 추가하고 구현한다.
 *  	(저장파일명은 'phoneData.dat'로 한다.)     (엑셀, pdf 등)
 *  2) 프로그램이 시작될 때 저장된 파일이 있으면 그 데이터를 읽어와 Map에 저장한다.
 *  3) 프로그램을 종료할 때 Map의 데이터가 수정되거나 추가 또는 삭제되면 저장한 후 종료되도록 한다.
 *       
 *   메뉴 예시) 
 *      1. 전화번호 등록
 *      2. 전화번호 수정        ==> '이름'을 입력 받아 처리한다. 
 *      3. 전화번호 삭제
 *      4. 전화번호 검색 
 *      5. 전화번호 전체 출력
 *      6. 전화번호 저장
 *      0. 프로그램 종료   
 * -----------------------------------
 * 실행에시) 
 *   	1. 전화번호 등록
 *      2. 전화번호 수정        
 *      3. 전화번호 삭제
 *      4. 전화번호 검색 
 *      5. 전화번호 전체 출력
 *      6. 전화번호 저장
 *      0. 프로그램 종료   
 *     번호 입력 >> 1 
 * ------------------------------------
 *     새롭게 등록할 전화번호 정보를 입력하세요.
 *     이름 >> 홍길동 
 *     전화번호 >> 010-1111-1111
 *     주소 >> 대전시 중구 오류동 
 *     
 *     '홍길동'전화번호 정보 등록 완료!!
 * ------------------------------------
 * 		1. 전화번호 등록
 *      2. 전화번호 수정        
 *      3. 전화번호 삭제
 *      4. 전화번호 검색 
 *      5. 전화번호 전체 출력
 *      6. 전화번호 저장
 *      0. 프로그램 종료   
 * ------------------------------------    
 *     번호 입력 >> 1
 *  새롭게 등록할 전화번호 정보를 입력하세요.
 *     이름 >> 홍길동 
 *  '홍길동'은 이미 등록된 사람입니다.
 * ------------------------------------    
 *      1. 전화번호 등록
 *      2. 전화번호 수정        
 *      3. 전화번호 삭제
 *      4. 전화번호 검색 
 *      5. 전화번호 전체 출력
 *      6. 전화번호 저장
 *      0. 프로그램 종료   
 * ------------------------------------    
 *      번호입력 >> 5
 *      
 *  번호 이름    전화번호   	  주소
 *  1   홍길동  010-1111-1111  대전시 중구 오류동 
 *  
 *     
 */

public class PhoneBookTest {
	private HashMap<String, Phone> phoneBookMap;
	private Scanner scan;
	private String fileName = "d:/d_other/phoneData.dat";
	JSONObject obj;
	
	// 에디터가 변경되었는지 여부를 나타내는 변수 선언
	// 데이터가 변경되면 이 변수값이 true가 된다.
	private boolean dataChange;
	
	// 생성자
	public PhoneBookTest() {
		// 파일 내용을 읽어와 Map에 저장한다.
		phoneBookMap = load();
		
		// 파일이 없거나 잘못되었을 때
		if(phoneBookMap == null) { // 데이터를 읽어오지 못한 경우
			phoneBookMap = new HashMap<String, Phone>();
		}
		scan = new Scanner(System.in);
	}

	public static void main(String[] args) {
		new PhoneBookTest().phoneStart();
	}
	
	// 프로그램을 시작하는 메서드
	public void phoneStart() {
		System.out.println();
		System.out.println("*****************************");
		System.out.println("전화번호 관리 프로그램");
		System.out.println("*****************************");
		System.out.println();
		
		while(true) {
			int choice = displayMenu();
			switch(choice) {
			case 1 : // 등록
				insert();
				break;
			case 2 : // 수정
				update();
				break;
			case 3 : // 삭제
				delete();
				break;
			case 4 : // 검색
				search();
				break;
			case 5 : // 전체 출력
				displayAll();
				break;
			case 6 : // 저장
				save();
				break;
			case 0 :
				if(dataChange == true)
					save();
				
				System.out.println("프로그램이 종료되었습니다.");
				System.exit(0);
			default : 
				System.out.println("작업 번호를 잘못 입력했습니다.");
				System.out.println("다시 선택하세요.");
			}
		}
	}
	
	// 저장된 전화번호 정보 파일을 읽어오는 메서드
	// 반환값 : 파일에서 읽어온 데이터 (Map객체)
	private HashMap<String, Phone> load() {
		// 읽어온 데이터가 저장될 변수 선언
		HashMap<String, Phone> pMap = null;

		File file = new File(fileName);
		if (!file.exists()) { // 저장된 파일이 없으면
			return null;
		}

		ObjectInputStream ois = null;
		try {
			// 입력용 스트림 객체 생성
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
			// 파일 내용을 읽어서 변수에 저장한다.
			pMap = (HashMap<String, Phone>) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
			return null;
		} finally {
			// 사용했던 스트림 닫기
			if(ois != null)
				try { ois.close(); } catch (IOException e2) {}
		}

		return pMap;
	}
	
	// 전화번호 정보를 파일로 저장하는 메서드
	private void save() {
		ObjectOutputStream oos = null;

		try {
			// 객체 출력용 스트림 객체 생성 (보조스트림이므로 기반이 되는 스트림 객체 생성)
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));

			// Map객체를 파일로 저장한다.
			oos.writeObject(phoneBookMap);

			System.out.println("저장이 완료되었습니다.");
			
			dataChange = false;

		} catch (IOException e) {
			System.out.println("저장 실패");
			e.printStackTrace();
		} finally {
			// 사용했던 스트림 객체 닫기
			if (oos != null) try { oos.close(); } catch (IOException e2) {}
		}
		
	}

	// 전화번호 정보를 검색하는 메서드
	private void search() {
		System.out.println();
		System.out.println("검색할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = scan.next();
		
		// 입력한 사람의 이름이 전화번호 정보에 없으면
		if(!phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 등록되지 않은 사람입니다.");
			System.out.println("검색 작업을 마칩니다.");
			return;
		}
		
		Phone p = phoneBookMap.get(name);
		System.out.println(name + "씨의 전화번호 정보");
		System.out.println("----------------------------");
		System.out.println("이름 : " + p.getName());
		System.out.println("전화번호 : " + p.getTel());
		System.out.println("주소 : " + p.getAddr());
		System.out.println("----------------------------");
	}

	// 전화번호 정보를 삭제하는 메서드
	private void delete() {
		System.out.println();
		System.out.println("삭제할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = scan.next();
		
		// 입력한 사람의 이름이 전화번호 정보에 없으면
		if(!phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 등록되지 않은 사람입니다.");
			System.out.println("삭제 작업을 마칩니다.");
			return;
		}
		
		phoneBookMap.remove(name);
		
		System.out.println(name + "씨 전화번호 정보 삭제 완료!!");
		dataChange = true;
	}

	// 전화번호 정보를 수정하는 메서드
	private void update() {
		System.out.println();
		System.out.println("수정할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = scan.next();
		
		// 입력한 사람의 이름이 전화번호 정보에 없으면
		if(!phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 등록되지 않은 사람입니다.");
			System.out.println("수정 작업을 마칩니다.");
			return;
		}
		
		scan.nextLine();
		System.out.println("새로운 주소 >> ");
		String newAddr = scan.nextLine();
		
		System.out.println("새로운 전화번호 >> ");
		String newTel = scan.next();
		
		// 같은 이름의 키값으로 저장하면 수정 완료
		phoneBookMap.put(name, new Phone(name, newAddr, newTel));
		
		System.out.println(name + "씨 전화번호 정보 수정 완료!!");
		dataChange = true;
		
	}
	
	// 전화번호 정보 전체 자료를 출력하는 메서드
	private void displayAll() {
		System.out.println();
		
//		JSONParser parser = new JSONParser();
//		JSONObject object = (JSONObject)parser.parse(obj);
//		
//		JSONArray jsonArray = new JSONArray();
//		
//		JSONArray jsonArrayParse = (JSONArray) jsonArray;
		
		for (int i = 0; i < obj.size(); i++) {
			
			System.out.println("전체 arr[" + i + "] : " + obj.get(i));
			String data = String.valueOf(obj.get(i)); // 데이터 string 형태 저장
			JSONParser parser = new JSONParser(); // 파싱 객체 생성
			Object obj2 = null;
			
			try {
				obj2 = parser.parse(data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			JSONObject jsonObject_Parse = (JSONObject) obj2;
			System.out.println("파싱 이름 : " + jsonObject_Parse.get("name"));
			System.out.println("");
		}
		
		System.out.println("");
		
		// 모든 key값 가져오기
//		Set<String> phoneKeySet = phoneBookMap.keySet();
//		System.out.println("----------------------------");
//		System.out.println("번호\t이름\t전화번호\t주소");
//		System.out.println("----------------------------");
//		if(phoneKeySet.size() == 0) {
//			System.out.println("등록된 전화번호 정보가 하나도 없습니다.");
//		}else {
//			int cnt = 0; // 번호 출력용 변수
//			for(String name : phoneKeySet) {
//				cnt++;
//				Phone p = phoneBookMap.get(name);
//				System.out.println(cnt + "\t" + name + "\t" + p.getTel() + "\t" + p.getAddr());
//			}
//		}
//		System.out.println("----------------------------");
//		System.out.println("출력 완료");
	}
	
	// 새로운 전화번호 정보를 등록하는 메서드
	private void insert() {
		
		System.out.println();
		System.out.println("새롭게 등록할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = scan.next();
		
		// 이미 등록된 사람인지 여부를 검사
		if(phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 이미 등록된 사람입니다.");
			return; // return을 사용하지 않으려면 else를 써야돼 
		}
		
		System.out.print("전화번호 >> ");
		String tel = scan.next();
		
		scan.nextLine(); // 입력 버퍼 비우기
		System.out.print("주소 >> ");
//		String addr = scan.next();
		String addr = scan.nextLine();
		
		/*
		 * Scanner객체의 입력 메서드의 특징
		 * 
		 * - next(), nextInt(), nextDouble(), ...등
		 *  => 사이띄기, Tab키, Enter키를 구분 문자로 분리해서 분리된 자료만 읽어간다.
		 *  
		 * - nextLine()
		 *  => 한 줄 단위로 입력한다. 즉, 자료를 입력하고 Enter키를 누르면 Enter키까지 읽어간다.
		 *  
		 * - Scanner는 입력한 값이 입력버퍼에 먼저 저장된 후에 차례로 꺼내와서 처리된다.  
		 */
		
		// 방법1
//		Phone p = new Phone(name, tel, addr);
//		phoneBookMap.put(name, p);
		
		// 방법2
		phoneBookMap.put(name, new Phone(name, addr, tel));
		
		obj = new JSONObject(phoneBookMap);
		System.out.println(obj.toJSONString());
		
		System.out.println(name + "씨 전화번호 정보 등록 완료!!");
		dataChange = true;
	}
	
	// 메뉴를 출력하고 작업 번호를 입력받아 반환하는 메서드
	private int displayMenu() {
		System.out.println();
		System.out.println("----------------------------");
		System.out.println("1. 전화번호 등록");
		System.out.println("2. 전화번호 수정");
		System.out.println("3. 전화번호 삭제");
		System.out.println("4. 전화번호 검색");
		System.out.println("5. 전화번호 전체 출력");
		System.out.println("6. 전화번호 저장");
		System.out.println("0. 프로그램 종료");
		System.out.println("----------------------------");
		System.out.print("번호입력 >> ");
		int num = scan.nextInt();
		return num;
	}

}

// 하나의 전화번호 정보가 저장될 class 작성
class Phone implements Serializable{
	private String name;
	private String addr;
	private String tel;
	
	// 생성자
	public Phone(String name, String addr, String tel) {
		super();
		this.name = name;
		this.addr = addr;
		this.tel = tel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "Phone [name=" + name + ", addr=" + addr + ", tel=" + tel + "]";
	}
	
}