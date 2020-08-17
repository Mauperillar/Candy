package logic.candies;

import java.util.ArrayList;

public class ListCandies {
    private ArrayList<Candy> candies = new ArrayList<Candy>();

    public ListCandies() {
        String[] namesCandies = {"sausage", "lozenge", "teardrop", "pillow", "planet", "flower"};
        String[] srcIconCandies = {"/images/sausage.png", "/images/lozenge.png", "/images/teardrop.png", "/images/pillow.png", "/images/planet.png", "/images/flower.png"};
        String[] singCandies = {"§", "€", "¶", "Ώ", "©", "*"};
        String[] colorCandies = {"red", "orange", "yellow", "green", "blue", "purple"};

        for(int i = 0; i<namesCandies.length; i++){
            Candy candy = new Candy(namesCandies[i], singCandies[i], srcIconCandies[i], colorCandies[i]);
            candies.add(candy);
        }
    }

    public char[] candies() {
        return this.candies();
    }

    public Candy getRandomCandy() {
        final int aleatorio = (int) Math.floor(Math.random() * this.candies.size());
        return this.candies.get(aleatorio);
    }

    public void addCandies(Candy newCandy) {
        this.candies.add(newCandy);
    }

    public Candy getSweetByIndex(int index){
        return this.candies.get(index);
    }
}