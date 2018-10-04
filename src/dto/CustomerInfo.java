package dto;

public class CustomerInfo {
	String id;
	String email;
	int gender;
	int age;
	public CustomerInfo(String id, String email, String gender, String age){
		this.id = id;
		this.email = email;
		if(gender.equals("M"))
			this.gender = 1; 
		else
			this.gender = 0; 
		String[] tmp = age.split("-");
		this.age = Integer.parseInt(tmp[0]);
	}
	
	public CustomerInfo(String id, String email, int gender, int age){
		this.id = id;
		this.email = email;
		this.gender=gender;
		this.age=age;
	}
	public String getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}
	public int getGender() {
		return gender;
	}
	public int getAge() {
		return age;
	}

	@Override
	public String toString() {
		return "CustomerInfo [id=" + id + ", email=" + email + ", gender=" + gender + ", age=" + age + "]";
	}
	
	
	
}
