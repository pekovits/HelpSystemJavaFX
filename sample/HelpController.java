package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.*;

/**
 * Created by fykos on 31/12/15.
 */
public class HelpController {
    @FXML private TreeItem treeItem;
    @FXML private TreeView treeView = new TreeView(treeItem);
    @FXML private WebView webView;
    @FXML private Button backbutton;
    @FXML private Button printButton;
    @FXML private Button forwardButton;

    private final Image depIcon = new Image(getClass().getResourceAsStream("../Images/html_icon_small.gif"));
    private final Image printIcon = new Image(getClass().getResourceAsStream("../Images/Print.gif"));
    private final Image backIcon = new Image(getClass().getResourceAsStream("../Images/Back.gif"));
    private final Image forwardIcon = new Image(getClass().getResourceAsStream("../Images/Forward.gif"));

    private Stack st = new Stack();
    private Stack forwardStack = new Stack();

    private MapParser mapParser = new MapParser();
    private TOCParser tocParser = new TOCParser();
    private WebEngine webEngine;


    @FXML
    private void initialize() {
        backbutton.setGraphic(new ImageView(backIcon));
        forwardButton.setGraphic(new ImageView(forwardIcon));
        printButton.setGraphic(new ImageView(printIcon));

        final HashMap<String, String> secondMap = tocParser.parseForTreeitems();

        makeTree();

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                st.push(selectedItem);
                loadHTMLContent(secondMap, selectedItem);
            }

        });
    }


    /**
     * Loops through the map and tries to associate the target from the xml file
     * with the target from the .jhm file
     *
     * @param secondMap    the map with the target and the text
     * @param selectedItem the item selected in the tree
     */
    public void loadHTMLContent(Map<String, String> secondMap, TreeItem<String> selectedItem) {
        for (Map.Entry<String, String> entry : secondMap.entrySet()) {
            String key = entry.getValue();

            if (selectedItem.getValue().equals(key)) {
                if (entry.getValue() == "") {
                    System.out.println("TRUE");
                    continue;
                } else {
                    String URL = mapParser.findURL(entry.getKey());
                    final java.net.URI uri = java.nio.file.Paths.get("src/" + URL).toAbsolutePath().toUri();
                    webEngine = webView.getEngine();
                    webEngine.load(uri.toString());
                }
            }
        }
    }

    @FXML
    public void handleBackButton() {
        forwardStack.push(st.pop());
        treeView.getSelectionModel().select(st.pop());
    }

    @FXML
    public void handleForward() {
        System.out.println("forwardStack: " + forwardStack);
        treeView.getSelectionModel().select(forwardStack.pop());
    }

    /**
     * This method is supposed to print the current webview
     * Not sure if it works
     */
    @FXML
    public void handlePrint(){
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            webEngine.print(job);
            job.endJob();
        }
    }


    /**
     * This function takes as parameter the linked hashmap
     * loops through to take the elements and creates the treeview
     */
    public void makeTree() {
        Iterator it = tocParser.getMap().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ArrayList<String> list = (ArrayList) pair.getValue();

            TreeItem parent = (TreeItem) treeView.getSelectionModel().getSelectedItem();
            if (parent == null) {
                parent = treeView.getRoot();
            }

            treeItem = makeBranch((String) pair.getKey(), parent);
            for (String item : list) {
                makeBranch(item, treeItem);
                treeView.setShowRoot(false);
            }
        }
    }


    /**
     * Create branches
     *
     * @param title
     * @param parent
     * @return
     */
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title, new ImageView(depIcon));
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }
}
