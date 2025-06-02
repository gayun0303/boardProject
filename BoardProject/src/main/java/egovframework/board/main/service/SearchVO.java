package egovframework.board.main.service;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchVO {
	
	private String searchKeyword;		// 검색어
	private boolean searchType;	// false : 제목		true : 작성자
	private boolean searchUse;	// 검색 사용 여부
	private int pageIndex = 1;	// 현재 페이지 인덱스
	private int pageUnit;		// 페이지 개수
	private int pageSize;		// 한 페이지 사이즈
	private int firstIndex = 1;
	private int lastIndex = 1;
	private int recordCountPerPage = 10;

	public SearchVO() {
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public boolean isSearchType() {
		return searchType;
	}

	public void setSearchType(boolean searchType) {
		this.searchType = searchType;
	}

	public boolean isSearchUse() {
		return searchUse;
	}

	public void setSearchUse(boolean searchUse) {
		this.searchUse = searchUse;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
