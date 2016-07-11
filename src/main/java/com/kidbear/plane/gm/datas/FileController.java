package com.kidbear.plane.gm.datas;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.kidbear.plane.net.http.HttpServer;

@Controller
@RequestMapping(value = "/file")
public class FileController {

	@RequestMapping(value = "/hotfix")
	public void hotfix(HttpServletRequest request, Model model) {

	}

	@RequestMapping(value = "/fileupload")
	@ResponseBody
	public String fileUpload(String filename, String fileversion,
			HttpServletRequest request,
			@RequestParam("file") MultipartFile file, ModelMap model)
			throws Throwable {
		String version = request.getParameter("version");
		if (filename == null || filename.length() == 0) {
			filename = file.getOriginalFilename();
		}
		if (filename.length() == 0) {
			return "failed";
		}
		filename = version + "." + filename;
		boolean flag = false;
		try {
			// rpc调用文件服务器上传更新文件
			URL serviceUrl = new URL("http://" + HttpServer.fileServer + ":"
					+ HttpServer.filePort);
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Rpc-Type", "file");
			JsonRpcHttpClient client = new JsonRpcHttpClient(serviceUrl,
					headers);
			flag = client.invoke("upload", new Object[] { file, filename },
					Boolean.class);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		if (flag) {
			return "success";
		} else {
			return "fail";
		}
	}
}
