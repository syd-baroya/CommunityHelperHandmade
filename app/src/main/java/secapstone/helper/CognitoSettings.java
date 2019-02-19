package secapstone.helper;

import android.content.Context;

import com.amazon.identity.auth.device.api.authorization.Region;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class CognitoSettings {
    private String userPoolId = "us-east-1_uPLpBDqwz";
    private String clientId = "5a88p07a0mpgisbv4u37t5jklq";
    private String clientSecret = "f83feorpn8shvdin2a9jt11hthv32b8htjk29brob5uptlcpnpk";
    private Regions cognitoRegion = Regions.US_EAST_1;

    private String identityPoolId = "us-east-1:7a853828-64b8-48f5-880b-fde1a15fc9ce";

    private Context context;

    public CognitoSettings(Context context){
        this.context = context;
    }

    public String getUserPoolId(){
        return userPoolId;
    }

    public String getClientId(){
        return clientId;
    }

    public String getClientSecret(){
        return clientSecret;
    }

    public Regions getCognitoRegion(){
        return cognitoRegion;
    }

    public CognitoUserPool getUserPool(){
        return new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider(){
        return new CognitoCachingCredentialsProvider(context.getApplicationContext(), identityPoolId, cognitoRegion);
    }

}
