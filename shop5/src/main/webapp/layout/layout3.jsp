<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%--/webapp/layout/layout.jsp --%>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<title><decorator:title /></title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="${path}/css/main.css">
<style>
	body,h1,h2,h3,h4,h5,h6 {font-family: "Raleway", sans-serif}
</style>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script type="text/javascript" src="http://www.chartjs.org/dist/2.9.3/Chart.min.js"></script>
<script type="text/javascript" src="http://cdn.ckeditor.com/4.5.7/standard/ckeditor.js"></script>
<decorator:head />
<body class="w3-light-grey w3-content" style="max-width:1600px">

<!-- Sidebar/menu -->
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
  <div class="w3-container">
    <a href="#" onclick="w3_close()" class="w3-hide-large w3-right w3-jumbo w3-padding w3-hover-grey" title="close menu">
      <i class="fa fa-remove"></i>
    </a>
    <h4>
    	<b>
    		<c:if test="${empty sessionScope.loginUser}">
					<a href="${path }/user/login.shop">로그인</a>
					<a href="${path }/user/userEntry.shop">회원가입</a>
				</c:if>
				<c:if test="${!empty sessionScope.loginUser}">
					${sessionScope.loginUser.username} 님
					<a href="${path }/user/logout.shop">로그아웃</a>
				</c:if>
    	</b>
    </h4>
  </div>
  <div class="w3-bar-block">
    <a href="${path }/user/main.shop" onclick="w3_close()" class="w3-bar-item w3-button w3-padding w3-text-teal"><i class="fa fa-th-large fa-fw w3-margin-right"></i>회원관리</a> 
    <a href="${path }/item/list.shop" onclick="w3_close()" class="w3-bar-item w3-button w3-padding"><i class="fa fa-user fa-fw w3-margin-right"></i>상품관리</a> 
    <a href="${path }/board/list.shop" onclick="w3_close()" class="w3-bar-item w3-button w3-padding"><i class="fa fa-envelope fa-fw w3-margin-right"></i>게시판</a>
    <a href="${path }/chat/chat.shop" onclick="w3_close()" class="w3-bar-item w3-button w3-padding"><i class="fa fa-envelope fa-fw w3-margin-right"></i>채팅</a>
  </div>
  <br>
  <hr>
  <br>
  <div class="w3-panel w3-large">
    <!-- 환율정보 -->
      <div id="exchange"></div><br><br>
 
  </div>
</nav>

<!-- Overlay effect when opening sidebar on small screens -->
<div class="w3-overlay w3-hide-large w3-animate-opacity" onclick="w3_close()" style="cursor:pointer" title="close side menu" id="myOverlay"></div>

<!-- !PAGE CONTENT! -->
<div class="w3-main" style="margin-left:300px">

  <!-- Header -->
  <header id="portfolio">
    <a href="#"><img src="/w3images/avatar_g2.jpg" style="width:65px;" class="w3-circle w3-right w3-margin w3-hide-large w3-hover-opacity"></a>
    <span class="w3-button w3-hide-large w3-xxlarge w3-hover-text-grey" onclick="w3_open()"><i class="fa fa-bars"></i></span>
    <div class="w3-container">
    <h1><b>information</b></h1>
    
      <!-- 파이/막대 그래프 -->
      <div id="piecontainer" style="width: 40%; float:left; border : 1px solid black">
    	<canvas id="canvas1" style="width:100%;"></canvas>
      </div>
	  <div id="barcontainer" style="width: 40%; float:right; border : 1px solid black">
    	<canvas id="canvas2" style="width:100%;"></canvas>
      </div>
    </div>
  </header>
  <br>
  <br>
  <div class="w3-row-padding">
    <decorator:body />
  </div>
     <div id="exchange2"></div>
<!-- End page content -->
</div>

<script>
// Script to open and close sidebar
function w3_open() {
    document.getElementById("mySidebar").style.display = "block";
    document.getElementById("myOverlay").style.display = "block";
}
 
