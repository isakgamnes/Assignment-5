
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class App
{
    private JSONArray HTTPpost = new JSONArray();
    private final String HOST = "datakomm.work";
    private final int PORT  = 80;

    private int SessionID;

    private HTTPPOST post = new HTTPPOST(HOST, PORT);
    private GET get = new GET(HOST, PORT);


    public static void main (String args[])
    {
        App runApp = new App();
        runApp.start();
    }


    public App()
    {
    }

    private void start()
    {
        authorizeAndGetSessionID();
    }

    private void authorizeAndGetSessionID()
    {
        JSONObject object = new JSONObject();
        object.put("email", "isakgs@stud.ntnu.no");
        object.put("phone", "95210495");
        System.out.println(object.toString());

        JSONObject object2 = post.sendPost("dkrest/auth", object);

        if(object2 != null)
        {
            SessionID = object2.getInt("sessionId");
            System.out.println(object2.getString("comment"));
        }
        else
        {
            System.out.println("Failed to authorize...");
        }

    }


    private void doTask1()
    {

    }
}
