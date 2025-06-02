package egovframework.board.main.service;

public class UserVO {
	
	private String userId;	// 사용자 id
	private String name;	// 사용자 이름
	
	public UserVO() {}
	
	public UserVO(String userId, String name) {
		this.userId = userId;
		this.name = name;
		
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
