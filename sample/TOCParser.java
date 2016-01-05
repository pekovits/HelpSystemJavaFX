package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.awt.image.ImageWatched;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by fykos on 03/01/16.
 */
public class TOCParser {
    // this map stores the text and the respective target
    private HashMap<String, String> map = new HashMap<>();

    // using linked hashmap because it keeps the insertion order of elements
    // it stores the text and the text of the child nodes
    private LinkedHashMap<String, ArrayList<String>> lnmap = new LinkedHashMap<>();

    public TOCParser(){

    }

    public LinkedHashMap getMap(){
        return this.lnmap;
    }

    public void setMap(LinkedHashMap lnmap){
        this.lnmap = lnmap;
    }

    /**
     * This method will parse the xml file with the table of contents item and
     * it will create the treeview for the help menu.
     *
     * @return
     */
    public HashMap parseForTreeitems(){
        try {
            File inputFile = new File("src/lib/UncertaintyEstimationTOC.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("tocitemext");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                Element eElement = (Element) nNode;

                // getting the childs of the external node
                NodeList tocList = nNode.getChildNodes();

                // to store the child nodes
                ArrayList<String> ls = new ArrayList<>();

                map.put(eElement.getAttribute("target"), eElement.getAttribute("text"));

                // looping through the nodes to get the text and the target
                for (int i = 0; i < tocList.getLength(); i++) {
                    Node node = tocList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element elem = (Element) node;
                        ls.add(elem.getAttribute("text"));
                        map.put(elem.getAttribute("target"), elem.getAttribute("text"));

//                        System.out.println(elem.getAttribute("target") + "--> Text: " + elem.getAttribute("text"));
                    }
                }



                // store the text and its child nodes
                lnmap.put(eElement.getAttribute("text"), ls);
            }

            this.setMap(lnmap);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return map;
    }

}
