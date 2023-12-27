package com.seungminyi.geera.utill.session;

public interface SessionManager {
	void setAttribute(String attributeName, Object attributeValue);

	Object getAttribute(String attributeName);

	String getId();
}
