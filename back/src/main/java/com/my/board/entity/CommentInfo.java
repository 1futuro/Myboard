package com.my.board.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "comment_info")
public class CommentInfo {
    @Id
    private int commentNo;
    private int boardNo;
    private Date commentDt;
    private Date commentModifyDt;
    private int commentLike;
    private int commentStatus;
    private String id;

}
