package org.example;
import java.net.*;
import java.io.*;
import java.lang.reflect.*;

public class CalculadoraHttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(36000);
            System.out.println("Servidor calculadora iniciado y esperando conexiones...");
        } catch (IOException e) {
            System.err.println("No se pudo escuchar en el puerto: 36000.");
            System.exit(1);
        }

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Conexión aceptada desde: " + clientSocket.getInetAddress());

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String inputLine;
                String requestUri = "";
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.startsWith("GET")) {
                        requestUri = inputLine.split(" ")[1];
                    }
                    if (!in.ready()) { break; }
                }

                if (requestUri.startsWith("/compreflex")) {
                    String[] params = requestUri.split("\\?")[1].split("&");
                    String operacion = params[0].split("=")[1];
                    String numeros = params[1].split("=")[1];

                    String resultado = calcular(operacion, numeros);
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: application/json");
                    out.println();
                    out.println("{\"resultado\": \"" + resultado + "\"}");
                }

                out.close();
                in.close();
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error al manejar la conexión.");
                e.printStackTrace();
            }
        }
    }

    private static String calcular(String operacion, String numeros) {
        try {
            String[] numArray = numeros.split(",");
            double[] parsedNumbers = new double[numArray.length];
            for (int i = 0; i < numArray.length; i++) {
                parsedNumbers[i] = Double.parseDouble(numArray[i]);
            }

            Method[] methods = Math.class.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equalsIgnoreCase(operacion)) {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    if (paramTypes.length == 1 && paramTypes[0] == double.class) {
                        return String.valueOf(method.invoke(null, parsedNumbers[0]));
                    } else if (paramTypes.length == 2 && paramTypes[0] == double.class && paramTypes[1] == double.class) {
                        return String.valueOf(method.invoke(null, parsedNumbers[0], parsedNumbers[1]));
                    }
                }
            }
            
            return "Operación no soportada o parámetros incorrectos.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error en el cálculo.";
        }
    }
}
