package vn.prostylee.core.repository.query;

import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.HibernateException;
import org.hibernate.property.access.internal.PropertyAccessStrategyBasicImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyChainedImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyFieldImpl;
import org.hibernate.property.access.internal.PropertyAccessStrategyMapImpl;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.AliasToBeanResultTransformer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class SimpleResultTransformer extends AliasToBeanResultTransformer {

	// IMPL NOTE : due to the delayed population of setters (setters cached
	// for performance), we really cannot properly define equality for
	// this transformer

	private static final long serialVersionUID = -6027108564127737313L;
	private final Class<?> resultClass;
	private boolean isInitialized;
	private Setter[] setters;

	public SimpleResultTransformer(Class resultClass) {
		super(resultClass);
		this.resultClass = resultClass;
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;
		try {
			if (!isInitialized) {
				performInitialize(aliases);
			}

			result = resultClass.getDeclaredConstructor().newInstance();

			for (int i = 0; i < aliases.length; i++) {
				if (setters[i] == null) {
					continue;
				}

				Object tupleValue = tuple[i];
				if (tupleValue == null) {
					setters[i].set(result, null, null);
				} else if (tupleValue instanceof Timestamp) {
					convertTimestampToLocalDateSetter(setters[i], (Timestamp) tupleValue, result);
				} else if (tupleValue instanceof BigInteger || shouldSetLongValue(setters[i])) {
					setters[i].set(result, Long.valueOf(tupleValue.toString()), null);
				} else {
					setters[i].set(result, tupleValue, null);
				}
			}
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new HibernateException("Could not instantiate resultClass: " + resultClass.getName(), e);
		}
		return result;
	}

	private boolean shouldSetLongValue(Setter setter) {
		Method method = setter.getMethod();
		return method != null
				&& method.getParameters().length > 0
				&& method.getParameters()[0].getType().isAssignableFrom(Long.class);
	}

	private void convertTimestampToLocalDateSetter(Setter setter, Timestamp timestamp, Object result) {
		Method setterMethod = setter.getMethod();
		Parameter[] params = setterMethod.getParameters();
		for (Parameter parameter : params) {
			String typeName = parameter.getType().getTypeName();
			if (typeName.equals(LocalDateTime.class.getName())) {
				setter.set(result, timestamp.toLocalDateTime(), null);
			} else if (typeName.equals(LocalDate.class.getName())) {
				setter.set(result, timestamp.toLocalDateTime().toLocalDate(), null);
			}
		}
	}

	private void performInitialize(String[] aliases) {
		PropertyAccessStrategyChainedImpl propertyAccessStrategy = new PropertyAccessStrategyChainedImpl(
				PropertyAccessStrategyBasicImpl.INSTANCE, PropertyAccessStrategyFieldImpl.INSTANCE,
				PropertyAccessStrategyMapImpl.INSTANCE);
		setters = new Setter[aliases.length];
		Field[] fieldResultClass = resultClass.getDeclaredFields();
		Field[] fieldParentClass = resultClass.getSuperclass().getDeclaredFields();
		fieldResultClass = ArrayUtils.addAll(fieldResultClass, fieldParentClass);
		String fieldName;
		for (int i = 0; i < aliases.length; i++) {
			String alias = aliases[i].replace("_", "");
			for (Field field : fieldResultClass) {
				fieldName = field.getName();
				if (fieldName.equalsIgnoreCase(alias)) {
					alias = fieldName;
					setters[i] = propertyAccessStrategy.buildPropertyAccess(resultClass, alias).getSetter();
					break;
				}
			}
		}
		isInitialized = true;
	}
}
