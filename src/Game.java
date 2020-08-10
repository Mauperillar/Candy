import java.util.Scanner;

public class Game {
    public Board board = new Board();
    private Candies candies = new Candies();
    private Player player;
    private int availableMovements = 50;
    private int points = 0;

    Scanner scanner = new Scanner(System.in);

    Game() {
        System.out.print("Escriba su nombre: ");
        this.player = new Player(this.scanner.nextLine());
        this.fillAllBoard();
        //this.startGame();
    }

    private void startGame() {
        boolean canToPlay = this.canToPlay();
        boolean wantToPlay = true;

        while (canToPlay && wantToPlay) {
            this.fillAllBoard();
            this.printGame();
            this.rewievRepeatedCandiesOnBoard();

            while (this.availableMovements > 0 && this.points < 1000) {
                if(!this.canMakeMoreMovements()){
                    this.fillAllBoard();
                }
                this.exchangeCandy();
            }

            if (this.points < 1000) {
                this.player.decreaseLife(1);
            } else {
                this.player.levelUp();
            }

            canToPlay = this.canToPlay();
            if (canToPlay) {
                wantToPlay = this.scanner.nextBoolean();
            }
        }
    }

    private boolean canToPlay() {
        boolean can = this.player.getLifes() > 0;
        if (!can) {
            System.out.println("Oh no! No tienes más vidas para jugar");
        }
        return can;
    }

    private final void fillAllBoard() {
        for (int row = 0; row < this.board.getWidth(); row++) {
            for (int column = 0; column < this.board.getHeight(); column++) {
                this.board.putPiece(row, column, new Candies().getSweet());
            }
        }
    }

    private void printGame() {
        System.out.println("Vidas: " + this.player.getLifes());
        System.out.println("Puntos: " + this.points);
        System.out.println("Movimientos restantes: " + this.availableMovements + "\n");
        this.board.printBoard();
        System.out.println();
    }

    private void sumPoints(int numberRepeatedCandys) {
        switch (numberRepeatedCandys) {
            case 3:
                this.points += 50;
                break;
            case 4:
                this.points += 100;
                break;
            case 5:
                this.points += 200;
                break;
            case 6:
                this.points += 400;
                break;
        }
    }

    private boolean rewievRepeatedCandiesOnBoard() {
        boolean haveRepeatedCandies = false;
        boolean hasTheBoardChanged;
        int[] intervalRepeat = new int[2];
        Character[] axes = { 'x', 'y' };

        do {
            hasTheBoardChanged = false;

            for (Character axis : axes) {
                int heigth;
                if (axis.equals('x')) {
                    heigth = this.board.getWidth();
                } else {
                    heigth = this.board.getHeight();
                }

                for (int indexAxis = heigth - 1; indexAxis >= 0; indexAxis--) {
                    intervalRepeat = null;

                    if (axis.equals('x')) {
                        intervalRepeat = this.intervalCandiesRepeated(this.board.getRow(indexAxis), 3);
                    } else {
                        intervalRepeat = this.intervalCandiesRepeated(this.board.getColumn(indexAxis), 3);
                    }

                    if (intervalRepeat != null) {
                        int numberRepeatedCandys = intervalRepeat[1] - intervalRepeat[0] + 1;
                        this.sumPoints(numberRepeatedCandys);

                        this.removeCandyOnline(intervalRepeat[0], intervalRepeat[1], axis, indexAxis);
                        this.descendCandy(intervalRepeat, axis, indexAxis);

                        hasTheBoardChanged = true;
                        haveRepeatedCandies = true;
                    }

                }
            }
        } while (hasTheBoardChanged);

        return haveRepeatedCandies;
    }

