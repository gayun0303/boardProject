package egovframework.board.main.service.impl;

import java.util.UUID;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import egovframework.board.main.service.BoardVO;
import egovframework.board.main.service.UserService;
import egovframework.board.main.service.UserVO;

@Service("userService")
public class UserServiceImpl extends EgovAbstractServiceImpl implements UserService {
	
	@Resource(name = "userMapper")
	private UserMapper userDAO;

	@Override
	public UserVO selectUser(BoardVO vo) throws Exception {
		
		/* 유저 조회, 없으면 생성 */
		UserVO user = userDAO.selectUser(vo.getName());
		if(user == null) {
			String userId = UUID.randomUUID().toString();
			user = new UserVO(userId, vo.getName());
			userDAO.insertUser(user);
		}
		return user;
	}

}
