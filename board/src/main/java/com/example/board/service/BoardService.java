package com.example.board.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.board.domain.entity.BoardEntity;
import com.example.board.domain.repository.BoardRepository;
import com.example.board.dto.BoardDto;
import com.example.board.exception.AddException;
import com.example.board.exception.FindException;
import com.example.board.exception.ModifyException;
import com.example.board.exception.RemoveException;

@Service
public class BoardService {
//	Logger logger = LoggerFactory.getILoggerFactory(getClass());

	@Autowired
	BoardRepository boardRepo;

	// @Autowired
	// JPAQueryFactory jpaQueryFactory;
	
	// 게시글 목록
	// public List<BoardDto> boardList() throws FindException {
	// 	List<BoardEntity> entity = boardRepo.findAll();
		
	// 	ModelMapper modelMapper = new ModelMapper();
	// 	List<BoardDto> boardDto = modelMapper.map(entity, new TypeToken<List<BoardDto>>() {
	// 	}.getType());

	// 	return boardDto;
	// }
	
	// 게시글 목록(pageable)
	public Page<BoardDto> boardList(int currentPage) throws FindException {
		Page<BoardEntity> entity = boardRepo.findAll(PageRequest.of(currentPage, 5, Sort.by("boardNo").descending()));

		ModelMapper modelMapper = new ModelMapper();
		Page<BoardDto> boardDto = modelMapper.map(entity, new TypeToken<Page<BoardDto>>() {
		}.getType());

		return boardDto;
	}

	// 게시글 상세
	public BoardDto boardDetail(Long boardNo) throws FindException {

		Optional<BoardEntity> optB = boardRepo.findById(boardNo); // 글 번호에 해당하는 글 조회

		// 1. 조회수 증가
		if (optB.isPresent()) {
			BoardEntity entity = optB.get();
			entity.setViewCnt(entity.getViewCnt() + 1);
			boardRepo.save(entity);
		} else {
			throw new FindException("게시글이 없습니다.");
		}

		// 2. 게시글 불러오기
		BoardEntity entity = optB.get(); // 증가한 조회수 포함 게시글 불러오기

		ModelMapper modelMapper = new ModelMapper();
		BoardDto dto = modelMapper.map(entity, BoardDto.class);
		return dto;
	}

	// 검색어 조회 - entitymanager / jpa query dsl
	public Page<BoardDto> searchBoard(String word, int currentPage) {
		Page<BoardEntity> entity = boardRepo.findByWord(word, PageRequest.of(currentPage, 5));

		ModelMapper modelMapper = new ModelMapper();
		Page<BoardDto> dto = modelMapper.map(entity, new TypeToken<Page<BoardDto>>() {
		}.getType());

		return dto;
	}
	
	// 검색어 조회
	// public List<BoardDto> searchBoard(String word) {
	// 	List<BoardEntity> entity = boardRepo.findByWord(word);

	// 	ModelMapper modelMapper = new ModelMapper();
	// 	List<BoardDto> dto = modelMapper.map(entity, new TypeToken<List<BoardDto>>() {
	// 	}.getType());

	// 	return dto;
	// }
	

	// 게시글 작성
	public void writeBoard(BoardDto dto) throws AddException{
		ModelMapper modelMapper = new ModelMapper();
		BoardEntity entity = modelMapper.map(dto, BoardEntity.class);
		boardRepo.save(entity);
	}

	// 게시글 수정
	public void modifyBoard(BoardDto dto) throws ModifyException {
		Optional<BoardEntity> optB = boardRepo.findById(dto.getBoardNo());

		if (optB.isPresent()) {
			BoardEntity entity = optB.get();
			entity.setBoardTitle(dto.getBoardTitle());
			entity.setBoardContent(dto.getBoardContent());

			boardRepo.save(entity);
		} else {
			throw new ModifyException("게시글이 없습니다.");
		}
	}

	// 게시글 삭제
	@Transactional
	public void removeBoard(Long boardNo) throws RemoveException {
		Optional<BoardEntity> optB = boardRepo.findById(boardNo);

		if (optB.isPresent()) {
			boardRepo.deleteById(boardNo);
		} else {
			throw new RemoveException("게시글이 없습니다.");
		}
	}
	
	
	// - entitymanager / jpa query dsl
	// public List<BoardDto> findByWord(String word){
	// 	return jpaQueryFactory.select(*).from();
	// }

}
