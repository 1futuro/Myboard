package com.example.board.domain.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name="board")
@DynamicInsert
@DynamicUpdate
public class BoardEntity {
	
	@Id
	@Column(name= "board_no")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long boardNo; // PK
	
	@Column(name= "board_title")
	private String boardTitle;
	
	@Column(name= "board_content")
	private String boardContent;
	
	@Column(name= "board_id")
	private String boardId;
	
	@Column(name= "board_date")
	@ColumnDefault(value= "sysdate")
	@JsonFormat(pattern = "yyyy/MM/dd", timezone = "Asia/Seoul")
	private Date boardDate;
	
	@Column(name= "view_cnt")
	@ColumnDefault(value= "0")
	private Integer viewCnt;
	
}
