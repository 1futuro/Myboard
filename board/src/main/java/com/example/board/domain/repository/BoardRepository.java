package com.example.board.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.board.domain.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
	
	//게시글 목록 조회(최신순)
	public Page<BoardEntity> findAll(Pageable pageable);	

	// 검색어로 조회(최신순)
	@Query(value="SELECT * FROM board\n"
			+ "WHERE board_title LIKE %?1%\n"
			+ "OR board_content LIKE %?1%\n"
			+ "OR board_id LIKE %?1%\n"
			+ "ORDER BY board_date DESC", nativeQuery=true)
	public Page<BoardEntity> findByWord(String word, Pageable pageable);
}



