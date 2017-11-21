package com.tejashah.internal;

public interface Callback<T> {
	void onFinish(T obj);
}