import java.util.*;

// Class representing molecular data
public class MolecularData {

    // Private fields
    private final List<Molecule> molecules; // List of molecules

    // Constructor
    public MolecularData(List<Molecule> molecules) {
        this.molecules = molecules;
    }

    // Getter for molecules
    public List<Molecule> getMolecules() {
        return molecules;
    }



    // Method to identify molecular structures
    // Return the list of different molecular structures identified from the input data
    public List<MolecularStructure> identifyMolecularStructures() {
        List<MolecularStructure> structures = new ArrayList<>();

        // Tüm molekülleri işlemek için bir kopya oluştur
        List<Molecule> remainingMolecules = new ArrayList<>(molecules);
        List<Molecule> fullMolecule = new ArrayList<>(molecules);

        // Tüm molekülleri dolaş
        for (Molecule molecule : molecules) {
            // Eğer bu molekül zaten bir yapıda kullanıldıysa atla
            if (!remainingMolecules.contains(molecule)) {
                continue;
            }
            boolean a= false;
            for (String moleculeId : molecule.getBonds()) {
                Molecule connectedMolecule = findMoleculeById(moleculeId, fullMolecule);

                for (MolecularStructure existingStructure : structures) {
                    if(existingStructure.hasMolecule(connectedMolecule.getId())){
                        if(!existingStructure.hasMolecule(molecule.getId())){
                            existingStructure.addMolecule(molecule);
                            a = true;
                        }

                    }
                }
            }
            if(a==false){
                // Yeni bir moleküler yapı oluştur
                MolecularStructure structure = new MolecularStructure();
                structure.addMolecule(molecule); // Bu molekülü yapının içine ekle
                remainingMolecules.remove(molecule); // Kullanılan molekülü listeden çıkar

                // Bu molekülün bağlandığı diğer molekülleri kontrol et
                List<String> bonds = molecule.getBonds();


                for (String bondId : bonds) {
                    Molecule bondedMolecule = findMoleculeById(bondId, remainingMolecules);
                    if (bondedMolecule != null) {
                        structure.addMolecule(bondedMolecule); // Bağlı molekülü yapının içine ekle
                        remainingMolecules.remove(bondedMolecule); // Kullanılan bağlı molekülü listeden çıkar
                    }
                }
                structures.add(structure);
            }

        }

        for (int i = 0; i < structures.size(); i++) {
            MolecularStructure structure = structures.get(i);
            if (structure.getMolecules().size() == 2) {
                try{
                    for(String bondId : structure.getMolecules().get(1).getBonds()) {
                        Molecule bondedMolecule = findMoleculeById(bondId, fullMolecule);
                        for (MolecularStructure existingStructure : structures) {
                            if (existingStructure.hasMolecule(bondedMolecule.getId())) {
                                Molecule molecule = structure.getMolecules().get(0);
                                Molecule molecule1 = structure.getMolecules().get(1);

                                existingStructure.addMolecule(molecule);
                                existingStructure.addMolecule(molecule1);

                                structures.remove(structure);
                                i--; // Bir yapıyı listeden kaldırdık, bu yüzden indeksi azaltıyoruz
                                break;
                            }
                        }
                    }

                }catch (Exception e){
                    continue;
                }
            }

            else if (structure.getMolecules().size() == 1) {
                Molecule molecule = structure.getMolecules().get(0);
                try{
                    for (MolecularStructure otherStructure : structures) {
                        if (otherStructure != structure) {
                            for (Molecule otherMolecule : otherStructure.getMolecules()) {
                                for (String bondId : otherMolecule.getBonds()) {
                                    if (bondId.equals(molecule.getId())) {
                                        otherStructure.addMolecule(molecule);
                                        structures.remove(structure);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    continue;
                }
            }
        }
        return structures;
    }

    private Molecule findMoleculeById(String moleculeId, List<Molecule> molecules) {
        for (Molecule molecule : molecules) {
            if (molecule.getId().equals(moleculeId)) {
                return molecule;
            }
        }
        return null;
    }

    public void printMolecularStructures(List<MolecularStructure> molecularStructures, String species) {
        System.out.println(molecularStructures.size() + " molecular structures have been discovered in " + species + ".");
        for (int i = 0; i < molecularStructures.size(); i++) {
            List<Molecule> molecules = molecularStructures.get(i).getMolecules();
            // Molekülleri ID'ye göre sırala
            Collections.sort(molecules);
            System.out.println("Molecules in Molecular Structure " + (i + 1) + ": " + molecules);
        }
    }

    public static ArrayList<MolecularStructure> getVitalesAnomaly(List<MolecularStructure> targetStructures, List<MolecularStructure> sourceStructures) {
        ArrayList<MolecularStructure> anomalyList = new ArrayList<>();

        for (MolecularStructure sourceStructure : sourceStructures) {
            boolean foundInTarget = false;
            for (MolecularStructure targetStructure : targetStructures) {
                if (sourceStructure.equals(targetStructure)) {
                    foundInTarget = true;
                    break;
                }
            }
            if (!foundInTarget) {
                anomalyList.add(sourceStructure);
            }
        }

        return anomalyList;
    }

    public void printVitalesAnomaly(List<MolecularStructure> molecularStructures) {

        System.out.println("Molecular structures unique to Vitales individuals:");
        for (int i = 0; i < molecularStructures.size(); i++) {
            System.out.println(molecularStructures.get(i));
        }

    }
}
