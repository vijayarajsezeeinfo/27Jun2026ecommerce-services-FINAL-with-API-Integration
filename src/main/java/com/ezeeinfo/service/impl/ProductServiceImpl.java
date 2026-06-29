package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.ProductDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.ProductDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductDAO productDAO;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Override
	public List<ProductDTO> getAllProducts(String namespaceCode) {
		// TODO Auto-generated method stub
		return productDAO.getAllProducts(namespaceCode);
	}

	@Override
	public ProductDTO getProductByCode(String code) {
		// TODO Auto-generated method stub
		return productDAO.getProductByCode(code);
	}

	@Override
	public ProductDTO update(ProductDTO productDTO, HttpServletRequest request) {

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
		productDTO.setUpdatedBy(loggedInUser);
		LOG.info("product dto: {}", productDTO);

		if (!productDTO.getNamespace().getCode().equals(loggedInUser.getNamespace().getCode())) {
			throw new ServiceException("EXCEPTION 403: ONLY SAME NAMESPACE USER CAN SAVE/MODIFY PRODUCT");
		}

		if (loggedInUser.getRole().getId() != 1) {
			throw new ServiceException("EXCEPTION 403: ONLY ADMIN CAN SAVE/MODIFY PRODUCT");
		}
		return productDAO.update(productDTO);
	}

	@Override
	public List<ProductDTO> getProductsByNamePriceAndNamespace(String name, Double price, String namespaceCode) {
		// TODO Auto-generated method stub
		LOG.info("Getting products by NAME, PRICE and NAMESPACE");
		return productDAO.getProductsByNamePriceAndNamespace(name, price, namespaceCode);
	}

}
