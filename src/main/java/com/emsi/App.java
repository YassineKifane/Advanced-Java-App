package com.emsi;

import com.emsi.entities.Emission;
import com.emsi.entities.Producer;
import com.emsi.service.ProducerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Cell;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void readFromTextFile(ArrayList<Emission> list) {

        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/InputData.txt"));
            Emission c = null;
            String readLine = br.readLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            int id =1;
            while(readLine != null){
                String [] emission  = readLine.split(",");
                c = new Emission();
                c.setId(id);
                c.setTitre(emission[0].trim());
                c.setDateEmission(dateFormat.parse(emission[1].trim()));
                c.setDureeEmission(Integer.valueOf(emission[2].trim()));
                c.setGenre(emission[3].trim());
                list.add(c);
                readLine = br.readLine();
                id+=1;
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static XSSFRow row;
    public static void writeInOutputFile(ArrayList<Emission> list){
        try( FileOutputStream fout = new FileOutputStream("src/main/resources/outputData.txt"))
        {
            for(Emission emission : list){
                fout.write(emission.toString().getBytes());
                fout.write('\n');

                System.out.println("emission :"+emission.toString());
            }
        }
        catch (IOException e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void CreateAndWriteInStyleSheet() throws Exception{
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Création d'un objet de type feuille Excel
        XSSFSheet spreadsheet = workbook.createSheet(" emission-info ");

        //Création d'un objet row (ligne)
        XSSFRow row;

        //Les données à inserer;
        Map< String, Object[] > emissioninfo =
                new TreeMap< String, Object[] >();
        emissioninfo.put( "1", new Object[] { "Titre", "Date Emission", "Duree Emission (min)","Genre"});
        emissioninfo.put( "2", new Object[] { "Marvel", "2022-11-20","120","Action" });
        emissioninfo.put( "3", new Object[] { "Spiderman", "2011-11-11","60","Action" });


        //parcourir les données pour les écrire dans le fichier Excel
        Set< String > keyid = emissioninfo.keySet();
        int rowid = 0;

        for (String key : keyid) {
            row = spreadsheet.createRow(rowid++);
            Object [] objectArr = emissioninfo.get(key);
            int cellid = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }

        //Ecrire les données dans un FileOutputStream
        FileOutputStream out = new FileOutputStream(new File("src/main/resources/inputDataX.xlsx"));
        workbook.write(out);
        out.close();
        System.out.println("Travail bien fait!!!");
    }

    public static void readJSONFromTxtFile() throws FileNotFoundException, IOException, ParseException {
        JSONParser parser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();
        Object obj = parser.parse(new FileReader("src/main/resources/inputDataJson.json"));
        File outputFile = new File("src/main/resources/outputData.json");
        JSONObject jsonObject = (JSONObject) obj;

        try {
        String jsonObjectList = (String)jsonObject.get("emissionList").toString();
        System.out.println(jsonObjectList+"\n");

        objectMapper.writeValue(outputFile, jsonObjectList);

        System.out.println("Data successfully read from input file and written to output file.");
        System.out.println("Success!");


        }catch (IOException e) {
            System.out.println("Error processing JSON files: " + e.getMessage());
        }



    }

    public static void readFromStyleSheet(){
        try(FileInputStream fis = new FileInputStream(new File("src/main/resources/emissionInfo.xlsx")))
        {
            XSSFWorkbook workbook1 = new XSSFWorkbook(fis);
            XSSFSheet spreadsheet1 = workbook1.getSheetAt(0);
            Iterator <Row>  rowIterator = spreadsheet1.iterator();

            while (rowIterator.hasNext()) {
                row = (XSSFRow) rowIterator.next();
                Iterator < Cell >  cellIterator = row.cellIterator();

                while ( cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    System.out.print(cell.toString()+"\t\t");

                }
                System.out.println();
            }
        }
        catch (FileNotFoundException e) {
            // TODO: handle exception
        }
        catch (IOException e) {
            // TODO: handle exception
        }
    }

    public static void main( String[] args ) throws Exception {
        ArrayList<Emission> list = new ArrayList<Emission>();
        System.out.println(list);
        System.out.println("----text----");
        readFromTextFile(list);

        System.out.println(list);

        writeInOutputFile(list);
        System.out.println("----xlsx----");

        CreateAndWriteInStyleSheet();
        readFromStyleSheet();
        System.out.println("----json----");
        readJSONFromTxtFile();



        System.out.println("----JDBC----");


        ProducerService producerService = new ProducerService();

        Producer producer = new Producer(6,"Kifane","F675788","Oujda",064);
        producerService.save(producer);

        for( Producer p :producerService.findAll())
            System.out.println(p);

    }
}
