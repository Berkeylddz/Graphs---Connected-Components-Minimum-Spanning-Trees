import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


// Class representing the mission of Genesis
public class MissionGenesis {

    // Private fields
    private MolecularData molecularDataHuman; // Molecular data for humans
    private MolecularData molecularDataVitales; // Molecular data for Vitales

    // Getter for human molecular data
    public MolecularData getMolecularDataHuman() {
        return molecularDataHuman;
    }

    // Getter for Vitales molecular data
    public MolecularData getMolecularDataVitales() {
        return molecularDataVitales;
    }

    // Method to read XML data from the specified filename
    // This method should populate molecularDataHuman and molecularDataVitales fields once called
    public void readXML(String filename) {

        /* YOUR CODE HERE */
        try {
            // XML dosyasını oku
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(filename));

            // Insan ve Vitales moleküler verileri için ayrı listeler oluştur
            List<Molecule> humanMolecules = new ArrayList<>();
            List<Molecule> vitalesMolecules = new ArrayList<>();

            // Insan moleküler verilerini oku
            NodeList humanList = doc.getElementsByTagName("HumanMolecularData");
            NodeList humanMoleculesNodes = ((Element) humanList.item(0)).getElementsByTagName("Molecule");
            for (int i = 0; i < humanMoleculesNodes.getLength(); i++) {
                Element moleculeElement = (Element) humanMoleculesNodes.item(i);
                String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
                List<String> bonds = new ArrayList<>();
                NodeList bondsList = moleculeElement.getElementsByTagName("MoleculeID");
                for (int j = 0; j < bondsList.getLength(); j++) {
                    bonds.add(bondsList.item(j).getTextContent());
                }
                Molecule molecule = new Molecule(id, bondStrength, bonds);
                humanMolecules.add(molecule);
            }

            // Vitales moleküler verilerini oku
            NodeList vitalesList = doc.getElementsByTagName("VitalesMolecularData");
            NodeList vitalesMoleculesNodes = ((Element) vitalesList.item(0)).getElementsByTagName("Molecule");
            for (int i = 0; i < vitalesMoleculesNodes.getLength(); i++) {
                Element moleculeElement = (Element) vitalesMoleculesNodes.item(i);
                String id = moleculeElement.getElementsByTagName("ID").item(0).getTextContent();
                int bondStrength = Integer.parseInt(moleculeElement.getElementsByTagName("BondStrength").item(0).getTextContent());
                List<String> bonds = new ArrayList<>();
                NodeList bondsList = moleculeElement.getElementsByTagName("MoleculeID");
                for (int j = 0; j < bondsList.getLength(); j++) {
                    bonds.add(bondsList.item(j).getTextContent());
                }
                Molecule molecule = new Molecule(id, bondStrength, bonds);
                vitalesMolecules.add(molecule);
            }

            molecularDataHuman = new MolecularData(humanMolecules);
            molecularDataVitales = new MolecularData(vitalesMolecules);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
