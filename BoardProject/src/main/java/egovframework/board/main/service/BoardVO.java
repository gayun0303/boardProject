package egovframework.board.main.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BoardVO {
	
	private String boardId;	// 게시글 id
	private String title;	// 게시글 제목
	private String name;		// 작성자 이름
	private String userId;		// 작성자 id
	
	private Date createDate;	// 게시글 작성일시
	private int clickCount;		// 조회수
	private String content;		// 게시글 내용
	private String password;	// 비밀번호
	private boolean isAnswer;	// 답글인지 여부
	private String rootId;			// 최상위 게시글 id
	private String parentId;		// 상위 게시글 id
	
	private Integer answerLevel;		// 게시글 레벨 0: 그냥 게시글 1: 답글 2: 답글의 답글
	private boolean existFile;		// 파일이 있는지 여부
	private List<FileVO>	fileVOList;
	private Integer groupId;

	public BoardVO() {
	}

	public String getBoardId() {
		return boardId;
	}

	public void setBoardId(String boardId) {
		this.boardId = boardId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(createDate);
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getClickCount() {
		return clickCount;
	}

	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPassword() {
		// null 체크 (수정 시 빈 값이 들어오는 경우 방지)
        if (password != null && !password.isEmpty()) {
            this.password = DigestUtils.sha256Hex(password); // SHA-256 해시 처리
        }
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAnswer() {
		return isAnswer;
	}

	public void setAnswer(boolean isAnswer) {
		this.isAnswer = isAnswer;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

	public Integer getAnswerLevel() {
		return answerLevel;
	}

	public void setAnswerLevel(Integer answerLevel) {
		this.answerLevel = answerLevel;
	}

	public boolean isExistFile() {
		return existFile;
	}

	public void setExistFile(boolean existFile) {
		this.existFile = existFile;
	}
	
	
	public List<FileVO> getFileVOList() {
		return fileVOList;
	}

	public void setFileVOList(List<FileVO> fileVOList) {
		this.fileVOList = fileVOList;
	}
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
