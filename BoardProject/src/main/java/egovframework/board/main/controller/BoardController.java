package egovframework.board.main.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.board.main.service.BoardService;
import egovframework.board.main.service.BoardVO;
import egovframework.board.main.service.SearchVO;

@Controller
public class BoardController {
	
	private static Logger Logger = LoggerFactory.getLogger(BoardController.class);
	
	@Resource(name="boardService")
	private BoardService boardService;
	
	@Resource(name="propertiesService")
	private EgovPropertyService propertiesService;
	
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	
	/** 
	 * 게시글 목록 조회
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "data/boardList"
	 * @exception Exception
	 */
	@RequestMapping(value = "/boardList.do")
	public String selectBoardList(@ModelAttribute("searchVO") SearchVO searchVO, ModelMap model) throws Exception {
		

		/** context-properties.xml에 각각 10으로 저장되어 있음 */
		searchVO.setPageUnit(propertiesService.getInt("pageUnit"));
		searchVO.setPageSize(propertiesService.getInt("pageSize"));

		/** 
		 * 페이징
		 * 현재 페이지 인덱스, 한 페이지 크기, 페이지 개수로
		 * 현제 패이지에서 첫 글 인덱스, 마지막 글 인덱스를 받아옴
		 *  */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());

		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());

		/* 글 목록 조회 */
		List<?> boardList = boardService.selectBoardList(searchVO);
		
		model.addAttribute("resultList", boardList);

		/** 게시글 전체 개수 */
		int totCnt = boardService.selectBoardListTotCnt(searchVO);
		paginationInfo.setTotalRecordCount(totCnt);
		
		model.addAttribute("paginationInfo", paginationInfo);
		
		return "data/boardList";
		
	}
	
	/** 
	 * 게시글 등록 화면 조회
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "data/baordRegister"
	 * @exception Exception
	 */
	@RequestMapping(value = "/addBoardView.do", method = RequestMethod.POST)
	public String addBoardView(@ModelAttribute("searchVO") SearchVO searchVO,
			BoardVO boardVO,
			ModelMap model) throws Exception {
		
		if(!boardVO.isAnswer()) {
			boardVO.setAnswerLevel(0);
		}
		// modify 구분
		boardVO.setBoardId(null);
		
		model.addAttribute("boardVO", boardVO);
		
		return "data/boardRegister";
		
	}
	
	/** 
	 * 게시글 등록
	 * @param boardVO - 등록할 게시글 정보 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 */
	@RequestMapping(value = "/addBoard.do", method = RequestMethod.POST)
	public String addBoard(@ModelAttribute("searchVO") SearchVO searchVO, 
			@ModelAttribute("boardVO") BoardVO boardVO, 
			@RequestParam("file") List<MultipartFile> fileList, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes)
					throws Exception {
		
		Logger.debug("MainController :: addBoard 등록");
		
		boardService.insertBoard(boardVO, fileList);
		redirectAttributes.addFlashAttribute("searchVO", searchVO);
		return "redirect:/boardList.do";
	}
	
	/** 
	 * 게시글 수정 화면으로 이동
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "data/baordRegister"
	 * @exception Exception
	 */
	@RequestMapping(value = "/updateBoardView.do", method = RequestMethod.POST)
	public String updateBoardView(@ModelAttribute("searchVO") SearchVO searchVO,
			BoardVO boardVO,
			ModelMap model,
			RedirectAttributes redirectAttributes) throws Exception {
			
			BoardVO dbBoard = boardService.selectBoard(boardVO);
			
			if (dbBoard.getPassword().equals(DigestUtils.sha256Hex(boardVO.getPassword()))) {
				model.addAttribute("boardVO", dbBoard);
			} else {
				redirectAttributes.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
				redirectAttributes.addFlashAttribute("searchVO", searchVO);
		        return "redirect:/boardView.do?selectedId=" + boardVO.getBoardId()+"&rootId="+boardVO.getRootId();
			}
		
		return "data/boardRegister";
		
	}
	
	/** 
	 * 게시글 수정
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "data/boardView"
	 * @exception Exception
	 */
	@RequestMapping(value = "/updateBoard.do", method = RequestMethod.POST)
	public String updateBoard(@ModelAttribute("searchVO") SearchVO searchVO,
			BoardVO boardVO,
			@RequestParam("deleteFileIds") String deletedFileIdsString,
			@RequestParam("file") List<MultipartFile> fileList,
			ModelMap model,
			RedirectAttributes redirectAttributes) throws Exception {
			
		List<String> deletedFileIds = new ArrayList<>();
		if (deletedFileIdsString != null && !deletedFileIdsString.trim().isEmpty()) {
		    deletedFileIds = Arrays.asList(deletedFileIdsString.split(","));
		}
		
		boardService.updateBoard(boardVO, fileList, deletedFileIds);
		
		redirectAttributes.addFlashAttribute("searchVO", searchVO);
		return "redirect:/boardView.do?selectedId=" + boardVO.getBoardId()+"&rootId="+boardVO.getRootId();
		
	}
	
	
	/** 
	 * 게시글 조회 화면으로 이동한다.
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "forward:/boardView"
	 * @exception Exception
	 */
	@RequestMapping(value = "/boardView.do", method = RequestMethod.GET)
	public String boardView(
			@RequestParam("selectedId") String boardId,
			@RequestParam("rootId") String rootId,
			@ModelAttribute("searchVO") SearchVO searchVO, 
			ModelMap model) throws Exception {
		
		Logger.debug("BoardController :: 조회 화면 이동");
		
		BoardVO boardVO = new BoardVO();
		boardVO.setBoardId(boardId);
		boardVO.setRootId(rootId);
		
		model.addAttribute("boardVOList", boardService.selectBoardAndAnswer(boardVO));
		
		return "data/boardView";
		
	}
	
	/** 
	 * 게시글 조회
	 * @param boardVO - 등록할 게시글 정보 VO
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 */
	@RequestMapping(value = "/selectBoardAndAnswer.do", method = RequestMethod.GET)
	public List<?> selectBoardAndAnswer(@ModelAttribute("searchVO") SearchVO searchVO, BoardVO boardVO)
			throws Exception {
		
		Logger.debug("BoardController:: 조회 boardVO :: {}", boardVO.toString());
		
		return boardService.selectBoardAndAnswer(boardVO);
	}
	
	/** 
	 * 게시글 삭제
	 * @param searchVO - 목록 조회조건 정보가 담긴 VO
	 * @param model
	 * @return "data/baordRegister"
	 * @exception Exception
	 */
	@RequestMapping(value = "/deleteBoard.do", method = RequestMethod.POST)
	public String deleteBoard(@ModelAttribute("searchVO") SearchVO searchVO,
			BoardVO boardVO,
			ModelMap model,
			RedirectAttributes redirectAttributes) throws Exception {
			
		BoardVO dbBoard = boardService.selectBoard(boardVO);
		
		if (dbBoard.getPassword().equals(DigestUtils.sha256Hex(boardVO.getPassword()))) {
			model.addAttribute("boardVO", dbBoard);
		} else {
			redirectAttributes.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
			redirectAttributes.addFlashAttribute("searchVO", searchVO);
	        return "redirect:/boardView.do?selectedId=" + boardVO.getBoardId()+"&rootId="+boardVO.getRootId();
		}
		
		
		boardService.deleteBoard(boardVO);
			
		redirectAttributes.addFlashAttribute("searchVO", searchVO);
		return "redirect:/boardList.do";
		
	}
	
	
	
}
