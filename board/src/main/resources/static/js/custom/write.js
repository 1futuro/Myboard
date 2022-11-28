(function ($) {
    
    let queryString = location.search.split("=")[1];
    console.log(queryString);
    let boardNo = queryString;
    console.log(boardNo);
    $('#summernote').summernote({
        tabsize: 2,
        height: 250,
        focus: true,
        fontSize : 18
    });
    


    // 작성, 수정 버튼 설정
    if(typeof queryString == "undefined" || queryString==null || queryString==""){ // 글 번호 없는 경우
        $("button.bt-modify").hide();
        $("button.bt-submit").show();
    }else{
        $("button.bt-submit").hide();
        $("button.bt-modify").show();
        // 게시글 불러오기
        $.ajax({
            url:"http://localhost:8888/board/board/detail/"+ boardNo,
            method:"get",
            success: function(jsonObj){
                if(jsonObj.status == 1){
                    let title = jsonObj.t.boardTitle;
                    let content = jsonObj.t.boardContent;
                    $("div.write-title>input").val(title);
                    $('#summernote').summernote('code', content);
                }
            },
            error: function(jsonObj){
                alert("error : " + jsonObj.status);
            }
        });
    }

    // 게시글 작성
    let $btObj = $("button.bt-submit");
    $btObj.click(function(){
        let title = $('div.write-title>input').val();
        let content = $('#summernote').summernote('code');
        console.log("제목 :" + title);
        console.log("내용 : " + content);
        let dataObj ={
            boardTitle : title,
            boardContent : content,
            boardId : "id10"
        }
        let parsingData = JSON.stringify(dataObj)
        console.log(parsingData);

        if(title == "" || title == " " || title == "  "){
            alert("제목은 필수 입력 값입니다.");
        }else if(content == "" || content == " " || content=="  "){
            alert("내용은 필수 입력 값입니다.");
        }

        $.ajax({
            url:"http://localhost:8888/board/board/write",
            method:"post",
            contentType : "application/json",
            data : JSON.stringify(dataObj),
            success: function(jsonObj){
                if(jsonObj.status == 1){
                    alert("게시글 등록 성공");
                    location.href = "../templates/index.html";
                }
            },
            error: function(jqXHR){
                alert("등록 실패 error : " + jqXHR.status);
            }
            
        });
    }); 
    
    
    // 게시글 수정
    let $btmodify = $("button.bt-modify");   
    $btmodify.click(function(){
        let title = $('div.write-title>input').val();
        let content = $('#summernote').summernote('code');
        console.log("제목 :" + title);
        console.log("내용 : " + content);
        let dataObj ={
            boardTitle : title,
            boardContent : content
        }
        let parsingData = JSON.stringify(dataObj)
        console.log(parsingData);

        if(title == "" || title == " " || title == "  "){
            alert("제목은 필수 입력 값입니다.");
        }else if(content == "" || content == " " || content=="  "){
            alert("내용은 필수 입력 값입니다.");
        }
        $.ajax({
            url:"http://localhost:8888/board/board/"+ boardNo,
            method:"put",
            data: JSON.stringify(dataObj),
            contentType : "application/json",
            success: function(jsonObj){
                if(jsonObj.status == 1){
                    alert("수정 성공");
                    location.href = "../templates/index.html";
                }
            },
            error: function(jqXHR){
                alert("수정 실패 error : " + jqXHR.status);
            }
        });
    });    
})(jQuery);