    private int[] intervalCandiesRepeated(Object[] line, int minRepeat){
        int[] interval = this.intervalCandiesRepeated(line, minRepeat, 0, line.length-1);
        return interval;
    }
    private int[] intervalCandiesRepeated(Object[] line, int minRepeat,int initIndex,int endIndex) {
        int countRepeatCandy = 1;
        Object currentCandy, nextCandy;
        int[] interval = new int[2];

        for (int index = initIndex; index < endIndex; index++) {
            currentCandy = line[index];
            nextCandy = line[index + 1];

            if (countRepeatCandy == 1) {
                interval[0] = index;
            }

            if (nextCandy.equals(currentCandy)) {
                countRepeatCandy++;
                interval[1] = index + 1;
            } else {
                if (countRepeatCandy >= minRepeat) {
                    break;
                } else {
                    countRepeatCandy = 1;
                }
            }
        }

        if (countRepeatCandy < minRepeat) {
            return null;
        } else {
            return interval;
        }

    }

    private void removeCandyOnline(int from, int to, Character eje, int indexAxis) {
        System.out.println("/// Removiendo dulces repetidos ///");
        System.out.println();
        for (int i = from; i <= to; i++) {
            if (eje.equals('x')) {
                this.board.removePiece(indexAxis, i);
            } else if (eje.equals('y')) {
                this.board.removePiece(i, indexAxis);
            }
        }

        this.printGame();
    }

    private void descendCandy(int[] rangeEmpty, Character eje, int indexAxis) {

        if (eje.equals('x')) {
            this.descendCandyXAxis(rangeEmpty, indexAxis);
        } else if (eje.equals('y')) {
            this.descendCandyYAxis(rangeEmpty, indexAxis);
        }

        System.out.println("\n" + "################################");
        System.out.println("/// Bajando nuevos dulces ///" + "\n");
        this.printGame();
    }

    private void descendCandyXAxis(int[] rangeEmpty, int indexRow) {
        Object previousPiece;
        for (int column = rangeEmpty[0]; column <= rangeEmpty[1]; column++) {
            for (int row = indexRow; row > 0; row--) {
                previousPiece = this.board.getPiece(row - 1, column);
                if (previousPiece.equals('_')) {
                    previousPiece = this.candies.getRandomCandy();
                }
                this.board.putPiece(row, column, previousPiece);
            }
            Object newCandy = this.candies.getRandomCandy();
            this.board.putPiece(0, column, newCandy);
        }
    }

    private void descendCandyYAxis(int[] rangeEmpty, int indexColumn) {
        Object previousPiece;
        for (int row = rangeEmpty[1]; row > 0; row--) {
            previousPiece = this.board.getPiece(row - 1, indexColumn);
            if (previousPiece.equals('_')) {
                previousPiece = this.candies.getRandomCandy();
            }
            this.board.putPiece(row, indexColumn, previousPiece);
        }
        Object newCandy = this.candies.getRandomCandy();
        this.board.putPiece(0, indexColumn, newCandy);
    }

    private void exchangeCandy() {
        int[] positionCandy1 = new int[2];
        int[] positionCandy2 = new int[2];
        boolean canExchange = false;
        boolean hadRepeated = false;

        do {
            for (int candy = 1; candy <= 2; candy++) {
                System.out.println("\nIngrese posición Dulce " + candy);

                if (candy == 1) {
                    positionCandy1 = this.getCandyPositionByUser();
                } else if (candy == 2) {
                    positionCandy2 = this.getCandyPositionByUser();
                }
            }
            canExchange = this.board.T_linedPiecesTogether(positionCandy1, positionCandy2);

            if (!canExchange) {
                System.out.println("/!/  Posiciónes no validas pata intercambiar dulces, vuelva a ingresarlas\n");
            }
        } while (!canExchange);

        this.board.changePositionBetweenPieces(positionCandy1, positionCandy2);

        hadRepeated = this.rewievRepeatedCandiesOnBoard();

        if (!hadRepeated) {
            this.board.changePositionBetweenPieces(positionCandy1, positionCandy2);

            System.out.println(
                    "/!/  No se puede intercambiar estos dulces, intente con otros para crea una fila con más de 3 dulces seguidos\n");
            this.printGame();
        }

        this.availableMovements--;

    }

