package org.trainingTracker.servlets;

import java.io.IOException;
import java.lang.NullPointerException;
import java.lang.NumberFormatException;

import java.lang.StringBuffer;
import java.io.BufferedReader;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

class ServletCommon {

	/**
     * @param bf
     * @returns JSON Object that contains all the content readed from buffer bf.
     */
	static JSONObject readJSON (BufferedReader bf) throws Exception {
		StringBuffer jb = new StringBuffer();
		String line = null;
		
		while ((line = bf.readLine()) != null) {
			jb.append(line);
		}
		
		return JSONObject.fromObject(jb.toString());
	}

}