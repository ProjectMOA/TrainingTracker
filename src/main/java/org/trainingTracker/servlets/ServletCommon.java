package org.trainingTracker.servlets;

import java.util.Map;
import java.util.HashMap;
import java.lang.StringBuffer;
import java.io.BufferedReader;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

class ServletCommon {

    private static final Map<String, int> intensidades;
    static {
        intensidades = new HashMap<String, int>(3);
        intensidades.put("Baja", 1);
        intensidades.put("Media", 2);
        intensidades.put("Alta", 3);
    }
    
    /**
     * @returns HashMap intensidades.
     */
    static HashMap<String, int> getIntensidades() {
        return intensidades;
    }
    
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
