package com.ezeeinfo.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ezeeinfo.dto.AuthDTO;
import com.ezeeinfo.dto.UserDTO;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			String auth = request.getHeader("Authorization");
			LOG.info("Authorization Header : {}", auth);
			if (auth != null && auth.startsWith("Bearer ")) {
				String token = auth.substring(7);
				token = token.replace("\"", "");

				UserDTO userDTO = JwtUtil.getUserDTO(token);

				AuthDTO authDTO = new AuthDTO();
				authDTO.setUser(userDTO);

				request.setAttribute("auth", authDTO);
			}
			filterChain.doFilter(request, response);
		}
		catch (Exception e) {
			if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
				LOG.info("EXCEPTION 401: UNAUTHORIZED. Exception in JWT FILTER : {}", e);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Your session has expired. Please login again.");
				return;
			}
			LOG.info("Exception in JWT FILTER : {}", e);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid Credentials");
			return;

		}

	}

}
