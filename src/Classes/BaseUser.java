// Filename: BaseUser.java
package Classes;

import java.io.Serializable;

/**
 * Base interface for user-related operations
 */
public interface BaseUser extends Serializable {
    /**
     * Get the unique identifier for the user
     * @return User identifier
     */
    String getUserId();

    /**
     * Set the user identifier
     * @param userId Unique user identifier
     */
    void setUserId(String userId);

    /**
     * Check if the user is verified
     * @return Verification status
     */
    Boolean getIsVerified();

    /**
     * Set the verification status
     * @param isVerified Verification status
     */
    void setIsVerified(Boolean isVerified);
}