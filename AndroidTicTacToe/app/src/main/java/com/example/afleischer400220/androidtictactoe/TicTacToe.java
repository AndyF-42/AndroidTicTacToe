package com.example.afleischer400220.androidtictactoe;

/*
 * Standard game of Tic Tac Toe with an AI
 * X (player) goes first, and grid is a
 * 3 by 3 grid of buttons
 * Author: Andy Fleischer
 * Date: 2/5/2020
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TicTacToe extends AppCompatActivity implements View.OnClickListener {

    Button[][] grid = new Button[3][3];
    int[][] board = new int[3][3];
    final int BLANK = 0;
    final int X_MOVE = 1;
    final int O_MOVE = 2;
    int turn = 1;
    int playerWins = 0;
    int aiWins = 0;

    //Links the grid of empty buttons to the 3x3 button grid in the game
    //and sets onClickListeners for each button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        grid[0][0] = (Button)this.findViewById(R.id.button1);
        grid[0][1] = (Button)this.findViewById(R.id.button2);
        grid[0][2] = (Button)this.findViewById(R.id.button3);
        grid[1][0] = (Button)this.findViewById(R.id.button4);
        grid[1][1] = (Button)this.findViewById(R.id.button5);
        grid[1][2] = (Button)this.findViewById(R.id.button6);
        grid[2][0] = (Button)this.findViewById(R.id.button7);
        grid[2][1] = (Button)this.findViewById(R.id.button8);
        grid[2][2] = (Button)this.findViewById(R.id.button9);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j].setOnClickListener(this);
            }
        }


    }

    //On a click, puts an X there (if valid), makes an AI move, and checks for wins
    @Override
    public void onClick(View v) {
        Button b = (Button)v;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (b.equals(grid[i][j])) {
                    if (board[i][j] == BLANK) {
                        b.setText("X");
                        b.setEnabled(false);
                        board[i][j] = X_MOVE;
                        turn = O_MOVE;
                    }
                    checkGameEnd();

                    //If the game hasn't ended already, make the AI move instantly
                    if (turn == O_MOVE) {
                        Move aiChoice = findBestMove(board);
                        board[aiChoice.row][aiChoice.col] = O_MOVE;
                        grid[aiChoice.row][aiChoice.col].setText("O");
                        grid[aiChoice.row][aiChoice.col].setEnabled(false);
                        turn = X_MOVE;
                        checkGameEnd();
                    }
                }
            }
        }
    }

    //Check for win on either side or tie
    public void checkGameEnd() {
        if (checkWin(X_MOVE, board)) {
            clearBoard();
            playerWins++;
            Toast.makeText(this, "Player wins! Player: " + playerWins + ", Computer: " + aiWins, Toast.LENGTH_SHORT).show();
        } else if (checkWin(O_MOVE, board)) {
            clearBoard();
            aiWins++;
            Toast.makeText(this, "Computer wins! Player: " + playerWins + ", Computer: " + aiWins, Toast.LENGTH_SHORT).show();
        } else if (checkTie(board)) {
            clearBoard();
            Toast.makeText(this, "It's a tie! Player: " + playerWins + ", Computer: " + aiWins, Toast.LENGTH_SHORT).show();
        }
    }

    // Checks for a win for a specific player, returns boolean true or false
    public boolean checkWin(int player, int[][] board) {
        if (board[0][0] == player && board[0][1] == player && board[0][2] == player) {
            return true;
        } else if (board[1][0] == player && board[1][1] == player && board[1][2] == player) {
            return true;
        } else if (board[2][0] == player && board[2][1] == player && board[2][2] == player) {
            return true;
        } else if (board[0][0] == player && board[1][0] == player && board[2][0] == player) {
            return true;
        } else if (board[0][1] == player && board[1][1] == player && board[2][1] == player) {
            return true;
        } else if (board[0][2] == player && board[1][2] == player && board[2][2] == player) {
            return true;
        } else if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        } else if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    // Checks for tie, returns boolean true or false
    public boolean checkTie(int[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == BLANK) {
                    return false;
                }
            }
        }
        return true;
    }

    //Reset int array board to zeroes, clear all button text, enable all buttons, and set turn to X
    public void clearBoard() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                board[row][col] = BLANK;
                grid[row][col].setText("");
                grid[row][col].setEnabled(true);
            }
        }
        turn = X_MOVE;
    }

    //tries all possible future moves and determines which are beneficial to the AI
    public int minimax(int[][] state, int depth, int player) {
        //O_MOVE win == GOOD for AI (10)
        //X_MOVE win == BAD for AI (-10)
        //TIE == NEUTRAL for AI (0)
        if (checkWin(O_MOVE, state)) {
            return 10;
        }
        if (checkWin(X_MOVE, state)) {
            return -10;
        }
        if (checkTie(state)) {
            return 0;
        }
        //If it is the AI's turn
        if (player == O_MOVE) {
            int best = Integer.MIN_VALUE;
            //Go through each empty space on the board, set it to O_MOVE, and evaluate the move recursively (player's turn now)
            //Best move is when the AI's moves are maximized
            for (int row = 0; row < state.length; row++) {
                for (int col = 0; col < state[0].length; col++) {
                    if (state[row][col] == BLANK) {
                        state[row][col] = O_MOVE;
                        best = Math.max(best, minimax(state, depth + 1, X_MOVE));
                        //reset board
                        state[row][col] = BLANK;
                    }
                }
            }
            return best;
        }
        //If it is the player's turn
        else {
            int best = Integer.MAX_VALUE;
            //Go through each empty space on the board, set it to X_MOVE, and evaluate the move recursively (computer's turn now)
            //Best move is when the player's opportunities are minimized
            for (int row = 0; row < state.length; row++) {
                for (int col = 0; col < state[0].length; col++) {
                    if (state[row][col] == BLANK) {
                        state[row][col] = X_MOVE;
                        best = Math.min(best, minimax(state, depth + 1, O_MOVE));
                        state[row][col] = BLANK;
                    }
                }
            }
            return best;
        }

    }

    //tries every possible move and evaluates (with minimax) the move with all future possible moves
    public Move findBestMove(int[][] state) {
        int bestVal = Integer.MIN_VALUE;
        Move bestMove = new Move();
        bestMove.row = -1;
        bestMove.col = -1;

        //For every empty space, add an O, evaluate the board, and undo the move
        //Find the most optimal space to move
        for (int row = 0; row < state.length; row++) {
            for (int col = 0; col < state[0].length; col++) {
                if (state[row][col] == BLANK) {
                    state[row][col] = O_MOVE;
                    int moveVal = minimax(state, 0, X_MOVE);
                    state[row][col] = BLANK;

                    if (moveVal > bestVal) {
                        bestVal = moveVal;
                        bestMove.row = row;
                        bestMove.col = col;
                    }
                }
            }
        }
        return bestMove;
    }
}
