/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import behaviors.TriquiMinimax;
import jade.core.Agent;

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
            System.out.println(triqui.bestMove());
        }
    }

}
