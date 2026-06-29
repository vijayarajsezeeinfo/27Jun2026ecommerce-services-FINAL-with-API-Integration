package com.ezeeinfo.controller.io;

import lombok.Data;

@Data
public class LoginRequestIO {

	private String userCode;
	private String username;
	private String password;
	private String namespaceCode;
}
