package br.com.efraimgentil.chat_websocket.web.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

public class RequestToObject {
  
  public static <T> T readObjectInRequest(Class<T> clazz , HttpServletRequest req , String objectParamPrefix ){
    try {
      Constructor<T> constructor = clazz.getConstructor();
      T newInstance;
      try {
        newInstance = constructor.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
          field.setAccessible(true);
          String fieldName = field.getName();
          if(objectParamPrefix != null && !objectParamPrefix.isEmpty() ){
            fieldName = objectParamPrefix + "." + fieldName;
          }
          try {
            if(req.getParameterMap().containsKey(fieldName )){
              field.set(newInstance, req.getParameter(fieldName) );
            }
          } catch (IllegalArgumentException | IllegalAccessException e) {
            System.out.println("Tipo de atribuido ao parametro invalido");
            e.printStackTrace();
          }
          field.setAccessible(false);
        }
        return newInstance;
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e1) {
        e1.printStackTrace();
      }
    } catch (NoSuchMethodException | SecurityException e) {
      System.out.println("Verify if the class have the default constructor");
      e.printStackTrace();
    }
    return null;
  }
  
  public static <T> T readObjectInRequest(Class<T> clazz , HttpServletRequest req ){
    return readObjectInRequest(clazz, req, "");
  }

}