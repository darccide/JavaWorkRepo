dimport java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean quit = false;
        while (!quit) {
            String [][] board = new String [7][7];
            String player1 = "Player #1";
            String player2 = "Player #2";

            welcomeMessage();

            player1 = getName(player1);
            greeting(player1);

            player2 = getName(player2);
            greeting(player2);

            gameStartPrompt();

            String firstPlayer = selectFirst(player1, player2);

            String secondPlayer = selectSecond(firstPlayer, player1, player2);

            board = startBoard(board);

            playGame(firstPlayer, secondPlayer, board);

            quit = playAgain();
        }

    }

    public static boolean playAgain() {
        String answer = "";
        while (!(answer.equalsIgnoreCase("y")) || !(answer.equalsIgnoreCase("n"))) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Play again (Y/N): ");
            answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("y")) {
                return false;
            }
            if (answer.equalsIgnoreCase("n")) {
                return true;
            }
            notValidResponse();
        }

        return true;
    }

    public static String[][] startBoard(String[][] board) {
        String[][] startBoard = board;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (i == 0) {
                    String column = String.valueOf(j + 1);
                    startBoard[i][j] = column;
                } else {
                    startBoard[i][j] = "_";
                }
            }
        }
        return startBoard;
    }

    public static void printBoard(String[][] board) {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

    public static void welcomeMessage() {
        System.out.println("WELCOME TO CONNECT FOUR!");
        System.out.println("");
    }

    public static void greeting(String player) {
        System.out.println("Hello, " + player + ".");
        System.out.println("");
    }

    public static void gameStartPrompt() {
        System.out.println("Let's play CONNECT FOUR!");
        System.out.println("(Randomizing...)");
    }

    public static String getName() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name: ");
        String inputName = scanner.nextLine();
        boolean onlyLetters = validateString(inputName);

        if (!onlyLetters || inputName.equals("")) {
            inputName = continueWithName(inputName);
        }
        return inputName;
    }

    public static String getName(String player) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(player + ", enter your name: ");
        String inputName = scanner.nextLine();
        boolean onlyLetters = validateString(inputName);

        if (!onlyLetters || inputName.equals("")) {
            inputName = continueWithName(inputName);
        }
        return inputName;
    }

    public static boolean validateString(String str) {
        str = str.toLowerCase();
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char ch = charArray[i];
            if (!(ch >= 'a' && ch <= 'z')) {
                return false;
            }
        }
        return true;
    }

    public static String continueWithName(String inputName) {
        String newName = "";

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Are you sure you want this name? Please answer yes or no (Y/N): ");
            String userAnswer = scanner.nextLine();

            if (userAnswer.equalsIgnoreCase("n")
                    || userAnswer.equalsIgnoreCase("no")) {
                newName = getName();
                break;
            }
            if (userAnswer.equalsIgnoreCase("y")
                    || userAnswer.equalsIgnoreCase("yes")) {
                newName = inputName;
                break;
            } else {
                notValidResponse();
            }
        }
        return newName;
    }

    public static void notValidResponse() {
        System.out.println("That is not a valid response!");
    }

    public static void notValidColumn() {
        System.out.println("That's not a valid column.");
    }

    public static String selectFirst(String player1, String player2) {
        int select = (int) Math.round(Math.random() * 100);
        if (select % 2 == 0) {
            return player1;
        } else {
            return player2;
        }
    }

    public static String selectSecond(String starter, String player1, String player2) {
        if (starter.equals(player1)) {
            return player2;
        } else {
            return player1;
        }
    }

    public static void playGame(String firstPlayer, String secondPlayer, String[][] board) {

        String[][] updatedBoard = board;
        int turnCounter = 0;
        boolean inProgress = true;

        playerPrompt(firstPlayer);
        printBoard(board);

        while (inProgress) {
            turnCounter++;
            inProgress = gameTurn(turnCounter, firstPlayer, updatedBoard, inProgress);
            if (inProgress == false) {
                System.out.println(firstPlayer + " won! Good game!");
                break;
            }

            turnCounter++;
            inProgress = gameTurn(turnCounter, secondPlayer, updatedBoard, inProgress);
            if (inProgress == false) {
                System.out.println(secondPlayer + " won! Good game!");
            }
        }

    }

    public static void playerPrompt(String player) {
        System.out.println("It's " + player + "'s turn.");
        System.out.println("");
    }

    public static void selectionPrompt(String player) {
        System.out.println(player + ", choose a column: ");
    }

    public static int playerSelection(String player) {
        int choice = 0;

        while (true) {
            Scanner scanner = new Scanner(System.in);
            selectionPrompt(player);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice > 0 && choice < 8) {
                    break;
                } else {
                    notValidColumn();
                }
            } catch (NumberFormatException ex) {
                notValidColumn();
            }
            System.out.println("");
        }
        return choice;
    }

    public static boolean gameTurn(int turnCounter, String player, String[][] updatedBoard, boolean inProgress) {
        String [][] newBoard = updatedBoard;

        while (true) {
            int choice = playerSelection(player);
            boolean fullColumn = checkFullColumn(newBoard, choice);

            newBoard = gameMove(choice, turnCounter, newBoard);
            if (!fullColumn) {
                break;
            }
        }
        printBoard(updatedBoard);
        return checkWin(turnCounter, inProgress, updatedBoard);
    }

    public static String [][] gameMove(int choice, int turnCounter, String[][] board) {
        int column = --choice;
        String playerPiece = "";
        playerPiece = setPiece(turnCounter, playerPiece);


        for (int i = 6; i >= 0; i--) {
            if (board[i][column].equals("_")) {
                board[i][column] = playerPiece;
                break;
            } else if ((i == 0) && (!board[i][column].equals("_"))) {
                System.out.println("This column is full. Please choose another.");
                System.out.println("");
            }
        }

        return board;
    }

    public static String setPiece(int turnCounter, String playerPiece) {
        if (turnCounter % 2 != 0) {
            playerPiece = "X";
        } else {
            playerPiece = "O";
        }
        return playerPiece;
    }

    public static boolean checkBoard(String[][] board) {
        String strX = "X";
        String strO = "O";

        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j].equals(strX)) {
                    if (board[i][j + 1].equals(strX)
                            && board[i][j + 2].equals(strX)
                            && board[i][j + 3].equals(strX)) {
                        return false;
                    }
                } else if (board[i][j].equals(strO)) {
                    if (board[i][j + 1].equals(strO)
                            && board[i][j + 2].equals(strO)
                            && board[i][j + 3].equals(strO)) {
                        return false;
                    }
                }
            }
        }

        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j].equals(strX)) {
                    if (board[i + 1][j].equals(strX)
                            && board[i + 2][j].equals(strX)
                            && board[i + 3][j].equals(strX)) {
                        return false;
                    }
                } else if (board[i][j].equals(strO)) {
                    if (board[i + 1][j].equals(strO)
                            && board[i + 2][j].equals(strO)
                            && board[i + 3][j].equals(strO)) {
                        return false;
                    }
                }
            }
        }

        for (int i = 1; i < 4; i++) {
            for (int j = 0; j <= 4; j++) {
                if (board[i][j].equals(strX)) {
                    if (board[i + 1][j + 1].equals(strX)
                            && board[i + 2][j + 2].equals(strX)
                            && board[i + 3][j + 3].equals(strX)) {
                        return false;
                    }
                } else if (board[i][j].equals(strO)) {
                    if (board[i + 1][j + 1].equals(strO)
                            && board[i + 2][j + 2].equals(strO)
                            && board[i + 3][j + 3].equals(strO)) {
                        return false;
                    }
                }
            }
        }

        for (int i = 1; i < 4; i++) {
            for (int j = 6; j >= 3; j--) {
                if (board[i][j].equals(strX)) {
                    if (board[i + 1][j - 1].equals(strX)
                            && board[i + 2][j - 2].equals(strX)
                            && board[i + 3][j - 3].equals(strX)) {
                        return false;
                    }
                } else if (board[i][j].equals(strO)) {
                    if (board[i + 1][j - 1].equals(strO)
                            && board[i + 2][j - 2].equals(strO)
                            && board[i + 3][j - 3].equals(strO)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean checkFullColumn(String[][] board, int choice) {
        int column = --choice;
        if (!(board[1][column]).equals("_")) {
            return true;
        }
        return false;
    }

    public static boolean checkWin(int turnCounter, boolean inProgress, String[][] updatedBoard) {
        if (turnCounter > 6) {
            inProgress = checkBoard(updatedBoard);
        }
        return inProgress;
    }

}
