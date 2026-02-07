import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainLoadAndSearch {

    public static void main(String[] args) {

        String apiKey = (new DotEnv()).getVariable("GEMINI_API_KEY");
        String modelName = "models/gemini-embedding-001"; 
        
        GeminiApi geminiApi = new GeminiApi();
        geminiApi.setOutputDimension(3072);
        geminiApi.setApiKey(apiKey);

        Embeddings embeddingService = new Embeddings(geminiApi, modelName);
        
        System.out.println("--- Loading Embeddings ---");
        
        List<Club> loadedClubs = (new ClubStorage()).loadClubs(); 

        if (loadedClubs.isEmpty()) {
            System.out.println("No clubs loaded. Run the previous Main class to generate the file first.");
            return;
        }

        System.out.println("\n--- Creating User Query ---");
        User user = new User(
            new ArrayList<>(
                Arrays.asList(
                    "artificial intelligence",
                    "programming competitions",
                    "chess engines",
                    "electronics and embedded systems"
                )
            ),
            "Freshman",
            "Computer Science / Computer Engineering"
        );

        user.setSearchCriteria(1, 2);
        // crafts the embedding prompt for clubs, filters by major and interests

        System.out.println(user);
        
        embeddingService.ensureEmbeddingIsCalculated(user);

        System.out.println("\n--- Processing and Exporting Clubs ---");
        List<ScoredMatch> allMatches = new ArrayList<>();

        for (Club club : loadedClubs) {
            if (club.getEmbedding() == null) continue;

            double score = VectorUtils.calculateCosineSimilarity(user.getEmbedding(), club.getEmbedding());
            allMatches.add(new ScoredMatch(club, score));
        }

        System.out.println("\n--- Ranking Clubs ---");

        Collections.sort(allMatches);

        System.out.println("\nTOP 10 RECOMMENDED CLUBS:");
        System.out.println("=========================");
        
        int limit = Math.min(10, allMatches.size());
        
        for (int i = 0; i < limit; i++) {
            ScoredMatch match = allMatches.get(i);
            System.out.printf("#%d. (Score: %.4f) %s %n", (i + 1), match.score, ((Club)(match.scoredElement)).getName());
        }
        
        if (allMatches.isEmpty()) {
            System.out.println("No matches found.");
        }
        System.out.println("=========================");
    }
}