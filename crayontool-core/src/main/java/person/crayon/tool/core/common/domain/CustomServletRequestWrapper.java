package person.crayon.tool.core.common.domain;

import cn.hutool.core.io.IoUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @author Crayon
 * @date 2022/6/1 17:23
 * 自定义Wrapper
 * 解决request中InputStream只能读取一次的问题
 */
public class CustomServletRequestWrapper extends HttpServletRequestWrapper {
    private final byte[] body;

    /**
     * 重写构造器，读取出流的数据保存在body属性中，后续读取从body中读取，从而达到流多次读取的目的
     * @param request 请求对象
     * @throws IOException IO异常
     */
    public CustomServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream is = request.getInputStream();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        IoUtil.copy(is, os);
        body = os.toByteArray();
    }

    /**
     * 重写获取InputStream的方法，内部通过body构造新的输入流返回，从而能够让流多次被读取
     * @return ServletInputStream
     */
    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return new ByteArrayInputStream(body).read();
            }
        };
    }

    /**
     * 重写获取Reader的方法
     * @return BufferedReader
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
