package kr.or.ksmart.ksmart_TestPro.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ksmart.ksmart_TestPro.vo.Board;

@Mapper
public interface BoardMapper {
	public int addBoard(Board board);
	// 게시글 추가
	
	public List<Board> boardList(Map<String, Object> map);
	// 게시글 리스트
	
	public int boardListCount();
	// 전체 게시글 갯수
	
	public Board getBoardByNo(int boardNo);
	// 게시글 한개
	
	public int modifyBoard(Board board);
	// 게시글 수정
}