    private int[] getCandyPositionByUser() {
        int[] coordinate = new int[2];
        boolean isValidPosition = false;

        Character currentAxis = 'x';

        for (int indexAxis = 0; indexAxis <= 1; indexAxis++) {

            do {
                System.out.print("    Posición " + currentAxis + ": ");
                coordinate[indexAxis] = this.scanner.nextInt();
                isValidPosition = this.board.isValidPositionPiece(coordinate[indexAxis], currentAxis);

                if (!isValidPosition) {
                    System.out.println("/!/  Posición no valida vuelva a ingresarla\n");
                }

            } while (!isValidPosition);
            // Change axis
            currentAxis = 'y';
        }

        return coordinate;
    }

    private boolean canMakeMoreMovements() {
        int[] intervalRepeat = new int[2];
        Character[] axes = { 'x', 'y' };

        for (Character axis : axes) {
            int heigth;
            if (axis.equals('x')) {
                heigth = this.board.getWidth();
            } else {
                heigth = this.board.getHeight();
            }

            Object[] lineToCheck = new Object[heigth];

            for (int indexAxis = heigth - 1; indexAxis >= 0; indexAxis--) {
                intervalRepeat = null;

                if (axis.equals('x')) {
                    lineToCheck = this.board.getRow(indexAxis);
                } else {
                    lineToCheck = this.board.getColumn(indexAxis);
                }

                intervalRepeat = this.intervalCandiesRepeated(lineToCheck, 2);
                int initIndex = 0;
                int endIndex = heigth-1;
                while(intervalRepeat!=null){
                    //Verificar moviendo dulces horizontalmente
                    if(intervalRepeat[0]>1){
                        if (axis.equals('x')) {
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(indexAxis, intervalRepeat[0]-2);
                        } else {
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(intervalRepeat[0]-2, indexAxis);
                        }
                    }

                    if(intervalRepeat[1]<heigth-2){
                        if (axis.equals('x')) {
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(indexAxis, intervalRepeat[0]+2);
                        } else {
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(intervalRepeat[0]+2, indexAxis);
                        }
                    }
                    if(this.intervalCandiesRepeated(lineToCheck, 2, initIndex,endIndex)==null){
                        return true;
                    }

                    if(axis.equals('x')){
                        if(intervalRepeat[0]>1){
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(indexAxis, intervalRepeat[0]-2);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }

                        if(intervalRepeat[1]<heigth-2){
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(indexAxis, intervalRepeat[0]+2);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }

                        if(indexAxis>1){
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(indexAxis-1, intervalRepeat[0]);
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(indexAxis-1, intervalRepeat[1]);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }

                        if(indexAxis<this.board.getHeight()-1){
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(indexAxis+1, intervalRepeat[0]);
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(indexAxis+1, intervalRepeat[0]);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getRow(indexAxis);
                            }
                        }
                    }else  if(axis.equals('y')){
                        if(intervalRepeat[0]>1){
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(intervalRepeat[0]-2, indexAxis);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }

                        if(intervalRepeat[1]<heigth-2){
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(intervalRepeat[0]+2, indexAxis);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }
                        //Cambiar horizontalemnte la columna 'y' por dulces de la izquierda
                        if(indexAxis>1){
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(intervalRepeat[0], indexAxis-1);
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(intervalRepeat[1], indexAxis-1);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }
                        //Cambiar horizontalemnte la columna 'y' por dulces de la derecha
                        if(indexAxis<this.board.getWidth()-1){
                            lineToCheck[intervalRepeat[0]-1] = this.board.getPiece(intervalRepeat[0], indexAxis+1);
                            lineToCheck[intervalRepeat[1]+1] = this.board.getPiece(intervalRepeat[1], indexAxis+1);
                            if(this.intervalCandiesRepeated(lineToCheck, 3, initIndex,endIndex)==null){
                                return true;
                            }else{
                                lineToCheck = this.board.getColumn(indexAxis);
                            }
                        }
                    }
                
                    initIndex = intervalRepeat[1]+1;
                }
            }
        }
        return false;
    }

    public Board getBoard(){
        return this.board;
    }
}