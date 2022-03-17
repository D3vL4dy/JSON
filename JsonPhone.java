package json;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import util.ScanUtil;

public class JsonPhone {
	private Map<String, Phone> phoneBookMap;
	private String fileName = "d:/d_other/jsonTest.json";
	
	// 데이터가 변경되었는지 여부를 나타내는 변수 선언
	// 데이터가 변경되면 이 변수값이 true가 된다.
	private boolean dataChange;
	
	public JsonPhone() {
		//파일 내용을 읽어와 Map에 저장한다.
		phoneBookMap = load();
		
		// 파일이 없거나 데이터를 못읽어왔을때 처리
		if(phoneBookMap == null) { 
			phoneBookMap = new HashMap<String, Phone>();
		}
	}
	
	
	public static void main(String[] args) {
		new JsonPhone().phoneStart();
	}
		
		
	@SuppressWarnings("resource")
	public void phoneStart() {

		System.out.println();
		System.out.println("*************************");
		System.out.println("        전화번호부");
		System.out.println("*************************");
		System.out.println();
		
		while(true) {
			int choice = displayMenu();
			switch(choice) {
			case 1://등록
				insert();
				break;
			case 2://수정
				update();
				break;
			case 3://삭제
				delete();
				break;
			case 4://검색
				search();
				break;
			case 5://전체출력
				displayAll();
				break;
			case 6:	//데이터 저장
				save();
				break;
			case 0:
				if(dataChange == true) save();
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("작업 번호를 잘못 입력하셨습니다.");
				System.out.println("다시 선택하세요");
			}
		}
	} 
	
	// json객체를 문자열로 저장하고 빼올때도 문자열로 빼내기
	
// 저장된 전화번호 정보 파일을 읽어오는 메서드
	// 반환값 : 파일에서 읽어온 데이터 (Map객체-phoneBookMap)
	@SuppressWarnings("unchecked")
	private Map<String, Phone> load(){
		// 읽어온 데이터가 저장될 변수 선언
		Map<String, Phone> pMap = null;
		JsonObject jsonObj = new JsonObject();
		Gson gson = new Gson();
		
		File file = new File(fileName);
		if(!file.exists()) { // 저장된 파일이 없으면
			return null;
		}
		
		ObjectInputStream ois = null;
		try {
//			//입력용 스트림 객체 생성
			ois = new ObjectInputStream(
					new BufferedInputStream(
						new FileInputStream(fileName)
						)
					);
			
		// 파일 내용을 읽어서 변수에 저장한다.
			//String타입으로 저장해둔 json을 읽어온다.
			String jsonStr = ois.readUTF();
			//읽어온 String형식을 Map으로 형변환하고 pMap으로 넣어준다.
			Type listType = new TypeToken<HashMap<String, Phone>>(){}.getType();
			pMap =  gson.fromJson(jsonStr, listType);

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("t");
			return null;
		} 
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		} 
		finally {
//			사용했던 스트림 닫기
			if(ois != null) try {
				ois.close();
			} catch (IOException e2) {
				// TODO: handle exception
			}
		}
		return pMap;
	}

	// 정보를 파일에 저장하는 메서드
//	 * 	1) '6. 전화번호 저장' 메뉴를 추가하고 구현한다.
//	 * 		(저장파일명은 'phoneData.dat'로 한다.)
	private void save() {
		ObjectOutputStream oos = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		

		try {
			// JsonObject를 파일에 쓰기
			// 쌤 방법
			// 객체 출력용 스트림 객체 생성
			oos = new ObjectOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(fileName)
							)
					);
			// 1) 맵을 json형식의 string으로 변환
			String jsonStr = gson.toJson(phoneBookMap);
			// 2) jsonStr을 파일에 저장한다.
			oos.writeUTF(jsonStr);
			
			// Map 객체를 파일로 저장한다.
