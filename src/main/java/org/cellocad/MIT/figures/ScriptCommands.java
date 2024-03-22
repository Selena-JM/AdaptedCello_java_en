package org.cellocad.MIT.figures;


import org.cellocad.BU.netsynth.Utilities;
import org.cellocad.MIT.dnacompiler.Util;

import java.io.File;

public class ScriptCommands {


    public ScriptCommands(String home, String output_directory, String dateID) {
//        _home = home.substring(1);
        _home = home;
        String x = System.getProperty("os.name");
        System.out.println(output_directory.substring(0,6));
        System.out.println(output_directory.substring(0,6)=="/Users");
        System.out.println(output_directory.substring(0,6).equals("/Users"));
        if (output_directory.substring(0,6).equals("/Users") && Utilities.isWindows(x)) {
        	_output_directory = "C:" + output_directory;
        }
        else {
        	_output_directory = output_directory;
        }
        _dateID = dateID;
    }
    /***********************************************************************

     Synopsis    [  ]

     Plasmid figure with blocks for CDS and pins for promoter

     ***********************************************************************/
    public void makeCirdnaPlasmidFigure( String name_cirdna_out) { //could move this to Eugene.java
        String cmd = "perl " + _home + "/resources/scripts/make_cirdna_plasmid.pl " + _output_directory + " " + _dateID + " " + name_cirdna_out;
        String command_result = Util.executeCommand(cmd);
        System.out.println("Command makeCirdnaPlasmidFigure : " + cmd);
        System.out.println(command_result);
    }



    /***********************************************************************

     Synopsis    [  ]

     Shows the wiring diagram with transfer function images as nodes.
     Black dots show the noise margin points for each, and gray dashed lines show the RPU values for each gate.

     ***********************************************************************/
    public void makeCircuitRPUFigure(String prefixA) { //could move this to Eugene.java
        //process all figures and generate correct file types (PNG, PDF) for webapp
        String cmd = "perl " + _home + "/resources/scripts/make_gnuplot_rpu.pl " + _output_directory + " " + prefixA;
        String command_result = Util.executeCommand(cmd);
        System.out.println("Command makeCircuitRPUFigure : " + cmd);
        System.out.println(command_result);
    }

    public void makeCircuitSNRFigure(String prefixA) { //could move this to Eugene.java
        //process all figures and generate correct file types (PNG, PDF) for webapp
        String cmd = "perl " + _home + "/resources/scripts/make_gnuplot_snr.pl " + _output_directory + " " + prefixA;
        String command_result = Util.executeCommand(cmd);
        System.out.println("Command makeCircuitSNRFigure : " + cmd);
        System.out.println(command_result);

    }

    /***********************************************************************

     Synopsis    [  ]

     ***********************************************************************/
    public void makeDot2Png(String dotfile) {
        String cmd = "perl " + _home + "/resources/scripts/convert_this_dot2png.pl " + _output_directory + " " + dotfile;
        System.out.println("command : " + cmd);
        String command_result = Util.executeCommand(cmd);
        System.out.println("Output:\n" + command_result);
    }

    /***********************************************************************

     Synopsis    [  ]

     ***********************************************************************/
    public void makePdf2Png(String pdf_filename) {
        String cmd = "perl " + _home + "/resources/scripts/convert_pdf2png.pl " + _output_directory + " " + pdf_filename;
        String command_result = Util.executeCommand(cmd);
        System.out.println("Command makePdf2Png : " + cmd);
        System.out.println(command_result);
    }



    public void removeEPSFiles(String directory) {

        File[] files = new File(directory).listFiles();

        for (File file : files) {
            if (file.isFile()) {

                if(file.getName().contains(".eps")) {
                    file.delete();
                }
            }
        }
    }


    public void removeGateFiles(String directory) {

        File[] files = new File(directory).listFiles();

        for (File file : files) {
            if (file.isFile()) {

                if(file.getName().contains("_gate.")) {
                    file.delete();
                }
            }
        }
    }

    public void makeZIP(String directory, String zipname) {

        org.zeroturnaround.zip.ZipUtil.pack(new File(directory), new File(directory + zipname), true);
    }


    private String _home;
    private String _output_directory;
    private String _dateID;
}