function w3_close() {
    document.getElementById("mySidebar").style.display = "none";
    document.getElementById("myOverlay").style.display = "none";
}

var randomColorFactor = function() {
	return Math.round(Math.random()*255);
}
var randomColor = function(opacity) {	//opacity : 투명도
	return "rgba(" + randomColorFactor() + ","
			+ randomColorFactor() + ","
			+ randomColorFactor() + ","
			+ (opacity || '.3') + ")";
};

$(function() {
	piegraph();
	bargraph();
	//exchangeRate();	//환율정보를 ajax을 통해 크롤링하기.
	exchangeRate2();
})

function exchangeRate() {
	$.ajax("${path}/ajax/exchange1.shop", {
		success : function(data) {
			$("#exchange").html(data);
		},
		error : function(e) {
			alert("환율 조회시 서버 오류 : " + e.status);
		}
	})
}

function exchangeRate2() {
	$.ajax("${path}/ajax/exchange2.shop", {
		success : function(data) {
			$("#exchange2").html(data);
		},
		error : function(e) {
			alert("환율 조회시 서버 오류 : " + e.status);
		}
	})
}

function piegraph() {
	$.ajax("${path}/ajax/graph1.shop", {
		success : function(data) {	//data : json형태의 문자열로 수신
			pieGraphPrint(data);
		},
		error : function(e) {
			alert("서버 오류 : " + e.status);
		}
	})
}
function bargraph() {
	$.ajax("${path}/ajax/graph2.shop", {	//최근 7일간 게시물 등록 건수
		success : function(data) {
			barGraphPrint(data);
		},
		error : function(e) {
			alert("서버 오류 : " + e.status);
		}
	})
}
function pieGraphPrint(data) {
	console.log(data)
	var rows = JSON.parse(data)
	var names = []
	var datas = []
	var colors = []
	$.each(rows, function(index, item) {
		/* names[index] = item.name; */
		names[index] = item.name;
		datas[index] = item.cnt;
		colors[index] = randomColor(1);
	})
	var config = {
		type : 'pie',
		data : {
			datasets : [{
				data : datas,
				backgroundColor : colors
			}],
			labels : names
		},
		options : {
			responsive : true,
			legend : {position : 'top'},
			title : {
				display : true,
				text : '글쓴이별 게시판 등록 건수',
				position : 'bottom'
			}
		}
	}
	var ctx = document.getElementById("canvas1").getContext("2d");
	new Chart(ctx, config);
}
function barGraphPrint(data) {
	console.log(data)
	var rows = JSON.parse(data)
	var dates = []
	var datas = []
	var colors = []
	$.each(rows, function(index, item) {
		/* names[index] = item.name; */
		dates[index] = item.regdate;
		datas[index] = item.cnt;
		colors[index] = randomColor(0.7);
	})

	var chartData = {
		labels : dates,
		datasets : [
			{
				type:'line',
				borderWidth : 2,
				borderColor:colors,
				label : '건수',
				fill : false,
				data : datas
			},
			{
				type:'bar',
				label : '건수',
				backgroundColor:colors,
				data : datas,
			}
		]
	};
	
	var ctx = document.getElementById("canvas2").getContext("2d");
	new Chart(ctx, {
		type : 'bar',
		data : chartData,
		options : {
			responsive : true,
			title : {
				display : true,
				text : '최근 일주일 일자별 게시판 등록 건수'
			},
			legend : {display : false},
			scales : {
				xAxes : [{
					display : true,
					scaleLabel : {
						display : true,
						labelString : '게시물 작성일'
					}
				}],
				yAxes : [{
					display : true,
					scaleLabel : {
						display : true,
						labelString : '게시물 작성 건수'
					},
					stacked : true	//(0부터 시작)
				}]
			}
		}
	});
}
</script>

</body>
</html>
