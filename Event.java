/**
 * Represents an event. Implements the Embeddable interface.
 */
public class Event implements Embeddable {

    private final String name;
    private final String description;
    private final String date;
    private float[] embedding;

    public Event(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.embedding = null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String generateEmbeddingText() {
        // build the embedding text
        return String.format(
            "%s: %s",
            this.name,
            this.description
        );
    }

    @Override
    public String getTaskType() {
        return "SEMANTIC_SIMILARITY";
        // for some unknown reason SEMANTIC_SIMILARITY is outperforming RETRIEVAL_DOCUMENT, should investigate later on
    }

    @Override
    public float[] getEmbedding() {
        return this.embedding;
    }

    @Override
    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    @Override
    public String toString() {
        String embeddingStatus = (embedding == null) ? "Not Calculated" : "Calculated (" + embedding.length + " dimensions)";
        return "Event: " + name + " (" + date + ")\n" +
               "\tDescription: " + description + "\n" +
               "\tEmbedding Status: " + embeddingStatus;
    }
}