public class Candies {
    private char sweet;
    private char[] existingCandies = { 'Ώ', '€', '¥', '©', '¶', '§' };

    public Candies() {
        this.sweet = this.getRandomCandy();
    }

    public char[] getExistingCandies() {
        return existingCandies;
    }

    public char getRandomCandy() {
        final int aleatorio = (int) Math.floor(Math.random() * this.existingCandies.length);
        return this.existingCandies[aleatorio];
    }

    public void addCandies(final char[] candies) {
        this.existingCandies = (this.existingCandies.toString()+candies.toString()).toCharArray();
    }

    public char getSweet(){
        return this.sweet;
    }
}