//			oos.writeObject(phoneBookMap);
			
			System.out.println("저장이 완료되었습니다");
			
			dataChange = false;
			
			} catch (IOException e) {
				System.out.println("저장 실패");
				e.printStackTrace();
			} finally {
				//finally에 close설정을 하고싶다면 oos를 전역변수로 설정하기
				//사용했던 스트림 객체 닫기
				if(oos != null) try {
					oos.close();
				} catch (IOException e2) {
					// TODO: handle exception
				}
			}
	}


	//전화번호 정보를 입력하는 메서드
	private void search() {
		System.out.println();
		System.out.println("검색할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >>");
		String name = ScanUtil.nextLine();

		if(!phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 등록되지 않은 사람입니다.");
			System.out.println("검색 작업을 마칩니다.");
			return;
		}
		Phone p = phoneBookMap.get(name);
		System.out.println(name + "씨의 전화번호 정보");
		System.out.println("============================");
		System.out.println("이름=> " + p.getName());
		System.out.println("전화번호=> " + p.getTel());
		System.out.println("주소=> " + p.getAddr());
		System.out.println("============================");
	}


	//전화번호 정보를 수정하는 메서드
	private void delete() {
		System.out.println();
		System.out.println("삭제할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = ScanUtil.nextLine();
		
		// 입력한 사람의 이름이 전화번호 정보에 없으면..
		if(!phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 등록되지 않은 사람입니다.");
			System.out.println("삭제 작업을 마칩니다.");
			return;
		}
		phoneBookMap.remove(name);
		System.out.println(name + "씨 전화번호 정보가 삭제되었습니다.");
		dataChange = true;
	}


	//전화번호 정보를 수정하는 메서드
	private void update() {
		System.out.println();
		System.out.println("수정할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = ScanUtil.nextLine();
		
		// 입력한 사람의 이름이 전화번호 정보에 없으면..
		if(!phoneBookMap.containsKey(name)) {
			System.out.println(name + "씨는 등록되지 않은 사람입니다.");
			System.out.println("수정 작업을 마칩니다.");
			return;
		}
		System.out.print("새로운 전화번호 >> ");
		String newTel = ScanUtil.nextLine();
		System.out.print("새로운 주소 >> ");
		String newAddr = ScanUtil.nextLine();
		
		phoneBookMap.put(name, new Phone(name, newAddr, newTel));
		System.out.println("정보가 수정되었습니다.");
		
		dataChange = true;
	}


	//전화 정보 전체자료를 출력하는 메서드
	private void displayAll() {
		System.out.println();
		
		//모든 키값을 가져온다
		Set<String> keySet = phoneBookMap.keySet();
		
		System.out.println("-------------------------------");
		System.out.println("번호\t이름\t전화번호\t\t주소");
		System.out.println("-------------------------------");
		
		if(keySet.size()==0) {
			System.out.println("등록된 전화번호 정보가 하나도 없습니다.");
		}else {
			int cnt = 0; //번호 출력용 변수
			
			for(String name : keySet) {
				cnt++;
				Phone p = phoneBookMap.get(name);
				System.out.println(cnt + "\t" + p.getName() 
			 	 + "\t" + p.getTel() 
				 + "\t" + p.getAddr());
			}
			System.out.println("-------------------------------");

		}
	}


	private void insert() {
		System.out.println();
		System.out.println("새롭게 등록할 전화번호 정보를 입력하세요.");
		System.out.print("이름 >> ");
		String name = ScanUtil.nextLine();
		
		//이미 등록된 사람인지 여부를 검사한다.
		if(phoneBookMap.containsKey(name)) {
			System.out.println(name + "님은 이미 등록된 사람입니다.");
			return;
		}
		System.out.print("전화번호 >> ");
		String tel = ScanUtil.nextLine();
		System.out.print("주소 >> ");
		String addr = ScanUtil.nextLine();
		
//		phoneBookMap.put(name, new Phone(name, addr, tel));
		
		Phone p = new Phone(name, addr, tel);
		phoneBookMap.put(name, p);
		
		System.out.println(name + "님 전화번호 정보 등록 완료!!");
		
		dataChange = true;
	}
	
	private int displayMenu() {
		System.out.println();
		System.out.println("-------------------------");
		System.out.println("1. 전화번호 등록");
		System.out.println("2. 전화번호 수정");
		System.out.println("3. 전화번호 삭제");
		System.out.println("4. 전화번호 검색");
		System.out.println("5. 전화번호 전체출력");
		System.out.println("6. 전화번호 저장");
		System.out.println("0. 프로그램종료");
		System.out.println("-------------------------");
		System.out.print("번호 입력 >> ");
		int input = ScanUtil.nextInt();
		
		return input;
	}
	

}

//하나의 전화번호 정보가 저장될 class 작성
class Phone implements Serializable{//직렬화 설정해놓음
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String addr;
	private String tel;
	
	//생성자
	public Phone(String name, String addr, String tel) {
		super();
		this.name = name;
		this.addr = addr;
		this.tel = tel;
	}
	
	@Override
	public String toString() {
		return "Phone [name=" + name + ", addr=" + addr + ", tel=" + tel + "]";
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
}

