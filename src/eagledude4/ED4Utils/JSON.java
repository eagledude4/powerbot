package eagledude4.ED4Utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class JSON extends ClientAccessor {
    public JSON(ClientContext ctx) {
    	super(ctx);
    }
    
    @SuppressWarnings("unchecked")
	public void writeFile(String fileName, File storageDirectory, String objects[][]) {
    	JSONObject obj = new JSONObject();
    	
    	for (int x = 0; x < objects.length; x ++) {
    	    String subArray[] = objects[x]; 
    	    //System.out.println( "Length of array " + x + " is " + subArray.length );
    	    
    	    obj.put(subArray[0], subArray[1]);
    	    
    	    /*for (int y = 0; y < subArray.length; y ++) {
    	    	 String item = subArray[y];
    	    	 System.out.println( "  Item " +y + " is " + item );
    	    }*/
    	}
        try (FileWriter file = new FileWriter(storageDirectory+fileName)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //String string = obj.toJSONString();
        //System.out.print(string);
    }
    
    public String getJSONObj(String fileName, String objString, File storageDirectory) {
    	JSONParser parser = new JSONParser();
    	String returnedObj = "";
        try {
            Object obj = parser.parse(new FileReader(storageDirectory+fileName));

            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);

            returnedObj = (String) jsonObject.get(objString);
            return returnedObj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}