<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
	<style>
        body {
            font-family: '맑은 고딕', sans-serif;
            margin: 20px;
            background-color: #f8f9fa;
        }
        #search ul {
            list-style: none;
            padding: 0;
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        #search select, #search input {
            padding: 5px;
        }
        #search a {
            display: inline-block;
            padding: 6px 12px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        #content_box {
            background: white;
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 0 8px rgba(0,0,0,0.1);
            max-width: 1000px;
            margin: 0 auto;
        }
        .custom-table {
            width: 100%;
            border-collapse: collapse;
        }
        .custom-table th, .custom-table td {
            border: 1px solid #dee2e6;
            padding: 10px;
            /* text-align: center; */
        }
        .custom-table th {
            background-color: #e9ecef;
        }
        .custom-table tr:hover {
            background-color: #f1f3f5;
        }
        .title {
        	display: inline-block;
        	color: black;
        }
        .reply-title {
            padding-left: 20px;
            display: inline-block;
            position: relative;
            color: black;
        }
        .reply-title::before {
            content: "ㄴ";
            position: absolute;
            left: 0;
            color: #6c757d;
        }
        .reply-reply-title {
            padding-left: 40px;
            display: inline-block;
            position: relative;
            color: black;
        }
        .reply-reply-title::before {
        	padding-left: 20px;
        	
            content: "ㄴ";
            position: absolute;
            left: 0;
            color: #6c757d;
        }
        #paging {
            margin-top: 20px;
            text-align: center;
        }
        .other-page-label {
        	display: inline-block;
        	color: black;
        }
        .write-btn {
            display: inline-block;
            margin-top: 10px;
            padding: 8px 16px;
            background-color: #28a745;
            color: white;
            text-decoration: none;
            border-radius: 6px;
        }
    </style>
	<script type="text/javaScript" language="javascript" defer="defer">
	function fn_board_addBoard() {
       	document.listForm.action = "<c:url value='/addBoardView.do'/>";
       	document.listForm.submit();
    }
	
	function fn_board_select(selectedId, rootId) {
		
		document.listForm.method = "get";
		document.listForm.selectedId.value = selectedId;
		document.listForm.rootId.value = rootId;
		document.listForm.action = "<c:url value='/boardView.do'/>";
		document.listForm.submit();
	}
	
	function fn_board_list() {
		document.listForm.action = "<c:url value='/boardList.do'/>";
		document.listForm.submit();
	}
	
	function fn_board_search() {
		document.listForm.pageIndex.value = 1;
		document.listForm.action = "<c:url value='/boardList.do'/>";
		document.listForm.submit();
	}
	
	 function fn_board_link_page(pageNo){
		 
     	document.listForm.pageIndex.value = pageNo;
     	document.listForm.action = "<c:url value='/boardList.do'/>";
        document.listForm.submit();
     }
	</script>
</head>
<body>
<form:form modelAttribute="searchVO" id="listForm" name="listForm">
        <input type="hidden" name="selectedId" />
        <input type="hidden" name="rootId"/>
        <div id="search">
            <ul>
                <li>
                    <label for="searchType" style="visibility:hidden;"><spring:message code="search.choose" /></label>
                    <form:select path="searchType" cssClass="use">
                        <form:option value="true" label="작성자" />
                        <form:option value="false" label="제목" />
                    </form:select>
                </li>
                <li>
                    <label for="searchKeyword" style="visibility:hidden;display:none;"><spring:message code="search.keyword" /></label>
                    <form:input path="searchKeyword" cssClass="txt" />
                </li>
                <li>
                    <a href="javascript:fn_board_search();">검색</a>
                </li>
            </ul>
        </div>

        <div id="content_box">
        	<div>전체 <c:out value="${paginationInfo.totalRecordCount}"/>건</div>
            <table class="custom-table">
                <thead>
                    <tr>
                        <th>순번</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>등록일</th>
                        <th>조회수</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty resultList}">
                        <c:forEach var="result" items="${resultList}" varStatus="status">
                            <tr>
                                <td><c:out value="${paginationInfo.totalRecordCount+1 - ((searchVO.pageIndex-1) * searchVO.pageSize + status.count)}"/></td>
                                <td>
                                    <a href="javascript:fn_board_select('<c:out value="${result.boardId}" />', '<c:out value="${result.rootId}" />')">
                                        
	                                    <c:if test="${result.answerLevel == 1}">
	                                        <span class="reply-title"><c:out value="${result.title}" /></span>
	                                    </c:if>
	                                    <c:if test="${result.answerLevel == 2}">
	                                        <span class="reply-reply-title"><c:out value="${result.title}" /></span>
	                                    </c:if>
	                                    <c:if test="${result.answerLevel == 0}">
	                                        <span class="title"><c:out value="${result.title}" /></span>
	                                    </c:if>
                                        
                                    </a>
                                </td>
                                <td><c:out value="${result.name}" /></td>
                                <td><fmt:formatDate value="${result.createDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                                <td><c:out value="${result.clickCount}" /></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                </tbody>
            </table>

            <!-- 페이지 -->
            <div id="paging">
                <ui:pagination paginationInfo="${paginationInfo}" type="image2" jsFunction="fn_board_link_page" />
                <form:hidden path="pageIndex" />
            </div>

            <!-- 글쓰기 버튼 -->
            <a href="javascript:fn_board_addBoard();" class="write-btn">글쓰기</a>
        </div>
    </form:form>

</body>
</html>