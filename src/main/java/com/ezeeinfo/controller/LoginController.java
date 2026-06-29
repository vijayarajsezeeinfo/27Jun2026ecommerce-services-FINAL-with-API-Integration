package com.ezeeinfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.controller.io.LoginRequestIO;
import com.ezeeinfo.controller.io.LoginResponseIO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.util.JwtUtil;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Autowired
	UserDAO userDAO;

	@RequestMapping(method = RequestMethod.POST)
	public LoginResponseIO login(@RequestBody LoginRequestIO request) {
		UserDTO userDTO = userDAO.login(request.getUserCode(), request.getUsername(), request.getPassword(), request.getNamespaceCode());

		String token = JwtUtil.generateToken(userDTO);

		LoginResponseIO loginResponseIO = new LoginResponseIO();
		loginResponseIO.setToken(token);
		return loginResponseIO;
	}

}
