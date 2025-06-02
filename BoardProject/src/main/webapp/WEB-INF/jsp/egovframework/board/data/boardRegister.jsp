<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"         uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"      uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring"    uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<c:set var="registerFlag" value="${empty boardVO.boardId ? 'create' : 'modify'}"/>
<c:if test="${registerFlag != 'modify' }">
<title>글 작성</title>
</c:if>
<c:if test="${registerFlag == 'modify' }">
<title>글 수정</title>
</c:if>
	<style>
    body {
        font-family: '맑은 고딕', sans-serif;
        margin: 20px;
        background-color: #f8f9fa;
    }
    .form-container {
        background: white;
        max-width: 700px;
        margin: auto;
        padding: 30px;
        border-radius: 12px;
        box-shadow: 0 0 8px rgba(0,0,0,0.1);
    }
    .form-container table {
        width: 100%;
        border-collapse: collapse;
    }
    .form-container td {
        padding: 10px;
        vertical-align: top;
    }
    .form-container label {
        font-weight: bold;
        display: inline-block;
        width: 80px;
    }
    .form-container input[type="text"],
    .form-container input[type="password"],
    .form-container textarea {
        width: 100%;
        padding: 6px;
        border: 1px solid #ced4da;
        border-radius: 6px;
        font-size: 14px;
    }
    .form-container textarea {
        height: 150px;
        resize: vertical;
    }
    .form-container input[type="file"] {
        margin-top: 5px;
    }
    .form-container .submit-btn {
        display: inline-block;
        margin-top: 20px;
        padding: 10px 20px;
        background-color: #007bff;
        color: white;
        border-radius: 6px;
        text-decoration: none;
        text-align: center;
    }
    
    .form-container .cancel-btn {
        display: inline-block;
        margin-top: 20px;
        padding: 10px 20px;
        background-color: #d3d3d3;
        color: black;
        border-radius: 6px;
        text-decoration: none;
        text-align: center;
    }
    .form-container .message {
        color: green;
        margin-top: 5px;
    }
</style>
	<script src="/lib/jquery-3.7.1.min.js"></script>
	<script type="text/javaScript" language="javascript" defer="defer">
	
	/* 게시글 등록, 수정 */	
	$(document).on("click", "#submitBtn", function() {
		var name = $('#name').val().trim();
		var password = $('#password').val().trim();
		var title = $('#title').val().trim();
		var content = $('#content').val().trim();
		
		if(name == "") {
			alert("작성자는 필수 입력 항목입니다!");
			event.preventDefault();
			return;
		} else if(password == "") {
			alert("비밀번호는 필수 입력 항목입니다!");
			event.preventDefault();
			return;
		} else if(title == "") {
			alert("제목은 필수 입력 항목입니다!");
			event.preventDefault();
			return;
		} else if(content == "") {
			alert("글 내용은 필수 입력 항목입니다!");
			event.preventDefault();
			return;
		}
		
		// 저장할 파일 하나씩 DataTransfer에 추가
		var dt = new DataTransfer();
		for(var i = 0; i < fileArray.length; i++) {
			dt.items.add(fileArray[i]);
		}
		
		$("#file")[0].files = dt.files;
		
		console.log("fileArray", fileArray);
		console.log("file", $("#file")[0].files);
		console.log("form", $("#detailForm"));
		event.preventDefault();
		
		var action = "<c:url value="${registerFlag == 'create' ? '/addBoard.do' : '/updateBoard.do'}"/>";
		$("#detailForm").attr("action", action).submit();
	});

	$(document).on("click", "#cancelBtn", function() {
		$("#detailForm").attr("action", "/boardList.do").submit();
	});
	
	/* 첨부 파일 */
	var fileArray = [];
	
	$(document).on("change", "#file", function(e){
		
		console.log("file change event", e);
		/* 선택한 파일들 배열에 추가 */
		var files = Array.from(e.target.files);
		fileArray = fileArray.concat(files);
		
		renderFileList();
		resetInput();
	});
		
	/* 파일 리스트 화면 다시 띄우기 */
	function renderFileList() {
		var list = $("#newFileList").empty();
		
		list.innerHTML = '';
		
		for(var index=0; index < fileArray.length; index++) {

			var li = $("<li>").text(fileArray[index].name + " ");
			var btn = $("<button>").text("X").attr('data-index', index).on("click", function() {
				if(!confirm("삭제 하시겠습니까?")) return;
				var i = $(this).data('index');
				fileArray.splice(i, 1);
				renderFileList();
			});
			
			li.append(btn);
			list.append(li);
		};
        console.log("fileArray", fileArray);	
	}
	
