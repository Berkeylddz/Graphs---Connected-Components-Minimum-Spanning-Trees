import java.util.*;

// Class representing the Mission Synthesis
public class MissionSynthesis {

    // Private fields
    private final List<MolecularStructure> humanStructures; // Molecular structures for humans
    private final ArrayList<MolecularStructure> diffStructures; // Anomalies in Vitales structures compared to humans

    // Constructor
    public MissionSynthesis(List<MolecularStructure> humanStructures, ArrayList<MolecularStructure> diffStructures) {
        this.humanStructures = humanStructures;
        this.diffStructures = diffStructures;
    }

    // Method to synthesize bonds for the serum
    public List<Bond> synthesizeSerum() {
        List<Bond> serum = new ArrayList<>();

        // Lists to store selected molecules from typical humans and Vitales
        List<Molecule> humanMolecules = new ArrayList<>();
        List<Molecule> vitalesMolecules = new ArrayList<>();

        // Select molecules with the lowest bond strength from each structure
        for (MolecularStructure structure : humanStructures) {
            Molecule selectedMolecule = structure.getMoleculeWithWeakestBondStrength();
            humanMolecules.add(selectedMolecule);
        }

        for (MolecularStructure structure : diffStructures) {
            Molecule selectedMolecule = structure.getMoleculeWithWeakestBondStrength();
            vitalesMolecules.add(selectedMolecule);
        }

        List<Molecule> allMolecules = new ArrayList<>();
        allMolecules.addAll(humanMolecules);
        allMolecules.addAll(vitalesMolecules);

        Molecule weakestMolecule = findWeakestMolecule(allMolecules);
        allMolecules.remove(weakestMolecule);

        while (!allMolecules.isEmpty()){
            Molecule molecule = allMolecules.get(0);

            addBondsBetweenMolecules(serum, weakestMolecule, molecule);

            allMolecules.remove(molecule);
        }




        return  serum;
    }

    private Molecule findWeakestMolecule(List<Molecule> molecules) {
        Molecule weakestMolecule = null;
        double minStrength = Double.MAX_VALUE;

        for (Molecule molecule : molecules) {
            if (molecule.getBondStrength() < minStrength) {
                minStrength = molecule.getBondStrength();
                weakestMolecule = molecule;
            }
        }

        return weakestMolecule;
    }

    private void addBondsBetweenMolecules(List<Bond> serum, Molecule molecule, Molecule allMolecules) {
        Bond bond= new Bond(molecule,allMolecules,calculateBondStrength(molecule,allMolecules));
        serum.add(bond);
    }

    // İki molekül arasındaki bağın kuvvetini hesaplar (örneğin, ortalamasını alabilirsiniz)
    private double calculateBondStrength(Molecule moleculeA, Molecule moleculeB) {
        return (moleculeA.getBondStrength() + moleculeB.getBondStrength()) / 2.0;
    }

    public void printSynthesis(List<Bond> serum) {
        Collections.sort(serum, Comparator.comparingDouble(Bond::getWeight));

        double totalBondStrength = 0.0;
        System.out.println("Typical human molecules selected for synthesis: " + getMoleculeIds(humanStructures));
        System.out.println("Vitales molecules selected for synthesis: " + getMoleculeIds(diffStructures));
        System.out.println("Synthesizing the serum...");

        // Bağ kuvvetine göre sıralanmış serumu yazdır
        for (Bond bond : serum) {
            // M değerlerini küçükten büyüğe doğru sırala
            Molecule from = bond.getFrom();
            Molecule to = bond.getTo();
            if (from.getId().compareTo(to.getId()) > 0) {
                // Eğer from'un id'si to'nun id'sinden büyükse yer değiştir
                Molecule temp = from;
                from = to;
                to = temp;
            }
            System.out.printf("Forming a bond between %s - %s with strength %.2f%n",
                    from.getId(), to.getId(), bond.getWeight());
            totalBondStrength += bond.getWeight();
        }

        System.out.printf("The total serum bond strength is %.2f%n", totalBondStrength);
    }

    private List<String> getMoleculeIds(List<MolecularStructure> structures) {
        List<String> moleculeIds = new ArrayList<>();
        for (MolecularStructure structure : structures) {
            Molecule molecule = structure.getMoleculeWithWeakestBondStrength();
            moleculeIds.add(molecule.getId());
        }
        return moleculeIds;
    }
}
