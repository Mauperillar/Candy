import java.util.Scanner;
public class App {
    public static void main(String[] args) throws Exception {

        boolean play = false;
        Scanner scanner = new Scanner(System.in);

        do {
            Game newGame = new Game();
            newGame.board.prinBoard();
            newGame.rewievRepeatedCandiesOnBoard();

            while (newGame.availableMovements > 0) {
                newGame.exchangeCandy();
            }

            if (newGame.points < 1000) {
                newGame.lifes--;
            }

            if(newGame.lifes>0){
                play = scanner.nextBoolean();
            }else{
                play = false;
            }
            
        } while (play);

        scanner.close();
    }
}
