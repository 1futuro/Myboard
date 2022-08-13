package com.my.board.dto;

import jdk.nashorn.internal.objects.annotations.Getter;

import java.util.Date;
@Getter
public class Board {
    private int boardNo;
    private String boardTitle;
    private String boardContent;
    private Date boardDt;
    private Date boardModifyDt;
    private int boardViewcount;
    private int boardLikecount;
    private int boardStatus;
    private String id;
    private int boardCommentcount;

}
