package com.example.board.dto;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BoardDto {

	private Long boardNo; 
	
	private String boardTitle;
	
	private String boardContent;
	
	private String boardId;
	
	@JsonFormat(pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
	private Date boardDate;
	
	private Integer viewCnt;
}
