package hello.LoggerFilter;

/**
 * <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-io -->
 *         <dependency>
 *             <groupId>org.apache.commons</groupId>
 *             <artifactId>commons-io</artifactId>
 *             <version>1.3.2</version>
 *         </dependency>
 */
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component("LoggerFilter")
public class LoggerFilter implements Filter {
    private static class ByteArrayServletStream extends ServletOutputStream {

        ByteArrayOutputStream baos;

        ByteArrayServletStream(ByteArrayOutputStream baos) {
            this.baos = baos;
        }

        public void write(int param) throws IOException {
            baos.write(param);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }

    private static class ByteArrayPrintWriter {

        private ByteArrayOutputStream baos = new ByteArrayOutputStream();

        private PrintWriter pw = new PrintWriter(baos);

        private ServletOutputStream sos = new ByteArrayServletStream(baos);

        public PrintWriter getWriter() {
            return pw;
        }

        public ServletOutputStream getStream() {
            return sos;
        }

        byte[] toByteArray() {
            return baos.toByteArray();
        }
    }

    public void destroy() {
        // Nothing to do
    }

    private HttpServletResponse getHttpServletResponse(final ByteArrayPrintWriter pw, ServletResponse response) {
        HttpServletResponse wrappedResp = new HttpServletResponseWrapper((HttpServletResponse) response) {
            public PrintWriter getWriter() {
                return pw.getWriter();
            }

            public ServletOutputStream getOutputStream() {
                return pw.getStream();
            }

        };

        return wrappedResp;
    }

    private void doCustomFilter(String name, ServletRequest request, ServletResponse response,
                                FilterChain chain) throws IOException, ServletException {
        ResettableStreamHttpServletRequest wrappedRequest
                = new ResettableStreamHttpServletRequest((HttpServletRequest) request);
        String body = IOUtils.toString(wrappedRequest.getReader());
        System.out.println("___REQUEST___");
        System.out.println(((HttpServletRequest) request).getMethod());
        System.out.println(((HttpServletRequest) request).getMethod());
        System.out.println(((HttpServletRequest) request).getRequestURI());
        System.out.println("REQUEST -> " + body);
        System.out.println("___REQUEST___");
        wrappedRequest.resetInputStream();

        final ByteArrayPrintWriter pw = new ByteArrayPrintWriter();
        HttpServletResponse wrappedResp = getHttpServletResponse(pw, response);

        chain.doFilter(wrappedRequest, wrappedResp);

        if (((HttpServletRequest) request).getMethod().equalsIgnoreCase("POST")
                &&((HttpServletRequest) request).getRequestURI().equalsIgnoreCase("/test")
                )
            System.out.println("DONE");

        System.out.println("___RESPONSE___");
        System.out.println("RESPONSE -> " + getCustomResponseBody(pw, response));
        System.out.println("___RESPONSE___");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        if (((HttpServletRequest) request).getMethod().equalsIgnoreCase("post")
                && ((HttpServletRequest) request).getRequestURI().equalsIgnoreCase("/login1")) {
            doCustomFilter("1", request, response, chain);
        } else {
            chain.doFilter(request, response);
        }

    }

    private String getCustomResponseBody(final ByteArrayPrintWriter pw, ServletResponse response) throws IOException {
        byte[] bytes = pw.toByteArray();
        response.getOutputStream().write(bytes);

        return new String(bytes);
    }

    public void init(FilterConfig arg0) throws ServletException {
        // Nothing to do
    }

    private static class ResettableStreamHttpServletRequest extends
            HttpServletRequestWrapper {

        private byte[] rawData;
        private HttpServletRequest request;
        private ResettableServletInputStream servletStream;

        public ResettableStreamHttpServletRequest(HttpServletRequest request) {
            super(request);
            this.request = request;
            this.servletStream = new ResettableServletInputStream();
        }


        public void resetInputStream() {
            servletStream.stream = new ByteArrayInputStream(rawData);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return servletStream;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            if (rawData == null) {
                rawData = IOUtils.toByteArray(this.request.getReader(), StandardCharsets.UTF_8.name());
                servletStream.stream = new ByteArrayInputStream(rawData);
            }
            return new BufferedReader(new InputStreamReader(servletStream));
        }


        private class ResettableServletInputStream extends ServletInputStream {

            private InputStream stream;

            @Override
            public int read() throws IOException {
                return stream.read();
            }

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
        }
    }


}