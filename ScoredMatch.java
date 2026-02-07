
public class ScoredMatch implements Comparable<ScoredMatch> {
    // implement a comparable so the sorting is easier
    Embeddable scoredElement;
    double score;

    ScoredMatch(Embeddable scoredElement, double score) {
        this.scoredElement = scoredElement;
        this.score = score;
    }

    @Override
    public int compareTo(ScoredMatch other) {
        return Double.compare(other.score, this.score);
    }
}