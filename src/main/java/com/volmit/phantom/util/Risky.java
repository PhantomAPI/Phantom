package com.volmit.phantom.util;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;

@Retention(SOURCE)
public @interface Risky
{
	public String value() default "";
}
