/**
 * 
 */
package kumon2014.webservice;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/**
 * Soapリクエストのベースクラス
 * @author shinm
 *
 */
public abstract class BaseSoapRequest implements KvmSerializable {
	public static class Property {
		public String name;
		public Type type;
		
		public Property(String n, Type t) {
			name = n;
			type = t;
		}
	}

	/**
	 * 
	 */
	public BaseSoapRequest() {
		Class<?> cls = getClass();
		properties = new Vector<Property>();
		for (Field f : cls.getDeclaredFields()) {
			if (f.getAnnotation(Annotation.DataMember.class) != null) {
				Property prop = new Property(f.getName(), f.getType());
				properties.add(prop);
			}
		}
	}
	
	protected Vector<Property> properties = null;
	

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getProperty(int)
	 */
	@Override
	public final Object getProperty(int index) {
		int len = properties.size();
		if (index < 0 || index >= len) {
			throw new IllegalArgumentException();
		}
		Class<? extends BaseSoapRequest> c = this.getClass();
		Property prop = properties.get(index);
		try {
			Field f = c.getDeclaredField(prop.name);
			return f.get(this);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyCount()
	 */
	@Override
	public final int getPropertyCount() {
		return properties.size();
	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#getPropertyInfo(int, java.util.Hashtable, org.ksoap2.serialization.PropertyInfo)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public final void getPropertyInfo(int index, Hashtable arg1, PropertyInfo arg2) {
		int len = properties.size();
		if (index < 0 || index >= len) {
			throw new IllegalArgumentException();
		}

		Property prop = properties.get(index);

		if (prop.type == String.class) {
			arg2.type = PropertyInfo.STRING_CLASS;
		}
		else if (prop.type == Integer.class) {
			arg2.type = PropertyInfo.INTEGER_CLASS;
		}
		else {
			arg2.type = prop.type;
		}
		arg2.name = prop.name;

	}

	/* (non-Javadoc)
	 * @see org.ksoap2.serialization.KvmSerializable#setProperty(int, java.lang.Object)
	 */
	// @Override
	public final void setProperty(int index, Object obj) {
		int len = properties.size();
		if (index < 0 || index >= len) {
			throw new IllegalArgumentException();
		}
		Class<? extends BaseSoapRequest> c = this.getClass();
		Property prop = properties.get(index);
		try {
			Field f = c.getDeclaredField(prop.name);
			f.set(this, obj);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
}
