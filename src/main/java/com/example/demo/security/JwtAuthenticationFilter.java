package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/*
 * 1. ��û ������� Bearer ��ū�� ������ -> parseBearerToken()
 * 2. TokenProvider�� �̿��� ��ū ���� �� UsernamePasswordAuthenticationToken �ۼ�
 * 	  �� ������Ʈ���� ����� ���������� �����ϰ� SecurityContext�� ������ ����� ����Ͽ� ��û�� ���� ������ ������ ����� ���������� ���� �ֵ��� ��.
 * 	  (��û ó�� �������� ����ڰ� �����Ǿ����� ���γ� ������ ����ڰ� �������� �˱� ���ؼ�)
 * */
@Slf4j
@Component
// ��ū �Ľ� �� ����
// OncePerRequestFilter Ŭ���� ����� ���� ���� : �ϳ��� ��û �� �� ���� ����Ǵ� ����
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private TokenProvider tokenProvider;

	// doFilterInternal �������̵�
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws ServletException, IOException {
		try {
			// ��û���� ��ū ��������
			String token = parseBearerToken(request);
			log.info("Filter is running...");
			// ��ū �˻��ϱ�. JWT�̹Ƿ� �ΰ� ������ ��û���� �ʰ� ���� ����.
			if(token != null && !token.equalsIgnoreCase("null")) {
				// tokenProvider���� ��ū ����
				// userId ��������. ������ ��� ���� ó���ȴ�.
				String userId = tokenProvider.validateAndGetUserId(token);
				log.info("Authenticated user ID : " + userId);
				// ���� �Ϸ�, ����� �������� ����, SecurityContextHolder�� ����ؾ� ������ ����ڶ�� �����Ѵ�.
				AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userId,	// ������ ������� ����. ���ڿ��� �ƴϾ �ƹ��ų� ���� �� �ִ�. ���� UserDetails��� ������Ʈ�� �ִµ�, �츮�� �� �������.
						null,
						AuthorityUtils.NO_AUTHORITIES
				);
				// ������ ����� ���
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);	// ���������� �ְ�
				SecurityContextHolder.setContext(securityContext);	// �ٽ� SecurityContextHolder�� ���ؽ�Ʈ�� ����
																	// SecurityContextHolder�� ThreadLocal�� ���� (����� ������Ʈ�� �� �����庰�� ����ǰ�, �ҷ��� ���� �� �����忡�� ������ ������Ʈ�� �ҷ��� �� �ִ�.)
			}
		} catch(Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}
		filterchain.doFilter(request, response);
	}

	// ��û ������� Bearer ��ū�� ������
	private String parseBearerToken(HttpServletRequest request) {
		// Http ��û�� ����� �Ľ��� Bearer ��ū�� �����Ѵ�.
		String bearerToken = request.getHeader("Authorization");

		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
