package pl.rys.core.test;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public abstract class AbstractRestTemplate {

	private String cookie;

	@Autowired
	private TestRestTemplate restTemplate;

	protected void login(String username, String password) {
		ResponseEntity<String> response = this.restTemplate.postForEntity(getLoginUrl() + "?username=" + username + "&password=" + password, null, String.class);
		List<String> cookie = response.getHeaders().get(HttpHeaders.SET_COOKIE);
		this.cookie = cookie.get(0).substring(0, cookie.get(0).indexOf(";"));
	}

	protected <T> ResponseEntity<T> get(String param, Class<T> reponseType) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", cookie);
		HttpEntity requestEntity = new HttpEntity(null, requestHeaders);
		return this.restTemplate.exchange(getControllerUrl() + param, HttpMethod.GET, requestEntity, reponseType);
	}

	protected <T> ResponseEntity<T> post(String param, Class<T> reponseType, Object body) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", cookie);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, requestHeaders);
		return this.restTemplate.exchange(getControllerUrl() + param, HttpMethod.POST, requestEntity, reponseType);
	}

	protected <T> ResponseEntity<T> put(String param, Class<T> reponseType, Object body) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", cookie);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, requestHeaders);
		return this.restTemplate.exchange(getControllerUrl() + param, HttpMethod.PUT, requestEntity, reponseType);
	}

	protected <T> ResponseEntity<T> delete(String param, Class<T> reponseType, Object body) {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.add("Cookie", cookie);
		HttpEntity<Object> requestEntity = new HttpEntity<Object>(body, requestHeaders);
		return this.restTemplate.exchange(getControllerUrl() + param, HttpMethod.DELETE, requestEntity, reponseType);
	}

	protected abstract String getLoginUrl();

	protected abstract String getControllerUrl();
}
