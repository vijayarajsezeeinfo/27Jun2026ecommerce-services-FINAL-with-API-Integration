package com.ezeeinfo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ezeeinfo.config.DBConfig;
import com.ezeeinfo.controller.io.ProductIO;
import com.ezeeinfo.exception.ServiceException;

@Repository
public class ProductDAO {

	private static final Logger LOG = LoggerFactory.getLogger(ProductDAO.class);

	public List<ProductIO> save(List<ProductIO> productIOs) {
		String sql = "CALL EZEE_SP_PRODUCT_IUD( ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection connection = DBConfig.getInstance().getConnection();
				CallableStatement statement = connection.prepareCall(sql);) {
			for (ProductIO p : productIOs) {
				statement.setString(1, p.getCode());
				statement.setString(2, p.getName());
				statement.setString(3, p.getDescription());
				statement.setDouble(4, p.getPrice());
				statement.setString(5, p.getBrand().getCode());
				statement.setString(6, p.getCategory().getCode());
				statement.setString(7, p.getNamespace().getCode());
				statement.setInt(8, p.getActiveFlag());

				statement.registerOutParameter(9, Types.INTEGER);
				statement.execute();
				LOG.info("EZEE_SP_PRODUCT_IUD successfully executed. Data stored in Ecommerce Client DB");
			}

		} catch (SQLException e) {
			LOG.info("SQLException while executing EZEE_SP_PRODUCT_IUD", e);
			throw new ServiceException("SQLException while executing EZEE_SP_PRODUCT_IUD");
		}
		return productIOs;
	}
}
