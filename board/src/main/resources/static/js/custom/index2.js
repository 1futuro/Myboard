(function ($) {
	function showList(url) {
		// 화면 로딩
		$.ajax({
			url: url,
			method: "get",
			success: function (jsonObj) {
				if (jsonObj.status == 1) {
					let content = jsonObj.t.content;
					let $tbody = $(".table-responsive tbody");
					$tbody.children().remove();
					$(content).each(function (index, item) {
						$tbody.append(
						`
						<tr>
						<td>${item.boardNo}</td>
						<td>${item.boardTitle}</td>
						<td>${item.boardId}</td>
						<td>${item.boardDate}</td>
						<td>${item.viewCnt}</td>
						</tr>
						`
						);
					});

					const pages = $(".pagination");
					pages.children().remove();

					let pageObj = jsonObj.t;
					let totalPage = pageObj.totalPages;
					let currentPage = pageObj.number + 1;
					const pageCount = 3;

					// console.log("totalPage :", totalPage);
					// console.log("currentPage :", currentPage);

					let pageGroup = Math.ceil(currentPage / pageCount); // 페이지 그룹
					let last = pageGroup * pageCount; // 화면에 보여질 마지막 페이지 번호
					let first = last - (pageCount - 1);
					const next = last + 1;
					const prev = first - 1;

					// console.log("last : ", last);
					// console.log("first : ", first);
					// console.log("next : ", next);
					// console.log("prev : ", prev);

					if (last > totalPage) { 
						last = totalPage;
					}

					if (first > 3) {
						pages.append(
							`<li class = "page-item" id= "previous"><a class="page-link" href="javascript:void(0)">Previous</a></li>`
						);
					}

					for (let j = first; j <= last; j++) {
						if (currentPage === j) {
							pages.append(
								`<li class="page-item active">
								<a class="page-link" href="javascript:void(0)">${j} <span class="sr-only">(current)</span></a>
								</li>`
							);
						} else if (j > 0) {
						pages.append(
							`<li class="page-item"><a class="page-link" href="javascript:void(0)">${j}</a></li>`
						);
						}
					}
					if (next > 3 || next < totalPage) {
						pages.append(
						`<li class="page-item" id= "next"><a class="page-link" href="javascript:void(0)">Next</a></li>`
						);
					} 
					if(first == totalPage || last == totalPage){
						$('#next').hide();
					}
				}
			},
			error: function (jqXHR) {
				alert(jqXHR.status);
			},
		});
		// return false;
	}
	showList("http://localhost:8888/board/board/list");

	// 검색창 클릭
	$("button.bt-search").click(function () {
		let word = $("input.search-box").val().trim();
		console.log(word);
		url = "http://localhost:8888/board/board/search/" + word;
		showList(url);
	});

	// 페이지 클릭
	// $("ul.pagination li").on("click", (e) => {
	$("ul.pagination").on("click", "li.page-item", (e) => {
		// e : 이벤트 발생(function()과 같은 기능)
		// const pageValue = $(e.currentTarget).text(); // 클릭한 페이지 값
		let pageValue = $(e.currentTarget).text();
		console.log("pageValue check : ", pageValue);
		//next 클릭 시 -> endPage +1 이 startPage

		const $searchVal = $("input.search-box").val().trim(); // 검색어 입력 값
		console.log("searchVal check : ", $searchVal);

		if(pageValue == "Next"){
			pageValue = parseInt($(e.currentTarget).prev().text()) +1;
			console.log("next page : ", pageValue);

		}else if(pageValue == "Previous"){
			pageValue = parseInt($(e.currentTarget).next().text()) -1;
			console.log("previous page :" , pageValue)
		}	
		const pageUrl =
		$searchVal.length == 0
			? `http://localhost:8888/board/board/list/${pageValue}`
			: `http://localhost:8888/board/board/search/${$searchVal}/${pageValue}`;

		console.log("pageUrl check : ", pageUrl);

		showList(pageUrl);
	});

	// 게시글 작성 이동
	let $btwrite = $("button.write-button");
	$btwrite.click(function () {
		location.href = "../templates/write.html";
	});

	//  게시글 상세 이동
	$(".table-responsive").on("click", "tbody tr", function () {
		$board = $("tr").first();
		// alert("클릭완료");
		$boardNo = $(this).children($board).html(); // this-> 클릭한 것
		// alert($boardNo);
		location.href = "../templates/detail.html?boardNo=" + $boardNo;
	});

	// 엑셀 다운로드 클릭
	$("div.excel-download").on("click", "button", (e) => {
		$.ajax({
			url:"http://localhost:8888/board/board/download/excel",
			method:"get",
			success: function(){
				location.href = "http://localhost:8888/board/board/download/excel"
			},
			error: function(){
				alert("다운로드 실패");
			}
		})
	});

})(jQuery);
