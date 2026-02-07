/*
    a useless class that I may remove in the future
*/

public class Embeddings {

    private final GeminiApi geminiApi;
    private final String modelName;

    public Embeddings(GeminiApi geminiApi, String modelName) {
        this.geminiApi = geminiApi;
        this.modelName = modelName;
    }

    /*
        ensure an embeddable has the embeddings calculated
    */
    public void ensureEmbeddingIsCalculated(Embeddable item) {
        if (item.getEmbedding() != null) {
            return; // if embeddings have been already calculated, skip
        }
        
        String textToEmbed = item.generateEmbeddingText();
        String taskType = item.getTaskType(); 
        
        float[] newEmbedding = geminiApi.generateEmbedding(textToEmbed, modelName, taskType);

        item.setEmbedding(newEmbedding);
        System.out.println("Success (" + taskType + "). Stored new embedding of dimension: " + newEmbedding.length);
    
    }
}