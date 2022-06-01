package person.crayon.tool.core.common.domain;

import cn.hutool.core.io.IoUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Crayon
 * @date 2022/6/1 17:23
 * 自定义Wrapper
 * 解决request中InputStream只能读取一次的问题
 */
public class CustomServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    public CustomServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream is = request.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IoUtil.copy(is, os);
        body = os.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        final InputStream is = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return is.read();
            }
        };
    }
}
