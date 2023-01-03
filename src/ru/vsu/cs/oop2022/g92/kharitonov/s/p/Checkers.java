package ru.vsu.cs.oop2022.g92.kharitonov.s.p;

import java.util.Scanner;

public class Checkers
{
    public static final int RED = 1;
    public static final int BLUE = -1;
    public static final int EMPTY = 0;

    public static final int BOARD_SIZE = 8;
    public static final int MARK = 3;

    public static Interface grid;

    public static Scanner getPlayerFullMoveScanner = null;

    public static void main(String[] args)
    {
        grid = new Interface(BOARD_SIZE);
        Game();
    }

    public static int[][] createBoard() { // Помещаем фигуры на доску
        int[][] board = new int[BOARD_SIZE][BOARD_SIZE];

        for (int row = 0; row < BOARD_SIZE; row++)
            for (int col = 0; col < BOARD_SIZE; col++)
            {
                if (row < 3)
                {
                    if ((row % 2) + (col % 2) == 1)
                        board[row][col] = 0;
                    else
                        board[row][col] = 1;
                }
                else if (row > (BOARD_SIZE - 4))
                {
                    if ((row % 2) + (col % 2) == 1)
                        board[row][col] = 0;

                    else
                        board[row][col] = -1;
                }
                else
                    board[row][col] = 0;
            }
        return board;
    }

    public static int countFigures(int[][] board, int player) { // Количество фигур на доске
        int counter = 0;

        for (int row = 0; row < BOARD_SIZE; row++)
            for (int col = 0; col < BOARD_SIZE; col++)
                if (board[row][col] == player || board[row][col] == 2 * player)
                    counter++;

        return counter;
    }

