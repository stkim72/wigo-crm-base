package com.ceragem.crm.common.util;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {
	/**
	 * Request 에서 Body Data를 꺼냄
	 * 
	 * @param request
	 * @return
	 */
	public static String getBodyDataByRequest(HttpServletRequest request) {
//		if(request == null)
//			return null;
//		String charEncoding = request.getCharacterEncoding(); // 인코딩 설정
//		Charset encoding = StringUtils.isBlank(charEncoding) ? StandardCharsets.UTF_8 : Charset.forName(charEncoding);
//
//		String collect = null;
//		try {
//
//			InputStream is = request.getInputStream();
//			byte[] rawData = IOUtils.toByteArray(is); // InputStream 을 별도로 저장한 다음 getReader() 에서 새 스트림으로 생성
//
//			// body 파싱
//			collect = new BufferedReader(new InputStreamReader(getInputStream(rawData), encoding)).lines()
//					.collect(Collectors.joining(System.lineSeparator()));
//			if (StringUtils.isEmpty(collect)) { // body 가 없을경우 로깅 제외
//
//				return null;
//			}
//			if (request.getContentType() != null && request.getContentType().contains("multi")) { // 파일 업로드시 로깅제외
//				return null;
//			}
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//		return collect;
		return "";
	}

//	private static ServletInputStream getInputStream(byte[] rawData) throws IOException {
//
//		final ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
//		ServletInputStream sis = new ServletInputStream() {
//
//			@Override
//			public int read() throws IOException {
//				return bais.read();
//			}
//
//			@Override
//			public void setReadListener(ReadListener readListener) {
//			}
//
//			@Override
//			public boolean isReady() {
//				return false;
//			}
//
//			@Override
//			public boolean isFinished() {
//				return false;
//			}
//		};
//
//		return sis;
//	}
}
