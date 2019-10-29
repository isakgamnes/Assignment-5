
import org.apache.commons.codec.digest.DigestUtils;
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

    private JSONArray array = null;


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
        doTask1();
        doTask2();
        doTask3();
        doTask4();
        receiveFeedback();
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
            getTask(1);
        }
        else
        {
            System.out.println("Failed to authorize...");
        }

    }

    private void getTask(int taskNumber)
    {
        JSONObject object = get.sendGet("dkrest/gettask/" + taskNumber + "?sessionId=" + SessionID);
        if(object != null)
        {
            array = (JSONArray) object.get("arguments");
        }
        else
        {
            System.out.println("Failed to receive a valid object from the server...");
        }
    }

    private void doTask1()
    {
        JSONObject object = new JSONObject();
        object.put("msg", "Hello");
        object.put("sessionId", SessionID);

        post.sendPost("dkrest/solve", object);
        getTask(2);
    }

    private void doTask2()
    {
        JSONObject object = new JSONObject();
        String messageReceived = array.getString(0);
        object.put("sessionId", SessionID);
        object.put("msg", messageReceived);

        post.sendPost("dkrest/solve", object);

        getTask(3);
    }

    private void doTask3()
    {
        JSONObject object = new JSONObject();
        int multiplier = 1;
        int answer = 1;
        int i;

        for(i = 0; i < array.length(); i++)
        {
            multiplier = array.getInt(i);
            answer = answer*multiplier;
        }

        object.put("sessionId", SessionID);
        object.put("result", answer);

        post.sendPost("dkrest/solve", object);

        getTask(4);
    }

    private void doTask4()
    {
        JSONObject object = new JSONObject();
        boolean foundPin = false;
        int i;
        int pin = 0;



            for (i = 0; i <= 9999; i++)
            {

                String str = String.format("%04d", i);

                String code = DigestUtils.md5Hex(str);

                if(code.equals(array.getString(0)))
                {

                    pin = Integer.parseInt(str);

                }
            }
        System.out.println(pin);
        object.put("sessionId", SessionID);
        object.put("pin", pin);
        post.sendPost("dkrest/solve", object);

        getTask(2016);
        }

        private void doSecretTask()
        {
            
        }

        private void receiveFeedback()
        {
            get.sendGet("dkrest/results/" + SessionID);
        }

}
