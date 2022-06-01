package person.crayon.tool.core.captcha.check.filter;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONUtil;
import person.crayon.tool.core.common.domain.CustomServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

public class CheckCaptchaFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequestWrapper wrapper = new CustomServletRequestWrapper((HttpServletRequest) servletRequest);

        filterChain.doFilter(wrapper, servletResponse);
    }

}
