package com.ezeeinfo.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ezeeinfo.dto.UserDTO;

public interface UserService {

	List<UserDTO> getAllUsers(String namespaceCode);

	UserDTO getUserByCode(String code) throws Exception;

	UserDTO update(UserDTO userDTO, HttpServletRequest request);
}
