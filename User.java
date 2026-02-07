import java.util.List;

/*
    a class that represents a user
    implements the embeddable interface so it can be used in the reccommendation engine 
 */
public class User implements Embeddable {

    private List<String> interests;
    private String grade;
    private String major;
    private float[] embedding;
    
    private int searchReturnType = 1; 
    private int searchCriteria = 2;

    public User(List<String> interests, String grade, String major) {
        this.interests = interests;
        this.grade = grade;
        this.major = major;
        this.embedding = null;
    }

    
    public List<String> getInterests() { return interests; }
    public String getGrade() { return grade; }
    public String getMajor() { return major; }

    @Override
    public float[] getEmbedding() { return embedding; }

    
    public void setInterests(List<String> interests) {
        this.interests = interests;
        this.invalidateEmbedding();
    }

    public void setGrade(String grade) {
        this.grade = grade;
        this.invalidateEmbedding();
    }

    public void setMajor(String major) {
        this.major = major;
        this.invalidateEmbedding();
    }
    
    @Override
    public void setEmbedding(float[] embedding) {
        this.embedding = embedding;
    }

    private void invalidateEmbedding() {
        System.out.println("User data or search criteria modified. Invalidating current embedding.");
        this.embedding = null;
    }

    /*
        returnType  0 = search for events,
                    1 = search for clubs

        criteria    0 = based on major only, 
                    1 = based on interests only,
                    2 = based on both major and interests
     */
    public void setSearchCriteria(int returnType, int criteria) {
        
        if (returnType < 0 || returnType > 1) {
            System.err.println("Invalid returnType. keeping previous value.");
            return;
        }
        if (criteria < 0 || criteria > 2) {
            System.err.println("Invalid criteria. keeping previous value.");
            return;
        }

        if (this.searchReturnType != returnType || this.searchCriteria != criteria) {
            this.searchReturnType = returnType;
            this.searchCriteria = criteria;
            this.invalidateEmbedding();
            // if our criteria changes, the old embeddings are invalidated and need to be recalculated
        }
    }

    @Override
    public String generateEmbeddingText() {
        StringBuilder query = new StringBuilder();
        
        String targetObject = (searchReturnType == 0) ? "upcoming events" : "student clubs";
        query.append("Find ")
             .append(targetObject) // construct a suitable prompt based on return type
             .append(" ");
        
        String interestsString = String.join(", ", this.interests);

        switch (searchCriteria) {
            case 0:
                // only use major
                query.append(String.format("suitable for a %s student.", this.major));
                break;
            case 1:
                // only use the interests
                query.append(String.format("related to %s.", interestsString));
                break;
            case 2: // both major and interests will be our default
            default:
                query.append(String.format("suitable for a %s student who is interested in %s.", 
                             this.major, interestsString));
                break;
        }
        
        return query.toString();
    }

    @Override
    public String getTaskType() {
        return "RETRIEVAL_QUERY";
        // for some unknown reason RETRIEVAL_QUERY is outperforming here contrary to the rest, should investigate later on
    }

    @Override
    public String toString() {
        String embeddingStatus = (embedding == null) ? "Not Calculated" : "Calculated (" + embedding.length + " dimensions)";
        String typeStr = (searchReturnType == 0) ? "Events" : "Clubs";
        String critStr = (searchCriteria == 0) ? "Major" : (searchCriteria == 1 ? "Interests" : "Major + Interests");
        
        return "User Profile:\n" +
               "\tGrade: " + grade + "\n" +
               "\tMajor: " + major + "\n" +
               "\tInterests: " + interests + "\n" +
               "\tSearch Mode: " + typeStr + " based on " + critStr + "\n" +
               "\tEmbedding Status: " + embeddingStatus;
    }
}