package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.UserService;
import com.ezeeinfo.util.PasswordUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CacheManager cacheManager;

	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	@SuppressWarnings("unchecked")
	public List<UserDTO> getAllUsers(String namespaceCode) {

		Cache<String, List> cache = cacheManager.getCache("userListCache", String.class, List.class);
		List<UserDTO> users = (List<UserDTO>) cache.get(namespaceCode);
		if (users != null) {
			LOG.info("getAllUsers retrieved from cache");
		}
		if (users == null) {
			LOG.info("Hitting DB to getAllUsers");
			users = userDAO.getAllUsers(namespaceCode);
			if (users != null) {
				cache.put(namespaceCode, users);
			}
		}

		return users;
	}

	@Override
	public UserDTO getUserByCode(String code) {

		Cache<String, UserDTO> cache = cacheManager.getCache("userCache", String.class, UserDTO.class);
		UserDTO user = cache.get(code);
		if (user != null) {
			LOG.info("getUserByCode retrieved from cache");
		}
		if (user == null) {
			LOG.info("Hitting DB to getUserByCode");
			user = userDAO.getUserByCode(code);
			if (user != null) {
				cache.put(code, user);
			}
		}

		return user;
	}

	@Override
	public UserDTO update(UserDTO userDTO, HttpServletRequest request) {

		// HASHING PASSWORD
		userDTO.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));

		// SETTING LOGGED IN USER
		AuthDTO authDTO = (AuthDTO) request.getAttribute("auth");
		if (authDTO == null) {
			LOG.info("Login not done. So AuthDTO is null");
			throw new ServiceException("Please Login First");
		}
		if (authDTO.getUser().getId() == null) {
			LOG.info("Login not done. So AuthDTO is null");
			throw new ServiceException("Please Login First");
		}
		UserDTO loggedInUser = userDAO.getUser(authDTO.getUser().getId());
		userDTO.setUpdatedBy(loggedInUser);

		LOG.info("Input USER in USER SERVICE IMPL after Setting updated by : {}", userDTO);
		if (!userDTO.getCode().equalsIgnoreCase("null")) {
			UserDTO dbUser = userDAO.getUserByCodeInternal(userDTO.getCode());
			LOG.info("DB User {}", dbUser);

			// NOBODY CAN CHANGE NAMESPACE FOR EXISTING USER
			if (!dbUser.getNamespace().getCode().equalsIgnoreCase(userDTO.getNamespace().getCode())) {
				throw new ServiceException("EXCEPTION 400: CANNOT CHANGE NAMESPACE FOR EXISTING USER");
			}

			// ONLY ADMIN CAN CHANGE THE USER ROLE
			if (userDTO.getRole().getId() != dbUser.getRole().getId()) {
				if (userDTO.getUpdatedBy().getRole().getId() != 1) {
					throw new ServiceException("EXCEPTION 403: ONLY ADMIN CAN CHANGE THE ROLE");
				}
			}
		}

		// OTHER NAMESPACE USER CANNOT MAKE CHANGE
		if (!userDTO.getNamespace().getCode().equals(loggedInUser.getNamespace().getCode())) {
			throw new ServiceException("EXCEPTION 403: ONLY SAME NAMESPACE USER CAN MAKE CHANGES");
		}

		// UPDATING
		UserDTO updatedUser = userDAO.update(userDTO);
		if (updatedUser != null) {

			// STORING IN userCache
			Cache<String, UserDTO> userCache = cacheManager.getCache("userCache", String.class, UserDTO.class);
			if (updatedUser.getActiveFlag() == 9 || updatedUser.getActiveFlag() < 2) {
				userCache.put(updatedUser.getCode(), updatedUser);
			}
			// if the user is deleted, removing from userCache
			if (updatedUser.getActiveFlag() == 2) {
				userCache.remove(updatedUser.getCode());
			}
			// REMOVING userListCache AFTER UPDATING USER
			Cache<String, List> userListCache = cacheManager.getCache("userListCache", String.class, List.class);
			userListCache.remove(updatedUser.getNamespace().getCode());
			LOG.info("User cache updated and user list cache cleared");
		}

		return updatedUser;
	}
}