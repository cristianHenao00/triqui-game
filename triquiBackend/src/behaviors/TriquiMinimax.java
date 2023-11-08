/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase que contiene la lógica del juego
 *
 * @author Cristian Henao
 */
public class TriquiMinimax {

    private final String params;

    public TriquiMinimax(String params) {
        this.params = params;
    }

    /**
     * Método que inicia el juego
     *
     * @return String, coordenada del tablero de la proxima jugada
     */
    public String bestMove() {
        String[][] board = changeJson();
        int[] move = findBestMove(board);

        String winner = checkWinner(board);
        if (!winner.equals(" ")) {
            if (winner.equals("X")) {
                return "{\"message\": \"El ganador es 'X'\"}";
            } else if (winner.equals("O")) {
                return "{\"message\": \"El ganador es 'O'\"}";
            } else if (winner.equals("T")) {
                return "{\"message\": \"Es un empate\"}";
            }
        }
        return "{\"message\": \"Mi jugada es en la fila: " + move[0] + " columna: " + move[1] + "\"}";
    }

    /**
     * Método que busca la mejor coordenada en el tablero que maximiza el
     * resultado del jugador Computadora
     *
     * @param boardGame, tablero actual en formato String
     *
     * @return coordenada del mejor movimiento del jugador computadora
     */
    protected int[] findBestMove(String[][] boardGame) {
        int[] bestMove = {-1, -1};
        int bestValue = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (boardGame[i][j].equals(" ")) {
                    boardGame[i][j] = "O";
                    int moveValue = minimax(boardGame, 0, false, alpha, beta);
                    boardGame[i][j] = " ";

                    if (moveValue > bestValue) {
                        bestValue = moveValue;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }

                }
            }
        }

        return bestMove;
    }

    /**
     * Método que desarrolla el agoritmo minimax junto con poda alfa-beta
     *
     * @param board, tablero actual del juego
     * @param depth, numero de nodos que se deben evaluar
     * @param isMaximizingPlayer, el turno del jugador Computadora
     * @param alpha, numero alfa
     * @param beta, numero beta
     *
     * @return número de la mejor evaluación o el número del ganador
     */
    protected int minimax(String[][] board, int depth, boolean isMaximizingPlayer, int alpha, int beta) {
        String result = checkWinner(board);
        if (result.equals("O")) {
            return 1;
        } else if (result.equals("X")) {
            return -1;
        } else if (result.equals("T") || depth == 2) {
            return 0;
        }

        if (isMaximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals(" ")) {
                        board[i][j] = "O";
                        int moveValue = minimax(board, depth + 1, false, alpha, beta);
                        board[i][j] = " ";
                        bestValue = Math.max(bestValue, moveValue);
                        alpha = Math.max(alpha, bestValue);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j].equals(" ")) {
                        board[i][j] = "X";
                        int moveValue = minimax(board, depth + 1, true, alpha, beta);
                        board[i][j] = " ";
                        bestValue = Math.min(bestValue, moveValue);
                        beta = Math.min(beta, bestValue);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestValue;
        }
    }

    /**
     * Método que evalua el ganador del juego después de cada movimiento, la
     * evaluación la hace ortogonal
     *
     * @param board, tablero actual del juego
     *
     * @return el simbolo del ganador "X", "O" o "T" si es empate
     */
    protected String checkWinner(String[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][1].equals(board[i][2]) && !board[i][0].equals(" ")) {
                return board[i][0];
            }
        }

        for (int j = 0; j < 3; j++) {
            if (board[0][j].equals(board[1][j]) && board[1][j].equals(board[2][j]) && !board[0][j].equals(" ")) {
                return board[0][j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals(" ")) {
                    return " ";
                }
            }
        }

        return "T";
    }

    /**
     * Método que convierte el json que me llega por evento POST en una matriz
     * de String
     *
     * @return board, el tablero actual del juego en formato String
     */
    protected String[][] changeJson() {
        JSONObject jsonObject = new JSONObject(this.params);
        JSONArray jsonBoard = jsonObject.getJSONArray("board");
        int numRows = jsonBoard.length();
        int numCols = jsonBoard.getJSONArray(0).length();
        String[][] board = new String[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            JSONArray row = jsonBoard.getJSONArray(i);
            for (int j = 0; j < numCols; j++) {
                board[i][j] = row.getString(j);
            }
        }

        return board;
    }

    /**
     * Método para actualizar el tablero después del movimiento del jugador
     * computadora
     *
     * @param board, tablero que llega por evento POST
     * @param move, coordenada del mejor movimiento de la computadora
     *
     * @return tablero actualizado después del movimiento
     */
    protected String[][] makeMove(String[][] board, int[] move) {
        int row = move[0];
        int col = move[1];
        board[row][col] = "X";
        return board;
    }

}