/* 	$(document).on("click", "deleteNewFile_", function(e) {
		if(!confirm("삭제 하시겠습니까?")) return;
		
		console.log("e", e);
	}); */
	
	function resetInput() {
	    // 중복 파일 가능하게 초기화
	    var oldInput = $("#file");
	    var newInput = oldInput.clone();
	    oldInput.replaceWith(newInput);
	}

	/* 기존 파일에서 삭제한 파일 id 리스트 */
	var deletedFileIds = [];

	$(document).on('click', '.deleteExistingFile', function () {
		if(!confirm("삭제 하시겠습니까?")) return;

	    var fileId = $(this).data('file-id');
	    if (fileId) {
	    	deletedFileIds.push(fileId);
	    }
	    $(this).parent().remove();
	    
	    $('#deleteFileIds').val(deletedFileIds.join(','));
	});
	
	</script>
</head>
<body>
	<div class="form-container">
    <form:form modelAttribute="boardVO" id="detailForm" name="detailForm" enctype="multipart/form-data">
        <form:hidden path="answer" />
        <form:hidden path="boardId" />
        <form:hidden path="answerLevel" id="answerLevel"/>
        <form:hidden path="rootId" id="rootId"/>
        <c:if test="${not empty boardVO.parentId}">
            <form:hidden path="parentId" />
        </c:if>

        <table>
            <tr>
                <td><label for="name">작성자</label></td>
                <td><c:if test="${registerFlag != 'modify' }">
                	<form:input path="name" id="name" maxlength="20" />
                </c:if>
                <c:if test="${registerFlag == 'modify' }">
                    <div style="padding: 10px; background-color: #f1f3f5; border-radius: 8px;">
                        <c:out value="${boardVO.name}" escapeXml="true" />
                        <form:hidden path="name" id="name"/>
                    </div>
                </c:if>
                </td>
            </tr>
            <tr>
                <td><label for="password">비밀번호</label></td>
                <td><c:if test="${registerFlag != 'modify' }">
                	<form:input type="password" path="password" maxlength="20" />
                </c:if>
                <c:if test="${registerFlag == 'modify' }">
                    <div style="padding: 10px; background-color: #f1f3f5; border-radius: 8px;">
                        <c:out value="XXXX" escapeXml="true" />
                        <form:hidden path="password" id="password"/>
                    </div>
                </c:if>
                </td>
            </tr>
            <tr>
                <td><label for="title">제목</label></td>
                <td><form:input path="title" maxlength="100" /></td>
            </tr>
            <tr>
                <td><label for="content">내용</label></td>
                <td>
                    <c:if test="${registerFlag == 'modify' }">
                        <form:textarea path="content" />
                    </c:if>
                    <c:if test="${registerFlag != 'modify' }">
                        <div style="padding: 10px; background-color: #f1f3f5; border-radius: 8px;">
                            <form:textarea value="${boardVO.content}" escapeXml="true" path="content"/>
                        </div>
                    </c:if>
                </td>
            </tr>
            <tr>
		    	<c:if test="${registerFlag == 'modify' }">
		    		<td><label for="file">기존 첨부파일</label></td>
		    		<td>
			            <input type="hidden" id="deleteFileIds" name="deleteFileIds" value=''/>
			    		<c:forEach var="file" items="${boardVO.fileVOList}">
			    			<li class="list-group-item">
					            ${file.originalName} (${file.fileSize} byte)
					            <button type="button" class="deleteExistingFile" data-file-id="${file.fileId}">X</button>
					        </li>
			    		</c:forEach>
		    		</td>
				</c:if>
            </tr>
            <tr>
                <td><label for="file">첨부파일</label></td>
				<td>
					<label for="file" >파일선택</label>
					<input type="file" id="file" class="upload-images_" multiple="multiple" name="file" style="display:none;">
					<div class="input-group w-100 p-2">
					  <label class="mx-2 m-auto input-group-text"></label>
					  <div class="form-control m-2 p-0 " style="height: auto">
					    <ul id="fileList" class=" w-100 list-group">
					    </ul>
					    <ul id="newFileList" class=" w-100 list-group">
					    </ul>
					  </div>
					</div>
				</td>
				<td>
                    <c:if test="${not empty message}">
                        <div class="message">${message}</div>
                    </c:if>
                </td>
            </tr>
        </table>

        <div>
            <!-- <a href="javascript:fn_board_save();" class="submit-btn"> -->
            <button class="submit-btn" id="submitBtn">
                <c:choose>
                    <c:when test="${registerFlag == 'create'}">등록</c:when>
                    <c:otherwise>수정</c:otherwise>
                </c:choose>
            </button>
            <!-- <a href="javascript:fn_board_list();" class="cancel-btn">
                취소
            </a> -->
            <button class="cancel-btn" id="cancelBtn">
            	취소
            </button>
        </div>
        	    <!-- 검색조건 유지 -->
	    <input type="hidden" name="searchType" value="<c:out value='${searchVO.searchType}'/>"/>
	    <input type="hidden" name="searchKeyword" value="<c:out value='${searchVO.searchKeyword}'/>"/>
	    <input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>"/>
        
    </form:form>


</div>
</body>
</html>