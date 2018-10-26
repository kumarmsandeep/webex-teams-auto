package com.webexteams.auto.filter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.util.ContentCachingResponseWrapper;

import com.jcabi.manifests.Manifests;
import com.webexteams.auto.utils.ManifestUtil;

@SuppressWarnings("serial")
public class SwaggerUIRebrandFilter implements Filter {

	private static final String DATA_IMAGE_PNG_BASE64_PREFIX = "data:image/png;base64,";
	private Map<String, Map<String, String>> replaceMap;

	public SwaggerUIRebrandFilter() {
		this(readOrgNameFromManifest(), getBase64LogoData());
	}

	public SwaggerUIRebrandFilter(String name, String logoData) {
		this(name, logoData, logoData, logoData);
	}

	public SwaggerUIRebrandFilter(final String name, final String logo, final String icon1616, final String icon3232) {

		this.replaceMap = new HashMap<String, Map<String, String>>() {
			{
				put("swagger-ui.html", new HashMap<String, String>() {
					{
						put("<title>Swagger UI</title>", "<title>" + ManifestUtil.getProjectTitle() + "</title>");
						put("webjars/springfox-swagger-ui/favicon-16x16.png?v=2.9.2", icon1616);
						put("webjars/springfox-swagger-ui/favicon-32x32.png?v=2.9.2", icon3232);
					}
				});
				put("swagger-ui-standalone-preset.js", new HashMap<String, String>() {
					{
						put(getBase64SwaggerLogo(), logo);
						put("\"Swagger UI\"", "\"\"");
						put("\"swagger\"", "\"" + ((name != null) ? (name) : ("")) + "\"");
					}
				});
				put("swagger-ui.css", new HashMap<String, String>() {
					{
						put("background-color:#89bf04", "");
						put("#547f00", "blue");
						put(".swagger-ui .topbar a{font-size:1.5em;font-weight:700;-webkit-box-flex:1;-ms-flex:1;flex:1;max-width:300px;text-decoration:none;font-family:Titillium Web,sans-serif;color:#fff}",
								".swagger-ui .topbar a{font-size:1.5em;font-weight:700;-webkit-box-flex:1;-ms-flex:1;flex:1;max-width:300px;text-decoration:none;font-family:Titillium Web,sans-serif;color:black}");
					}
				});
			}
		};
	}

	private String getBase64SwaggerLogo() {
		return DATA_IMAGE_PNG_BASE64_PREFIX
				+ "iVBORw0KGgoAAAANSUhEUgAAAB4AAAAeCAMAAAAM7l6QAAAAYFBMVEUAAABUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwBUfwB0lzB/n0BfhxBpjyC0x4////+qv4CJp1D09++ft3C/z5/K16/U379UfwDf58/q79+Ur2D2RCk9AAAAHXRSTlMAEEAwn9//z3Agv4/vYID/////////////////UMeji1kAAAD8SURBVHgBlZMFAoQwDATRxbXB7f+vPKnlXAZn6k2cf3A9z/PfOC8IIYni5FmmABM8FMhwT17c9hnhiZL1CwvEL1tmPD0qSKq6gaStW/kMXanVmAVRDUlH1OvuuTINo6k90Sxf8qsOtF6g4ff1osP3OnMcV7d4pzdIUtu1oA4V0DZoKmxmlEYvtDUjjS3tmKmqB+pYy8pD1VPf7jPE0I40HHcaBwnue6fGzgyS5tXIU96PV7rkDWHNLV0DK4FkoKmFpN5oUnvi8KoeA2/JXsmXQuokx0siR1G8tLkN6eB9sLwJp/yymcyaP/TrP+RPmbMMixcJVgTR1aUZ93oGXsgXQAaG6EwAAAAASUVORK5CYII=";
	}

	public static String readOrgNameFromManifest() {
		return ManifestUtil.getOrgName();
	}

	public static String getBase64LogoData() {
		String logoData = DATA_IMAGE_PNG_BASE64_PREFIX;
		try {
			byte[] imageData = Files.readAllBytes(Paths.get("static/favicon.ico"));
			byte[] encoded = Base64.getEncoder().encode(imageData);
			logoData = logoData + new String(encoded);
		} catch (IOException e) {

		}
		return logoData;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(res);

		chain.doFilter(request, responseWrapper);

		if (replaceMap != null) {
			for (Map.Entry<String, Map<String, String>> entry : replaceMap.entrySet()) {
				if (req.getRequestURL().toString().contains(entry.getKey())) {
					Map<String, String> map = entry.getValue();
					if (map != null && map.size() > 0) {
						byte[] contentAsByteArray = responseWrapper.getContentAsByteArray();
						String str = new String(contentAsByteArray, "utf-8");
						for (Entry<String, String> innerEntry : map.entrySet()) {
							str = str.replace(innerEntry.getKey(), innerEntry.getValue());
						}
						responseWrapper.resetBuffer();
						responseWrapper.getWriter().write(str);
					}
					break;
				}
			}
		}
		responseWrapper.copyBodyToResponse();
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