    public static boolean isBasicMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) { //  проверка хода на 1 клетку
        boolean ans = true;

        if (( fromRow < 0|| fromRow >= BOARD_SIZE)||( fromCol < 0 || fromCol >= BOARD_SIZE)||( toRow < 0 || toRow >= BOARD_SIZE)||( toCol < 0|| toCol >= BOARD_SIZE))
            ans = false;

        else if (board[fromRow][fromCol] != player && board[fromRow][fromCol] != player * 2)
            ans = false;

        else if (board[toRow][toCol] != 0)
            ans = false;

        else if (board[fromRow][fromCol] == RED || board[fromRow][fromCol] == BLUE)
        {
            if (fromRow + player != toRow)
                ans = false;

            if (fromCol + 1 != toCol && fromCol - 1 != toCol)
                ans = false;
        }
        else
        if ((fromRow + 1 != toRow && fromRow - 1 != toRow) || (fromCol + 1 != toCol && fromCol - 1 != toCol))
            ans = false;

        return ans;
    }

    public static boolean isBasicJumpValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) { // можно ли туда перейти
        boolean ans = true;

        if (( fromRow < 0 || fromRow >= BOARD_SIZE)||( fromCol < 0 || fromCol >= BOARD_SIZE)||( toRow < 0||toRow >= BOARD_SIZE)||( toCol < 0 || toCol >= BOARD_SIZE))
            ans = false;

        else if ((board[fromRow][fromCol] != player) && (board[fromRow][fromCol] != (2 * player)))
            ans = false;

        else if (board[(toRow + fromRow) / 2][(toCol + fromCol) / 2] != (player * (-1)) && (board[(toRow + fromRow) / 2][(toCol + fromCol) / 2] != (player * (-2))))
            ans = false;

        else if (board[toRow][toCol] != 0)
            ans = false;

        else if (board[fromRow][fromCol] == RED || board[fromRow][fromCol] == BLUE)
        {
            if (((fromRow + (2 * player) != toRow) || (fromCol + 2 != toCol && fromCol - 2 != toCol)))
                ans = false;
        }
        else
        {
            if ((fromRow + 2 != toRow && fromRow - 2 != toRow) || (fromCol + 2 != toCol && fromCol - 2 != toCol))
                ans = false;
        }

        return ans;
    }

    public static int[][] getAllBasicMoves(int[][] board, int player) // получаем все базовые ходы
    {
        int[][] moves = null;
        int totalMoves = 0;
        int moveIndex = 0;

        for (int row = 0; row < BOARD_SIZE; row++)
            for (int col = 0; col < BOARD_SIZE; col++)
            {
                if (isBasicMoveValid(board, player, row, col, row + 1, col - 1))
                    totalMoves++;
                if (isBasicMoveValid(board, player, row, col, row + 1 ,col + 1))
                    totalMoves++;
                if (isBasicMoveValid(board, player, row, col, row - 1, col - 1))
                    totalMoves++;
                if (isBasicMoveValid(board, player, row, col, row - 1, col + 1))
                    totalMoves++;
            }

        moves = new int [totalMoves][4];

        if (totalMoves != 0)
        {
            for (int row = 0; row < BOARD_SIZE && moveIndex < totalMoves; row++)
                for (int col = 0; col < BOARD_SIZE && moveIndex < totalMoves; col++)
                {
                    if (isBasicMoveValid(board, player, row, col, row + 1, col - 1))
                    {
                        moves[moveIndex][0] = row;
                        moves[moveIndex][1] = col;
                        moves[moveIndex][2] = row + 1;
                        moves[moveIndex][3] = col - 1;
                        moveIndex++;
                    }
                    if (isBasicMoveValid(board, player, row, col, row + 1, col + 1))
                    {
                        moves[moveIndex][0] = row;
                        moves[moveIndex][1] = col;
                        moves[moveIndex][2] = row + 1;
                        moves[moveIndex][3] = col + 1;
                        moveIndex++;
                    }
                    if (isBasicMoveValid(board, player, row, col, row - 1, col - 1))
                    {
                        moves[moveIndex][0] = row;
                        moves[moveIndex][1] = col;
                        moves[moveIndex][2] = row - 1;
                        moves[moveIndex][3] = col - 1;
                        moveIndex++;
                    }
                    if (isBasicMoveValid(board, player, row, col, row - 1, col + 1))
                    {
                        moves[moveIndex][0] = row;
                        moves[moveIndex][1] = col;
                        moves[moveIndex][2] = row - 1;
                        moves[moveIndex][3] = col + 1;
                        moveIndex++;
                    }
                }
        }
        return moves;
    }

    public static int [][] getRestrictedBasicJumps(int[][] board, int player, int row, int col)
    {
        int[][] moves = null;
        int totalMoves = 0;
        int moveIndex = 0;

        if (isBasicJumpValid(board, player, row, col, row - 2, col - 2))
            totalMoves++;
        if (isBasicJumpValid(board, player, row, col, row - 2, col + 2))
            totalMoves++;
        if (isBasicJumpValid(board, player, row, col, row + 2, col - 2))
            totalMoves++;
        if (isBasicJumpValid(board, player, row, col, row + 2, col + 2))
            totalMoves++;

        moves = new int[totalMoves][4];
        if (totalMoves != 0)
        {
            if (isBasicJumpValid(board, player, row, col, row - 2, col - 2))
            {
                moves[moveIndex][0] = row;
                moves[moveIndex][1] = col;
                moves[moveIndex][2] = row - 2;
                moves[moveIndex][3] = col - 2;
                moveIndex++;
            }
            if (isBasicJumpValid(board, player, row, col, row - 2, col + 2))
            {
                moves[moveIndex][0] = row;
                moves[moveIndex][1] = col;
                moves[moveIndex][2] = row - 2;
                moves[moveIndex][3] = col + 2;
                moveIndex++;
            }
            if (isBasicJumpValid(board, player, row, col, row + 2, col - 2))
            {
                moves[moveIndex][0] = row;
                moves[moveIndex][1] = col;
                moves[moveIndex][2] = row + 2;
                moves[moveIndex][3] = col - 2;
                moveIndex++;
            }
            if (isBasicJumpValid(board, player, row, col, row + 2, col + 2))
            {
                moves[moveIndex][0] = row;
                moves[moveIndex][1] = col;
                moves[moveIndex][2] = row + 2;
                moves[moveIndex][3] = col + 2;
                moveIndex++;
            }
        }

        return moves;
    }

    public static int[][] getAllBasicJumps(int[][] board, int player) { //получаем все возможные базовые ходы
        int[][] moves = null;
        int[][] restrictedMoves = null;
        int totalMoves = 0;
        int moveIndex = 0;

        for (int row = 0; row < BOARD_SIZE; row++)
            for (int col = 0; col < BOARD_SIZE; col++)
            {
                if (isBasicJumpValid(board, player, row, col, row + 2, col - 2))
                    totalMoves++;
                if (isBasicJumpValid(board, player, row, col, row + 2, col + 2))
                    totalMoves++;
                if (isBasicJumpValid(board, player, row, col, row - 2, col - 2))
                    totalMoves++;
                if (isBasicJumpValid(board, player, row, col, row - 2, col + 2))
                    totalMoves++;
            }

        moves = new int[totalMoves][4];

        if(totalMoves != 0)
        {
            for (int row = 0; row < BOARD_SIZE && moveIndex < totalMoves; row++)
                for (int col = 0; col < BOARD_SIZE && moveIndex < totalMoves; col++)
                {
                    restrictedMoves = getRestrictedBasicJumps(board, player, row, col);
                    if (restrictedMoves.length >= 0)
                    {
                        for (int currentMove = 0; currentMove < restrictedMoves.length; currentMove++)
                        {
                            for (int idx = 0; idx < 4; idx++)
                            {
                                moves[moveIndex][idx] = restrictedMoves[currentMove][idx];
                            }
                            moveIndex++;
                        }
                    }
                }
        }
        return moves;
    }

    public static boolean canJump(int[][] board, int player) {
        boolean ans = false;
        int [][]allJumpsArr = getAllBasicJumps(board, player);

        if(allJumpsArr.length != 0)
            ans = true;

        return ans;
    }

    public static boolean isMoveValid(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) { // правильно ли ходим
        boolean ans = true;

        if (( fromRow < 0 || fromRow >= BOARD_SIZE) || ( fromCol < 0 || fromCol >= BOARD_SIZE)||( toRow < 0 || toRow >= BOARD_SIZE) || ( toCol < 0 || toCol >= BOARD_SIZE))
            ans = false;

        else if ((board[fromRow][fromCol] != player) && (board[fromRow][fromCol] != 2 * player))
            ans = false;

        else if (board[toRow][toCol] != 0)
            ans = false;

        else if (!isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol) && !isBasicJumpValid(board, player, fromRow, fromCol, toRow, toCol))
            ans = false;

        else if ((isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol)) && (canJump(board, player)))
            ans = false;

        return ans;
    }

    public static boolean hasValidMoves(int[][] board, int player) {
        boolean ans = false;

        int [][] basicMoves = getAllBasicMoves(board, player);

        int [][]basicJumps = getAllBasicJumps(board, player);

        if(basicMoves.length != 0 || basicJumps.length != 0)
            ans = true;

        return ans;
    }

    public static int[][] playMove(int[][] board, int player, int fromRow, int fromCol, int toRow, int toCol) {
        if ((isBasicMoveValid(board, player, fromRow, fromCol, toRow, toCol)))
        {
            if ((player == RED && toRow == (BOARD_SIZE - 1)) || (player == BLUE && toRow == 0))
            {
                board[toRow][toCol] = 2 * player;
            }
            else
            {
                board[toRow][toCol] = board[fromRow][fromCol];
            }
            board[fromRow][fromCol] = 0;
        }
        else
        {
            if ((isBasicJumpValid(board, player, fromRow, fromCol, toRow, toCol)))
            {
                if ((player == RED && toRow == (BOARD_SIZE - 1)) || (player == BLUE && toRow == 0))
                {
                    board[toRow][toCol] = 2 * player;
                }
                else
                {
                    board[toRow][toCol] = board[fromRow][fromCol];
                }

                board[fromRow][fromCol] = 0;
                board[(toRow + fromRow) / 2][(toCol + fromCol) / 2] = 0;
            }
        }
        return board;
    }

    public static boolean gameOver(int[][] board, int player) // проверка на конец игры
    {
        boolean ans = false;

        if (!hasValidMoves(board, player))
            ans = true;

        return ans;
    }

    public static int findTheLeader(int[][] board)
    {
        int ans = 0;
        int sumScorePlayer1 = 0;
        int sumScorePlayer2 = 0;

        for (int row = 0; row < BOARD_SIZE; row++)
            for (int col = 0; col < BOARD_SIZE; col++)
            {
                if (board[row][col] == RED)
                    sumScorePlayer1++;
                if (board[row][col] == 2 * RED)
                    sumScorePlayer1+= 2;
                if (board[row][col] == BLUE)
                    sumScorePlayer2++;
                if (board[row][col] == 2 * BLUE)
                    sumScorePlayer2+= 2;
            }

        if(sumScorePlayer1 == sumScorePlayer2)
            ans = 0;
        else if(sumScorePlayer1 > sumScorePlayer2)
            ans = RED;
        else
            ans = BLUE;

        return ans;
    }

    public static void Game()
    {
        int[][] board = createBoard();
        showBoard(board);
        grid.enableBoard();

        boolean oppGameOver = false;
        while (!gameOver(board, RED) & !oppGameOver)
        {
            board = getPlayerFullMove(board, RED);
            oppGameOver = gameOver(board, BLUE);
            if (!oppGameOver)
            {
                board = getPlayerFullMove(board, BLUE);
            }
        }
        int winner = 0;
        if (countFigures(board, RED) == 0 | countFigures(board, BLUE) == 0)
            winner = findTheLeader(board);


        if (winner == RED)
            System.out.println("\t * Победил красный игрок *");
        else if (winner == BLUE)
            System.out.println("\t * Победил  синий игрок *");

        grid.disableBoard();
    }


    public static int[][] getPlayerFullMove(int[][] board, int player) { // Получаем весь ход от игрока и проверяем правильный ли он
        int fromRow = -1, fromCol = -1, toRow = -1, toCol = -1;
        boolean jumpingMove = canJump(board, player);
        boolean notValidMove = true;
        getPlayerFullMoveScanner = new Scanner(System.in);
        while (notValidMove)
        {
            fromRow = getPlayerFullMoveScanner.nextInt();
            fromCol = getPlayerFullMoveScanner.nextInt();

            int[][] moves = jumpingMove ? getAllBasicJumps(board, player) : getAllBasicMoves(board, player);
            markPossibleMoves(board, moves, fromRow, fromCol, MARK);
            toRow = getPlayerFullMoveScanner.nextInt();
            toCol = getPlayerFullMoveScanner.nextInt();
            markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

            notValidMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol);
            if (notValidMove)
                System.out.println("\nНеправильный ход");
        }

        board = playMove(board, player, fromRow, fromCol, toRow, toCol);
        showBoard(board);

        // мульти ходы
        if (jumpingMove)
        {
            boolean longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
            while (longMove)
            {
                fromRow = toRow;
                fromCol = toCol;

                int[][] moves = getRestrictedBasicJumps(board, player, fromRow, fromCol);

                boolean notValidExtraMove = true;
                while (notValidExtraMove)
                {
                    markPossibleMoves(board, moves, fromRow, fromCol, MARK);
                    toRow = getPlayerFullMoveScanner.nextInt();
                    toCol = getPlayerFullMoveScanner.nextInt();
                    markPossibleMoves(board, moves, fromRow, fromCol, EMPTY);

                    notValidExtraMove = !isMoveValid(board, player, fromRow, fromCol, toRow, toCol);
                    if (notValidExtraMove)
                        System.out.println("\nНеправильный прыжок");
                }
                board = playMove(board, player, fromRow, fromCol, toRow, toCol);
                showBoard(board);

                longMove = (getRestrictedBasicJumps(board, player, toRow, toCol).length > 0);
            }
        }
        return board;
    }

    public static void markPossibleMoves(int[][] board, int[][] moves, int fromRow, int fromColumn, int value) {
        for (int moveIdx = 0; moveIdx < moves.length; moveIdx++)
            if (moves[moveIdx][0] == fromRow & moves[moveIdx][1] == fromColumn)
                board[moves[moveIdx][2]][moves[moveIdx][3]] = value;

        showBoard(board);
    }

    public static void showBoard(int[][] board) {
        grid.showBoard(board);
    }
}

