package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by fykos on 02/01/16.
 */
public class MapParser {

    public MapParser(){

    }

    /**
     * Parses the .jhm file and extracts the values of the target and url attributes
     * to store them in a map.
     * @return
     */
    public Map parseForURL(){
        HashMap<String, String> map = new HashMap<>();
        try {
            File inputFile = new File("src/lib/UncertaintyEstimationHelp.jhm");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("for map");

            NodeList nList = doc.getElementsByTagName("mapID");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    map.put(eElement.getAttribute("target"), eElement.getAttribute("url"));

//                    System.out.println(eElement.getAttribute("target") + " --> " + eElement.getAttribute("url"));
                }
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Loops through the map with the target and url attributes and
     * tries to find the target value of the text selected from the tree
     * @param target
     * @return
     */
    public String findURL(String target) {
        // the map with the target and the url
        Map<String, String> targets = parseForURL();
        String url = null;

//        System.out.println("check equality");
        for (Map.Entry<String, String> entry : targets.entrySet()) {

//            System.out.println("target: " + target + " key url: " + entry.getKey());
            if (target.equals(entry.getKey())) {
//                System.out.println("Success");
                url = entry.getValue();
            }
        }

        return url;
    }



}
