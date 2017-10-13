package kumon2014.webservice;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import kumon2014.webservice.BaseSoapRequest.Property;

public class BaseSoapResponse extends Parsable {

	public KumonSoapResult Result;

	public BaseSoapResponse() {
		super();
		Result = new KumonSoapResult();
	}

	/**
	 * SoapObjectÇÃÉpÅ[ÉX
	 * 
	 * @param response
	 * @return ê¨î€
	 */
	@Override
	public boolean Parse(SoapObject response) {
		// Result
		try {
			SoapObject result = (SoapObject) response.getProperty("Result");
			if (result != null) {
				try {
					String status = result.getPropertyAsString("Status");
					if (status != null)
						Result.setStatus(status);
				}
				catch (Exception unused) {
				}
				try {
					String error = result.getPropertyAsString("Error");
					if (error != null)
						Result.setError(error);
				}
				catch (Exception unused) {
				}
			}
		} catch (Exception unused) {
		}
		return super.Parse(response);
	}
}
class Parsable {
	protected Vector<Property> properties = null;
	
	public Parsable() {
		Class<?> cls = getClass();
		properties = new Vector<Property>();
		for (Field f : cls.getDeclaredFields()) {
			if (f.getAnnotation(Annotation.DataMember.class) != null) {
				Property prop = new Property(f.getName(), f.getType());
				properties.add(prop);
				Log.d(getClass().getSimpleName()+"#BaseSoapResponse()", prop.name +  " added");
			}
		}
		
	}
	
	boolean Parse(SoapObject response) {
		boolean fRet = false;
		for (Property prop : properties) {
			try {
				Object so = response.getProperty(prop.name);
				if (so != null) {
					if (so instanceof SoapObject) {
						try {
							Field f = getClass().getDeclaredField(prop.name);
							if (f != null) {
								Object o = f.get(this);
								if (o instanceof Parsable) {
									((Parsable) o).Parse((SoapObject) so);
								}
							}
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (NoSuchFieldException e) {
							e.printStackTrace();
						}
					}
					else {
						Field f = getClass().getDeclaredField(prop.name);
						Log.d(getClass().getSimpleName(), "so = " + so.getClass().getSimpleName() + ", name = " + prop.name + ", type = " + prop.type.toString());
						if (prop.type == String.class) {
							f.set(this, so.toString());
						}
						else if (prop.type == Date.class) {
							
						}
						else {
							f.setInt(this, Integer.parseInt(so.toString()));
						}
					}
				}
			} catch (Exception unused) {
			}
		}
		return fRet;
	}
}
