package com.ezeeinfo.service;

import java.util.List;

import com.ezeeinfo.controller.io.ProductIO;

public interface ProductService {

	List<ProductIO> getAllProducts(String namespaceCode);

	ProductIO getProductByCode(String code);

	ProductIO update(String token, ProductIO productIO);

	List<ProductIO> save(String namespaceCode);

}
