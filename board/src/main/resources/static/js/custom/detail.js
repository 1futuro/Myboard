$(function(){

    let boardNo = location.search.split("=")[1];
    console.log(boardNo);


    $.ajax({
        url:"http://localhost:8888/board/board/detail/"+ boardNo,
        method:"get",
        success: function(jsonObj){
            if(jsonObj.status == 1){
                let detailObj = jsonObj.t;

                $("div.board_no").html(detailObj.boardNo);
                $("div.board_title").html(detailObj.boardTitle);
                $("div.board_id").html(detailObj.boardId);
                $("div.board_dt").html(detailObj.boardDate);
                $("div.view_cnt").html(detailObj.viewCnt);
                $("div.board_content").html(detailObj.boardContent);

            }
        },
        error: function(jqXHR){
            alert(jqXHR.status);
        }

    });

    // 수정
    $btmodify = $("button.bt-modify");
    $btmodify.click(function(){
        location.href = "../templates/write.html?boardNo=" + boardNo
    });
    
    // 삭제
    $btremove = $("button.bt-delete");
    $btremove.click(function(){
        $.ajax({
            url: "http://localhost:8888/board/board/" + boardNo,
            method: "delete",
            success: function(jsonObj){
                if(jsonObj.status == 1){
                    alert("삭제성공");
                    location.href = "../templates/index.html" ;
                }
            },
            error: function(jqXHR){
                alert(jqXHR.status);
            }
        });
    });

    


});