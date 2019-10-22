package com.teste.imp;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface CryptographerServer {
  @WebMethod String encriptar(String str);
  @WebMethod String decriptar(String str);
  @WebMethod byte[] base64StringToByteArray(String s);
  @WebMethod String byteArrayToBase64String(byte[] b);
  @WebMethod String byteArrayToBase64String(byte[] b, int len);
  @WebMethod String byteArrayToHexString(byte[] b);
  @WebMethod String byteToHexString(byte b);
  @WebMethod double stringMatching(String pString1, String pString2);
}
