/*
    utility class for calculating cosine similarity of two embeddables.
*/
public class VectorUtils {
    public static double calculateCosineSimilarity(float[] vecA, float[] vecB) {
        if (vecA == null || vecB == null || vecA.length != vecB.length) {
            System.out.println("Vectors must be non-null and have the same length.");
            return 0;
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vecA.length; i++) {
            dotProduct += vecA[i] * vecB[i];
            normA += Math.pow(vecA[i], 2);
            normB += Math.pow(vecB[i], 2);
        }

        if (normA == 0 || normB == 0) {
            return 0.0; 
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}