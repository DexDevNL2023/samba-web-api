package com.teleo.manager.generic.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	private final Logger logger = LogManager.getLogger(getClass());

	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        logger.error("Nom de la méthode -" + method.getName(), throwable);
		for (Object param : obj) {
            logger.error("Valeur du paramètre -" + param);
		}
	}
}
