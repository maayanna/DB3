import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExternalMemoryImpl extends IExternalMemory {
    private long BlockSize = 4000;
    private long rowSize = 52;
    private long MaxMemory = 20000000;
    private long M = (long) (0.7 * (MaxMemory / BlockSize));


    private Comparator<String> ComparatorString = new Comparator<String>() {
        @Override
        public int compare(String s, String t1) {
            if (s == null) {
                return 1;
            }
            if (t1 == null) {
                return -1;
            }
            return s.compareTo(t1);
        }
    };

    private void sortHelperStep2(long count, String out, ArrayList<File> AllSortedFiles, long B_R) {
        try {

            ArrayList<BufferedReader> SortedFilesReader = new ArrayList<>();
            ArrayList<String> pointers = new ArrayList<>();
            BufferedWriter ResultWriter;
            ResultWriter = new BufferedWriter(new FileWriter(out));
            for (File file : AllSortedFiles) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                SortedFilesReader.add(reader);
                pointers.add(reader.readLine());

            }
            int k = 0;
            if(B_R == 0){
                B_R = 1;
            }
            while (k < count) {
                int indexMin = pointers.indexOf(Collections.min(pointers, ComparatorString));
                ResultWriter.write(pointers.get(indexMin));
                ResultWriter.newLine();

                BufferedReader file_to_take = SortedFilesReader.get(indexMin);
                pointers.set(indexMin, file_to_take.readLine());

                k += 1;
//                if (k % (count / B_R) == 0) {
//                    ResultWriter.flush();
//                }
            }
            ResultWriter.flush();
            ResultWriter.close();
            for (BufferedReader buffer : SortedFilesReader) {
                buffer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sort(String in, String out, String tmpPath) {
        try {
            int BlockNumber = (int) Math.ceil((new File(in)).length() / (double) BlockSize);
            int countLines = 0;
            M = 20000000;
            long linePerCall = (long) ((M / BlockSize)*rowSize);
            File tmpFile = new File(tmpPath);
            ArrayList<File> tmpFileList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(in));
            ArrayList<String> main_memory = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {

                while (main_memory.size() < linePerCall && (line != null)) {
                    main_memory.add(line);
                    countLines++;
                    line = reader.readLine();
                }

                Collections.sort(main_memory);
                File tempFile = File.createTempFile("temp_file", ".txt", tmpFile);
                tempFile.deleteOnExit();

                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                for (String line_array : main_memory) {
                    writer.write(line_array);
                    writer.newLine();
                }
                tmpFileList.add(tempFile);
                writer.flush();
                writer.close();
                main_memory.clear();


            }

            sortHelperStep2(countLines, out, tmpFileList, BlockNumber);


        } catch (IOException e) {
            e.printStackTrace();

        }
    }



    @Override
    protected void join(String in1, String in2, String out, String tmpPath) {
        try {
//            sort(in1, "sorted1.txt", tmpPath);
//            sort(in2, "sorted2.txt", tmpPath);
            File file1 = new File(in1);
            FileReader file1reader = new FileReader(file1);
            BufferedReader reader1 = new BufferedReader(file1reader);
            File file2 = new File(in2);
            FileReader file2reader = new FileReader(file2);
            BufferedReader reader2 = new BufferedReader(file2reader);
            String Tr = reader1.readLine();
            String Ts = reader2.readLine();
            String Gs = Ts;
            File writerfile = new File(out);
            BufferedWriter writer = new BufferedWriter(new FileWriter(writerfile));
            String[] parts_Tr = null;
            String[] parts_Gs = null;
            if(Ts != null) {
                parts_Tr = Tr.split(" ");
            }
            if(Gs != null) {
                parts_Gs = Gs.split(" ");
            }
            file1.deleteOnExit();
            file2.deleteOnExit();
            while ((Tr != null) && (Gs != null)) {

                while ((Tr != null) && ((parts_Tr[0].compareTo(parts_Gs[0]) < 0))) {
                    Tr = reader1.readLine();
                    if (Tr != null) {
                        parts_Tr = Tr.split(" ");
                    }
                }
                while ((Gs != null) && (parts_Tr[0].compareTo(parts_Gs[0]) > 0)) {
                    Gs = reader2.readLine();
                    if (Gs != null) {
                        parts_Gs = Gs.split(" ");
                    }
                }
                while ((Tr != null) && (parts_Tr[0].compareTo(parts_Gs[0]) == 0)) {
                    Ts = Gs;
                    String[] parts_Ts = Ts.split(" ");
                    while ((Ts != null) && (parts_Ts[0].compareTo(parts_Tr[0]) == 0)) {
                        writer.write(Tr);
                        writer.write(" " + parts_Ts[1]);
                        writer.write(" " + parts_Ts[2]);
                        writer.newLine(); //not for the fist line


                        Ts = reader2.readLine();
                        if (Ts != null) {
                            parts_Ts = Ts.split(" ");
                        }
                    }
                    Tr = reader1.readLine();
                    if (Tr != null) {
                        parts_Tr = Tr.split(" ");
                    }
                }
                Gs = Ts;
                if (Gs != null) {
                    parts_Gs = Gs.split(" ");
                }

            }
            writer.flush();
            reader1.close();
            reader2.close();
//            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void select(String in, String out, String substrSelect, String tmpPath) {
//        try {
//            File fileIn = new File(in);
//            FileReader inReader = new FileReader(fileIn);
//            BufferedReader reader = new BufferedReader(inReader);
//            File fileOut = new File(out);
//            FileWriter outWriter = new FileWriter(fileOut);
//            BufferedWriter writer = new BufferedWriter(outWriter);
//
//            String line = reader.readLine();
//
//            while (line != null) {
//                if (line.contains(substrSelect)) {
//
//                    writer.write(line);
//                    writer.newLine();
//                }
//                line = reader.readLine();
//            }
//            writer.flush();
//            writer.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
//            int BlockNumber = (int) Math.ceil((new File(in)).length() / (double) BlockSize);
            int countLines = 0;
            M = 20000000;
            long linePerCall = (long) ((M / BlockSize)*rowSize);
            File tmpFile = new File(tmpPath);
            ArrayList<File> tmpFileList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(in));
            ArrayList<String> main_memory = new ArrayList<>();
            String line = reader.readLine();
            countLines = 1;
            ArrayList<String> allLines = new ArrayList<String>();
            while (line != null) {

                while (allLines.size() < linePerCall && (line != null)) {
                    allLines.add(line);
                    if (line.contains(substrSelect)) {
                        main_memory.add(line);

                    }
                    line = reader.readLine();
                    countLines++;
                }
                countLines = 1;
                File tempFile = File.createTempFile("temp_file", ".txt", tmpFile);
                tempFile.deleteOnExit();

                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                for (String line_array : main_memory) {
                    writer.write(line_array);
                    writer.newLine();
                }
                tmpFileList.add(tempFile);
                writer.flush();
                writer.close();
                main_memory.clear();
                allLines.clear();
            }
            reader.close();
            File fileOut = new File(out);
            FileWriter outWriter = new FileWriter(fileOut);
            BufferedWriter writer = new BufferedWriter(outWriter);
            for(File file: tmpFileList){
                FileReader inReader = new FileReader(file);
                BufferedReader readTmp = new BufferedReader(inReader);
                String lineTmp = readTmp.readLine();
                while(lineTmp != null)
                {
                    writer.write(lineTmp);
                    writer.newLine();
                    lineTmp = readTmp.readLine();
                }
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void joinAndSelecteff_Helper(String in, String out, String substrSelect, String tmpPath) {
        try {
            int BlockNumber = (int) Math.ceil((new File(in)).length() / (double) BlockSize);
            int countLines = 0;
            M = 20000000;
            long linePerCall = (long) ((M / BlockSize)*rowSize);
            File tmpFile = new File(tmpPath);
            ArrayList<File> tmpFileList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(in));
            ArrayList<String> main_memory = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {

                while (main_memory.size() < linePerCall && (line != null)) {
                    if(line.contains(substrSelect)) {
                        main_memory.add(line);
                        countLines++;
                    }
                    line = reader.readLine();
                }

                Collections.sort(main_memory);
                File tempFile = File.createTempFile("temp_file", ".txt", tmpFile);
                tempFile.deleteOnExit();

                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                for (String line_array : main_memory) {
                    writer.write(line_array);
                    writer.newLine();
                }
                tmpFileList.add(tempFile);
                writer.flush();
                writer.close();
                main_memory.clear();
            }
            sortHelperStep2(countLines, out, tmpFileList, BlockNumber);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void joinAndSelectEfficiently(String in1, String in2, String out,
                                         String substrSelect, String tmpPath) {
        joinAndSelecteff_Helper(in1, "sorted1.txt", substrSelect, tmpPath);
        joinAndSelecteff_Helper(in2, "sorted2.txt", substrSelect, tmpPath);
        join("sorted1.txt", "sorted2.txt", out, tmpPath);
    }


}