package controller;
 import matlabcontrol.*;


public class UseMatLab {
	public static String runRec(String path) throws MatlabConnectionException, MatlabInvocationException{
		MatlabProxyFactoryOptions.Builder B = new MatlabProxyFactoryOptions.Builder();
		        B.setMatlabLocation("C:/Program Files/MATLAB/R2013a/bin");//path to matlab's bin directory
		        MatlabProxyFactory factory = new MatlabProxyFactory();
		        MatlabProxy myproxy = factory.getProxy();
		        String matlabcommand = "rec(" + "\'"+path +"\'" + "," + Integer.toString(2) + ") ";
		        //myproxy.eval(matlabcommand);//call the function here
		        Object[] name = myproxy.returningEval(matlabcommand,1);//call the function here
		       
		        myproxy.exit();
		        myproxy.disconnect();//this closes the Matlab program automatically
		        return name[0].toString();
		        //return "";
		}
}
