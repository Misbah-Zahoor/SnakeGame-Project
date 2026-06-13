import java.util.ArrayList;

public class ScoreBoard {

    private ArrayList<Integer> scores;
    private static final int MAX_SCORES = 5;

    public ScoreBoard() {
        scores = new ArrayList<>();
    }

    public void addScore(int score) {
        scores.add(score);
        insertionSort();
        if (scores.size() > MAX_SCORES) {
            scores.remove(scores.size() - 1);
        }
    }

    private void insertionSort() {
        for (int i = 1; i < scores.size(); i++) {
            int key = scores.get(i);
            int j = i - 1;
            while (j >= 0 && scores.get(j) < key) {
                scores.set(j + 1, scores.get(j));
                j--;
            }
            scores.set(j + 1, key);
        }
    }

    public ArrayList<Integer> getScores() {
        return scores;
    }
}
