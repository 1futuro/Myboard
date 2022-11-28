package com.example.board.control;

import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.board.dto.BoardDto;
import com.example.board.dto.ResultBean;
import com.example.board.exception.AddException;
import com.example.board.exception.FindException;
import com.example.board.exception.ModifyException;
import com.example.board.exception.RemoveException;
import com.example.board.service.BoardService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/board/*")
public class BoardController {

	@Autowired
	BoardService service;

	@Autowired
	ServletContext sc;

	
	// // 게시글 목록
	// @GetMapping(value = {"list", "list/{optCp}" })
	// public ResultBean<List<BoardDto>> boardList(@PathVariable Optional<Integer> optCp) {
	// 	ResultBean<List<BoardDto>> rb = new ResultBean<>();

	// 	try {
	// 		List<BoardDto> pb = service.boardList();
	// 		rb.setStatus(1);
	// 		rb.setT(pb);
	// 		return rb;
	// 	} catch (FindException e) {
	// 		rb.setStatus(0);
	// 		rb.setMsg(e.getMessage());
	// 	}
	// 	return rb;
	// }
	
	// 게시글 목록(pageable)
	@GetMapping(value = {"list", "list/{optCp}" })
	public ResultBean<Page<BoardDto>> boardList(@PathVariable Optional<Integer> optCp) {
		ResultBean<Page<BoardDto>> rb = new ResultBean<>();

		int currentPage;

		if (optCp.isPresent()) {
			currentPage = optCp.get()-1;
		} else {
			currentPage = 0;
		}

		try {
			Page<BoardDto> pb = service.boardList(currentPage);
			rb.setStatus(1);
			rb.setT(pb);
			return rb;
		} catch (FindException e) {
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	// 게시글 상세
	@GetMapping(value = "detail/{boardNo}")
	public ResultBean<BoardDto> boardDetail(@PathVariable Long boardNo) {

		ResultBean<BoardDto> rb = new ResultBean<>();

		try {
			BoardDto b = service.boardDetail(boardNo);
			rb.setStatus(1);
			rb.setT(b);
		} catch (FindException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}

	//검색어 조회
	// @GetMapping(value = {"search/{optWord}/{optCp}","search/{optWord}","search"})
	// public ResultBean<List<BoardDto>> searchBoard (@PathVariable Optional<String> optWord,
	// 												@PathVariable Optional<Integer> optCp){
												
	// 	ResultBean<List<BoardDto>> rb = new ResultBean<>();
	// 	List<BoardDto> pb;
		
	// 	String word ;
		
	// 	try {
	// 		if(optWord.isPresent()) {
	// 			word = optWord.get();
	// 		}else {
	// 			word = "";
	// 		}
	
	// 		if("".equals(word)) {
	// 			// pb = service.boardList();
	// 			pb = service.boardList();
	// 		}else {
	// 				pb = service.searchBoard(word);
	// 				rb.setStatus(1);
	// 			rb.setT(pb);
	// 		}
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// 	return rb;
	// }
	
	// 검색어 조회(pageable)
	@GetMapping(value = {"search/{optWord}/{optCp}","search/{optWord}","search"})
	public ResultBean<Page<BoardDto>> searchBoard (@PathVariable Optional<String> optWord,
													@PathVariable Optional<Integer> optCp,
													@PageableDefault(page = 0, sort = "boardNo", direction = Direction.DESC) Pageable pageable){
												
		ResultBean<Page<BoardDto>> rb = new ResultBean<>();
		Page<BoardDto> pb;
		
		String word ;
		int currentPage ;
		
		try {
			if(optWord.isPresent()) {
				word = optWord.get();
			}else {
				word = "";
			}
	
			if(optCp.isPresent()) {
				currentPage = optCp.get()-1;
			}else {
				currentPage = 0;
			}
		
			if("".equals(word)) {
				pb = service.boardList(currentPage);
			}else {
				pb = service.searchBoard(word, currentPage);
				rb.setStatus(1);
				rb.setT(pb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rb;
	}

	
	// 게시글 등록
	@PostMapping(value= "{write}", consumes="application/json")
	public ResultBean<BoardDto> writeBoard(@RequestBody BoardDto boardDto){
		ResultBean<BoardDto> rb = new ResultBean<>();
		
			try {
				System.out.println("제목 : " + boardDto.getBoardTitle());
				service.writeBoard(boardDto);
				rb.setStatus(1);
				rb.setT(boardDto);			
			} catch (AddException e) {
				e.printStackTrace();
				rb.setStatus(0);
				rb.setMsg(e.getMessage());
			}
		return rb;
	}

	// 게시글 수정
	@PutMapping(value = "{boardNo}", consumes="application/json")
	public ResultBean<BoardDto> modifyBoard(@PathVariable Long boardNo,
											@RequestBody BoardDto boardDto){
		ResultBean<BoardDto> rb = new ResultBean<>();
		
		try {
			boardDto.setBoardNo(boardNo);
			service.modifyBoard(boardDto);
			rb.setStatus(1);
			rb.setT(boardDto);
		} catch (ModifyException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg(e.getMessage());
		}
		return rb;
	}
	
	// 게시글 삭제
	@DeleteMapping(value = "{boardNo}",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResultBean<BoardDto> removeBoard(@PathVariable Long boardNo){
		
		ResultBean<BoardDto> rb = new ResultBean<>();
		try {
			service.removeBoard(boardNo);
			rb.setStatus(1);
			rb.setMsg("삭제완료");
		} catch (RemoveException e) {
			e.printStackTrace();
			rb.setStatus(0);
			rb.setMsg("삭제실패");
		}
		return rb;
	}

}

