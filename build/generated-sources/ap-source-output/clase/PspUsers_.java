package clase;

import java.math.BigInteger;
import java.util.Date;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-02-21T13:34:54", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(PspUsers.class)
public class PspUsers_ { 

    public static volatile SingularAttribute<PspUsers, String> password;
    public static volatile SingularAttribute<PspUsers, BigInteger> validationToken;
    public static volatile SingularAttribute<PspUsers, String> name;
    public static volatile SingularAttribute<PspUsers, Boolean> isActive;
    public static volatile SingularAttribute<PspUsers, String> email;
    public static volatile SingularAttribute<PspUsers, String> username;
    public static volatile SingularAttribute<PspUsers, Date> activationExpiryDate;

}