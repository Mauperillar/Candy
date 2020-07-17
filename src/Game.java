import java.util.Scanner;

public class Game {
    Board board = new Board();
    Candies candies = new Candies();
    int availableMovements = 50;
    int points = 0;
    int lifes = 5;

    Game() {
        this.fillAllBoard();
    }

    private final void fillAllBoard() {
        for (int row = 0; row < this.board.getWidth(); row++) {
            for (int column = 0; column < this.board.getHeight(); column++) {
                this.board.putPiece(row, column, new Candies().getSweet());
            }
        }
    }

    private void printGame() {
        System.out.println("Vidas: " + this.lifes);
        System.out.println("Puntos: " + this.points);
        System.out.println("Movimientos restantes: " + this.availableMovements + "\n");
        this.board.prinBoard();
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

    public boolean rewievRepeatedCandiesOnBoard() {
        boolean hasTheBoardChanged;
        boolean haveRepeatedCandies = false;
        int[] intervalRepeat = new int[2];
        Character[] axes = {'x', 'y'};

        do {
            hasTheBoardChanged = false;

            for(Character axis: axes){
                int heigth;
                if(axis.equals('x')){
                    heigth = this.board.getWidth();
                }else{
                    heigth = this.board.getHeight();
                }
    
                for(int indexAxis=heigth-1; indexAxis>=0; indexAxis--){
                    intervalRepeat = null;
                    
                    if(axis.equals('x')){
                        intervalRepeat = this.intervalCandiesRepeated(this.board.getRow(indexAxis));
                    }else{
                        intervalRepeat = this.intervalCandiesRepeated(this.board.getColumn(indexAxis));
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

    private int[] intervalCandiesRepeated(Object[] line) {
        int countRepeatCandy = 1;
        Object currentCandy, nextCandy;
        int[] interval = new int[2];

        for (int index = 0; index < line.length; index++) {
            currentCandy = line[index];
            nextCandy = line[index+1];
            if (nextCandy.equals(currentCandy)) {
                countRepeatCandy++;
            } else {
                if (countRepeatCandy >= 3) {
                    interval[0] = index - countRepeatCandy;
                    interval[1] = index;
                    break;
                } else {
                    countRepeatCandy = 1;
                }
            }
        }

        if (countRepeatCandy < 3) {
            return null;
        } else {
            return interval;
        }

    }

    public void removeCandyOnline(int from, int to, Character eje, int indexAxis) {
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

    public void descendCandy(int[] rangeEmpty, Character eje, int indexAxis) {
        
        if (eje.equals('x')) {
            this.descendCandyXAxis(rangeEmpty, indexAxis);
        } else if (eje.equals('y')) {
            this.descendCandyYAxis(rangeEmpty, indexAxis);
        }

        System.out.println("\n"+"################################");
        System.out.println("/// Bajando nuevos dulces ///"+"\n");
        this.printGame();
    }

    private void descendCandyXAxis(int[] rangeEmpty, int indexRow){
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

    private void descendCandyYAxis(int[] rangeEmpty, int indexColumn){
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

    public void exchangeCandy() {
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
        Scanner scanner = new Scanner(System.in);

        Character currentAxis = 'x';

        for (int indexAxis = 0; indexAxis <= 1; indexAxis++) {

            do {
                System.out.print("    Posición " + currentAxis + ": ");
                coordinate[indexAxis] = scanner.nextInt();
                isValidPosition = this.board.isValidPositionPiece(coordinate[indexAxis], currentAxis);

                if (!isValidPosition) {
                    System.out.println("/!/  Posición no valida vuelva a ingresarla\n");
                }

            } while (!isValidPosition);
            // Change axis
            currentAxis = 'y';
        }

        scanner.close();
        return coordinate;
    }
}