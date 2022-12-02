package com.example.board.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.example.board.domain.entity.BoardEntity;
import com.example.board.domain.entity.QBoardEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class BoardRepositorySupport extends QuerydslRepositorySupport {

	private final JPAQueryFactory queryFactory;
	
	// QBoardEntity qBoardEntity = new QBoardEntity("boardEntity");
	QBoardEntity qBoardEntity = QBoardEntity.boardEntity;

		public BoardRepositorySupport(JPAQueryFactory queryFactory){
			super(BoardEntity.class);
			this.queryFactory = queryFactory;
		}

		public List<BoardEntity> findByWord(String word){
			return queryFactory.selectFrom(qBoardEntity).fetch();

		}

	// 검색어로 조회(최신순)
	// 	@Query(value="SELECT * FROM board\n"
	// 		+ "WHERE board_title LIKE %?1%\n"
	// 		+ "OR board_content LIKE %?1%\n"
	// 		+ "OR board_id LIKE %?1%\n"
	// 		+ "ORDER BY board_date DESC", nativeQuery=true)
	// public Page<BoardEntity> findByWord(String word, Pageable pageable);


	// 검색어로 조회(최신순)
	// @Query(value="SELECT * FROM board\n"
	// 		+ "WHERE board_title LIKE %?1%\n"
	// 		+ "OR board_content LIKE %?1%\n"
	// 		+ "OR board_id LIKE %?1%\n"
	// 		+ "ORDER BY board_date DESC", nativeQuery=true)
	// public Page<BoardEntity> findByWord(String word, Pageable pageable);


}