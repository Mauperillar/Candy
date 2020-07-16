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

    public boolean reviewCandiesOnline() {
        // By row
        boolean hasChanged;
        boolean hadRepeated = false;
        int numberRepeatedCandys = 0;
        do {
            hasChanged = false;

            int[] rangeRepeat = new int[2];
            for (int row = this.board.getWidth() - 1; row >= 0; row--) {
                rangeRepeat = this.rangeCandiesRepeated(this.board.getRow(row));
                if (rangeRepeat != null) {
                    this.removeCandyOnline(rangeRepeat[0], rangeRepeat[1], 'x', row);
                    numberRepeatedCandys = rangeRepeat[1] - rangeRepeat[0] +1;
                    this.sumPoints(numberRepeatedCandys);
                    this.descendCandy(rangeRepeat, 'x', row);
                    hasChanged = true;
                    hadRepeated = true;
                    rangeRepeat = null;
                }
            }

            for (int column = this.board.getHeight() - 1; column >= 0; column--) {
                rangeRepeat = this.rangeCandiesRepeated(this.board.getColumn(column));
                if (rangeRepeat != null) {
                    this.removeCandyOnline(rangeRepeat[0], rangeRepeat[1], 'y', column);
                    numberRepeatedCandys = rangeRepeat[1] - rangeRepeat[0] +1;
                    this.sumPoints(numberRepeatedCandys);
                    this.descendCandy(rangeRepeat, 'y', column);
                    hasChanged = true;
                    hadRepeated = true;
                    rangeRepeat = null;
                }
            }

        } while (hasChanged);
        return hadRepeated;
    }

    private void sumPoints(int numberRepeatedCandys){
        switch(numberRepeatedCandys){
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

    private int[] rangeCandiesRepeated(Object[] line) {
        int countRepeatCandy = 1;
        Object previousCandy = ' ';
        Object currentCandy;
        int[] range = new int[2];

        for (int i = 0; i < line.length; i++) {
            currentCandy = line[i];
            if (previousCandy.equals(currentCandy)) {
                countRepeatCandy++;
            } else {
                if (countRepeatCandy >= 3) {
                    range[0] = i - countRepeatCandy;
                    range[1] = i - 1;
                    break;
                } else {
                    countRepeatCandy = 1;
                }
            }

            previousCandy = currentCandy;

            if (countRepeatCandy >= 3 && i == line.length - 1) {
                range[0] = i + 1 - countRepeatCandy;
                range[1] = i;
                break;
            }
        }

        if (countRepeatCandy < 3) {
            return null;
        } else {
            return range;
        }

    }

    public void removeCandyOnline(int from, int to, Character eje, int index) {
        System.out.println("Removiendo dulces repetidos");
        System.out.println();
        for (int i = from; i <= to; i++) {
            if (eje.equals('x')) {
                this.board.removePiece(index, i);
            } else if (eje.equals('y')) {
                this.board.removePiece(i, index);
            }
        }

        this.board.prinBoard();
    }

    public void descendCandy(int[] rangeEmpty, Character eje, int index) {
        Object previousPiece;
        System.out.println("Bajando nuevos dulces");
        System.out.println();
        if (eje.equals('x')) {
            for (int column = rangeEmpty[0]; column <= rangeEmpty[1]; column++) {
                for (int row = index; row > 0; row--) {
                    previousPiece = this.board.getPiece(row - 1, column);
                    if (previousPiece.equals('_')) {
                        previousPiece = this.candies.getRandomCandy();
                    }
                    this.board.putPiece(row, column, previousPiece);
                }
                Object newCandy = this.candies.getRandomCandy();
                this.board.putPiece(0, column, newCandy);
            }
        } else if (eje.equals('y')) {
            for (int row = rangeEmpty[1]; row > 0; row--) {
                previousPiece = this.board.getPiece(row - 1, index);
                if (previousPiece.equals('_')) {
                    previousPiece = this.candies.getRandomCandy();
                }
                this.board.putPiece(row, index, previousPiece);
            }
            Object newCandy = this.candies.getRandomCandy();
            this.board.putPiece(0, index, newCandy);
        }
        this.board.prinBoard();
        System.out.println("################################");
        System.out.println();
    }

    public void exchangeCandy() {
        Scanner scanner = new Scanner(System.in);
        Character eje = 'x';
        int[] positionCandy1 = new int[2];
        int[] positionCandy2 = new int[2];
        int[] coordenada = new int[2];
        boolean isValidPosition;
        boolean canExchange = false;
        boolean hadRepeated = false;

        do {

            for (int candy = 1; candy <= 2; candy++) {
                eje = 'x';
                for (int e = 1; e <= 2; e++) {
                    System.out.println("Ingrese posición Dulce " + candy);
                    do {
                        System.out.print("    Posición " + eje + ": ");
                        if (eje.equals('x')) {
                            coordenada[0] = scanner.nextInt();
                            System.out.println();
                            isValidPosition = isValidPositionCandy(coordenada[0], 'x');
                        } else {
                            coordenada[1] = scanner.nextInt();
                            isValidPosition = isValidPositionCandy(coordenada[1], 'y');
                        }

                        if(!isValidPosition){
                            System.out.println("/!/  Posición no valida vuelva a ingresarla\n");
                        }
                        
                    } while (!isValidPosition);

                    if (candy == 1) {
                        positionCandy1[0] = coordenada[0];
                        positionCandy1[1] = coordenada[1];
                    } else if (candy == 2) {
                        positionCandy2[0] = coordenada[0];
                        positionCandy2[1] = coordenada[1];
                    }
                    eje = 'y';
                }
            }

            if (Math.abs(positionCandy1[0] - positionCandy2[0]) == 1
                    || Math.abs(positionCandy1[0] - positionCandy2[0]) == 0) {
                canExchange = true;
            } else {
                canExchange = false;
            }

            if (Math.abs(positionCandy1[1] - positionCandy2[1]) == 1
                    || Math.abs(positionCandy1[1] - positionCandy2[1]) == 0) {
                canExchange = true;
            } else {
                canExchange = false;
            }

            if(!canExchange){
                System.out.println("/!/  Posiciónes no validas vuelva a ingresarlas\n");
            }
        } while (!canExchange);

        

        Object candy1 = this.board.getPiece(positionCandy1[0], positionCandy1[1]);
        Object candy2 = this.board.getPiece(positionCandy2[0], positionCandy2[1]);

        this.board.putPiece(positionCandy1[0], positionCandy1[1], candy2);
        this.board.putPiece(positionCandy2[0], positionCandy2[1], candy1);

        hadRepeated = this.reviewCandiesOnline();

        if(!hadRepeated){
            this.board.putPiece(positionCandy1[0], positionCandy1[1], candy2);
            this.board.putPiece(positionCandy2[0], positionCandy2[1], candy1);
        }

    }

    private boolean isValidPositionCandy(int position, Character eje) {
        boolean isValidPosition;
        if (eje.equals('x') && position < 0 || position >= this.board.getWidth()) {
            isValidPosition = false;
        } else if (eje.equals('y') && position < 0 || position >= this.board.getHeight()) {
            isValidPosition = false;
        } else {
            isValidPosition = true;
        }
        return isValidPosition;
    }

}