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
	
	/**
	 * @param str
	 * @param response
	 * @returns true if str confirms weight specifications
	 */
	static boolean isValidWeight (String str, HttpServletResponse response) throws IOException {
	    boolean error = false;
	    
	    try {
	        if (!(Double.parseDouble(str) > 0)) {
	            error = true;
	        }
	    }
	    catch (NullPointerException e) {
	        error = true;
	    }
	    catch (NumberFormatException e) {
	        error = true;
	    }
	    
	    if (error) {
	        response.getWriter().println("Peso no válido");
	    }
	    
	    return !error;
	}
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms series specifications
     */
    static boolean isValidSeries (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        
        try {
            if (!(Integer.parseInt(str) > 0)) {
                error = true;
            }
        }
        catch (NullPointerException e) {
            error = true;
        }
        catch (NumberFormatException e) {
            error = true;
        }
        
        if (error) {
            response.getWriter().println("Número de series no válido");
        }
        
        return !error;
    }
    
    /**
     * @param str
     * @param response
     * @returns true if str confirms repetitions specifications
     */
    static boolean isValidRepetitions (String str, HttpServletResponse response) throws IOException {
        boolean error = false;
        
        try {
            if (!(Integer.parseInt(str) > 0)) {
                error = true;
            }
        }
        catch (NullPointerException e) {
            error = true;
        }
        catch (NumberFormatException e) {
            error = true;
        }
        
        if (error) {
            response.getWriter().println("Número de repeticiones no válido");
        }
        
        return !error;
    }

}