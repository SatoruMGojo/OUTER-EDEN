//esta parte fue un dolor de huevos, nada queria jalar
//no encontraba las librerias correctas pq algunas ya no se usaban, y otra es que esta esta basada en koitlin, entonces mejor hago las peticiones manuales por http usando okhttp
//aparte todos me querian cobrar alv

package src.ai;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class GenerarBiografia {

    private static final String TOGETHER_API_KEY = "4f65a23335afcefdd98dd3974dd3fcf20b1f6923d09dafa47435f3f2aff341b2";
    //se que en una app real no deberias dejar la key aqui, pero como solo es algo interno pues la deje para esta ¨demo¨.
    private static final String API_URL = "https://api.together.xyz/v1/chat/completions";
    private static final String MODEL = "meta-llama/Llama-3-8b-chat-hf";  //

    public static String generarBiografia(String nombreUsuario) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Prompt que se enviará al modelo
        String prompt = "Escribe una biografía divertida, creativa y amigable para un perfil llamado '" + nombreUsuario + "' limitate a solo 2 parrafos, por favor.";

        // Estructura JSON para la API de Together.
        JSONArray messages = new JSONArray();
        JSONObject mensajeUsuario = new JSONObject();
        mensajeUsuario.put("role", "user");
        mensajeUsuario.put("content", prompt);
        messages.put(mensajeUsuario);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", MODEL);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 500);

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + TOGETHER_API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();

            if (!response.isSuccessful()) {
                System.err.println("Error en la respuesta: " + response.code() + " - " + response.message());
                System.err.println("Cuerpo: " + responseBody);
                throw new IOException("Error en la respuesta");
            }

            // Parseo del JSON
            JSONObject result = new JSONObject(responseBody);
            String respuesta = result
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            return respuesta.trim();
        }
    }
/* esto solo era para probarlo
    public static void main(String[] args) {
        try {
            String bio = generarBiografia("soloIA");
            System.out.println("📜 Biografía generada:\n" + bio);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

 */
}
