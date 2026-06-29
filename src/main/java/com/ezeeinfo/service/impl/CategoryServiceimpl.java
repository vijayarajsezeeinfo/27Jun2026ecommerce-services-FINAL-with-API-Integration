package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.CategoryDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.CategoryDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService {

	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceimpl.class);

	@Override
	public List<CategoryDTO> getAllCategories(String namespaceCode) {
		// TODO Auto-generated method stub
		return categoryDAO.getAllCategories(namespaceCode);
	}

	@Override
	public CategoryDTO getCategoryByCode(String code) {
		// TODO Auto-generated method stub
		return categoryDAO.getCategoryByCode(code);
	}

	@Override
	public CategoryDTO update(CategoryDTO categoryDTO, HttpServletRequest request) {
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
		categoryDTO.setUpdatedBy(loggedInUser);

		if (!loggedInUser.getNamespace().getCode().equalsIgnoreCase(categoryDTO.getNamespace().getCode())) {
			throw new ServiceException("EXCEPTION 403: ONLY SAME NAMESPACE USER CAN SAVE/MODIFY CATEGORY");
		}

		if (loggedInUser.getRole().getId() != 1) {
			throw new ServiceException("EXCEPTION 403: ONLY ADMIN CAN SAVE/MODIFY CATEGORY");
		}

		return categoryDAO.update(categoryDTO);
	}

}
