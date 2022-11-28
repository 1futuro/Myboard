(function ($) {
	function showList(url){
		// 화면 로딩
		$.ajax({
			url: "http://localhost:8888/board/board/list",
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

					
					// 페이지 그룹
					// 총 페이지 수가 3(cntPerPageGroup)보다 많으면 next 활성화. next 클릭 시 리셋하고 4,5,6 노출. previous 노출
					// 총 페이지 수가 3 보다 적으면 next, previous 숨김
					// let $pagegroup = $('ul.pagination li');
					let $pagegroupHtml = '';
					let pageObj = jsonObj.t;
					let totalPage = pageObj.totalPages;
					let currentPage = pageObj.number + 1;
					let cntPerPageGroup = 3;
					let endPage = Math.ceil(currentPage / cntPerPageGroup) * cntPerPageGroup;
					let startPage = endPage - cntPerPageGroup + 1;
					let nextPage = endPage + 1;
					
					// console.log('startPage :', startPage)
					// console.log('endPage :', endPage)
					// console.log('totalPage :', totalPage)
					// console.log('currentPage :', currentPage)
					let $ul = $('ul.pagination');
					$ul.children().remove();

					if(totalPage < endPage){
						endPage = totalPage;
					}
					// 첫번째 페이지
					if(currentPage > 1 && cntPerPageGroup < currentPage){
						$pagegroupHtml += `<a class="page-link"
						href="javascript:void(0)">Previous</a>`
					}


					
					// if (totalPage < 1) {
					// startPage = endPage;
					// } else if (totalPage < endPage) {
					// 	endPage = totalPage;
					// }
					// if(endPage == 3){
					// 	$('#previous').hide();
					// }else if(endPage == totalPage){
					// 	$('#next').hide();
					// } else if(endPage < totalPage ){
					// 	$('#next').show();
					// } else if(startPage > 1) {
					// 	$('#previous').show();
					// }
					
					
				} 
					
				},
			
			error: function (jqXHR) {
				alert(jqXHR.status);
			}
		});
	
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
	$("ul.pagination li").on("click", (e) => {
		// e : 이벤트 발생(function()과 같은 기능)
		const pageValue = $(e.currentTarget).text(); // 클릭한 페이지 값
		console.log('pageValue check : ' , pageValue);
		//next 클릭 시 -> endPage +1 이 startPage


		const $searchVal = $("input.search-box").val().trim(); // 검색어 입력 값
		console.log('searchVal check : ' , $searchVal);

		const pageUrl =
			$searchVal.length == 0
			? `http://localhost:8888/board/board/list/${pageValue}`
			: `http://localhost:8888/board/board/search/${$searchVal}/${pageValue}`;

		console.log("pageUrl check : ", pageUrl);

		showList(pageUrl);
	});

	
	
})(jQuery);
