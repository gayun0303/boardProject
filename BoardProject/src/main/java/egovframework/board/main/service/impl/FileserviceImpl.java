package egovframework.board.main.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.board.main.service.BoardVO;
import egovframework.board.main.service.FileService;
import egovframework.board.main.service.FileVO;

@Service("fileService")
public class FileserviceImpl extends EgovAbstractServiceImpl implements FileService  {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileserviceImpl.class);
	
	@Resource(name = "fileMapper")
	private FileMapper fileDAO;
	
	private static final String UPLOAD_DIR = "C:/upload";
	
	/* 게시글에 첨부된 파일 조회 */
	@Override
	public List<FileVO> selectFileList(BoardVO vo) throws Exception {
		return fileDAO.selectFileList(vo.getBoardId());
	}

	/* 파일 정보 저장 */
	@Override
	public void insertFile(FileVO fileVO) throws Exception {
		fileDAO.insertFile(fileVO);
	}
	
	/* 파일 리스트 저장 */
	@Override
	public void insertFileList(List<MultipartFile> fileList, BoardVO vo) throws Exception {
		
		
		/* 오늘 날짜 폴더로 저장 경로 지정 */
		String today = new SimpleDateFormat("yyMMdd").format(new Date());
		String saveFolder = UPLOAD_DIR + File.separator + today;
		
		/* 폴더 없으면 생성 */
		File folder = new File(saveFolder);
		if(!folder.exists())	folder.mkdirs();
		
		/* 여러 파일 각각 등록 */
		for(MultipartFile oneFile : fileList) {
			FileVO fileVO = new FileVO();
			
			/* 원래 파일명, 저장 파일명 */
			String originalFileName = oneFile.getOriginalFilename();
			String saveFileName = UUID.randomUUID().toString()
					+ originalFileName.substring(originalFileName.lastIndexOf('.'));
			
			fileVO.setFilePath(today);
			fileVO.setOriginalName(originalFileName);
			fileVO.setSaveName(saveFileName);
			fileVO.setFileSize(oneFile.getSize());
			fileVO.setBoardId(vo.getBoardId());
			fileVO.setFileId(UUID.randomUUID().toString());
			
			// 파일 저장
			oneFile.transferTo(new File(folder, saveFileName));
			insertFile(fileVO);
			
		}
		
	}

	@Override
	public void deleteDbFile(BoardVO vo) throws Exception {
		
		/* db에서 파일 삭제 */
		fileDAO.deleteFile(vo.getBoardId());
	}

	/* 실제 파일 삭제 */
	@Override
	public void deleteFile(List<FileVO> fileList) throws Exception {
		for(FileVO fileVO: fileList) {
			File file = new File(UPLOAD_DIR + File.separator + fileVO.getFilePath(), fileVO.getSaveName());
			if(file.exists() && !file.delete()) {
				throw new RuntimeException("파일 삭제 실패: " + UPLOAD_DIR + File.separator + fileVO.getFilePath() + File.separator + fileVO.getSaveName());
			}
		}
	}

	/* board 수정 시에 x한 파일들 db 데이터를 파일 id로 삭제 */
	@Override
	public void deleteFileListById(List<String> deletedFileIds) throws Exception {
		if (deletedFileIds != null 
			    && deletedFileIds.stream().anyMatch(id -> id != null && !id.trim().isEmpty())) {
			fileDAO.deleteFileListById(deletedFileIds);
		}
	}

	@Override
	public boolean existFile(String boardId) throws Exception {
		return fileDAO.existFile(boardId);
	}

	/* 삭제할 파일 리스트 정보 조회 */
	@Override
	public List<FileVO> selectFileListForDelete(List<String> boardIdList) throws Exception {
		return fileDAO.selectFileListForDelete(boardIdList);
	}

	/* 게시글 리스트에 담긴 첨부파일 db 정보 모두 삭제 */
	@Override
	public void deleteDBFileListByBoardId(List<String> boardIdList) throws Exception {
		fileDAO.deleteDBFileListByBoardId(boardIdList);
	}



	

}
