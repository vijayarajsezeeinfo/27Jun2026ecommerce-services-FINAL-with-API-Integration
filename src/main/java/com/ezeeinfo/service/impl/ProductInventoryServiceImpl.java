package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.ProductInventoryDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.ProductInventoryDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.ProductInventoryService;

@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {
	@Autowired
	ProductInventoryDAO productInventoryDAO;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(ProductInventoryServiceImpl.class);

	@Override
	public List<ProductInventoryDTO> getAllProductInventories(String namespaceCode) {
		// TODO Auto-generated method stub
		return productInventoryDAO.getAllProductInventories(namespaceCode);
	}

	@Override
	public ProductInventoryDTO getProductInventoryByCode(String code) {
		// TODO Auto-generated method stub
		return productInventoryDAO.getProductInventoryByCode(code);
	}

	@Override
	public ProductInventoryDTO update(ProductInventoryDTO productInventoryDTO, HttpServletRequest request) {

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
		productInventoryDTO.setUpdatedBy(loggedInUser);

		if (!productInventoryDTO.getNamespace().getCode().equals(loggedInUser.getNamespace().getCode())) {
			throw new ServiceException("EXCEPTION 403: ONLY SAME NAMESPACE USER CAN SAVE/MODIFY PRODUCT INVENTORY");
		}

		if (loggedInUser.getRole().getId() != 1) {
			throw new ServiceException("EXCEPTION 403: ONLY ADMIN CAN SAVE/MODIFY PRODUCT INVENTORY");
		}
		return productInventoryDAO.update(productInventoryDTO);
	}

}
