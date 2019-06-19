package JavaBonusTask1;

import java.io.File;
import java.util.Arrays;
import java.util.Stack;
import java.util.Comparator;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import static org.apache.commons.io.comparator.SizeFileComparator.*;
import org.apache.commons.io.*;

public class ImplementLsCommand {
   
	boolean sortsize_opt;
	boolean verbose_opt;
	boolean help_opt;
	boolean list_all_opt;
	boolean reverse_opt;
	boolean show_hidden_opt;
	boolean file_size_opt;
	boolean time_opt;
	String pwd;
	File current_folder;
	float file_size;
	Stack<String> stack = new Stack<>();


   
   public static void main(String[] args) throws Exception {
      ImplementLsCommand main = new ImplementLsCommand();
      main.run(args);
      
   }
   
   public void run(String [] args) throws Exception{
         Options options = createOptions();
         
         if(parseOptions(options, args)){
            if(help_opt){
               printHelp(options);
               return;
            }

            
            if(pwd == null) {
            	pwd = System.getProperty("user.dir");
            	current_folder = new File(pwd);
            } else if(pwd != null) {
            	File checkDir = new File(pwd);
            	boolean valid = checkDir.exists();
            	if(!valid) {
            		System.out.println("Given directory does not exist. Please insert valid directory.");
            		System.exit(0);
            	} else {
            		current_folder = new File(pwd);
            	}
            }
           
            if(list_all_opt) {
            getListOfALLFiles();
            }else if(reverse_opt)
            getListOfFilesReverseOrder();
            else if(sortsize_opt)
            	sortByFileSize();
            else if(time_opt)
            	sortByTime();
            else
            getListOfFiles();
         }
   }
   
//   private void getListOfFilesSize(){
//      File[] files = current_folder.listFiles();
//
//       if(files!=null) { 
//           for(File f: files) {
//              file_size = f.length() / 1024;
//               if(!f.isDirectory()) {
//                  System.out.println(f.getName() + " : " + file_size + " KB");
//                   
//               }
//           }
//       }
//   }
   
   /*1. Sort by file size*/
   private void sortByFileSize() {
	   File[] files = current_folder.listFiles();
	   Arrays.sort(files, SIZE_REVERSE);
	   if(files!=null) { 
	         for(File f: files) {
	        	 String checkHidden = f.getName();
	        	 if(!checkHidden.startsWith(".")) {
	        		 if(f.isDirectory()) {
	        		 String dir = f.getName() + "/";
	        		 System.out.println(dir);
	        	 } else {
	        		 System.out.println(f.getName());
	        	 }
	         }
	       }
	   }    
   }
   
   
   /*2. List in the reverse order.*/
   private void getListOfFilesReverseOrder(){
      File[] files = current_folder.listFiles();

       if(files!=null) { 
           for(File f: files) {
        	   String checkHidden = f.getName();
        	   if(!checkHidden.startsWith(".")) {
        		   if(f.isDirectory()) {
        			   String dir = f.getName() + "/";
        			   stack.push(dir);
        		   } else
        			   stack.push(f.getName());
        	   }
           }
       }
          while(!stack.isEmpty()){
          System.out.println(stack.pop());
       }
   }
   
   /*3. List almost all files*/
   private void getListOfALLFiles(){
      File[] files = current_folder.listFiles();     
       if(files!=null) { 
         for(File f: files) {
        	 if(f.isDirectory()) {
        		 String dir = f.getName() + "/";
        		 System.out.println(dir);
        	 } else {
        		 System.out.println(f.getName());
        	 }
         }
       }
   }
   
   /*4. Basic ls option*/
   private void getListOfFiles(){
      File[] files = current_folder.listFiles();
     
       if(files!=null) { 
         for(File f: files) {
        	 String checkHidden = f.getName();
           if(!checkHidden.startsWith(".")) {
            if(f.isDirectory()) {
            	String dir = f.getName() + "/";
            	System.out.println(dir);
            }else {
            	System.out.println(f.getName());
            }
            }
         }
     }   
  }
   
   /*5. List files by modification order*/
   private void sortByTime() {
	   File[] files = current_folder.listFiles();
	   Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
	   if(files!=null) { 
	         for(File f: files) {
	        	 String checkHidden = f.getName();
	        	 if(!checkHidden.startsWith(".")) {
	        		 if(f.isDirectory()) {
	        		 String dir = f.getName() + "/";
	        		 System.out.println(dir);
	        	 } else {
	        		 System.out.println(f.getName());
	        	 }
	         }
	       }
	   }    
	   
   }
   
   
   private boolean parseOptions(Options options, String [] args){
        CommandLineParser parser = new DefaultParser();
         
         try{
            CommandLine cmd = parser.parse(options, args);
            verbose_opt = cmd.hasOption("v");
            help_opt = cmd.hasOption("h");
            reverse_opt = cmd.hasOption("r");
            sortsize_opt = cmd.hasOption("S");
            list_all_opt = cmd.hasOption("A");
            pwd = cmd.getOptionValue("p");
            time_opt = cmd.hasOption("t");
            
         } catch (Exception e) {
            printHelp(options);
            return false;
         }
         
         return true;
   }
   
   private Options createOptions(){
         Options options = new Options();
         
         options.addOption(Option.builder("h").longOpt("help")
        		.desc("Help")
               	.build());
         
         options.addOption(Option.builder().longOpt("ls")
        		.desc("Show list of files")
               	//.hasArg()
               	.argName("Show files")
               	.required()
               	.build());

         options.addOption(Option.builder().longOpt("r")
        		 .desc("Show files in reverse")
        		 //.hasArg()
        		 .argName("Show files in reverse")
                 .build());
         
         options.addOption(Option.builder().longOpt("A")
                 .desc("List files including hidden files")
                 //.hasArg()
                 .argName("Show Almost All")
                 .build());
         
         options.addOption(Option.builder().longOpt("S")
                 .desc("List files in desceding order")
                 //.hasArg()
                 .argName("Sort by Descending order")
                 .build());
         
         options.addOption(Option.builder().longOpt("t")
                 .desc("List files in modification time order")
                 //.hasArg()
                 .argName("Sort by modification time")
                 .build());
         
         options.addOption(Option.builder().longOpt("p")
        		 .desc("Sets the pwd")
        		 .hasArg()
        		 .argName("Set pwd")
        		 .build());
         
         return options;
   }
   
    private void printHelp(Options options){
         HelpFormatter formatter = new HelpFormatter();
         System.out.println("If the pwd is not set by the user, pwd will be set with default pwd which is the current project directory.");
         System.out.println("To set the path input the desired path with -p command.");
         System.out.println("*** -p option is not one of the ls options. I included it so that the user is able to set the path.");
         String header = "Show List of files under current path with different options.";
         String footer = "\nEnjoy!";
         formatter.printHelp("CLI", header, options, footer, true);
      }

}

