package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.NamespaceDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.NamespaceDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.NamespaceService;

@Service
public class NamespaceServiceImpl implements NamespaceService {

	@Autowired
	private NamespaceDAO namespaceDAO;
	@Autowired
	UserDAO userDAO;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private static Logger LOG = LoggerFactory.getLogger(NamespaceServiceImpl.class);

	@Override
	@SuppressWarnings("unchecked")
	public List<NamespaceDTO> getAllNamespaces() {

		List<NamespaceDTO> namespaces = (List<NamespaceDTO>) redisTemplate.opsForValue().get("ALL_NAMESPACES");
		if (namespaces != null) {
			LOG.info("getAllNamespaces retrieved from cache");
		}
		if (namespaces == null) {
			LOG.info("Hitting DB to getAllNamespaces");
			namespaces = namespaceDAO.getAllNamespaces();
			if (namespaces != null) {
				redisTemplate.opsForValue().set("ALL_NAMESPACES", namespaces);
			}
		}

		return namespaces;
	}

	@Override
	public NamespaceDTO getNamespaceByCode(String code) {

		NamespaceDTO namespace = (NamespaceDTO) redisTemplate.opsForValue().get(code);
		if (namespace != null) {
			LOG.info("getNamespaceByCode retrieved from cache");
		}
		if (namespace == null) {
			LOG.info("Hitting DB to getNamespaceByCode");
			namespace = namespaceDAO.getNamespaceByCode(code);
			if (namespace != null) {
				redisTemplate.opsForValue().set(code, namespace);
			}
		}

		return namespace;
	}

	@Override
	public NamespaceDTO update(NamespaceDTO namespaceDTO, HttpServletRequest request) {

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
		UserDTO updatedBy = new UserDTO();
		updatedBy.setId(loggedInUser.getId());
		namespaceDTO.setUpdatedBy(updatedBy);

		if (loggedInUser.getRole().getId() != 0) {
			throw new ServiceException("EXCEPTION 403: ONLY SUPER ADMIN HAS ACCESS TO SAVE OR MODIFY NAMESPACES");
		}

		NamespaceDTO updatedNamespace = namespaceDAO.update(namespaceDTO);
		if (updatedNamespace != null) {
			if (updatedNamespace.getActiveFlag() == 9 || updatedNamespace.getActiveFlag() < 2) {
				redisTemplate.opsForValue().set(updatedNamespace.getCode(), updatedNamespace);
			}
			// if the namespace is deleted, removing from namespaceCacheF
			if (updatedNamespace.getActiveFlag() == 2) {
				redisTemplate.delete(updatedNamespace.getCode());
			}
			redisTemplate.delete("ALL_NAMESPACES");
			LOG.info("Namespace cache updated and namespace list cache cleared");
		}

		return updatedNamespace;
	}
}