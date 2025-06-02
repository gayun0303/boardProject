package egovframework.board.cmmn.web;

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.web.context.ServletContextAware;

public class CustomPaginationRenderer extends AbstractPaginationRenderer implements ServletContextAware {

	private ServletContext servletContext;

	protected String disabledFirstPageLabel;
	protected String disabledPreviousPageLabel;
	protected String disabledNextPageLabel;
	protected String disabledLastPageLabel;

	public CustomPaginationRenderer() {
	}

	@Override
	public String renderPagination(PaginationInfo paginationInfo, String jsFunction) {

		StringBuffer strBuff = new StringBuffer();

		int firstPageNo = paginationInfo.getFirstPageNo();
		int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
		int totalPageCount = paginationInfo.getTotalPageCount();
		int pageSize = paginationInfo.getPageSize();
		int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
		int currentPageNo = paginationInfo.getCurrentPageNo();
		int lastPageNo = paginationInfo.getLastPageNo();

		if (totalPageCount > pageSize) {
			if (firstPageNoOnPageList > pageSize) { // 현재 페이지네이션 블럭에서 첫 페이지가 페이지 사이즈보다 크면 = 앞 페이지 블럭이 있다면
				strBuff.append(MessageFormat.format(firstPageLabel,
						new Object[] { jsFunction, Integer.toString(firstPageNo) }));
				strBuff.append(MessageFormat.format(previousPageLabel,
						new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList - 1) }));
			} else {	// 페이지 첫 블럭이라면
				strBuff.append(MessageFormat.format(disabledFirstPageLabel, new Object[] {}));
				strBuff.append(MessageFormat.format(disabledPreviousPageLabel, new Object[] {}));
			}
		} else { // 이전 페이지 블럭이 없음 10페이지까지만 있음
			strBuff.append(MessageFormat.format(disabledFirstPageLabel, new Object[] {}));
			strBuff.append(MessageFormat.format(disabledPreviousPageLabel, new Object[] {}));
		}

		for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
			if (i == currentPageNo) {
				strBuff.append(MessageFormat.format(currentPageLabel, new Object[] { Integer.toString(i) }));
			} else {
				strBuff.append(MessageFormat.format(otherPageLabel,
						new Object[] { jsFunction, Integer.toString(i), Integer.toString(i) }));
			}
		}

		if (totalPageCount > pageSize) {
			if (lastPageNoOnPageList < totalPageCount) {
				strBuff.append(MessageFormat.format(nextPageLabel,
						new Object[] { jsFunction, Integer.toString(firstPageNoOnPageList + pageSize) }));
				strBuff.append(
						MessageFormat.format(lastPageLabel, new Object[] { jsFunction, Integer.toString(lastPageNo) }));
			} else {
				strBuff.append(MessageFormat.format(disabledNextPageLabel, new Object[] {}));
				strBuff.append(MessageFormat.format(disabledLastPageLabel, new Object[] {}));
			}
		} else { // 다음 페이지 블럭이 없음
			strBuff.append(MessageFormat.format(disabledNextPageLabel, new Object[] {}));
			strBuff.append(MessageFormat.format(disabledLastPageLabel, new Object[] {}));
		}

		return strBuff.toString();
	}

	public void initVariables() {
		firstPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='"
				+ servletContext.getContextPath()
				+ "/images/egovframework/cmmn/btn_page_pre10.gif' border=0/></a>&#160;";
		previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='"
				+ servletContext.getContextPath()
				+ "/images/egovframework/cmmn/btn_page_pre1.gif' border=0/></a>&#160;";
		currentPageLabel = "<strong>{0}</strong>&#160;";
		otherPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\"><span class=\"other-page-label\">{2}</span></a></span>&#160;";
		nextPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='"
				+ servletContext.getContextPath()
				+ "/images/egovframework/cmmn/btn_page_next1.gif' border=0/></a>&#160;";
		lastPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\">" + "<image src='"
				+ servletContext.getContextPath()
				+ "/images/egovframework/cmmn/btn_page_next10.gif' border=0/></a>&#160;";

		// 작동 안하는 이미지
		disabledFirstPageLabel = "<image src='" + servletContext.getContextPath()
				+ "/images/egovframework/cmmn/pre2.png' width=\"13\" height=\"13\" border=0/>&#160;";
		disabledPreviousPageLabel = "<image src='" + servletContext.getContextPath()
				+ "/images/egovframework/cmmn/pre1.png' width=\"13\" height=\"13\" border=0/>&#160;";
		disabledNextPageLabel = "<image src='" + servletContext.getContextPath()
				+ "/images/egovframework/cmmn/next1.png' width=\"13\" height=\"13\" border=0/>&#160;";
		disabledLastPageLabel = "<image src='" + servletContext.getContextPath()
				+ "/images/egovframework/cmmn/next2.png' width=\"13\" height=\"13\" border=0/>&#160;";
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
		initVariables();
	}

}
