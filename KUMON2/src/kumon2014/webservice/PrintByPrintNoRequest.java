package kumon2014.webservice;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class PrintByPrintNoRequest implements KvmSerializable {
	String 	Password; 
	String 	KyokaID; 
	String 	KyozaiID; 
	int 	PrintNo; 

	@Override
	public Object getProperty(int arg0) {
		switch (arg0)
		{
			case 0:
				return Password;
			case 1:
				return KyokaID;
			case 2:
				return KyozaiID;
			case 3:
				return PrintNo;
			default:
				return null;
        }
	}

	@Override
	public int getPropertyCount() {
		return 4;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0)
		{
			case 0:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Password";
				break;
			case 1:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "KyokaID";
				break;
			case 2:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "KyozaiID";
				break;
			case 3:
				arg2.type = PropertyInfo.INTEGER_CLASS;
				arg2.name = "PrintNo";
				break;
			default:break;
		}

	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0)
		{
			case 0:
				Password = (String)arg1;
				break;
			case 1:
				KyokaID = (String)arg1;
				break;
			case 2:
				KyozaiID = (String)arg1;
				break;
			case 3:
				PrintNo = (Integer)arg1;
				break;
			default:
				break;
		}
	}
}