package com.teste.imp;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.jws.WebService;

@WebService(endpointInterface = "com.teste.imp.Cyptographer")
public class CryptographerServerImpl implements CryptographerServer{
	
	private static String CHAVE_CRIPTOGRAFIA_BLOWFISH = "neurotechcetorue";

	public String encriptar(String str)
	{
		String strCript = str;
		try
		{
			Cipher ch = Cipher.getInstance("Blowfish");
			SecretKey k1 = new SecretKeySpec(CHAVE_CRIPTOGRAFIA_BLOWFISH.getBytes(), "Blowfish");
			ch.init(1, k1);
			byte[] b = ch.doFinal(strCript.getBytes());
			String s1 = byteArrayToBase64String(b);
			strCript = s1;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return strCript;
	}

	public String decriptar(String str)
	{
		String strDecript = str;
		try
		{
			Cipher ch = Cipher.getInstance("Blowfish");
			SecretKey k1 = new SecretKeySpec(CHAVE_CRIPTOGRAFIA_BLOWFISH.getBytes(), "Blowfish");
			ch.init(2, k1);
			byte[] b = base64StringToByteArray(strDecript);
			byte[] b1 = ch.doFinal(b);
			String s1 = new String(b1);
			strDecript = s1;
		}
		catch (Exception exception)
		{
			Exception excecao = new Exception("Erro ao decriptografar palavra");
			excecao.setStackTrace(exception.getStackTrace());
			//				Log.excecao(excecao);
		}
		return strDecript;
	}

	private static byte[] _base64ToBytes(String s)
	{
		int len = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != '=') {
				len++;
			}
		}
		int[] digit = new int[len];
		for (int i = 0; i < len; i++)
		{
			char c = s.charAt(i);
			if ((c >= 'A') && (c <= 'Z')) {
				digit[i] = (c - 'A');
			} else if ((c >= 'a') && (c <= 'z')) {
				digit[i] = (c - 'a' + 26);
			} else if ((c >= '0') && (c <= '9')) {
				digit[i] = (c - '0' + 52);
			} else if (c == '+') {
				digit[i] = 62;
			} else if (c == '/') {
				digit[i] = 63;
			}
		}
		byte[] b = new byte[len - 1];
		switch (len)
		{
		case 4: 
			b[2] = ((byte)((digit[2] & 0x3) << 6 | digit[3]));
		case 3: 
			b[1] = ((byte)((digit[1] & 0xF) << 4 | (digit[2] & 0x3C) >>> 2));
		case 2: 
			b[0] = ((byte)(digit[0] << 2 | (digit[1] & 0x30) >>> 4));
		}
		return b;
	}

	private static char _base64Digit(int i)
	{
		if (i < 26) {
			return (char)(65 + i);
		}
		if (i < 52) {
			return (char)(97 + (i - 26));
		}
		if (i < 62) {
			return (char)(48 + (i - 52));
		}
		return i != 62 ? '/' : '+';
	}

	public byte[] base64StringToByteArray(String s)
			throws NumberFormatException
	{
		String t = "";
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if (c != '\n') {
				if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z')) || ((c >= '0') && (c <= '9')) || (c == '+') || (c == '/'))
				{
					t = t + c;
				}
				else
				{
					if (c == '=') {
						break;
					}
					throw new NumberFormatException();
				}
			}
		}
		int len = t.length();
		int n = 3 * (len / 4);
		switch (len % 4)
		{
		case 1: 
			throw new NumberFormatException();
		case 2: 
			len += 2;
			n++;
			t = t + "==";
			break;
		case 3: 
			len++;
			n += 2;
			t = t + "=";
		}
		byte[] b = new byte[n];
		for (int i = 0; i < len / 4; i++)
		{
			byte[] temp = _base64ToBytes(t.substring(4 * i, 4 * (i + 1)));
			for (int j = 0; j < temp.length; j++) {
				b[(3 * i + j)] = temp[j];
			}
		}
		return b;
	}

	public String byteArrayToBase64String(byte[] b)
	{
		return byteArrayToBase64String(b, b.length);
	}

	public String byteArrayToBase64String(byte[] b, int len)
	{
		String s = "";
		int n = len / 3;
		int m = len % 3;
		for (int i = 0; i < n; i++)
		{
			int j = i * 3;
			s = s + toBase64(b[j], b[(j + 1)], b[(j + 2)]);
		}
		if (m == 1) {
			s = s + toBase64(b[(len - 1)]);
		} else if (m == 2) {
			s = s + toBase64(b[(len - 2)], b[(len - 1)]);
		}
		String result = "";
		len = s.length();
		n = len / 64;
		m = len % 64;
		for (int i = 0; i < n; i++) {
			result = result + s.substring(i * 64, (i + 1) * 64);
		}
		if (m > 0) {
			result = result + s.substring(n * 64, len);
		}
		return result;
	}

	public String byteArrayToHexString(byte[] b)
	{
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + byteToHexString(b[i]);
		}
		return result;
	}

	public String byteToHexString(byte b)
	{
		String hexDigits = "0123456789abcdef";
		int n = b;
		if (n < 0) {
			n += 256;
		}
		return "" + "0123456789abcdef".charAt(n / 16) + "0123456789abcdef".charAt(n % 16);
	}

	private static String toBase64(byte b1)
	{
		int[] digit = new int[2];
		digit[0] = ((b1 & 0xFC) >>> 2);
		digit[1] = ((b1 & 0x3) << 4);
		String result = "";
		for (int i = 0; i < digit.length; i++) {
			result = result + _base64Digit(digit[i]);
		}
		result = result + "==";
		return result;
	}

	private static String toBase64(byte b1, byte b2)
	{
		int[] digit = new int[3];
		digit[0] = ((b1 & 0xFC) >>> 2);
		digit[1] = ((b1 & 0x3) << 4);
		digit[1] |= (b2 & 0xF0) >> 4;
		digit[2] = ((b2 & 0xF) << 2);
		String result = "";
		for (int i = 0; i < digit.length; i++) {
			result = result + _base64Digit(digit[i]);
		}
		result = result + "=";
		return result;
	}

	private static String toBase64(byte b1, byte b2, byte b3)
	{
		int[] digit = new int[4];
		digit[0] = ((b1 & 0xFC) >>> 2);
		digit[1] = ((b1 & 0x3) << 4);
		digit[1] |= (b2 & 0xF0) >> 4;
		digit[2] = ((b2 & 0xF) << 2);
		digit[2] |= (b3 & 0xC0) >> 6;
		digit[3] = (b3 & 0x3F);
		String result = "";
		for (int i = 0; i < digit.length; i++) {
			result = result + _base64Digit(digit[i]);
		}
		return result;
	}

	private static String caracteresAcentuados = "���������������������������������������������������";
	private static String caracteresNaoAcentuados = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";
	private static char[] tabelaCaracteres = new char['?'];

	static
	{
		for (int i = 0; i < tabelaCaracteres.length; i++) {
			tabelaCaracteres[i] = ((char)i);
		}
//		for (int i = 0; i < caracteresAcentuados.length(); i++) {
//			tabelaCaracteres[caracteresAcentuados.charAt(i)] = caracteresNaoAcentuados.charAt(i);
//		}
	}

	public static String removerAcentos(String stringAcentuada)
	{
		StringBuilder stringNaoAcentuada = new StringBuilder();
		for (int i = 0; i < stringAcentuada.length(); i++)
		{
			char c = stringAcentuada.charAt(i);
			if (c < '?') {
				stringNaoAcentuada.append(tabelaCaracteres[c]);
			} else {
				stringNaoAcentuada.append(c);
			}
		}
		return stringNaoAcentuada.toString();
	}

	public double stringMatching(String pString1, String pString2)
	{
		pString1 = preprocessarString(pString1);
		pString2 = preprocessarString(pString2);

		int vTamanhoNome1 = pString1.length();
		int vTamanhoNome2 = pString2.length();
		if (vTamanhoNome1 + vTamanhoNome2 == 0) {
			return 1.0D;
		}
		if ((vTamanhoNome1 == 0) || (vTamanhoNome2 == 0)) {
			return 0.0D;
		}
		int[][] vMatrizDistancias = new int[vTamanhoNome1 + 1][vTamanhoNome2 + 1];
		for (int i = 0; i <= vTamanhoNome1; vMatrizDistancias[i][0] = (i++)) {}
		for (int j = 0; j <= vTamanhoNome2; vMatrizDistancias[0][j] = (j++)) {}
		for (int i = 1; i <= vTamanhoNome1; i++) {
			for (int j = 1; j <= vTamanhoNome2; j++)
			{
				int cost = pString2.charAt(j - 1) == pString1.charAt(i - 1) ? 0 : 1;
				vMatrizDistancias[i][j] = Math.min(Math.min(vMatrizDistancias[(i - 1)][j] + 1, vMatrizDistancias[i][(j - 1)] + 1), vMatrizDistancias[(i - 1)][(j - 1)] + cost);
			}
		}
		return 1.0D - vMatrizDistancias[vTamanhoNome1][vTamanhoNome2] / Math.max(vTamanhoNome1, vTamanhoNome2);
	}

	private static String preprocessarString(String pString)
	{
		String vRet = "";
		if (pString != null)
		{
			vRet = pString;
			vRet = vRet.toLowerCase().trim();
			vRet = removerAcentos(vRet);
			vRet = removerEspacosInternos(vRet);
		}
		return vRet;
	}

	public static String removerEspacosInternos(String pStringEntrada)
	{
		String vRet = pStringEntrada;
		int vTamanho1 = vRet.length();
		int vTamanho2 = 0;
		while (vTamanho1 != vTamanho2)
		{
			vTamanho2 = vTamanho1;
			vRet = vRet.replace("  ", " ");
			vTamanho1 = vRet.length();
		}
		return vRet;
	}

	public static String toCamelCase(boolean pEspecial, String pNome)
	{
		String vNmColuna = pNome;
		String vSubstringResto = vNmColuna.substring(1, vNmColuna.length());
		if (!pEspecial) {
			vSubstringResto = vSubstringResto.toLowerCase();
		}
		vNmColuna = vNmColuna.substring(0, 1).toUpperCase() + vSubstringResto;
		int vIndex = vNmColuna.indexOf("_");
		while (vIndex > 0) {
			try
			{
				vNmColuna = vNmColuna.substring(0, vIndex) + vNmColuna.substring(vIndex + 1, vIndex + 2).toUpperCase() + vNmColuna.substring(vIndex + 2);
				vIndex = vNmColuna.indexOf("_");
			}
			catch (Exception e)
			{
				vIndex = -1;
			}
		}
		return vNmColuna;
	}

	public static String escapeHTML(String pInput)
	{
		if (pInput == null) {
			return null;
		}
		return pInput.replaceAll("<", "&#60;").replaceAll(">", "&#62;").replaceAll("\"", "&#34;").replaceAll("&", "&#38;");
	}

}
