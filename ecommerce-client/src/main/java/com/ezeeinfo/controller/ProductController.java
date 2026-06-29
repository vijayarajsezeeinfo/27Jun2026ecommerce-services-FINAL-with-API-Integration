package com.ezeeinfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ezeeinfo.controller.io.ProductIO;
import com.ezeeinfo.service.ProductService;

@RestController
@RequestMapping("/feign/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@RequestMapping(value = "/{namespaceCode}", method = RequestMethod.GET)
	public List<ProductIO> getAllProducts(@PathVariable("namespaceCode") String namespaceCode) {
		return productService.getAllProducts(namespaceCode);
	}

	@RequestMapping(value = "/code/{code}", method = RequestMethod.GET)
	public ProductIO getProductByCode(@PathVariable("code") String code) {
		return productService.getProductByCode(code);
	}

	// save or update or soft-delete in ecommerce-services
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ProductIO update(@RequestHeader("Authorization") String token, @RequestBody ProductIO productIO) {
		return productService.update(token, productIO);
	}

	// Get from ecommerce-services and save or update or soft-delete in this client
	@RequestMapping(value = "/save/{namespaceCode}", method = RequestMethod.GET)
	public List<ProductIO> save(@PathVariable("namespaceCode") String namespaceCode) {
		return productService.save(namespaceCode);
	}

}
