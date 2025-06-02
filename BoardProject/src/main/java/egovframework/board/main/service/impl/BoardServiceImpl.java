package egovframework.board.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.board.main.service.BoardService;
import egovframework.board.main.service.BoardVO;
import egovframework.board.main.service.FileService;
import egovframework.board.main.service.FileVO;
import egovframework.board.main.service.SearchVO;
import egovframework.board.main.service.UserService;
import egovframework.board.main.service.UserVO;

@Service("boardService")
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BoardServiceImpl.class);


	@Resource(name = "boardMapper")
	private BoardMapper boardDAO;
	
	@Resource(name = "userService")
	private UserService userService;
	
	@Resource(name = "fileService")
	private FileService fileService;
	
	/**
	 * 글을 등록한다.
	 * @return 등록한 글 id
	 * @exception Exception
	 */
	@Override
	public String insertBoard(BoardVO vo, List<MultipartFile> fileList) throws Exception {
		
		// 유저 등록
		UserVO user = userService.selectUser(vo);	
		vo.setUserId(user.getUserId());
		vo.setName(user.getName());
		
		// board id 생성
		String boardId = UUID.randomUUID().toString();
		vo.setBoardId(boardId);
		
		LOGGER.debug("boardVO 등록 service :: {}", vo.toString());
		
		// 답글이 아니라면 root id에 자기 자신
		if(!vo.isAnswer()) {
			vo.setRootId(boardId);
		}
		
		// 파일 여부를 existFile에 담아줌
		vo.setExistFile(fileList != null 
				&& !fileList.isEmpty() 
				&& fileList.get(0).getSize() > 0);
		
		// 글 저장
		boardDAO.insertBoard(vo);
		
		// 파일 있다면 저장
		if(vo.isExistFile()) {
			fileService.insertFileList(fileList, vo);			
		}
		
		return boardId;
	}

	/**
	 * 글을 수정한다.
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void updateBoard(BoardVO vo, List<MultipartFile> fileList, List<String> deletedFileIds) throws Exception {
		
		/* 기존 파일 중에 삭제한 파일 id list로 파일 정보 조회 */
		List<FileVO> deleteFileList = fileService.selectFileList(vo);
		
		/* db에 파일 삭제 */
		fileService.deleteFileListById(deletedFileIds);
			
		
		/* 추가 파일 등록 */
		if(fileList != null 
				&& !fileList.isEmpty()
				&& fileList.get(0).getSize() > 0) {
			fileService.insertFileList(fileList, vo);
		}
		
		/* boardId로 db에 파일 여부 조회 */
		if(fileService.existFile(vo.getBoardId())) {
			vo.setExistFile(true);
		}
		
		/* 글 수정 */
		boardDAO.updateBoard(vo);
		
		/* 실제 파일 삭제 */
		if(vo.isExistFile()) {
			fileService.deleteFile(deleteFileList);
		}
		
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 BoardVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void deleteBoard(BoardVO vo) throws Exception {
		
		/* 선택한 boardVO 본인, 자식 트리 id 조회 */
		List<String> boardIdList = boardDAO.selectBoardIdsforDelete(vo);
		
		/* 삭제할 파일 선택 */
		List<FileVO> fileListForDelete = fileService.selectFileListForDelete(boardIdList);
		
		/* 파일 db 삭제 */
		if(fileListForDelete != null 
				&& !fileListForDelete.isEmpty()
				&& fileListForDelete.size() > 0) {
			fileService.deleteDBFileListByBoardId(boardIdList);
		}
		
		/* 글 삭제 */
		boardDAO.deleteBoard(vo);
		
		/* 실제 파일 삭제 */
		if(fileListForDelete != null 
				&& !fileListForDelete.isEmpty()
				&& fileListForDelete.size() > 0) {
			fileService.deleteFile(fileListForDelete);
		}
	}
	
	/**
	 * 글과 답글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	@Override
	public List<BoardVO> selectBoardAndAnswer(BoardVO vo) throws Exception {
		
		
		/* 선택한 글 조회수 증가 */
		increaseClickCount(vo);
		
		
		/* 글, 답글 조회 */
		List<BoardVO> result = boardDAO.selectBoardAndAnswer(
				vo.getRootId());
		
		for(BoardVO board : result) {
			/* 파일 목록 가져오기 */
			if(board.isExistFile()) {
				board.setFileVOList(fileService.selectFileList(board));
			}
		}
		
		return result;
	}
	
	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	@Override
	public BoardVO selectBoard(BoardVO vo) throws Exception {
		
		
		BoardVO result = boardDAO.selectBoard(vo);
		
		/* 파일 목록 가져오기 */
		if(result.isExistFile()) {
			result.setFileVOList(fileService.selectFileList(vo));
		}
		return result;
	}
	
	/**
	 * 조회수 증가
	 * @param 조회수 증가시킬 BoardVO
	 * @return void
	 * @exception Exception
	 */
	@Override
	public void increaseClickCount(BoardVO boardVO) throws Exception {
		boardDAO.increaseClickCount(boardVO);
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<?> selectBoardList(SearchVO searchVO) throws Exception {
		
		List<?> boardList = boardDAO.selectBoardList(searchVO);
		
		/* 조회한 글이 없다면 빈 리스트 전달해준다. */
		if (boardList == null) {
		    boardList = new ArrayList<>();
		}
		
		return boardList;
	}

	/**
	 * 검색한 글의 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	@Override
	public int selectBoardListTotCnt(SearchVO searchVO) {
		return boardDAO.selectBoardListTotCnt(searchVO);
	}

}
