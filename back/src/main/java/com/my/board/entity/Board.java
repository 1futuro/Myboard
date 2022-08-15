package com.my.board.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
@Entity
@Table (name = "board")
public class Board {
    @Id
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
