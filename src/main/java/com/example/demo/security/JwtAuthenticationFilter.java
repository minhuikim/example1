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
 * 1. 요청 헤더에서 Bearer 토큰을 가져옴 -> parseBearerToken()
 * 2. TokenProvider를 이용해 토큰 인증 후 UsernamePasswordAuthenticationToken 작성
 * 	  이 오브젝트에서 사용자 인증정보를 저장하고 SecurityContext에 인증된 사용자 등록하여 요청이 끝날 때까지 서버가 사용자 인증정보를 갖고 있도록 함.
 * 	  (요청 처리 과정에서 사용자가 인증되었는지 여부나 인증된 사용자가 누구인지 알기 위해서)
 * */
@Slf4j
@Component
// 토큰 파싱 및 인증
// OncePerRequestFilter 클래스 상속해 필터 생성 : 하나의 요청 당 한 번만 실행되는 필터
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private TokenProvider tokenProvider;

	// doFilterInternal 오버라이딩
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterchain) throws ServletException, IOException {
		try {
			// 요청에서 토큰 가져오기
			String token = parseBearerToken(request);
			log.info("Filter is running...");
			// 토큰 검사하기. JWT이므로 인가 서버에 요청하지 않고도 검증 가능.
			if(token != null && !token.equalsIgnoreCase("null")) {
				// tokenProvider에서 토큰 인증
				// userId 가져오기. 위조된 경우 예외 처리된다.
				String userId = tokenProvider.validateAndGetUserId(token);
				log.info("Authenticated user ID : " + userId);
				// 인증 완료, 사용자 인증정보 저장, SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
				AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userId,	// 인증된 사용자의 정보. 문자열이 아니어도 아무거나 넣을 수 있다. 보통 UserDetails라는 오브젝트를 넣는데, 우리는 안 만들었음.
						null,
						AuthorityUtils.NO_AUTHORITIES
				);
				// 인증된 사용자 등록
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);	// 인증정보를 넣고
				SecurityContextHolder.setContext(securityContext);	// 다시 SecurityContextHolder에 컨텍스트로 저장
																	// SecurityContextHolder는 ThreadLocal에 저장 (저장된 오브젝트는 각 스레드별로 저장되고, 불러올 때도 내 스레드에서 저장한 오브젝트만 불러올 수 있다.)
			}
		} catch(Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}
		filterchain.doFilter(request, response);
	}

	// 요청 헤더에서 Bearer 토큰을 가져옴
	private String parseBearerToken(HttpServletRequest request) {
		// Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴한다.
		String bearerToken = request.getHeader("Authorization");

		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
}
