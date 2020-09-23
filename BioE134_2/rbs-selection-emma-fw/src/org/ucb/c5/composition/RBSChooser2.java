package org.ucb.c5.composition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ucb.c5.composition.model.RBSOption;
import org.ucb.c5.sequtils.Translate;
import org.ucb.c5.utils.FileUtils;

/**
 * Second generation RBSChooser algorithm
 *
 * Employs a list of genes and their associated ribosome binding sites for
 * highly-expressed proteins in E. coli.
 *
 * @author J. Christopher Anderson
 * @author Emma Wawrzynek emma-fw
 */
public class RBSChooser2 {

    private List<RBSOption> rbss;
    private HashMap<String, String> cdss;
    private Translate translation = new Translate();
    //TODO:  Fill in

    public void initiate() throws Exception {

        translation.initiate();

        //Read the data file.
        String coliData = FileUtils.readResourceFile("composition/data/coli_genes.txt"); //Read in coli_genes.txt
        String rbsData = FileUtils.readResourceFile("composition/data/rbs_options.txt"); //Read in rbs_options.txt

        //Parse coli_genes.txt to seperate out gene name and CDS.
        Pattern coliCapturer = Pattern.compile("(\\w+)\\s([a-zA-z]+)\\s(TRUE|FALSE)\\s(\\d+)\\s(\\d+)\\s([ACTG]+)\\s([ACTG]+)");
        Matcher coliMatcher = coliCapturer.matcher(coliData);

        //Populate hashmap with gene as the key and its CDS as the value.
        while (coliMatcher.find()) {
            cdss.put(coliMatcher.group(2), coliMatcher.group(6));
        }

        //Check that hashmap is created correctly.
        System.out.println(cdss.keySet());
        System.out.println(cdss.values());

        //Parse rbs_options.txt to capture gene names and their RBS.
        Pattern RBSCapturer = Pattern.compile("([a-zA-z]+)\\s(ACTG]+)");
        Matcher RBSMatcher = RBSCapturer.matcher(rbsData);

        //Populate rbss list with new RBSOption objects.
        while (RBSMatcher.find()) {
            String name = RBSMatcher.group(1);
            String rbs = RBSMatcher.group(2);
            String cds = cdss.get(name); //Get matching CDS from cdss hashmap.
            String protein = translation.run(rbs); //Translate rbs into proteins.
            RBSOption thisRBS = new RBSOption(name, null, rbs, cds, protein.substring(0, 6));
            rbss.add(thisRBS);
        }
    }

    /**
     * Provided an ORF of sequence 'cds', this computes the best ribosome
     * binding site to use from a list of options.
     * 
     * It also permits a list of options to exclude.
     *
     * @param cds The DNA sequence, ie ATGCATGAT...
     * @param ignores The list of RBS's to exclude
     * @return
     * @throws Exception
     */
    public RBSOption run(String cds, Set<RBSOption> ignores) throws Exception {
        //TODO:  Fill in
        
        return null;
    }


    public static void main(String[] args) throws Exception {
        //Create an example
        String cds = "ATGGTAAGAAAACAGTTGCAGAGAGTTGAATTATCACCATCGTTATATGACACAGCTTGGGTGGCTATGGTGCCGGAGCGTAGTTCTTCTCAA";

        //Initiate the chooser
        RBSChooser2 chooser = new RBSChooser2();
        chooser.initiate();

        //Make the first choice with an empty Set of ignores
        Set<RBSOption> ignores = new HashSet<>();
        RBSOption selected1 = chooser.run(cds, ignores);

        //Add the first selection to the list of things to ignore
        ignores.add(selected1);

        //Choose again with an ignore added
        RBSOption selected2 = chooser.run(cds, ignores);

        //Print out the two options, which should be different
        System.out.println("CDS starts with:");
        System.out.println(cds.substring(0, 18));
        System.out.println();
        System.out.println("Selected1:\n");
        System.out.println(selected1.toString());
        System.out.println();
        System.out.println("Selected2:\n");
        System.out.println(selected2.toString());
    }
}
