import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 测试
 * 
 * @author zengxm<http://github.com/JumperYu>
 *
 * @date 2016年3月17日
 */
public class HelloServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		out.write("Welcome my first servlet.");
		out.flush();
	}
}
