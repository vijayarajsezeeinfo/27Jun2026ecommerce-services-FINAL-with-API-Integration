package com.ezeeinfo.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezeeinfo.dao.OrderDAO;
import com.ezeeinfo.dao.UserDAO;
import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.OrderItemDTO;
import com.ezeeinfo.dto.OrderRequestDTO;
import com.ezeeinfo.dto.UserDTO;
import com.ezeeinfo.exception.ServiceException;
import com.ezeeinfo.service.OrderRequestService;

@Service

public class OrderRequestServiceImpl implements OrderRequestService {

	@Autowired
	OrderDAO orderDAO;
	@Autowired
	UserDAO userDAO;

	private static final Logger LOG = LoggerFactory.getLogger(OrderRequestServiceImpl.class);

	@Override
	public OrderRequestDTO getOrderByCode(String code) {
		return orderDAO.getOrderByCode(code);
	}

	@Override
	public OrderRequestDTO update(OrderRequestDTO orderRequestDTO, HttpServletRequest request) {
		LOG.info("OrderRequest DTO : {}", orderRequestDTO);
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

		// SETTING UPDATED BY FOR ORDERS
		orderRequestDTO.getOrder().setUpdatedBy(loggedInUser);

		// SETTING UPDATED BY FOR ORDER ITEMS
		for (OrderItemDTO item : orderRequestDTO.getOrderItems()) {
			item.setUpdatedBy(loggedInUser);
			if (!item.getNamespace().getCode().equalsIgnoreCase(orderRequestDTO.getOrder().getNamespace().getCode())) {
				LOG.info("EXCEPTION 403: ORDER NAMESPACE AND ORDER ITEM NAMESPACE NOT MATCH. Item Namespace : {}, Order Namespace : {}", item.getNamespace().getCode(), orderRequestDTO.getOrder().getNamespace().getCode());
				throw new ServiceException("EXCEPTION 403: ORDER NAMESPACE AND ORDER ITEM NAMESPACE NOT MATCH");
			}
		}

		// SETTING UPDATED BY FOR PAYMENTS
		orderRequestDTO.getPayment().setUpdatedBy(loggedInUser);

		if (!orderRequestDTO.getOrder().getNamespace().getCode().equalsIgnoreCase(orderRequestDTO.getOrder().getUser().getNamespace().getCode())) {
			LOG.info("EXCEPTION 403: ORDER NAMESPACE AND USER NAMESPACE NOT MATCH. Order Namespace : {}, User Namespace : {}", orderRequestDTO.getOrder().getNamespace().getCode(), orderRequestDTO.getOrder().getUser().getNamespace().getCode());
			throw new ServiceException("EXCEPTION 403: ORDER NAMESPACE AND USER NAMESPACE NOT MATCH");
		}

		if (!loggedInUser.getCode().equalsIgnoreCase(orderRequestDTO.getOrder().getUser().getCode())) {
			LOG.info("EXCEPTION 403: ONLY VALID USER CAN ORDER. Logged In User : {}. Order's User : {}", loggedInUser.getCode(), orderRequestDTO.getOrder().getUser().getCode());
			throw new ServiceException("EXCEPTION 403: LOGGED IN USER AND ORDER USER NOT MATCH");
		}

		return orderDAO.update(orderRequestDTO);
	}

	@Override
	public List<OrderRequestDTO> getAllOrders(String namespaceCode) {

		return orderDAO.getAllOrders(namespaceCode);
	}

}
