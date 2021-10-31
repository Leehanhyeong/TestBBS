<%@page import="mybatis.vo.BbsVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style type="text/css">
	#bbs table {
	    width:580px;
	    margin-left:10px;
	    border:1px solid black;
	    border-collapse:collapse;
	    font-size:14px;
	    
	}
	
	#bbs table caption {
	    font-size:20px;
	    font-weight:bold;
	    margin-bottom:10px;
	}
	
	#bbs table th,#bbs table td {
	    text-align:center;
	    border:1px solid black;
	    padding:4px 10px;
	}
	
	.no {width:15%}
	.subject {width:30%}
	.writer {width:20%}
	.reg {width:20%}
	.hit {width:15%}
	.title{background:lightsteelblue}
	
	.odd {background:silver}
	
	/* paging */
	
	table tfoot ol.paging {
	    list-style:none;
	}
	
	table tfoot ol.paging li {
	    float:left;
	    margin-right:8px;
	}
	
	table tfoot ol.paging li a {
	    display:block;
	    padding:3px 7px;
	    border:1px solid #00B3DC;
	    color:#2f313e;
	    font-weight:bold;
	    text-decoration: none;
	}
	
	table tfoot ol.paging li a:hover {
	    background:#00B3DC;
	    color:white;
	    font-weight:bold;
	}
	
	.disable {
	    padding:3px 7px;
	    border:1px solid silver;
	    color:silver;
	}
	
	.now {
	   padding:3px 7px;
	    border:1px solid #ff4aa5;
	    background:#ff4aa5;
	    color:white;
	    font-weight:bold;
	}
		
</style>
</head>
<body>
	<div id="bbs">
		<table summary="게시판 목록">
			<caption>게시판 목록</caption>
			<thead>
				<tr class="title">
					<th class="no">번호</th>
					<th class="subject">제목</th>
					<th class="writer">글쓴이</th>
					<th class="reg">날짜</th>
					<th class="hit">조회수</th>
				</tr>
			</thead>
			
			<tfoot>
                      <tr>
                          <td colspan="4">
                              ${pageCode }
                          </td>
						  <td>
							<input type="button" value="글쓰기"
			onclick="javascript:location.href='write.inc'"/>
						  </td>
                      </tr>
                  </tfoot>
			<tbody>
			<c:forEach var="vo" items="${ar }" varStatus="st">
				<tr>
					<td> <%--순차적인 번호를 만들어서 표현하자! --%>
						${rowTotal-((nowPage-1)*blockList+st.index) }
					</td>
					<td style="text-align: left">
					
					
						<a href="view.inc?b_idx=${vo.b_idx}&cPage=${nowPage}">
							${vo.subject }
							<!-- 길이를 구하는함수인데 구할걸 안에다 넣어주면 된다. -->
							<c:if test="${fn:length(vo.c_list)>0}">
							(${fn:length(vo.c_list)})
							</c:if>
						</a>
					</td>
					<td>${vo.writer }</td> <!-- 글쓴이 -->
					<td>
					<c:set var="write_date" value="${vo.write_date }"></c:set>
						${fn:substring(write_date,0,10)} <!-- 올린날짜 -->
						
					</td>
					<td>${vo.hit }</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
	</div>
	
</body>
</html>






