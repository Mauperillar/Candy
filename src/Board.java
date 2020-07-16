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

    public void putPiece(int row, int colum, Object piece){
        this.board[row][colum] = piece;
    }

    public void removePiece(int row, int colum){
        this.board[row][colum] = '_';
    }

    public Object getPiece(int row, int column){
        return this.board[row][column];
    }

    public void prinBoard(){

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
}