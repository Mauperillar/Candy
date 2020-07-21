public class Board {
    private int width = 9;
    private int height = 9;
    public Object[][] board;
    
    Board(){
        this.board = new Object[this.width][this.height];
    }

    Board(int height, int width){
        this.height = height;
        this.width = width;
        this.board = new Object[this.width][this.height];
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public void changePositionBetweenPieces(int[] positionPiece1, int positionPiece2[]){
        Object candy1 = this.getPiece(positionPiece1[0], positionPiece1[1]);
        Object candy2 = this.getPiece(positionPiece2[0], positionPiece2[1]);

        this.putPiece(positionPiece1[0], positionPiece1[1], candy2);
        this.putPiece(positionPiece2[0], positionPiece2[1], candy1);
    }

    public void putPiece(int row, int colum, Object piece){
        this.board[row][colum] = piece;
    }

    public void removePiece(int row, int colum){
        this.board[row][colum] = '_';
    }

    public Object getPiece(int row, int column){
        return this.board[row][column];
    }

    public void printBoard(){

        //Imprimir encabezado
        for(int i = -1; i<this.width; i++){
            if(i==-1){
                System.out.print(" ");
            }else{
                System.out.print(" "+i);
            }
        }

        System.out.println();
        //Imprimir contenido
        for(int row=0; row<this.width; row++){
            for(int column=-1; column<this.height; column++){
                if(column==-1){
                    System.out.print(row);
                }else{
                    System.out.print(" "+this.board[row][column]);
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
    
    public Object[] getRow(int index){
        Object[] row = new Object[this.width];

        for(int i = 0; i<this.height; i++){
            row[i] = this.board[index][i];
        }
        return row;
    }

    public Object[] getColumn(int index){
        Object[] column = new Object[this.height];

        for(int i = 0; i<this.height; i++){
            column[i] = this.board[i][index];
        }
        return column;
    }

    public boolean isValidPositionPiece(int position, Character axis) {
        boolean isValidPosition;
        if (axis.equals('x') && position < 0 || position >= this.width) {
            isValidPosition = false;
        } else if (axis.equals('y') && position < 0 || position >= this.height) {
            isValidPosition = false;
        } else {
            isValidPosition = true;
        }
        return isValidPosition;
    }

    public boolean T_linedPiecesTogether(int[] positionCandy1, int[] positionCandy2){
        boolean aligned = false;

        if (Math.abs(positionCandy1[0] - positionCandy2[0]) == 1
                    && Math.abs(positionCandy1[1] - positionCandy2[1]) == 0
                    || Math.abs(positionCandy1[0] - positionCandy2[0]) == 0
                            && Math.abs(positionCandy1[1] - positionCandy2[1]) == 1) {
                aligned = true;
            } else {
                aligned = false;
            }
        return aligned;
    }

}