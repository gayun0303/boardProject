package egovframework.board.main.service.impl;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.board.main.service.BoardVO;
import egovframework.board.main.service.UserVO;

@Mapper("userMapper")
public interface UserMapper {
	UserVO selectUser(String name) throws Exception;
	
	void insertUser(UserVO userVO) throws Exception;

}
