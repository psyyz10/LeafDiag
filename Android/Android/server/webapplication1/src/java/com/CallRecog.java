/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;

/**
 *
 * @author mac
 */
@MultipartConfig
public class CallRecog extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    String filename;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        
        Part filePart = request.getPart("img"); // Retrieves <input type="file" name="file">
        System.out.println("asdfsdf");
        filename = getFilename(filePart);
        InputStream filecontent = null;
        OutputStream out = null;
        final PrintWriter writer = response.getWriter();
        
        try{
            out = new FileOutputStream("/Users/mac/NetBeansProjects/WebApplication1/web/" + filename);
            filecontent = filePart.getInputStream();
            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            //writer.println("New file " + filename + " created at ");  
            System.out.println("New file " + filename + " created at ");
            writer.append("/Users/mac/NetBeansProjects/WebApplication1/web/identification.png");
        }catch(FileNotFoundException fne){
            
        }finally {
        if (out != null) {
            out.close();
        }
        if (filecontent != null) {
            filecontent.close();
        }
        }
  
        try {
            runRecog();
        } catch (MatlabConnectionException ex) {
            Logger.getLogger(CallRecog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MatlabInvocationException ex) {
            Logger.getLogger(CallRecog.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    public void runRecog()  throws MatlabConnectionException, MatlabInvocationException{
        MatlabProxyFactoryOptions.Builder B = new MatlabProxyFactoryOptions.Builder();
		        B.setMatlabLocation("/Applications/MATLAB_R2013a.app/bin");//path to matlab's bin directory
		        MatlabProxyFactory factory = new MatlabProxyFactory();
		        MatlabProxy myproxy = factory.getProxy();
		        String matlabcommand = "  rec (" + "\'/Users/mac/NetBeansProjects/WebApplication1/web/" + filename + "\'" + "," +
		                                                             Integer.toString(2) + ") ";
		        myproxy.eval(matlabcommand);//call the function here
		        
		        myproxy.exit();
		        myproxy.disconnect();//this closes the Matlab program automatically
    }
    private static String getFilename(Part part) {
    for (String cd : part.getHeader("content-disposition").split(";")) {
        if (cd.trim().startsWith("filename")) {
            String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
        }
    }
    return null;
}

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
