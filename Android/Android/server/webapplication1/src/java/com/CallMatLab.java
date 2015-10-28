package com;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mac
 */
import matlabcontrol.*;
 
public class CallMatLab {
    public static void main(String[] args) throws MatlabConnectionException, MatlabInvocationException{
		MatlabProxyFactoryOptions.Builder B = new MatlabProxyFactoryOptions.Builder();
		        B.setMatlabLocation("/Applications/MATLAB_R2013a.app/bin");//path to matlab's bin directory
		        MatlabProxyFactory factory = new MatlabProxyFactory();
		        MatlabProxy myproxy = factory.getProxy();
		        String matlabcommand = "  rec (" + "\'/Users/mac/Documents/MATLAB/plant2.jpg\'" + "," +
		                                                             Integer.toString(2) + ") ";
                        //rec('/Users/mac/Documents/MATLAB/plant2.jpg',2);
		        myproxy.eval(matlabcommand);//call the function here
		        
		        myproxy.exit();
		        myproxy.disconnect();//this closes the Matlab program automatically
		}

}
