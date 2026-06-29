package com.ezeeinfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.controller.io.PaymentIO;
import com.ezeeinfo.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

	@Autowired
	PaymentService paymentService;
	@Autowired
	OrderRequestController orderRequestController;

	@RequestMapping(value = "/{namespaceCode}", method = RequestMethod.GET)
	public List<PaymentIO> getAllPayments(@PathVariable("namespaceCode") String namespaceCode) {
		return paymentService.getAllPayments(namespaceCode).stream().map(dto -> orderRequestController.paymentDTOToIO(dto)).toList();
	}

	@RequestMapping(value = "/code/{paymentCode}", method = RequestMethod.GET)
	public PaymentIO getPaymentByCode(@PathVariable("paymentCode") String paymentCode) throws Exception {
		return orderRequestController.paymentDTOToIO(paymentService.getPaymentByCode(paymentCode));
	}
}
