<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- /WEB-INF/view/board/list.jsp --%>
<%@ include file="/WEB-INF/view/jspHeader.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 목록</title>
<script type="text/javascript">
	function listdo(page) {
		f = document.searchform;
		f.pageNum.value=page;
		f.submit();
	}
</script>
</head>
<body>
	<table>
		<tr>
			<td colspan="5">
				<div style="display:inline;">
					<form action="list.shop" method="post" name="searchform">
						<input type="hidden" name="pageNum" value="1">
						<select name="searchtype" style="width:100px;">
							<option value="">선택하세요</option>
							<option value="subject">제목</option>
							<option value="name">작성자</option>
							<option value="content">내용</option>
						</select>
						<script>
							searchform.searchtype.value="${param.searchtype}";
						</script>
						<input type="text" name="searchcontent" value="${param.searchcontent }" style="width:250px;">
						<input type="submit" value="검색">
						<input type="button" value="전체게시물보기" onclick="location.href='list.shop'">
					</form>
				</div>
			</td>
		</tr>
		<c:if test="${listcount > 0 }">	<%--등록된 게시물이 있음 --%>
			<tr>
				<td colspan="4">Spring 게시판</td>
				<td>글개수:${listcount }</td>
			</tr>
			<tr>
				<th>번호</th>
				<th>제목</th>
				<th>글쓴이</th>
				<th>날짜</th>
				<th>조회수</th>
			</tr>
			<c:forEach var="board" items="${boardlist }">
				<tr>
					<td>${boardno }</td>
					<c:set var="boardno" value="${boardno-1 }" />
					<td style="text-align:left;">
						<c:if test="${! empty board.fileurl }">	<%--첨부파일이 존재하는 경우 --%>
							<a href="file/${board.fileurl }">@</a>
						</c:if>
						<c:if test="${empty board.fileurl }">
							&nbsp;&nbsp;&nbsp;
						</c:if>
						<c:forEach begin="1" end="${board.grplevel }">	<%-- 답변글인 경우 들여쓰기 --%>
							&nbsp;&nbsp;
						</c:forEach>
						<c:if test="${board.grplevel > 0 }">
							└
						</c:if>
						<a href="detail.shop?num=${board.num }">${board.subject }</a>
					</td>
					<td>${board.name }</td>
					<fmt:formatDate value="${board.regdate }" pattern="yyyy-MM-dd" var="day"/>
					<c:set var="today" value="${today }" />
					<c:choose>
						<c:when test="${day==today }">
							<td><fmt:formatDate value="${board.regdate }" pattern="HH:mm:ss" /></td>
						</c:when>
						<c:otherwise>
							<td><fmt:formatDate value="${board.regdate }" pattern="yyyy-MM-dd HH:mm" /></td>
						</c:otherwise>
					</c:choose>
					<td>${board.readcnt }</td>
				</tr>
			</c:forEach>
			<%--페이지 부분 출력 --%>
			<tr>
				<td colspan="5">
					<c:if test="${pageNum > 1}">
						<a href="javascript:listdo('${pageNum-1 }')">[이전]</a>
					</c:if>
					<c:if test="${pageNum <= 1}">
						[이전]
					</c:if>
					<c:forEach var="a" begin="${startpage }" end="${endpage }">
						<c:if test="${a == pageNum }">[${a }]</c:if>
						<c:if test="${a != pageNum }">
							<a href="javascript:listdo('${a }')">[${a }]</a>
						</c:if>
					</c:forEach>
					<c:if test="${pageNum < maxpage }">
						<a href="javascript:listdo('${pageNum+1 }')">[다음]</a>
					</c:if>
					<c:if test="${pageNum >= maxpage }">[다음]</c:if>
				</td>
			</tr>
		</c:if>
		<c:if test="${listcount == 0}">
			<tr>
				<td colspan="5">등록된 게시물이 없습니다.</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="5" align="right">
				<a href="write.shop">[글쓰기]</a>
			</td>
		</tr>
	</table>
</body>
</html>