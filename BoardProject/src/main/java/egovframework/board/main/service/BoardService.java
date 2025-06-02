package egovframework.board.main.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface BoardService {
	
	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 BoardVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	String insertBoard(BoardVO vo, List<MultipartFile> fileList) throws Exception;

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 BoardVO
	 * @param fileList - 추가할 파일 리스트 
	 * @param deleteFileIds - 삭제할 파일 id 리스트
	 * @return 없음
	 * @exception Exception
	 */
	void updateBoard(BoardVO vo, List<MultipartFile> fileList, List<String> deletedFileIds) throws Exception;

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 BoardVO
	 * @return 없음
	 * @exception Exception
	 */
	void deleteBoard(BoardVO vo) throws Exception;
	
	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	List<?> selectBoardAndAnswer(BoardVO vo) throws Exception;

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	List<?> selectBoardList(SearchVO searchVO) throws Exception;

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 게시글 총 갯수
	 * @exception
	 */
	int selectBoardListTotCnt(SearchVO searchVO);

	/**
	 * 조회수 증가
	 * @param 조회수 증가시킬 BoardVO
	 * @return 없음
	 * @exception Exception
	 */
	void increaseClickCount(BoardVO boardVO) throws Exception;
	
	/**
	 * 글 1개를 조회한다.
	 * @param vo - 조회할 정보가 담긴 BoardVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	BoardVO selectBoard(BoardVO vo) throws Exception;

}
