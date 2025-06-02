package egovframework.board.main.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	void insertFile(FileVO fileVO) throws Exception;
	
	void deleteDbFile(BoardVO vo) throws Exception;
	
	void deleteFile(List<FileVO> fileList) throws Exception;

	List<FileVO> selectFileList(BoardVO vo) throws Exception;

	void deleteFileListById(List<String> deletedFileIds) throws Exception;

	boolean existFile(String boardId) throws Exception;
	
	void insertFileList(List<MultipartFile> fileList, BoardVO vo) throws Exception;

	List<FileVO> selectFileListForDelete(List<String> boardIdList) throws Exception;

	void deleteDBFileListByBoardId(List<String> boardIdList) throws Exception;
}
