package com.example.board.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString
public class ResultBean<T> {
	private int status;
	private String msg;
	private T t;
	private List<T> ltl;
		
	
}
