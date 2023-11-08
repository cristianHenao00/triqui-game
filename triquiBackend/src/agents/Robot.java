/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import models.TriquiMinimax;
import jade.core.Agent;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author Cristian Henao
 */
public class Robot extends Agent {

    @Override
    public void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            String params = (String) args[0];
            TriquiMinimax triqui = new TriquiMinimax(params);
            String message = triqui.bestMove();
            try {
                sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendMessage(String message) throws Exception {
        HttpPost post = new HttpPost("http://localhost:5000/voice");
        post.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(message);
        post.setEntity(entity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(post);

    }

}
