public class Player {
    private String name;
    private int lifes;
    private int currentLevel;

    public Player(String name) {
        this.setName(name);
        this.setLifes(5);
        this.setCurrentLevel(1);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLifes() {
        return lifes;
    }

    public void setLifes(int lifes) {
        this.lifes = lifes;
    }

    public void decreaseLife(int quantity) {
        this.lifes -= quantity;
    }

    public void levelUp() {
        this.setCurrentLevel(this.getCurrentLevel() + 1);
    }
}