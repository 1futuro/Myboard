package com.example.board.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
// @Slf4j
public class BoardService {

	// @Autowired
	private final BoardRepository boardRepo;

	private final JPAQueryFactory queryFactory;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	// 게시물 목록(pageable X)
	public List<BoardDto> boardList() throws FindException {
		List<BoardEntity> entity = boardRepo.findAll();
		
		ModelMapper modelMapper = new ModelMapper();
		List<BoardDto> boardDto = modelMapper.map(entity, new TypeToken<List<BoardDto>>() {
		}.getType());

		return boardDto;
	}
	

	// 게시글 목록(pageable)
	public Page<BoardDto> boardList(int currentPage) throws FindException { //BoardENntity, Pageable
		Page<BoardEntity> entity = boardRepo.findAll(PageRequest.of(currentPage, 5, Sort.by("boardNo").descending()));
		
		// Example<BoardEntity> example = Example.of(new BoardEntity()); // 가상의 테이블
		
		// boardRepo.findAll(example, null);
		
		ModelMapper modelMapper = new ModelMapper();
		Page<BoardDto> boardDto = modelMapper.map(entity, new TypeToken<Page<BoardDto>>() {
		}.getType());
		
		return boardDto;
	}

	// public Page<BoardEntity> findAll(BoardDto dto , Pageable pageable){
	// 	Example<BoardEntity> example = Example.of(new BoardEntity());
	// 	return boardRepo.findAll(example, pageable);
	// }

	
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
	
//	검색어 조회
	// public List<BoardDto> searchBoard2(String word) {
	// 	QBoardEntity qBoard = QBoardEntity.boardEntity;
	// 	List<BoardEntity> entity = 
	// 	queryFactory.selectFrom(qBoard)
	// 	.where((qBoard.boardTitle.contains("").or(qBoard.boardContent.contains("").or(qBoard.boardId.contains("")))))
	// 	.orderBy(qBoard.boardDate.desc()).fetch();

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
	
	
	// 엑셀 다운로드
	public void downloadExcel(HttpServletResponse response) throws IOException{
		
		final String fileName = "게시글 목록";

		// 빈 workbook(하나의 엑셀파일) 생성
		Workbook workbook = new SXSSFWorkbook();
		// 워크시트 생성
		Sheet sheet = workbook.createSheet("게시물 목록");
		
		// 헤더 Cell 스타일
		CellStyle headerStyle = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		headerStyle.setFont(font);

		Row row = null;
		Cell cell = null;

		// Sheet를 채우기 위한 데이터 
		// 헤더
		String[] header = {"번호","제목","내용", "작성자","작성일","조회수"};
		row = sheet.createRow(0); // sheet 안의 행
		for(int i=0 ; i< header.length; i ++) {
			cell = row.createCell(i);
			cell.setCellValue(header[i]);
			cell.setCellStyle(headerStyle);
		}

		List<BoardEntity> dataList = boardRepo.findAll();

		//바디
		// logger.info("check : "+ boardRepo.findAll().size());
		for(int i=0 ; i < dataList.size() ;i++ ){
			row = sheet.createRow(i+1); // 1부터 시작해야함 (헤더 이후로 출력 되어야 하기 때문)
			BoardEntity data = dataList.get(i);

			cell =  row.createCell(0);
			cell.setCellValue(data.getBoardNo());
			
			cell =  row.createCell(1);
			cell.setCellValue(data.getBoardTitle());
			
			cell =  row.createCell(2);
			cell.setCellValue(data.getBoardContent());
			
			cell =  row.createCell(3);
			cell.setCellValue(data.getBoardId());


			cell =  row.createCell(4);
			// logger.error("크기 : "+ sheet.getColumnWidth(4));
			sheet.setColumnWidth(4, 3000);		
			// cell.setCellValue(data.getBoardDate());
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			cell.setCellValue(simpleDateFormat.format(data.getBoardDate()));

			cell =  row.createCell(5);
			cell.setCellValue(data.getViewCnt());
		}

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8") + ".xlsx");
		
		FileOutputStream os = new FileOutputStream(fileName); 
		
		try{
			workbook.write(response.getOutputStream());
			workbook.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
