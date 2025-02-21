package org.example;

import java.io.*;
import java.net.*;

public class FachadaHttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000); // Puerto de la fachada
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            System.out.println("Fachada lista para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

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

        if (requestUri.equals("/")) {
            sendHtmlPage(out);
        }
        else if (requestUri.startsWith("/computar")) {
            String[] params = requestUri.split("\\?")[1].split("&");
            String operacion = params[0].split("=")[1];
            String numeros = params[1].split("=")[1];
            String resultado = consultarCalculadora(operacion, numeros);
            sendResult(out, resultado);
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }

    private static void sendHtmlPage(PrintWriter out) {
        String page = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<title>Calculadora</title>" +
                "</head>" +
                "<body>" +
                "<h1>Calculadora</h1>" +
                "<form action=\"#\" onsubmit=\"calcular(); return false;\">" +
                "<br>Escriba la operacion:<br>" +
                "<input type=\"text\" id=\"operacion\" name=\"operacion\">" +
                "<br>Escriba los numeros (separados por coma):<br>" +
                "<input type=\"text\" id=\"numeros\" name=\"numeros\"><br><br>" +
                "<input type=\"submit\" value=\"Calcular\">" +
                "</form>" +
                "<div id=\"getrespmsg\"></div>" +
                "<script>" +
                "function calcular() {" +
                "    let operacion = document.getElementById(\"operacion\").value;" +
                "    let numeros = document.getElementById(\"numeros\").value;" +
                "    const url = `/computar?comando=${operacion}&numeros=${numeros}`;" +
                "    const xhttp = new XMLHttpRequest();" +
                "    xhttp.onload = function() {" +
                "        document.getElementById(\"getrespmsg\").innerHTML = this.responseText;" +
                "    }" +
                "    xhttp.open(\"GET\", url, true);" +
                "    xhttp.send();" +
                "}" +
                "</script>" +
                "</body>" +
                "</html>";
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();
        out.println(page);
    }

    private static void sendResult(PrintWriter out, String resultado) {
        String response = "<html><body>" +
                "<h1>Resultado</h1>" +
                "<p>" + resultado + "</p>" +
                "</body></html>";
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println();
        out.println(response);
    }

    private static String consultarCalculadora(String operacion, String numeros) {
        try {
            URL url = new URL("http://localhost:36000/compreflex?comando=" + operacion + "&numeros=" + numeros);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al obtener el resultado de la calculadora.";
        }
    }
}
