package xzh.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@RequestMapping(method = RequestMethod.GET)
	@ApiOperation(value = "getUserList", notes = "用户列表")
	public List<String> getUserList() {
		List<String> users = new ArrayList<String>();
		users.add("Tom");
		users.add("CongCong");
		return users;
	}

	@RequestMapping(value = "test/*", method = RequestMethod.GET, consumes = MediaType.APPLICATION_XML_VALUE)
	@ResponseBody
	public String testXML(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("=============");
		System.out.println(request.getRequestURI());
		System.out.println("=============");
		return readFile("/mockfile/UserResponse.xml");
	}

	private String readFile(String fileName) {
		try (InputStream in = getClass().getResourceAsStream(fileName)) {
			return StreamUtils.copyToString(in, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

	}
}
