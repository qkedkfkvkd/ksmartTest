package kr.or.ksmart.ksmart_TestPro.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ksmart.ksmart_TestPro.mapper.BoardMapper;
import kr.or.ksmart.ksmart_TestPro.vo.Board;

@Service
@Transactional
public class BoardService {
	
	@Autowired
	private BoardMapper boardMapper;
	
	// 글 리스트 보기
	public Map<String, Object> boardList(int currentPage) {
		
		final int ROW_PER_PAGE = 10;
		// 페이지를 구성할 행의 갯수
		
		int startPageNum = 1;
		// 보여줄 시작 페이지 번호
		
		int lastPageNum = ROW_PER_PAGE;
		// 보여줄 페이징의 마지막 번호의 갯수 초기화
		
		
		// 6번째일 때 2,3,4,5,[6],7,8,9,10,11
		// 2는 startPageNum, 11은 lastPageNum
		
		// 7번째일 때 3,4,5,6,[7],8,9,10,11,12
		// 3은 startPageNum, 12는 lastPageNum
		// 즉, 6이나 7이 가운데에 올 수 있게 한다.
		if(currentPage > (ROW_PER_PAGE/2)) {
			startPageNum = currentPage - ((lastPageNum/2)-1);
			// currentPage = 6일 경우
			// startPageNum = 6 - ((10/5)-1) = 2
			
			lastPageNum += (startPageNum-1);
			// lastPageNum = lastPageNum + (2-1) = 10 + 1 = 11
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		int startRow = (currentPage-1)*ROW_PER_PAGE;
		// 쿼리문에서 limit (startRow, RowPerPage) 에 들어갈 출력 시작 행
		// currentPage = 1 일 경우
		// startRow = (1-1)*10 = 0
		
		// currentPage = 4 일 경우
		// startRow = (4-1)*10 = 30
		
		// currentPage = 6 일 경우
		// startRow = (6-1)*10 = 50
		
		
		map.put("startRow", startRow);			// 출력 시작 행
		map.put("RowPerPage", ROW_PER_PAGE);	// 테이블에서 가져올 행 갯수
		
		double boardCount = boardMapper.boardListCount();
		// 전체 글 갯수 받아온다. 더블로 받아와야 마지막 페이지 계산 시 무조건 올림 계산할 때 편하다
		
		int lastPage = (int)(Math.ceil(boardCount/ROW_PER_PAGE));
		// 마지막 페이지 계산
		// 기본 출력 행(ROW_PER_PAGE)는 10으로 되어있으므로 만약 게시판 글 갯수가
		// 정확하게 10의 배수 개일 경우 마지막 페이지는 예를 들어 전체 글 갯수가 100개다. 마지막 페이지 10페이지.
		// 전체 글 갯수(boardCount) : 103개 일 경우
		// 103/10 = 10.3 → 무조건 올림처리 되어 lastPage(마지막페이지) = 11 페이지
		
		
		// currentPage(현재 페이지) = 10
		// lastPage(마지막 페이지) = 14 (전체 글 갯수 133개)
		// 10 >= (14-4)   →   10 >= 10
		// lastPageNum(페이징 마지막 번호) = 14
		
		// curr이 8 이다.
		// 4,5,6,7,[8],9,10,11,12,13		-> 페이지 10개
		
		// curr이 9 이다.
		// 5,6,7,8,[9],10,11,12,13,14		-> 페이지 10개
		
		// curr이 10 이다.
		// 6,7,8,9,[10],11,12,13,14		-> 페이지 9개
		
		// curr이 11 이다.
		// 7,8,9,10,[11],12,13,14			-> 페이지 8개
		
		// 즉, 보여지는 페이징의 갯수를 줄이는 개념의 if 문이다.
		// 보여지는 현재 페이지의 앞에 4개의 페이지가 있기 때문에 -4를 해야한다.
		// 마지막 페이지에 가까워 질수록 페이징된 리스트의 갯수를 줄인다.
		if(currentPage >= (lastPage-4)) {
			lastPageNum = lastPage;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", boardMapper.boardList(map));		// 글 리스트
		resultMap.put("currentPage", currentPage);				// 현재 페이지
		resultMap.put("lastPage", lastPage);					// 마지막 페이지
		resultMap.put("startPageNum", startPageNum);			// 페이징 시 시작 페이지
		resultMap.put("lastPageNum", lastPageNum);				// 페이징 시 마지막 페이지
		
		return resultMap;
	}
	
	// 헤더에서 게시판 글쓰기 클릭하면 실행되는 메소드
	public int addBoard(Board board) {
		return boardMapper.addBoard(board);
	}
	
	// 게시판에서 검색어 입력 후 검색하기 버튼 누르면 실행되는 메소드
	public List<Board> boardList(String sk, String sv) {
		int startPage = 0;
		// 시작 페이지
		
		final int row_per_Page = 10;
		// 한 페이지에 보여줄 최대 페이지 수
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("startPage", startPage);
		map.put("perPage", row_per_Page);
		map.put("sk", sk);
		map.put("sv", sv);
		
		return boardMapper.boardList(map);
	}
	
	// 게시판에서 글을 보거나 글 수정 시 게시글 내용 보기
	public Board getBoardByNo(int boardNo) {
		return boardMapper.getBoardByNo(boardNo);
	}
	
	// 게시판 글 수정
	public Board modifyBoard(Board board) {
		int modifyCheck = boardMapper.modifyBoard(board);
		Board result = null;
		// 수정 성공으로 초기화
		
		if(modifyCheck == 0) { // 비번 불일치
			result = boardMapper.getBoardByNo(board.getBoardNo());
		}
		
		return result;
	}
}
