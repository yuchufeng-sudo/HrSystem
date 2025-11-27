package com.ys.common.core.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ys.common.core.text.Convert;
import com.ys.common.core.utils.DateUtils;

/**
 * Reflection utility class. Provides utility functions for calling getter/setter methods,
 * accessing private variables, invoking private methods, getting generic type Class,
 * obtaining the actual class behind AOP proxies, etc.
 *
 * @author ruoyi
 */
@SuppressWarnings("rawtypes")
public class ReflectUtils
{
    private static final String SETTER_PREFIX = "set";

    private static final String GETTER_PREFIX = "get";

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);

    /**
     * Invoke getter method.
     * Supports multi-level access, e.g.: objectName.objectName.method
     *
     * @param obj the target object
     * @param propertyName the property name, supports dot notation for nested properties
     * @param <E> the return type
     * @return the property value
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeGetter(Object obj, String propertyName)
    {
        Object object = obj;
        for (String name : StringUtils.split(propertyName, "."))
        {
            String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
        }
        return (E) object;
    }

    /**
     * Invoke setter method, matching only by method name.
     * Supports multi-level access, e.g.: objectName.objectName.method
     *
     * @param obj the target object
     * @param propertyName the property name, supports dot notation for nested properties
     * @param value the value to set
     * @param <E> the value type
     */
    public static <E> void invokeSetter(Object obj, String propertyName, E value)
    {
        Object object = obj;
        String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; i++)
        {
            if (i < names.length - 1)
            {
                String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
            }
            else
            {
                String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }

    /**
     * Directly read object field value, ignoring private/protected modifiers, without using getter method.
     *
     * @param obj the target object
     * @param fieldName the field name
     * @param <E> the field type
     * @return the field value, or null if field not found
     */
    @SuppressWarnings("unchecked")
    public static <E> E getFieldValue(final Object obj, final String fieldName)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            logger.debug("Field [" + fieldName + "] not found in [" + obj.getClass() + "]");
            return null;
        }
        E result = null;
        try
        {
            result = (E) field.get(obj);
        }
        catch (IllegalAccessException e)
        {
            logger.error("Impossible exception: {}", e.getMessage());
        }
        return result;
    }

    /**
     * Directly set object field value, ignoring private/protected modifiers, without using setter method.
     *
     * @param obj the target object
     * @param fieldName the field name
     * @param value the value to set
     * @param <E> the value type
     */
    public static <E> void setFieldValue(final Object obj, final String fieldName, final E value)
    {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null)
        {
            logger.debug("Field [" + fieldName + "] not found in [" + obj.getClass() + "]");
            return;
        }
        try
        {
            field.set(obj, value);
        }
        catch (IllegalAccessException e)
        {
            logger.error("Impossible exception: {}", e.getMessage());
        }
    }

    /**
     * Directly invoke object method, ignoring private/protected modifiers.
     * For one-time invocation. For repeated calls, use getAccessibleMethod() to get Method first.
     * Matches both method name and parameter types.
     *
     * @param obj the target object
     * @param methodName the method name
     * @param parameterTypes the parameter types
     * @param args the arguments
     * @param <E> the return type
     * @return the method return value, or null if method not found
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
                                     final Object[] args)
    {
        if (obj == null || methodName == null)
        {
            return null;
        }
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null)
        {
            logger.debug("Method [" + methodName + "] not found in [" + obj.getClass() + "]");
            return null;
        }
        try
        {
            return (E) method.invoke(obj, args);
        }
        catch (Exception e)
        {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * Directly invoke object method, ignoring private/protected modifiers.
     * For one-time invocation. For repeated calls, use getAccessibleMethodByName() to get Method first.
     * Matches only by method name. If there are multiple methods with the same name, invokes the first one.
     *
     * @param obj the target object
     * @param methodName the method name
     * @param args the arguments
     * @param <E> the return type
     * @return the method return value, or null if method not found
     */
    @SuppressWarnings("unchecked")
    public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args)
    {
        Method method = getAccessibleMethodByName(obj, methodName, args.length);
        if (method == null)
        {
            logger.debug("Method [" + methodName + "] not found in [" + obj.getClass() + "]");
            return null;
        }
        try
        {
            // Type conversion (convert argument data types to target method parameter types)
            Class<?>[] cs = method.getParameterTypes();
            for (int i = 0; i < cs.length; i++)
            {
                if (args[i] != null && !args[i].getClass().equals(cs[i]))
                {
                    if (cs[i] == String.class)
                    {
                        args[i] = Convert.toStr(args[i]);
                        if (StringUtils.endsWith((String) args[i], ".0"))
                        {
                            args[i] = StringUtils.substringBefore((String) args[i], ".0");
                        }
                    }
                    else if (cs[i] == Integer.class)
                    {
                        args[i] = Convert.toInt(args[i]);
                    }
                    else if (cs[i] == Long.class)
                    {
                        args[i] = Convert.toLong(args[i]);
                    }
                    else if (cs[i] == Double.class)
                    {
                        args[i] = Convert.toDouble(args[i]);
                    }
                    else if (cs[i] == Float.class)
                    {
                        args[i] = Convert.toFloat(args[i]);
                    }
                    else if (cs[i] == Date.class)
                    {
                        if (args[i] instanceof String)
                        {
                            args[i] = DateUtils.parseDate(args[i]);
                        }
                        else
                        {
                            args[i] = DateUtil.getJavaDate((Double) args[i]);
                        }
                    }
                    else if (cs[i] == boolean.class || cs[i] == Boolean.class)
                    {
                        args[i] = Convert.toBool(args[i]);
                    }
                }
            }
            return (E) method.invoke(obj, args);
        }
        catch (Exception e)
        {
            String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }

    /**
     * Loop upward through inheritance hierarchy to get the object's DeclaredField,
     * and force it to be accessible.
     * Returns null if not found after reaching Object class.
     *
     * @param obj the target object
     * @param fieldName the field name
     * @return the accessible Field, or null if not found
     */
    public static Field getAccessibleField(final Object obj, final String fieldName)
    {
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
        {
            try
            {
                Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (NoSuchFieldException e)
            {
                continue;
            }
        }
        return null;
    }

    /**
     * Loop upward through inheritance hierarchy to get the object's DeclaredMethod,
     * and force it to be accessible.
     * Returns null if not found after reaching Object class.
     * Matches method name and parameter types.
     * Use this method to get Method first, then call Method.invoke(Object obj, Object... args)
     * for methods that need to be called multiple times.
     *
     * @param obj the target object
     * @param methodName the method name
     * @param parameterTypes the parameter types
     * @return the accessible Method, or null if not found
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName,
                                             final Class<?>... parameterTypes)
    {
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            try
            {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (NoSuchMethodException e)
            {
                continue;
            }
        }
        return null;
    }

    /**
     * Loop upward through inheritance hierarchy to get the object's DeclaredMethod,
     * and force it to be accessible.
     * Returns null if not found after reaching Object class.
     * Matches only by method name.
     * Use this method to get Method first, then call Method.invoke(Object obj, Object... args)
     * for methods that need to be called multiple times.
     *
     * @param obj the target object
     * @param methodName the method name
     * @param argsNum the number of arguments
     * @return the accessible Method, or null if not found
     */
    public static Method getAccessibleMethodByName(final Object obj, final String methodName, int argsNum)
    {
        if (obj == null)
        {
            return null;
        }
        Validate.notBlank(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass())
        {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods)
            {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum)
                {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * Change private/protected method to public. Avoids calling actual modification statements
     * when possible to prevent JDK SecurityManager complaints.
     *
     * @param method the method to make accessible
     */
    public static void makeAccessible(Method method)
    {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible())
        {
            method.setAccessible(true);
        }
    }

    /**
     * Change private/protected field to public. Avoids calling actual modification statements
     * when possible to prevent JDK SecurityManager complaints.
     *
     * @param field the field to make accessible
     */
    public static void makeAccessible(Field field)
    {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible())
        {
            field.setAccessible(true);
        }
    }

    /**
     * Get the type of generic parameter declared in Class definition through reflection.
     * Note that the generic must be defined at the parent class level.
     * Returns Object.class if not found.
     *
     * @param clazz the target class
     * @param <T> the generic type
     * @return the generic parameter type
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassGenricType(final Class clazz)
    {
        return getClassGenricType(clazz, 0);
    }

    /**
     * Get the type of generic parameter declared in the parent class through reflection.
     * Returns Object.class if not found.
     *
     * @param clazz the target class
     * @param index the index of the generic parameter
     * @return the generic parameter type at the specified index
     */
    public static Class getClassGenricType(final Class clazz, final int index)
    {
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType))
        {
            logger.debug(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0)
        {
            logger.debug("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class))
        {
            logger.debug(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * Get the user-defined class, handling CGLIB proxy classes.
     *
     * @param instance the instance object
     * @return the actual user class (not the proxy class)
     * @throws RuntimeException if instance is null
     */
    public static Class<?> getUserClass(Object instance)
    {
        if (instance == null)
        {
            throw new RuntimeException("Instance must not be null");
        }
        Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR))
        {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass))
            {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * Convert checked exceptions during reflection to unchecked exceptions.
     *
     * @param msg the error message
     * @param e the exception to convert
     * @return the converted RuntimeException
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(String msg, Exception e)
    {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException)
        {
            return new IllegalArgumentException(msg, e);
        }
        else if (e instanceof InvocationTargetException)
        {
            return new RuntimeException(msg, ((InvocationTargetException) e).getTargetException());
        }
        return new RuntimeException(msg, e);
    }
}
