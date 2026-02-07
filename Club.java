public class Club implements Embeddable {
    private String name;
    private String cleanDescription;
    private float[] embedding;

    public Club(String name, String description) {
        this.name = name;
        this.cleanDescription = processDescription(name, description);
    }

    private String processDescription(String name, String desc) {
        if (desc == null || desc.trim().isEmpty()) {
            return null; // if the description is empty, return null for now
        }

        String processed = desc.trim();
        
        if (processed.startsWith("AI:")) {
            // "AI:" prefix is used for AI generated descriptions of the clubs
            // we strip the prefix to prevent unexpected bias in the embeddings
            processed = processed.substring(3).trim();
        }

        return processed;
    }

    public String getName() { return name; }
    
    public String getDesciprtion() { return cleanDescription; }

    @Override
    public String generateEmbeddingText() {
        if (cleanDescription == null)
            return null; // return the name if no description was provided, we can only use the name for embedding
        return cleanDescription; // return description, may need a rework here
    }

    @Override
    public String getTaskType() {
        return "SEMANTIC_SIMILARITY";
        // for some unknown reason SEMANTIC_SIMILARITY is outperforming RETRIEVAL_DOCUMENT, should investigate later on
    }

    @Override
    public float[] getEmbedding() { return embedding; }

    @Override
    public void setEmbedding(float[] embedding) { this.embedding = embedding; }

    @Override
    public String toString() {
        return "Club: " + name + " (Emb Size: " + (embedding != null ? embedding.length : 0) + ")";
    }
}