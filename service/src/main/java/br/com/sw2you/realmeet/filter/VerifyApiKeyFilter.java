package br.com.sw2you.realmeet.filter;

import static org.apache.commons.lang3.StringUtils.isBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.sw2you.realmeet.domain.entity.Client;
import br.com.sw2you.realmeet.domain.repository.ClientRepository;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

public class VerifyApiKeyFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyApiKeyFilter.class);
    public static final String HEADER_API_KEY = "api-key";

    private final ClientRepository clientRepository;

    public VerifyApiKeyFilter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) servletRequest;
        var httpResponse = (HttpServletResponse) servletResponse;

        var apiKey = httpRequest.getHeader(HEADER_API_KEY);

        if (!isBlank(apiKey) && isValidApiKey(apiKey)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            sendUnauthorizedError(httpResponse, apiKey);
        }
    }

    private boolean isValidApiKey(String apiKey) {
        return clientRepository
                .findById(apiKey)
                .filter(Client::getActive)
                .stream()
                .peek(client -> LOG.info("Valid API Key: {} ({})", client.getApiKey(), client.getDescription()))
                .findFirst()
                .isPresent();
    }

    private void sendUnauthorizedError(HttpServletResponse response, String apiKey) throws IOException {
        var errorMessage = isBlank(apiKey) ? "API Key is missing" : "API Key is invalid";
        LOG.error(errorMessage);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentLength(errorMessage.length());
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);

        try (Writer out = response.getWriter()) {
            out.write(errorMessage);
        }
    }

}
