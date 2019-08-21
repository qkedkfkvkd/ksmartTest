package kr.or.ksmart.ksmart_TestPro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ksmart.ksmart_TestPro.service.BoardService;
import kr.or.ksmart.ksmart_TestPro.vo.Board;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/addBoard")
	public String addBoard() {
		return "/board/bInsert/addBoard";
	}
	
	@PostMapping("/addBoard")
	public String addBoard(Board board) {
		System.out.println(board + " <- board   addBoard()   BoardController.java");
		int result = boardService.addBoard(board);
		System.out.println(result + " <- result   addBoard()   BoardController.java");
		return "redirect:/boardList";
	}
	
	@GetMapping("/boardList")
	public String boardList(
			 Model model
			,@RequestParam(value = "currentPage"
						  ,required = false		// 반드시 받아야할 필요가 없다. 안 받을시 에러나는걸 방지한다.
						  ,defaultValue = "1") int currentPage) {
		
//		Map<String, Object> map = boardService.boardList(currentPage);
		
//		model.addAttribute("boardList", map.get("list"));
//		model.addAttribute("currentPage", map.get("currentPage"));
//		model.addAttribute("lastPage", map.get("lastPage"));
//		model.addAttribute("startPageNum", map.get("startPageNum"));
//		model.addAttribute("lastPageNum", map.get("lastPageNum"));
//		하다가 에러날 땐 일단 길게 코딩하면 이해가 쉬워진다.
//		에러먼저 잡고 나중에 간결하게 고치자.
		
		model.addAttribute("map", boardService.boardList(currentPage));
		return "/board/bList/boardList";
	}
	
	@PostMapping("/boardList")
	public String boardList(
			 @RequestParam(value = "sk") String sk
			,@RequestParam(value = "sv") String sv
			,Model model) {
		model.addAttribute("boardList",
				boardService.boardList(sk, sv));
		return "/board/bList/boardList";
	}
	
	@GetMapping("/modifyBoard")
	public String modifyBoard(Model model,
			@RequestParam(value = "boardNo") int boardNo) {
		model.addAttribute("board", boardService.getBoardByNo(boardNo));
		return "/board/bUpdate/modifyBoard";
	}
	
	@PostMapping("/modifyBoard")
	public String modifyBoard(Board board, Model model) {
		System.out.println(board + " <- board   modifyBoard()   BoardController.java");
		Board resultBoard = boardService.modifyBoard(board);
		String path = "redirect:/boardList";
		// 수정 성공하여 리스트로 리다이렉트 되게끔 초기화
		
		if(resultBoard != null) {
			model.addAttribute("result", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("board", resultBoard);
			path = "/board/bUpdate/modifyBoard";
		}
		
		return path;
	}
}
