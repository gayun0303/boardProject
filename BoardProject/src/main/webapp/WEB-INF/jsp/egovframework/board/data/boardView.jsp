<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글</title>
	<style>
    .board-container {
        width: 100%;
        max-width: 800px;
        font-family: '맑은 고딕', sans-serif;
        margin-bottom: 40px;
        border: 1px solid #ddd;
        padding: 20px;
        border-radius: 6px;
    }

    .board-title {
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 10px;
        padding-left: 10px;
    }

    .board-meta {
        font-size: 14px;
        color: #555;
        margin-bottom: 15px;
        padding-left: 10px;
    }

    .board-content {
        min-height: 100px;
        margin-bottom: 15px;
        padding: 10px;
        border: 1px solid #eee;
        background: #fafafa;
    }

    .file-list {
        margin-top: 10px;
        padding-left: 10px;
    }

    .file-list a {
        /* display: block; */
        color: #0056b3;
        text-decoration: none;
    }
    
    .file-list div {
    	display: block;
    }

    .btn-area {
        margin-top: 20px;
    }

    .btn-area button {
        margin-right: 10px;
        padding: 6px 12px;
    }

    .indent {
        padding-left: 20px;
    }
	
    .password-box {
        margin-top: 10px;
        padding-left: 10px;
    }
</style>
	<script type="text/javaScript" language="javascript" defer="defer">
	/* 글 수정 */
	function fn_board_modify(boardId, answer, existFile, rootId) {
		
	    var form = document.getElementById("actionForm");
	    var password = document.getElementById("password_" + boardId).value;
	
	    form.boardId.value = boardId;
	    form.password.value = password;
	    form.existFile.value = existFile;
	    form.answer.value = answer;
	    form.rootId.value = rootId;
	
	    form.action = "<c:url value='/updateBoardView.do'/>";
	    
	    form.submit();
	}
	
	/* 글 삭제 */
	function fn_board_delete(boardId, answer, existFile, rootId) {
	    var form = document.getElementById("actionForm");
	    var password = document.getElementById("password_" + boardId).value;
	
	    form.boardId.value = boardId;
	    form.password.value = password;
	    form.existFile.value = existFile;
	    form.answer.value = answer;
	    form.rootId.value = rootId;
	
	    form.action = "<c:url value='/deleteBoard.do'/>";
	    form.submit();
	}
	
	
	/* 답글 등록 */
	function fn_board_answer(rootId, boardId) {
	    
	    var form = document.getElementById("actionForm");
	    form.rootId.value = rootId;
	    form.parentId.value = boardId;
	    form.answer.value = true;
	    form.existFile.value = false;
	    
	    form.action = "<c:url value='/addBoardView.do'/>";

	    form.submit();
	}
	
	/* 목록으로 이동 */
	function fn_board_list() {
		document.detailViewForm.action = "<c:url value='/boardList.do'/>";
		document.detailViewForm.submit();
	}
	
	</script>
</head>
<body>
    
	<c:if test="${not empty msg}">
    <script>
        alert("${msg}");
    </script>
    </c:if>
	<form:form modelAttribute="boardVOList" id="detailViewForm" name="detailViewForm" method="post">
    <input type="hidden" name="boardId" />
    <input type="hidden" name="answerLevel" />

    <c:forEach var="board" items="${boardVOList}">
    	<div style="display: flex;">
		    <c:forEach var="i" begin="1" end="${board.answerLevel}">
		    ㄴ
		    </c:forEach>
	        <div class="board-container" style="margin-left: ${board.answerLevel * 20}px;">
	            <!-- 제목 -->
	            <div class="board-title">
	                <c:out value="${board.title}" />
	            </div>
	
	            <!-- 작성자, 등록일, 조회수 -->
	            <div class="board-meta">
	                작성자: <c:out value="${board.name}" /> |
	                등록일: <c:out value="${board.createDate}" /> |
	                조회수: <c:out value="${board.clickCount}" />
	            </div>
	
	            <!-- 내용 -->
	            <div class="board-content">
	                <pre><c:out value="${board.content}"/></pre>
	            </div>
	
	            <!-- 첨부파일 -->
	            <div class="file-list">
	                <b>첨부파일:</b>
	                <c:if test="${board.existFile}">
	                    <c:forEach var="oneFile" items="${board.fileVOList}">
	                    	<div>
		                        <a href="<c:url value='/file/download.do?filePath=${oneFile.filePath}&saveName=${oneFile.saveName}&originalName=${oneFile.originalName}'/>">
		                            <c:out value="${oneFile.originalName}" />
		                        </a>
	                        </div>
	                    </c:forEach>
	                </c:if>
	                <c:if test="${not board.existFile}">
	                    없음
	                </c:if>
	            </div>
		        <c:if test="${board.answerLevel < 2}">
		            <button type="button" onclick="fn_board_answer('${boardVOList[0].boardId}', '${board.boardId}')">답글 등록</button>
		        </c:if>
	
	            <!-- 비밀번호 입력 및 수정/삭제 버튼 -->
	            <div class="password-box">
	                <input type="password" name="password" id="password_${board.boardId}" placeholder="비밀번호 입력" />
	                <button type="button" onclick="fn_board_modify('${board.boardId}', '${board.answer}', '${board.existFile}', '${board.rootId}')">수정</button>
	                <button type="button" onclick="fn_board_delete('${board.boardId}', '${board.answer}', '${board.existFile}', '${board.rootId}')">삭제</button>
	            </div>
	        </div>
        </div>
    </c:forEach>

    <div class="btn-area">
        <button type="button" onclick="fn_board_list()">목록</button>
    </div>
		<!-- 검색조건 유지 -->
	    <input type="hidden" name="searchType" value="<c:out value='${searchVO.searchType}'/>"/>
	    <input type="hidden" name="searchKeyword" value="<c:out value='${searchVO.searchKeyword}'/>"/>
	    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
	    
    </form:form>
    
    <!-- 글 하나의 요청을 비밀번호와 함께 보내기 위한 form -->
    <form:form modelAttribute="boardVO" id="actionForm" method="post">
	    <input type="hidden" name="rootId" />
	    <input type="hidden" name="boardId" />
	    <input type="hidden" name="password" />
	    <input type="hidden" name="existFile"/>
	    <input type="hidden" name="answer"/>
	    <input type="hidden" name="parentId"/>
	    <!-- 검색조건 유지 -->
	    <input type="hidden" name="searchType" value="<c:out value='${searchVO.searchType}'/>"/>
	    <input type="hidden" name="searchKeyword" value="<c:out value='${searchVO.searchKeyword}'/>"/>
	    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
	</form:form>
</body>
</html>