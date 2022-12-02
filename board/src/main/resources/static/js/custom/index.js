(function ($) {
	// 화면 로딩
	$.ajax({
		url: "http://localhost:8888/board/board/list",
		method: "get",
		success: function (jsonObj) {
			if (jsonObj.status == 1) {
				let $tbody = $("#data-table tbody");
				$tbody.children().remove(); // tbody 안의 하위 요소들 제거 해 주어야 함 (불필요한 내용 삭제, 리스트업 후 새로고침 시 데이터 업데이트 되도록)
				// 제거 해 주어야 함
				$(jsonObj.t).each(function (index, board) {
					$tbody.append(` 
						<tr>
							<td>${board.boardNo}</td>
							<td>${board.boardTitle}</tßd>
							<td>${board.boardId}</td>
							<td>${board.boardDate}</td>
							<td>${board.viewCnt}</td>
						</tr>
					`); // ` ` 안을 문자열로 인식하여 문자열을 append 하겠다는 뜻
				});
				// 화면 로딩 완료 후 실행될 수 있도록 위치 조정
				$("#data-table").DataTable({
					dom: '<"html5buttons" B>lTfgitp',
				});
			}
		},
		error: function (jqXHR) {
			alert(jqXHR.status);
		},
	});

	//클릭한 해당 게시물로 이동

	// $("div.table-responsive").on("click", "td", function(){
	//     $board = $("tr").first();
	//     // $boardNo = $(this).children($board).val(); // this-> 클릭한 것
	//     $boardNo = $this.first().html();
	//     alert($boardNo);
	//     console.log("게시글번호는"+$boardNo);
	//     // alert($boardNo);
	//     location.href = '../templates/detail.html/';
	// });

	// 게시글 작성 이동
	let $btwrite = $("button.write-button");
	$btwrite.click(function () {
		location.href = "../templates/write.html";
	});

	//  게시글 상세 이동
	$("#data-table").on("click", "tbody tr", function () {
		$board = $("tr").first();
		// alert("클릭완료");
		$boardNo = $(this).children($board).html(); // this-> 클릭한 것
		// alert($boardNo);
		location.href = "../templates/detail.html?boardNo=" + $boardNo;
	});
})(jQuery);
