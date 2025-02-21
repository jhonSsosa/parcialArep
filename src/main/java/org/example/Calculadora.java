package org.example;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Calculadora {
    public static void calcular(String operation, String numbers) {
        try {
            Class<?> c = Class.forName("java.lang.Math");
            String[] numArray = numbers.split(",");
            double[] parsedNumbers = new double[numArray.length];
            for (int i = 0; i < numArray.length; i++) {
                parsedNumbers[i] = Double.parseDouble(numArray[i]);
            }
            Method method = null;
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equalsIgnoreCase(operation)) {
                    method = m;
                    break;
                }
            }
            if (method != null) {
                Object result = null;
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == double.class) {
                    result = method.invoke(null, parsedNumbers[0]);
                } else if (method.getParameterCount() == 2 && method.getParameterTypes()[0] == double.class
                        && method.getParameterTypes()[1] == double.class) {
                    result = method.invoke(null, parsedNumbers[0], parsedNumbers[1]);
                }
                System.out.format("Resultado de %s(%s): %s\n", operation, numbers, result);
            } else {
                System.out.println("Operación no soportada.");
            }

        } catch (ClassNotFoundException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        } catch (InvocationTargetException x) {
            x.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Formato de número incorrecto.");
        }
    }

    public static void main(String[] args) {
        calcular("sqrt", "16");
        calcular("pow", "2,3");
        calcular("max", "5,10");
    }
}

