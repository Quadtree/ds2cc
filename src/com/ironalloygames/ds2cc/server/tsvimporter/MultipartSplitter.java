package com.ironalloygames.ds2cc.server.tsvimporter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultipartSplitter {
	public Map<String, String> getParts(String body, String contentType)
	{
		Map<String, String> map = new HashMap<>();

		Pattern contentTypeSplitter = Pattern.compile("multipart/form-data; boundary=(.*)");

		Matcher m = contentTypeSplitter.matcher(contentType);

		if (m.matches())
		{
			// Pattern partFinder = Pattern
			// .compile(Pattern.quote(m.group(1)) +
			// "\\s+Content-Disposition: form-data;.{0,20}name=\"([^\"]+)\".{0,40}Content-Type: text/plain\n(.*)"
			// + Pattern.quote(m.group(1)), Pattern.DOTALL);

			Pattern partFinder = Pattern
					.compile(Pattern.quote(m.group(1)) + "(.*)" + Pattern.quote(m.group(1)), Pattern.DOTALL);

			m = partFinder.matcher(body);

			while (m.find()) {
				Logger.getGlobal().info(m.group(1));
			}
		}



		return map;
	}
}
