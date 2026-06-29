package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.CartItemDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.CartItemDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	CartItemDAO cartItemDAO;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(CartItemServiceImpl.class);

	@Override
	public List<CartItemDTO> getAllCartItems(String namespaceCode) {
		return cartItemDAO.getAllCartItems(namespaceCode);
	}

	@Override
	public CartItemDTO getCartItemByCode(String code) {
		return cartItemDAO.getCartItemByCode(code);
	}

	@Override
	public CartItemDTO update(CartItemDTO cartItemDTO, HttpServletRequest request) {

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
		cartItemDTO.setUpdatedBy(loggedInUser);

		UserDTO cartOwningUser = userDAO.getUserByCodeInternal(cartItemDTO.getCart().getUser().getCode());

		if (loggedInUser.getId() != cartOwningUser.getId()) {
			throw new ServiceException("EXCEPTION 403: ONLY CART OWNER CAN ADD CART ITEM");
		}

		return cartItemDAO.update(cartItemDTO);
	}
}