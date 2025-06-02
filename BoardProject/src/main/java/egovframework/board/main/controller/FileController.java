package egovframework.board.main.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/file")
public class FileController {
	
	private static Logger Logger = LoggerFactory.getLogger(FileController.class);
	
	private static final String UPLOAD_DIR = "C:/upload";
	
	
	
	@RequestMapping("/download.do")
	public void downloadFile(@RequestParam("filePath") String folder,
							@RequestParam("saveName") String saveName,
							@RequestParam("originalName") String originalName,
							HttpServletRequest request,
                            HttpServletResponse response
			) throws Exception {
		
		/* 파일 경로 설정 */
		String filePath = UPLOAD_DIR + File.separator + folder + File.separator + saveName;
		
		Logger.debug("FileController:: downloadFile {}", filePath);
		
		File file = new File(filePath);
		
		/* 파일 존재 여부 체크 */
        if (!file.exists()) {
        	Logger.error("파일이 존재하지 않습니다: {}", filePath);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일을 찾을 수 없습니다.");
            return;
        }
        
		/* 원본 파일명 인코딩 */
        String encodedFilename = URLEncoder.encode(originalName, "UTF-8").replaceAll("\\+", "%20");

        
        /*파일 다운로드 응답 헤더 설정*/
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFilename + "\"");
        response.setContentLengthLong((Long) file.length());

		/* 파일 스트림 처리 */
        try (
        	BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        	BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {

			/* 1024byte 단위로 읽고 출력 스트림으로 전송 후 flush() 클라이언트로 보냄 */
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = in.read(buffer)) != -1) {
	            out.write(buffer, 0, bytesRead);
	        }
	        out.flush();
	        
        } catch (IOException e) {
            Logger.error("파일 다운로드 중 오류 발생", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "파일 다운로드 실패");
        }
	}
}
