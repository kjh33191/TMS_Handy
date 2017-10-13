package kumon2014.webservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Annotation {

	/**
	 * シリアライズ対象メンバーであることをあらわすAnnotation
	 * @author shinm
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public static @interface DataMember {
	}
}