package com.asd.prirserver.config.JWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Klasa obsługująca błędy autoryzacji JWT
 * @author Dawid Kańtoch
 * @version 1.0
 * @since 1.0
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint
{
    private final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Metoda obsługuąca błędy autoryzacji
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException
    {
        logger.error("Unauthorized error: {}", e.getMessage());
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
